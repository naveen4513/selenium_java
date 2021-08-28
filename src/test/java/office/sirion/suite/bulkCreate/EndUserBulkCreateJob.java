package office.sirion.suite.bulkCreate;

import office.sirion.suite.contract.TestSuiteBase;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.List;


public class EndUserBulkCreateJob extends TestSuiteBase {

    public void EndUserBulkCreateObligationTest(String entityParentType, String entityParentName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        if (entityParentType.equalsIgnoreCase("Contract")) {
            fluentWaitMethod(locateElementBy("contracts_quick_link"));

            locateElementBy("contracts_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            String parentName = entityParentName.substring(0, entityParentName.length() - 12);

            if (!parentName.equalsIgnoreCase("") && parentName.length() <= 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + parentName + "']/preceding-sibling::td[1]/a")).click();
            else if (!parentName.equalsIgnoreCase("") && parentName.length() > 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + parentName + "']]/preceding-sibling::td[1]/a")).click();
            //
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[8]/label"))).clickAndHold().build().perform();
            Thread.sleep(300);

            driver.findElement(By.xpath("//ul//li[8]//ul[1]//li[2]//a[1]//label[1]")).click();

            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

        } else if (entityParentType.equalsIgnoreCase("Supplier")) {

            //
            fluentWaitMethod(locateElementBy("suppliers_quick_link"));

            locateElementBy("suppliers_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() <= 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + entityParentName + "']/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            else if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() > 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + entityParentName + "']]/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            //
            try {
                fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
            } catch (NoSuchElementException e) {
                //
            }

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            driver.findElement(By.xpath("//label[contains(text(),'Bulk Create Obligation')]")).click();

            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

    }

    public void EndUserBulkCreateServiceLevelTest(String entityParentType, String entityParentName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        if (entityParentType.equalsIgnoreCase("Contract")) {
            fluentWaitMethod(locateElementBy("contracts_quick_link"));

            locateElementBy("contracts_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("200");
            //

            String parentName = entityParentName.substring(0, entityParentName.length() - 12);

            if (!parentName.equalsIgnoreCase("") && parentName.length() <= 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + parentName + "']/preceding-sibling::td[1]/a")).click();
            else if (!parentName.equalsIgnoreCase("") && parentName.length() > 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + parentName + "']]/preceding-sibling::td[1]/a")).click();
            //
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[9]/label"))).clickAndHold().build().perform();
            Thread.sleep(300);

            driver.findElement(By.xpath("//ul//li[9]//ul[1]//li[2]//a[1]//label[1]")).click();

            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

        } else if (entityParentType.equalsIgnoreCase("Supplier")) {

            //
            fluentWaitMethod(locateElementBy("suppliers_quick_link"));

            locateElementBy("suppliers_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() <= 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + entityParentName + "']/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            else if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() > 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + entityParentName + "']]/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            //
            try {
                fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
            } catch (NoSuchElementException e) {
                //
            }

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            driver.findElement(By.xpath("//label[contains(text(),'Bulk Create SL')]")).click();

            Thread.sleep(3000);


            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

    }

    public void EndUserBulkCreateInvoiceTest(String entityParentType, String entityParentName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        if (entityParentType.equalsIgnoreCase("Contract")) {
            fluentWaitMethod(locateElementBy("contracts_quick_link"));

            locateElementBy("contracts_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            String parentName = entityParentName.substring(0, entityParentName.length() - 12);

            if (!parentName.equalsIgnoreCase("") && parentName.length() <= 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + parentName + "']/preceding-sibling::td[1]/a")).click();
            else if (!parentName.equalsIgnoreCase("") && parentName.length() > 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + parentName + "']]/preceding-sibling::td[1]/a")).click();
            //
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[5]/label"))).clickAndHold().build().perform();
            Thread.sleep(300);

            driver.findElement(By.xpath("//ul//li[5]//ul[1]//li[2]//a[1]//label[1]")).click();

            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

        } else if (entityParentType.equalsIgnoreCase("Supplier")) {

            //
            fluentWaitMethod(locateElementBy("suppliers_quick_link"));

            locateElementBy("suppliers_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() <= 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + entityParentName + "']/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            else if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() > 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + entityParentName + "']]/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            //
            try {
                fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
            } catch (NoSuchElementException e) {
                //
            }

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[5]/label"))).clickAndHold().build().perform();
            Thread.sleep(300);

            driver.findElement(By.xpath("//ul//li[5]//ul[1]//li[2]//a[1]//label[1]")).click();

            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

    }

    public void EndUserBulkCreateInvoiceLineItemTest(String entityParentName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Invoice Communication for -- " + entityParentName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("inv_quick_link"));

        locateElementBy("inv_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
        //

        String parentName = entityParentName.substring(0, entityParentName.length() - 12);

        if (!parentName.equalsIgnoreCase("") && parentName.length() <= 60) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + parentName + "']/preceding-sibling::td[2]/a")).click();
            } catch (NoSuchElementException e) {
                Logger.debug("Invoice with Title as " + parentName + " is not available, skipping Test-Case");
            }
        } else if (!parentName.equalsIgnoreCase("") && parentName.length() > 60) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + parentName + "']]/preceding-sibling::td[2]/a")).click();
            } catch (NoSuchElementException e) {
                Logger.debug("Invoice with Title as " + parentName + " is not available, skipping Test-Case");
            }
        }
        //
        fluentWaitMethod(locateElementBy("entity_create_page_comments_textarea"));

        fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

        new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

        new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[1]/label"))).clickAndHold().build().perform();
        Thread.sleep(300);

        driver.findElement(By.xpath("//ul//li[1]//ul[1]//li[6]//a[1]//label[1]")).click();

        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

    public void EndUserBulkCreateClauseTest() throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        locateElementBy("contract_draft_request_quick_link").click();
        fluentWaitMethod(driver.findElement(By.xpath("//span[@class='menu icon']")));
        Thread.sleep(5000);
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//span[@class='menu icon']"))).clickAndHold().build().perform();
        driver.findElement(By.xpath("//a[contains(text(),'Clauses')]")).click();

        fluentWaitMethod(driver.findElement(By.xpath("//a[@ng-title='Create Clause/Definition']")));
        Thread.sleep(5000);
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//a[@ng-title='Create Clause/Definition']"))).clickAndHold().build().perform();

        fluentWaitMethod(driver.findElement(By.xpath("//label[contains(text(),'Create Clause')]")));
        Thread.sleep(5000);
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//label[contains(text(),'Create Clause')]"))).clickAndHold().build().perform();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//div[contains(text(),'Bulk')]")).click();

        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Clause-20180710.xlsm");

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

    public void EndUserBulkCreateContractTemplateTest() throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        locateElementBy("contract_draft_request_quick_link").click();
        fluentWaitMethod(driver.findElement(By.xpath("//span[@class='menu icon']")));
        Thread.sleep(5000);
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//span[@class='menu icon']"))).clickAndHold().build().perform();
        driver.findElement(By.xpath("//a[contains(text(),'Contract Templates')]")).click();

        fluentWaitMethod(driver.findElement(By.xpath("///a[@ng-title='Create Contract Template']")));
        Thread.sleep(5000);
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//a[@ng-title='Create Contract Template']"))).clickAndHold().build().perform();

        fluentWaitMethod(driver.findElement(By.xpath("//label[contains(text(),'Create Contract Template')]")));
        Thread.sleep(5000);
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//label[contains(text(),'Create Contract Template')]"))).clickAndHold().build().perform();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//div[contains(text(),'Bulk')]")).click();

        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Clause-20180710.xlsm");

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

    public void EndUserBulkCreateDisputeTest(String entityParentType, String entityParentName) throws InterruptedException {

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case of Bulk Create on Obligation -- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        //

        if (entityParentType.equalsIgnoreCase("Contract")) {
            fluentWaitMethod(locateElementBy("contracts_quick_link"));

            locateElementBy("contracts_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            String parentName = entityParentName.substring(0, entityParentName.length() - 12);

            if (!parentName.equalsIgnoreCase("") && parentName.length() <= 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + parentName + "']/preceding-sibling::td[1]/a")).click();
            else if (!parentName.equalsIgnoreCase("") && parentName.length() > 60)
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + parentName + "']]/preceding-sibling::td[1]/a")).click();
            //
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[7]/label"))).clickAndHold().build().perform();
            Thread.sleep(300);

            driver.findElement(By.xpath("//ul//li[7]//ul[1]//li[2]//a[1]//label[1]")).click();

            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

        } else if (entityParentType.equalsIgnoreCase("Supplier")) {

            //
            fluentWaitMethod(locateElementBy("suppliers_quick_link"));

            locateElementBy("suppliers_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() <= 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + entityParentName + "']/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            else if (!entityParentName.equalsIgnoreCase("") && entityParentName.length() > 60)
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + entityParentName + "']]/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            //
            try {
                fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
            } catch (NoSuchElementException e) {
                //
            }

            fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

            new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

            new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[7]/label"))).clickAndHold().build().perform();
            Thread.sleep(300);

            driver.findElement(By.xpath("//ul//li[7]//ul[1]//li[2]//a[1]//label[1]")).click();

            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

    }

    public void EndUserBulkCreateServiceDataTest(String entityParentType, String entityParentName) throws InterruptedException {

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

        String parentName = entityParentName.substring(0, entityParentName.length() - 12);

        if (!parentName.equalsIgnoreCase("") && parentName.length() <= 60)
            driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + parentName + "']/preceding-sibling::td[1]/a")).click();
        else if (!parentName.equalsIgnoreCase("") && parentName.length() > 60)
            driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + parentName + "']]/preceding-sibling::td[1]/a")).click();
        //
        fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

        fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));

        new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

        new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id='drop']/ul/li[11]/label"))).clickAndHold().build().perform();
        Thread.sleep(300);

        driver.findElement(By.xpath("//ul//li[11]//ul[1]//li[2]//a[1]//label[1]")).click();

        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='fileDataBulkUpload']")).sendKeys(System.getProperty("user.dir") + "\\src\\test\\java\\office\\sirion\\xls\\mcd-Obligations-20180709.xlsm");

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

}

