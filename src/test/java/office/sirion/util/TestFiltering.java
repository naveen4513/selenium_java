package office.sirion.util;

import java.util.List;

import office.sirion.base.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class TestFiltering extends TestBase {
	public static int getFilterCount() {
		try {
			List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("left-filter"));

			return filtersContent.size();
			} catch (Exception e) {
				return 0;
				}
		}

	public static String getFilterType(int i) {
		try {
			List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("left-filter"));

			return filtersContent.get(i).findElement(By.tagName("div")).getAttribute("class");
			} catch (Exception e) {
				Logger.debug("Filter Element at index -- " + i + " --- might be Auto Complete");
			    return "autoComplete";
			    }
		}

	public static String getFilterName(int i) {
		List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
				.findElement(By.id("filter"))
				.findElement(By.className("filter-content"))
				.findElements(By.className("left-filter"));
		
		try {
			if (getFilterType(i).equalsIgnoreCase("selectExtended")) {
				List<WebElement> e = filtersContent.get(i)
						.findElement(By.tagName("button"))
						.findElements(By.tagName("span"));
				
				return e.get(1).getText();
				}

			else if (getFilterType(i).equalsIgnoreCase("slider") || getFilterType(i).equalsIgnoreCase("daterange") || getFilterType(i).equalsIgnoreCase("stakeholder")) {
				return filtersContent.get(i).findElement(By.tagName("h3")).getText();
				}
			} catch (Exception e) {
				Logger.debug("Filter Name at index -- " + i + " --- might be missing");
				return null;
			}
		return null;
		}
	
	public static int getFilterColumnNumber(int i, String str) {
		List<WebElement> rowHeader = driver
				.findElement(By.className("dataTables_scrollHead"))
				.findElement(By.tagName("tr")).findElements(By.tagName("th"));
		
		try {
			if (str!="autoComplete") {
				for(int m=0; m<rowHeader.size(); m++) {
					new Actions(driver).moveToElement(rowHeader.get(m)).perform();
					if(rowHeader.get(m).getText().equalsIgnoreCase(str)) {
						Logger.debug("Listing Page Column Number for --- " +getFilterName(i)+ " --- is --- " +m);
						return m;
						}
					}
				}
			} catch (Exception e) {
				Logger.debug("Listing Header of Filter Name of index --- " + i + " --- is missing");
				return -1;
				}
		return -1;
		}

	
	public static int getFilterOptionCount(int i) {
		List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
				.findElement(By.id("filter"))
				.findElement(By.className("filter-content"))
				.findElements(By.className("left-filter"));
	
		try {
			if (getFilterType(i).equalsIgnoreCase("selectExtended")) {

				new Actions(driver).moveToElement(filtersContent.get(i).findElement(By.tagName("button"))).perform();
				
				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);
				
				List<WebElement> element = driver.findElement(By.cssSelector("div.ui-multiselect-menu.ui-widget.ui-widget-content.ui-corner-all.dynamicWidth"))
								.findElement(By.cssSelector("ul.ui-multiselect-checkboxes.ui-helper-reset"))
								.findElements(By.tagName("li"));
				
				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);

				return element.size();
				}
			} catch (Exception e) {
				Logger.debug("Filter Options Count is missing for index --- " + i);
				return 0;
				}
		return 0;
		}
	
	public String[] selectFilterOptions(int i, String str) {
		List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
				.findElement(By.id("filter"))
				.findElement(By.className("filter-content"))
				.findElements(By.className("left-filter"));
		
		String[] optionalArray = null;

		try {
			if (getFilterType(i).equalsIgnoreCase("selectExtended")) {
				int optionsCount = getFilterOptionCount(i);
				
				int selectedOptions = 0;
				if (optionsCount > Integer.valueOf(str))
					selectedOptions = Integer.valueOf(str);
				else 
					selectedOptions = optionsCount;
				
				optionalArray = new String[selectedOptions];

				new Actions(driver).moveToElement(filtersContent.get(i).findElement(By.tagName("button"))).perform();

				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);
				
				WebElement element = driver
						.findElement(By.cssSelector("div.ui-multiselect-menu.ui-widget.ui-widget-content.ui-corner-all.dynamicWidth"));

				element.findElement(
						By.cssSelector("div.ui-widget-header.ui-corner-all.ui-multiselect-header.ui-helper-clearfix"))
						.findElement(By.tagName("ul"))
						.findElement(By.className("ui-multiselect-none"))
						.click();
				
				for (int j = 0; j < selectedOptions; j++) {
					List<WebElement> elementList = element.findElement(By.cssSelector("ul.ui-multiselect-checkboxes.ui-helper-reset"))
							.findElements(By.tagName("li"));
					if (!elementList.get(j).findElement(By.tagName("label")).findElement(By.tagName("input")).isSelected())
						elementList.get(j).findElement(By.tagName("label")).findElement(By.tagName("input")).click();
					
					optionalArray[j] = elementList.get(j).findElement(By.tagName("label")).findElement(By.tagName("span")).getText();
					}

				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);
				
				return optionalArray;
				}
			
			else if (getFilterType(i).equalsIgnoreCase("daterange")) {
				List<WebElement> dateFilters = filtersContent.get(i).findElement(By.tagName("h3")).findElements(By.tagName("span"));

				optionalArray = new String[dateFilters.size()];
				for (int z=0; z<dateFilters.size(); z++) {
					dateFilters.get(z).findElement(By.tagName("input")).click();

					String entityDate = null;
					if (z==0)
						entityDate = CONFIG.getProperty("filterDateFrom");
					else if (z==1)
						entityDate = CONFIG.getProperty("filterDateTo");
					
					selectDate(entityDate);
				    optionalArray[z]=(entityDate.toString());
					}
				
				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);
				
				return optionalArray;
				}

			} catch (Exception e) {
				Logger.debug("Filter Options Count is missing for index --- " + i);
				return null;
				}
		
		return null;
		}
	
	public static boolean checkTableDataPresence() throws InterruptedException {
		WebElement listTable = driver.findElement(By.className("dataTables_scrollBody")).findElement(By.tagName("tbody"));
	    List<WebElement> rows = listTable.findElements(By.tagName("tr"));
	    
	    if (rows.size()==1) {
	    	if (rows.get(0).findElement(By.tagName("td")).getAttribute("class").equalsIgnoreCase("dataTables_empty"))
	    		return true;
	    	else
	    		return false;
	    	}
		return false;
		}

	public static String[] getFilterOptions(int i, int j) {
		List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
				.findElement(By.id("filter"))
				.findElement(By.className("filter-content"))
				.findElements(By.className("left-filter"));
		
		try {
			if (getFilterType(i).equalsIgnoreCase("selectExtended")) {
				new Actions(driver).moveToElement(filtersContent.get(i).findElement(By.tagName("button"))).perform();
//				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", filtersContent.get(i).findElement(By.tagName("button")));
				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);
				
				List<WebElement> elementOptionsBox = driver.findElements(By.cssSelector("div.ui-multiselect-menu.ui-widget.ui-widget-content.ui-corner-all.dynamicWidth"));
				
				List<WebElement> elementOptionsList = elementOptionsBox.get(j)
								.findElement(By.cssSelector("ul.ui-multiselect-checkboxes.ui-helper-reset"))
								.findElements(By.tagName("li"));

				String[] iArray = new String[elementOptionsList.size()];
				
				for (int iCount=0; iCount<elementOptionsList.size(); iCount++) {
					String str = elementOptionsList.get(iCount).findElement(By.tagName("label"))
					.findElement(By.tagName("span")).getText();
					
					iArray[iCount] = str;
					}

				filtersContent.get(i).findElement(By.tagName("button")).click();
				Thread.sleep(2000);

				return iArray;
				}
			} catch (Exception e) {
				Logger.debug("Filter Options are missing for index --- " + i);
				return null;
				}
		return null;
		}
	}