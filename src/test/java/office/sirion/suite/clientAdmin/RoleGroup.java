package office.sirion.suite.clientAdmin;

import java.io.IOException;
import java.util.List;

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

import testlink.api.java.client.TestLinkAPIResults;

public class RoleGroup extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	static boolean returnTest=false;
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
	public void RoleGroupCreation (String roleGroupEntity, String roleGroupListReport, String roleGroupListChart, String roleGroupUserType,
			String roleGroupName, String roleGroupDisplayName, String roleGroupOrder, String defaultLoggedinUser,
			String multipleUser, String roleGroupActive, String includeInGridView, String stakeholderSubGroup, String userRoleGroup) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y") || returnTest) {
			if (returnTest)
				Logger.error("Skipping Test Data "+roleGroupName+" as Previous Test data has failed");

			throw new SkipException("Runmode for Test Data -- " +roleGroupName +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Role Group Creation Test -- "+roleGroupName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Role Group")));

		driver.findElement(By.linkText("Role Group")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
		
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_entityType.id_id")));

		if (!roleGroupEntity.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_entityType.id_id"))).selectByVisibleText(roleGroupEntity.trim());

		if (roleGroupListReport.equalsIgnoreCase("")) {
			List<WebElement> listReport = new Select(driver.findElement(By.id("_listReportId_id"))).getOptions();
			for (WebElement e : listReport) {
				String listReportValue = e.getText();
				new Select(driver.findElement(By.id("_listReportId_id"))).selectByVisibleText(listReportValue.trim());
				}
			} else {
				for (String entityData : roleGroupListReport.split(";")) {
					new Select(driver.findElement(By.id("_listReportId_id"))).selectByVisibleText(entityData.trim());
					}
				}

		if (roleGroupListChart.equalsIgnoreCase("")) {
			List<WebElement> listChart = new Select(driver.findElement(By.id("_chartIds_id"))).getOptions();
			for (WebElement e : listChart) {
				String listChartValue = e.getText();
				new Select(driver.findElement(By.id("_chartIds_id"))).selectByVisibleText(listChartValue.trim());
				}
			} else {
				for (String entityData : roleGroupListChart.split(";")) {
					new Select(driver.findElement(By.id("_chartIds_id"))).selectByVisibleText(entityData.trim());
					}
				}
				
		if (roleGroupUserType.equalsIgnoreCase("")) {
			List<WebElement> listUserType = new Select(driver.findElement(By.id("_validUserType_id"))).getOptions();
			for (WebElement e : listUserType) {
				String listUserTypeValue = e.getText();
				new Select(driver.findElement(By.id("_validUserType_id"))).selectByVisibleText(listUserTypeValue.trim());
				}
			} else {
				for (String entityData : roleGroupUserType.split(";")) {
					new Select(driver.findElement(By.id("_validUserType_id"))).selectByVisibleText(entityData.trim());
					}
				}
		
		if (!roleGroupName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(roleGroupName.trim());
			}

		if (!roleGroupDisplayName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_description_id")).clear();
			driver.findElement(By.id("_description_id")).sendKeys(roleGroupName.trim());
			}
		
		if (!roleGroupOrder.equalsIgnoreCase("")) {
			Double temp_entity_double = Double.parseDouble(roleGroupOrder);
			int temp_entity_int = temp_entity_double.intValue();
			String temp_entity_string = Integer.toString(temp_entity_int);		

			driver.findElement(By.id("_sequenceOrder_id")).clear();
			driver.findElement(By.id("_sequenceOrder_id")).sendKeys(temp_entity_string.trim());
			}
		
		if (defaultLoggedinUser.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_defaultLoggedInUser_id")).isSelected())
				driver.findElement(By.id("_defaultLoggedInUser_id")).click();
			}

		if (multipleUser.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_multiValue_id")).isSelected())
				driver.findElement(By.id("_multiValue_id")).click();
			}
		
		if (roleGroupActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}

		if(roleGroupEntity.equalsIgnoreCase("Suppliers") || roleGroupEntity.equalsIgnoreCase("Clients")) {
			if (includeInGridView.equalsIgnoreCase("Yes")) {
				if (!driver.findElement(By.id("_includeInVendorHierarchyGridView_id")).isSelected())
					driver.findElement(By.id("_includeInVendorHierarchyGridView_id")).click();
				}
			}

		if (!stakeholderSubGroup.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_subHeader_id"))).selectByVisibleText(stakeholderSubGroup.trim());
		
		if (!userRoleGroup.equalsIgnoreCase("")) {
			for (String entityData : userRoleGroup.split(";")) {
				new Select(driver.findElement(By.id("_userRoleGroups_id"))).selectByVisibleText(entityData.trim());
				}
			}

		driver.findElement(By.id("proceed")).click();
		//

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Role Group, " +roleGroupName+" --- This name already exists in the system.");
									
			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(roleGroupName);
		Thread.sleep(2000);

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterRoleGroup']/tbody/tr[@role='row']/td[contains(.,'"+ roleGroupEntity +"')]/preceding-sibling::td[1]/a")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterRoleGroup_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, roleGroupName, "Role Group at show page is -- " +entityTypeShowPage+ " instead of -- " +roleGroupName);
        
        Logger.debug("Role Group created successfully, with Name -- " + roleGroupName);

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
		if (isTestPass)
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	}