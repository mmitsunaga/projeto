<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Pré-Cálculo</title>
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
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formul&aacute;rio de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaPreviaCalculo" id="GuiaPreviaCalculo" >
			
			<%
			GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) request.getAttribute("GuiaEmissaoDt");
			%>
			
			<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input type="hidden" id="PassoEditar" name="PassoEditar" value="<%=request.getAttribute("PassoEditar")%>" />
			<input type="hidden" id="Id_GuiaEmissaoReferencia" name="Id_GuiaEmissaoReferencia" value="<%=guiaEmissaoDt.getId()%>" />
			<input type="hidden" id="legendFieldset" name="legendFieldset" value="<%=request.getAttribute("legendFieldset")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input type="hidden" id="porcentagemDescontoSelecionado" name="porcentagemDescontoSelecionado" value="<%=request.getAttribute("porcentagemDescontoSelecionado")%>" />
			<input type="hidden" id="quantidadeParcelasSelecionado" name="quantidadeParcelasSelecionado" value="<%=request.getAttribute("quantidadeParcelasSelecionado")%>" />
			<input type="hidden" id="quantidadeMaximaParcelasSelecionado" name="quantidadeMaximaParcelasSelecionado" value="<%=request.getAttribute("quantidadeMaximaParcelasSelecionado")%>" />
			
			<%if( request.getAttribute("mostrarAreaReemissaoGuia") != null && (Boolean)request.getAttribute("mostrarAreaReemissaoGuia") ) { %>
				<input type="hidden" id="reemitirGuiaSelecionado" name="reemitirGuiaSelecionado" value="<%=request.getAttribute("reemitirGuiaSelecionado")%>" />
			<%} %>
			
			<div id="divEditar" class="divEditar">
               
                <fieldset class="formEdicao">
                	<legend class="formEdicaoLegenda"><%=request.getAttribute("legendFieldset")%></legend>
                	
                	<%
                	if( processoDt != null && processoDt.getId_Processo() != null && processoDt.getProcessoNumero().length() > 0 ) {
                	%>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Processo</legend>
                	
                		<div>Número</div>
						<span>
							<a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2">
		                		<%=processoDt.getProcessoNumero()%>
		                	</a>
						</span>
                	</fieldset>
					<%}%>
                	
                	<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>Guia de Referência</legend>
						
						<div>Número da Guia</div>
						<span>
							<%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto())%>
						</span>
						
					</fieldset>
					
					<%if( request.getAttribute("mostrarDescontoGuia") != null && (Boolean)request.getAttribute("mostrarDescontoGuia") ) { %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend>Informe o Índice de Desconto para a nova Guia</legend>
							
							<div>Porcentagem de Desconto</div>
							<span>
								<input type="text" id="porcentagemDesconto" name="porcentagemDesconto" onkeyup="MascaraValor(this);autoTab(this,20);PercentualMaximo(this, '99,00');" onkeypress="return DigitarSoNumero(this, event)" <%=(request.getAttribute("porcentagemDescontoSelecionado") != null && !request.getAttribute("porcentagemDescontoSelecionado").toString().isEmpty())?"disabled":""%> value="<%=request.getAttribute("porcentagemDescontoSelecionado")%>" maxlength="5" />
								%
							</span>
							
						</fieldset>
                	<%}%>
                	
                	<%if( request.getAttribute("mostrarParcelamentoGuia") != null && (Boolean)request.getAttribute("mostrarParcelamentoGuia") ) { %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend>Informe a Quantidade de Parcelas</legend>
							
							<div>Quantidade de Parcelas Desejada</div>
							<span>
								<select id="quantidadeParcelas" name="quantidadeParcelas" <%=(request.getAttribute("quantidadeParcelasSelecionado") != null && !request.getAttribute("quantidadeParcelasSelecionado").toString().isEmpty())?"disabled":""%> onchange="validaQuantidadeParcelas(this);">
									<option value=""></option>
<%-- 									<%/*for(int i = 2; i <= GuiaEmissaoNe.getQuantidadeMaximaParcelas(guiaEmissaoDt.getId()); i++) {*/%> --%>
									<%
										for(int i = 2; i <= 30; i++) {%>
										<%if( i <= 5 ) { %>
											<option value="<%=i%>" <%=(request.getAttribute("quantidadeParcelasSelecionado") != null && request.getAttribute("quantidadeParcelasSelecionado").toString().equals(String.valueOf(i)))?"selected":""%> ><%=i%></option>
										<%} %>
										<%if( i == 5 ) { %>
											<option value=""></option>
											<option value="">Acima de 5 Parcelas?</option>
										<%} %>
										<%if( i > 5 ) { %>
											<option value="<%=i%>" <%=(request.getAttribute("quantidadeParcelasSelecionado") != null && request.getAttribute("quantidadeParcelasSelecionado").toString().equals(String.valueOf(i)))?"selected":""%> ><%=i%></option>
										<%} %>
									<%}%>
								</select>
							</span>
						</fieldset>
						
						<div id="divMotivoParcelamento" class="<%=(Boolean)request.getAttribute("mostrarDivMotivoParcelamento")?"":"DivInvisivel"%>">
							<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend>ATENÇÃO</legend>
								
								<br />
								Do parcelamento vide Art. 38-B da Lei 14.376/2002.
								<br />
								Art. 38-B. As custas iniciais podem ser parceladas em até 05 (cinco) vezes, por decisão do juiz competente para conhecer do pedido.
								<br />
								<br />
								
								<div>Justificativa</div>
								<span>
									<textarea id="motivoParcelamento" name="motivoParcelamento" cols="80" rows="5" <%=(Boolean)request.getAttribute("mostrarDivMotivoParcelamento")?"disabled":""%>><%=request.getAttribute("motivoParcelamentoSelecionado")%></textarea>
								</span>
							</fieldset>
						</div>
                	<%}%>
                	
                	<%if( request.getAttribute("mostrarPermissaoParcelamentoGuia") != null && (Boolean)request.getAttribute("mostrarPermissaoParcelamentoGuia") ) { %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend>Informe a Quantidade Máxima de Parcelas</legend>
							
							<div>Quantidade Máxima de Parcelas</div>
							<span>
								<select id="quantidadeMaximaParcelas" name="quantidadeMaximaParcelas" <%=(request.getAttribute("quantidadeMaximaParcelasSelecionado") != null && !request.getAttribute("quantidadeMaximaParcelasSelecionado").toString().isEmpty())?"disabled":""%>>
									<option value=""></option>
									<%for(int i = 0; i <= 24; i++) {%>
										<option value="<%=i%>" <%=(request.getAttribute("quantidadeMaximaParcelasSelecionado") != null && request.getAttribute("quantidadeMaximaParcelasSelecionado").toString().equals(String.valueOf(i)))?"selected":""%> ><%=i%></option>
									<%}%>
								</select>
							</span>
							
						</fieldset>
                	<%}%>
                	
                	<%if( request.getAttribute("mostrarAreaReemissaoGuia") != null && (Boolean)request.getAttribute("mostrarAreaReemissaoGuia") ) { %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend>Reemitir esta Guia Adicionando 15 dias a partir de Hoje</legend>
							
							<div>Nova Data de Vencimento</div>
							<span>
								<%=Funcoes.getDataVencimentoGuia15Dias() %>
							</span>
							
						</fieldset>
                	<%}%>
                	
                	<%if( request.getAttribute("mostrarBotoes") == null ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		
               			<button name="buttonVoltarProcessoGuias" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');" >			                    	
							Voltar
						</button>
						
						<button name="buttonEmitirGuiaDescontoParcelada" value="Emitir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Salvar%>');" >			                    	
							<%if( request.getAttribute("mostrarPermissaoParcelamentoGuia") != null && (Boolean)request.getAttribute("mostrarPermissaoParcelamentoGuia") ) { %>
								Alterar
							<%} else { %>
								Emitir
							<%} %>
						</button>
                		
                		<br />
                		
                	</fieldset>
                	<%} %>
							
				</fieldset>
                
			</div>
			
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			
		</form>
		
		<br /><br />
		
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>