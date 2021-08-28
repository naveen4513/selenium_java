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


public class Dispute extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    Map<Integer, Object> disputecolumnData = new HashMap<>();

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
    public void BulkCreateDisputeTest(String entityParentType, String Sino, String BISupplier, String BIContract, String BITitle, String BIDescription, String BIType, String BIMilestone, String BIPriority, String BICurrency, String BIFinancialImpact,
                                      String BIVFadditionalexpensesincurredbytheclaim, String BIValueSettled, String BIValueClaimed, String BIPotentialClaimValue, String BIIdentifiedClaimValue, String BIValueRealised,
                                      String BIValueofotherdamagesincurredfromthisclaim, String BIExpectedClaimValue, String BIRealizationAmount, String BISavingsValue, String BIDeliveryCountries, String BITimeZone,
                                      String BIRestrictPublicAccess, String BIRestrictRequesterAccess, String BISupplierAccess, String BITier, String BIDependentEntity, String BIGovernanceBodyMeeting, String IssueDate,
                                      String PlannedCompletionDate, String RecipientClientEntity, String ContractingClientEntity, String ContractingCompanyCode, String CompanyCode, String Functions, String Services,
                                      String Regions, String Countries, String ContractCountries, String DisputesManager, String Responsibility, String RIProcessAreaImpacted, String RIActionTaken, String RIResolutionRemarks,
                                      String RIClosedDate, String PIProjectID, String PIInitiatives, String PIProjectLevels, String Process) throws InterruptedException, IOException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        //Second Sheet "Data" of mcd-Dispute Management-20180710.xlsm
        List<String> DisputeData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "Dispute", 2);

        for (int i = 0; i < DisputeData.size() - 3; i++) {
            disputecolumnData.put(i, DisputeData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-Dispute Management-20180710.xlsm");

        if (xls.writeRowData("Disputes", 6, disputecolumnData) == true) {
            EndUserBulkCreateJob eubcj = new EndUserBulkCreateJob();
            if (entityParentType.equalsIgnoreCase("Contract")) {
                eubcj.EndUserBulkCreateObligationTest(entityParentType, BIContract);
            } else if (entityParentType.equalsIgnoreCase("Supplier")) {
                eubcj.EndUserBulkCreateObligationTest(entityParentType, BISupplier);
            }

            EndUserBulkCreateShowPageVerification EUBCSPV = new EndUserBulkCreateShowPageVerification();
            EUBCSPV.EndUserBulkCreateDisputeTest("mcd-Dispute Management-20180710.xlsm");
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
