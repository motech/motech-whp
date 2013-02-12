<div class="pull-right patients-list-legend">
    <div id="legend-container">
        <table id="legend" class="table table-bordered table-condensed">
            <thead>
            <tr>
                <th colspan="2">Legend</th>
            </tr>
            </thead>
            <tbody>
            <#list legends as legend>
            <tr>
                <td>
                    <div class="label">${legend.color}</div>
                </td>
                <td>
                    <div class="text-center"><@spring.message '${legend.displayText}'/></div>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
