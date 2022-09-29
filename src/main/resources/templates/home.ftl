<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>
<#import "/patterns/tasks.ftl" as tsk>
<#import "/patterns/users.ftl" as usr>

<@bas.basis "home">

            <@bas.header />

            <@usr.table />

            <@ctg.table />

            <@ctg.createRedirect />

            <@tsk.table />

            <@tsk.createRedirect />

            <@bas.navigation />

</@bas.basis>