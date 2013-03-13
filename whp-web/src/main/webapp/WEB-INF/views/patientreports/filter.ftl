<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="Patient Reports" entity="cmfadmin">

<h1>Patient Reports</h1>
<form name="patientReportsFilter">

    <div class="row-fluid">
        <div class="well">
            <fieldset class="filters">
                <div class="control-group span2">
                    <label class="control-label">Select Report</label>

                    <div class="controls">
                        <select id="report" name="report">
                            <option value="<@spring.message 'whp.reports.url'/>/patientreports/patientSummary.xls">Patient Summary Report</option>
                            <option value="<@spring.message 'whp.reports.url'/>/patientreports/patientRegistrations.xls">Patient Registrations Report</option>
                            <option value="<@spring.message 'whp.reports.url'/>/patientreports/patientClosedTreatments.xls">Patient Closed Treatments Report</option>
                        </select>
                    </div>

                    <label class="control-label">Provider District</label>

                    <div class="controls">
                        <select id="district" name="district">
                            <option value="">All</option>
                            <#list districts as district>
                                <option value="${district.name}">${district.name}</option>
                            </#list>
                        </select>
                    </div>


                    <div class="controls">
                        <label class="control-label  date-field">Date From</label>

                        <div class="input-append input-prepend">
                            <span class="add-on show-date-button"><i class="icon-calendar"></i></span>
                            <input class="dates" type="text" placeholder="From: dd/mm/yyyy"
                                   data-date-format="dd/mm/yyyy" id="from" name="from" data-date-range="to">
                            <span class="add-on clear-date-button"><i class="icon-remove-sign"></i></span>
                        </div>
                    </div>
                    <label class="control-label  date-field">Date To</label>

                    <div class="controls">
                        <div class="input-append input-prepend">
                            <span class="add-on show-date-button"><i class="icon-calendar"></i></span>
                            <input class="dates" type="text" placeholder="To: dd/mm/yyyy" data-date-format="dd/mm/yyyy"
                                   id="to" name="to"  data-date-range="from">
                            <span class="add-on clear-date-button"><i class="icon-remove-sign"></i></span>
                        </div>
                    </div>
                </div>
        </div>
    </div>
    <div class="control-group buttons-group row-fluid">
        <div class="controls pull-left">
            <button id="clearFilter" type="reset" class="btn">Clear All</button>
            <button type="button" id="download" class="btn btn-primary">Download <i
                    class="icon-download icon-white"></i></button>
        </div>
    </div>
</form>


<script type="text/javascript">
    $(function () {

        $("#from").datepicker({
            dateFormat:'dd/mm/yy',
            onClose:function (selectedDate) {
                $("#to").datepicker("option", "minDate", selectedDate);
                $("#to").datepicker("option", "maxDate", getDate(selectedDate, 180));
            }
        });
        $("#to").datepicker({
            dateFormat:'dd/mm/yy',
            onClose:function (selectedDate) {
                $("#from").datepicker("option", "minDate", getDate(selectedDate, -180));
                $("#from").datepicker("option", "maxDate", selectedDate);
            }
        });

        getDate = function(dtString, deltaDays){
            var dateParts = dtString.split("/");
            var newDate = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
            newDate.setDate(parseInt(dateParts[0]) + deltaDays)

            return newDate;
        }

        $("#district").combobox();

        $("#district").bind("autocomplete-selected", function (event, ui) {
            $("#district").val($(this).val());
        });

        $(".show-date-button").click(function () {
            $(this).parent().find(".dates").focus();
        });

        $(".clear-date-button").click(function () {
            var dateElement = $(this).parent().find(".dates");
            dateElement.val("");

            var relatedDateField = $("#" + dateElement.data("date-range"));
            relatedDateField.datepicker("option", "minDate", "");
            relatedDateField.datepicker("option", "maxDate", "");
        });

        $('#download').bind('click', function (event) {
            $("#district").val($("#district-autocomplete").val());
            window.open($("#report").val() + "?" + $("#district, #from, #to").serialize());
            return false;
        });
    });
</script>
</@layout.defaultLayout>