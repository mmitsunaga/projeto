<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="GuiaEmissaoDtBase" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Geração de Boleto Consulta</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>	
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>
<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; | Geração de Boleto / Consultar Itens da Guia | Formulário de Consulta</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GerarBoleto" id="GerarBoleto">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PassoConsultar" name="PassoConsultar" type="hidden" value="<%=request.getAttribute("PassoConsultar")%>">
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Informe o número da Guia (Somente Números) Ex. <strong>22152109</strong></legend>                	
                	<div class="col30">Número da Guia </div>
                	<span>                		
                		<input class="formEdicaoInput" name="numeroGuiaConsulta" id="numeroGuiaConsulta"  type="text" maxlength="15" value="<%=GuiaEmissaoDtBase.getNumeroGuiaCompleto()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,15)">
                	</span>
                </fieldset>
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgGerarBoleto" value="Gerar Boleto" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoConsultar','1');" >                    
                    	Gerar Boleto
                    </button>
                    <button name="imgConsultarItens" value="Consultar Itens" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoConsultar','2');" >                    
                    	Consultar Itens
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >                    	
                    	Limpar
                    </button>
                </div>
                
			</div>
			
			<br/><br/>
			<%@ include file="Padroes/reCaptcha.jspf" %>
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>