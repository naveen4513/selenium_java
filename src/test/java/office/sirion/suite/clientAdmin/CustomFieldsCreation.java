package office.sirion.suite.clientAdmin;

import java.io.IOException;
import java.util.regex.Pattern;

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

public class CustomFieldsCreation extends TestSuiteBase {
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
	public void CustomFieldsCreationTest (String customFieldName, String customFieldLabel, String customFieldFieldOrder,
										  String customFieldEntityType, String customFieldLayoutGroup, String customFieldHTMLType,
										  String customFieldListingColumn, String customFieldExcelColumn, String customFieldFilter,
										  String customFieldActive, String customFieldSearchable, String customFieldAutoComplete,
										  String customFieldOptionsSize, String customFieldOptionsSort, String customFieldSupportRichText,
										  String customFieldValidationOptions, String customFieldDropdownOptions) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +customFieldName +" set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Custom Fields Creation Test -- "+customFieldName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Custom Fields")));

		driver.findElement(By.linkText("Custom Fields")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));

		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!customFieldName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(customFieldName.trim());
			}
		
		if (!customFieldLabel.equalsIgnoreCase("")) {
			driver.findElement(By.id("_displayName_id")).clear();
			driver.findElement(By.id("_displayName_id")).sendKeys(customFieldLabel.trim());
			}

		if (!customFieldFieldOrder.equalsIgnoreCase("")) {
			driver.findElement(By.id("_orderSeq_id")).clear();
			driver.findElement(By.id("_orderSeq_id")).sendKeys(convertStringToInteger(customFieldFieldOrder.trim()));
			}
		
		if (!customFieldEntityType.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_entityType.id_id"))).selectByVisibleText(customFieldEntityType.trim());
			Thread.sleep(2000);
			
			if (!customFieldLayoutGroup.equalsIgnoreCase(""))
				new Select(driver.findElement(By.id("_subHeader.id_id"))).selectByVisibleText(customFieldLayoutGroup.toUpperCase().trim());
			}
		
		if (!customFieldHTMLType.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_htmlType.id_id"))).selectByVisibleText(customFieldHTMLType.trim());
		
		/*if (customFieldListingColumn.equalsIgnoreCase("")) {
			List<WebElement> allOptions = new Select(driver.findElement(By.id("_listReportId_id"))).getOptions();
			for (WebElement e : allOptions) {
				new Select(driver.findElement(By.id("_listReportId_id"))).selectByVisibleText(e.getText());
				}
			} else {
				for (String entityData : customFieldListingColumn.split(";")) {
					new Select(driver.findElement(By.id("_listReportId_id"))).selectByVisibleText(entityData.trim());
					}
				}*/

		/*if (customFieldExcelColumn.equalsIgnoreCase("")) {
			List<WebElement> allOptions = new Select(driver.findElement(By.id("_listReportExcelId_id"))).getOptions();
			for (WebElement e : allOptions) {
				new Select(driver.findElement(By.id("_listReportExcelId_id"))).selectByVisibleText(e.getText());
				}
			} else {
				for (String entityData : customFieldExcelColumn.split(";")) {
					new Select(driver.findElement(By.id("_listReportExcelId_id"))).selectByVisibleText(entityData.trim());
					}
				}*/
		
		/*if (!customFieldHTMLType.equalsIgnoreCase("Text Field") && !customFieldHTMLType.equalsIgnoreCase("Text Area") && !customFieldHTMLType.equalsIgnoreCase("Check Box")) {
			if (customFieldFilter.equalsIgnoreCase("")) {
				List<WebElement> allOptions = new Select(driver.findElement(By.id("_filterListReportIds_id"))).getOptions();
				for (WebElement e : allOptions) {
					new Select(driver.findElement(By.id("_filterListReportIds_id"))).selectByVisibleText(e.getText());
					}
				} else {
					for (String entityData : customFieldFilter.split(";")) {
						new Select(driver.findElement(By.id("_filterListReportIds_id"))).selectByVisibleText(entityData.trim());
						}
					}
			}*/
		
		if (customFieldActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}

		if (customFieldSearchable.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_searchable_id")).isSelected())
				driver.findElement(By.id("_searchable_id")).click();
			}
		
		if (customFieldHTMLType.equalsIgnoreCase("Single Select") || customFieldHTMLType.equalsIgnoreCase("Multi Select")) {
			if (customFieldAutoComplete.equalsIgnoreCase("Yes")) {
				if (!driver.findElement(By.id("_autocomplete_id")).isSelected())
					driver.findElement(By.id("_autocomplete_id")).click();
				}

			if (!customFieldOptionsSize.equalsIgnoreCase("")) {
				driver.findElement(By.id("_optionsSize_id")).clear();
				driver.findElement(By.id("_optionsSize_id")).sendKeys(convertStringToInteger(customFieldOptionsSize.trim()));
				}
			
			if (customFieldOptionsSort.equalsIgnoreCase("Yes")) {
				if (!driver.findElement(By.id("_alphabeticalSort_id")).isSelected())
					driver.findElement(By.id("_alphabeticalSort_id")).click();
				}
			}
		
		if (customFieldHTMLType.equalsIgnoreCase("Text Area")) {
			if (customFieldSupportRichText.equalsIgnoreCase("Yes")) {
				if (!driver.findElement(By.id("_enableHtmlParsing_id")).isSelected())
					driver.findElement(By.id("_enableHtmlParsing_id")).click();
				}
			}
			
		
		if (customFieldHTMLType.equalsIgnoreCase("Text Field")
				|| customFieldHTMLType.equalsIgnoreCase("Text Area")
				|| customFieldHTMLType.equalsIgnoreCase("Numeric")
				|| customFieldHTMLType.equalsIgnoreCase("Currency")) {

			String[] entityData = customFieldValidationOptions.split(";");
			
			if (entityData.length<=1)
				driver.findElement(By.id("parentValidationDiv")).findElement(By.className("removebutton")).click();

			for (int iPlus = 1; iPlus<entityData.length; iPlus++) {
				driver.findElement(By.className("validationsDiv")).findElement(By.className("tabs-inner-sec-header")).findElement(By.className("add-icon-form")).click();
				
				if (iPlus>=2) {
					if (driver.findElements(By.className("success-icon")).size()!=0) {
						String entityAlert = driver.findElement(By.className("success-icon")).getText();
						
						Assert.assertEquals(entityAlert, "Maximum possible validations have been applied");
						Logger.debug("For Custom Fields, --- Maximum possible validations have been applied");

						driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
						}
					}
				}

			/*for (int iData = 0; iData<entityData.length; iData++) {
				String[] entityDataInput = entityData[iData].toString().split(Pattern.quote("||"));
				if (iData>1)
					break;

				new Select(driver.findElement(By.name("dynamicFieldValidations["+iData+"].validationType"))).selectByVisibleText(entityDataInput[0].trim());
				
				driver.findElement(By.name("dynamicFieldValidations["+iData+"].message")).sendKeys(entityDataInput[1].trim());

				driver.findElement(By.name("dynamicFieldValidations["+iData+"].parameter")).sendKeys(entityDataInput[2].trim());
				}*/
			}
		
		if (customFieldHTMLType.equalsIgnoreCase("Single Select") || customFieldHTMLType.equalsIgnoreCase("Multi Select")) {
			String[] entityData = customFieldDropdownOptions.split(";");

			for (int iPlus = 1; iPlus<entityData.length; iPlus++) {
				driver.findElement(By.className("optionsDiv")).findElement(By.className("tabs-inner-sec-header")).findElement(By.className("add-icon-form")).click();
				}
			
			for (int iData = 0; iData<entityData.length; iData++) {
				String[] entityDataInput = entityData[iData].toString().split(Pattern.quote("||"));
				driver.findElement(By.name("dynamicFieldOptionValues["+iData+"].name")).sendKeys(entityDataInput[0].trim());
				driver.findElement(By.name("dynamicFieldOptionValues["+iData+"].orderSeq")).sendKeys(entityDataInput[1].trim());

				if (entityDataInput[2].trim().equalsIgnoreCase("Yes")) {					
					if (!driver.findElement(By.name("dynamicFieldOptionValues["+iData+"].active")).isSelected())
						driver.findElement(By.name("dynamicFieldOptionValues["+iData+"].active")).click();
					}
				}
			}
		
		driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		//
		
		if (customFieldName.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._name_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Fields Name is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }
		
		if (customFieldName.length() > 50) {
			String entityErrors = driver.findElement(By.cssSelector("div._name_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 50 characters allowed");
			Logger.debug("For Custom Fields Name, --- Maximum 50 characters allowed");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }	
		
		if (customFieldLabel.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._displayName_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Fields Label is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }
		
		if (customFieldLabel.length() > 50) {
			String entityErrors = driver.findElement(By.cssSelector("div._displayName_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 50 characters allowed");
			Logger.debug("For Custom Fields Label, --- Maximum 50 characters allowed");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }	
		
		if (customFieldFieldOrder.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._orderSeq_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Fields Order is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		
		if (Integer.valueOf(convertStringToInteger(customFieldFieldOrder.trim()))>99999) {
			String entityErrors = driver.findElement(By.cssSelector("div._orderSeq_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum value is 99999");
			Logger.debug("For Custom Fields Order, --- Maximum value is 99999");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }
		
		if (customFieldEntityType.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._entityType_id_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Fields Entity Type is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (customFieldLayoutGroup.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._subHeader_id_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Fields Layout Group is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (customFieldHTMLType.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._htmlType_id_idformError.parentFormdynamicMetadata.formError"))
										.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Custom Fields HTML Type is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Custom Fields, " +customFieldName+" --- This name already exists in the system.");

			if (entityErrors.contains("This label already exists in the system."))
				Logger.debug("For Custom Fields, " +customFieldLabel+" --- This label already exists in the system.");

			if (entityErrors.contains("This field order already exists in the system."))
				Logger.debug("For Custom Fields, " +customFieldFieldOrder+" --- This group order already exists in the system.");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		if (driver.findElements(By.className("success-icon")).size()!=0) {
			String entityAlert = driver.findElement(By.className("success-icon")).getText();
			
			Assert.assertEquals(entityAlert, "Field created successfully");
			Logger.debug("Custom Fields created successfully with Name -- " +customFieldName);

			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(customFieldName);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[2][div[./text()='"+customFieldEntityType+"']]/preceding-sibling::td[1]/div/a")).click();
		
        String entityTypeNameShowPage = driver.findElement(By.id("_s_com_sirionlabs_business_entity_model_dynamicmetadata_name_name_id")).getText();
        Assert.assertEquals(entityTypeNameShowPage, customFieldName, "Custom Fields Name at show page is -- " +entityTypeNameShowPage+ " instead of -- " +customFieldName);

        String entityTypeLabelShowPage = driver.findElement(By.id("_s_com_sirionlabs_business_entity_model_dynamicmetadata_label_displayName_id")).getText();
        Assert.assertEquals(entityTypeLabelShowPage, customFieldLabel, "Custom Fields Label at show page is -- " +entityTypeLabelShowPage+ " instead of -- " +customFieldLabel);

        String entityTypeTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_business_entity_model_dynamicmetadata_entitytype_entityType.name_id")).getText();
        Assert.assertEquals(entityTypeTypeShowPage, customFieldEntityType, "Custom Fields Label at show page is -- " +entityTypeTypeShowPage+ " instead of -- " +customFieldEntityType);
		
        Logger.debug("Custom Field Created and Verified on Show Page successfully, with Name -- " +customFieldName);
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