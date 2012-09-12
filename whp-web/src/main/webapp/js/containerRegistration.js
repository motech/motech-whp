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
		},
		messages: {
			containerId: {
				required: "Please enter the container id",
				minlength: "Container Id must be of 10 digits in length",
				maxlength: "Container Id must be of 10 digits in length"
			},
			instance: {
				required: "Please select the instance"
			},
		}
	});

    createAutoClosingAlert(".container-registration-message-alert", 5000);

    $("#containerId").focus();
});
