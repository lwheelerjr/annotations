package test;

import javax.annotation.Generated;

@Generated(value="myAnnotations.BuilderAnnotationProcessor")

public class ItemBuilder
{

   Item pojo = new Item();

   public ItemBuilder withId( int value) {
      pojo.id = value;
      return this;
   }

   public ItemBuilder withPrice( double value) {
      pojo.price = value;
      return this;
   }

   public ItemBuilder withDescription( java.lang.String value) {
      pojo.description = value;
      return this;
   }

   public ItemBuilder withType( java.lang.Integer value) {
      pojo.type = value;
      return this;
   }

   public Item build() {
      return pojo;
   }

}
