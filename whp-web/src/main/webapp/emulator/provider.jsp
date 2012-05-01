<%@page import="org.motechproject.security.service.MotechAuthenticationService" %>
<%@page import="org.motechproject.whp.patient.domain.Provider" %>
<%@page import="org.motechproject.whp.patient.repository.AllProviders" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="java.util.Arrays" %>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <%
        ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        MotechAuthenticationService authenticationService = (MotechAuthenticationService) appCtx.getBean("motechAuthenticationService", MotechAuthenticationService.class);
        if (null != request.getParameter("provider_id")) {
            String providerId = request.getParameter("provider_id");
            String password = request.getParameter("password");
            Provider provider = ((AllProviders) appCtx.getBean("allProviders", AllProviders.class)).findByProviderId(providerId);
            authenticationService.register(providerId, password, "PROVIDER", provider.getId(), Arrays.asList("PROVIDER"));
        }

        String posted = (null != request.getParameter("provider_id")) ? "POSTED" : "SomethingElse";
    %>
</head>
<body>
<span id="statusMessage" style="font-size: medium; font-weight: bold; color: blue;"></span>
<br/>
<br/>

<form name="testSubmit" action="/whp/emulator/provider.jsp" method="POST">
    <input type="hidden" id="posted_successfully" value="<%=posted%>"/>
    <span style="vertical-align:top">Enter Provider Id:</span>
    <input id="provider_id" name="provider_id" type="text" value="raj"/>
    <br/>
    <span style="vertical-align:top">Enter Password:</span>
    <input id="password" name="password" type="text" value="raj"/>
    <br/>
    <span style="vertical-align:top">Enter Primary Mobile Number:</span>
    <input id="primary_mobile" type="text" value="1234567890"/>
    <br/>
    <span style="vertical-align:top">Enter Secondary Mobile:</span>
    <input id="secondary_mobile" type="text" value="1234567890"/>
    <br/>
    <span style="vertical-align:top">Enter Tertiary Mobile:</span>
    <input id="tertiary_mobile" type="text" value="1234567890"/>
    <br/>
    <span style="vertical-align:top">Enter District:</span>
    <input id="district" type="text" value="Chambal"/>
    <br/>
    <input type="button" id="post-button" value="Submit"/>
</form>
<textarea id="template" style="display:none"><$REGISTRATION$ xmlns="http://openrosa.org/user/registration">
    <username></username>
    <password></password>
    <uuid></uuid>
    <date>20/02/2012 10:10:15</date>
    <user_data>
        <data key="primary_mobile">$PRIMARY_MOBILE$</data>
        <data key="secondary_mobile">$SECONDARY_MOBILE$</data>
        <data key="tertiary_mobile">$TERTIARY_MOBILE$</data>
        <data key="provider_id">$PROVIDER_ID$</data>
        <data key="district">$DISTRICT$</data>
    </user_data>
</$REGISTRATION$></textarea>
<script type="text/javascript">
    var providerXML = $("#template").val();
    providerXML = $.trim(providerXML);
    function replaceTemplateValues() {
        providerXML = providerXML.replace('$PROVIDER_ID$', $("#provider_id").val());
        providerXML = providerXML.replace('$PRIMARY_MOBILE$', $("#primary_mobile").val());
        providerXML = providerXML.replace('$SECONDARY_MOBILE$', $("#secondary_mobile").val());
        providerXML = providerXML.replace('$TERTIARY_MOBILE$', $("#tertiary_mobile").val());
        providerXML = providerXML.replace('$DISTRICT$', $("#district").val());
        providerXML = providerXML.replace('$REGISTRATION$', "Registration");
        providerXML = providerXML.replace('$REGISTRATION$', "Registration");
    }
    $('#post-button').click(function () {
        replaceTemplateValues();
        var host = window.location.host;
        $.ajax({
            type:'POST',
            url:"http://" + host + "/whp/provider/process",
            data:providerXML,
            contentType:"application/xml; charset=utf-8",
            success:function (data, textStatus, jqXHR) {
                $('#statusMessage').html("Status of request: SUCCESS");
                document.forms["testSubmit"].submit();
            },
            error:function (xhr, status, error) {
                $('#statusMessage').html("Status of request: FAILURE. Reason: " + error);
            }
        });

    });
</script>
</body>
</html>
