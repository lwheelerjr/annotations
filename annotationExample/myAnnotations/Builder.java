package myAnnotations;
import java.lang.annotation.*;
 
 @Documented
 @Target(ElementType.TYPE)
 @Retention(RetentionPolicy.SOURCE)
 public @interface Builder {     
     public String exclude() default "";
 }