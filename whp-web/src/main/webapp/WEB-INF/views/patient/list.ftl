<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#include "../layout/legend.ftl">
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="Patient List" entity="cmfadmin">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>
<h1>Patients Listing</h1>

<div class="row-fluid">
    <div class="well" id="search-section">
        <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search
            Pane</a></h3>
        <@paginator.filter id = "patient_list_filter"  pagination_id = "patient_listing">
            <div id="search-pane">
                    <fieldset class="filters">
                        <div class="row-fluid filters">
                            <div class="control-group span2">
                                <label class="control-label">Provider ID</label>

                                <div class="controls">
                                    <input type="text" name="selectedProvider" id="providerId"
                                           value="{{searchCriteria.providerId}}"/>
                                </div>
                            </div>

                            <div class="control-group span2">
                                <label class="control-label">Provider District</label>

                                <div class="controls">
                                    <select id="district" name="selectedDistrict" data-id="district-autocomplete">
                                        <option value=""></option>
                                        <#list districts as district>
                                            <option value="${district.name}"
                                                    ng-selected="{{isSelected('${district.name}', searchCriteria.district, 'district')}}">${district.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                    <div class="control-group span2 pull-down">
                        <div class="controls">
                            <button type="submit" id="searchButton" class="btn btn-primary">
                                Search <i class="icon-search icon-white"></i>
                            </button>
                        </div>
                    </div>
            </div>
        </@paginator.filter>
    </div>
    <div class="row-fluid">
        <div class="results">

            <div id="patients">
                <#include "./patientList.ftl"/>
            </div>
        </div>
    </div>
    <div class="row-fluid ">
        <i>&#42; Cumulative missed doses shown as of ${lastSunday}</i>

        <div class="pull-right patients-list-legend">
            <@legend key1="paused" value1="Current Treatment Paused" />
        </div>
    </div>
</div>
    <script type="text/javascript"
            src="<@spring.url '/resources-${applicationVersion}/js/listPatients.js'/>"></script>
</@layout.defaultLayout>
