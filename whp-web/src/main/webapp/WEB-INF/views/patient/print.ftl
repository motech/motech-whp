<#import "/spring.ftl" as spring />
<#setting date_format="MM/dd/yyyy"/>
<!DOCTYPE html>
<html>
<head>
    <title>Treatment Card</title>
<#include "../layout/scripts.ftl"/>
</head>
<body>
<div class="container printable-version print-version-line-height">
<div id="navibar" class="navbar-fixed-top">
    <a href="<@spring.url '/' />">
        <img class="pull-right" src="<@spring.url '/resources-${applicationVersion}/images/whplogo.png'/>"/>
    </a>
</div>
<h3 class="text-center uppercase">Treatment Card</h3>
<hr class="bordered"/>
<div class="row-fluid" id="mainContent">

<div class="relative-size">
    <div class="tc-div">
        <div><label class="tc-label">State</label> <label class="tc-value">${patient.addressState}</label></div>
        <div><label class="tc-label">City/District with code</label>
            <label class="tc-value">${patient.addressDistrict}&nbsp;&nbsp;&nbsp;&nbsp;</label></div>
        <label class="tc-label">Name</label><label class="tc-value">${patient.firstName} ${patient.lastName}</label>

        <div>
            <label class="tc-label">Sex</label><label class="tc-value">${patient.gender}</label>
            <label class="tc-label">Age</label><label class="tc-value">${patient.age}</label>
        </div>
        <div>
            <label class="tc-label">Occupation</label>
        </div>
        <div>
            <label class="tc-label">Address</label><label class="tc-value">${patient.address}</label>
        </div>
        <div>
            <label class="tc-label">Phone Number</label><label class="tc-value">${patient.phoneNumber}</label>
        </div>
        <div>
            <label class="tc-label">Address and Phone Number of Contact Person</label><label
                class="tc-value"><br/></label>
            <br/>
        </div>
        <div>
            <label class="tc-label">Initial home visit by</label><label class="tc-value width-60px"></label>
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
    <div class="left-spaced tc-div">
        <div><label class="tc-label">TB Unit with code</label></div>
        <div>
            <label class="tc-label">Patient TB Id</label><label class="tc-value">${patient.tbId}</label>
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
            <label class="">${patient.providerId}, ${patient.providerMobileNumber}<br/></label><br/>
        </div>
        <div>
            <label class="tc-label">DOT centre</label>
            <label class="tc-value">&nbsp;&nbsp;&nbsp;&nbsp;</label>
        </div>
        <div><label class="tc-label">Signature of MO with date</label>
            <label class="tc-value">&nbsp;&nbsp;&nbsp;</label>
        </div>
    </div>
</div>
<br/>
<div id="test-results-pause-provider-history" class="relative-size">
    <div class="pull-left">
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
                        <td>${treatmentHistory.providerId}</td>
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
    <div class="pull-right">
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
</div>
<div>
    <label class="tc-label">H/O previous Anti-TB treatment with duration</label>
</div>
<div id="treatmentCard">
<#include "../treatmentcard/print.ftl">
</div>
<div class="overflow-hidden center">
    <label class="tc-div tc-label">Treatment outcome with date</label>
    <label class="tc-div tc-label">Signature of MO with date</label>
    <br/>
</div>
<div class="x-ray-and-remarks">
    <div class="x-ray">
        Details of X ray / EP tests
    </div>
    <label class="span2 tc-label">
        Remarks
    </label>
</div>
<div class="actions-for-missed-doses">
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
<div class="chemoprophylaxis">
    <h4 class="inherit-font text-center">Household Contacts </h4>
    <h4 class="inherit-font text-center">(Children &lt; 6 yrs)</h4>
    <table class="table table-bordered sharp fixed text-center line-height-normal">
        <tr>
            <th>No.</th>
            <th>Chemoprophylaxis</th>
        </tr>
        <#list 1..5 as i>
            <tr>
                <td></td>
                <td></td>
            </tr>
        </#list>
    </table>
</div>
<div class="additional-treatments">
    <h4 class="tc-label text-center">Addtional Treatments</h4>

    <div>
        <label class="tc-label">HIV status</label>

        <div class="empty-bordered"></div>
        <label class="tc-value">Unknown</label>

        <div class="empty-bordered"></div>
        <label class="tc-value">Pos</label>

        <div class="empty-bordered"></div>
        <label class="tc-value">Neg</label>
        <label class="tc-value"> (date)</label>
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
        <label class="tc-value">Yes</label>
    </div>
</div>
</div>
</div>
</body>
<script type="text/javascript">
    if(parseInt($('#test-results-pause-provider-history').css('height'))>400) {
        $('#ip-card').addClass('page-break');
        <#if treatmentCard.CPAdherenceSectionValid>
            $('.actions-for-missed-doses').addClass('page-break');
        </#if>
        }
    else  {
        <#if treatmentCard.IPAdherenceSectionValid || treatmentCard.CPAdherenceSectionValid>
            $('#cp-card').addClass('page-break');
        <#else>
            $('.actions-for-missed-doses').addClass('page-break');
        </#if>
    }

    window.print();
</script>
</html>

