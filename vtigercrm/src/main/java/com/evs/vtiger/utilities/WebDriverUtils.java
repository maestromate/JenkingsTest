package com.evs.vtiger.utilities;

import java.io.File;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;
//import com.google.common.io.Files;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import java.nio.file.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtils {

	protected WebDriver driver;
	protected ExtentTest extentTest; // rename variable for clarity

	// Constructor
	public WebDriverUtils(WebDriver driver, ExtentTest extentTest) {
		this.driver = driver;
		this.extentTest = extentTest;
	}

	//////////////////////////////// driver variable and getter method
	//////////////////////////////// ///////////////////////////////////////////
	// Getter for driver
	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	// Getter for extentTest
	public ExtentTest getExtentTest() {
		return extentTest;
	}
	/*
	 * // public void log(Status status, String message) {// in Future I have to
	 * implement this // extTest.log(status, message); // }
	 */
	//////////////////////////////// method getPropertyValue To raed value from
	//////////////////////////////// config.properties file ////////////

	////////////////////////////// launchBrowser method
	////////////////////////////// /////////////////////////////////////////////////

	/*
	 * this method will launch browser parameters - String browserName return type
	 * -driver
	 * 
	 * String browser = getPropertyValue("browser"); driver =
	 * launchBrowser(browser); via TestNG XML: <parameter name="browser"
	 * value="firefox"/>
	 * 
	 * Author - Atul Yadav Modified Date - 23-10-2025 Modified By - Maestromate
	 */
	public WebDriver launchBrowser(String browserName) {
		if (browserName == null || browserName.trim().isEmpty()) {
			throw new RuntimeException("Browser name is not provided!");
		}
		switch (browserName.toLowerCase()) {

		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		default:
			throw new RuntimeException("Invalid browser: " + browserName);
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		extentTest.log(Status.INFO, browserName + " launched successfully");
		return driver;
	}

	////////////////////////////// openUrl method
	////////////////////////////// ///////////////////////////////////////////////////////

	/*
	 * this method will hit the URL parameters - String URL return type - void
	 * Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void hitUrl(String url) {
		try {
			if (url == null || url.isEmpty()) {
				throw new IllegalArgumentException("URL cannot be null or empty");
			}

			driver.get(url);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

			// Log with ExtentReport
			extentTest.log(Status.INFO, "Navigated to URL: " + url);

			// Optional: small wait for stability
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		} catch (IllegalArgumentException e) {
			extentTest.log(Status.FAIL, "Invalid URL provided: " + e.getMessage());
			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			takeSnapshot("OpenUrlError");
			extentTest.log(Status.FAIL, "Failed to open URL: " + url + " | Exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Unable to open URL: " + url, e);
		}
	}

	////////////////////////////// searchElement method
	////////////////////////////// -findWebElement///////////////////////////////////////////////////

	/*
	 * this method will search an element parameters - String xpath return type -
	 * WebELement Object Author - Atul Yadav Modified Date - 23-10-25 Modified By -
	 * Maestromate Yadav
	 */
	public WebElement findWebElement(String xpath) {
		WebElement element = null;
		int retryCount = 0;
		int maxRetries = 3;

		try {
			if (xpath == null || xpath.trim().isEmpty()) {
				throw new IllegalArgumentException("XPath cannot be null or empty");
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			// Try to find the element with retry logic
			while (retryCount < maxRetries) {
				try {
					element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
					if (element.isDisplayed()) {
						extentTest.log(Status.INFO, "Element found successfully: " + xpath);
						return element;
					}
				} catch (NoSuchElementException | TimeoutException e) {
					retryCount++;
					extentTest.log(Status.WARNING,
							"Retry " + retryCount + "/" + maxRetries + ": Element not found yet. Waiting...");
					staticSleepWait(2);
				}
			}

			if (element == null) {
				throw new NoSuchElementException("Unable to locate element after retries: " + xpath);
			}

		} catch (InvalidSelectorException e) {
			extentTest.log(Status.FAIL, "❌ Invalid XPath selector: " + xpath);
			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			takeSnapshot("FindElementError_" + System.currentTimeMillis());
			extentTest.log(Status.FAIL, "❌ Failed to find element: " + xpath + " | Exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error while finding element: " + xpath, e);
		}

		return element;
	}

	public boolean checkElement(WebElement element, String name) {
		try {
			new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(element));

			if (element.isDisplayed() && element.isEnabled()) {
				extentTest.log(Status.INFO, name + " is visible & enabled");
				return true;
			}

			takeSnapshot("Check_" + name);
			extentTest.log(Status.FAIL, name + " is NOT visible/enabled");
			return false;

		} catch (Exception e) {
			takeSnapshot("CheckError_" + name);
			extentTest.log(Status.FAIL, "Error checking " + name + ": " + e.getMessage());
			return false;
		}
	}

	////////////////////////////// getAllElementsText method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will return innertext of multiple element parameters - string
	 * xpath return type - elementsTextList Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */

	////////////////////////////// staticWait method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will apply static wait parameters - int timeInSeconds return type
	 * - void Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void staticSleepWait(int timeInSeconds) {
		if (timeInSeconds <= 0) {
			extentTest.log(Status.WARNING, "staticSleepWait called with invalid time: " + timeInSeconds);
			return;
		}

		try {
			Thread.sleep(timeInSeconds * 1000L);
			extentTest.log(Status.INFO, "Waited for " + timeInSeconds + " seconds (static sleep).");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Best practice: restore interrupt status
			extentTest.log(Status.WARNING, "Sleep was interrupted: " + e.getMessage());
		}
	}

	////////////////////////////// type any value in text box method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will type any value in text box parameters - WebElement we,
	 * String value, String elementName return type - void Author - Atul Yadav
	 * Modified Date - Modified By - Maestromate
	 */
	public void sendKeysText(WebElement element, String value, String elementName) {
		try {
			if (value == null || value.trim().isEmpty())
				return;

			if (!checkElement(element, elementName))
				return;

			element.sendKeys(value);
			extentTest.info("Entered value in: " + elementName);

		} catch (Exception e) {
			jsSendKeys(element, value);
			extentTest.warning("JS sendKeys used for " + elementName);
		}
	}

	////////////////////////////// clickMultipleElements method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will be used for click on multiple element parameters -String
	 * xpath return type - void Author - Atul Yadav Modified Date - Modified By -
	 * Atul Yadav
	 */
	public void clickMultipleElements(String xpath) {
		List<WebElement> list = driver.findElements(By.xpath(xpath));
		for (WebElement we : list) {
			we.click();
			extentTest.log(Status.INFO, "All element clicked successfully");

		}

	}

	////////////////////////////// click on single element method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will be used for click on single element parameters - WebElement
	 * we, String elementName return type - void Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void clickButton(WebElement element, String elementName) {
		try {
			if (checkElement(element, elementName)) {
				element.click();
				extentTest.info("Clicked: " + elementName);
			}
		} catch (Exception e) {
			jsClick(element);
			extentTest.warning("Fallback JS click used for " + elementName);
		}
	}

	// =====================================( rightClick
	// )============================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used to perform right click action of the mouse parameters -
	 * WebElement we, String elmentName return type - void Author - Maestromate
	 * Modified Date - Modified By - Maestromate
	 */
	public void rightClick(WebElement we, String elmentName) {
		try {

			Actions actob = new Actions(driver);
			actob.contextClick(we).build().perform();
			// System.out.println(elmentName + " right click performed successfully");
			extentTest.log(Status.INFO, " right click performed on" + elmentName + " successfully");

		} catch (ElementNotInteractableException e) {
			e.printStackTrace();

		} catch (StaleElementReferenceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// =====================================( doubleClick
	// )============================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used to perform double click action of the mouse parameters -
	 * WebELement object return type - void Author - Maestromate Modified Date -
	 * Modified By - Maestromate
	 */
	public void doubleClick(WebElement we) {

		Actions act = new Actions(driver);
		act.doubleClick(we).build().perform();
		// System.out.println("double click performed successfully");
		extentTest.log(Status.INFO, "double click performed successfully");

	}

	// =====================================( jsClick
	// )====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will return innertext of element parameters - WebELement Object
	 * return type - String\ Author - Maestromate Modified Date - Modified By -
	 * Maestromate Yadav
	 */
	// ---------------- JS HELPERS ----------------

	public void jsClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void jsSendKeys(WebElement element, String value) {
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + value + "';", element);
	}

	// =====================================( jsScrollByAmount
	// )====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for scrolling page through amount by java script
	 * parameters - int x, int y return type - void Author - Atul Yadav Modified
	 * Date - Modified By - Atul Yadav
	 */
	public void jsScrollByAmount(int x, int y) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(" + x + ", " + y + ")");
	}

	// =====================================( jsScrollToBottom
	// )=====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for page scrolling to bottom by java script parameters -
	 * return type - Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void jsScrollToBottom() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.body.scrollHeight");
	}

	// =====================================( jsScrollToElement
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for scrolling page to an element through java script
	 * parameters - WebELement Object return type - Author - Maestromate * Modified
	 * Date - Modified By - Maestromate
	 */
	public void jsScrollToElement(WebElement we) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", we);
	}

	// =====================================( ScollToElement
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for scroll page to an element parameters - WebELement
	 * Object return type - Author - Maestromate Modified Date - Modified By -
	 * Maestromate
	 */
	public void ScollToElement(WebElement we) {
		Actions act = new Actions(driver);
		act.scrollToElement(we).build().perform();
		// System.out.println("page is scrolled to element successfully");
		extentTest.log(Status.INFO, "page is scrolled to element successfully");

	}

	// =====================================( clickByActions
	// )======================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for click By Actions class on an element. parameters -
	 * WebELement Object return type - Author - Maestromate Modified Date - Modified
	 * By - Maestromate
	 */
	public void clickByActions(WebElement we) {
		Actions act = new Actions(driver);
		act.click(we).build().perform();
	}

	// =====================================( mouseOver
	// )======================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for mouse over on an element. parameters - WebELement
	 * Object return type - Author - Atul Yadav Modified Date - Modified By -
	 * AtulYadav
	 */
	public void mouseOver(WebElement we) {
		Actions act = new Actions(driver);
		act.moveToElement(we).build().perform();
	}

	// =====================================( switchToWindowByTitle
	// )==================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for switch to window by title parameters - String
	 * actualPageTitle return type - Author - Maestromate Modified Date - Modified
	 * By - Maestromate
	 */
	public void switchToWindowByTitleAndURL(String ExpectedPageTitle) {

		Set<String> handleValues = driver.getWindowHandles();
		for (String handleValue : handleValues) {
			driver.switchTo().window(handleValue);
			String currentWindowTitle = driver.getTitle();
			if (currentWindowTitle.equalsIgnoreCase(ExpectedPageTitle)) {
				extentTest.log(Status.INFO, "Focus switched in window - its title -" + ExpectedPageTitle);
				break;
			}
			// System.out.println("Focus switched in window - its title -" +
			// actualPageTitle);

		}
	}

	// =====================================( switchToWindowByURL
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for switch To Window By URL parameters - String pageUrl
	 * return type - Author - Maestromate Modified Date - Modified By - Maestromate
	 */
	public void switchToWindowByURL(String pageUrl) {

		Set<String> handleValues = driver.getWindowHandles();
		for (String handleValue : handleValues) {
			driver.switchTo().window(handleValue);
			String currentWindowUrl = driver.getCurrentUrl();
			if (currentWindowUrl.equalsIgnoreCase(pageUrl)) {
				// System.out.println("Focus switched to window - its url -" + pageUrl);
				extentTest.log(Status.INFO, "Focus switched to window - its url -" + pageUrl);

				break;
			}
		}
	}

	// =====================================( switchToFrame
	// )==================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for switch to frame parameters - WebELement Object return
	 * type - void Author - Maestromate Modified Date - Modified By - Maestromate
	 */
	public void switchToFrame(WebElement weFrame) {
		driver.switchTo().frame(weFrame);
		// System.out.println("sucessfully Switch to Iframe ");
		extentTest.log(Status.INFO, "sucessfully Switch to Iframe ");

	}

	// =====================================( switchToFrame By Index
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for switch to frame by using index number parameters -
	 * int index return type - Author - Atul Yadav Modified Date - Modified By -
	 * Atul Yadav
	 */
	public void switchToFrame(int index) {
		driver.switchTo().frame(index);
		// System.out.println("sucessfully Switch to Iframe By -" + index);
		extentTest.log(Status.INFO, "sucessfully Switch to Iframe By -" + index);

	}

	// =====================================( selectDropdownByVisibleText
	// )==================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for select Dropdown By VisibleText parameters -
	 * WebELement Object , String visibleTextName return type - void Author -
	 * Maestromate Modified Date - Modified By - Maestromate
	 */
	public void selectDropdownByVisibleText(WebElement we, String visibleTextName) {
		try {
			Select select = new Select(we);
			select.selectByVisibleText(visibleTextName);
			// System.out.println("Dropdown is selected by -" + visibleTextName);
			extentTest.log(Status.INFO, "Dropdown is selected by -" + visibleTextName);

		} catch (Exception e) {
			extentTest.log(Status.FAIL, e);

		}
	}

	// =====================================( selectDropdownByIndex
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used for select Drop down By Index parameters - WebELement
	 * Object , int indexNumber return type - Author - Maestromate Modified Date -
	 * Modified By - Maestromate
	 */
	public void selectDropdownByIndex(WebElement we, int indexNumber) {
		try {
			Select select = new Select(we);
			select.selectByIndex(indexNumber);
			// System.out.println("Dropdown is selected by index no. -" + indexNumber);
			extentTest.log(Status.INFO, "Dropdown is selected by index no. -" + indexNumber);

		} catch (Exception e) {
			extentTest.log(Status.FAIL, e);

		}
	}

	// =====================================( fullScreenshot
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will capture screen shot of full page parameters - String
	 * location,String imageNamet return type - Author - Maestromate Modified Date -
	 * Modified By - Maestromate
	 */
