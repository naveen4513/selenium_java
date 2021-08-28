package office.sirion.util;

import office.sirion.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommonTestFiltering extends TestBase {

	public static int getFilterCount() {
		try {
		    // Expand Filter Section
		    driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
		    Thread.sleep(2000);

			List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("filters-new"));

		    // Collapse Filter Section
			driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.jx-filterToggle.selected")).click();

		    Thread.sleep(2000);

			return filtersContent.size();
			} catch (Exception e) {
				return 0;
				}
		}

	public static List<String> getFiltername() {
		List<String> names = new ArrayList<>();
		try {
			// Expand Filter Section
			driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
			Thread.sleep(2000);

			List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("filters-new"));

			// Collapse Filter Section
			driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.jx-filterToggle.selected")).click();

			Thread.sleep(2000);

			for(int i =0; i<filtersContent.size();i++) {
				String[] listNames = filtersContent.get(i).getText().split("\n");
				names.add(listNames[0]);
			}
			return names;
		} catch (Exception e) {
			return null;
		}

	}

	public static String getFilterType(int i) {
		try {
			List<WebElement> multiSelectFiltersContent = driver.findElement(By.id("filterDiv"))
					.findElement(By.id("filter"))
					.findElement(By.className("filter-content"))
					.findElements(By.className("filters-new"));

			return multiSelectFiltersContent.get(i).findElement(By.tagName("div")).getAttribute("class");
			}
        catch (Exception e){
            System.out.println("Exception occured for Multi Select Filter");
        }
        try{
            List<WebElement> RangeFiltersContent = driver.findElement(By.id("filterDiv"))
                    .findElement(By.id("filter"))
                    .findElement(By.className("filter-content"))
                    .findElements(By.className("left-filter"));

            return RangeFiltersContent.get(i).findElement(By.tagName("select")).getAttribute("class");
        }
        catch (Exception e){
            System.out.println("Exception occured for Range Filter");
        }
        try {
            List<WebElement> sliderFiltersContent = driver.findElement(By.id("filterDiv"))
                    .findElement(By.id("filter"))
                    .findElement(By.className("filter-content"))
                    .findElements(By.className("left-filter"));

            return sliderFiltersContent.get(i).findElement(By.tagName("div")).getAttribute("class");
        }catch (Exception e){
            System.out.println("Exception occured for Slider Filter");
        }
        return null;
		}

	public static String getFilterName(int i,List<WebElement> filtersContent) {
		try {
			if (getFilterType(i).equalsIgnoreCase("multi-sel ng-scope ng-isolate-scope")) {

				String[] e = filtersContent.get(i).getText().split("\n");
				//List<WebElement> e = filtersContent.get(i).findElements(By.className("label-name"));
				
				return e[0];

				}
			} catch (Exception e) {
            System.out.println("Filter Name at index -- " + i + " --- might be missing");
				return null;
			}
			try{
                List<WebElement> filtersContent1 = driver.findElement(By.id("filterDiv"))
                        .findElement(By.id("filter"))
                        .findElement(By.className("filter-content"))
                        .findElements(By.className("left-filter"));

                if (getFilterType(i).equalsIgnoreCase("daterange sirion2 ng-scope") || getFilterType(i).equalsIgnoreCase("stakeholder")) {
                    return filtersContent1.get(i).findElement(By.tagName("h3")).getText();
                }
            }catch (Exception e){
                System.out.println("Filter Name at index -- " + i + " --- might be missing");
                return null;
            }
		return null;
		}

	public static int getFilterColumnNumber(String str) throws InterruptedException {

		Thread.sleep(5000);
		fluentWaitMethod(driver.findElement(By.className("dataTables_scrollHead")).findElement(By.tagName("tr")).findElement(By.tagName("th")));

		List<WebElement> rowHeader = driver.findElement(By.className("dataTables_scrollHead"))
				.findElement(By.tagName("tr")).findElements(By.tagName("th"));

		try {
			if (str != "autoComplete") {
				for (int m = 0; m < rowHeader.size(); m++) {
					Thread.sleep(1000);
					new Actions(driver).moveToElement(rowHeader.get(m)).perform();
					if (rowHeader.get(m).getText().toLowerCase().equalsIgnoreCase(str)) {
                        System.out.println("Column Name for " + str	+ " -- is Present at Index -- " + m);
						return m;
					}
				}
			}
		} catch (Exception e) {
            System.out.println("There is No such Column Name as -- " + str);
			return -1;
			}

		Logger.debug("There is No such Column Name as -- " + str);
		return -1;
		}
	
	public static int getFilterOptionCount(int filterIndex) {
		List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
				.findElement(By.id("filter"))
				.findElement(By.className("filter-content"))
				.findElements(By.className("filters-new"));
	
		try {
			if (getFilterType(filterIndex).equalsIgnoreCase("multi-sel ng-scope ng-isolate-scope")) {

				new Actions(driver).moveToElement(filtersContent.get(filterIndex).findElement(By.tagName("button"))).perform();
				
				filtersContent.get(filterIndex).findElement(By.tagName("button")).click();
				Thread.sleep(2000);


				
				List<WebElement> element = driver.findElements(By.cssSelector("small.ng-scope.ng-binding"));
				
				List<WebElement> elementOptionsList = new ArrayList<WebElement>();
				for (WebElement elementOptions : element) {
                    System.out.println(elementOptions.getAttribute("class").toString());
					if (elementOptions.getAttribute("class").equalsIgnoreCase("ng-scope ng-binding"))
						elementOptionsList.add(elementOptions);
					}
				
				filtersContent.get(filterIndex).findElement(By.tagName("button")).click();
				Thread.sleep(2000);

				return elementOptionsList.size();
				}
			} catch (Exception e) {
				Logger.debug("Filter Options Count is missing for index --- " + filterIndex);
				return 0;
				}
		return 0;
		}
	
	public static String[] selectFilterOptions(int filterIndex, String filterCount, String filterType) {
		List<WebElement> filtersContent = driver.findElement(By.id("filterDiv"))
				.findElement(By.id("filter"))
				.findElement(By.className("filter-content"))
				.findElements(By.className("filters-new"));
		
		String[] optionalArray = null;

		try {
			if (filterType.equalsIgnoreCase("multi-sel ng-scope ng-isolate-scope")) {
				int optionsCount = getFilterOptionCount(filterIndex);
				
				if (optionsCount==0)
					return null;
				
				int selectedOptions = 0;
				if (optionsCount > Integer.valueOf(filterCount))
					selectedOptions = Integer.valueOf(filterCount);
				else
					selectedOptions = optionsCount;
				
				optionalArray = new String[selectedOptions];

				new Actions(driver).moveToElement(filtersContent.get(filterIndex).findElement(By.tagName("button"))).perform();

				filtersContent.get(filterIndex).findElement(By.tagName("button")).click();
				Thread.sleep(2000);

				/*driver.findElement(By.cssSelector("div.ui-widget-header.ui-corner-all.ui-multiselect-header.ui-helper-clearfix"))
						.findElement(By.tagName("ul"))
						.findElement(By.className("ui-multiselect-close"))
						.click();*/

                List<WebElement> element = driver.findElements(By.xpath("//a/div/label/span"));

                List<WebElement> elementOptionsList = new ArrayList<WebElement>();
                for (WebElement elementOptions : element) {
                    System.out.println(elementOptions.getTagName().toString());
                    if (elementOptions.getTagName().equalsIgnoreCase("span"))

                        elementOptionsList.add(elementOptions);
                }

				for (int filterOptions = 0; filterOptions < selectedOptions; filterOptions++) {
					if (!elementOptionsList.get(filterOptions).isSelected())
						elementOptionsList.get(filterOptions).click();

					optionalArray[filterOptions] = elementOptionsList.get(filterOptions).getText();
					}

				filtersContent.get(filterIndex).findElement(By.tagName("button")).click();
				Thread.sleep(2000);
				
				return optionalArray;
				}
			
			else if (filterType.equalsIgnoreCase("daterange sirion2 ng-scope")) {
				new Actions(driver).moveToElement(filtersContent.get(filterIndex).findElement(By.tagName("h3"))).perform();

				List<WebElement> dateFilters = filtersContent.get(filterIndex).findElement(By.name("dueDate")).findElements(By.tagName("span"));

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

				return optionalArray;
				}
		} catch (Exception e) {
			Logger.debug("Filter Options Count is missing for index --- "	+ filterIndex);
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

	// Check For Date Validation
	public static boolean checkDateValidator(String actualDate, String startDate, String endDate) {
		SimpleDateFormat sdfSelection = new SimpleDateFormat("MMMM-dd-yyyy");
		SimpleDateFormat sdfActual = new SimpleDateFormat(CONFIG.getProperty("entityDateFormat"));
		
		try {
			Date actualColumnDate = sdfActual.parse(actualDate);
			Date selectedStartDate = sdfSelection.parse(startDate);
			Date selectedEndDate = sdfSelection.parse(endDate);
			
			if (actualColumnDate.compareTo(selectedStartDate)>=0 && actualColumnDate.compareTo(selectedEndDate)<=0)
				return true;
			else
				return false;
			} catch (ParseException p) {
					return false;				
			}
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