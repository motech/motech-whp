<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<@layout.defaultLayout "MoTeCH-WHP">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>

    <#if message?exists && (message?length>0)>
    <div class="message-alert row text-center alert alert-info fade in">
        <button class="close" data-dismiss="alert">&times;</button>
    ${message}
        <#assign message=""/>
    </div>
    </#if>

<script type="text/javascript">
    createAutoClosingAlert(".message-alert", 5000);
</script>
<div class="well float-right">
    <a href="/whp/cmfAdmin/create">Create CMF Admin</a>
</div>
<table id="cmfAdminList" class="table table-bordered table-condensed" redirectOnRowClick="true">
    <thead>
    <tr>
        <th>Staff Name</th>
        <th>User Name</th>
        <th>Location</th>
    </tr>
    </thead>
    <tbody>
        <#if allCmfAdmins?size == 0>
        <tr>
            <td class="warning" style="text-align: center" colspan="4">
                No registered CMF Admins to show
            </td>
        </tr>
        <#else>
            <#list allCmfAdmins as cmfAdmin>
            <tr id="cmfAdmin_${cmfAdmin.userId}" class="link"
                redirect-url="<@spring.url '/cmfAdmin/edit?userId=${cmfAdmin.userId}' />">
                <td>
                ${cmfAdmin.staffName}
                </td>
                <td>
                ${cmfAdmin.userId}
                </td>
                <td>
                ${cmfAdmin.locationId}
                </td>
            </tr>
            </#list>
        </#if>
    </tbody>
</table>

</@layout.defaultLayout>
