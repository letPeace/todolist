<#import "/patterns/navigation.ftl" as nav>
<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>

<@bas.basis "tasks">

            <@h.header></@h.header>

            <table class="table table-bordered">
                <thead class="thead-dark">
                    <tr>
                        <th>Actions</th>
                        <th>Id</th>
                        <th>Task</th>
                        <th>Completed</th>
                        <th>Created date</th>
                        <th>Modified date</th>
                        <th>Author</th>
                        <th>Category</th>
                    </tr>
                </thead>
                <tbody>

                    <#list tasks as task>
                    <tr class="${task.completed?string('bg-white text-secondary','bg-light text-dark')}">
                        <td>
                            <div class="btn btn-group-sm" role="group">
                                <a class="btn btn-outline-primary" href="/tasks/update/${task.id}">Update</a>
                                <form action="/tasks/delete/${task.id}" method="POST">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                    <button class="btn btn-outline-danger" type="submit">Delete</button>
                                </form>
                            </div>
                        </td>
                        <td>${task.id}</td>
                        <td>${task.text}</td>
                        <td>${task.completed?string('TRUE','FALSE')}</td>
                        <td>${task.createdDate}</td>
                        <td>${task.modifiedDate}</td>
                        <td><#if task.user??>${task.user.username}</#if></td>
                        <td><#if task.category??>${task.category}</#if></td>
                    </tr>
                    <#else>
                    No existing tasks
                    </#list>
                    
                </tbody>
            </table>

            <p><a class="btn btn-success" href="/tasks/create">Create a new task</a></p>

            <@nav.navigation />

</@bas.basis>