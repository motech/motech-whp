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
        <h1>Register on behalf of provider</h1>

        <div id="container-registration" class="offset1-fixed">
            <form id="container-registration-form"  autocomplete="off" action="<@spring.url '/containerRegistration/by_cmfAdmin/register'/>" input method="POST" submitOnEnterKey="true" class="form-horizontal">
                <div class="well">
                    <input type="hidden" id="providerContainer" name="containerRegistrationMode" value="ON_BEHALF_OF_PROVIDER"/>

                    <div class="control-group">
                        <label class="control-label">Provider ID*</label>

                        <div class="controls">
                            <@spring.bind "containerRegistrationRequest.providerId" />
                            <input id="providerId" class="span" name="${spring.status.expression}" type="text" value="${spring.status.value?default("")}"  placeholder="Enter provider ID"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Container ID*</label>

                        <div class="controls">
                            <@spring.bind "containerRegistrationRequest.containerId" />
                            <input id="containerId" class="span" name="${spring.status.expression}" type="text" value="${spring.status.value?default("")}" placeholder="Enter a container ID from provider's pool"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Instance*</label>
                        <div class="controls">
                            <@spring.bind "containerRegistrationRequest.instance" />
                            <select id="instance" name="${spring.status.expression}" validate="required:true">
                                <option value=""></option>
                                <#list instances as instance>
                                    <option value="${instance}" <#if spring.status.value?default("") == instance>selected="true"</#if>>${instance}</option>
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