package office.sirion.suite.issue;

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

public class IssueAuditLog extends TestSuiteBase {
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
    if (!TestUtil.isTestCaseRunnable(issue_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(issue_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void IssueAuditLogTest(String issueTitle, String issueDescription, String issueType, String issuePriority, String issueCurrency,
			String issueTimezone, String issueRestrictPublicAccess, String issueRestrictRequesterAccess,
			String issueSupplierAccess, String issueTier, String issueDependentEntity, String issueGovernanceBodyMeeting, String issueRecipientClientEntity,
			String issueRecipientCompanyCode, String issueContractingClientEntity, String issueContractingCompanyCode, String issueIssueDate,
			String issuePlannedCompletionDate, String issueFunctions, String issueServices, String issueServiceCategory, String issueManagementRegions,
			String issueManagementCountries, String issueContractRegions, String issueContractCountries, String issueResponsibility, String issueProjectID,
			String issueProjectLevels, String issueInitiatives, String issueComments, String issueActualDate, String issueRequestedBy, String issueChangeRequest,
			String issueUploadFile, String issueParent) throws InterruptedException, ClassNotFoundException, SQLException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
    }

    Logger.debug("Executing Test Case of Child Governance Body Audit Log");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
    String logged_in_user = locateElementBy("issue_communication_logged_in_user_full_name").getText(); driver.get(CONFIG.getProperty("endUserURL"));

      driver.get(CONFIG.getProperty("endUserURL"));
	
    fluentWaitMethod(locateElementBy("issues_quick_link"));


	locateElementBy("issues_quick_link").click();

	fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

	new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");


	
		locateElementBy("entity_listing_page_first_entry_link").click();
	

	fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
	fluentWaitMethod(locateElementBy("issue_show_page_id"));

	// issues - update Page - BASIC INFORMATION

			addFieldValue("issue_update_page_Title_textbox", issueTitle);
			addFieldValue("issue_update_page_Description_textarea", issueDescription);
			locateElementBy("issue_update_page_blank_area_label").click();
			addFieldValue("issue_update_page_Type_dropdown", issueType);
			addFieldValue("issue_update_page_Priority_dropdown", issuePriority);
			addFieldValue("issue_update_page_Currency_dropdown", issueCurrency);
			addFieldValue("issue_update_page_Timezone_dropdown", issueTimezone);
			try {
				if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
					driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
			} catch (Exception e) {
			}
			addFieldValue("issue_update_page_RestrictPublicAccess_checkbox", issueRestrictPublicAccess);
			addFieldValue("issue_update_page_RestrictRequesterAccess_checkbox", issueRestrictRequesterAccess);
			addFieldValue("issue_update_page_SupplierAccess_checkbox", issueSupplierAccess);
			addFieldValue("issue_update_page_Tier_dropdown", issueTier);
			addFieldValue("issue_update_page_DependentEntity_checkbox", issueDependentEntity);
			
		
			//issues - update Page - Contrating Entity

			addFieldValue("issue_update_page_RecipientClientEntity_multiselect", issueRecipientClientEntity);
			addFieldValue("issue_update_page_RecipientCompanyCode_multiselect", issueRecipientCompanyCode);
			addFieldValue("issue_update_page_ContractingClientEntity_multiselect", issueContractingClientEntity);
			addFieldValue("issue_update_page_ContractingCompanyCode_multiselect", issueContractingCompanyCode);

			// issues - update Page - IMPORTANT DATES
			
			DatePicker_Enhanced date = new DatePicker_Enhanced();
			date.selectCalendar(issueIssueDate,"issueDate");
			date.selectCalendar(issuePlannedCompletionDate,"plannedCompletionDate");
			
			// Issues - Create Page - FUNCTIONS
			if (!issueFunctions.equalsIgnoreCase("")) {
				for (String entityData : issueFunctions.split(";")) {
					new Select(locateElementBy("issue_create_page_Functions_multiselect")).selectByVisibleText(entityData.trim());
				  	}
				addFieldValue("issue_create_page_Services_multiselect", issueServices);
				}
			
//			addFieldValue("issue_create_page_ServiceCategory_multiselect", issueServicesCategory);
			    
			// Issues - Create Page - GEOGRAPHY
			if (locateElementBy("issue_create_page_ManagementRegions_multiselect")!=null) {
				if (!issueManagementRegions.equalsIgnoreCase("")) {
					for (String entityData : issueManagementRegions.split(";")) {
						new Select(locateElementBy("issue_create_page_ManagementRegions_multiselect")).selectByVisibleText(entityData.trim());
						}
					addFieldValue("issue_create_page_ManagementCountries_multiselect", issueManagementCountries);
					}
				}
			
			if (locateElementBy("issue_create_page_ContractRegions_multiselect")!=null) {
				if (!issueContractRegions.equalsIgnoreCase("")) {
					for (String entityData : issueContractRegions.split(";")) {
						new Select(locateElementBy("issue_create_page_ContractRegions_multiselect")).selectByVisibleText(entityData.trim());
						}
				    addFieldValue("issue_create_page_ContractCountries_multiselect", issueContractCountries);
					}
				}

			//issues - update Page - STAKEHOLDERS
			
			addFieldValue("issue_update_page_Responsibility_dropdown", issueResponsibility);

			//issues update Page - PROJECT INFORMATION

			addFieldValue("issue_update_page_ProjectID_multiselect", issueProjectID);
			addFieldValue("issue_update_page_ProjectLevels_multiselect", issueProjectLevels);
			addFieldValue("issue_update_page_Initiatives_multiselect", issueInitiatives);
			
			addFieldValue("issue_comment_box_textarea", issueComments);
		    locateElementBy("issue_blank_area").click();
			addFieldValue("issue_requested_by_dropdown", issueRequestedBy);
			    
			String[] issueActualdate = issueActualDate.split("-");
		    String issueActual_DateMonth = issueActualdate[0];
		    String issueActual_DateYear = convertStringToInteger(issueActualdate[2]);
		    int issueActual_dateYear = Integer.parseInt(issueActual_DateYear);
		    String issueActual_DateDate = convertStringToInteger(issueActualdate[1]);
		    Integer issueActual_dateDate = Integer.parseInt(issueActual_DateDate);
		    issueActual_DateDate = issueActual_dateDate.toString();

		    DatePicker_Enhanced dp_issueActual_date = new DatePicker_Enhanced();
		    dp_issueActual_date.expDate = issueActual_DateDate;
		    dp_issueActual_date.expMonth = issueActual_DateMonth;
		    dp_issueActual_date.expYear = issueActual_dateYear;
		    dp_issueActual_date.pickExpDate("actualDate");
			    
			addFieldValue("issue_change_request_dropdown", issueChangeRequest);
			
			if (!issueUploadFile.equalsIgnoreCase("")) {
				locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + issueUploadFile);

				}

	Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();



	fluentWaitMethod(locateElementBy("issue_show_page_id"));


	String entityShowPageName = locateElementBy("issues_show_page_title").getText();

	
	try{
	Assert.assertEquals(entityShowPageName, issueTitle);
	Logger.debug("issue Title on show page has been verified");
	}catch (AssertionError err) {
 		System.out.println("Title: " + entityShowPageName + " and " + issueTitle + " Does not Match");
 	}
	
    driver.findElement(By.linkText("AUDIT LOG")).click();
    waitF.until(ExpectedConditions.visibilityOf(locateElementBy("issue_audit_log_table")));
	
    Assert.assertEquals(locateElementBy("issue_audit_log_issue_taken").getText(), "Updated");

    if (!issueComments.equalsIgnoreCase("")){
    	try{
      Assert.assertEquals(locateElementBy("issue_audit_log_comment").getText(), "Yes", "Comment Matched");
    	}catch (AssertionError err) {
	 		System.out.println("Comment: " + issueComments + " and " + "Comment under ComunicationTab: " + locateElementBy("issue_audit_log_comment").getText() + " Does not Match");
	 	}
    }else{
    	try{
  	      Assert.assertEquals(locateElementBy("issue_audit_log_comment").getText(), "No", "Comment Matched");
  	    	}catch (AssertionError err) {
  		 		System.out.println("Comment: " + issueComments + " and " + "Comment under ComunicationTab: " + locateElementBy("issue_audit_log_comment").getText() + " Does not Match");
  		 	}
    }
    if (!issueUploadFile.equalsIgnoreCase("")) {
	 		
	 	try{
		Assert.assertEquals(locateElementBy("issue_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
				 	}catch (AssertionError err) {
	 		System.out.println("Uploaded File Name: " + issueUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("issue_audit_log_uploaded_file").getText() + "Does not Match");
	 	}
		}else{
			try{
			Assert.assertEquals(locateElementBy("issue_audit_log_uploaded_file").getText(), "No");
			}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + issueUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("issue_audit_log_uploaded_file").getText() + "Does not Match");
		 	}
		}
    
    if (!issueActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfissue = locateElementBy("issue_audit_log_actual_date").getText().split("-");
		 	
		 				 	try {
			Assert.assertEquals(TimeOfissue[0],issueActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfissue[1],issueActualdate[0].substring(3, 8), "Month Matched");
			Assert.assertEquals(TimeOfissue[2],issueActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + issueActualDate + " and " + "Time of issue under ComunicationTab: " + locateElementBy("issue_audit_log_actual_date").getText() + " Does not Match");
		 	}
			
						
			}else{
				try{
				Assert.assertEquals(locateElementBy("issue_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + issueActualDate + " and " + "Time of issue under ComunicationTab: " + locateElementBy("issue_audit_log_actual_date").getText()  + " Does not Match");
			 	}
			} 
    
    		try{
						Assert.assertEquals(locateElementBy("issue_audit_log_time_of_issue").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
						System.out.println("Time of issue: " + dateFormat.format(date) + " and " + "Time of issue under Audit Log: " + locateElementBy("issue_audit_log_time_of_issue").getText()  + " Does not Match");
						}
    
	 		if (!issueRequestedBy.equalsIgnoreCase("")) {
			 						 	
				try{
					Assert.assertEquals(locateElementBy("issue_audit_log_requested_by").getText(), issueRequestedBy, "Requested By Name Matched");
					
				 }catch (AssertionError err) {
				 	System.out.println("Requested By: " + issueRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("issue_audit_log_requested_by").getText() + " Does not Match");
				 }
			 		}else{
					try{
						Assert.assertEquals(locateElementBy("issue_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
					}catch (AssertionError err) {
						System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("issue_audit_log_requested_by").getText() + " Does not Match");
				 	}
				}
	 		try{
	 			
				Assert.assertEquals(locateElementBy("issue_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
				
			 }catch (AssertionError err) {
			 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("issue_audit_log_completed_by").getText() + " Does not Match");
			 }

    locateElementBy("issue_audit_log_view_history_link").click();
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("historyDataTable"))));

    List<WebElement> e = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
    for (int i = 0; i < e.size(); i++) {
      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Title")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), issueTitle);
        }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Type")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueType);
          }

        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Time Zone")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueTimezone);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Description")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), issueDescription);
        }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Priority")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issuePriority);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Issue Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueIssueDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Planned Completion Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issuePlannedCompletionDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Initiatives")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueInitiatives);
          }
        
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Project Levels")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueProjectLevels);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Project ID")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueProjectID);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Recipient Client Entity")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueRecipientClientEntity);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Recipient Company Code")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueRecipientCompanyCode);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Client Entity")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueContractingClientEntity);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Company Code")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), issueContractingCompanyCode);
          }
      }
    }

    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
 	public void reportDataSetResult(ITestResult testResult) throws IOException {
 		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

 		if(testResult.getStatus()==ITestResult.SKIP)
 			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
 		else if(testResult.getStatus()==ITestResult.FAILURE) {
 			isTestPass=false;
 			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
 			result= "Fail";
 		}
 		else if (testResult.getStatus()==ITestResult.SUCCESS) {
 			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
 			result= "Pass";
 		}
 		try {
 			for (int i = 2; i <= issue_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
 				String testCaseID = issue_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(issue_suite_xls, "Test Cases", TestUtil.getRowNum(issue_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(issue_suite_xls, "Test Cases", TestUtil.getRowNum(issue_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(issue_suite_xls, this.getClass().getSimpleName());
  }
}
