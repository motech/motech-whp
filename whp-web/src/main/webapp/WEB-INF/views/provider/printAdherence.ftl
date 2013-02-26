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
                        <th class="smaller-column">Adherence Missing Weeks Since 8 Weeks</th>
                    </tr>
                </thead>
                <tbody>
                <#if providerAdherenceStatuses.pendingAdherenceSummaryList?size == 0>
                <tr class="provider-row">
                    <td class="warning" style="text-align: center" colspan="5">
                        No providers with pending adherence
                    </td>
                </tr>
                <#else>
                    <#list providerAdherenceStatuses.pendingAdherenceSummaryList as providerSummary>
                    <tr class="provider-row adherence-status-row not-reported">
                        <td class="adherenceNotCaptured smallest-column">&#10008;</td>
                        <td class="providerId">
                        ${providerSummary.providerId}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.primaryMobile}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.secondaryMobile!}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.tertiaryMobile!}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.adherenceMissingWeeks}
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
                        <th class="smaller-column">Adherence Missing Weeks Since 8 Weeks</th>
                    </tr>
                </thead>
                <tbody>
                <#if providerAdherenceStatuses.adherenceGivenSummaryList?size == 0>
                <tr class="provider-row">
                    <td class="warning" style="text-align: center" colspan="5">
                        No providers with adherence
                    </td>
                </tr>
                <#else>
                    <#list providerAdherenceStatuses.adherenceGivenSummaryList as providerSummary>
                    <tr class="provider-row adherence-status-row reported">
                        <td class="adherenceCaptured  smallest-column">&#10004;</td>
                        <td class="providerId">
                        ${providerSummary.providerId}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.primaryMobile}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.secondaryMobile!}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.tertiaryMobile!}
                        </td>
                        <td class="smaller-column">
                        ${providerSummary.adherenceMissingWeeks}
                        </td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
<html>