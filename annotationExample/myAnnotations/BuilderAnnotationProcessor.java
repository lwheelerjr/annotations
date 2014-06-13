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
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
       for (TypeElement t : annotations) {        
          for (Element classElement : roundEnv.getElementsAnnotatedWith(t)) {             
             if (classElement.getKind() == ElementKind.CLASS) {

               String excludedFields = ((Builder)classElement.getAnnotation(Builder.class)).exclude().toString();
               Map<String, String> fields = new LinkedHashMap<>();
               
               for (Element fieldElement : classElement.getEnclosedElements()) {
                    if(fieldElement.getKind() == ElementKind.FIELD 
                          && !excludedFields.contains(fieldElement.getSimpleName().toString())) {                     
                     String fieldName = fieldElement.getSimpleName().toString();
                     fields.put(fieldName, fieldElement.asType().toString());
                   }
                }

                try {
                  writeBuilderInfoFile(classElement ,fields);
                }
                catch (IOException ioe)
                {
                  ioe.printStackTrace();
                } 
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
      String packageName = ((PackageElement)element.getEnclosingElement()).getQualifiedName().toString();
      String pojoQualifiedName = ((TypeElement) element).getQualifiedName().toString();
      String pojoClassName = pojoQualifiedName.substring(pojoQualifiedName.lastIndexOf(".") + 1);
      String builderName = pojoClassName + "Builder";

      JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + builderName, element);
      PrintWriter out = new PrintWriter(sourceFile.openWriter());                 
      
      out.println("package " + packageName + ";\n");               
      out.println("import javax.annotation.Generated;\n");      
      out.println("@Generated(value=\"myAnnotations.BuilderAnnotationProcessor\")\n");
      out.print("public class ");
      out.println(builderName);
      out.println("{\n"); 
      out.println("   " + pojoClassName  + " pojo = new " + pojoClassName + "();\n" );  // write pojo instance variable declaration

      for (Map.Entry<String, String> field : fields.entrySet())  // write with...() methods for each field
      {
         String fieldName = field.getKey();
         String fieldNameCapd = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
         String fieldType = field.getValue();
         out.println("   public " + builderName + " with" + fieldNameCapd + "( " + fieldType + " value) {");
         out.println("      pojo." + fieldName + " = value;");
         out.println("      return this;");
         out.println("   }\n");         
      }

     out.println("   public " + pojoClassName + " build() {");  // write build() method
     out.println("      return pojo;");
     out.println("   }\n");     
     out.println("}");
     out.close();
  }
}