<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Prime Number Generator</title>
</head>
<body>
    <h2>Prime Number Generator</h2>
    <form action="prime.jsp" method="post">
        <label for="min">Minimum:</label>
        <input type="number" id="min" name="min" value="5"><br><br>

        <label for="max">Maximum:</label>
        <input type="number" id="max" name="max" value="50"><br><br>

        <input type="radio" name="format" value="simple" checked> Simple
        <input type="radio" name="format" value="table"> Table<br><br>

        <input type="submit" value="Generate">
    </form>
</body>
</html>
