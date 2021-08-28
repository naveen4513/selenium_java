package office.sirion.util;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.*;

public class BPA_CPA_Integration {

    public static org.apache.logging.log4j.Logger Logger = null;
    public static HashMap<String,String> IntegrationID;
    public static String OracleID;

    public static void endUserLogin() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//Drivers//chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://rbi.prod.sirioncloud.io");
        Thread.sleep(7000);
        driver.findElement(By.xpath("//*[@id='user']")).sendKeys("test.integration");
        driver.findElement(By.xpath("//*[@id='p1']")).sendKeys("hello@123");
        driver.findElement(By.xpath("//*[@id='loginButton']")).click();

        List<String> Contract = new ArrayList<String>();
        IntegrationID=new HashMap<String,String>();
        for(int i= 0; i<10;i++) {

            driver.get("https://rbi.prod.sirioncloud.io/#/show/tblcontracts/4869");

            driver.findElement(By.xpath("//button[contains(text(),'Clone')]")).click();
            Thread.sleep(9000);
            driver.findElement(By.xpath("//input[@name='title']")).clear();
            driver.findElement(By.xpath("//input[@name='title']")).sendKeys("BPA CPA Integration Test_" + new Date().getTime());
            driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Save')]")).click();
            Thread.sleep(5000);
            String ContractID = driver.findElement(By.xpath("//a[@id='hrefElemId']")).getText();
            Contract.add(ContractID);
            driver.findElement(By.xpath("//a[@id='hrefElemId']")).click();
            Thread.sleep(8000);
            driver.findElement(By.xpath("//button[contains(text(),'Active')]")).click();
            Thread.sleep(8000);
        }

        for (String temp : Contract) {
            driver.findElement(By.xpath("//input[@id='searchtext']")).sendKeys(temp);
            driver.findElement(By.xpath("//input[@id='searchtext']")).sendKeys(Keys.ENTER);
            Thread.sleep(9000);
            OracleID = driver.findElement(By.xpath("//span[@id='elem_103524']/span")).getText();
            IntegrationID.put(temp,OracleID);

        }

        for(Map.Entry m:IntegrationID.entrySet()){
            System.out.println(m.getKey()+" - "+m.getValue());
        }
    }

    public static void main(String args[]) throws InterruptedException {
        endUserLogin();
    }
}
