<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>

<@layout.defaultLayout title="MoTeCH-WHP" entity="fieldStaff">
    <table class="table table-striped table-bordered" id="providerList">
        <thead>
            <tr>
                <th>Provider Id</th>
                <th>Primary Mobile Number</th>
                <th>Secondary Mobile Number</th>
                <th>Tertiary Mobile Number</th>
            </tr>
        </thead>
        <tbody>
            <#list providerList as provider>
                <tr class="provider-row">
                    <td class="providerId">${provider.providerId}</td>
                    <td>
                        ${provider.primaryMobile}
                    </td>
                    <td>
                        <#if provider.secondaryMobile?exists> ${provider.secondaryMobile}</#if>
                    </td>
                    <td>
                        <#if provider.tertiaryMobile?exists> ${provider.tertiaryMobile}</#if>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
</@layout.defaultLayout>