<#import "/patterns/navigation.ftl" as nav>

<#macro table>

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
                                <@nav.redirectUpdateTask task />
                                <@nav.formDeleteTask task />
                            </div>
                        </td>
                        <td>${task.id?c}</td>
                        <td>${task.text}</td>
                        <td>${task.completed?string('TRUE','FALSE')}</td>
                        <td>${task.createdDate}</td>
                        <td>${task.modifiedDate}</td>
                        <td><#if task.user??>${task.user.username}</#if></td>
                        <td><#if task.category??>${task.category.title}</#if></td>
                    </tr>
                    <#else>
                    No existing tasks
                    </#list>
                </tbody>
            </table>

</#macro>

<#macro form type h2 action value button>

            <div class="col-md-6">

                <h2>${h2}</h2>

                <form action="${action}" method="POST">
                    <div class="form-group">
                        <label for="text">Text</label>
                        <input class="form-control" type="text" name="text" id="text" placeholder="Put some text here" ${value}>
                    </div>
                    <#if type=="update">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="completed" id="completed" value="${task.completed?c}" ${task.completed?string("checked","")} onclick="taskCompletedHandler(this);"/>Completed
                        </label>
                    </div>
                    </#if>
                    <select name="category" class="form-select">
                        <#list categories as category>
                        <#assign selected = type=="update" && task.category==category || type=="create" && categorySelected=category>
                        <option value="${category.id?c}" ${selected?string("selected", "")}>${category.title}</option>
                        <#else>
                        No existing categories
                        </#list>
                    </select>
                    <@nav.csrf />
                    <button type="submit" class="btn btn-success">${button}</button>
                </form>

            </div>

            <#if type=="update"><script src="/js/taskCompletedHandler.js"></script></#if>

</#macro>
