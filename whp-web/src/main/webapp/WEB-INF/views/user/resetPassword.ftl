<#macro resetPassword>
<form class="modal hide" id="resetPasswordModal" action="<@spring.url "/resetPassword"/>">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Reset Password</h3>
    </div>
    <div class="modal-body">
        Are you sure you want to reset password for the user <p id='resetPasswordUserName' class="user-name"></p> ?
        <div id="resetPasswordServerSideError" class="alert alert-error hide"></div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal" id="resetPasswordClose">Cancel</button>
        <button type="button" class="btn btn-group btn-primary" id="resetPassword">OK</button>
    </div>
</form>
<script type="text/javascript">
    $(function () {
        $('#resetPassword').click(function () {
            $('#resetPasswordModal').submit();
        });

        $('#resetPasswordModal').submit(function (event) {
            event.preventDefault();
            var formData = {
                "userName":$('#resetPasswordUserName').text()
            };
            $.post($(this).attr('action'), formData,
                    function (response) {
                        if (response == '') {
                            $('#resetPasswordModal').modal('hide');
                            $('#resetPasswordModal').trigger('resetPasswordSuccess', [$('#resetPasswordUserName').text()]);
                        }
                        else {
                            $('#resetPasswordServerSideError').text(response);
                            $('#resetPasswordServerSideError').show();
                        }
                    }
            );
        });
    });
</script>
</#macro>