<#macro treatmentCardLegend>
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
</#macro>