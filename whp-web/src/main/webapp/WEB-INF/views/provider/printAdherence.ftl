<#import "/spring.ftl" as spring />
<html>
<head>
    <title>Adherence Status</title>
    <#include "../layout/scripts.ftl"/>
</head>
<body class="printable-version">
        <h3>
            From ${providedAdherenceFrom} to ${providedAdherenceTo}
        </h3>

    <div class="row-fluid adherence-report">
        <div class="span6">
            <h4 class="pull-left">Pending adherence</h4>
            <table class="printable-table">
                <thead>
                    <tr>
                        <th class="smallest-column">Status</th>
                        <th class="providerId">Provider Id</th>
                        <th class="smaller-column">Mobile Number 1</th>
                        <th class="smaller-column">Mobile Number 2</th>
                        <th class="smaller-column">Mobile Number 3</th>
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
                                <td  class="smallest-column">&#10008;</td>
                                <td  class="providerId">
                                    ${provider.providerId}
                                </td>
                                <td class="smaller-column">
                                    ${provider.primaryMobile}
                                </td>
                                <td class="smaller-column">
                                    <#if provider.secondaryMobile?exists> ${provider.secondaryMobile}</#if>
                                </td>
                                <td class="smaller-column">
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
        <div class="span6">
            <h4 class="pull-left">Reported adherence</h4>
            <table class="printable-table">
                <thead>
                    <tr>
                        <th class="smallest-column">Status</th>
                        <th class="providerId">Provider Id</th>
                        <th class="smaller-column">Mobile Number 1</th>
                        <th class="smaller-column">Mobile Number 2</th>
                        <th class="smaller-column">Mobile Number 3</th>
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
                                <td class="smallest-column">&#10004;</td>
                                <td class="providerId">
                                    ${provider.providerId}
                                </td>
                                <td class="smaller-column">
                                    ${provider.primaryMobile}
                                </td>
                                <td class="smaller-column">
                                    <#if provider.secondaryMobile?exists> ${provider.secondaryMobile}</#if>
                                </td>
                                <td class="smaller-column">
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