<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>
<#include "treatmentCategory.ftl"/>
<#if treatmentCard.IPAdherenceSectionValid == true || treatmentCard.CPAdherenceSectionValid == true>
<div class="printable-version">
    <div class="row-fluid">
        <div id="legend-container" class="pull-right">

        </div>
    </div>
    <div id="ip-card">
    <#if treatmentCard.IPAdherenceSectionValid == true>
            <h3 class="text-center">I. Intensive Phase</h3>

            <div>
                <@treatmentCategory/>
            </div>
            <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences isPrintVersion=true />
    </#if>
    </div>
    <div id="cp-card">
    <#if treatmentCard.CPAdherenceSectionValid == true>
            <h3 class="text-center">II. Continuation Phase</h3>
            <@treatmentCategory isCP=true/>
            <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences isPrintVersion=true/>
    </#if>
    </div>

</div>
</#if>