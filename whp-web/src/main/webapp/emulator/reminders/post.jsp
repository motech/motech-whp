<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
    <%

        Object responseParam = application.getAttribute("reminder-response-type");
        boolean returnSuccessResponse = responseParam == null ? true : responseParam.equals("success");
        Object supportedMethod = application.getAttribute("request-method-type");

        int bufferSize = 10;
        ArrayList<String> responses = (ArrayList<String>) application.getAttribute("responses");
        if (responses == null)
            responses = new ArrayList<String>();
        else if (responses.size() >= bufferSize) {
            responses.remove(0);
        }

        if (supportedMethod == null)
            supportedMethod = "POST";

        String output = "";
        if (!request.getMethod().equalsIgnoreCase(supportedMethod.toString())) {
            output += String.format("Unsupported method type. Supported request method '%s' but got '%s' method\n", supportedMethod, request.getMethod());
            response.setStatus(400);
            output += "Sending error code 400\n";
        } else if (returnSuccessResponse) {
            response.setStatus(200);
            output += "Success; Sending status code 200\n";
        } else {
            System.out.println("");
            response.setStatus(400);
            output += "Unexpected error encountered; Sending error code 400\n";
        }
    %>
</head>
<body>
<%
    output += "Received response:\n";
    output += "Got Request Method Type :" + request.getMethod()+"\n";
    output += "Got Request Query String :\n" + request.getQueryString()+"\n";
    StringWriter writer = new StringWriter();
    IOUtils.copy(request.getInputStream(), writer);
    output += "Got Request Body: \n" + writer.toString();
    responses.add(output);
    application.setAttribute("responses", responses);
%>

<pre>
<%=output%>
</pre>
</body>
</html>
