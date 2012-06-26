<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout "Update Adherence">
<div class="row">
    <#if adherence.warningMessage != ''>
        <div id="adherenceWarning" class="alert alert-error">
        ${adherence.warningMessage}
        </div>
    </#if>
    <h4 class="page-header form-header" id="adherenceCaption">Update last week's adherence for Patient ID: ${adherence.patientId}</h4>
</div>

<div class="row">
    <form id="weeklyAdherenceForm" action="<@spring.url '/adherence/update/' + adherence.patientId/>" method="POST">
        <input type="hidden" name="patientId" value="${adherence.patientId}"/>
        <input type="hidden" name="referenceDateString" value="${referenceDate}"/>
        <input type="hidden" name="categoryCode" value="${categoryCode}"/>
        <div id="numberSelect" class="well control-group">
            <label class="pull-left span7 offset1"><b>How many doses did the patient take in the last week?</b></label>
            <select id="dosesTaken" name="numberOfDosesTaken" <#if readOnly> disabled </#if>>
                <#list 0..totalDoses as number>
                    <option <#if adherence.numberOfDosesTaken == number> selected </#if> value="${number}">${number}</option>
                </#list>
            </select>
            <div>
                <label class="pull-left span7 offset1"><b>Remarks:</b></label>
                <textarea id="remarks" name="remarks" <#if readOnly> disabled class="disable-remarks"</#if>></textarea>
            </div>
        </div>
        <div class="control-group">
            <div class="controls pull-right">
                <a href="<@spring.url ''/>" class="btn">Cancel</a>
                <#if !readOnly>
                    <input type="submit" class="btn btn-primary login-btn" value="Submit"/>
                </#if>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
