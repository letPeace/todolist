<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/users.ftl" as usr>

<@bas.basis "update user">

            <@bas.header />

            <@usr.form "update" "Update the user" "/users/update/${user.id?c}" "Update" user isAdmin />

</@bas.basis>