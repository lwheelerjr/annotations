package test;
 
public class HelloWorld {

	@LogIt
    public void sayHello(String name) {
    	delay(4);
        System.out.println("Hello "+name);
    }

	@LogIt(prefix = "PFX")
    public void sayGoodbye(String name) {
    	delay(10);
        System.out.println("Goodbye "+name);
    }    
    
    private void delay(long millis) {
    	try { 
    	   Thread.sleep(millis); 
    	} catch (InterruptedException ie) { 
    	}
    }
    
}