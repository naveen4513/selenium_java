package office.sirion.util;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class columns_verification {
	public static WebDriver driver = null;
	public static WebDriverWait wait_in_report = null;
	static String[] commaSeparated = {"ID","Status","Title"};
	
	static List<WebElement> chkBox;
	static ArrayList<String> arrayList = new ArrayList<String>();
    static String [] finalcolumns;
	static int[] quicklinks = {1,2,3,4,5,6,8,9,10,11,12,211,212,251,265,271,273,279,286,318};
	public static void main(String[] args) throws InterruptedException {
		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait_in_report = new WebDriverWait(driver, 30);
		System.out.println("Implicitly again wait applied for 1 seconds for check test waiting");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("http://final.transformers.office");
		driver.findElement(By.xpath("//*[@id='user']")).sendKeys("abhi_rc");
		driver.findElement(By.xpath("//*[@id='p1']")).sendKeys("admin123");
		driver.findElement(By.xpath("//*[@id='loginButton']")).click();
		
		Thread.sleep(15000);
		for (int l=0; l<quicklinks.length;l++){
		driver.get("http://final.transformers.office/#/listRender/"+quicklinks[l]+"?contractId=&relationId=&vendorId=");
		Thread.sleep(25000);
		
		driver.findElement(By.xpath("//*[@id='preference']/ul[1]/li[2]")).click();
		//
		
		 chkBox = driver.findElements(By.cssSelector(".ColVis_collection.TableTools_collection.searchable input[type=checkbox]")); 
		for(int i=1; i<=chkBox.size(); i++){ 
			
			String columns = driver.findElement(By.xpath(".//*[@id='data-ng-app']//button["+i+"]/span/span[2]")).getAttribute("title");
			
			arrayList.add(columns);
		}
		
			finalcolumns = arrayList.toArray(new String [arrayList.size()]);
		
			for (int j = 0; j < commaSeparated.length; j++) {
				   for (int k = 0; k < finalcolumns.length; k++) {
				      if(commaSeparated[j].equalsIgnoreCase(finalcolumns[k])) {
				         System.out.println(quicklinks[l]+"-->"+commaSeparated[j]+ " Match found");
						}
				   }
			}
			
		}
	}
}
