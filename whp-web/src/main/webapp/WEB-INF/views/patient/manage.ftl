<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<@layout.defaultLayout title="Manage Patient Treatments" entity="itadmin">

<h1>Manage Patient Treatments</h1>
<#if message??>
    <div id="notification" class="row alert alert-error">
        <button class="close" data-dismiss="alert">&times;</button>
    ${message}
    </div>
</#if>
<div>
    <form id = "patientForm" action="/whp/managepatients/removeTreatment" method="POST">
        <fieldset class="filters">

            <div class="row-fluid sel-result">
                <div class="control-group span2">
                    <label class="control-label">Patient ID</label>
                    <div class="controls">
                        <input type="text" name="patientId" id="patientId" value=""/>
                    </div>
                </div>

                <div class="control-group span2">
                    <label class="control-label">TB ID</label>

                    <div class="controls">
                        <div class="controls">
                            <input type="text" name="tbId" id="tbId" value=""/>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
        <div class="control-group buttons-group row-fluid">
            <div class="controls pull-right">
                <button id="searchButton" class="btn btn-primary">
                    Find <i class="icon-search icon-white"></i>
                </button>
            </div>
        </div>

</div>
<div id = "treatmentDetails"></div>

<script type="text/javascript">
    $(function () {
        $('#searchButton').click(function() {
            if($("#patientId").val().trim() == "") alert("Patient ID is required");
            if($("#tbId").val().trim() == "") alert("TB Id is required");

            var loadTreatmentDetailsUrl = "/whp/managepatients/treatment?patientId=" + $("#patientId").val() + "&tbId=" + $("#tbId").val();
            $("#treatmentDetails").load(loadTreatmentDetailsUrl, function() {
                $('#deleteTreatment').click(function() {
                    $("#patientForm").submit();
                });
            });
        });

    });
</script>
</@layout.defaultLayout>