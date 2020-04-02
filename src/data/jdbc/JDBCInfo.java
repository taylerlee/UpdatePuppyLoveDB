package data.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class JDBCInfo {
	private Properties p;

	   public JDBCInfo(String xf) {
	      FileInputStream fis = null;
	      p = new Properties();
	      try {
	         fis = new FileInputStream(xf);
	         p.loadFromXML(fis);
	         fis.close();
	      } catch (IOException e) {
	         System.out.println("XML file :"+ xf + "\n" + e);
	         System.exit(1);
	      }   
	   }
	   
	   public String getDBServerIP() {
		   return p.getProperty("dbserverip");
	   }
	   
	   public int getDBServerPort() {
		   try {
			   Integer.parseInt(p.getProperty("dbserverport"));
		   }
		   catch (NumberFormatException e) {
			   return 1433;
		   }
		   return Integer.parseInt(p.getProperty("dbserverport"));
	   }
	   
	   public String getDBUserName() {
		   return p.getProperty("dbusername");
	   }
	   
	   public String getDBPassword() {
		   return p.getProperty("dbpassword");
	   }
	   
	   public String getDriver() {
		      return p.getProperty("driver");
	   }
	   
	   public String getImageDir() {
		      return p.getProperty("imagedir");
	   }
	   
	   public String getDatabase() {
		      return p.getProperty("database");
	   }
}
