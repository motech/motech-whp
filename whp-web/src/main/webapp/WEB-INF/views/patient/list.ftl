<#import "/spring.ftl" as spring />
<#import "../layout/default-cmfadmin.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout "Patient List">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/redirctOnRowClick.js'/>"></script>

<div>
    <div class="well row">
        <form id="searchForm" action="<@spring.url '/patients/search'/>" method="POST"
              class="offset2-fixed form-horizontal">
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
                <label class="control-label">Provider ID</label>

                <div class="controls">
                    <select id="providerId" name="selectedProvider">
                        <!-- AJAX Fetch Provider IDs -->
                    </select>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="button" id="searchButton" class="btn btn-primary form-button-center">Search</button>
                </div>
            </div>
        </form>

    </div>
    <@legend key1="paused" value1="Current Treatment Paused" />
    <div id="patients">
        <table id="patientList" class="table table-bordered table-condensed" redirectOnRowClick="true">
            <thead>
            <tr>
                <th>Patient ID</th>
                <th>TB-ID</th>
                <th>Name</th>
                <th>Age</th>
                <th>Gender</th>
                <th>Provider ID</th>
                <th>Village</th>
                <th>District</th>
                <th>Treatment Category</th>
                <th>Treatment Start Date</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="text-center" colspan="10">No patients to show</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">
    $("#searchButton").click(function () {
        var districtId = $("#district").val();
        var providerId = $("#providerId").val() ? $("#providerId").val() : "";
        var data = {
            "selectedDistrict":districtId,
            "selectedProvider":providerId
        };
        $("#patients").load('/whp/patients/search', data);
    });
</script>
</@layout.defaultLayout>
