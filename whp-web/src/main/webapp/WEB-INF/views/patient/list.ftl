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
        <#include "./patientList.ftl"/>
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
        $.post('/whp/patients/search', data, function(response) {
            $('#patients').html(response);
        })
    });
</script>
</@layout.defaultLayout>
