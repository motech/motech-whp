package org.motechproject.whp.functional.framework;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.page.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public abstract class BaseTest {

    private Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Rule
    public TestName testName = new TestName();

    protected WebDriver webDriver;

    @Before
    public void setUp() {
        createWebDriver();
        logger.info("Using login URL as %s", LoginPage.LOGIN_URL);
        webDriver.get(LoginPage.LOGIN_URL);
    }

    private void createWebDriver() {
        try {
            webDriver = WebDriverFactory.getInstance();
        } catch (WebDriverException e) {
            if (e.getMessage().contains("Unable to bind to locking port")) {
                createWebDriver();
            } else {
                throw e;
            }
        }
    }

    protected String unique(String name) {
        return name + DateUtil.now().toInstant().getMillis() + Math.random();
    }

    @After
    public void tearDown() throws IOException {
        String testMethodName = testName.getMethodName();
        testMethodName = StringUtils.isEmpty(testMethodName) ? DateUtil.now().toString("yyyy-MM-dd HH-mm") : testMethodName;
        if (webDriver == null) return;
        BufferedWriter output = null;
        try {
            String pageSource = webDriver.getPageSource();
            File file = new File(System.getProperty("base.dir"), String.format("target/%s.html", testMethodName));
            output = new BufferedWriter(new FileWriter(file));
            output.write(pageSource);
            logger.info("HTML Output logged to %s", file.getName());
        } catch (Exception ignore) {
            System.err.println("Unable to write html result to file " + ignore.getMessage());
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.close();
            webDriver.quit();
            if (output != null)
                output.close();
        }
        adjustDateTime(DateUtil.now());
    }

    public void adjustDateTime(int dayOfMonth, int monthOfYear, int year) {
        adjustDateTime(DateUtil.newDateTime(new LocalDate(year, monthOfYear, dayOfMonth)));
    }

    public void adjustDateTime(DateTime dateTime) {
        String url = WHPUrl.baseFor("motech-delivery-tools/datetime/update");
        GetMethod getMethod = new GetMethod(url);
        NameValuePair[] queryString = new NameValuePair[3];
        queryString[0] = new NameValuePair("date", dateTime.toString("yyyy-MM-dd"));
        queryString[1] = new NameValuePair("hour", String.valueOf(dateTime.getHourOfDay()));
        queryString[2] = new NameValuePair("minute", String.valueOf(dateTime.getMinuteOfHour()));
        getMethod.setQueryString(queryString);

        int statusCode = 0;
        try {
            statusCode = new HttpClient().executeMethod(getMethod);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(200, statusCode);
    }
}
