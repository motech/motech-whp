<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<@layout.defaultLayout title="Container-Registration" entity="provider">
<script type="text/javascript"
        src="<@spring.url '/resources-${applicationVersion}/js/containerRegistration.js'/>"></script>
    <#if errors??>
    <div id="container-registration-error" class="container-registration-message-alert row alert alert-error fade in">
        <button class="close" data-dismiss="alert">&times;</button>
        <#list errors as error>
            <div><@spring.messageArgs code=error.code args=error.parameters/></div>
        </#list>
    </div>
    </#if>
    <#if message??>
    <div id="container-registration-confirmation"
         class="container-registration-message-alert alert alert-success fade in">
        <button class="close" data-dismiss="alert">&times;</button>
    ${message}
    </div>
    </#if>
<h1>Container Registration</h1>

<div id="container-registration">
    <form id="container-registration-form" autocomplete="off"
          action="<@spring.url '/containerRegistration/by_provider/register'/>" class="well form-horizontal" input
          method="POST" submitOnEnterKey="true">
        <table class="controls">
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label">Container ID*</label>

                        <div class="controls">
                            <@spring.bind "containerRegistrationRequest.containerId" />
                            <input id="containerId" class="span" name="${spring.status.expression}" type="text"
                                   value="${spring.status.value?default("")}" maxlength="5"
                                   placeholder="Enter last 5 characters"/>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label">Instance*</label>

                        <div class="controls">
                            <@spring.bind "containerRegistrationRequest.instance" />
                            <select id="instance" name="${spring.status.expression}" validate="required:true">
                                <option value=""></option>
                                <#list instances as instance>
                                    <option value="${instance}"
                                            <#if spring.status.value?default("") == instance>selected="true"</#if>>${instance}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <div class="control-group">
                    <label class="control-label">Patient Name</label>

                    <div class="controls">
                        <@spring.bind "containerRegistrationRequest.patientName" />
                        <input id="patientName" class="span" name="${spring.status.expression}" type="text"
                               value="${spring.status.value?default("")}" placeholder="Enter patient Name"/>
                    </div>
                </div>
            </tr>
            <tr>
                <div class="control-group">
                    <label class="control-label">Patient ID</label>

                    <div class="controls">
                        <@spring.bind "containerRegistrationRequest.patientId" />
                        <input id="patientId" class="span" name="${spring.status.expression}" type="text"
                               value="${spring.status.value?default("")}" placeholder="Enter patient ID"/>
                    </div>
                </div>
            </tr>
            <tr>
                <div class="control-group">
                    <label class="control-label">Age</label>

                    <div class="controls">
                        <@spring.bind "containerRegistrationRequest.age" />
                        <input id="age" class="span" name="${spring.status.expression}" type="text" value="${spring.status.value?default("")}"  placeholder="Enter Age"/>
                    </div>
                </div>
            </tr>
            <tr>
                <div class="control-group">
                    <label class="control-label">Gender</label>
                    <div class="controls">
                        <@spring.bind "containerRegistrationRequest.gender" />
                        <select id="gender" name="${spring.status.expression}">
                            <option value=""></option>
                            <#list genders as gender>
                                <option value="${gender.name()}"
                                        <#if spring.status.value?default("") == gender.name()>selected="true"</#if>>${gender.getValue()}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </tr>

            <tr>
                <td>
                    <div class="control-group pull-down">
                        <div class="controls">
                            <button type="submit" id="registerButton" class="btn btn-primary">
                                Register
                            </button>
                            <a id="back" class="btn" href="<@spring.url ''/>">Back</a>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</div>
</@layout.defaultLayout>