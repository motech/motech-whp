$('#createCmfAdminForm').submit(function(event) {
    if (!$('#createCmfAdminForm').valid()) {
        event.preventDefault();
        return false;
    }
});
$('#createCmfAdminForm').validate({
    rules: {
        staffName:{
          required:true
        },
        userId:{
          required:true
        },
        password:  {
            required: true,
            minlength: 4
        },
        confirmPassword: {
            required: true,
            equalTo: '#password'
        },
        email:{
           required:true,
           regex: "^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$"
        }

    },
    messages: {
        staffName : {
            required:"'Staff Name' cannot be empty"
        },
        userId : {
            required:"'User ID' cannot be empty"
        },
        password: {
            required: "'Password' cannot be empty",
            minlength: "'Password' should be at least 4 characters long"
        },
        confirmPassword: {
            required: "'Confirm Password' cannot be empty",
            equalTo: "'Confirm Password' should match 'Password'"
        },
        email : {
            required:"'Email' cannot be empty",
            regex:"Please enter a valid email id. Example: abc@xyz.com"
        }
    },
    errorPlacement: function(error, element) {
        $($(element).parent().find('.field-error')[0]).append(error);
    },
    success: function(element) {
        $($(element).parent().find('b')[0]).hide();
    },
    errorLabelContainer: $(this).parent().find('.field-error')

});
