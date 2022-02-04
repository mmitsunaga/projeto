<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.OficialSPGDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaCalculoNe"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Recurso STJ</title>
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
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaInicial" id="GuiaInicial">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" id="posicaoListaCustaExcluir" name="posicaoListaCustaExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                	<div> Número </div>
					<span><a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
                </fieldset>
                
                <br /><br />
				
				<!-- PROMOVENTES -->
				<%
					List listaPromoventes = processoDt.getListaPolosAtivos();
					   	    	if (listaPromoventes != null && listaPromoventes.size() > 0){
				%>
			   	<fieldset id="VisualizaDados" class="VisualizaDados">   
			   		<legend> Polo Ativo</legend>
			   		 
					<%
		   	    		for (int i=0;i < listaPromoventes.size();i++) {
			   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
			   		%>
			
			       	<div> Nome </div>
			       	<span class="span1"><%=parteDt.getNome()%></span>
			       	
			       	<div> CPF </div>
			        <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
			        <br/>
			        <%	}%>
				</fieldset>
				<br/><br/>
				<%} %>
			
				<!-- PROMOVIDOS -->
				<%
					List listaPromovidos = processoDt.getListaPolosPassivos();
					   			if (listaPromovidos != null && listaPromovidos.size() > 0){
				%>
				<fieldset id="VisualizaDados" class="VisualizaDados">
			   		<legend> Polo Passivo</legend>
			 		<%
			   				for (int i=0;i < listaPromovidos.size();i++){
			   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
				   	%>
			    	<div> Nome </div>
			       	<span class="span1"><%=parteDt.getNome()%></span>
			       	
			       	<div> CPF </div>
			        <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
			        <br/>
			        <%}%>
				</fieldset>
				<%} %>
                
                <br/><br/>
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Informações </legend>
				    
					<div> Serventia </div>
					<span class="span1"><%= processoDt.getServentia()%></span>
					<br />
					
					<div> Classe </div>
				    <span style="width: 500px;"><%=processoDt.getProcessoTipo()%></span>
				    <br />
				   	
				    <%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
				   	
				    <% if (!processoDt.getValor().equals("Null")){ %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
					<%} %>
				</fieldset>
				
				<br /><br />
				
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Quantidade de Folhas
                	</legend>
                	<span>
                		<input type="text" name="porteRemessaQuantidade" id="porteRemessaQuantidade" value="<%=GuiaEmissaoDt.getRemessaStjQuantidade()%>" maxlength="4" onkeypress="return DigitarSoNumero(this, event)" title="Informe a quantidade de folhas para o protocolo integrado." onblur="zerarCampoLimpo(this, '0')" />
                	</span>
                	
                </fieldset>
				
				<br /><br />
                
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgPreviaCalculo" value="Prévia do Cálculo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" >
                    	<img src="./imagens/16x16/calculadora.png" alt="Prévia do Cálculo" />
                    	Prévia do Cálculo
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	<img src="./imagens/16x16/edit-clear.png" alt="Limpar" />
                    	Limpar
                    </button>
                </div>
                
			</div>
			
			<br/><br/>
		
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>