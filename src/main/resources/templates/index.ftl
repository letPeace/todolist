<#import "/patterns/navigation.ftl" as nav>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/webjars/bootstrap/5.2.0/css/bootstrap.min.css" />
    </head>
    <body>

        <div class="container">

            <h1 class="text-center">Todolist Application</h1>

            <@nav.navigation />

        </div>

        <script src="/webjars/jquery/3.6.1/jquery.min.js"></script>
        <script src="/webjars/bootstrap/5.2.0/js/bootstrap.min.js"></script>

    </body>
</html>