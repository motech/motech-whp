<#import "/spring.ftl" as spring />
<#setting date_format="MM/dd/yyyy"/>
<!DOCTYPE html>
<html>
<head>
    <title>Treatment Card</title>
<#include "../layout/scripts.ftl"/>
</head>
<body class="printable-version">
<div class="container">
<div id="navibar" class="navbar-fixed-top">
    <a href="<@spring.url '/' />">
        <img class="pull-right" src="<@spring.url '/resources-${applicationVersion}/images/whplogo.png'/>"/>
    </a>
</div>
<h3 class="text-center uppercase">TB Treatment Card</h3>
<hr class="bordered"/>
<div class="row-fluid" id="mainContent">

<div class="row-fluid">
    <div class="span6">
        <div><label class="tc-label">State</label> <label class="tc-value">${patient.addressState}</label></div>
        <div>
            <label class="tc-label">City/District with code</label>
            <label class="tc-value">${patient.treatmentDetails.districtWithCode}</label>
        </div>
        <label class="tc-label">Name</label><label
            class="tc-value name">${patient.firstName!} ${patient.lastName!}</label>

        <div>
            <label class="tc-label">Sex</label><label class="tc-value">${patient.gender!}</label>
            <label class="tc-label">Age</label><label class="tc-value">${patient.age!}</label>
        </div>
        <div>
            <label class="tc-label">Occupation</label>
        </div>
        <div>
            <label class="tc-label">Complete Address</label><label class="tc-value">${patient.address!}
            , ${patient.addressVillage} , ${patient.addressDistrict} , ${patient.addressState}</label>
        </div>
        <div>
            <label class="tc-label">Phone Number</label><label class="tc-value">${patient.phoneNumber!}</label>
        </div>
        <div>
            <label class="tc-label">Name and Address of Contact Person</label>
            <label class="tc-value">${patient.treatmentDetails.contactPersonName}</label>
            <br/>
        </div>
        <div>
            <label class="tc-label">Phone Number of Contact Person</label>
            <label class="tc-value">${patient.treatmentDetails.contactPersonPhoneNumber}</label>
            <br/>
        </div>
        <div>
            <label class="tc-label">Initial home visit by</label><label class="tc-value width-60px"></label>
            <label class="tc-label">Date</label><label class="tc-value">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
        </div>
        <div>
            <label class="tc-label">Disease Classification</label>
            <label class="tc-value">${patient.diseaseClass}</label>
        </div>
        <div>
            <label class="tc-label">EP Site</label>
            <label class="tc-value">${patient.treatmentDetails.epSite}</label>
        </div>
        <div>
            <label class="tc-label">Type of Patient</label><label class="tc-value">${patient.patientType!}</label>
        </div>
    </div>
    <div class="span6">
        <div>
            <label class="tc-label">TB Unit with code</label>
            <label class="tc-value">${patient.treatmentDetails.tbUnitWithCode}</label>
        </div>
        <div>
            <label class="tc-label">Patient Id</label><label class="tc-value">${patient.patientId!}</label>
        </div>
        <div>
            <label class="tc-label">Patient TB Id</label><label class="tc-value">${patient.tbId!}</label>
        </div>
        <div>
            <label class="tc-label">Patient TB No/Year</label><label
                class="tc-value">${patient.tbRegistrationNumber!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
        </div>
        <div>
            <label class="tc-label">PHI</label>
            <label class="tc-value">${patient.phi!}</label>
        </div>
        <div>
            <label class="tc-label">Name and designation of DOT provider & Tel No</label>
            <label class="">${patient.treatmentDetails.providerName!}, ${patient.providerId!}
                , ${patient.providerMobileNumber!}
                <br/></label><br/>
        </div>
        <div>
            <label class="tc-label">DOT centre</label>
            <label class="tc-value">${patient.treatmentDetails.dotCentre}</label>
        </div>
        <div><label class="tc-label">Signature of MO with date</label>
            <label class="tc-value">${patient.treatmentDetails.cmfDoctor}</label>
            <label class="tc-value">${patient.therapyStartDate}</label>
        </div>
    </div>
</div>
<br/>

<div id="test-results-pause-provider-history" class="row">
    <div class="span6">
    <#if treatmentCard.treatmentHistories?has_content>
        <div class="provider-details">
            <h4 class="inherit-font">Provider History</h4>
            <table class="table table-bordered sharp fixed text-center">
                <tr>
                    <th>Provider</th>
                    <th>From</th>
                    <th>To</th>
                </tr>
                <#list treatmentCard.treatmentHistories as treatmentHistory>
                    <tr>
                        <td>${treatmentHistory.providerId!}</td>
                        <td>${treatmentHistory.startDate}</td>
                        <td>${treatmentHistory.endDate}</td>
                    </tr>
                </#list>
            </table>
        </div>
    </#if>
    <#if treatmentCard.treatmentPausePeriods?has_content>
        <div class="pause-details">
            <h4 class="inherit-font">Treatment Pause Details</h4>
            <table class="table table-bordered sharp fixed text-center">
                <tr>
                    <th>From</th>
                    <th>To</th>
                </tr>
                <#list treatmentCard.treatmentPausePeriods as treatmentPausePeriod>
                    <tr>
                        <td>${treatmentPausePeriod.startDate}</td>
                        <td>${treatmentPausePeriod.endDate}</td>
                    </tr>
                </#list>
            </table>
        </div>
    </#if>
    </div>
    <div class="span6">
        <h4 class="inherit-font">Smear Test Results</h4>
        <table class="table table-bordered sharp fixed text-center">
            <tr>
                <th rowspan="2">Sample Instance</th>
                <th rowspan="2">DMC</th>
                <th rowspan="2">Lab No.</th>
                <th colspan="4">Smear Test Results</th>
                <th rowspan="2">Weight</th>
            </tr>
            <tr>
                <th class="no-wrap">Date 1</th>
                <th class="no-wrap">Result 1</th>
                <th class="no-wrap">Date 2</th>
                <th class="no-wrap">Result 2</th>
            </tr>
        <#list patient.testResults as testResult>
            <tr>
                <td>${testResult.sampleInstance}</td>
                <td>${testResult.labName}</td>
                <td>${testResult.labNumber}</td>
                <td>${testResult.smearTestDate1}</td>
                <td>${testResult.smearTestResult1}</td>
                <td>${testResult.smearTestDate2}</td>
                <td>${testResult.smearTestResult2}</td>
                <td>${testResult.weight}</td>
            </tr>
        </#list>
        </table>
        <h4 class="inherit-font">GeneXpert Test Results</h4>
        <table class="table table-bordered sharp fixed text-center">
            <thead>
            <tr>
                <th class="no-wrap">Xpert MTB/RIF Device Number</th>
                <th class="no-wrap">Date of Xpert Test</th>
                <th class="no-wrap">Xpert Test Result</th>
                <th class="no-wrap">Rif Resistance Result</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${patient.treatmentDetails.xpertDeviceNumber}</td>
                <td>${patient.treatmentDetails.xpertTestDate}</td>
                <td>${patient.treatmentDetails.xpertTestResult}</td>
                <td>${patient.treatmentDetails.rifResistanceResult}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div>
    <label class="tc-label">H/O previous Anti-TB treatment with duration</label>
    <label class="tc-value">${patient.treatmentDetails.previousTreatmentHistory}</label>
</div>
<div id="treatmentCard">
<#include "../treatmentcard/print.ftl">
</div>
<div class="row-fluid">
    <div class="span6">
        <label class="tc-label">Treatment outcome with date</label>
        <label class="tc-value">${patient.currentTreatment.treatmentOutcome!}</label>
    </div>
    <label class="span6 tc-label">Signature of MO with date</label>

</div>
<div class="x-ray-and-remarks row-fluid">
    <div class="span6">

        <div class="x-ray ">
            <b> Details of X ray / EP tests:</b><br>
        ${patient.treatmentDetails.otherInvestigations}
        </div>
    </div>
    <div class="span6">
        <label class="tc-label">Remarks</label>
        <label class="tc-value">${patient.currentTreatment.closeTreatmentRemarks!}</label>
    </div>
</div>
<div class="row-fluid">
    <div class="actions-for-missed-doses span7">
        <h4 class="inherit-font">Retrieval Actions for Missed Doses</h4>
        <table class="table table-bordered sharp fixed text-center line-height-normal">
            <tr>
                <th>Date</th>
                <th>By whom</th>
                <th>Whom contacted</th>
                <th>Reason for missed doses</th>
                <th>Outcome of retrieval action</th>
            </tr>
        <#list 1..7 as i>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </#list>
        </table>
    </div>
    <div class="span5">
        <h4 class="inherit-font text-center">Household Contacts </h4>
        <h4 class="inherit-font text-center">(Children &lt; 6 yrs)</h4>
        <table class="table table-bordered sharp fixed text-center line-height-normal">
            <tr>
                <th>No.</th>
                <th>Chemoprophylaxis</th>
            </tr>
            <tr>
                <td>${patient.treatmentDetails.membersBelowSixYears}</td>
                <td>${patient.treatmentDetails.phcReferred}</td>
            </tr>
        <#list 1..4 as i>
            <tr>
                <td></td>
                <td></td>
            </tr>
        </#list>
        </table>
    </div>
</div>
<div class="additional-treatments">
    <h4 class="inherit-font tc-label text-center">Additional Treatments</h4>

    <div>
        <label class="tc-label">HIV status</label>
        <label class="tc-value">${patient.treatmentDetails.hivStatus}</label>
        <label class="tc-label">HIV Test Date</label>
        <label class="tc-value">${patient.treatmentDetails.hivTestDate}</label>
    </div>
    <div>
        <label class="tc-label">CPT delivered on (date)</label>
        <label class="tc-value width-30px">(1)</label>
        <label class="tc-value width-30px">(2)</label>
        <label class="tc-value width-30px">(3)</label>
        <label class="tc-value width-30px">(4)</label>
        <label class="tc-value width-30px">(5)</label>
    </div>
    <div>
        <label class="tc-label">Pt referred to ART centre (date)</label>

    </div>
    <div>
        <label class="tc-label">Initiated on ART</label>

        <div class="empty-bordered"></div>
        <label class="tc-value">No</label>

        <div class="empty-bordered"></div>
        <label class="tc-value">Yes (date)</label>
    </div>
</div>
</div>
</div>
</body>
<script type="text/javascript">
    if (parseInt($('#test-results-pause-provider-history').css('height')) > 400) {
        $('#ip-card').addClass('page-break');
    <#if treatmentCard.CPAdherenceSectionValid>
        $('.actions-for-missed-doses').addClass('page-break');
    </#if>
    }
    else {
    <#if treatmentCard.IPAdherenceSectionValid || treatmentCard.CPAdherenceSectionValid>
        $('#cp-card').addClass('page-break');
    <#else>
        $('.actions-for-missed-doses').addClass('page-break');
    </#if>
    }

    window.print();
</script>
</html>

