<%--
  Created by IntelliJ IDEA.
  User: pyu
  Date: 2020/3/29
  Time: 下午5:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>PinJyu's blog home</title>

  <link rel="shortcut icon" href="favicon.ico"/>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
  <link rel="stylesheet" href="static/plugins/bootstrap-4.1.3-dist/css/bootstrap.min.css" type="text/css"/>
  <style>
    body {
      padding-top: 70px;
    }
  </style>
</head>

<body>
  <!-- 导航栏 -->
  <nav class="navbar navbar-expand-lg fixed-top navbar-light bg-light">
    <div class="container">
      <!-- 导航栏标题 -->
      <a href="index.jsp" class="navbar-brand">PinJyu's Blog</a>
      <!-- 折叠导航栏按钮 -->
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#nav-cls" aria-controls="nav-cls" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <divi class="collapse navbar-collapse" id="nav-cls">
        <!-- 导航分类 -->
        <ul class="nav navbar-nav">
          <li class="nav-item active"><a class="nav-link" href="#">Home</a></li>
          <!-- 下拉菜单 -->
          <li class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false"> blog </a>
            <div class="dropdown-menu">
              <a class="dropdown-header">java</a>
              <a class="dropdown-item" href="#">spring framework</a>
              <a class="dropdown-item" href="#">spring MVC</a>
              <a class="dropdown-item" href="#">mybatis</a>
              <div class="dropdown-divider"></div>
            </div>
          </li>
          <li class="nav-item"><a class="nav-link" href="#">About</a></li>
        </ul>

        <!-- 搜索栏 -->
        <form class="form-inline ml-lg-auto">
          <div class="input-group">
            <input class="form-control" type="search" placeholder="Search" aria-label="Search" aria-describedby="basic-addon2"/>
            <div class="input-group-append">
              <button class="btn btn-outline-secondary" type="submit">Button</button>
            </div>
          </div>
        </form>

      </div>
    </div>
  </nav>

  <div class="container">
    <ol class="breadcrumb">
      <li><a href="#">Home</a></li>
      <li><a href="#">Library</a></li>
      <li class="active">Data</li>
    </ol>
  </div>
  <div class="container">
    <div class="row">
      <div class="col-lg-6 col-lg-offset-6">你好</div>
    </div>
  </div>



  <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.4.1/dist/jquery.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
  <!-- 加载 Bootstrap 的所有 JavaScript 插件。也可以根据需要只加载单个插件。 -->
  <script src="static/plugins/bootstrap-4.1.3-dist/js/bootstrap.min.js"></script>
</body>
</html>
