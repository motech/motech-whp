<#macro resetPassword>
<form class="modal hide" id="resetPasswordModal" submitOnEnterKey="true" action="<@spring.url "/activateUser"/>">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Activate Web User</h3>
    </div>
    <div class="modal-body">
        <div id="resetPasswordServerSideError" class="alert alert-error hide"></div>
        <div id="resetPasswordError" class="alert alert-error hide"></div>
        <div class="control-group">
            <label class="float-left" for="resetPasswordUserName">User Name :&nbsp;</label>
            <label id="resetPasswordUserNameLabel" class="control-label reset-password-username"></label>
            <div class="controls">
                <input class="input-xlarge" type="hidden" name='userName' id="resetPasswordUserName"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="resetPasswordNewPassword">Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='resetPasswordNewPassword' id="resetPasswordNewPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="resetPasswordConfirmNewPassword">Confirm Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='resetPasswordConfirmNewPassword' id="resetPasswordConfirmNewPassword"/>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal" id="resetPasswordClose">Close</button>
        <button type="button" class="btn btn-group btn-primary" id="resetPassword">Save</button>
    </div>
</form>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/resetPassword.js'/>"></script>
</#macro>