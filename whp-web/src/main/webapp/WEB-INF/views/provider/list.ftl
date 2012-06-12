<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "MoTeCH-WHP">
<div id="searchProvidersForm">
    <form action="<@spring.url '/providers/search'/>" method="POST" class="row well form-horizontal">
        <div class="offset2">

            <div class="control-group">
                <label class="control-label" for="providerId">Provider ID</label>

                <div class="controls">
                    <input class="input-xlarge" type="text" name='providerId' id="providerId" autofocus="autofocus"/>
                </div>
            </div>

            <#if districts?exists>
                <#if districts?size != 0>
                    <div class="control-group">
                        <label class="control-label" for="district">District</label>

                        <div class="controls">
                            <select id="districts" name="district">
                                <#list districts as district>
                                    <option value="${district}">${district}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </#if>
            </#if>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary form-button-center">Search</button>
                </div>
            </div>
        </div>
    </form>
    <table id="providerList" class="table table-bordered table-condensed">
        <thead>
            <tr>
                <th>Provider ID</th>
                <th>District</th>
                <th>Primary Mobile Number</th>
                <th>Secondary Mobile Number</th>
                <th>Tertiary Mobile Number</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <#if providerList?size == 0>
                <tr>
                    <td style="text-align: center" colspan="6">No providers to show</td>
                </tr>
            <#else>
                <#list providerList as provider>
                    <tr id="providerList_${provider.providerId}">
                        <td class="providerId" id="provider_${provider.providerId}_ProviderId">${provider.providerId}</td>
                        <td id="provider_${provider.providerId}_District">${provider.district?cap_first}</td>
                        <td id="provider_${provider.providerId}_PrimaryMobile">
                            <#if provider.primaryMobile?exists>
                            ${provider.primaryMobile}
                            </#if>
                        </td>
                        <td id="provider_${provider.providerId}_SecondaryMobile">
                            <#if provider.secondaryMobile?exists>
                            ${provider.secondaryMobile}
                            </#if>
                        </td>
                        <td id="provider_${provider.providerId}_TertiaryMobile">
                            <#if provider.tertiaryMobile?exists>
                            ${provider.tertiaryMobile}
                            </#if>
                        </td>
                        <td id="provider_${provider.providerId}_Status">
                            <#if provider.active>
                            Active
                                <#else>
                                Inactive
                            </#if>
                        </td>
                    </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>

</@layout.defaultLayout>
