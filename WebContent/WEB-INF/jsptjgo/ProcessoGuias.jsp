<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LocomocaoDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe" %>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt" />
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>Guias do Processo <%=processoDt.getProcessoNumero()%></title>
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
</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Guias do Processo </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="ProcessoGuias" id="ProcessoGuias" />
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
			<input id="GuiaCancelar" name="GuiaCancelar" type="hidden" value="<%=request.getAttribute("GuiaCancelar")%>" />
			<input id="DesfazerGuiaCancelar" name="DesfazerGuiaCancelar" type="hidden" value="<%=request.getAttribute("DesfazerGuiaCancelar")%>" />
			<input id="GuiaAlterarStatus" name="GuiaAlterarStatus" type="hidden" value="<%=request.getAttribute("GuiaAlterarStatus")%>" />
			<input id="TipoGuiaReferenciaDescontoParcelamento" name="TipoGuiaReferenciaDescontoParcelamento" type="hidden" value="<%=request.getAttribute("TipoGuiaReferenciaDescontoParcelamento")%>" />
			<input id="Id_GuiaEmissaoReferencia" name="Id_GuiaEmissaoReferencia" type="hidden" value="<%=request.getAttribute("Id_GuiaEmissaoReferencia")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
                	<legend class="formEdicaoLegenda">Guias do Processo</legend>
                	
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Processo</legend>
                	
                		<div> Número </div>
						<span><a id="numeroProcesso" href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
                	</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Guias</legend>
                		
						<table id="Tabela" class="Tabela">
							<thead>
								<tr>
									<th>Nº</th>
									<th>Número Guia</th>
									<th>Tipo Guia</th>
									<th>Data Emissão</th>
									<th>Data Vencimento</th>
									<th>Data Recebimento</th>
									<th>Data Cancelamento</th>
									<th>Situação</th>
									<th>Natureza</th>
									<th>Guia Parcelada/Desconto</th>
									<%if( request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
										<th>Opções</th>
									<%} %>
								</tr>
							</thead>
							<tbody id="tabListaEscala">
							<%
							List liTemp = (List)request.getAttribute("ListaGuiaEmissao");
							GuiaEmissaoDt objTemp = null;
							if( liTemp != null && liTemp.size() > 0 ) {
								for(int i = 0 ; i< liTemp.size();i++) {
									objTemp = (GuiaEmissaoDt)liTemp.get(i);
									if( objTemp != null ) { %>
									<tr>
										<td align="center"><%=(i + 1)%></td>
										
										<td align="center">
											<label id="labelNumeroGuiaCompleto<%=(i + 1)%>">
												<%
												if( objTemp.getId_GuiaStatus() != null && !objTemp.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
													%>
													<a href="<%=request.getAttribute("tempRetorno")%>?hash=<%=Funcoes.GeraHashMd5(objTemp.getId() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=objTemp.getId()%>&NumeroGuia=<%=objTemp.getNumeroGuiaCompleto()%>&Id_GuiaTipo=<%=objTemp.getGuiaModeloDt().getId_GuiaTipo()%>&EhGuiaSPG_SSG=<%=(objTemp.isGuiaEmitidaSPG() || objTemp.isGuiaEmitidaSSG()?"S":"N")%>">
													<%
												}
												%>
													<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>
												<%
												if( objTemp.getId_GuiaStatus() != null && !objTemp.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
													%>
													</a>
													<%
												}
												%>
											</label>
										</td>
										
										<td>
											<%if( objTemp.getGuiaTipo() != null && objTemp.getGuiaTipo().length() > 0 ) { %>
												<%=objTemp.getGuiaTipo() %>
											<%}
											else { %>
												<%=objTemp.getGuiaModeloDt().getGuiaTipo() %>
											<%} %>
										</td>
										
										<td align="center">
											<%=Funcoes.FormatarDataHora(objTemp.getDataEmissao())%>
										</td>
										
										<td align="center">
											<%=Funcoes.TelaData(objTemp.getDataVencimento()) %>
										</td>
										
										<td align="center">
											<%if( objTemp.getId_GuiaStatus() != null && objTemp.getId_GuiaStatus().length() > 0 && Integer.parseInt(objTemp.getId_GuiaStatus()) == GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA ) {
												%><%=objTemp.getGuiaStatus() %><%
											}
											else {%>
												<%=Funcoes.TelaData(objTemp.getDataRecebimento()) %>
											<%} %>
										</td>
										
										<td align="center">
											<%=Funcoes.FormatarDataHora(objTemp.getDataCancelamento()) %>
										</td>
										
										<td align="center">
											<%if(objTemp.getGuiaStatus() != null && objTemp.getGuiaStatus().trim().length() > 0) { %>
												<%=objTemp.getGuiaStatus() %>
												<%
												if( objTemp.isGuiaPaga() && objTemp.getPercRepasse() != null && !objTemp.getPercRepasse().isEmpty() && (Boolean)request.getAttribute("apresentarPercentualRepasse") ) {
													%>
													<br />
													(Percentual Repasse:<%=objTemp.getPercRepasse()%>%)
													<%
												}
												%>
											<%} else { %>
												CONSULTAR SPG*
											<%} %>
										</td>
										
										<td align="center">
											<%if(objTemp.getNaturezaSPG() != null && objTemp.getNaturezaSPG().trim().length() > 0) { %>
												<%=objTemp.getNaturezaSPG() %>											
											<%} %>
										</td>
										
										<td align="center">
											<%
											if( objTemp.getTipoGuiaReferenciaDescontoParcelamento() != null && !objTemp.getTipoGuiaReferenciaDescontoParcelamento().isEmpty() ) {
												if( objTemp.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_PARCELADA) ) {
													%>(Guia Parcelada: Parcela <%=objTemp.getParcelaAtual()%> de <%=objTemp.getQuantidadeParcelas()%>)<%
												}
												else {
													if( objTemp.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO) ) {
														%>(Guia Descontada: <%=objTemp.getPorcentagemDesconto()%>% de Desconto)<%
													}
												}
												%><br /><%
											}
											else {
												%><%
											}
											%>
										</td>
										
										<td>
											<%if((!objTemp.isGuiaEmitidaSPG() && !objTemp.isGuiaEmitidaSSG()) && request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
												<%if( objTemp.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) || objTemp.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)) ) { %>
													<%if( !objTemp.isGuiaEnviadaCadin() ) { %>
														<input class="FormEdicaoimgLocalizar" id="imaCancelarGuia" name="imaCancelarGuia<%=objTemp.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/16x16/remove.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga9%>');AlterarValue('GuiaCancelar','<%=objTemp.getId()%>');" title="Cancelar esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>)" /> Cancelar
														<%if( (!objTemp.isGuiaEmitidaSPG() && !objTemp.isGuiaEmitidaSSG()) && request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
															<%if( !objTemp.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_LOCOMOCAO) ) {%>
																<br />
																<br />
																<input class="FormEdicaoimgLocalizar" id="descontarGuia" name="descontarGuia<%=objTemp.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/descontar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>');AlterarValue('TipoGuiaReferenciaDescontoParcelamento','<%=GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO%>');AlterarValue('Id_GuiaEmissaoReferencia','<%=objTemp.getId()%>');" title="Descontar esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>)"/> Descontar
																<br />
																<br />
																<input class="FormEdicaoimgLocalizar" id="parcelarGuia" name="parcelarGuia<%=objTemp.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/parcelar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');AlterarValue('TipoGuiaReferenciaDescontoParcelamento','<%=GuiaEmissaoDt.TIPO_GUIA_PARCELADA%>');AlterarValue('Id_GuiaEmissaoReferencia','<%=objTemp.getId()%>');" title="Parcelar esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>)"/> Parcelar
															<%} %>
														<%} %>
														<br />
														<br />
														
														<input class="FormEdicaoimgLocalizar" id="reemitirGuia" name="reemitirGuia<%=objTemp.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/imgAtualizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');AlterarValue('TipoGuiaReferenciaDescontoParcelamento','<%=GuiaEmissaoDt.TIPO_GUIA_PARCELADA%>');AlterarValue('Id_GuiaEmissaoReferencia','<%=objTemp.getId()%>');" title="Guia Vencida. Reemitir esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>) adicionando 15 dias a partir de hoje no vencimento?"/> Alterar Data de Vencimento
														
														<br />
													<%} %>
													<%if( objTemp.isGuiaEnviadaCadin() ) { %>
														<div title="Guia Cadastrada no CADIN(Sem Permissão para Cancelar, Parcelar, Descontar ou Reemitir dando 15 Dias de Vencimento)">CADIN</div>
													<%} %>
												<%} else {
													if (objTemp.isGuiaAguardandoDeferimento() || objTemp.isGuiaBaixadaComAssistencia()) {%>
														<br />
														<input class="FormEdicaoimgLocalizar" id="mudarStatusGuia" name="mudarStatusGuia<%=objTemp.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/descontar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarDWR%>');AlterarValue('PassoEditar','<%=Configuracao.Salvar%>'); AlterarValue('GuiaAlterarStatus','<%=objTemp.getId()%>');" title="Mudar a Situação da Guia: (<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>) para AGUARDANDO PAGAMENTO"/> Alterar situação da Guia
														<br />
													<%}
													if( objTemp.isGuiaCancelada() ) {
														%>
														<br />
														<input class="FormEdicaoimgLocalizar" id="desfazerCancelamentoGuia" name="desfazerCancelamentoGuia<%=objTemp.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/imgRestaurarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');AlterarValue('DesfazerGuiaCancelar','<%=objTemp.getId()%>');" title="Desfazer Cancelamento da Guia: (<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>)" /> Desfazer Cancelamento
														<br />
														<%
													}
												}%>
												
											<%}%>
										</td>
									</tr>
								<%
									}
								}
							}
							else {
								%>
								<tr>
									<td colspan="9">
										<em> Nenhuma Guia Emitida pelo Processo Judicial Digital Localizada para este Processo. </em>
									</td>
								</tr>
								<%
							}
							%>
							</tbody>
						</table>
					</fieldset>
					
					<%if( request.getAttribute("ListaGuiaEmissaoLocomocaoVinculadaOficial") != null ) {%>
						<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Guias de Locomoções Vinculadas a Oficiais de Justiça</legend>
	                		
							<table id="Tabela" class="Tabela">
								<thead>
									<tr>
										<th>Nº</th>
										<th>Número Guia</th>
										<th>Tipo Guia</th>
										<th>Nome</th>
										<th>Data Emissão</th>
										<th>Data Vencimento</th>
										<th>Data Recebimento</th>
										<th>Data Cancelamento</th>
										<th>Situação</th>
										<th>Natureza</th>
										<th>Guia Parcelada/Desconto</th>
										<%if( request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
											<th>Opções</th>
										<%} %>
									</tr>
								</thead>
								<tbody id="tabListaEscala">
								<%
								List liTempLocomocaoVinculadaOficial = (List)request.getAttribute("ListaGuiaEmissaoLocomocaoVinculadaOficial");
								GuiaEmissaoDt objTempGuiaLocomocao = null;
								if( liTempLocomocaoVinculadaOficial != null && liTempLocomocaoVinculadaOficial.size() > 0 ) {
									for(int i = 0 ; i< liTempLocomocaoVinculadaOficial.size();i++) {
										objTempGuiaLocomocao = (GuiaEmissaoDt)liTempLocomocaoVinculadaOficial.get(i);
										if( objTempGuiaLocomocao != null ) { %>
										<tr>
											<td align="center"><%=(i + 1)%></td>
											
											<td align="center">
												<label id="labelNumeroGuiaCompleto<%=(i + 1)%>">
													<%
													if( objTempGuiaLocomocao.getId_GuiaStatus() != null && !objTempGuiaLocomocao.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
														%>
														<a href="<%=request.getAttribute("tempRetorno")%>?hash=<%=Funcoes.GeraHashMd5(objTempGuiaLocomocao.getId() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=objTempGuiaLocomocao.getId()%>&NumeroGuia=<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>&Id_GuiaTipo=<%=objTempGuiaLocomocao.getGuiaModeloDt().getId_GuiaTipo()%>&EhGuiaSPG_SSG=<%=(objTempGuiaLocomocao.isGuiaEmitidaSPG() || objTempGuiaLocomocao.isGuiaEmitidaSSG()?"S":"N")%>">
														<%
													}
													%>
														<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>
													<%
													if( objTempGuiaLocomocao.getId_GuiaStatus() != null && !objTempGuiaLocomocao.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
														%>
														</a>
														<%
													}
													%>
												</label>
											</td>
											
											<td>
												<%if( objTempGuiaLocomocao.getGuiaTipo() != null && objTempGuiaLocomocao.getGuiaTipo().length() > 0 ) { %>
													<%=objTempGuiaLocomocao.getGuiaTipo() %>
												<%}
												else { %>
													<%=objTempGuiaLocomocao.getGuiaModeloDt().getGuiaTipo() %>
												<%} %>
											</td>
											
											<td>
											   <%if( objTempGuiaLocomocao.getUsuario() != null && objTempGuiaLocomocao.getUsuario().length() > 0 ) { %>
													<%=objTempGuiaLocomocao.getUsuario() %>
									     	   <%}%>													 
											</td>
											
											<td align="center">
												<%=Funcoes.FormatarDataHora(objTempGuiaLocomocao.getDataEmissao())%>
											</td>
											
											<td align="center">
												<%=Funcoes.TelaData(objTempGuiaLocomocao.getDataVencimento()) %>
											</td>
											
											<td align="center">
												<%if( objTempGuiaLocomocao.getId_GuiaStatus() != null && objTempGuiaLocomocao.getId_GuiaStatus().length() > 0 && Integer.parseInt(objTempGuiaLocomocao.getId_GuiaStatus()) == GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA ) {
													%><%=objTempGuiaLocomocao.getGuiaStatus() %><%
												}
												else {%>
													<%=Funcoes.TelaData(objTempGuiaLocomocao.getDataRecebimento()) %>
												<%} %>
											</td>
											
											<td align="center">
												<%=Funcoes.FormatarDataHora(objTempGuiaLocomocao.getDataCancelamento()) %>
											</td>
											
											<td align="center">
												<%if(objTempGuiaLocomocao.getGuiaStatus() != null && objTempGuiaLocomocao.getGuiaStatus().trim().length() > 0) { %>
													<%=objTempGuiaLocomocao.getGuiaStatus() %>
													<%
													if( objTempGuiaLocomocao.isGuiaPaga() && objTempGuiaLocomocao.getPercRepasse() != null && !objTempGuiaLocomocao.getPercRepasse().isEmpty() && (Boolean)request.getAttribute("apresentarPercentualRepasse") ) {
														%>
														<br />
														(Percentual Repasse:<%=objTempGuiaLocomocao.getPercRepasse()%>%)
														<%
													}
													%>
												<%} else { %>
													CONSULTAR SPG*
												<%} %>
											</td>
											
											<td align="center">
												<%if(objTempGuiaLocomocao.getNaturezaSPG() != null && objTempGuiaLocomocao.getNaturezaSPG().trim().length() > 0) { %>
													<%=objTempGuiaLocomocao.getNaturezaSPG() %>											
												<%} %>
											</td>
											
											<td align="center">
												<%
												if( objTempGuiaLocomocao.getTipoGuiaReferenciaDescontoParcelamento() != null && !objTempGuiaLocomocao.getTipoGuiaReferenciaDescontoParcelamento().isEmpty() ) {
													if( objTempGuiaLocomocao.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_PARCELADA) ) {
														%>(Guia Parcelada: Parcela <%=objTempGuiaLocomocao.getParcelaAtual()%> de <%=objTempGuiaLocomocao.getQuantidadeParcelas()%>)<%
													}
													else {
														if( objTempGuiaLocomocao.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO) ) {
															%>(Guia Descontada: <%=objTempGuiaLocomocao.getPorcentagemDesconto()%>% de Desconto)<%
														}
													}
													%><br /><%
												}
												else {
													%><%
												}
												%>
											</td>
											
											<td>
												<%if((!objTempGuiaLocomocao.isGuiaEmitidaSPG() && !objTempGuiaLocomocao.isGuiaEmitidaSSG()) && request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
													<%if( objTempGuiaLocomocao.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) || objTempGuiaLocomocao.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)) ) { %>
														<%if( !objTempGuiaLocomocao.isGuiaEnviadaCadin() ) { %>
															<input class="FormEdicaoimgLocalizar" id="imaCancelarGuia" name="imaCancelarGuia<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/16x16/remove.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga9%>');AlterarValue('GuiaCancelar','<%=objTempGuiaLocomocao.getId()%>');" title="Cancelar esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>)" /> Cancelar
															<%if( (!objTempGuiaLocomocao.isGuiaEmitidaSPG() && !objTempGuiaLocomocao.isGuiaEmitidaSSG()) && request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
																<%if( !objTempGuiaLocomocao.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_LOCOMOCAO) ) {%>
																	<br />
																	<br />
																	<input class="FormEdicaoimgLocalizar" id="descontarGuia" name="descontarGuia<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/descontar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>');AlterarValue('TipoGuiaReferenciaDescontoParcelamento','<%=GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO%>');AlterarValue('Id_GuiaEmissaoReferencia','<%=objTempGuiaLocomocao.getId()%>');" title="Descontar esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>)"/> Descontar
																	<br />
																	<br />
																	<input class="FormEdicaoimgLocalizar" id="parcelarGuia" name="parcelarGuia<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/parcelar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');AlterarValue('TipoGuiaReferenciaDescontoParcelamento','<%=GuiaEmissaoDt.TIPO_GUIA_PARCELADA%>');AlterarValue('Id_GuiaEmissaoReferencia','<%=objTempGuiaLocomocao.getId()%>');" title="Parcelar esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>)"/> Parcelar
																<%} %>
															<%} %>
															<br />
															<br />
															
															<input class="FormEdicaoimgLocalizar" id="reemitirGuia" name="reemitirGuia<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/imgAtualizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');AlterarValue('TipoGuiaReferenciaDescontoParcelamento','<%=GuiaEmissaoDt.TIPO_GUIA_PARCELADA%>');AlterarValue('Id_GuiaEmissaoReferencia','<%=objTempGuiaLocomocao.getId()%>');" title="Guia Vencida. Reemitir esta Guia (<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>) adicionando 15 dias a partir de hoje no vencimento?"/> Alterar Data de Vencimento
															
															<br />
														<%} %>
														<%if( objTempGuiaLocomocao.isGuiaEnviadaCadin() ) { %>
															<div title="Guia Cadastrada no CADIN(Sem Permissão para Cancelar, Parcelar, Descontar ou Reemitir dando 15 Dias de Vencimento)">CADIN</div>
														<%} %>
													<%} else {
														if (objTempGuiaLocomocao.isGuiaAguardandoDeferimento() || objTempGuiaLocomocao.isGuiaBaixadaComAssistencia()) {%>
															<br />
															<input class="FormEdicaoimgLocalizar" id="mudarStatusGuia" name="mudarStatusGuia<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/descontar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarDWR%>');AlterarValue('PassoEditar','<%=Configuracao.Salvar%>'); AlterarValue('GuiaAlterarStatus','<%=objTempGuiaLocomocao.getId()%>');" title="Mudar a Situação da Guia: (<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>) para AGUARDANDO PAGAMENTO"/> Alterar situação da Guia
															<br />
														<%}
														if( objTempGuiaLocomocao.isGuiaCancelada() ) {
															%>
															<br />
															<input class="FormEdicaoimgLocalizar" id="desfazerCancelamentoGuia" name="desfazerCancelamentoGuia<%=objTempGuiaLocomocao.getNumeroGuiaCompleto()%>" type="image"  src="./imagens/imgRestaurarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');AlterarValue('DesfazerGuiaCancelar','<%=objTempGuiaLocomocao.getId()%>');" title="Desfazer Cancelamento da Guia: (<%=Funcoes.FormatarNumeroSerieGuia(objTempGuiaLocomocao.getNumeroGuiaCompleto())%>)" /> Desfazer Cancelamento
															<br />
															<%
														}
													}%>
													
												<%}%>
											</td>
										</tr>
									<%
										}
									}
								}
								%>
								</tbody>
							</table>
						</fieldset>
					<%} %>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Saldo de Itens de Despesa Postal neste Processo</legend>
                		
                		<div> Disponível Aguardando Pagamento </div>
						<span class="span2">
							<%if(request.getAttribute("quantidadeItemDepesaPostalNaoPago") != null) { %>
								<%=request.getAttribute("quantidadeItemDepesaPostalNaoPago")%>
							<%} else { %>
								Sem dados
							<%} %>
						</span>
						
						<div> Disponível Pago Sem Vínculo com Pendência </div>
						<span class="span2">
							<%if(request.getAttribute("quantidadeItemDepesaPostalPagoSemVinculoPendencia") != null) { %>
								<%=request.getAttribute("quantidadeItemDepesaPostalPagoSemVinculoPendencia")%>
							<%} else { %>
								Sem dados
							<%} %>
						</span>

						<div> Quantidade Vinculado com Pendência </div>
						<span class="span2">
							<%if(request.getAttribute("quantidadeItemVinculadoPendencia") != null) { %>
								<%=request.getAttribute("quantidadeItemVinculadoPendencia")%>
							<%} else { %>
								Sem dados
							<%} %>
						</span>
                		
                	</fieldset>
                	
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Quantidade de Ordem de Serviço de Despesa Postal</legend>
                		
                		<div> Ordem de Serviço Aberta </div>
						<span class="span2">
							<%if(request.getAttribute("quantidadeOSAberta") != null) { %>
								<%=request.getAttribute("quantidadeOSAberta")%>
							<%} else { %>
								Sem dados
							<%} %>
						</span>
						
						<div> Ordem de Serviço Fechada </div>
						<span class="span2">
							<%if(request.getAttribute("quantidadeOSFechada") != null) { %>
								<%=request.getAttribute("quantidadeOSFechada")%>
							<%} else { %>
								Sem dados
							<%} %>
						</span>
                		
                	</fieldset>
                	
                	<%if(request.getAttribute("quantidadeLocomocaoDisponivel") != null || request.getAttribute("saldoLocomocaoDisponivel") != null) {%>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Saldo de Locomoções Disponíveis</legend>
	                		
	                		<%
	                		if( request.getAttribute("quantidadeLocomocaoDisponivel") != null ) {
	               			List<LocomocaoDt> listaLocomocaoDtDisponivel = (List)request.getAttribute("quantidadeLocomocaoDisponivel");
		               			for(int z = 0; z < listaLocomocaoDtDisponivel.size(); z++ ) {
		               				LocomocaoDt locomocaoDt = listaLocomocaoDtDisponivel.get(z);
		               			%>
		                			<div>Quantidade <b><%=locomocaoDt.getQtdLocomocao() %></b></div>
		                			<span><%=locomocaoDt.getBairroDt().getBairro()%></span>
	                			<%}
	                		}%>
	                		
	                		<%
	                		if( request.getAttribute("saldoLocomocaoDisponivel") != null ) {
	               			%>
	                			<div>Saldo Remanescente (Saldo SPG)</b></div>
	                			<span><%=request.getAttribute("saldoLocomocaoDisponivel")%></span>
                			<%}%>
	                	</fieldset>
                	<%}%>
					
                </fieldset>
			</div>	
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>	
		</form>		
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>

</html>