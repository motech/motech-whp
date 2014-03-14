<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>

<@layout.defaultLayout title="Delete Patient" entity="itadmin">

 <div class="warning-box" >
     WARNING!!! <img id="achtung" ng-show="item.showAlert == true" height="30" width="30"
      src="<@spring.url '/resources-${applicationVersion}/img/smallwarning.png'/>"/>
      Use of this feature will result in permanent deletion of patient data from the MOTECH database,
      and should only be used by the high-level client administrator authorized to carry out this function.
 </div>

<div class="padding-top">
    <div class="bold">
     <h1>Delete Patient</h1>
    </div>
</div>

<#if message??>
    <div id="notification" class="row alert alert-error">
        <button class="close" data-dismiss="alert">&times;</button>
    ${message}
    </div>
</#if>
<div>
    <form id = "patient" action="/whp/managepatients/delete" method="POST">
        <fieldset class="filters">

            <div class="row-fluid sel-result">
                <div class="control-group span2">
                    <label class="control-label">Patient ID</label>
                    <div class="controls">
                        <input type="text" name="patientId" id="patientId" value=""/>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
        <div id="deletePatientConfirm" style="display:none;" class="control-group buttons-group row-fluid">
            <div class="controls pull-right">
                <button id="YesButton" class="btn btn-primary btn-danger">
                       Yes <i class="icon-trash icon-white"></i>
                   </button>
                <button id="YesButton" class="btn btn-primary btn-danger">
                       No <i class="icon-trash icon-white"></i>
                </button>
            </div>
        </div>


        <div  class="control-group buttons-group row-fluid">
            <div class="controls pull-right">
                <button id="searchButton" class="btn btn-primary">
                    Find <i class="icon-search icon-white"></i>
                </button>
            </div>
        </div>
</div>
<div id = "patientDetails"></div>

<script type="text/javascript">
    $(function () {
        $('#searchButton').click(function() {
            if($("#patientId").val().trim() == "") alert("Patient ID is required");

            var loadPatientDetailsUrl = "/whp/managepatients/patient?patientId=" + $("#patientId").val();
            $("#patientDetails").load(loadPatientDetailsUrl, function() {
                $('#deletePatient').click(function() {
                ConfirmDelete();

                });
            });
        });

    });

    function ConfirmDelete()
    {
      var x = confirm("Are you sure you want to delete the Patient Details?");
      if (x)
         $("#patient").submit();
      else
        return false;
    };
</script>
</@layout.defaultLayout>