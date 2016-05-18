package ecommerce_2016_1610889.data;

public class User {
	
	private String name;
	private String username;
	private String password;
	private String surname;
	private String accountType;
	
	
	public void setName(String name) {
		this.name = name;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public User(){
		
	};
	public User(String username, String name, String surname,String accountType){
		this.username = username;
		this.name= name;
		this.surname=surname;
		this.accountType= accountType;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getAccountType(){
		return accountType;
	}
	public String getUsername(){
		return username;
	}
	public String getName(){
		return name;
	}
	public String getSurname(){
		return surname;
	}
	

}
