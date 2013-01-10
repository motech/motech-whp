<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout title="Patient List" entity="cmfadmin">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>
<h1>Patients Listing</h1>

<div class="row-fluid">
        <div class="well" id="search-section">
            <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search
                Pane</a></h3>

            <div id="search-pane">
                <form id="searchForm" action="<@spring.url '/patients/search'/>" input method="POST" submitOnEnterKey="true">
                    <div class="row-fluid filters">
                        <div class="control-group span2">
                            <label class="control-label">Provider ID</label>
                            <div class="controls">
                                <input type="text" name="selectedProvider" id="providerId" value="${selectedProvider!}">
                            </div>
                        </div>

                        <div class="control-group span2">
                            <label class="control-label">Provider District</label>

                            <div class="controls">
                                <select id="district" name="selectedDistrict">
                                    <option value=""></option>
                                    <#list districts as district>
                                        <option <#if selectedDistrict! == district.name> selected </#if>
                                                                                        value="${district.name}">${district.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>

                        <div class="control-group span2 pull-down">
                            <div class="controls">
                                <button type="button" id="searchButton" class="btn btn-primary">
                                    Search <i class="icon-search icon-white"></i>
                                </button>
                            </div>
                        </div>

                    </div>

                </form>
            </div>
        </div>
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
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listPatients.js'/>"></script>
</@layout.defaultLayout>
