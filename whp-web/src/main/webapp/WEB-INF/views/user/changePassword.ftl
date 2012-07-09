<#macro changePassword>
<form class="modal hide" id="changePasswordModal" submitOnEnterKey="true" action="<@spring.url '/changePassword'/>">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Change Password</h3>
    </div>
    <div class="modal-body">
        <div id="changePasswordServerSideError" class="alert alert-error hide"></div>
        <div id="changePasswordError" class="alert alert-error hide"></div>
        <div class="control-group">
            <label class="control-label" for="currentPassword">Current Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='currentPassword' id="currentPassword"
                       autofocus="autofocus"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="newPassword">New Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='newPassword' id="newPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="confirmNewPassword">Confirm New Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='confirmNewPassword' id="confirmNewPassword"/>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal">Close</button>
        <button type="submit" class="btn btn-group btn-primary" id="changePassword">Save</button>
    </div>
</form>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/changePassword.js'/>"></script>
</#macro>