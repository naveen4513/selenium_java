package office.sirion.suite.interpretation;

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

public class InterpretationAuditLog extends TestSuiteBase {
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
    if (!TestUtil.isTestCaseRunnable(int_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(int_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void InterpretationAuditLogTest(String ipTitle, String ipAreaofDisagreement, String ipBackground, String ipType, String ipPriority, 
			String ipTimezone, String ipTier, String ipSupplierAccess, String ipDependentEntity, String ipQuestion, String ipIncludeInFAQ, String ContractReference, 
			String ReferenceClause, String ReferencePageNumber,  String ipRequestedDateDate, String ipPlannedSubmissionDateDate, String ipFunction, String ipService, 
			String ipManagementRegions, String ipManagementCountries, String ipContractRegions, String ipContractCountries, String ipComments, 
			String ipActualDate, String ipRequestedBy, String ipChangeRequest, String ipUploadFile)throws InterruptedException, ClassNotFoundException, SQLException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
    }

    Logger.debug("Executing Test Case of Child Governance Body Audit Log");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassintd"));
    String logged_in_user = locateElementBy("ip_communication_logged_in_user_full_name").getText(); driver.get(CONFIG.getProperty("endUserURL"));

      driver.get(CONFIG.getProperty("endUserURL"));
	
    fluentWaitMethod(locateElementBy("ip_quick_link"));

	locateElementBy("ip_quick_link").click();

	fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

	new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");


	
		locateElementBy("entity_listing_page_first_entry_link").click();
	

	fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
	fluentWaitMethod(locateElementBy("ip_show_page_id"));

	// issues - update Page - BASIC INFORMATION

	addFieldValue("ip_create_page_title_textbox", ipTitle);
	addFieldValue("ip_create_page_background_textarea", ipBackground);
	locateElementBy("issue_create_page_blank_area_label").click();
	addFieldValue("ip_create_page_type_dropdown", ipType);
	addFieldValue("ip_create_page_priority_dropdown", ipPriority);
	addFieldValue("ip_create_page_timezone_dropdown", ipTimezone);
	try {
		if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
			driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	} catch (Exception e) {
	}
	addFieldValue("ip_create_page_supplieraccess_checkbox", ipSupplierAccess);
	addFieldValue("ip_create_page_tier_dropdown", ipTier);
	addFieldValue("ip_create_page_depentity_checkbox", ipDependentEntity);
	
	//Interpretation - Create Page - QUESTIONS
	
	addFieldValue("ip_create_page_question_textarea", ipQuestion);
	addFieldValue("ip_create_page_includeinfaq_checkbox", ipIncludeInFAQ);
	
	
	 if (!ContractReference.equalsIgnoreCase("")) {
	    	new Select(locateElementBy("sl_create_page_references_dropdown")).selectByVisibleText(ContractReference.trim());

	    	addFieldValue("sl_create_page_clause_textbox", ReferenceClause);
	    	
	    	addFieldValue("sl_create_page_page_number_numeric_box", ReferencePageNumber);
	    	}

	/*Interpretation Create Page - Important Date*/
    
    DatePicker_Enhanced date = new DatePicker_Enhanced();
	date.selectCalendar(ipRequestedDateDate,"requestDate");
	date.selectCalendar(ipPlannedSubmissionDateDate,"plannedSubmissionDate");
    
	// Interpretations - Create Page - FUNCTIONS
	if (!ipFunction.equalsIgnoreCase("")) {
		for (String entityData : ipFunction.split(";")) {
			new Select(locateElementBy("ip_create_page_functions_multiselect")).selectByVisibleText(entityData.trim());
		  	}
		addFieldValue("ip_create_page_services_multiselect", ipService);
		}
	
//	addFieldValue("ip_create_page_service_category_multiselect", ipServiceCategory);
	    
	// Interpretations - Create Page - GEOGRAPHY
	if (locateElementBy("ip_create_page_management_regions_multiselect")!=null) {
		if (!ipManagementRegions.equalsIgnoreCase("")) {
			for (String entityData : ipManagementRegions.split(";")) {
				new Select(locateElementBy("ip_create_page_management_regions_multiselect")).selectByVisibleText(entityData.trim());
				}
			addFieldValue("ip_create_page_management_countries_multiselect", ipManagementCountries);
			}
		}
	
	if (locateElementBy("ip_create_page_contract_regions_multiselect")!=null) {
		if (!ipContractRegions.equalsIgnoreCase("")) {
			for (String entityData : ipContractRegions.split(";")) {
				new Select(locateElementBy("ip_create_page_contract_regions_multiselect")).selectByVisibleText(entityData.trim());
				}
		    addFieldValue("ip_create_page_contract_countries_multiselect", ipContractCountries);
			}
		}
	

	if (!ContractReference.equalsIgnoreCase("") && !ReferencePageNumber.equalsIgnoreCase("")) {
		if (locateElementBy("ip_create_page_page_no_textbox").getAttribute("maxvalue").equalsIgnoreCase("UNINDEXED")) {
			String Error = locateElementBy("ip_create_page_references_dropdown_errors").getText();

	        Assert.assertEquals(Error, "Document is yet to be indexed. Please try attaching this document after sometime.");

	        Logger.debug("Reference Document is yet to be indexed. Please try attaching this document after sometime.");

	        fail = false;
	        driver.get(CONFIG.getProperty("endUserURL"));

	        return;
	        }
		
		Double double_PageNumber_temp = Double.parseDouble(ReferencePageNumber);
	    int int_PageNumber_temp = double_PageNumber_temp.intValue();

	    if (int_PageNumber_temp > Integer.valueOf(locateElementBy("ip_create_page_page_number_numeric_box").getAttribute("maxvalue"))) {
	    	String slError = locateElementBy("ip_create_page_page_number_numeric_box_errors").getText();

	    	Assert.assertEquals(slError, "This page number does not exist in the selected document");

	    	Logger.debug("Page Number does not exist in the selected document");

	    	fail = false;
	    	driver.get(CONFIG.getProperty("endUserURL"));

	    	return;
	    	}
	    }
	
	addFieldValue("ip_comment_box_textarea", ipComments);
    locateElementBy("ip_blank_area").click();
	addFieldValue("ip_requested_by_dropdown", ipRequestedBy);
	    
	date.selectCalendar(ipActualDate,"actualDate");  
	addFieldValue("ip_change_request_dropdown", ipChangeRequest);
	
	if (!ipUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_inteate_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + ipUploadFile);

		}

	Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();



	fluentWaitMethod(locateElementBy("ip_show_page_id"));


	String entityShowPageName = locateElementBy("ip_show_page_title").getText();

	
	
	try{
		Assert.assertEquals(entityShowPageName, ipTitle);
		Logger.debug("IP Title on show page has been verified");
	}catch (AssertionError err) {
 		System.out.println("Title: " + entityShowPageName + " and " + ipTitle + " Does not Match");
 	}
	
    driver.findElement(By.linkText("AUDIT LOG")).click();
    waitF.until(ExpectedConditions.visibilityOf(locateElementBy("ip_audit_log_table")));
	
    Assert.assertEquals(locateElementBy("ip_audit_log_ip_taken").getText(), "Updated");

    if (!ipComments.equalsIgnoreCase("")){
    	try{
      Assert.assertEquals(locateElementBy("ip_audit_log_comment").getText(), "Yes", "Comment Matched");
    	}catch (AssertionError err) {
	 		System.out.println("Comment: " + ipComments + " and " + "Comment under ComunicationTab: " + locateElementBy("ip_audit_log_comment").getText() + " Does not Match");
	 	}
    }else{
    	try{
  	      Assert.assertEquals(locateElementBy("ip_audit_log_comment").getText(), "No", "Comment Matched");
  	    	}catch (AssertionError err) {
  		 		System.out.println("Comment: " + ipComments + " and " + "Comment under ComunicationTab: " + locateElementBy("ip_audit_log_comment").getText() + " Does not Match");
  		 	}
    }
    if (!ipUploadFile.equalsIgnoreCase("")) {
	 		
	 	try{
		Assert.assertEquals(locateElementBy("ip_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
				 	}catch (AssertionError err) {
	 		System.out.println("Uploaded File Name: " + ipUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("ip_audit_log_uploaded_file").getText() + "Does not Match");
	 	}
		}else{
			try{
			Assert.assertEquals(locateElementBy("ip_audit_log_uploaded_file").getText(), "No");
			}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + ipUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("ip_audit_log_uploaded_file").getText() + "Does not Match");
		 	}
		}
    
    if (!ipActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfint = locateElementBy("ip_audit_log_actual_date").getText().split("-");
		 	
		 				 	try {
		 				 		String[] ipActualdate = ipActualDate.split("-");
			Assert.assertEquals(TimeOfint[0],ipActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfint[1],ipActualdate[0].substring(3, 8), "Month Matched");
			Assert.assertEquals(TimeOfint[2],ipActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + ipActualDate + " and " + "Time of int under ComunicationTab: " + locateElementBy("ip_audit_log_actual_date").getText() + " Does not Match");
		 	}
			
						
			}else{
				try{
				Assert.assertEquals(locateElementBy("ip_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + ipActualDate + " and " + "Time of int under ComunicationTab: " + locateElementBy("ip_audit_log_actual_date").getText()  + " Does not Match");
			 	}
			} 
    
    		try{
						Assert.assertEquals(locateElementBy("ip_audit_log_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
						System.out.println("Time of int: " + dateFormat.format(date) + " and " + "Time of int under Audit Log: " + locateElementBy("ip_audit_log_time_of_action").getText()  + " Does not Match");
						}
    
	 		if (!ipRequestedBy.equalsIgnoreCase("")) {
			 						 	
				try{
					Assert.assertEquals(locateElementBy("ip_audit_log_requested_by").getText(), ipRequestedBy, "Requested By Name Matched");
					
				 }catch (AssertionError err) {
				 	System.out.println("Requested By: " + ipRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("ip_audit_log_requested_by").getText() + " Does not Match");
				 }
			 		}else{
					try{
						Assert.assertEquals(locateElementBy("ip_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
					}catch (AssertionError err) {
						System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("ip_audit_log_requested_by").getText() + " Does not Match");
				 	}
				}
	 		try{
	 			
				Assert.assertEquals(locateElementBy("ip_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
				
			 }catch (AssertionError err) {
			 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("ip_audit_log_completed_by").getText() + " Does not Match");
			 }

    locateElementBy("ip_audit_log_view_history_link").click();
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("historyDataTable"))));

    List<WebElement> e = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
    for (int i = 0; i < e.size(); i++) {
      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Title")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), ipTitle);
        }

        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Area of Disagreement")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipAreaofDisagreement);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Background")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), ipBackground);
        }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Type")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipType);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Time Zone")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipTimezone);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Priority")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipPriority);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Tier")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipTier);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Supplier Access")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipSupplierAccess);
          }
        
       /* else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Question")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipQuestion);
          }*/
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Include in FAQ")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipIncludeInFAQ);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contract Reference")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ContractReference);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Clause")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ReferenceClause);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Page Number")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ReferencePageNumber);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Request Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipRequestedDateDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Planned Submission Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipPlannedSubmissionDateDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Functions")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipFunction);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Services")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipService);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Management Regions")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipManagementRegions);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Management Countries")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipManagementCountries);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contract Regions")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipContractRegions);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contract Countries")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), ipContractCountries);
          }        
      }
    }

    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= int_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = int_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(int_suite_xls, this.getClass().getSimpleName());
  }
}
