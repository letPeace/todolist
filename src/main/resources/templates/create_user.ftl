<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/users.ftl" as usr>

<@bas.basis "create user">

            <@bas.header />

            <@usr.form "create" "Create a new user" "/users/create" "Create" />

</@bas.basis>