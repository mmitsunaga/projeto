<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="GuiaEmissaoDtInicial" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<%
if( GuiaEmissaoDtInicial != null 
	&& (GuiaEmissaoDtInicial.getId_Comarca() != null && !GuiaEmissaoDtInicial.getId_Comarca().isEmpty() ) 
	&& (GuiaEmissaoDtInicial.getId_AreaDistribuicao() != null && !GuiaEmissaoDtInicial.getId_AreaDistribuicao().isEmpty() )
	&& (GuiaEmissaoDtInicial.getGuiaModeloDt().getId_GuiaTipo() != null && ( GuiaEmissaoDtInicial.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) || GuiaEmissaoDtInicial.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU) ))) {
	GuiaEmissaoDt = GuiaEmissaoDtInicial;
}
GuiaCertidaoGeralDt guiaCertidaoGeralDt = null;
if( request.getSession().getAttribute("guiaCertidaoGeralDt") != null ) {
	guiaCertidaoGeralDt = (GuiaCertidaoGeralDt)request.getSession().getAttribute("guiaCertidaoGeralDt");
}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Pré-Cálculo</title>
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
			
			<input type="hidden" id="taxaJudiciariaServicoCertidao" name="taxaJudiciariaServicoCertidao" value="<%=request.getParameter("taxaJudiciariaServicoCertidao") %>" />
			
			<input type="hidden" id="validaPagamento" name="validaPagamento" />
			<input type="hidden" id="numeroGuiaCompletoValidarSPG" name="numeroGuiaCompletoValidarSPG" />
			
			<input id="PassoConsultar" name="PassoConsultar" type="hidden" value="<%=request.getAttribute("PassoConsultar")%>">
		
			<div id="divEditar" class="divEditar">
                <%
                List liTemp = (List)request.getAttribute("ListaGuiaItemDt");
                if( liTemp != null && liTemp.size() > 0 ) {
                	liTemp = GuiaEmissaoDt.getListaGuiaItemDtAgrupadosImpressao(liTemp);                	
                %>
                <fieldset class="formEdicao">
                	
                	<legend class="formEdicaoLegenda">Prévia do Cálculo</legend>
                	
                	<%if( GuiaEmissaoDt.getTipoGuiaReferenciaDescontoParcelamento() != null && !GuiaEmissaoDt.getTipoGuiaReferenciaDescontoParcelamento().isEmpty() && GuiaEmissaoDt.getGuiaEmissaoDtReferencia() != null ) {
                		
                		%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                		<%
                		
						if( GuiaEmissaoDt.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_PARCELADA) ) {
							%>
							<legend>Guia Pertence a um Parcelamento</legend>
							
							<div>Guia de Referência</div>
							<span><%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt.getGuiaEmissaoDtReferencia().getNumeroGuiaCompleto())%></span>
							
							<br /><br /><br />
							
							<div>Quantidade de Parcelas</div>
							<span><%=GuiaEmissaoDt.getQuantidadeParcelas()%></span>
							
							<br /><br /><br />
							
							<div>Parcela Atual</div>
							<span><%=GuiaEmissaoDt.getParcelaAtual()%></span>
							<%
						}
						else {
							if( GuiaEmissaoDt.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO) ) {
								%>
								<legend>Guia Gerada Com Desconto</legend>
								
								<div>Guia de Referência</div>
								<span><%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt.getGuiaEmissaoDtReferencia().getNumeroGuiaCompleto())%></span>
								
								<br /><br /><br />
								
								<div>Percentual de Desconto</div>
								<span><%=GuiaEmissaoDt.getPorcentagemDesconto()%> %</span>
								<%
							}
						}
                		
                		%>
                		
                			<div>Data de Vencimento</div>
                			<span><%=Funcoes.TelaData(GuiaEmissaoDt.getDataVencimento())%></span>
                			
                			<br /><br />
                		
                		</fieldset>
                		<%
                		
					}%>
					
					<%
                	if( GuiaEmissaoDt != null && GuiaEmissaoDt.getGuiaEmissaoDtPrincipal() != null ) {
                	%>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Guia Principal de Referência utilizada para Complementar</legend>
						
						<div>Número da Guia</div>
						<span><%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt.getGuiaEmissaoDtPrincipal().getNumeroGuiaCompleto())%></span>
						
						<div>Tipo da Guia</div>
						<span><%=GuiaEmissaoDt.getGuiaModeloDt().getGuiaTipo()%></span>
						
                	</fieldset>
					<%}%>
					
                	<%
                	if( processoDt != null && processoDt.getId_Processo() != null && processoDt.getProcessoNumero() != null && processoDt.getProcessoNumero().length() > 0 ) {
                	%>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Processo</legend>
	                	
	                		<div>Número</div>
							<span>
								<%if( request.getAttribute("tempRetornoBuscaProcesso") != null && request.getAttribute("visualizarLinkProcesso") != null && (Boolean)request.getAttribute("visualizarLinkProcesso") ) { %>
									<a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2">
								<%} %>
									<%=processoDt.getProcessoNumero()%>
								<%if( request.getAttribute("tempRetornoBuscaProcesso") != null && request.getAttribute("visualizarLinkProcesso") != null && (Boolean)request.getAttribute("visualizarLinkProcesso") ) { %>
									</a>
								<%} %>
							</span>
							
							<%if( request.getAttribute("visualizarDadosProcesso") != null && (Boolean)request.getAttribute("visualizarDadosProcesso") ) { %>
								<br />
								
								<div>Valor Causa</div>
								<span>
									<%=processoDt.getValor()%>
								</span>
								
								<br />
								
								<div>Classe</div>
								<span>
									<%=processoDt.getProcessoTipo()%>
								</span>
							<%} %>
	                	</fieldset>
					<%} 
                	
                	if( GuiaEmissaoDt != null && GuiaEmissaoDt.getProcessoDt() != null && GuiaEmissaoDt.getProcessoDt().getId_Processo() != null && GuiaEmissaoDt.getProcessoDt().getProcessoNumero() != null && GuiaEmissaoDt.getProcessoDt().getProcessoNumero().length() > 0 ) { %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Processo</legend>
	                	
	                		<div>Número</div>
							<span>
								<%=GuiaEmissaoDt.getProcessoDt().getProcessoNumero()%>
							</span>
	                	</fieldset>
	                	<% if (GuiaEmissaoDt.getProcessoDt().getProcessoTipo() != null && GuiaEmissaoDt.getProcessoDt().getProcessoTipo().length() > 0) { %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">   
					   		<legend>Classe do Processo</legend>
					   		
					       	<div></div>
					       	<span class="span1"><%=GuiaEmissaoDt.getProcessoDt().getProcessoTipo() %></span>
						</fieldset>
						<% } %>
					<% } %>
					
					<%if(ProcessoCadastroDt != null && ProcessoCadastroDt.getProcessoTipo() != null && ProcessoCadastroDt.getProcessoTipo().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">   
				   		<legend>Classe do Processo</legend>
				   		
				       	<div></div>
				       	<span class="span1"><%=ProcessoCadastroDt.getProcessoTipo() %></span>
					</fieldset>
					<% } %>
					
					<%if( GuiaEmissaoDt.getNovoValorAcaoAtualizado() != null && GuiaEmissaoDt.getNovoValorAcaoAtualizado().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">   
				   		<legend>Valor Base Cálculo da Guia</legend>
				   		
				       	<div>R$</div>
				       	<span class="span1">
				       		<%=Funcoes.FormatarDecimal(GuiaEmissaoDt.getNovoValorAcaoAtualizado()) %>
				       	</span>
					</fieldset>
                	<%} %>
                	                	
