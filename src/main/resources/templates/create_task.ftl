<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/tasks.ftl" as tsk>

<@bas.basis "create task">

            <@bas.header />

            <@tsk.form "create" "Create a new task" "/tasks/create" "" "Create" />

</@bas.basis>