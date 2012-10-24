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



        var setContainerIdOnClosureForm = function () {
            var containerId = $(this).parents('tr').attr('containerId');
            $('#setReason input[name=containerId]').val(containerId);
            $('#containerIdDisplay').html(containerId);
        }

        var showTbNegativeOptionForPositiveDiagnosis = function () {
            var TB_NEGATIVE_OPTION_CODE = "1";
            if ($(this).parents('tr').children('td#diagnosisValue').text().toLowerCase() === "positive") {
                $("#reason option[value=" + TB_NEGATIVE_OPTION_CODE + "]").attr('disabled', 'disabled').hide();
            }
            else {
                $("#reason option[value=" + TB_NEGATIVE_OPTION_CODE + "]").removeAttr('disabled').show();
            }
        }

        $(".openContainerLink").click(function () {
            var containerId = $(this).parents("tr").attr("containerId");
            var endpoint = $($("#sputumTrackingDashboardRowsList")[0]).attr("endpoint") + "/open-container?containerId=" + containerId;
            $.ajax({
                type:"GET",
                url:endpoint,
                success:function () {
                    angular.element($('#sputum_tracking_pagination .paginator')).controller().loadPage();
                }
            });
            return false;
        });
    });

    var resetFormFields = function () {
        $('#reason').val('');
        $('#alternateDiagnosis').val('');
        $("#alternateDiagnosis-autocomplete").val("");
        $('#consultationDate').val('');
        $("#tbNegativeControls").hide();
        $("#container-tracking-error").html('');
    }

    var today = new Date();
    $('#consultationDate').datepicker({maxDate:today, dateFormat:'dd/mm/yy'});

    $('#reason').change(function () {
        showAlternateDiagnosisTbNegativeIsSelected($(this).val());
    });
    $('input.hasDatepicker').each(function() {
        $(this).css('cursor', 'pointer');
    });
    $(".date-dismiss").click(function(){
        $(this).parent().find("input.hasDatepicker").val("");
    });
    $(".show-date-button").click(function(){
        $(this).parent().find("input.hasDatepicker").focus();
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
        rules:{
            consultationDate:{
                required:true
            }
        },
        messages:{
            consultationDate:{
                required:"Please enter the consultation date"
            },
            reason:{
                required:"Please select the reason"
            },
            alternateDiagnosis:{
                required:"Please select alternate diagnosis"
            }
        }
    });

    $("#alternateDiagnosis").combobox();

    $("#saveReason").click(function () {
        var dataString = $("#setReason").serialize();
        var endpoint = $($("#sputumTrackingDashboardRowsList")[0]).attr("endpoint") + "/close-container";
        $.ajax({
            type:"POST",
            url:endpoint,
            data:dataString,
            success:function () {
                angular.element($('#sputum_tracking_pagination .paginator')).controller().loadPage();
                $('#setReason').modal('hide');
            },
            statusCode: {
                400 : function(data){
                    var listOfErrors = JSON.parse(data.responseText).errors;
                    var allErrors = "";
                    $.each(listOfErrors, function(index, element){
                        allErrors += element + "<br/>";
                    })
                    $("#container-tracking-error").html(allErrors);
                    $("#container-tracking-error").show();
                }
            }
        });
        resetFormFields();
        return false;
    });

    $('#setReason').bind('hidden', function () {
        $("#container-tracking-error").hide()
    });


    $("#container-tracking-error").hide()

});

