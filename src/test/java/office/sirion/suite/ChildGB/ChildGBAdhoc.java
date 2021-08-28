package office.sirion.suite.ChildGB;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

public class ChildGBAdhoc extends TestSuiteBase {
	
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	static SimpleDateFormat fromExcel = new SimpleDateFormat("MMMM-dd-yyyy");
	static SimpleDateFormat desiredFormat = new SimpleDateFormat("MM/dd/yyyy");
	static SimpleDateFormat applicationformat = new SimpleDateFormat("MMMM-dd-yyyy");
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
	public void ChildGBAdhocTest(String cgbTitle, String cgbDueDate, String cgbStartTime, String cgbDuration, String cgbLocation, String cgbTimezone) 
			throws InterruptedException, ClassNotFoundException, SQLException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Set Data Set to NO " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        driver.get(CONFIG.getProperty("endUserURL"));

        DatePicker_Enhanced Date = new DatePicker_Enhanced();
	    Logger.debug("Executing Test Case for Creation of Adhoc Child GB ---- ");

	    WebElement element = driver.findElement(By.xpath("//*[@id='gl-account-section']/div[1]/div"));
	    
        Actions action = new Actions(driver);
 
        action.moveToElement(element).build().perform();

        WebElement element1 = driver.findElement(By.xpath("//*[@id='gl-account-section']/div[2]/ul/li[1]/a/div[2]"));
        Actions action1 = new Actions(driver);
        action1.moveToElement(element1).build().perform();
	    

	    fluentWaitMethod(locateElementBy("cgb_adhoc_create_page_governance_body_schedule_tab"));
		locateElementBy("cgb_adhoc_create_page_governance_body_schedule_tab");
		
		locateElementBy("cgb_adhoc_create_page_governance_body_schedule_tab").click();
		
		if(!cgbTitle.equalsIgnoreCase("")){
			try{
		new Select (locateElementBy("cgb_adhoc_create_page_governance_body_option_dropdown")).selectByVisibleText(cgbTitle);
			}catch (NoSuchElementException e){
				Logger.debug(cgbTitle + ": is not available aa Option");
			}
			
		}

		if(!cgbDueDate.equalsIgnoreCase("")){
			
			
		try {

		    String childgbDueDate = desiredFormat.format(fromExcel.parse(cgbDueDate));
		    System.out.println(childgbDueDate);
		    
		    Date date = new Date();
		    String TodaysDate = desiredFormat.format(date);
		    
		    Date excelDueDate = desiredFormat.parse(childgbDueDate);
		    Date calendarTodaysDate = desiredFormat.parse(TodaysDate);
		    
		    if(excelDueDate.compareTo(calendarTodaysDate) < 0 || excelDueDate.compareTo(calendarTodaysDate) == 0){
		    	
				Date.selectCalendar(cgbDueDate,"governBodyScheduleDate");
		    }
		    
		    if(excelDueDate.compareTo(calendarTodaysDate) > 0){
		    	
				Date.selectCalendar(cgbDueDate,"governBodyScheduleDate");
		    }
		  	    
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		}
		
		if(!cgbStartTime.equalsIgnoreCase("")){
		new Select (locateElementBy("cgb_adhoc_create_page_start_time_dropdown")).selectByVisibleText(cgbStartTime.split(" ")[0]);
		}
		
		if(!cgbDuration.equalsIgnoreCase("")){
		new Select (locateElementBy("cgb_adhoc_create_page_duration_dropdown")).selectByVisibleText(cgbDuration);
		}
		
		if(!cgbTimezone.equalsIgnoreCase("")){
		
	    if(cgbTimezone.equalsIgnoreCase("")){
	    	new Select (locateElementBy("cgb_adhoc_create_page_timezone_dropdown")).selectByIndex(2);
	    }else{
	    	new Select (locateElementBy("cgb_adhoc_create_page_timezone_dropdown")).selectByVisibleText(cgbTimezone);
	    }
		}
		
		if(!cgbLocation.equalsIgnoreCase("")){
	    locateElementBy("cgb_adhoc_create_page_location_dropdown").sendKeys(cgbLocation);
		}
		
		
	    locateElementBy("cgb_adhoc_create_page_schedule_meeting_button").click();
	    
	    if (cgbTitle.equalsIgnoreCase("")) {
	    	String AlertMesage = locateElementBy("cgb_adhoc_create_page_alert_1").getText();
			Assert.assertEquals(AlertMesage, "Please select Governance Body.");
			locateElementBy("cgb_adhoc_create_page_alert_popup_ok_button").click();
			} 
	   
	    if (cgbDueDate.equalsIgnoreCase("")) {
	    	String AlertMesage = locateElementBy("cgb_adhoc_create_page_alert_1").getText();
			Assert.assertEquals(AlertMesage, "");
			locateElementBy("cgb_adhoc_create_page_alert_popup_ok_button").click();
			} 
		
	    if(locateElementBy("cgb_adhoc_create_page_alert_1").getText().equalsIgnoreCase("Please select date after Governance Body's effective date")){
		
	    	for(int i=1; i<=30;i++){
	    		locateElementBy("cgb_adhoc_create_page_alert_popup_ok_button").click();

	    		try {
					Date.selectCalendar(cgbDueDate,"governBodyScheduleDate");
		    		locateElementBy("cgb_adhoc_create_page_schedule_meeting_button").click();
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}    
	    	}
	    }
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
			  for (int i = 2; i <= child_gb_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				  String testCaseID = child_gb_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
	      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "PASS");
	    else
	      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }

	  @DataProvider
	  public Object[][] getTestData() {
	    return TestUtil.getData(child_gb_suite_xls, this.getClass().getSimpleName());
	  }
	}
