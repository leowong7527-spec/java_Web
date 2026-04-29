<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style> 
            redColor { color: red; }
        </style>
    </head>
    <body>
        <h1>Simple Calculator <redColor>exercise 3</redColor></h1>
        
        
        <form action="Calculate.jsp" method="get">
 First Number:
            <input type="text" name="value1"></br></br>
Second Number:
            <input type="text" name="value2">
            </br>
            Operator
            <input type="radio" name="operation" value="+" />add |
            <input type="radio" name="operation" value="-" />subtract |
            <input type="radio" name="operation" value="*" />multiply
            </br><button type="submit">submit</button>
            <button type="reset">reset</button>
        </form>
    </body>
</html>
