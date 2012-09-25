<#import "/spring.ftl" as spring />
<#import "../layout/default-with-sidebar.ftl" as layout>
<@layout.defaultLayout "Container-Registration">
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
        <div id="container-registration">
            <form id="container-registration-form"  autocomplete="off" action="<@spring.url '/containerRegistration/by_cmfAdmin/register'/>" input method="POST" submitOnEnterKey="true" class="row well form-horizontal">
                <table class="controls">
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
                        <div class="control-group row">
                            <td>
                                <input type="radio" class="radio controls" name="containerRegistrationMode"
                                       checked="true" value="ON_BEHALF_OF_PROVIDER"> Register on behalf of provider</input>
                            </td>
                        </div>
                    </tr>
                    <tr>
                        <div class="control-group row">
                            <td>
                                <input type="radio" class="radio controls" name="containerRegistrationMode"
                                       value="NEW_CONTAINER"> Register a new container</input>
                            </td>
                        </div>
                    </tr>
                    <tr>
                        <td>
                            <div class="control-group pull-down">
                                <div class="controls">
                                    <button type="submit" id="registerButton" class="btn btn-primary form-button-center">
                                        Register
                                    </button>
                                    <a id="back" class="btn btn-primary padding-left" href="<@spring.url ''/>">Back</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
</@layout.defaultLayout>