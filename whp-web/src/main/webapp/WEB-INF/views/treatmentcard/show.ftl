<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>

<#if treatmentCard?exists>

<div class="row-fluid">
    <div id="legend-container" class="pull-right">
        <div id="patient-id" class="hide">${patientId}</div>
        <#include "legend.ftl"/>
    </div>
</div>
<div>
    <h3>IP and EIP treatment card</h3>
    <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences />
</div>
    <#if treatmentCard.CPAdherenceSectionValid == true>
    <div>
        <h3> CP treatment card </h3>
        <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences />
    </div>
    </#if>

<form id="treatmentCardDeltaform" action="/whp/treatmentcard/update" method="post">
    <input type="hidden" name="delta" id="delta"/>

    <div class="controls pull-right">
        <a href="/whp/patients/show?patientId=${patientId}" class="btn">Clear</a>
        <button type="button" id='submitAdherence' class="btn btn-primary">Save</button>
    </div>
</form>

</#if>