<#macro changePassword>
    <div class="modal hide fade" id="changePasswordModal">
        <div class="modal-header">
            <button class="close" data-dismiss="modal">×</button>
            <h3>Change Password</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label" for="j_currentPassword">Current Password *</label>

                <div class="controls">
                    <input class="input-xlarge" type="password" name='j_currentPassword' id="j_currentPassword" autofocus="autofocus"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="j_newPassword">New Password *</label>

                <div class="controls">
                    <input class="input-xlarge" type="password" name='j_newPassword' id="j_newPassword"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="j_confirmNewPassword">Confirm New Password *</label>

                <div class="controls">
                    <input class="input-xlarge" type="password" name='j_confirmNewPassword' id="j_confirmNewPassword"/>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" data-dismiss="modal">Close</a>
            <a href="#" class="btn btn-primary">Save</a>
        </div>
    </div>
</#macro>