<#macro phaseInfo>
<form class="modal hide fade" id="setDatesModal" method="POST" action="<@spring.url '/patients/adjustPhaseStartDates'/>">
    <input type="hidden" id="patientId" name="patientId" value="${phaseStartDates.patientId}"/>
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Adjust Start Dates</h3>
    </div>
    <div class="modal-body">
        <div class="control-group">
            <div>
                <table id="patientList" class="table table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th>Phase</th>
                        <th>Start Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Intensive Phase</td>
                        <td>
                            <input class="span 2" data-date-format="dd/mm/yyyy" id="ipDatePicker" name="ipStartDate"
                                   type="text" value="${phaseStartDates.ipStartDate}">
                        </td>
                    </tr>
                    <tr>
                        <td>Extended Intensive Phase</td>
                        <td>
                            <input class="span 2" data-date-format="dd/mm/yyyy" id="eipDatePicker" name="eipStartDate"
                                   type="text" value="${phaseStartDates.eipStartDate}">
                        </td>
                    </tr>
                    <tr>
                        <td>Continuation Phase</td>
                        <td>
                            <input class="span 2" data-date-format="dd/mm/yyyy" id="cpDatePicker" name="cpStartDate"
                                   type="text" value="${phaseStartDates.cpStartDate}">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal">Close</button>
        <button class="btn btn-group" id="clearFields">Clear All</button>
        <button type="submit" class="btn btn-group btn-primary" id="saveTheDate">Save</button>
    </div>
</form>
</#macro>