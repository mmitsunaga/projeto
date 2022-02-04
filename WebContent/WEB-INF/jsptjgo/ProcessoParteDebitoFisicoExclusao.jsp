<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="ProcessoParteDebitoFisicodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Exclusão de Débito - Processo Físico </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Exclusão de Débito - Processo Físico</h2></div>
		<form action="ProcessoParteDebitoFisico" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<fieldset id="VisualizaDados" class="VisualizaDados"> 
				<legend>Exclusão de Débito - Processo Físico </legend>
				
				<div>Processo</div> 
				<span><b><%=Funcoes.formataNumeroCompletoProcesso(ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto())%></b></span>
				<br />
				
				<div>Parte</div> 
				<span><%=ProcessoParteDebitoFisicodt.getNome()%></span>
				<br />
				
				<div>Débito</div>  
				<span><%=ProcessoParteDebitoFisicodt.getProcessoDebito()%></span>
				<br />
				<br />
				
			</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>