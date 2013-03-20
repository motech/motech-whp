<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>
<!DOCTYPE html>
<html>
<head>
    <%
    ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
    Properties whpProperties = appCtx.getBean("whpProperties", Properties.class);
    String appVersion = whpProperties.getProperty("application.version");
    %>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/whp/resources-<%=appVersion%>/styles/standard.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
</head>
<body>

<div class="container">
    <p class=""><a href="/whp/emulator/"><i class="icon-home"></i> Home</a></p>

    <form name="testSubmit">
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Case Id:</span>
            <input id="case_id" class="span4" type="text" value="12345"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Case Name:</span>
            <input id="case_name" class="span4" type="text" value="Raju Singh"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter TB_Id:</span>
            <input id="tb_id" class="span4" type="text" value="12345678891"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter TB Registration Date:</span>
            <input id="tb_registration_date" class="span4" type="text" value="25/11/2012"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Provider Id:</span>
            <input id="provider_id" class="span4" type="text" value="raj"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter First Name:</span>
            <input id="first_name" class="span4" type="text" value="raju"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Last Name:</span>
            <input id="last_name" class="span4" type="text" value="singh"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Age:</span>
            <input id="age" class="span4" type="text" value="12"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Gender:</span>
            <input id="gender" class="span4" type="text" value="M"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Date of Birth:</span>
            <input id="date_of_birth" class="span4" type="text" value="25/11/1987"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Smear test instance:</span>
            <input id="smear_instance" class="span4" type="text" value="PreTreatment"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Test Date 1:</span>
            <input id="test_date1" class="span4" type="text" value="01/03/2012"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Test Result 1:</span>
            <input id="test_result1" class="span4" type="text" value="Positive"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Test Date 2:</span>
            <input id="test_date2" class="span4" type="text" value="01/03/2012"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Test Result 2:</span>
            <input id="test_result2" class="span4" type="text" value="Positive"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Treatment Category:</span>
            <input id="tc" class="span4" type="text" value="01"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Address location:</span>
            <input id="al" class="span4" type="text" value="1001"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Address landmark:</span>
            <input id="landmark" class="span4" type="text" value="Near banyan tree"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Address village:</span>
            <input id="av" class="span4" type="text" value="chambal"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Address block:</span>
            <input id="ab" class="span4" type="text" value="Vijaynagar"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter District:</span>
            <input id="address_district" class="span4" type="text" value="Patna"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Address state:</span>
            <input id="as" class="span4" type="text" value="Bihar"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Mobile number:</span>
            <input id="mobile" class="span4" type="text" value="9880123456"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Disease class:</span>
            <input id="dclass" class="span4" type="text" value="P"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter weight instance:</span>
            <input id="wii" class="span4" type="text" value="PreTreatment"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter weight:</span>
            <input id="w" class="span4" type="text" value="80"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter district with code:</span>
            <input id="district_with_code" class="span4" type="text" value="Begusarai 0087"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter TB Unit with code:</span>
            <input id="tb_unit_with_code" class="span4" type="text" value="Info to be given by PHC"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter EP site:</span>
            <input id="ep_site" class="span4" type="text" value="Cervical lymphadenitis"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Other investigations:</span>
            <input id="other_investigations" class="span4" type="text" value="Chest Xray 20/9/2011 reported cavity right apex"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Previous Treatment History:</span>
            <input id="previous_treatment_history" class="span4" type="text" value="No history of TB treatment prior to current"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter HIV status:</span>
            <input id="hiv_status" class="span4" type="text" value="Positive"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter HIV Test Date:</span>
            <input id="hiv_test_date" class="span4" type="text" value="12/01/2013"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Members below Six years:</span>
            <input id="members_below_six_years" class="span4" type="text" value="5"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter PHC Preferred:</span>
            <input id="phc_referred" class="span4" type="text" value="Yes"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Provider Name:</span>
            <input id="provider_name" class="span4" type="text" value="raj"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Dot Center:</span>
            <input id="dot_centre" class="span4" type="text" value="MURWA SkyHealth Centre"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Provider Type:</span>
            <input id="provider_type" class="span4" type="text" value="TPC"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Cmf Doctor:</span>
            <input id="cmf_doctor" class="span4" type="text" value="Arun nath"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Xpert Device Number:</span>
            <input id="xpert_device_number" class="span4" type="text" value="WHP-03"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Xpert Test Date:</span>
            <input id="xpert_test_date" class="span4" type="text" value="12/02/2013"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Xpert Test Result:</span>
            <input id="xpert_test_result" class="span4" type="text" value="MTB Detected Medium"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Resistance Result:</span>
            <input id="rif_resistance_result" class="span4" type="text" value="Rif Resistance Detected"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Contact Person Name:</span>
            <input id="contact_person_name" class="span4" type="text" value="Raja sahu"/>
        </div>
        <div class="row-fluid">
            <span class="pull-left span3" style="vertical-align:top">Enter Person Phone Number:</span>
            <input id="contact_person_phone_number" class="span4" type="text" value="9999999999"/>
        </div>
        <div class="row-fluid">
            <input type="button" id="submit" value="Submit" class="btn btn-large btn-primary offset3"/>
        </div>

    </form>
    <br/>
    <br/>
    <textarea id="statusMessage" rows="30" cols="100" style="min-width: 100%" readonly></textarea>
    <textarea id="template" style="display:none">
        <case xmlns="http://openrosa.org/javarosa" case_id="$CASE_ID$" api_key="3F2504E04F8911D39A0C0305E82C3301" date_modified="03/04/2012 11:23:40"
              user_id="system">
            <create>
                <case_type>whp_tb_patient</case_type>
                <case_name>$CASE_NAME$</case_name>
            </create>
            <update>
                <tb_id>$TB_ID$</tb_id>
                <provider_id>$PROVIDER_ID$</provider_id>
                <patient_type>New</patient_type>
                <first_name>$FIRST_NAME$</first_name>
                <last_name>$LAST_NAME$</last_name>
                <age>$AGE$</age>
                <gender>$GENDER$</gender>
                <date_of_birth>$DATE_OF_BIRTH$</date_of_birth>
                <smear_sample_instance>$SMEAR_INSTANCE$</smear_sample_instance>
                <smear_test_date_1>$TEST_DATE1$</smear_test_date_1>
                <smear_test_result_1>$TEST_RESULT1$</smear_test_result_1>
                <smear_test_date_2>$TEST_DATE2$</smear_test_date_2>
                <smear_test_result_2>$TEST_RESULT2$</smear_test_result_2>
                <treatment_category>$TC$</treatment_category>
                <address_location>$AL$</address_location>
                <address_landmark>$LANDMARK$</address_landmark>
                <address_village>$AV$</address_village>
                <address_block>$AB$</address_block>
                <address_district>$AD$</address_district>
                <address_state>$AS$</address_state>
                <mobile_number>$MOBILE$</mobile_number>
                <phi>WHP</phi>
                <disease_class>$DCLASS$</disease_class>
                <weight_instance>$WII$</weight_instance>
                <weight>$W$</weight>
                <tb_registration_date>$TB_REGISTRATION_DATE$</tb_registration_date>
                <district_with_code>$DISTRICT_WITH_CODE$</district_with_code>
                <tb_unit_with_code>$TB_UNIT_WITH_CODE$</tb_unit_with_code>
                <ep_site>$EP_SITE$</ep_site>
                <other_investigations>$OTHER_INVESTIGATIONS$</other_investigations>
                <previous_treatment_history>$PREVIOUS_TREATMENT_HISTORY$</previous_treatment_history>
                <hiv_status>$HIV_STATUS$</hiv_status>
                <hiv_test_date>$HIV_TEST_DATE$</hiv_test_date>
                <members_below_six_years>$MEMBERS_BELOW_SIX_YEARS$</members_below_six_years>
                <phc_referred>$PHC_REFERRED$</phc_referred>
                <provider_name>$PROVIDER_NAME$</provider_name>
                <dot_centre>$DOT_CENTRE$</dot_centre>
                <provider_type>$PROVIDER_TYPE$</provider_type>
                <cmf_doctor>$CMF_DOCTOR$</cmf_doctor>
                <xpert_device_number>$XPERT_DEVICE_NUMBER$</xpert_device_number>
                <xpert_test_date>$XPERT_TEST_DATE$</xpert_test_date>
                <xpert_test_result>$XPERT_TEST_RESULT$</xpert_test_result>
                <rif_resistance_result>$RIF_RESISTANCE_RESULT$</rif_resistance_result>
                <contact_person_name>$CONTACT_PERSON_NAME$</contact_person_name>
                <contact_person_phone_number>$CONTACT_PERSON_PHONE_NUMBER$</contact_person_phone_number>
            </update>
        </case>
    </textarea>
    <script type="text/javascript">
        var patientXML = "";
        function generatePatientXml() {
            patientXML =  $("#template").val();
            patientXML = $.trim(patientXML);
            patientXML = patientXML.replace('$TB_ID$', $("#tb_id").val());
            patientXML = patientXML.replace('$TB_REGISTRATION_DATE$', $("#tb_registration_date").val());
            patientXML = patientXML.replace('$CASE_NAME$', $("#case_name").val());
            patientXML = patientXML.replace('$CASE_ID$', $("#case_id").val());
            patientXML = patientXML.replace('$PROVIDER_ID$', $("#provider_id").val());
            patientXML = patientXML.replace('$FIRST_NAME$', $("#first_name").val());
            patientXML = patientXML.replace('$LAST_NAME$', $("#last_name").val());
            patientXML = patientXML.replace('$AGE$', $("#age").val());
            patientXML = patientXML.replace('$GENDER$', $("#gender").val());
            patientXML = patientXML.replace('$DATE_OF_BIRTH$', $("#date_of_birth").val());
            patientXML = patientXML.replace('$SMEAR_INSTANCE$', $("#smear_instance").val());
            patientXML = patientXML.replace('$TEST_DATE1$', $("#test_date1").val());
            patientXML = patientXML.replace('$TEST_RESULT1$', $("#test_result1").val());
            patientXML = patientXML.replace('$TEST_DATE2$', $("#test_date2").val());
            patientXML = patientXML.replace('$TEST_RESULT2$', $("#test_result2").val());
            patientXML = patientXML.replace('$TC$', $("#tc").val());
            patientXML = patientXML.replace('$AL$', $("#al").val());
            patientXML = patientXML.replace('$LANDMARK$', $("#landmark").val());
            patientXML = patientXML.replace('$AV$', $("#av").val());
            patientXML = patientXML.replace('$AB$', $("#ab").val());
            patientXML = patientXML.replace('$AD$', $("#address_district").val());
            patientXML = patientXML.replace('$AS$', $("#as").val());
            patientXML = patientXML.replace('$MOBILE$', $("#mobile").val());
            patientXML = patientXML.replace('$DCLASS$', $("#dclass").val());
            patientXML = patientXML.replace('$WII$', $("#wii").val());
            patientXML = patientXML.replace('$W$', $("#w").val());
            patientXML = patientXML.replace('$DISTRICT_WITH_CODE$', $("#district_with_code").val());
            patientXML = patientXML.replace('$TB_UNIT_WITH_CODE$', $("#tb_unit_with_code").val());
            patientXML = patientXML.replace('$EP_SITE$', $("#ep_site").val());
            patientXML = patientXML.replace('$OTHER_INVESTIGATIONS$', $("#other_investigations").val());
            patientXML = patientXML.replace('$PREVIOUS_TREATMENT_HISTORY$', $("#previous_treatment_history").val());
            patientXML = patientXML.replace('$HIV_STATUS$', $("#hiv_status").val());
            patientXML = patientXML.replace('$HIV_TEST_DATE$', $("#hiv_test_date").val());
            patientXML = patientXML.replace('$MEMBERS_BELOW_SIX_YEARS$', $("#members_below_six_years").val());
            patientXML = patientXML.replace('$PHC_REFERRED$', $("#phc_referred").val());
            patientXML = patientXML.replace('$PROVIDER_NAME$', $("#provider_name").val());
            patientXML = patientXML.replace('$DOT_CENTRE$', $("#dot_centre").val());
            patientXML = patientXML.replace('$PROVIDER_TYPE$', $("#provider_type").val());
            patientXML = patientXML.replace('$CMF_DOCTOR$', $("#cmf_doctor").val());
            patientXML = patientXML.replace('$XPERT_DEVICE_NUMBER$', $("#xpert_device_number").val());
            patientXML = patientXML.replace('$XPERT_TEST_DATE$', $("#xpert_test_date").val());
            patientXML = patientXML.replace('$XPERT_TEST_RESULT$', $("#xpert_test_result").val());
            patientXML = patientXML.replace('$RIF_RESISTANCE_RESULT$', $("#rif_resistance_result").val());
            patientXML = patientXML.replace('$CONTACT_PERSON_NAME$', $("#contact_person_name").val());
            patientXML = patientXML.replace('$CONTACT_PERSON_PHONE_NUMBER$', $("#contact_person_phone_number").val());
        }
        $(document).ready(function() {
            $('#submit').click(function () {
                generatePatientXml();
                var host = window.location.host;
                $.ajax({
                    type:'POST',
                    url:"http://" + host + "/whp/patient/process",
                    data:patientXML,
                    contentType:"application/xml; charset=utf-8",
                    success:function (data, textStatus, jqXHR) {
                        $('#statusMessage').text("Status of request: SUCCESS");
                    },
                    error:function (xhr, status, error) {
                        $('#statusMessage').text("Status of request: FAILURE. Reason: " + error + "</br>" + xhr.responseText);
                    }
                })
            });
        });
    </script>
</div>
</body>
</html>
