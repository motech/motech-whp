<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Update Adherence">
<div class="row">

    <div class="well page-header"><h3>Update last week's adherence</h3></div>

    <form id="adherenceForm" action="<@spring.url '/adherence/update/' + patientId/>" method="POST">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>
                    Day of week
                </th>
                <th>
                    Date
                </th>
                <th>
                    Taken
                </th>
                <th>
                    Not taken
                </th>
            </tr>
            </thead>
            <tbody>
                <#list adherence.adherenceLogs as adherenceLog>
                <tr class="adherenceRow">
                    <td>
                    ${adherenceLog.pillDay}
                        <input type="hidden" name="adherenceLogs[${adherenceLog_index}].pillDay" class="pillDay"
                               value="${adherenceLog.pillDay}"/>
                    </td>
                    <td>
                    ${adherenceLog.pillDateString}
                        <input type="hidden" name="adherenceLogs[${adherenceLog_index}].pillDateString"
                               value="${adherenceLog.pillDateString}"/>
                    </td>
                    <td>
                        <input type="radio" name="adherenceLogs[${adherenceLog_index}].pillStatus"
                               class="pillStatusTaken"
                               value="Taken" <#if adherenceLog.isTaken> checked </#if> <#if readOnly > disabled </#if>/>
                    </td>
                    <td>
                        <input type="radio" name="adherenceLogs[${adherenceLog_index}].pillStatus"
                               class="pillStatusNotTaken"
                               value="NotTaken" <#if adherenceLog.isNotTaken> checked </#if> <#if readOnly >
                               disabled </#if>/>
                    </td>
                </tr>
                </#list >
            </tbody>
        </table>
        <div class="control-group">
            <div class="controls pull-right">
                <#if !readOnly>
                    <input type="submit" class="btn btn-primary login-btn"/>
                </#if>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
