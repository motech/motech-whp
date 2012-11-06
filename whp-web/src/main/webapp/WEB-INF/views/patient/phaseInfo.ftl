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
                <table id="patientList" class="table table-bordered table-condensed table-striped">
                    <thead>
                    <tr>
                        <th>Phase</th>
                        <th>Start Date</th>

                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Intensive Phase</td>
                        <td class="input-prepend input-append">
                            <span class="add-on show-date-button"><i class="icon-calendar"></i></span>
                            <input class="" placeholder="dd/mm/yyyy" data-date-format="dd/mm/yyyy" id="ipDatePicker" name="ipStartDate"
                                   readonly="readonly" type="text" value="${phaseStartDates.ipStartDate}"/>
                            <span class="add-on date-dismiss"><i class="icon-remove-sign"></i></span>

                        </td>

                    </tr>
                    <tr>
                        <td>Extended Intensive Phase</td>
                        <td class="input-prepend input-append">
                            <span class="add-on show-date-button"><i class="icon-calendar"></i></span>
                            <input class="" placeholder="dd/mm/yyyy"  data-date-format="dd/mm/yyyy" id="eipDatePicker" name="eipStartDate"
                                   readonly="readonly" type="text" value="${phaseStartDates.eipStartDate}"/>
                            <span class="add-on  date-dismiss"><i class="icon-remove-sign"></i></span>
                        </td>

                    </tr>
                    <tr>
                        <td>Continuation Phase</td>
                        <td class="input-prepend input-append">
                            <span class="add-on show-date-button"><i class="icon-calendar"></i></span>
                            <input class="" placeholder="dd/mm/yyyy"  data-date-format="dd/mm/yyyy" id="cpDatePicker" name="cpStartDate"
                                   readonly="readonly" type="text" value="${phaseStartDates.cpStartDate}"/>
                            <span class="add-on  date-dismiss"><i class="icon-remove-sign"></i></span>
                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="modal-footer ">
        <button class="btn " data-dismiss="modal"><i class="icon-remove"></i> Close</button>
        <button class="btn " id="clearFields"><i class="icon-remove-sign"></i> Clear All</button>
        <button type="submit" class="btn  btn-primary" id="saveTheDate"><i class="icon-ok icon-white"></i> Save</button>
    </div>
</form>