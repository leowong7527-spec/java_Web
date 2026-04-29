<%-- 
    Document   : converter
    Created on : Jan 28, 2026, 10:13:39 AM
    Author     : a1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Temperature Converter</title>
    </head>
    <body>
        <h1>Temperature Converter</h1>
        <p>Enter a temperature value and select conversion type:</p>
        <form method="get" action="temperature" >
            <label>Temperature: </label>
            <input type="text" name="temperature"  />
            <br/><br/>
            <label>Convert to:</label><br/>
            <input type="radio" name="conversionType" value="toFahrenheit" /> Celsius to Fahrenheit<br/>
            <input type="radio" name="conversionType" value="toCelsius" /> Fahrenheit to Celsius<br/>
            <br/><input type="submit" value="Convert">
        </form>
    </body>
</html>
