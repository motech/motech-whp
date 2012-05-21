<#macro legend key1 value1 span="span3">
    <div class="row pull-right">
        <table id="legend" class="table ${span} table-bordered table-condensed">
            <thead>
            <tr>
                <th>Legend</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <div class="label ${key1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
                </td>
                <td>
                    <div class="text-center">${value1}</div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</#macro>