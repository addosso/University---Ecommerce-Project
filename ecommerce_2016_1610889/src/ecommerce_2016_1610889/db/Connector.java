package ecommerce_2016_1610889.db;

public class Connector {
	
	private String urlXmlDocument;
	private boolean isXmlMode;
	
	private String dbName;
	private String dbUser;
	private String dbPassword;
	
	
	public String getUrlXmlDocument() {
		return urlXmlDocument;
	}
	public void setUrlXmlDocument(String urlXmlDocument) {
		this.urlXmlDocument = urlXmlDocument;
	}
	public boolean isXmlMode() {
		return isXmlMode;
	}
	public void setXmlMode(boolean isXmlMode) {
		this.isXmlMode = isXmlMode;
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	
	
	

}
