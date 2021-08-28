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


public class Invoice extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    static Map<Integer, Object> invoicecolumnData = new HashMap<>();

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
    public void BulkCreateInvoiceTest(String entityParentType, String Sino, String BISupplier, String BIContract, String BIInvoiceNumber, String BITitle, String BIPONumber, String BIPurchaseOrder,
                                      String BICurrency, String BITimeZone, String BIContractingEntity, String BIInvoiceAmount, String BISupplierID, String BIClientID,
                                      String BISirionSupplierID, String BIBilltoAddress, String BIShiptoAddress, String BICostCenterCode, String BISupplierAddress,
                                      String BISupplierAccess, String BIEmailAddress, String BITier, String BIPhoneNumber, String BIPreviousInvoiceID, String RHRRecipientClientEntity,
                                      String RHRRecipientCompanyCode, String CHRContractingClientEntity, String CHRContractingCompanyCode, String IDInvoicePeriodStart,
                                      String IDInvoicePeriodEnd, String IDCreditPeriodindays, String IDInvoiceDate, String IDPaymentTerm, String IDExpectedReceiptDate,
                                      String IDSystemReceiptDate, String IDPaymentDueDate, String IDActualPaymentDate, String IDActualReceiptDate, String Functions,
                                      String Services, String ServiceCategory, String MGRegions, String MGCountries, String CGContractRegions, String CGContractCountries,
                                      String InvoiceManager, String FIAmountApproved, String FISupplierTAXID, String FITAX, String FITAXAmount, String FISupplierBankAccount,
                                      String FIDiscrepancyAmount, String FINoOfLineItems, String FINoOfLineItemsWithDiscrepancy, String FIPaidAmount, String FIResolvedDiscrepancy,
                                      String FIDisputeAmount, String FIDiscrepancyOvercharge, String FIDiscrepancyUndercharge, String FINetSavings, String PIProjectID,
                                      String PIInitiatives, String PIProjectLevels, String CAComments, String CAActualDate, String CARequestedBy, String CAChangeRequest, String Process)
            throws InterruptedException, IOException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        //Second Sheet "Invoices" of mcd-Invoice-20180710.xlsm
        List<String> InvoiceData = XLSUtils.getExcelDataOfOneRow(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "Bulk Create.xlsx", "Invoice", 2);

        for (int i = 0; i < InvoiceData.size() - 3; i++) {
            invoicecolumnData.put(i, InvoiceData.get(i));
        }
        XLSUtils xls = new XLSUtils(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls", "mcd-Invoice-20180710.xlsm");

        if (xls.writeRowData("Invoices", 6, invoicecolumnData) == true) {
            EndUserBulkCreateJob eubcj = new EndUserBulkCreateJob();
            if (entityParentType.equalsIgnoreCase("Contract")) {
                eubcj.EndUserBulkCreateObligationTest(entityParentType, BIContract);
            } else if (entityParentType.equalsIgnoreCase("Supplier")) {
                eubcj.EndUserBulkCreateObligationTest(entityParentType, BISupplier);
            }

            EndUserBulkCreateShowPageVerification EUBCSPV = new EndUserBulkCreateShowPageVerification();
            EUBCSPV.EndUserBulkCreateInvoiceTest("mcd-Invoice-20180710.xlsm");
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
