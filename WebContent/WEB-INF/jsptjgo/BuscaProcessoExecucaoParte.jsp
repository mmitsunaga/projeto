<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<html>
	<head>
		<title>Consulta de Sentenciados</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		<link href="./css/Paginacao.css" type="text/css" rel="stylesheet" />
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
      	<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoCaracteres.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<%@ include file="./js/Paginacao.js"%>
		<%if (request.getAttribute("tempRetorno").equals("ProcessoExecucao")) {%>
			<script type="text/javascript">
				$(document).ready(function() {
					$("#Nascimento").mask("99/99/9999");
				});
			
				function selecionaSubmete(id, descricao){
					var form =	document.getElementById('Formulario');
					form.Id_ProcessoParte.value = id;
					form.PaginaAtual.value = '<%=Configuracao.Editar%>' ;
					form.submit();
				}
			</script>
		<%} %>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Consulta de Sentenciados </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda"> Parâmetros de Consulta </legend>
						<br></br>
						<label class="formEdicaoLabel" for="NumeroProcesso" style="width: 20%">Nº do Processo de Execução</label><br> 
						<input type="text" name="NumeroProcesso" id="NumeroProcesso" class="formEdicaoInput" size="30" maxlength="18" onkeypress="return DigitarSoNumero(this, event)">
						<br/>
		    			<label class="formEdicaoLabel" for="Cpf" style="width: 20%">CPF</label><br> 
		    			<input type="text" name="CpfCnpj" class="formEdicaoInput" size="30" maxlength="18" onkeypress="return DigitarSoNumero(this, event)">
		    			<br />
						<label class="formEdicaoLabel" for="Nome" style="width: 20%">Nome do Sentenciado</label><br> 
		    			<input class="formEdicaoInput" name="Sentenciado" id="Sentenciado"  type="text" size="50" maxlength="50" onkeyup=" autoTab(this,50)" >
		    			<br />
						<label class="formEdicaoLabel" for="NomeMae" style="width: 20%">Nome da Mãe</label><br> 
		    			<input class="formEdicaoInput" name="Mae" id="Mae" type="text" size="50" maxlength="50" onkeyup=" autoTab(this,50)" />
		    			<br />			
						<label class="formEdicaoLabel" for="DataNascimento" style="width: 20%">Data de Nascimento</label><br> 
		    			<input class="formEdicaoInput" name="Nascimento" id="Nascimento" type="text" size="10" maxlength="10" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)">
		    			<input type="image" id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this);return false;"/>
		    			<br />									
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Consultar" onclick="AlterarValue('PassoEditar','2')">
							<%if ((request.getAttribute("PassoEditar").toString().equals("2"))){// && (((request.getAttribute("Sentenciado")!=null) && (!request.getAttribute("Sentenciado").equals(""))) || ((request.getAttribute("CpfCnpj")!=null) && (!request.getAttribute("CpfCnpj").equals(""))) || ((request.getAttribute("DataNascimento")!=null) && (!request.getAttribute("DataNascimento").equals(""))) || ((request.getAttribute("numeroProcesso")!=null) && (!request.getAttribute("numeroProcesso").equals(""))))){%>
							<input name="imgInserir" type="submit" value="Incluir Sentenciado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','16');">
							<%} %>
							<input name="imgCancelar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','7');">
						</div>
						</fieldset>
						
						<% //String teste = request.getAttribute("Sentenciado").toString();
						if (request.getAttribute("Sentenciado")!=null){
							%>
							<%@ include file="DadosProcessoParteExecucao.jspf"%>
							</div>
							<%if (liTemp!=null){%>
								<%@ include file="./Padroes/Paginacao.jspf"%>
							<%}%>
						<%} %>
					
				
				<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
		</div>
	</body>
</html>
	