package office.sirion.suite.clientUserAdmin;

import java.io.IOException;
import java.util.List;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

import testlink.api.java.client.TestLinkAPIResults;

public class UserRoleGroup extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_user_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_user_admin_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void UserRoleGroupCreation (String userRoleGroupName, String userRoleGroupDescription, String userRoleGroupUserType, String userRoleGroupSupplier,
			String userRoleGroupFinancialDocument, String userRoleGroupBusinessCase, String userRoleGroupLegalAccess, String userRoleGroupActive,
			String userRoleGroupSelectAll, String userRoleGroupPermissions, String userRoleGroupReportPermissions, String userRoleGroupManualReportPermissions,
			String userRoleGroupInsightPermissions, String userRoleGroupDashboardPermissions, String userRoleGroupManualDashboardPermissions) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + userRoleGroupName + " -- set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin User Role Group Creaton Test -- " + userRoleGroupName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("User Administration")));

		driver.findElement(By.linkText("User Administration")).click();
		fluentWaitMethod(driver.findElement(By.linkText("User Role Group")));

		driver.findElement(By.linkText("User Role Group")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
		
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!userRoleGroupName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(userRoleGroupName.trim());
			}
		
		if (!userRoleGroupDescription.equalsIgnoreCase("")) {
			driver.findElement(By.id("_description_id")).clear();
			driver.findElement(By.id("_description_id")).sendKeys(userRoleGroupDescription.trim());
			}
		
		if (!userRoleGroupUserType.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_loginType_id"))).selectByVisibleText(userRoleGroupUserType.trim());
		
		if (!userRoleGroupSupplier.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_supplierId_id"))).selectByVisibleText(userRoleGroupSupplier.trim());
		
		if (userRoleGroupFinancialDocument.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_financialAccess_id")).isSelected())
				driver.findElement(By.id("_financialAccess_id")).click();
			}

		if (userRoleGroupBusinessCase.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_businessAccess_id")).isSelected())
				driver.findElement(By.id("_businessAccess_id")).click();
			}
		
		if (userRoleGroupLegalAccess.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_legalAccess_id")).isSelected())
				driver.findElement(By.id("_legalAccess_id")).click();
			}
		
		if (userRoleGroupActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}

		driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")).click();
		Thread.sleep(2000);
		
		if (userRoleGroupSelectAll.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")).isSelected())
				driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")).click();
			} else {
				if (!userRoleGroupPermissions.equalsIgnoreCase("")) {
					List <WebElement> permissionElements = driver.findElement(By.id("permission")).findElements(By.tagName("h3"));
					for (String entityData : userRoleGroupPermissions.split(";")) {
						for (WebElement e : permissionElements) {
							String permissionHeader = e.findElement(By.tagName("label")).getText();
							if (permissionHeader.equalsIgnoreCase(entityData.trim())) {
								((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e.findElement(By.tagName("input")));
								if (!e.findElement(By.tagName("input")).isSelected())
									e.findElement(By.tagName("input")).click();
								}
							}
						}
					}
		
			if (!userRoleGroupReportPermissions.equalsIgnoreCase("")) {
				List <WebElement> reportPermissionElements = driver.findElement(By.id("reportRoles")).findElements(By.tagName("h3"));
				for (String entityData : userRoleGroupReportPermissions.split(";")) {
					for (WebElement e : reportPermissionElements) {
						String permissionHeader = e.findElement(By.tagName("label")).getText();
						if (permissionHeader.equalsIgnoreCase(entityData.trim())) {
						    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e.findElement(By.tagName("input")));
							if (!e.findElement(By.tagName("input")).isSelected())
								e.findElement(By.tagName("input")).click();
							}
						}
					}
				}
		
			if (!userRoleGroupManualReportPermissions.equalsIgnoreCase("")) {
				List <WebElement> reportPermissionElements = driver.findElement(By.id("manualReportRoles")).findElements(By.tagName("h3"));
				for (String entityData : userRoleGroupManualReportPermissions.split(";")) {
					for (WebElement e : reportPermissionElements) {
						String permissionHeader = e.findElement(By.tagName("label")).getText();
						if (permissionHeader.equalsIgnoreCase(entityData.trim())) {
						    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e.findElement(By.tagName("input")));
							if (!e.findElement(By.tagName("input")).isSelected())
								e.findElement(By.tagName("input")).click();
							}
						}
					}
				}
	
			if (!userRoleGroupInsightPermissions.equalsIgnoreCase("")) {
				List <WebElement> permissionElements = driver.findElement(By.id("insightRoles")).findElements(By.tagName("h3"));
				for (String entityData : userRoleGroupInsightPermissions.split(";")) {
					for (WebElement e : permissionElements) {
						String permissionHeader = e.findElement(By.tagName("label")).getText();
						if (permissionHeader.equalsIgnoreCase(entityData.trim())) {
						    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e.findElement(By.tagName("input")));
							if (!e.findElement(By.tagName("input")).isSelected())
								e.findElement(By.tagName("input")).click();
							}
						}
					}
				}

			if (!userRoleGroupDashboardPermissions.equalsIgnoreCase("")) {
				List <WebElement> reportPermissionElements = driver.findElement(By.id("chartRoles")).findElements(By.tagName("h3"));
				for (String entityData : userRoleGroupDashboardPermissions.split(";")) {
					for (WebElement e : reportPermissionElements) {
						String permissionHeader = e.findElement(By.tagName("label")).getText();
						if (permissionHeader.equalsIgnoreCase(entityData.trim())) {
						    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e.findElement(By.tagName("input")));
							if (!e.findElement(By.tagName("input")).isSelected())
								e.findElement(By.tagName("input")).click();
							}
						}
					}
				}	
		
			if (!userRoleGroupManualDashboardPermissions.equalsIgnoreCase("")) {
				List <WebElement> reportPermissionElements = driver.findElement(By.id("manualChartRoles")).findElements(By.tagName("h3"));
				for (String entityData : userRoleGroupManualDashboardPermissions.split(";")) {
					for (WebElement e : reportPermissionElements) {
						String permissionHeader = e.findElement(By.tagName("label")).getText();
						if (permissionHeader.equalsIgnoreCase(entityData.trim())) {
						    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e.findElement(By.tagName("input")));
							if (!e.findElement(By.tagName("input")).isSelected())
								e.findElement(By.tagName("input")).click();
							}
						}
					}
				}
			}

		driver.findElement(By.id("proceed")).click();
		Thread.sleep(2000);
		
		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For User Role Group, " +userRoleGroupName+" --- This name already exists in the system.");
									
			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(userRoleGroupName);
		Thread.sleep(2000);

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterUserRoleGroup']/tbody/tr[@role='row']/td[contains(.,'"+ userRoleGroupName +"')]/preceding-sibling::td[1]/a")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterUserRoleGroup_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, userRoleGroupName, "User Role Group at show page is -- " +entityTypeShowPage+ " instead of -- " +userRoleGroupName);
        
        Logger.debug("User Role Group created successfully, with Name -- " + userRoleGroupName);

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
	      if (!testCaseID.equalsIgnoreCase(""))
	         TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
	      } catch (Exception e) {
	                   Logger.debug(e);
	         }
	   }

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(client_user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_user_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_user_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_user_admin_suite_xls, this.getClass().getSimpleName());
		}
	}