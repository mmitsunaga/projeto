<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Consultar Guia para Desvincular de Processo</title>
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
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
	  	<%
		GuiaEmissaoDt GuiaEmissaoDt_A = (GuiaEmissaoDt)request.getAttribute("GuiaEmissaoDt_A");
		GuiaEmissaoDt guiaEmissaoDtConsulta_A_SPG = (GuiaEmissaoDt)request.getAttribute("guiaEmissaoDtConsulta_A_SPG");
		List<InfoRepasseSPGDt> listaInfoRepasseSPGDt = (List<InfoRepasseSPGDt>)request.getAttribute("listaInfoRepasseSPGDt");
		List<LocomocaoSPGDt> listaLocomocaoMandadoSPGDt = (List<LocomocaoSPGDt>)request.getAttribute("listaLocomocaoMandadoSPGDt");
		List<LocomocaoSPGDt> listaLocomocaoSPGDt = (List<LocomocaoSPGDt>)request.getAttribute("listaLocomocaoSPGDt");
		%>
	  	
		<div class="area">
			<h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Consultar Guia para Desvincular de Processo</h2>
		</div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="DesvincularGuiaProcesso" id="DesvincularGuiaProcesso">
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input id="numeroGuiaDesvincular" name="numeroGuiaDesvincular" type="hidden" value="<%=request.getAttribute("numeroGuiaDesvincular")%>">
			<input id="motivo" name="motivo" type="hidden" value="<%=request.getAttribute("motivo")%>">
			
			<input id="numeroGuiaAlterarStatus" name="numeroGuiaAlterarStatus" type="hidden" value="<%=request.getAttribute("numeroGuiaAlterarStatus")%>">
			<input id="novoStatusGuia" name="novoStatusGuia" type="hidden" value="<%=request.getAttribute("novoStatusGuia")%>">
			<input id="motivoAlteracao" name="motivoAlteracao" type="hidden" value="<%=request.getAttribute("motivoAlteracao")%>">
			
			<input id="Id_AreaDistribuicao" name="Id_AreaDistribuicao" type="hidden" value="<%=request.getAttribute("Id_AreaDistribuicao")%>">
			<input id="Id_Comarca" name="Id_Comarca" type="hidden" value="<%=request.getAttribute("Id_Comarca")%>">
			<input id="motivoAlteracaoComarca" name="motivoAlteracaoComarca" type="hidden" value="<%=request.getAttribute("motivoAlteracaoComarca")%>">
			<input id="numeroGuiaAlterarComarca" name="numeroGuiaAlterarComarca" type="hidden" value="<%=request.getAttribute("numeroGuiaAlterarComarca")%>">
			
			<div id="divEditar" class="divEditar">
			
				<%if( GuiaEmissaoDt_A != null ) {
					
					boolean possuiLocomocao = Funcoes.guiaItemPossuiLocomocao(GuiaEmissaoDt_A.getListaGuiaItemDt());
					%>
					<br />
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend style="background-color: silver;">Dados da Guia no PJD: <%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt_A.getNumeroGuiaCompleto())%></legend>
	                		
	                		<div>Número da Guia</div>
	                		<span><%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt_A.getNumeroGuiaCompleto())%></span>
	                		
	                		<div>Tipo da Guia</div>
							<span><%=GuiaEmissaoDt_A.getGuiaModeloDt().getGuiaTipo()%></span>
							
							<br />
							<br />
							<br />
								
							<%if( GuiaEmissaoDt_A.getNaturezaSPG() != null && !GuiaEmissaoDt_A.getNaturezaSPG().isEmpty() ) { %>
								<div>Natureza SPG Informada na Guia</div>
								<span>									
										<%=GuiaEmissaoDt_A.getNaturezaSPG()%>									
								</span>
							<%} %>
							
							<div>Valor da Causa Informada na Guia</div>
							<span>R$ <%=Funcoes.FormatarDecimal(GuiaEmissaoDt_A.getValorAcao()) %></span>
							
							<br />
							<br />
							<br />
							
							<%
               				String cor = "#CC6600"; //alaranjado
              				if( GuiaEmissaoDt_A.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
              					cor = "#CC0000"; //vermelho
              				}
              				else {
              					if( GuiaEmissaoDt_A.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO)) ) {
              						cor = "#66CC00"; //verde
              					}
              				}
              				%>
							<div>Status</div>
				       		<span style="color:<%=cor%>;">
				       			<%=GuiaEmissaoDt_A.getGuiaStatus() %>
				       		</span>
				       		
				       		<div>Data Emissão</div>
							<span><%=Funcoes.TelaData(GuiaEmissaoDt_A.getDataEmissao()) %></span>
							
							<br />
							<br />
							<br />
							
							<div>Data Recebimento</div>
							<span><%=Funcoes.TelaData(GuiaEmissaoDt_A.getDataRecebimento()) %></span>
	                		
	                		<br />
	                		<br />
	                		<br />
	                		
	                		<div>Id Área de Distribuição</div>
	                		<span>
	                		<%if( GuiaEmissaoDt_A.getId_AreaDistribuicao() != null ) {%>
	                			<%=GuiaEmissaoDt_A.getId_AreaDistribuicao()%>
	                		<%}%>
	                		</span>
	                		<br />
	                		<br />
	                		<br />
	                		
	                		<div>Classe Informada na Guia</div>
	                		<span>
	                		<%if( GuiaEmissaoDt_A.getId_ProcessoTipo() != null && GuiaEmissaoDt_A.getProcessoTipo() != null ) {%>
	                			<%=GuiaEmissaoDt_A.getId_ProcessoTipo()%> - <%=GuiaEmissaoDt_A.getProcessoTipo()%>
	                		<%} %>
	                		</span>
	                		
	                		<br />
	                		<br />
	                		<br />
	                		
	                		<fieldset id="VisualizaDados" class="VisualizaDados">
	                			<legend>Dados do Processo</legend>
	                			
	                			<%if( GuiaEmissaoDt_A.getProcessoDt() != null ) { %>
	                				
	                				<div>Número do Processo</div>
	                				<span><%=GuiaEmissaoDt_A.getProcessoDt().getProcessoNumeroCompleto()%></span>
	                				
	                				<div>Classe do Processo</div>
	                				<span><%=GuiaEmissaoDt_A.getProcessoDt().getServentia()%></span>
	                				
	                				<br />
									<br />
									<br />
	                				
	                				<div>Valor da Causa</div>
	                				<span><%=Funcoes.FormatarDecimal(GuiaEmissaoDt_A.getProcessoDt().getValor())%></span>
	                				
	                				<div>Valor da Condenação</div>
	                				<span><%=Funcoes.FormatarDecimal(GuiaEmissaoDt_A.getProcessoDt().getValorCondenacao())%></span>
	                				
	                				<br />
	                				<br />
									<br />
	                			<%}
	                			else {%>
	                				<label>Guia sem vinculação com processo.</label>
	                			<%} %>
	                			
	                		</fieldset>
	                		
	                		<br />
	                		<br />
	                		<br />
	                		
	                		<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend>Itens da Guia</legend>
		                		
								<table id="Tabela" class="Tabela">
									<thead>
										<tr>
											<th>Nº</th>
											<th>Descrição(Cód.Regimento)</th>
											<%if(possuiLocomocao || GuiaEmissaoDt_A.possuiLocomocao() ) { %>
												<td>Bairro</td>
											<% } %>
											<th>Código</th>
											<th>Quantidade</th>
											<%if( GuiaEmissaoDt_A.isLocomocaoComplementar() ) { %>
												<td>Recolhido</td>
												<td>Original</td>											
												<th>Complemento</th>
											<% } else { %>
												<th>Valor</th>
											<% } %>
										</tr>
									</thead>
									<tbody id="tabListaEscala">
										<%
										GuiaItemDt objTemp = null;
										if( GuiaEmissaoDt_A != null && GuiaEmissaoDt_A.getListaGuiaItemDt() != null ) {
											for(int i = 0 ; i< GuiaEmissaoDt_A.getListaGuiaItemDt().size();i++) {
												objTemp = (GuiaItemDt)GuiaEmissaoDt_A.getListaGuiaItemDt().get(i);%>
												<tr>
													<td align="center">
														<%=(i + 1)%>
													</td>
													<td>
														<%=objTemp.getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getCustaDt().getCodigoRegimentoTratamento() %>
													</td>
													
													<%if(possuiLocomocao || GuiaEmissaoDt_A.possuiLocomocao() ) { %>
														<td>
														 	<%if( objTemp.getLocomocaoDt() != null && objTemp.getLocomocaoDt().getBairroDt() != null ) { %>
																<%=objTemp.getLocomocaoDt().getBairroDt().getBairro() %>
															<% } %>
														</td>
													<% } %>
													
													<td align="center">
														<%=objTemp.getCustaDt().getCodigoArrecadacao() %>
													</td>
													
													<td align="center">
														<%=objTemp.getQuantidade() %>
													</td>
													
													<%if( GuiaEmissaoDt_A.isLocomocaoComplementar() ) { %>
														<td align="right">
															R$ <%=Funcoes.FormatarDecimal( objTemp.getValorAbatimento() ) %>
														</td>
														<td align="right">
															R$ <%=Funcoes.FormatarDecimal( objTemp.getValorCalculadoOriginal() ) %>
														</td>												
													<% } %>
													
													<td align="right">
														R$ <%=Funcoes.FormatarDecimal( objTemp.getValorCalculado() ) %>
													</td>
												</tr>
											<%
											}
										}
										%>
									</tbody>
									<tfoot>
										<tr>
											<%
												int colspan = 4;
												if( possuiLocomocao || GuiaEmissaoDt_A.isGuiaLocomocao() ) colspan += 1;
												if( GuiaEmissaoDt_A.isLocomocaoComplementar() ) colspan += 2;
											%>
											<td align="center" colspan="<%=colspan%>">
												<label class="formEdicaoLabel"><b>Total da Guia</b></label><br>
											</td>
											<td>
												<label class="formEdicaoLabel">
													<b>R$ <%= Funcoes.FormatarDecimal( GuiaEmissaoDt_A.getValorTotalGuiaDouble()) %></b>
												</label>
												<br>
											</td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
	                		
	                </fieldset>
                <%} %>
                
                <br />
                <br />
                
                <%if( guiaEmissaoDtConsulta_A_SPG != null ) { %>
	                <fieldset id="VisualizaDados" class="VisualizaDados">
		                <legend style="background-color: silver;">Dados da Guia no SPG: <%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtConsulta_A_SPG.getNumeroGuiaCompleto())%></legend>
		                
		                <div>Número da Guia</div>
				       	<span class="span1">
				       		<%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtConsulta_A_SPG.getNumeroGuiaCompleto())%>
				       	</span>
				       	
	       				<div>Status</div>
				       	<span class="span1">
				       		<%if(guiaEmissaoDtConsulta_A_SPG.getGuiaStatus() != null) { %>
				       			<%=guiaEmissaoDtConsulta_A_SPG.getGuiaStatus()%>
				       		<%} %>
				       	</span>
		                
		                <br />
		                <br />
						<br />
		                
	       				<div>Data de Emissão</div>
				       	<span class="span1">
				       		<%if(guiaEmissaoDtConsulta_A_SPG.getDataEmissao() != null) { %>
			       				<%=guiaEmissaoDtConsulta_A_SPG.getDataEmissao()%>
			       			<%} %>
				       	</span>
				       	
	       				<div>Data de Vencimento</div>
				       	<span class="span1">
				       		<%if(guiaEmissaoDtConsulta_A_SPG.getDataVencimento() != null) { %>
			       				<%=guiaEmissaoDtConsulta_A_SPG.getDataVencimento()%>
			       			<%} %>
				       	</span>
				       	
				       	<br />
				       	<br />
						<br />
		                
	       				<div>Data de Apresentação</div>
				       	<span class="span1">
				       		<%if(guiaEmissaoDtConsulta_A_SPG.getDataApresentacaoSPG() != null) { %>
				       			<%=guiaEmissaoDtConsulta_A_SPG.getDataApresentacaoSPG()%>
				       		<%} %>
					    </span>
         
						<div>Situação do Pagamento</div>
						<span class="span1">
							<%if(guiaEmissaoDtConsulta_A_SPG.getSituacaoPagamentoSPG() != null) { %>
								<%=guiaEmissaoDtConsulta_A_SPG.getSituacaoPagamentoSPG()%>
							<%} %>
						</span>
         
				       	<br />
				       	<br />
						<br />
		                
						<div style="width: 200px;">Número Processo PJD</div>
						<span class="span1">
							<%if(guiaEmissaoDtConsulta_A_SPG.getNumeroProcesso() != null) { %>
								<%=guiaEmissaoDtConsulta_A_SPG.getNumeroProcesso()%>
							<%} %>
						</span>
         
						<div style="width: 200px;">Número Processo SPG</div>
						<span class="span1">
							<%if(guiaEmissaoDtConsulta_A_SPG.getNumeroProcessoSPG() != null) { %>
								<%=guiaEmissaoDtConsulta_A_SPG.getNumeroProcessoSPG()%>
							<%} %>
						</span>
         
				       	<br />
				       	<br />
						<br />
		                
						<div>Código da Serventia na Guia</div>
						<span class="span1">
							<%if(guiaEmissaoDtConsulta_A_SPG.getInfoLocalCertidaoSPG() != null) { %>
								<%=guiaEmissaoDtConsulta_A_SPG.getInfoLocalCertidaoSPG()%>
							<%} %>
						</span>
						
		            </fieldset>
		            
		            <br />
                	<br />
		            
		            <fieldset id="VisualizaDados" class="VisualizaDados">
		                <legend style="background-color: silver;">Dados de Repasses no SPG da Guia: <%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtConsulta_A_SPG.getNumeroGuiaCompleto())%></legend>
		                
		                <div>Atenção</div>
						<span class="span1">
							Para maiores informações de repasses consultar no SPG.
						</span>
						
						<br />
						<br />
						<br />
		                
		                <%
		                if( listaInfoRepasseSPGDt != null && listaInfoRepasseSPGDt.size() > 0 ) {
			                for(InfoRepasseSPGDt infoRepasseSPGDt: listaInfoRepasseSPGDt) { %>
			                
			                	<div>Código da Escrivania</div>
								<span class="span1">
									<%if(infoRepasseSPGDt.getCodgEscrivania() != null) { %>
										<%=infoRepasseSPGDt.getCodgEscrivania()%>
									<%} %>
								</span>
		         
								<div>Percentual de Repasse</div>
								<span class="span1">
									<%if(infoRepasseSPGDt.getPercRepasse() != null) { %>
										<%=infoRepasseSPGDt.getPercRepasse()%> %
									<%} %>
								</span>
		         				
		         				<br />
				       			<br />
								<br />
								
								<div>Data do Repasse</div>
								<span class="span1">
									<%if(infoRepasseSPGDt.getDataRepasse() != null) { %>
										<%=infoRepasseSPGDt.getDataRepasse()%>
									<%} %>
								</span>
		                	
		                	<%}
		                }%>
		                
		            </fieldset>
		            
		            <br />
                	<br />
		            
		            <fieldset id="VisualizaDados" class="VisualizaDados">
		                <legend style="background-color: silver;">Locomoções no SPG para essa Guia: <%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtConsulta_A_SPG.getNumeroGuiaCompleto())%></legend>
		                
		                <%
		                if( listaLocomocaoSPGDt != null && listaLocomocaoSPGDt.size() > 0 ) {
			                for(LocomocaoSPGDt locomocaoSPGDt: listaLocomocaoSPGDt) { %>
			                	
			                	<div>Código do Bairro</div>
								<span class="span1">
									<%if(locomocaoSPGDt.getCodigoBairro() != null) { %>
										<%=locomocaoSPGDt.getCodigoBairro()%>
									<%} %>
								</span>
		         
								<div>Código do Município</div>
								<span class="span1">
									<%if(locomocaoSPGDt.getCodigoMunicipio() != null) { %>
										<%=locomocaoSPGDt.getCodigoMunicipio()%>
									<%} %>
								</span>
								
								<br />
						       	<br />
								<br />
		         
								<div>Quantidade</div>
								<span class="span1">
									<%if(locomocaoSPGDt.getQuantidade() != null) { %>
										<%=locomocaoSPGDt.getQuantidade()%>
									<%} %>
								</span>
		                	
		                	<%}
		                }%>
		                
		            </fieldset>
		            
		            <br />
                	<br />
		            
		            <fieldset id="VisualizaDados" class="VisualizaDados">
		                <legend style="background-color: silver;">Números de Mandados no SPG para essa Guia: <%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtConsulta_A_SPG.getNumeroGuiaCompleto())%></legend>
		                
		                <%
		                if( listaLocomocaoMandadoSPGDt != null && listaLocomocaoMandadoSPGDt.size() > 0 ) {
			                for(LocomocaoSPGDt locomocaoSPGDt: listaLocomocaoMandadoSPGDt) { %>
			                	
			                	<div>Número do Mandado</div>
								<span class="span1">
									<%if(locomocaoSPGDt.getNumeroMandado() > 0L) { %>
										<%=locomocaoSPGDt.getNumeroMandado()%>
									<%} %>
								</span>
			                	
			                <%}
		                }%>
		                
		            </fieldset>
		            
	            <%} else {%>
<!--                 	<fieldset id="VisualizaDados" class="VisualizaDados"> -->
<!--                 		<legend style="background-color: silver;">A guia não foi encontrada no SPG.</legend> -->
<!--                 	</fieldset> -->
                <%} %>
	            
	            <%if( GuiaEmissaoDt_A != null || guiaEmissaoDtConsulta_A_SPG != null ) {%>
	            	<br />
	            	<br />
	            	<br />
	            	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<button name="imgDesvincularGuia" value="Desvincular Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>');" >
							Alterar Guia
						</button>
					</div>
                <%}%>
			</div>
			
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
		
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>