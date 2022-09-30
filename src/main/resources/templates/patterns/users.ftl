<#import "/patterns/basis.ftl" as bas>

<#macro table>

            <table class="table table-bordered">
                <thead class="thead-dark">
                    <tr>
                        <th>Actions</th>
                        <th>Id</th>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Created date</th>
                        <th>Roles</th>
                    </tr>
                </thead>
                
                <tbody>
                    <#if users??>
                        <#list users as user>
                        <@userInfo user />
                        </#list>
                    <#elseif user??>
                        <@userInfo user />
                    <#else>
                        No existing users
                    </#if>
                </tbody>
            </table>

</#macro>

<#macro userInfo user>

                    <tr class="bg-light text-dark">
                        <td>
                            <div class="btn btn-group-sm" role="group">
                                <a class="btn btn-outline-primary" href="/users/update/${user.id?c}">Update</a>
                                <form action="/users/delete/${user.id?c}" method="POST">
                                    <@bas.csrf />
                                    <button class="btn btn-outline-danger" type="submit">Delete</button>
                                </form>
                            </div>
                        </td>
                        <td>${user.id?c}</td>
                        <td>${user.username}</td>
                        <td>${user.password}</td>
                        <td>${user.createdDate}</td>
                        <td>
                            <#list user.roles as role>
                            <div>${role}</div>
                            </#list>
                        </td>
                    </tr>

</#macro>
