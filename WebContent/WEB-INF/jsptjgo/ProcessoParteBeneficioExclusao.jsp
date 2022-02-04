<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="ProcessoParteBeneficiodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Exclusão de Benefício  </title>
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
		<div class="area"><h2>&raquo; Exclusão de Benefício</h2></div>
		<form action="ProcessoParteBeneficio" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<fieldset id="VisualizaDados" class="VisualizaDados"> 
				<legend>Exclusão de Benefício </legend>
				
				<div>Processo</div> 
				<span><a href="BuscaProcesso?Id_Processo=<%=ProcessoParteBeneficiodt.getId_Processo()%>"><%=ProcessoParteBeneficiodt.getProcessoNumero()%></a></span>
				<br />
				
				<div>Parte</div> 
				<span><%=ProcessoParteBeneficiodt.getNome()%></span>
				<br />
				
				<div>Benefício</div>  
				<span><%=ProcessoParteBeneficiodt.getProcessoBeneficio()%></span>
				<br />
				
				<div>Data Inicial</div> 
				<span><%=ProcessoParteBeneficiodt.getDataInicial()%></span>
				
				<div>Data Final</div> 
				<span><%=ProcessoParteBeneficiodt.getDataFinal()%></span>
				<br />
				
				
			</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>