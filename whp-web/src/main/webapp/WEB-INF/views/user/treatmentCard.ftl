<#macro treatmentCardView>
    <#if treatmentCard?exists>
    <div id="legend-container" class="pull-right">
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
                <div class="text-center">Therapy Paused</div>
            </td>
        </tr>
        <#list treatmentCard.providerIds as providerId>
        <tr>
            <td>
                <div class="windings-font pull-left" providerId=${providerId}> &#10003; </div>
                <div class="pull-left"> , &nbsp; </div>
                <div class="legend-round-icon" providerId=${providerId}>O </div>
            </td>
            <td>
                <div class="text-center text-center">Adherence given by provider ${providerId}</div>
            </td>
        </tr>
        </#list>
    </tbody>
    </table>
    </div>

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
                                <#elseif dailyAdherence.pillStatus == 2> "round-icon editable ${isItASunday} ${isItDuringPausedPeriod}"
                                <#elseif dailyAdherence.futureAdherence == true> "editable ${isItASunday} ${isItDuringPausedPeriod}"
                                <#else> "dash-icon editable ${isItASunday} ${isItDuringPausedPeriod}"
                                </#if>
                                id="${day} ${monthlyAdherence.monthAndYear}"
                                dayOfMonth="${day}"
                                monthStartDate="${monthlyAdherence.monthStartDate}"
                                pillStatus="${dailyAdherence.pillStatus}"
                                providerId = ${dailyAdherence.providerId}>
                                <div>
                                    <#if dailyAdherence.pillStatus == 1> &#10003;
                                    <#elseif dailyAdherence.pillStatus == 2> O
                                    <#elseif dailyAdherence.futureAdherence == true>
                                    <#else> -
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
                            id="${day} ${monthlyAdherence.monthAndYear}">
                            <div class="watermarked-sunday">
                                <#if isItASunday == 'sunday'>
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

    <script type="text/javascript">
        $(function () {
            var providerIds = new Array();
            var providerColors = new Array();
            <!--setting provider id & colors to javascript variables-->
            <#assign i=0/>
            <#list treatmentCard.providerIds as providerId>
                providerIds[${i}] = "${providerId}";
                <#assign i=i+1/>
            </#list>
            <#assign i=0/>
            <#list treatmentCard.providerColorCodes as color>
                providerColors[${i}] = "${color}";
                <#assign i=i+1/>
            </#list>

            for (var i = 0; i < providerIds.length; i++) {
                $("[providerid =" + providerIds[i] + "]").css('color', providerColors[i % providerColors.length]);
            }
        });

        <!--Tooltips to show provider information and dose status-->

        function annotate(){
            $('.tick-icon, .round-icon').each(function(){
                var providerId = $(this).attr('providerId');
                if(!providerId) {
                    providerId = "CMF Admin";
                }
                var pillStatus;
                if ($(this).hasClass('tick-icon')) {
                    pillStatus = "Taken."
                }
                else if ($(this).hasClass('round-icon')) {
                    pillStatus = "Not Taken."
                }
                else {
                    pillStatus = "";
                }
                $(this).tooltip({title: "Provider " + providerId + " gave adherence as " + pillStatus});
            })
        }

        annotate();

        <!--Troggle - 3 state toggling-->
        $('.editable').click(function () {
            if ($(this).hasClass('tick-icon')) {
                $(this).removeClass('tick-icon');
                $(this).addClass('round-icon');
                $(this).attr('pillStatus', '2');
                $(this).html('O');
            }
            else if ($(this).hasClass('round-icon')) {
                $(this).removeClass('round-icon');
                $(this).addClass('tick-icon');
                $(this).attr('pillStatus', '1');
                $(this).html('&#10003;');
            }
            else {
                $(this).removeClass('dash-icon');
                $(this).addClass('tick-icon');
                $(this).attr('pillStatus', '1');
                $(this).html('&#10003;');
            }
        });


    </script>
    </#if>
</#macro>