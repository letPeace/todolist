<#macro navigation>
<p><a class="btn btn-dark" href="/users/create">Sign up</a></p>
<p><a class="btn btn-dark" href="/login">Sign in</a></p>
<p><a class="btn btn-dark" href="/users">Users list</a></p>
<p><a class="btn btn-dark" href="/tasks">Tasks list</a></p>
<p><a class="btn btn-dark" href="/categories">Categories list</a></p>
<form action="/logout" method="POST">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button type="submit">Sign Out</button>
</form>
</#macro>