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


public class ServiceData extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    Map<Integer, Object> servicedatacolumnData = new HashMap<>();

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
    public void BulkCreateServiceDataTest(String entityParentType, String Sino, String BISupplier, String BIContract, String BIServiceCategory, String BIServiceSubCategory, String BIName, String BIContractService, String BIServiceIdClient, String BIServiceIdSupplier,
                                          String BIUnitType, String BICurrency, String BIAggregatingService, String BISplitatlowercontractlevel, String BIIsBillable, String BIConsumptionAvailable, String BIForecastingAvailable,
                                          String BIPricingAvailable, String BIBaseSpecificLineItem, String BIInvoicingType, String BIParentServiceDataClientServiceId, String BIForecastingFrequency, String BIForecastType, String BIForecastDataType,
                                          String BIAcceptableForecastPeriod, String BIDescription, String BIContractServiceFlag, String BISplitAttributeType, String BISplitRatioType, String BIConversionMatrix, String BICurrencyConversionDate,
                                          String BIIntermediateCurrency, String BIRoundingRule, String BINoofDecimals, String BIRoundingRuleSlabs, String BINoofDecimalsSlabs, String ServiceDataManager, String IDServiceStartDate,
                                          String IDBillingPeriod, String IDServiceEndDate, String Functions, String Services, String Regions, String Countries, String Process) throws InterruptedException, IOException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        //Second Sheet "Data" of mcd-Service Data-20180710.xlsm
        List<String> ServiceDataData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "ServiceData", 2);

        for (int i = 1; i < ServiceDataData.size() - 3; i++) {
            servicedatacolumnData.put(i, ServiceDataData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-Service Data-20180710.xlsm");

        if (xls.writeRowData("Data", 6, servicedatacolumnData) == true) {
            EndUserBulkCreateJob eubcj = new EndUserBulkCreateJob();
            if (entityParentType.equalsIgnoreCase("Contract")) {
                eubcj.EndUserBulkCreateServiceDataTest(entityParentType, BIContract);
            } else if (entityParentType.equalsIgnoreCase("Supplier")) {
                eubcj.EndUserBulkCreateServiceDataTest(entityParentType, BISupplier);
            }
            EndUserBulkCreateShowPageVerification EUBCSPV = new EndUserBulkCreateShowPageVerification();
            EUBCSPV.EndUserBulkCreateServiceDataTest("mcd-Service Data-20180710.xlsm");

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
