<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Prime number generator</title>
</head>
<body>
    <h1>Prime number generator</h1>
    <form action="prime.jsp" method="get">
        <label for="min">Minimum:</label>
        <input type="text" id="min" name="min" value="5" /><br/>
        <label for="max">Maximum:</label>
        <input type="text" id="max" name="max" value="50" /><br/>

        <input type="radio" id="simple" name="tagType" value="simple" checked="checked" />
        <label for="simple">Simple</label>
        <input type="radio" id="table" name="tagType" value="table" />
        <label for="table">Table</label>
        <br/>

        <input type="submit" value="generate" />
    </form>
</body>
</html>
