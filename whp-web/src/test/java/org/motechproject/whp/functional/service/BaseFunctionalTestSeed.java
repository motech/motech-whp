package org.motechproject.whp.functional.service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.framework.WHPUrl;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class BaseFunctionalTestSeed {

    public void adjustDateTime(DateTime dateTime) {
        String url = WHPUrl.baseFor("motech-delivery-tools/datetime/update");
        GetMethod getMethod = new GetMethod(url);
        NameValuePair[] queryString = new NameValuePair[4];
        queryString[0] = new NameValuePair("type", "flow");
        queryString[1] = new NameValuePair("date", dateTime.toString("yyyy-MM-dd"));
        queryString[2] = new NameValuePair("hour", String.valueOf(dateTime.getHourOfDay()));
        queryString[3] = new NameValuePair("minute", String.valueOf(dateTime.getMinuteOfHour()));
        getMethod.setQueryString(queryString);

        int statusCode = 0;
        try {
            statusCode = new HttpClient().executeMethod(getMethod);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(200, statusCode);
    }

    public void adjustDate(String date, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        adjustDateTime(DateUtil.newDateTime(LocalDate.parse(date, formatter)));
    }
}