<!-- ********************************************************************************************************************************************************************************************************************************************	 -->
<!-- *********************** GUIA DE CERTIDÃO NARRATIVA/INTERDIÇÃO/PRÁTICA FORENTESE ******** INFORMAÇÕES DO REQUERENTE *************************************************************************************************************************	 -->
<!-- ********************************************************************************************************************************************************************************************************************************************	 -->
                	
					<!-- IF - Para Mostrar ou Não as Informações do Requerente. -->
                	<% if(guiaCertidaoGeralDt != null && GuiaEmissaoDt.getId_GuiaTipo() != null && !GuiaEmissaoDt.getId_GuiaTipo().isEmpty() ) { %>                	
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Informações do Requerente</legend>
	                		
	                		
	                	<% if(GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("34")) { //certidão narrativa %>
	                			
	                		<div>Requerente</div>
	                		<span><b><%=guiaCertidaoGeralDt.getNome()%></b></span><br>
	                						                		
	                		<div>CPF</div>
	                		<span><b><%=guiaCertidaoGeralDt.getCpf()%></b></span>
	                				
	                	<% } %>
	                			
	                	<% else { //outras certidões %>	
							
							<table border=0 width="100%">
							<tr>
							<td width="16%" valign=top>Requerente</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getNome()%></b></td>
							
							<td width="16%" valign=top>RG</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getRg()%></b></td>						
							</tr>
							
							<tr>
							<td width="16%" valign=top>CPF</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getCpf()%></b></td>
							
							<td width="16%" valign=top>Órgao Expedidor</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getRgOrgaoExpedidor()%></b></td>
							</tr>
							
							<tr>
							<td width="16%" valign=top>Tipo de Pessoa</td>
							<% if(!guiaCertidaoGeralDt.getNome().equalsIgnoreCase("") && guiaCertidaoGeralDt.getNome() != null){ %>
							<td width="34%" valign=top><b><%=(guiaCertidaoGeralDt.getTipoPessoa().equalsIgnoreCase("J"))?"Pessoa Jurídica":"Pessoa Física"%></b></td>
							<% } else { %>
							<td width="34%" valign=top></td>
							<% } %>
							
							<td width="16%" valign=top>Data de Expedição</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getRgDataExpedicao()%></b></td>
							</tr>
							
							<tr><td colspan=4>&nbsp;</td></tr>
							
							<tr>
							<td width="16%" valign=top>Naturalidade</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getNaturalidade()%></b></td>
							
							<td width="16%" valign=top>Estado Civil</td>
							<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getEstadoCivil()%></b></td>
							</tr>
							
							<% if( !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("38") ) { // não mostrar caso seja certidão de prática forense %>
								<tr>
								<td width="16%" valign=top>Data de Nascimento</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getDataNascimento()%></b></td>
								
								<td width="16%" valign=top>Profissão</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getProfissao()%></b></td>
								</tr>
							<% } %>
							
							<tr>
							<td width="16%" valign=top>Sexo</td>
							<% if(!guiaCertidaoGeralDt.getNome().equalsIgnoreCase("") && guiaCertidaoGeralDt.getNome() != null){ %>
							<td width="34%" valign=top><b><%=(guiaCertidaoGeralDt.getSexo().equalsIgnoreCase("F"))?"Feminino":"Masculino"%></b></td>
							<% } else { %>
							<td width="34%" valign=top></td>
							<% } %>
							
							<% if(GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("35")) { %> certidão de interdição
							
								<td width="16%" valign=top>Nacionalidade</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getNacionalidade()%></b></td>
								<% } else { %>
								<td width="16%" valign=top></td>
								<td width="34%" valign=top></td>
							
								<% if( !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("38") ) { // não mostrar caso seja certidão de prática forense %>
									<tr><td colspan=4>&nbsp;</td></tr>
									<tr>
									<td width="16%" valign=top>Nome da Mãe</td>
									<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getNomeMae()%></b></td>
									
									<td width="16%" valign=top>Registro Geral</td>
									<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getRegistroGeral()%></b></td>
									</tr>
									
									<tr>
									<td width="16%" valign=top>Nome do Pai</td>
									<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getNomePai()%></b></td>
									
									<td width="16%" valign=top>Patente</td>
									<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getPatente()%></b></td>
									</tr>
								<% } %>								
							<% } %>							
							</tr>
							
							<% if( !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("38") ) { // não mostrar caso seja certidão de prática forense %>							
								<tr><td colspan=4>&nbsp;</td></tr>
								<tr>
								<td width="16%" valign=top>Domicilio</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getDomicilio()%></b></td></tr>
								</tr>							
							<% } %>
							
							<% if(GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("38")) { // certidão de prática forense %>
							
								<tr><td colspan=4>&nbsp;</td></tr>
								
								<tr>
								<td width="16%" valign=top>Número OAB/Matrícula</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getOabNumero()%></b></td>
								
								<td width="16%" valign=top>Comarca</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getComarca()%></b></td>	
								</tr>
								
								<tr>
								<td width="16%" valign=top>OAB Complemento</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getOabComplemento()%></b></td>
								
								<td width="16%" valign=top>Serventia</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getServentia()%></b></td>	
								</tr>
								
								<tr>
								<td width="16%" valign=top>OAB UF</td>
								<td width="34%" valign=top><b><%=guiaCertidaoGeralDt.getOabUf()%></b></td>
								
								<td width="16%" valign=top></td>
								<td width="34%" valign=top></td>
								</tr>
							
							<% } %>
							
							</table>
							
						<% } %>
							
						</fieldset>
					<% } %>							
