$(function () {
    $('#editCmfAdminForm').submit(function (event) {
        if (!$('#editCmfAdminForm').valid()) {
            event.preventDefault();
            return false;
        }
    });

    $('#editCmfAdminForm input').change(function () {
        $('#editCmfAdminForm').valid();
    });

    $('#editCmfAdminForm').validate({
        rules:{
            staffName:{
                required:true
            },
            email:{
                required:true,
                regex:"^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$"
            }

        },
        messages:{
            staffName:{
                required:"'Staff Name' cannot be empty"
            },
            email:{
                required:"'Email' cannot be empty",
                regex:"Please enter a valid email id. Example: abc@xyz.com"
            }
        },
        errorPlacement:function (error, element) {
            $($(element).parent().find('.field-error')[0]).append(error);
        },
        success:function (element) {
            $($(element).parent().find('b')[0]).hide();
        },
        errorLabelContainer:$(this).parent().find('.field-error')

    });
});