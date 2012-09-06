<#import "/spring.ftl" as spring />
<#import "../layout/default-cmfadmin.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout "Patient List">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>

<div>
    <div class="row well" id="search-section">
        <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search
            Pane</a></h3>

        <div id="search-pane">
            <form id="searchForm" action="<@spring.url '/patients/search'/>" input method="POST" submitOnEnterKey="true"
                  class="">
                <table>
                    <tr>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Provider District*</label>

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
                                <label class="control-label">Provider ID</label>

                                <div class="controls">
                                    <select id="providerId" name="selectedProvider"
                                            <#if selectedProvider??>selectedProviderInSession=${selectedProvider}</#if>
                                            >
                                        <!-- AJAX Fetch Provider IDs -->
                                    </select>
                                </div>
                            </div>
                        </td>

                        <td>
                            <div class="control-group pull-down">
                                <div class="controls">
                                    <button type="button" id="searchButton" class="btn btn-primary form-button-center">
                                        Search
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
        <@legend key1="paused" value1="Current Treatment Paused" />
        <div id="patients">
            <#include "./patientList.ftl"/>
        </div>
    </div>
</div>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listPatients.js'/>"></script>
</@layout.defaultLayout>
