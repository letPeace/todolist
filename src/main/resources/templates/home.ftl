<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>
<#import "/patterns/tasks.ftl" as tsk>
<#import "/patterns/users.ftl" as usr>
<#import "/patterns/navigation.ftl" as nav>

<@bas.basis "home">

            <@bas.header />

            <h3 class="text-center">USER</h3>

            <@usr.table type="user" />

            <h3 class="text-center">CATEGORIES</h3>

            <@ctg.table />

            <@nav.redirectCreateCategory />

            <h3 class="text-center">TASKS</h3>

            <@tsk.table />

            <@nav.redirectCreateTask />

            <h3 class="text-left">NAVIGATION</h3>

            <@nav.navigation />

</@bas.basis>