<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>
<#include "treatmentCategory.ftl"/>
<#if treatmentCard.IPAdherenceSectionValid == true || treatmentCard.CPAdherenceSectionValid == true>
<div class="printable-version">
    <div class="row-fluid">
        <div id="legend-container" class="pull-right">

        </div>
    </div>
    <#if treatmentCard.IPAdherenceSectionValid == true>
        <div>
            <h3 class="text-center">I. Intensive Phase</h3>

            <div>
                <@treatmentCategory/>
            </div>
            <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences isPrintVersion=true />
        </div>
    </#if>
    <#if treatmentCard.CPAdherenceSectionValid == true>
        <div class="page-break">
            <h3 class="text-center">II. Continuation Phase</h3>
            <@treatmentCategory isCP=true/>
            <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences isPrintVersion=true/>
        </div>
    </#if>
    <#if treatmentCard.treatmentHistories?has_content>
        <div class="provider-details">
            <h4>Provider Details</h4>
            <#list treatmentCard.treatmentHistories as treatmentHistory>
                <div>${treatmentHistory.providerId} gave adherence
                    from ${treatmentHistory.startDate} <#if treatmentHistory.endDate??>
                        to ${treatmentHistory.endDate!}</#if></div>
            </#list>
        </div>
    </#if>

    <#if treatmentCard.treatmentPausePeriods?has_content>
        <div class="pause-details">
            <h4>Treatment Pause Details</h4>
            <#list treatmentCard.treatmentPausePeriods as treatmentPausePeriod>
                <div>Treatment was paused from ${treatmentPausePeriod.startDate} <#if treatmentPausePeriod.endDate??>
                    to ${treatmentPausePeriod.endDate!}</#if></div>
            </#list>
        </div>
    </#if>

</div>
</#if>