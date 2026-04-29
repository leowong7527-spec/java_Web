<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Radio Button</title>
    </head>
    <body>
        <h1>Employee Count Survey</h1>
        <form method="get" action="radio">
            <p>How many employees in your company?</p>
            <input type="radio" name="employee" value="1-100" /> 1-100<br/>
            <input type="radio" name="employee" value="101-200" /> 101-200<br/>
            <input type="radio" name="employee" value="201-300" /> 201-300<br/>
            <input type="radio" name="employee" value="301-400" /> 301-400<br/>
            <input type="radio" name="employee" value="401-more" /> 401-more<br/>
            <br/>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
