package office.sirion.suite.Calender;

import office.sirion.util.CurrentDaynDate;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;

public class EndUserCalendar extends TestSuiteBase{


    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    String result=null;
    int counter = 0;

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {

        if (!TestUtil.isTestCaseRunnable(calendar_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// logs
            throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
        }
        // load the runmodes off the tests
        runmodes = TestUtil.getDataSetRunmodes(calendar_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void EndUsercalendar() throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();

        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        //

        String TodayDate = locateElementBy("end_user_today_date").getText();
        String TodayDayofWeek = locateElementBy("end_user_today_dayofweek").getText().toLowerCase();
        String[] Currentmonthyear = locateElementBy("end_user_today_current_month_year").getText().split(", ");

        CurrentDaynDate DaynDate = new CurrentDaynDate();
        DaynDate.dayndate(TodayDate, TodayDayofWeek, Currentmonthyear[0].toLowerCase(), Currentmonthyear[1]);

        locateElementBy("end_user_calendar_link").click();


        for(int i=0;i<2 ;i++) {
            if (dataUnderDate().equalsIgnoreCase(" ")) {
                String MonthName = locateElementBy("end_user_calendar_month_name").getText();
                Logger.debug("Data is not available for "+ MonthName);
                locateElementBy("end_user_calendar_next_month_arrow_button").click();
            }
            else {
                break;
            }
        }

        Thread.sleep(2000);

        locateElementBy("end_user_calendar_today_button").click();

        for(int i=0;i<2 ;i++) {
            if (dataUnderDate().equalsIgnoreCase(" ")) {
                String MonthName = locateElementBy("end_user_calendar_month_name").getText();
                Logger.debug("Data is not available for "+ MonthName);
                locateElementBy("end_user_calendar_previous_month_arrow_button").click();


            }
            else {
                break;
            }
        }

        /*for(int i=0;i<=1 && dataUnderDate().equalsIgnoreCase(" ") ;i++) {
                locateElementBy("end_user_calendar_next_month_arrow_button").click();
        }*/


        locateElementBy("end_user_calendar_week_button").click();
        locateElementBy("end_user_calendar_day_button").click();
        locateElementBy("end_user_calendar_month_button").click();

        locateElementBy("end_user_calendar_download_button").click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#data-ng-app > div.ui-dialog.ui-widget.ui-widget-content.ui-corner-all.ui-front.ui-dialog-buttons.ui-draggable > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button")).click();

        driver.navigate().refresh();
    }

    public String dataUnderDate() throws InterruptedException {

       
        String EntityID= null;
        for (int i = 1; i <= 6; i++) {

            for (int j = 1; j <= 7; j++) {

                EntityID  = driver.findElement(By.xpath("//*[@id='eventCalendarFull']/div[1]/div/table/tbody/tr[" + i + "]/td[" + j + "]/div/div[2]/div")).getText();
                if (!EntityID.equalsIgnoreCase(" ")) {
                    driver.findElement(By.xpath("//*[@id='eventCalendarFull']/div[1]/div/table/tbody/tr[" + i + "]/td[" + j + "]/div/div[2]/div")).click();
                    //
                    if (driver.getPageSource().contains(EntityID)) {
                    	
                        System.out.println("ID displayed on Task pop-up matched with show page");
                        getObject("analytics_link").click();
                        //
                        getObject("end_user_calendar_link").click();
                    }
                } else {
                    counter++;

                }
            }
        }
        return EntityID;
    }
    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if(testResult.getStatus()==ITestResult.SKIP)
            TestUtil.reportDataSetResult(calendar_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
        else if(testResult.getStatus()==ITestResult.FAILURE) {
            isTestPass=false;
            TestUtil.reportDataSetResult(calendar_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
            result= "Fail";
        }
        else if (testResult.getStatus()==ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(calendar_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
            result= "Pass";
        }
        try {
            for (int i = 2; i <= calendar_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = calendar_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
                TestUtil.reportDataSetResult(calendar_suite_xls, "Test Cases", TestUtil.getRowNum(calendar_suite_xls, this.getClass().getSimpleName()), "PASS");
            else
                TestUtil.reportDataSetResult(calendar_suite_xls, "Test Cases", TestUtil.getRowNum(calendar_suite_xls, this.getClass().getSimpleName()), "FAIL");

        }

        @DataProvider
        public Object[][] getTestData() {
            return TestUtil.getData(calendar_suite_xls, this.getClass().getSimpleName());
        }
    }
