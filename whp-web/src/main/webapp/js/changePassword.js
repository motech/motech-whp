$('#changePasswordModal').on('show', function() {
    $('#validationError').empty();
    $('#changePasswordModal').find("input").val("");
    $('#validationError').hide();
});

$('#changePassword').click(function () {
    $('#changePasswordModal').validate({
        rules: {
            currentPassword: "required",
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
            currentPassword: "Please enter 'Current Password'",
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
            $('#validationError').append(error);
        },
        errorLabelContainer: '#validationError'
    });
    if (!$('#changePasswordModal').valid()) {
        $('#validationError').show();
    }
});