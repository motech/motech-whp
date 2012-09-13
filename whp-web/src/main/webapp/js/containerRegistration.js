$().ready(function() {
    $.metadata.setType("attr", "validate");

	$("#container-registration-form").validate({
		rules: {
			containerId: {
			                required: true,
                         	minlength: 10,
                         	maxlength: 10,
                         	digits: true
                         },
            providerId: {
                    required: true,
            },
		},
		messages: {
			containerId: {
				required: "Please enter the container id",
				minlength: "Container Id must be of 10 digits in length",
				maxlength: "Container Id must be of 10 digits in length"
			},
		    providerId: {
		        required: "Please enter the provider id"
		    },
			instance: {
				required: "Please select the instance"
			},
		}
	});

    createAutoClosingAlert(".container-registration-message-alert", 5000);
    $('input:text:first').focus();
});
