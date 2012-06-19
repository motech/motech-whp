<#import "/spring.ftl" as spring />
<#import "../layout/default-itadmin.ftl" as layout>
<@layout.defaultLayout "Create CMF Admin Account">
    <form action="<@spring.url '/cmfAdmin/edit'/>" method="POST" class="row well form-horizontal">
        <div class="offset4">
            <div class="control-group">
                <label class="control-label" for="staffName">Staff Name*</label>
                <div class="controls">
                    <input name="staffName" id="staffName" value="${cmfAdmin.staffName}"/>
                    <div class="field-error">
                        <@spring.bind "cmfAdmin.staffName" />
                        <@spring.showErrors ',' />
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="email">Email</label>
                <div class="controls">
                    <input name="email" id="email" value="${cmfAdmin.email}"/>
                </div>
                <div class="field-error">
                    <@spring.bind "cmfAdmin.email" />
                    <@spring.showErrors ',' />
                </div>

            </div>
            <div class="control-group">
                <label class="control-label" for="department">Department</label>
                <div class="controls">
                    <input name="department" id="department" value="${cmfAdmin.department}"/>
                </div>
                <div class="field-error">
                    <@spring.bind "cmfAdmin.department" />
                    <@spring.showErrors ',' />
                </div>

            </div>
            <div class="control-group">
                <label class="control-label" for="locationId">Location*</label>
                <div class="controls">
                    <select name="locationId" id="locationId">
                    <#list locations as location>
                        <option <#if cmfAdmin.locationId == location> selected </#if> value="${location}">${location}</option>
                    </#list>
                    </select>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <a href="<@spring.url "/cmfAdmin/list"/>" class="btn btn-primary">Cancel</a>
                    <button type="submit" class="btn btn-primary" style="margin-right: 504px; float:right">Update</button>
                </div>
            </div>
            <input type="hidden" id="id" name="id" value="${cmfAdmin.id}">
        </div>
    </form>
</@layout.defaultLayout>