$().ready(function() {
    $.metadata.setType("attr", "validate");

	$("#container-registration-form").validate({
		rules: {
			containerId: {
			                required: true,
                         	minlength: 10,
                         	maxlength: 10
                         },
		},
		messages: {
			containerId: {
				required: "Please enter the container id",
				minlength: "Container Id must be of 10 characters length",
				maxlength: "Container Id must be of 10 characters length"
			},
			instance: {
				required: "Please select the instance"
			},
		}
	});
});
