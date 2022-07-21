package org.example.jackson_generic_instrospector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.example.jackson_generic_instrospector.Type.NumberType;
import org.example.jackson_generic_instrospector.Type.TextType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @Autowired private ObjectMapper objectMapper;
  @Autowired private TypeRegister typeRegister;

  @GetMapping("/demo/foo")
  public List<Property<?>> all() throws JsonProcessingException {
    typeRegister.addType(Type.class);
    var typeDef = typeRegister.getType(Type.class);
    typeDef.addSubType(NumberType.class);
    typeDef.addSubType(TextType.class);

    var result = new ArrayList<Property<?>>();
    result.add(
        new Property<>() {
          {
            setType(new NumberType("1234"));
            setName("number");
          }
        });
    result.add(
        new Property<>() {
          {
            setType(new TextType("1234"));
            setName("text");
          }
        });

    var json = objectMapper.writeValueAsString(result);

    var a = objectMapper.readValue(json, new TypeReference<List<Property<?>>>() {});

    return a;
  }
}
