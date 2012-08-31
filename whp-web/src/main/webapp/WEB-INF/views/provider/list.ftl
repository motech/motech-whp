<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<#include "../user/activateProvider.ftl">
<#include "../user/resetPassword.ftl">

<@layout.defaultLayout "MoTeCH-WHP">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listProvider.js'/>"></script>

<@paginator.paginate entity="provider">
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Provider Id</th>
                <th>District</th>
                <th>Primary Mobile Number</th>
                <th>Secondary Mobile Number</th>
                <th>Tertiary Mobile Number</th>
                <th>Status</th>
                <th></th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <tr class="odd gradeX" ng-repeat="item in data.results">
                <td>{{item.providerId}}</td>
                <td>{{item.district}}</td>
                <td>{{item.primaryMobile}}</td>
                <td>{{item.primaryMobile}}</td>
                <td>{{item.primaryMobile}}</td>
                <td>{{item.status}}</td>
                <td></td>
                <td></td>
            </tr>
    </table>
</@paginator.paginate>

</@layout.defaultLayout>