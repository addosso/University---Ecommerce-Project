package ecommerce_2016_1610889.utils;

import java.util.Comparator;

import ecommerce_2016_1610889.data.Item;

public class ItemsComparator {

	public static Comparator<Item> idComparator =  new Comparator<Item>() {
		public int compare(Item o, Item t) {
			int toR = 0;

			if (o.getCategory().compareTo(t.getCategory()) <= 0) {
				if (o.getId() > t.getId())
					toR = 1;
				else if (o.getId() < t.getId())
					toR = -1;
			} else {
				toR = o.getCategory().compareTo(t.getCategory());
			}
			return toR;
		}
	};
	
	public static Comparator<Item> priceComparator = new Comparator<Item>() {
		public int compare(Item o, Item t) {
			int toR = 0;

			if (o.getCategory().compareTo(t.getCategory()) <= 0) {
				if (o.getPrice() > t.getPrice())
					toR = 1;
				else if (o.getPrice() < t.getPrice())
					toR = -1;
			} else {
				toR = o.getCategory().compareTo(t.getCategory());
			}
			return toR;
		}
	};
	
	public static Comparator<Item> titleComparator = new Comparator<Item>() {
		public int compare(Item o, Item t) {
			int toR = 0;
			if (o.getCategory().compareTo(t.getCategory()) <= 0) {
				toR = o.getTitle().compareTo(t.getTitle());
			} else {
				toR = o.getCategory().compareTo(t.getCategory());
			}
			return toR;
		}
	};
	
	public static Comparator<Item> availableComparator = new Comparator<Item>() {
		public int compare(Item o, Item t) {
			int toR = 0;
			if (o.getCategory().compareTo(t.getCategory()) <= 0) {
				toR = o.getDate().compareTo(t.getDate());
			} else {
				toR = o.getDate().compareTo(t.getDate());
			}
			return toR;
		}
	};
	
	
}
