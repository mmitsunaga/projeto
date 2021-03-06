<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.BairroLocomocaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.OficialSPGDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaCalculoNe"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaFinalNe"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Custas Finais</title>
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
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function() {
		
		  $("#chamateclado").click(function(evento){
		    $("#divTecladoVirtual").fadeToggle();	
			$("#senha").focus();
		  });
			
		  $("#wait").click(function(){
			  $("#modal").css("display","block");
			  $(".modal-bg").fadeIn();
		});
		
		$("#close").click(function(){
			  $(".modal-bg").fadeOut();		
			  $("#modal").fadeOut();
		  return false;
		});

		$( "#accordion0" ).accordion({
			  active: 1
		});
		
		$( "#accordion1" ).accordion({
			  active: 1
		});
		
		$("#accordion1").accordion({ autoHeight: false, collapsible: true });
		$("#accordion0").accordion({ autoHeight: false, collapsible: true });
					
	});
	</script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaInicial" id="GuiaInicial">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" id="posicaoListaCustaExcluir" name="posicaoListaCustaExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroContaVinculadaExcluir" name="posicaoListaBairroContaVinculadaExcluir" value="-1" />
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
		
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                	<div> Número</div>
					<span><a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
                </fieldset>
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Informaçőes </legend>
				    
					<%if(!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) {%>
						<div> Serventia </div>
						<span class="span1"><%= processoDt.getServentia()%></span>
						<br />
					<%} %>
					
					<div> Classe </div>
				    <span style="width: 500px;"><%=processoDt.getProcessoTipo()%></span>
				    <br />
				   	
				    <%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
				   	
				    <% if (!processoDt.getValor().equals("Null")){ %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
					<%} %>
				</fieldset>
				
				<fieldset id="VisualizaDados">
                	<legend>
                		Valor Atualizado
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel"> Valor da Causa </label><br>
					
						<input class="formEdicaoInputSomenteLeitura" type="text" name="valorAcao" id="valorAcao" readonly value="<%=processoDt.getValor()%>" maxlength="15" />
					</div>
					
					<div class="col30"><label class="formEdicaoLabel"> Data Recebimento do Processo </label><br>
					
						<input type="text" class="formEdicaoInputSomenteLeitura" name="dataBaseAtualizacao" id="dataBaseAtualizacao" value="<%=GuiaEmissaoDt.getDataBaseAtualizacao()%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</div>
					
					
					
					<div class="col30"><label class="formEdicaoLabel">Valor Base para Cálculo</label><br>
					
						<input type="text" name="novoValorAcao" id="novoValorAcao" value="<%=GuiaEmissaoDt.getNovoValorAcao()%>" maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" onBlur="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();"/>
					</div>
					
					<div class="clear"></div>
					
					<div class="col30"><label class="formEdicaoLabel"> Data Base para Cálculo </label><br>
					
						<input type="text" name="dataBaseFinalAtualizacao" id="dataBaseFinalAtualizacao" value="<%=GuiaEmissaoDt.getDataBaseFinalAtualizacao()%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" />
						<img id="calendarioDataBaseFinalAtualizacao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataBaseFinalAtualizacao,'dd/mm/yyyy',this)"/>
					</div>
					
					<div class="col30"><label class="formEdicaoLabel">Valor da Causa Base Atualizado</label><br>
					
						<input class="formEdicaoInputSomenteLeitura" type="text" name="novoValorAcaoAtualizado" id="novoValorAcaoAtualizado" readonly value="<%=GuiaEmissaoDt.getNovoValorAcaoAtualizado()%>" maxlength="15" />
					</div>
					
                </fieldset>
				
				<fieldset id="VisualizaDados">
                	<legend>
                		Rateio
                	</legend>
                	
                	<%
                	                		List listaPromoventes = processoDt.getListaPolosAtivos();
                	                	                	                				List listaPromovidos = processoDt.getListaPolosPassivos();
                	                	                	                				
                	                	                	                				String stringRateioParteJavascript = "new Array(";
                	                	                	                				String stringEmitirGuiaJavascript = "new Array(";
                	                	                	                				if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
                	                	                	                					for( int i = 0; i < listaPromoventes.size(); i++ ) {
                	                	                	                						ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
                	                	                	                						stringRateioParteJavascript += "'"+GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() + "',";
                	                	                	                						stringEmitirGuiaJavascript += "'"+GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() + "',";
                	                	                	                					}
                	                	                	                				}
                	                	                	                				if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
                	                	                	                					for( int i = 0; i < listaPromovidos.size(); i++ ) {
                	                	                	                						ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
                	                	                	                						stringRateioParteJavascript += "'"+GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() + "',";
                	                	                	                						stringEmitirGuiaJavascript += "'"+GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() + "',";
                	                	                	                					}
                	                	                	                				}
                	                	                	                				stringRateioParteJavascript += ")";
                	                	                	                				stringRateioParteJavascript = stringRateioParteJavascript.replace( ",)" , ")" );
                	                	                	                				
                	                	                	                				stringEmitirGuiaJavascript += ")";
                	                	                	                				stringEmitirGuiaJavascript = stringEmitirGuiaJavascript.replace( ",)" , ")" );
                	                	%>
                	
                	<div class="col30"><label class="formEdicaoLabel">Tipo de Rateio </label><br>
					
						<select id="rateioCodigo" name="rateioCodigo" onChange="mostrarDivRateioPartes('divRateioPartes',rateioCodigo.value,<%=stringRateioParteJavascript%>,<%=stringEmitirGuiaJavascript%>);">
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalNe.RATEIO_100_REQUERENTE))?"selected":"") %> value="<%=GuiaFinalNe.RATEIO_100_REQUERENTE%>" >100% Requerente</option>
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalNe.RATEIO_100_REQUERIDO))?"selected":"") %> value="<%=GuiaFinalNe.RATEIO_100_REQUERIDO%>">100% Requerido</option>
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalNe.RATEIO_50_50))?"selected":"") %> value="<%=GuiaFinalNe.RATEIO_50_50%>">50% Para Cada Parte</option>
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalNe.RATEIO_VARIAVEL))?"selected":"") %> value="<%=GuiaFinalNe.RATEIO_VARIAVEL%>">Rateio Variável</option>
						</select>
					</div>
					
					<div id="divRateioPartes" class="<%=request.getAttribute("visualizaDivRateioPartesVariavel")%>">
					
						Emitir?
						<%
						if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
							for( int i = 0; i < listaPromoventes.size(); i++ ) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
								%>
									<br />
									<%=parteDt.getNome()%>
									<%
									String variavelRateioParteVariavel = "0.00";
									if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()) != null ) {
										variavelRateioParteVariavel = request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()).toString();
									}
									String variavelEmitirGuiaParte = "";
									if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()) != null && request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()).equals(parteDt.getId()) ) {
										variavelEmitirGuiaParte = "checked";
									}
									%>
									<input type="text" id="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" name="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" value="<%=variavelRateioParteVariavel%>" maxlength="6" onkeyup="MascaraValor(this);autoTab(this,20);somarRateioPartesVariavel(this,<%=stringRateioParteJavascript%>);" onkeypress="return DigitarSoNumero(this, event)" />
									<input type="checkbox" id="<%=GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE%><%=parteDt.getId()%>" name="<%=GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE%><%=parteDt.getId()%>" value="<%=parteDt.getId()%>" title="Sim, emitir esta guia." <%=variavelEmitirGuiaParte%> onclick="tratamentoRateio50Porcento(<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>, this, rateioCodigo, <%=stringRateioParteJavascript%>);" />
								<%
							}
						}
						if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
							for( int i = 0; i < listaPromovidos.size(); i++ ) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
								%>
									<br />
									<%=parteDt.getNome()%>
									<%
									String variavelRateioParteVariavel = "0.00";
									if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()) != null ) {
										variavelRateioParteVariavel = request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()).toString();
									}
									String variavelEmitirGuiaParte = "";
									if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()) != null && request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()).equals(parteDt.getId()) ) {
										variavelEmitirGuiaParte = "checked";
									}
									%>
									<input type="text" id="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" name="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" value="<%=variavelRateioParteVariavel%>" maxlength="6" onkeyup="MascaraValor(this);autoTab(this,20);somarRateioPartesVariavel(this,<%=stringRateioParteJavascript%>);" onkeypress="return DigitarSoNumero(this, event)" />
									<input type="checkbox" id="<%=GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE%><%=parteDt.getId()%>" name="<%=GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE%><%=parteDt.getId()%>" value="<%=parteDt.getId()%>" title="Sim, emitir esta guia." <%=variavelEmitirGuiaParte%> onclick="tratamentoRateio50Porcento(<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>, this, rateioCodigo, <%=stringRateioParteJavascript%>);" />
								<%
							}
						}
						%>
						
						<hr />
						<b>Total</b>
						<input type="text" class="<%=request.getAttribute("formEdicaoInputSomenteLeituraRateio").toString()%>" id="rateioParteVariavelTotal" readonly value="<%=GuiaEmissaoDt.getTotalRateio()%>" onkeyup="MascaraValor(this);autoTab(this,20)" />
						<b>%</b>
					</div>
					
                </fieldset>
				
				<fieldset id="VisualizaDados">
					<%if( GuiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade() == null ) { %>
                	<legend>
                		Taxa Judiciária
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Tipo de Cobrança </label><br>
					
						<select id="descontoTaxaJudiciaria" name="descontoTaxaJudiciaria">
							<option <%=(GuiaEmissaoDt.getDescontoTaxaJudiciaria().equals(String.valueOf(GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0))?"selected":"") %> value="<%=GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0%>">Integral</option>
							<option <%=(GuiaEmissaoDt.getDescontoTaxaJudiciaria().equals(String.valueOf(GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_50))?"selected":"") %> value="<%=GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_50%>">Desconto 50%</option>
							<option <%=(GuiaEmissaoDt.getDescontoTaxaJudiciaria().equals(String.valueOf(GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_100))?"selected":"") %> value="<%=GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_100%>" selected >Isento</option>
						</select>
					</div>
					<%}
					else { %>
					<legend>
                		Taxa de Serviço
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel"> Taxa de Serviço Dívida Ativa </label><br>
					
						<select id="taxaServicoDividaAtivaQuantidade" name="taxaServicoDividaAtivaQuantidade">
							<option <%=(GuiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade().equals(GuiaEmissaoDt.VALOR_SIM)?"selected":"")%> value="<%=GuiaEmissaoDt.VALOR_SIM%>">SIM</option>
							<option <%=(GuiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade().equals(GuiaEmissaoDt.VALOR_NAO)?"selected":"")%> value="<%=GuiaEmissaoDt.VALOR_NAO%>">NĂO</option>
						</select>
						
					</div>
					<%} %>
                </fieldset>
                
                
                <%if( (Boolean)request.getAttribute("permiteAdicionarEscrivaoCrime") ) {%>
	                <fieldset id="VisualizaDados">
	                	<legend>
	                		Atos dos Escrivăes do Crime
	                	</legend>
	                	
	                	<div class="col35"><label class="formEdicaoLabel"> </label><br>
							<select id="atoEscrivao" name="atoEscrivao">
								
								<option <%=(GuiaEmissaoDt.getAtoEscrivao().equals("100")?"selected":"") %> value="100">Sim</option>
							</select>
						</div>
	                </fieldset>
                <%} %>
                
                <%if( !(Boolean)request.getAttribute("permiteAdicionarEscrivaoCrime") ) {%>
	                <fieldset id="VisualizaDados">
	                	<legend>
	                		Atos dos Escrivăes do Cível
	                	</legend>
	                	
	                	<div class="col35"><label class="formEdicaoLabel">Quantidade</label><br>
	                	
	                		<select id="porcentagemEscrivaesCivel" name="porcentagemEscrivaesCivel">
								<option <%=(GuiaEmissaoDt.getPorcentagemEscrivaesCivel().equals("0")?"selected":"") %> value="0">0%</option>
								<option <%=(GuiaEmissaoDt.getPorcentagemEscrivaesCivel().equals("30")?"selected":"") %> value="30">30%</option>
								<option <%=(GuiaEmissaoDt.getPorcentagemEscrivaesCivel().equals("40")?"selected":"") %> value="40">40%</option>
								<option <%=(GuiaEmissaoDt.getPorcentagemEscrivaesCivel().equals("70")?"selected":"") %> value="70">70%</option>
								<option <%=(GuiaEmissaoDt.getPorcentagemEscrivaesCivel().equals("100")?"selected":"") %> value="100">100%</option>
							</select>
	                	</div>
						
	                </fieldset>
	            <%} %>
                
                <fieldset id="VisualizaDados">
                	<legend>Distribuidor</legend>
                	
                	<div class="col35"><label class="formEdicaoLabel">Quantidade</label><br>
                	
                		<input type="text" name="distribuidorQuantidade" id="distribuidorQuantidade" value="<%=GuiaEmissaoDt.getDistribuidorQuantidade()%>" maxlength="3" size="5" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0');minimoUm(this, 'Distribuidor');" />
                	</div>
                </fieldset>
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Atos dos Contadores
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Contador</label><br>
                	
                		<input type="text" name="contadorQuantidade" id="contadorQuantidade" value="<%=GuiaEmissaoDt.getContadorQuantidade()%>" maxlength="3" size="5" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                	<div class="col30"><label class="formEdicaoLabel">Acréscimo Contador</label><br>
                	
                		<input type="text" name="contadorQuantidadeAcrescimo" id="contadorQuantidadeAcrescimo" value="<%=GuiaEmissaoDt.getContadorQuantidadeAcrescimo()%>" maxlength="3" size="5" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>
                
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Atos dos Avaliadores
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Quantidade</label><br>
                	
                		<input type="text" name="avaliadorQuantidade" id="avaliadorQuantidade" value="<%=GuiaEmissaoDt.getAvaliadorQuantidade()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>
                
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Atos dos Depositários
                	</legend>
                	
                	<div class="col50"><label class="formEdicaoLabel">Quantidade de Atos dos Depositários</label><br>
                	
                		<input type="text" name="depositarioPublico" id="depositarioPublico" value="<%=GuiaEmissaoDt.getDepositarioPublico()%>" maxlength="15" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>
	            
                <fieldset id="VisualizaDados">
                	<legend>
                		Atos dos Partidores
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Quantidade</label><br>
                	
                		<input type="text" name="partidorQuantidade" id="partidorQuantidade" value="<%=GuiaEmissaoDt.getPartidorQuantidade()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Atos dos Porteiros dos Auditórios
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Protocolo Quantidade (Reg. 15)</label><br>
                		
                		<input type="text" name="leilaoQuantidade" id="leilaoQuantidade" value="<%=GuiaEmissaoDt.getLeilaoQuantidade()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
