package myClasses;

import myAnnotations.*;

@Builder(exclude = "ta")
public class Item {
	
	int id;

	double price;

	String description;

	@Exclude
	Integer type;

	Double tax;
}