<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout/>
<@layout.defaultLayout "Patient Dashboard">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/patientDashboard.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/treatmentCard.js'/>"></script>
<script type="text/javascript">
    <#if patient.currentTreatment?? && patient.nextPhaseName??>
    $(function() {
        $("#${patient.nextPhaseName.name()}").addClass("active");
    });
    </#if>
    <#if patient.currentTreatment??>
        <#list patient.phasesNotPossibleToTransitionTo as phases>
        $(function() {
            $("#${phases}").hide();
        });
        </#list>
    </#if>
    $(document).ready(function(){
        $('#achtung').fadeOut(350).fadeIn(350).fadeOut(350).fadeIn(350).fadeOut(350).fadeIn(350);
    });
</script>
<#if message??>
<div class="dateUpdated-message-alert row alert alert-info fade in">
    <button class="close" data-dismiss="alert">&times;</button>
${message}
</div>
</#if>

<#include "phaseTransitionAlert.ftl"/>
<br/>
<#include "patientInfo.ftl"/>
<br/>
<#include "phaseInfo.ftl"/>

<div id="treatmentCard"></div>
<div class="controls">
    <a href="<@spring.url "/"/>" class="btn">Back To Patient List</a>
    <a id="setDateLink" data-toggle="modal" href="#setDatesModal" class="btn btn-primary brand">Adjust Phase Start
        Dates</a>
    <a id="printTreatmentCard" target = "_blank" class="btn" href = "<@spring.url '/patients/print/' + patient.patientId/>" >Print</a>
</div>

<br/>
<br/>


<form id="addRemarkForm" action="<@spring.url '/patients/addRemark/' + patient.patientId/>" method="post">

    <div class="well row">
        <textarea class="float-left" id="patientRemark" name="patientRemark"></textarea>
        <button class="btn btn-primary float-right" type="submit" id='addRemark'>Add Remark</button>
    </div>

</form>

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
    $('#ipDatePicker').datepicker({maxDate: new Date, dateFormat:'dd/mm/yy'});
    $('#eipDatePicker').datepicker({maxDate: new Date, dateFormat:'dd/mm/yy'});
    $('#cpDatePicker').datepicker({maxDate: new Date, dateFormat:'dd/mm/yy'});
    $("#clearFields").click(function () {
        $('#ipDatePicker').val('');
        $('#eipDatePicker').val('');
        $('#cpDatePicker').val('');
        event.preventDefault();
    });
    createAutoClosingAlert(".dateUpdated-message-alert", 5000)
    $('#treatmentCard').load('/whp/treatmentcard/show?patientId=${patient.patientId}', function() {
        setUpTreatmentCardTable();
    });
</script>
</@layout.defaultLayout>
