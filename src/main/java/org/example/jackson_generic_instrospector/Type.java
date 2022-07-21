package org.example.jackson_generic_instrospector;

import lombok.NoArgsConstructor;
@GenericType
public interface Type {

  @GenericTypeId
  public default String getTypeName() {
    return this.getClass().getSimpleName();
  }

  @NoArgsConstructor
  public class TextType implements Type {

    private String language;

    public TextType(String language) {
      this.language = language;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }
  }

  @NoArgsConstructor
  public class NumberType implements Type {

    private String format;

    public NumberType(String format) {
      this.format = format;
    }

    public String getFormat() {
      return format;
    }

    public void setFormat(String format) {
      this.format = format;
    }
  }

  public class FooType extends AbsType {

  }

  public abstract class AbsType implements Type {

  }
}
