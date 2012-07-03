<#macro adherenceBox title monthlyAdherences >
<table id=${title} class="table table-bordered table-condensed fixed disable-select">
<thead>
<tr>
    <th>Month/Year</th>
    <#list 1..31 as x>
        <th>${x}</th>
    </#list>
</tr>
    <#list monthlyAdherences as monthlyAdherence>
    <tr>
        <div>
            <td>${monthlyAdherence.monthAndYear}</td>
        </div>
        <#assign sunday = monthlyAdherence.firstSunday/>
        <#list 1..31 as day>
            <#assign drawn = false/>
            <#assign isItASunday = ""/>
            <#if day == sunday>
                <#assign isItASunday = "sunday"/>
                <#assign sunday=sunday+7/>
            </#if>
            <#list monthlyAdherence.getLogs() as dailyAdherence>
                <#assign isItDuringPausedPeriod = ""/>
                <#if dailyAdherence.adherenceCapturedDuringPausedPeriod>
                    <#assign isItDuringPausedPeriod = "pausedAdherenceData">
                </#if>
                <#if dailyAdherence.day == day>
                    <#assign drawn = true/>
                        <td class=
                    <#if dailyAdherence.pillStatus == 1> "tick-icon editable ${isItASunday} ${isItDuringPausedPeriod}"
                        <#elseif dailyAdherence.pillStatus == 2> "round-icon
                            editable ${isItASunday} ${isItDuringPausedPeriod}"
                        <#elseif dailyAdherence.futureAdherence == true>
                            "editable ${isItASunday} ${isItDuringPausedPeriod}"
                        <#else> "dash-icon editable ${isItASunday} ${isItDuringPausedPeriod}"
                    </#if>
                    id="${day}-${monthlyAdherence.month}-${monthlyAdherence.year}"
                    day="${day}"
                    month="${monthlyAdherence.month}"
                    year="${monthlyAdherence.year}"
                    currentPillStatus="${dailyAdherence.pillStatus}"
                    oldPillStatus="${dailyAdherence.pillStatus}"
                    providerId = ${dailyAdherence.providerId}>
                    <div>
                        <#if dailyAdherence.pillStatus == 1>&#10004;
                            <#elseif dailyAdherence.pillStatus == 2>O
                            <#elseif dailyAdherence.futureAdherence == true>
                            <#else>-
                        </#if>
                    </div>
                    </td>
                    <#break/>
                </#if>
            </#list>
            <#if drawn == false>
                    <td class=
                <#if monthlyAdherence.maxDays &lt; day >"bg-gray"
                    <#else>"bg-gray ${isItASunday}"
                </#if>
                id="${day}-${monthlyAdherence.month}-${monthlyAdherence.year}">
                <div class="watermarked-sunday">
                    <#if isItASunday == 'sunday' && (monthlyAdherence.maxDays &gt; day || monthlyAdherence.maxDays == day)>
                        S
                    </#if>
                </div>
                </td>
            </#if>
        </#list>
    </tr>
    </#list>
</thead>
<tbody>
</tbody>
</table>
</#macro>