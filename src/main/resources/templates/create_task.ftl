<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>
<#import "/patterns/csrf.ftl" as c>

<@bas.basis "create task">

            <@h.header></@h.header>

            <div class="col-md-6">

                <h2>Create a new task</h2>

                <form action="/tasks/create" method="POST">
                    <div class="form-group">
                        <label for="text">Text</label>
                        <input class="form-control" type="text" name="text" id="text" placeholder="Put some text here">
                    </div>
                    <select name="category" class="form-select">
                        <#list categories as category>
                        <option value="${category.id?c}">${category.title}</option>
                        <#else>
                        No existing categories
                        </#list>
                    </select>
                    <@c.csrf></@c.csrf>
                    <button type="submit" class="btn btn-success">Create</button>
                </form>

            </div>

</@bas.basis>