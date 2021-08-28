var webdriver = require('selenium-webdriver'),
    By = webdriver.By,
    until = webdriver.until;
var driver = new webdriver.Builder()
    .forBrowser('chrome')
    .build();

driver.get('http://www.webdriverjs.com/');
driver.findElement(By.css('.search-field.form-control')).sendKeys("WebdriverJs");
driver.findElement(By.css('button.search-submit i')).click();
driver.wait(until.elementTextIs(driver.findElement(By.css('body.search.search-results.wp-custom-logo:nth-child(2) div.vl-site-content div.vl-container div.content-area div.theiaStickySidebar header.vl-main-header > h1:nth-child(1)')), "Search Results for:"));
//driver.quit();

.vl-main-header>h1
#primary > div > header > h1