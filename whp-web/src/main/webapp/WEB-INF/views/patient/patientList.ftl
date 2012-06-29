<#import "/spring.ftl" as spring />
<#include "../layout/legend.ftl">
<table id="patientList" class="table table-bordered table-condensed" redirectOnRowClick="true">
    <thead>
    <tr>
        <th>Patient ID</th>
        <th>TB-ID</th>
        <th>Name</th>
        <th>Age</th>
        <th>Gender</th>
        <th>Provider ID</th>
        <th>Village</th>
        <th>District</th>
        <th>Treatment Category</th>
        <th>Treatment Start Date</th>
    </tr>
    </thead>
    <tbody>
        <#if patientList?size == 0>
        <tr>
            <td class="text-center" colspan="10">No patients to show</td>
        </tr>
        <#else>
            <#list patientList as patient>
            <tr id="patientList_${patient.patientId}" class="link"
                redirect-url="<@spring.url '/patients/show?patientId=${patient.patientId}' />"
                class="<#if patient.currentTreatmentPaused>paused</#if>">
                <td class="patientId"><b>${patient.patientId}</b></td>
                <td class="tbId">${patient.currentTreatment.tbId}</td>
                <td class="name">${patient.firstName?cap_first}
                                <#if patient.lastName?exists>
                ${patient.lastName?cap_first}
                </#if></td>
                <td>${patient.currentTreatment.therapy.patientAge!}</td>
                <td id="patient_${patient.patientId}_Gender">${patient.gender}</td>
                <td id="patient_${patient.patientId}_ProviderId">${patient.currentTreatment.providerId}</td>
                <td id="patient_${patient.patientId}_Village">${patient.currentTreatment.patientAddress.address_village}</td>
                <td id="patient_${patient.patientId}_District">${patient.currentTreatment.patientAddress.address_district}</td>
                <td id="patient_${patient.patientId}_TreatmentCategory">${patient.currentTreatment.therapy.treatmentCategory.name}</td>
                <td id="patient_${patient.patientId}_TreatmentStartDate">
                    <#if patient.currentTreatment.therapy.startDate?? >
                                    ${patient.currentTreatment.therapy.startDateAsString}
                            </#if>
                </td>
            </tr>
            </#list>
        </#if>
    </tbody>
</table>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/redirctOnRowClick.js'/>"></script>
