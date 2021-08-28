package office.sirion.suite.ChildGB;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import office.sirion.util.DatePicker_Enhanced;
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

public class ChildGBAuditLog extends TestSuiteBase {
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
    if (!TestUtil.isTestCaseRunnable(child_gb_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(child_gb_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void ChildGBAuditLogTest(String testCaseID, String cgbTitle, String cgbDescription, String cgbOccuranceDate, String cgbStartTime,
			String cgbDuration, String cgbComments, String cgbActualDate, String cgbRequestedBy, String cgbChangeRequest,
			String cgbUploadFile) throws InterruptedException, ClassNotFoundException, SQLException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
    }

    Logger.debug("Executing Test Case of Child Governance Body Audit Log");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
    String logged_in_user = locateElementBy("cgb_communication_logged_in_user_full_name").getText(); driver.get(CONFIG.getProperty("endUserURL"));

      driver.get(CONFIG.getProperty("endUserURL"));


      locateElementBy("gb_quick_link");

    
    locateElementBy("gb_quick_link").click();

	new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

	locateElementBy("entity_listing_page_first_entry_link");
	
	if (!cgbTitle.equalsIgnoreCase("")) {
		driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + cgbTitle + "')]/preceding-sibling::td[1]/a")).click();
		} else {
			locateElementBy("entity_listing_page_first_entry_link").click();
			}
	locateElementBy("cgb_update_button");

	Assert.assertNotNull(locateElementBy("cgb_update_button"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("cgb_update_button"));
      locateElementBy("cgb_update_button").click();
    locateElementBy("cgb_update_page_title_textbox");
    
    addFieldValue("cgb_update_page_title_textbox", cgbTitle);
    
    addFieldValue("cgb_update_page_description_textarea", cgbDescription);
    
    locateElementBy("cgb_blank_area").click();
    
    addFieldValue("cgb_update_page_start_time_dropdown", cgbStartTime);
    
    addFieldValue("cgb_update_page_duration_dropdown", cgbDuration);
    
    String[] cgbOccurancedate = cgbOccuranceDate.split("-");
    String cgbOccurance_DateMonth = cgbOccurancedate[0];
    String cgbOccurance_DateYear = convertStringToInteger(cgbOccurancedate[2]);
    int cgbOccurance_dateYear = Integer.parseInt(cgbOccurance_DateYear);
    String cgbOccurance_DateDate = convertStringToInteger(cgbOccurancedate[1]);
    Integer cgbOccurance_dateDate = Integer.parseInt(cgbOccurance_DateDate);
    cgbOccurance_DateDate = cgbOccurance_dateDate.toString();

    DatePicker_Enhanced dp_cgbOccurance_date = new DatePicker_Enhanced();
    dp_cgbOccurance_date.expDate = cgbOccurance_DateDate;
    dp_cgbOccurance_date.expMonth = cgbOccurance_DateMonth;
    dp_cgbOccurance_date.expYear = cgbOccurance_dateYear;
    dp_cgbOccurance_date.pickExpDate("occurrenceDate");

	// Child Service Levels - Edit Page - COMMENTS AND ATTACHMENTS
	addFieldValue("cgb_comment_box_textarea", cgbComments);
	    
	addFieldValue("cgb_requested_by_dropdown", cgbRequestedBy);
	    
	String[] cgbActualdate = cgbActualDate.split("-");
    String cgbActual_DateMonth = cgbActualdate[0];
    String cgbActual_DateYear = convertStringToInteger(cgbActualdate[2]);
    int cgbActual_dateYear = Integer.parseInt(cgbActual_DateYear);
    String cgbActual_DateDate = convertStringToInteger(cgbActualdate[1]);
    Integer cgbActual_dateDate = Integer.parseInt(cgbActual_DateDate);
    cgbActual_DateDate = cgbActual_dateDate.toString();

    DatePicker_Enhanced dp_cgbActual_date = new DatePicker_Enhanced();
    dp_cgbActual_date.expDate = cgbActual_DateDate;
    dp_cgbActual_date.expMonth = cgbActual_DateMonth;
    dp_cgbActual_date.expYear = cgbActual_dateYear;
    dp_cgbActual_date.pickExpDate("actualDate");
	    
	addFieldValue("cgb_change_request_dropdown", cgbChangeRequest);
	
	if (!cgbUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + cgbUploadFile);
		}

