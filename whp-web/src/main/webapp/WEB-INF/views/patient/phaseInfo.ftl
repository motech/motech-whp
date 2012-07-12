<form class="modal hide fade" id="setDatesModal" method="POST" action="<@spring.url '/patients/adjustPhaseStartDates'/>"
      xmlns="http://www.w3.org/1999/html">
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
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Intensive Phase</td>
                        <td>
                            <input class="span 2" data-date-format="dd/mm/yyyy" id="ipDatePicker" name="ipStartDate"
                                   readonly="readonly" type="text" value="${phaseStartDates.ipStartDate}"/>
                        </td>
                        <td><a class="date-dismiss" href="#">x</a></td>
                    </tr>
                    <tr>
                        <td>Extended Intensive Phase</td>
                        <td>
                            <input class="span 2" data-date-format="dd/mm/yyyy" id="eipDatePicker" name="eipStartDate"
                                   readonly="readonly" type="text" value="${phaseStartDates.eipStartDate}"/>
                        </td>
                        <td><a class="date-dismiss" href="#">x</a></td>
                    </tr>
                    <tr>
                        <td>Continuation Phase</td>
                        <td>
                            <input class="span 2" data-date-format="dd/mm/yyyy" id="cpDatePicker" name="cpStartDate"
                                   readonly="readonly" type="text" value="${phaseStartDates.cpStartDate}"/>
                        </td>
                        <td><a class="date-dismiss" href="#">x</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal">Close</button>
        <input type="button" class="btn btn-group" id="clearFields" value="Clear All"/>
        <button type="submit" class="btn btn-group btn-primary" id="saveTheDate">Save</button>
    </div>
</form>