<!-- ********************************************************************************************************************************************************************************************************************************************	 -->
                	
                	<%
                	//If para a guia inicial
                	if( (processoDt != null && !processoDt.isSigiloso() && !processoDt.isSegredoJustica()) && GuiaEmissaoDt.getRequerente() != null && GuiaEmissaoDt.getRequerente().length() > 0 ) {
                		%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Polo Ativo | Promovente | Requerente </legend>
							
							<div> Nome </div>
	       					<span class="span1">
	       						<%=GuiaEmissaoDt.getRequerente()%>
	       					</span>
							
						</fieldset>	
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Polo Passivo | Promovido | Requerido </legend>
							
							<div> Nome </div>
	       					<span class="span1">
	       						<%if( GuiaEmissaoDt.getRequerido() != null ) { %>
	       							<%=GuiaEmissaoDt.getRequerido()%>
	       						<%}%>
	       					</span>							
						</fieldset>												
                		<%
					}
                	%>
                	
                	
                	<%
                	//************************************************************
					List<GuiaEmissaoDt> listaGuiasIniciaisComplementaresPagas = (List<GuiaEmissaoDt>) request.getAttribute("ListaGuiasIniciaisComplementaresPagas");
					if( listaGuiasIniciaisComplementaresPagas != null && !listaGuiasIniciaisComplementaresPagas.isEmpty() ) {
						%>
						<fieldset id="VisualizaDados" class="VisualizaDados" style="background-color: silver;">
							<legend style="background-color: silver;">
								Guias Iniciais e Complementares Utilizadas para Base deste Cálculo
							</legend>
						<%
						boolean possuiLocomocao = Funcoes.guiaEmissaoPossuiLocomocao(listaGuiasIniciaisComplementaresPagas);
						for(GuiaEmissaoDt guiaEmissaoDtTemp: listaGuiasIniciaisComplementaresPagas) {
							%>
							
							<div>Número da Guia</div>
				       		<span class="span1"><%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtTemp.getNumeroGuiaCompleto()) %></span>
				       		
				       		<br />
				       		
				       		<div>Tipo da Guia</div>
				       		<span class="span1"><%=guiaEmissaoDtTemp.getGuiaModeloDt().getGuiaTipo() %></span>
				       		
				       		<br />
				       		
							<div>Classe CNJ</div>
				       		<span class="span1"><%=guiaEmissaoDtTemp.getProcessoTipo() %></span>
				       		
				       		<br />
				       		
				       		<div>Status</div>
				       		<span class="span1"><%=guiaEmissaoDtTemp.getGuiaStatus() %></span>
				       		
				       		<br />
				       		
				       		<div>Data Emissão</div>
				       		<span class="span1"><%=Funcoes.TelaData(guiaEmissaoDtTemp.getDataEmissao()) %></span>
				       		
				       		<br />
				       		
				       		<div>Data Recebimento</div>
				       		<span class="span1"><%=Funcoes.TelaData(guiaEmissaoDtTemp.getDataRecebimento()) %></span>
							
							<br />
							
							<div>Valor Base Cálculo da Guia</div>
				       		<span class="span1">
				       			R$ 
					       		<%if( guiaEmissaoDtTemp.getNovoValorAcaoAtualizado() != null && guiaEmissaoDtTemp.getNovoValorAcaoAtualizado().length() > 0 ) {
					       			
					       			if( guiaEmissaoDtTemp.getValorCausaCoringa() != null && guiaEmissaoDtTemp.getValorCausaCoringa().length() > 0 ) {
					       				%><%=Funcoes.FormatarDecimal(guiaEmissaoDtTemp.getValorCausaCoringa()) %><%
					       			}
					       			else {
					       				%><%=Funcoes.FormatarDecimal(guiaEmissaoDtTemp.getNovoValorAcaoAtualizado()) %><%
					       			}
					       		}
					       		%>
				       		</span>
				       		
				       		<br />
							
							<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend style="background-color: silver;">Itens de Custa</legend>
		                		
								<table id="Tabela" class="Tabela">
									<thead>
										<tr>
											<th>Nº</th>
											<th>Descrição(Cód.Regimento)</th>
											<%if(possuiLocomocao || guiaEmissaoDtTemp.possuiLocomocao() ) { %>
												<td>Bairro</td>
											<% } %>
											<th>Código</th>
											<th>Quantidade</th>
											<%if( guiaEmissaoDtTemp.isLocomocaoComplementar() ) { %>
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
										if( guiaEmissaoDtTemp != null && guiaEmissaoDtTemp.getListaGuiaItemDt() != null ) {
											for(int i = 0 ; i< guiaEmissaoDtTemp.getListaGuiaItemDt().size();i++) {
												objTemp = (GuiaItemDt)guiaEmissaoDtTemp.getListaGuiaItemDt().get(i);%>
												<tr>
													<td align="center">
														<%=(i + 1)%>
													</td>
													<td>
														<%=objTemp.getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getCustaDt().getCodigoRegimentoTratamento() %>
													</td>
													
													<%if(possuiLocomocao || guiaEmissaoDtTemp.possuiLocomocao() ) { %>
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
													
													<%if( guiaEmissaoDtTemp.isLocomocaoComplementar() ) { %>
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
												if( possuiLocomocao || guiaEmissaoDtTemp.isGuiaLocomocao() ) colspan += 1;
												if( guiaEmissaoDtTemp.isLocomocaoComplementar() ) colspan += 2;
											%>
											<td align="center" colspan="<%=colspan%>">
												<label class="formEdicaoLabel"><b>Total da Guia</b></label><br>
											</td>
											<td>
												<label class="formEdicaoLabel">
													<b>R$ <%= Funcoes.FormatarDecimal( guiaEmissaoDtTemp.getValorTotalGuiaDouble()) %></b>
												</label><br>
											</td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						
							<%
						}
						%>
						</fieldset>
						<%
					}
					//***********************************************************
					%>
					
					
					<%
                	//************************************************************
					List<GuiaEmissaoDt> listaGuiasParceladasAguardandoPagamento = (List<GuiaEmissaoDt>) request.getAttribute("ListaGuiasParceladasAguardandoPagamento");
					if( listaGuiasParceladasAguardandoPagamento != null && !listaGuiasParceladasAguardandoPagamento.isEmpty() ) {
						%>
						<fieldset id="VisualizaDados" class="VisualizaDados" style="background-color: silver;">
							<legend style="background-color: silver;">
								Guias Parcelas, de Serviços e Complementares Aguardando Pagamento - Estes itens serão adicionados nesta Guia Final
							</legend>
						<%
						boolean possuiLocomocao = Funcoes.guiaEmissaoPossuiLocomocao(listaGuiasParceladasAguardandoPagamento);
						for(GuiaEmissaoDt guiaEmissaoDtTemp: listaGuiasParceladasAguardandoPagamento) {
							%>
							
							<div>Número da Guia</div>
				       		<span class="span1"><%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtTemp.getNumeroGuiaCompleto()) %></span>
				       		
				       		<br />
				       		
				       		<div>Tipo da Guia</div>
				       		<span class="span1"><%=guiaEmissaoDtTemp.getGuiaModeloDt().getGuiaTipo() %></span>
				       		
				       		<br />
				       		
				       		<div>Classe CNJ</div>
				       		<span class="span1"><%=guiaEmissaoDtTemp.getProcessoTipo() %></span>
				       		
				       		<br />
				       		
				       		<div>Status</div>
				       		<span class="span1"><%=guiaEmissaoDtTemp.getGuiaStatus() %></span>
				       		
				       		<br />
				       		
				       		<div>Data Emissão</div>
				       		<span class="span1"><%=Funcoes.TelaData(guiaEmissaoDtTemp.getDataEmissao()) %></span>
				       		
				       		<br />
				       		
				       		<div>Data Vencimento</div>
				       		<span class="span1"><%=Funcoes.TelaData(guiaEmissaoDtTemp.getDataVencimento()) %></span>
							
							<br />
							
							<div>Valor Base Cálculo da Guia</div>
				       		<span class="span1">
				       			R$ 
					       		<%if( guiaEmissaoDtTemp.getNovoValorAcaoAtualizado() != null && guiaEmissaoDtTemp.getNovoValorAcaoAtualizado().length() > 0 ) {
					       			
					       			if( guiaEmissaoDtTemp.getValorCausaCoringa() != null && guiaEmissaoDtTemp.getValorCausaCoringa().length() > 0 ) {
					       				%><%=Funcoes.FormatarDecimal(guiaEmissaoDtTemp.getValorCausaCoringa()) %><%
					       			}
					       			else {
					       				%><%=Funcoes.FormatarDecimal(guiaEmissaoDtTemp.getNovoValorAcaoAtualizado()) %><%
					       			}
					       		}
					       		%>
				       		</span>
				       		
				       		<br />
				       		
				       		<%
				       		if( guiaEmissaoDtTemp.getGuiaEmissaoDtReferencia() != null && guiaEmissaoDtTemp.getTipoGuiaReferenciaDescontoParcelamento() != null && guiaEmissaoDtTemp.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_PARCELADA) ) {
							%>
								<br />
								
								<div>Guia de Referência deste Parcelamento</div>
								<span><%=Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtTemp.getGuiaEmissaoDtReferencia().getNumeroGuiaCompleto())%></span>
								
								<br /><br /><br />
								
								<div>Número desta Parcela</div>
								<span><%=guiaEmissaoDtTemp.getParcelaAtual()%></span>
								
								<br /><br />
								
								<div>Quantidade de Parcelas</div>
								<span><%=guiaEmissaoDtTemp.getQuantidadeParcelas()%></span>
								
								<br />
							<%
							}
				       		%>
							
							<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend style="background-color: silver;">Itens de Custa</legend>
		                		
								<table id="Tabela" class="Tabela">
									<thead>
										<tr>
											<th>Nº</th>
											<th>Descrição(Cód.Regimento)</th>
											<%if(possuiLocomocao || guiaEmissaoDtTemp.possuiLocomocao() ) { %>
												<td>Bairro</td>
											<% } %>
											<th>Código</th>
											<th>Quantidade</th>
											<%if( guiaEmissaoDtTemp.isLocomocaoComplementar() ) { %>
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
										if( guiaEmissaoDtTemp != null && guiaEmissaoDtTemp.getListaGuiaItemDt() != null ) {
											for(int i = 0 ; i< guiaEmissaoDtTemp.getListaGuiaItemDt().size();i++) {
												objTemp = (GuiaItemDt)guiaEmissaoDtTemp.getListaGuiaItemDt().get(i);%>
												<tr>
													<td align="center">
														<%=(i + 1)%>
													</td>
													<td>
														<%=objTemp.getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getCustaDt().getCodigoRegimentoTratamento() %>
													</td>
													
													<%if(possuiLocomocao || guiaEmissaoDtTemp.possuiLocomocao() ) { %>
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
													
													<%if( guiaEmissaoDtTemp.isLocomocaoComplementar() ) { %>
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
												if( possuiLocomocao || guiaEmissaoDtTemp.isGuiaLocomocao() ) colspan += 1;
												if( guiaEmissaoDtTemp.isLocomocaoComplementar() ) colspan += 2;
											%>
											<td align="center" colspan="<%=colspan%>">
												<label class="formEdicaoLabel"><b>Total da Guia</b></label><br>
											</td>
											<td>
												<label class="formEdicaoLabel">
													<b>R$ <%= Funcoes.FormatarDecimal( guiaEmissaoDtTemp.getValorTotalGuiaDouble()) %></b>
												</label><br>
											</td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						
							<%
						}
						%>
						</fieldset>
						<%
					}
					//***********************************************************
					%>
					
					
					<%if(!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) { %>
	                	<%if( GuiaEmissaoDt.getProcessoTipo() != null && GuiaEmissaoDt.getProcessoTipo().length() > 0 ) { %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">   
					   		<legend>Classe Informada na Guia</legend>
					   		
					       	<div></div>
					       	<span class="span1"><%=GuiaEmissaoDt.getProcessoTipo() %></span>
						</fieldset>
						<%} %>
					<%} %>
					
					<%if( (!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) || ( GuiaEmissaoDt.getId_Processo() != null && GuiaEmissaoDt.getId_Processo().isEmpty() ) ) { %>
						<%if( GuiaEmissaoDt.getAreaDistribuicao() != null && GuiaEmissaoDt.getAreaDistribuicao().length() > 0 ) { %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">   
					   		<legend>Área de Distribuição</legend>
					   		
					       	<div></div>
					       	<span class="span1"><%=GuiaEmissaoDt.getAreaDistribuicao() %></span>
						</fieldset>
						<%} %>
					<%} %>
					
					<%if( GuiaEmissaoDt.getGuiaModeloDt() != null && GuiaEmissaoDt.getGuiaModeloDt().getProcessoTipo() != null && GuiaEmissaoDt.getGuiaModeloDt().getProcessoTipo().length() > 0 && GuiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo() != null && ( GuiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) || GuiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU) ) ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">   
				   		<legend>Classe Base para o Cálculo</legend>
				   		
				       	<div></div>
				       	<span class="span1"><%=GuiaEmissaoDt.getGuiaModeloDt().getProcessoTipo() %></span>
					</fieldset>
					<%} %>
                	
                	<%if(!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) { %>
	                	<%if( (GuiaEmissaoDt.getApelante() != null && GuiaEmissaoDt.getApelado() != null) ||
	                			(GuiaEmissaoDt.getApelante() == null && GuiaEmissaoDt.getApelado() != null) || 
	                			(GuiaEmissaoDt.getApelante() != null && GuiaEmissaoDt.getApelado() == null) ) { %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Recorrente/Apelante e Recorrido/Apelado</legend>
	                		
	                		<div style="width:140px;"> Recorrente/Apelante </div>
				       		<span class="span1"><%=(GuiaEmissaoDt.getApelante()==null? "":GuiaEmissaoDt.getApelante())%></span>
				       		<br />
				       		<div style="width:140px;"> Recorrido/Apelado </div>
				       		<span class="span1"><%=(GuiaEmissaoDt.getApelado()==null? "":GuiaEmissaoDt.getApelado())%></span>
				       	
	                	</fieldset>
	                	<%}%>
                	<%} %>
                	
                	<%if( GuiaEmissaoDt.getGuiaModeloDt() != null && GuiaEmissaoDt.getGuiaModeloDt().getGuiaTipo() != null && GuiaEmissaoDt.getGuiaModeloDt().getGuiaTipo().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Tipo de Guia</legend>
                		
                		<div style="width:140px;"> Tipo de Guia </div>
			       		<span class="span1">
			       			<%=GuiaEmissaoDt.getGuiaModeloDt().getGuiaTipo()%>
			       		</span>
			       	
                	</fieldset>
                	<%} %>
                	
                	<%if( GuiaEmissaoDt.getRateioCodigo() != null && GuiaEmissaoDt.getRateioCodigo().length() > 0 ) { %>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Rateio</legend>
                		
                		<div style="width:140px;"> Tipo Rateio </div>
			       		<span class="span1">
			       			<%=GuiaEmissaoNe.getNomeRateio(Funcoes.StringToInt(GuiaEmissaoDt.getRateioCodigo()))%>
			       		</span>
			       	
                	</fieldset>
                	<%}%>
                	
                	<%if(!processoDt.isSigiloso() && !processoDt.isSegredoJustica()) { %>
	                	<%if( GuiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && GuiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {	%>
	                		<fieldset id="VisualizaDados" class="VisualizaDados">
	                			<legend>Responsável pela Guia</legend>
	                			
	                			<div style="width:140px;"> Nome </div>
	                			<span class="span1">
					       			<%=GuiaEmissaoDt.getNomeParte( GuiaEmissaoDt.getId_ProcessoParteResponsavelGuia() )%>
				       			</span>
	                		</fieldset>
	                	<%} %>
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
                	<%} %>
                	
                	<%if( GuiaEmissaoDt.getNumeroGuiaCompleto() != null && GuiaEmissaoDt.getNumeroGuiaCompleto().length() > 0 ) { %>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Número Guia</legend>
                			
                			<div style="width:140px;"> Número Guia </div>
                			<span class="span1">
				       			<%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt.getNumeroGuiaCompleto())%>
			       			</span>
			       			
			       			<%if( GuiaEmissaoDt.getId_Usuario() != null && GuiaEmissaoDt.getId_Usuario().length() > 0 && GuiaEmissaoDt.getUsuario() != null && GuiaEmissaoDt.getUsuario().length() > 0 ) { %>
			       				<br />
			       			
			       				<div style="width:140px;"> Emissor da Guia </div>
	                			<span class="span1">
					       			<%=GuiaEmissaoDt.getUsuario()%>
				       			</span>
			       			<%} %>
                		</fieldset>
                	<%} %>
                	
                	<%if( GuiaEmissaoDt.getId_GuiaStatus() != null && GuiaEmissaoDt.getId_GuiaStatus().length() > 0 && GuiaEmissaoDt.getGuiaStatus() != null ) {%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Status</legend>
                			
                			<%
               				String cor = "#CC6600"; //alaranjado
              				if( GuiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
              					cor = "#CC0000"; //vermelho
              				}
              				else {
              					if( GuiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO)) ) {
              						cor = "#66CC00"; //verde
              					}
              				}
              				%>
                			<span class="span1" style="color:<%=cor%>;">
                				<%=GuiaEmissaoDt.getGuiaStatus() %>
			       			</span>
			       			
			       			<%if(GuiaEmissaoDt.getValorRecebimento() != null && GuiaEmissaoDt.getValorRecebimento().trim().length() > 0) {%>
			       				<br />
			       				<div style="width:140px;"> Valor Pago </div>
	                			<span class="span1">	                			
					       			R$ <%=Funcoes.FormatarDecimal(GuiaEmissaoDt.getValorRecebimento())%>
				       			</span>
              			    <%}%>
              			    
              			    <%if(GuiaEmissaoDt.getDataMovimento() != null && GuiaEmissaoDt.getDataMovimento().trim().length() > 0) {%>
			       				<br />
              					<div style="width:140px;"> Data Baixa (Movimento) </div>
	                			<span class="span1">	                			
					       			<%=Funcoes.FormatarData(GuiaEmissaoDt.getDataMovimento())%>
				       			</span>
              			    <%}%>
              			    
              			    <%if(GuiaEmissaoDt.getDataRepasse() != null && GuiaEmissaoDt.getDataRepasse().trim().length() > 0) {%>
			       				<br />
              					<div style="width:140px;"> Data Repasse </div>
	                			<span class="span1">	                			
					       			<%=Funcoes.FormatarData(GuiaEmissaoDt.getDataRepasse())%>
				       			</span>
              			    <%}%>
                		</fieldset>
                		
                		
                	<%} %>
                	
					<%
						if(request.getAttribute("oficialAdHoc") != null){
							OficialSPGDt oficial = (OficialSPGDt)request.getAttribute("oficialAdHoc");
					%>
                		<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Locomoção</legend>
                			
                			<div style="width:140px;">Oficial:</div>
                			<span class="span1">
                				<%=oficial.getCodigoOficial()%>&nbsp;:&nbsp;&nbsp;
				       			<%=oficial.getNomeOficial()%>
			       			</span>
                		</fieldset>
                	<%}	%>
                	
                	<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeLocomocoesNaoUtilizadas")))) { %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend>
								Locomoções Não Vinculadas a Diligências Disponíveis para Serem Complementadas								
							</legend>
							
							<table id="TabelaLocomocoesNaoUtilizadas" class="Tabela">
								<thead>
									<tr class="TituloColuna">
										<th>Id</th>
										<th>Nº Guia</th>
										<th>Descrição(Cód.Regimento)</th>
										<th>Código</th>
										<th>Qtde</th>
										<th>Valor Locomoção</th>
										<th>Valor TJGO</th>									
										<td>Bairro</td>
										<td>Cidade</td>
										<td>UF</td>
										<td>Zona</td>												                  			
									</tr>			               			
								</thead>
								<tbody id="tabListaLocomocoesNaoUtilizadas">
									<%					
									List liTempNaoUtilizada = (List)request.getSession().getAttribute("ListaLocomocaoNaoUtilizada");
									double valorTotalLocomocao = 0;
									double valorTotalTJGO = 0;
									if( liTempNaoUtilizada != null && liTempNaoUtilizada.size() > 0 ) {
										for( int i = 0; i < liTempNaoUtilizada.size(); i++ ) {
											LocomocaoDt objTemp = (LocomocaoDt)liTempNaoUtilizada.get(i);
											valorTotalLocomocao += objTemp.getValorCalculadoLocomocoes();
											valorTotalTJGO += objTemp.getValorCalculadoTJGO();
											%>
											<tr>
												<td align="center"><%= objTemp.getId() %></td>
												<td align="center">
													<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto())%>													
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
							
							<% if( liTempNaoUtilizada != null && liTempNaoUtilizada.size() > 0 ) { %>
								<div style="width:280px;">Valor Total das Locomoções (Oficial de Justiça):</div>
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
					
					<%if( request.getAttribute("visualizarBoletoEmitido") != null 
						&& (Boolean)request.getAttribute("visualizarBoletoEmitido")
						&& GuiaEmissaoDt.getDataEmissaoBoleto() != null ) { %>
					
						<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Boleto Emitido</legend>
                			
                			<div style="width:140px;"> Data Emissão Boleto </div>
                			<span class="span1">	                			
				       			<%=Funcoes.FormatarData(GuiaEmissaoDt.getDataEmissaoBoleto())%>
			       			</span>
						</fieldset>
						
					<%} %>
					
					<%if( request.getAttribute("visualizarGuiaUtilizadaCertidaoProjudi") != null 
						&& (Boolean)request.getAttribute("visualizarGuiaUtilizadaCertidaoProjudi")
						&& request.getAttribute("visualizarDataEmissaoCertidao") != null ) { %>
					
						<fieldset id="VisualizaDados" class="VisualizaDados">
                			<legend>Guia Utilizada Emissão Certidão</legend>
                			
                			<div style="width:140px;"> Data Emissão Certidão </div>
                			<span class="span1">	                			
				       			<%=request.getAttribute("visualizarDataEmissaoCertidao")%>
			       			</span>
						</fieldset>
						
					<%} %>
                	
                	<%
                	if( request.getSession().getAttribute("ListaGuiasRateio") == null ) {
                		boolean possuiLocomocao = Funcoes.guiaItemPossuiLocomocao(liTemp);
                	%>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<legend>Itens de Custa</legend>
	                		
							<table id="Tabela" class="Tabela">
								<thead>
									<tr>
										<th>Nº</th>
										<th>Descrição(Cód.Regimento)</th>
										<%if( possuiLocomocao || GuiaEmissaoDt.possuiLocomocao() ) { %>
											<td>Bairro</td>
										<% } %>
										<th>Código</th>
										<th>Quantidade</th>
										<%if( GuiaEmissaoDt.isLocomocaoComplementar() ) { %>
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
									for(int i = 0 ; i< liTemp.size();i++) {
										objTemp = (GuiaItemDt)liTemp.get(i);%>
										<tr>
											<td align="center">
												<%=(i + 1)%>
											</td>
											<td>
												<%if (objTemp.getCustaDt() != null) {%>
												<%=objTemp.getCustaDt().getArrecadacaoCusta() %> <%=objTemp.getCustaDt().getCodigoRegimentoTratamento() %>
												<%}%>
											</td>
											
											<%if(possuiLocomocao || GuiaEmissaoDt.possuiLocomocao() ) { %>
												<td>
												 	<%if( objTemp.getLocomocaoDt() != null && objTemp.getLocomocaoDt().getBairroDt() != null ) { %>
														<%=objTemp.getLocomocaoDt().getBairroDt().getBairro() %>
													<% } %>
												</td>
											<% } %>
											
											<td align="center">
												<%if (objTemp.getCustaDt() != null) {%>
												<%=objTemp.getCustaDt().getCodigoArrecadacao() %>
												<%}%>
											</td>
											
											<td align="center">
												<%=objTemp.getQuantidade() %>
											</td>
											
											<%if( GuiaEmissaoDt.isLocomocaoComplementar() ) { %>
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
									%>
								</tbody>
								<tfoot>
									<tr>
										<%
											int colspan = 4;
											if( possuiLocomocao || GuiaEmissaoDt.isGuiaLocomocao() ) colspan += 1;
											if( GuiaEmissaoDt.isLocomocaoComplementar() ) colspan += 2;
										%>
										<td align="center" colspan="<%=colspan%>">
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
							<br />
							<hr />
							<%if( auxListaNomeParteGuia != null && auxListaNomePartePorcentagemGuia != null ) { %>
							<fieldset id="VisualizaDados" class="VisualizaDados">
                				<legend><%=(k+1)%>º Guia</legend>
                			
                				<div style="width:140px;"> Parte(Porcentagem) </div>
                				<span class="span1">
				       				<%=auxListaNomeParteGuia.get(k).toString().split(":")[0]%>
				       				(<%=auxListaNomePartePorcentagemGuia.get(k).toString()%>%)
			       				</span>
                			</fieldset>                			
                			<% } %>
                		
							<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend>Itens de Custa</legend>
		                		
								<table id="Tabela" class="Tabela">
									<thead>
										<tr>
											<th>Descrição(Cód.Regimento)</th>
											<th>Código</th>
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
												<td align="center"><%=objTemp.getQuantidade() %></td>
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
					<%	
                		}
					}%>
					
					<%
					if( !UsuarioSessao.isAdvogado() ) {
						Set<String> numeroMandadoSPG = null;
						Set<String> numeroGuiaComplementarSPG = null;
						List<LocomocaoSPGDt> listaLocomocaoSPGDt = (List<LocomocaoSPGDt>)request.getAttribute("ListLocomocaoMandado");
						if( listaLocomocaoSPGDt != null && !listaLocomocaoSPGDt.isEmpty() ) {
							
							for( LocomocaoSPGDt locomocaoSPGDt: listaLocomocaoSPGDt ) {
								if( locomocaoSPGDt != null ) {
									if( locomocaoSPGDt.getNumeroMandado() > 0L ) {
										if( numeroMandadoSPG == null ) {
											numeroMandadoSPG = new LinkedHashSet<String>();
										}
										
										numeroMandadoSPG.add(String.valueOf(locomocaoSPGDt.getNumeroMandado()));
									}
									if( locomocaoSPGDt.getNumeroGuiaComplementar() > 0L ) {
										if( numeroGuiaComplementarSPG == null ) {
											numeroGuiaComplementarSPG = new LinkedHashSet<String>();
										}
										
										numeroGuiaComplementarSPG.add(String.valueOf(locomocaoSPGDt.getNumeroGuiaComplementar()));
									}
								}
							}
							
						}
						%>
						<%if( numeroMandadoSPG != null && !numeroMandadoSPG.isEmpty() ) { %>
							<fieldset id="VisualizaDados" class="VisualizaDados">   
						   		<legend>Esta Guia possui Locomoção Vinculada a Mandado no SPG</legend>
						   		<%if( numeroGuiaComplementarSPG != null && numeroGuiaComplementarSPG.size() > 0 ) { %>
							   		<%for( String numeroGuiaComplementar: numeroGuiaComplementarSPG) {%>
								       	<div style="width:200px;margin-left:30px;">Esta Guia foi Complementada</div>
								       	<span class="span1">
								       		<%=numeroGuiaComplementar %>
								       	</span>
								       	<br />
								       	<br />
							       	<%} %>
							    <%} %>
							    <%if( numeroMandadoSPG != null && numeroMandadoSPG.size() > 0 ) { %>
							   		<%for( String numeroMandado: numeroMandadoSPG) {%>
								       	<div style="width:200px;margin-left:30px;">Número do Mandado</div>
								       	<span class="span1">
								       		<%=numeroMandado %>
								       	</span>
								       	<br />
								       	<br />
							       	<%} %>
							    <%} %>
							</fieldset>
						<%}
					}%>
					
					<%if ((request.getAttribute("tempRetorno") == null || !request.getAttribute("tempRetorno").toString().equalsIgnoreCase("GerarBoleto")) &&  request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") != null && (Boolean)request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") && (request.getAttribute("TotalGuia") == null || Funcoes.StringToDouble(request.getAttribute("TotalGuia").toString()) > 0)) { %>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> Informações Sobre Pagamentos</legend>					
						<em>
							Para realizar o pagamento desta guia em qualquer banco é necessário gerar o seu boleto. Após a emissão, acesse o endereço <b>https://projudi.tjgo.jus.br/GerarBoleto</b> informando o número da guia para gerar o boleto. Outra opção é consultar esta guia no processo após a sua emissão e clicar no botão "Gerar Boleto".
						</em>
					</fieldset>
					
					<% } %>
					
                </fieldset>
                <%}%>                
                
                <%
                if( request.getAttribute("listaLogDtHistoricoLogsGuia") != null ) {
	                List<LogDt> historicoLogsGuia = (List<LogDt>) request.getAttribute("listaLogDtHistoricoLogsGuia");
	                if( historicoLogsGuia != null && !historicoLogsGuia.isEmpty() ) {
                %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">   
					   		<legend>Histórico da Guia nos Logs</legend>
					   		
					   		<%for(LogDt logDt: historicoLogsGuia) { %>
					   			<fieldset id="VisualizaDados" class="VisualizaDados">   
					   				<legend style="background-color: silver;">Data e Hora do Log: <%=logDt.getHora() %></legend>
					   		
					       			<div>Tipo Log</div>
					       			<span class="span1">(<%=logDt.getId_LogTipo()%>) <%=logDt.getLogTipo()%></span>
					       			
					       			<br />
							       	<br />
					       			
					       			<div>Tabela</div>
					       			<span class="span1"><%=logDt.getTabela()%></span>
					       			
					       			<br />
							       	<br />
							       	
							       	<%if( logDt.getValorAtual() != null && !logDt.getValorAtual().trim().isEmpty() ) { %>
								       	<div>Valor Anterior</div>
						       			<span class="span1"><%=logDt.getValorAtual()%></span>
						       			
						       			<br />
						       			<br />
						       		<%} %>
					       			
							       	<%if( logDt.getValorAtual() != null && !logDt.getValorAtual().trim().isEmpty() ) { %>
						       			<div>Valor Alterado</div>
						       			<span class="span1"><%=logDt.mostrarDiferencaTextoLog()%></span>
						       			
						       			<br />
								       	<br />
						       		<%} else { %>
						       			<%if( logDt.getValorNovo() != null && !logDt.getValorNovo().trim().isEmpty() ) { %>
					       					<div>Valor Alterado</div>
					       					<span class="span1"><%=logDt.getValorNovoEspecial()%></span>
					       				<%} %>
					       			<%} %>
					       		</fieldset>
					       	<%} %>
						</fieldset>
                <%	} 
                }%>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                	<%if( request.getAttribute("visualizarBotaoVoltar") != null && (Boolean)request.getAttribute("visualizarBotaoVoltar") ) {
                		String comandoOnClickBotaoVoltar = "AlterarValue('PaginaAtual','" + Configuracao.Curinga6 + "');AlterarValue('PassoEditar','" + Configuracao.Curinga7 + "');";                		
                		if (request.getAttribute("comandoOnClickBotaoVoltar") != null && request.getAttribute("comandoOnClickBotaoVoltar").toString().length() > 0) {
                			comandoOnClickBotaoVoltar = request.getAttribute("comandoOnClickBotaoVoltar").toString();
                		}
                	%>
                    <button name="imgVoltar" value="Voltar" onclick="<%=comandoOnClickBotaoVoltar%>" >                    	
                    	Voltar
                    </button>
                    <%} %>
                    
                    <%if( request.getAttribute("visualizarBotaoSalvarGuia") != null && (Boolean)request.getAttribute("visualizarBotaoSalvarGuia") && !(request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) && !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("34") && !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("35") && !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("36") && !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("37") && !GuiaEmissaoDt.getId_GuiaTipo().equalsIgnoreCase("38") ) { %>
	                    <button id="imgEmitirGuia" name="imgEmitirGuia" value="Emitir Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');AlterarValue('PassoEditar','<%=Configuracao.Salvar%>');" >
		                  	Emitir Guia
		                </button>
	                <%} %>
	                	                
	                <%
	                if( GuiaEmissaoDt.getId_GuiaStatus() != null && 
	                    (GuiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) || 
	                     GuiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)))) {
	                	if( request.getAttribute("visualizarBotaoValidarPagamentoGuia") != null && (Boolean)request.getAttribute("visualizarBotaoValidarPagamentoGuia") ) {
	                		%>
	                		<button name="imgValidarPagamento" value="Validar Pagamento com o SPG" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('validaPagamento','<%=Configuracao.Curinga6%>');AlterarValue('numeroGuiaCompletoValidarSPG','<%=GuiaEmissaoDt.getNumeroGuiaCompleto()%>');" >
		                  		Validar Pagamento com o SPG
		                	</button>
	                		<%
	                	}
	                }
	                %>
                    
                    <%if( request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") != null && (Boolean)request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") ) { %>
                    	
                    		<%if( request.getSession().getAttribute("ListaGuiasRateio") == null ) {%>
                    			
			                    <button id="imgEmitirGuiaImprimirPDF" name="imgEmitirGuiaImprimirPDF" value="Imprimir" type="button" onclick="MostrarOcultar('divEmitirBoleto');Ocultar('divPagamentoOnlineBancos');location.hash='#divEmitirBoleto';" >
			                    	
			                    	Imprimir
			                    </button>
			                    
		                    <%}%>
                    	
                    <%} %>
                    
