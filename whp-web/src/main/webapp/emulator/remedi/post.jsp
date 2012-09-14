<%@ page import="java.io.BufferedInputStream" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<!DOCTYPE html>
<html>
<head>
    <%


        Object responseParam = application.getAttribute("remedi-response-type");
        boolean returnSuccessResponse = responseParam == null ? true : responseParam.equals("success");
        Object supportedMethod = application.getAttribute("request-method-type");
        String output;
        if (supportedMethod == null)
            supportedMethod = "POST";

        if (!request.getMethod().equalsIgnoreCase(supportedMethod.toString())) {
            System.out.println(String.format("Supported request method '%s' but got '%s' method", supportedMethod, request.getMethod()));
            response.setStatus(400);
            output = "Unsupported method type used; Sending error code 400";
        } else if (returnSuccessResponse) {
            response.setStatus(200);
            output = "Success; Sending status code 200";
        } else {
            System.out.println("");
            response.setStatus(400);
            output = "Unexpected error encountered; Sending error code 400";
        }
    %>
</head>
<body>
<%=output%>
<%
    System.out.println("Received response....\n");
    System.out.println("Got Request Method Type :" + request.getMethod());
    System.out.println("Got Request Query String :\n" + request.getQueryString());
    System.out.println(output);
    StringWriter writer = new StringWriter();
    IOUtils.copy(request.getInputStream(), writer);
    System.out.println("Got Request Body: \n" + writer.toString());
%>
</body>
</html>
