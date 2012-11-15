<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout/>
<@layout.defaultLayout title="Patient Dashboard" entity="cmfadmin">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/patientDashboard.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/treatmentCard.js'/>"></script>
<script type="text/javascript">
        <#if patient.currentTreatment?? && patient.nextPhaseName??>
        $(function () {
            $("#${patient.nextPhaseName.name()}").addClass("active");
        });
        </#if>
        <#if patient.currentTreatment??>
            <#list patient.phasesNotPossibleToTransitionTo as phases>
            $(function () {
                $("#${phases}").hide();
            });
            </#list>
        </#if>
    $(document).ready(function () {
        $('#achtung').fadeOut(350).fadeIn(350).fadeOut(350).fadeIn(350).fadeOut(350).fadeIn(350);
    });
</script>

<div class="patient-info word-wrap row-fluid offset-top">

        <#if message??>
        <div class="dateUpdated-message-alert alert alert-success fade in">
            <button class="close" data-dismiss="alert">&times;</button>
        ${message}
        </div>
        </#if>
        <h1 class="">Patient details: ID# ${patient.patientId!}</h1>
        <#include "phaseTransitionAlert.ftl"/>

        <#include "patientInfo.ftl"/>

        <#include "phaseInfo.ftl"/>

        <div id="treatmentCard"></div>

    <br />
    <div class="patients-buttons-container">
        <div class="row-fluid">
            <div class="controls  pull-left">
                <a href="<@spring.url "/"/>" class="btn"><i class="icon-chevron-left"></i> Back To Patient List</a>


            </div>
            <div class="controls pull-right">
                <a id="printTreatmentCard" target="_blank" class="btn btn-large btn-info" href="<@spring.url '/patients/print/' + patient.patientId/>"><i class="icon-print icon-white"></i> Print</a>
                <a id="setDateLink" data-toggle="modal" href="#setDatesModal" class="btn btn-success  btn-large"><i class="icon-calendar icon-white"></i> Adjust Phase Start
                    Dates</a>
            </div>
        </div>
    </div>
    <br />
       <div id="remarks" class="well" >
            <h3>Remarks</h3>
            <form class="remarks-form" id="addRemarkForm" action="<@spring.url '/patients/addRemark/' + patient.patientId/>" method="post">
                <div class="row-fluid">
                  <div class="remarks-field">
                      <textarea id="patientRemark" name="patientRemark" placeholder="Please enter your remarks here"></textarea>
                  </div>

                   <div class="remarks-button clearfix">
                       <button class="btn btn-info" type="submit" id='addRemark'><i class="icon-pencil icon-white"></i> Add Remark</button>
                   </div>
                </div>
            </form>

            <#include "remarks.ftl"/>
        </div>



    <script type="text/javascript">
        $('#ipDatePicker').datepicker({maxDate:'${today}', dateFormat:'dd/mm/yy'});
        $('#eipDatePicker').datepicker({maxDate:'${today}', dateFormat:'dd/mm/yy'});
        $('#cpDatePicker').datepicker({maxDate:'${today}', dateFormat:'dd/mm/yy'});

        $('#treatmentCard').load('/whp/treatmentcard/show?patientId=${patient.patientId}', function () {
            setUpTreatmentCardTable();
        });
    </script>
</div>

</@layout.defaultLayout>
