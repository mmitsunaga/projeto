<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="GuiaEmissaoDtInicial" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Custas Iniciais</title>
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
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area">
			<h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia Inicial</h2>
		</div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaInicial" id="GuiaInicial">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" name="Id_Comarca" id="Id_Comarca" value="<%=GuiaEmissaoDtInicial.getId_Comarca()%>" />
			<input type="hidden" name="Id_AreaDistribuicao" id="Id_AreaDistribuicao" value="<%=GuiaEmissaoDtInicial.getId_AreaDistribuicao()%>" />
			<input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=GuiaEmissaoDtInicial.getId_ProcessoTipo()%>" />
			
			<div id="divEditar" class="divEditar">
			
				
			
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						* Primeiro ou Segundo Grau ?
					</legend>
					
					<div> 1º Primeiro Grau </div>
					<span>
						<input type="radio" id="grau1" name="grau" value="<%=GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU%>" <%=GuiaEmissaoDtInicial.isGuiaInicial1Grau()?"checked":""%> onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');submit();"/>
					</span>
					
					<br /><br />
					
					<div> 2º Segundo Grau </div>
					<span>
						<input type="radio" id="grau2" name="grau" value="<%=GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU%>" <%=GuiaEmissaoDtInicial.isGuiaInicial2Grau()?"checked":""%> onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');submit();" />
					</span>
					
					<br /><br />
					
					<div> Mandado de Segurança<br />(Turma Recursal) </div>
					<span>
						<input type="radio" id="grau3" name="grau" value="<%=GuiaTipoDt.TURMA_RECURSAL%>" <%=GuiaEmissaoDtInicial.isGuiaInicialTurmaRecursal()?"checked":""%> onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');submit();" />
					</span>
					
					<br /><br />
					
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						* Cível ou Criminal ?
					</legend>
					
					<div> Cível </div>
					<span>
						<input type="radio" id="areaCivel" name="area" value="<%=AreaDt.CIVEL%>" <%=GuiaEmissaoDtInicial.isAreaCivil()?"checked":""%> onclick="limparAreaDistribuicaoProcessoTipoGuiaInicial('<%=request.getAttribute("tempRetorno")%>', '<%=Configuracao.Curinga6 %>', '<%=Configuracao.Excluir %>');" />
					</span>
					
					<br /><br />
					
					<div> Criminal </div>
					<span>
						<input type="radio" id="areaCriminal" name="area" value="<%=AreaDt.CRIMINAL%>" <%=GuiaEmissaoDtInicial.isAreaCriminal()?"checked":""%> onclick="limparAreaDistribuicaoProcessoTipoGuiaInicial('<%=request.getAttribute("tempRetorno")%>', '<%=Configuracao.Curinga6 %>', '<%=Configuracao.Excluir %>');" />
					</span>
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Número do Processo Principal (Dependente/Conexão)
                	</legend>
                	
                	<div> Processo Vinculado </div>
					<span>
						<input type="text" name="numeroProcessoDependente" id="numeroProcessoDependente" value="<%=GuiaEmissaoDtInicial.getNumeroProcessoDependente()%>" maxlength="26" onkeyup="mostrarOcultarDivAreaDistribuicaoGuiaInicial(this);mascara(this,'#######.##.####.#.##.####');autoTab(this,25);" onkeypress="return DigitarSoNumero(this, event)" oninput="isProcessoVinculadoGuiaInicialPreenchido(this, '<%=String.valueOf(Configuracao.Curinga7)%>', 'GuiaInicial')" onpaste="isProcessoVinculadoGuiaInicialPreenchido(this, '<%=String.valueOf(Configuracao.Curinga7)%>', 'GuiaInicial')" size="80" />
					</span>
                </fieldset>
			
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						* Dados
					</legend>
					
					<div id="divComarca"> * Comarca </div>
					<span>
						<%if( GuiaEmissaoDtInicial.isGuiaInicial1Grau() ) { %>
							<input id="imaLocalizarComarca" name="imaLocalizarComarca" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
						<%} %>
                    	<input class="formEdicaoInputSomenteLeitura" readonly name="Comarca" id="Comarca" type="text" size="30" maxlength="100" value="<%=GuiaEmissaoDtInicial.getComarca()%>"/>
					</span>
					
					<br /><br />
					
					<div id="divAreaDistribuicao"> * Área de Distribuição </div>
					<span style="width: 500px;">
						<input id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
						<input class="formEdicaoInputSomenteLeitura" readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="60" maxlength="100" value="<%=GuiaEmissaoDtInicial.getAreaDistribuicao()%>"/>
					</span>
					
					<br /><br />
					
					<div>
						<a href="http://www.cnj.jus.br/sgt/consulta_publica_classes.php" target="blank" title="Clique neste link para visualizar a tabela detalhada de classes do CNJ">*Classe</a>
					</div>
					<span style="width: 600px;">
						<input id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
                    	<input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="60" maxlength="100" value="<%=GuiaEmissaoDtInicial.getProcessoTipo()%>"/>
					</span>
				</fieldset>
			
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Partes
					</legend>
					
					<div> * Primeiro Autor </div>
					<span>
						<input type="text" name="requerente" id="requerente" value="<%=GuiaEmissaoDtInicial.getRequerente()%>" maxlength="80" size="80" />
					</span>
					
					<br /><br />
					
					<div> Primeiro Réu </div>
					<span>
						<input type="text" name="requerido" id="requerido" value="<%=GuiaEmissaoDtInicial.getRequerido()%>" maxlength="80" size="80" />
					</span>
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		* Valor da Causa
                	</legend>
                	
                	<div> Valor da Causa </div>
					<span>
						<input type="text" name="novoValorAcao" id="novoValorAcao" value="<%=GuiaEmissaoDtInicial.getNovoValorAcao()%>" maxlength="25" onkeyup="MascaraValor(this);autoTab(this,25)" onkeypress="return DigitarSoNumero(this, event)" size="80" />
					</span>
                </fieldset>
                
                <% if( new GuiaEmissaoNe().isProcessoTipoPortePostagem_e_OrigemEstado(GuiaEmissaoDtInicial.getProcessoTipoCodigo()) ) { %>
	                <fieldset id="VisualizaDados" class="VisualizaDados">
	                	<legend>
	                		* Originário do Estado de Goiás?
	                	</legend>
	                	
	                	<div> Originário do Estado de Goiás? </div>
						<span>
							<select id="origemEstado" name="origemEstado">
								<option value="">Selecione uma opção</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(!GuiaEmissaoDtInicial.isOrigemEstado()?"selected":"")%>>NÃO</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDtInicial.isOrigemEstado()?"selected":"")%>>SIM</option>
	                		</select>
						</span>
	                </fieldset>
	                
	                <fieldset id="VisualizaDados" class="VisualizaDados">
	                	<legend>
	                		* Porte Postagem para Protocolo Integrado
	                	</legend>
                		
	                	<div>Porte Postagem para Protocolo Integrado?</div>
	                	<span>
	                		<select id="protocoloIntegrado" name="protocoloIntegrado">
								<option value="">Selecione uma opção</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(!GuiaEmissaoDtInicial.isProtocoloIntegrado()?"selected":"")%>>NÃO</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDtInicial.isProtocoloIntegrado()?"selected":"")%>>SIM</option>
	                		</select>
	                	</span>
						
	                	<div>Quantidade de Folhas</div>
	                	<span>
	                		<input type="text" name="porteRemessaQuantidade" id="porteRemessaQuantidade" value="<%=GuiaEmissaoDtInicial.getPorteRemessaQuantidade()%>" maxlength="4" onkeypress="return DigitarSoNumero(this, event)" title="Informe a quantidade de folhas para o protocolo integrado." onblur="zerarCampoLimpo(this, '0')" />
	                	</span>
                	</fieldset>
                <%} %>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Número de Impetrantes ?
                	</legend>
                	
                	<div> Número de Impetrantes </div>
					<span>
						<input type="text" name="numeroImpetrantes" id="numeroImpetrantes" value="<%=GuiaEmissaoDtInicial.getNumeroImpetrantes()%>" maxlength="10" onkeyup="autoTab(this,10)" onkeypress="return DigitarSoNumero(this, event)" size="80" />
					</span>
                </fieldset>
				
				<%if( GuiaEmissaoDtInicial.isGuiaInicial1Grau() ) { %>
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Bens a partilhar ?
						</legend>
						
						<div> Sim </div>
						<span>
							<input type="radio" id="bensPartilharSim" name="bensPartilhar" value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=GuiaEmissaoDtInicial.getBensPartilhar().equals(new String(GuiaEmissaoDt.VALOR_SIM))?"checked":""%> />
						</span>
						
						<br /><br />
						
						<div> Não </div>
						<span>
							<input type="radio" id="bensPartilharNão" name="bensPartilhar" value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=GuiaEmissaoDtInicial.getBensPartilhar().equals(new String(GuiaEmissaoDt.VALOR_NAO))?"checked":""%> />
						</span>
					</fieldset>
				<%} %>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Despesas Postais ?
                	</legend>
                	
                	<div> Quantidade </div>
					<span>
						<input type="text" name="correioQuantidade" id="correioQuantidade" value="<%=GuiaEmissaoDtInicial.getCorreioQuantidade()%>" maxlength="3" size="80" onkeypress="return DigitarSoNumero(this, event)" onblur="zerarCampoLimpo(this, '0')" />
					</span>
                </fieldset>
				
				<%if( GuiaEmissaoDtInicial.isGuiaInicial1Grau() ) { %>
					<fieldset id="VisualizaDados">
	                	<legend>
	                		Finalidade
	                	</legend>
	                	
	                	<div class="col35"><label class="formEdicaoLabel">Escolha a Finalidade</label><br>
	                	
	                		<select id="finalidade" name="finalidade" onChange="finalidadeGuiaLocomocaoAlterada()">
	                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO))?"selected":"")%>>Locomoção</option>
	                			<option value="<%=GuiaLocomocaoNe.PENHORA_AVALIACAO_ALIENACAO%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.PENHORA_AVALIACAO_ALIENACAO))?"selected":"")%>>Penhora, avaliação e alienação</option>
	                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO))?"selected":"")%>>Citação, penhora, avaliação e alienação</option>
	                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO))?"selected":"")%>>Citação, penhora e praça/leilão</option>
	                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO))?"selected":"")%>>Citação, penhora, avaliação e praça/leilão</option>
	                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO))?"selected":"")%>>Locomoção para avaliação</option>
	                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA%>" <%=(GuiaEmissaoDtInicial.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA))?"selected":"")%>>Locomoção para avaliação e Praça</option>
	                		</select>
	                	</div>
	                	
	                	<% if(request.getAttribute("exibeOficialCompanheiro") != null && Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeOficialCompanheiro")))) { %>
		                	<div class="col35"><label class="formEdicaoLabel">Oficial Companheiro?</label><br>
		                	
		                		<select id="oficialCompanheiro" name="oficialCompanheiro" onChange="oficialCompanheiroAlterado()">
		                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDtInicial.isOficialCompanheiro()?"":"selected")%> >NÃO</option>
		                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDtInicial.isOficialCompanheiro()?"selected":"")%> >SIM</option>
		               			</select>
		                	</div>
	                	<% } %>
	                	
	                	<div class="clear"></div>
	                	
	                	<div class="col35"><label class="formEdicaoLabel">Penhora?</label><br>
	                	
	                		<select id="penhora" name="penhora" onChange="penhoraAlterada()">
	                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDtInicial.isPenhora()?"":"selected")%> >NÃO</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDtInicial.isPenhora()?"selected":"")%> >SIM</option>
	               			</select>
	                	</div>
	                	
	                	<div class="col35"><label class="formEdicaoLabel">Intimação?</label><br>
	                	
	                		<select id="intimacao" name="intimacao">
	                			<option value="<%=GuiaEmissaoDt.VALOR_NAO%>" <%=(GuiaEmissaoDtInicial.isIntimacao()?"":"selected")%> >NÃO</option>
	                			<option value="<%=GuiaEmissaoDt.VALOR_SIM%>" <%=(GuiaEmissaoDtInicial.isIntimacao()?"selected":"")%> >SIM</option>
	               			</select>
	                	</div>
	                	
	                </fieldset>
	               <%} %>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Endereços para as Locomoções?
						&nbsp;
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image" alt="Adicionar Locomoção" title="Adicionar Locomoção" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</legend>
					
					<%
					if( request.getSession().getAttribute("ListaBairroDt") != null && request.getSession().getAttribute("ListaQuantidadeBairroDt") != null ) {
		            	List liTemp = (List)request.getSession().getAttribute("ListaBairroDt");
						List listaQuantidadeBairroDt = (List)request.getSession().getAttribute("ListaQuantidadeBairroDt");
	                	if( liTemp != null && liTemp.size() > 0 ) {
		                	for( int i = 0; i < liTemp.size(); i++ ) {
							BairroGuiaLocomocaoDt bairroAuxDt = (BairroGuiaLocomocaoDt)liTemp.get(i);
							%>
							<div style="width: 22px;"> Qtde:</div>
							<span style="width: 110px;">
								<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i%>" name="quantidadeLocomocao<%=i%>" value="<%=bairroAuxDt.getQuantidade()%>" size="1" />
								<input type="button" id="somarQuantidade<%=i%>" name="somarQuantidade<%=i%>" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i%>);" />
								<input type="button" id="subtrairQuantidade<%=i%>" name="subtrairQuantidade<%=i%>" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i%>,'1');" />
							</span>
							
							<div style="width: 30px;"> Bairro:</div>
							<span style="width: 200px;">
		       					<%= bairroAuxDt.getBairroDt().getBairro() %>
		       				</span>
		       				
		       				<div style="width: 35px;"> Cidade: </div>
		       				<span style="width: 200px;">
		       					<%= bairroAuxDt.getBairroDt().getCidade() %>-<%= bairroAuxDt.getBairroDt().getUf() %>
		       				</span>
		       				
		       				<div style="width: 60px;"> Finalidade: </div>
		       				<span style="width: 150px;">
		       					<%= GuiaEmissaoNe.getTextoFinalidade(Funcoes.StringToInt(bairroAuxDt.getFinalidade())) %> (<%= (bairroAuxDt.isOficialCompanheiro() ? "Sim" : "Não") %>|<%= (bairroAuxDt.isPenhora() ? "Sim" : "Não") %>|<%= (bairroAuxDt.isIntimacao() ? "Sim" : "Não") %>)
		       				</span>
		       				
		       				<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" title="Excluir esta Locomoção" />
		       				<br />
							<%
							}
	                	}
	                	else {
	                		%>
	                		<em> Insira um Bairro para uma Locomoção. </em>
	                		<%
	                	}
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
			<%@ include file="Padroes/reCaptcha.jspf" %>
	  	</form>   
	</div>
	
	<script type="text/javascript">
		$(document).ready(function() {
			finalidadeGuiaLocomocaoAlterada();
			$('#penhora option:eq(0)').prop('selected', true);
			$('#intimacao option:eq(0)').prop('selected', true);
			<%if (GuiaEmissaoDtInicial.isPenhora()) { %>
				$('#penhora option:eq(1)').prop('selected', true);
				<%if (GuiaEmissaoDtInicial.isIntimacao()) { %>
					$('#intimacao option:eq(1)').prop('selected', true);
				<%} %>
			<%} %>			
			penhoraAlterada();
		});	
		
		<%if( GuiaEmissaoDtInicial.isGrauGuiaInicialMesmoGrauProcessoDependente()) { %>
			Ocultar('divComarca');
			Ocultar('imaLocalizarComarca');
			Ocultar('Comarca');
			Ocultar('divAreaDistribuicao');
			Ocultar('imaLocalizarAreaDistribuicao');
			Ocultar('AreaDistribuicao');
		<%} %>
	</script>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>