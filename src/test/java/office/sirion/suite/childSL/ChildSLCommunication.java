package office.sirion.suite.childSL;

import java.io.IOException;
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
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ChildSLCommunication extends TestSuiteBase {

  String runmodes[] = null;
  static int count = -1;
  static boolean fail = false;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Date date = new Date();
  String testCaseID=null;
  String result=null;;
  
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(child_sl_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(child_sl_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void ChildSLCommunicationTest(String cslComments, String cslActualDate, String cslRequestedBy, String cslChangeRequest,
      String cslUploadFile) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case Child Service Level Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

    driver.get(CONFIG.getProperty("endUserURL"));
    //

    locateElementBy("sl_quick_link").click();

    locateElementBy("csl_link_listing_page");


    locateElementBy("csl_link_listing_page").click();
    

    locateElementBy("global_bulk_listing_page_first_entry_link").click();
    locateElementBy("csl_show_page_save_comment_attachment_button");
    //

    // Child Service Level - Edit Page - COMMENTS AND ATTACHMENTS
    
    
    	addFieldValue("csl_edit_page_comments_textarea", cslComments);
    	locateElementBy("csl_blankarea_textarea").click();
    	addFieldValue("csl_edit_page_requested_by_dropdown", cslRequestedBy);

    	DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(cslActualDate,"actualDate");

    if (!cslChangeRequest.equalsIgnoreCase("")) {
      addFieldValue("csl_edit_page_change_request_dropdown", cslChangeRequest);
    }

    if (!cslUploadFile.equalsIgnoreCase("")) {
      locateElementBy("csl_edit_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\Child SL\\" + cslUploadFile);

    }

    Assert.assertNotNull(locateElementBy("csl_show_page_save_comment_attachment_button"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        locateElementBy("csl_show_page_save_comment_attachment_button"));
    locateElementBy("csl_show_page_save_comment_attachment_button").click();


    if (getObject("csl_error_notifications") != null) {
      List<WebElement> e = locateElementBy("csl_generic_error_list").findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_create_page = element.getText();
        if (errors_create_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_create_page);
        }
      }
    }

    locateElementBy("csl_communication_tab").click();
    locateElementBy("csl_communication_tab_inner_part");
    

    
    try{
    Assert.assertEquals(locateElementBy("csl_communication_status").getText(), "Comment/Attachment");
    }catch(AssertionError err){
		System.out.println("Status does not Matched");
	}
    Assert.assertEquals(locateElementBy("csl_communication_comment").getText(), cslComments);
    driver.switchTo().frame("iframe_textEditor_showpage");
    if (!cslComments.equalsIgnoreCase("")) {
    	try{
      
      Assert.assertEquals(locateElementBy("csl_communication_details_comment").getText(), cslComments);
    	}catch(AssertionError err){
    		System.out.println("Comment"+ cslComments + "does not Matched");
    	}
    }
    
    driver.switchTo().defaultContent();

    if (!cslUploadFile.equalsIgnoreCase("")) {
    	try{
      Assert.assertEquals(locateElementBy("csl_communication_documents_link").getText(), cslUploadFile);
      locateElementBy("csl_communication_documents_link").click();

      
      Assert.assertEquals(locateElementBy("csl_communication_details_documents_link").getText(), cslUploadFile);
      locateElementBy("csl_communication_details_documents_link").click();

    	}catch(AssertionError err){
    		System.out.println("Uploaded File name"+ cslUploadFile + "does not get uploaded");
    	}
    }
    
    String[] cobActualdate = cslActualDate.split("-");
    
	 if (!cslActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfcob = locateElementBy("csl_communication_time_of_action").getText().split("-");
		 
		 
		 	locateElementBy("csl_communication_latest_entry_link").click();
		 	try {
			Assert.assertEquals(TimeOfcob[0],cobActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfcob[1],cobActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(TimeOfcob[2],cobActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + cobActualdate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("csl_communication_time_of_action").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfwor = locateElementBy("csl_communication_details_time_of_action").getText().replace("Time of Action: ", "").split("-");
			
			try {
			Assert.assertEquals(DetailsTimeOfwor[0],cobActualdate[1], "Date Matched");
			Assert.assertEquals(DetailsTimeOfwor[1],cobActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(DetailsTimeOfwor[2],cobActualdate[2], "Year Matched");
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + cobActualdate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("csl_communication_time_of_action").getText()  + " Does not Match");
		 	}
			
			}else{
				locateElementBy("csl_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("csl_communication_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("csl_communication_details_time_of_action").getText().replaceAll("Time of wor: ", ""), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + cobActualdate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("csl_communication_time_of_action").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!cslRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("csl_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("csl_communication_completed_by").getText(), cslRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("csl_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), cslRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + cslRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("csl_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("csl_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("csl_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("csl_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("csl_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + cslRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("csl_communication_completed_by").getText() + " Does not Match");
			 	}
			}

    fail = false;
    /*
     * if (!fail) result= "Pass"; else result= "Fail";
     * TestlinkIntegration.updateTestLinkResult(testCaseId,"",result);
     */
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
