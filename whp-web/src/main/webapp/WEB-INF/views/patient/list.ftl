<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
    <@layout.defaultLayout "Patient List">
    <div class="row">
        <h3 class="well">Patients</h3>
        <div>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Patient ID</th>
                        <th>TB-ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Age</th>
                        <th>Gender</th>
                        <th>District</th>
                        <th>Treatment Category</th>
                        <th>Treatment Start Date</th>
                        <th>Adherence</th>
                        <th>Treatment Outcome</th>
                    </tr>
                </thead>
                <tbody>
                    <#if patientList?size == 0>
                        <tr>
                            <td style="text-align: center" colspan="11">No patients to show</td>
                        </tr>
                    <#else>
                        <#list patientList as patient>
                            <tr id="patientList_${patient.patientId}">
                                <td class="patientId">${patient.patientId}</td>
                                <td class="tbId">${patient.currentProvidedTreatment.tbId}</td>
                                <td class="name">${patient.firstName}</td>
                                <td>${patient.lastName}</td>
                                <td>${patient.currentProvidedTreatment.treatment.patientAge!}</td>
                                <td>${patient.gender}</td>
                                <td>${patient.currentProvidedTreatment.patientAddress.address_district}</td>
                                <td id="patient_${patient.patientId}_TreatmentCategory">${patient.currentProvidedTreatment.treatment.treatmentCategory.name}</td>
                                <td>
                                    <#if patient.currentProvidedTreatment.treatment.doseStartDate?? >
                                        ${patient.currentProvidedTreatment.treatment.doseStartDate?date?string("dd/mm/yyyy") }
                                    </#if>
                                </td>
                                <td class="updateAdherenceLink">
                                    <#if !patient.currentTreatmentClosed>
                                        <a href="<@spring.url '/adherence/update/${patient.patientId}' />">Edit</a>
                                    </#if>
                                </td>
                                <td>
                                    <#if patient.currentTreatmentClosed>
                                        <div id="patient_${patient.patientId}_TreatmentOutcome" class="label label-info text-center">${patient.treatmentOutcome.outcome}</div>
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
