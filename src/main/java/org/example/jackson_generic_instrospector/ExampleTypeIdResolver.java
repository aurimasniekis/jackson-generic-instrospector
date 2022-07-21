package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;

public class ExampleTypeIdResolver implements TypeIdResolver {
  protected Class<?> baseType;
  protected MapperConfig<?> config;
  protected Annotated am;

  protected TypeRegister typeRegistry;

  public ExampleTypeIdResolver(
      MapperConfig<?> config, Annotated am, Class<?> baseType, TypeRegister typeRegistry) {
    this.baseType = baseType;
    this.config = config;
    this.am = am;
    this.typeRegistry = typeRegistry;
  }

  @Override
  public void init(JavaType baseType) {
    throw new IllegalArgumentException("GenericIdTypeResolver::init");
  }

  @Override
  public String idFromValue(Object value) {
    throw new IllegalArgumentException("GenericIdTypeResolver::idFromValue");
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    throw new IllegalArgumentException("GenericIdTypeResolver::idFromValueAndType");
  }

  @Override
  public String idFromBaseType() {
    return idFromValueAndType(null, baseType);
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) throws IOException {
    var typeDef = typeRegistry.getType(baseType);

    if (typeDef == null) {
      return null;
    }

    return context.getTypeFactory().constructType(typeDef.getSubTypeFromId(id));
  }

  @Override
  public String getDescForKnownTypeIds() {
    return null;
  }

  @Override
  public Id getMechanism() {
    return Id.CUSTOM;
  }
}

