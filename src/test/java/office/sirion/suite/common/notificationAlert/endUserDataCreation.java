package office.sirion.suite.common.notificationAlert;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import testlink.api.java.client.TestLinkAPIResults;
import java.io.IOException;
import java.util.List;

public class endUserDataCreation extends TestSuiteBase{

    String runmodes[]=null;
    static int count=-1;
    static boolean fail=true;
    static boolean skip=false;
    static boolean isTestPass=true;
    String testCaseID=null;
    String result=null;

    @BeforeTest
    public void checkTestSkip() {
        if(!TestUtil.isTestCaseRunnable(notification_alert_xls,this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
            throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(notification_alert_xls, this.getClass().getSimpleName());
    }

    @Test (dataProvider="getTestData")
    public void endUserDataCreationTest (String entityTypeName, String entityTypeField, String fieldName, String entityFieldValue) throws InterruptedException {
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for Test Data -- " + entityTypeName + "with "+ fieldName + "as "+ entityTypeField +" is set to NO " + count);
        }

        openBrowser();

        //Contract Entity
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        driver.get(CONFIG.getProperty("endUserURL"));


        //Contract Entity

        if(entityTypeName.equalsIgnoreCase("Contracts")) {


            fluentWaitMethod(locateElementBy("contracts_quick_link"));

            locateElementBy("contracts_quick_link").click();

            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            Logger.debug("Executing Test Case Contract Update with Name -- " + driver.findElement(By.xpath("//tr[1]/td[3][@class=' col_type_truncate col_wrap_documenttitle']")).getText());

            locateElementBy("enduser_contract_listing_page_first_entity_link").click();


            // CONTRACT - SHOW PAGE - EDIT BUTTON

            try {
                driver.findElement(By.xpath("//button[contains(.,'Restore')]")).click();
                String entityWFMessage = driver.findElement(By.className("success-icon")).getText();
                if (entityWFMessage.equalsIgnoreCase("In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?")) {
                    Assert.assertEquals(entityWFMessage, "In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?");

                    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Yes')]")));
                    driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();

                }

                WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

                fluentWaitMethod(locateElementBy("co_create_page_name_textbox"));

            } catch (NoSuchElementException e) {
                WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

                fluentWaitMethod(locateElementBy("co_create_page_name_textbox"));
            }

            // CONTRACT - EDIT PAGE - Updating Pre-Define Fields
            if (fieldName.equalsIgnoreCase("Tier")) {
                addFieldValue("co_create_page_tier_dropdown", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("co_create_page_title_textbox", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Custom Field - Date")) {
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue, "expirationDate");
            }

            // CONTRACT - EDIT PAGE - UPDATE BUTTON
            WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


            // CONTRACT - EDIT PAGE - FIELD VALIDATIONS
            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Contract -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Contract -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }

            Logger.debug("Execution against Test Data Creation of Notification Alert for Contract Entity is Completed");
        }
        //ACTION - ENTITY
        else if (entityTypeName.equalsIgnoreCase("Actions")){

            fluentWaitMethod(locateElementBy("actions_quick_link"));


            locateElementBy("actions_quick_link").click();

            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");


            locateElementBy("entity_listing_page_first_entry_link").click();


            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
            fluentWaitMethod(locateElementBy("action_create_page_title_textbox"));

            // Actions - Edit Page - Pre-Define Fields
            if (fieldName.equalsIgnoreCase("Tier")) {
                addFieldValue("action_create_page_tier_dropdown", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("action_create_page_title_textbox", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Custom Field - Date")) {
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue, "plannedCompletionDate");
            }

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));

            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


            // ACTIONS - EDIT PAGE - FIELD VALIDATIONS
            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            Logger.debug("Execution against Test Data Creation of Notification Alert for Action Entity is Completed");
        }
        //CHANGE REQUEST ENTITY
        else if(entityTypeName.equalsIgnoreCase("Change Requests")){

            locateElementBy("cr_quick_link").click();

            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
            Thread.sleep(2000);

            locateElementBy("entity_listing_page_first_entry_link_cr").click();


            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
            fluentWaitMethod(locateElementBy("cr_create_page_title_textbox"));

            // Change Request - Edit Page - PRE-DEFINE FIELDS
            if(fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("cr_create_page_title_textbox", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Tier")) {
                addFieldValue("cr_create_page_tier_dropdown", entityFieldValue.trim());
            } else if (entityFieldValue.equalsIgnoreCase("Custom Field - Date")) {
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue.trim(), "plannedCompletionDate");
            }

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


            // Change Request - Update Page - VALIDATIONS

            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Change Request -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Change Request -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }

            Logger.debug("Execution against Test Data Creation of Notification Alert for Change Request Entity is Completed");
        }

        //  CHILD OBLIGATIONS
        else if(entityTypeName.equalsIgnoreCase("Child Obligations")) {

            locateElementBy("ob_quick_link").click();

            locateElementBy("global_bulk_listing_page_first_entry_link").click();
            fluentWaitMethod(locateElementBy("cob_edit_button"));

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            locateElementBy("cob_edit_button").click();


            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("cob_edit_page_update_button"));
            fluentWaitMethod(locateElementBy("cob_edit_page_update_button"));

