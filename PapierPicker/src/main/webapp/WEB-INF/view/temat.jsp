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
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/user/home">Strona
								główna</a></li>
						<security:authorize access="hasAnyRole('PROFESOR')">
						<li class="nav-item"><a class="nav-link active"
							href="${pageContext.request.contextPath}/user/temat">Dodaj/usuń
								temat</a></li>
						</security:authorize>
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/user/lista">Lista
								tematów</a></li>
						<security:authorize access="hasAnyRole('ADMIN')">
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/admin/userlist">Panel
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
							<div class="alert alert-danger h-100" role="alert">

								${registrationError}</div>
						</c:if>

						<c:if test="${infoError != null}">
							<div class="alert alert-success h-100" role="alert">

								${infoError}</div>
						</c:if>
					</div>


					<form:form action="addTemat" modelAttribute="tematTmp" method="POST">
						<form:hidden path="id" />

						<label for="temat">Wprowadzanie tematu</label>
						<form:textarea path="temat" class="form-control" id="temat"
							placeholder="Wprowadź temat" />
						
						<input type="submit" value="Zapisz" class="btn btn-success btn-lg" />
					</form:form>

					<table class="table table-striped table-hover">
						<tr>
							<th>Temat</th>
							<th>Student</th>
							<th>Akcja
						</tr>
						<!-- loop over and print-->
						<c:forEach var="i" items="${temats}">
							<c:url var="deleteLink" value="/user/delete">
								<c:param name="tematId" value="${i.id}" />
							</c:url>

							<c:url var="updateLink" value="/user/update">
								<c:param name="tematId" value="${i.id}" />
							</c:url>
							<tr>
								<td>${i.temat}</td>
								<td><address>${i.student.firstName}${i.student.lastName}<br> ${i.student.email}</address></td>
								<td><a href="${updateLink}" style="color: #FF8800">Modyfikuj</a> 
								&nbsp;&nbsp;
								<a href="${deleteLink}" style="color: #CC0000"
									onclick="if (!(confirm('Jesteś pewien że chcesz usunąć temat?'))) return false">Usuń</a>
								</td>
							</tr>

						</c:forEach>

					</table>
					<c:if test="${empty temats}">
						<div class="alert alert-success" role="alert">
  							Lista pusta!
						</div>
					</c:if>
					
					<br><hr style="border: 1px solid black"><br>

					
					<table class="table table-striped table-hover table-bordered">
								<tr> 
									<th colspan="3">Lista tematów zaproponowanych przez studentów</th>
								</tr>
								<tr>
									<th style="width: 40%">Student</th>
									<th style="width: 40%">Temat</th>
									<th>Akcja</th>
								</tr>
						<c:forEach var="i" items="${proponowane}">
							<c:url var="deleteLink" value="/user/deletePropozycja2">
								<c:param name="tematId" value="${i.temat.id}" />
							</c:url>
							<c:url var="pickLink" value="/user/pickPropozycja">
								<c:param name="tematId" value="${i.temat.id}" />
							</c:url>
								<tr>
									<td>${i.profesor.firstName} &nbsp; ${i.profesor.lastName} </td>
									<td>${i.temat.temat}</td>
									<td><a href="${deleteLink}" style="color: #CC0000"
									onclick="if (!(confirm('Jesteś pewien że chcesz usunąć proponowany temat?'))) return false">Usuń</a>
									<a href="${pickLink}" style="color: green"
									onclick="if (!(confirm('Jesteś pewien że chcesz przyjąć proponowany temat?'))) return false">Zaakceptuj</a>
									
									
									</td>
								</tr>
						</c:forEach>
						</table>
					<c:if test="${empty proponowane}">
						<div class="alert alert-success" role="alert">
  							Żaden student nie złożył jeszcze żadnej propozycji
						</div>
					</c:if>
					<div class="alert alert-warning" role="alert">
  						Możesz zaakceptować temat aby następnie zmodyfikować jego treść
					</div>

				</div>
				<div class="col-4" style="background-color: rgb(184, 222, 250)">
					<!--Panel Dodatkowy-->

					<div style="height: 200px; font-size: 18px">
						<div class="alert alert-primary h-100" role="alert">
							Imię:<b>${logged.firstName}</b><br> <b>Nazwisko:${logged.lastName}</b><br>

							<b>Email:${logged.email}</b><br>
							<security:authorize access="hasAnyRole('PROMOWANY','STUDENT')">
								 Wybrany temat pracy:<br>
								<b> ${temat}</b>
								<br>
								Promotor: <br>
								<b> ${promotor.firstName} ${promotor.lastName}
									${promotor.email} </b>
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