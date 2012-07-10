$('#activateProviderModal').on('shown', function () {
    $('#activateProviderNewPassword').focus();
    $('#activateProviderError').empty();
    $('#activateProviderError').hide();
});
$('#activateProvider').click(function() {
    $('#activateProviderModal').submit();
});
$('#activateProviderModal').submit(function (event) {
    if (!$('#activateProviderModal').valid()) {
        $('#activateProviderError').show();
    }
    else {
        event.preventDefault();
        var $form = $(this), url = $form.attr('action');
        var formData = {
            "userName":$('#activateProviderUserName').val(),
            "newPassword":$('#activateProviderNewPassword').val()
        };
        $.post(url, formData, function ( response) {
                if (response == '') {
                    $('#activateProviderModal').modal('hide');
                    $('#activateProviderModal').trigger('activateProviderSuccess', [$('#activateProviderUserName').val()]);
                }
                else {
                    $('#activateProviderServerSideError').text(response);
                    $('#activateProviderServerSideError').show();
                }
            }
        );
    }
});

$('#activateProvider').change(function () {
    $('#activateProviderServerSideError').hide();
});

$('#activateProviderModal').validate({
    rules:{
        activateProviderNewPassword:{
            required:true,
            minlength:4
        },
        activateProviderConfirmNewPassword:{
            required:true,
            equalTo:'#activateProviderNewPassword'
        }
    },
    messages:{
        activateProviderNewPassword:{
            required:"'Password' cannot be empty",
            minlength:"'Password' should be at least 4 characters long"
        },
        activateProviderConfirmNewPassword:{
            required:"'Confirm Password' cannot be empty",
            equalTo:"'Confirm Password' should match 'Password'"
        }
    },
    errorPlacement:function (error, element) {
        $('#activateProviderError').append(error);
    },
    errorLabelContainer:'#activateProviderError'
});
