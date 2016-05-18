package ecommerce_2016_1610889.data;



public class Category implements Comparable<Category> {
	private int id;
	private String name;
	private String imageUrl;

	public int getId() {
		return id;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Category){
			return ((Category) obj).getId() == id; 
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public int compareTo(Category o) {
		int toRet =0;
		if(o.getId() < id) toRet=1;
		else if(o.getId() > id) toRet = -1;
		return toRet;
	}

}
