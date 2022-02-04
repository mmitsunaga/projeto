<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaFinalNe"%>
<%@page import="br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaProcessoTJGO"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoCompletaDt"%>

<jsp:useBean id="guiaEmissaoCompletaDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoCompletaDt"/>
<jsp:useBean id="guiaEmissaoConsultaDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoConsultaDt"/>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Pr�-C�lculo</title>
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
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formul&aacute;rio de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaPreviaCalculo" id="GuiaPreviaCalculo" />
			<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input type="hidden" id="PassoEditar" name="PassoEditar" value="<%=request.getAttribute("PassoEditar")%>" />
			<input type="hidden" id="ParteTipo" name="ParteTipo" value="<%=request.getAttribute("ParteTipo")%>" />
			
			<input type="hidden" id="bensPartilhar" name="bensPartilhar" value="<%=request.getAttribute("bensPartilhar")%>" />
			<input type="hidden" id="penhora" name="penhora" value="<%=request.getAttribute("penhora")%>" />
			<input type="hidden" id="areaQueixaCrime" name="areaQueixaCrime" value="<%=request.getAttribute("areaQueixaCrime")%>" />
			<input type="hidden" id="qtdeFolhas" name="qtdeFolhas" value="<%=request.getAttribute("qtdeFolhas")%>" />
			<input type="hidden" id="quantidadeCorreio" name="quantidadeCorreio" value="<%=request.getAttribute("quantidadeCorreio")%>" />
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
			<input type="hidden" id="Id_GuiaEmissao" name="Id_GuiaEmissao" value="<%=request.getAttribute("Id_GuiaEmissao")%>" />
		
			<div id="divEditar" class="divEditar">
                <%
                List liTemp = (List)request.getAttribute("ListaGuiaItemDt");
				if( liTemp != null && liTemp.size() > 0 ) { %>
                <fieldset class="formEdicao">
                	<legend class="formEdicaoLegenda">Pr�via do C�lculo</legend>
                	<br />
                	
                	<%
                	if(guiaEmissaoConsultaDt != null && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt() != null && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId() != null && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getNumeroProcessoCompletoDt() != null) {
                	%>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Processo</legend>
                	
                		<div> N�mero </div>
                			<%
                				if(request.getAttribute("visualizarLinkProcesso") != null && (Boolean)request.getAttribute("visualizarLinkProcesso") && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().equals(EnumSistemaProcessoTJGO.projudi)) {
                			%>		                			
								<span><a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId()%>&PassoBusca=2"><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getNumeroProcessoCompletoDt().getNumeroCompletoProcesso()%></a></span> 
							<%
 								} else {
 							%>
								<span><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getNumeroProcessoCompletoDt().getNumeroCompletoProcesso()%></span> 
							<%
									}
								%>
						<div> Sistema </div>
					    <span><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().getValor()%></span>
                	</fieldset>
					<br/><br/>
					<%}%>
                	
                	<%if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getProcessoTipo() != null && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getProcessoTipo().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">   
				   		<legend>Classe</legend>
				   		
				       	<div></div>
				       	<span class="span1"><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getProcessoTipo() %></span>
					</fieldset>
					<br/><br/>
					<%} %>
                	
                	<br/>
                	
                	<%if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getValor() != null && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getValor().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">   
				   		<legend>Valor da A��o Base para C�lculo</legend>
				   		
				       	<div>R$</div>
				       	<span class="span1">
				       		<%if(guiaEmissaoCompletaDt.getNovoValorAcaoAtualizado() != null && guiaEmissaoCompletaDt.getNovoValorAcaoAtualizado().length() > 0 ) {%>
				       			<%=guiaEmissaoCompletaDt.getNovoValorAcaoAtualizado() %>
				       		<%}
				       		else {%>
				       			<%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getValor() %>
				       		<%}%>
				       	</span>
					</fieldset>
                	<br/><br/>
                	<%} %>
                	
                	<%if( (guiaEmissaoCompletaDt.getApelante() != null && guiaEmissaoCompletaDt.getApelado() != null) ||
                			(guiaEmissaoCompletaDt.getApelante() == null && guiaEmissaoCompletaDt.getApelado() != null) || 
                			(guiaEmissaoCompletaDt.getApelante() != null && guiaEmissaoCompletaDt.getApelado() == null) ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Recorrente/Apelante e Recorrido/Apelado</legend>
                		
                		<div style="width:140px;"> Recorrente/Apelante </div>
			       		<span class="span1"><%=(guiaEmissaoCompletaDt.getApelante()==null? "":guiaEmissaoCompletaDt.getApelante())%></span>
			       		
			       		<br /><br />
			       		
			       		<div style="width:140px;"> Recorrido/Apelado </div>
			       		<span class="span1"><%=(guiaEmissaoCompletaDt.getApelado()==null? "":guiaEmissaoCompletaDt.getApelado())%></span>
			       	
                	</fieldset>
                	<%}%>
                	
                	<%if( guiaEmissaoCompletaDt.getGuiaTipo() != null && guiaEmissaoCompletaDt.getGuiaTipo().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Tipo de Guia</legend>
                		
                		<div style="width:140px;"> Tipo de Guia </div>
			       		<span class="span1">
			       			<%=guiaEmissaoCompletaDt.getGuiaTipo()%>
			       		</span>
			       	
                	</fieldset>
                	<%} %>
                	
                	<%if( guiaEmissaoCompletaDt.getRateioCodigo() != null && guiaEmissaoCompletaDt.getRateioCodigo().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Rateio</legend>
                		
                		<div style="width:140px;"> Tipo Rateio </div>
			       		<span class="span1">
			       			<%=GuiaEmissaoNe.getNomeRateio(Funcoes.StringToInt(guiaEmissaoCompletaDt.getRateioCodigo()))%>
			       		</span>
			       	
                	</fieldset>
                	<%}%>
                	
                	<br />
                	
                	<%if( guiaEmissaoCompletaDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoCompletaDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {	%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Respons�vel pela Guia</legend>
                			
                			<div style="width:140px;"> Nome </div>
                			<span class="span1">
				       			<%=guiaEmissaoCompletaDt.getNomeProcessoParteResponsavelGuia()%> (100%)
			       			</span>
                		</fieldset>
                		
                		<br /><br />
                	<%} %>
                	
                	<%if( request.getAttribute("apresentarLinkGuiaComplementar") != null ) {%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Guia Complementar</legend>
                			
                			<div style="width:140px;"> Guia Complementar </div>
                			<span class="span1">
				       			<a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=request.getAttribute("apresentarLinkGuiaComplementar").toString()%>&Id_GuiaTipo=<%=GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR%>">
				       				Guia
				       			</a>
			       			</span>
                		</fieldset>
                		
                		<br /><br />
                	<%} %>
                	
                	<%if( guiaEmissaoCompletaDt.getNumeroGuiaCompleto() != null && guiaEmissaoCompletaDt.getNumeroGuiaCompleto().length() > 0 ) { %>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>N�mero Guia</legend>
                			
                			<div style="width:140px;"> N�mero Guia </div>
                			<span class="span1">
				       			<%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoCompletaDt.getNumeroGuiaCompleto())%>
			       			</span>
			       			
			       			<%if( guiaEmissaoCompletaDt.getId_Usuario() != null && guiaEmissaoCompletaDt.getId_Usuario().length() > 0 && guiaEmissaoCompletaDt.getUsuario() != null && guiaEmissaoCompletaDt.getUsuario().length() > 0 ) { %>
			       				<br /><br />
			       			
			       				<div style="width:140px;"> Emissor da Guia </div>
	                			<span class="span1">
					       			<%=guiaEmissaoCompletaDt.getUsuario()%>
				       			</span>
			       			<%} %>
                		</fieldset>
                		
                		<br /><br />
                	<%} %>
                	
                	<%if( guiaEmissaoCompletaDt.getId_GuiaStatus() != null && guiaEmissaoCompletaDt.getId_GuiaStatus().length() > 0 && guiaEmissaoCompletaDt.getGuiaStatus() != null ) {%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Status</legend>
                			
                			<%
               				String cor = "#CC6600"; //alaranjado
              				if( guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
              					cor = "#CC0000"; //vermelho
              				}
              				else {
              					if( guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO)) ) {
              						cor = "#66CC00"; //verde
              					}
              				}
              				%>
                			<span class="span1" style="color:<%=cor%>;">
                				<%=guiaEmissaoCompletaDt.getGuiaStatus() %>
			       			</span>
                		</fieldset>
                		
                		<br /><br />
                	<%} %>
                	
					<%
						if(request.getAttribute("oficialAdHoc") != null){
							OficialSPGDt oficial = (OficialSPGDt)request.getAttribute("oficialAdHoc");
					%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Locomo��o</legend>
                			
                			<div style="width:140px;">Oficial:</div>
                			<span class="span1">
                				<%=oficial.getCodigoOficial()%>&nbsp;:&nbsp;&nbsp;
				       			<%=oficial.getNomeOficial()%>
			       			</span>
                		</fieldset>
                		
                		<br /><br />
                	<%}	%>
                	
                	<%if( request.getSession().getAttribute("ListaGuiasRateio") == null ) { %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Itens de Custa</legend>
	                		
							<table id="Tabela" class="Tabela">
								<thead>
									<tr>
										<th>Descri��o(C�d.Regimento)</th>
										<th>C�digo</th>
										<th>Quantidade</th>
										<th>Valor</th>
									</tr>
								</thead>
								<tbody id="tabListaEscala">
									<%
									GuiaItemDt objTemp = null;
									for(int i = 0 ; i< liTemp.size();i++) {
										objTemp = (GuiaItemDt)liTemp.get(i);%>
										<tr>
											<td>
												<%=objTemp.getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getCustaDt().getCodigoRegimentoTratamento() %>
											</td>
											
											<td align="center">
												<%=objTemp.getCustaDt().getCodigoArrecadacao() %>
											</td>
											
											<td align="center">
												<%=Funcoes.FormatarDecimal(objTemp.getQuantidade()) %>
											</td>
											
											<td align="right">
												R$ <%=Funcoes.FormatarDecimal( objTemp.getValorCalculado() ) %>
											</td>
										</tr>
									<%
									}
									%>
								</tbody>
								<tfoot>
									<tr>
										<td align="center" colspan="3">
											<label class="formEdicaoLabel"><b>Total da Guia</b></label><br>
										</td>
										<td>
											<label class="formEdicaoLabel">
												<b>R$ <%= Funcoes.FormatarDecimal( request.getAttribute("TotalGuia").toString() ) %></b>
											</label><br>
										</td>
									</tr>
								</tfoot>
							</table>
						</fieldset>
					<%
					}
                	else {
                		List auxListaRateio = (List) request.getSession().getAttribute("ListaGuiasRateio");
                		List auxListaTotais = (List) request.getSession().getAttribute("ListaTotalGuiaRateio");
                		List auxListaNomeParteGuia = (List) request.getSession().getAttribute("ListaNomeParteGuia");
                		List auxListaNomePartePorcentagemGuia = (List) request.getSession().getAttribute("ListaNomePartePorcentagemGuia");
						
                		for( int k = 0; k < auxListaRateio.size(); k++ ) {
                			liTemp = (List) auxListaRateio.get(k);
					%>
							<br /><br />
							<hr />
							<%if( auxListaNomeParteGuia != null && auxListaNomePartePorcentagemGuia != null ) { %>
							<fieldset id="VisualizaDados" class="VisualizaDados">
                				<legend><%=(k+1)%>� Guia</legend>
                			
                				<div style="width:140px;"> Parte(Porcentagem) </div>
                				<span class="span1">
				       				<%=auxListaNomeParteGuia.get(k).toString().split(":")[0]%>
				       				(<%=auxListaNomePartePorcentagemGuia.get(k).toString()%>%)
			       				</span>
                			</fieldset>
                			<br />
                			<% } %>
                		
							<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend>Itens de Custa</legend>
		                		
								<table id="Tabela" class="Tabela">
									<thead>
										<tr>
											<th>Descri��o(C�d.Regimento)</th>
											<th>C�digo</th>
											<th>Quantidade</th>
											<th>Valor</th>
										</tr>
									</thead>
									<tbody id="tabListaEscala">
										<%
										GuiaItemDt objTemp = null;
										for(int i = 0 ; i< liTemp.size();i++) {
											objTemp = (GuiaItemDt)liTemp.get(i);%>
											<tr>
												<td><%=objTemp.getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getCustaDt().getCodigoRegimentoTratamento() %></td>
												<td align="center"><%=objTemp.getCustaDt().getCodigoArrecadacao() %></td>
												<td align="center"><%=Funcoes.FormatarDecimal(objTemp.getQuantidade()) %></td>
												<td align="right">R$ <%=Funcoes.FormatarDecimal( objTemp.getValorCalculado() ) %></td>
											</tr>
										<%
										}
										%>
									</tbody>
									<tfoot>
										<tr>
											<td align="center" colspan="3">
												<label class="formEdicaoLabel"><b>Total da Guia</b></label><br>
											</td>
											<td>
												<label class="formEdicaoLabel">
													<b>R$ <%= Funcoes.FormatarDecimal( auxListaTotais.get(k).toString() ) %></b>
												</label><br>
											</td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
							
							<br /><br />
					<%	
                		}
					}%>
					
					<br /><br />
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend><img src="./imagens/22x22/ico_informacao.png" alt="Informa��o" /> Informa��es Sobre Pagamentos</legend>
					
						<em>
							1- Ao realizar o pagamento da guia no Banco do Brasil, a confirma��o do pagamento pode* chegar em at� 15 minutos.
							2 - Ao realizar o pagamento nos outros Bancos conveniados** Caixa Econ�mica e Ita�, a confirma��o pode chegar em at� 72*** horas.
			                <p />
			                * Este tempo pode ser maior em casos de problemas externos fora de controle do Tribunal de Justi�a do Estado de Goi�s e do Banco do Brasil, por exemplo como problemas de conex�o de internet com as empresas telef�nicas.
			                <br />
			                ** Os Bancos conveniados para receber as guias do Tribunal de Justi�a do Estado de Goi�s s�o: Banco do Brasil, Ita� e Caixa Econ�mica.
			                <br />
			                *** O Tribunal de Justi�a est� trabalhando para que as confirma��es dos outros Bancos chegue tamb�m em poucos minutos. Agradecemos a compreens�o.
						</em>
					</fieldset>
					
                </fieldset>
                <%}%>
                
                <br /><br />
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                	<%if( request.getAttribute("visualizarBotaoVoltar") != null && (Boolean)request.getAttribute("visualizarBotaoVoltar") ) { %>
                    <button name="imgVoltar" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" >
                    	<img src="./imagens/imgVoltarPequena.png" alt="Voltar" />
                    	Voltar
                    </button>
                    <%} %>
                    
                    <%if( request.getAttribute("visualizarBotaoSalvarGuia") != null && (Boolean)request.getAttribute("visualizarBotaoSalvarGuia") ) { %>
	                    <button name="imgEmitirGuia" value="Emitir Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');AlterarValue('PassoEditar','<%=Configuracao.Salvar%>');" >
		                  	Emitir Guia
		                </button>
	                <%} %>
                    
                    <%if( request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") != null && (Boolean)request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") ) { %>
                    	
                    		<%if( (List) request.getSession().getAttribute("ListaGuiasRateio") == null ) {%>
			                    <button name="imgEmitirGuia" value="Emitir Boleto" type="button" onclick="MostrarOcultar('divEmitirBoleto');Ocultar('divPagamentoOnlineBancos');" >
			                    	<img src="./imagens/icone_boleto.gif" height="16" alt="Emitir Boleto" />
			                    	Imprimir Boleto
			                    </button>
			               	
			                    
		                    <%}%>
                    	
                    <%} %>
                    
                    <%if( request.getAttribute("emitirGuiaLocomocaoComplementar") != null && request.getAttribute("emitirGuiaLocomocaoComplementar").toString().equals(GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_LOC_COMPLEMENTAR) ) { %>
                		<a href="GuiaLocomocaoComplementar?PaginaAtual=<%=Configuracao.Novo%>&Id_GuiaEmissaoPaga=<%=guiaEmissaoCompletaDt.getId()%>">
	                		<button name="imgEmitirGuiaComplementar" type="button" value="Emitir Guia Complementar" >
			                  	Emitir Guia Complementar
			                </button>
		                </a>
                	<%} %>
                	
                	<%if( request.getAttribute("emitirGuiaInicialLocomocaoComplementar") != null && request.getAttribute("emitirGuiaInicialLocomocaoComplementar").toString().equals(GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_INICIAL_LOC_COMPLEMENTAR) ) { %>
                		<a href="GuiaInicialLocomocaoComplementar?PaginaAtual=<%=Configuracao.Novo%>&Id_GuiaEmissaoPaga=<%=guiaEmissaoCompletaDt.getId()%>">
	                		<button name="imgEmitirGuiaInicialLocComplementar" type="button" value="Emitir Guia Inicial de Locomo��o Complementar" >
			                  	Emitir Guia Inicial de Locomo��o Complementar
			                </button>
		                </a>
                	<%} %>
                </div>
                
                <br /><br />
                
                <%if( (Boolean)request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") ) { %>
	                <div id="divEmitirBoleto" class="DivInvisivel">
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Deseja Emitir o Boleto?</legend>
	                			
		                	<fieldset class="fieldsetEndereco">
		                		<legend>Informa��es</legend>
		                	
		                		<em>
		                		1- Mesmo depois de imprimir este Boleto e n�o for poss�vel realizar o pagamento em algum Banco conveniado*, voc� pode utilizar a op��o de pagamento utilizando o n�mero da Guia presente no Boleto.
		                	
		                		<p />
		                		* Os Bancos Conveniados do Tribunal do Justi�a do Estado de Goi�s s�o: Banco do Brasil, Caixa Econ�mica Federal e Banco Ita�.
		                		</em>
		                	</fieldset>
		                    	
		                	<br />
		                
		                	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
		                    	<button name="imgEmitirGuia" value="Boleto" onclick="Ocultar('divBotoesCentralizados');Ocultar('divEmitirBoleto');AlterarValue('PaginaAtual','<%=Configuracao.Imprimir %>');AlterarValue('PassoEditar','<%=Configuracao.Imprimir %>');" >
			                    	<img src="./imagens/icone_boleto.gif" height="16" alt="Boleto" />
			                    	Boleto
			                    </button>
			                </div>
			                
			                <br /><br />
			                
	                    </fieldset>
	                </div>
	                
	                <div id="divPagamentoOnlineBancos" class="DivInvisivel">
	               		<fieldset id="VisualizaDados" class="VisualizaDados">
	               			<legend>Escolha o Banco de sua Prefer�ncia</legend>
	               			
	               			<fieldset class="fieldsetEndereco">
	               				<legend>Informa��es</legend>
	               				
	               				<em>
	               				1- Op��o v�lida apenas para clientes dos Bancos listados que tenham acesso ao InternetBank. Estes s�o os Bancos conveniados ao Tribunal de Justi�a do Estado de Goi�s.
	               				<p />
	               				2- Sua senha � de uso pessoal e intransfer�vel.
	               				<p />
	               				3- Lembre-se sempre de guardar os comprovantes de suas transa��es banc�rias.(Salve o PDF e evite imprimir folhas desnecessariamente)
	               				<p />
	               				4- Apartir do momento que for encaminhado para o Banco de sua prefer�ncia, esta guia ficar� aguardando o pagamento at� a data de vencimento.
	               				<p />
	               				5- Ap�s o Vencimento esta Guia ser� cancelada e n�o aceito mais para pagamento.
	               				<p />
	               				6- D�vidas com pagamento entre em contato nos seguintes canais de atendimento:
	               				<br />
	               				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Banco do Brasil: 0800-7290500
	               				<br />
	               			</fieldset>
	               			
	               			<br /><br />
							
							<span>
								<button name="imgBancoBrasil" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7 %>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6 %>');">
									<img src="./imagens/bancobrasil.png" alt="Pagamento via Banco do Brasil" />
									<br />
									Banco do Brasil
								</button>
							</span>
							
							<span>
								<button type="button" name="imgItau" onclick="alert('Banco sem Conv�nio de Pagamento on-line.');">
									<img src="./imagens/itau.gif" width="30" height="40" alt="Pagamento via Banco Ita�" />
									<br />
									Banco Ita�
								</button>
							</span>
							
							<span>
								<button type="button" name="imgCEF" onclick="alert('Banco sem Conv�nio de Pagamento on-line.');">
									<img src="./imagens/cef.png" width="90" height="40" alt="Pagamento via Caixa Econ�mica" />
									<br />
									Caixa Econ�mica
								</button>
							</span>
							
						</fieldset>
					</div>
                <%} %>
                
			</div>
			</form>
		<br /><br />
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>