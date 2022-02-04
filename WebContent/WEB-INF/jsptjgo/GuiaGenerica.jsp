<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Recolhimento Simplificada</title>
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
		function alteracaoMenuTipoGuia(idGuiaTipo) {
			var habilita = true;
			
			if( idGuiaTipo == <%=GuiaTipoDt.ID_CARTA_SENTENCA%> ) {
				habilita = false;
			}


			if( habilita == true ) {
				habilitaItensGuiaGenerica();
			}
			else {
				desabilitaItensGuiaGenerica();
			}
		}
	
		function desabilitaItensGuiaGenerica() {
			//Seta os dois primeiros
			document.getElementById("idArrecadacaoCusta_0").value = "<%=ArrecadacaoCustaDt.CUSTAS_GRS %>";
			document.getElementById("quantidadeArrecadacaoCusta_0").value = "1";
			document.getElementById("quantidadeArrecadacaoCusta_0").readOnly = true;
			document.getElementById("quantidadeArrecadacaoCusta_0").className 	= "formEdicaoInputSomenteLeitura";
			document.getElementById("idArrecadacaoCusta_0").style.display = 'none';
			document.getElementById("idSpan_0").textContent = "5010";
			document.getElementById("idSpan_0").style.display = 'block';
			document.getElementById("valorArrecadacaoCusta_0").value = "";
			
			document.getElementById("idArrecadacaoCusta_1").value = "<%=ArrecadacaoCustaDt.TAXA_JUDICIARIA_GRS %>";
			document.getElementById("quantidadeArrecadacaoCusta_1").value = "1";
			document.getElementById("quantidadeArrecadacaoCusta_1").readOnly = true;
			document.getElementById("quantidadeArrecadacaoCusta_1").className 	= "formEdicaoInputSomenteLeitura";
			document.getElementById("idArrecadacaoCusta_1").style.display = 'none';
			document.getElementById("idSpan_1").textContent = "5029";
			document.getElementById("idSpan_1").style.display = 'block';
			document.getElementById("valorArrecadacaoCusta_1").value = "";
			
			//Desabilita os outros
			for( var i = 2; i < <%=GuiaGenericaNe.QUANTIDADE_ITENS_CUSTA_GENERICO%>; i++ ) {
				document.getElementById("idArrecadacaoCusta_" + i).disabled 			= true;
				document.getElementById("quantidadeArrecadacaoCusta_" + i).disabled 	= true;
				document.getElementById("valorArrecadacaoCusta_" + i).disabled 			= true;

				document.getElementById("quantidadeArrecadacaoCusta_" + i).value = "";
				document.getElementById("valorArrecadacaoCusta_" + i).value = "";
				
				document.getElementById("idArrecadacaoCusta_" + i).className 			= "formEdicaoInputSomenteLeitura";
				document.getElementById("quantidadeArrecadacaoCusta_" + i).className 	= "formEdicaoInputSomenteLeitura";
				document.getElementById("valorArrecadacaoCusta_" + i).className 		= "formEdicaoInputSomenteLeitura";
			}
		}

		function habilitaItensGuiaGenerica() {
			
			document.getElementById("quantidadeArrecadacaoCusta_0").readOnly = false;
			document.getElementById("quantidadeArrecadacaoCusta_1").readOnly = false;
			document.getElementById("idArrecadacaoCusta_0").style.display = 'block';
			document.getElementById("idArrecadacaoCusta_1").style.display = 'block';
			
			for( var i = 0; i < <%=GuiaGenericaNe.QUANTIDADE_ITENS_CUSTA_GENERICO%>; i++ ) {
				document.getElementById("idArrecadacaoCusta_" + i).disabled 			= false;
				document.getElementById("quantidadeArrecadacaoCusta_" + i).disabled 	= false;
				document.getElementById("valorArrecadacaoCusta_" + i).disabled 			= false;
				document.getElementById("idSpan_" + i).style.display = 'none';

				document.getElementById("idArrecadacaoCusta_" + i).value = "";
				document.getElementById("quantidadeArrecadacaoCusta_" + i).value = "1";
				document.getElementById("valorArrecadacaoCusta_" + i).value = "";

				document.getElementById("idArrecadacaoCusta_" + i).className 			= "";
				document.getElementById("quantidadeArrecadacaoCusta_" + i).className 	= "";
				document.getElementById("valorArrecadacaoCusta_" + i).className 		= "";
			}
		}
	</script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="formGuiaGenerica" id="formGuiaGenerica">
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
				    <legend> Informações </legend>
				    
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
					
					<div class="col30"><label class="formEdicaoLabel">Data Recebimento do Processo</label><br>
					
						<input type="text" class="formEdicaoInputSomenteLeitura" name="dataBaseAtualizacao" id="dataBaseAtualizacao" value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseAtualizacao())%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</div>
					
					
					
					<div class="col30"><label class="formEdicaoLabel">Valor Base para Cálculo</label><br>
					
						<input type="text" name="novoValorAcao" id="novoValorAcao" value="<%=GuiaEmissaoDt.getNovoValorAcao()%>" maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" onBlur="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();"/>
					</div>
					
					<div class="clear"></div>
					
					<div class="col30"><label class="formEdicaoLabel"> Data Base para Cálculo </label><br>
					
						<input type="text" name="dataBaseFinalAtualizacao" id="dataBaseFinalAtualizacao" value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseFinalAtualizacao())%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" />
						<img id="calendarioDataBaseFinalAtualizacao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataBaseFinalAtualizacao,'dd/mm/yyyy',this)"/>
					</div>
					
					
					
					<div class="col30"><label class="formEdicaoLabel">Valor da Causa Base Atualizado</label><br>
					
						<input class="formEdicaoInputSomenteLeitura" type="text" name="novoValorAcaoAtualizado" id="novoValorAcaoAtualizado" readonly value="<%=GuiaEmissaoDt.getNovoValorAcaoAtualizado()%>" maxlength="15" />
					</div>
					
                </fieldset>
                
                
				
				<fieldset id="VisualizaDados">
                	<legend>
                		Recorrente e Recorrido
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Recorrente </label><br>
					
						<select id="id_apelante" name="id_apelante" onchange="alteradoApelante()">
							<option selected value=""></option>
							<%
								List listaPromoventes = processoDt.getListaPolosAtivos();
																	List listaPromovidos = processoDt.getListaPolosPassivos();
																	
																	if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
													   	    			for (int i=0;i < listaPromoventes.size();i++) {
														   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
							%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%></option>
								<%}
							}%>
							<%
							if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
			   	    			for (int i=0;i < listaPromovidos.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%></option>
								<%}
							}%>
							<%
							List listaPartesLitisconsorte = (List)request.getSession().getAttribute("ListaPartesLitisconsorte");
							if( listaPartesLitisconsorte != null && listaPartesLitisconsorte.size() > 0 ) {
								for (int i=0;i < listaPartesLitisconsorte.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPartesLitisconsorte.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%></option>
								<%}
							}
							%>
						</select>
					</div>
					
					
					
					<div class="col30"><label class="formEdicaoLabel"> Recorrido </label><br>
					
						<select id="id_apelado" name="id_apelado" onchange="alteradoApelado()">
							<option selected value=""></option>
							<%
							if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
			   	    			for (int i=0;i < listaPromoventes.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelado())?"selected":"")%>><%=parteDt.getNome()%></option>
								<%}
							}%>
							<%
							if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
			   	    			for (int i=0;i < listaPromovidos.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelado())?"selected":"")%>><%=parteDt.getNome()%></option>
								<%}
							}%>
							<%
							if( listaPartesLitisconsorte != null && listaPartesLitisconsorte.size() > 0 ) {
			   	    			for (int i=0;i < listaPartesLitisconsorte.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPartesLitisconsorte.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelado())?"selected":"")%>><%=parteDt.getNome()%></option>
								<%}
							}%>
						</select>
					</div>
                </fieldset>
				
				
				
				<fieldset id="VisualizaDados">
                	<legend>
                		Rateio
                	</legend>
                	
                	<%
					
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
                	
                	<div class="col30"><label class="formEdicaoLabel">Tipo de Rateio</label><br>
					
						<select id="rateioCodigo" name="rateioCodigo" onChange="mostrarDivRateioPartes('divRateioPartes',rateioCodigo.value,<%=stringRateioParteJavascript%>,<%=stringEmitirGuiaJavascript%>);">
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_100_REQUERENTE))?"selected":"") %> value="<%=GuiaFinalZeroNe.RATEIO_100_REQUERENTE%>" >100% Requerente</option>
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_100_REQUERIDO))?"selected":"") %> value="<%=GuiaFinalZeroNe.RATEIO_100_REQUERIDO%>">100% Requerido</option>
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_50_50))?"selected":"") %> value="<%=GuiaFinalZeroNe.RATEIO_50_50%>">50% Para Cada Parte</option>
							<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_VARIAVEL))?"selected":"") %> value="<%=GuiaFinalZeroNe.RATEIO_VARIAVEL%>">Rateio Variável</option>
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
									<input type="text" id="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" name="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" value="<%=variavelRateioParteVariavel%>" maxlength="5" onkeyup="MascaraValor(this);autoTab(this,20);somarRateioPartesVariavel(this,<%=stringRateioParteJavascript%>);" onkeypress="return DigitarSoNumero(this, event)" />
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
									<input type="text" id="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" name="<%=GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL%><%=parteDt.getId()%>" value="<%=variavelRateioParteVariavel%>" maxlength="5" onkeyup="MascaraValor(this);autoTab(this,20);somarRateioPartesVariavel(this,<%=stringRateioParteJavascript%>);" onkeypress="return DigitarSoNumero(this, event)" />
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
                	<legend>
                		Tipo de Guia
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel"> Tipo de Guia </label><br>
					
						<select id="idTipoGuia" name="idTipoGuia" onChange="alteracaoMenuTipoGuia(this.value)" >
							<option value="">Selecione</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_LOCOMOCAO %>">Locomoção</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_CARTA_SENTENCA %>">Carta de Sentença</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_CARTA_PRECATORIA %>">Carta Precatória</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU %>">Complementar 1º Grau</option>
							<option value="<%=GuiaTipoDt.ID_FAZENDA_MUNICIPAL %>">Fazenda Municipal</option>
							<option value="<%=GuiaTipoDt.ID_FINAL %>">Final</option>
							<option value="<%=GuiaTipoDt.ID_FINAL_ZERO %>">Final Zero</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_CARTA_SENTENCA %>">Carta de Sentença</option>
							<option value="<%=GuiaTipoDt.ID_RECURSO_INOMINADO %>">Recurso Inominado</option>
							<option value="<%=GuiaTipoDt.ID_RECURSO_INOMINADO_QUEIXA_CRIME %>">Recurso Inominado Queixa-Crime</option>
							<option value="<%=GuiaTipoDt.ID_FINAL_EXECUCAO_SENTENCA %>">Final (Exec. Sentença)</option>
							<option value="<%=GuiaTipoDt.ID_FINAL_EXECUCAO_QUEIXA_CRIME %>">Final (Exec. Sentença Queixa-Crime)</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_AGRAVO_REGIMENTAL %>">Agravo Regimental</option>
							<option value="<%=GuiaTipoDt.ID_RECURSO_STJ %>">Recurso STJ</option>
							<option value=""></option>
							<option value="<%=GuiaTipoDt.ID_GUIA_GENERICA %>">Guia de Recolhimento Simplificada</option>
						</select>
					</div>
					
                </fieldset>
                
                
				
                <fieldset class="formEdicao">
                	<legend class="formEdicaoLegenda">
                		Itens de Custa
                	</legend>
                	<%
                	List listaArrecadacaoCustaDt = (List)request.getSession().getAttribute("ListaArrecadacaoCustaDt");
                	%>
                	<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr>
								<th class="colunaMinima">Código Arrecadação</th>
								<th align="center">Quantidade</th>
								<th>Valor</th>
							</tr>
			           	</thead>
         				<tbody id="tabListaCustas" align="center">
         					<%
         					if( listaArrecadacaoCustaDt != null && listaArrecadacaoCustaDt.size() > 0 ) {
	         					for(int i = 0; i < GuiaGenericaNe.QUANTIDADE_ITENS_CUSTA_GENERICO; i++ ) { %>
				                	<tr>
			               				<td align="center" width="30px">
			               					<select id="idArrecadacaoCusta_<%=i%>" name="idArrecadacaoCusta_<%=i%>">
			               						<option value="">Selecione</option>
			               						<option value=""></option>
				               					<%for(int m = 0; m < listaArrecadacaoCustaDt.size(); m++) {
				               						ArrecadacaoCustaDt arrecadacaoCustaDt = (ArrecadacaoCustaDt) listaArrecadacaoCustaDt.get(m);
				               					%>
				               						<option value="<%=arrecadacaoCustaDt.getId()%>"><%=arrecadacaoCustaDt.getCodigoArrecadacao()%></option>
				               					<%} %>
			               					</select>
			               					<span id="idSpan_<%=i%>" style="display: block"></span>
				        				</td>
				        				
				        				<td align="center" width="30px">
				        					
				        						<input type="text" id="quantidadeArrecadacaoCusta_<%=i%>" name="quantidadeArrecadacaoCusta_<%=i%>" value="1" maxlength="3" size="5" onkeypress="return DigitarSoNumero(this, event)" />
				        					
				        				</td>
				        				
				        				<td align="center" width="35px">
			               					<input type="text" id="valorArrecadacaoCusta_<%=i%>" name="valorArrecadacaoCusta_<%=i%>" value="" maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" />
				        				</td>
				        			</tr>
			        			<%}
		        			}%>
	                	</tbody>
	                </table>
	                
                </fieldset>
				
				
				
				<fieldset id="VisualizaDados">
					<legend>
						Locomoções 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</legend>
					
					<div><small><em><p id="mensagem_locomocao"></p></em></small></div>
						<div class="clear"></div>
					
					<div class="col30"><label class="formEdicaoLabel"> Oficial de Justiça </label><br>
					
						<select id="codigoOficialSPGLocomocao" name="codigoOficialSPGLocomocao">
							<option value="<%=OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA%>" selected ><%=OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA%></option>
							<option value=""></option>
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
						<div style="margin:0 0 0 15px">&nbsp;</div>
					</div>
						
					
					
					<%
	            	List liTemp = (List)request.getSession().getAttribute("ListaBairroDt");
					List listaQuantidadeBairroDt = (List)request.getSession().getAttribute("ListaQuantidadeBairroDt");
                	if( liTemp != null && liTemp.size() > 0 ) {
                		%>
						<div class="divTabela">
                				<table class="Tabela">
                					<thead>
                						<tr>
                							<th width="20%">Quantidade</th>
                							<th width="35%">Bairro</th>
                							<th width="35%">Cidade</th>
                							<th width="5%">UF</th>
                							<th width="5%">Ações</th>
                						</tr>
									</thead>
									<tbody>
						<%
	                	for( int i = 0; i < liTemp.size(); i++ ) {
						BairroDt bairroAuxDt = (BairroDt)liTemp.get(i);
						%>
						<tr><td>
							<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i%>" name="quantidadeLocomocao<%=i %>" value="<%=listaQuantidadeBairroDt.get(i)%>" size="1" />
							<input type="button" id="mais<%=i%>" name="mais<%=i%>" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i %>);" />
							<input type="button" id="menos<%=i%>" name="menos<%=i%>" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i %>,'1');" />
						</td>
						
						<td>
	       					<%= bairroAuxDt.getBairro() %>
	       				</td>
	       				
	       				<td>
	       					<%= bairroAuxDt.getCidade() %>
	       				</td>
	       				
	       				<td align="center">
	       					<%= bairroAuxDt.getUf() %>
	       				</td>
	       				
	       				<td align="center">
	       				<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" title="Excluir esta Locomoção" />
	       				</td>
	       				</tr>
	       				
						<%
						}
						%>
						</tbody>
	       					</table>
	       				</div>
	       				<%
                	}
                	else {
                		%>
                		<script type='text/javascript'>document.getElementById("mensagem_locomocao").innerHTML="Insira um Bairro para uma Locomoção.";</script>
                		<%
                	}
					%>
				</fieldset>
				
				
                
				<fieldset id="VisualizaDados">
					<legend>
						Locomoções Conta Vinculada
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairroContaVinculada" name="imaLocalizarBairroContaVinculada" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1)%>')" />
					</legend>
					
					<div><small><em><p id="mensagem_locomocao_vinculada"></p></em></small></div>
						<div class="clear"></div>
						
						<div class="col30"><label class="formEdicaoLabel"> Oficial de Justiça </label><br>
					
						<select id="codigoOficialSPGLocomocaoContaVinculada" name="codigoOficialSPGLocomocaoContaVinculada">
							<option value="<%=OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA%>" selected ><%=OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA%></option>
						</select>
						<div style="margin:0 0 0 15px">&nbsp;</div>
					</div>
					
					
					
					<%
					List listaBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
					List listaQuantidadeBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
                	if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
                		%>
						<div class="divTabela">
                				<table class="Tabela">
                					<thead>
                						<tr>
                							<th width="20%">Quantidade</th>
                							<th width="35%">Bairro</th>
                							<th width="35%">Cidade</th>
                							<th width="5%">UF</th>
                							<th width="5%">Ações</th>
                						</tr>
									</thead>
									<tbody>
						<%
	                	for( int i = 0; i < listaBairroLocomocaoContaVinculada.size(); i++ ) {
						BairroDt bairroAuxDt = (BairroDt)listaBairroLocomocaoContaVinculada.get(i);
						%>
						<tr>
						  <td>
							<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocaoContaVinculada<%=i %>" name="quantidadeLocomocaoContaVinculada<%=i %>" value="<%=listaQuantidadeBairroLocomocaoContaVinculada.get(i)%>" size="1" />
							<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocaoContaVinculada<%=i %>);" />
							<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocaoContaVinculada<%=i %>,'1');" />
						  </td>
						  <td><%= bairroAuxDt.getBairro() %></td>
	       				  <td><%= bairroAuxDt.getCidade() %></td>
	       				  <td align="center"><%= bairroAuxDt.getUf() %></td>
	       				  <td align="center"><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroContaVinculadaExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" title="Excluir esta Locomoção" /></td>
	       				<tr>
						<%
						}
						%>
						</tbody>
	       					</table>
	       				</div>
	       				<%
                	}
                	else {
                		%>
                		<script type='text/javascript'>document.getElementById("mensagem_locomocao_vinculada").innerHTML="Insira um Bairro para uma Locomoção de Conta Vinculada.";</script>
                		<%
                	}
					%>
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