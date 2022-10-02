<#import "/patterns/navigation.ftl" as nav>

<#macro table type>

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
                    <#if type=="users" && users??>
                        <#list users as user>
                        <@userInfo user />
                        </#list>
                    <#elseif type=="user" && user??>
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
                                <@nav.redirectUpdateUser user />
                                <@nav.formDeleteUser user />
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

<#macro form type h2 action button>

            <div class="col-md-6">

                <h2>${h2}</h2>

                <#if error??><span>${error}</span></#if>

                <form action="${action}" method="POST">
                    <div class="form-group"><label> Username: <input class="form-control" type="text" name="username"/> </label></div>
                    <#if type=="update">
                        <div class="form-group"><label> Confirm Username: <input class="form-control" type="text" name="usernameConfirm"/> </label></div>
                    </#if>
                    <div class="form-group"><label> Password: <input class="form-control" type="password" name="password"/> </label></div>
                    <#if type=="update">
                        <div class="form-group"><label> Confirm Password: <input class="form-control" type="password" name="passwordConfirm"/> </label></div>
                    </#if>
                    <@nav.csrf />
                    <button type="submit" class="btn btn-success">${button}</button>
                </form>

            </div>

</#macro>