            WebElement element = locateElementBy("cob_edit_page_update_button");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();

            Thread.sleep(5000);

            fail = false;
            driver.get(CONFIG.getProperty("endUserURL"));

        } else if(entityTypeName.equalsIgnoreCase("Child Service Levels")){
            locateElementBy("sl_quick_link").click();


            fluentWaitMethod(locateElementBy("csl_link_listing_page"));
            locateElementBy("csl_link_listing_page").click();

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
            Thread.sleep(2000);
            fluentWaitMethod(locateElementBy("entity_listing_page_first_entry_link"));

             locateElementBy("entity_listing_page_first_entry_link").click();


            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


            fail = false;

            driver.get(CONFIG.getProperty("endUserURL"));
        } else if (entityTypeName.equalsIgnoreCase("Disputes")) {

            Logger.debug("Executing Test Case Dispute Update with Title ---- " + entityTypeName);

            fluentWaitMethod(locateElementBy("disputes_quick_link"));


            locateElementBy("disputes_quick_link").click();

            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");


            locateElementBy("entity_listing_page_first_entry_link").click();


            //
            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
            Thread.sleep(8000);
            fluentWaitMethod(locateElementBy("dispute_show_page_id"));

            // dispute - Create Page - BASIC INFORMATION

            if(fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("dispute_create_page_title_textbox", entityFieldValue.trim());
            } else if(fieldName.equalsIgnoreCase("Tier")) {
                addFieldValue("dispute_create_page_tier_dropdown", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Custom Field - Date")) {
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue.trim(), "plannedCompletionDate");
            }

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();
            //

            // Change Request - Create Page - VALIDATIONS
            if(fieldName.equalsIgnoreCase("Title")) {
                if (entityFieldValue.equalsIgnoreCase("")) {
                    fail = true;
                    if (fieldValidationMandatory("dispute_create_page_title_textbox")) {
                        Logger.debug("Dispute Title is Mandatory");

                        fail = false;
                        driver.get(CONFIG.getProperty("endUserURL"));
                        return;
                    }
                }
            }
            fail = false;
            driver.get(CONFIG.getProperty("endUserURL"));
        } else if(entityTypeName.equalsIgnoreCase("Governance Body")){

            //
            fluentWaitMethod(locateElementBy("gb_quick_link"));

            locateElementBy("gb_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODY")));

            driver.findElement(By.linkText("VIEW GOVERNANCE BODY")).click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            locateElementBy("entity_listing_page_first_entry_link").click();

            //
            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
            fluentWaitMethod(locateElementBy("gb_create_page_title_textbox"));

            // Governance Body - Create Page - BASIC INFORMATION

            if(fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("gb_create_page_title_textbox", entityFieldValue.trim());
            }else if(fieldName.equalsIgnoreCase("Custom Field - Date")) {
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue.trim(), "effectiveDate");
            }

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();
            //

            if(fieldName.equalsIgnoreCase("Title")) {
                if (entityFieldValue.equalsIgnoreCase("")) {
                    if (fieldValidationMandatory("gb_create_page_title_textbox")) {
                        Logger.debug("Governance Body Title is Mandatory");
                        return;
                    }
                }
            }
            fail=false;
            driver.get(CONFIG.getProperty("endUserURL"));
        } else if(entityTypeName.equalsIgnoreCase("Governance Body Meetings")){

        } else if(entityTypeName.equalsIgnoreCase("Issues")){

            fluentWaitMethod(locateElementBy("issues_quick_link"));
            //

            locateElementBy("issues_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
            //

            locateElementBy("entity_listing_page_first_entry_link").click();

            Thread.sleep(8000);
            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
            driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
            fluentWaitMethod(locateElementBy("issue_show_page_id"));

            // issues - update Page - BASIC INFORMATION
            if (fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("issue_update_page_Title_textbox", entityFieldValue.trim());
            }else if (fieldName.equalsIgnoreCase("Tier")){
                addFieldValue("issue_update_page_Tier_dropdown", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Custom Field - Date")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue.trim(),"plannedCompletionDate");
            }

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

            //

            fail = false;

            driver.get(CONFIG.getProperty("endUserURL"));

        } else if (entityTypeName.equalsIgnoreCase("Obligations")){

            //
            fluentWaitMethod(locateElementBy("ob_quick_link"));

            locateElementBy("ob_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));

            driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

                    locateElementBy("global_bulk_listing_page_first_entry_link").click();

            // OBLIGATIONS - SHOW PAGE - EDIT BUTTON
            //
            try{
                driver.findElement(By.xpath(".//button[contains(.,'Restore')]")).click();
                //
                WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
                //
                fluentWaitMethod(locateElementBy("ob_create_page_title_textbox"));
            } catch (NoSuchElementException e){
                WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
                //
                fluentWaitMethod(locateElementBy("ob_create_page_title_textbox"));
            }

            // OBLIGATIONS - EDIT PAGE - BASIC INFORMATION
            if(fieldName.equalsIgnoreCase("Title")){
            addFieldValue("ob_create_page_title_textbox", entityFieldValue.trim());
            } else if (fieldName.equalsIgnoreCase("Tier")){
                addFieldValue("ob_create_page_tier_dropdown", entityFieldValue.trim());
            }else if(fieldName.equalsIgnoreCase("Custom Field - Date")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue.trim(), "effectiveDate");
            }

            // OBLIGATIONS - EDIT PAGE - UPDATE BUTTON
            WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();
            //

            // OBLIGATIONS - EDIT PAGE - FIELD VALIDATIONS
            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Obligations -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Obligations -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }
                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            fail=false;
            driver.get(CONFIG.getProperty("endUserURL"));

        } else if (entityTypeName.equalsIgnoreCase("Service Levels")){

            //
            fluentWaitMethod(locateElementBy("sl_quick_link"));

            locateElementBy("sl_quick_link").click();
            //
            try {
                fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
            }catch (NoSuchElementException e){
                //
            }
            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //

            locateElementBy("entity_listing_page_first_entry_link").click();

            //
            try{
                fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
            }catch (NoSuchElementException e){
                //
            }
            // SERVICE LEVELS - SHOW PAGE - EDIT BUTTON
            try{
                driver.findElement(By.xpath(".//button[contains(.,'Restore')]")).click();
                Thread.sleep(8000);
                WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
                //
                fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));

            }catch (NoSuchElementException e){
                WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
                //
                fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));
            }
            // SERVICE LEVELS - EDIT PAGE - BASIC INFORMATION

            if(fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("sl_create_page_title_textbox", entityFieldValue.trim());
            }else if(fieldName.equalsIgnoreCase("Tier")){
                addFieldValue("sl_create_page_tier_dropdown", entityFieldValue.trim());
            } else if(fieldName.equalsIgnoreCase("Custom Field - Date")){
                addFieldValue("sl_create_page_reporting_effective_date", entityFieldValue.trim());
            }

            // SERVICE LEVELS - EDIT PAGE - UPDATE BUTTON
            WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));

            elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();
            //

            // SERVICE LEVELS - EDIT PAGE - FIELD VALIDATIONS
            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Service Levels -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Service Levels -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }
                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            fail=false;
            driver.get(CONFIG.getProperty("endUserURL"));

        } else if (entityTypeName.equalsIgnoreCase("Work Order Requests")){

            fluentWaitMethod(locateElementBy("wor_quick_link"));
            //

            locateElementBy("wor_quick_link").click();
            //
            fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
            Thread.sleep(2000);

            driver.findElement(By.xpath("//div[@id='dm']//tr[1]/td[1]/a")).click();

            //

            fluentWaitMethod(driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")).click();
            //
            driver.navigate().refresh();
            fluentWaitMethod(driver.findElement(By.xpath("//span[@id='elem_901']")));
            //

            // WOR - Create Page - BASIC INFORMATION
            if(fieldName.equalsIgnoreCase("Title")) {
                addFieldValue("wor_create_page_title_textbox", entityFieldValue.trim());
            } else if(fieldName.equalsIgnoreCase("Tier")){
                addFieldValue("wor_create_page_tier_dropdown", entityFieldValue.trim());
            } else if(fieldName.equalsIgnoreCase("Custom Field - Date")){
                // WOR - Create Page - IMPORTANT DATES
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(entityFieldValue,"effectiveDate");
            }

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
            driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();
            //

            // WORK ORDER REQUEST - EDIT PAGE - FIELD VALIDATIONS
            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Service Levels -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Service Levels -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }
                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            fail=false;
            driver.get(CONFIG.getProperty("endUserURL"));
        }
    }
    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if(testResult.getStatus()==ITestResult.SKIP)
            TestUtil.reportDataSetResult(notification_alert_xls, this.getClass().getSimpleName(), count+2, "SKIP");
        else if(testResult.getStatus()==ITestResult.FAILURE) {
            isTestPass=false;
            TestUtil.reportDataSetResult(notification_alert_xls, this.getClass().getSimpleName(), count+2, "FAIL");
            result= "Fail";
        }
        else if (testResult.getStatus()==ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(notification_alert_xls, this.getClass().getSimpleName(), count+2, "PASS");
            result= "Pass";
        }
        try {
            if (!testCaseID.equalsIgnoreCase(""))
                TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
        } catch (Exception e) {
            Logger.debug(e);
        }
    }

    @AfterTest
    public void reportTestResult() {
        if(isTestPass)
            TestUtil.reportDataSetResult(notification_alert_xls, "Test Cases", TestUtil.getRowNum(notification_alert_xls,this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(notification_alert_xls, "Test Cases", TestUtil.getRowNum(notification_alert_xls,this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(notification_alert_xls, this.getClass().getSimpleName());
    }
}
