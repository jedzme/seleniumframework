package othersamples.fileupload;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.Test;

public class DemoFileUpload {

	@Test
	public void testFileUpload() throws InterruptedException, IOException {
		// System.setProperty("webdriver.edge.driver",
		// "resources\\drivers\\MicrosoftWebDriver_3.14393.exe");

		System.setProperty("webdriver.chrome.driver", "resources\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.manage().window().maximize();

		String directory = System.getProperty("user.dir");

		driver.get("file:///" + directory + "/etc/samplepages/fileUploadSample.html");

		// You need to install AutoIT3 inorder to edit the .au3 files in etc\\autoitscripts64\\uncompiled
		// visit https://www.autoitscript.com/site/autoit/downloads/
		
		//We can use Runtime.getRuntime() or the ProcessBuilder 
		//Runtime.getRuntime().exec("etc\\autoitscriptsx64\\compiled\\fileupload.exe");
		Process process = new ProcessBuilder("etc\\autoitscriptsx64\\compiled\\fileupload.exe").start();

		driver.findElement(By.xpath(".//*[@id='btnUpload']")).click();

		// Sleep the current thread running to see output; this is not
		// recommended on actual test
		Thread.sleep(5000);

		driver.quit();
	}

}
