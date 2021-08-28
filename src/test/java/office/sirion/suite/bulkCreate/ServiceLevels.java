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


public class ServiceLevels extends TestSuiteBase {

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
    public void BulkCreateServiceLevelTest(String entityParentType, String Sino, String Supplier, String Contract, String Title, String Description, String Slid, String ServiceLevelKPI, String SLCategory, String SLSubCategory,
                                           String ScopeofService1, String SL, String ScopeofService2, String TimeZone, String DeliveryCountries, String GlobalLocal, String Currency, String SupplierAccess,
                                           String Tier, String PerformanceComputationCalculation, String PerformanceDataCalculation, String UniqueDataCriteria, String RagApplicable, String CGContractRegions,
                                           String CGContractCountries, String MasterServiceLevelsManager, String OIPriority, String OIReferences, String OIApplicationGroup, String CCCreditApplicable,
                                           String CCCreditApplicableDate, String CCCreditClause, String CCCreditOfInvoice, String CCSLCreditCategory, String CCSLCreditSubCategory, String CCSLCreditLineItem,
                                           String CCCreditFrequency, String PMinimumMaximum, String PUnitofSLMeasurement, String PMaximum, String PExpected, String PSignificantlyMaximum, String PMeasurementWindow,
                                           String PYTDStartDate, String PCalendarType, String DRRecipientClientEntity, String RCEContractingClientEntity, String DRCompanyCode, String RCEContractingCompanyCode,
                                           String CPVendorContractingParty, String ECEarnbackApplicable, String ECEarnbackApplicableDate, String ECEarnbackClause, String ECEarnbackCategory, String ECEarnbackSubCategory,
                                           String ECEarnbackLineItem, String ECEarnbackFrequency, String CISubjectToContinuousImprovement, String CIContinuousImprovementClause, String IDStartDate, String IDEndDate,
                                           String IDFrequency, String IDExcludeHoliday, String SDFrequencyType, String SDWeekType, String SDComputationFrequency, String SDPatternDate, String SDEffectiveDate,
                                           String RDReportingFrequencyType, String RDReportingComputationFrequency, String RDReportingWeekType, String RDReportingPatternDate, String RDReportingEffectiveDate,
                                           String Functions, String Services, String ServiceCategory, String MGRegions, String MGCountries, String PIProjectID, String PIInitiatives, String PIProjectLevels,
                                           String Responsibility, String FIFinancialImpactApplicable, String FICreditImpactApplicable, String FIFinancialImpactValue, String FIFinancialImpactClause, String CICreditImpactValue,
                                           String CICreditImpactClause, String ITImpactDays, String ITImpactType, String CAPrivacy, String CAComments, String CAActualDate, String CARequestedBy, String CAChangeRequest, String Process,
                                           String CLSino, String CLServiceLevelSino, String CLCLauseName, String CLPriortiy, String CLCreditMode, String CLCreditValue, String CLCreditamount, String ECSino, String ECServiceLevelSino,
                                           String ECCLCLauseName, String ECCLPriortiy, String ECCLCreditMode, String ECCLCreditValue, String ECCLCreditamount, String OISino, String OIServiceLevelSino, String OIClause, String OIPageNumber,
                                           String OIDocPageNumber, String OIReferencedocumentJSON)
            throws InterruptedException, IOException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        //Second Sheet "Service Level" of mcd-SLA-20180709.xlsm
        List<String> SLData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "ServiceLevels", 2);

        Map<Integer, Object> slcolumnData = new HashMap<>();
        for (int i = 1; i < SLData.size() - 23; i++) {
            slcolumnData.put(i, SLData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-SLA-20180709.xlsm");

        //Third Sheet "SL - credit clause" of mcd-SLA-20180709.xlsm
        List<String> SLCCData = XLSUtils.getExcelDataOfSpecificColumnOneROW(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "ServiceLevels", 2, 97, 103);

        Map<Integer, Object> slcccolumnData = new HashMap<>();
        for (int i = 0; i < SLCCData.size(); i++) {
            slcccolumnData.put(i, SLCCData.get(i));
        }

        //Fourth Sheet "SL - earnback clause" of mcd-SLA-20180709.xlsm
        List<String> SLECData = XLSUtils.getExcelDataOfSpecificColumnOneROW(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "ServiceLevels", 2, 104, 110);

        Map<Integer, Object> sleccolumnData = new HashMap<>();
        for (int i = 0; i < SLECData.size(); i++) {
            sleccolumnData.put(i, SLECData.get(i));
        }

        //Fifth Sheet "SL - contract reference" of mcd-SLA-20180709.xlsm
        List<String> SLCRData = XLSUtils.getExcelDataOfSpecificColumnOneROW(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "ServiceLevels", 2, 111, 115);

        Map<Integer, Object> slcrcolumnData = new HashMap<>();
        for (int i = 0; i < SLCRData.size(); i++) {
            slcrcolumnData.put(i, SLCRData.get(i));
        }

        if (xls.writeRowData("Service Level", 6, slcolumnData) == true) {
            EndUserBulkCreateJob eubcj = new EndUserBulkCreateJob();
            if (entityParentType.equalsIgnoreCase("Contract")) {
                eubcj.EndUserBulkCreateServiceLevelTest(entityParentType, Contract);
            } else if (entityParentType.equalsIgnoreCase("Supplier")) {
                eubcj.EndUserBulkCreateServiceLevelTest(entityParentType, Supplier);
            }

            EndUserBulkCreateShowPageVerification EUBCSPV = new EndUserBulkCreateShowPageVerification();
            EUBCSPV.EndUserBulkCreateServiceLevelTest("mcd-SLA-20180709.xlsm");
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
