package org.example.jackson_generic_instrospector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class TypeRegister {
  protected final Map<Class<?>, TypeDefinition<?>> typeDefinitions;

  public TypeRegister() {
    this.typeDefinitions = new HashMap<>();
  }

  public TypeDefinition<?> addType(Class<?> type) {
    var def = this.typeDefinitions.put(type, new TypeDefinition<>(type));

    return def;
  }

  public void addSubType(Class<?> type, Class<?> subType) {
    var typeDef = getType(type);
    if (Objects.isNull(typeDef)) {
      typeDef = addType(type);
    }

    typeDef.addSubType(subType);
  }

  public TypeDefinition<?> getType(Class<?> type) {
    return this.typeDefinitions.get(type);
  }

  public Boolean hasType(Class<?> type) {
    return this.typeDefinitions.containsKey(type);
  }

  public TypeDefinition<?> findTypeBySubType(Class<?> subType) {
    return this.typeDefinitions.values().stream()
        .filter(
            genericTypeDefinition -> genericTypeDefinition.hasSubType(subType))
        .findFirst()
        .orElse(null);
  }

  public TypeDefinition<?> findByTypeOrSubType(Class<?> type) {
    if (hasType(type)) {
      return getType(type);
    }

    return findTypeBySubType(type);
  }

  public static TypeRegister fromIterable(Iterable<? extends Class<?>> types) {
    var register = new TypeRegister();

    typeLoop:
    for (var type : types) {
      if (type.isAnnotationPresent(GenericType.class)) {
        register.addType(type);
      } else {
        var klass = type.getSuperclass();
        while (klass != Object.class && !Objects.isNull(klass)) {
          if (klass.isAnnotationPresent(GenericType.class)) {
            register.addSubType(klass, type);

            continue typeLoop;
          }

          klass = klass.getSuperclass();
        }

        for (var inter : type.getInterfaces()) {
          klass = inter;
          while (klass != Object.class && !Objects.isNull(klass)) {
            if (klass.isAnnotationPresent(GenericType.class)) {
              register.addSubType(klass, type);

              continue typeLoop;
            }

            klass = klass.getSuperclass();
          }
        }
      }
    }

    return register;
  }
}
