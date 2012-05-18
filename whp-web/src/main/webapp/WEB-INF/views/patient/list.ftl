<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<#include "../layout/legend.ftl">
    <@layout.defaultLayout "Patient List">
    <@legend key1="paused" value1="Current Treatment Paused" />
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
                        <th>District</th>
                        <th>Treatment Category</th>
                        <th>Treatment Start Date</th>
                        <th>Adherence</th>
                    </tr>
                </thead>
                <tbody>
                    <#if patientList?size == 0>
                        <tr>
                            <td style="text-align: center" colspan="9">No patients to show</td>
                        </tr>
                    <#else>
                        <#list patientList as patient>
                            <tr id="patientList_${patient.patientId}" class="<#if patient.currentTreatmentPaused>paused</#if>">
                                <td class="patientId"><b>${patient.patientId}</b></td>
                                <td class="tbId">${patient.currentProvidedTreatment.tbId}</td>
                                <td class="name">${patient.firstName?cap_first} ${patient.lastName?cap_first}</td>
                                <td>${patient.currentProvidedTreatment.treatment.patientAge!}</td>
                                <td id="patient_${patient.patientId}_Gender">${patient.gender}</td>
                                <td>${patient.currentProvidedTreatment.patientAddress.address_district}</td>
                                <td id="patient_${patient.patientId}_TreatmentCategory">${patient.currentProvidedTreatment.treatment.treatmentCategory.name}</td>
                                <td>
                                    <#if patient.currentProvidedTreatment.treatment.startDate?? >
                                        ${patient.currentProvidedTreatment.treatment.startDate?date?string("dd/mm/yyyy") }
                                    </#if>
                                </td>
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
        </div>
    </div>
</@layout.defaultLayout>
