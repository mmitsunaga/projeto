<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroLocomocaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.OficialSPGDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaCalculoNe"%>

<jsp:useBean id="processoDt" scope="session"	class="br.gov.go.tj.projudi.dt.ProcessoDt" />
<jsp:useBean id="GuiaEmissaoDt" scope="session"	class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
<title>TJGO/Projudi - Formulário de Guia de Fazenda Municipal</title>
<style type="text/css">
@import url('./css/Principal.css');

@import url('./css/Paginacao.css');

#bkg_projudi {
	display: none;
}
</style>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
<script type='text/javascript' src='./js/jquery.js'></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<link type='text/css' rel='stylesheet'
	href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112'
	media='screen' />
<script type="text/javascript"
	src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
<script type="text/javascript">
	function retirarTudoPog() {
		document.getElementById("leilaoValor").value = "";
		document.getElementById("leilaoQuantidade").value = "";
	}
	function pogDay() {
		if( document.getElementById("leilaoValor").value == "0" || document.getElementById("leilaoValor").value == ",0" || document.getElementById("leilaoValor").value == "0,0" || document.getElementById("leilaoValor").value == "0,00" ) {
			document.getElementById("leilaoValor").value = "";
			document.getElementById("leilaoQuantidade").value = "";
		}
	}
</script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">

		<div class="area"><h2>&raquo; 
			|<%=request.getAttribute("tempPrograma")%>| Formulário de Guia
		</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaFazendaMunicipal" id="GuiaFazendaMunicipal">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" /> 
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			 
			<input type="hidden" id="posicaoListaCustaExcluir" name="posicaoListaCustaExcluir" value="-1" /> 
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" /> 
			<input type="hidden" id="posicaoListaBairroContaVinculadaExcluir" name="posicaoListaBairroContaVinculadaExcluir" value="-1" /> 
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />

			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Processo</legend>

					<div>Número</div>
					<span><a
						href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a>
					</span/>
				</fieldset>

				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend> Informações </legend>

					<%if(!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) {%>
						<div> Serventia </div>
						<span class="span1"><%= processoDt.getServentia()%></span>
						<br />
					<%} %>

					<div>Classe</div>
					<span style="width: 500px;"><%=processoDt.getProcessoTipo()%></span>
					<br />

					<%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>

					<%
						if (!processoDt.getValor().equals("Null")) {
					%>
					<div>Valor da Causa</div>
					<span class="span1"><%=processoDt.getValor()%></span>
					<%
						}
					%>
				</fieldset>

				<fieldset id="VisualizaDados" class="VisualizaDados"
					style="background: #E6E6E6;">
					<legend style="background: #E6E6E6;"> Valor Atualizado </legend>

					<div>Valor da Causa</div>
					<span> <input class="formEdicaoInputSomenteLeitura"
						type="text" name="valorAcao" id="valorAcao" readonly
						value="<%=processoDt.getValor()%>" maxlength="15" /> </span>

					<div>Data Recebimento do Processo</div>
					<span> <input type="text"
						class="formEdicaoInputSomenteLeitura" name="dataBaseAtualizacao"
						id="dataBaseAtualizacao"
						value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseAtualizacao())%>"
						size="10" maxlength="10" onkeyup="mascara_data(this)"
						onblur="verifica_data(this)"
						onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();"
						readonly /> </span> <br />

					<div>Valor Base para Cálculo</div>
					<span> <input type="text" name="novoValorAcao"
						id="novoValorAcao" value="<%=GuiaEmissaoDt.getNovoValorAcao()%>"
						maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)"
						onkeypress="return DigitarSoNumero(this, event)"
						onBlur="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" />
					</span>

					<div>Data Base para Cálculo</div>
					<span> <input type="text"
						class="formEdicaoInputSomenteLeitura"
						name="dataBaseFinalAtualizacao" id="dataBaseFinalAtualizacao"
						value="<%=Funcoes.TelaData(GuiaEmissaoDt
					.getDataBaseFinalAtualizacao())%>"
						size="10" maxlength="10" onkeyup="mascara_data(this)"
						onblur="verifica_data(this)"
						onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();"
						readonly /> <!-- <img id="calendarioDataBaseFinalAtualizacao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataBaseFinalAtualizacao,'dd/mm/yyyy',this)"/> -->
					</span> <br />
					<br />

					<div>Valor da Causa Base Atualizado</div>
					<span> <input class="formEdicaoInputSomenteLeitura"
						type="text" name="novoValorAcaoAtualizado"
						id="novoValorAcaoAtualizado" readonly
						value="<%=GuiaEmissaoDt.getNovoValorAcaoAtualizado()%>"
						maxlength="15" /> </span>

				</fieldset>

				<fieldset id="VisualizaDados" class="VisualizaDados"
					style="background: #E6E6E6;">
					<legend style="background: #E6E6E6;">Taxa Protocolo</legend>

					<div>Quantidade</div>
					<span> <input type="text" name="taxaProtocoloQuantidade"
						id="taxaProtocoloQuantidade"
						value="<%=GuiaEmissaoDt.getTaxaProtocoloQuantidade()%>"
						maxlength="3" size="5"
						onkeypress="return DigitarSoNumero(this, event)"
						onblur="zerarCampoLimpo(this, '0');minimoUm(this,'Taxa de Protocolo');" /> </span>
				</fieldset>


				<fieldset id="VisualizaDados" class="VisualizaDados"
					style="background: #E6E6E6;">
					<legend style="background: #E6E6E6;"> Outras Custas </legend>

