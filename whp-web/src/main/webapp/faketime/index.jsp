<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>
<%@ page import="org.motechproject.util.DateUtil" %>
<!DOCTYPE html>
<html>
<head>
    <%
    ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
    Properties whpProperties = appCtx.getBean("whpProperties", Properties.class);
    String appVersion = whpProperties.getProperty("application.version");
    %>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="../js/jquery/jquery-ui-1.8.21.custom.js"></script>
    <script type="text/javascript" src="../js/jquery/jquery-datetimepicker-addon.js"></script>
    <script type="text/javascript" src="../js/jquery/jquery-ui-slider.js"></script>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/bootstrap.css/"/>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/standard.css"/>
    <link rel="stylesheet" media="all" type="text/css" href="../styles/jquery-ui-theme.css"/>
</head>
<body>
<span id="statusMessage" style="font-size: medium; font-weight: bold; color: blue;"></span>
<br/>
<br/>

<div class="container">
    <div class="pull-right"><a href="/whp/emulator/">home</a></div>
    <div>
        <form action="" method="get">
            Current Time : <%=DateUtil.now().toDate()%><input type="submit" value="Refresh"/>
        </form>
    </div>

    <form name="fakeTimeSubmit">
        <label for="newDateTime">New Date Time</label>
        <input type="text" name="newDateTime" id="newDateTime" value=""/>
        <input type="button" id="post-button" value="Submit"/>
    </form>
    <script type="text/javascript">
        $(function () {
            $('#newDateTime').datetimepicker({
                dateFormat:'yy-mm-dd',
                timeFormat:'hh:mm'
            });
        });

        $('#post-button').click(function () {
            var host = window.location.host;
            var newDate = $("#newDateTime").val().split(" ")[0];
            var timeComponent = $("#newDateTime").val().split(" ")[1];
            var urlString = "/whp/motech-delivery-tools/datetime/update?type=flow&date=" + newDate + "&hour=" + timeComponent.split(":")[0] + "&minute=" + timeComponent.split(":")[1];
            $.ajax({
                type:'GET',
                url:"http://" + host + urlString,
                contentType:"application/xml; charset=utf-8",
                success:function (data, textStatus, jqXHR) {
                    location.reload();
                },
                error:function (xhr, status, error) {
                    $('#statusMessage').html("Status of request: FAILURE. Reason: " + error);
                }
            });

        });
    </script>
</div>
</body>
</html>
