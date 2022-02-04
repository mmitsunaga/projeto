<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDtBase" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia Complementar da Inicial</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>
<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaComplementar" id="GuiaComplementar">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" name="Id_Comarca" id="Id_Comarca" value="<%=GuiaEmissaoDt.getId_Comarca()%>" />
			<input type="hidden" name="Id_AreaDistribuicao" id="Id_AreaDistribuicao" value="<%=GuiaEmissaoDt.getId_AreaDistribuicao()%>" />
			<input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=GuiaEmissaoDtBase.getId_ProcessoTipo()%>" />
						
			<div id="divEditar" class="divEditar">
                
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Dados da Guia a ser complementada </legend>
				    
					<div> Número da Guia Complementar </div>
					<span class="span1"><%= Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDtBase.getNumeroGuiaCompleto())%></span>
					<br /><br /><br />
					
					<div> Classe </div>
				    <span style="width: 500px;"><%=GuiaEmissaoDtBase.getProcessoTipo()%></span>
				    <br /><br />
				    
				    <div> Valor da Causa Informado na Guia </div>
					<span class="span1">R$ <%=Funcoes.FormatarDecimal(GuiaEmissaoDtBase.getNovoValorAcaoAtualizado())%></span>
					<br /><br />
				</fieldset>
				
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						* Novos Dados?
					</legend>
					
					<div id="divComarca"> * Nova Comarca </div>
					<span>
						<input id="imaLocalizarComarca" name="imaLocalizarComarca" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
                    	<input class="formEdicaoInputSomenteLeitura" readonly name="Comarca" id="Comarca" type="text" size="30" maxlength="100" value="<%=GuiaEmissaoDt.getComarca()%>"/>
					</span>
					
					<br /><br />
					
					<div id="divAreaDistribuicao"> * Nova Área de Distribuição </div>
					<span style="width: 500px;">
						<input id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
						<input class="formEdicaoInputSomenteLeitura" readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="60" maxlength="100" value="<%=GuiaEmissaoDt.getAreaDistribuicao()%>"/>
					</span>
					
					<br /><br />
					
					<div>
						* Nova Classe
					</div>
					<span style="width: 500px;">
						<input id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
                    	<input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="60" maxlength="100" value="<%=GuiaEmissaoDt.getProcessoTipo()%>"/>
					</span>
				</fieldset>
                
				
                <fieldset id="VisualizaDados">
                	<legend>
                		Novo Valor da Causa
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel"> Valor da Causa (R$) </label><br>
					
						<input class="formEdicaoInputSomenteLeitura" type="text" name="valorAcao" id="valorAcao" readonly value="<%=Funcoes.FormatarDecimal(GuiaEmissaoDtBase.getNovoValorAcaoAtualizado())%>" maxlength="15" />
					</div>
					
                	<div class="col30"><label class="formEdicaoLabel"> Novo Valor da Causa (R$)</label><br>
					
						<input type="text" name="novoValorAcao" id="novoValorAcao" value="<%=Funcoes.FormatarDecimal(GuiaEmissaoDt.getNovoValorAcaoAtualizado())%>" maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" />
					</div>
					
                </fieldset>
                
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgPreviaCalculo" value="Prévia do Cálculo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" >
                    
                    	Prévia do Cálculo
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	
                    	Limpar
                    </button>
                </div>
                
			</div>
			
			<br/><br/>
		
	  	</form>   
	</div>
	<%@ include file="Padroes/reCaptcha.jspf" %>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>