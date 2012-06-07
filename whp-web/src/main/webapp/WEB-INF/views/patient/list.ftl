<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout "Patient List">

<div class="pull-left">
<ul class="nav nav-list leftnav-menu-margin pull-left">
    <li class="nav-header">
        <b>Patient List</b>
    </li>
    <li>
        <a id="show-patients" href="/whp/patients/all">Show all patients</a>
    </li>
    <li class="divider"></li>
    <li class="nav-header">
        <b>Downloads</b>
    </li>
    <li>
    <a href="/whp/reports/adherence/adherenceReport.xls">Download all adherence data to excel</a>
    <li>
    <li class="divider"></li>
</ul>
</div>
<div class="pull-right">
    <@legend key1="paused" value1="Current Treatment Paused" span="span13"/>
    <div class="row">
        <div>
            <table id="patientList" class="table table-bordered table-condensed">
                <thead>
                <tr>
                    <th>Patient ID</th>
                    <th>TB-ID</th>
                    <th>Name</th>
                    <th>Age</th>
                    <th>Gender</th>
                    <th>Village</th>
                    <th>District</th>
                    <th>Treatment Category</th>
                    <th>Patient Type</th>
                    <th>Treatment Start Date</th>
                    <th>Adherence</th>
                </tr>
                </thead>
                <tbody>
                    <#if patientList?size == 0>
                    <tr>
                        <td style="text-align: center" colspan="11">No patients to show</td>
                    </tr>
                    <#else>
                        <#list patientList as patient>
                        <tr id="patientList_${patient.patientId}" class="<#if patient.currentTreatmentPaused>paused</#if>">
                            <td class="patientId"><b>${patient.patientId}</b></td>
                            <td class="tbId">${patient.currentTreatment.tbId}</td>
                            <td class="name">${patient.firstName?cap_first}
                                <#if patient.lastName?exists>
                                    ${patient.lastName?cap_first}
                                </#if></td>
                            <td>${patient.currentTreatment.therapy.patientAge!}</td>
                            <td id="patient_${patient.patientId}_Gender">${patient.gender}</td>
                            <td id="patient_${patient.patientId}_Village">${patient.currentTreatment.patientAddress.address_village}</td>
                            <td id="patient_${patient.patientId}_District">${patient.currentTreatment.patientAddress.address_district}</td>
                            <td id="patient_${patient.patientId}_TreatmentCategory">${patient.currentTreatment.therapy.treatmentCategory.name}</td>
                            <td id="patient_${patient.patientId}_PatientType">
                                <#if patient.currentTreatment?? >
                                    ${patient.currentTreatment.patientType }
                                </#if>
                            </td>
                            <td id="patient_${patient.patientId}_TreatmentStartDate">
                                <#if patient.currentTreatment.therapy.startDate?? >
                                    ${patient.currentTreatment.therapy.startDate?date?string("dd/mm/yyyy") }
                                </#if>
                            </td>
                            <td class="updateAdherenceLink">
                                <#if !patient.currentTreatmentClosed>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>

</@layout.defaultLayout>
