package office.sirion.suite.clientUserAdmin;

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

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ClientUserCreation extends TestSuiteBase {

	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
    String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(client_user_admin_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_user_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ClientUserCreationTest (String userFirstName, String userLastName, String userLoginId, String userEmailId, String userContactNo,
			String userDesignation, String userTimezone, String userUniqueLoginId, String userDefaultTier, String userPasswordExpires,
			String userType, String userVendorHierarchy, String userLanguage, String userSendEmail, String userClientAlias, String userRoleGroup, String userAccessCriteria
			) throws InterruptedException, ClassNotFoundException, SQLException {
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + userLoginId + " set to NO " + count);

		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
	
		Logger.debug("Executing Client Admin Client User Creation Test -- User Role Group");
	
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("User Administration")));

		driver.findElement(By.linkText("User Administration")).click();
		fluentWaitMethod(driver.findElement(By.linkText("User Configuration")));

		driver.findElement(By.linkText("User Configuration")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
	
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_firstName_id")));

		if (!userFirstName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_firstName_id")).clear();
			driver.findElement(By.id("_firstName_id")).sendKeys(userFirstName.trim());
			}
    
		if (!userLastName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_lastName_id")).clear();
			driver.findElement(By.id("_lastName_id")).sendKeys(userLastName.trim());
			}
	
		if (!userLoginId.equalsIgnoreCase("")) {
			driver.findElement(By.id("_loginId_id")).clear();
			driver.findElement(By.id("_loginId_id")).sendKeys(userLoginId.trim());
			}

		if (!userEmailId.equalsIgnoreCase("")) {
			driver.findElement(By.id("_email_id")).clear();
			driver.findElement(By.id("_email_id")).sendKeys(userEmailId.trim());
			}
	
		if (!userContactNo.equalsIgnoreCase("")) {
			Double temp_entity_double = Double.parseDouble(userContactNo);
			int temp_entity_int = temp_entity_double.intValue();
			String temp_entity_string = Integer.toString(temp_entity_int);
		
			driver.findElement(By.id("_contactNo_id")).clear();
			driver.findElement(By.id("_contactNo_id")).sendKeys(temp_entity_string.trim());
			}

		if (!userDesignation.equalsIgnoreCase("")) {
			driver.findElement(By.id("_designation_id")).clear();
			driver.findElement(By.id("_designation_id")).sendKeys(userDesignation.trim());
			}

		if (!userTimezone.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_timeZone.id_id"))).selectByVisibleText(userTimezone.trim());
	
		if (!userUniqueLoginId.equalsIgnoreCase("")) {
			driver.findElement(By.id("_uniqueLoginId_id")).clear();
			driver.findElement(By.id("_uniqueLoginId_id")).sendKeys(userUniqueLoginId.trim());
			}
	
		if (!userDefaultTier.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_tierId_id"))).selectByVisibleText(userDefaultTier.trim());


		if (userPasswordExpires.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_passwordNeverExpires_id")).isSelected())
				driver.findElement(By.id("_passwordNeverExpires_id")).click();
			}	
	
		if (!userType.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_type_id"))).selectByVisibleText(userType.trim());
			Thread.sleep(2000);
			try {
				if(driver.findElements(By.id("_vendorId_id")).size()!=0) {
					if (!userVendorHierarchy.equalsIgnoreCase(""))
						new Select(driver.findElement(By.id("_vendorId_id"))).selectByVisibleText(userVendorHierarchy.trim());
					}
				} catch (Exception e) {
					Logger.debug("User Vendor Hierarchy is Mandatory");
					}
			}
	
		if (!userLanguage.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_language_id"))).selectByVisibleText(userLanguage.trim());
		
		if (userSendEmail.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_sendEmail_id")).isSelected())
				driver.findElement(By.id("_sendEmail_id")).click();
			}
	
		if (!userRoleGroup.equalsIgnoreCase("")) {
			for (String entityData : userRoleGroup.split(";")) {
				new Select(driver.findElement(By.id("_userRoleGroups_id"))).selectByVisibleText(entityData.trim());
				}
			}

		if (!userAccessCriteria.equalsIgnoreCase("")) {
			for (String entityData : userAccessCriteria.split(";")) {
				new Select(driver.findElement(By.id("_accessCriterias_id"))).selectByVisibleText(entityData.trim());
				}
			}
	
		driver.findElement(By.id("proceed")).click();
		//
	
		if (userFirstName.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._firstName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("User First Name is Mandatory");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
	    	}

		if (userFirstName.length()>512) {
			WebElement e = driver.findElement(By.cssSelector("div._firstName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Maximum 512 characters allowed");
			Logger.debug("Fore User First Name --- , Maximum 128 characters allowed");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
	    	}

		if (userLastName.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._lastName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("User Last Name is Mandatory");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
	    	}
	
		if (userLastName.length()>512) {
			WebElement e = driver.findElement(By.cssSelector("div._lastName_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Maximum 512 characters allowed");
			Logger.debug("Fore User Last Name --- , Maximum 128 characters allowed");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
	    	}
	
		if (userLoginId.equalsIgnoreCase("") || userLoginId.contains(" ")) {
			WebElement e = driver.findElement(By.cssSelector("div._loginId_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Extra space/tab present");
			Logger.debug("User Login ID is Mandatory");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
	    	}

		if (userEmailId.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._email_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("User Email ID is Mandatory");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		if (userUniqueLoginId.equalsIgnoreCase("") || userUniqueLoginId.contains(" ")) {
			WebElement e = driver.findElement(By.cssSelector("div._uniqueLoginId_idformError.parentFormtblUser.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Extra space/tab present");
			Logger.debug("User Unique Login ID is Mandatory");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();

			if (entityErrors.contains("This login id already exists in the system."))
				Logger.debug("For User, " +userLoginId+" --- This login id already exists in the system.");
			if (entityErrors.contains("This unique login id already exists in the system."))
				Logger.debug("For User, " +userUniqueLoginId+" --- This unique login id already exists in the system.");
			if (entityErrors.contains("This email id already exists in the system."))
				Logger.debug("For User, " +userEmailId+" --- This email id already exists in the system.");
			
			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		driver.findElement(By.xpath(".//*[@class='submit']/input[contains(@value,'Activate')]")).click();
		//
		waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("alertdialog-icon"))));

		if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
			String userAlert = driver.findElement(By.className("alertdialog-icon")).getText();
		
			Assert.assertEquals(userAlert, "User is activated now. Check your email entered above to get the password to login.");
			Logger.debug("Client User created successfully with Login ID -- " +userLoginId);
		
			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
		
			String userShowPageLoginID = driver.findElement(By.id("_c_com_sirionlabs_model_TblUser_loginId_loginId_id")).getText();
			Assert.assertEquals(userShowPageLoginID, userLoginId);
			Logger.debug("Client User Login ID on show page has been verified");

			DatabaseQueryChange.clientUserPasswordChange("Select id from app_user where login_id='" +userLoginId+ "' and client_id in (select id from client  where alias ilike'"+userClientAlias+"')" );
			//
    	
			Logger.debug("Password changed successfully for Client User --- " +userFirstName +" " +userLastName);
			}
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_user_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_user_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_user_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
	      result= "Pass";
	      }
        try {
            for (int i = 2; i <= client_user_admin_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = client_user_admin_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
                if (!testCaseID.equalsIgnoreCase("")) {
                    updateRun(testCaseID, new java.lang.Exception().toString(), result);
                }
            }
        }catch(Exception e){
            Logger.debug(e);
        }
	   }

	@AfterTest
	public void reportTestResult() {
		if (isTestPass) {
            TestUtil.reportDataSetResult(client_user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_user_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
        }else {
            TestUtil.reportDataSetResult(client_user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_user_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
        }
        //closeRun();
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_user_admin_suite_xls, this.getClass().getSimpleName());
		}
	}