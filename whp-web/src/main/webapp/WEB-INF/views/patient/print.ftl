<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>Treatment Card</title>
<#include "../layout/scripts.ftl"/>
</head>
<body>
<div id="navibar" class="navbar-fixed-top">
    <a href="<@spring.url '/' />">
        <img class="pull-right" src="<@spring.url '/resources-${applicationVersion}/images/whplogo.png'/>"/>
    </a>
</div>

<div class="container">
    <div class="title">Treatment Card</div>
    <div class="row-fluid" id="mainContent">
        <div>
            <label class="tc-label">State</label> <label class="tc-value">${patient.addressState}</label>
            <label class="tc-label">City/District with code</label> <label
                class="tc-value">${patient.addressDistrict}&nbsp;&nbsp;&nbsp;&nbsp;</label>
            <label class="tc-label">TB Unit with code</label> <label class="tc-value">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
        </div>
        <div class="pull-left">
            <label class="tc-label">Name</label><label class="tc-value">${patient.firstName} ${patient.lastName}</label>

            <div>
                <label class="tc-label">Sex</label><label class="tc-value">${patient.gender}</label>
                <label class="tc-label">Occupation</label><label class="tc-value">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
            </div>
            <div>
                <label class="tc-label">Address</label><label class="tc-value">${patient.address}</label>
            </div>
            <div>
                <label class="tc-label">Phone Number</label><label class="tc-value">${patient.phoneNumber}</label>
            </div>
            <div>
                <label class="tc-label">Address and Phone Number of Contact Person</label><label class="tc-value"><br/></label>
                <br/>
            </div>
            <div>
                <label class="tc-label">Initial home visit by</label><label class="tc-value">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                <label class="tc-label">Date</label><label class="tc-value">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
            </div>
            <div>
                <label class="tc-label">Disease Classification</label><label
                    class="tc-value">${patient.diseaseClass}</label>
            </div>
            <div>
                <label class="tc-label">Type of Patient</label><label class="tc-value">${patient.patientType}</label>
            </div>
        </div>
        <div class="pull-right">
            <div>
                <label class="tc-label">Patient TB Id</label><label class="tc-value">${patient.tbId}</label>
                <label class="tc-label">Patient TB No/Year</label><label
                    class="tc-value">${patient.tbRegistrationNumber!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
            </div>
            <div>
                <label class="tc-label">PHI</label>
                <label class="tc-value">${patient.phi!}</label>
            </div>
            <div>
                <label class="tc-label">Name and designation of DOT provider & Tel No</label>
                <label class=""><br/></label><br/>
            </div>
            <div>
                <label class="tc-label">DOT centre</label>
                <label class="tc-value">&nbsp;&nbsp;&nbsp;&nbsp;</label>
            </div>
            <div><label class="tc-label">Signature of MO with date</label>
                <label class="tc-value">&nbsp;&nbsp;&nbsp;</label>
            </div>
        </div>
        <div class="pull-right">
            <table class="table table-bordered text-center">
                <tr>
                    <th rowspan="2">Sample Instance</th>
                    <th rowspan="2">DMC</th>
                    <th rowspan="2">Lab No.</th>
                    <th colspan="4">Smear Test Results</th>
                    <th rowspan="2">Weight</th>
                </tr>
                <tr>
                    <th>Date 1</th>
                    <th>Result 1</th>
                    <th>Date 2</th>
                    <th>Result 2</th>
                </tr>
            <#list patient.testResults as testResult>
                <tr>
                    <td>${testResult.sampleInstance}</td>
                    <td></td>
                    <td></td>
                    <td>${testResult.smearTestDate1}</td>
                    <td>${testResult.smearTestResult1}</td>
                    <td>${testResult.smearTestDate2}</td>
                    <td>${testResult.smearTestResult2}</td>
                    <td>${testResult.weight}</td>
                </tr>
            </#list>
            </table>
        </div>

        <div id="treatmentCard"></div>
    </div>
</div>
</body>
<script type="text/javascript">
    $('#treatmentCard').load('/whp/treatmentcard/show?patientId=${patient.patientId}', function () {

        //window.print();
    });
</script>

</html>

