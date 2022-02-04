<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.HashMap"%>
<jsp:useBean id="RelatorioSumariodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioSumarioProdutividade" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
					src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
			</div/><br />
			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Período</legend>
					
					<label class="formLocalizarLabel" for="DataInicial">*Data Inicial</label>
				    <input class="formLocalizarInput" name="DataInicial" id="DataInicial" type="text" size="10" maxlength="10" value="<%=RelatorioSumariodt.getDataInicial()%>" /> 
				    <img id="calendarioDataInicial" class="calendario" src="./imagens/dlcalendar_2.gif" title="Data Inicial"  alt="Data Inicial" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" />
					<br> 
					<label class="formLocalizarLabel" for="DataFinal">*Data Final</label>
				    <input class="formLocalizarInput" name="DataFinal" id="DataFinal" type="text" size="10" maxlength="10" value="<%=RelatorioSumariodt.getDataFinal()%>" /> 
				    <img id="calendarioDataFinal" class="calendario" src="./imagens/dlcalendar_2.gif" title="Data Final"  alt="Data Final" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" />
					<br/>

					<label for="Aviso" style="float:left;color:red;"><small>ATENÇÃO: devido a sobrecarga de processamento, o intervalo entre as datas não pode ser superior a 90 (noventa) dias.</small></label><br> 
				</fieldset>

				<%if(UsuarioSessao.isDesembargador()){ %>
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Tipo de Relatório</legend>
						<label class="formEdicaoLabel" for="tipo_Relatorio">*Tipo de Relatório</label><br>  
						<input type="radio" name="OpcaoRelatorio" id="OpcaoRelatorio" value="1" checked="true"/>Assessores do magistrado
					    <input type="radio" name="OpcaoRelatorio" id="OpcaoRelatorio" value="2"/>Assistentes de gabinete
				    </fieldset>
				<%} %>
			    
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>