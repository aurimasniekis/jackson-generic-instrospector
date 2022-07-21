package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ExampleModule extends SimpleModule {
  @Autowired private ExampleIntrospector introspector;

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);

    context.insertAnnotationIntrospector(introspector);
  }
}
