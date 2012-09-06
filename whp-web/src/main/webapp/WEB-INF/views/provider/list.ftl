<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<#include "../user/activateProvider.ftl">
<#include "../user/resetPassword.ftl">

<@layout.defaultLayout "MoTeCH-WHP">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listProvider.js'/>"></script>
<div id="search-section" class="row well">
    <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search
        Pane</a></h3>

    <div id="search-pane">
        <form action="<@spring.url '/providers/search'/>" method="GET">
            <table>
                <tr>
                    <td>
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
                    </td>
                    <td>
                        <div class="control-group">
                            <label class="control-label" for="providerId">Provider ID</label>

                            <div class="controls">
                                <input class="input-large pull-down-5px" type="text" name='providerId' id="providerId"
                                       autofocus="autofocus"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group pull-down padding-left">
                            <div class="controls">
                                <button type="submit" id="search" class="btn btn-primary form-button-center">Search
                                </button>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
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
        <#--Reset Password column should always be the last one (we are appending this column dynamically if this column does not exist)-->
            <th type="reset-password"></th>
        </tr>

<@paginator.paginate entity="provider">
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Provider Id</th>
                <th>District</th>
                <th>Primary Mobile Number</th>
                <th>Secondary Mobile Number</th>
                <th>Tertiary Mobile Number</th>
                <th>Status</th>
                <th></th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <tr class="provider-row" ng-repeat="item in data" id="providerList_{{item.providerId}}" providerId="{{item.providerId}}">
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
                <td></td>
                <td></td>
                <td></td>
                    <#--<td id="provider_{{provider.providerId}}_Status" type="status">-->
                        <#--<#if provider.active>-->
                            <#--Active-->
                        <#--<#else>-->
                            <#--Inactive-->
                        <#--</#if>-->
                    <#--</td>-->
                    <#--<td type="activate-provider">-->
                        <#--<#if !provider.active>-->
                            <#--<a type="activate-link" data-toggle="modal" href="#activateProviderModal"-->
                               <#--class="activate-link"-->
                               <#--userName="{{provider.providerId}}">Activate</a>-->
                        <#--</#if>-->
                    <#--</td>-->
                    <#--<td type="reset-password">-->
                        <#--<#if provider.active>-->
                            <#--<a type="reset-password-link" class="reset-password one-line" data-toggle="modal"-->
                               <#--href="#resetPasswordModal">Reset Password</a>-->
                        <#--</#if>-->
                    <#--</td>-->
                </tr>
        </tbody>
    </table>
</@paginator.paginate>

</@layout.defaultLayout>