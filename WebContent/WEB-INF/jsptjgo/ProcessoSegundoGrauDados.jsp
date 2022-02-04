<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<html>
	<head>
		<title>Dados Processo de 2º Grau</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>

	<body>
		<div class="divCorpo">
	  		<div class="area"><h2>&raquo; Dados de Processo de 2º Grau</h2></div>
		
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Responsáveis </legend>
			    	
			   		<label class="formEdicaoLabel"> Processo </label><br>
			   		<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
			   		<br />
			   		
			   		<% 
			   		ServentiaCargoDt presidente = null;
			   		ServentiaCargoDt relator = null;
			   		
			   		%>
			   		
			   		<label class="formEdicaoLabel">Presidente</label><br>
			   		<%
			   		if (request.getAttribute("presidente") != null && !request.getAttribute("presidente").equals("")){ 
			   			presidente = (ServentiaCargoDt) request.getAttribute("presidente");
			   		%>
					<span class="spanDestaque"><%=presidente.getServentiaCargo()%> - <%=presidente.getNomeUsuario() %></span>
					<% } else { %>
					<span class="spanDestaque">Presidente não cadastrado</span>
					<% } %>
					<br />
	
					<label class="formEdicaoLabel">Relator</label><br>   
					<%
			   		if (request.getAttribute("relator") != null && !request.getAttribute("relator").equals("")){ 
			   			relator = (ServentiaCargoDt) request.getAttribute("relator");
			   		%>
					<span class="spanDestaque"><%=relator.getServentiaCargo()%> - <%=relator.getNomeUsuario() %></span>
					<% } else { %>
					<span class="spanDestaque">Relator não cadastrado</span>
					<% } %>
					<br />
				    		
				</fieldset>
			</div>
		</div>
	</body>
</html>