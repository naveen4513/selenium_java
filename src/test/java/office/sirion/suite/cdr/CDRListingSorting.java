package office.sirion.suite.cdr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

public class CDRListingSorting extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	static LinkedList<String> listExpected;
	static LinkedList<String> listActual;
	static int resultCount = 0;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(cdr_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cdr_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CDRListingSortingTest (
			) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data CDR set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Listing Sorting of CONTRACT DRAFT REQUEST");
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
	    fluentWaitMethod(locateElementBy("cdr_quick_link"));

	    locateElementBy("cdr_quick_link").click();
	    //

	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Listing Page of -- CONTRACT DRAFT REQUEST -- opened Successfully");

	    String listingNameShowPage = driver.findElement(By.cssSelector("span.name.ng-scope")).getText();
	    Logger.info("CONTRACT DRAFT REQUEST -- Listing Show Page Name is -- " +listingNameShowPage);
	    
	    // Check for Listing Data
	    if (CommonTestFiltering.checkTableDataPresence()) {
	    	Logger.info("For CONTRACT DRAFT REQUEST -- Listing there is No Corresponding Data Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
	    
	    String dataTableInfo = 	driver.findElement(By.id("cr_wrapper")).findElement(By.className("dataTables_info")).getText();
	    String totalEntries = StringUtils.substringBetween(dataTableInfo, "of ", " entries");

	    Logger.debug("Total Number of Entries Present under Listing -- CONTRACT DRAFT REQUEST -- is = " + totalEntries);

	    String[] downloadStyleAttribute = driver.findElement(By.id("exportXLdownload")).getAttribute("style").split(Pattern.quote(": "));
	    String[] downloadDisplayAttribute= downloadStyleAttribute[3].split(Pattern.quote(";"));

	    if (downloadDisplayAttribute[0].trim().equalsIgnoreCase("list-item")) {
	    	File file = new File(System.getProperty("user.home")+"\\Downloads\\"+listingNameShowPage+".xlsx");

	    	if (file.exists())
	    		file.delete();

	    	// Click on Download and then Select All Columns Link
	    	new Actions(driver).moveToElement(driver.findElement(By.id("exportXLdownload"))).clickAndHold().build().perform();
	    	driver.findElement(By.id("selectedColumnsDownload")).click();
	    	//

	    	if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
	    		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

	    	// Moving of Downloaded File to Project Location
	    	if (file.exists()) {
	    		File selectAllColumnsFolder = new File(System.getProperty("user.dir")+"//Downloaded Data//"+ new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()) + "//Common Listing//Selected Columns");
	    		File selectAllColumnsFile = new File(selectAllColumnsFolder + "//" + listingNameShowPage +".xlsx");

	    		if (selectAllColumnsFile.exists())
	    			selectAllColumnsFile.delete();

	    		try {
					FileUtils.moveFile(file, selectAllColumnsFile);

					FileInputStream selectAllColumnsInputStream = new FileInputStream(selectAllColumnsFile);
		    		@SuppressWarnings("resource")
					XSSFWorkbook selectAllColumnsWorkbook = new XSSFWorkbook(selectAllColumnsInputStream);

		    		Assert.assertEquals(selectAllColumnsWorkbook.getSheetName(0), "Data");
		    		selectAllColumnsInputStream.close();
		    		} catch (IOException e) {
		    			e.printStackTrace();
						}
	    			}

	    		// Clicking on Download and then All Columns Link
	    		new Actions(driver).moveToElement(driver.findElement(By.id("exportXLdownload"))).clickAndHold().build().perform();
	    		driver.findElement(By.id("exportXL")).click();
	    		//
	    				
	    		if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
	    			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

	    		if (file.exists()) {
	    			File allColumnsFolder = new File(System.getProperty("user.dir")+"//Downloaded Data//"+ new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()) + "//Common Listing//All Columns");
	    			File allColumnsFile = new File(allColumnsFolder + "//" + listingNameShowPage +".xlsx");
				    
	    			if (allColumnsFile.exists())
	    				allColumnsFile.delete();
				    	
	    			try {
						FileUtils.moveFile(file, allColumnsFile);
		    			FileInputStream allColumnsInputStream = new FileInputStream(allColumnsFile);
		    			@SuppressWarnings("resource")
						XSSFWorkbook allColumnsWorkbook = new XSSFWorkbook(allColumnsInputStream);

		    			Assert.assertEquals(allColumnsWorkbook.getSheetName(0), "Data");

		    			allColumnsInputStream.close();
		    			} catch (IOException e) {
		    				e.printStackTrace();
		    			}
	    			}
	    		Logger.debug("Listing Page Downloaded Successfully for -- CONTRACT DRAFT REQUEST");
	    		}
	    
	    // Clicking on Columns Button
	    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("columnsList")).click();
	    //

	    List<WebElement> columnElementList = driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchable"))
	            .findElements(By.cssSelector("button.ColVis_Button.TableTools_Button"));

	    int columnCount = columnElementList.size()-2;
    	Logger.info("For CONTRACT DRAFT REQUEST -- Total Columns = " + columnCount);
	    
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
	    	Thread.sleep(2000);

	    	new Actions(driver).moveToElement(locateElementBy("entity_listing_page_display_dropdown_link")).build().perform();
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
	    		Logger.debug("For Column -- " + i + " -- with -- " + columnHeader[0] + " -- is -- " +String.valueOf(result).toUpperCase());
	    		
	    		if (!result) {
	    			resultCount++;
	    			}
	    		
	    		listExpected.clear();
	    		listActual.clear();
			    //
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
	    	Assert.fail("Sorting of CONTRACT DRAFT REQUEST is Failed");
	    else
	    	Logger.debug("Sorting of CONTRACT DRAFT REQUEST is Completed");
	    	
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(cdr_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(cdr_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}

		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(cdr_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(cdr_suite_xls, "Test Cases", TestUtil.getRowNum(cdr_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(cdr_suite_xls, "Test Cases", TestUtil.getRowNum(cdr_suite_xls, this.getClass().getSimpleName()), "FAIL");
			}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(cdr_suite_xls, this.getClass().getSimpleName());
		}
	}