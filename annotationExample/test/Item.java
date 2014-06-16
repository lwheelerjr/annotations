package test;

import myAnnotations.*;

@Builder(exclude = "tax")
public class Item {
	
	int id;

	double price;

	String description;

	Integer type;

	Double tax;
}