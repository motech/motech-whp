<%@page import="org.apache.commons.lang.exception.ExceptionUtils" %>
<%@page import="org.motechproject.util.DateUtil" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.lang.reflect.Method" %>
<%
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    boolean success = false;
    try {
        Method m = ClassLoader.class.getDeclaredMethod("loadLibrary", Class.class, String.class, Boolean.TYPE);
        m.setAccessible(true);
        m.invoke(null, java.lang.System.class, "jvmfaketime", false);
    } catch (Exception e) {
        System.out.println("couldn't load native library.");
    }
    System.startFakingTime();
    try {
        if (request.getMethod().equals("POST")) {
            String dateTime = request.getParameter("newDateTime");
            Date newDateTime = dateFormat.parse(dateTime);

            System.out.println("Current Time: " + dateFormat.format(new Date()));
            System.out.println("Request for Updated Time: " + dateFormat.format(newDateTime));

            System.moveTimeBy(newDateTime.getTime() - System.currentTimeMillis());

        }
    } catch (java.lang.Exception e) {
        out.println("Error: " + ExceptionUtils.getFullStackTrace(e));
        return;
    }

%>
<html>

<head>
    <%
        ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        Properties whpProperties = appCtx.getBean("whpProperties", Properties.class);
        String appVersion = whpProperties.getProperty("application.version");
    %>

    <title>FakeTime</title>

    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/jquery-ui-1.9.1.custom.min.css"/>

    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/standard.css"/>

    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery.validate.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery.metadata.js"></script>

    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery-ui-1.9.1.custom.min.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery//jquery-datetimepicker-addon.js"></script>

    <script type="text/javascript">
        $(function () {
            $('#newDateTime').datetimepicker({
                dateFormat:'dd/mm/yy',
                timeFormat:'hh:mm'
            });
        });
    </script>

</head>

<body>
<span id="statusMessage" style="font-size: medium; font-weight: bold; color: blue;"></span>
<br/>
<br/>

<div class="container">
    <p class=""><a href="/whp/emulator/"><i class="icon-home"></i> Home</a></p>
    <%
        if (request.getMethod().equals("POST")) {
    %>
    <%
        }
    %>
    <form action="" method="get">
        <p>
            Current Time : <span class="bold"> <%=DateUtil.now().toDate()%> </span>
            <input type="submit" value="Refresh" class="btn"/>
        </p>
    </form>

    <form name="fakeTimeSubmit" method="post">
        <p>
            <label for="newDateTime">New Date Time</label>
            <input type="text" name="newDateTime" id="newDateTime" value="<%=dateFormat.format(new Date())%>"/>
            <input type="submit" id="post-button" value="Submit" class="btn btn-primary"/>
        </p>
    </form>
</div>
</body>
</html>
