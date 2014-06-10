package test;
 
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import java.lang.annotation.*;
 

public class Injector {
     
    public Class injectLoggingAroundMethods(String className) {
        Class clazz = null;
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass ctClazz = cp.get(className);

            for(CtMethod method : ctClazz.getDeclaredMethods()) {

                Object annotation = method.getAnnotation(LogIt.class);

                if(annotation instanceof LogIt) {
                    
                    String prefix = ((LogIt)annotation).prefix();

                    method.addLocalVariable("startTime", CtClass.longType);
                    
                    method.insertBefore("{ startTime = System.currentTimeMillis(); " + 
                                        "  System.out.println(\"" + prefix + "Entering Method: " + method.getName() + " \"); }");

                    method.insertAfter(" { System.out.println(\"" + prefix + "Exiting Method: " + method.getName() + 
                                       " ,Elapsed Time: \" + (System.currentTimeMillis() - startTime) +  \" millis\"); }");
                }
            }

            clazz = ctClazz.toClass();            
    
        } catch (NotFoundException | CannotCompileException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return clazz;
    }
 
}