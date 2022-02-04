<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/rastreamentoCorreios.css');			
		</style>				
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='/js/jquery.js'></script>
		<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script>
			function submitForm(){
				AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');				
				FormSubmit('Formulario');
			}
		</script>		
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>				
				<div id="divEditar" class="divEditar">					
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Dados da Consulta</legend>
						<div class='col100 clear'>
							<div class='col20'>
								<label class="formEdicaoLabel" for="DataInicial">Código de Rastreamento</label><br>
								<input class="formEdicaoInput" name="codigo" id="codigo"  type="text" size="30" maxlength="30" value="<%=request.getAttribute("Codigo")%>">												
							</div>
							<div class='col20'>
								<div style="height: 18px;"></div>
								<input id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Localizar" value="Consultar" onclick="javascript:submitForm();" >
							</div>																							
						</div>
					</fieldset>
					
					<c:if test="${not empty processo}">
						<fieldset class="formEdicao"> 
							<legend class="formEdicaoLegenda">Dados da Postagem</legend>
							<div>
								<p><b>Número do Processo: </b><i><c:out value="${processo.processo}"/></i></p>
								<p><b>Nome da parte intimada/citada: </b><i><c:out value="${processo.nomeParte}"/></i></p>
								<p><b>Serventia: </b><i><c:out value="${processo.serventia}"/></i></p>
								<p><b>Data de postagem: </b><i><c:out value="${processo.dataPostagem}"/></i></p>								
								<p><b>Mão Própria: </b><i><c:out value="${processo.maoPropriaTela}"/></i></p>
							</div>
						</fieldset>
					</c:if>
					
					<%if (request.getAttribute("resultado") != null){ %>
						<fieldset class="formEdicao"> 
							<legend class="formEdicaoLegenda">Resultado</legend>
							<div><%=request.getAttribute("resultado")%></div>
						</fieldset>
					<%}%>						
				</div>
							  			  			  			 
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div> 		 
	</body>
</html>