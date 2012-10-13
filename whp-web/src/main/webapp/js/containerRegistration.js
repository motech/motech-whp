$().ready(function() {
    $.metadata.setType("attr", "validate");

    var containerIdLength = $("#containerId").attr("maxlength");

	$("#container-registration-form").validate({
		rules: {
			containerId: {
			                required: true,
                         	minlength: containerIdLength,
                         	maxlength: containerIdLength,
                         	digits: true
                         },
            providerId: {
                    required: true
            }
		},
		messages: {
			containerId: {
				required: "Please enter the container id",
				minlength: "Container Id must be of " + containerIdLength + " digits in length",
				maxlength: "Container Id must be of " + containerIdLength + " digits in length"
			},
		    providerId: {
		        required: "Please enter the provider id"
		    },
			instance: {
				required: "Please select the instance"
			}
		}
	});

    createAutoClosingAlert(".container-registration-message-alert", 5000);
    $('input:text:first').focus();
});
