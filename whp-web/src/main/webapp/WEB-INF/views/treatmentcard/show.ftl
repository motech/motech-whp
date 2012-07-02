<#import "/spring.ftl" as spring />
<#include "legend.ftl"/>
<#include "adherenceBox.ftl"/>

<#if treatmentCard?exists>
<div id="legend-container" class="pull-right">
    <div id="patient-id" class="hide">${patientId}</div>
    <#include "adherenceBox.ftl"/>
</div>
<br/>

<@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAdherenceSection.monthlyAdherences />
<@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences />

<form id="treatmentCardDeltaform" action="/whp/patients/saveTreatmentCard" method="post">
    <input type="hidden" name="delta" id="delta"/>

    <div class="controls pull-right">
        <a href="/whp/patients/show?patientId=${patientId}" class="btn">Clear</a>
        <button type="button" id='submitAdherence' class="btn btn-primary">Save</button>
    </div>
</form>
</#if>