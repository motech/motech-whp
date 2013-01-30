<#import "/spring.ftl" as spring />
<#import "../paginator.ftl" as paginator>
<#include "../layout/legend.ftl">
<script type="text/javascript"
        src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>

<script type="text/javascript">
    $(function() {
        $('#achtung').each(function () {
            $(this).tooltip({title: "Patient Nearing Transition!"});
        })
    });
</script>

<@paginator.paginate id = "patient_listing" entity="patient_results" filterSectionId="patient_list_filter" contextRoot="/whp" rowsPerPage="2"  stylePath="/resources-${applicationVersion}/styles">
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
        <tr class="patient-listing" ng-repeat="item in data.results"
            id="PatientRows_{{item.patientId}}"
            containerId="{{item.patientId}}">
            <#--<tr id="patientList_{{item.patientId}}"/>-->
            <#--<tr id="patientList_{{item.patientId}}" class="<#if patient.currentTreatmentPaused>paused</#if> link"-->
                <#--redirect-url="<@spring.url '/patients/show?patientId={{item.patientId}}' />">-->
                <td class="name">{{item.firstName}}  </td>
                <td>{{item.age}}</td>
                <td id="patient_{{item.patientId}}_Gender">{{item.gender}}</td>
                <td class="patientId">{{item.patientId}}</td>
                <td class="tbId">{{item.tbId}}</td>
                <td id="patient_{{item.patientId}}_ProviderId">{{item.currentTreatment.providerId}}</td>
                <td id="patient_{{item.patientId}}_Village">
                {{item.addressVillage}}
                </td>
                <td id="patient_{{item.patientId}}_District">{{item.currentTreatment.providerDistrict}}</td>
                <td id="patient_{{item.patientId}}_TreatmentCategory">
                {{item.treatmentCategoryName}}
                </td>
                <td id="patient_{{item.patientId}}_TherapyCreationDate">
                                {{item.currentTreatmentStartDate}}
                </td>
                <td id="patient_{{item.patientId}}_TreatmentStartDate">
                                {{item.therapyStartDate}}
                </td>
                <td id="patient_{{item.patientId}}_IPProgress">
                {{item.ipProgress}}
                </td>
                <td id="patient_{{item.patientId}}_CPProgress">
                {{item.cpProgress}}
                </td>
                <td id="patient_{{item.patientId}}_MissedDoses">
                {{item.cumulativeMissedDoses}}
                </td>
            </tr>
    </tbody>
</table>
</@paginator.paginate>
<@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>

