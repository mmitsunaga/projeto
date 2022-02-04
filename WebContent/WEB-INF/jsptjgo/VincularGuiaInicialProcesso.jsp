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
	  	
		<div class="area"><h2>&raquo; | <%=request.getAttribute("tempPrograma")%> |</h2></div>
			<form action="VincularGuiaInicialProcesso" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
								
				<div id="divEditar" class="divEditar">
				
					<fieldset id="formLocalizar" class="formLocalizar"> 
						<legend id="formLocalizarLegenda" class="formLocalizarLegenda"><%=request.getAttribute("tempPrograma")%></legend>
						
						<div> Número Processo: 
							<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumeroCompleto()%></a></span>
						</div>
						<br />
						
						<label id="formLocalizarLabel" class="formLocalizarLabel">
							Informe o número da Guia (Somente Números) Ex. <strong>22152109</strong>
							<br/>
							<font color='red'><b>- Por favor, caso a série de sua guia seja final 9 emitida pelo site do Tribunal de Justiça de Goiás ou pelo sistema SPG, então adicione 0(zero) antes do 9, ou seja, 09 ao final do número completo da guia. A série é composta por dois dígitos.</b></font>
							<br/>
							<font color='red'><b>- Ao informar o número do processo, por favor, adicionar os pontos. Exemplo: 1234567.12.2018.1.12.1234</b></font>
							
							<br />
							<br />
						</label>
						
						<br />
						<br />
						<br />
						<br />
						<br />
						<br />
						
						<div style="width: 150px;">Número da Guia</div>
		               	<span>
		               		<input id="numeroCompletoGuiaInicial" class="formLocalizarInput" name="numeroCompletoGuiaInicial" type="text" value="<%=request.getAttribute("numeroCompletoGuiaInicial")%>" size="20" maxlength="11" onkeypress="return DigitarSoNumero(this, event)" title="Informe somente o número da Guia Inicial" />
		               	</span>
						
						<br />
						<br />
						
						<div style="width: 250px;">Número do Processo Completo</div>
		               	<span>
		               		<input id="numeroProcessoCompleto" class="formLocalizarInput" name="numeroProcessoCompleto" type="text" value="<%=request.getAttribute("numeroProcessoCompleto")%>" size="30" maxlength="30" title="Informe somente o número completo do Processo" />
		               	</span>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" title="Localizar e Vincular Guia Inicial" value="Localizar e Vincular Guia Inicial" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar %>');" />
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