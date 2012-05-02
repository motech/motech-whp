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
                <#list weeklyAdherenceForm.allDailyAdherenceForms as dailyAdherenceForm>
                <tr>
                    <td>
                        ${dailyAdherenceForm.pillDay}
                        <input type="hidden" name="allDailyAdherenceForms[${dailyAdherenceForm_index}].pillDay" value="${dailyAdherenceForm.pillDay}"/>
                    </td>
                    <td>
                        ${dailyAdherenceForm.pillDateString}
                        <input type="hidden" name="allDailyAdherenceForms[${dailyAdherenceForm_index}].pillDateString" value="${dailyAdherenceForm.pillDateString}"/>
                    </td>
                    <td>
                        <input type="radio" name="allDailyAdherenceForms[${dailyAdherenceForm_index}].pillStatus" value="Taken" <#if dailyAdherenceForm.isTaken> selected </#if>/>
                    </td>
                    <td>
                        <input type="radio" name="allDailyAdherenceForms[${dailyAdherenceForm_index}].pillStatus" value="NotTaken" <#if dailyAdherenceForm.isNotTaken> selected </#if>/>
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
