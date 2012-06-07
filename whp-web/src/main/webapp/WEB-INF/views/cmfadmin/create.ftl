<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Create CMF Admin Account">
<div class="span12" id="createCmfAdminForm">
    </br>
    <form action="<@spring.url '/cmfAdmin/create'/>" method="POST" class="row well form-horizontal">
        <div class="offset2">

            <div class="control-group">
                <label class="control-label" for="staffName">Staff Name*</label>

                <div class="controls">
                    <@spring.formInput "account.staffName","class='input-xlarge' autocomplete='off'","text"/>
                    <div class="field-error">
                        <@spring.bind "account.staffName" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="userId">User ID*</label>

                <div class="controls">
                    <@spring.formInput "account.userId","class='input-xlarge' autocomplete='off'","text"/>
                    <div class="field-error">
                        <@spring.bind "account.userId" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="password">Password*</label>

                <div class="controls">
                    <@spring.formInput "account.password","class='input-xlarge' autocomplete='off'","password"/>
                    <div class="field-error">
                        <@spring.bind "account.password" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="confirmPassword">Confirm Password*</label>

                <div class="controls">
                    <@spring.formInput "account.confirmPassword","class='input-xlarge' autocomplete='off'","password"/>
                    <div class="field-error">
                        <@spring.bind "account.confirmPassword" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="email">Email*</label>

                <div class="controls">
                    <@spring.formInput "account.email","class='input-xlarge' autocomplete='off'","text"/>
                    <div class="field-error">
                        <@spring.bind "account.email" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="department">Department</label>

                <div class="controls">
                    <@spring.formInput "account.department","class='input-xlarge' autocomplete='off'","text"/>
                    <div class="field-error">
                        <@spring.bind "account.department" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>

            <#if locations?size != 0>
                <div class="control-group">
                    <label class="control-label" for="location">Location*</label>

                    <div class="controls">
                        <@spring.formSingleSelect "account.location",locations,"class='select-xlarge' autocomplete='off'"/>
                        <div class="error">
                            <@spring.bind "account.location" />
                            <@spring.showErrors ',' />
                        </div>
                    </div>
                </div>
            </#if>

            <div class="control-group">
                <div class="controls">
                    <a href="<@spring.url "/"/>" class="btn btn-primary form-button-left">Cancel</a>
                    <button type="submit" class="btn btn-primary form-button-right">Create Account</button>
                </div>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
