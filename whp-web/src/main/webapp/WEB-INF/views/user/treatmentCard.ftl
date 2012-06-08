<#macro treatmentCardView>
<#if treatmentCard?exists>
    <table class="table table-bordered table-condensed fixed sharp disable-select">
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
                            <#elseif dailyAdherence.pillStatus == 2> "cross-icon editable ${isItASunday} ${isItDuringPausedPeriod}"
                            <#else> "round-icon editable ${isItASunday} ${isItDuringPausedPeriod}"
                            </#if>
                            >
                            <#if dailyAdherence.pillStatus == 1> &#252;
                            <#elseif dailyAdherence.pillStatus == 2> &#251;
                            </#if>
                            </td>
                            <#break/>
                        </#if>
                    </#list>
                    <#if drawn == false>
                        <td class=
                            <#if monthlyAdherence.maxDays &lt; day >"bg-gray"
                            <#else>"bg-gray ${isItASunday}"
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
<script type="text/javascript">
    $('.editable').click(function(){
        if($(this).hasClass('tick-icon')) {
           $(this).removeClass('tick-icon');
           $(this).addClass('cross-icon');
           $(this).html('&#251;');
        }
        else if($(this).hasClass('cross-icon')) {
            $(this).removeClass('cross-icon');
            $(this).addClass('round-icon');
            $(this).text('');
        }
        else {
            $(this).removeClass('round-icon');
            $(this).addClass('tick-icon');
            $(this).html('&#252;');
        }
    });
</script>
</#macro>