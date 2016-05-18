package ecommerce_2016_1610889.db;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecommerce_2016_1610889.data.Category;
import ecommerce_2016_1610889.data.Currency;
import ecommerce_2016_1610889.data.DataLayer;
import ecommerce_2016_1610889.data.Item;
import ecommerce_2016_1610889.data.User;
import ecommerce_2016_1610889.utils.CategoriesComparator;
import ecommerce_2016_1610889.utils.ItemsComparator;

public class XmlDb implements DataLayer {

	private static XmlDb instance = null;
	private Document doc;
	private Connector c;

	private XmlDb(Connector c) {
		
		this.c = c;
		try {
			File inputFile = new File(c.getUrlXmlDocument());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);

			doc.getDocumentElement().normalize();
			this.doc = doc;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static XmlDb getXMLdb(Connector c) {
		if (instance == null) {
			instance = new XmlDb(c);
		}
		return instance;
	}

	public NodeList getNodelist(String name) {
		return doc.getElementsByTagName(name);
	}

	public Document getDocument() {
		return doc;
	}

	public Node getNodeQuery(int categoryId) {
		NodeList nList = doc.getElementsByTagName("category");
		for (int i = 0; i < nList.getLength(); i++) {
			if (Integer.valueOf(
					((Element) nList.item(i)).getElementsByTagName("id").item(0).getTextContent()) == categoryId) {
				return nList.item(i);
			}
		}
		return null;

	}

	public User getUser(String username, String password) {
		User userToReturn = null;
		NodeList nList = doc.getElementsByTagName("account");
		for (int i = 0; i < nList.getLength(); i++) {
			String uname = ((Element) nList.item(i)).getAttribute("username");
			String name = ((Element) nList.item(i)).getAttribute("realname");
			String surname = ((Element) nList.item(i)).getAttribute("realsurname");
			String accountType = ((Element) nList.item(i)).getAttribute("accountType");
			if (((Element) nList.item(i)).getAttribute("username").equals(username)
					&& ((Element) nList.item(i)).getAttribute("password").equals(password)) {
				userToReturn = new User(uname, name, surname, accountType);
				break;
			}
		}
		return userToReturn;
	}

	private void updateDocument(Document doc) {
		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(c.getUrlXmlDocument());
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		instance = new XmlDb(c);

	}

	public boolean deleteUser(String username) {
		NodeList nList = doc.getElementsByTagName("account");
		for (int i = 0; i < nList.getLength(); i++) {
			if (((Element) nList.item(i)).getAttribute("username").equals(username)) {
				nList.item(i).getParentNode().removeChild(nList.item(i));
			}
		}
		updateDocument(doc);

		return true;
	}

	private User getUserIfExists(String username) {
		User userToReturn = null;
		NodeList nList = doc.getElementsByTagName("account");
		for (int i = 0; i < nList.getLength(); i++) {
			String uname = ((Element) nList.item(i)).getAttribute("username");
			String name = ((Element) nList.item(i)).getAttribute("realname");
			String surname = ((Element) nList.item(i)).getAttribute("realsurname");
			String accountType = ((Element) nList.item(i)).getAttribute("accountType");
			if (((Element) nList.item(i)).getAttribute("username").equals(username)) {
				userToReturn = new User(uname, name, surname, accountType);
				break;
			}
		}
		return userToReturn;
	}

	public boolean addUser(String username, String password, String name, String surname, String accountType) {
		if (getUserIfExists(username) != null)
			return false;
		Node accountsNode = doc.getElementsByTagName("accounts").item(0);
		Element newUser = doc.createElement("account");
		newUser.setAttribute("username", username);
		newUser.setAttribute("password", password);
		if (!name.equals(""))
			newUser.setAttribute("realname", name);
		if (!surname.equals(""))
			newUser.setAttribute("realsurname", surname);
		if (accountType != null)
			newUser.setAttribute("accountType", accountType);
		else
			newUser.setAttribute("accountType", "regular");

		accountsNode.appendChild(newUser);
		updateDocument(doc);
		return true;
	}

	@Override
	public TreeSet<Category> getCategories() {
		TreeSet<Category> setCategories = new TreeSet<Category>();

		NodeList c = getNodelist("category");
		for (int i = 0; i < c.getLength(); i++) {
			Element el = (Element) c.item(i);
			Category cat = new Category();
			for(Node child = el.getFirstChild(); child != null; child = child.getNextSibling())
			    {
			        if(child instanceof Element && "id".equals(child.getNodeName()))
			        	cat.setId(Integer.parseInt( ((Element) child).getTextContent()));
			        	
			    
			        if(child instanceof Element && "image".equals(child.getNodeName()))
			        	cat.setImageUrl(((Element) child).getAttribute("src"));
			   
			}
			String categoryName = el.getAttribute("name");
			cat.setName(categoryName);
			setCategories.add(cat);

		}
		return c.getLength() > 0 ? setCategories : null;
	}

	@Override
	public Category getCategoryById(int id) {
		for (Iterator<Category> c = getCategories().iterator(); c.hasNext();) {
			Category act = c.next();
			if (act.getId() == id) {
				return act;
			}
		}
		return null;
	}

	@Override
	public List<Item> getItemsSortedBy(List<Category> cat, String type) {
		ArrayList<Item> m = new ArrayList<Item>();
		Collections.sort(cat, CategoriesComparator.idComparator);

		for (int i = 0; i < cat.size(); i++) {
			NodeList x = ((Element) getNodeQuery(cat.get(i).getId())).getElementsByTagName("item");
			for (int j = 0; j < x.getLength(); j++) {
				Element tmpEl = (Element) x.item(j);
				Item itemToAdd = new Item();
				Currency currency = new Currency();
				itemToAdd.setVisible(true);
				itemToAdd.setCategory(cat.get(i));
				if(!tmpEl.getAttribute("show").equals("")){
					itemToAdd.setVisible(Boolean.parseBoolean(tmpEl.getAttribute("show")));
				}
				
				currency.setName(tmpEl.getElementsByTagName("name").item(0).getTextContent());
				currency.setToEur(Double.valueOf(tmpEl.getElementsByTagName("toEUR").item(0).getTextContent()));
				int id = Integer.valueOf(tmpEl.getElementsByTagName("id").item(0).getTextContent());
				itemToAdd.setId(id);
				if (((Element) tmpEl.getElementsByTagName("image").item(0)) != null) {
					itemToAdd.setImgSrc(((Element) tmpEl.getElementsByTagName("image").item(0)).getAttribute("src"));
				}
				itemToAdd
						.setDescription(((Element) tmpEl.getElementsByTagName("description").item(0)).getTextContent());
				itemToAdd.setPrice(
						Double.valueOf(((Element) tmpEl.getElementsByTagName("price").item(0)).getTextContent()));

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				try {
					itemToAdd.setDate(
							sdf.parse(((Element) tmpEl.getElementsByTagName("available").item(0)).getTextContent()));
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				itemToAdd.setTitle(((Element) tmpEl.getElementsByTagName("title").item(0)).getTextContent());
				itemToAdd.setCurrency(currency);
				m.add(itemToAdd);
			}
		}
		if (type.equals("id")) {
			Collections.sort(m, ItemsComparator.idComparator);
		}

		if (type.equals("price")) {
			Collections.sort(m, ItemsComparator.priceComparator);
		}

		if (type.equals("title")) {
			Collections.sort(m, ItemsComparator.titleComparator);

		}
		if (type.equals("available")) {
			Collections.sort(m, ItemsComparator.availableComparator);

		}

		return m;
	}

	@Override
	public Item getItem(int idCategory, int idItem) {
		Item toReturn = null;
		ArrayList<Category> m = new ArrayList<Category>(getCategories());
		List<Item> x = getItemsSortedBy(m, "id");
		for (int i = 0; i < x.size(); i++) {
			if (x.get(i).getId() == idItem && x.get(i).getCategory().getId() == idCategory) {
				toReturn = x.get(i);
			}
		}
		return toReturn;
	}

	@Override
	public List<User> getUsersByRole(String role) {
		List<User> userToReturn = new ArrayList<User>();
		NodeList nList = doc.getElementsByTagName("account");
		for (int i = 0; i < nList.getLength(); i++) {
			String uname = ((Element) nList.item(i)).getAttribute("username");
			String name = ((Element) nList.item(i)).getAttribute("realname");
			String surname = ((Element) nList.item(i)).getAttribute("realsurname");
			String accountType = ((Element) nList.item(i)).getAttribute("accountType");
			String password = ((Element) nList.item(i)).getAttribute("password");
			if (accountType.equals(role)) {
				User toInsert = new User(uname, name, surname, accountType);
				toInsert.setPassword(password);
				userToReturn.add(toInsert);
			}
		}
		Collections.sort(userToReturn, new Comparator<User>() {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getUsername().compareTo(arg1.getUsername());
			}
		});
		return userToReturn;
	}

	@Override
	public boolean addCategory(Category newCat) {

		TreeSet<Category> cat = getCategories();
		int lastIndex = cat.last().getId();
		int indexForNewCategory = lastIndex + 1;
		Node categoriesNode = doc.getElementsByTagName("categories").item(0);
		Element newCategory = doc.createElement("category");
		newCategory.setAttribute("name", newCat.getName());
		Element id = doc.createElement("id");
		id.setTextContent(String.valueOf(indexForNewCategory));
		Element img = doc.createElement("image");
		img.setAttribute("src", newCat.getImageUrl());
		newCategory.appendChild(id);
		if (newCat.getImageUrl() != null && !newCat.getImageUrl().equals(""))
			newCategory.appendChild(img);
		categoriesNode.appendChild(newCategory);
		updateDocument(doc);
		return true;
	}
	@Override
	public boolean modifyCategory(Category newCat) {
		Node cat = doc.getElementsByTagName("categories").item(0);
		NodeList allCategories = doc.getElementsByTagName("category");
		Node categoriesNode = null;
		for(int i=0; i< allCategories.getLength(); i++){
			System.out.println(allCategories.item(i).getChildNodes().getLength());
			
			if(((Element)allCategories.item(i)).getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(newCat.getId()))){
				categoriesNode = allCategories.item(i);
				
				
			}
		}
		
		
		Element newCategory = doc.createElement("category");
		newCategory.setAttribute("name", newCat.getName());
		Element id = doc.createElement("id");
		id.setTextContent(String.valueOf(newCat.getId()));
		Element img = doc.createElement("image");
		img.setAttribute("src", newCat.getImageUrl());

		NodeList items = ((Element)categoriesNode).getElementsByTagName("item"); 
		removeCategory(newCat.getId());
		//Prima di settare l'id cancello la vecchia categoria
		
		newCategory.appendChild(id);
		if (newCat.getImageUrl() != null && !newCat.getImageUrl().equals(""))
			newCategory.appendChild(img);
		
		
		ArrayList<Node> c = new ArrayList<Node>();
		for(int m=0; m<items.getLength(); m++){
			c.add(items.item(m));
			//newCategory.appendChild(currToInsert);
		}
		for(int i=0; i<c.size();i++){
			newCategory.appendChild(c.get(i));
		}
	        	
		cat.appendChild(newCategory);
		
		
		updateDocument(doc);
		return true;
	}

