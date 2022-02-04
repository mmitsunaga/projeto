<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="UsuarioPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioParteDt"/>

<html>
<head>
	<title>Usuario Parte</title>
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>		
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type="text/javascript" src="./js/DivFlutuante.js" > </script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Cadastro de Usu�rio Parte </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
			
			<fieldset id="VisualizaDados" class="VisualizaDados">
		      	<legend>Editar Usu�rio da Parte</legend>
		      	<br />
		      	
		      	<div> N�mero Processo </div>
				<span><a href="BuscaProcesso?Id_Processo=<%=UsuarioPartedt.getParteDt().getId_Processo()%>"><%=UsuarioPartedt.getParteDt().getProcessoNumero()%></a></span/><br />
				<br />
				
				<div>Parte</div> <span> <%=UsuarioPartedt.getParteDt().getNome()%></span>
				<div>CPF</div> <span> <%=UsuarioPartedt.getParteDt().getCpfCnpjFormatado()%></span><br /> <br />
				<div>Usu�rio</div>
				
				<% if (UsuarioPartedt.getParteDt().getId_UsuarioServentia().length()==0){ %>
					<span><em> N�o cadastrado </em></span>
					<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgCadeado.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Id_ProcessoParte','<%=UsuarioPartedt.getParteDt().getId()%>');" title="Cadastrar Usu�rio para Parte">
				<%
					} else {
				%>
		    		<span> <%=UsuarioPartedt.getUsuario()%> </span>
					<input id="imgLimparSenha" class="imgEditar" name="imgLimparSenha" type="image"  src="./imagens/imgSenhaLimpar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>');AlterarValue('Id_ProcessoParte','<%=UsuarioPartedt.getParteDt().getId()%>');" title="Limpar a senha do Usu�rio">
					<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Id_ProcessoParte','<%=UsuarioPartedt.getParteDt().getId()%>');" title="Desabilitar Usu�rio da Parte"/><br />
		       	<% 	} %>
		       	
		       	<br />
		       	
			</fieldset>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</form>
	</div>
</body>
</html>