<!--                 	<div class="col60"><label class="formEdicaoLabel">Quantidade de Documentos Publicados Diário da Justiça (Reg. 16.IV)</label><br> -->
                	
<%--                 		<input type="text" name="documentoDiarioJustica" id="documentoDiarioJustica" value="<%=GuiaEmissaoDt.getDocumentoDiarioJustica()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" /> --%>
<!--                 	</div> -->
                	
                </fieldset>
                
<!--                 <fieldset id="VisualizaDados"> -->
<!--                 	<legend>Despesas Postais</legend> -->
                	
<!--                 	<div class="col30"><label class="formEdicaoLabel">Quantidade</label><br> -->
                	
<%--                 		<input type="text" name="correioQuantidade" id="correioQuantidade" value="<%=GuiaEmissaoDt.getCorreioQuantidade()%>" maxlength="3" size="5" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" /> --%>
<!--                 	</div> -->
<!--                 </fieldset> -->
                
                <div id="accordion0" class='mensagens' >
                	
                	<%if( request.getAttribute("ProcessoSegundoGrau") != null && !(Boolean)request.getAttribute("ProcessoSegundoGrau") ) { %>
                	<h3><a href="#" >Taxas de Serviço - Primeiro Grau</a></h3>
					<div>
						 <%@ include file="Padroes/ItensRegimento16.jspf"%>
					</div>
					<%} %>
					
					<%if( request.getAttribute("ProcessoSegundoGrau") != null && (Boolean)request.getAttribute("ProcessoSegundoGrau") ) { %>
					<h3><a href="#" >Taxas de Serviço - Segundo Grau</a></h3>
					<div>
						 <%@ include file="Padroes/ItensRegimento4.jspf"%>
					</div>
					<%} %>
                	
                </div>
	            
				<fieldset id="VisualizaDados">
					<legend>
						Locomoçőes 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</legend>
					
					<div class="col30"><label class="formEdicaoLabel">Oficial de Justiça </label><br>
					
						<select id="codigoOficialSPGLocomocao" name="codigoOficialSPGLocomocao">
							<option value="" selected></option>
							<%
							List listaOficiaisSPGDt = (List)request.getSession().getAttribute("ListaOficiaisSPGDt");
							if( listaOficiaisSPGDt != null && listaOficiaisSPGDt.size() > 0 ) {
								for( int i = 0; i < listaOficiaisSPGDt.size(); i++ ) {
									OficialSPGDt oficialSPGDt = (OficialSPGDt)listaOficiaisSPGDt.get(i);
									%>
									<option <%=(GuiaEmissaoDt.getCodigoOficialSPGLocomocao().equals(oficialSPGDt.getCodigoOficial())?"selected":"") %> value="<%=oficialSPGDt.getCodigoOficial()%>"><%=oficialSPGDt.getNomeOficial()%></option>
									<%
								}
							}
							%>
						</select>
						<br>
					</div>
						
					<br>
					
					<table id="TabelaLocomocoes" class="Tabela">
	       				<thead>
	           				<tr class="TituloColuna">
	               				<td width="15%">Quantidade</td>
	                 			<td width="25%">Bairro</td>
	                 			<td width="20%">Cidade</td>
	                 			<td width="10%">UF</td>
	                 			<td width="25%">Oficial</td>
	                 			<td class="colunaMinima"></td>				                  			
	              			</tr>			               			
	          			</thead>
		   				<tbody id="tabListaLocomocoes">
							<%					
			            	List liTemp = (List)request.getSession().getAttribute("ListaBairroDt");
							List listaQuantidadeBairroDt = (List)request.getSession().getAttribute("ListaQuantidadeBairroDt");
		                	if( liTemp != null && liTemp.size() > 0 ) {
			                	for( int i = 0; i < liTemp.size(); i++ ) {
			                		BairroLocomocaoDt bairroAuxDt = (BairroLocomocaoDt)liTemp.get(i);
									%>
									<tr>
										<td width="15%" align="center">
											<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i %>" name="quantidadeLocomocao<%=i %>" value="<%=listaQuantidadeBairroDt.get(i)%>" size="1" />
											<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i %>);" />
											<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i %>,'1');" />
										</td>
										<td width="25%" align="center">
											<%= bairroAuxDt.getBairroDt().getBairro() %>
										</td>
										<td width="20%" align="center">
											<%= bairroAuxDt.getBairroDt().getCidade() %>
										</td>
										<td width="10%" align="center">
											<%= bairroAuxDt.getBairroDt().getUf() %>
										</td>
										<td width="25%" align="center">
											<%=(bairroAuxDt.getOficialSPGDt() != null?bairroAuxDt.getOficialSPGDt().getNomeOficial():"") %>
										</td>
										<td align="center">
											<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" title="Excluir esta Locomoçăo" />
										</td>
									</tr>							
									<%
								}
		                	}
		                	else {
		                		%>
		                		<tr>
		                			<td colspan="6">                			
		                				<em> Insira um Bairro para uma Locomoçăo. </em>
		                			</td>
		                		</tr>
		                		<%
		                	}
							%>
			    		</tbody>
					</table>
				</fieldset>
				
                <fieldset id="VisualizaDados">
                	<legend>
                		Acréscimo por pessoa
                	</legend>

                	<div class="col30"><label class="formEdicaoLabel">Quantidade</label><br>
                	
                		<input type="text" name="quantidadeAcrescimoPessoa" id="quantidadeAcrescimoPessoa" value="<%=GuiaEmissaoDt.getQuantidadeAcrescimo()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>
				
				<fieldset id="VisualizaDados">
					<legend>
						Locomoçőes Conta Vinculada
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairroContaVinculada" name="imaLocalizarBairroContaVinculada" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1)%>')" />
					</legend>
					
					<div class="col30"><label class="formEdicaoLabel">Oficial de Justiça </label><br>
					
						<select id="codigoOficialSPGLocomocaoContaVinculada" name="codigoOficialSPGLocomocaoContaVinculada">
							<option value="<%=OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA%>" selected><%=OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA%></option>
						</select>
						<br>
					</div>
					
					<br />
					
					
					<table id="TabelaLocomocoes" class="Tabela">
	       				<thead>
	           				<tr class="TituloColuna">
	               				<td width="15%">Quantidade</td>
	                 			<td width="25%">Bairro</td>
	                 			<td width="20%">Cidade</td>
	                 			<td width="10%">UF</td>
	                 			<td width="25%">Oficial</td>
	                 			<td class="colunaMinima"></td>				                  			
	              			</tr>			               			
	          			</thead>
		   				<tbody id="tabListaLocomocoes">
							<%					
							List listaBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							List listaQuantidadeBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
		                	if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
			                	for( int i = 0; i < listaBairroLocomocaoContaVinculada.size(); i++ ) {
			                		BairroLocomocaoDt bairroAuxDt = (BairroLocomocaoDt)listaBairroLocomocaoContaVinculada.get(i);			                		
									%>
									<tr>
										<td width="15%" align="center">
											<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocaoContaVinculada<%=i %>" name="quantidadeLocomocaoContaVinculada<%=i %>" value="<%=listaQuantidadeBairroLocomocaoContaVinculada.get(i)%>" size="1" />
											<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocaoContaVinculada<%=i %>);" />
											<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocaoContaVinculada<%=i %>,'1');" />
										</td>
										<td width="25%" align="center">
											<%= bairroAuxDt.getBairroDt().getBairro() %>
										</td>
										<td width="20%" align="center">
											<%= bairroAuxDt.getBairroDt().getCidade() %>
										</td>
										<td width="10%" align="center">
											<%= bairroAuxDt.getBairroDt().getUf() %>
										</td>
										<td width="25%" align="center">
											<%=(bairroAuxDt.getOficialSPGDt() != null?bairroAuxDt.getOficialSPGDt().getNomeOficial():"") %>
										</td>
										<td align="center">
											<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroContaVinculadaExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" title="Excluir esta Locomoçăo" />
										</td>
									</tr>							
									<%
								}
		                	}
		                	else {
		                		%>
		                		<tr>
		                			<td colspan="6">                			
		                				<em> Insira um Bairro para uma Locomoçăo de Conta Vinculada. </em>
		                			</td>
		                		</tr>
		                		<%
		                	}
							%>
			    		</tbody>
					</table>
				</fieldset>
                

				
			
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Reduçăo em 50% das custas, somente se determinado pelo juiz.
                	</legend>
                	
                	<div>Reduçăo</div>
                	<span>
                		<input type="checkbox" name="reducao50Porcento" id="reducao50Porcento" value="<%=GuiaCalculoNe.LEI_REDUCAO_50_PORCENTO %>" />
                	</span>
                	
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
			
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>