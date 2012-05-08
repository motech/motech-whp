$(function() {
    $('#changePasswordModal input').keypress(function (e) {
        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
            $('#changePasswordModal').submit();
            return false;
        } else {
            return true;
        }
    });
});

$('#changePasswordModal').on('shown', function() {
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
                    $('#userPassword').val($('#newPassword').val());
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
            required: "'Current Password' cannot be empty",
            equalTo: "'Current Password' you entered is incorrect"
        },
        newPassword: {
            required: "'New Password' cannot be empty",
            minlength: "'New Password' should be at least 4 characters long",
            notEqualTo: "'New Password' should not be the same as the 'Current Password'"
        },
        confirmNewPassword: {
            required: "'Confirm New Password' cannot be empty",
            equalTo: "'Confirm New Password' should match 'New Password'"
        }
    },
    errorPlacement: function(error, element) {
        $('#changePasswordError').append(error);
    },
    errorLabelContainer: '#changePasswordError'
});
