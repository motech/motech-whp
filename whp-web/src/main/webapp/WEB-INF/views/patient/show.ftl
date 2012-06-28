<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout/>
<#include "../user/phaseInfo.ftl"/>
<@layout.defaultLayout "Patient Dashboard">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/injectHtml.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/treatmentCard.js'/>"></script>

    <#if messages?exists && (messages?size>0)>
    <div class="dateUpdated-message-alert row alert alert-info fade in">
        <button class="close" data-dismiss="alert">&times;</button>
        <#list messages as message>
        ${message}
            <#break/>
        </#list>
        <table id="messageList" class="table table-bordered table-condensed sharp">
            <thead>
            <tr>
                <th>Phase Information</th>
            </tr>
            <tbody>
                <#list messages as message>
                    <#if message_index != 0>
                    <tr>
                        <td>${message}</td>
                        <br/>
                    </tr>
                    </#if>
                    <#assign message=""/>
                </#list>
            </tbody>
        </table>
    </div>
    </#if>
<div class="well">
    <a id="setDateLink" data-toggle="modal" href="#setDatesModal" class="brand pull-left">Adjust Phase Start Dates</a>

    <div class="float-right">Patient Id : ${patient.patientId}</div>
</div>
    <@phaseInfo/>
<div class="patient-info">
    <div class="pull-left">

        <table class="table table-bordered default-arrow">
            <tr>
                <td>State</td>
                <td>${patient.currentTreatment.patientAddress.address_state}</td>
            </tr>
            <tr>
                <td>Patient ID</td>
                <td>${patient.patientId}</td>
            </tr>
            <tr>
                <td>TB ID</td>
                <td>${patient.currentTreatment.tbId}</td>
            </tr>

            <tr>
                <td>TB Registration Number</td>
                <td>${patient.currentTreatment.tbRegistrationNumber!}</td>
            </tr>

            <tr>
                <td>PHI</td>
                <td>${patient.phi!}</td>
            </tr>

            <tr>
                <td>Provider ID</td>
                <td>${patient.currentTreatment.providerId}</td>
            </tr>

            <tr>
                <td>Provider Name</td>
                <td>${patient.currentTreatment.providerId}</td>
            </tr>

            <tr>
                <td>Provider Contact No.</td>
                <td>${provider.primaryMobile}</td>
            </tr>

            <tr>
                <td>Patient Name</td>
                <td>${patient.firstName} ${patient.lastName!}</td>
            </tr>

            <tr>
                <td>Gender</td>
                <td>${patient.gender}</td>
            </tr>

            <tr>
                <td>Age</td>
                <td>${patient.currentTreatment.therapy.patientAge}</td>
            </tr>

            <tr>
                <td>Patient Address: Location</td>
                <td>${patient.currentTreatment.patientAddress.address_location}</td>
            </tr>

            <tr>
                <td>Patient Address: Landmark</td>
                <td>${patient.currentTreatment.patientAddress.address_landmark!}</td>
            </tr>

            <tr>
                <td>Patient Address: Village</td>
                <td>${patient.currentTreatment.patientAddress.address_village}</td>
            </tr>

            <tr>
                <td>Patient Address: Block</td>
                <td>${patient.currentTreatment.patientAddress.address_block}</td>
            </tr>

            <tr>
                <td>Patient Address: District</td>
                <td>${patient.currentTreatment.patientAddress.address_district}</td>
            </tr>

            <tr>
                <td>Patient Address: State</td>
                <td>${patient.currentTreatment.patientAddress.address_state}</td>
            </tr>

            <tr>
                <td>Patient Mobile Number</td>
                <td>${patient.phoneNumber}</td>
            </tr>

            <tr>
                <td>Disease Classification</td>
                <td>${patient.currentTreatment.therapy.diseaseClass}</td>
            </tr>

            <tr>
                <td>Type of patient</td>
                <td>${patient.currentTreatment.patientType}</td>
            </tr>

            <tr>
                <td>Treatment Category</td>
                <td>${patient.currentTreatment.therapy.treatmentCategory.name}</td>
            </tr>

            <tr>
                <td>Treatment Start Date</td>
                <td>${patient.currentTreatment.startDate.toString("dd/MM/yyyy")}</td>
            </tr>

        </table>
    </div>
    <div class="pull-right">
        <table class="table table-bordered text-center">
            <tr>
                <th rowspan="2">Sample Instance</th>
                <th colspan="4">Smear Test Results</th>
            </tr>
            <tr>
                <td>Date 1</td>
                <td>Result 1</td>
                <td>Date 2</td>
                <td>Result 2</td>
            </tr>
            <#if patient.currentTreatment.smearTestResults?exists>
                <#list patient.currentTreatment.smearTestResults.getAll() as smearTestRecord>
                    <tr>
                        <td>${smearTestRecord.smear_sample_instance}</td>
                        <td>${smearTestRecord.smear_test_date_1.toString("dd/MM/yyyy")}</td>
                        <td>${smearTestRecord.smear_test_result_1}</td>
                        <td>${smearTestRecord.smear_test_date_2.toString("dd/MM/yyyy")}</td>
                        <td>${smearTestRecord.smear_test_result_2}</td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>
<br/>
<div id="treatmentCard"/>
<script type="text/javascript">
    $('#saveTheDate').click(function () {
        if ($('#ipDatePicker').val() === "" && $('#eipDatePicker').val() === "" && $('#cpDatePicker').val() === "") {
            event.preventDefault();
            jConfirm('All phase start dates are set to empty. Do you want to continue?', 'Warning', function (r) {
                if (r === true) {
                    $('#setDatesModal').submit();
                }
            });
        }
    });
    $('#ipDatePicker').datepicker({dateFormat:'dd/mm/yy'});
    $('#eipDatePicker').datepicker({dateFormat:'dd/mm/yy'});
    $('#cpDatePicker').datepicker({dateFormat:'dd/mm/yy'});
    $("#clearFields").click(function () {
        $('#ipDatePicker').val('');
        $('#eipDatePicker').val('');
        $('#cpDatePicker').val('');
        event.preventDefault();
    });
    createAutoClosingAlert(".dateUpdated-message-alert", 5000)
    $('#treatmentCard').load('/whp/treatmentcard/show?patientId=${patient.patientId}');
</script>
</@layout.defaultLayout>