<!-- 					<div style="width: 270px;">Qtde. Autos (Reg. 61)</div> -->
<!-- 					<span> <input type="text" name="penhoraQuantidade" -->
<!-- 						id="penhoraQuantidade" -->
<%-- 						value="<%=GuiaEmissaoDt.getPenhoraQuantidade()%>" maxlength="3" --%>
<!-- 						size="5" onkeypress="return DigitarSoNumero(this, event)" -->
<!-- 						onblur="zerarCampoLimpo(this, '0')" /> </span> <br /> -->
<!-- 					<br /> -->

					<div style="width: 270px;">Despesas Postais</div>
					<span> <input type="text" name="correioQuantidade"
						id="correioQuantidade"
						value="<%=GuiaEmissaoDt.getCorreioQuantidade()%>" maxlength="3"
						size="5" onkeypress="return DigitarSoNumero(this, event)"
						onblur="zerarCampoLimpo(this, '0')" /> </span> <br />
					<br />

				</fieldset>

				<%
					if (request.getSession().getAttribute("possuiHonorarios") != null
							&& (Boolean) request.getSession().getAttribute(
									"possuiHonorarios")) {
				%>

				<fieldset id="VisualizaDados" class="VisualizaDados"
					style="background: #E6E6E6;">
					<legend style="background: #E6E6E6;">Honorários do
						Procurador</legend>

					<div>Porcentagem (%)</div>
					<span>
						<select name="honorariosQuantidade" id="honorariosQuantidade" class="formEdicaoInputSomenteLeitura"
						value="<%=GuiaEmissaoDt.getHonorariosQuantidade()%>">
							<option value=""></option>
							<option value="0" <%if(GuiaEmissaoDt.getHonorariosQuantidade().equals("0")){%>selected<%}%> >0 %</option>
							<option value="10" <%if(GuiaEmissaoDt.getHonorariosQuantidade().equals("10")){%>selected<%}%> >10 %</option>
						</select>
					</span>

					<div>Débito Atualizado</div>
					<span> <input type="text" name="honorariosValor"
						id="honorariosValor"
						value="<%=GuiaEmissaoDt.getHonorariosValor()%>" maxlength="15"
						onkeyup="MascaraValor(this);autoTab(this,20)"
						onkeypress="return DigitarSoNumero(this, event)" <% if (request.getSession().getAttribute("bloqueiaHonorarios") != null && (Boolean) request.getSession().getAttribute("bloqueiaHonorarios")) { %>readonly class="formEdicaoInputSomenteLeitura" <% } %> /> </span>

					<div>Parcelas</div>
					<span> <input type="text" name="parcelasQuantidade"
						id="parcelasQuantidade"
						value="<%=GuiaEmissaoDt.getParcelasQuantidade()%>" maxlength="3"
						size="5" onkeypress="return DigitarSoNumero(this, event)"
						onblur="zerarCampoLimpo(this, '0')" /> </span>

				</fieldset>

				<%
					}
				%>

				<fieldset id="VisualizaDados" class="VisualizaDados"
					style="background: #E6E6E6;">
					<legend style="background: #E6E6E6;"> Atos dos Avaliadores </legend>

					<div>Quantidade</div>
					<span> <input type="text" name="avaliadorQuantidade"
						id="avaliadorQuantidade"
						value="<%=GuiaEmissaoDt.getAvaliadorQuantidade()%>" maxlength="3"
						onkeypress="return DigitarSoNumero(this, event)"
						onblur="zerarCampoLimpo(this, '0')" /> </span>

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
                		Atos dos Porteiros dos Auditórios
                	</legend>
                	
                	<div class="col30"><label class="formEdicaoLabel">Protocolo Quantidade (Reg. 15)</label><br>
                		
                		<input type="text" name="leilaoQuantidade" id="leilaoQuantidade" value="<%=GuiaEmissaoDt.getLeilaoQuantidade()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                	<div class="col30"><label class="formEdicaoLabel">Quantidade de Documentos Publicados Diário da Justiça</label><br>
                	
                		<input type="text" name="documentoDiarioJustica" id="documentoDiarioJustica" value="<%=GuiaEmissaoDt.getDocumentoDiarioJustica()%>" maxlength="3" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
                	</div>
                	
                </fieldset>

				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Locomoções 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</legend>
					
					<div> Oficial de Justiça </div>
					<span>
						<select id="codigoOficialSPGLocomocao" name="codigoOficialSPGLocomocao">
							<option value="" selected="selected"></option>
							
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
					</span>
						
					<br /><br />
					
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
											<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i%>" name="quantidadeLocomocao<%=i%>" value="<%=listaQuantidadeBairroDt.get(i)%>" size="1" />
											<input type="button" id="mais<%=i%>" name="mais<%=i%>" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i%>);" />
											<input type="button" id="menos<%=i%>" name="menos<%=i%>" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i %>,'1');" />
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
											<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" title="Excluir esta Locomoção" />
										</td>
									</tr>							
									<%
								}
		                	}
		                	else {
		                		%>
		                		<tr>
		                			<td colspan="6">                			
		                				<em> Insira um Bairro para uma Locomoção. </em>
		                			</td>
		                		</tr>
		                		<%
		                	}
							%>
			    		</tbody>
					</table>
				</fieldset>

				<fieldset id="VisualizaDados" class="VisualizaDados"
					style="background: #E6E6E6;">
					<legend style="background: #E6E6E6;"> Acréscimo por pessoa
					</legend>

					<div>Quantidade</div>
					<span> <input type="text" name="quantidadeAcrescimoPessoa"
						id="quantidadeAcrescimoPessoa"
						value="<%=GuiaEmissaoDt.getQuantidadeAcrescimo()%>" maxlength="3"
						onkeypress="return DigitarSoNumero(this, event)"
						onblur="zerarCampoLimpo(this, '0')" /> </span>

				</fieldset>

				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Locomoções Conta Vinculada
					</legend>
					
					<br /><br />
					
					<table id="TabelaLocomocoes" class="Tabela">
	       				<thead>
	           				<tr class="TituloColuna">
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
											TRIBUNAL DE JUSTIÇA
										</td>
										<td align="center">
											<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroContaVinculadaExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" title="Excluir esta Locomoção" />
										</td>
									</tr>							
									<%
								}
		                	}
		                	else {
		                		%>
		                		<tr>
		                			<td colspan="5">                			
		                				<em> Insira um Bairro para uma Locomoção de Conta Vinculada. </em>
		                			</td>
		                		</tr>
		                		<%
		                	}
							%>
			    		</tbody>
					</table>
				</fieldset>


				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<button name="imgPreviaCalculo" value="Prévia do Cálculo"
						onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');">
						<img src="./imagens/16x16/calculadora.png" alt="Prévia do Cálculo" />
						Prévia do Cálculo
					</button>
					<button name="imgLimpar" value="Limpar"
						onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						<img src="./imagens/16x16/edit-clear.png" alt="Limpar" /> Limpar
					</button>
				</div>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>