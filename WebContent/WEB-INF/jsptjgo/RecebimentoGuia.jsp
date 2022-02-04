<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Recebimento de Guia  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
   	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
   	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Apensar Processo </h2></div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
			   		<legend class="formEdicaoLegenda">Recebimento de Guia</legend>
			   		
			   		<label class="formEdicaoLabel">Número do Processo</label><br>
		   			<input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero" type="text" size="25" maxlength="25"  value="<%=request.getAttribute("ProcessoNumero")%>" onkeyup=" autoTab(this,60)" onkeypress="return DigitarNumeroProcesso(this, event)">
		   			<label for="Aviso" style="float:left;margin-left:25px;" ><small><strong>Atenção</strong>: Digite o Número do Processo completo. Ex: <strong>5000280.28.2010.8.09.0059</strong></small></label><br>		   			
		   			<br />
			   		
			   		<label class="formEdicaoLabel">Número da Guia</label><br>
		    		<input class="formEdicaoInput" name="NumeroGuia" id="NumeroGuia" type="text" size="25" maxlength="11" onkeypress="return DigitarSoNumero(this, event)" value="<%=request.getAttribute("NumeroGuia")%>">
		    		<br />
		    		
		    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Registrar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
					</div>
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
