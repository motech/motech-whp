<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<#include "../user/activateProvider.ftl">
<#include "../user/resetPassword.ftl">

<@layout.defaultLayout "MoTeCH-WHP">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listProvider.js'/>"></script>

<@paginator.paginate entity="patient">
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Patient Id</th>
                <th>Provider Id</th>
                <th>Mobile Number</th>
                <th>District</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <tr class="odd gradeX" ng-repeat="item in data">
                <td>{{item.patientId}}</td>
                <td>{{item.providerId}}</td>
                <td>{{item.mobileNumber}}</td>
                <td>{{item.district}}</td>
                <td>{{item.status}}</td>
            </tr>
    </table>
</@paginator.paginate>

</@layout.defaultLayout>