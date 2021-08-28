package office.sirion.suite.cdr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class CDRListingOpen extends TestSuiteBase {
	String result = null;
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(cdr_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cdr_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void CDRListingOpenTest (
			) throws InterruptedException {
		
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data CDR set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Listing Page Open of CONTRACT DRAFT REQUEST");
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
	    fluentWaitMethod(locateElementBy("cdr_quick_link"));

	    locateElementBy("cdr_quick_link").click();
	    //

	    driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Listing Page of -- CONTRACT DRAFT REQUEST -- opened Successfully");
	    
	    String listingNameShowPage = driver.findElement(By.cssSelector("span.name.ng-scope")).getText();
	    Logger.info("CONTRACT DRAFT REQUEST -- Listing Show Page Name is -- " +listingNameShowPage);

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