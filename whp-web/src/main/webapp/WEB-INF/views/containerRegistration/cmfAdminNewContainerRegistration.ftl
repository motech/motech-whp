<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<@layout.defaultLayout title="Container-Registration" entity="cmfadmin">
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/containerRegistration.js'/>"></script>
    <#if errors??>
            <div id="container-registration-error" class="container-registration-message-alert row alert alert-error fade in">
                <button class="close" data-dismiss="alert">&times;</button>
                <#list errors as error>
                    <div><@spring.messageArgs code=error.code args=error.parameters/></div>
                </#list>
            </div>
    </#if>
    <#if message??>
            <div id="container-registration-confirmation" class="container-registration-message-alert alert alert-success fade in">
                <button class="close" data-dismiss="alert">&times;</button>
                ${message}
            </div>
    </#if>
        <h1>Register a new container</h1>

        <div id="container-registration" class="offset1-fixed">
            <form id="container-registration-form"  autocomplete="off" action="<@spring.url '/containerRegistration/by_cmfAdmin/register'/>" input method="POST" submitOnEnterKey="true" class="form-horizontal">
                <div class="well">
                    <input type="hidden" id="providerContainer" name="containerRegistrationMode" value="NEW_CONTAINER"/>

                    <div class="control-group">
                        <label class="control-label">Container ID*</label>

                        <div class="controls">
                            <input id="containerId" class="input-xlarge" name="containerId" type="text" maxlength="${containerIdMaxLength}" placeholder="Enter a container ID from CMF admin pool"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Assign to Provider*</label>

                        <div class="controls">
                            <input id="providerId" class="input-xlarge" name="providerId" type="text" placeholder="Enter provider ID"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Instance*</label>

                        <div class="controls">
                            <select id="instance" name="instance" class="select-xlarge" validate="required:true">
                                <option value=""></option>
                                <#list instances as instance>
                                    <option value="${instance}">${instance}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="control-group pull-right">
                    <div class="controls">

                        <button type="submit" id="registerButton" class="btn btn-primary">
                            <i class="icon-ok icon-white"></i> Register Container
                        </button>
                        <a id="back" class="btn padding-right" href="<@spring.url ''/>"><i class="icon-remove"></i> Cancel</a>
                    </div>
                </div>
            </form>
        </div>
</@layout.defaultLayout>