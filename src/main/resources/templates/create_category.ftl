<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>

<@bas.basis "create category">

            <@bas.header />

            <#assign value = ''>
            <#if category??>
            <#assign value = 'value=\"'+category.title+'\"'>
            </#if>
            <@ctg.form "Create a new category" "/categories/create" "${value}" "Create" />

</@bas.basis>