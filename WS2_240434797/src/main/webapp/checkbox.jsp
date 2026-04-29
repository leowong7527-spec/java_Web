<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>check box</title>
    </head>
    <body>
        <h1>IDE Selection Survey</h1>
        <form method="get" action="checkbox">
            <p>What IDEs do you use?</p>
            <input type="checkbox" name="ide" value="netbean" /> NetBeans<br/>
            <input type="checkbox" name="ide" value="jdeveloper" /> JDeveloper<br/>
            <input type="checkbox" name="ide" value="eclipse" /> Eclipse<br/>
            <br/>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
