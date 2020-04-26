<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<!doctype html>
<html>
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">


<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">

<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/test.css" />

<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">


<title>PapierPicker</title>
</head>
<body>
	<div class="container-fluid h-100" style="background-color: #263238">
		<div class="row" style="height: 10%">
			<div class="col text-primary"
				style="font-family: 'Roboto'; background-color: rgb(24, 26, 27);">
				<h1 style="margin-top: 10px; margin-left: 20%;">
					<b style="font-size: 1.8em;">PapierPicker</b>
				</h1>
			</div>
		</div>
		<div class="container h-100">

			<div class="row">

				<nav class="navbar navbar-expand-sm w-100 navbar-light"
					style="background-color: rgb(160, 211, 248); margin-bottom: 0; font-weight: bold; padding: 0%">
					<ul class="navbar-nav" style="width: 60%;">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/user/home">Strona
								główna</a></li>
						<security:authorize access="hasAnyRole('PROFESOR')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/user/temat">Dodaj/usuń
								temat</a></li>
						</security:authorize>
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/user/lista">Lista
								tematów</a></li>
						<security:authorize access="hasAnyRole('ADMIN')">
						<li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/admin/userlist">Panel
								Administracyjny</a></li>
						</security:authorize>

					</ul>
					<ul class="navbar-nav ml-auto">
						<p style="text-align: left; margin-right: 30px">
							Zalogowano jako:<br> ${logged.users.username}
						<p>

							<form:form action="${pageContext.request.contextPath}/logout"
								method="POST">
								<span class="glyphicon"> <input type="submit"
									value="&#xe163; Wyloguj" class="btn btn-primary btn-lg active"
									onclick="if (!(confirm('Czy napewno chcesz się wylogować?'))) return false" />
								</span>
							</form:form>
					</ul>



				</nav>

			</div>

			<div class="row h-100">

				<div class="col-8" style="background-color: rgb(207, 233, 252)">
					<!--Główny panel-->

					<div style="height: 50px;">
						<c:if test="${registrationError != null}">
							<div class="alert alert-warning h-100" role="alert">

								${registrationError}
								
							</div>
						</c:if>
						
						<c:if test="${infoError != null}">
							<div class="alert alert-success h-100" role="alert">
							
								${infoError}
							
							</div>
						</c:if>
					</div>

					

					<c:choose>
						<c:when test="${role == 'ROLE_STUDENT'}">
							<c:set var="formAction" value="process" />
		    <h3>Tworzenie/Edycja Studenta</h3>
		  </c:when>
						<c:otherwise>
							<c:set var="formAction" value="process2" />
		    <h3>Tworzenie/Edycja Profesora</h3>
		  </c:otherwise>
					</c:choose>

					
					<form:form action="${formAction}" modelAttribute="student" method="POST">
					
					<div class="form-group">
						<label for="loginname">Login</label>
						<c:choose>
							<c:when test="${empty student.users.username}">
								<form:input path="users.username" class="form-control" id="loginname" placeholder="Wprowadź login użytkownika"/>
							</c:when>
							<c:otherwise>
								<form:input path="users.username" readOnly="true" class="form-control" id="loginname" placeholder="Wprowadź login użytkownika"/>
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="form-group">
						<label for="pass">Hasło</label>
						<form:input path="users.password" class="form-control" id="pass" placeholder="Wprowadź hasło użytkownika"/>
					</div>
					
					<div class="form-group">
						<label for="fn">Imię</label>
						<form:input path="firstName" class="form-control" id="fn" placeholder="Wprowadź imię użytkownika"/>
					</div>
					
					<div class="form-group">
						<label for="ln">Nazwisko</label>
						<form:input path="lastName" class="form-control" id="ln" placeholder="Wprowadź Nazwisko użytkownika"/>
					</div>
					
					<input type="submit" value="Zapisz" class="btn btn-success btn-lg"/>
					
					</form:form>


					
						<a href="${pageContext.request.contextPath}/admin/userlist" style="font-weight: bold"><i class="glyphicon glyphicon-arrow-left"></i>
							Powrót</a>
					






				</div>
				<div class="col-4" style="background-color: rgb(184, 222, 250)">
					<!--Panel Dodatkowy-->

					<div style="height: 200px; font-size: 18px">
						<div class="alert alert-primary h-100" role="alert">
							Imię:<b>${logged.firstName}</b><br> <b>Nazwisko:${logged.lastName}</b><br>

							<b>Email:${logged.email}</b><br>
							<security:authorize access="hasAnyRole('PROMOWANY','STUDENT')">
								 Wybrany temat pracy:<br><b> ${temat}</b> <br>
								Promotor: <br><b>
								${promotor.firstName} ${promotor.lastName}
								${promotor.email}
								</b>
							</security:authorize>

						</div>
					</div>









				</div>
			</div>
		</div>
	</div>




	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous"></script>
</body>
</html>