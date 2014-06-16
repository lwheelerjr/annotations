package myAnnotations;
import java.lang.annotation.*;
 
 @Target(ElementType.TYPE)
 @Retention(RetentionPolicy.SOURCE)
 public @interface Builder {     
     public String exclude() default "";
 }