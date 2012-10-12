<#import "/spring.ftl" as spring />
<#import "../layout/default-with-sidebar.ftl" as layout>
<#import "../paginator.ftl" as paginator>

<@layout.defaultLayout title="MoTeCH-WHP" entity="cmfadmin">
    <#if !pageNo??>
        <#assign pageNo=1/>
    </#if>
<#include "reasonForClosure.ftl"/>

<div class="results">

    <@paginator.paginate id="sputum_tracking_pagination" filterSectionId="" entity="container_tracking_dashboard_row" rowsPerPage="20" contextRoot="/whp" stylePath="/resources-${applicationVersion}/styles">
        <table class="table table-striped table-bordered" id="sputumTrackingDashboardRowsList">
            <thead>
            <tr>
                <th>Container Id</th>
                <th>Container Issued On</th>
                <th>Lab Name</th>
                <th>Result 1</th>
                <th>Date Of Test 1</th>
                <th>Result 2</th>
                <th>Date Of Test 2</th>
                <th>Consulted On</th>
                <th>Diagnosis</th>
                <th>Patient ID</th>
                <th>Provider District</th>
                <th>Provider Id</th>
                <th>Container Status</th>
                <th>Action</th>
                <th>Reason for Closure</th>
            </tr>
            </thead>
            <tbody>
            <tr class="sputum-tracking-dashboard-row" ng-repeat="item in data.results"
                id="dashboardRows_{{item.containerId}}"
                containerId="{{item.containerId}}">
                <td>{{item.containerId}}</td>
                <td>{{item.containerIssuedOn}}</td>
                <td>{{item.labName}}</td>
                <td>{{item.consultationOneResult}}</td>
                <td>{{item.consultationOneDate}}</td>
                <td>{{item.consultationTwoResult}}</td>
                <td>{{item.consultationTwoDate}}</td>
                <td>{{item.consultation}}</td>
                <td id="diagnosis">{{item.diagnosis}}</td>
                <td><a href="<@spring.url '/patients/show?patientId={{item.patientId}}' />">{{item.patientId}}</a></td>
                <td>{{item.district}}</td>
                <td>{{item.providerId}}</td>
                <td>{{item.containerStatus}}</td>
                <td><a id="setReasonForClosure" class="setReasonForClosure" data-toggle="modal" href="#setReason">{{item.action}}</a></td>
                <td>{{item.reasonForClosure}}</td>
            </tr>
            <tr type="no-results" class="hide">
                <td class="warning text-center" colspan="15"></td>
            </tr>
            </tbody>


        </table>
    </@paginator.paginate>
    <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listSputumTrackingDashboardRows.js'/>"></script>

</@layout.defaultLayout>