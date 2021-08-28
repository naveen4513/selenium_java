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


public class InvoiceLineItem extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    Map<Integer, Object> invoicelineitemcolumnData = new HashMap<>();

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
    public void BulkCreateInvoiceLineItemTest(String Sino, String BISupplier, String BIContract, String BIInvoiceId, String BILineItemDescription, String BISupplierServiceCategory, String BILineItemType,
                                              String BIServiceData, String BIAttributevalue, String BISirionMilestoneChildID, String BIMilestoneDescription, String BIMilestoneIDClient, String BITimeZone,
                                              String BIMilestoneIDSupplier, String BILineItemNumber, String BIMilestoneDueDate, String BIMilestoneApprovedDate, String BIAdjustmentType, String BIReferenceInvoiceNo,
                                              String BIReferenceLineItemNumber, String IDServiceStartDate, String IDServiceEndDate, String IDInvoiceDate, String IDDueDate, String GDeliveryRegion,
                                              String GDeliveryCountry, String GBillingRegion, String GBillingCountry, String PurchaseOrder, String InvoiceLineItemManager, String SICurrency, String SIUnit,
                                              String SIRate, String SIConversionRate, String SIQuantity, String SIBaseVolume, String SIForecastedVolume, String SIVariance, String SIAmount, String SITax,
                                              String SITotal, String SIComments, String SYISystemCurrency, String SYISystemUnit, String SYISystemRate, String SYISystemConversionRate, String SYISystemQuantity,
                                              String SYISystemBaseVolume, String SYISystemForecastedVolume, String SYISystemVariance, String SYISystemAmount, String SYISystemTax, String SYISystemTotal,
                                              String SYISystemComments, String DiscrepancyRate, String DiscrepancyConversionRate, String DiscrepancyQuantity, String DiscrepancyBaseVolume, String DiscrepancyForecastedVolume,
                                              String DiscrepancyVariance, String DiscrepancyAmount, String DiscrepancyTax, String DiscrepancyTotal, String DiscrepancyComments, String DiscrepancyReason, String Type,
                                              String AmountInFavorOfSupplierTotal, String AmountInFavorOfClientTotal, String DiscrepancyResolutionAmount, String DiscrepencyResolutionPendingAmount,
                                              String DiscrepancyResolutionComments, String ApprovedAmount, String CAComments, String CAActualDate, String CARequestedBy, String CAChangeRequest, String Process)
            throws InterruptedException, IOException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        //Second Sheet "Data" of mcd-Invoice Line Item-20180710.xlsm
        List<String> InvoiceLineItemData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "InvoiceLineItem", 2);

        for (int i = 0; i < InvoiceLineItemData.size() - 3; i++) {
            invoicelineitemcolumnData.put(i, InvoiceLineItemData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-Invoice Line Item-20180710.xlsm");

        if (xls.writeRowData("Data", 6, invoicelineitemcolumnData) == true) {
            EndUserBulkCreateJob eubcj = new EndUserBulkCreateJob();
            eubcj.EndUserBulkCreateInvoiceLineItemTest(BIInvoiceId);

            EndUserBulkCreateShowPageVerification EUBCSPV = new EndUserBulkCreateShowPageVerification();
            EUBCSPV.EndUserBulkCreateInvoiceLineItemTest("mcd-Invoice Line Item-20180710.xlsm");
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
