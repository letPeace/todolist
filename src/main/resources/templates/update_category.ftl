<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>

<@bas.basis "update category">

            <@bas.header />

            <@ctg.form "update" "Update the category" "/categories/update/${id?c}" title "Update" />

</@bas.basis>