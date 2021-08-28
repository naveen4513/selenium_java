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

public class DashboardContentControls extends TestSuiteBase {
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
	public void DashboardContentControlsTest (String dashboardName, String statusToIncludeAll, String statusToInclude, String statusToIncludeVolumeTrendAll,
			String statusToIncludeVolumeTrend, String statusToIncludeAgingAll, String statusToIncludeAging
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + dashboardName + " is set to NO " + count);

		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));

		Logger.debug("Executing Dashboard Content Controls Test for --- "+dashboardName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Dashboard Content Controls")));

		driver.findElement(By.linkText("Dashboard Content Controls")).click();
		fluentWaitMethod(driver.findElement(By.name("l_com_sirionlabs_model_MasterGroup_length")));
		
		driver.findElement(By.id("l_com_sirionlabs_model_MasterGroup_filter")).findElement(By.tagName("input")).sendKeys(dashboardName);

		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[1][div[@sort='" +dashboardName.trim()+ "']]/following-sibling::td[3]/div/a")).click();

		if (statusToIncludeAll.equalsIgnoreCase("Yes")) {
			List<WebElement> statusList = new Select(driver.findElement(By.id("_params[0].statusList_id"))).getOptions();
			for (WebElement elementStatus : statusList) {
				new Select(driver.findElement(By.id("_params[0].statusList_id"))).selectByVisibleText(elementStatus.getText().trim());
				}
			}
		else {
    		for (String entityData : statusToInclude.split(";")) {
    			new Select(driver.findElement(By.id("_params[0].statusList_id"))).selectByVisibleText(entityData.trim());
    			}
    		}

		if(driver.findElements(By.id("_params[1].statusList_id")).size()!=0) {
			if (statusToIncludeVolumeTrendAll.equalsIgnoreCase("Yes")) {
				List<WebElement> statusList = new Select(driver.findElement(By.id("_params[1].statusList_id"))).getOptions();
				for (WebElement elementStatus : statusList) {
					new Select(driver.findElement(By.id("_params[1].statusList_id"))).selectByVisibleText(elementStatus.getText().trim());
					}
				}
			else {
	    		for (String entityData : statusToIncludeVolumeTrend.split(";")) {
	    			new Select(driver.findElement(By.id("_params[1].statusList_id"))).selectByVisibleText(entityData.trim());
	    			}
	    		}
			}
		
		if(driver.findElements(By.id("_params[2].statusList_id")).size()!=0) {
			if (statusToIncludeAgingAll.equalsIgnoreCase("Yes")) {
				List<WebElement> statusList = new Select(driver.findElement(By.id("_params[2].statusList_id"))).getOptions();
				for (WebElement elementStatus : statusList) {
					new Select(driver.findElement(By.id("_params[2].statusList_id"))).selectByVisibleText(elementStatus.getText().trim());
					}
				}
			else {
	    		for (String entityData : statusToIncludeAging.split(";")) {
	    			new Select(driver.findElement(By.id("_params[2].statusList_id"))).selectByVisibleText(entityData.trim());
	    			}
	    		}
			}
		
		String chartNameOnShowPage = driver.findElement(By.className("tabs-inner-sec-header")).getText();
		
		if (driver.findElements(By.id("_outputGroups[0].clientTooltip_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[0].clientTooltip_id")).clear();
			driver.findElement(By.id("_outputGroups[0].clientTooltip_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[0].clientDescription_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[0].clientDescription_id")).clear();
			driver.findElement(By.id("_outputGroups[0].clientDescription_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[1].clientTooltip_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[1].clientTooltip_id")).clear();
			driver.findElement(By.id("_outputGroups[1].clientTooltip_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[1].clientDescription_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[1].clientDescription_id")).clear();
			driver.findElement(By.id("_outputGroups[1].clientDescription_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[2].clientTooltip_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[2].clientTooltip_id")).clear();
			driver.findElement(By.id("_outputGroups[2].clientTooltip_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[2].clientDescription_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[2].clientDescription_id")).clear();
			driver.findElement(By.id("_outputGroups[2].clientDescription_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[3].clientTooltip_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[3].clientTooltip_id")).clear();
			driver.findElement(By.id("_outputGroups[3].clientTooltip_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		if (driver.findElements(By.id("_outputGroups[3].clientDescription_id")).size()!=0) {
			driver.findElement(By.id("_outputGroups[3].clientDescription_id")).clear();
			driver.findElement(By.id("_outputGroups[3].clientDescription_id")).sendKeys(chartNameOnShowPage.toUpperCase());
			}
		
		driver.findElement(By.id("submitData")).click();
		//
		
		driver.findElement(By.id("alertdialog"));

		String entityAlert = driver.findElement(By.id("alertdialog")).getText();

		Assert.assertEquals(entityAlert, "Dashboard Parameters updated successfully");
		Logger.debug("Dashboard Parameters updated successfully for --- " +dashboardName);

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