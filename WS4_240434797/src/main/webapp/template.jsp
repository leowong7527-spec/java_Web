<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html> <head> 
        <title>template</title>   
    </head>
    <body>
        <div id="maincontainer">
            <div id="topsection"><div class="innertube">
                    <jsp:include page="heading.jsp" />
                </div>
            </div>

            <div id="contentwrapper">
                <div id="contentcolumn">
                    <div class="innertube">
                        <jsp:include page="main.jsp" />
                    </div>
                </div>
            </div>
            <div id="leftcolumn">
                <div class="innertube">
                    <jsp:include page="menu.jsp" />
                </div>
            </div>
            <div id="footer">
                <jsp:include page="footer.jsp"         />
            </div>
        </div>
    </body>
</html>
