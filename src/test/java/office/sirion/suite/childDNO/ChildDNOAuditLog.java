	package office.sirion.suite.childDNO;
	
import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

    public class ChildDNOAuditLog extends TestSuiteBase {
	  String runmodes[] = null;
	  String result = null;
	  static int count = -1;
	  static boolean fail = true;
	  static boolean skip = false;
	  static boolean isTestPass = true;

	  @BeforeTest
	  public void checkTestSkip() {
	    if (!TestUtil.isTestCaseRunnable(child_obligation_suite_xls, this.getClass().getSimpleName())) {
	      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
	      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
	    }
	    runmodes = TestUtil.getDataSetRunmodes(child_obligation_suite_xls, this.getClass().getSimpleName());
	  }
        @Deprecated
	  @Test(dataProvider = "getTestData")
	  public void ChildSLAuditLogTest(String cobComments, String cobActualDate, String cobRequestedBy, String cobChangeRequest,
		      String cobUploadFile) throws InterruptedException, ClassNotFoundException, SQLException {
	
	    count++;
	    if (!runmodes[count].equalsIgnoreCase("Y")) {
	      skip = true;
	      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
	    }

            Logger.debug("Executing Test Case of Child Obligation Audit Log");
	
	    // Launch The Browser
	    openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	
	    driver.get(CONFIG.getProperty("endUserURL"));

	    String logged_in_user = locateElementBy("cob_communication_logged_in_user_full_name").getText(); 
	    
	    locateElementBy("ob_quick_link").click();

	    locateElementBy("global_bulk_listing_page_first_entry_link").click();
	    locateElementBy("cob_edit_button");
	
	    Assert.assertNotNull(locateElementBy("cob_edit_button"));
	    locateElementBy("cob_edit_button").click();

	    // Child Service Levels - Create Page - COMMENTS AND ATTACHMENTS
	    addFieldValue("cob_edit_page_comments_textarea", cobComments);
		locateElementBy("cob_blankarea_textarea").click();
		addFieldValue("cob_edit_page_requested_by_dropdown", cobRequestedBy);
	
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(cobActualDate,"actualDate");
			
			if (!cobChangeRequest.equalsIgnoreCase("")) {
			  addFieldValue("cob_edit_page_change_request_dropdown", cobChangeRequest);
			}
			
			if (!cobUploadFile.equalsIgnoreCase("")) {
			  locateElementBy("cob_edit_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\Child Obligation\\" + cobUploadFile);
			}
            locateElementBy("cob_edit_page_update_button");
			Assert.assertNotNull(locateElementBy("cob_edit_page_update_button"));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("cob_edit_page_update_button"));
            locateElementBy("cob_edit_page_update_button");

            WebElement element = locateElementBy("cob_edit_page_update_button");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();

			locateElementBy("cob_show_page_audit_log_tab");

	    locateElementBy("cob_show_page_audit_log_tab").click();
	   
	   locateElementBy("cob_audit_log_table");
		
	    Assert.assertEquals(locateElementBy("cob_audit_log_action_taken").getText(), "Updated");

	    if (!cobComments.equalsIgnoreCase("")){
	    	try{
	      Assert.assertEquals(locateElementBy("cob_audit_log_comment").getText(), "Yes", "Comment Matched");
	    	}catch (AssertionError err) {
		 		System.out.println("Comment: " + cobComments + " and " + "Comment under ComunicationTab: " + locateElementBy("cob_audit_log_comment").getText() + " Does not Match");
		 	}
	    }else{
	    	try{
	  	      Assert.assertEquals(locateElementBy("cob_audit_log_comment").getText(), "No", "Comment Matched");
	  	    	}catch (AssertionError err) {
	  		 		System.out.println("Comment: " + cobComments + " and " + "Comment under ComunicationTab: " + locateElementBy("cob_audit_log_comment").getText() + " Does not Match");
	  		 	}
	    }
	    if (!cobUploadFile.equalsIgnoreCase("")) {
		 		
		 	try{
			Assert.assertEquals(locateElementBy("cob_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
					 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + cobUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cob_audit_log_uploaded_file").getText() + "Does not Match");
		 	}
			}else{
				try{
				Assert.assertEquals(locateElementBy("cob_audit_log_uploaded_file").getText(), "No");
				}catch (AssertionError err) {
			 		System.out.println("Uploaded File Name: " + cobUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cob_audit_log_uploaded_file").getText() + "Does not Match");
			 	}
			}
	    
	    if (!cobActualDate.equalsIgnoreCase("")) {
			 
			 String[] TimeOfcob = locateElementBy("cob_audit_log_actual_date").getText().split("-");
			 String[] cobActualdate = cobActualDate.split("-");
			 	
			 				 	try {
				Assert.assertEquals(TimeOfcob[0],cobActualdate[1], "Date Matched");
				Assert.assertEquals(TimeOfcob[1],cobActualdate[0].substring(3, 8), "Month Matched");
				Assert.assertEquals(TimeOfcob[2],cobActualdate[2], "Year Matched");
			 	}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + cobActualDate.toString() + " and " + "Time of cob under ComunicationTab: " + locateElementBy("cob_audit_log_actual_date").getText() + " Does not Match");
			 	}
				
							
				}else{
					try{
					    Assert.assertEquals(locateElementBy("cob_audit_log_actual_date").getText(), java.time.LocalDate.now().toString(), "Date, Month and Year Matched");
					}catch (AssertionError err) {
				 		System.out.println("Actual Date: " + cobActualDate.toString() + " and " + "Time of cob under ComunicationTab: " + locateElementBy("cob_audit_log_actual_date").getText()  + " Does not Match");
				 	}
				} 
	    
	    		try{
							Assert.assertEquals(locateElementBy("cob_audit_log_time_of_action").getText(), java.time.LocalDate.now().toString(), "Date, Month and Year Matched");
							}catch (AssertionError err) {
							System.out.println("Time of cob: " + java.time.LocalDate.now().toString() + " and " + "Time of cob under Audit Log: " + locateElementBy("cob_audit_log_time_of_action").getText()  + " Does not Match");
							}
	    
		 		if (!cobRequestedBy.equalsIgnoreCase("")) {
				 						 	
					try{
						Assert.assertEquals(locateElementBy("cob_audit_log_requested_by").getText(), cobRequestedBy, "Requested By Name Matched");
						
					 }catch (AssertionError err) {
					 	System.out.println("Requested By: " + cobRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("cob_audit_log_requested_by").getText() + " Does not Match");
					 }
				 		}else{
						try{
							Assert.assertEquals(locateElementBy("cob_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
						}catch (AssertionError err) {
							System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("cob_audit_log_requested_by").getText() + " Does not Match");
					 	}
					}
		 		try{
		 			
					Assert.assertEquals(locateElementBy("cob_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
					
				 }catch (AssertionError err) {
				 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("cob_audit_log_completed_by").getText() + " Does not Match");
				 }

	    locateElementBy("cob_audit_log_view_history_link").click();


	   try{
		   Assert.assertEquals(locateElementBy("entity_audit_log_history_table").getText(), " History not available ");
	   }catch(AssertionError err){
		   Logger.debug("historyDataTable is not visible, exception is generated \n "+err);
	   }
	    
	    fail = false;
	    driver.get(CONFIG.getProperty("endUserURL"));
	  }

        @AfterMethod
       	public void reportDataSetResult(ITestResult testResult) throws IOException {
       		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

       		if(testResult.getStatus()==ITestResult.SKIP)
       			TestUtil.reportDataSetResult(child_obligation_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
       		else if(testResult.getStatus()==ITestResult.FAILURE) {
       			isTestPass=false;
       			TestUtil.reportDataSetResult(child_obligation_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
       			result= "Fail";
       			}
       		else if (testResult.getStatus()==ITestResult.SUCCESS) {
       			TestUtil.reportDataSetResult(child_obligation_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
       			result= "Pass";
       			}
       		try {
                 for (int i = 2; i <= child_obligation_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                     String testCaseID = child_obligation_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
	      TestUtil.reportDataSetResult(child_obligation_suite_xls, "Test Cases", TestUtil.getRowNum(child_obligation_suite_xls, this.getClass().getSimpleName()), "PASS");
	    else
	      TestUtil.reportDataSetResult(child_obligation_suite_xls, "Test Cases", TestUtil.getRowNum(child_obligation_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }
	
	  @DataProvider
	  public Object[][] getTestData() {
	    return TestUtil.getData(child_obligation_suite_xls, this.getClass().getSimpleName());
	  }
	}
