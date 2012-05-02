<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Patient List">
<div class="row">

    <h3 class="well">Patients</h3>
    <div >
    <table class="table table-striped">
        <thead>
        <tr>
            <th>
                Patient ID
            </th>
            <th>
                TB-ID
            </th>
            <th>
                First Name
            </th>
            <th>
                Last Name
            </th>
            <th>
                Age
            </th>
            <th>
                Gender
            </th>
            <th>
                District
            </th>
            <th>
                Treatment Category
            </th>
            <th>
                Treatment Startdate
            </th>
            <th>
                Adherence
            </th>
        </tr>
        </thead>
        <tbody>
            <#if patientList?size == 0>
            <tr>
                <td  style="text-align: center" colspan="9">No patients to show</td>
            </tr>
            <#else>
                <#list patientList as patient>
                <tr>
                    <td>${patient.patientId}</td>
                    <td>${patient.currentProvidedTreatment.tbId}</td>
                    <td class="name">${patient.firstName}</td>
                    <td>${patient.lastName}</td>
                    <td>${patient.currentProvidedTreatment.treatment.patientAge}</td>
                    <td>${patient.gender}</td>
                    <td>${patient.currentProvidedTreatment.patientAddress.district}</td>
                    <td>${patient.currentProvidedTreatment.treatment.treatmentCategory.name}</td>
                    <td>${patient.currentProvidedTreatment.treatment.startDateAsDate}</td>
                    <td><a href="<@spring.url '/adherence/update/${patient.patientId}' />">Edit</a></td>
                </tr>
                </#list>
            </#if>

        </tbody>
    </table>
</div>
    </div>
</@layout.defaultLayout>
