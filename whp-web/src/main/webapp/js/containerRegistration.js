$().ready(function() {
    $.metadata.setType("attr", "validate");

	$("#container-registration-form").validate({
		rules: {
			containerId: {
			                required: true,
                         	digits: true
                         },
            providerId: {
                    required: true
            }
		},
		messages: {
			containerId: {
				required: "Please enter the container id",
			},
		    providerId: {
		        required: "Please enter the provider id"
		    },
			instance: {
				required: "Please select the instance"
			},
			patientName: {
				required: "Please select the patient name"
			},
			patientId: {
				required: "Please select the patient id"
			},
			gender: {
				required: "Please select the gender"
			},
			age: {
				required: "Please select the age"
			}
		}
	});

    createAutoClosingAlert(".container-registration-message-alert", 5000);
    $('input:text:first').focus();
});
