package myClasses;

public class Test {
	
	static public void main(String[] args) {
		Item item = new ItemBuilder().withDescription("This item's description").withPrice(12.00).build();
		System.out.println("Item: " + item.description + " Price: " + item.price);
	}
}