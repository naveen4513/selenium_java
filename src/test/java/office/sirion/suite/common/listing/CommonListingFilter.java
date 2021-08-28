package office.sirion.suite.common.listing;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class CommonListingFilter extends TestSuiteBase {
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
		if (!TestUtil.isTestCaseRunnable(common_listing_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_listing_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CommonListingFilterTest (String testCaseID, String entityName
			) throws InterruptedException, TestLinkAPIException, ParseException {
		
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +entityName +" set to NO " +count);

		this.testCaseID = testCaseID;
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Filter Test Case for Listing Page of --- " + entityName);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
	    
	    if (entityName.equalsIgnoreCase("Vendor Hierarchy")) {
		    fluentWaitMethod(locateElementBy("vh_quick_link"));
		    locateElementBy("vh_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Suppliers")) {
		    fluentWaitMethod(locateElementBy("suppliers_quick_link"));
		    locateElementBy("suppliers_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Contracts")) {
		    fluentWaitMethod(locateElementBy("contracts_quick_link"));
		    locateElementBy("contracts_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Service Levels")) {
		    fluentWaitMethod(locateElementBy("sl_quick_link"));
		    locateElementBy("sl_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Child Service Levels")) {
		    fluentWaitMethod(locateElementBy("sl_quick_link"));
		    locateElementBy("sl_quick_link").click();
			//

			fluentWaitMethod(driver.findElement(By.linkText("View Child Service Levels")));
		    driver.findElement(By.linkText("View Child Service Levels")).click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Obligations")) {
		    fluentWaitMethod(locateElementBy("ob_quick_link"));
		    locateElementBy("ob_quick_link").click();
			//

		    fluentWaitMethod(driver.findElement(By.linkText("View Obligations")));
		    driver.findElement(By.linkText("View Obligations")).click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Child Obligations")) {
		    fluentWaitMethod(locateElementBy("ob_quick_link"));
		    locateElementBy("ob_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Governance Body")) {
		    fluentWaitMethod(locateElementBy("gb_quick_link"));
		    locateElementBy("gb_quick_link").click();
			//
		    
		    fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODIES")));
		    driver.findElement(By.linkText("VIEW GOVERNANCE BODIES")).click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Governance Body Meeting")) {
		    fluentWaitMethod(locateElementBy("gb_quick_link"));
		    locateElementBy("gb_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Actions")) {
		    fluentWaitMethod(locateElementBy("actions_quick_link"));
		    locateElementBy("actions_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Issues")) {
		    fluentWaitMethod(locateElementBy("issues_quick_link"));
		    locateElementBy("issues_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Change Requests")) {
		    fluentWaitMethod(locateElementBy("cr_quick_link"));
		    locateElementBy("cr_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Interpretations")) {
		    fluentWaitMethod(locateElementBy("ip_quick_link"));
		    locateElementBy("ip_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Disputes")) {
		    fluentWaitMethod(locateElementBy("disputes_quick_link"));
		    locateElementBy("disputes_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Invoices")) {
		    fluentWaitMethod(locateElementBy("inv_quick_link"));
		    locateElementBy("inv_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Purchase Order")) {
		    fluentWaitMethod(locateElementBy("po_quick_link"));
		    locateElementBy("po_quick_link").click();
			//
			}

	    else if (entityName.equalsIgnoreCase("Work Order Requests")) {
		    fluentWaitMethod(locateElementBy("wor_quick_link"));
		    locateElementBy("wor_quick_link").click();
			//
			}
	    
	    else if (entityName.equalsIgnoreCase("Clause")) {
		    fluentWaitMethod(locateElementBy("cdr_quick_link"));
		    locateElementBy("cdr_quick_link").click();
			//

		    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
		    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();
		    driver.findElement(By.linkText("View Clause Library")).click();
		    //
			}
	    
	    else if (entityName.equalsIgnoreCase("Contract Template")) {
		    fluentWaitMethod(locateElementBy("cdr_quick_link"));
		    locateElementBy("cdr_quick_link").click();
		    //
		    
		    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
		    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();
		    driver.findElement(By.linkText("View Contract Templates")).click();
		    //
			}

	    else if (entityName.equalsIgnoreCase("Contract Draft Request")) {
		    fluentWaitMethod(locateElementBy("cdr_quick_link"));
		    locateElementBy("cdr_quick_link").click();
			//
			}
		//
	    fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
	    driver.findElement(By.className("dataTables_scrollBody"));

	    Logger.debug("Listing Page of --- " +entityName+ " --- opened Successfully");
		
	    // Check for Listing Data
	    if (CommonTestFiltering.checkTableDataPresence()) {
	    	Logger.info("For " +entityName+ " --- Listing there is No Corresponding Data Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
		
	    int filtersCount = CommonTestFiltering.getFilterCount();
	    Logger.info("Total Filters Count on -- " + entityName + " -- Listing Page is = " +filtersCount);
		
	    if (filtersCount==0) {
	    	org.testng.Assert.fail("For " +entityName+ " --- Listing there are No Corresponding Filters Available");

	    	driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }

	    for (int filterIndex=0; filterIndex<filtersCount; filterIndex++) {
		    // Clicking on Filter Button
		    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
		    Thread.sleep(2000);

			filtersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("filters-new"));
		
	    	String filterName = CommonTestFiltering.getFilterName(filterIndex,filtersContent);
		    Logger.info("Filters Name on -- " + entityName + " -- Listing Page at Index -- " +filterIndex+ " -- is = " +filterName);
		    
		    String filterType = CommonTestFiltering.getFilterType(filterIndex);
		
	    	if (!(filterType.equalsIgnoreCase("selectExtended")) && !(filterType.equalsIgnoreCase("daterange"))) {
			    // Clicking on Filter Button
			    driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
			    Thread.sleep(2000);

			    continue;
	    		}

		    // Select Filter Options at Given Index
	    	String[] filterOptions = CommonTestFiltering.selectFilterOptions(filterIndex, convertStringToInteger(CONFIG.getProperty("filterOptionsCount")), filterType);
		    Logger.info("For Filter -- " + filterName + " -- Selected Options List is = " + Arrays.toString(filterOptions));
		    
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
		
		    // Clicking on Filter Button
		    driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
		    Thread.sleep(2000);
	    
		    // Chec8k for Listing Data
		    if (CommonTestFiltering.checkTableDataPresence()) {
			    Logger.info("For Filter -- " + filterName + " -- For Selected Options List There is no Corresponding Data Available");

			    driver.navigate().refresh();
				//
				fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

		        continue;
		        }
	    
		    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("columnsList")).click();
		    Thread.sleep(2000);
	    
		    driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection.searchable"))
		    .findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_ShowAll")).click();

		    listExpected = TestSorting.getColumnData(CommonTestFiltering.getFilterColumnNumber(filterName));
		    Logger.info("For Filter -- " + filterName + " -- Column Data After Applying Filter is = " + listExpected);
	    
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
	    		Logger.error("For " +entityName+ " --- for Filter Name --- " +filterName+ "--- Filter is not working");
	    		filterTestStatus = filterTestStatus+resultCount;
	    		resultCount=0;
	    		break;
	    		}
		    	
		    listExpected.clear();
		    
		    driver.navigate().refresh();
			//
			fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
		    }

	    if (filterTestStatus>0)
	    	org.testng.Assert.fail("For " +entityName+ " --- Filter is not working");

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
			
			try {
				if (!testCaseID.equalsIgnoreCase(""))
					TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
				} catch (Exception e) {
				
				}
			}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_listing_suite_xls, this.getClass().getSimpleName());
		}
	}
