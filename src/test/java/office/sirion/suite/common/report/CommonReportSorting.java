package office.sirion.suite.common.report;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;

import org.apache.commons.lang3.StringUtils;
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

public class CommonReportSorting extends TestSuiteBase {
	String result = null;
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	static LinkedList<String> listExpected;
	static LinkedList<String> listActual;
	static int resultCount = 0;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(common_report_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_report_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void CommonReportOpenTest (String testCaseID, String entityName, String entityTypeID, String entityReportName, String entityReportID
			)
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + entityReportName + " is set to NO " +count);
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Report Sorting for -- " + entityReportName);

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
		fluentWaitMethod(driver.findElement(By.id("header")).findElement(By.linkText("Reports")));

		driver.findElement(By.id("header")).findElement(By.linkText("Reports")).click();
		//
		fluentWaitMethod(driver.findElement(By.id("listTd1")).findElement(By.id("allLists1")));

		String entityNameAtDropdown = driver.findElement(By.xpath(".//*[@id='allLists1']/option[@value='"+convertStringToInteger(entityTypeID.trim())+"']")).getText();

		new Select(driver.findElement(By.id("listTd1")).findElement(By.id("allLists1"))).selectByVisibleText(entityNameAtDropdown);
	    //
		fluentWaitMethod(driver.findElement(By.id("listTd2")).findElement(By.id("allLists2")));
		
		String entityReportNameAtDropdown = driver.findElement(By.xpath(".//*[@id='allLists2']/option[@value='"+convertStringToInteger(entityReportID.trim())+"']")).getText();

		new Select(driver.findElement(By.id("listTd2")).findElement(By.id("allLists2"))).selectByVisibleText(entityReportNameAtDropdown);
	    //
		fluentWaitMethod(driver.findElement(By.id("downloadXls")));

	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Report Page of -- " + entityReportName + " -- opened Successfully");
	    
	    // Check for Listing Data
	    if (CommonTestFiltering.checkTableDataPresence()) {
	    	Logger.info("For " +entityName+ " --- Listing there is No Corresponding Data Available");
	    	
	    	fluentWaitMethod(driver.findElement(By.id("cr")));
	    	driver.findElement(By.id("cr"));

	    	String dataTableInfo = 	driver.findElement(By.id("cr_wrapper")).findElement(By.className("dataTables_info")).getText();
			String totalEntries = StringUtils.substringBetween(dataTableInfo, "of ", " entries");

	    	if (!totalEntries.equalsIgnoreCase("0"))
		    	org.testng.Assert.fail("For -- " + entityReportName + " --- Listing Page Data is not getting Populated");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }

	    driver.findElement(By.cssSelector("div.ColVis.TableTools")).findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_MasterButton")).click();
	    Thread.sleep(2000);
	    
	    List<WebElement> columnElementList = driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchableConfig"))
	            .findElements(By.cssSelector("button.ColVis_Button.TableTools_Button"));

	    int columnCount = columnElementList.size()-2;
	    Logger.info("Number of Columns Present under -- " + entityReportName + " = " + columnCount);
	    
	    int selectionSize = Integer.parseInt((CONFIG.getProperty("listingColumnCount")));
	    
	    int totalIterations = columnCount/selectionSize;
	    
	    if(columnCount%selectionSize != 0)
	    	totalIterations++;

	    for(int itr=1; itr <= totalIterations; itr++) {
	    	int fromIndex = (itr-1)*selectionSize;
	    	int toIndex = itr*selectionSize;
	    	
	    	if(toIndex >= columnCount)
	    		toIndex = columnCount;

		    List<WebElement> columnElementBeforeList = driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchableConfig"))
		            .findElements(By.cssSelector("button.ColVis_Button.TableTools_Button"));

	    	List<WebElement> columnSelection = columnElementBeforeList.subList(fromIndex, toIndex);
	    	
	    	for (int itrC=0; itrC<columnSelection.size(); itrC++) {
	    		 if (!columnSelection.get(itrC).findElement(By.tagName("input")).isSelected())
	    			 columnSelection.get(itrC).findElement(By.tagName("input")).click();
	    		 }
	    	
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
	    		
	    		Logger.info("Sorting Status At -- " + i + " -- " + columnHeader[0] + " = " +String.valueOf(result).toUpperCase());
	    		
	    		if (!result) {
	    			resultCount++;
	    			Logger.error("Sorting on Column Header --- " + columnHeader[0] + " = " +String.valueOf(result).toUpperCase());
	    			}
	    		
	    		listExpected.clear();
	    		listActual.clear();
		    	}
	    	
		    driver.navigate().refresh();
			//
			fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

		    driver.findElement(By.cssSelector("div.ColVis.TableTools")).findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_MasterButton")).click();
		    Thread.sleep(2000);

		    List<WebElement> columnElementAfterList = driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchableConfig"))
		            .findElements(By.cssSelector("button.ColVis_Button.TableTools_Button"));

	    	List<WebElement> columnSelectionList = columnElementAfterList.subList(fromIndex, toIndex);

	    	for (int itrC=0; itrC<columnSelectionList.size(); itrC++) {
	    		 if (columnSelectionList.get(itrC).findElement(By.tagName("input")).isSelected())
	    			 columnSelectionList.get(itrC).findElement(By.tagName("input")).click();
	    		 }
	    	}
	    
	    if (resultCount > 0)
	    	Assert.fail("Sorting of following Report is FAILED --- " + entityName);
	    else
	    	Logger.debug("Sorting of following Report is PASSED --- " + entityName);
	    	
		driver.get(CONFIG.getProperty("endUserURL"));
	    }
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}

		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_report_suite_xls, this.getClass().getSimpleName());
	}
}