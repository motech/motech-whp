<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>

<@layout.defaultLayout title="MoTeCH-WHP">
        <div class="row-fluid">
            <h3><center>From ${providedAdherenceFrom}</strong> to <strong>${providedAdherenceTo}</center></h3>
        </div>
        <div class="row-fluid">
            <div class="span10 offset1">
                <h4 class="pull-left">Pending adherence</h4>
                <table class="table table-striped table-condensed" id="providerList">
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th>Provider Id</th>
                            <th>Primary Mobile Number</th>
                            <th>Secondary Mobile Number</th>
                            <th>Tertiary Mobile Number</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#if providersPendingAdherence?size == 0>
                            <tr class="provider-row">
                                <td class="warning" style="text-align: center" colspan="5">
                                    No providers with pending adherence
                                </td>
                            </tr>
                        <#else>
                            <#list providersPendingAdherence as provider>
                                <tr class="provider-row adherence-status-row not-reported">
                                    <td class="adherenceNotCaptured">&#10008;</td>
                                    <td class="providerId">
                                        <strong>${provider.providerId}</strong>
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
        <div class="row-fluid">
            <div class="span10 offset1">
                <h4 class="pull-left">Reported adherence</h4>
                <table class="table table-striped table-condensed" id="providerList">
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th>Provider Id</th>
                            <th>Primary Mobile Number</th>
                            <th>Secondary Mobile Number</th>
                            <th>Tertiary Mobile Number</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#if providersWithAdherence?size == 0>
                            <tr class="provider-row">
                                <td class="warning" style="text-align: center" colspan="5">
                                    No providers with adherence
                                </td>
                            </tr>
                        <#else>
                            <#list providersWithAdherence as provider>
                                <tr class="provider-row  adherence-status-row reported">
                                    <td class="adherenceCaptured">&#10004;</td>
                                    <td class="providerId">
                                        <strong>${provider.providerId}</strong>
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
</@layout.defaultLayout>