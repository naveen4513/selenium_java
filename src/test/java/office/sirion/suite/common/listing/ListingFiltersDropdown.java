package office.sirion.suite.common.listing;

import java.io.IOException;
import java.util.Arrays;

import office.sirion.util.TestFiltering;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
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

public class ListingFiltersDropdown extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	static int resultCount = 0;
	static int countBox;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(common_listing_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_listing_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ListingFiltersDropdownTest (String testCaseID, String entityName, String entityListingName
			) throws InterruptedException, TestLinkAPIException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +entityName +" set to NO " +count);
			}

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
    	Logger.info("Executed Test Case Filter Options Sorting for Entity Name --- " +entityName);
		
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
	    Logger.debug("Listing Page of --- " +entityListingName+ " --- opened Successfully");
	    
	    // Clicking on Filter Button
	    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
	    //
	    
	    int filtersSize = TestFiltering.getFilterCount();
	    if (filtersSize==0) {
	        fail = false;
	        
	        if (!testCaseID.equalsIgnoreCase("")) {
				if (!fail)
			        result= "Pass";
			      else   
			         result= "Fail";
			     TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
			     }

	    	Logger.info("For --- " +entityName+ " --- Listing there are No Corresponding Filters Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
	    
	    Logger.info("Number of Filters Present for --- " +entityName+ " --- is --- " +filtersSize);
	    
	    for (int i=0; i<filtersSize; i++) {
	    	if (!(TestFiltering.getFilterType(i).equalsIgnoreCase("selectExtended")))
	    		continue;
	    	
	    	Logger.info("Executing Filter Options Sorting for Filter Named --- " +TestFiltering.getFilterName(i));
	    	String[] iArrayActual = TestFiltering.getFilterOptions(i, countBox);
	    	countBox++;
	    	if (iArrayActual==null)
	    		continue;

	    	String[] iArrayExpected = iArrayActual;
	    	
	    	Arrays.sort(iArrayExpected, String.CASE_INSENSITIVE_ORDER);
			
			if (!Arrays.equals(iArrayActual, iArrayExpected)) {
				resultCount++;
		    	Logger.info("Executed Filter Options Sorting for Entity Name --- " +entityName+
		    			" --- at Filter --- " +TestFiltering.getFilterName(i)+ " --- is unsuccessfull");
				Logger.debug("The List is not in Sorted Manner at index --- "+i);
				}
		    }

	    if (resultCount > 0)
	    	fail = true;
	    else
	    	fail = false;

        if (!testCaseID.equalsIgnoreCase("")) {
			if (!fail)
		        result= "Pass";
		      else
		         result= "Fail";
		     TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
		     }
        
        countBox=0;
    	Logger.info("Executed Filter Options Sorting for Entity Name --- " +entityName+ " --- successfully");
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(skip)
			TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(fail) {
			isTestPass=false;
			TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			}
		else
			TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
		
		skip=false;
		fail=true;
		}
	
	@AfterTest
	public void reportTestResult() {
		if(isTestPass)
			TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_listing_suite_xls, this.getClass().getSimpleName());
		}
	}