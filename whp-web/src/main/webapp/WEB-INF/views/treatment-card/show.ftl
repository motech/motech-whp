<#import "/spring.ftl" as spring />
<#if message?exists && (message?length>0)>
<div class="message-alert row text-center alert alert-info fade in">
    <button class="close" data-dismiss="alert">&times;</button>
${message}
    <#assign message=""/>
</div>
</#if>
<#if treatmentCard?exists>
<div id="legend-container" class="pull-right">
    <div id="patient-id" class="hide">${patientId}</div>
    <table id="legend" class="table table-bordered table-condensed">
        <thead>
        <tr>
            <th>Legend</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <div class="label pausedAdherenceData">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
            </td>
            <td>
                <div class="text-center">Treatment Paused</div>
            </td>
        </tr>
            <#list treatmentCard.providerIds as providerId>
            <tr>
                <td>
                    <div class="tick-icon pull-left" providerId=${providerId}> &#10004; </div>
                    <div class="pull-left"> , &nbsp; </div>
                    <div class="round-icon legend-round-icon" providerId=${providerId}>O</div>
                </td>
                <td>
                    <div class="text-center text-center">Adherence given by provider ${providerId}</div>
                </td>
            </tr>
            </#list>
        <tr>
            <td>
                <div class="tick-icon pull-left" providerId=""> &#10004; </div>
                <div class="pull-left"> , &nbsp; </div>
                <div class="round-icon legend-round-icon" providerId="">O</div>
            </td>
            <td>
                <div class="text-center text-center">Provider Unknown</div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br/>
<table id="IPTreatmentCard" class="table table-bordered table-condensed fixed sharp disable-select">
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
<form id="treatmentCardDeltaform" action="/whp/patients/saveTreatmentCard" method="post">
    <input type="hidden" name="delta" id="delta"/>

    <div class="controls pull-right">
        <a href="/whp/patients/dashboard?patientId=${patientId}" class="btn">Clear</a>
        <a href="<@spring.url "/"/>" class="btn">Back To Patient List</a>
        <button type="button" id='submitJson' class="btn btn-primary">Save</button>
    </div>
</form>

<script type="text/javascript">
    $(function () {
        setUpTreatmentCardTable('${treatmentCard.therapyDocId}');
    });


</script>

</#if>