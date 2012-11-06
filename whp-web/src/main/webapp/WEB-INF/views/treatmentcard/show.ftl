<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>

<#if treatmentCard.IPAdherenceSectionValid == true || treatmentCard.CPAdherenceSectionValid == true>
<div class="well">
    <div class="treatment-card-container">
        <div class="row-fluid">
            <div class="span10">
                <#if treatmentCard.IPAdherenceSectionValid == true>

                    <h3 id="ipTreatmentCardHeading">Intensive Phase</h3>
                    <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences />

                </#if>
                <#if treatmentCard.CPAdherenceSectionValid == true>

                    <h3 id="cpTreatmentCardHeading">Continuation Phase</h3>
                    <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences />

                </#if>
            </div>
            <div id="legend-container" class="span2">
                <div id="patient-id" class="hide">${patientId}</div>
                <#include "legend.ftl"/>
            </div>
        </div>

    </div>
    <div class="treatment-card-buttons">
        <form id="treatmentCardDeltaform" action="/whp/treatmentcard/update" method="post" class="clearfix">

            <input type="hidden" name="delta" id="delta"/>

            <div class="controls pull-right">
                <a href="/whp/patients/show?patientId=${patientId}" class="btn"><i class="icon-remove"></i> Clear Current Changes</a>
                <button type="button" id='submitAdherence' class="btn btn-info"><i class="icon-ok icon-white"></i> Save Changes</button>
            </div>

        </form>
    </div>
</div>
</#if>