package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleIntrospector extends NopAnnotationIntrospector {
  @Autowired protected TypeRegister typeRegistry;

  @Override
  public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac,
      JavaType baseType) {
    return resolveTypeResolver(config, ac, baseType);
  }

  @Override
  public TypeResolverBuilder<?> findPropertyTypeResolver(
      MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
    if (baseType.isContainerType() || baseType.isReferenceType()) {
      return null;
    }

    return resolveTypeResolver(config, am, baseType);
  }

  @Override
  public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config,
      AnnotatedMember am, JavaType containerType) {
    if (containerType.getContentType() == null) {
      throw new IllegalArgumentException("Must call method with a container or reference type (got " + containerType + ")");
    }

    return resolveTypeResolver(config, am, containerType.getContentType());
  }

  protected TypeResolverBuilder<?> resolveTypeResolver(MapperConfig<?> config, Annotated a, JavaType baseType)
  {
    var rawClass = baseType.getRawClass();
    if (rawClass.equals(Object.class)) {
      if (a instanceof AnnotatedMethod am) {
        if (am.getParameterCount() == 1) {
          rawClass = am.getRawParameterType(0);
        } else if(am.getParameterCount() == 0) {
          rawClass = am.getRawReturnType();
        }
      } else {
        rawClass = a.getRawType();
      }
    }

    if (rawClass.isPrimitive()) {
      return null;
    }

    var typeDef = typeRegistry.getType(rawClass);
    if (Objects.isNull(typeDef)) {
      return null;
    }

    var builder = new StdTypeResolverBuilder();
    builder.init(Id.CUSTOM, new ExampleTypeIdResolver(config, a, rawClass, typeRegistry));
    builder.inclusion(typeDef.getInclusion());
    builder.typeProperty(typeDef.getPropertyName());
    builder.typeIdVisibility(true);

    return builder;
  }

  @Override
  public List<NamedType> findSubtypes(Annotated a) {
    Class<?> baseType = null;
    if (a instanceof AnnotatedClass ac) {
      baseType = ac.getRawType();
    }

    if (a instanceof AnnotatedMethod am) {
      if (am.getParameterCount() > 0) {
        baseType = am.getRawParameterType(0);
      } else {
        baseType = am.getRawReturnType();
      }
    }

    if (a instanceof AnnotatedField af) {
      baseType = af.getRawType();
    }

    if (baseType == null) {
      return null;
    }

    var typeDef = typeRegistry.findByTypeOrSubType(baseType);
    if (Objects.isNull(typeDef)) {
      return null;
    }

    var subTypes = typeDef.getSubTypes();

    if (subTypes == null) {
      return null;
    }

    return subTypes.stream().map(NamedType::new).toList();
  }

  @Override
  public String findTypeName(AnnotatedClass ac) {
    var typeDef = typeRegistry.findTypeBySubType(ac.getRawType());

    if (Objects.isNull(typeDef)) {
      return null;
    }

    return typeDef.getIdFromSubType(ac.getRawType());
  }

  @Override
  public Boolean isTypeId(AnnotatedMember am) {
    return am.hasAnnotation(GenericTypeId.class);
  }

  @Override
  public boolean isAnnotationBundle(Annotation ann) {
    return ann.annotationType() == GenericType.class;
  }
}
