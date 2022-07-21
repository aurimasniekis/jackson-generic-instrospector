package org.example.jackson_generic_instrospector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface TypeIdGetter {
  String getTypeId(Object object);

  public class MethodGenericTypeIdGetter implements TypeIdGetter {
    protected final Method method;

    public MethodGenericTypeIdGetter(Method method) {
      this.method = method;
    }

    @Override
    public String getTypeId(Object object) {
      if (!this.method.getDeclaringClass().isAssignableFrom(object.getClass())) {
        throw new IllegalArgumentException(
            "Object provided '%s' is not same as defined method class '%s'"
                .formatted(object.getClass().getName(), this.method.getDeclaringClass().getName()));
      }

      try {
        return (String) this.method.invoke(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new IllegalArgumentException("Unable to get type id");
      }
    }
  }

  public class SimpleGenericTypeIdGetter implements TypeIdGetter {

    @Override
    public String getTypeId(Object object) {
      return object.getClass().getSimpleName();
    }
  }
}
