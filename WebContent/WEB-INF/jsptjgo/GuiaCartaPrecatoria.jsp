<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.OficialSPGDt"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Custas Execução de Sentença</title>
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
</head>
<body>
	<div id="divCorpo" class="divCorpo">
	  	
	<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaExecucaoSentenca" id="GuiaExecucaoSentenca">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" id="posicaoListaCustaExcluir" name="posicaoListaCustaExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroContaVinculadaExcluir" name="posicaoListaBairroContaVinculadaExcluir" value="-1" />
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
			<input type="hidden" id="atoEscrivao" name="atoEscrivao" value="100" />
		
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                	<div> Número</div>
					<span><a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
                </fieldset>
                
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
				
				<fieldset id="VisualizaDados" class="VisualizaDados" >
                	<legend>
                		Valor Atualizado
                	</legend>
                	
                	<div> Valor da Causa </div>
					<span>
						<input class="formEdicaoInputSomenteLeitura" type="text" name="valorAcao" id="valorAcao" readonly value="<%=processoDt.getValor()%>" maxlength="15" />
					</span>
					
					<div> Data Recebimento do Processo </div>
					<span>
						<input type="text" class="formEdicaoInputSomenteLeitura" name="dataBaseAtualizacao" id="dataBaseAtualizacao" value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseAtualizacao())%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</span>
					
					<br />
					
					<div>Valor Base para Cálculo</div>
					<span>
						<input type="text" class="formEdicaoInputSomenteLeitura" name="novoValorAcao" id="novoValorAcao" value="<%=GuiaEmissaoDt.getNovoValorAcao()%>" maxlength="15" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" onBlur="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</span>
					
					<div> Data Base para Cálculo </div>
					<span>
						<input type="text" class="formEdicaoInputSomenteLeitura" name="dataBaseFinalAtualizacao" id="dataBaseFinalAtualizacao" value="<%=Funcoes.TelaData(GuiaEmissaoDt.getDataBaseFinalAtualizacao())%>" size="10" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onChange="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Atualizar)%>');submit();" readonly />
					</span>
					
					<br /><br />
					
					<div>Valor da Causa Base Atualizado</div>
					<span>
						<input class="formEdicaoInputSomenteLeitura" type="text" name="novoValorAcaoAtualizado" id="novoValorAcaoAtualizado" readonly value="<%=GuiaEmissaoDt.getNovoValorAcaoAtualizado()%>" maxlength="15" />
					</span>
					
                </fieldset>
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Locomoções 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</legend>
					
					<div> Oficial de Justiça </div>
					<span>
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
					</span>
						
					<br /><br />
					
					<%
	            	List liTemp = (List)request.getSession().getAttribute("ListaBairroDt");
                	if( liTemp != null && liTemp.size() > 0 ) {
	                	for( int i = 0; i < liTemp.size(); i++ ) {
						BairroDt bairroAuxDt = (BairroDt)liTemp.get(i);
						%>
						<div> Bairro </div> 
	       				<span style="width: 220px;">
	       					<%= bairroAuxDt.getBairro() %>
	       				</span>
	       				
	       				<div> Cidade </div> 
	       				<span style="width: 220px;">
	       					<%= bairroAuxDt.getCidade() %>
	       				</span>
	       				
	       				<div> UF </div>
	       				<span style="width: 50px;">
	       					<%= bairroAuxDt.getUf() %>
	       				</span>
	       				
	       				<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" title="Excluir esta Locomoção" />
						<%
						}
                	}
                	else {
                		%>
                		<em> Insira um Bairro para uma Locomoção. </em>
                		<%
                	}
					%>
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Locomoções - Penhora
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>')" />
					</legend>
					<%
	            	liTemp = (List)request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
					List listaQuantidadeLocomocaoPenhora = (List)request.getSession().getAttribute("ListaQuantidadeLocomocaoPenhora");
                	if( liTemp != null && liTemp.size() > 0 ) {
	                	for( int i = 0; i < liTemp.size(); i++ ) {
						BairroDt bairroAuxDt = (BairroDt)liTemp.get(i);
						%>
						<div style="width: 60px;"> Quantidade </div>
						<span style="width: 120px;">
							<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocaoPenhora<%=i %>" name="quantidadeLocomocaoPenhora<%=i %>" value="<%=listaQuantidadeLocomocaoPenhora.get(i)%>" size="1" />
							<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocaoPenhora<%=i %>);" />
							<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocaoPenhora<%=i %>,'1');" />
						</span>
						
						<div style="width: 50px;"> Bairro </div>
						<span style="width: 200px;">
	       					<%= bairroAuxDt.getBairro() %>
	       				</span>
	       				
	       				<div style="width: 50px;"> Cidade </div>
	       				<span style="width: 150px;">
	       					<%= bairroAuxDt.getCidade() %>
	       				</span>
	       				
	       				<div> UF </div>
	       				<span style="width: 50px;">
	       					<%= bairroAuxDt.getUf() %>
	       				</span>
	       				
	       				<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga9%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>')" title="Excluir esta Locomoção" style="float: right;"/>
	       				<br />
						<%
						}
                	}
                	else {
                		%>
                		<em id="labelInserirPenhora"> Insira uma Locomoção para um Bairro. </em>
                		<%
                	}
					%>
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Locomoções Conta Vinculada
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairroContaVinculada" name="imaLocalizarBairroContaVinculada" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1)%>')" />
					</legend>
					
					<div> Oficial de Justiça </div>
					<span>
						<select id="codigoOficialSPGLocomocaoContaVinculada" name="codigoOficialSPGLocomocaoContaVinculada">
							<option value="<%=OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA%>" selected ><%=OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA%></option>
						</select>
					</span>
					
					<br /><br />
					
					<%
					List listaBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
					List listaQuantidadeBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
                	if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
	                	for( int i = 0; i < listaBairroLocomocaoContaVinculada.size(); i++ ) {
						BairroDt bairroAuxDt = (BairroDt)listaBairroLocomocaoContaVinculada.get(i);
						%>
						<div style="width: 60px;"> Quantidade </div>
						<span style="width: 120px;">
							<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocaoContaVinculada<%=i %>" name="quantidadeLocomocaoContaVinculada<%=i %>" value="<%=listaQuantidadeBairroLocomocaoContaVinculada.get(i)%>" size="1" />
							<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocaoContaVinculada<%=i %>);" />
							<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocaoContaVinculada<%=i %>,'1');" />
						</span>
						
						<div style="width: 50px;"> Bairro </div>
						<span style="width: 200px;">
	       					<%= bairroAuxDt.getBairro() %>
	       				</span>
	       				
	       				<div style="width: 50px;"> Cidade </div>
	       				<span style="width: 150px;">
	       					<%= bairroAuxDt.getCidade() %>
	       				</span>
	       				
	       				<div> UF </div>
	       				<span style="width: 50px;">
	       					<%= bairroAuxDt.getUf() %>
	       				</span>
	       				
	       				<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroContaVinculadaExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" title="Excluir esta Locomoção" />
	       				<br />
						<%
						}
                	}
                	else {
                		%>
                		<em> Insira um Bairro para uma Locomoção de Conta Vinculada. </em>
                		<%
                	}
					%>
				</fieldset>
                
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