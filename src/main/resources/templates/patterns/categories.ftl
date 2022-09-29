<#import "/patterns/basis.ftl" as bas>

<#macro table>

            <table class="table table-bordered">
                <thead class="thead-dark">
                    <tr>
                        <th>Actions</th>
                        <th>Id</th>
                        <th>Category</th>
                        <th>Created date</th>
                        <th>Modified date</th>
                        <th>Author</th>
                    </tr>
                </thead>
                <tbody>
                    <#list categories as category>
                    <tr>
                        <td>
                            <div class="btn btn-group-sm" role="group">
                                <a class="btn btn-outline-primary" href="/categories/update/${category.id?c}">Update</a>
                                <form action="/categories/delete/${category.id?c}" method="POST">
                                    <@bas.csrf />
                                    <button class="btn btn-outline-danger" type="submit">Delete</button>
                                </form>
                            </div>
                        </td>
                        <td>${category.id?c}</td>
                        <td>${category.title}</td>
                        <td>${category.createdDate}</td>
                        <td>${category.modifiedDate}</td>
                        <td>${category.user.username}</td>
                    </tr>
                    <#else>
                    No existing categories
                    </#list>
                </tbody>
            </table>

</#macro>

<#macro createRedirect>

<p><a class="btn btn-success" href="/categories/create">Create a new category</a></p>

</#macro>

<#macro form h2 action value button>

            <div class="col-md-6">

                <h2>${h2}</h2>

                <form action="${action}" method="POST">
                    <div class="form-group">
                        <label for="title">Text</label>
                        <input class="form-control" type="text" name="title" id="title" placeholder="Put some text here" ${value}>
                    </div>
                    <@bas.csrf />
                    <button type="submit" class="btn btn-success">${button}</button>
                </form>

            </div>

</#macro>
