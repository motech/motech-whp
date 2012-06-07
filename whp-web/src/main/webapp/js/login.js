$(document).ready(function() {
    $.validator.addMethod('lowercase', function (value, element, param){
        return !(/.[!,:,@,#,$,%,^,&,*,?,_,~]/.test(value)
                || /[A-Z]/.test(value));
    }, 'Only lower case allowed');

    $('#loginForm').validate({
        rules: {
            j_username: {
                required: true,
                lowercase: true
            },
            j_password:  {
                required: true
            }
        },
        messages: {
            j_username: {
                required: "Username cannot be empty",
                lowercase: "Username cannot have upper case or special characters."
            },
            j_password: {
                required: "Password cannot be empty"
            }
        },
        errorPlacement: function(error, element) {
            $('#loginError').append(error);
        },
        errorLabelContainer: '#loginError'
    });
});