    Assert.assertNotNull(locateElementBy("cbg_edit_button"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("cbg_edit_button"));
      locateElementBy("cbg_edit_button").click();

    locateElementBy("cgb_audit_log_tab").click();
    locateElementBy("cgb_audit_log_table");
	
    Assert.assertEquals(locateElementBy("cgb_audit_log_action_taken").getText(), "Updated");

    if (!cgbComments.equalsIgnoreCase("")){
    	try{
      Assert.assertEquals(locateElementBy("cgb_audit_log_comment").getText(), "Yes", "Comment Matched");
    	}catch (AssertionError err) {
	 		System.out.println("Comment: " + cgbComments + " and " + "Comment under ComunicationTab: " + locateElementBy("cgb_audit_log_comment").getText() + " Does not Match");
	 	}
    }else{
    	try{
  	      Assert.assertEquals(locateElementBy("cgb_audit_log_comment").getText(), "No", "Comment Matched");
  	    	}catch (AssertionError err) {
  		 		System.out.println("Comment: " + cgbComments + " and " + "Comment under ComunicationTab: " + locateElementBy("cgb_audit_log_comment").getText() + " Does not Match");
  		 	}
    }
    if (!cgbUploadFile.equalsIgnoreCase("")) {
	 		
	 	try{
		Assert.assertEquals(locateElementBy("cgb_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
				 	}catch (AssertionError err) {
	 		System.out.println("Uploaded File Name: " + cgbUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cgb_audit_log_uploaded_file").getText() + "Does not Match");
	 	}
		}else{
			try{
			Assert.assertEquals(locateElementBy("cgb_audit_log_uploaded_file").getText(), "No");
			}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + cgbUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cgb_audit_log_uploaded_file").getText() + "Does not Match");
		 	}
		}
    
    if (!cgbActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfAction = locateElementBy("cgb_audit_log_actual_date").getText().split("-");
		 	
		 				 	try {
			Assert.assertEquals(TimeOfAction[0],cgbActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfAction[1],cgbActualdate[0].substring(3, 8), "Month Matched");
			Assert.assertEquals(TimeOfAction[2],cgbActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + cgbActualDate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("cgb_audit_log_actual_date").getText() + " Does not Match");
		 	}
			
						
			}else{
				try{
				Assert.assertEquals(locateElementBy("cgb_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + cgbActualDate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("cgb_audit_log_actual_date").getText()  + " Does not Match");
			 	}
			} 
    
    		try{
						Assert.assertEquals(locateElementBy("cgb_audit_log_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
						System.out.println("Time of Action: " + dateFormat.format(date) + " and " + "Time of Action under Audit Log: " + locateElementBy("cgb_audit_log_time_of_action").getText()  + " Does not Match");
						}
    
	 		if (!cgbRequestedBy.equalsIgnoreCase("")) {
			 						 	
				try{
					Assert.assertEquals(locateElementBy("cgb_audit_log_requested_by").getText(), cgbRequestedBy, "Requested By Name Matched");
					
				 }catch (AssertionError err) {
				 	System.out.println("Requested By: " + cgbRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("cgb_audit_log_requested_by").getText() + " Does not Match");
				 }
			 		}else{
					try{
						Assert.assertEquals(locateElementBy("cgb_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
					}catch (AssertionError err) {
						System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("cgb_audit_log_requested_by").getText() + " Does not Match");
				 	}
				}
	 		try{
	 			
				Assert.assertEquals(locateElementBy("cgb_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
				
			 }catch (AssertionError err) {
			 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("cgb_audit_log_completed_by").getText() + " Does not Match");
			 }

    locateElementBy("cgb_audit_log_view_history_link").click();
    locateElementBy("cgb_audit_log_history_table");

    List<WebElement> e = locateElementBy("cgb_audit_log_history_table").findElement(By.id("data")).findElements(By.tagName("tr"));
    for (int i = 0; i < e.size(); i++) {
      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Title")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), cgbTitle);
        }

        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Description")) {
          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
          element.get(3);
          Assert.assertEquals(element.get(3).getText(), cgbDescription);
        }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Due Date")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), cgbOccuranceDate);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Start Time")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), cgbStartTime);
          }
        
        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Duration")) {
            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
            element.get(3);
            Assert.assertEquals(element.get(3).getText(), cgbDuration);
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
	      TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(child_gb_suite_xls, this.getClass().getSimpleName());
  }
}
