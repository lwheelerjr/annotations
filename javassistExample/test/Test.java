package test;

public class Test {
 
    public static void main(String[] args) { 
    	System.out.println("Started Test main()");
 		HelloWorld hw = new HelloWorld();
 		System.out.println("Created HelloWorld instance");
 		hw.sayHello("to my little friend");
 		hw.sayGoodbye("cruel world");
    } 
}