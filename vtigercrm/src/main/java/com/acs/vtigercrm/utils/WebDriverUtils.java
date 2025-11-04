package com.acs.vtigercrm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;
//import com.google.common.io.Files;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import java.nio.file.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtils {
	   protected WebDriver driver;
	    protected ExtentTest extent;
	    
	    public WebDriverUtils(WebDriver driver, ExtentTest extTest) {
	        this.driver = driver;
	        this.extent = extTest;
	    }

	////////////////////////////////  driver variable and getter method ///////////////////////////////////////////

	public WebDriver getDriver() {
		return driver;
	}
	////////////////////////////////  variable and getter method for extent report ///////////////////////////

	public ExtentTest getExtTest() {
		return extent;
	}

	////////////////////////////////  variable of method getPropertyValue //////////////////////////////////

	private Properties prop;
	
/*//	public void log(Status status, String message) {// in Future I have to implement this 
//	    extTest.log(status, message);
//	}
*/
	//////////////////////////////// method getPropertyValue To raed value from config.properties file ////////////

	public String getPropertyValue(String propertyName) {
	    if (prop == null) {
	        prop = new Properties();
	        try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "/config.properties")) {
	            prop.load(input);
	            extent.log(Status.INFO, "✅ Config file loaded successfully.");
	        } catch (FileNotFoundException e) {
	            extent.log(Status.FAIL, "❌ Config file not found: " + e.getMessage());
	        } catch (IOException e) {
	            extent.log(Status.FAIL, "⚠️ Error loading config file: " + e.getMessage());
	        }
	    }

	    String value = prop.getProperty(propertyName);
	    if (value == null || value.trim().isEmpty()) {
	        extent.log(Status.WARNING, "⚠️ Property '" + propertyName + "' not found or empty in config.properties.");
	    }
	    return value;
	}


	//////////////////////////////   launchBrowser method /////////////////////////////////////////////////

	/*
	 * this method will launch browser 
	 * parameters - String browserName 
	 * return type -driver 
	 * 
	 * String browser = getPropertyValue("browser");
       driver = launchBrowser(browser);
via TestNG XML:
     <parameter name="browser" value="firefox"/>

	 * Author - Atul Yadav 
	 * Modified Date - 23-10-2025
	 * Modified By - Maestromate
	 */
	public WebDriver launchBrowser(String BrowserName ) {
		driver = null;
		if (BrowserName.equalsIgnoreCase("chrome")) {
			ChromeOptions co = new ChromeOptions();
			/*
			 * co.addArguments("--headless=new");/////// for headless mode
			 * 
			 * 
			 * Map<String, Object> chromeprefs = new HashMap<>();
			 * chromeprefs.put("profile.default_content_settings.popups", 0); String
			 * downloadPath = System.getProperty("user.dir");
			 * chromeprefs.put("download.default_directory", downloadPath);
			 * chromeprefs.put("download.prompt_for_download", false);
			 * co.setExperimentalOption("prefs", chromeprefs);  /////(all line for download file by bypassing os box )
			 */
			// 3. Register authentication credentials
			/*
			 * ((HasAuthentication) driver).register( UsernameAndPassword.of("admin",
			 * "admin123") );
			 */

			driver = new ChromeDriver(co);
			extent.log(Status.INFO, BrowserName +" Browser Launched successfully");
		} else if (BrowserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions fo = new FirefoxOptions();
	        fo.addArguments("--headless=new");
			driver = new FirefoxDriver(fo);
			extent.log(Status.INFO, BrowserName +" Browser Launched successfully");

		} else if (BrowserName.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			extent.log(Status.INFO, BrowserName +" Browser Launched successfully");
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		return driver;
	}



	//////////////////////////////   openUrl method ///////////////////////////////////////////////////////

	/*
	 * this method will hit the URL 
	 * parameters - String URL 
	 * return type - void
	 * Author - Atul Yadav 
	 * Modified Date - 
	 * Modified By - Atul Yadav
	 */
	public void hitUrl(String url) {
	    try {
	        if (url == null || url.isEmpty()) {
	            throw new IllegalArgumentException("URL cannot be null or empty");
	        }

	        driver.get(url);
	        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

	        // Log with ExtentReport
	        extent.log(Status.INFO, "Navigated to URL: " + url);

	        // Optional: small wait for stability
	        Thread.sleep(2000);

	    } catch (IllegalArgumentException e) {
	        extent.log(Status.FAIL, "Invalid URL provided: " + e.getMessage());
	        e.printStackTrace();
	        throw e;

	    } catch (Exception e) {
	        takeSnapshot("OpenUrlError");
	        extent.log(Status.FAIL, "Failed to open URL: " + url + " | Exception: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("Unable to open URL: " + url, e);
	    }
	}

	//////////////////////////////   searchElement method -findWebElement///////////////////////////////////////////////////

	/*
	 * this method will search an element 
	 * parameters - String xpath 
	 * return type - WebELement Object 
	 * Author - Atul Yadav 
	 * Modified Date - 23-10-25
	 * Modified By - Maestromate
	 * Yadav
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
	                    extent.log(Status.INFO, "Element found successfully: " + xpath);
	                    return element;
	                }
	            } catch (NoSuchElementException | TimeoutException e) {
	                retryCount++;
	                extent.log(Status.WARNING, "Retry " + retryCount + "/" + maxRetries + 
	                             ": Element not found yet. Waiting...");
	                staticSleepWait(2);
	            }
	        }

	        if (element == null) {
	            throw new NoSuchElementException("Unable to locate element after retries: " + xpath);
	        }

	    } catch (InvalidSelectorException e) {
	        extent.log(Status.FAIL, "❌ Invalid XPath selector: " + xpath);
	        e.printStackTrace();
	        throw e;

	    } catch (Exception e) {
	        takeSnapshot("FindElementError_" + System.currentTimeMillis());
	        extent.log(Status.FAIL, "❌ Failed to find element: " + xpath + " | Exception: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("Error while finding element: " + xpath, e);
	    }

	    return element;
	}
	
	public boolean checkElement(WebElement we,String elementName) {
		boolean status=false;
		try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	        wait.until(ExpectedConditions.visibilityOf(we));
	        
	        if(we.isDisplayed()) {		
            extent.log(Status.PASS, "✅ Element [" + elementName + "] is visible on the page.");

			if(we.isEnabled()) {
				status=true;
                extent.log(Status.PASS, "✅ Element [" + elementName + "] is enabled and ready for interaction.");
			}else {
                extent.log(Status.WARNING, "⚠️ Element [" + elementName + "] is visible but disabled (not clickable).");
                takeSnapshot("DisabledElement_" + elementName);
			}
		}else {
            extent.log(Status.FAIL, "❌ Element [" + elementName + "] is not visible.");
            takeSnapshot("ElementNotVisible_" + elementName);

		}
		}catch (TimeoutException e) {
	        extent.log(Status.FAIL, "❌ Timeout waiting for element [" + elementName + "] to be visible.");
	        takeSnapshot("ElementTimeout_" + elementName);
	    } catch (NoSuchElementException e) {
	        extent.log(Status.FAIL, "❌ Element [" + elementName + "] not found in DOM.");
	        takeSnapshot("ElementNotFound_" + elementName);
	    } catch (Exception e) {
	        extent.log(Status.FAIL, "⚠️ Unexpected error while checking element [" + elementName + "]: " + e.getMessage());
	        takeSnapshot("ElementCheckError_" + elementName);
	    }
		return status;
	}

	//////////////////////////////   getAllElementsText method ///////////////////////////////////////////////////

	/*
	 * this method will return innertext of multiple element parameters - string
	 * xpath return type - elementsTextList Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	
	//////////////////////////////   staticWait method ///////////////////////////////////////////////////

	/*
	 * this method will apply static wait parameters - int timeInSeconds return type
	 * - void Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void staticSleepWait(int timeInSeconds) {
	    if (timeInSeconds <= 0) {
	        extent.log(Status.WARNING, "staticSleepWait called with invalid time: " + timeInSeconds);
	        return;
	    }

	    try {
	        Thread.sleep(timeInSeconds * 1000L);
	        extent.log(Status.INFO, "Waited for " + timeInSeconds + " seconds (static sleep).");
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Best practice: restore interrupt status
	        extent.log(Status.WARNING, "Sleep was interrupted: " + e.getMessage());
	    }
	}

	//////////////////////////////   type any value in text box method ///////////////////////////////////////////////////

	/*
	 * this method will type any value in text box parameters - WebElement we,
	 * String value, String elementName return type - void Author - Atul Yadav
	 * Modified Date - Modified By - Maestromate
	 */
	public void sendKeysText(WebElement element, String value, String elementName) {
	    try {
	        if (value == null || value.trim().isEmpty()) {
	            extent.log(Status.WARNING, "Skipped entering empty/null value into " + elementName);
	            return;
	        }

	        boolean isPresent = checkElement(element, elementName);
	        if (!isPresent) {
	            extent.log(Status.FAIL, elementName + " not present or not visible");
	            takeSnapshot("ElementNotFound_" + elementName);
	            return;
	        }

	        element.sendKeys(value.trim());
	        extent.log(Status.INFO, String.format("Entered '%s' into %s text box successfully", value, elementName));

	    } catch (ElementNotInteractableException e) {
	        jsSendKeys(element, value);
	        extent.log(Status.INFO, String.format("Entered '%s' into %s text box successfully using JavaScript", value, elementName));

	    } catch (Exception e) {
	        takeSnapshot("SendKeysError_" + elementName);
	        extent.log(Status.FAIL, "Failed to enter value in " + elementName + " | Exception: " + e.getMessage());
	        throw new RuntimeException("Error entering value in " + elementName, e);
	    }
	}

	//////////////////////////////   clickMultipleElements method ///////////////////////////////////////////////////

	/*
	 * this method will be used for click on multiple element parameters -String
	 * xpath return type - void Author - Atul Yadav Modified Date - Modified By -
	 * Atul Yadav
	 */
	public void clickMultipleElements(String xpath) {
		List<WebElement> list = driver.findElements(By.xpath(xpath));
		for (WebElement we : list) {
			we.click();
			extent.log(Status.INFO, "All element clicked successfully");

		}

	}

	//////////////////////////////   click on single element method ///////////////////////////////////////////////////

	/*
	 * this method will be used for click on single element 
	 * parameters - WebElement we, String elementName
	 * return type - void 
	 * Author - Atul Yadav 
	 * Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void clickButton(WebElement we, String elementName) {
		try {
			boolean status = checkElement(we, elementName);
			if(status==true) {
				we.click();
				//			System.out.println(elementName + " clicked successfully");
				extent.log(Status.INFO," clicked on "+elementName + " button successfully");
			}

		} catch (ElementClickInterceptedException e) {
			jsClick(we);
			extent.log(Status.INFO," clicked on "+elementName + " button successfully");
		} catch (ElementNotInteractableException e) {
			jsClick(we);
			extent.log(Status.INFO," clicked on "+elementName + " button successfully");
		} catch (Exception e) {
			//			e.printStackTrace();
			extent.log(Status.FAIL,e);

			throw e;
		}
	}

	// =====================================( rightClick )============================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used to perform right click action of the mouse parameters -
	 * WebElement we, String elmentName return type - void Author - Maestromate
	 * Modified Date - Modified By - Maestromate
	 */
	public void rightClick(WebElement we, String elmentName) {
		try {

			Actions actob = new Actions(driver);
			actob.contextClick(we).build().perform();
			//			System.out.println(elmentName + " right click performed successfully");
			extent.log(Status.INFO, " right click performed on"+ elmentName+" successfully");

		} catch (ElementNotInteractableException e) {
            e.printStackTrace();

		} catch (StaleElementReferenceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// =====================================( doubleClick )============================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used to perform double click action of the mouse parameters -
	 * WebELement object return type - void Author - Maestromate Modified Date -
	 * Modified By - Maestromate
	 */
	public void doubleClick(WebElement we) {

		Actions act = new Actions(driver);
		act.doubleClick(we).build().perform();
		//		System.out.println("double click performed successfully");
		extent.log(Status.INFO,"double click performed successfully");

	}

	// =====================================( jsClick )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will return innertext of element parameters - WebELement Object
	 * return type - String\ Author - Maestromate Modified Date - Modified By - Maestromate
	 * Yadav
	 */
	public void jsClick(WebElement we) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click();", we);
	}

	// =====================================( jsType )=======================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will type any value in the textbox by using java script
	 * parameters - WebELement Object ,String value return type - void Author - Atul
	 * Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void jsSendKeys(WebElement we, String value) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].value='" + value + "'", we);
	}

	// =====================================( jsScrollByAmount )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for scrolling page through amount by java script
	 * parameters - int x, int y return type - void Author - Atul Yadav Modified
	 * Date - Modified By - Atul Yadav
	 */
	public void jsScrollByAmount(int x, int y) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(" + x + ", " + y + ")");
	}

	// =====================================( jsScrollToBottom )=====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for page scrolling to bottom by java script parameters -
	 * return type - Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void jsScrollToBottom() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.body.scrollHeight");
	}

	// =====================================( jsScrollToElement )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for scrolling page to an element through java script
	 * parameters - WebELement Object 
	 * return type - 
	 * Author - Maestromate	 * Modified Date - 
	 * Modified By - Maestromate
	 */
	public void jsScrollToElement(WebElement we) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", we);
	}

	// =====================================( ScollToElement )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for scroll page to an element 
	 * parameters - WebELement
	 * Object return type - 
	 * Author - Maestromate
	 * Modified Date - 
	 * Modified By - Maestromate
	 */
	public void ScollToElement(WebElement we) {
		Actions act = new Actions(driver);
		act.scrollToElement(we).build().perform();
		//		System.out.println("page is scrolled to element successfully");
		extent.log(Status.INFO,"page is scrolled to element successfully");

	}

	// =====================================( clickByActions )======================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for click By Actions class on an element.
	 * parameters - WebELement
	 * Object return type - 
	 * Author - Maestromate
	 * Modified Date - 
	 * Modified By - Maestromate
	 */
	public void clickByActions(WebElement we) {
		Actions act = new Actions(driver);
		act.click(we).build().perform();
	}

	// =====================================( mouseOver )======================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for mouse over on an element. 
	 * parameters - WebELement
	 * Object return type - 
	 * Author - Atul Yadav 
	 * Modified Date - 
	 * Modified By - AtulYadav
	 */
	public void mouseOver(WebElement we) {
		Actions act = new Actions(driver);
		act.moveToElement(we).build().perform();
	}

	// =====================================( switchToWindowByTitle )==================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for switch to window by title parameters - String
	 * actualPageTitle return type - 
	 * Author - Maestromate
	 * Modified Date - Modified By
	 * - Maestromate
	 */
	public void switchToWindowByTitleAndURL(String ExpectedPageTitle) {

		Set<String> handleValues = driver.getWindowHandles();
		for (String handleValue : handleValues) {
			driver.switchTo().window(handleValue);
				String currentWindowTitle = driver.getTitle();
				if (currentWindowTitle.equalsIgnoreCase(ExpectedPageTitle)) {
					extent.log(Status.INFO,"Focus switched in window - its title -" + ExpectedPageTitle);
					break;
			}
				//				System.out.println("Focus switched in window - its title -" + actualPageTitle);

		}
	}

	// =====================================( switchToWindowByURL )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

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
				//				System.out.println("Focus switched to window - its url -" + pageUrl);
				extent.log(Status.INFO,"Focus switched to window - its url -" + pageUrl);

				break;
			}
		}
	}



	// =====================================( switchToFrame )==================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for switch to frame parameters - WebELement Object return
	 * type - void Author - Maestromate Modified Date - Modified By - Maestromate
	 */
	public void switchToFrame(WebElement weFrame) {
		driver.switchTo().frame(weFrame);
		//		System.out.println("sucessfully Switch to Iframe ");
		extent.log(Status.INFO, "sucessfully Switch to Iframe ");


	}

	// =====================================( switchToFrame By Index )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for switch to frame by using index number parameters -
	 * int index return type - Author - Atul Yadav Modified Date - Modified By -
	 * Atul Yadav
	 */
	public void switchToFrame(int index) {
		driver.switchTo().frame(index);
		//		System.out.println("sucessfully Switch to Iframe By -" + index);
		extent.log(Status.INFO, "sucessfully Switch to Iframe By -" + index);

	}

	// =====================================( selectDropdownByVisibleText )==================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for select Dropdown By VisibleText parameters -
	 * WebELement Object , String visibleTextName return type - void Author - Maestromate
	 * Modified Date - Modified By - Maestromate
	 */
	public void selectDropdownByVisibleText(WebElement we, String visibleTextName) {
		try {
			Select select = new Select(we);
			select.selectByVisibleText(visibleTextName);
			//			System.out.println("Dropdown is selected by -" + visibleTextName);
			extent.log(Status.INFO,"Dropdown is selected by -" + visibleTextName);

		} catch (Exception e) {
			extent.log(Status.FAIL, e);

		}
	}

	// =====================================( selectDropdownByIndex )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used for select Drop down By Index parameters - WebELement
	 * Object , int indexNumber return type - Author - Maestromate Modified Date -
	 * Modified By - Maestromate
	 */
	public void selectDropdownByIndex(WebElement we, int indexNumber) {
		try {
			Select select = new Select(we);
			select.selectByIndex(indexNumber);
			//			System.out.println("Dropdown is selected by index no. -" + indexNumber);
			extent.log(Status.INFO,"Dropdown is selected by index no. -" + indexNumber);


		} catch (Exception e) {
			extent.log(Status.FAIL, e);

		}
	}

	// =====================================( fullScreenshot )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

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
	
	// NEW =====================================( Best Practice (Reusable Utility Style) )===================================
		//////////////////////////////   screenshot method ///////////////////////////////////////////////////
	/*
	 * Pass only name (auto path) Cleanest, consistent, reusable Less manual control
	 * Enterprise frameworks / long-term projects
	 */
	public void takeSnapshot(String imageName) {
		String baseDir = System.getProperty("user.dir"+"/Screenshots/");
		try {
		TakesScreenshot tss= (TakesScreenshot)driver;
		File sourceFile = tss.getScreenshotAs(OutputType.FILE);
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		Path destinationPath =Paths.get(baseDir,imageName+"_"+timeStamp+".png");
		
		// createDirectories() is from java.nio.file.Files
			Files.createDirectories(destinationPath.getParent());
			Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
	        extent.log(Status.INFO, "Screenshot saved: " + destinationPath.toString());

		}catch(IOException e) {
			e.printStackTrace();
	        extent.log(Status.FAIL, "Screenshot failed: " + e.getMessage());
		}
		
	}

	// =====================================( fullScreenshot )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will capture screen shot of full page parameters - String
	 * location,String imageNamet return type - Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public String screenshotFullPage() {
		String imageCode=null;


		try {
			TakesScreenshot tss = (TakesScreenshot) driver;
			imageCode = tss.getScreenshotAs(OutputType.BASE64);

			extent.log(Status.INFO, "full screenshot captured of page as BASE64");

		} catch (Exception e) {
			e.printStackTrace();
			extent.log(Status.FAIL, e);
		}
		return imageCode;

	}


	// =====================================( specificScreenshot )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will capture screenshot of an specific element 
	 * parameters -WebELement Object 
	 * return type - String
	 * Author - Atul Yadav 
	 * Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void  screenshotSpecilArea(String xpath, String location, String imageName) {

		try {
			File sourceFile = driver.findElement(By.xpath(xpath)).getScreenshotAs(OutputType.FILE);
			;
			File destination = new File(location + "\\" + imageName + "Image.png");
			//Files.copy(sourceFile, destination);
			//			System.out.println("specific screenshot captured succesfully by name -" + imageName);
			extent.log(Status.INFO, "specific screenshot captured succesfully by name -" + imageName);

		} catch (Exception e) {

			//			e.getStackTrace();
			extent.log(Status.FAIL, e);

		}

	}

	// =====================================( getInnerText )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will return innertext of element parameters - WebELement Object
	 * return type - String Author - Atul Yadav Modified Date - Modified By - Atul
	 * Yadav
	 */
	public String getInnerText(WebElement we, String elementName) {
		String innerText="";
		try {
		innerText = we.getText();
		}catch(Exception e) {
			e.printStackTrace();
			takeSnapshot(elementName);
			extent.log(Status.INFO, "Unable to fetch "+elementName+" text");
		}
		return innerText;
	}

	// =====================================( getAttribute )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will return value of any attribute of element parameters -
	 * WebELement Object,String attributeName return type - String Author - Atul
	 * Yadav Modified Date - Modified By - Atul Yadav
	 */
	public String getAttribute(WebElement we, String attributeName,String elementName) {
		String attrValue="";
		try {
			attrValue = we.getAttribute(attributeName);
		}catch(Exception e) {
			e.printStackTrace();
			takeSnapshot(elementName);
			extent.log(Status.INFO, "Unable to fetch "+elementName+" text");
		}
		return attrValue;
	}

	// =====================================( getPageTitle )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will return page title of current page parameters - return type -
	 * String Author - Atul Yadav Modified Date - Modified By - Atul Yadav
	 */
	public String getPageTitle(String pageName) {
		String title="";
		try {
			title = driver.getTitle().trim();
			extent.log(Status.INFO, "Page title for [" + pageName + "] is: " + title);
            return title;
		}catch(Exception e) {
			takeSnapshot("PageTitleError_" + pageName);
			e.printStackTrace();
			extent.log(Status.FAIL, "Failed to fetch page title for [" + pageName + "]: " + e.getMessage());
			return title;
		}
	}

	// =====================================( getURL )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will return url of current page parameters - return type - String
	 * Author - Maestromate Modified Date - Modified By - Maestromate
	 */
	public String getURL(String pageName) {
		String currentUrl="";
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	        wait.until(d -> !driver.getCurrentUrl().isEmpty());
			currentUrl = driver.getCurrentUrl().trim();
	        extent.log(Status.PASS, "Fetched URL for [" + pageName + "]: " + currentUrl);

		}catch(Exception e) {
			takeSnapshot(pageName);
	        extent.log(Status.FAIL, "❌ Unable to fetch URL for [" + pageName + "]. Error: " + e.getMessage());
		}
		return currentUrl;
	}

	// =====================================( maximizeWindow )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method is used to maximize window parameters - return type - Author -
	 * Maestromate Modified Date - Modified By - Maestromate
	 */
	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	// =====================================( closeAllWindow )==================================

	/*
	 * this method will close all window parameters - return type - Author - Atul
	 * Maestromate Modified Date - Modified By - Maestromate
	 */
	public void quitBrowser() {
		driver.quit();
	}

	// =====================================( closeCurrentWindow )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will close current window only parameters - return type - Author
	 * - Maestromate Modified Date - Modified By - Maestromate
	 */
	public void closeCurrentWindow() {
		driver.close();
	}

	// =====================================( setSize )===================================

	/*
	 * this method will set size of an element parameters - int width, int height
	 * return type - Author - Maestromate Modified Date - Modified By - Maestromate 
	 */
	public void setSize(int width, int height) {
		Dimension dim = new Dimension(width, height);
		driver.manage().window().setSize(dim);
		;
	}

	// =====================================( waitForVisibility )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will wait for visibility of an element parameters - WebELement
	 * Object,int timeoutInSecond return type - Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void waitForVisibility(WebElement we, int timeoutInSecond) {
		WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecond));
		wt.until(ExpectedConditions.visibilityOf((we)));
	}

	// =====================================( waitForClickable )=================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will wait for clickable of an element parameters - WebELement
	 * Object,int timeoutInSecond return type - Author - Atul Yadav Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void waitForClickable(WebElement we, int timeoutInSecond) {
		WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecond));
		wt.until(ExpectedConditions.elementToBeClickable((we)));
	}

	// =====================================( waitForDisabling )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will wait for disable of an element 
	 * parameters - WebELementObject,int timeoutInSecond 
	 * return type - 
	 * Author - Atul Yadav 
	 * Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void waitForDisabling(WebElement we, int timeoutInSecond) {
		WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecond));
		wt.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable((we))));
	}

	// =====================================( waitForText )==================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will wait for text to be present in element 
	 * parameters -WebElement we, int timeoutInSecond, String text 
	 * return type - 
	 * Author - Maestromate
	 * Modified Date - 
	 * Modified By - Maestromate
	 */
	public void waitForText(WebElement we, int timeoutInSecond, String text) {
		WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecond));
		wt.until(ExpectedConditions.textToBePresentInElement(we, text));
	}

	// =====================================( waitForInvisibility )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will wait for invisibility of element 
	 * parameters - WebELementObject ,int timeoutInSecond 
	 * return type - 
	 * Author - Maestromate
	 * Modified Date -
	 * Modified By - Maestromate
	 */
	public void waitForInvisibility(WebElement we, int timeoutInSecond) {
		WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecond));
		wt.until(ExpectedConditions.not(ExpectedConditions.visibilityOf((we))));
	}

	// =====================================( changePageLoadTimeout )==================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will change the timeout of page loading 
	 * parameters - timeoutInSecond 
	 * return type - 
	 * Author - Maestromate
	 * Modified Date - 
	 * Modified By- Maestromate
	 */
	public void changePageLoadTimeout(int timeoutInSecond) {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeoutInSecond));
	}

	// =====================================( getElementDisplayStatus )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will sure the display status of element 
	 * parameters - WebELement Object 
	 * return type - boolean 
	 * Author - Maestromate 
	 * Modified Date - 
	 * Modified By- Maestromate
	 */
	public boolean getElementDisplayStatus(WebElement we) {
		Dimension dim = we.getSize();
		boolean status = false;
		if (dim.getHeight() > 0 && dim.getWidth() > 0) {
			status = true;
		}
		return status;
	}

	// =====================================( validateInnerText )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method validate the innertext of element 
	 * parameters - WebELement Object,String expectedText 
	 * return type - 
	 * Author - Maestromate
	 * Modified Date -
	 * Modified By - Maestromate
	 */
	public void validateInnerText(WebElement we, String elementName, String expectedText) {
        try {
		String actualText=getInnerText(we, elementName).trim();
		if (actualText.equalsIgnoreCase(expectedText.trim())) {
			extent.log(Status.PASS, elementName + " text validation passed. Actual: [" + actualText + "] Expected: [" + expectedText + "]");

		} else {
			takeSnapshot(elementName);
			extent.log(Status.FAIL, elementName + " text mismatch. Actual: [" + actualText + "] Expected: [" + expectedText + "]");
		}
        }catch (Exception e) {
            extent.log(Status.FAIL, "Unable to validate text for element: " + elementName + " - Exception: " + e.getMessage());
        }
	}

	// =====================================( validateAttribute )====================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method validate the attribute value of element 
	 * parameters - WebELementObject, String expectedAttribute, String attributeName 
	 * return type - 
	 * Author -Atul Yadav 
	 * Modified Date - 
	 * Modified By - Atul Yadav
	 */
	public void validateAttribute(WebElement we,String elementName, String attributeName, String expectedAttribute) {

		try{
			String	actualAttribute = we.getAttribute(attributeName);
        if (actualAttribute == null) {
            extent.log(Status.FAIL, "❌ Attribute '" + attributeName + "' not found for element: " + elementName);
            takeSnapshot("MissingAttribute_" + elementName);
            return;
        }

		actualAttribute = actualAttribute.trim();
        expectedAttribute = expectedAttribute.trim();

        if (actualAttribute.equalsIgnoreCase(expectedAttribute)) {
            extent.log(Status.PASS, "✅ Attribute '" + attributeName + "' of element [" + elementName + 
                "] matched. Actual: [" + actualAttribute + "] Expected: [" + expectedAttribute + "]");
        } else {
            extent.log(Status.FAIL, "❌ Attribute '" + attributeName + "' mismatch for element [" + elementName + 
                "]. Actual: [" + actualAttribute + "] Expected: [" + expectedAttribute + "]");
            takeSnapshot("AttributeValidationFailed_" + elementName);
        }

    } catch (Exception e) {
        extent.log(Status.FAIL, "⚠️ Failed to validate attribute '" + attributeName + "' for element [" + elementName + 
            "] due to exception: " + e.getMessage());
        takeSnapshot("AttributeValidationError_" + elementName);
    }
	}

	// =====================================( validateElementIsinvisible )===================================
	//////////////////////////////   searchElement method ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is visible or not 
	 * parameters -WebELement Object 
	 * return type - 
	 * Author - Maestromate
	 * Modified Date - 
	 * ModifiedBy - Maestromate
	 */
	public void validateElementIsVisible(WebElement we, String elementName) {
        try {
        	boolean isVisible = checkElement(we, elementName);
        	
		    if (isVisible) {
	            extent.log(Status.PASS, "✅ Validation Passed: Element [" + elementName + "] is visible and enabled.");

		}  else {
            extent.log(Status.FAIL, "❌ Validation Failed: Element [" + elementName + "] is not visible or not enabled.");
            takeSnapshot("VisibilityValidationFailed_" + elementName);
        }
	}catch (Exception e) {
        extent.log(Status.FAIL, "⚠️ Error during element visibility validation for [" + elementName + "]: " + e.getMessage());
        takeSnapshot("VisibilityValidationError_" + elementName);
    }
	}
	//////////////////////////////   validateElementIsinvisible method ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is invisible or not 
	 * parameters -WebELement Object 
	 * return type - 
	 * Author - Atul Yadav 
	 * Modified Date - 
	 * Modified By - Atul Yadav
	 */
	public void validateElementIsinvisible(WebElement we, String elementName) {

		 try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		        boolean invisible = wait.until(ExpectedConditions.invisibilityOf(we));

		        if (invisible) {
		            extent.log(Status.PASS, "✅ Element [" + elementName + "] is invisible as expected.");
		        } else {
		            extent.log(Status.FAIL, "❌ Element [" + elementName + "] is still visible when it should be hidden.");
		            takeSnapshot("InvisibilityFailed_" + elementName);
		        }

		    } catch (TimeoutException e) {
		        extent.log(Status.FAIL, "❌ Element [" + elementName + "] did not become invisible within timeout. " + e.getMessage());
		        takeSnapshot("InvisibilityTimeout_" + elementName);
		    } catch (NoSuchElementException e) {
		        // If element is not found in DOM, it’s considered invisible — pass silently.
		        extent.log(Status.PASS, "✅ Element [" + elementName + "] is not present in DOM (considered invisible).");
		    } catch (StaleElementReferenceException e) {
		        // If element goes stale, it usually means it disappeared — also acceptable.
		        extent.log(Status.PASS, "✅ Element [" + elementName + "] became stale (likely removed from DOM, invisible).");
		    } catch (Exception e) {
		        extent.log(Status.FAIL, "⚠️ Error validating invisibility of [" + elementName + "]: " + e.getMessage());
		        takeSnapshot("InvisibilityError_" + elementName);
		    }
	}

	//////////////////////////////   validateElementIsEnabled method ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is enable or not 
	 * parameters -WebELement Object 
	 * return type - String
	 * Author - Maestromate
	 * Modified Date -
	 * Modified By - Maestromate
	 */
	public void validateElementIsEnabled(WebElement we, String elementName) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	        wait.until(ExpectedConditions.elementToBeClickable(we));  // waits until element is enabled/clickable

	        if (we.isEnabled()) {
	            extent.log(Status.PASS, "✅ Element [" + elementName + "] is enabled and ready for interaction.");
	        } else {
	            extent.log(Status.FAIL, "❌ Element [" + elementName + "] is disabled and cannot be interacted with.");
	            takeSnapshot("ElementDisabled_" + elementName);
	        }

	    } catch (TimeoutException e) {
	        extent.log(Status.FAIL, "❌ Element [" + elementName + "] did not become enabled within timeout.");
	        takeSnapshot("ElementEnableTimeout_" + elementName);
	    } catch (StaleElementReferenceException e) {
	        extent.log(Status.FAIL, "⚠️ Element [" + elementName + "] became stale before enablement check. Possibly refreshed DOM.");
	        takeSnapshot("ElementStale_" + elementName);
	    } catch (NoSuchElementException e) {
	        extent.log(Status.FAIL, "❌ Element [" + elementName + "] not found in DOM while checking for enablement.");
	        takeSnapshot("ElementNotFound_" + elementName);
	    } catch (Exception e) {
	        extent.log(Status.FAIL, "⚠️ Unexpected error while validating enablement of [" + elementName + "]: " + e.getMessage());
	        takeSnapshot("EnableCheckError_" + elementName);
	    }
	}
	//////////////////////////////   validateElementIsDisabled method ///////////////////////////////////////////////////

	/*
	 * this method will validate the element is disable or not 
	 * parameters -WebELement Object 
	 * return type - String 
	 * Author - AtulYadav 
	 * Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void validateElementIsDisabled(WebElement we, String elementName) {
	    try {
	        boolean enabled = we.isEnabled();
	        if (!enabled) {
	            extent.log(Status.PASS, "✅ Element [" + elementName + "] is correctly disabled.");
	        } else {
	            extent.log(Status.FAIL, "❌ Element [" + elementName + "] is enabled but expected to be disabled.");
	            takeSnapshot("ElementEnabledUnexpected_" + elementName);
	        }

	    } catch (NoSuchElementException e) {
	        extent.log(Status.FAIL, "❌ Element [" + elementName + "] not found while checking disabled state.");
	        takeSnapshot("ElementDisabledNotFound_" + elementName);
	    } catch (StaleElementReferenceException e) {
	        extent.log(Status.WARNING, "⚠️ Element [" + elementName + "] became stale during disabled validation. It may have been removed from the DOM.");
	    } catch (Exception e) {
	        extent.log(Status.FAIL, "⚠️ Unexpected error validating disabled state for [" + elementName + "]: " + e.getMessage());
	        takeSnapshot("ElementDisabledError_" + elementName);
	    }
	}

	//////////////////////////////  validate the page title ///////////////////////////////////////////////////

	/*
	 * this method will validate the page title with expected page title 
	 * parameters- String expectedTitle 
	 * return type - 
	 * Author - Atul Yadav 
	 * Modified Date -
	 * Modified By - Atul Yadav
	 */
	public void verifyPageTitle(String expectedTitle) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        boolean titleMatched = wait.until(ExpectedConditions.titleIs(expectedTitle));

	        String actualTitle = driver.getTitle();
	        if (titleMatched) {
	            extent.log(Status.PASS, 
	                "✅ Page title is as expected: \"" + actualTitle + "\"");
	        } else {
	            extent.log(Status.FAIL, 
	                "❌ Page title mismatch. Expected: \"" + expectedTitle + "\" but found: \"" + actualTitle + "\"");
	            takeSnapshot("TitleMismatch_" + actualTitle.replaceAll("[^a-zA-Z0-9]", "_"));
	        }

	    } catch (TimeoutException e) {
	        String actualTitle = driver.getTitle();
	        extent.log(Status.FAIL, 
	            "❌ Page title did not match within timeout. Expected: \"" + expectedTitle + "\" but found: \"" + actualTitle + "\"");
	        takeSnapshot("TitleTimeout_" + expectedTitle.replaceAll("[^a-zA-Z0-9]", "_"));
	    } catch (Exception e) {
	        extent.log(Status.FAIL, 
	            "⚠️ Error validating page title: " + e.getMessage());
	        takeSnapshot("TitleValidationError");
	    }
	}

	////////////////////////////// validate Dropdown first Selected Text  ////////////////////////////////////////

	/*
	 * this method will validate Dropdown first Selected Text 
	 * parameters -WebELement Object,String expectedSelectedText return type - Author - Atul
	 * Yadav Modified Date - Modified By - Atul Yadav
	 */
	public void validateDropdownSelectedText(WebElement we, String elementName, String expectedSelectedText) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.visibilityOf(we));

	        Select select = new Select(we);
	        String actualSelectedText = select.getFirstSelectedOption().getText().trim();

	        if (actualSelectedText.equalsIgnoreCase(expectedSelectedText.trim())) {
	            extent.log(Status.PASS,
	                "✅ Dropdown [" + elementName + "] has the correct selected value: \"" + actualSelectedText + "\"");
	        } else {
	            extent.log(Status.FAIL,
	                "❌ Dropdown [" + elementName + "] selected value mismatch. Expected: \"" + expectedSelectedText +
	                "\" but found: \"" + actualSelectedText + "\"");
	            takeSnapshot("DropdownValueMismatch_" + elementName);
	        }

	    } catch (NoSuchElementException e) {
	        extent.log(Status.FAIL,
	            "❌ Dropdown [" + elementName + "] not found or has no selected option.");
	        takeSnapshot("DropdownNotFound_" + elementName);
	    } catch (UnexpectedTagNameException e) {
	        extent.log(Status.FAIL,
	            "⚠️ Element [" + elementName + "] is not a valid <select> dropdown.");
	        takeSnapshot("DropdownInvalid_" + elementName);
	    } catch (TimeoutException e) {
	        extent.log(Status.FAIL,
	            "❌ Timeout waiting for dropdown [" + elementName + "] to be visible.");
	        takeSnapshot("DropdownTimeout_" + elementName);
	    } catch (Exception e) {
	        extent.log(Status.FAIL,
	            "⚠️ Unexpected error validating dropdown [" + elementName + "]: " + e.getMessage());
	        takeSnapshot("DropdownError_" + elementName);
	    }
	}

	////////////////////////////////method for extent report /////////////////////////////////////////////
	
//createExtentTest(extent, "Login Functionality Test", "Verify login with valid credentials", "Regression", "UI");
	public void createExtentTest(ExtentTest ext, String testCaseName, String description, String string) {

if (ext == null) {
throw new IllegalStateException("ExtentReports object is not initialized. Please initialize it before creating a test.");
}

extent = ext.createtcreateTest(testCaseName, description);

// Add categories if any
for (String category : categories) {
extent.assignCategory(category);
}

extent.log(Status.INFO, "🧩 Test case created in report: " + testCaseName);
}

		
	}

}


