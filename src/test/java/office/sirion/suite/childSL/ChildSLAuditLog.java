	package office.sirion.suite.childSL;
	
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
	
	public class ChildSLAuditLog extends TestSuiteBase {
	  String runmodes[] = null;
	  static int count = -1;
	  static boolean fail = true;
	  static boolean skip = false;
	  static boolean isTestPass = true;
	  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
	  static Date date = new Date();
	  String testCaseID=null;
	  String result=null;
	  
	  @BeforeTest
	  public void checkTestSkip() {
	    if (!TestUtil.isTestCaseRunnable(child_sl_suite_xls, this.getClass().getSimpleName())) {
	      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
	      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
	    }
	    runmodes = TestUtil.getDataSetRunmodes(child_sl_suite_xls, this.getClass().getSimpleName());
	  }
	
	  @Test(dataProvider = "getTestData")
	  public void ChildSLAuditLogTest(String cslSupplierNumerator, String cslActualNumerator, String cslSupplierDenominator, String cslActualDenominator,
	      String cslSupplierCalculation, String cslActualPerformance, String cslDiscrepancy, String cslDiscrepancyResolutionStatus, String cslFinalNumerator,
	      String cslFinalDenominator, String cslFinalPerformance, String cslApplicableCredit, String cslApplicableEarnback, String cslDiscrepancy1, String cslDiscrepancystatus, String cslInvoiceNumber, 
	      String csldiscrepancyValue, String cslImpactDays, String cslImpactType, String cslComments, String cslActualDate, String cslRequestedBy, String cslChangeRequest,
	      String cslUploadFile) throws InterruptedException, ClassNotFoundException, SQLException {
	
	    count++;
	    if (!runmodes[count].equalsIgnoreCase("Y")) {
	      skip = true;
	      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
	    }
	
	    Logger.debug("Executing Test Case of Child Service Level Audit Log");
	
	    // Launch The Browser
	    openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPasscsld"));
	
	    driver.get(CONFIG.getProperty("endUserURL"));

	    String logged_in_user = locateElementBy("csl_communication_logged_in_user_full_name").getText(); 
	   
	    locateElementBy("sl_quick_link");
	    locateElementBy("sl_quick_link").click();
	    locateElementBy("csl_link_listing_page");
	
	    locateElementBy("csl_link_listing_page").click();

	    locateElementBy("global_bulk_listing_page_first_entry_link").click();
	    locateElementBy("csl_edit_button");
	
	    Assert.assertNotNull(locateElementBy("csl_edit_button"));
          locateElementBy("csl_edit_button").click();

	
	    addFieldValue("csl_edit_page_supplier_numerator_numeric_box", cslSupplierNumerator);
	    
	
	    addFieldValue("csl_edit_page_actual_numerator_numeric_box", cslActualNumerator);
	    
	
	    addFieldValue("csl_edit_page_supplier_denominator_numeric_box", cslSupplierDenominator);
	    
	
	    addFieldValue("csl_edit_page_actual_denominator_numeric_box", cslActualDenominator);
	    
	
	    addFieldValue("csl_edit_page_supplier_calculation_numeric_box", cslSupplierCalculation);
	    
	
	    addFieldValue("csl_edit_page_actual_performance_numeric_box", cslActualPerformance);
	    
	
	    addFieldValue("csl_edit_page_discrepancy_textarea", cslDiscrepancy);
	    
	    addFieldValue("csl_edit_page_discrepancy_value_numeric_box", csldiscrepancyValue);
	    
	
	    addFieldValue("csl_edit_page_discrepancy_resolution_status_textarea", cslDiscrepancyResolutionStatus);
	    
	
	    addFieldValue("csl_edit_page_final_numerator_numeric_box", cslFinalNumerator);
	    
	    addFieldValue("csl_edit_page_final_denominator_numeric_box", cslFinalDenominator);
	    
	
	    addFieldValue("csl_edit_page_final_performance_numeric_box", cslFinalPerformance);
	    locateElementBy("csl_edit_page_final_performance_numeric_box").sendKeys(Keys.TAB);
	    
	
	    // Child Service Levels - Create Page - COMMENTS AND ATTACHMENTS
	    addFieldValue("csl_edit_page_comments_textarea", cslComments);
	    
	
	    addFieldValue("csl_edit_page_requested_by_dropdown", cslRequestedBy);
	    DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(cslActualDate,"actualDate");
	
	    addFieldValue("csl_edit_page_change_request_dropdown",cslChangeRequest);
	    
	
	    if (!cslUploadFile.equalsIgnoreCase("")) {
	      locateElementBy("csl_edit_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\Child SL\\" + cslUploadFile);
	      
	    }

	    Assert.assertNotNull(locateElementBy("csl_update_button"));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("csl_update_button"));
          locateElementBy("csl_update_button").click();

	    locateElementBy("csl_audit_log").click();
	    waitF.until(ExpectedConditions.visibilityOf(locateElementBy("csl_audit_log_inner_part")));
	
	    Assert.assertEquals(locateElementBy("csl_audit_log_action_taken").getText(), "Updated");
	
	    if (!cslComments.equalsIgnoreCase("")){
	    	try{
	      Assert.assertEquals(locateElementBy("csl_audit_log_comment").getText(), "Yes", "Comment Matched");
	    	}catch (AssertionError err) {
		 		System.out.println("Comment: " + cslComments + " and " + "Comment under ComunicationTab: " + locateElementBy("csl_audit_log_comment").getText() + " Does not Match");
		 	}
	    }else{
	    	try{
	  	      Assert.assertEquals(locateElementBy("csl_audit_log_comment").getText(), "No", "Comment Matched");
	  	    	}catch (AssertionError err) {
	  		 		System.out.println("Comment: " + cslComments + " and " + "Comment under ComunicationTab: " + locateElementBy("csl_audit_log_comment").getText() + " Does not Match");
	  		 	}
	    }
	    if (!cslUploadFile.equalsIgnoreCase("")) {
		 		
		 	try{
			Assert.assertEquals(locateElementBy("csl_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
					 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + cslUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("csl_audit_log_uploaded_file").getText() + "Does not Match");
		 	}
			}else{
				try{
				Assert.assertEquals(locateElementBy("csl_audit_log_uploaded_file").getText(), "No");
				}catch (AssertionError err) {
			 		System.out.println("Uploaded File Name: " + cslUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("csl_audit_log_uploaded_file").getText() + "Does not Match");
			 	}
			}
	    
	    if (!cslActualDate.equalsIgnoreCase("")) {
			 
			 String[] TimeOfcsl = locateElementBy("csl_audit_log_actual_date").getText().split("-");
			 	
			try {
				String[] entityDate = cslActualDate.split("-");
				Assert.assertEquals(TimeOfcsl[0],entityDate[1], "Date Matched");
				Assert.assertEquals(TimeOfcsl[1],entityDate[0].substring(3, 8), "Month Matched");
				Assert.assertEquals(TimeOfcsl[2],entityDate[2], "Year Matched");
			 	}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + cslActualDate + " and " + "Time of csl under ComunicationTab: " + locateElementBy("csl_audit_log_actual_date").getText() + " Does not Match");
			 	}
				
							
				}else{
					try{
					Assert.assertEquals(locateElementBy("csl_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
					}catch (AssertionError err) {
				 		System.out.println("Actual Date: " + cslActualDate + " and " + "Time of csl under ComunicationTab: " + locateElementBy("csl_audit_log_actual_date").getText()  + " Does not Match");
				 	}
				} 
	    
	    		try{
							Assert.assertEquals(locateElementBy("csl_audit_log_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
							}catch (AssertionError err) {
							System.out.println("Time of csl: " + dateFormat.format(date) + " and " + "Time of csl under Audit Log: " + locateElementBy("csl_audit_log_time_of_action").getText()  + " Does not Match");
							}
	    
		 		if (!cslRequestedBy.equalsIgnoreCase("")) {
				 						 	
					try{
						Assert.assertEquals(locateElementBy("csl_audit_log_requested_by").getText(), cslRequestedBy, "Requested By Name Matched");
						
					 }catch (AssertionError err) {
					 	System.out.println("Requested By: " + cslRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("csl_audit_log_requested_by").getText() + " Does not Match");
					 }
				 		}else{
						try{
							Assert.assertEquals(locateElementBy("csl_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
						}catch (AssertionError err) {
							System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("csl_audit_log_requested_by").getText() + " Does not Match");
					 	}
					}
		 		try{
		 			
					Assert.assertEquals(locateElementBy("csl_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
					
				 }catch (AssertionError err) {
				 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("csl_audit_log_completed_by").getText() + " Does not Match");
				 }
	
	    locateElementBy("csl_audit_log_view_history_link").click();
	    locateElementBy("entity_audit_log_history_table");
	
	    List<WebElement> e = locateElementBy("entity_audit_log_history_table").findElement(By.id("data")).findElements(By.tagName("tr"));
	    for (int i = 0; i < e.size(); i++) {
	      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
	        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Supplier Calculation")) {
	          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	          element.get(3);
	          Assertion(element.get(3).getText(), cslSupplierCalculation);
	        }
	
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Actual Performance")) {
	          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	          element.get(3);
	          Assertion(element.get(3).getText(), cslActualPerformance);
	        }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Final Performance")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslFinalPerformance);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Discrepancy")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslDiscrepancy);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Discrepancy Resolution Status")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslDiscrepancyResolutionStatus);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Supplier Numerator")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslSupplierNumerator);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Supplier Denominator")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslSupplierDenominator);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Actual Numerator")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslActualNumerator);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Actual Denominator")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslActualDenominator);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Final Numerator")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslFinalNumerator);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Final Denominator")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslFinalDenominator);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Applicable Credit")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslApplicableCredit);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Applicable Earnback")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslApplicableEarnback);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Discrepancy")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslDiscrepancy1);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Discrepancy Status")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslDiscrepancystatus);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Invoice No")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslInvoiceNumber);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Discrepancy Value")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), convertStringToInteger(csldiscrepancyValue));
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Impact(Days)")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslImpactDays);
	          }
	        
	        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Impact Type")) {
	            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
	            element.get(3);
	            Assertion(element.get(3).getText(), cslImpactType);
	          }        
	      }
	    }
	
	    fail = false;
	    driver.get(CONFIG.getProperty("endUserURL"));
	  }
	
	  @AfterMethod
		public void reportDataSetResult(ITestResult testResult) throws IOException {
			takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(child_sl_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(child_sl_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(child_sl_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}
			try {
	          for (int i = 2; i <= child_sl_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
	              String testCaseID = child_sl_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
	              if (!testCaseID.equalsIgnoreCase("")) {
	                  updateRun(convertStringToInteger(testCaseID),new java.lang.Exception().toString(), result);
	              }
	          }
		   }catch(Exception e){
	          Logger.debug(e);
	      }
			}
	
	  @AfterTest
	  public void reportTestResult() {
	    if (isTestPass)
	      TestUtil.reportDataSetResult(child_sl_suite_xls, "Test Cases", TestUtil.getRowNum(child_sl_suite_xls, this.getClass().getSimpleName()), "PASS");
	    else
	      TestUtil.reportDataSetResult(child_sl_suite_xls, "Test Cases", TestUtil.getRowNum(child_sl_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }
	
	  @DataProvider
	  public Object[][] getTestData() {
	    return TestUtil.getData(child_sl_suite_xls, this.getClass().getSimpleName());
	  }
	}
