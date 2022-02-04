<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Area  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./css/Paginacao.css');
	</style>
	
	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
		<script type="text/javascript" src="./js/jqDnR.js"> </script>  
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
      	<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoCaracteres.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Unificar Pena </h2></div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
		<div id="divEditar" class="divEditar">
		<fieldset id="VisualizaDados" class="VisualizaDados">
	      	<legend>Dados do Processo de Execução Penal - Processo Destino</legend>
	      	<br />
	      	<div style="width: 15%"> Número do Processo </div>
			<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumeroCompleto()%></a></span/><br />
			<input name="IdProcessoDestino" id="IdProcessoDestino" type="hidden" size="15" maxlength="15" value="<%=processoDt.getId_Processo()%>"/>
			<br />
		</fieldset>
		<fieldset id="VisualizaDados" class="VisualizaDados">
	      	<legend>Processo para Unificação de Pena - Processo Origem</legend>
	      	<br />
	      	<label class="formEdicaoLabel" for="NumeroProcessoExecucaoPenalOrigem">Número do Processo: Ex 01234567.89</label><br>
			<input class="formEdicaoInput" name="NumeroProcessoExecucaoPenalOrigem" id="NumeroProcessoExecucaoPenalOrigem" type="text" size="15" maxlength="10" value=""/>
		</fieldset>
		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
			<input name="imgInserir" type="submit" value="Unificar Pena" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('PassoEditar','2')">
			<input name="imgCancelar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('PassoEditar','1');">
	    </div>
	    </div>
		</form>
		
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %> 
</body>
</html>