	public boolean removeCategory(int idCategory) {
		NodeList x = doc.getElementsByTagName("category");
		for (int i = 0; i < x.getLength(); i++) {
			Node e = ((Element) x.item(i)).getElementsByTagName("id").item(0);
			if (Integer.valueOf(e.getTextContent()) == idCategory) {
				x.item(i).getParentNode().removeChild(x.item(i));
			}
		}
		updateDocument(doc);
		return true;
	}
	
	public boolean removeItem(int category, int idItem){
		NodeList x = doc.getElementsByTagName("category");
		for (int i = 0; i < x.getLength(); i++) {
			Node e = ((Element) x.item(i)).getElementsByTagName("id").item(0);
			
			if (Integer.valueOf(e.getTextContent()) == category) {
				NodeList items = ((Element)x.item(i)).getElementsByTagName("item");
				for(int j=0; j< items.getLength(); j++){
					int value = Integer.valueOf(((Element)items.item(j)).getElementsByTagName("id").item(0).getTextContent());
					if(value == idItem)
						items.item(j).getParentNode().removeChild(items.item(j));	
				}
			}
		}
		updateDocument(doc);
		
		
		return true;
	}

	@Override
	public boolean addItem(int categoryId, Item c) {
		NodeList x = doc.getElementsByTagName("category");
		int maxID = 0;
		Element category = null;
		for(int i=0; i< x.getLength();i++){
			if(Integer.valueOf(((Element)x.item(i)).getElementsByTagName("id").item(0).getTextContent()) == categoryId){
				category = (Element)x.item(i);
				NodeList items = ((Element)x.item(i)).getElementsByTagName("item");
				for(int j=0; j< items.getLength(); j++){
					if(Integer.valueOf(((Element)items.item(j)).getElementsByTagName("id").item(0).getTextContent()) >maxID){
						maxID = Integer.valueOf(((Element)items.item(j)).getElementsByTagName("id").item(0).getTextContent());
					}
				}
			}
			
		}
		Element newItem = doc.createElement("item");
		if(!c.isVisible()){
			newItem.setAttribute("show", "false");
		}
		Element id = doc.createElement("id");
		id.setTextContent(String.valueOf(++maxID));
		newItem.appendChild(id);
		
		Element title = doc.createElement("title");
		title.setTextContent(c.getTitle());
		newItem.appendChild(title);
		
		Element description = doc.createElement("description");
		description.setTextContent(c.getDescription());
		newItem.appendChild(description);
		
		Element price = doc.createElement("price");
		price.setTextContent(String.valueOf(c.getPrice()));
		newItem.appendChild(price);
		
		Element available = doc.createElement("available");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String reportDate = df.format(c.getDate());
		available.setTextContent(reportDate);
		newItem.appendChild(available);
		
		Element image = doc.createElement("image");
		image.setAttribute("src", c.getImgSrc());
		newItem.appendChild(image);
		
		Element currency = doc.createElement("currency");
		
		Element toEur = doc.createElement("toEUR");
		toEur.setTextContent(String.valueOf(c.getCurrency().getToEur()));
		Element toDollar = doc.createElement("toDollar");
		toDollar.setTextContent(String.valueOf(c.getCurrency().getToDollar()));
		currency.appendChild(toEur);
		currency.appendChild(toDollar);
		
		newItem.appendChild(currency);
		
		
		category.appendChild(newItem);
		updateDocument(doc);
		return true;
	}
	
