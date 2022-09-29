<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>

<@bas.basis "create category">

            <@bas.header />

            <@ctg.form "Create a new category" "/categories/create" "" "Create" />

</@bas.basis>