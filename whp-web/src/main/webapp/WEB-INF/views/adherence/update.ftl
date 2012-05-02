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
                    <td>${dailyAdherenceForm.pillDay}</td>
                    <td><input type="checkbox"
                               name="weeklyAdherenceForm.allDailyAdherenceForms[${dailyAdherenceForm_index}].taken" <#if dailyAdherenceForm.taken>
                               checked </#if> /></td>
                    <td><input type="checkbox"
                               name="weeklyAdherenceForm.allDailyAdherenceForms[${dailyAdherenceForm_index}].notTaken" <#if dailyAdherenceForm.notTaken>
                               checked </#if>/></td>
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
