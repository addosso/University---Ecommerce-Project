package ecommerce_2016_1610889.utils;

import ecommerce_2016_1610889.data.Item;

public class Tuple {

	int numberOfItems;
	Item refItem;

	public Tuple(){
	
	}
	public Tuple(int c, Item e){
		numberOfItems = c;
		refItem = e;
	}
	
	public void addItem(int c){
		numberOfItems++;
	}
	
	public Item getItem(){
		return refItem;
	}
	
	public void updateQuantity(int n){
		numberOfItems =n;
	}
	
	public int getNumberOfItems(){
		return numberOfItems;
	}
	
	public double getTotalPrice(){
		return numberOfItems * refItem.getPrice();
	}
	
}
