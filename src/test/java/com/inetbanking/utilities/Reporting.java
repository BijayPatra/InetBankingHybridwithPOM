package com.inetbanking.utilities;

//Listener class used to generate Extent reports

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Reporting extends TestListenerAdapter
{
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	
		
	public void onStart(ITestContext testContext)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
		String repName="Test-Report-"+timeStamp+".html";
		
		htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+ "/test-output/"+repName);//specify location of the report
		htmlReporter.loadXMLConfig(System.getProperty("user.dir")+ "/extent-config.xml");
		
		extent=new ExtentReports();
		
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host name","localhost");
		extent.setSystemInfo("OS Name",System.getProperty("os.name"));
		extent.setSystemInfo("Environemnt","QA");
		extent.setSystemInfo("user","Bijay");
		extent.setSystemInfo("Java Version",System.getProperty("java.version"));
		
		htmlReporter.config().setDocumentTitle("InetBanking Test Project"); // Tile of report
		htmlReporter.config().setReportName("Functional Test Automation Report"); // name of the report
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); //location of the chart
		htmlReporter.config().setTheme(Theme.DARK);
	}
	
	public void onTestSuccess(ITestResult result)
	{
		logger=extent.createTest(result.getName()); // create new entry in the report
		logger.log(Status.PASS,MarkupHelper.createLabel(result.getName(),ExtentColor.GREEN)); // send the passed information to the report with GREEN color highlighted
	}
	
	public void onTestFailure(ITestResult result)
	{
		logger=extent.createTest(result.getName()); // create new entry in the report
		logger.log(Status.FAIL,MarkupHelper.createLabel(result.getName(),ExtentColor.RED)); // send the passed information to the report with GREEN color highlighted
		
		String screenshotPath=System.getProperty("user.dir")+"\\Screenshots\\"+result.getName()+".png";
		
		File f = new File(screenshotPath); 
		
		if(f.exists())
		{
		try {
			logger.fail("Screenshot is below:" + logger.addScreenCaptureFromPath(screenshotPath));
			} 
		catch (IOException e) 
				{
				e.printStackTrace();
				}
		}
		
	}
	
	public void onTestSkipped(ITestResult result)
	{
		logger=extent.createTest(result.getName()); // create new entry in th report
		logger.log(Status.SKIP,MarkupHelper.createLabel(result.getName(),ExtentColor.ORANGE));
	}
	
	public void onFinish(ITestContext testContext)
	{
		extent.flush();
	}
}
