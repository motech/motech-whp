<div class="legends-list">
    <h5>Legends :</h5>
    <ul>
    <#list legends as legend>
        <li class="legend-brdr" style="border-color:${legend.color}">
            <@spring.message '${legend.displayText}'/>
        </li>
    </#list>
    </ul>
</div>
