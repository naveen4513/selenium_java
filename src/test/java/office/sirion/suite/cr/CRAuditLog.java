package office.sirion.suite.cr;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
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

public class CRAuditLog extends TestSuiteBase {
  String runmodes[] = null;
  String result = null;
  static int count = -1;
  static boolean fail = true;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Date date = new Date();
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(cr_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(cr_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void CRAuditLogTest(String crTitle, String crDescription, String crContractReferences, String crTimezone, String crClass, String crType,
		  String crPriority, String crResponsibility, String crAssumptions, String crID, String crContractingEntity, String crSupplierAccess, String crTier,
		  String crDependentEntity, String crRecipientClientEntity, String crRecipientCompanyCode, String crContractingClientEntity, String crContractingCompanyCode,
		  String crCRDate, String crEffectiveDate, String crPlannedCompletionDate, String crFunctions, String crServices, String crServiceCategory,
		  String crManagementRegions, String crManagementCountries, String crContractRegions, String crContractCountries, String crOriginalTCV, String crOriginalTCVCurrency,  String crRevisedTCV,
		  String crRevisedTCVCurrency, String crVarianceTCV, String crVarianceTCVCurrency, String crOriginalACV, String crOriginalACVCurrency, String crRevisedACV,
		  String crRevisedACVCurrency, String crVarianceACV, String crVarianceACVCurrency, String crOriginalFACV, String crOriginalFACVCurrency, String crRevisedFACV,
		  String crRevisedFACVCurrency, String crVarianceFACV, String crVarianceFACVCurrency, String crProjectID, String crProjectLevels, String crInitiatives,
		  String crComments, String crActualDate, String crRequestedBy, String crChangeRequest, String crUploadFile,
		  String crParent)throws InterruptedException, ClassNotFoundException, SQLException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
    }

    Logger.debug("Executing Test Case of Child Governance Body Audit Log");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
    String logged_in_user = locateElementBy("cr_communication_logged_in_user_full_name").getText(); driver.get(CONFIG.getProperty("endUserURL"));

      driver.get(CONFIG.getProperty("endUserURL"));
	
    fluentWaitMethod(locateElementBy("cr_quick_link"));

    
    locateElementBy("cr_quick_link").click();

	fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
	
	new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

	
	if (!crParent.equalsIgnoreCase("")) {
		driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + crParent + "')]/preceding-sibling::td[2]/a")).click();
		} else {
			locateElementBy("entity_listing_page_first_entry_link").click();
			}

	fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
    driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
    fluentWaitMethod(locateElementBy("cr_create_page_title_textbox"));


    // Change Request - Edit Page - BASIC INFORMATION
    addFieldValue("cr_create_page_title_textbox", crTitle);

    addFieldValue("cr_create_page_description_textarea", crDescription);
/*	    
    if (!crSourceType.equalsIgnoreCase("Supplier"))
    	addFieldValue("cr_create_page_contract_references_textarea", crContractReferences);
*/
    addFieldValue("cr_create_page_timezone_dropdown", crTimezone);
    try {
    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
    	} catch (Exception e) {
    			}
    
    addFieldValue("cr_create_page_cr_class_dropdown", crClass);
    
    addFieldValue("cr_create_page_cr_type_dropdown", crType);
    
    addFieldValue("cr_create_page_priority_dropdown", crPriority);

    addFieldValue("cr_create_page_responsibility_dropdown", crResponsibility);
/*
    if (!crSourceType.equalsIgnoreCase("Supplier"))
    	addFieldValue("cr_create_page_assumptions_textarea", crAssumptions);
*/
    addFieldValue("cr_create_page_cr_id_textbox", crID);
    
    addFieldValue("cr_create_page_contracting_entity_dropdown", crContractingEntity);
    
