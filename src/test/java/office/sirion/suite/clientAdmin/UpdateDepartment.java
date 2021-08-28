package office.sirion.suite.clientAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIResults;

public class UpdateDepartment extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean fail=true;
	static boolean skip=false;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void UpdateDepartmentTest (String departmentName, String departmentNameUpdate, String departmentBusinessUnitUpdate, String departmentActiveUpdate) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +departmentNameUpdate +" set to NO " +count);
			}
		
		openBrowser();
		
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Department Update Test -- "+departmentNameUpdate);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		Thread.sleep(2000);
		
		driver.findElement(By.linkText("Department")).click();

        new Select(getObject("ca_department_listing_page_display_dropdown")).selectByIndex(3);
        
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_Department']/tbody/tr[@role='row']/td[contains(.,'"+ departmentNameUpdate +"')]/a/div")).click();
        
        getObject("ca_department_show_page_edit_button").click();
        
		getObject("ca_department_name_textbox").clear();
		getObject("ca_department_name_textbox").sendKeys(departmentNameUpdate);
		
		new Select(getObject("ca_department_business_unit_dropdown")).selectByVisibleText(departmentBusinessUnitUpdate);
		
		if(!departmentActiveUpdate.equalsIgnoreCase("Yes")) {
			getObject("ca_department_active_checkbox").click();
			}
		
		getObject("ca_department_edit_page_update_button").click();

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String errors_create_page = getObject("ca_department_notification_popup_errors_dialogue_box").getText();
			
			getObject("ca_department_notification_popup_ok_button").click();
			
			getObject("ca_department_cancel_button").click();
			
			getObject("ca_department_confirmation_popup_yes_button").click();
			
			Logger.debug("Department already exists with Name -- " +departmentNameUpdate);
			Logger.debug("Errors: "+errors_create_page);

	        fail = false;
	        driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

        String entityTypeShowPage = getObject("ca_department_show_page_name").getText();
        Assert.assertEquals(entityTypeShowPage, departmentNameUpdate, "Department at show page is -- " +entityTypeShowPage+ " instead of -- " +departmentNameUpdate);

        Logger.debug("Department opened successfully, and following parameters have been validated: Department Name -- " +departmentNameUpdate);
        
        fail = false;
        driver.get(CONFIG.getProperty("clientAdminURL"));
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
	      if (!testCaseID.equalsIgnoreCase(""))
	         TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
	      } catch (Exception e) {
	                   Logger.debug(e);
	         }
	   }
	
	@AfterTest
	public void reportTestResult() {
		if(isTestPass)
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	}