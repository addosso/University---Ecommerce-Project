package ecommerce_2016_1610889.data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public interface DataLayer {
	
	boolean deleteUser(String username);
	User getUser(String username, String password);
	boolean addUser(String username, String password, String realName, String realSurname, String accountType);
	List<User> getUsersByRole(String role);
	
	Set<Category> getCategories();
	Category getCategoryById(int id);
	boolean addCategory(Category newCat);
	boolean modifyCategory(Category newCat);
	boolean removeCategory(int idCategory);
	
	
	List<Item> getItemsSortedBy(List<Category> cat, String type);
	Item getItem(int idCategory, int idItem);
	boolean removeItem(int category, int idItem);
	boolean addItem(int categoryId, Item c );
	public boolean addItem(int categoryId,int itemId, Item c);


}