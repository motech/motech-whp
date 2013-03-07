<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>

<@layout.defaultLayout title="MoTeCH-WHP">
<div class="patients-buttons-container">
    <div class="row-fluid">
        <div class="controls pull-right">
            <a target="_blank" class="btn btn-large btn-info" href="<@spring.url '/providers/adherenceStatus/print/'/>"><i
                    class="icon-print icon-white"></i> Print</a>
        </div>
    </div>
</div>
<div class="row-fluid">
    <h3 class="offset4">
        <div class="offset1">From ${providedAdherenceFrom} to ${providedAdherenceTo}</div>
    </h3>
</div>
<div class="row-fluid adherence-report">
    <div class="span6 offset3">
        <h4 class="pull-left">Pending adherence</h4>
        <table class="adherence-status-table table table-striped table-condensed table-condensed table-bordered">
            <thead>
            <tr>
                <th class="smallest-column">Status</th>
                <th class="providerId">Provider Id</th>
                <th class="smaller-column">Mobile Number 1</th>
                <th class="smaller-column">Mobile Number 2</th>
                <th class="smaller-column">Mobile Number 3</th>
                <th class="smaller-column">Number of unreported adherence weeks by provider (since 8 weeks)</th>
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
    <div class="span6 offset3">
        <h4 class="pull-left">Reported adherence</h4>
        <table class="adherence-status-table table table-striped table-condensed table-bordered">
            <thead>
            <tr>
                <th class="smallest-column">Status</th>
                <th class="providerId">Provider Id</th>
                <th class="smaller-column">Mobile Number 1</th>
                <th class="smaller-column">Mobile Number 2</th>
                <th class="smaller-column">Mobile Number 3</th>
                <th class="smaller-column">Number of unreported adherence weeks by provider (since 8 weeks)</th>
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
</@layout.defaultLayout>