package office.sirion.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class ConfigMigrator {
	
	static String InstanceName = "USSANDBOX";
	static String oldPropertyFileName = "configuration_ussdbox_app.properties";
	
    public static void main(String args[]) throws Exception{
        
    	
    	
        Properties oldProps = new Properties();
        oldProps.load(new FileInputStream("D:\\SeleniumAutomation\\PropertiesFiles\\"+oldPropertyFileName));

        Properties newOldMap = new Properties();
        newOldMap.load(new FileInputStream("D:\\SeleniumAutomation\\mapper.csv"));

        Properties newProp = new Properties();

        Enumeration<Object> newKeys = newOldMap.keys();
        while (newKeys.hasMoreElements()){
            String newKey = (String) newKeys.nextElement();
            String oldKey = newOldMap.getProperty(newKey);
            if (oldKey == null || oldKey.length() == 0){
                oldKey = newKey;
            }
            if (oldProps.getProperty(oldKey) == null){
                System.out.println("Mapping not found for "+oldKey);
            }
            else {
                newProp.setProperty(newKey,oldProps.getProperty(oldKey));
                
            }
        }

        
        OutputStream newConfFile = new FileOutputStream("D:\\SeleniumAutomation\\PropertiesFiles\\Updated-"+InstanceName+".properties");
        newProp.store(newConfFile, null);
        System.out.println("New configuration saved at : " + newConfFile.toString());
    }
}
