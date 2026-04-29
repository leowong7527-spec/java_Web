
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@ taglib uri="/WEB-INF/tlds/ict-taglib.tld"  prefix="ict" %>

        <ict:heading bgColor="#C0C0C0">
            Default Heading
        </ict:heading>
        <p>
            <ict:heading bgColor="BLACK" color="WHITE">
                White on Black Heading
            </ict:heading>
        <p>
            <ict:heading bgColor="#EF8429" fontSize="60" border="5">
                Large Border Heading
            </ict:heading>
        <p>
            <ict:heading bgColor="CYAN" width="100%">
                Heading with Full-Width Background
            </ict:heading>
        <p>
            <ict:heading bgColor="CYAN" fontSize="60"
                         fontList="Times, sans-serif">
                Heading with Non-Standard Font
            </ict:heading>

    </body>
</html>
