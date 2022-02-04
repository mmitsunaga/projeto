<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="VincularGuiaComplementarProcessodt" scope="session" class= "br.gov.go.tj.projudi.dt.VincularGuiaComplementarProcessoDt"/>

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
			<form action="VincularGuiaComplementarProcesso" method="post" name="Formulario" id="Formulario">
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
						
						<label id="formLocalizarLabel" class="formLocalizarLabel"> Informe o número da Guia Complementar (Somente Números) Ex. <strong>22152109</strong> </label><br> 
						<input id="NumeroCompletoGuiaComplementarVincular" class="formLocalizarInput" name="NumeroCompletoGuiaComplementarVincular" type="text" value="<%=VincularGuiaComplementarProcessodt.getNumeroGuiaCompleto()%>" size="20" maxlength="11" onkeypress="return DigitarSoNumero(this, event)" title="Informe somente o número da Guia Complementar" />
						
						<div class="col100">
						    <label class="formEdicaoLabel" for="Id_ProcessoTipo">
						    	<a href="http://www.cnj.jus.br/sgt/consulta_publica_classes.php" target="blank" title="Clique neste link para visualizar a tabela detalhada de classes do CNJ">*Classe</a>
						    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						    	<input class="FormEdicaoimgLocalizar" name="imaLimparPrioridade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoTipo','ProcessoTipo'); return false;" title="Limpar Classe">  
						    </label><br>  
						    <input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo">
						    <input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="113" maxlength="100" value="<%=VincularGuiaComplementarProcessodt.getProcessoTipo()%>"/><br />
						</div>
						<br />
												
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" title="Localizar e Vincular Guia Complementar" value="Localizar e Vincular Guia Complementar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar %>');" />
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