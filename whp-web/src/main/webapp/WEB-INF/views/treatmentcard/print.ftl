<#import "/spring.ftl" as spring />
<#include "adherenceBox.ftl"/>
<#include "treatmentCategory.ftl"/>
<#if treatmentCard.IPAdherenceSectionValid == true || treatmentCard.CPAdherenceSectionValid == true>
 <div class="row-fluid">

    <div id="ip-card">
    <#if treatmentCard.IPAdherenceSectionValid == true>
            <h3 class="text-center">I. Intensive Phase</h3>

            <div>
                <@treatmentCategory/>
            </div>
            <div>(&#10004;) indicates that the pill has been swallowed under direct observation; (<label class="inline bold">O</label>) indicates a missed dose</div>
            <@adherenceBox title="IPTreatmentCard" monthlyAdherences=treatmentCard.ipAndEipAdherenceSection.monthlyAdherences isPrintVersion=true />
    </#if>
    </div>
    <div id="cp-card">
    <#if treatmentCard.CPAdherenceSectionValid == true>
            <h3 class="text-center">II. Continuation Phase</h3>
            <@treatmentCategory isCP=true/>
         <div>(&#10004;) indicates that the pill has been swallowed under direct observation; (<label class="inline bold">O</label>) indicates a missed dose</div>
        <@adherenceBox title="CPTreatmentCard" monthlyAdherences=treatmentCard.cpAdherenceSection.monthlyAdherences isPrintVersion=true/>
    </#if>
    </div>

</div>
</#if>