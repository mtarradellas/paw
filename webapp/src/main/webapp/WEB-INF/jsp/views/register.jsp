<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:centeredLayout title="Register">
    <h1>Register</h1>
    <form method="POST" target="${pageContext.request.contextPath}/register">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" name="username" class="form-control" id="username" placeholder="Enter username">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" placeholder="Password">
        </div>
        <div class="form-group">
            <label for="repeatPassword">Repeat password</label>
            <input type="password" class="form-control" id="repeatPassword" placeholder="Repeat password">
        </div>
        <button type="submit" class="btn btn-primary">Register</button>
    </form>
</t:centeredLayout>