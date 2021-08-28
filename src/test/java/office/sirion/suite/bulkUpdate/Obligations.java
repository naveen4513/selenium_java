package office.sirion.suite.bulkUpdate;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import office.sirion.util.XLSUtils;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Obligations extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(bulk_update_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(bulk_update_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void BulkCreateObligationTest(String EntityName, String Sino,String ID,String BITitle,String BIDescription,String BIPerformanceType,String BICategory,String BISubCategory,String BITimeZone,String BIDeliveryCountries,
                                         String BISupplierAccess,String OIPriority,String OIPhase,String OITriggered,String IDFrequencyType,String IDFrequency,String IDWeekType,String IDStartDate,String IDEndDate,
                                         String IDIncludeStartDate,String IDIncludeEndDate,String IDPatternDate,String IDEffectiveDate,String IDExcludeHoliday,String MasterObligationsManager,String PIProjectID,
                                         String PIInitiatives,String PIProjectLevels,String CAPrivacy,String CAComments,String CAActualDate,String CARequestedBy,String CAChangeRequest,String Process)
                                        throws InterruptedException, IOException, TestLinkAPIException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        EndUserBulkUpdateJob eubcj = new EndUserBulkUpdateJob();
        eubcj.EndUserBulkUpdateObligationDownloadTemplateTest(EntityName);

        List<String> OBData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "Obligations", 2);

        Map<Integer, Object> columnData = new HashMap<>();
        for (int i = 1; i < OBData.size() - 3; i++) {
            columnData.put(i, OBData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-Master Obligations-Update.xlsm");

        if (xls.writeRowData("Obligation", 6, columnData) == true) {
            eubcj.EndUserBulkUpdateObligationUploadTemplateTest(xls.toString());

            EndUserBulkUpdateShowPageVerification EUBUSPV = new EndUserBulkUpdateShowPageVerification();
            EUBUSPV.EndUserBulkUpdateObligationTest("mcd-Master Obligations-Update.xlsm");
        }
    }

    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if (testResult.getStatus() == ITestResult.SKIP)
            TestUtil.reportDataSetResult(bulk_update_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");

        else if (testResult.getStatus() == ITestResult.FAILURE) {
            isTestPass = false;
            TestUtil.reportDataSetResult(bulk_update_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
            result = "Fail";
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(bulk_update_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");
            result = "Pass";
        }

        try {
            if (!testCaseID.equalsIgnoreCase(""))
                TestlinkIntegration.updateTestLinkResult(testCaseID, "", result);
        } catch (Exception e) {

        }
    }

    @AfterTest
    public void reportTestResult() {

        if (isTestPass)
            TestUtil.reportDataSetResult(bulk_update_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_update_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(bulk_update_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_update_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(bulk_update_suite_xls, this.getClass().getSimpleName());
    }
}
