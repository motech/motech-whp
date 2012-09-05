<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<#include "../user/activateProvider.ftl">
<#include "../user/resetPassword.ftl">

<@layout.defaultLayout "MoTeCH-WHP">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listProvider.js'/>"></script>

    <@paginator.filter paginationControl="provider_pagination" >
        <input name="filterTextBox" type="text"/>
    </@paginator.filter>

    <@paginator.paginate id="provider_pagination" entity="provider" rowsPerPage="1" contextRoot="/whp" stylePath="/resources-${applicationVersion}/styles">
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

    <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>

</@layout.defaultLayout>