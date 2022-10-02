
<#macro csrf>
<input type="hidden" name="_csrf" value="${_csrf.token}" />
</#macro>

<#macro form action button css>
<form action="${action}" method="POST">
    <#nested>
    <@csrf />
    <button class="btn ${css}" type="submit">${button}</button>
</form>
</#macro>

<#macro redirect href text css>
<p><a class="btn ${css}" href="${href}">${text}</a></p>
</#macro>

<#-- patterns -->

<#macro formGreen action button>
<@form action button "btn-success">
    <#nested>
</@form>
</#macro>

<#macro formRed action button>
<@form action button "btn-outline-danger" />
</#macro>

<#macro redirectDark href text>
<@redirect href text "btn-dark" />
</#macro>

<#macro redirectGreen href text>
<@redirect href text "btn-success" />
</#macro>

<#macro redirectRed href text>
<@redirect href text "btn-outline-danger" />
</#macro>

<#macro redirectBlue href text>
<@redirect href text "btn-outline-primary" />
</#macro>

<#-- implementations -->

<#macro redirectLogin>
<@redirectDark "/login" "Sign In" />
</#macro>

<#macro formLogout>
<@formRed "/logout" "Sign Out" />
</#macro>

<#macro redirectAdmin>
<@redirectDark "/users/admin" "Admin" />
</#macro>

<#macro redirectHome>
<@redirectDark "/users/home" "Home" />
</#macro>

<#macro redirectCreateUser>
<@redirectDark "/users/create" "Sign Up" />
</#macro>

<#macro redirectUpdateUser user>
<@redirectBlue "/users/update/${user.id?c}" "Update" />
</#macro>

<#macro formDeleteUser user>
<@formRed "/users/delete/${user.id?c}" "Delete" />
</#macro>

<#macro redirectCreateTask>
<@redirectGreen "/tasks/create" "Create a new task" />
</#macro>

<#macro redirectUpdateTask task>
<@redirectBlue "/tasks/update/${task.id?c}" "Update" />
</#macro>

<#macro formDeleteTask task>
<@formRed "/tasks/delete/${task.id?c}" "Delete" />
</#macro>

<#macro redirectCreateCategory>
<@redirectGreen "/categories/create" "Create a new category" />
</#macro>

<#macro redirectUpdateCategory category>
<@redirectBlue "/categories/update/${category.id?c}" "Update" />
</#macro>

<#macro formDeleteCategory category>
<@formRed "/categories/delete/${category.id?c}" "Delete" />
</#macro>

<#-- general implementations -->

<#macro navigation>
<@redirectAdmin />
<@redirectHome />
<@redirectCreateUser />
<@redirectLogin />
<@formLogout />
</#macro>