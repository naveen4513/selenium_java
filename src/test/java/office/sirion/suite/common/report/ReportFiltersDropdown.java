package office.sirion.suite.common.report;

import java.io.IOException;
import java.util.Arrays;

import office.sirion.util.TestFiltering;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
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

public class ReportFiltersDropdown extends TestSuiteBase {
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
		if (!TestUtil.isTestCaseRunnable(common_report_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_report_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ReportFiltersDropdownTest (String testCaseID, String entityName, String entityReportName, String entityReportListingName
			) throws InterruptedException, TestLinkAPIException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +entityName +" set to NO " +count);
			}

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
    	Logger.info("Executed Test Case Filter Options Sorting for Entity Name --- " +entityName+ " --- and Report Name --- "+entityReportName);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    fluentWaitMethod(driver.findElement(By.linkText("Reports")));
	    
		driver.findElement(By.linkText("Reports")).click();
		fluentWaitMethod(driver.findElement(By.id("allLists1")));

		new Select(driver.findElement(By.id("allLists1"))).selectByVisibleText(entityName);
		fluentWaitMethod(driver.findElement(By.id("allLists2")));

		new Select(driver.findElement(By.id("allLists2"))).selectByVisibleText(entityReportName);
		fluentWaitMethod(driver.findElement(By.id("downloadXls")));

	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Report Page of --- " +entityReportListingName+ " --- opened Successfully");
/*
		String reportNameShowPage = driver.findElement(By.id("downloadXls")).getText();
	    Assert.assertEquals(reportNameShowPage, entityReportListingName, "Report Name is -- " +reportNameShowPage+ " instead of -- "+entityReportListingName);
*/	    
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

	    	Logger.info("For --- " +entityReportListingName+ " --- Listing there are No Corresponding Filters Available");

	        driver.get(CONFIG.getProperty("endUserURL"));
	        return;
	        }
	    
	    Logger.info("Number of Filters Present for --- " +entityReportListingName+ " --- is --- " +filtersSize);
	    
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
		    	Logger.info("Executed Filter Options Sorting for Entity Report Name --- " +entityReportListingName+
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
    	Logger.info("Executed Filter Options Sorting for Entity Report --- " +entityReportListingName+ " --- successfully");
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(skip)
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(fail) {
			isTestPass=false;
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			}
		else
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
		
		skip=false;
		fail=true;
		}
	
	@AfterTest
	public void reportTestResult() {
		if(isTestPass)
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_report_suite_xls, this.getClass().getSimpleName());
		}
	}