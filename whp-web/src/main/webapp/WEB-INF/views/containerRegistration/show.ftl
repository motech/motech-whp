    <#if message??>
    <div class="dateUpdated-message-alert row alert alert-info fade in">
        <button class="close" data-dismiss="alert">&times;</button>
    ${message}
    </div>
    </#if>
<#import "/spring.ftl" as spring />
        <div id="container-registration">
            <form id="container-registration-form" action="<@spring.url '/containerRegistration/register'/>" input method="POST" submitOnEnterKey="true"
                  class="">
                <table>
                    <tr>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Container ID*</label>

                                <div class="controls">
                                    <input id="container_id" class="span" name="container_id" type="text"/>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="control-group">
                                <label class="control-label">Instance</label>

                                <div class="controls">
                                    <select id="instance" name="instance">
                                           <#list instances as instance>
                                              <option value="${instance}">${instance}</option>
                                           </#list>
                                    </select>
                                </div>
                            </div>
                        </td>

                        <td>
                            <div class="control-group pull-down">
                                <div class="controls">
                                    <button type="submit" id="registerButton" class="btn btn-primary form-button-center">
                                        Register
                                    </button>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </table>
            </form>
        </div>
