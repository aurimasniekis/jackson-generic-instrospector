package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface GenericTypeId {
}

