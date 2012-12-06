<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>

<@layout.defaultLayout title="MoTeCH-WHP">
        <div class="row-fluid">
              <h4 class="pull-left">Pending adherence</h4>
              <div class="pull-right beside-header">
                  Adherence status from <strong>${providedAdherenceFrom}</strong> to <strong>${providedAdherenceTo}</strong>
              </div>
        </div>
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
                                <td>&#88;</td>
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
                    </#if>
                </tbody>
        </table>
        <h4>Reported adherence</h4>
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
                            <td>&#10004;</td>
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
                </#if>
            </tbody>
        </table>

</@layout.defaultLayout>