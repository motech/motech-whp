<#macro activateProvider>
<form class="modal hide" id="activateProviderModal" submitOnEnterKey="true" action="<@spring.url "/activateUser"/>">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Activate Web User</h3>
    </div>
    <div class="modal-body">
        <div id="activateProviderServerSideError" class="alert alert-error hide"></div>
        <div id="activateProviderError" class="alert alert-error hide"></div>
        <div class="control-group">
            <label class="float-left" for="activateProviderUserName">User Name :&nbsp;</label>
            <label id="activateProviderUserNameLabel" class="control-label activate-provider-username"></label>
            <div class="controls">
                <input class="input-xlarge" type="hidden" name='userName' id="activateProviderUserName"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="activateProviderNewPassword">Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='activateProviderNewPassword' id="activateProviderNewPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="activateProviderConfirmNewPassword">Confirm Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='activateProviderConfirmNewPassword' id="activateProviderConfirmNewPassword"/>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal" id="activateProviderClose">Close</button>
        <button type="button" class="btn btn-group btn-primary" id="activateProvider">Save</button>
    </div>
</form>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/activateProvider.js'/>"></script>
</#macro>