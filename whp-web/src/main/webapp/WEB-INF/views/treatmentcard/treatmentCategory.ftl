<#macro treatmentCategory isCP = false>
    <#include "categoryDrugs.ftl"/>
<div class="print-version-text bold">${patient.treatmentCategoryName}</div>
<div class="print-version-text">
    <#if patient.treatmentCategoryCode == "01">
        New Case
        (Pulmonory Smear-Positive,
        Seriously III Smear Negative, or
        Seriously III extra pulmonory)
        <#if isCP>
            <@categoryDrugs/>
        <#else>
            <@categoryDrugs drugs=["H","R","Z","E"]/>
        </#if>
    <#elseif patient.treatmentCategoryCode == "02">
        Retreatment
        (relapses, failure,
        treatment after
        default, others)
        <#if isCP>
            <@categoryDrugs drugs=["H","R","E"]/>
        <#else>
            <@categoryDrugs drugs=["H","R","Z","E","S"]/>
        </#if>
    <#elseif patient.treatmentCategoryCode == "11">
        New Case
        (Pulmonory Smear-Positive,
        Seriously III Smear Negative, or
        Seriously III extra pulmonory)
        <#if isCP>
            <@categoryDrugs dosesPerWeek=7/>
        <#else>
            <@categoryDrugs dosesPerWeek=7 drugs=["H","R","Z","E"]/>
        </#if>
    <#elseif patient.treatmentCategoryCode == "12">
        Retreatment
        (relapses, failure,
        treatment after
        default, others)
        <#if isCP>
            <@categoryDrugs dosesPerWeek=7 drugs=["H","R","E"]/>
        <#else>
            <@categoryDrugs dosesPerWeek=7 drugs=["H","R","Z","E","S"]/>
        </#if>
    </#if>
</div>
</#macro>