package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.example.jackson_generic_instrospector.TypeIdGetter.SimpleGenericTypeIdGetter;

public class TypeDefinition<T> {

  private final Class<T> rawClass;

  private final Set<Class<?>> subTypes;

  private final Map<String, Class<?>> idToSubType;

  private final Map<Class<?>, String> subTypeToId;

  private JsonTypeInfo.As inclusion;

  private String propertyName;

  private TypeIdGetter typeIdGetter;

  public TypeDefinition(
      Class<T> rawClass, As inclusion, String propertyName, TypeIdGetter typeIdGetter) {
    this.rawClass = rawClass;
    this.inclusion = inclusion;
    this.propertyName = propertyName;
    this.typeIdGetter = typeIdGetter;

    this.subTypes = new HashSet<>();
    this.idToSubType = new HashMap<>();
    this.subTypeToId = new HashMap<>();
  }

  public TypeDefinition(Class<T> rawClass) {
    this(rawClass, As.PROPERTY, "type", new SimpleGenericTypeIdGetter());
  }

  public Class<T> getRawClass() {
    return rawClass;
  }

  public Set<Class<?>> getSubTypes() {
    return subTypes;
  }

  public void addSubType(Class<?> subType) {

    String subTypeId =
        null;
    try {
      subTypeId = this.typeIdGetter.getTypeId(subType.getConstructor(new Class[] {}).newInstance());
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    this.subTypes.add(subType);
    this.idToSubType.put(subTypeId, subType);
    this.subTypeToId.put(subType, subTypeId);
  }

  public Boolean hasSubType(Class<?> subType) {
    return this.subTypes.contains(subType);
  }

  public Class<?> getSubTypeFromId(String id) {
    return this.idToSubType.get(id);
  }

  public String getIdFromSubType(Class<?> subType) {
    return this.subTypeToId.get(subType);
  }

  public As getInclusion() {
    return inclusion;
  }

  public void setInclusion(As inclusion) {
    this.inclusion = inclusion;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public TypeIdGetter getTypeIdGetter() {
    return typeIdGetter;
  }

  public void setTypeIdGetter(TypeIdGetter typeIdGetter) {
    this.typeIdGetter = typeIdGetter;
  }
}
