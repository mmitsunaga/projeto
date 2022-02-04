<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>

<jsp:useBean id="AudienciaFisicodt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaFisicoDt" />

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>		   		   
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>		
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo;Agendamento de Audiência - Processo Físico</h2></div>
	    	
	    	<form action="AudienciaFisico" method="post" name="Formulario" id="Formulario">
	    		
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden" id="PaginaAnterior" name="PaginaAnterior" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input type="hidden" id="__Pedido__" name=__Pedido__ value="<%=request.getAttribute("__Pedido__")%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Agendamento de Audiência - Processo Físico </legend>
	
						<label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo </label><br> 
				    	<input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="25" value="<%=AudienciaFisicodt.getProcessoNumero()%>" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarSoNumero(this, event)" />
				    	<span><small><em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em></small></span>
						<br />
						
						<label class="formLocalizarLabel">*Tipo da Audiência
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaTipo" name="imaLocalizarAudienciaTipo" readonly="readonly" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
						</label><br>
						
						<input class="formLocalizarInput" name="AudienciaTipo" id="AudienciaTipo" readonly type="text" size="87" maxlength="100" value="<%=AudienciaFisicodt.getAudienciaTipo()%>" />
						<input type="hidden" id="Id_AudienciaTipo" name="Id_AudienciaTipo" value="<%=AudienciaFisicodt.getId_AudienciaTipo()%>" />
						<br />
						
						<label class="formEdicaoLabel" for="Id_ProcessoBeneficio">*Tipo Agendamento</label><br>  
						<input type="radio" name="TipoAgendamento" value="1" <%=AudienciaFisicodt.isAgendamentoManual() ? "checked" : ""%> />Manual
			       		<input type="radio" name="TipoAgendamento" value="2" <%=AudienciaFisicodt.isAgendamentoAutomatico() ? "checked" : ""%> />Automático
						<br />
				
						<div id="divConfirmarSalvar" class="ConfirmarSalvar">
		        			<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
		        			<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		           			<% } %>
							<br />
							           			
							<input name="btnConfirmar" type="submit" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>')">
		        			
		      			</div>
		      		</fieldset>
		      	</div>
				
				<%@include file="Padroes/Mensagens.jspf"%> 
			</form>
		</div>
	</body>
</html>