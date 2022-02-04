<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<html>
	<head>
		<title>Processo Civel</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/MascararValor.js" ></script>
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; Cadastro de Processo Cível</h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
								
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="tempRetorno" name="tempRetorno" type="hidden" value="<%=request.getAttribute("tempRetorno")%>" />
				
				<%@ include file="ProcessoPassos.jspf" %>
				
				<div id="divPortaBotoes" class="divPortaBotoes" style="width: 955px; height: 23px; ">
					<input id="imgCarregar" alt="Carregar" class="imgCarregar" title="Carregar - Carrega os dados salvos" name="imaCarregar" type="image" src="./imagens/ex_ico_solucionar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','11');" />
				</div>
				
				<input type="hidden" id="dependente" name="dependente" value="<%=ProcessoCadastroDt.isProcessoDependente()%>">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Passo 1 - Dados do Processo Dependente</legend>
					    
						<%@ include file="PartesProcessoCivel.jspf"%> 
						
						<%@ include file="AdvogadosProcesso.jspf"%> 
					         	    
					    <fieldset>
					    	<legend class="formEdicaoLegenda">Características </legend>
			
				    		<label class="formEdicaoLabel" for="ProcessoNumeroDependente">*Processo Principal</label><br> 
				    		<input <%if((request.getAttribute("tempRetorno") != null &&  ( request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoCivelSemAssistencia") || request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoSegundoGrauCivelSemAssistenciaCt") ) ) ) { %>class="formEdicaoInputSomenteLeitura" readonly<%} else {%> class="formEdicaoInput" <%}%> name="ProcessoNumeroDependente" id="ProcessoNumeroDependente" type="text" size="30" maxlength="25" value="<%=ProcessoCadastroDt.getProcessoNumeroPrincipal()%>" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)"/>
				    		<span><small><em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em></small></span><br />    		
				
				 			<label class="formEdicaoLabel" for="Id_ProcessoTipo">
						    	<a href="http://www.cnj.jus.br/sgt/consulta_publica_classes.php" target="blank" title="Clique neste link para visualizar a tabela detalhada de classes do CNJ">*Classe</a>
						    <%if(!(request.getAttribute("tempRetorno") != null &&  ( request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoCivelSemAssistencia") || request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoSegundoGrauCivelSemAssistenciaCt") ) ) ) { %>
						    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						    <%} %>  
						    </label><br>  
						    
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="100" maxlength="100" value="<%=ProcessoCadastroDt.getProcessoTipo()%>"/><br />
							
							<%if((request.getAttribute("tempRetorno") != null &&  ( request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoCivelSemAssistencia") || request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoSegundoGrauCivelSemAssistenciaCt") ) )  && ProcessoCadastroDt.getNaturezaSPG() != null && ProcessoCadastroDt.getNaturezaSPG().trim().length() > 0) { %>
								<div class="col100">
						    	<label class="formEdicaoLabel" for="Id_NaturezaSPG">*Natureza</label><br>
						    	<input class="formEdicaoInputSomenteLeitura"  readonly name="NatruezaSPG" id="NatruezaSPG" type="text" size="113" maxlength="100" value="<%=ProcessoCadastroDt.getNaturezaSPG()%>"/><br />
						    	</div>
						    <%} %>   
				
				    		<%@ include file="AssuntosProcesso.jspf"%> 
					    	<br />
							
							<div class="col50">
							<label class="formEdicaoLabel" for="Id_ProcessoPrioridade">Prioridade
							 <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoPrioridade" name="imaLocalizarProcessoPrioridade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						    <input class="FormEdicaoimgLocalizar" name="imaLimparPrioridade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoPrioridade','ProcessoPrioridade'); return false;" title="Limpar Prioridade do Processo">  
							</label><br> 
				    		<input type="hidden" name="Id_ProcessoPrioridade" id="Id_ProcessoPrioridade"> 
						   
						    <input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoPrioridade" id="ProcessoPrioridade" type="text" size="60" maxlength="100" value="<%=ProcessoCadastroDt.getProcessoPrioridade()%>"/><br />
							</div>
							
							<div class="col30">
				    		<label class="formEdicaoLabel" for="Valor">*Valor</label><br> 
				    		<input <%if((request.getAttribute("tempRetorno") != null &&  ( request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoCivelSemAssistencia") || request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoSegundoGrauCivelSemAssistenciaCt") ) ) ) { %>class="formEdicaoInputSomenteLeitura" readonly<%} else {%> class="formEdicaoInput" <%}%>  name="Valor" id="Valor"  type="text" size="20" maxlength="20" value="<%=ProcessoCadastroDt.getValor()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/><br />    		
					    	</div>
					    	<div class="clear"></div>
					    </fieldset>	
					    
					    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Inserir" title="Insere os dados da tela no cadastro" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','5');"> 
							<input name="imgInserir" type="submit" value="Limpar" title= "Limpa os campos da tela" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('dependente','true');">
							<input name="imgInserir" type="submit" value="Salvar Dados" title="Salva os dados digitados na tela até o momento" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','12');">
					    </div>
    				</fieldset>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>