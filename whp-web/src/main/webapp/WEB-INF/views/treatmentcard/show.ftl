<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>

<#if treatmentCard?exists>

<div class="row-fluid">
    <div id="legend-container" class="pull-right">
        <div id="patient-id" class="hide">${patientId}</div>
        <#include "legend.ftl"/>
    </div>
</div>
    <#if treatmentCard.IPAdherenceSectionValid == true>
    <div>
        <h3 id="ipTreatmentCardHeading">Intensive Phase</h3>
        <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences />
    </div>
    </#if>
    <#if treatmentCard.CPAdherenceSectionValid == true>
    <div>
        <h3 id="cpTreatmentCardHeading">Continuation Phase</h3>
        <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences />
    </div>
    </#if>

<form id="treatmentCardDeltaform" action="/whp/treatmentcard/update" method="post">
    <input type="hidden" name="delta" id="delta"/>

    <#if treatmentCard.IPAdherenceSectionValid == true || treatmentCard.CPAdherenceSectionValid == true>
        <div class="controls pull-right">
            <a href="/whp/patients/show?patientId=${patientId}" class="btn">Clear</a>
            <button type="button" id='submitAdherence' class="btn btn-primary">Save</button>
        </div>
    </#if>
</form>

</#if>