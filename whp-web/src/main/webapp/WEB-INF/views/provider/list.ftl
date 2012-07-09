<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<#include "../user/resetPassword.ftl">
<@layout.defaultLayout "MoTeCH-WHP">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listProvider.js'/>"></script>
<div id="search-section" class="row well">
    <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search
        Pane</a></h3>

    <div id="search-pane">
        <form action="<@spring.url '/providers/search'/>" method="GET" class="form-horizontal">
            <div class="offset1">
                <div class="control-group">
                    <label class="control-label">District*</label>

                    <div class="controls">
                        <select id="district" name="selectedDistrict">
                            <#list districts as district>
                                <option <#if selectedDistrict == district.name> selected </#if>
                                                                                value="${district.name}">${district.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="providerId">Provider ID</label>

                    <div class="controls">
                        <input class="input-large" type="text" name='providerId' id="providerId" autofocus="autofocus"/>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <button type="submit" id="search" class="btn btn-primary form-button-center">Search</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="results">
    <table id="providerList" class="table table-bordered table-condensed default-arrow">
        <thead>
        <tr>
            <th>Provider ID</th>
            <th>District</th>
            <th>Primary Mobile Number</th>
            <th>Secondary Mobile Number</th>
            <th>Tertiary Mobile Number</th>
            <th>Status</th>
            <th type="activate-provider"></th>
        </tr>
        </thead>
        <tbody>
            <#if providerList?size == 0>
            <tr>
                <td class="warning" style="text-align: center" colspan="6">
                    <#if providerId != "">
                        No providers found for District: '${selectedDistrict}' with provider ID: '${providerId}'
                    <#else>
                        No providers found for District: '${selectedDistrict}'
                    </#if>
                </td>
            </tr>
            <#else>
                <#list providerList as provider>
                <tr class="provider-row" id="providerList_${provider.providerId}" providerId="${provider.providerId}">
                    <td class="providerId" id="provider_${provider.providerId}_ProviderId">${provider.providerId}</td>
                    <td id="provider_${provider.providerId}_District">${provider.district}</td>
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
                    <td id="provider_${provider.providerId}_Status" type="status">
                        <#if provider.active>
                            Active
                        <#else>
                            Inactive
                        </#if>
                    </td>
                    <td type="activate-provider">
                        <#if !provider.active>
                            <a type="activate-button" data-toggle="modal" href="#resetPasswordModal" class="activate-link"
                               userName="${provider.providerId}">Activate</a>
                        </#if>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>

    <@resetPassword/>

</@layout.defaultLayout>