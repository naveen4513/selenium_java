package office.sirion.suite.common.listing;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class CommonListingSorting extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	static LinkedList<String> listExpected;
	static LinkedList<String> listActual;
	static int resultCount = 0;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(common_listing_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_listing_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ListingSortingTest (String testCaseID, String entityName
			) throws InterruptedException, TestLinkAPIException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +entityName +" set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Listing Page Sorting of ---- " + entityName);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    
	    if (entityName.equalsIgnoreCase("Vendor Hierarchy")) {
		    fluentWaitMethod(locateElementBy("vh_quick_link"));
		    //
		    
		    locateElementBy("vh_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Suppliers")) {
		    fluentWaitMethod(locateElementBy("suppliers_quick_link"));
		    //
		    
		    locateElementBy("suppliers_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Contracts")) {
		    fluentWaitMethod(locateElementBy("contracts_quick_link"));
		    //
		    
		    locateElementBy("contracts_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Service Levels")) {
		    fluentWaitMethod(locateElementBy("sl_quick_link"));
		    //
		    
		    locateElementBy("sl_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Child Service Levels")) {
		    fluentWaitMethod(locateElementBy("sl_quick_link"));
		    //

		    locateElementBy("sl_quick_link").click();
			//

		    fluentWaitMethod(driver.findElement(By.linkText("View Child Service Levels")));
		    //

		    driver.findElement(By.linkText("View Child Service Levels")).click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Obligations")) {
		    fluentWaitMethod(locateElementBy("ob_quick_link"));
		    //

		    locateElementBy("ob_quick_link").click();
			//

		    fluentWaitMethod(driver.findElement(By.linkText("View Obligations")));
		    //

		    driver.findElement(By.linkText("View Obligations")).click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Child Obligations")) {
		    fluentWaitMethod(locateElementBy("ob_quick_link"));
		    //
		    
		    locateElementBy("ob_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Governance Body")) {
		    fluentWaitMethod(locateElementBy("gb_quick_link"));
		    //
		    
		    locateElementBy("gb_quick_link").click();
			//
		    
		    fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODIES")));
		    //

		    driver.findElement(By.linkText("VIEW GOVERNANCE BODIES")).click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Governance Body Meeting")) {
		    fluentWaitMethod(locateElementBy("gb_quick_link"));
		    //
		    
		    locateElementBy("gb_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Actions")) {
		    fluentWaitMethod(locateElementBy("actions_quick_link"));
		    //
		    
		    locateElementBy("actions_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Issues")) {
		    fluentWaitMethod(locateElementBy("issues_quick_link"));
		    //
		    
		    locateElementBy("issues_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Change Requests")) {
		    fluentWaitMethod(locateElementBy("cr_quick_link"));
		    //
		    
		    locateElementBy("cr_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Interpretations")) {
		    fluentWaitMethod(locateElementBy("ip_quick_link"));
		    //
		    
		    locateElementBy("ip_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Disputes")) {
		    fluentWaitMethod(locateElementBy("disputes_quick_link"));
		    //
		    
		    locateElementBy("disputes_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Invoices")) {
		    fluentWaitMethod(locateElementBy("inv_quick_link"));
		    //
		    
		    locateElementBy("inv_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Purchase Order")) {
		    fluentWaitMethod(locateElementBy("po_quick_link"));
		    //
		    
		    locateElementBy("po_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Work Order Requests")) {
		    fluentWaitMethod(locateElementBy("wor_quick_link"));
		    //
		    
		    locateElementBy("wor_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Clause")) {
		    fluentWaitMethod(locateElementBy("cdr_quick_link"));
		    //

		    locateElementBy("cdr_quick_link").click();
		    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
		    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();
		    
		    driver.findElement(By.linkText("View Clause Library")).click();
		    //
			}
	    
	    else if (entityName.equalsIgnoreCase("Contract Template")) {
		    fluentWaitMethod(locateElementBy("cdr_quick_link"));
		    //
		    
		    locateElementBy("cdr_quick_link").click();
		    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
		    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();
		    
		    driver.findElement(By.linkText("View Contract Templates")).click();
		    //
			}

	    else if (entityName.equalsIgnoreCase("Contract Draft Request")) {
		    fluentWaitMethod(locateElementBy("cdr_quick_link"));
		    //
		    
		    locateElementBy("cdr_quick_link").click();
			//
			}

	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Listing Page of --- " +entityName+ " --- opened Successfully");
	    
	    // Check for Listing Data
	    if (CommonTestFiltering.checkTableDataPresence()) {
	    	Logger.info("For " +entityName+ " --- Listing there is No Corresponding Data Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
	    
	    // Clicking on Columns Button
	    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("columnsList")).click();
	    //

	    List<WebElement> columnElementList = driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchable"))
	            .findElements(By.cssSelector("button.ColVis_Button.TableTools_Button"));

	    int columnCount = columnElementList.size()-2;
	    Logger.info("Total Number of Columns Present under --- " + entityName +" --- Listing is --- " + columnCount);
	    
	    int selectionSize = Integer.parseInt((CONFIG.getProperty("listingColumnCount")));
	    
	    int totalIterations = columnCount/selectionSize;
	    
	    if(columnCount%selectionSize != 0)
	    	totalIterations++;
	    
	    for(int itr=1; itr <= totalIterations; itr++) {
	    	int fromIndex = (itr-1)*selectionSize;
	    	int toIndex = itr*selectionSize;
	    	
	    	if(toIndex >= columnCount)
	    		toIndex = columnCount;
	    	
	    	List<WebElement> columnSelection = columnElementList.subList(fromIndex, toIndex);
	    	
	    	for (int itrC=0; itrC<columnSelection.size(); itrC++) {
	    		 if (!columnSelection.get(itrC).findElement(By.tagName("input")).isSelected())
	    			 columnSelection.get(itrC).findElement(By.tagName("input")).click();
	    		 }
		    driver.findElement(By.cssSelector("div.ColVis_collectionBackground.TableTools_collectionBackground.opacityNone")).click();

		    fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
			new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
	    	//
	    		 
	    	// Collection of Column Data One by One
	    	for (int i = 0; i < columnSelection.size(); i++) {
	    		listExpected = TestSorting.getColumnData(i);

	    		WebElement e = driver.findElement(By.id("cr_paginate")).findElement(By.id("cr_first"));
	    		String[] str = e.getAttribute("class").split(" ");
	    		        
	    		if (str.length == 2) {
	    			e.click();
	    			//
	    			fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
	    			}

	    		List<WebElement> rowHeader = driver.findElement(By.className("dataTables_scrollHead")).findElement(By.tagName("tr")).findElements(By.tagName("th"));
	    		new Actions(driver).moveToElement(rowHeader.get(i)).build().perform();

	    		String[] columnHeader = rowHeader.get(i).getAttribute("aria-label").split(":");

	    		if (columnHeader[0].equalsIgnoreCase(""))
	    			continue;

	    		rowHeader.get(i).click();
	    		
	    		//
	    		fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
	    			      
	    		listActual = TestSorting.getColumnData(i);
	    		
	    		boolean result = TestSorting.isTableColumnSorted(listExpected, listActual);
	    		
	    		Logger.info("Sorting Status At index -- " + i + " -- " + columnHeader[0] + " -- is --- " +String.valueOf(result).toUpperCase());
	    		
	    		if (!result) {
	    			resultCount++;
	    			Logger.error("Sorting on Column Header --- " + columnHeader[0] + " --- has --- " +String.valueOf(result).toUpperCase());
	    			}
	    		
	    		listExpected.clear();
	    		listActual.clear();
		    	}

	    	driver.findElement(By.className("listingPanelLeft")).findElement(By.className("columnsList")).click();
		    Thread.sleep(2000);

	    	List<WebElement> columnSelectionList = columnElementList.subList(fromIndex, toIndex);

	    	for (int itrC=0; itrC<columnSelectionList.size(); itrC++) {
	    		 if (columnSelectionList.get(itrC).findElement(By.tagName("input")).isSelected())
	    			 columnSelectionList.get(itrC).findElement(By.tagName("input")).click();
	    		 }
	    	}
	    
	    if (resultCount > 0)
	    	Assert.fail("Sorting of following Entity is failed --- " + entityName);
	    else
	    	Logger.debug("Sorting of following Entity is passed --- " + entityName);
	    	
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}

		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls, this.getClass().getSimpleName()), "FAIL");
			}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_listing_suite_xls, this.getClass().getSimpleName());
		}
	}