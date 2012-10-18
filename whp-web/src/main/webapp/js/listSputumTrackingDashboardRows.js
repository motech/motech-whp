$(function () {
    $('#sputum_tracking_pagination').bind('pageLoadSuccess', function () {
        if ($('#sputumTrackingDashboardRowsList tbody tr').length == 1) {
            var noResultsMessage = "No Containers found";

            $('[type=no-results] td').html(noResultsMessage);
            $('[type=no-results]').show();
        }
        else {
            $('[type=no-results]').hide();
        }

        $(".closeContainer").click(function () {
            setContainerIdOnClosureForm.call(this);
            showTbNegativeOptionForPositiveDiagnosis.call(this);
        });

        $("#close").click(function () {
            resetFormFields();
        });

        var resetFormFields = function () {
            $('#reason').val('');
            $('#alternateDiagnosis').val('');
            $("#alternateDiagnosis-autocomplete").val("");
            $('#consultationDatePicker').val('');
            $("#tbNegativeControls").hide();
        }

        var setContainerIdOnClosureForm = function () {
            var containerId = $(this).parents('tr').attr('containerId');
            $('#setReason input[name=containerId]').val(containerId);
        }

        var showTbNegativeOptionForPositiveDiagnosis = function () {
            var TB_NEGATIVE_OPTION_CODE = "1";
            if ($(this).parents('tr').children('td#diagnosis').text().toLowerCase() === "positive") {
                $("#reason option[value=" + TB_NEGATIVE_OPTION_CODE + "]").attr('disabled', 'disabled').hide();
            }
            else {
                $("#reason option[value=" + TB_NEGATIVE_OPTION_CODE + "]").removeAttr('disabled').show();
            }
        }
    });

    var today = new Date();
    $('#consultationDate').datepicker({maxDate:today, dateFormat:'dd/mm/yy'});

    $('#reason').change(function () {
        showAlternateDiagnosisTbNegativeIsSelected($(this).val());
    });

    var showAlternateDiagnosisTbNegativeIsSelected = function (selectedValue) {
        if (selectedValue === "1") {
            $("#tbNegativeControls").show();
        }
        else {
            $("#tbNegativeControls").hide();
        }
    };

    createAutoClosingAlert(".container-tracking-message-alert", 5000);

    $.metadata.setType("attr", "validate");

    $("#setReason").validate({
        rules: {
            consultationDate: {
                required: true
            }
        },
        messages: {
            consultationDate: {
                required: "Please enter the consultation date"
            },
            reason: {
                required: "Please select the reason"
            },
            alternateDiagnosis: {
                required: "Please select alternate diagnosis"
            }
        }
    });

    $("#alternateDiagnosis").combobox();

    $("#saveReason").click(function() {
        var dataString = $("#setReason").serialize();
        $.ajax({
            type: "POST",
            url: "/whp/sputum-tracking/close-container",
            data: dataString,
            success: function() {
                angular.element($('#sputum_tracking_pagination .paginator')).controller().loadPage();
                $('#setReason').modal('hide');
            }
        });
        return false;
    });

    $(".openContainerLink").click(function() {
        var containerId = $(this).parents("tr").attr("containerId");
        $.ajax({
            type: "GET",
            url: "/whp/sputum-tracking/open-container" + "?containerId=" + containerId,
            success: function() {
                angular.element($('#sputum_tracking_pagination .paginator')).controller().loadPage();
            }
        });
        return false;
    });
});

