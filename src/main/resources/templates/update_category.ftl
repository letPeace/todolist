<#import "/patterns/basis.ftl" as bas>
<#import "/patterns/header.ftl" as h>
<#import "/patterns/csrf.ftl" as c>

<@bas.basis "update category">

            <@h.header></@h.header>

            <div class="col-md-6">

                <h2>Update the category</h2>

                <form action="/categories/update/${category.id?c}" method="POST">
                    <div class="form-group">
                        <label for="title">Text</label>
                        <input class="form-control" type="text" name="title" id="title" placeholder="Put some text here" value="${category.title}">
                    </div>
                    <@c.csrf></@c.csrf>
                    <button type="submit" class="btn btn-success">Update</button>
                </form>

            </div>

</@bas.basis>