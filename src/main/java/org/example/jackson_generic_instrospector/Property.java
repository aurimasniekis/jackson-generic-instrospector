package org.example.jackson_generic_instrospector;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Property<T extends Type> {
  private T type;

  private String name;

  public Property(T type, String name) {
    this.type = type;
    this.name = name;
  }

  public T getType() {
    return type;
  }

  public void setType(T type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
