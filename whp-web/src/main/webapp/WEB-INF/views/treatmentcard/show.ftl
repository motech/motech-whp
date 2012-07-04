<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>

<#if treatmentCard?exists>

    <div id="legend-container" class="pull-left">
        <div id="patient-id" class="hide">${patientId}</div>
        <#include "legend.ftl"/>
        </div>
    <br/>
    <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences />
    <#if treatmentCard.CPAdherenceSectionValid == true>
        <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences />
    </#if>

    <form id="treatmentCardDeltaform" action="/whp/treatmentcard/update" method="post">
        <input type="hidden" name="delta" id="delta"/>
        <div class="controls pull-right">
            <a href="/whp/patients/show?patientId=${patientId}" class="btn">Clear</a>
            <button type="button" id='submitAdherence' class="btn btn-primary">Save</button>
        </div>
    </form>

</#if>