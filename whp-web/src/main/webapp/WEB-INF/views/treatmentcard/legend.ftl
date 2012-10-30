<table id="legend" class="table table-bordered table-condensed">
    <thead>
    <tr>
        <th colspan="2">Legend</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
            <div class="label pausedAdherenceData"></div>
        </td>
        <td>
            Treatment Paused
        </td>
    </tr>
    <#list treatmentCard.providerIds as providerId>
    <tr>
        <td>
            <span class="tick-icon" providerId=${providerId}> &#10004; </span>
            <span > , &nbsp; </div>
            <span class="round-icon legend-round-icon" providerId=${providerId}>O</span>
        </td>
        <td>
            Adherence given by provider ${providerId}
        </td>
    </tr>
    </#list>
    <tr>
        <td>
            <span class="tick-icon" providerId=""> &#10004; </span>
            <span class=""> , &nbsp; </span>
            <span class="round-icon legend-round-icon" providerId="">O</span>
        </td>
        <td>
            Provider Unknown
        </td>
    </tr>
    </tbody>
</table>