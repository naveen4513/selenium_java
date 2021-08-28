package office.sirion.suite.bulkCreate;

import office.sirion.suite.contract.TestSuiteBase;
import office.sirion.util.EmailAttachmentDownloader;
import office.sirion.util.XLSUtils;

import java.util.List;
import java.util.Map;

public class EndUserBulkCreateShowPageVerification extends TestSuiteBase {

    static List<String> successrows = null;

    public void EndUserBulkCreateObligationTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateServiceLevelTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateInvoiceTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateInvoiceLineItemTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateClauseTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateContractTemplateTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateDisputeTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }

    public void EndUserBulkCreateServiceDataTest(String Uploadedfile) throws InterruptedException {

        EmailAttachmentDownloader.downloadEmailAttachments(Uploadedfile);
        Map<Integer, Integer> Error = XLSUtils.getColumnDataFromFailuresRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Error);
        Map<String, Integer> Success = XLSUtils.getColumnDataFromSuccessRows(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", 44, 6);
        System.out.println(Success);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Data Validation after Bulk Create on Service Data -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        for (String clientsqeunceentityid : Success.keySet()) {
            locateElementBy("end_user_search_link").sendKeys(clientsqeunceentityid);

            if (locateElementBy("end_user_quick_search_no_permission").getText().equalsIgnoreCase("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.")) {

                System.out.println("Either You Do Not Have The Required Permissions Or This Entity Has Been Deleted Or Doesn'T Exist Anymore.");

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            //
            for (Integer SuccessRows : Success.values()) {
                successrows = XLSUtils.getExcelDataOfAnyRow(System.getProperty("user.dir") + "\\Attachments", Uploadedfile, "Data", SuccessRows, 43);
            }

            for (String entityvalues : successrows)

                if (driver.getPageSource().contains(entityvalues)) {
                    System.out.println("Data got match with show page " + entityvalues);
                } else {
                    System.out.println("Data dows not match with show page " + entityvalues + " Please re-check it manually");
                }
        }
    }
}

