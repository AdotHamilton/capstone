<html>
<head>
    <meta charset="UTF-8" />
    <title>myGarage Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>

<body class="bg bg-dark text-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark ml-1">
        <a class="navbar-brand" href="#">myGarage Admin</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav w-75">
                <li class="nav-item active">
                    <a class="nav-link active" href="/api/admin/portal">Home</a>
                </li>

<%--                <li class="nav-item">--%>
<%--                    <a class="nav-link" href="#">Manage Posts</a>--%>
<%--                </li>--%>
<%--                <li class="nav-item">--%>
<%--                    <a class="nav-link" href="#">Manage Files</a>--%>
<%--                </li>--%>
<%--                <li class="nav-item">--%>
<%--                    <a class="nav-link" href="#">Manage Events</a>--%>
<%--                </li>--%>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Manage
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="users">Users</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#">Posts</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#">Meets</a>
                        <div class="dropdown-divider"></div>

                    </div>
                </li>
                <li class="nav-item mr-0">
                    <a class="nav-link" href="http://localhost:3000/">Go to myGarage</a>
                </li>
            </ul>
<%--            <form class="form-inline my-2 my-lg-0">--%>
<%--                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">--%>
<%--                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>--%>
<%--            </form>--%>
        </div>
    </nav>
