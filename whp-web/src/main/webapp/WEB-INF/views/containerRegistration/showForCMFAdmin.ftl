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
            <div id="container-registration-confirmation" class="container-registration-message-alert row alert alert-info fade in">
                <button class="close" data-dismiss="alert">&times;</button>
                ${message}
            </div>
    </#if>
        <h1>Container Registration</h1>

        <div id="container-registration">
            <form id="container-registration-form"  autocomplete="off" action="<@spring.url '/containerRegistration/by_cmfAdmin/register'/>" input method="POST" submitOnEnterKey="true" class="well form-horizontal">
                <table class="controls">
                    <tr>
                            <td>
								<div class="control-group row margin-bottom-10px margin-left-3px">
									<input type="radio" id="providerContainer" class="radio controls" name="containerRegistrationMode"
										   checked="true" value="ON_BEHALF_OF_PROVIDER"/>
                                    <label class="providerContainer" for="providerContainer">  Register on behalf of provider</label>

									<span class = "margin-left-15px">
                                        <input type="radio" id="newContainer" class="radio" name="containerRegistrationMode" value="NEW_CONTAINER"/>
                                        <label class="newContainer" for="newContainer">  Register a new container</label>
									</span>
								</div>
                            </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Container ID*</label>

                                <div class="controls">
                                    <input id="containerId" class="span" name="containerId" type="text" maxlength="${containerIdMaxLength}" />
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Provider ID*</label>

                                <div class="controls">
                                    <input id="providerId" class="span" name="providerId" type="text"/>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Instance*</label>

                                <div class="controls">
                                    <select id="instance" name="instance" validate="required:true">
                                           <option value=""></option>
                                           <#list instances as instance>
                                              <option value="${instance}">${instance}</option>
                                           </#list>
                                    </select>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="control-group pull-down">
                                <div class="controls">
                                    <a id="back" class="btn padding-right" href="<@spring.url ''/>">Back</a>
                                    <button type="submit" id="registerButton" class="btn btn-primary">
                                        Register
                                    </button>

                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
</@layout.defaultLayout>