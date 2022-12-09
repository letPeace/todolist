<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>

<@bas.basis "create category">

            <@bas.header />
            
            <@ctg.form "create" "Create a new category" "/categories/create" title "Create" />

</@bas.basis>