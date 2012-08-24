<#import "/spring.ftl" as spring />
<#include "../layout/legend.ftl">
<script type="text/javascript"
        src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>
<script type="text/javascript"
        src="<@spring.url '/resources-${applicationVersion}/js/bootstrap/bootstrap-tooltip.js'/>"></script>
<script type="text/javascript">
    $(function() {
        $('#achtung').each(function () {
            $(this).tooltip({title: "Patient Nearing Transition!"});
        })
    });
</script>
<table id="patientList" class="table table-striped table-bordered table-condensed" redirectOnRowClick="true">
    <thead>
    <tr>
        <th>Name</th>
        <th>Age</th>
        <th>Gender</th>
        <th>Patient ID</th>
        <th>TB-ID</th>
        <th>Provider ID</th>
        <th>Village</th>
        <th>Provider District</th>
        <th>Treatment Category</th>
        <th>TB Registration Date</th>
        <th>Treatment Start Date</th>
        <th>IP Treatment Progress</th>
        <th>CP Treatment Progress</th>
        <th>Cumulative Missed Doses &#42;</th>
    </tr>
    </thead>
    <tbody>
    <#if patientList?size == 0>
    <tr>
        <td class="warning" style="text-align: center" colspan="14">
            <#if selectedProvider?? && selectedProvider != "">
                No patients found for Provider District as: '${selectedDistrict}' and Provider ID as:
                '${selectedProvider}'
                <#else>
                    No patients found for Provider District as: '${selectedDistrict}'
            </#if>
        </td>
    </tr>
        <#else>
            <#list patientList as patient>
            <tr id="patientList_${patient.patientId}" class="<#if patient.currentTreatmentPaused>paused</#if> link"
                redirect-url="<@spring.url '/patients/show?patientId=${patient.patientId}' />">
                <td class="name">${patient.firstName?cap_first}
                                         <#if patient.lastName?exists>
                ${patient.lastName?cap_first}
                </#if>
                </td>
                <td>${patient.currentTherapy.patientAge!}</td>
                <td id="patient_${patient.patientId}_Gender">${patient.gender}</td>
                <td class="patientId">
                    <#if (patient.nearingPhaseTransition || patient.transitioning) && !patient.orHasBeenOnCp>
                        <img id="achtung" class="pull-left"
                             src="<@spring.url '/resources-${applicationVersion}/img/smallwarning.png'/>"/>
                    </#if>
                    <b>${patient.patientId}</b>
                </td>
                <td class="tbId">${patient.currentTreatment.tbId}</td>
                <td id="patient_${patient.patientId}_ProviderId">${patient.currentTreatment.providerId}</td>
                <td id="patient_${patient.patientId}_Village">
                ${patient.currentTreatment.patientAddress.address_village}
                </td>
                <td id="patient_${patient.patientId}_District">${selectedDistrict}</td>
                <td id="patient_${patient.patientId}_TreatmentCategory">
                ${patient.currentTherapy.treatmentCategory.name}
                </td>
                <td id="patient_${patient.patientId}_TherapyCreationDate">
                    <#if patient.currentTherapy.creationDate?? >
                                ${patient.currentTherapy.creationDateAsString}
                            </#if>
                </td>
                <td id="patient_${patient.patientId}_TreatmentStartDate">
                    <#if patient.currentTherapy.startDate?? >
                                ${patient.currentTherapy.startDateAsString}
                            </#if>
                </td>
                <td id="patient_${patient.patientId}_IPProgress">
                ${patient.IPProgress}
                </td>
                <td id="patient_${patient.patientId}_CPProgress">
                ${patient.CPProgress}
                </td>
                <td id="patient_${patient.patientId}_MissedDoses">
                ${patient.cumulativeDosesNotTaken}
                </td>
            </tr>
            </#list>
    </#if>
    </tbody>
</table>
<i>&#42; Cumulative missed doses shown as of ${lastSunday}</i>
