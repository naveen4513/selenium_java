package office.sirion.suite.entityShow;

import java.io.IOException;
import java.util.List;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class EUProduction extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(entity_show_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(entity_show_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void USSandbox (String testCaseID, String entity) 
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
		// Opening VH show page  
	    if (entity.equalsIgnoreCase("Vendor Hierarchy")) {
		fluentWaitMethod(locateElementBy("vh_quick_link"));
		locateElementBy("vh_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
	    fluentWaitMethod(locateElementBy("vh_show_page_id"));
        String entityShowPageID = locateElementBy("vh_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Vendor Hierarchy");
        System.out.println("Show page open successfully for entity -- Vendor Hierarchy -- " +entityListingID);
		}
	    
	    
		// Opening Supplier show page
	    if (entity.equalsIgnoreCase("Supplier")) {
		fluentWaitMethod(locateElementBy("suppliers_quick_link"));
		locateElementBy("suppliers_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("supplier_show_page_id"));
        String entityShowPageID = locateElementBy("supplier_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Supplier");
        System.out.println("Show page open successfully for entity -- Supplier -- " +entityListingID);
		}

	
		// Opening contract show page
	    if (entity.equalsIgnoreCase("Contract")) {
		fluentWaitMethod(locateElementBy("contracts_quick_link"));
		locateElementBy("contracts_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("co_show_page_id"));
        String entityShowPageID = locateElementBy("co_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Contract");
        System.out.println("Show page open successfully for entity -- Contract -- " +entityListingID);
		}	    
	    
		// Opening obligation show page
	    if (entity.equalsIgnoreCase("Obligation")) {
		fluentWaitMethod(locateElementBy("ob_quick_link"));
		locateElementBy("ob_quick_link").click();
		fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));
		driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();
		
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
	    fluentWaitMethod(locateElementBy("ob_show_page_id"));
        String entityShowPageID = locateElementBy("ob_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Obligation");
        System.out.println("Show page open successfully for entity -- Obligation -- " +entityListingID);
		}
		
		// Opening child obligation show page
	    if (entity.equalsIgnoreCase("Child Obligation")) {
		fluentWaitMethod(locateElementBy("ob_quick_link"));
		locateElementBy("ob_quick_link").click();
		
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
	    fluentWaitMethod(locateElementBy("cob_show_page_id"));
        String entityShowPageID = locateElementBy("cob_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Child Obligation");
        System.out.println("Show page open successfully for entity -- Child Obligation -- " +entityListingID);
		}	       
		
	    // Opening Service Level show page
	    if (entity.equalsIgnoreCase("Service Level")) {
		fluentWaitMethod(locateElementBy("sl_quick_link"));
		locateElementBy("sl_quick_link").click();
				
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("sl_show_page_id"));
        String entityShowPageID = locateElementBy("sl_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Service Level");
        System.out.println("Show page open successfully for entity -- Service Level -- " +entityListingID);
		}	
	    
	    
	    // Opening Child Service Level show page -- need to correct sl show id
	    if (entity.equalsIgnoreCase("Child Service Level")) {
		fluentWaitMethod(locateElementBy("sl_quick_link"));
		locateElementBy("sl_quick_link").click();
		
	    fluentWaitMethod(locateElementBy("csl_link_listing_page"));
	    locateElementBy("csl_link_listing_page").click();				
		
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
	    fluentWaitMethod(locateElementBy("csl_show_page_id"));
        String entityShowPageID = locateElementBy("csl_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Child Service Level");
        System.out.println("Show page open successfully for entity -- Child Service Level -- " +entityListingID);
		} 	    
	    
		// Opening Action show page
	    if (entity.equalsIgnoreCase("Action")) {
		fluentWaitMethod(locateElementBy("actions_quick_link"));
		locateElementBy("actions_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("actions_show_page_id"));
        String entityShowPageID = locateElementBy("actions_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Action");
        System.out.println("Show page open successfully for entity -- Action -- " +entityListingID);
		}	    
	    
		// Opening Issue show page
	    if (entity.equalsIgnoreCase("Issue")) {
		fluentWaitMethod(locateElementBy("issues_quick_link"));
		locateElementBy("issues_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("issue_show_page_id"));
        String entityShowPageID = locateElementBy("issue_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Issue");
        System.out.println("Show page open successfully for entity -- Issue -- " +entityListingID);
		}	   
	    	    
		// Opening Dispute show page
	    if (entity.equalsIgnoreCase("Dispute")) {
		fluentWaitMethod(locateElementBy("disputes_quick_link"));
		locateElementBy("disputes_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("dispute_show_page_issue_id"));
        String entityShowPageID = locateElementBy("dispute_show_page_issue_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Dispute");
        System.out.println("Show page open successfully for entity -- Dispute -- " +entityListingID);
		}
	    	    
		// Opening Change Request show page
	    if (entity.equalsIgnoreCase("Change Request")) {
		fluentWaitMethod(locateElementBy("cr_quick_link"));
		locateElementBy("cr_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
	    fluentWaitMethod(locateElementBy("cr_show_page_id"));
        String entityShowPageID = locateElementBy("cr_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Change Request");
        System.out.println("Show page open successfully for entity -- Change Request -- " +entityListingID);
		}
	    	    
		// Opening Interpretation show page
	    if (entity.equalsIgnoreCase("Interpretation")) {
		fluentWaitMethod(locateElementBy("ip_quick_link"));
		locateElementBy("ip_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
	    fluentWaitMethod(locateElementBy("ip_show_page_id"));
        String entityShowPageID = locateElementBy("ip_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Interpretation");
        System.out.println("Show page open successfully for entity -- Interpretation -- " +entityListingID);
		}
	    	    
		// Opening WOR show page
	    if (entity.equalsIgnoreCase("Work Order Request")) {
		fluentWaitMethod(locateElementBy("wor_quick_link"));
		locateElementBy("wor_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
	    fluentWaitMethod(locateElementBy("wor_show_page_id"));
        String entityShowPageID = locateElementBy("wor_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Work Order Request");
        System.out.println("Show page open successfully for entity -- Work Order Request -- " +entityListingID);
		}
	    
		// Opening GB show page
	    if (entity.equalsIgnoreCase("Governance Body")) {
		fluentWaitMethod(locateElementBy("gb_quick_link"));
		locateElementBy("gb_quick_link").click();
		fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODY")));

		driver.findElement(By.linkText("VIEW GOVERNANCE BODY")).click();	
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
	    fluentWaitMethod(locateElementBy("gb_show_page_id"));
        String entityShowPageID = locateElementBy("gb_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Governance Body");
        System.out.println("Show page open successfully for entity -- Governance Body -- " +entityListingID);
		}
	    
		// Opening Child GB show page
	    if (entity.equalsIgnoreCase("Governance Body Meeting")) {
		fluentWaitMethod(locateElementBy("gb_quick_link"));
		locateElementBy("gb_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("cgb_show_page_id"));
        String entityShowPageID = locateElementBy("cgb_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Governance Body -- " +entityListingID);
		}
	    
		// Opening Invoice show page
	    if (entity.equalsIgnoreCase("Invoice")) {
		fluentWaitMethod(locateElementBy("inv_quick_link"));
		locateElementBy("inv_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("inv_show_page_id"));
        String entityShowPageID = locateElementBy("inv_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Invoice");
        System.out.println("Show page open successfully for entity -- Invoice -- " +entityListingID);  
	    }
	    
		// Opening Invoice Line Item show page
	    if (entity.equalsIgnoreCase("Invoice Line Item")) {
		fluentWaitMethod(locateElementBy("inv_quick_link"));
		locateElementBy("inv_quick_link").click();
		
		fluentWaitMethod(driver.findElement(By.linkText("View Invoice Line Items")));
		driver.findElement(By.linkText("View Invoice Line Items")).click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("inv_line_item_show_page_id"));
        String entityShowPageID = locateElementBy("inv_line_item_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Invoice Line Item");
        System.out.println("Show page open successfully for entity -- Invoice Line Item -- " +entityListingID);
		}
	  
		// Opening Service Data show page
	    if (entity.equalsIgnoreCase("Service Data")) {
		fluentWaitMethod(locateElementBy("sd_quick_link"));
		locateElementBy("sd_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("sd_show_page_id"));
        String entityShowPageID = locateElementBy("sd_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Service Data");
        System.out.println("Show page open successfully for entity -- Service Data -- " +entityListingID);
		}
	    
		// Opening Consumption show page
	    if (entity.equalsIgnoreCase("Consumption")) {
		fluentWaitMethod(locateElementBy("sd_quick_link"));
		locateElementBy("sd_quick_link").click();
		
		fluentWaitMethod(driver.findElement(By.linkText("View Consumptions")));
		driver.findElement(By.linkText("View Consumptions")).click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a")).click();
		
	    fluentWaitMethod(locateElementBy("consumption_show_page_id"));
        String entityShowPageID = locateElementBy("consumption_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Consumption");
        System.out.println("Show page open successfully for entity -- Consumption --" +entityListingID);
		}
	    
		// Opening Contract Draft Request show page
	    if (entity.equalsIgnoreCase("Contract Draft Request")) {
		fluentWaitMethod(locateElementBy("cdr_quick_link"));
		locateElementBy("cdr_quick_link").click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
	    fluentWaitMethod(locateElementBy("cdr_show_page_id"));
        String entityShowPageID = locateElementBy("cdr_show_page_id").getText();
        Assert.assertEquals(entityShowPageID, entityListingID);
     
        Logger.debug("Show page open successfully for entity -- Contract Draft Request");
        System.out.println("Show page open successfully for entity -- Contract Draft Request --" +entityListingID);
		}
	    
		// Opening Contract Template show page
	    if (entity.equalsIgnoreCase("Contract Template")) {
		fluentWaitMethod(locateElementBy("cdr_quick_link"));
		locateElementBy("cdr_quick_link").click();
		
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[3]/ul[2]/li[1]/span[1]")).click();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[3]/ul[2]/li[1]/ul[1]/li[4]/a")).click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
		// CLick on gereral tab
		 fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));
		 driver.findElement(By.linkText("GENERAL")).click();
		
			fluentWaitMethod(locateElementBy("contract_template_show_page_id"));
			String entityShowPageID = locateElementBy("contract_template_show_page_id").getText();

        Assert.assertEquals(entityShowPageID, entityListingID);     
        Logger.debug("Show page open successfully for entity -- Contract Template");
        System.out.println("Show page open successfully for entity -- Contract Template --" +entityListingID);
		}
	    
		// Opening Template Structure show page
	    if (entity.equalsIgnoreCase("Template Structure")) {
		fluentWaitMethod(locateElementBy("cdr_quick_link"));
		locateElementBy("cdr_quick_link").click();
		
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[3]/ul[2]/li[1]/span[1]")).click();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[3]/ul[2]/li[1]/ul[1]/li[4]/a")).click();
		
		fluentWaitMethod(driver.findElement(By.linkText("VIEW TEMPLATE STRUCTURE")));
		driver.findElement(By.linkText("VIEW TEMPLATE STRUCTURE")).click();
		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		String entityListingID = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).getText();
		driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[3]/div[7]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a")).click();
		
	
			fluentWaitMethod(locateElementBy("template_structure_show_page_id"));
			String entityShowPageID = locateElementBy("template_structure_show_page_id").getText();

        Assert.assertEquals(entityShowPageID, entityListingID);     
        Logger.debug("Show page open successfully for entity -- Template Structure");
        System.out.println("Show page open successfully for entity -- Template Structure --" +entityListingID);
		}

        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(entity_show_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(entity_show_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(entity_show_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
			if (!testCaseID.equalsIgnoreCase(""))
				TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
			} catch (Exception e) {
				
				}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(entity_show_suite_xls, "Test Cases", TestUtil.getRowNum(entity_show_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(entity_show_suite_xls, "Test Cases", TestUtil.getRowNum(entity_show_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(entity_show_suite_xls, this.getClass().getSimpleName());
		}
	}
