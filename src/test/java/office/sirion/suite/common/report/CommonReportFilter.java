package office.sirion.suite.common.report;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class CommonReportFilter extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;
	static LinkedList<String> listExpected;
	int resultCount;
	int filterTestStatus;
	public static List<WebElement> filtersContent;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(common_report_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_report_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CommonListingFilterTest (String testCaseID, String entityName, String entityTypeID, String entityReportName, String entityReportID
			) throws InterruptedException, TestLinkAPIException, ParseException {
		
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + entityReportName + " is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Filter Test Case for -- " + entityReportName);
		
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
		Logger.debug("Executing Filter for Report Name -- " + entityReportNameAtDropdown);

		new Select(driver.findElement(By.id("listTd2")).findElement(By.id("allLists2"))).selectByVisibleText(entityReportNameAtDropdown);
	    //
		fluentWaitMethod(driver.findElement(By.id("downloadXls")));
		
//		String reportNameOnShowPage = driver.findElement(By.id("downloadXls")).getText();
	    
	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Report Page of -- " + entityReportNameAtDropdown + " -- opened Successfully");
	    
	    // Check for Listing Data
	    if (CommonTestFiltering.checkTableDataPresence()) {
	    	Logger.info("For -- " + entityReportNameAtDropdown + " -- Listing there is No Corresponding Data Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
	    
	    int filtersCount = TestFiltering.getFilterCount();
	    Logger.info("Total Filters Count is = " +filtersCount);
		
	    // Check for Filters Count
	    if (filtersCount==0) {
	    	org.testng.Assert.fail("For -- " + entityReportNameAtDropdown + " -- Listing there are No Corresponding Filters Available");

	    	driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }

	    // Loop for Filters
	    for (int filterIndex=0; filterIndex<filtersCount; filterIndex++) {
		    //

			driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
			Thread.sleep(2000);

			filtersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("filters-new"));
		
	    	String filterName = CommonTestFiltering.getFilterName(filterIndex,filtersContent);
		    Logger.info("Filters Name at Index -- " + filterIndex + " -- is = " + filterName);
		    
		    String filterType = CommonTestFiltering.getFilterType(filterIndex);
		
		    // Check For Filter Type
	    	if (!(filterType.equalsIgnoreCase("selectExtended")) && !(filterType.equalsIgnoreCase("daterange")))
			    continue;

		    // Selection of Filter Options
	    	String[] filterOptions = CommonTestFiltering.selectFilterOptions(filterIndex, convertStringToInteger(CONFIG.getProperty("filterOptionsCount")), filterType);

	    	if (filterOptions==null) {
			    Logger.info("For Filter -- " + filterName + " -- Selected Options List is EMPTY");

			    // Clicking on RESET Button
		    	driver.findElement(By.id("filterDiv")).findElement(By.id("filter"))
				.findElement(By.className("filterhead")).findElement(By.cssSelector("input.reset.filter-buttons")).click();
				//
				fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

	    		continue;
	    		}

		    // Clicking on Filter Button
	    	driver.findElement(By.id("filterDiv")).findElement(By.id("filter"))
			.findElement(By.className("filterhead")).findElement(By.cssSelector("input.filter.filter-buttons")).click();
			//
			
		    // Check for Listing Data
		    if (CommonTestFiltering.checkTableDataPresence()) {
		    	Logger.info("For -- " + entityReportName + " -- Listing there is No Corresponding Data Available");

		    	fluentWaitMethod(driver.findElement(By.id("cr")));
		    	driver.findElement(By.id("cr"));

		    	String dataTableInfo = 	driver.findElement(By.id("cr_wrapper")).findElement(By.className("dataTables_info")).getText();
				String totalEntries = StringUtils.substringBetween(dataTableInfo, "of ", " entries");

				if (!totalEntries.equalsIgnoreCase("0"))
			    	org.testng.Assert.fail("For -- " + entityReportName + " --- Listing Page is not getting Opened after Filter");
					
			    // Clicking on RESET Button
		    	driver.findElement(By.id("filterDiv")).findElement(By.id("filter"))
				.findElement(By.className("filterhead")).findElement(By.cssSelector("input.reset.filter-buttons")).click();
				//
				fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
	    		
				continue;
				}
		    
		    driver.findElement(By.cssSelector("div.ColVis.TableTools")).findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_MasterButton")).click();
		    Thread.sleep(2000);

		    driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchableConfig"))
		    .findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_ShowAll")).click();
		    //
		    
			new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
	    	//

		    listExpected = TestSorting.getColumnData(CommonTestFiltering.getFilterColumnNumber(filterName));
	    
		    int failCount = 0;
	    	if (filterType.equalsIgnoreCase("selectExtended")) {
	    		for (int jCount=0; jCount<listExpected.size(); jCount++) {
		    		for (int kCount=0; kCount<filterOptions.length; kCount++) {
		    			if (listExpected.get(jCount).contains(filterOptions[kCount])) {
		    				failCount=0;
		    				break;
		    				}
		    			else
		    				failCount++;
		    			}
		    		resultCount = resultCount+failCount;		    			
		    		}
	    			
	    		}

	    	else if (filterType.equalsIgnoreCase("daterange")) {
	    		for (int jCount=0; jCount<listExpected.size(); jCount++) {
		    		if (CommonTestFiltering.checkDateValidator(listExpected.get(jCount), filterOptions[0], filterOptions[1]))
		    			failCount=0;
		    		else
		    			failCount++;
		    		resultCount = resultCount+failCount;
		    		}
	    		}
	    	
	    	if (resultCount>0) {
	    		Logger.error("For -- " + entityReportNameAtDropdown + " --  Filter Status for -- " +filterName+ " -- FAIL");
	    		filterTestStatus = filterTestStatus+resultCount;
	    		resultCount=0;
	    		break;
	    		}
	    	else
		    	Logger.debug("For -- " + entityReportNameAtDropdown + " -- Filter Status for -- " + filterName + " -- TRUE");
		    	
		    listExpected.clear();
		    
		    driver.navigate().refresh();
			//
			fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
		    }

	    if (filterTestStatus>0)
	    	org.testng.Assert.fail("For -- " + entityReportNameAtDropdown + " --- Filter is not working");
	    else
	    	Logger.debug("For -- " + entityReportNameAtDropdown + " -- Filter is Working as Expected");

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
			
			try {
				if (!testCaseID.equalsIgnoreCase(""))
					TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
				} catch (Exception e) {
				
				}
			}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_report_suite_xls, this.getClass().getSimpleName());
		}
	}
