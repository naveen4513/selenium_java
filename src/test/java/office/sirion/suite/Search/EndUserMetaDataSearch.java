package office.sirion.suite.Search;

import office.sirion.suite.issue.TestSuiteBase;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class EndUserMetaDataSearch extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(search_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(search_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void EndUserMetaDataSearchTest (String EntityTypeName,String SearchFieldName,String FieldValue) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no " + count);
		}
		Logger.debug("Executing Test Case issue Workflow");

		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
		Logger.debug("Executing Test Case MetaData Search --- ");

		driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("end_user_search_link"));

		locateElementBy("end_user_search_link").click();
		locateElementBy("end_user_advance_search_link").click();

		fluentWaitMethod(locateElementBy("end_user_advance_search_drop_down"));
		addFieldValue("end_user_advance_search_drop_down","Metadata");

		addFieldValue("end_user_advance_search_attachment_search_entity_type_dropdown",EntityTypeName);



		locateElementBy("end_user_advance_search_field_drop_down").click();
		driver.findElement(By.xpath("//*[contains(text(),'"+SearchFieldName+"')]/preceding-sibling::input[@type='checkbox']")).click();

		locateElementBy("end_user_advance_search_header_bar").click();

		addFieldValue("end_user_advance_search_metadata_search_action_status_dropdown",FieldValue);

		fluentWaitMethod(locateElementBy("end_user_advance_search_metadata_search_button"));

		locateElementBy("end_user_advance_search_metadata_search_button").click();

		//waitF.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//*[@id='ajaxcalloading']"))));

		System.out.println("Loader Icon is disappear");

		try{
			driver.findElement(By.xpath("//td[@valign='top']"));

			if (driver.findElement(By.xpath("//td[@valign='top']")).getText().equalsIgnoreCase("No corresponding data available")){
				Logger.debug("No corresponding data available for "+ EntityTypeName + "with Field Name" + SearchFieldName);
				fail=false;
				driver.get(CONFIG.getProperty("endUserURL"));

			}else{
				Logger.debug("No corresponding data available for "+ EntityTypeName + "with Field Name" + SearchFieldName);
				fail=false;
				driver.get(CONFIG.getProperty("endUserURL"));

			}
		}catch(NoSuchElementException e){

			if (driver.findElements(By.xpath("//table[@name='searchResults']//tbody/tr/td[1]/a")).size()!=0){

				List<WebElement> EntityIDs = driver.findElements(By.xpath("//table[@name='searchResults']//tbody/tr/td[1]/a"));
				for (WebElement SearchResult : EntityIDs)
				{
					String AttachmentResultLinks =SearchResult.getText();
					System.out.println(SearchResult);
					Actions newTab = new Actions(driver);
					newTab.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(SearchResult).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();


					//handle windows change
					String base = driver.getWindowHandle();
					Set<String> set = driver.getWindowHandles();

					set.remove(base);
					assert set.size() == 1;
					driver.switchTo().window((String) set.toArray()[0]);

					try {
						driver.findElement(By.xpath("//div[@class='modal-content']"));
						if(driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
								equalsIgnoreCase("Either you do not have the required permissions or requested page does not exist anymore.") ||
								driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
										equalsIgnoreCase("Server Error")) {
							Logger.debug("Unable to view show page of an Entity" + AttachmentResultLinks + "Might be Permission issue");
							locateElementBy("entity_show_page_permission_notification_pop_up_ok_button").click();
						}
					} catch (NoSuchElementException exception) {
						if (driver.getPageSource().contains(AttachmentResultLinks)) {
							System.out.println("ID displayed on Search Result matched with show page");
							fail = false;
							driver.get(CONFIG.getProperty("endUserURL"));

						} else {
							System.out.println("Show page of different ID is getting open");
							fail = false;
							driver.get(CONFIG.getProperty("endUserURL"));

						}
					}
					//close the window and sitch back to the base tab
					driver.close();
					driver.switchTo().window(base);
				}
			}
		}
	}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= search_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = search_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
	}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(search_suite_xls, "Test Cases",
					TestUtil.getRowNum(search_suite_xls, this.getClass()
							.getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(search_suite_xls, "Test Cases",
					TestUtil.getRowNum(search_suite_xls, this.getClass()
							.getSimpleName()), "FAIL");

	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(search_suite_xls, this.getClass().getSimpleName());
	}

}
