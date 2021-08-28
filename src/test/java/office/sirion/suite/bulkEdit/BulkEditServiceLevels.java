package office.sirion.suite.bulkEdit;


import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import testlink.api.java.client.TestLinkAPIResults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BulkEditServiceLevels extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    List<String> EntityID = new ArrayList<>();

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(bulk_edit_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(bulk_create_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void BulkEditServiceLevelTest(String EntityName, String title, String description, String slid, String sl_category, String sl_sub_category, String sl, String rag_applicable,
                                           String service_level_kpi, String scope_of_service_1, String scope_of_service_2, String timezone, String delivery_countries, String currency,
                                           String supplier_access, String performance_computation_calculation, String performance_data_calculation, String unique_data_criteria,
                                           String priority, String minimum_maximum, String unit_of_sl_measurement, String maximum, String expected, String significantly_maximum,
                                           String measurement_window, String ytd_start_date, String ytd_average, String subject_to_continuous_improvement, String continuous_improvement_clause_textbox,
                                           String start_date, String end_date, String frequency, String frequency_type, String computation_frequency, String week_type, String pattern_date,
                                           String effective_date, String reporting_frequency_type, String reporting_computation_frequency, String reporting_week_type, String reporting_pattern_date,
                                           String reporting_effective_date, String project_id, String initiatives, String project_levels, String vendor_contracting_party,
                                           String recipient_client_entity, String company_code, String master_service_levels_manager, String responsibility, String financial_impact_applicable,
                                           String financial_impact_value, String impact_days, String impact_type, String credit_impact_applicable, String credit_impact_value)
            throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("sl_quick_link"));

        locateElementBy("sl_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
        //

        if (!EntityName.equalsIgnoreCase("")) {
            for (String name : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + name + "']/preceding-sibling::td//input")).click();
                String ids = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + name + "']/preceding-sibling::td[1]/a")).getText();
                EntityID.add(ids);
            }

            locateElementBy("bulk_edit_listing_page_button").click();
            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    locateElementBy("bulk_edit_alert_ok_button").click();
                    locateElementBy("bulk_edit_page_first_entity").click();
                    locateElementBy("bulk_edit_listing_page_button").click();
                }
            } catch (NoSuchElementException e) {
                Logger.debug("Entities are selected for Bulk Edit");
            }

            locateElementBy("bulk_edit_search_dropdown").click();

            locateElementBy("bulk_edit_basic_information_checkbox").click();//Select All fields in Basic Information Section
            locateElementBy("bulk_edit_important_date_checkbox").click();//Select All fields in Important Dates Section
            locateElementBy("bulk_edit_stakeholders_checkbox").click();//Select All fields in StakeHolders Section

            locateElementBy("bulk_edit_page_general_tab").click();

            if (!title.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_title_textbox", title);
            }
            if (!description.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_description_textarea", description);
            }
            if (!slid.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_sl_id_textarea", slid);
            }
            if (!sl_category.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_sl_category_dropdown", sl_category);
            }
            if (!sl_sub_category.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_sl_sub_category_dropdown", sl_sub_category);
            }
            if (!sl.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_sl_item_dropdown", sl);
            }
            if (!rag_applicable.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_rag_applicable_dropdown", rag_applicable);
            }
            if (!service_level_kpi.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_sl_kpi_dropdown", service_level_kpi);
            }
            if (!scope_of_service_1.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_scope_of_service_one_dropdown", scope_of_service_1);
            }
            if (!scope_of_service_2.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_scope_of_service_two_dropdown", scope_of_service_2);
            }
            if (!timezone.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_timezone_dropdown", timezone);
            }
            if (!delivery_countries.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_delivery_countries_multi_dropdown", delivery_countries);
            }
            if (!currency.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_currency_dropdown", currency);
            }
            if (!supplier_access.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_supplier_access_checkbox", supplier_access);
            }
            if (!performance_computation_calculation.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_performance_computation_calculation_textarea", performance_computation_calculation);
            }
            if (!performance_data_calculation.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_performance_data_calculation_textarea", performance_data_calculation);
            }
            if (!unique_data_criteria.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_unique_criteria_textarea", unique_data_criteria);
            }
            if (!priority.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_priority_dropdown", priority);
            }
            if (!minimum_maximum.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_minimum_maximum_dropdown", minimum_maximum);
            }
            if (!unit_of_sl_measurement.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_unit_of_sl_measurement_dropdown", unit_of_sl_measurement);
            }
            if (!maximum.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_minimum_maximum_numeric_box", maximum);
            }
            if (!expected.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_expected_numeric_box", expected);
            }
            if (!significantly_maximum.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_significantly_min_max_numeric_box", significantly_maximum);
            }
            if (!measurement_window.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_measurement_window_dropdown", measurement_window);
            }
            if (!ytd_start_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_ytd_start_date", ytd_start_date);
            }
            if (!ytd_average.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_ytd_average_textbox", ytd_average);
            }
            if (!subject_to_continuous_improvement.equalsIgnoreCase("No")) {
                addFieldValue("bulk_edit_sla_subject_to_continuous_improvement_checkbox", subject_to_continuous_improvement);
                addFieldValue("bulk_edit_sla_continuous_improvement_clause_textbox", continuous_improvement_clause_textbox);
            }
            if (!start_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_start_date", start_date);
            }
            if (!end_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_end_date", end_date);
            }
            if (!frequency.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_frequency_dropdown", frequency);
            }
            if (!frequency_type.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_frequency_type_dropdown", frequency_type);
            }
            if (!computation_frequency.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_computation_frequency_dropdown", computation_frequency);
            }
            if (!week_type.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_week_type_dropdown", week_type);
            }
            if (!pattern_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_pattern_date", pattern_date);
            }
            if (!effective_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_effective_date", effective_date);
            }
            if (!reporting_frequency_type.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_reporting_frequency_type_dropdown", reporting_frequency_type);
            }
            if (!reporting_computation_frequency.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_reporting_frequency_dropdown", reporting_computation_frequency);
            }
            if (!reporting_week_type.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_reporting_week_type", reporting_week_type);
            }
            if (!reporting_pattern_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_reporting_pattern_date", reporting_pattern_date);
            }
            if (!reporting_effective_date.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_reporting_effective_date", reporting_effective_date);
            }
            if (!project_id.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_project_id_multi_dropdown", project_id);
            }
            if (!initiatives.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_initiatives_multi_dropdown", initiatives);
            }
            if (!project_levels.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_project_levels_multi_dropdown", project_levels);
            }
            if (!vendor_contracting_party.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_vendor_contracting_party_multi_dropdown", vendor_contracting_party);
            }
            if (!recipient_client_entity.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_recipient_client_entities_multi_dropdown", recipient_client_entity);
            }
            if (!company_code.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_contracting_company_codes_multi_dropdown", company_code);
            }
            if (!master_service_levels_manager.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_responsibility_dropdown", master_service_levels_manager);
            }
            if (!responsibility.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_responsibility_dropdown", responsibility);
            }
            if (!financial_impact_applicable.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_financial_impact_applicable_checkbox", financial_impact_applicable);
                addFieldValue("bulk_edit_sla_financial_impact_value_numeric_box", financial_impact_value);
                addFieldValue("bulk_edit_sla_impact_days_numeric_box", impact_days);
                addFieldValue("bulk_edit_sla_impact_type_multi_dropdown", impact_type);
            }

            if (!credit_impact_applicable.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_sla_credit_impact_applicable_checkbox", credit_impact_applicable);
                addFieldValue("bulk_edit_sla_credit_impact_value_numeric_box", credit_impact_value);
            }

        }
        locateElementBy("bulk_edit_submit_button").click();
        Thread.sleep(7000);

        if (driver.findElement(By.className("success-icon")).getText().equalsIgnoreCase("Your request has been successfully submitted.")) {
            locateElementBy("bulk_edit_popup_ok_button").click();
        }

        try {
            locateElementBy("bulk_edit_lock_icon");
            Logger.debug("Entities are locked for Bulk operations");
        } catch (NoSuchElementException e) {
            Logger.debug("Entities are now unlocked for Bulk operations");
        }

        driver.navigate().refresh();

        locateElementBy("end_user_quick_search_box").click();
        for (String id : EntityID) {
            locateElementBy("end_user_quick_search_box").sendKeys(id);
            locateElementBy("end_user_quick_search_box").sendKeys(Keys.RETURN);
        }

        if (!title.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_title_showpage").getText(),title);
        }
        if (!description.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_description_showpage").getText(),description);
        }
        if (!slid.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_sl_id_showpage").getText(),slid);
        }
        if (!sl_category.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_sl_category_showpage").getText(),sl_category);
        }
        if (!sl_sub_category.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_sl_sub_category_showpage").getText(),sl_sub_category);
        }
        if (!sl.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_sl_item_showpage").getText(),sl);
        }
        if (!rag_applicable.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_rag_applicable_showpage").getText(),rag_applicable);
        }
        if (!service_level_kpi.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_sl_kpi_showpage").getText(),service_level_kpi);
        }
        if (!scope_of_service_1.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_scope_of_service_one_showpage").getText(),scope_of_service_1);
        }
        if (!scope_of_service_2.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_scope_of_service_two_showpage").getText(),scope_of_service_2);
        }
        if (!timezone.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_timezone_showpage").getText(),timezone);
        }
        if (!delivery_countries.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_delivery_countries_multi_showpage").getText(),delivery_countries);
        }
        if (!currency.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_currency_showpage").getText(),currency);
        }
        if (!supplier_access.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_supplier_access_showpage").getText(),supplier_access);
        }
        if (!performance_computation_calculation.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_performance_computation_calculation_showpage").getText(),performance_computation_calculation);
        }
        if (!performance_data_calculation.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_performance_data_calculation_showpage").getText(),performance_data_calculation);
        }
        if (!unique_data_criteria.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_unique_criteria_showpage").getText(),unique_data_criteria);
        }
        if (!priority.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_priority_showpage").getText(),priority);
        }
        if (!minimum_maximum.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_minimum_maximum_showpage").getText(),minimum_maximum);
        }
        if (!unit_of_sl_measurement.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_unit_of_sl_measurement_showpage").getText(),unit_of_sl_measurement);
        }
        if (!maximum.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_minimum_maximum_numeric_showpage").getText(),maximum);
        }
        if (!expected.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_expected_numeric_showpage").getText(),expected);
        }
        if (!significantly_maximum.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_significantly_min_max_numeric_showpage").getText(),significantly_maximum);
        }
        if (!measurement_window.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_measurement_window_showpage").getText(),measurement_window);
        }
        if (!ytd_start_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_ytd_start_showpage").getText(),ytd_start_date);
        }
        if (!ytd_average.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_ytd_average_showpage").getText(),ytd_average);
        }
        if (!subject_to_continuous_improvement.equalsIgnoreCase("No")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_subject_to_continuous_improvement_showpage").getText(),subject_to_continuous_improvement);
            Assert.assertEquals(locateElementBy("bulk_edit_sla_continuous_improvement_clause_showpage").getText(),continuous_improvement_clause_textbox);
        }
        if (!start_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_start_showpage").getText(),start_date);
        }
        if (!end_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_end_showpage").getText(),end_date);
        }
        if (!frequency.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_frequency_showpage").getText(),frequency);
        }
        if (!frequency_type.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_frequency_type_showpage").getText(),frequency_type);
        }
        if (!computation_frequency.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_computation_frequency_showpage").getText(),computation_frequency);
        }
        if (!week_type.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_week_type_showpage").getText(),week_type);
        }
        if (!pattern_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_pattern_showpage").getText(),pattern_date);
        }
        if (!effective_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_effective_showpage").getText(),effective_date);
        }
        if (!reporting_frequency_type.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_reporting_frequency_type_showpage").getText(),reporting_frequency_type);
        }
        if (!reporting_computation_frequency.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_reporting_frequency_showpage").getText(),reporting_computation_frequency);
        }
        if (!reporting_week_type.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_reporting_week_type_showpage").getText(),reporting_week_type);
        }
        if (!reporting_pattern_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_reporting_pattern_showpage").getText(),reporting_pattern_date);
        }
        if (!reporting_effective_date.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_reporting_effective_showpage").getText(),reporting_effective_date);
        }
        if (!project_id.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_project_id_multi_showpage").getText(),project_id);
        }
        if (!initiatives.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_initiatives_multi_showpage").getText(),initiatives);
        }
        if (!project_levels.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_project_levels_multi_showpage").getText(),project_levels);
        }
        if (!vendor_contracting_party.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_vendor_contracting_party_multi_showpage").getText(),vendor_contracting_party);
        }
        if (!recipient_client_entity.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_recipient_client_entities_multi_showpage").getText(),recipient_client_entity);
        }
        if (!company_code.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_contracting_company_codes_multi_showpage").getText(),company_code);
        }
        if (!master_service_levels_manager.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_responsibility_showpage").getText(),master_service_levels_manager);
        }
        if (!responsibility.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_responsibility_showpage").getText(),responsibility);
        }
        if (!financial_impact_applicable.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_financial_impact_applicable_showpage").getText(),financial_impact_applicable);
            Assert.assertEquals(locateElementBy("bulk_edit_sla_financial_impact_value_numeric_showpage").getText(),financial_impact_value);
            Assert.assertEquals(locateElementBy("bulk_edit_sla_impact_days_numeric_showpage").getText(),impact_days);
            Assert.assertEquals(locateElementBy("bulk_edit_sla_impact_type_multi_showpage").getText(),impact_type);
        }

        if (!credit_impact_applicable.equalsIgnoreCase("")) {
            Assert.assertEquals(locateElementBy("bulk_edit_sla_credit_impact_applicable_showpage").getText(),credit_impact_applicable);
            Assert.assertEquals(locateElementBy("bulk_edit_sla_credit_impact_value_numeric_showpage").getText(),credit_impact_value);
        }


        fail = false;
        driver.get(CONFIG.getProperty("clientAdminURL"));

        return;
    }

    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if (testResult.getStatus() == ITestResult.SKIP)
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");

        else if (testResult.getStatus() == ITestResult.FAILURE) {
            isTestPass = false;
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
            result = "Fail";
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");
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
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_edit_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_edit_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(bulk_edit_suite_xls, this.getClass().getSimpleName());
    }
}
