<#import "/patterns/navigation.ftl" as nav>
<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>

<@bas.basis "categories">

            <@h.header></@h.header>

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
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
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

            <p><a class="btn btn-success" href="/categories/create">Create a new category</a></p>

            <@nav.navigation />

</@bas.basis>