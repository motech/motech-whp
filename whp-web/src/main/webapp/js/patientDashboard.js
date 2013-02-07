$(function () {
    $('#addRemarkForm').submit(function(event) {
        event.preventDefault();

        if($('#patientRemark').val().trim() != "") {
            $('#addRemark').attr('disabled', true);
            var data = {
                "patientRemark": $('#patientRemark').val().trim()
            };

            $.post($('#addRemarkForm').attr('action'), data, function(response) {
                $('#patientRemark').val("");
                $('#addRemark').removeAttr('disabled');
                $('#remarks').html(response);
                $('.cmf-admin-remark').first().effect("highlight", {}, 6000);
            });
        }
    });

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

    $(".date-dismiss").click(function(){
        $(this).parent().find("input").val("");
    });
    $(".show-date-button").click(function(){
        $(this).parent().find("input").focus();
    });

    $("#clearFields").click(function () {
        $('.hasDatepicker').each(function() {
             $(this).val('');
        });
    });

    $('.hasDatepicker').each(function() {
        $(this).css('cursor', 'pointer');
    });

    $('#flag_star').bind('click', function(event){
        var target = event.target;
        $.ajax({
            type:"GET",
            url:$(target).attr("endpoint"),
            success:function (data) {
                var imageSrc = $(target).attr('src');
                var endpoint = $(target).attr('endpoint');
                var flagImageCurrentValue = $(target).attr("flagValue");
                var flagImageNextValue = (flagImageCurrentValue == "true") ? "false" : "true";

                $(target).attr('src', imageSrc.replace(flagImageCurrentValue + "-star", flagImageNextValue + "-star"));
                $(target).attr('endpoint', endpoint.replace("value=" + flagImageNextValue, "value=" + flagImageCurrentValue));
                $(target).attr("flagValue", flagImageNextValue);
            }
        });
    });

    createAutoClosingAlert(".dateUpdated-message-alert", 5000);
});