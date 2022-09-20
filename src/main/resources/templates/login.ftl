<#import "/patterns/navigation.ftl" as nav>
<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>
<#import "/patterns/csrf.ftl" as c>

<@bas.basis "users">

            <@h.header></@h.header>

            <#if param??>
                <#if param.error??><div>Invalid username and password.</div></#if>
                <#if param.logout??><div>You have been logged out.</div></#if>
            </#if>

            <#if message??><span>${message}</span></#if>

            <form action="/login" method="POST">
                <div><label> Username: <input type="text" name="username"/> </label></div>
                <div><label> Password: <input type="password" name="password"/> </label></div>
                <@c.csrf></@c.csrf>
                <div><button type="submit">Sign In</button></div>
            </form>

            <@nav.navigation />

</@bas.basis>