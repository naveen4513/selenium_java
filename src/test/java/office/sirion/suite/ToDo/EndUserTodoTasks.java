package office.sirion.suite.ToDo;

import office.sirion.suite.ToDo.TestSuiteBase;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;

public class EndUserTodoTasks extends TestSuiteBase {
	  String runmodes[] = null;
	  static int count = -1;
	  static boolean fail = true;
	  static boolean skip = false;
	  static boolean isTestPass = true;
	  String result=null;

	  // Runmode of test case in a suite
	  @BeforeTest
	  public void checkTestSkip() {

	    if (!TestUtil.isTestCaseRunnable(todo_suite_xls, this.getClass().getSimpleName())) {
	      Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// logs
	      throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
	    }
	    // load the runmodes off the tests
	    runmodes = TestUtil.getDataSetRunmodes(todo_suite_xls, this.getClass().getSimpleName());
	  }
	  
	  @Test(dataProvider = "getTestData")
	  public void EndUserTodoApprovalTest(String testCaseID) throws InterruptedException {
	  
		// test the runmode of current dataset
		    count++;
		    if (!runmodes[count].equalsIgnoreCase("Y")) {
		      skip = true;
		      throw new SkipException("Runmode for test set data set to no " + count);
		    }
		    
		    openBrowser();
		    endUserLogin(CONFIG.getProperty("endUserURL"),CONFIG.getProperty("endUserUsername"),CONFIG.getProperty("endUserPassword"));

          driver.get(CONFIG.getProperty("endUserURL"));

		  if (!driver.findElement(By.xpath("//*[@id='today']/div")).getText().equalsIgnoreCase("No events")) {

			  if (!locateElementBy("end_user_todays_task_count_home_page").getText().equalsIgnoreCase("")) {

				  String todayTaskCount = locateElementBy("end_user_todays_task_count_home_page").getText();
				  int todayTaskcount = Integer.parseInt(todayTaskCount.replaceAll(",", "").replaceAll("\\+",""));


				  locateElementBy("end_user_todays_tasks_link").click();

				  String TaskCount = locateElementBy("end_user_todays_tasks_count").getText();
				  String[] Taskcount = TaskCount.split(" ");
				  String TaskSplitString = Taskcount[4];

				  int tasksplitcount = Integer.parseInt(TaskSplitString.replaceAll(",", "").replaceAll("\\+",""));
				  compareIntegerVals(todayTaskcount, tasksplitcount);

				  String Entity_ID = locateElementBy("end_user_todo_task_entity_id").getText();
				  locateElementBy("end_user_todo_task_entity_id").click();
				  					  try {
						  driver.findElement(By.xpath("//div[@class='modal-content']"));
						  if(driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
								  equalsIgnoreCase("Either you do not have the required permissions or requested page does not exist anymore.") ||
								  driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
										  equalsIgnoreCase("Server Error")) {
							  Logger.debug("Unable to view show page of an Entity" + Entity_ID + "Might be Permission issue");
							  locateElementBy("entity_show_page_permission_notification_pop_up_ok_button").click();
						  }
				  } catch (NoSuchElementException ex) {
					  if (driver.getPageSource().contains(Entity_ID)) {
						  System.out.println("ID displayed on Task pop-up matched with show page");
						  fail = false;
						  driver.get(CONFIG.getProperty("endUserURL"));

					  } else {
						  System.out.println("There is no data available under Today's Task");
						  fail = false;
						  driver.get(CONFIG.getProperty("endUserURL"));

					  }
				  }
			  } else {
				  System.out.println("There is no data available under Today's Task");
				  fail = false;
				  driver.get(CONFIG.getProperty("endUserURL"));

			  }
		  }
	  }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= todo_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = todo_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
	      TestUtil.reportDataSetResult(todo_suite_xls, "Test Cases", TestUtil.getRowNum(todo_suite_xls, this.getClass().getSimpleName()), "PASS");
	    else
	      TestUtil.reportDataSetResult(todo_suite_xls, "Test Cases", TestUtil.getRowNum(todo_suite_xls, this.getClass().getSimpleName()), "FAIL");

	  }

	  @DataProvider
	  public Object[][] getTestData() {
	    return TestUtil.getData(todo_suite_xls, this.getClass().getSimpleName());
	  }
}
