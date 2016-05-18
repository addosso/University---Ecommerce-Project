package ecommerce_2016_1610889.data;

import java.util.ArrayList;

import ecommerce_2016_1610889.utils.Tuple;

public class Carrello {
	
	
	private ArrayList<Tuple> allItems;
	

	
	public Carrello(){
		allItems = new ArrayList<Tuple>();
	}
	
	public int getNumbersOfItems(){
		int number = 0;
		for(int i=0; i< allItems.size(); i++){
			number += allItems.get(i).getNumberOfItems();
		}
		return number;
	}
	public void updateItem(String c, int n){
		for(int i=0; i< allItems.size(); i++){
			if(allItems.get(i).getItem().getTitle().equals(c)){
				if(n >0)
				allItems.get(i).updateQuantity(n);
				else
					allItems.remove(i);
			}
			
		}
	};
	
	public double getTotalPrice(){
		double finalPrice =0;
		for(int i=0; i<allItems.size(); i++){
			finalPrice += allItems.get(i).getTotalPrice();
		}
		return finalPrice;
	}
	
	public void insertItem(Tuple e){
		allItems.add(e);
	}
	
	public void insertItem(Tuple e, int quantity){
		for(int i=0; i< quantity; i++){
			insertItem(e);
		}
	}
	
	public ArrayList<Tuple> getItems(){
		return allItems;
	}
}
