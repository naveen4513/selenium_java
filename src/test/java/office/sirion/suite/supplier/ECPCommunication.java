package office.sirion.suite.supplier;

import java.io.File;
import java.io.IOException;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
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

public class ECPCommunication extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(supplier_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(supplier_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ECPCommunicationTest (String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest,
			String UploadFiles, String ParentSupplier, String ParentEntity
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

	

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case ECP Communication for -- " + ParentEntity);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("suppliers_quick_link"));

		locateElementBy("suppliers_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentSupplier.equalsIgnoreCase("") && ParentSupplier.length()<=30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='"+ParentSupplier+"']/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}
		else if (!ParentSupplier.equalsIgnoreCase("") && ParentSupplier.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+ParentSupplier+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}

	    fluentWaitMethod(driver.findElement(By.linkText("EXTERNAL CONTRACTING PARTY")));

	    driver.findElement(By.linkText("EXTERNAL CONTRACTING PARTY")).click();

		fluentWaitMethod(locateElementBy("ecp_listing_page_display_dropdown_link"));

		new Select(locateElementBy("ecp_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			driver.findElement(By.xpath(".//*[@id='table_3609']/tbody/tr/td[2][./text()='"+ParentEntity+"']/preceding-sibling::td[1]/a")).click();
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			driver.findElement(By.xpath(".//*[@id='table_3609']/tbody/tr/td[2][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[1]/a")).click();


		// EXTERNAL CONTRACTING PARTY - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

		fluentWaitMethod(locateElementBy("entity_create_page_comments_textarea"));

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(ActualDate,"actualDate");

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\ExternalContractingParty\\" + UploadFiles);

			}

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		driver.findElement(By.linkText("COMMUNICATION")).click();
	    Logger.debug("Test Case ECP Communication for -- " + ParentEntity + " -- is STARTED");

	    fluentWaitMethod(driver.findElement(By.className("dataTables_scroll")));
		WebElement elementOddRowSelected = driver.findElement(By.id("table_3600")).findElements(By.className("odd")).get(0);

		if (!UploadFiles.equalsIgnoreCase("")) {
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), UploadFiles);

		    File file = new File(System.getProperty("user.home")+"//Downloads//"+UploadFiles);

			if (file.exists())
				file.delete();

			elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).findElement(By.tagName("a")).click();


			if (file.exists())
				Logger.debug("ECP Communication File Download PASSED");
			else
				Assert.fail("ECP Communication File Download FAILED");
			}
	    
	    if (!Comments.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), Comments);

		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));
	    
		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_status_name")).getText(), "Updated");

	    Logger.debug("Test Case ECP Communication with Name -- " + ParentEntity + " -- is PASSED");
	    driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= supplier_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = supplier_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
                if (!testCaseID.equalsIgnoreCase("")) {
                    updateRun(convertStringToInteger(testCaseID),new java.lang.Exception().toString(), result);
                }
            }
	   }catch(Exception e){
            Logger.debug(e);
        }
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(supplier_suite_xls, "Test Cases", TestUtil.getRowNum(supplier_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(supplier_suite_xls, "Test Cases", TestUtil.getRowNum(supplier_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(supplier_suite_xls, this.getClass().getSimpleName());
		}
	}
