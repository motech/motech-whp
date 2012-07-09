$('#resetPasswordModal').on('shown', function () {
    $('#resetPasswordNewPassword').focus();
    $('#resetPasswordError').empty();
    $('#resetPasswordError').hide();
});
$('#resetPassword').click(function() {
    $('#resetPasswordModal').submit();
});
$('#resetPasswordModal').submit(function (event) {
    if (!$('#resetPasswordModal').valid()) {
        $('#resetPasswordError').show();
    }
    else {
        event.preventDefault();
        var $form = $(this), url = $form.attr('action');
        var formData = {
            "userName":$('#resetPasswordUserName').val(),
            "newPassword":$('#resetPasswordNewPassword').val()
        };
        $.post(url, formData, function (response) {
                if (response == '') {
                    $('#resetPasswordModal').modal('hide');
                    $('#resetPasswordModal').trigger('resetPasswordSuccess', [$('#resetPasswordUserName').val()]);
                }
                else {
                    $('#resetPasswordServerSideError').text(response);
                    $('#resetPasswordServerSideError').show();
                }
            }
        );
    }
});

$('#resetPassword').change(function () {
    $('#resetPasswordServerSideError').hide();
});

$('#resetPasswordModal').validate({
    rules:{
        resetPasswordNewPassword:{
            required:true,
            minlength:4
        },
        resetPasswordConfirmNewPassword:{
            required:true,
            equalTo:'#resetPasswordNewPassword'
        }
    },
    messages:{
        resetPasswordNewPassword:{
            required:"'New Password' cannot be empty",
            minlength:"'New Password' should be at least 4 characters long",
            notEqualTo:"'New Password' should not be the same as the 'Current Password'"
        },
        resetPasswordConfirmNewPassword:{
            required:"'Confirm New Password' cannot be empty",
            equalTo:"'Confirm New Password' should match 'New Password'"
        }
    },
    errorPlacement:function (error, element) {
        $('#resetPasswordError').append(error);
    },
    errorLabelContainer:'#resetPasswordError'
});
