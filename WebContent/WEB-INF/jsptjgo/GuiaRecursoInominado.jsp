<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CustaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.OficialSPGDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaCalculoNe"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaRecursoInominadoNe"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Recurso Inominado</title>
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
		$(document).ready(
						
			function() {
				//pego todos objetos que foram marcados com a class nomes
				//e verifico se tem número no nome
				 $(".nomes").each(function( index ) {
				 	var texto =  $( this ).text();
					for(var numero=0; numero<=9; numero++){
						texto= texto.replace(numero,'<p class="destacarNumero" tag="Foi utilizado número no Nome, favor conferir com os dados da petição" title="Foi utilizado número no Nome, favor conferir com os dados da petição">'+ numero +'</p>');
					}
	
					$( this ).html(texto);			
				});		
			}
		); 	
	</script>
</head>
<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="formGuiaRecursoInominado" id="formGuiaRecursoInominado">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" id="posicaoListaCustaExcluir" name="posicaoListaCustaExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroContaVinculadaExcluir" name="posicaoListaBairroContaVinculadaExcluir" value="-1" />
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
		
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                	<div> Número </div>
					<span>
						<% if (!(request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica"))) { %>
							<a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2">
						<% } %>						
						 <%=processoDt.getProcessoNumero()%>						
						<% if (!(request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica"))) { %>
							</a>
						<% } %>
					</span/>
                </fieldset>
                

				
				<%@ include file="BuscaPartesProcesso.jspf"%>

                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Informações </legend>
				    
				    <%
				    				    	if(!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) {
				    				    %>
						<div> Serventia </div>
						<span class="span1"><%=processoDt.getServentia()%></span>
						<br />
					<%
						}
					%>
					
					<div> Classe </div>
				    <span style="width: 500px;"><%=processoDt.getProcessoTipo()%></span>
				    <br />
				   	
				    <%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
				   	
				    <%
				   					    	if (!processoDt.getValor().equals("Null")){
				   					    %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
						<br />
					<%
						}
					%>
					
					<%
											if (!processoDt.getValorCondenacao().equals("Null")){
										%>
						<div> Valor da Condenação</div>
						<span class="span1"><%=processoDt.getValorCondenacao()%></span>
						<br />
					<%
						}
					%>
					<div class="clear"></div>
				</fieldset>
				

				
				<fieldset id="VisualizaDados">
                	<legend>
                		Valor Atualizado
                	</legend>
                	
                	<div class="col45"> <label class="formEdicaoLabel">Valor da Causa</label> <br>
					
						<input class="formEdicaoInputSomenteLeitura" type="text" name="valorAcao" id="valorAcao" readonly value="<%=processoDt.getValor()%>" maxlength="15" />
					</div>
					
					<div class="col45"> <label class="formEdicaoLabel">Data Recebimento do Processo </label><br>
					
						<input type="text" class="formEdicaoInputSomenteLeitura" name="dataBaseAtualizacao" id="dataBaseAtualizacao" value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseAtualizacao())%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</div>
					
					<div class="clear"></div>
					
					<div class="col45"> <label class="formEdicaoLabel">Valor Base para Cálculo</label><br>
					
						<input type="text" class="formEdicaoInputSomenteLeitura" readonly name="novoValorAcao" id="novoValorAcao" value="<%=GuiaEmissaoDt.getNovoValorAcao()%>" maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" onBlur="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" />
					</div>
					
					<div class="col45"> <label class="formEdicaoLabel"> Data Base para Cálculo </label><br>
					
						<input type="text" class="formEdicaoInputSomenteLeitura" name="dataBaseFinalAtualizacao" id="dataBaseFinalAtualizacao" value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseFinalAtualizacao())%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</div>
					
					<div class="clear"></div>
					
					<div class="col45"> <label class="formEdicaoLabel">Valor da Causa Base Atualizado</label><br>
					
						<input class="formEdicaoInputSomenteLeitura" type="text" name="novoValorAcaoAtualizado" id="novoValorAcaoAtualizado" readonly value="<%=GuiaEmissaoDt.getValorCausaCoringa()%>" maxlength="15" />
					</div>
					
					<div class="clear"></div>
					
                </fieldset>
                

				<%if( request.getAttribute("GuiaRecursoInominadoPublica") == null ) {%>
					<fieldset id="VisualizaDados">
	                	<legend>
	                		Rateio
	                	</legend>
	                	
	                	<%
	                	                		listaPromoventes = processoDt.getListaPolosAtivos();
	                	                	                	                				listaPromovidos = processoDt.getListaPolosPassivos();
	                	                	                	                				
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
	                	
	                	<div class="col45"> <label class="formEdicaoLabel">Tipo de Rateio </label><br>
						
							<select id="rateioCodigo" name="rateioCodigo" onChange="mostrarDivRateioPartes('divRateioPartes',rateioCodigo.value,<%=stringRateioParteJavascript%>,<%=stringEmitirGuiaJavascript%>);">
								<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaRecursoInominadoNe.RATEIO_100_REQUERENTE))?"selected":"") %> value="<%=GuiaRecursoInominadoNe.RATEIO_100_REQUERENTE%>" >100% Requerente</option>
								<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaRecursoInominadoNe.RATEIO_100_REQUERIDO))?"selected":"") %> value="<%=GuiaRecursoInominadoNe.RATEIO_100_REQUERIDO%>">100% Requerido</option>
								<option <%=(GuiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaRecursoInominadoNe.RATEIO_50_50))?"selected":"") %> value="<%=GuiaRecursoInominadoNe.RATEIO_50_50%>">50% Para Cada Parte</option>
							</select>
						</div>
						
						<div class="clear"></div>
						
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
	            <%} else {%>
	            	<input type="hidden" name="rateioCodigo" id="rateioCodigo" value="<%=GuiaRecursoInominadoNe.RATEIO_100_REQUERENTE%>" />
	            <%} %>
				
				
				<fieldset id="VisualizaDados">
                	<legend>
                		Recorrente e Recorrido
                	</legend>
                	
                	<div class="col45"> <label class="formEdicaoLabel">Recorrente </label><br>
					
						<select id="id_apelante" name="id_apelante" onchange="alteradoApelante()">
							<option selected value=""></option>
							<%
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
					

					
					<div class="col45"> <label class="formEdicaoLabel"> Recorrido </label><br>
					
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
				

				<%if( request.getAttribute("GuiaRecursoInominadoPublica") == null ) {%>
					<fieldset id="VisualizaDados">
	                	<legend>
	                		Taxa Judiciária 
	                	</legend>
	                	
	                	<div class="col45"> <label class="formEdicaoLabel">Tipo de Cobrança </label><br>
						
							<select id="descontoTaxaJudiciaria" name="descontoTaxaJudiciaria">
								<option selected value="<%=GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0%>">Integral</option>
								<option <%=(GuiaEmissaoDt.getDescontoTaxaJudiciaria().equals(String.valueOf(GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_100))?"selected":"") %> value="<%=GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_100%>">Isento</option>
							</select>
						</div>
						
	                </fieldset>
	            <%} else {%>
	            	<input type="hidden" id="descontoTaxaJudiciaria" name="descontoTaxaJudiciaria" value="<%=GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0%>" />
				<%} %>

                
                <fieldset id="VisualizaDados">
                	<legend>
                		Itens de Custa
                	</legend>
                	
                	<div class="divTabela">
                	<table class="Tabela">
                	<thead>
                		<tr>
                			<th>Cód.Reg.</th>
                			<th>Descrição</th>
                			<th>Arrecadação</th>
                			<th>Ações</th>
                		</tr>
                		</thead>
                		<tbody>
                	
                	<%
         			List listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
         			if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
			           	for(int i = 0; i < listaCustaDt.size(); i++) {
		         			CustaDt custaDt = (CustaDt) listaCustaDt.get(i);
			           		%>
			           		<tr>
				           		<td>
				           			<%=custaDt.getCodigoRegimento() %>
				           		</td>
				           		<td>
									<%=custaDt.getCusta() %>
								</td>
				           		<td>
									<%=custaDt.getArrecadacaoCusta() %>
								</td>
								<td align="center">
									<%if( request.getAttribute("GuiaRecursoInominadoPublica") == null && custaDt.isCustaRecursoInominadoItem18ITabelaII() ) {%>
										<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaCustaExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6 %>');" title="Remover Item de Custa(Código de Regimento:<%=custaDt.getCodigoRegimento() %>)" />
									<%} %>
								</td>
							</tr>
	                	<%
			           	}
         			}
         			else {
         				%>
         				<em> Insira um Item de Custa. </em>
         				<%
         			}
         			%>
         			</tbody>
         			</table>
         			</div>
                </fieldset>
                
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Atos dos Porteiros dos Auditórios
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Protocolo Quantidade (Reg. 15)</label><br>
                		
                		<input type="text" name="leilaoQuantidade" id="leilaoQuantidade" value="<%=GuiaEmissaoDt.getLeilaoQuantidade()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>
                

                
                <fieldset id="VisualizaDados">
                	<legend>Despesas Postais</legend>
                	
                	<div class="col45"> <label class="formEdicaoLabel">Quantidade</label><br>
                	
                		<input type="text" name="correioQuantidade" id="correioQuantidade" value="<%=GuiaEmissaoDt.getCorreioQuantidade()%>" maxlength="3" size="5" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
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
						Locomoções 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</legend>
					
					<div><small><em><p id="mensagem_locomocao"></p></em></small></div>
						<div class="clear"></div>
						<div class="col30"><label class="formEdicaoLabel">Oficial de Justiça </label><br>
					
					
				
						<select id="codigoOficialSPGLocomocao" name="codigoOficialSPGLocomocao">
							<option value="<%=OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA%>" selected ><%=OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA%></option>
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
						<tr>
						<td>
							<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i %>" name="quantidadeLocomocao<%=i %>" value="<%=listaQuantidadeBairroDt.get(i)%>" size="1" />
							<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i %>);" />
							<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i %>,'1');" />
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
	       				</td></tr>
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
                		Acréscimo por pessoa
                	</legend>

                	<div>Quantidade</div>
                	<span>
                		<input type="text" name="quantidadeAcrescimoPessoa" id="quantidadeAcrescimoPessoa" value="<%=GuiaEmissaoDt.getQuantidadeAcrescimo()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</span>
                	
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
	       				<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroContaVinculadaExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" title="Excluir esta Locomoção" />
						</td></tr>
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
			
			<br/><br/>
		
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>