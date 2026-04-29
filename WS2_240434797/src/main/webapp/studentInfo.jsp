<%-- 
    Document   : studentInfo
    Created on : Jan 28, 2026, 10:16:34 AM
    Author     : a1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student Input Form</title>
    </head>
    <body>
        <h1>Student Information Form</h1>
        <form action="StudentInfoServlet" method="post">
            
            <!-- Personal Particular -->
            <fieldset>
                <legend>Personal Particular</legend>
                <label for="studentName">Name:</label><br/>
                <input type="text" id="studentName" name="studentName"  /><br/><br/>
                
                <label for="password">Password:</label><br/>
                <input type="password" id="password" name="password"  /><br/><br/>
                
                <label>Gender:</label><br/>
                <input type="radio" id="male" name="gender" value="Male"  />
                <label for="male">Male</label>
                <input type="radio" id="female" name="gender" value="Female" />
                <label for="female">Female</label><br/><br/>
                
                <label for="campus">Campus:</label><br/>
                <select id="campus" name="campus" >
                    <option value="Tsing yi">Tsing yi</option>
                    <option value="Tuen Mun">Tuen Mun</option>
                </select><br/><br/>
            </fieldset>
            
            <!-- Languages -->
            <fieldset>
                <legend>Languages</legend>
                <input type="checkbox" id="java" name="language" value="Java" />
                <label for="java">Java</label>
                <input type="checkbox" id="c" name="language" value="C/C++" />
                <label for="c">C/C++</label>
                <input type="checkbox" id="csharp" name="language" value="C#" />
                <label for="csharp">C#</label><br/><br/>
            </fieldset>
            
            <!-- Instruction -->
            <fieldset>
                <legend>Instruction</legend>
                <textarea id="instruction" name="instruction" rows="4" cols="40" placeholder="Enter your instruction here..."></textarea><br/><br/>
            </fieldset>
            
            <input type="hidden" name="secret" value="888"  />
            
            <!-- Buttons -->
            <input type="submit" value="SEND" />
            <input type="reset" value="CLEAR" />
        </form>
    </body>
</html>