<%--                     <%if( GuiaEmissaoDt != null && GuiaEmissaoDt.getId() != null && GuiaEmissaoDt.getNumeroGuiaCompleto() != null && !GuiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {%> --%>
<%--                     	<a href="GerarBoleto?PaginaAtual=2&numeroGuiaConsulta=<%=GuiaEmissaoDt.getNumeroGuiaCompleto()%>"> --%>
<!-- 	                		<button id="imgGerarBoleto" name="imgGerarBoleto" type="button" value="Gerar Boleto" > -->
<!-- 			                  	Gerar Boleto -->
<!-- 			                </button> -->
<!-- 		                </a> -->
<%--                     <%} %> --%>
                    
                    <%if( GuiaEmissaoDt != null && request.getAttribute("emitirGuiaLocomocaoComplementar") != null && request.getAttribute("emitirGuiaLocomocaoComplementar").toString().equals(GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_LOC_COMPLEMENTAR) ) { %>
                		<a href="GuiaLocomocaoComplementar?PaginaAtual=<%=Configuracao.Novo%>&Id_GuiaEmissaoPaga=<%=GuiaEmissaoDt.getId()%>">
	                		<button id="imgEmitirGuiaComplementar" name="imgEmitirGuiaComplementar" type="button" value="Emitir Guia Complementar" >
			                  	Emitir Guia de Locomoção Complementar
			                </button>
		                </a>
                	<%} %>
                	
                	<br />
                	
                </div>
                
                <div id="divBotoesCentralizadosVoltar" class="divBotoesCentralizados DivInvisivel">
                	<%if( request.getAttribute("visualizarBotaoVoltar") != null && (Boolean)request.getAttribute("visualizarBotaoVoltar") ) {
                		String comandoOnClickBotaoVoltar = "AlterarValue('PaginaAtual','" + Configuracao.Curinga6 + "');AlterarValue('PassoEditar','" + Configuracao.Curinga7 + "');";                		
                		if (request.getAttribute("comandoOnClickBotaoVoltar") != null && request.getAttribute("comandoOnClickBotaoVoltar").toString().length() > 0) {
                			comandoOnClickBotaoVoltar = request.getAttribute("comandoOnClickBotaoVoltar").toString();
                		}
                	%>
                    <button name="imgVoltar" value="Voltar" onclick="<%=comandoOnClickBotaoVoltar%>" >                    	
                    	Voltar
                    </button>
                    <%} %>
                </div>                
                                
                <%if( request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") != null && (Boolean)request.getAttribute("visualizarBotaoImpressaoBotaoPagamento") ) { %>
	                <div id="divEmitirBoleto" class="DivInvisivel">
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
	                		<% if (request.getAttribute("tempRetorno") != null && request.getAttribute("tempRetorno").toString().equalsIgnoreCase("GerarBoleto")) { %>
	                		<legend>Deseja Imprimir o Boleto?</legend>
	                		
	                		<fieldset class="fieldsetEndereco">
		                		<legend>Informações</legend>
		                	
		                		<em>
		                		Este boleto poderá ser pago até o seu vencimento em qualquer banco, após o vencimento do boleto deverá ser emitido um novo boleto para efetuar o pagamento, a reemissão do boleto poderá ser realizada até a data de vencimento da guia. Para consultar os itens da guia acesse o endereço <b>https://projudi.tjgo.jus.br/GerarBoleto</b> informando o número da guia, e clique no botão "Consultar Itens".
		              			</em>
		              		</fieldset>
	                		<% } else { %>
	                		<legend>Deseja Imprimir a Guia?</legend>
	                			
		                	<fieldset class="fieldsetEndereco">
		                		<legend>Informações</legend>
		                	
		                		<em>
		                		Para realizar o pagamento desta guia em qualquer banco é necessário gerar o seu boleto. Após a emissão da guia, acesse o endereço <b>https://projudi.tjgo.jus.br/GerarBoleto</b> informando o número da guia. Através dessa funcionalidade poderá gerar o boleto ou consultar os itens da guia: para gerar o boleto clique no botão "Gerar Boleto" e para consultar os itens da guia clique no botão "Consultar Itens".
		              			</em>
		              		</fieldset>
	                		<% } %>	                		
		                    	
		                	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
		                    	<button name="imgEmitirGuia" value="Imprimir" onclick="Ocultar('divBotoesCentralizados');Ocultar('divEmitirBoleto');Mostrar('divBotoesCentralizadosVoltar');AlterarValue('PaginaAtual','<%=Configuracao.Imprimir %>');AlterarValue('PassoEditar','<%=Configuracao.Imprimir %>');" >			                    	
			                    	Imprimir
			                    </button>
			                </div>
			                
			                <br />
			                <br />			                
	                    </fieldset>
	                </div>
                <%} %>
                
			</div>
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
		<br /><br />
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>