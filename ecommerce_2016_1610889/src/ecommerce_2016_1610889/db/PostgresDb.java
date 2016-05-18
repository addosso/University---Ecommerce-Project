package ecommerce_2016_1610889.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import ecommerce_2016_1610889.data.Category;
import ecommerce_2016_1610889.data.Currency;
import ecommerce_2016_1610889.data.DataLayer;
import ecommerce_2016_1610889.data.Item;
import ecommerce_2016_1610889.data.User;
import ecommerce_2016_1610889.utils.CategoriesComparator;

import org.postgresql.*;

public class PostgresDb implements DataLayer {
	private static PostgresDb instance;
	private String dbName;
	private String dbUser;
	private String dbPassword;
	private Connection conn;
	
	/*
	 * Qui implemento tutte le query come stringhe final, almeno me le ritrovo e le uso nei vari
	 * metodi.
	 */
	private final String DELETE_USER= "DELETE FROM ACCOUNT WHERE username=?";
	private final String ADD_USER = "INSERT INTO ACCOUNT values (?,?,?,?,?)";
	private final String GET_USER_NAME_PASSWORD = "SELECT username,realname, realsurname,is_admin FROM ACCOUNT where username = ? and password = ? ";
	private final String GET_USER_BY_ROLE = "SELECT * FROM ACCOUNT WHERE is_admin=?";
	private final String GET_CATEGORIES = "SELECT * FROM CATEGORY";
	private final String GET_CATEGORY_BY_ID = GET_CATEGORIES+ " WHERE id = ?";
	private final String ADD_CATEGORY = "INSERT INTO CATEGORY VALUES (?,?,?,?)";
	private final String DELETE_CATEGORY = "DELETE FROM CATEGORY WHERE id = ?";
	private final String GET_ITEM_SORTED  ="SELECT C.id as ItemID, C.title, C.description, "
											+ "C.price, C.available, C.image_path, C.show,D.curr_name as Currency, D.to_eur, D.to_dollar " 
											+"from category A"
											+" join categ_item B on A.id = B.categ_id "
											+" join item C on  C.id = B.item_id" 
											+ " join currency D on D.curr_name =  C.currency " 
											+" where categ_id = ?";


	private final String DELETE_ITEM = "DELETE FROM categ_item where categ_id = ? and item_id = ?;"
									 + "DELETE FROM item where id = ?; ";
	 
	private final String NEW_ITEM_ID = "select categ_item"
									  + " from categ_item"
									  + " where categ_item.categ_id = ?"
									  + " order by categ_item DESC"
									  + "	limit 1";
	private final String GET_CURRENCY_BY_NAME ="SELECT * FROM currency where curr_name = ?";
	private final String UPDATE_CURRENCY = "Update currency set to_eur=?, to_dollar=? where curr_name=? ";
	private final String CREATE_CURRENCY = "INSERT INTO currency values (?,?,?)";
	private final String CREATE_ITEM = "insert into item values ( ?,?,?,?,?,?,?,'',?)";
	private final String ADD_ITEM_TO_CATEGORY ="INSERT INTO categ_item values (?,?);";
	/*
	 * Fine query!
	 * 
	 */
	
	private PostgresDb(Connector c){
		dbName = c.getDbName();
		dbUser = c.getDbUser();
		dbPassword = c.getDbPassword();
		try{
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/"+dbName;
		Properties props = new Properties();
		props.setProperty("user",dbUser );
		props.setProperty("password",dbPassword);
		conn = DriverManager.getConnection(url, props);
		}catch( Exception e){
			System.out.println("Qualcosa non va nel db");
			e.printStackTrace();
		}
		
		
	}
	
	public static PostgresDb getInstance(Connector c){
		
		if(instance != null) return instance;
		instance = new PostgresDb(c);
		return instance;
		
		
	}
	
	

