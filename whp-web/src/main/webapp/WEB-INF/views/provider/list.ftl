<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<#include "../user/activateProvider.ftl">
<#include "../user/resetPassword.ftl">

<@layout.defaultLayout title="MoTeCH-WHP" entity="itadmin">
<h1>Provider Listing</h1>
<div id="search-section" class="well">
    <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search
        Pane</a></h3>

    <div id="search-pane">
        <@paginator.filter id="provider_pagination_filter" pagination_id = "provider_pagination">
            <table>
                <tr>
                    <td>
                        <div class="control-group">
                            <label class="control-label">District*</label>

                            <div class="controls">
                                <select id="district" name="selectedDistrict">
                                    <option value = "" ></option>
                                    <#list districts as district>
                                        <option value="${district.name}"   ng-selected="{{isSelected('${district.name}', searchCriteria.selectedDistrict, 'district')}}">${district.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group">
                            <label class="control-label" for="providerId">Provider ID</label>

                            <div class="controls">
                                <input class="input-large" type="text" name='selectedProvider' id="providerId"
                                       ng-model="searchCriteria.selectedProvider" autofocus="autofocus"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group pull-down padding-left">
                            <div class="controls">
                                <button type="submit" id="search" class="btn btn-primary r">Search <i class="icon-search icon-white"></i>
                                </button>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </@paginator.filter>
    </div>
</div>
<#if !pageNo??>
    <#assign pageNo=1/>
</#if>
<div class="results">
    <@paginator.paginate id="provider_pagination" filterSectionId="provider_pagination_filter" entity="provider" rowsPerPage="10" contextRoot="/whp" stylePath="/resources-${applicationVersion}/styles">
        <table class="table table-striped table-bordered" id="providerList">
            <thead>
            <tr>
                <th>Provider Id</th>
                <th>District</th>
                <th>Primary Mobile Number</th>
                <th>Secondary Mobile Number</th>
                <th>Tertiary Mobile Number</th>
                <th>Status</th>
                <th type="action-on-provider"></th>
            </tr>
            </thead>
            <tbody>
            <tr class="provider-row" ng-repeat="item in data.results" id="providerList_{{item.providerId}}"
                providerId="{{item.providerId}}">
                <td class="providerId" id="providerid_.{{item.providerId}}">{{item.providerId}}</td>
                <td id="provider_{{item.providerId}}_District">{{item.district}}</td>
                <td id="provider_{{provider.providerId}}_PrimaryMobile">
                    {{item.primaryMobile}}
                </td>
                <td id="provider_{{provider.providerId}}_SecondaryMobile">
                    {{item.secondaryMobile}}
                </td>
                <td id="provider_{{provider.providerId}}_TertiaryMobile">
                    {{item.tertiaryMobile}}
                </td>
                <td id="provider_{{provider.providerId}}_Status" type="status">
                    <div ng-show="item.active">
                        Active
                    </div>
                    <div ng-hide="item.active">
                        Inactive
                    </div>
                </td>
                <td type="action-on-provider">
                    <div ng-show="item.active">
                        <a type="reset-password-link" class="reset-password one-line" data-toggle="modal"
                           href="#resetPasswordModal">Reset Password</a>
                    </div>
                    <div ng-hide="item.active">
                        <a type="activate-link" data-toggle="modal" href="#activateProviderModal"
                           class="activate-link"
                           userName="{{provider.providerId}}">Activate</a>
                    </div>
                </td>
            </tr>
            <tr type="no-results" class="hide">
                <td class="warning text-center" colspan="6"></td>
            </tr>
            </tbody>
        </table>
    </@paginator.paginate>
    <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>
    <@resetPassword/>
    <@activateProvider/>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listProvider.js'/>"></script>
</@layout.defaultLayout>