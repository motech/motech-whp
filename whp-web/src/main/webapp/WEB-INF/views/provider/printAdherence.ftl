<#import "/spring.ftl" as spring />
<html>
<head>
    <title>Adherence Status</title>
    <#include "../layout/scripts.ftl"/>
</head>
<body class="printable-version">
        <h3>
            <center>From ${providedAdherenceFrom} to ${providedAdherenceTo}</center>
        </h3>

    <div class="row-fluid adherence-report">
        <div class="span6 offset3">
            <h4 class="pull-left">Pending adherence</h4>
            <table class="printable-table">
                <thead>
                    <tr>
                        <th>Status</th>
                        <th>Provider Id</th>
                        <th>Mobile Number 1</th>
                        <th>Mobile Number 2</th>
                        <th>Mobile Number 3</th>
                    </tr>
                </thead>
                <tbody>
                    <#if providersPendingAdherence?size == 0>
                        <tr>
                            <td style="text-align: center" colspan="5">
                                No providers with pending adherence
                            </td>
                        </tr>
                    <#else>
                        <#list providersPendingAdherence as provider>
                            <tr>
                                <td>&#10008;</td>
                                <td>
                                    ${provider.providerId}
                                </td>
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
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row-fluid  adherence-report">
        <div class="span6 offset3">
            <h4 class="pull-left">Reported adherence</h4>
            <table class="printable-table">
                <thead>
                    <tr>
                        <th>Status</th>
                        <th>Provider Id</th>
                        <th>Mobile Number 1</th>
                        <th>Mobile Number 2</th>
                        <th>Mobile Number 3</th>
                    </tr>
                </thead>
                <tbody>
                    <#if providersWithAdherence?size == 0>
                        <tr>
                            <td style="text-align: center" colspan="5">
                                No providers with adherence
                            </td>
                        </tr>
                    <#else>
                        <#list providersWithAdherence as provider>
                            <tr>
                                <td>&#10004;</td>
                                <td>
                                    ${provider.providerId}
                                </td>
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
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
<html>