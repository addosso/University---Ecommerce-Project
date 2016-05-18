package ecommerce_2016_1610889.data;

import java.util.Date;

public class Item {
	
	private Category category;
	private Currency currency;
	private String title;
	private String imgSrc;
	private boolean visible;
	private int id;
	private Date date;
	private double price;
	private String description;
	
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return (id+"-"+category.getId()).equals(((Item) obj).getId()+"-"+((Item) obj).getCategory().getId());
	}
	
	public Item(){
		
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return currency.getName() != null && currency.getName().toLowerCase().equals("eur")? price : (price * currency.getToEur());
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
