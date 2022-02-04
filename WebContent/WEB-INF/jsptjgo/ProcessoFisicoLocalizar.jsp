<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="processoFisicoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoFisicoDt"/>


<html>
	<head>
	
	<title>Consultar Processo Fisico</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	    
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>	
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		  	
				<form action="ProcessoFisico" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				
				    <fieldset class="formEdicao">
				   	<legend class="formEdicaoLegenda">Dados do Processo</legend>
			 											
					<label class="formEdicaoLabel" for="numeroProcesso">Número Processo</label><br> 
	    			<input class="formEdicaoInput" name="numeroProcesso" id="numeroProcesso"  type="text" size="30" maxlength="18" value="<%=processoFisicoDt.getNumeroProcesso()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o número do processo a ser consultado"/>
	    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgSubmeter" type="submit" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
					<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"> 
					</div>
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
		</form>
				
				
