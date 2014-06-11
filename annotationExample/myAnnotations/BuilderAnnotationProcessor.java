 package myAnnotations;
 
 import java.beans.*;
 import java.io.*;
 import java.util.*;
 import javax.annotation.processing.*;
 import javax.lang.model.*;
 import javax.lang.model.element.*;
 import javax.tools.*;
 import javax.tools.Diagnostic.*;
 /**
  * This class is the processor that analyzes Builder annotations.
  */
 @SupportedAnnotationTypes("myAnnotations.Builder")
 @SupportedSourceVersion(SourceVersion.RELEASE_7)
 public class BuilderAnnotationProcessor extends AbstractProcessor
 {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
       for (TypeElement t : annotations)
       {
          Map<String, String> fields = new LinkedHashMap<>();

          for (Element e : roundEnv.getElementsAnnotatedWith(t))
          {
             String excludedFields = ((Builder)e.getAnnotation(Builder.class)).exclude().toString();

             for (Element field : e.getEnclosedElements()) {

                  if(field.getKind() == ElementKind.FIELD 
                      && field.getAnnotation(Exclude.class) == null 
                        && !excludedFields.contains(field.getSimpleName().toString())) {
                   
                   String fname = field.getSimpleName().toString();
                   fields.put(fname, field.asType().toString());
                 }
              }

              try {
                writeBuilderInfoFile(e ,fields);
              }
              catch (IOException ioe)
              {
                ioe.printStackTrace();
              }   
        }

      }
      return true;
   }

   /**
    * Writes the source file for the Builder class.
    * @param pojoQualifiedName the name of the bean class
    * @param fields a map of property names and their annotations
    */
   private void writeBuilderInfoFile(Element element, Map<String, String> fields)
      throws IOException
   {
      String pojoQualifiedName = ((TypeElement) element).getQualifiedName().toString();
      int i = pojoQualifiedName.lastIndexOf(".");
      String pojoClassName = pojoQualifiedName.substring(i + 1);
      String builderName = pojoClassName + "Builder";

      JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
         pojoQualifiedName.substring(0, i) + "." + builderName, element);
      PrintWriter out = new PrintWriter(sourceFile.openWriter());                 
      
      if (i > 0)
      {
         out.print("package ");
         out.print(pojoQualifiedName.substring(0, i));
         out.println(";\n");
      }
      
      out.println("import javax.annotation.Generated;\n");
      
      out.println("@Generated(value=\"myAnnotations.BuilderAnnotationProcessor\")\n");

      out.print("public class ");
      out.println(builderName);
      out.println("{\n"); 

      out.println("   " + pojoClassName  + " pojo = new " + pojoClassName + "();\n" );  // write pojo instance variable declaration

      for (Map.Entry<String, String> e : fields.entrySet())  // write with...() methods for each field
      {
         String name = e.getKey();
         String capName = e.getKey().toUpperCase().substring(0,1) + e.getKey().substring(1);
         String type = e.getValue();

         out.println("   public " + builderName + " with" + capName + "( " + type + " value) {");
         out.println("      pojo." + name + " = value;");
         out.println("      return this;");
         out.println("   }\n");         
      }

     out.println("   public " + pojoQualifiedName.substring(i + 1) + " build() {");  // write build() method
     out.println("      return pojo;");
     out.println("   }\n");
     
     out.println("}");
     out.close();
  }
}