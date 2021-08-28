package office.sirion.suite.wor;

import java.io.IOException;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import office.sirion.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

public class WORAuditLog extends TestSuiteBase {
  String runmodes[] = null;
  String result = null;
  static int count = -1;
  static boolean fail = true;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Calendar c = Calendar.getInstance();
  static Date date = new Date();
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(wor_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(wor_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void WORAuditLogTest(String worTitle, String worDescription, String worContractReferences, String worTimezone, String worClass, String worType,
		  String worPriority, String worResponsibility, String worAssumptions, String worID, String worContractingEntity, String worSupplierAccess, String worTier,
		  String worDependentEntity, String worRecipientClientEntity, String worRecipientCompanyCode, String worContractingClientEntity, String worContractingCompanyCode,
		  String worEffectiveDate, String worExpirationDate, String worPlannedCompletionDate,String worRequestDate, String worFunctions, String worServices, String worServiceCategory,
		  String worManagementRegions, String worManagementCountries, String worContractRegions, String worContractCountries, String worAddtionalTCV, String worAddtionalFACV,  String worAdditionalACV,
		  String worProjectID, String worProjectLevels, String worInitiatives, String worComments, String worActualDate, String worRequestedBy, String worChangeRequest, String worUploadFile,
		  String worParent)throws InterruptedException, ClassNotFoundException, SQLException {

      count++;
      if (!runmodes[count].equalsIgnoreCase("Y")) {
          skip = true;
          throw new SkipException("Runmode for Test Set Data Set to NO " + count);
      }

      Logger.debug("Executing Test Case of Child Governance Body Audit Log");

      // Launch The Browser
      openBrowser();
      endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));

      fluentWaitMethod(locateElementBy("wor_quick_link"));

      String logged_in_user = locateElementBy("wor_communication_logged_in_user_full_name").getText();

      locateElementBy("wor_quick_link").click();

      fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

      new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
      if (!worParent.equalsIgnoreCase("")) {
          driver.findElement(By.xpath(".//tbody/tr[@role='row']/td[contains(.,'" + worParent + "')]/preceding-sibling::td[5]/a")).click();
      } else {
          locateElementBy("entity_listing_page_first_entry_link_wor").click();
      }

      fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
      Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
      driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
      fluentWaitMethod(locateElementBy("wor_create_page_title_textbox"));

      Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage = TestAuditLogUtil.getEntityMapBeforeUpdate();

      // Change Request - Edit Page - BASIC INFORMATION
      addFieldValue("wor_create_page_title_textbox", worTitle);

      addFieldValue("wor_create_page_brief_textarea", worDescription);
/*	    
    if (!worSourceType.equalsIgnoreCase("Supplier"))
    	addFieldValue("wor_create_page_contract_references_textarea", worContractReferences);
*/
      addFieldValue("wor_create_page_timezone_dropdown", worTimezone);
      try {
          if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
              driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
      } catch (Exception e) {
      }

      addFieldValue("wor_create_page_wor_class_dropdown", worClass);

      //addFieldValue("wor_create_page_wor_type_dropdown", worType);

      addFieldValue("wor_create_page_priority_dropdown", worPriority);

      addFieldValue("wor_create_page_responsibility_dropdown", worResponsibility);
/*
    if (!worSourceType.equalsIgnoreCase("Supplier"))
    	addFieldValue("wor_create_page_assumptions_textarea", worAssumptions);
*/
      //addFieldValue("wor_create_page_wor_id_textbox", worID);

      addFieldValue("wor_create_page_contracting_entity_dropdown", worContractingEntity);

//    addFieldValue("wor_create_page_supplier_access_checkbox", worSupplierAccess);

      addFieldValue("wor_create_page_tier_dropdown", worTier);

      addFieldValue("wor_create_page_dependent_entity_dropdown", worDependentEntity);

      // Change Request - Edit Page - CONTRACTING ENTITY
      addFieldValue("wor_create_page_recipient_client_entities_multiselect", worRecipientClientEntity);

      addFieldValue("wor_create_page_recipient_company_codes_multiselect", worRecipientCompanyCode);

      addFieldValue("wor_create_page_contracting_client_entities_multiselect", worContractingClientEntity);

      addFieldValue("wor_create_page_contracting_company_codes_multiselect", worContractingCompanyCode);

      // Change Request - Edit Page - IMPORTANT DATES

      DatePicker_Enhanced date = new DatePicker_Enhanced();
      date.selectCalendar(worEffectiveDate, "effectiveDate");
      date.selectCalendar(worExpirationDate, "expirationDate");
      date.selectCalendar(worPlannedCompletionDate, "plannedCompletionDate");
      date.selectCalendar(worRequestDate, "requestDate");

      // Change Request - Edit Page - FUNCTIONS
      if (!worFunctions.equalsIgnoreCase("")) {
          for (String entityData : worFunctions.split(";")) {
              addFieldValue("wor_create_page_function_multiselect", entityData.trim());
          }
          addFieldValue("wor_create_page_service_multiselect", worServices);
      }

      addFieldValue("wor_create_page_service_category_multiselect", worServiceCategory);

      // Change Request - Edit Page - GEOGRAPHY
      if (locateElementBy("wor_create_page_management_regions_multi_dropdown") != null) {
          if (!worManagementRegions.equalsIgnoreCase("")) {
              for (String entityData : worManagementRegions.split(";")) {
                  addFieldValue("wor_create_page_management_regions_multi_dropdown", entityData.trim());
              }
              addFieldValue("wor_create_page_management_countries_multi_dropdown", worManagementCountries);
          }
      }

      if (locateElementBy("wor_create_page_contract_regions_multi_dropdown") != null) {
          if (!worContractRegions.equalsIgnoreCase("")) {
              for (String entityData : worContractRegions.split(";")) {
                  addFieldValue("wor_create_page_contract_regions_multi_dropdown",entityData.trim());
              }
              addFieldValue("wor_create_page_contract_countries_multi_dropdown", worContractCountries);
          }
      }

      // Change Request - Edit Page - FINANCIAL INFORMATION
      addFieldValue("wor_create_page_additional_TCV_textbox", worAddtionalTCV);

      addFieldValue("wor_create_page_additional_FACV_textbox", worAddtionalFACV);

      addFieldValue("wor_create_page_additional_ACV_textbox", worAdditionalACV);

      // Change Request - Edit Page - PROJECT INFORMATION
      addFieldValue("wor_create_page_project_id_multiselect", worProjectID);

      addFieldValue("wor_create_page_project_levels_multiselect", worProjectLevels);

      addFieldValue("wor_create_page_initiatives_multiselect", worInitiatives);

      // Change Request - Edit Page - COMMENTS AND ATTACHMENTS
      addFieldValue("entity_create_page_comments_textarea", worComments);
      locateElementBy("wor_comment_attachment").click();

      addFieldValue("entity_create_page_requested_by_dropdown", worRequestedBy);

      addFieldValue("entity_create_page_change_request_dropdown", worChangeRequest);

      date.selectCalendar(worActualDate, "actualDate");

      //addFieldValue("wor_change_request_dropdown", worChangeRequest);

      if (!worUploadFile.equalsIgnoreCase("")) {
          locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\wor\\" + worUploadFile);

      }

     Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = TestAuditLogUtil.getEntityMapAfterUpdate(entityMapBeforeUpdatePage);

      Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
      driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


      //waitF.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(locateElementBy("wor_show_page_title"))));


      String entityShowPageName = locateElementBy("wor_show_page_title").getText();

      Assertion(entityShowPageName, worTitle);
      Logger.debug("wor Title on show page has been verified");

      try {
          Assert.assertEquals(entityShowPageName, worTitle);
          Logger.debug("wor Title on show page has been verified");
      } catch (AssertionError err) {
          System.out.println("Title: " + entityShowPageName + " and " + worTitle + " Does not Match");
      }

      driver.findElement(By.linkText("AUDIT LOG")).click();
      waitF.until(ExpectedConditions.visibilityOf(locateElementBy("wor_audit_log_table")));

      Assert.assertEquals(locateElementBy("wor_audit_log_wor_taken").getText(), "Updated");

      if (!worComments.equalsIgnoreCase("")) {
          try {
              Assert.assertEquals(locateElementBy("wor_audit_log_comment").getText(), "Yes", "Comment Matched");
          } catch (AssertionError err) {
              System.out.println("Comment: " + worComments + " and " + "Comment under ComunicationTab: " + locateElementBy("wor_audit_log_comment").getText() + " Does not Match");
          }
      } else {
          try {
              Assert.assertEquals(locateElementBy("wor_audit_log_comment").getText(), "No", "Comment Matched");
          } catch (AssertionError err) {
              System.out.println("Comment: " + worComments + " and " + "Comment under ComunicationTab: " + locateElementBy("wor_audit_log_comment").getText() + " Does not Match");
          }
      }
      if (!worUploadFile.equalsIgnoreCase("")) {

          try {
              Assert.assertEquals(locateElementBy("wor_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
          } catch (AssertionError err) {
              System.out.println("Uploaded File Name: " + worUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("wor_audit_log_uploaded_file").getText() + "Does not Match");
          }
      } else {
          try {
              Assert.assertEquals(locateElementBy("wor_audit_log_uploaded_file").getText(), "No");
          } catch (AssertionError err) {
              System.out.println("Uploaded File Name: " + worUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("wor_audit_log_uploaded_file").getText() + "Does not Match");
          }
      }

      if (!worActualDate.equalsIgnoreCase("")) {

          String[] TimeOfwor = locateElementBy("wor_audit_log_actual_date").getText().split("-");
          String[] worActualdate = worActualDate.split("-");

          try {
              Assert.assertEquals(TimeOfwor[0], worActualdate[1], "Date Matched");
              Assert.assertEquals(TimeOfwor[1], worActualdate[0].substring(3, 8), "Month Matched");
              Assert.assertEquals(TimeOfwor[2], worActualdate[2], "Year Matched");
          } catch (AssertionError err) {
              System.out.println("Actual Date: " + worActualDate + " and " + "Time of wor under ComunicationTab: " + locateElementBy("wor_audit_log_actual_date").getText() + " Does not Match");
          }
      } else {
          try {
             Assert.assertEquals(locateElementBy("wor_audit_log_actual_date").getText(), dateFormat.format(c.getTime()), "Date, Month and Year Matched");
          } catch (AssertionError err) {
              System.out.println("Actual Date: " + worActualDate + " and " + "Time of wor under ComunicationTab: " + locateElementBy("wor_audit_log_actual_date").getText() + " Does not Match");
          }
      }

      try {
          Assert.assertEquals(locateElementBy("wor_audit_log_time_of_action").getText(), dateFormat.format(c.getTime()), "Date, Month and Year Matched");
      } catch (AssertionError err) {
          System.out.println("Time of wor: " + dateFormat.format(c.getTime()) + " and " + "Time of wor under Audit Log: " + locateElementBy("wor_audit_log_time_of_action").getText() + " Does not Match");
      }

      if (!worRequestedBy.equalsIgnoreCase("")) {

          try {
              Assert.assertEquals(locateElementBy("wor_audit_log_requested_by").getText(), worRequestedBy, "Requested By Name Matched");

          } catch (AssertionError err) {
              System.out.println("Requested By: " + worRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("wor_audit_log_requested_by").getText() + " Does not Match");
          }
      } else {
          try {
              Assert.assertEquals(locateElementBy("wor_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
          } catch (AssertionError err) {
              System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("wor_audit_log_requested_by").getText() + " Does not Match");
          }
      }
      try {

          Assert.assertEquals(locateElementBy("wor_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");

      } catch (AssertionError err) {
          System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("wor_audit_log_completed_by").getText() + " Does not Match");
      }

      locateElementBy("wor_audit_log_view_history_link").click();
      waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("historyDataTable"))));

      Thread.sleep(300);
      fluentWaitMethod(driver.findElement(By.id("historyDataTable")));

      Map<String, Map<String, String>> auditLogDataMap = TestAuditLogUtil.getAuditLogViewHistoryData();

      TestAuditLogUtil.isAuditLogWorking(entityMapAfterUpdatePage, auditLogDataMap);


      driver.get(CONFIG.getProperty("endUserURL"));

    }

  @AfterMethod
 	public void reportDataSetResult(ITestResult testResult) throws IOException {
 		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

 		if(testResult.getStatus()==ITestResult.SKIP)
 			TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
 		else if(testResult.getStatus()==ITestResult.FAILURE) {
 			isTestPass=false;
 			TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
 			result= "Fail";
 		}
 		else if (testResult.getStatus()==ITestResult.SUCCESS) {
 			TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
 			result= "Pass";
 		}
 		try {
 			for (int i = 2; i <= wor_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
 				String testCaseID = wor_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil.getRowNum(wor_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil.getRowNum(wor_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(wor_suite_xls, this.getClass().getSimpleName());
  }
}
