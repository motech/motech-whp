<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Update Adherence">
<div class="row">

    <table class="table">
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
            <td><a href="<@spring.url '/adherence/${patient.patientId}' />">Edit</a></td>
        </tr>
        </tbody>
    </table>
</div>
</@layout.defaultLayout>