	@Override
	public boolean deleteUser(String username) {
		try {
			PreparedStatement st = conn.prepareStatement(DELETE_USER);
			st.setString(1, username);
			st.executeUpdate();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}

	@Override
	public User getUser(String username, String password) {
		User toRet = null;
		try {
			PreparedStatement st = conn.prepareStatement(GET_USER_NAME_PASSWORD );
			st.setString(1, username);
			st.setString(2, password);
			ResultSet s = st.executeQuery();
			while(s.next()){
				toRet = new User(s.getString(1),s.getString(2),s.getString(3),s.getBoolean(4)? "administrator": "regular");
			}
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
		return toRet;
		}
		
	}
		

	@Override
	public boolean addUser(String username, String password, String realName, String realSurname, String accountType) {
		if(getUser(username, password) != null) return false;
		
		try {
			PreparedStatement st = conn.prepareStatement(ADD_USER);
			st.setString(1, username);
			st.setString(2, password);
			st.setObject(3, realName);
			st.setString(4, realSurname);
			st.setBoolean(5, accountType.equals("administrator"));
			st.executeUpdate();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<User> getUsersByRole(String role) {
		List<User> allUser = new ArrayList<User>();
		try {
			PreparedStatement st = conn.prepareStatement(GET_USER_BY_ROLE );
			st.setBoolean(1, role.equals("administrator"));
			ResultSet s = st.executeQuery();
			while(s.next()){
				User add = new User();
				add.setUsername(s.getString(1));
				add.setPassword(s.getString(2));
				add.setName(s.getString(3));
				add.setSurname(s.getString(4));
				add.setAccountType(role);
				allUser.add(add);
			}
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allUser;
	}

	@Override
	public TreeSet<Category> getCategories() {
		TreeSet<Category> cat = new TreeSet<Category>();
		try {
			PreparedStatement st = conn.prepareStatement(GET_CATEGORIES );
			ResultSet s = st.executeQuery();
			while(s.next()){
				Category add = new Category();
				add.setId(s.getInt(1));
				add.setName(s.getString(2));
				add.setImageUrl(s.getString(3));
				cat.add(add);
			}
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cat;
	}

	@Override
	public Category getCategoryById(int id) {
		Category categoryToReturn = null;
		try {
			PreparedStatement st = conn.prepareStatement(GET_CATEGORY_BY_ID );
			st.setInt(1, id);
			ResultSet s = st.executeQuery();
			while(s.next()){
				Category add = new Category();
				add.setId(s.getInt(1));
				add.setName(s.getString(2));
				add.setImageUrl(s.getString(3));
				categoryToReturn = add;
			}
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return categoryToReturn;
	}

	@Override
	public boolean addCategory(Category newCat) {
		int newCategoryID = getCategories().last().getId();
		newCategoryID++;
		try {
			PreparedStatement st = conn.prepareStatement(ADD_CATEGORY);
			st.setInt(1, newCategoryID);
			st.setString(2, newCat.getName());
			st.setString(3, newCat.getImageUrl());
			st.setNull(4, java.sql.Types.VARCHAR);
			st.executeUpdate();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean modifyCategory(Category newCat) {
		removeCategory(newCat.getId());
		addCategory(newCat);
		return false;
	}

	@Override
	public boolean removeCategory(int idCategory) {
		try {
			PreparedStatement st = conn.prepareStatement(DELETE_CATEGORY);
			st.setInt(1, idCategory);
			st.executeUpdate();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public List<Item> getItemsSortedBy(List<Category> cat, String type) {
		List<Item> allItems = new ArrayList<Item>();
		Collections.sort(cat, CategoriesComparator.idComparator);
		for(int i=0; i< cat.size(); i++){
	
		 	try {
				PreparedStatement st = conn.prepareStatement(GET_ITEM_SORTED);
				st.setInt(1, cat.get(i).getId());
				ResultSet s = st.executeQuery();
				while(s.next()){
					Item item = new Item();
					item.setId(s.getInt(1));
					item.setCategory(cat.get(i));
					item.setTitle(s.getString(2));
					item.setDescription(s.getString(3));
					item.setPrice(s.getDouble(4));
					// La currency la prendo sulle colonne successive ! 
					item.setDate(s.getDate(5));
					item.setImgSrc(s.getString(6));
					//Image description? no!! la 8 la salto;
					item.setVisible(s.getBoolean(7));
					Currency curr  = new Currency();
					curr.setName(s.getString(8));
					curr.setToEur(s.getDouble(9));
					curr.setToDollar(s.getDouble(10));
					//Adesso setto la currency all'item
					item.setCurrency(curr);
					allItems.add(item);
				}
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return allItems;
	}

	@Override
	public Item getItem(int idCategory, int idItem) {
		Item toReturn = null;
		ArrayList<Category> cat = new ArrayList<Category>(getCategories());
		List<Item> it = getItemsSortedBy(cat, "id");
		for(int i=0; i< it.size(); i++){
			if(it.get(i).getId() == idItem && it.get(i).getCategory().getId() == idCategory)
				toReturn = it.get(i);
		}
		
		return toReturn;
	}

	@Override
	public boolean removeItem(int category, int idItem) {
		try {
			PreparedStatement st = conn.prepareStatement(DELETE_ITEM);
			st.setInt(1, category);
			st.setInt(2, idItem);
			st.setInt(3, idItem);
			st.executeUpdate();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean addItem(int categoryId, Item c) {
		int newId = 0;
		try {
			PreparedStatement st = conn.prepareStatement(NEW_ITEM_ID);
			st.setInt(1, categoryId);
			ResultSet rs= st.executeQuery();
			while(rs.next()){
				newId = rs.getInt(1) > newId? rs.getInt(1): newId;
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newId++;
		// Verifico se il name del currency è gia presente e in caso lo inserisco / modifico
		Currency myCurr = new Currency();
		try{
			PreparedStatement st = conn.prepareStatement(GET_CURRENCY_BY_NAME);
			st.setString(1, c.getCurrency().getName());
			ResultSet x = st.executeQuery();
			while(x.next()){
				myCurr.setName(x.getString(1));
				myCurr.setToEur(x.getDouble(2));
				myCurr.setToDollar(x.getDouble(3));
			}
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		if(myCurr.getName() != null && !myCurr.getName().equals("")){
			updateCurrency(c.getCurrency());
		}else{
			System.out.println("NON CI STAVA");
			createCurrency(c.getCurrency());
		}
		//Adesso finalmente creo il mio item e lo inserisco nella categoria!!!
		try{
			PreparedStatement st = conn.prepareStatement(CREATE_ITEM);
			st.setInt(1, newId);
			st.setString(2, c.getTitle());
			st.setString(3, c.getDescription());
			st.setDouble(4, c.getPrice());
			st.setString(5, c.getCurrency().getName());
			st.setDate(6, new Date( c.getDate().getTime()));
			st.setString(7, c.getImgSrc());
			st.setBoolean(8, c.isVisible());
			st.executeUpdate();
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		// Meglio inserirlo dopo nella categoria e non nella stessa esecuzione di query
		try{
			PreparedStatement st = conn.prepareStatement(ADD_ITEM_TO_CATEGORY);
			st.setInt(1, categoryId);
			st.setInt(2, newId);
			st.executeUpdate();
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	private void createCurrency(Currency c){
		try {
			PreparedStatement st = conn.prepareStatement(CREATE_CURRENCY);
			st.setDouble(2, c.getToEur());
			st.setDouble(3, c.getToDollar());
			st.setString(1, c.getName());
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateCurrency(Currency c){
		try {
			PreparedStatement st = conn.prepareStatement(UPDATE_CURRENCY);
			st.setDouble(1, c.getToEur());
			st.setDouble(2, c.getToDollar());
			st.setString(3, c.getName());
			st.executeUpdate();
			
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean addItem(int categoryId, int itemId, Item c) {
		
		// Verifico se il name del currency è gia presente e in caso lo inserisco / modifico
		Currency myCurr = new Currency();
		try{
			PreparedStatement st = conn.prepareStatement(GET_CURRENCY_BY_NAME);
			st.setString(1, c.getCurrency().getName());
			ResultSet x = st.executeQuery();
			while(x.next()){
				myCurr.setName(x.getString(1));
				myCurr.setToEur(x.getDouble(2));
				myCurr.setToDollar(x.getDouble(3));
			}
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		if(myCurr.getName() != null && !myCurr.getName().equals("")){
			updateCurrency(c.getCurrency());

		}else{
			createCurrency(c.getCurrency());
		}
		//Adesso finalmente creo il mio item e lo inserisco nella categoria!!!
		try{
			PreparedStatement st = conn.prepareStatement(CREATE_ITEM);
			st.setInt(1, itemId);
			st.setString(2, c.getTitle());
			st.setString(3, c.getDescription());
			st.setDouble(4, c.getPrice());
			st.setString(5, c.getCurrency().getName());
			st.setDate(6, new Date( c.getDate().getTime()));
			st.setString(7, c.getImgSrc());
			st.setBoolean(8, c.isVisible());
			st.executeUpdate();
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		// Meglio inserirlo dopo nella categoria e non nella stessa esecuzione di query
		try{
			PreparedStatement st = conn.prepareStatement(ADD_ITEM_TO_CATEGORY);
			st.setInt(1, categoryId);
			st.setInt(2, itemId);
			st.executeUpdate();
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return true;
	}
	

}
