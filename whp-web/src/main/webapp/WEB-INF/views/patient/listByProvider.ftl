<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout title="Patient List" entity="provider">
    <#if message?exists && (message?length>0)>
    <div class="adherence-message-alert row text-center alert alert-info fade in">
        <button class="close" data-dismiss="alert">&times;</button>
    ${message}
        <#assign message=""/>
    </div>
    </#if>

    <div class="pull-left">
        <h1>All Patients </h1>
        <h4>Adherence to be given for: ${weekStartDate} to ${weekEndDate}</h4>
    </div>

    <@legend key1="paused" value1="Current Treatment Paused" />


        <table id="patientList" class="table table-striped table-bordered table-condensed">
            <thead>
            <tr>
                <th>Patient ID</th>
                <th>TB-ID</th>
                <th>Name</th>
                <th>Age</th>
                <th>Gender</th>
                <th>Village</th>
                <th>Treatment Category</th>
                <th>Treatment Start Date</th>
                <th>Adherence Status</th>
                <th>Adherence</th>
            </tr>
            </thead>
            <tbody>
                <#if patientList?size == 0>
                <tr>
                    <td style="text-align: center" colspan="10">No patients to show</td>
                </tr>
                <#else>
                    <#list patientList as patient>
                    <tr id="patientList_${patient.patientId}" class="<#if patient.currentTreatmentPaused>paused</#if>">
                        <td class="patientId"><b>${patient.patientId}</b></td>
                        <td class="tbId">${patient.tbId}</td>
                        <td class="name">${patient.firstName?cap_first} <#if patient.lastName?exists>${patient.lastName?cap_first}</#if></td>
                        <td>${patient.age!}</td>
                        <td id="patient_${patient.patientId}_Gender">${patient.gender}</td>
                        <td id="patient_${patient.patientId}_Village">${patient.addressVillage}</td>
                        <td id="patient_${patient.patientId}_TreatmentCategory">${patient.treatmentCategoryName}</td>
                        <td id="patient_${patient.patientId}_TreatmentStartDate">
                            <#if patient.therapyStartDate?? >
                                ${patient.therapyStartDate}
                            </#if>
                        </td>
                        <#if patient.adherenceCapturedForThisWeek>
                            <td id="patient_${patient.patientId}_AdherenceTaken" class="adherenceCaptured">
                                &#10004;
                            </td>
                        <#else>
                            <td id="patient_${patient.patientId}_AdherenceNotTaken" class="adherenceNotCaptured">
                                &#10008;
                            </td>
                        </#if>
                        <td class="updateAdherenceLink">
                            <#if !patient.currentTreatmentClosed>
                                <a href="<@spring.url '/adherence/update/${patient.patientId}' />">Edit</a>
                            </#if>
                        </td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>

<script type="text/javascript">
    createAutoClosingAlert(".adherence-message-alert", 5000)
</script>
</@layout.defaultLayout>
