package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@JacksonAnnotation
public @interface GenericType {
  String propertyName() default "type";
}

