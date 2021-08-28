package office.sirion.suite.clientAdmin.UIContentSetup;

import java.io.IOException;
import java.util.List;

import office.sirion.util.TestUtil;

import org.openqa.selenium.By;
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

public class TabListContentControls extends TestSuiteBase {
	String result=null;
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_ui_content_setup_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_ui_content_setup_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void TabListContentControlsTest (String tabListName, String documentTypeToIncludeAll, String documentTypeToInclude,
			String statusToIncludeAll, String statusToInclude
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + tabListName + " is set to NO " + count);

		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));

		Logger.debug("Executing Tab Tab List Content Controls Test for --- "+tabListName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Tab List Content Controls")));

		driver.findElement(By.linkText("Tab List Content Controls")).click();
		fluentWaitMethod(driver.findElement(By.name("l_com_sirionlabs_model_MasterGroup_length")));
		
		driver.findElement(By.id("l_com_sirionlabs_model_MasterGroup_filter")).findElement(By.tagName("input")).sendKeys(tabListName);

		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[2][div[@sort='" +tabListName.trim()+ "']]/following-sibling::td[2]/div/a")).click();

		if (documentTypeToIncludeAll.equalsIgnoreCase("Yes")) {
			List<WebElement> documentTypeList = new Select(driver.findElement(By.id("_params[0].statusList_id"))).getOptions();
			for (WebElement elementStatus : documentTypeList) {
				new Select(driver.findElement(By.id("_params[0].statusList_id"))).selectByVisibleText(elementStatus.getText().trim());
				}
			}
		else {
    		for (String entityData : documentTypeToInclude.split(";")) {
    			new Select(driver.findElement(By.id("_params[0].statusList_id"))).selectByVisibleText(entityData.trim());
    			}
    		}

		if(driver.findElements(By.id("_params[1].statusList_id")).size()!=0) {
			if (statusToIncludeAll.equalsIgnoreCase("Yes")) {
				List<WebElement> statusList = new Select(driver.findElement(By.id("_params[1].statusList_id"))).getOptions();
				for (WebElement elementStatus : statusList) {
					new Select(driver.findElement(By.id("_params[1].statusList_id"))).selectByVisibleText(elementStatus.getText().trim());
					}
				}
			else {
	    		for (String entityData : statusToInclude.split(";")) {
	    			new Select(driver.findElement(By.id("_params[1].statusList_id"))).selectByVisibleText(entityData.trim());
	    			}
	    		}
			}

        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.id("alertdialog"));

		String entityAlert = driver.findElement(By.id("alertdialog")).getText();

		Assert.assertEquals(entityAlert, "Tab List Parameters updated successfully");
		Logger.debug("Tab List Parameters updated successfully for --- " +tabListName);

		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

		driver.get(CONFIG.getProperty("clientAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
		}

		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
		}
	}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, "Test Cases", TestUtil.getRowNum(client_ui_content_setup_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, "Test Cases", TestUtil.getRowNum(client_ui_content_setup_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_ui_content_setup_suite_xls, this.getClass().getSimpleName());
		}
	}