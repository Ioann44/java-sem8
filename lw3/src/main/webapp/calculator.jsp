<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculator</title>
</head>
<body>
    <form action="calculator" method="post">
        <input type="text" name="num1" value="${param.num1}"> 
        <select name="operation">
            <option value="add">+</option>
            <option value="subtract">-</option>
            <option value="multiply">*</option>
            <option value="divide">/</option>
        </select>
        <input type="text" name="num2" value="${param.num2}">
        <input type="submit" value="Calculate">
    </form>

    <%
        if (request.getAttribute("result") != null) {
            out.println("Result: " + request.getAttribute("result"));
        }
    %>
</body>
</html>
