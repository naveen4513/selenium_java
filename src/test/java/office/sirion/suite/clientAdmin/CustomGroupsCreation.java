package office.sirion.suite.clientAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
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

public class CustomGroupsCreation extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(client_admin_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void CustomGroupsCreationTest (String customGroupLabel, String customGroupGroupOrder, String customGroupEntityType, String customGroupParentGroup,
			String customGroupGroupType
			) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +customGroupLabel +" set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Custom Groups Creation Test -- "+customGroupLabel);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Custom Groups")));

		driver.findElement(By.linkText("Custom Groups")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));

		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_label_id")));

		if (!customGroupLabel.equalsIgnoreCase("")) {
			driver.findElement(By.id("_label_id")).clear();
			driver.findElement(By.id("_label_id")).sendKeys(customGroupLabel.trim());
			}
		
		if (!customGroupGroupOrder.equalsIgnoreCase("")) {
			driver.findElement(By.id("_orderSeq_id")).clear();
			driver.findElement(By.id("_orderSeq_id")).sendKeys(convertStringToInteger(customGroupGroupOrder.trim()));
			}

		if (!customGroupEntityType.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_entityType_id"))).selectByVisibleText(customGroupEntityType.trim());
			Thread.sleep(2000);
			
			if (!customGroupParentGroup.equalsIgnoreCase(""))
				new Select(driver.findElement(By.id("_parentGroup.id_id"))).selectByVisibleText(customGroupParentGroup.trim());
			}

		if (!customGroupGroupType.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_groupType.id_id"))).selectByVisibleText(customGroupGroupType.trim());
		
		driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		//
		
		if (customGroupLabel.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._label_idformError.parentFormdynamicLayoutGroup.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Groups Label is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }
		
		if (customGroupLabel.length() > 128) {
			String entityErrors = driver.findElement(By.cssSelector("div._label_idformError.parentFormdynamicLayoutGroup.formError"))
					.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Custom Groups Label, --- Maximum 128 characters allowed");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }	

		if (customGroupGroupOrder.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._orderSeq_idformError.parentFormdynamicLayoutGroup.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Groups Order is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		
		if (Integer.valueOf(convertStringToInteger(customGroupGroupOrder.trim()))>99999) {
			String entityErrors = driver.findElement(By.cssSelector("div._orderSeq_idformError.parentFormdynamicLayoutGroup.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum value is 99999");
			Logger.debug("For Custom Groups Order, --- Maximum value is 99999");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (customGroupEntityType.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._entityType_idformError.parentFormdynamicLayoutGroup.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Groups Entity Type is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (customGroupParentGroup.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._parentGroup_id_idformError.parentFormdynamicLayoutGroup.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Groups Parent Group is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (customGroupGroupType.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._groupType_id_idformError.parentFormdynamicLayoutGroup.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Groups Group Type is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }
		
		if (driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This label already exists in the system."))
				Logger.debug("For Custom Groups, " +customGroupLabel+" --- This label already exists in the system.");

			if (entityErrors.contains("This group order already exists in the system."))
				Logger.debug("For Custom Groups, " +customGroupGroupOrder+" --- This group order already exists in the system.");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		if (driver.findElements(By.className("success-icon")).size()!=0) {
			String entityAlert = driver.findElement(By.className("success-icon")).getText();
			
			Assert.assertEquals(entityAlert, "Group created successfully");
			Logger.debug("Custom Groups created successfully with Label -- " +customGroupLabel);

			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(customGroupLabel);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[2][div[./text()='"+customGroupEntityType+"']]/preceding-sibling::td[1]/div/a")).click();
		
        String entityTypeLabelShowPage = driver.findElement(By.id("_s_com_sirionlabs_business_entity_model_dynamicmetadata_label_label_id")).getText();
        Assert.assertEquals(entityTypeLabelShowPage, customGroupLabel, "Custom Groups Label at show page is -- " +entityTypeLabelShowPage+ " instead of -- " +customGroupLabel);

        String entityTypeTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_business_entity_model_dynamicmetadata_entitytype_entityType.name_id")).getText();
        Assert.assertEquals(entityTypeTypeShowPage, customGroupEntityType, "Custom Groups Label at show page is -- " +entityTypeTypeShowPage+ " instead of -- " +customGroupEntityType);
		
        Logger.debug("Custom Group Created and Verified on Show Page successfully, with Label -- " +customGroupLabel);
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