<#import "/patterns/navigation.ftl" as nav>
<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>

<@bas.basis "users">

            <@h.header></@h.header>

            <table class="table table-bordered">
                <thead class="thead-dark">
                    <tr>
                        <th>Id</th>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Created date</th>
                    </tr>
                </thead>
                <tbody>
                    <#list users as user>
                    <tr class="bg-light text-dark">
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.password}</td>
                        <td>${user.createdDate}</td>
                    </tr>
                    </#list>
                </tbody>
            </table>

            <@nav.navigation />

</@bas.basis>