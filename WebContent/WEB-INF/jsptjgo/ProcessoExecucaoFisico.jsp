<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>

<html>
	<head>
		<title>Processo Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript">
		function consultarGR(){
			AlterarValue('PaginaAtual', '-1');
			AlterarValue('PassoEditar', '15');
			var form = document.forms[0];
			form.submit();
		}
		function consultarProcesso(){
			AlterarValue('PaginaAtual', '-1');
			AlterarValue('PassoEditar', '22');
			var form = document.forms[0];
			form.submit();
		}
		</script>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen'></link>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Processo de Execução - Migrar Processo Físico</h2></div>
			
			<form action="ProcessoExecucao" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
				<%@ include file="ProcessoExecucaoPassos.jspf" %>
				
				<div id="divEditar" class="divEditar">
				    <fieldset class="formEdicao">					    	
						<legend class="formEdicaoLegenda">Processo Físico</legend>
				    	<em><strong><p/>Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em>
						<br /><br />
                       	<label class="formEdicaoLabel" for="ProcessoFisicoNumero">*Número do Processo de Execução Penal</label><br> 
				    	<input class="formEdicaoInput" name="ProcessoFisicoNumero" id="ProcessoFisicoNumero"  type="text" size="30" maxlength="25" value="<%=ProcessoExecucaodt_PE.getProcessoFisicoNumero()%>" onkeyup=" autoTab(this,25)"
								onkeypress="mascara1(this, numeroProcessoCNJ)" onBlur="preencheZeros(this,25)"/>
						<%if (!ProcessoExecucaodt_PE.isPodeCadastrarProcessoFisico()){%>
							<input align="middle" name="imgInserir" type="submit" value="Consultar Processo Cadastrado" onclick="javascript:consultarProcesso();">
						<% }%>
							
				    	<br /><br />

						<% if (ProcessoExecucaodt_PE.isPodeCadastrarProcessoFisico()){	%>
				    	<% if (ProcessoExecucaodt_PE.getCadastroTipo() == 2){ %>
						<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Juiz Responsável
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
						</label><br>  
		    			
		    			<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=ProcessoExecucaodt_PE.getServentiaCargo()%>"/><br />
						<% } %>
				    </fieldset>

					<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Ação Penal</legend>
						<fieldset  class="formEdicao">
							<legend class="formEdicaoLegenda">Caso deseje - Consulte AQUI a Guia de Recolhimento no SPG</legend>
							<label class="formEdicaoLabel" for="NumeroGR">Nº da Guia de Recolhimento</label><br>    
							<input class="formEdicaoInput" name="NumeroGuiaRecolhimento" id="NumeroGuiaRecolhimento" type="text" size="10" maxlength="8" value="<%=ProcessoExecucaodt_PE.getNumeroGuiaRecolhimento()%>" onkeypress="return DigitarSoNumero(this, event)"/>
							<input class="formEdicaoInput" name="AnoGuiaRecolhimento" id="AnoGuiaRecolhimento" type="text" size="4" maxlength="4" value="<%=ProcessoExecucaodt_PE.getAnoGuiaRecolhimento()%>" onkeypress="return DigitarSoNumero(this, event)"/>
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarGR" name="imaLocalizarGR" type="image"  src="./imagens/execpen_icons/buscar_gr.jpg"  onclick="javascript:consultarGR();" title="Consultar Guia de Recolhimento no SPG" > (99999999/aaaa)
							<!--<a href="#" onclick="javascript:consultarGR();">Consultar Guia de Recolhimento - SPG</a>-->
							<br />
						</fieldset>
						<br />
						<label class="formEdicaoLabel" for="GuiaRecolhimento">*Tipo da Guia de Recolhimento</label><br>
						<input id="radioGuiaRecolhimento" name="radioGuiaRecolhimento" type="radio" value="D" 
							<% if (ProcessoExecucaodt_PE.getGuiaRecolhimento().equalsIgnoreCase("D")){%> checked <%} %>
							/>Definitiva &nbsp;
						<input id="radioGuiaRecolhimento" name="radioGuiaRecolhimento" type="radio" value="P"
							<% if (ProcessoExecucaodt_PE.getGuiaRecolhimento().equalsIgnoreCase("P")){%> checked <%} %> 
							/>Provisória &nbsp;
						<br /><br />
						<label class="formEdicaoLabel" for="NumeroAcaoPenal">*Nº do Processo de Ação Penal</label><br>    
					    <input class="formEdicaoInput" name="NumeroAcaoPenal" id="NumeroAcaoPenal" type="text" size="20" maxlength="15" value="<%=ProcessoExecucaodt_PE.getNumeroAcaoPenal()%>" onkeypress="return DigitarSoNumero(this, event)"/>
						<br />
						<label class="formEdicaoLabel" for="Id_CidadeOrigem">*Comarca de Origem
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarCidade" name="imaLocalizarCidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >							
						</label><br>  
						
					    <input class="formEdicaoInputSomenteLeitura" readonly name="CidadeOrigem" id="CidadeOrigem" type="text" size="54" maxlength="54" value="<%=ProcessoExecucaodt_PE.getCidadeOrigem()%>"/>				    		
						<input class="formEdicaoInputSomenteLeitura"  readonly name="EstadoOrigem" id="EstadoOrigem" type="text" size="2" maxlength="2" value="<%=ProcessoExecucaodt_PE.getEstadoOrigem()%>"/><br />
			    	    
						<label class="formEdicaoLabel" for="VaraOrigem">*Vara de Origem</label><br>    
					    <input class="formEdicaoInput" name="VaraOrigem" id="VaraOrigem" type="text" size="66" maxlength="100" value="<%=ProcessoExecucaodt_PE.getVaraOrigem()%>"/><br />
					</fieldset>
					<br /><br />

					<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Execução Penal</legend>
					    <input name="Id_ProcessoParte" type="hidden" value="<%=request.getAttribute("Id_ProcessoParte")%>" />
						<%@ include file="PartesProcessoExecucao.jspf"%> 
						<br />
<%if (ProcessoExecucaodt_PE.isProcessoNovo()){%>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Características</legend><br>
<% if (ProcessoExecucaodt_PE.getCadastroTipo() == 3){ %>
							<label class="formEdicaoLabel" for="Id_Comarca">*Comarca
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >				    	    
							</label><br>  
				    		
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Comarca" id="Comarca" type="text" size="30" maxlength="100" value="<%=ProcessoExecucaodt_PE.getComarca()%>"/>
				    	    
						    <label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Área de Distribuição
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >  
						    </label><br>  
				    		
				    		<input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="50" maxlength="100" value="<%=ProcessoExecucaodt_PE.getProcessoDt().getAreaDistribuicao()%>"/><br />
<%} %>
						    <label class="formEdicaoLabel" for="Id_ProcessoTipo">*Classe CNJ
						     <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >  
						    </label><br>  
						   
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="90" maxlength="100" value="<%=ProcessoExecucaodt_PE.getProcessoDt().getProcessoTipo()%>"/><br />
				
				    		<label class="formEdicaoLabel" for="Id_Assunto">*Assunto CNJ
				    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >  
				    		</label><br>  
				    		
				    		<input class="formEdicaoInputSomenteLeitura" readonly name="Assunto" id="Assunto" type="text" size="90" maxlength="100" value="<%=ProcessoExecucaodt_PE.getAssunto()%>"/>
				    		<%@ include file="AssuntosProcessoExecucao.jspf"%>

				    		<label class="formEdicaoLabel" for="Id_ProcessoPrioridade">Prioridade
				    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoPrioridade" name="imaLocalizarProcessoPrioridade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >
						    <input class="FormEdicaoimgLocalizar" name="imaLimparPrioridade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoPrioridade','ProcessoPrioridade'); return false;" title="Limpar Prioridade do Processo">  
				    		</label><br> 
				    		<input type="hidden" name="Id_ProcessoPrioridade" id="Id_ProcessoPrioridade"> 
						    
						    <input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoPrioridade" id="ProcessoPrioridade" type="text" size="60" maxlength="100" value="<%=ProcessoExecucaodt_PE.getProcessoDt().getProcessoPrioridade()%>"/>
					    </fieldset>
<%} %>	
					</fieldset>

					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						<input name="imgInserir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','5');"> 
						
				    </div>
<%} %>	
					
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>