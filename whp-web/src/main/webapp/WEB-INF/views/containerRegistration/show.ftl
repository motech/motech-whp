<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Container-Registration">
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/containerRegistration.js'/>"></script>
    <#if errors??>
            <div id="container-registration-error" class="container-registration-message-alert row alert alert-error fade in">
                <button class="close" data-dismiss="alert">&times;</button>
                ${errors}
            </div>
    </#if>
    <#if message??>
            <div id="container-registration-confirmation" class="container-registration-message-alert row alert alert-info fade in">
                <button class="close" data-dismiss="alert">&times;</button>
                ${message}
            </div>
    </#if>
        <div id="container-registration">
            <form id="container-registration-form" action="<@spring.url '/containerRegistration/register'/>" input method="POST" submitOnEnterKey="true"
                  class="">
                <table>
                    <tr>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Container ID*</label>

                                <div class="controls">
                                    <input id="containerId" class="span" name="containerId" type="text" maxlength="10" />
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
                                    <button type="submit" id="registerButton" class="btn btn-primary form-button-center">
                                        Register
                                    </button>
                                    <a id="back" class="padding-left" href="<@spring.url ''/>">Back</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </table>
            </form>
        </div>
</@layout.defaultLayout>