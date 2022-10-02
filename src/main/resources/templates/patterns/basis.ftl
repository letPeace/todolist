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

<#macro header>

<h1 class="text-center">TodoList Application</h1>

</#macro>
