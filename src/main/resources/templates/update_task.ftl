<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>
<#import "/patterns/csrf.ftl" as c>

<@bas.basis "users">

            <@h.header></@h.header>

            <div class="col-md-6">

                <h2>Update the task</h2>

                <form action="/tasks/update/${task.id}" method="POST">
                    <div class="form-group">
                        <label for="text">Text</label>
                        <input class="form-control" type="text" name="text" id="text" placeholder="Put some text here" value="${task.text}">
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" checked="${task.completed?string('TRUE','FALSE')}" name="completed" id="completed" />Completed
                        </label>
                    </div>
                    <@c.csrf></@c.csrf>
                    <button type="submit" class="btn btn-success">Update</button>
                </form>

            </div>

</@bas.basis>