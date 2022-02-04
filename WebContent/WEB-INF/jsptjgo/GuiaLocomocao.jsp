<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.LocomocaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes" %>
<%@page import="br.gov.go.tj.projudi.dt.BairroGuiaLocomocaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaLocomocaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaLocomocaoNe"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe" %>
<%@page import="br.gov.go.tj.projudi.ct.GuiaLocomocaoCt" %>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formul�rio de Guia de Custas de Locomo��o</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
 	     #bkg_projudi { display: none;} 
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
</head>
<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formul�rio de Guia de Locomo��o</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaLocomocaoForm" id="GuiaLocomocaoForm">
			<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input type="hidden" id="PassoEditar" name="PassoEditar" value="<%=request.getAttribute("PassoEditar")%>" />
			
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" id="posicaoListaLocomocaoNaoUtilizadaExcluir" name="posicaoListaLocomocaoNaoUtilizadaExcluir" value="-1" />			
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
		
			<div id="divEditar" class="divEditar">
				
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                	<div> N&uacute;mero</div>
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
                                  
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Informa��es </legend>
				    
					<div> Serventia </div>
					<span class="span1"><%= processoDt.getServentia()%></span>
					<br />
					
					<div> Classe </div>
				    <span style="width: 500px;"><%= processoDt.getProcessoTipo()%></span>
				    <br />
				   	
				    <%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
				   	
				    <% if (!processoDt.getValor().equals("Null")){ %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
					<%} %>
				</fieldset>
				
				<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeLocomocoesNaoUtilizadas")))) { %>
	                <fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Locomo��es N�o Vinculadas a Dilig�ncias Dispon�veis para Serem Complementadas
							<input class="FormEdicaoimgLocalizarLocomocaoNaoUtilizada" id="imaLocalizarLocomocaoNaoUtilizada" name="imaLocalizarLocomocaoNaoUtilizada" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" />
						</legend>
						
						<table id="TabelaLocomocoesNaoUtilizadas" class="Tabela">
		       				<thead>
		           				<tr class="TituloColuna">
		           					<th>Id</th>
		           					<th>N� Guia</th>
		           					<th>Descri��o(C�d.Regimento)</th>
									<th>C�digo</th>
									<th>Qtde</th>
									<th>Valor Locomo��o</th>
									<th>Valor TJGO</th>									
		           					<td>Bairro</td>
		                 			<td>Cidade</td>
		                 			<td>UF</td>
		                 			<td>Zona</td>
		                 			<td class="colunaMinima"></td>				                  			
		              			</tr>			               			
		          			</thead>
			   				<tbody id="tabListaLocomocoesNaoUtilizadas">
								<%					
				            	List liTemp = (List)request.getSession().getAttribute("ListaLocomocaoNaoUtilizada");
								double valorTotalLocomocao = 0;
								double valorTotalTJGO = 0;
								if( liTemp != null && liTemp.size() > 0 ) {
				                	for( int i = 0; i < liTemp.size(); i++ ) {
				                		LocomocaoDt objTemp = (LocomocaoDt)liTemp.get(i);
				                		valorTotalLocomocao += objTemp.getValorCalculadoLocomocoes();
				                		valorTotalTJGO += objTemp.getValorCalculadoTJGO();
										%>
										<tr>
											<td align="center"><%= objTemp.getId() %></td>
											<td align="center">
												<%
												if( !objTemp.getGuiaItemDt().getGuiaEmissaoDt().getId_GuiaStatus().equals(GuiaStatusDt.CANCELADA) ) {
													%>
													<a href="GuiaEmissao?hash=<%=Funcoes.GeraHashMd5(objTemp.getGuiaItemDt().getGuiaEmissaoDt().getId() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=objTemp.getGuiaItemDt().getGuiaEmissaoDt().getId()%>&Id_GuiaTipo=<%=objTemp.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getId_GuiaTipo()%>&comandoOnClickBotaoVoltar=PaginaAtual=<%=Configuracao.Curinga8%>&tempRetorno=GuiaLocomocaoComplementar">
													<%
												}
												%>
													<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto())%>
												<%
												if( !objTemp.getGuiaItemDt().getGuiaEmissaoDt().getId_GuiaStatus().equals(GuiaStatusDt.CANCELADA) ) {
													%>
													</a>
													<%
												}
												%>
											</td>
											<td><%=objTemp.getGuiaItemDt().getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getGuiaItemDt().getCustaDt().getCodigoRegimentoTratamento() %></td>
											<td align="center"><%=objTemp.getGuiaItemDt().getCustaDt().getCodigoArrecadacao() %></td>
											<td align="center"><%=Funcoes.FormatarDecimal(objTemp.getQuantidadeDeLocomocoes()) %></td>
											<td align="right">R$ <%=Funcoes.FormatarDecimal(objTemp.getValorCalculadoLocomocoes()) %></td>
											<td align="right">R$ <%=Funcoes.FormatarDecimal(objTemp.getValorCalculadoTJGO()) %></td>										
											<td><%= objTemp.getBairroDt().getBairro() %></td>
											<td><%= objTemp.getBairroDt().getCidade() %></td>
											<td align="center"><%= objTemp.getBairroDt().getUf() %></td>
											<td><%= objTemp.getZonaDt().getZona() %></td>											
											<td align="center">
												<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');AlterarValue('posicaoListaLocomocaoNaoUtilizadaExcluir','<%=i%>')" title="Excluir esta Locomo��o n�o utilizada" />
											</td>
										</tr>							
										<%
									}
			                	}
			                	else {
			                		%>
			                		<tr>
			                			<td colspan="9">                			
			                				<em> <%=Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_LOCOMOCAO_COMPLEMENTAR_SEM_ITENS)%> </em>
			                			</td>
			                		</tr>
			                		<%
			                	}
								%>
				    		</tbody>
						</table>
						
						<% if( liTemp != null && liTemp.size() > 0 ) { %>
							<div style="width:280px;">Valor Total das Locomo��es (Oficial de Justi�a):</div>
		                	<span style="width:80px; text-align: right;">
		                		R$ <%=Funcoes.FormatarDecimal(valorTotalLocomocao) %>
		                	</span>
		                	<br />
		                	<div style="width:280px;">Valor Total TJGO (Conta Vinculada):</div>
		                	<span style="width:80px; text-align: right;">
		                		R$ <%=Funcoes.FormatarDecimal(valorTotalTJGO) %>
		                	</span>
	                	<% } %>
					</fieldset>                
                	
                <% } %>
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Acr�scimo por pessoa
                	</legend>

                	<div class="col35"><label class="formEdicaoLabel">Quantidade</label><br>
                	
                		<input type="text" name="quantidadeAcrescimoPessoa" id="quantidadeAcrescimoPessoa" value="<%=GuiaEmissaoDt.getQuantidadeAcrescimo()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" />
                	</div>
                	
                </fieldset>
				
				
				<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeLocomocoesNaoUtilizadas")))) { %>
					<fieldset id="VisualizaDados" class="VisualizaDados">
	                	<legend>
	                		Despesas Postais ?
	                	</legend>
	                	
	                	<div> Quantidade </div>
						<span>
							<input type="text" name="correioQuantidade" id="correioQuantidade" value="<%=GuiaEmissaoDt.getCorreioQuantidade()%>" maxlength="3" size="80" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
						</span>
	                </fieldset>
                <%} %>
                
                
                <fieldset id="VisualizaDados">
                	<legend>
                		Especifica��o de hor�rio
                	</legend>
                	
                	<div class="col35"><label class="formEdicaoLabel">Cita��o por hora certa?</label><br>
                	
                		<select id="citacaoHoraCerta" name="citacaoHoraCerta" onChange="citacaoHoraCertaAlterada()">
                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDt.isCitacaoHoraCerta()?"":"selected")%> > N�O </option>
                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDt.isCitacaoHoraCerta()?"selected":"")%> > SIM </option>
                		</select>
                	</div>
                	
                	<div class="col35"><label class="formEdicaoLabel">Fora do hor�rio normal/dia n�o �til?</label><br>
                	
                		<select id="foraHorarioNormal" name="foraHorarioNormal" onChange="foraHorarioNormalAlterada()">
                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDt.isForaHorarioNormal()?"":"selected")%> > N�O </option>
                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDt.isForaHorarioNormal()?"selected":"")%> > SIM </option>
                		</select>
                	</div>
                	
                </fieldset>
                
             
	            
	            <fieldset id="VisualizaDados">
                	<legend>
                		Finalidade
                	</legend>
                	
                	<div class="col35"><label class="formEdicaoLabel">Escolha a Finalidade</label><br>
                	
                		<select id="finalidade" name="finalidade" onChange="finalidadeGuiaLocomocaoAlterada()">
                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO))?"selected":"")%>>Locomo��o</option>
                			<option value="<%=GuiaLocomocaoNe.PENHORA_AVALIACAO_ALIENACAO%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.PENHORA_AVALIACAO_ALIENACAO))?"selected":"")%>>Penhora, avalia��o e aliena��o</option>
                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO))?"selected":"")%>>Cita��o, penhora, avalia��o e aliena��o</option>
                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO))?"selected":"")%>>Cita��o, penhora e pra�a/leil�o</option>
                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO))?"selected":"")%>>Cita��o, penhora, avalia��o e pra�a/leil�o</option>
                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO))?"selected":"")%>>Locomo��o para avalia��o</option>
                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA%>" <%=(GuiaEmissaoDt.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA))?"selected":"")%>>Locomo��o para avalia��o e Pra�a</option>
                		</select>
                	</div>
                	
                	<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeOficialCompanheiro")))) { %>
	                	<div class="col35"><label class="formEdicaoLabel">Oficial Companheiro?</label><br>
	                	
	                		<select id="oficialCompanheiro" name="oficialCompanheiro" onChange="oficialCompanheiroAlterado()">
	                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDt.isOficialCompanheiro()?"":"selected")%> >N�O</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDt.isOficialCompanheiro()?"selected":"")%> >SIM</option>
	               			</select>
	                	</div>
                	<% } %>
                	
                	<div class="clear"></div>
                	
                	<div class="col35"><label class="formEdicaoLabel">Penhora?</label><br>
                	
                		<select id="penhora" name="penhora" onChange="penhoraAlterada()">
                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDt.isPenhora()?"":"selected")%> >N�O</option>
                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDt.isPenhora()?"selected":"")%> >SIM</option>
               			</select>
                	</div>
                	
                	<div class="col35"><label class="formEdicaoLabel">Intima��o?</label><br>
                	
                		<select id="intimacao" name="intimacao">
                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDt.isIntimacao()?"":"selected")%> >N�O</option>
                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDt.isIntimacao()?"selected":"")%> >SIM</option>
               			</select>
                	</div>
                </fieldset>
                
                
                
	            <fieldset id="VisualizaDadosLocomocoesAdicionais" class="VisualizaDados">
					<legend>
						Listagem de Locomo��es Adicionadas
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>');" />
					</legend>
					
					<%if( UsuarioSessao != null && UsuarioSessao.isCoodernadorCentralMandado() ) {%>
			            <div class="col35">
			            	<br />
			            	<label class="formEdicaoLabel">
			            		Vincular a Locomo��o a um Oficial e Mandado
			            		<input class="FormEdicaoimgLocalizar" id="imaLocalizarMandado" name="imaLocalizarMandado" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('VisualizaDadosLocomocoesAdicionais','GuiaLocomocao','Consulta de Mandados', 'Escolha o mandado a ser vinculado', 'idMandado', 'Mandado', ['N�mero do Mandado', 'Nome do Oficial'], [], '<%=(MandadoJudicialDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
			            		<input class="formEdicaoInputSomenteLeitura" type="text" name="Mandado" id="Mandado" value="" size="60" readonly="true" />
			            	</label>
			            	<input type="hidden" id="idMandado" name="idMandado" value="<%=request.getAttribute("idMandado")%>" />
			            	<br />
							<br />
							<br />
							<br />
		    	        </div>
		    	    <%} %>
					
					<table id="TabelaLocomocoes" class="Tabela">
	       				<thead>
	           				<tr class="TituloColuna">
	               				<td width="15%">Quantidade</td>
	                 			<td width="20%">Bairro</td>
	                 			<td width="15%">Cidade</td>
	                 			<td width="5%">UF</td>
	                 			<td width="15%">Finalidade</td>
	                 			<td width="20%">Oficial</td>	 	                 			
	                 			<td class="colunaMinima"></td>				                  			
	              			</tr>			               			
	          			</thead>
		   				<tbody id="tabListaLocomocoes">
							<%					
							List liTemp = (List)request.getSession().getAttribute("ListaBairroLocomocao");
							List listaQuantidadeBairroDt = (List)request.getSession().getAttribute("ListaBairroLocomocaoQtde");
		                	if( liTemp != null && liTemp.size() > 0 && listaQuantidadeBairroDt != null && listaQuantidadeBairroDt.size() == liTemp.size()) {
			                	for( int i = 0; i < liTemp.size(); i++ ) {
			                		BairroGuiaLocomocaoDt bairroAuxDt = (BairroGuiaLocomocaoDt)liTemp.get(i);
									%>
									<tr>
										<td width="15%" align="center">
											<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i %>" name="quantidadeLocomocao<%=i %>" value="<%=listaQuantidadeBairroDt.get(i)%>" size="1" />
											<input type="button" id="somarQuantidade<%=i%>" name="somarQuantidade<%=i%>" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i %>);" />
											<input type="button" id="subtrairQuantidade<%=i%>" name="subtrairQuantidade<%=i%>" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i %>,'1');" />
										</td>
										<td width="20%" align="left">
											<%= bairroAuxDt.getBairroDt().getBairro() %>
										</td>
										<td width="15%" align="left">
											<%= bairroAuxDt.getBairroDt().getCidade() %>
										</td>
										<td width="5%" align="left">
											<%= bairroAuxDt.getBairroDt().getUf() %>
										</td>
										<td width="20%" align="left">
											<%= GuiaEmissaoNe.getTextoFinalidade(Funcoes.StringToInt(bairroAuxDt.getFinalidade())) %> (<%= (bairroAuxDt.isOficialCompanheiro() ? "Sim" : "N�o") %>|<%= (bairroAuxDt.isPenhora() ? "Sim" : "N�o") %>|<%= (bairroAuxDt.isIntimacao() ? "Sim" : "N�o") %>)
										</td>
										<td align="left">
											<%if( bairroAuxDt.getOficialSPGDt_Principal() != null ) {%>
												<%=bairroAuxDt.getOficialSPGDt_Principal().getNomeOficial()%>
											<%}%>
										</td>
										<td align="center">
											<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>')" title="Excluir esta Locomo��o" />
										</td>
									</tr>							
									<%
								}
		                	}
		                	else {
		                		%>
		                		<tr>
		                			<td colspan="5">                			
		                				<em> Insira uma Locomo��o para um Bairro. </em>
		                			</td>
		                		</tr>
		                		<%
		                	}
							%>
			    		</tbody>
					</table>					
				</fieldset>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgPreviaCalculo" value="Pr�via do C�lculo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" >
                    	
                    	Pr�via do C�lculo
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	
                    	Limpar
                    </button>
                </div>
                
			</div>
			
			
			<%@ include file="Padroes/reCaptcha.jspf" %>
	  	</form>   
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			finalidadeGuiaLocomocaoAlterada();
			$('#penhora option:eq(0)').prop('selected', true);
			$('#intimacao option:eq(0)').prop('selected', true);
			<%if (GuiaEmissaoDt.isPenhora()) { %>
				$('#penhora option:eq(1)').prop('selected', true);
				<%if (GuiaEmissaoDt.isIntimacao()) { %>
					$('#intimacao option:eq(1)').prop('selected', true);
				<%} %>
			<%} %>			
			penhoraAlterada();
		});	
	</script>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>