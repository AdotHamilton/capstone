<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="include/header.jsp" />

<h3>Welcome, <c:out value="${user.email}" /></h3>
<sec:authorize access="hasAuthority('ADMIN')">You are signed in as an admin</sec:authorize>
<a href="/api/login/logout">Logout</a>

<jsp:include page="include/footer.jsp" />
