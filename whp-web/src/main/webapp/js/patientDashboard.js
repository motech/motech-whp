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

    $(".date-dismiss").click(function(event){
        $(event.target).closest("tr").find("input").val("");
    });

    $("#clearFields").click(function () {
        $('#ipDatePicker').val('');
        $('#eipDatePicker').val('');
        $('#cpDatePicker').val('');
    });
    $('#ipDatePicker').css('cursor', 'pointer');
    $('#eipDatePicker').css('cursor', 'pointer');
    $('#cpDatePicker').css('cursor', 'pointer');
    createAutoClosingAlert(".dateUpdated-message-alert", 5000);
});