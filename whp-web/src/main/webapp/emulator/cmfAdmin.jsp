<%@page import="org.motechproject.security.service.MotechAuthenticationService" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>
<html>
<head>
    <%
        ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        Properties whpProperties = appCtx.getBean("whpProperties", Properties.class);
        String appVersion = whpProperties.getProperty("application.version");
    %>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/standard.css"/>

    <%
        MotechAuthenticationService authenticationService = appCtx.getBean("motechAuthenticationService", MotechAuthenticationService.class);
        String providerId = "";
        if (null != request.getParameter("provider_id")) {
            providerId = request.getParameter("provider_id");
            authenticationService.activateUser(providerId);
        }
        String posted = (null != request.getParameter("provider_id")) ? "Success" : "";
    %>
</head>
<body>
<span id="statusMessage" style="font-size: medium; font-weight: bold; color: blue;"><%= posted %></span>
<br/>
<br/>

<div class="container">
    <div class="pull-right"><a href="/whp/emulator/">home</a></div>
    <form name="testSubmit" action="/whp/emulator/cmfAdmin.jsp" method="POST">
        <input type="hidden" id="posted_successfully" value="<%=posted%>"/>

        <div class="row-fluid">
            <span style="vertical-align:top" class="pull-left span3">Enter Provider Id</span>
            <input id="provider_id" class="span" name="provider_id" type="text" value="<%= providerId %>"/>
        </div>
        <input type="submit" id="post-button" value="Submit"/>
    </form>
</div>
</body>
</html>
