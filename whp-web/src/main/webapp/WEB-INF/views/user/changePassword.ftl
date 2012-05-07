<#macro changePassword>
    <input type="hidden" id="userPassword" value="${Session.loggedInUser.password}" />
    <form class="modal hide fade" id="changePasswordModal" action="<@spring.url '/changePassword'/>">
        <div class="modal-header">
            <button class="close" data-dismiss="modal">×</button>
            <h3>Change Password</h3>
        </div>
        <div class="modal-body">
            <div id="changePasswordError" class="alert alert-error hide"></div>
            <div class="control-group">
                <label class="control-label" for="currentPassword">Current Password *</label>

                <div class="controls">
                    <input class="input-xlarge" type="password" name='currentPassword' id="currentPassword" autofocus="autofocus"/>
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
            <a href="#" class="btn" data-dismiss="modal">Close</a>
            <button type="submit" class="btn btn-primary" id="changePassword">Save</button>
        </div>
    </form>
    <script type="text/javascript" src="<@spring.url '/resources/js/changePassword.js'/>"></script>
</#macro>