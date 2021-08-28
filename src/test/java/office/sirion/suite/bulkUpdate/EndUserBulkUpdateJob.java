package office.sirion.suite.bulkUpdate;

import office.sirion.suite.contract.TestSuiteBase;
import office.sirion.util.XLSUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.E;

public class EndUserBulkUpdateJob extends TestSuiteBase {

    List<String> EntityID = new ArrayList<>();

    public void EndUserBulkUpdateObligationDownloadTemplateTest(String EntityName) throws InterruptedException {
        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Obligations BulkUpdate with Titles -- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("ob_quick_link"));

        locateElementBy("ob_quick_link").click();
        //
        fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));

        driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

        if(!EntityName.equalsIgnoreCase("")) {

            for (String entityname : EntityName.split(";")) {
                driver.findElement(By.xpath("//a[contains(text(),'"+entityname+"')]/parent::td/parent::tr/td[1]/input")).click();
                String ids = driver.findElement(By.xpath("//a[contains(text(),'"+entityname+"')]/parent::td/parent::tr/td[3]/div/div/a")).getText();
                EntityID.add(ids);
            }

            new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
            locateElementBy("entity_bulk_update_template_download_button").click();

            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    locateElementBy("bulk_edit_alert_ok_button").click();
                    locateElementBy("bulk_edit_page_first_entity").click();
                    new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
                    locateElementBy("entity_bulk_update_template_download_button").click();
                }
            }catch (NoSuchElementException e){
                Logger.debug("Entities are selected for Bulk Edit");
            }
            getLatestFilefromDir("E"+File.separator+"TestDataUIAutomation").renameTo(new File(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls/"+"mcd-Master Obligations-Update.xlsm"));
        }
    }

    public void EndUserBulkUpdateObligationUploadTemplateTest(String FileName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("ob_quick_link"));

        locateElementBy("ob_quick_link").click();
        //
        fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));

        driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

        new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
        locateElementBy("entity_bulk_update_template_upload_button").click();

        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(FileName);

        Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@id='bulkCreateServiceDialog']//input[@type='submit']")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//input[@value='OK']")).click();

            // BULK - CREATE POPUP - VALIDATIONS
         if (locateElementBy("entity_bulk_create_popup_notifications") != null) {
             List<WebElement> elementErrors = driver.findElements(By.xpath("//div[@class='error']"));
             for (WebElement element : elementErrors) {
                if (element.getText().contains("File extension csv not supported")) {
                    Logger.debug("Template File Incorrect, please re-check it and try again");
                } else if (element.getText().contains("Incorrect Template: Template Belongs To Different Parent Entity")) {
                    Logger.debug("Template File Incorrect, please re-check it and try again");
                } else if (element.getText().contains("Please select a file")) {
                     Logger.debug("Please select a file");
                }
             }
               driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
        }

    public void EndUserBulkUpdateChildObligationDownloadTemplateTest(String EntityName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Obligations Bulk creation with Titles -- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("ob_quick_link"));

        locateElementBy("ob_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
        //

        if(!EntityName.equalsIgnoreCase("")) {

            for (String entityname : EntityName.split(";")) {
                driver.findElement(By.xpath("//a[contains(text(),'"+entityname+"')]/parent::td/parent::tr/td[1]/input")).click();
                String ids = driver.findElement(By.xpath("//a[contains(text(),'"+entityname+"')]/parent::td/parent::tr/td[3]/div/div/a")).getText();
                EntityID.add(ids);
            }

            new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
            locateElementBy("entity_bulk_update_template_download_button").click();

            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    locateElementBy("bulk_edit_alert_ok_button").click();
                    locateElementBy("bulk_edit_page_first_entity").click();
                    new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
                    locateElementBy("entity_bulk_update_template_download_button").click();
                }
            }catch (NoSuchElementException e){
                Logger.debug("Entities are selected for Bulk Edit");
            }
            getLatestFilefromDir("E"+File.separator+"TestDataUIAutomation").renameTo(new File(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls/"+"mcd-Child Obligation-Update.xlsm"));
        }
        }

    public void EndUserBulkUpdateChildObligationUploadTemplateTest(String FileName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
        locateElementBy("entity_bulk_update_template_download_button").click();

        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(FileName);

            Thread.sleep(3000);
            driver.findElement(By.xpath("//div[@id='bulkCreateServiceDialog']//input[@type='submit']")).click();
            Thread.sleep(5000);
            driver.findElement(By.xpath("//input[@value='OK']")).click();

            // BULK - CREATE POPUP - VALIDATIONS
            if (locateElementBy("entity_bulk_create_popup_notifications") != null) {
                List<WebElement> elementErrors = driver.findElements(By.xpath("//div[@class='error']"));
                for (WebElement element : elementErrors) {
                    if (element.getText().contains("File extension csv not supported")) {
                        Logger.debug("Template File Incorrect, please re-check it and try again");
                    } else if (element.getText().contains("Incorrect Template: Template Belongs To Different Parent Entity I.e. CO01001, Kindly Use The Correct Template.")) {
                        Logger.debug("Template File Incorrect, please re-check it and try again");
                    } else if (element.getText().contains("Please select a file")) {
                        Logger.debug("Please select a file");
                    }
                }
                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
        }

    public void EndUserBulkUpdateDisputeDownloadTemplateTest(String EntityName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Update on Dispute -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

            locateElementBy("disputes_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

        if(!EntityName.equalsIgnoreCase("")) {

            for (String entityname : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + entityname + "']/preceding-sibling::td//input")).click();
                String ids = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='"+ entityname +"']/preceding-sibling::td[1]/a")).getText();
                EntityID.add(ids);
            }

            new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
            locateElementBy("entity_bulk_update_template_download_button").click();

            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    locateElementBy("bulk_edit_alert_ok_button").click();
                    locateElementBy("bulk_edit_page_first_entity").click();
                    new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
                    locateElementBy("entity_bulk_update_template_download_button").click();
                }
            }catch (NoSuchElementException e){
                Logger.debug("Entities are selected for Bulk Edit");
            }
            getLatestFilefromDir("E"+File.separator+"TestDataUIAutomation").renameTo(new File(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls/"+"mcd-Dispute Management-Update.xlsm"));
        }
    }

    public void EndUserBulkUpdateDisputeUploadTemplateTest(String FileName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Update on Dispute -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        locateElementBy("disputes_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
        //


        new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
        locateElementBy("entity_bulk_update_template_upload_button").click();
        locateElementBy("entity_bulk_update_template_upload_browse_button").sendKeys(FileName);
        locateElementBy("entity_bulk_update_template_upload_submit_button").click();

        Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@id='bulkCreateServiceDialog']//input[@type='submit']")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//input[@value='OK']")).click();

        // BULK - CREATE POPUP - VALIDATIONS
        if (locateElementBy("entity_bulk_create_popup_notifications") != null) {
            List<WebElement> elementErrors = driver.findElements(By.xpath("//div[@class='error']"));
            for (WebElement element : elementErrors) {
                if (element.getText().contains("File extension csv not supported")) {
                    Logger.debug("Template File Incorrect, please re-check it and try again");
                } else if (element.getText().contains("Incorrect Template: Template Belongs To Different Parent Entity")) {
                    Logger.debug("Template File Incorrect, please re-check it and try again");
                } else if (element.getText().contains("Please select a file")) {
                    Logger.debug("Please select a file");
                }
            }
            driver.get(CONFIG.getProperty("endUserURL"));
            return;
        }

    }

    public void EndUserBulkUpdateChildServiceLevelDownloadTemplateTest(String EntityName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        fluentWaitMethod(locateElementBy("contracts_quick_link"));

        locateElementBy("contracts_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
        //

        if(!EntityName.equalsIgnoreCase("")) {

            for (String entityname : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + entityname + "']/preceding-sibling::td//input")).click();
                String ids = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='"+ entityname +"']/preceding-sibling::td[1]/a")).getText();
                EntityID.add(ids);
            }

            new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
            locateElementBy("entity_bulk_update_template_download_button").click();

            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    locateElementBy("bulk_edit_alert_ok_button").click();
                    locateElementBy("bulk_edit_page_first_entity").click();
                    new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
                    locateElementBy("entity_bulk_update_template_download_button").click();
                }
            }catch (NoSuchElementException e){
                Logger.debug("Entities are selected for Bulk Edit");
            }
            getLatestFilefromDir("E"+File.separator+"TestDataUIAutomation").renameTo(new File(System.getProperty("user.dir") + "/src/test/java/office/sirion/xls/"+"mcd-Child Service Level-Update.xlsm"));
        }
    }

    public void EndUserBulkUpdateChildServiceLevelUploadTemplateTest(String FileName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        fluentWaitMethod(locateElementBy("contracts_quick_link"));

        locateElementBy("contracts_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
        //

        new Actions(driver).moveToElement(locateElementBy("entity_bulk_update_button_listing_page")).click().build().perform();
        locateElementBy("entity_bulk_update_template_upload_button").click();

        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(FileName);

        Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@id='bulkCreateServiceDialog']//input[@type='submit']")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//input[@value='OK']")).click();

        // BULK - CREATE POPUP - VALIDATIONS
        if (locateElementBy("entity_bulk_create_popup_notifications") != null) {
            List<WebElement> elementErrors = driver.findElements(By.xpath("//div[@class='error']"));
            for (WebElement element : elementErrors) {
                if (element.getText().contains("File extension csv not supported")) {
                    Logger.debug("Template File Incorrect, please re-check it and try again");
                } else if (element.getText().contains("Incorrect Template: Template Belongs To Different Parent Entity I.e. CO01001, Kindly Use The Correct Template.")) {
                    Logger.debug("Template File Incorrect, please re-check it and try again");
                } else if (element.getText().contains("Please select a file")) {
                    Logger.debug("Please select a file");
                }
            }
            driver.get(CONFIG.getProperty("endUserURL"));
            return;
        }

    }

    public static File getLatestFilefromDir(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

}

