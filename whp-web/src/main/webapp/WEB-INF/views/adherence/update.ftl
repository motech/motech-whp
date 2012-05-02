<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Update Adherence">
<div class="row">

    <div class="well page-header"><h3>Update last week's adherence</h3></div>

    <form action="<@spring.url '/adherence/update/' + patientId/>" method="POST">
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
                <#list weeklyAdherenceLog.allDailyAdherenceLogs as dailyAdherenceLog>
                <tr>
                    <td>
                        ${dailyAdherenceLog.pillDay}
                        <input type="hidden" name="allDailyAdherenceLogs[${dailyAdherenceLog_index}].pillDay" value="${dailyAdherenceLog.pillDay}"/>
                    </td>
                    <td>
                        ${dailyAdherenceLog.pillDateString}
                        <input type="hidden" name="allDailyAdherenceLogs[${dailyAdherenceLog_index}].pillDateString" value="${dailyAdherenceLog.pillDateString}"/>
                    </td>
                    <td>
                        <input type="radio" name="allDailyAdherenceLogs[${dailyAdherenceLog_index}].pillStatus" value="Taken" <#if dailyAdherenceLog.isTaken> selected </#if>/>
                    </td>
                    <td>
                        <input type="radio" name="allDailyAdherenceLogs[${dailyAdherenceLog_index}].pillStatus" value="NotTaken" <#if dailyAdherenceLog.isNotTaken> selected </#if>/>
                    </td>
                </tr>
                </#list >
            </tbody>
        </table>
        <div class="control-group">
            <div class="controls pull-right">
                <input type="submit" class="btn btn-primary login-btn"/>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
