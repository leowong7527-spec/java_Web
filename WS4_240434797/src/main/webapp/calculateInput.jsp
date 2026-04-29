<!DOCTYPE html>
<html>
<head>
    <title>Simple Calculator using JSP action</title>
    <style>
        h2 span {
            color: red;
        }
    </style>
</head>
<body>
    <h2>Simple Calculator <span>using jsp action</span></h2>
    
    <form action="calculate.jsp" method="get">
        <label for="firstNumber">First Number:</label>
        <input type="text" id="value1" name="value1"><br/><br/>
        
        <label for="secondNumber">Second Number:</label>
        <input type="text" id="value2" name="value2"><br/><br/>
        
        <label>Operator:</label>
        <input type="radio" name="operator" value="+"> add
        <input type="radio" name="operator" value="-"> subtract
        <input type="radio" name="operator" value="*"> multiply
        <br/><br/>
        
        <input type="submit" value="submit">
        <input type="reset" value="reset">
    </form>
</body>
</html>