//    addFieldValue("cr_create_page_supplier_access_checkbox", crSupplierAccess);

    addFieldValue("cr_create_page_tier_dropdown", crTier);
    
    addFieldValue("cr_create_page_dependent_entity_dropdown", crDependentEntity);
    
	// Change Request - Edit Page - CONTRACTING ENTITY
	addFieldValue("cr_create_page_recipient_client_entities_multi_dropdown", crRecipientClientEntity);
	
	addFieldValue("cr_create_page_recipient_company_codes_multi_dropdown", crRecipientCompanyCode);

	addFieldValue("cr_create_page_contracting_client_entities_multi_dropdown", crContractingClientEntity);

	addFieldValue("cr_create_page_contracting_company_codes_multi_dropdown", crContractingCompanyCode);

    // Change Request - Edit Page - IMPORTANT DATES   

	DatePicker_Enhanced date = new DatePicker_Enhanced();
	date.selectCalendar(crEffectiveDate,"effectiveDate");
	date.selectCalendar(crPlannedCompletionDate,"plannedCompletionDate");
	date.selectCalendar(crCRDate,"crDate");
    
	// Change Request - Edit Page - FUNCTIONS
	if (!crFunctions.equalsIgnoreCase("")) {
		for (String entityData : crFunctions.split(";")) {
			new Select(locateElementBy("cr_create_page_functions_multi_dropdown")).selectByVisibleText(entityData.trim());
		  	}
		addFieldValue("cr_create_page_services_multi_dropdown", crServices);
		}
	
	addFieldValue("cr_create_page_service_category_multi_dropdown", crServiceCategory);
	    
	// Change Request - Edit Page - GEOGRAPHY
	if (locateElementBy("cr_create_page_management_regions_multi_dropdown")!=null) {
		if (!crManagementRegions.equalsIgnoreCase("")) {
			for (String entityData : crManagementRegions.split(";")) {
				new Select(locateElementBy("cr_create_page_management_regions_multi_dropdown")).selectByVisibleText(entityData.trim());
				}
			addFieldValue("cr_create_page_management_countries_multi_dropdown", crManagementCountries);
			}
		}
	
	if (locateElementBy("cr_create_page_contract_regions_multi_dropdown")!=null) {
		if (!crContractRegions.equalsIgnoreCase("")) {
			for (String entityData : crContractRegions.split(";")) {
				new Select(locateElementBy("cr_create_page_contract_regions_multi_dropdown")).selectByVisibleText(entityData.trim());
				}
		    addFieldValue("cr_create_page_contract_countries_multi_dropdown", crContractCountries);
			}
		}

	// Change Request - Edit Page - FINANCIAL INFORMATION
   	addFieldValue("cr_create_page_original_tcv_numeric", crOriginalTCV);

	addFieldValue("cr_create_page_original_tcv_currency_dropdown", crOriginalTCVCurrency);

   	addFieldValue("cr_create_page_revised_tcv_numeric", crRevisedTCV);

	addFieldValue("cr_create_page_revised_tcv_currency_dropdown", crRevisedTCVCurrency);
	
   	addFieldValue("cr_create_page_variance_tcv_numeric", crVarianceTCV);

	addFieldValue("cr_create_page_variance_tcv_currency_dropdown", crVarianceTCVCurrency);
	
   	addFieldValue("cr_create_page_original_acv_numeric", crOriginalACV);

	addFieldValue("cr_create_page_original_acv_currency_dropdown", crOriginalACVCurrency);

   	addFieldValue("cr_create_page_revised_acv_numeric", crRevisedACV);

	addFieldValue("cr_create_page_revised_acv_currency_dropdown", crRevisedACVCurrency);
	
   	addFieldValue("cr_create_page_variance_acv_numeric", crVarianceACV);

	addFieldValue("cr_create_page_variance_acv_currency_dropdown", crVarianceACVCurrency);
	
   	addFieldValue("cr_create_page_original_facv_numeric", crOriginalFACV);

	addFieldValue("cr_create_page_original_facv_currency_dropdown", crOriginalFACVCurrency);

   	addFieldValue("cr_create_page_revised_facv_numeric", crRevisedFACV);

	addFieldValue("cr_create_page_revised_facv_currency_dropdown", crRevisedFACVCurrency);
	
   	addFieldValue("cr_create_page_variance_facv_numeric", crVarianceFACV);

	addFieldValue("cr_create_page_variance_facv_currency_dropdown", crVarianceFACVCurrency);

	// Change Request - Edit Page - PROJECT INFORMATION
	addFieldValue("cr_create_page_project_id_multi_dropdown", crProjectID);
	
	addFieldValue("cr_create_page_project_levels_multi_dropdown", crProjectLevels);

	addFieldValue("cr_create_page_initiatives_multi_dropdown", crInitiatives);

	// Change Request - Edit Page - COMMENTS AND ATTACHMENTS
	addFieldValue("entity_create_page_comments_textarea", crComments);
	    
	addFieldValue("entity_create_page_requested_by_dropdown", crRequestedBy);
	    
	addFieldValue("entity_create_page_change_request_dropdown", crChangeRequest);
	
	date.selectCalendar(crActualDate,"actualDate");
	    
	addFieldValue("cr_change_request_dropdown", crChangeRequest);
	
	if (!crUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\cr\\" + crUploadFile);

		}
	
    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
    driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

	
    fluentWaitMethod(locateElementBy("cr_show_page_title"));


	String entityShowPageName = locateElementBy("cr_show_page_title").getText();

	Assert.assertEquals(entityShowPageName, crTitle);
	Logger.debug("CR Title on show page has been verified");
	
	try{
		Assert.assertEquals(entityShowPageName, crTitle);
		Logger.debug("CR Title on show page has been verified");
	}catch (AssertionError err) {
 		System.out.println("Title: " + entityShowPageName + " and " + crTitle + " Does not Match");
 	}
	
    driver.findElement(By.linkText("AUDIT LOG")).click();
    waitF.until(ExpectedConditions.visibilityOf(locateElementBy("cr_audit_log_table")));
	
    Assert.assertEquals(locateElementBy("cr_audit_log_cr_taken").getText(), "Updated");

    if (!crComments.equalsIgnoreCase("")){
    	try{
      Assert.assertEquals(locateElementBy("cr_audit_log_comment").getText(), "Yes", "Comment Matched");
    	}catch (AssertionError err) {
	 		System.out.println("Comment: " + crComments + " and " + "Comment under ComunicationTab: " + locateElementBy("cr_audit_log_comment").getText() + " Does not Match");
	 	}
    }else{
    	try{
  	      Assert.assertEquals(locateElementBy("cr_audit_log_comment").getText(), "No", "Comment Matched");
  	    	}catch (AssertionError err) {
  		 		System.out.println("Comment: " + crComments + " and " + "Comment under ComunicationTab: " + locateElementBy("cr_audit_log_comment").getText() + " Does not Match");
  		 	}
    }
    if (!crUploadFile.equalsIgnoreCase("")) {
	 		
	 	try{
		Assert.assertEquals(locateElementBy("cr_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
				 	}catch (AssertionError err) {
	 		System.out.println("Uploaded File Name: " + crUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cr_audit_log_uploaded_file").getText() + "Does not Match");
	 	}
		}else{
			try{
			Assert.assertEquals(locateElementBy("cr_audit_log_uploaded_file").getText(), "No");
			}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + crUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cr_audit_log_uploaded_file").getText() + "Does not Match");
		 	}
		}
    
    if (!crActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfcr = locateElementBy("cr_audit_log_actual_date").getText().split("-");
		 	String[] crActualdate = crActualDate.split("-");
		 				 	try {
			Assertion(TimeOfcr[0],crActualdate[1]);
			Assertion(TimeOfcr[1],crActualdate[0]);
			Assertion(TimeOfcr[2],crActualdate[2]);
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + crActualDate + " and " + "Time of cr under ComunicationTab: " + locateElementBy("cr_audit_log_actual_date").getText() + " Does not Match");
		 	}
			
						
			}else{
				try{
				Assert.assertEquals(locateElementBy("cr_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + crActualDate + " and " + "Time of cr under ComunicationTab: " + locateElementBy("cr_audit_log_actual_date").getText()  + " Does not Match");
			 	}
			} 
    
    		try{
						Assert.assertEquals(locateElementBy("cr_audit_log_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
						System.out.println("Time of cr: " + dateFormat.format(date) + " and " + "Time of cr under Audit Log: " + locateElementBy("cr_audit_log_time_of_action").getText()  + " Does not Match");
						}
    
	 		if (!crRequestedBy.equalsIgnoreCase("")) {
			 						 	
				try{
					Assert.assertEquals(locateElementBy("cr_audit_log_requested_by").getText(), crRequestedBy, "Requested By Name Matched");
					
				 }catch (AssertionError err) {
				 	System.out.println("Requested By: " + crRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("cr_audit_log_requested_by").getText() + " Does not Match");
				 }
			 		}else{
					try{
						Assert.assertEquals(locateElementBy("cr_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
					}catch (AssertionError err) {
						System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("cr_audit_log_requested_by").getText() + " Does not Match");
				 	}
				}
	 		try{
	 			
				Assert.assertEquals(locateElementBy("cr_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
				
			 }catch (AssertionError err) {
			 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("cr_audit_log_completed_by").getText() + " Does not Match");
			 }

    locateElementBy("cr_audit_log_view_history_link").click();
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("historyDataTable"))));

    List<WebElement> e = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
    for (int i = 0; i < e.size(); i++) {
      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Title")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), crTitle);
        }

        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Description")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), crDescription);
        }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contract References")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crContractReferences);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Time Zone")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crPriority);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Class")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crClass);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Type")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crType);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Responsibility")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crResponsibility);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Assumptions")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crAssumptions);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("CR ID")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crID);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Entity")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crContractingEntity);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("CR Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assertion(element.get(3).getText(), crCRDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Effective Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assertion(element.get(3).getText(), crEffectiveDate);
          }
        
        /*else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Approval Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), cr);
          }*/
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Original ACV")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crOriginalACV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Revised ACV")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crRevisedACV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Variance")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crVarianceACV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Original TCV")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crOriginalTCV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Revised TCV")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crRevisedTCV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Variance")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crVarianceTCV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Original FACV")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crOriginalFACV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Revised FACV")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crRevisedFACV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Variance")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crVarianceFACV);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Project Levels")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crProjectLevels);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Planned Completion Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assertion(element.get(3).getText(), crPlannedCompletionDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Recipient Client Entity")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crRecipientClientEntity);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Recipient Company Code")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crRecipientCompanyCode);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Client Entity")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crContractingClientEntity);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Company Code")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), crContractingCompanyCode);
          }
        
      }
    }

    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= cr_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = cr_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(cr_suite_xls, "Test Cases", TestUtil.getRowNum(cr_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(cr_suite_xls, "Test Cases", TestUtil.getRowNum(cr_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(cr_suite_xls, this.getClass().getSimpleName());
  }
}
