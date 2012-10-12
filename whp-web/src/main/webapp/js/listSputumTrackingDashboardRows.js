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

        $(".setReasonForClosure").click(function () {
            setContainerIdOnClosureForm.call(this);
            showTbNegativeOptionForPositiveDiagnosis.call(this);
        });

        $("#saveReason").click(function () {
            resetFormFields();
        });

        $("#close").click(function () {
            resetFormFields();
        });

        var resetFormFields = function () {
            $('#reason').val('');
            $('#alternateDiagnosis').val('');
            $('#consultationDatePicker').val('');
            $("#tbNegativeControls").hide();
        }

        var setContainerIdOnClosureForm = function () {
            var containerId = $(this).parents('tr').attr('containerid');
            $('#setReason input[name=containerId]').val(containerId);
        }

        var TB_NEGATIVE_OPTION_CODE = "1";

        var showTbNegativeOptionForPositiveDiagnosis = function () {
            if ($(this).parents('tr').children('td#diagnosis').text().toLowerCase() === "positive") {
                $("#reason option[value=TB_NEGATIVE_OPTION_CODE]").attr('disabled', 'disabled').hide();
            }
            else {
                $("#reason option[value=TB_NEGATIVE_OPTION_CODE]").removeAttr('disabled').show();
            }
        }
    });

    var today = new Date();
    $('#consultationDatePicker').datepicker({maxDate:today, dateFormat:'dd/mm/yy'});

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
});

