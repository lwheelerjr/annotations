package test;

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
    public Class<?> loadClass(String name)
        throws ClassNotFoundException {
        Class clazz = null;
        //System.out.println("loading class '" + name + "'");
        
        if (name.startsWith("test.") && !name.contains("LogIt")) {            
           clazz = new Injector().injectLoggingAroundMethods(name);
        } else {
           clazz = super.loadClass(name);
        }
        return clazz;
    }

}
