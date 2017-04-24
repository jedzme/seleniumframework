package common;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.OperaDriverManager;

public abstract class BaseTest {

	final String defaultChromeDriverPath = "resources\\drivers\\chromedriver_2.29.exe";
	final String defaultIEEdgeDriverPath = "resources\\drivers\\MicrosoftWebDriver_3.14393.exe";
	protected WebDriver driver;
	protected TestDataProvider testDataProvider;
	String screenShotsPath;
	String testCaseName;

	public void startTest(String browser, String screenShotsPath, TestDataProvider testDataProvider) {

		this.testDataProvider = testDataProvider;
		this.screenShotsPath = screenShotsPath;
		this.testCaseName = this.getClass().getSimpleName();

		log("");
		log(">>>>> Executing " + testCaseName + " <<<<<");
		log("");

		if (browser.equalsIgnoreCase("chrome")) {
			Logger.log("Running in Google Chrome");
			System.setProperty("webdriver.chrome.driver", defaultChromeDriverPath);
			driver = new ChromeDriver();
			//ChromeDriverManager.getInstace().setup();
			driver = new ChromeDriver();
		}
		
		else if(browser.equalsIgnoreCase("firefox")){
			Logger.log("Running in Firefox");
			FirefoxDriverManager.getInstance().setup();
			driver = new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("edge")){
			Logger.log("Running in Internet Explorer Edge");
			System.setProperty("webdriver.edge.driver", defaultIEEdgeDriverPath);
			/*EdgeDriverManager.getInstance().setup();
			System.setProperty("wdm.override", "true");
			System.setProperty("wdm.edgeVersion", "3.14393");*/
			driver = new EdgeDriver();
		}
		else if(browser.equalsIgnoreCase("ie")){
			Logger.log("Running in Internet Explorer");
			InternetExplorerDriverManager.getInstance().setup();
			driver = new InternetExplorerDriver();
		}
		else if(browser.equalsIgnoreCase("opera")){
			Logger.log("Running in Opera");
			OperaDriverManager.getInstance().setup();
			driver = new OperaDriver();
		}
		//TODO: add Safari
		
		//Default timeout setting for our drivers - 10secs
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();

	}

	public String getTestCaseName() {
		return this.testCaseName;
	}

	@AfterMethod
	public void endTest() throws IOException {
		String TestCaseName = this.getClass().getSimpleName();
		log("");
		log(">>>>> Terminating " + TestCaseName + " <<<<<");
		log("");

		if (testDataProvider != null) {
			log("Closing Test Data Provider...");
			testDataProvider.close();
		}
		if (driver != null) {
			// driver.close();
			log("Closing Driver...");
			driver.quit();
		}

	}

	public static void log(String message) {
		Logger.log(message);
		Reporter.log(message);
	}

	public void takescreenshot(String screenShotName) throws IOException {
		File sourceFile;
		try {
			sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			/*
			 * FileUtils.copyFile(scrFile, new File(
			 * "C:\\Workspace\\SeleniumProject\\test-reports\\screenshots\\" +
			 * getFileName(this.getClass().getSimpleName())));
			 */
			FileUtils.copyFile(sourceFile, new File(screenShotsPath + screenShotName + ".png"));

		} catch (Exception e) {
			log("Screenshot is not created.");
			e.printStackTrace();
		}
	}

	/*
	 * private String getFileName(String nameTest) throws IOException {
	 * DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss"); Date
	 * date = new Date(); return dateFormat.format(date) + "_" + nameTest +
	 * ".png"; }
	 */

	public void assertTextPresentInElement(String locator, LocatorType locType, String valueToCheck) {

		if (locType.equals(LocatorType.ID)) {

			Assert.assertTrue(driver.findElement(By.id(locator)).getText().equals(valueToCheck));
			log("The text " + valueToCheck + " is present in the web element.");

		} else if (locType.equals(LocatorType.NAME)) {

			Assert.assertTrue(driver.findElement(By.name(locator)).getText().equals(valueToCheck));
			log("The text " + valueToCheck + " is present in the web element.");

		} else if (locType.equals(LocatorType.CLASS)) {

			Assert.assertTrue(driver.findElement(By.className(locator)).getText().equals(valueToCheck));
			log("The text " + valueToCheck + " is present in the web element.");

		} else {

			Assert.assertTrue(driver.findElement(By.xpath(locator)).getText().equals(valueToCheck));
			log("The text " + valueToCheck + " is present in the web element.");
		}
	}

	public void assertElementPresentInPage(String locator, LocatorType locType) {

		Assert.assertTrue(isElementPresent(locator, locType));
		log("Element is present in the page.");

	}

	public Boolean isElementPresent(String locator, LocatorType locType) {
		boolean found = true;
		try {
			if (locType.equals(LocatorType.ID)) {

				driver.findElement(By.id(locator));

			} else if (locType.equals(LocatorType.NAME)) {

				driver.findElement(By.name(locator));

			} else if (locType.equals(LocatorType.CLASS)) {

				driver.findElement(By.className(locator));

			} else {

				driver.findElement(By.xpath(locator));

			}

		} catch (NoSuchElementException e) {
			return false;
		}

		return found;
	}

}