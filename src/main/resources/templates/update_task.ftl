<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/tasks.ftl" as tsk>

<@bas.basis "update task">

            <@bas.header />

            <@tsk.form "update" "Update the task" "/tasks/update/${task.id?c}" "value=\"${task.text}\"" "Update" />

</@bas.basis>