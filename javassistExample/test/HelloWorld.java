package test;
 
public class HelloWorld {


	@LogIt
    public void sayHello(String name) {
    	delay(4);
        System.out.println("Hello "+name);
    }


	@LogIt(prefix = "Now Ending, ")
    public void sayGoodbye(String name) {
    	delay(10);
        System.out.println("Hello "+name);
    }    

    
    private void delay(long millis) {
    	try { 
    		Thread.sleep(millis); 
    	} catch (InterruptedException ie) { 
    	}
    }
    
}