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

    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/jquery-ui-1.9.1.custom.min.css"/>

    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/standard.css"/>

    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery.validate.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery.metadata.js"></script>

    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery/jquery-ui-1.9.1.custom.min.js"></script>
    <script type="text/javascript" src="/whp/resources-<%=appVersion%>/js/jquery//jquery-datetimepicker-addon.js"></script>
</head>
<body>
<span id="statusMessage" style="font-size: medium; font-weight: bold; color: blue;"></span>
<br/>
<br/>

<div class="container">
    <p class=""><a href="/whp/emulator/"><i class="icon-home"></i> Home</a></p>

        <form action="" method="get">
          <p>
              Current Time : <span class="bold"> <%=DateUtil.now().toDate()%> </span>
              <input type="submit" value="Refresh" class="btn"/>
         </p>
        </form>


    <form name="fakeTimeSubmit">
       <p>
           <label for="newDateTime">New Date Time</label>
            <input type="text" name="newDateTime" id="newDateTime" value=""/>
            <input type="button" id="post-button" value="Submit" class="btn btn-primary"/>
       </p>
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
