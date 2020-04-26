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


<title>Test</title>
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
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/user/temat">Dodaj/usuń
								temat</a></li>
						</security:authorize>
						<li class="nav-item"><a class="nav-link  active"
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


					<c:if test="${empty list.profesorowie}">
						Lista pusta
					</c:if>
					
					
							<c:forEach var="i" items="${list.profesorowie}">
								<table class="table table-striped table-hover table-bordered">
									<tr>
										<security:authorize access="hasAnyRole('STUDENT')">
										<th colspan="2" style="font-weight: Bolder; font-size: 22px;text-align: center; background-color: rgb(3, 66, 99)">
											<b style="text-align: center">
											<span class="glyphicon" style="font-size: 30px; text-align: center;">&#xe139;</span>
											${i.profesor.firstName}
									 		${i.profesor.lastName}
									 		</b>
									 	</th>
									 	</security:authorize>
									 	<security:authorize access="hasAnyRole('ADMIN','PROFESOR','PROMOWANY')">
									 	<th colspan="1" style="font-weight: Bolder; font-size: 22px;text-align: center; background-color: rgb(3, 66, 99)">
											<b style="text-align: center">
											${i.profesor.firstName}
									 		${i.profesor.lastName}
									 		</b>
									 	</th>
									 	</security:authorize>
									</tr>
									<tr>
										<th style="width: 80%;text-align: center;">Temat</th>
										<security:authorize access="hasAnyRole('STUDENT')">
										<th style="text-align: center; ">Akcja</th>
										</security:authorize>
									</tr>
									<c:forEach var="j" items="${i.tematy}">
										<c:url var="pick" value="/user/pick">
											<c:param name="tematId" value="${j.id}" />
										</c:url>
										<tr>
											<td style="text-align: center;">
											${j.temat}
											</td>
											<security:authorize access="hasAnyRole('STUDENT')">
											<td style="text-align: center;">
												<c:choose>
												    <c:when test="${empty j.student}">
												        <a href="${pick}" style="color: orange"
															onclick="if (!(confirm('Czy napewno chcesz wybrać temat ${j.temat}? Operacji nie można cofnąć'))) return false">Wybierz temat</a>
												    </c:when>
												    <c:otherwise>
												        <i style="color:blue"> Temat zajęty </i>
												    </c:otherwise>
												</c:choose>
											</td>
											</security:authorize>
							
										</tr>
									</c:forEach>
									<security:authorize access="hasAnyRole('STUDENT')">
									<tr style="text-align: center; background-color: white">
										<td>
											Zaproponuj promotorowi własny temat
										</td>
										<td>
											<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#exampleModal" data-whatever="${i.profesor.id}"
											data-whatever2="${i.profesor.firstName}" data-whatever3="${i.profesor.lastName}">Zaproponuj temat</button>
											
										</td>
									</tr>
									</security:authorize>
								</table>
								
								
								
							</c:forEach>

											<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
											  <div class="modal-dialog" role="document">
											    <div class="modal-content">
											    
											     <form:form action="propose" modelAttribute="propozycja" method="POST">
											      <div class="modal-header">
											        <h5 class="modal-title" id="exampleModalLabel">Proponowanie tematu</h5>
											        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											          <span aria-hidden="true">&times;</span>
											        </button>
											      </div>
											      <div class="modal-body">
											       
											        
											          <div class="form-group">
											           	Promotor<br>
											           	<p id="p1">
									 					</p>
											          </div>
											          
											          <div class="form-group">
											          <form:hidden path="id" id="dataDate"/>
											          <form:textarea path="temat" class="form-control"  required="required" />
											          </div>
											        
											      </div>
											      <div class="modal-footer">
											        <button type="button" class="btn btn-secondary" data-dismiss="modal">Zamknij</button>
											        <input type="submit" value="Wyślij propozycje tematu" class="btn btn-primary"/>
											      </div>
											      </form:form>
											    </div>
											  </div>
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


											<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
											  <div class="modal-dialog" role="document">
											    <div class="modal-content">
											    
											     <form:form action="propose" modelAttribute="propozycja" method="POST">
											      <div class="modal-header">
											        <h5 class="modal-title" id="exampleModalLabel">Proponowanie tematu</h5>
											        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											          <span aria-hidden="true">&times;</span>
											        </button>
											      </div>
											      <div class="modal-body">
											       
											        
											          <div class="form-group">
											           	Promotor<br>
														${i.profesor.firstName} &nbsp;
									 					${i.profesor.lastName}
											          </div>
											          
											          <div class="form-group">
											          	<form:hidden path="profesor" value="${i.profesor.id}" />										            
											            <label for="tmt" class="col-form-label">Proponowany temat:</label>
											            <form:textarea path="temat" placeholder="Wprowadź temat" id="tmt" class="form-control"  />
											            <input type="submit" value="Wyślij propozycje tematu" class="btn btn-primary"/>
											          </div>
											        
											      </div>
											      <div class="modal-footer">
											        <button type="button" class="btn btn-secondary" data-dismiss="modal">Zamknij</button>
											        <input type="submit" value="Wyślij propozycje tematu" class="btn btn-primary"/>
											      </div>
											      </form:form>
											    </div>
											  </div>
											</div>





				</div>
			</div>
		</div>
	</div>




	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/click.js"></script>
	
</body>
</html>