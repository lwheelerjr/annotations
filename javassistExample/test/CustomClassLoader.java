package test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import java.lang.annotation.*;
import java.io.*;
/**
 * Custom implementation of a ClassLoader.
 * For any of classes from the "test" package
 * it will use javassist to load and process the class. 
 * For any other class it will use the super.loadClass() method
 * from ClassLoader, which will eventually pass the
 * request to the parent.
 *
 */
public class CustomClassLoader extends ClassLoader {

    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        //System.out.println("loading class '" + name + "'");
        clazz = super.loadClass(name);

        if (clazz.getPackage().getName().equals("test") && !clazz.isAnnotation() && !clazz.isInterface()) { //name.startsWith("test.")) { // && !name.contains("LogIt")) {        
            clazz = injectLoggingAroundMethods(clazz);   
        }
        return clazz;
    }


    private Class injectLoggingAroundMethods(Class clazz) {
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass ctClazz = cp.get(clazz.getName());

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

            return ctClazz.toClass();            
    
        } catch (NotFoundException | CannotCompileException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
