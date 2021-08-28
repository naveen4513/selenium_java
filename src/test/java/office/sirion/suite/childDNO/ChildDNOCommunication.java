package office.sirion.suite.childDNO;

import java.io.IOException;
import java.util.List;
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

public class ChildDNOCommunication extends TestSuiteBase {
  String result = null;
  String testCaseID = null;
  String runmodes[] = null;
  static int count = -1;
  static boolean fail = false;
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
  public void ChildSLCommunicationTest(String cobComments, String cobActualDate, String cobRequestedBy, String cobChangeRequest,
      String cobUploadFile) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case Child Obligation Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

    driver.get(CONFIG.getProperty("endUserURL"));

    locateElementBy("ob_quick_link");
    locateElementBy("ob_quick_link").click();

    locateElementBy("global_bulk_listing_page_first_entry_link").click();

    // Child Service Level - Edit Page - COMMENTS AND ATTACHMENTS
    
    	addFieldValue("cob_edit_page_comments_textarea", cobComments);
    	DatePicker_Enhanced date = new DatePicker_Enhanced();
    	date.selectCalendar(cobActualDate,"actualDate");
    	addFieldValue("cob_edit_page_requested_by_dropdown", cobRequestedBy);

    if (!cobChangeRequest.equalsIgnoreCase("")) {
      addFieldValue("cob_edit_page_change_request_dropdown", cobChangeRequest);
    }

    if (!cobUploadFile.equalsIgnoreCase("")) {
      locateElementBy("cob_edit_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\Child Obligation\\" + cobUploadFile);

    }

    Assert.assertNotNull(locateElementBy("cob_comment_attachment_button"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        locateElementBy("cob_comment_attachment_button"));
    locateElementBy("cob_comment_attachment_button").click();

    if (getObject("cob_error_notifications") != null) {
      List<WebElement> e = locateElementBy("cob_generic_error").findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_create_page = element.getText();
        if (errors_create_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_create_page);
        }
      }
    }
   locateElementBy("cob_communication_tab");

        WebElement communication = locateElementBy("cob_communication_tab");
        Actions actions = new Actions(driver);
        actions.moveToElement(communication).click().build().perform();



    Assertion(locateElementBy("cob_communication_status").getText(), "Comment/Attachment");

    if (!cobComments.equalsIgnoreCase("")) {
    	try{
      Assert.assertEquals(locateElementBy("cob_communication_comment").getText(), cobComments);
      driver.switchTo().frame("iframe_textEditor_showpage");
      Assert.assertEquals(locateElementBy("cob_communication_details_comment").getText(), cobComments);
      driver.switchTo().defaultContent();
    	}catch(AssertionError err){
    		System.out.println("Comment"+ cobComments + "does not Matched");
    	}
    }

    if (!cobUploadFile.equalsIgnoreCase("")) {
    	try{
      Assert.assertEquals(locateElementBy("cob_communication_documents_link").getText(), cobUploadFile);
      locateElementBy("cob_communication_documents_link").click();
      Assert.assertEquals(locateElementBy("cob_communication_details_documents_link").getText(), cobUploadFile);
      locateElementBy("cob_communication_details_documents_link").click();
     	}catch(AssertionError err){
    		System.out.println("Uploaded File name: "+ cobUploadFile + " does not get uploaded");
    	}
    }
    
    String[] cobActualdate = cobActualDate.split("-");
    
	 if (!cobActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfcob = locateElementBy("cob_communication_time_of_action").getText().split("-");
		 
		 
		 	locateElementBy("cob_communication_latest_entry_link").click();
		 	try {
			Assert.assertEquals(TimeOfcob[0],cobActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfcob[1],cobActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(TimeOfcob[2],cobActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + cobActualdate + " and " + "Time of wor under ComunicationTab: " + locateElementBy("cob_communication_time_of_action").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfwor = locateElementBy("cob_communication_details_time_of_action").getText().replace("Time of wor: ", "").split("-");
			
			try {
			Assert.assertEquals(DetailsTimeOfwor[0],cobActualdate[1], "Date Matched");
			Assert.assertEquals(DetailsTimeOfwor[1],cobActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(DetailsTimeOfwor[2],cobActualdate[2], "Year Matched");
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + cobActualdate.toString() + " and " + "Time of wor under ComunicationTab: " + locateElementBy("cob_communication_time_of_action").getText()  + " Does not Match");
		 	}
			
			}else{
				locateElementBy("cob_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("cob_communication_time_of_action").getText(), java.time.LocalDate.now().toString(), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("cob_communication_details_time_of_action").getText().replaceAll("Time of wor: ", ""), java.time.LocalDate.now().toString(), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + cobActualdate.toString() + " and " + "Time of wor under ComunicationTab: " + locateElementBy("cob_communication_time_of_action").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!cobRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("cob_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cob_communication_completed_by").getText(), cobRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("cob_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), cobRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + cobRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("cob_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("cob_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("cob_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("cob_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("cob_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + cobRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("cob_communication_completed_by").getText() + " Does not Match");
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
