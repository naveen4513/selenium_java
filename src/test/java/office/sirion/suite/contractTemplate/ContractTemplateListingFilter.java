package office.sirion.suite.contractTemplate;

import java.io.IOException;
import java.text.ParseException;
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
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ContractTemplateListingFilter extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	static LinkedList<String> listExpected;
	int resultCount;
	int filterTestStatus;
	public static List<WebElement> filtersContent;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(contract_template_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(contract_template_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ContractTemplateListingFilterTest (
			) throws InterruptedException, TestLinkAPIException, ParseException {
		
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- CONTRACT TEMPLATE -- is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Filter Test Case for -- CONTRACT TEMPLATE");
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(locateElementBy("cdr_quick_link"));

	    locateElementBy("cdr_quick_link").click();
	    //
	    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
	    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();
	    
	    driver.findElement(By.linkText("View Contract Templates")).click();
	    //

	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Listing Page of -- CONTRACT TEMPLATE -- opened Successfully");

	    String listingNameShowPage = driver.findElement(By.cssSelector("span.name.ng-scope")).getText();
	    Logger.info("CONTRACT TEMPLATE -- Listing Show Page Name is -- " +listingNameShowPage);
	    
	    // Check for Listing Data
	    if (CommonTestFiltering.checkTableDataPresence()) {
	    	Logger.info("For CONTRACT TEMPLATE -- Listing there is No Corresponding Data Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
	    
	    int filtersCount = CommonTestFiltering.getFilterCount();
	    Logger.info("Total Filters Count is = " +filtersCount);
		
	    // Check for Filters Count
	    if (filtersCount==0) {
	    	org.testng.Assert.fail("For CONTRACT TEMPLATE -- Listing there are No Corresponding Filters Available");

	    	driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }

	    // Loop for Filters
	    for (int filterIndex=0; filterIndex<filtersCount; filterIndex++) {
		    // Clicking on Filter Button
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
	    	if (!(filterType.equalsIgnoreCase("selectExtended")) && !(filterType.equalsIgnoreCase("daterange"))) {
			    driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
			    Thread.sleep(2000);
			    continue;
	    		}

		    // Selection of Filter Options
	    	String[] filterOptions = CommonTestFiltering.selectFilterOptions(filterIndex, convertStringToInteger(CONFIG.getProperty("filterOptionsCount")), filterType);
		    
	    	if (filterOptions==null) {
			    Logger.info("For Filter -- " + filterName + " -- Selected Options List is EMPTY");

	    		driver.navigate().refresh();
				//
				fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

	    		continue;
	    		}
		
		    // Clicking on Filter Button
	    	driver.findElement(By.id("filterDiv")).findElement(By.id("filter"))
			.findElement(By.className("filterhead")).findElement(By.cssSelector("input.filter.filter-buttons")).click();
			//
		
		    driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
		    //
	    
		    // Chec8k for Listing Data
		    if (CommonTestFiltering.checkTableDataPresence()) {
			    Logger.info("For Filter -- " + filterName + " -- There is no Corresponding Data Available");

			    driver.navigate().refresh();
				//
				fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

		        continue;
		        }
	    
		    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("columnsList")).click();
		    Thread.sleep(2000);
	    
		    driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchable"))
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
	    		Logger.error("For CONTRACT TEMPLATE -- Filter -- " +filterName+ " -- is not working");
	    		filterTestStatus = filterTestStatus+resultCount;
	    		resultCount=0;
	    		break;
	    		}
	    	else
		    	Logger.debug("For CONTRACT TEMPLATE -- Filter Status for -- " + filterName + " -- TRUE");
		    	
		    listExpected.clear();
		    
		    driver.navigate().refresh();
			//
			fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
		    }

	    if (filterTestStatus>0)
	    	org.testng.Assert.fail("For CONTRACT TEMPLATE --- Filter is not working");
	    else
	    	Logger.debug("For CONTRACT TEMPLATE -- Filter is Working as Expected");

	    driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		
			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(contract_template_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(contract_template_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(contract_template_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}
			}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(contract_template_suite_xls, "Test Cases", TestUtil.getRowNum(contract_template_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(contract_template_suite_xls, "Test Cases", TestUtil.getRowNum(contract_template_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(contract_template_suite_xls, this.getClass().getSimpleName());
		}
	}
