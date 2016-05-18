package ecommerce_2016_1610889.data;

import ecommerce_2016_1610889.db.Connector;
import ecommerce_2016_1610889.db.PostgresDb;
import ecommerce_2016_1610889.db.XmlDb;

public class DataLayerFactory {

	public DataLayer getDataBase(Connector c){
		
		if(!c.isXmlMode()){
			return PostgresDb.getInstance(c);	
		}
			return 	XmlDb.getXMLdb(c);

	}
	
}


