<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi {display: none;}
	</style>
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area">
			<h2>&raquo; | <%=request.getAttribute("tempPrograma")%> |</h2>
		</div>
			<form action="VincularGuiaInicialSegundoGrauProcesso" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				
				<div id="divEditar" class="divEditar">
				
					<fieldset id="formLocalizar" class="formLocalizar"> 
						<legend id="formLocalizarLegenda" class="formLocalizarLegenda"><%=request.getAttribute("tempPrograma")%></legend>
						
						<div> N�mero Processo: 
							<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
						</div>
						<br />
						
						<label id="formLocalizarLabel" class="formLocalizarLabel">
							Informe o n�mero da Guia Inicial de Segundo Grau(Somente N�meros) Ex. <strong>22152109</strong>
							<br/>
							<font color='red'><b>Por favor, caso a s�rie de sua guia seja final 9 emitida pelo site do Tribunal de Justi�a de Goi�s ou pelo sistema SPG, ent�o adicione 0(zero) antes do 9, ou seja, 09 ao final do n�mero completo da guia. A s�rie � composta por dois d�gitos.</b></font>
						
							<br />
							<br />
						</label>
						
						<br />
						<br />
						<br />
						<br />
						
						<div style="width: 150px;">N�mero da Guia</div>
						<span>
							<input id="numeroCompletoGuiaInicialSegundoGrau" class="formLocalizarInput" name="numeroCompletoGuiaInicialSegundoGrau" type="text" value="<%=request.getAttribute("numeroCompletoGuiaInicialSegundoGrau")%>" size="20" maxlength="11" onkeypress="return DigitarSoNumero(this, event)" title="Informe somente o n�mero da Guia Inicial de Segundo Grau" />
						</span>
						
						<br />
						<br />
						
						<div style="width: 250px;">N�mero do Processo Completo</div>
		               	<span>
		               		<input id="numeroProcessoCompleto" class="formLocalizarInput" name="numeroProcessoCompleto" type="text" value="<%=request.getAttribute("numeroProcessoCompleto")%>" size="30" maxlength="30" title="Informe somente o n�mero completo do Processo" />
		               	</span>
												
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" title="Localizar e Vincular Guia Inicial de Segundo Grau" value="Localizar e Vincular Guia Inicial de Segundo Grau" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar %>');" />
						</div>
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					
					<%@ include file="Padroes/Mensagens.jspf" %>
				</div>
			</form>
			<br /><br />
		</div>	
</body>
</html>