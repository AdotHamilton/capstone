<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!Doctype html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Admin Login</title>
    <link rel="stylesheet" type="text/css" href="/api/login.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body class="bg bg-dark text-light">
<div id="login_container" class="">
    <h1>myGarage Admin</h1>

    <form action="/api/admin/login/loginSubmit" method="POST">

        <div class="form-control bg-dark text-light">
            <h2>Login</h2>
            <div class="form-group">
                <h5>Email:</h5>
                <input type="text" name="username" class="form-control"/>
            </div>
            <div class="form-group">
                <h5>Password:</h5>
                <input type="password" name="password" class="form-control" />
            </div>
            <div class="form-group mt-3">
                <input type="submit" class="btn btn-primary" value="Login">
            </div>

        </div>
    </form>
</div>

</body>
</html>