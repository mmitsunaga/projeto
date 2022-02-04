<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioRecursoRepetitivoDt"%>
<jsp:useBean id="RelatorioRecursoRepetitivodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioRecursoRepetitivoDt"/>
<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='/js/jquery.js'></script>
		<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioRecursoRepetitivo" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')">	
			</div>
			<div id="divEditar" class="divEditar">
			
				

				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Período</legend>
					<label class="formEdicaoLabel" for="DataInicial">*Data Inicial</label><br> 
					<input class="formEdicaoInput" name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="<%=RelatorioRecursoRepetitivodt.getDataInicial()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
					<img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)"/>
					<br/>
					<label class="formEdicaoLabel" for="DataFinal">*Data Final</label><br> 
					<input class="formEdicaoInput" name="DataFinal" id="DataFinal"  type="text" size="10" maxlength="10" value="<%=RelatorioRecursoRepetitivodt.getDataFinal()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
					<img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)"/><br />
				</fieldset>
				
				
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>   
	</body>
</html>