package office.sirion.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import office.sirion.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


// Expected Date, Month and Year
// Calendar Month and Year
// Click on date text box to open date picker popup.
// This loop will be executed continuously till dateNotFound Is true.
// Retrieve current selected month name from date picker popup.
// Retrieve current selected year name from date picker popup.
// If current selected month and year are same as expected month and
// year then go Inside this condition.
// Call selectDate function with date to select and set
// dateNotFound flag to false.
// If current selected month and year are less than expected month
// and year then go Inside this condition.
// Click on next button of date picker.
// If current selected month and year are greater than expected
// month and year then go Inside this condition.
// Click on previous button of date picker.
// Loop will rotate till expected date not found.
// Select the date from date picker when condition match.
// Expected Date, Month and Year
// Calendar Month and Year
// Click on date text box to open date picker popup.
// This loop will be executed continuously till dateNotFound Is true.
// Retrieve current selected month name from date picker popup.
// Retrieve current selected year name from date picker popup.
// If current selected month and year are same as expected month and
// year then go Inside this condition.
// Call selectDate function with date to select and set
// dateNotFound flag to false.
// If current selected month and year are less than expected month
// and year then go Inside this condition.
// Click on next button of date picker.
// If current selected month and year are greater than expected
// month and year then go Inside this condition.
// Click on previous button of date picker.
// Loop will rotate till expected date not found.
// Select the date from date picker when condition match.

public class DatePicker_Enhanced extends TestBase {

	WebElement datePicker;
	List<WebElement> noOfColumns;
	List<String> monthList = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December");
	// Expected Date, Month and Year
	public String expMonth;
	public int expYear;
	public String expDate = null;
	// Calendar Month and Year
	String calMonth = null;
	String calYear = null;
	boolean dateNotFound;
	public int Month;
	String[] Actualdate = null;
    String[] ActualDate = null;
	
	public void pickExpDate(String CalendarName) throws InterruptedException {

		// Click on date text box to open date picker popup.
		Month = monthList.indexOf(expMonth)+1;
		try {
            driver.findElement(By.xpath("//input[@name='" + CalendarName + "']")).click();
            dateNotFound = true;

            // This loop will be executed continuously till dateNotFound Is true.
            while (dateNotFound) {
                // Retrieve current selected month name from date picker popup.
                calMonth = driver.findElement(By.className("btn btn-default btn-sm uib-title")).getText();

                // Retrieve current selected year name from date picker popup.

                WebElement currentYear = driver.findElement(By.className("ui-datepicker-year"));
                Select select_dropdown = new Select(currentYear);

                List<WebElement> options = select_dropdown.getOptions();
                for (WebElement option : options) {
                    if (option.isSelected()) {
                        calYear = option.getText();
                    }
                }

                // If current selected month and year are same as expected month and
                // year then go Inside this condition.
                if (monthList.indexOf(calMonth) + 1 == Month && (expYear == Integer.parseInt(calYear))) {
                    // Call selectDate function with date to select and set
                    // dateNotFound flag to false.
                    selectDate_Enhanced(expDate, expMonth, expYear);
                    dateNotFound = false;
                }
                // If current selected month and year are less than expected month
                // and year then go Inside this condition.
                else if (monthList.indexOf(calMonth) + 1 < Month && (expYear == Integer.parseInt(calYear))
                        || expYear > Integer.parseInt(calYear)) {
                    // Click on next button of date picker.
                    driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span")).click();
                }
                // If current selected month and year are greater than expected
                // month and year then go Inside this condition.
                else if (monthList.indexOf(calMonth) + 1 > Month && (expYear == Integer.parseInt(calYear))
                        || expYear < Integer.parseInt(calYear)) {
                    // Click on previous button of date picker.
                    driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[1]/span")).click();
                }
            }
            Thread.sleep(500);
        }catch (NoSuchElementException e){
		    Thread.sleep(5000);
        } catch (ElementNotVisibleException e1){
		    Thread.sleep(5000);
        }

	}

	public void selectDatePicker(String date) {
		datePicker = driver.findElement(By.id("ui-datepicker-div"));
		noOfColumns = datePicker.findElements(By.tagName("td"));

		// Loop will rotate till expected date not found.
		for (WebElement cell : noOfColumns) {
			// Select the date from date picker when condition match.
			if (cell.getText().equals(date)) {
				cell.findElement(By.linkText(date)).click();
				break;
			}
		}
	}
	
	public void selectCalendar(String actionActualDate, String calendarName) throws InterruptedException{
		if(!actionActualDate.equalsIgnoreCase("")){
		Actualdate = actionActualDate.split("-");
		
		int Actualmonth = monthList.indexOf(Actualdate[0])+1;
		int Currentmonth = monthList.indexOf(CurrentMonth())+1;
		
		if (Actualmonth < Currentmonth){
	    String actionActual_DateMonth = Actualdate[0];
	    String actionActual_DateYear = convertStringToInteger(Actualdate[2]);
	    int actionActual_dateYear = Integer.parseInt(actionActual_DateYear);
	    String actionActual_DateDate = convertStringToInteger(Actualdate[1]);
	    Integer actionActual_dateDate = Integer.parseInt(actionActual_DateDate);
	    actionActual_DateDate = actionActual_dateDate.toString();

	    DatePicker_Enhanced dp_actionActual_date = new DatePicker_Enhanced();
	    dp_actionActual_date.expDate = actionActual_DateDate;
	    dp_actionActual_date.expMonth = actionActual_DateMonth;
	    dp_actionActual_date.expYear = actionActual_dateYear;
	    dp_actionActual_date.pickExpDate(calendarName);
		}
		else{
			ActualDate = CurrentDate().split("-");
			String actionActual_DateMonth = ActualDate[0];
		    String actionActual_DateYear = convertStringToInteger(ActualDate[2]);
		    int actionActual_dateYear = Integer.parseInt(actionActual_DateYear);
		    String actionActual_DateDate = convertStringToInteger(ActualDate[1]);
		    Integer actionActual_dateDate = Integer.parseInt(actionActual_DateDate);
		    actionActual_DateDate = actionActual_dateDate.toString();

		    DatePicker_Enhanced dp_actionActual_date = new DatePicker_Enhanced();
		    dp_actionActual_date.expDate = actionActual_DateDate;
		    dp_actionActual_date.expMonth = actionActual_DateMonth;
		    dp_actionActual_date.expYear = actionActual_dateYear;
		    dp_actionActual_date.pickExpDate(calendarName);
			}
	}
		
	}
		public String CurrentDate() {

				LocalDate localDate = LocalDate.now();
				String CurrentDateWithMonth = DateTimeFormatter.ofPattern("MMMM-dd-yyyy").format(localDate);
				return CurrentDateWithMonth;
	    }
	    
	    public String CurrentMonth() {
	        String[] monthName = {"January", "February",
	                "March", "April", "May", "June", "July",
	                "August", "September", "October", "November",
	                "December"};

	        Calendar cal = Calendar.getInstance();
	        String month = monthName[cal.get(Calendar.MONTH)];

	        return month;
	    }
}