//	public void takeSnapshot(String location, String imageName) {
//		try {
//			TakesScreenshot tss = (TakesScreenshot) driver;
//			File sourceFile = tss.getScreenshotAs(OutputType.FILE);
//			File destination = new File(location + "\\" + imageName + "Image.png");
//			Files.copy(sourceFile, destination);
//			System.out.println("full screenshot captured succesfully by name -" + imageName);
//			//extTest.log(Status.INFO, "full screenshot captured succesfully by name -" + imageName);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			extTest.log(Status.FAIL, e);
//
//		}
//
//	}

	// NEW =====================================( Best Practice (Reusable Utility
	// Style) )===================================
	////////////////////////////// screenshot method
	// ///////////////////////////////////////////////////
	/*
	 * Pass only name (auto path) Cleanest, consistent, reusable Less manual control
	 * Enterprise frameworks / long-term projects
	 */
	public void takeSnapshot(String imageName) {
		try {
			// Create folder path
			String folderPath = System.getProperty("user.dir") + "/Screenshots/";
			Files.createDirectories(Paths.get(folderPath));

			// Create file name with timestamp
			String filePath = folderPath + imageName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
					+ ".png";

			// Capture screenshot
			TakesScreenshot ts = (TakesScreenshot) driver;
			File sourceFile = ts.getScreenshotAs(OutputType.FILE);

			Files.copy(sourceFile.toPath(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

			// Attach in Extent Report
			extentTest.addScreenCaptureFromPath(filePath);
			extentTest.log(Status.INFO, "Screenshot captured: " + imageName);

		} catch (Exception e) {
			extentTest.log(Status.FAIL, "Screenshot failed: " + e.getMessage());
		}
	}

	// =====================================( fullScreenshot
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will capture screen shot of full page parameters - String
	 * location,String imageNamet return type - Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public String screenshotFullPage() {
		String imageCode = null;

		try {
			TakesScreenshot tss = (TakesScreenshot) driver;
			imageCode = tss.getScreenshotAs(OutputType.BASE64);

			extentTest.log(Status.INFO, "full screenshot captured of page as BASE64");

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(Status.FAIL, e);
		}
		return imageCode;

	}

	// =====================================( specificScreenshot
	// )====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will capture screenshot of an specific element parameters
	 * -WebELement Object return type - String Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void screenshotSpecilArea(String xpath, String location, String imageName) {

		try {
			File sourceFile = driver.findElement(By.xpath(xpath)).getScreenshotAs(OutputType.FILE);
			;
			File destination = new File(location + "\\" + imageName + "Image.png");
			// Files.copy(sourceFile, destination);
			// System.out.println("specific screenshot captured succesfully by name -" +
			// imageName);
			extentTest.log(Status.INFO, "specific screenshot captured succesfully by name -" + imageName);

		} catch (Exception e) {

			// e.getStackTrace();
			extentTest.log(Status.FAIL, e);

		}

	}

	// =====================================( getInnerText
	// )====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will return innertext of element parameters - WebELement Object
	 * return type - String Author - Atul Yadav Modified Date - Modified By - Atul
	 * Yadav
	 */
	public String getInnerText(WebElement element, String elementName) {
		try {
			new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(element));

			String text = element.getText().trim();
			extentTest.log(Status.INFO, "Fetched text from [" + elementName + "]: " + text);
			return text;

		} catch (Exception e) {
			takeSnapshot("GetText_" + elementName);
			extentTest.log(Status.FAIL, "Unable to fetch text from [" + elementName + "]");
			return "";
		}
	}

	// =====================================( getAttribute
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will return value of any attribute of element parameters -
	 * WebELement Object,String attributeName return type - String Author - Atul
	 * Yadav Modified Date - Modified By - Atul Yadav
	 */
	public String getAttribute(WebElement we, String attributeName, String elementName) {
		String attrValue = "";
		try {
			attrValue = we.getAttribute(attributeName);
		} catch (Exception e) {
			e.printStackTrace();
			takeSnapshot(elementName);
			extentTest.log(Status.INFO, "Unable to fetch " + elementName + " text");
		}
		return attrValue;
	}

	// =====================================( getPageTitle
	// )====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will return page title of current page parameters - return type -
	 * String Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public String getPageTitle(String pageName) {
		String title = "";
		try {
			title = driver.getTitle().trim();
			extentTest.log(Status.INFO, "Page title for [" + pageName + "] is: " + title);
			return title;
		} catch (Exception e) {
			takeSnapshot("PageTitleError_" + pageName);
			e.printStackTrace();
			extentTest.log(Status.FAIL, "Failed to fetch page title for [" + pageName + "]: " + e.getMessage());
			return title;
		}
	}

	// =====================================( getURL
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will return url of current page parameters - return type - String
	 * Author - Maestromate Modified Date - Modified By - Maestromate
	 */
	public String getURL(String pageName) {
		String currentUrl = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(d -> !driver.getCurrentUrl().isEmpty());
			currentUrl = driver.getCurrentUrl().trim();
			extentTest.log(Status.PASS, "Fetched URL for [" + pageName + "]: " + currentUrl);

		} catch (Exception e) {
			takeSnapshot(pageName);
			extentTest.log(Status.FAIL, "❌ Unable to fetch URL for [" + pageName + "]. Error: " + e.getMessage());
		}
		return currentUrl;
	}

	// =====================================( maximizeWindow
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method is used to maximize window parameters - return type - Author -
	 * Maestromate Modified Date - Modified By - Maestromate
	 */
	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	// =====================================( closeAllWindow
	// )==================================

	/*
	 * this method will close all window parameters - return type - Author - Atul
	 * Maestromate Modified Date - Modified By - Maestromate
	 */
	public void quitBrowser() {
		driver.quit();
	}

	// =====================================( closeCurrentWindow
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will close current window only parameters - return type - Author
	 * - Maestromate Modified Date - Modified By - Maestromate
	 */
	public void closeCurrentWindow() {
		driver.close();
	}

	// =====================================( setSize
	// )===================================

	/*
	 * this method will set size of an element parameters - int width, int height
	 * return type - Author - Maestromate Modified Date - Modified By - Maestromate
	 */
	public void setSize(int width, int height) {
		Dimension dim = new Dimension(width, height);
		driver.manage().window().setSize(dim);
		;
	}

	public void waitForURL(String partialURL, int seconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
		wait.until(ExpectedConditions.urlContains(partialURL));
	}

	// ---------------- WAIT UTILITIES ----------------

	public WebElement waitForVisible(WebElement element, int sec) {
		return new WebDriverWait(driver, Duration.ofSeconds(sec)).until(ExpectedConditions.visibilityOf(element));
	}

	public WebElement waitForClickable(WebElement element, int sec) {
		return new WebDriverWait(driver, Duration.ofSeconds(sec))
				.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitForText(WebElement element, int sec, String text) {
		new WebDriverWait(driver, Duration.ofSeconds(sec))
				.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForInvisibility(WebElement element, int sec) {
		new WebDriverWait(driver, Duration.ofSeconds(sec)).until(ExpectedConditions.invisibilityOf(element));
	}

	// =====================================( changePageLoadTimeout
	// )==================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will change the timeout of page loading parameters -
	 * timeoutInSecond return type - Author - Maestromate Modified Date - Modified
	 * By- Maestromate
	 */
	public void changePageLoadTimeout(int timeoutInSecond) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}

	// =====================================( getElementDisplayStatus
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will sure the display status of element parameters - WebELement
	 * Object return type - boolean Author - Maestromate Modified Date - Modified
	 * By- Maestromate
	 */
	public boolean getElementDisplayStatus(WebElement we) {
		Dimension dim = we.getSize();
		boolean status = false;
		if (dim.getHeight() > 0 && dim.getWidth() > 0) {
			status = true;
		}
		return status;
	}

	// =====================================( validateInnerText
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method validate the innertext of element parameters - WebELement
	 * Object,String expectedText return type - Author - Maestromate Modified Date -
	 * Modified By - Maestromate
	 */
	public void validateInnerText(WebElement we, String elementName, String expectedText) {
		try {
			String actualText = getInnerText(we, elementName).trim();
			if (actualText.equalsIgnoreCase(expectedText.trim())) {
				extentTest.log(Status.PASS, elementName + " text validation passed. Actual: [" + actualText
						+ "] Expected: [" + expectedText + "]");

			} else {
				takeSnapshot(elementName);
				extentTest.log(Status.FAIL,
						elementName + " text mismatch. Actual: [" + actualText + "] Expected: [" + expectedText + "]");
			}
		} catch (Exception e) {
			extentTest.log(Status.FAIL,
					"Unable to validate text for element: " + elementName + " - Exception: " + e.getMessage());
		}
	}

	// =====================================( validateAttribute
	// )====================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method validate the attribute value of element parameters -
	 * WebELementObject, String expectedAttribute, String attributeName return type
	 * - Author -Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void validateAttribute(WebElement we, String elementName, String attributeName, String expectedAttribute) {

		try {
			String actualAttribute = we.getAttribute(attributeName);
			if (actualAttribute == null) {
				extentTest.log(Status.FAIL,
						"❌ Attribute '" + attributeName + "' not found for element: " + elementName);
				takeSnapshot("MissingAttribute_" + elementName);
				return;
			}

			actualAttribute = actualAttribute.trim();
			expectedAttribute = expectedAttribute.trim();

			if (actualAttribute.equalsIgnoreCase(expectedAttribute)) {
				extentTest.log(Status.PASS, "✅ Attribute '" + attributeName + "' of element [" + elementName
						+ "] matched. Actual: [" + actualAttribute + "] Expected: [" + expectedAttribute + "]");
			} else {
				extentTest.log(Status.FAIL, "❌ Attribute '" + attributeName + "' mismatch for element [" + elementName
						+ "]. Actual: [" + actualAttribute + "] Expected: [" + expectedAttribute + "]");
				takeSnapshot("AttributeValidationFailed_" + elementName);
			}

		} catch (Exception e) {
			extentTest.log(Status.FAIL, "⚠️ Failed to validate attribute '" + attributeName + "' for element ["
					+ elementName + "] due to exception: " + e.getMessage());
			takeSnapshot("AttributeValidationError_" + elementName);
		}
	}

	// =====================================( validateElementIsinvisible
	// )===================================
	////////////////////////////// searchElement method
	// ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is visible or not parameters
	 * -WebELement Object return type - Author - Maestromate Modified Date -
	 * ModifiedBy - Maestromate
	 */
	public void validateElementIsVisible(WebElement we, String elementName) {
		try {
			boolean isVisible = checkElement(we, elementName);

			if (isVisible) {
				extentTest.log(Status.PASS,
						"✅ Validation Passed: Element [" + elementName + "] is visible and enabled.");

			} else {
				extentTest.log(Status.FAIL,
						"❌ Validation Failed: Element [" + elementName + "] is not visible or not enabled.");
				takeSnapshot("VisibilityValidationFailed_" + elementName);
			}
		} catch (Exception e) {
			extentTest.log(Status.FAIL,
					"⚠️ Error during element visibility validation for [" + elementName + "]: " + e.getMessage());
			takeSnapshot("VisibilityValidationError_" + elementName);
		}
	}
	////////////////////////////// validateElementIsinvisible method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is invisible or not parameters
	 * -WebELement Object return type - Author - Atul Yadav Modified Date - Modified
	 * By - Atul Yadav
	 */
	public void validateElementIsinvisible(WebElement we, String elementName) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			boolean invisible = wait.until(ExpectedConditions.invisibilityOf(we));

			if (invisible) {
				extentTest.log(Status.PASS, "✅ Element [" + elementName + "] is invisible as expected.");
			} else {
				extentTest.log(Status.FAIL,
						"❌ Element [" + elementName + "] is still visible when it should be hidden.");
				takeSnapshot("InvisibilityFailed_" + elementName);
			}

		} catch (TimeoutException e) {
			extentTest.log(Status.FAIL,
					"❌ Element [" + elementName + "] did not become invisible within timeout. " + e.getMessage());
			takeSnapshot("InvisibilityTimeout_" + elementName);
		} catch (NoSuchElementException e) {
			// If element is not found in DOM, it’s considered invisible — pass silently.
			extentTest.log(Status.PASS,
					"✅ Element [" + elementName + "] is not present in DOM (considered invisible).");
		} catch (StaleElementReferenceException e) {
			// If element goes stale, it usually means it disappeared — also acceptable.
			extentTest.log(Status.PASS,
					"✅ Element [" + elementName + "] became stale (likely removed from DOM, invisible).");
		} catch (Exception e) {
			extentTest.log(Status.FAIL, "⚠️ Error validating invisibility of [" + elementName + "]: " + e.getMessage());
			takeSnapshot("InvisibilityError_" + elementName);
		}
	}

	////////////////////////////// validateElementIsEnabled method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is enable or not parameters -WebELement
	 * Object return type - String Author - Maestromate Modified Date - Modified By
	 * - Maestromate
	 */
	public void validateElementIsEnabled(WebElement we, String elementName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.elementToBeClickable(we)); // waits until element is enabled/clickable

			if (we.isEnabled()) {
				extentTest.log(Status.PASS, "✅ Element [" + elementName + "] is enabled and ready for interaction.");
			} else {
				extentTest.log(Status.FAIL,
						"❌ Element [" + elementName + "] is disabled and cannot be interacted with.");
				takeSnapshot("ElementDisabled_" + elementName);
			}

		} catch (TimeoutException e) {
			extentTest.log(Status.FAIL, "❌ Element [" + elementName + "] did not become enabled within timeout.");
			takeSnapshot("ElementEnableTimeout_" + elementName);
		} catch (StaleElementReferenceException e) {
			extentTest.log(Status.FAIL,
					"⚠️ Element [" + elementName + "] became stale before enablement check. Possibly refreshed DOM.");
			takeSnapshot("ElementStale_" + elementName);
		} catch (NoSuchElementException e) {
			extentTest.log(Status.FAIL,
					"❌ Element [" + elementName + "] not found in DOM while checking for enablement.");
			takeSnapshot("ElementNotFound_" + elementName);
		} catch (Exception e) {
			extentTest.log(Status.FAIL,
					"⚠️ Unexpected error while validating enablement of [" + elementName + "]: " + e.getMessage());
			takeSnapshot("EnableCheckError_" + elementName);
		}
	}
	////////////////////////////// validateElementIsDisabled method
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is disable or not parameters
	 * -WebELement Object return type - String Author - AtulYadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void validateElementIsDisabled(WebElement we, String elementName) {
		try {
			boolean enabled = we.isEnabled();
			if (!enabled) {
				extentTest.log(Status.PASS, "✅ Element [" + elementName + "] is correctly disabled.");
			} else {
				extentTest.log(Status.FAIL, "❌ Element [" + elementName + "] is enabled but expected to be disabled.");
				takeSnapshot("ElementEnabledUnexpected_" + elementName);
			}

		} catch (NoSuchElementException e) {
			extentTest.log(Status.FAIL, "❌ Element [" + elementName + "] not found while checking disabled state.");
			takeSnapshot("ElementDisabledNotFound_" + elementName);
		} catch (StaleElementReferenceException e) {
			extentTest.log(Status.WARNING, "⚠️ Element [" + elementName
					+ "] became stale during disabled validation. It may have been removed from the DOM.");
		} catch (Exception e) {
			extentTest.log(Status.FAIL,
					"⚠️ Unexpected error validating disabled state for [" + elementName + "]: " + e.getMessage());
			takeSnapshot("ElementDisabledError_" + elementName);
		}
	}

	////////////////////////////// validate the page title
	////////////////////////////// ///////////////////////////////////////////////////

	/*
	 * this method will validate the page title with expected page title parameters-
	 * String expectedTitle return type - Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void verifyPageTitle(String expectedTitle) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			boolean titleMatched = wait.until(ExpectedConditions.titleIs(expectedTitle));

			String actualTitle = driver.getTitle();
			if (titleMatched) {
				extentTest.log(Status.PASS, "✅ Page title is as expected: \"" + actualTitle + "\"");
			} else {
				extentTest.log(Status.FAIL, "❌ Page title mismatch. Expected: \"" + expectedTitle + "\" but found: \""
						+ actualTitle + "\"");
				takeSnapshot("TitleMismatch_" + actualTitle.replaceAll("[^a-zA-Z0-9]", "_"));
			}

		} catch (TimeoutException e) {
			String actualTitle = driver.getTitle();
			extentTest.log(Status.FAIL, "❌ Page title did not match within timeout. Expected: \"" + expectedTitle
					+ "\" but found: \"" + actualTitle + "\"");
			takeSnapshot("TitleTimeout_" + expectedTitle.replaceAll("[^a-zA-Z0-9]", "_"));
		} catch (Exception e) {
			extentTest.log(Status.FAIL, "⚠️ Error validating page title: " + e.getMessage());
			takeSnapshot("TitleValidationError");
		}
	}

	////////////////////////////// validate Dropdown first Selected Text
	////////////////////////////// ////////////////////////////////////////

	/*
	 * this method will validate Dropdown first Selected Text parameters -WebELement
	 * Object,String expectedSelectedText return type - Author - Atul Yadav Modified
	 * Date - Modified By - Atul Yadav
	 */
	public void validateDropdownSelectedText(WebElement we, String elementName, String expectedSelectedText) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(we));

			Select select = new Select(we);
			String actualSelectedText = select.getFirstSelectedOption().getText().trim();

			if (actualSelectedText.equalsIgnoreCase(expectedSelectedText.trim())) {
				extentTest.log(Status.PASS, "✅ Dropdown [" + elementName + "] has the correct selected value: \""
						+ actualSelectedText + "\"");
			} else {
				extentTest.log(Status.FAIL, "❌ Dropdown [" + elementName + "] selected value mismatch. Expected: \""
						+ expectedSelectedText + "\" but found: \"" + actualSelectedText + "\"");
				takeSnapshot("DropdownValueMismatch_" + elementName);
			}

		} catch (NoSuchElementException e) {
			extentTest.log(Status.FAIL, "❌ Dropdown [" + elementName + "] not found or has no selected option.");
			takeSnapshot("DropdownNotFound_" + elementName);
		} catch (UnexpectedTagNameException e) {
			extentTest.log(Status.FAIL, "⚠️ Element [" + elementName + "] is not a valid <select> dropdown.");
			takeSnapshot("DropdownInvalid_" + elementName);
		} catch (TimeoutException e) {
			extentTest.log(Status.FAIL, "❌ Timeout waiting for dropdown [" + elementName + "] to be visible.");
			takeSnapshot("DropdownTimeout_" + elementName);
		} catch (Exception e) {
			extentTest.log(Status.FAIL,
					"⚠️ Unexpected error validating dropdown [" + elementName + "]: " + e.getMessage());
			takeSnapshot("DropdownError_" + elementName);
		}
	}

	//////////////////////////////// method for extent report
	//////////////////////////////// /////////////////////////////////////////////

//createExtentTest(extent, "Login Functionality Test", "Verify login with valid credentials", "Regression", "UI");
//	public void createExtentTest(ExtentTest ext, String testCaseName, String description, String string) {
//
//if (ext == null) {
//throw new IllegalStateException("ExtentReports object is not initialized. Please initialize it before creating a test.");
//}
//
//extent = ext.createtcreateTest(testCaseName, description);
//
//// Add categories if any
//for (String category : categories) {
//extent.assignCategory(category);
//}
//
//extent.log(Status.INFO, "🧩 Test case created in report: " + testCaseName);
//}
//
//		
//	}

}
