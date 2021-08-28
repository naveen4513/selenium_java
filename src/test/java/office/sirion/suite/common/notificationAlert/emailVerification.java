package office.sirion.suite.common.notificationAlert;

import edu.emory.mathcs.backport.java.util.Arrays;
import office.sirion.util.TestEmailFeature;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import testlink.api.java.client.TestLinkAPIResults;
import java.io.IOException;
import java.sql.Array;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class emailVerification extends TestSuiteBase{

    String runmodes[]=null;
    static int count=-1;
    static boolean fail=true;
    static boolean skip=false;
    static boolean isTestPass=true;
    String testCaseID=null;
    String result=null;

    @BeforeTest
    public void checkTestSkip() {
        if(!TestUtil.isTestCaseRunnable(notification_alert_xls,this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
            throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(notification_alert_xls, this.getClass().getSimpleName());
    }

    @Test (dataProvider="getTestData")
    public void emailVerificationTest (String entityName, String entityClientId,String subject)
            throws Exception {
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for Test Data -- " + entityName + " set to NO " + count);
        }

        String date_created = String.valueOf(LocalDate.now());
        String tableName = "system_emails";
        List<String> columnName = java.util.Arrays.asList("subject","client_id","date_created::date");
        List<String> comparator = java.util.Arrays.asList("ilike","equal","equal");
        List<String> columnValue = java.util.Arrays.asList("%Notification Alert "+subject+"%", convertStringToInteger(entityClientId),date_created);
        List<String> tableColumnNameToSelect = java.util.Arrays.asList("to_mail", "subject", "body", "client_id");

        TestEmailFeature na = new TestEmailFeature();
        na.testTS80700(entityName,convertStringToInteger(entityClientId),tableName,columnName,comparator,columnValue,tableColumnNameToSelect);
    }
    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if(testResult.getStatus()==ITestResult.SKIP)
            TestUtil.reportDataSetResult(notification_alert_xls, this.getClass().getSimpleName(), count+2, "SKIP");
        else if(testResult.getStatus()==ITestResult.FAILURE) {
            isTestPass=false;
            TestUtil.reportDataSetResult(notification_alert_xls, this.getClass().getSimpleName(), count+2, "FAIL");
            result= "Fail";
        }
        else if (testResult.getStatus()==ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(notification_alert_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
        if(isTestPass)
            TestUtil.reportDataSetResult(notification_alert_xls, "Test Cases", TestUtil.getRowNum(notification_alert_xls,this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(notification_alert_xls, "Test Cases", TestUtil.getRowNum(notification_alert_xls,this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(notification_alert_xls, this.getClass().getSimpleName());
    }
}
