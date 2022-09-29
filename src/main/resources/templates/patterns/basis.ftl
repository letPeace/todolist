<#macro basis title>

<!DOCTYPE html>
<html>
    <head>
        <title>${title}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/webjars/bootstrap/5.2.0/css/bootstrap.min.css" />
    </head>
    <body>

        <div class="container">

        <#nested>

        </div>

        <script src="/webjars/jquery/3.6.1/jquery.min.js"></script>
        <script src="/webjars/bootstrap/5.2.0/js/bootstrap.min.js"></script>

    </body>
</html>

</#macro>

<#macro csrf>

<input type="hidden" name="_csrf" value="${_csrf.token}" />

</#macro>

<#macro header>

<h1 class="text-center">Todolist Application</h1>

</#macro>

<#macro navigation>
<p><a class="btn btn-dark" href="/users/create">Sign up</a></p>
<p><a class="btn btn-dark" href="/login">Sign in</a></p>
<p><a class="btn btn-dark" href="/users">Users list</a></p>
<p><a class="btn btn-dark" href="/tasks">Tasks list</a></p>
<p><a class="btn btn-dark" href="/categories">Categories list</a></p>
<form action="/logout" method="POST">
    <@csrf />
    <button type="submit">Sign Out</button>
</form>
</#macro>
