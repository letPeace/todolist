<#import "/patterns/basis.ftl" as bas>

<@bas.basis "login">

            <@bas.header />

            <#if param??>
                <#if param.error??><div>Invalid username and password.</div></#if>
                <#if param.logout??><div>You have been logged out.</div></#if>
            </#if>

            <#if message??><span>${message}</span></#if>

            <form action="/login" method="POST">
                <div><label> Username: <input type="text" name="username"/> </label></div>
                <div><label> Password: <input type="password" name="password"/> </label></div>
                <@bas.csrf />
                <div><button type="submit">Sign In</button></div>
            </form>

            <@bas.navigation />

</@bas.basis>