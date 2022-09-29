<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/categories.ftl" as ctg>

<@bas.basis "update category">

            <@bas.header />

            <@ctg.form "Update the category" "/categories/update/${category.id?c}" "value=\"${category.title}\"" "Update" />

</@bas.basis>