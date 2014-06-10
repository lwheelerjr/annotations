package myClasses;

import myAnnotations.*;

@Builder(exclude = "tax")
public class Item {
	
	int id;

	double price;

	String description;

	@Exclude
	Integer type;

	Double tax;
}