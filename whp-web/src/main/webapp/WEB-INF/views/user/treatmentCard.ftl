<#macro treatmentCardView>
<#if treatmentCard?exists>
    <table class="table table-bordered table-condensed fixed sharp">
        <thead>
        <tr>
            <th>Month/Year</th>
            <#list 1..31 as x>
                <th>${x}</th>
            </#list>
        </tr>
            <#list treatmentCard.monthlyAdherences as monthlyAdherence>
            <tr>
                <div>
                <td>${monthlyAdherence.monthAndYear}</td>
                </div>
                <#assign sunday = monthlyAdherence.firstSunday/>
                <#list 1..31 as day>
                    <#assign drawn = false/>
                    <#assign sundayClass = ""/>
                    <#if day == sunday>
                        <#assign sundayClass = "sunday"/>
                        <#assign sunday=sunday+7/>
                    </#if>
                    <#list monthlyAdherence.getLogs() as dailyAdherence>
                        <#if dailyAdherence.day == day>
                            <#assign drawn = true/>
                            <td class=
                            <#if dailyAdherence.pillStatus == 1> "tick-icon ${sundayClass}"
                            <#elseif dailyAdherence.pillStatus == 2> "cross-icon ${sundayClass}"
                            <#else> "round-icon ${sundayClass}"
                            </#if>
                            >
                            </td>
                            <#break/>
                        </#if>
                    </#list>
                    <#if drawn == false>
                        <td class=
                            <#if monthlyAdherence.maxDays &lt; day >"bg-gray"
                            <#else>"bg-gray ${sundayClass}"
                            </#if>
                            >
                        <div/>
                        </td>
                    </#if>
                </#list>
            </tr>
            </#list>
        </thead>
        <tbody>
        </tbody>
    </table>
</#if>
</#macro>