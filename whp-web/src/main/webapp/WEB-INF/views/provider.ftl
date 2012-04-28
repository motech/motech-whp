<#import "layout/default.ftl" as layout>
<@layout.defaultLayout "MoTeCH-TB">
<div class="row" id="welcomeMessage">

    <table class="table">
        <thead>
        <tr>
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
        </tr>
        </thead>
        <tbody>
            <#list patientList as patient>
            <tr>
                <td>${patient.firstName}</td>
                <td>${patient.lastName}</td>
                <td>${patient.currentProvidedTreatment.treatment.patientAge}</td>
                <td>${patient.gender}</td>
                <td>${patient.currentProvidedTreatment.patientAddress.district}</td>
                <td>${patient.currentProvidedTreatment.treatment.treatmentCategory}</td>
                <td>${patient.currentProvidedTreatment.treatment.startDateAsDate}</td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</@layout.defaultLayout>
