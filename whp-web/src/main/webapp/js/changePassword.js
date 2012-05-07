$('#changePasswordModal').on('show', function() {
    $('#currentPassword').focus();
    $('#changePasswordError').empty();
    $('#changePasswordModal').find("input").val("");
    $('#changePasswordError').hide();
});

$('#changePasswordModal').submit(function(event) {
    if (!$('#changePasswordModal').valid()) {
        $('#changePasswordError').show();
    }
    else {
        event.preventDefault();
        var $form = $(this), url = $form.attr('action');
        $.post(url, $form.serialize(),
            function(data) {
                if (data == '') {
                    $('#changePasswordModal').modal('hide');
                }
            }
        );
    }
});

$('#changePasswordModal').validate({
    rules: {
        currentPassword: {
            required: true,
            equalTo: '#userPassword'
        },
        newPassword:  {
            required: true,
            minlength: 4,
            notEqualTo: '#currentPassword'
        },
        confirmNewPassword: {
            required: true,
            equalTo: '#newPassword'
        }
    },
    messages: {
        currentPassword: {
            required: "Please enter 'Current Password'",
            equalTo: "'Current Password' you entered does not match our records"
        },
        newPassword: {
            required: "Please enter 'New Password'",
            minlength: "'New Password' should at least be 4 characters long",
            notEqualTo: "'New Password' should not be the same as the 'Current Password'"
        },
        confirmNewPassword: {
            required: "Please enter 'Confirm New Password'",
            equalTo: "'Confirm New Password' should match 'New Password'"
        }
    },
    errorPlacement: function(error, element) {
        $('#changePasswordError').append(error);
    },
    errorLabelContainer: '#changePasswordError'
});
