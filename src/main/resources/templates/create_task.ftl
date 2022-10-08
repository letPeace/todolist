<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/tasks.ftl" as tsk>

<@bas.basis "create task">

            <@bas.header />

            <#assign value = ''>
            <#if task??>
            <#assign value = 'value=\"'+task.text+'\"'>
            </#if>
            <@tsk.form "create" "Create a new task" "/tasks/create" "${value}" "Create" />

</@bas.basis>