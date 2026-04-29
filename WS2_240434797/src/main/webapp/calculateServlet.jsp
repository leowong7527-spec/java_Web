<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>A Simple Calculator</title>
    </head>
    <body>
        <h1>A Simple Calculator</h1>
        <p>Enter 2 numbers and click the Calculate button.</p>
        <form action="sum" method="get">
            <input type="text" name="n1" /> <br/>
            <input type="text" name="n2" /> <br/>
            <button type="submit">Calculate</button>
        </form>
    </body>
</html>
