<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<@layout.defaultLayout "Edit CMF Admin Account">

<div class="span12">
    <form action="<@spring.url '/cmfAdmin/edit'/>" id="editCmfAdminForm" submitOnEnterKey="true" method="POST"
          class="form-horizontal">
        <div class="offset2-fixed">
            <div class="control-group well">
                <div class="control-group">
                    <label class="control-label" for="staffName">Staff Name*</label>

                    <div class="controls">
                        <@spring.formInput "cmfAdmin.staffName","class='input-xlarge' autocomplete='off'","text"/>

                        <div class="field-error">
                            <@spring.bind "cmfAdmin.staffName" />
                        <@spring.showErrors ',' />
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="email">Email*</label>

                    <div class="controls">
                        <@spring.formInput "cmfAdmin.email","class='input-xlarge' autocomplete='off'","text"/>

                        <div class="field-error">
                            <@spring.bind "cmfAdmin.email" />
                    <@spring.showErrors ',' />
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="department">Department</label>

                    <div class="controls">
                        <@spring.formInput "cmfAdmin.department","class='input-xlarge' autocomplete='off'","text"/>
                        <div class="field-error">
                            <@spring.bind "cmfAdmin.department" />
                    <@spring.showErrors ',' />
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="locationId">Location*</label>

                    <div class="controls">
                        <@spring.formSingleSelect "cmfAdmin.locationId",locations,"class='select-xlarge' autocomplete='off'"/>
                        <div class="error">
                            <@spring.bind "cmfAdmin.locationId" />
                        <@spring.showErrors ',' />
                        </div>
                    </div>
                </div>
            </div>
            <div class="control-group pull-right">
                <div class="controls">
                    <a href="<@spring.url "/cmfAdmin/list"/>" class="btn">Cancel</a>
                    <button type="submit" class="btn btn-primary">Update</button>
                </div>
            </div>

            <input type="hidden" id="id" name="id" value="${cmfAdmin.id}">
        </div>
    </form>
</div>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/updateCmfAdmin.js'/>"></script>

</@layout.defaultLayout>