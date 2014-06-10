package myAnnotations;
import java.lang.annotation.*;
 
 @Documented
 @Target(ElementType.FIELD)
 @Retention(RetentionPolicy.SOURCE)
  public @interface Exclude
  {     
  }