<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="entries" scope="session" class="models.Entries"/>
<c:forEach var="entry" items="${entries.entries}">
    <circle r="3.5"
            cx=${((300 / 2 + (entry.x + 0.02) / entry.r * 100))}
            cy=${((300 / 2 + (entry.y - 0.02) / entry.r * -100))}
            id="target-dot"
            fill=${entry.result ? "green" : "red"}>
    </circle>
</c:forEach>
