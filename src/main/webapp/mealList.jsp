<%--
  Created by IntelliJ IDEA.
  User: Arcanum
  Date: 06.06.2016
  Time: 23:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<link rel="stylesheet" href="resources/css/style.css">
<head>
    <title>Meal list</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h2>Список еды</h2>
<form method="post" action="meals">
    <input type="hidden" id="id" name="id" value="0"/>
    <label for="dateTime">Date Time: </label><input type="text" id="dateTime" name="dateTime"/>
    <label for="description">Description: </label><input type="text" id="description" name="description"/>
    <label for="calories">Calories: </label><input type="text" id="calories" name="calories"/>
    <input type="submit" value="Add"/>
</form>

<c:set var="meals" value="${requestScope.mealWithExceedList}"/>
<table>
    <thead>
    <tr>
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
        <td></td>
        <td></td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <c:choose>
            <c:when test="${meal.isExceed()}"><tr class="exceeded"></c:when>
            <c:otherwise><tr class="normal"></c:otherwise>
        </c:choose>
            <td><c:out value="${TimeUtil.toFormattedDateTime(meal.getDateTime())}"/></td>
        <td><c:out value="${meal.getDescription()}"/></td>
        <td><c:out value="${meal.getCalories()}"/></td>
        <td>Edit</td>
        <td>Delete</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
