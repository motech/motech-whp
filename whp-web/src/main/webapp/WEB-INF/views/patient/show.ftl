<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout/>
<#include "../user/phaseInfo.ftl"/>
<#include "../patient/patientInfo.ftl"/>
<@layout.defaultLayout "Patient Dashboard">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/injectHtml.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/treatmentCard.js'/>"></script>

    <#if messages??>
    <div class="dateUpdated-message-alert row alert alert-info fade in">
        <button class="close" data-dismiss="alert">&times;</button>
    ${messages}
    </div>
    </#if>


    <@patientInfo/>
<br/>
    <@phaseInfo/>

<div id="treatmentCard"></div>
<div class="controls">
    <a href="<@spring.url "/"/>" class="btn">Back To Patient List</a>
    <a id="setDateLink" data-toggle="modal" href="#setDatesModal" class="btn btn-primary brand">Adjust Phase Start Dates</a>
</div>
<br/>
<script type="text/javascript">
            $('#saveTheDate').click(function () {
        if ($('#ipDatePicker').val() === "" && $('#eipDatePicker').val() === "" && $('#cpDatePicker').val() === "") {
            event.preventDefault();
            jConfirm('All phase start dates are set to empty. Do you want to continue?', 'Warning', function (r) {
                if (r === true) {
                    $('#setDatesModal').submit();
                }
            });
        }
    });
    $('#ipDatePicker').datepicker({dateFormat:'dd/mm/yy'});
    $('#eipDatePicker').datepicker({dateFormat:'dd/mm/yy'});
    $('#cpDatePicker').datepicker({dateFormat:'dd/mm/yy'});
    $("#clearFields").click(function () {
        $('#ipDatePicker').val('');
        $('#eipDatePicker').val('');
        $('#cpDatePicker').val('');
        event.preventDefault();
    });
    createAutoClosingAlert(".dateUpdated-message-alert", 5000)
    $('#treatmentCard').load('/whp/treatmentcard/show?patientId=${patient.patientId}');
</script>
</@layout.defaultLayout>
