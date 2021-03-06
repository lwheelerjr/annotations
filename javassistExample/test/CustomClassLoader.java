package test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import java.lang.annotation.*;

/**
 * Custom implementation of a ClassLoader.
 * For any classes from the "test" package it will look for LogIt annotations and use javassist to inject some code. 
 */

public class CustomClassLoader extends ClassLoader {

    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class clazz = super.loadClass(name);
        if (clazz.getPackage().getName().equals("test") && !clazz.isAnnotation() && !clazz.isInterface()) { 
            System.out.println("Loaded Class: " + clazz.getName() );
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
                    System.out.println("Found LogIt annotation: " + ctClazz.getName() + "." + method.getName() + ", injecting code!");
                    String prefix = ((LogIt)annotation).prefix();
                    method.addLocalVariable("startTime", CtClass.longType);                    
                    method.insertBefore("{ startTime = System.currentTimeMillis(); " + 
                                        "  System.out.println(\"" + prefix + "Entering Method: " + method.getName() + " \"); }");
                    method.insertAfter(" { System.out.println(\"" + prefix + "Exiting Method: " + method.getName() + 
                                       ", Elapsed Time: \" + (System.currentTimeMillis() - startTime) +  \" millis\"); }");
                }
            }
            return ctClazz.toClass();                
        } catch (NotFoundException | CannotCompileException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
