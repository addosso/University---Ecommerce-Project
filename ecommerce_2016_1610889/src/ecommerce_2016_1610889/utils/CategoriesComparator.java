package ecommerce_2016_1610889.utils;

import java.util.Comparator;

import ecommerce_2016_1610889.data.Category;

public class CategoriesComparator {
	
	public static Comparator<Category> idComparator = new Comparator<Category>() {
		public int compare(Category o, Category t) {
			return o.compareTo(t);
		}
	};

}