	@Override
	public boolean addItem(int categoryId,int itemId, Item c) {
		NodeList x = doc.getElementsByTagName("category");
		
		Element category = null;
		for(int i=0; i< x.getLength();i++){
			if(Integer.valueOf(((Element)x.item(i)).getElementsByTagName("id").item(0).getTextContent()) == categoryId)
				category = (Element)x.item(i);
		}
		Element newItem = doc.createElement("item");
		if(!c.isVisible()){
			newItem.setAttribute("show", "false");
		}
		Element id = doc.createElement("id");
		id.setTextContent(String.valueOf(itemId));
		newItem.appendChild(id);
		Element title = doc.createElement("title");
		title.setTextContent(c.getTitle());
		newItem.appendChild(title);
		Element description = doc.createElement("description");
		description.setTextContent(c.getDescription());
		newItem.appendChild(description);
		Element price = doc.createElement("price");
		price.setTextContent(String.valueOf(c.getPrice()));
		newItem.appendChild(price);
		Element available = doc.createElement("available");
		System.out.println(c.getDate().toString());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String reportDate = df.format(c.getDate());
		available.setTextContent(reportDate);
		newItem.appendChild(available);
		Element image = doc.createElement("image");
		image.setAttribute("src", c.getImgSrc());
		newItem.appendChild(image);
		Element currency = doc.createElement("currency");
		
		Element toEur = doc.createElement("toEur");
		toEur.setTextContent(String.valueOf(c.getCurrency().getToEur()));
		Element toDollar = doc.createElement("toDollar");
		toDollar.setTextContent(String.valueOf(c.getCurrency().getToDollar()));
		currency.appendChild(toEur);
		currency.appendChild(toDollar);
		newItem.appendChild(currency);
		
		category.appendChild(newItem);
		updateDocument(doc);
		return true;
	}
	
	
}
