<#import "/spring.ftl" as spring />
<#include "../layout/legend.ftl">
    <table id="notaBena" class="table table-condensed pull-left thin-table no-top-border">
        <thead>
        <tr class="taller-row">
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#if patientList?size == 0>
            <tr class="taller-row">
                <td></td>
            </tr>
            <#else>
                <#list patientList as patient>
                    <#if patient.nearingPhaseTransition || patient.transitioning>
                        <tr class="taller-row">
                            <td style="float:right">
                                <img id="achtung" class="pull-left"
                                     src="<@spring.url '/resources-${applicationVersion}/img/smallwarning.png'/>"/>
                            </td>
                        </tr>
                        <#else>
                            <tr class="taller-row">
                                <td>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                            </tr>
                    </#if>
                </#list>
        </#if>
        </tbody>
    </table>

    <table id="patientList" class="table table-bordered table-condensed fat-table" redirectOnRowClick="true">
        <thead>
        <tr>
            <th>Patient ID</th>
            <th>TB-ID</th>
            <th>Name</th>
            <th>Age</th>
            <th>Gender</th>
            <th>Provider ID</th>
            <th>Village</th>
            <th>Provider District</th>
            <th>Treatment Category</th>
            <th>Treatment Start Date</th>
            <th>Weeks Elapsed</th>
        </tr>
        </thead>
        <tbody>
        <#if patientList?size == 0>
            <tr>
                <td class="warning" style="text-align: center" colspan="10">
                    <#if providerId != "">
                        No patients found for Provider District as: '${selectedDistrict}' and Provider ID as:
                        '${providerId}'
                        <#else>
                            No patients found for Provider District as: '${selectedDistrict}'
                    </#if>
                </td>
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
                            </#if>
                        </td>
                        <td>${patient.currentTreatment.therapy.patientAge!}</td>
                        <td id="patient_${patient.patientId}_Gender">${patient.gender}</td>
                        <td id="patient_${patient.patientId}_ProviderId">${patient.currentTreatment.providerId}</td>
                        <td id="patient_${patient.patientId}_Village">
                            ${patient.currentTreatment.patientAddress.address_village}
                        </td>
                        <td id="patient_${patient.patientId}_District">${selectedDistrict}</td>
                        <td id="patient_${patient.patientId}_TreatmentCategory">
                            ${patient.currentTreatment.therapy.treatmentCategory.name}
                        </td>
                        <td id="patient_${patient.patientId}_TreatmentStartDate">
                            <#if patient.currentTreatment.therapy.startDate?? >
                                ${patient.currentTreatment.therapy.startDateAsString}
                            </#if>
                        </td>
                        <td id="patient_${patient.patientId}_WeeksElapsed">
                            <#if patient.weeksElapsed?? >
                                ${patient.weeksElapsed}
                            <#else>
                                -
                            </#if>
                        </td>
                    </tr>
                </#list>
        </#if>
        </tbody>
    </table>
    <script type="text/javascript"
            src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>
