package office.sirion.suite.sirionUserAdmin;

import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.DatabaseQueryChange;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
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

public class ClientAdminCreation extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(user_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(user_admin_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void ClientAdminCreationTest (String clientFirstName, String clientLastName, String clientLoginId, String clientEmailId,
			String clientContactNo, String clientDesignation, String clientTimezone, String clientUniqueLoginId, String clientSupplier, String clientSupplierAlias,
			String clientSendEmail, String clientUserRoleGroup
			) throws InterruptedException, ClassNotFoundException, SQLException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +clientLoginId +" set to NO " +count);

		openBrowser();
		sirionUserAdminLogin(CONFIG.getProperty("sirionuserAdminURL"), CONFIG.getProperty("sirionuserAdminUsername"), CONFIG.getProperty("sirionuserAdminPassword"));

		Logger.debug("Executing Test Sirion User Admin Client/Supplier Admin Creation for -- "+clientLoginId);

		driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		

		locateElementBy("sua_user_configuration_tab").click();
		locateElementBy("sua_user_configuration_plus_icon").click();
		
		if (!clientFirstName.equalsIgnoreCase(""))
			locateElementBy("sua_user_configuration_client_admin_create_page_firstname").sendKeys(clientFirstName.trim());

		if (!clientLastName.equalsIgnoreCase(""))
			driver.findElement(By.id("_lastName_id")).sendKeys(clientLastName.trim());

		if (!clientLoginId.equalsIgnoreCase(""))
			driver.findElement(By.id("_loginId_id")).sendKeys(clientLoginId.trim());

		if (!clientEmailId.equalsIgnoreCase(""))
			driver.findElement(By.id("_email_id")).sendKeys(clientEmailId.trim());

		if (!clientContactNo.equalsIgnoreCase("")) {
			Double temp_entity_double = Double.parseDouble(clientContactNo);
			int temp_entity_int = temp_entity_double.intValue();
			String temp_entity_string = Integer.toString(temp_entity_int);		

			driver.findElement(By.id("_contactNo_id")).clear();
			driver.findElement(By.id("_contactNo_id")).sendKeys(temp_entity_string.trim());
			}

		if (!clientDesignation.equalsIgnoreCase(""))
			driver.findElement(By.id("_designation_id")).sendKeys(clientDesignation.trim());

		if (!clientTimezone.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_timeZone.id_id"))).selectByVisibleText(clientTimezone.trim());

		if (!clientUniqueLoginId.equalsIgnoreCase(""))
			driver.findElement(By.id("_uniqueLoginId_id")).sendKeys(clientUniqueLoginId.trim());

		if (!clientSupplier.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_clientId_id"))).selectByVisibleText(clientSupplier.trim());

		if (clientSendEmail.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_sendEmail_id")).isSelected())
				driver.findElement(By.id("_sendEmail_id")).click();
			}

//		if (!clientUserRoleGroup.equalsIgnoreCase("")) {
//			for (String entityData : clientUserRoleGroup.split(";")) {
//				new Select(driver.findElement(By.id("_userRoleGroups_id"))).selectByVisibleText(entityData.trim());
//				}
//			}

		driver.findElement(By.id("proceed")).click();
		//

		if (clientFirstName.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._firstName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client First Name is Mandatory");

			driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientFirstName.length()>512) {
			WebElement e = driver.findElement(By.cssSelector("div._firstName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Maximum 512 characters allowed");
			Logger.debug("Fore Client First Name --- , Maximum 128 characters allowed");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientLastName.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._lastName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client Last Name is Mandatory");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientLastName.length()>512) {
			WebElement e = driver.findElement(By.cssSelector("div._lastName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Maximum 512 characters allowed");
			Logger.debug("Fore Client Last Name --- , Maximum 128 characters allowed");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientLoginId.equalsIgnoreCase("") || clientLoginId.contains(" ")) {
			WebElement e = driver.findElement(By.cssSelector("div._loginId_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Extra space/tab present");
			Logger.debug("Client Login ID is Mandatory");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientEmailId.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._email_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client Email ID is Mandatory");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientUniqueLoginId.equalsIgnoreCase("") || clientUniqueLoginId.contains(" ")) {
			WebElement e = driver.findElement(By.cssSelector("div._uniqueLoginId_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Extra space/tab present");
			Logger.debug("Client Unique Login ID is Mandatory");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if (clientSupplier.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._clientId_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client Supplier is Mandatory");

		    driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		    return;
		    }

		if(driver.findElements(By.xpath("//div[@class='alertdialog-icon']")).size()!=0) {
			String entityErrors = driver.findElement(By.xpath("//div[@class='alertdialog-icon']")).getText();
			
			if (entityErrors.contains("This login id already exists in the system."))
				Logger.debug("For Client, " +clientLoginId+" -- This login id already exists in the system.");
			if (entityErrors.contains("This unique login id already exists in the system."))
				Logger.debug("For Client, " +clientUniqueLoginId+" -- This unique login id already exists in the system.");
			if (entityErrors.contains("This email id already exists in the system."))
				Logger.debug("For Client, " +clientEmailId+" -- This email id already exists in the system.");
									
			driver.get(CONFIG.getProperty("sirionuserAdminURL"));
			return;
			}

		driver.findElement(By.xpath(".//*[@class='submit']/input[contains(@value,'Activate')]")).click();
		//
		waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("alertdialog-icon"))));

		String clientAlert = driver.findElement(By.className("alertdialog-icon")).getText();

		Assert.assertEquals(clientAlert, "User is activated now. Check your email entered above to get the password to login.");
		Logger.debug("Client/Supplier Admin created successfully with Login ID -- " +clientLoginId);

		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

		String clientShowPageLoginID = driver.findElement(By.id("_c_com_sirionlabs_model_TblUser_loginId_loginId_id")).getText();
		Assert.assertEquals(clientShowPageLoginID, clientLoginId);

		DatabaseQueryChange.clientUserPasswordChange("Select id from app_user where login_id='" +clientLoginId+ "' and client_id in (select id from client  where alias ilike'"+clientSupplierAlias+"')" );
		//

		Logger.debug("Test Sirion User Admin Client/Supplier Admin Creation for -- " + clientLoginId + " is PASSED");
		driver.get(CONFIG.getProperty("sirionuserAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
	      result= "Pass";
	      }
	   try {
	      if (!testCaseID.equalsIgnoreCase(""))
	         TestlinkIntegration.updateTestLinkResult(testCaseID,new java.lang.Exception().toString(),result);
	      } catch (Exception e) {
	                   Logger.debug(e);
	         }
	   }

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(user_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(user_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(user_admin_suite_xls, this.getClass().getSimpleName());
		}
	}