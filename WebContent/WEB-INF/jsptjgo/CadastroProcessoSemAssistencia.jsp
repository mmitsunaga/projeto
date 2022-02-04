<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

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
	  	
		<div class="area"><h2>&raquo; | <%=request.getAttribute("tempPrograma")%> | Formul&aacute;rio de Consulta da Guia Inicial</h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>" />
				
				<div id="divEditar" class="divEditar">
				
					<fieldset id="formLocalizar" class="formLocalizar"> 
						<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Cadastro de Processo sem Assistência</legend>
						
						<br />
						<label id="formLocalizarLabel" class="formLocalizarLabel">
							Informe o número da Guia Inicial (Somente Números)
						</label>
						<br />
						<font color='red'><b>Por favor, caso a série de sua guia seja final 9 emitida pelo site do Tribunal de Justiça de Goiás ou pelo sistema SPG, então adicione 0(zero) antes do 9, ou seja, 09 ao final do número completo da guia. A série é composta por dois dígitos.</b></font>
						
						<br />
						<br />
						
						<input id="numeroCompletoGuiaInicial" class="formLocalizarInput" name="numeroCompletoGuiaInicial" type="text" value="" size="20" maxlength="11" onkeypress="return DigitarSoNumero(this, event)" title="Informe somente o número da Guia Inicial" />
						
						<p />
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" title="Localizar Guia Inicial" value="Localizar Guia Inicial" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar %>');" />
						</div>
					</fieldset>
					
				</div>
			</form>
			<br /><br />
		</div>
		
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>