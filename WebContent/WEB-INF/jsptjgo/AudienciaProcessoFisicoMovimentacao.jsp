<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="AudienciaFisicoMovimentacaoDt" class= "br.gov.go.tj.projudi.dt.AudienciaFisicoMovimentacaoDt" scope="session"/>

<html>
<head>
	<title>Concluir Audiência - Processo Físico</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
   	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>		
	<script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>	
	<script>
		function alterarValorRadio(tipoConsulta){
			if (tipoConsulta == "1") {
				$("#divValorAcordo").show();				
			} else {
				$("#divValorAcordo").hide();				
			}			
		}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Concluir Audiência - Processo Físico </h2></div>
		
		<form action="AudienciaProcessoFisicoMovimentacao" method="post" name="Formulario" id="Formulario">
		
			<%  
			   AudienciaDt audienciaDt = AudienciaFisicoMovimentacaoDt.getAudienciaDt();
			%>
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>">			
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			
			<div id="divEditar" class="divEditar">				
				<fieldset class="formEdicao"> 
				    <legend class="formEdicaoLegenda">Dados da Audiência - Processo Físico</legend>
					<fieldset>						
						<input type="hidden" name="AudienciaTipoCodigo" id="AudienciaTipoCodigo" value="<%=AudienciaFisicoMovimentacaoDt.getAudienciaTipoCodigo()%>" />
						<legend class="formEdicaoLegenda"> Dados Audiência <%=audienciaDt.getAudienciaTipo()%> - Processo Físico </legend>
						
						<label class="formEdicaoLabel"> Data </label><br>
						<span class="spanDestaque"> <%=audienciaDt.getDataAgendada()%> </span>							
						
						<br />				
						<label class="formEdicaoLabel"> Processo </label><br>
						<span class="spanDestaque">
							<%=AudienciaFisicoMovimentacaoDt.getProcessoNumero()%>							
						</span>
																							
						<br />													
						<label class="formEdicaoLabel"> *Status </label><br>  
						<input type="hidden" name="AudienciaStatus" id="AudienciaStatus" value="<%=AudienciaFisicoMovimentacaoDt.getAudienciaStatus()%>" />
						<select name="AudienciaStatusCodigo" id="AudienciaStatusCodigo" size="1" onchange="capturaTextoSelect('AudienciaStatusCodigo', 'AudienciaStatus');" <%=((AudienciaFisicoMovimentacaoDt.isAudienciaProcessoStatusSomenteLeitura())?"disabled=disabled style=\"color: #000000; background-color: #ffffff;\" class=\"formEdicaoInputSomenteLeitura\"":"")%>>
							<option value="-1">--Selecione o Status da Audiência-- </option>
							<%
								List listaAudienciaStatus = AudienciaFisicoMovimentacaoDt.getListaAudienciaProcessoStatus();
								for (int i=0;i<listaAudienciaStatus.size();i++){
									AudienciaProcessoStatusDt audienciaStatusDt = (AudienciaProcessoStatusDt)listaAudienciaStatus.get(i);
							%>
								<option value="<%=audienciaStatusDt.getAudienciaProcessoStatusCodigo()%>" <%=(AudienciaFisicoMovimentacaoDt.getAudienciaStatusCodigo().equals(audienciaStatusDt.getAudienciaProcessoStatusCodigo())?"selected":"")%>>
									<%=audienciaStatusDt.getAudienciaProcessoStatus()%>
								</option>
							<%		
								}
							%> 	  						   
						</select>	
						<% if(audienciaDt.devePossuirIndicadorDeAcordo()) {%>
							<br />						
							<label class="formEdicaoLabel" for="Acordo"> *Houve Acordo? </label><br>  
							<input type="radio" name="Acordo" value="1" onclick="alterarValorRadio('1')" <%=AudienciaFisicoMovimentacaoDt.getAudienciaProcessoFisicoDt().isHouveAcordo() ? "checked" : ""%> />Sim
				       		<input type="radio" name="Acordo" value="0" onclick="alterarValorRadio('0')" <%=AudienciaFisicoMovimentacaoDt.getAudienciaProcessoFisicoDt().isNaoHouveAcordo() ? "checked" : ""%> />Não
			       		
			       			<div id="divValorAcordo">
					       		<label class="formEdicaoLabel" for="ValorAcordo"> *Valor do Acordo</label><br>
					    		<input class="formEdicaoInput" name="ValorAcordo" id="ValorAcordo"  type="text" size="20" maxlength="20" value="<%= AudienciaFisicoMovimentacaoDt.getAudienciaProcessoFisicoDt().getValorAcordo() %>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/>
				    		</div>
						<% } %>	       																															
					</fieldset>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
					</div>					
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	
</body>
	<script type="text/javascript">
		$(document).ready(function(){
			alterarValorRadio($('input[name=Acordo]:checked', '#Formulario').val());		    
		});		
	</script>
</html>