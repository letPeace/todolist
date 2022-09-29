<#macro table>

            <table class="table table-bordered">
                <thead class="thead-dark">
                    <tr>
                        <th>Id</th>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Created date</th>
                    </tr>
                </thead>
                <#--  <@userInfo user id=user.id?c username=user.username password=user.password createdDate=user.createdDate />  -->
                <tbody>
                    <#if users??>
                        <#list users as user>
                        <@userInfo id=user.id?c username=user.username password=user.password createdDate=user.createdDate />
                        </#list>
                    <#elseif user??>
                        <@userInfo id=user.id?c username=user.username password=user.password createdDate=user.createdDate />
                    <#else>
                        No existing users
                    </#if>
                </tbody>
            </table>

</#macro>

<#macro userInfo id username password createdDate>

                    <tr class="bg-light text-dark">
                        <td>${id}</td>
                        <td>${username}</td>
                        <td>${password}</td>
                        <td>${createdDate}</td>
                    </tr>

</#macro>
