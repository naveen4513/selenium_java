package office.sirion.suite.bulkCreate;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import office.sirion.util.XLSUtils;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
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
        if (!TestUtil.isTestCaseRunnable(bulk_create_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(bulk_create_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void BulkCreateObligationTest(String entityParentType, String Sino, String BISupplier, String BIContract, String BITitle, String BIDescription, String BIPerformanceType,
                                         String BICategory, String BISubCategory, String BITimezone, String BIDeliveryCountries, String BICurrency, String BISupplierAccess,
                                         String BITier, String OIPriority, String OIPhase, String OITriggered, String IDFrequencyType, String IDFrequency, String IDWeekType,
                                         String IDStartDate, String IDEndDate, String IDIncludeStartDate, String IDIncludeEndDate, String IDPatternDate, String IDEffectiveDate,
                                         String IDExcludeHoliday, String Functions, String Services, String ServiceCategory, String MGRegions, String MGCountries, String CGContractRegions,
                                         String CGContractCountries, String CGVendorContractingParty, String MasterObligationsManager, String Responsibility, String FIFinancialImpactApplicable,
                                         String FICreditImpactApplicable, String FIImpactDays, String FIImpactType, String DRRecipientClientEntity, String RCEContractingClientEntity, String DRCompanyCode,
                                         String RCEContractingCompanyCode, String FIFinancialImpactValue, String FIFinancialImpactClause, String CICreditImpactValue, String CICreditImpactClause,
                                         String PIProjectID, String PIProjectLevels, String PIInitiatives, String CAPrivacy, String CAComments, String CAActualDate, String CARequestedBy, String CAChangeRequest,
                                         String Process) throws InterruptedException, IOException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }
        List<String> OBData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "Obligations", 2);

        Map<Integer, Object> columnData = new HashMap<>();
        for (int i = 1; i < OBData.size() - 3; i++) {
            columnData.put(i, OBData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-Obligations-20180709.xlsm");

        if (xls.writeRowData("Obligation", 6, columnData) == true) {
            EndUserBulkCreateJob eubcj = new EndUserBulkCreateJob();
            if (entityParentType.equalsIgnoreCase("Contract")) {
                eubcj.EndUserBulkCreateObligationTest(entityParentType, BIContract);
            } else if (entityParentType.equalsIgnoreCase("Supplier")) {
                eubcj.EndUserBulkCreateObligationTest(entityParentType, BISupplier);
            }

            EndUserBulkCreateShowPageVerification EUBCSPV = new EndUserBulkCreateShowPageVerification();
            EUBCSPV.EndUserBulkCreateObligationTest("mcd-Obligations-20180709.xlsm");
        }
    }

    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if (testResult.getStatus() == ITestResult.SKIP)
            TestUtil.reportDataSetResult(bulk_create_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");

        else if (testResult.getStatus() == ITestResult.FAILURE) {
            isTestPass = false;
            TestUtil.reportDataSetResult(bulk_create_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
            result = "Fail";
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(bulk_create_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");
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
            TestUtil.reportDataSetResult(bulk_create_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_create_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(bulk_create_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_create_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(bulk_create_suite_xls, this.getClass().getSimpleName());
    }
}
