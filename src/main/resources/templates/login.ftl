<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/navigation.ftl" as nav>

<@bas.basis "login">

            <@bas.header />

            <#if param??>
                <#if param.error??><div>Invalid username and password.</div></#if>
                <#if param.logout??><div>You have been logged out.</div></#if>
            </#if>

            <#if error??><span>${error}</span></#if>

            <@nav.formGreen "/login" "Sign In"> 
                <div class="form-group">
                    <label> Username: <input class="form-control" type="text" name="username"/> </label>
                </div>
                <div class="form-group">
                    <label> Password: <input class="form-control" type="password" name="password"/> </label>
                </div>
            </@nav.formGreen>

            <@nav.navigation />

</@bas.basis>