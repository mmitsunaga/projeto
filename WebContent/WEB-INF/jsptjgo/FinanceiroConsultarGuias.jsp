<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="FinanceiroConsultarGuiasDt" scope="session" class="br.gov.go.tj.projudi.dt.FinanceiroConsultarGuiasDt"/>
<jsp:useBean id="FinanceiroConsultarGuiasCt_ValorTotalGuiasConsultadas" scope="session" class="java.lang.String"/>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<title>TJGO/Projudi - Formulário Financeiro de Consulta de Guias</title>
		<style type="text/css">
		     @import url('./css/Principal.css');
		     @import url('./css/Paginacao.css');
		     #bkg_projudi { display: none;}
		</style>
		
		<script type="text/javascript" >
			var _PaginaEditar = "<%=Configuracao.Localizar%>";
			var _Acao = "Editar";
		</script>
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">	  	
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| </h2></div>
			<form action="FinanceiroConsultarGuias" method="post" name="FinanceiroConsultarGuias" id="FinanceiroConsultarGuias">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PaginaEditar" name="PaginaEditar" type="hidden" value="<%=request.getAttribute("PaginaEditar")%>">
				<input type="hidden" id="tempBuscaId" name="tempBuscaId"/>
        		<input type="hidden" id="tempBuscaDescricao" name="tempBuscaDescricao"/>				
			
				<div id="divEditar" class="divEditar">
                	<fieldset id="VisualizaDados" class="VisualizaDados">
				    	<legend> Consultar Guias </legend>
				    
						<label class="formEdicaoLabel" for="numeroGuiaCompleto">
							Número da Guia
						</label><br>
						<input class="formEdicaoInput" name="numeroGuiaCompleto" id="numeroGuiaCompleto" type="text" size="25" maxlength="13" value="<%=(FinanceiroConsultarGuiasDt.getNumeroGuiaCompleto()==null?"":FinanceiroConsultarGuiasDt.getNumeroGuiaCompleto())%>" onkeyup="autoTab(this,11)" onkeypress="return DigitarSoNumero(this, event)" />
						<br />
						<label class="formEdicaoLabel" for="numeroProcesso">
							Número Processo
						</label><br>
						<input class="formEdicaoInput" name="numeroProcesso" id="numeroProcesso" type="text" size="25" maxlength="15" value="<%=(FinanceiroConsultarGuiasDt.getNumeroProcesso()==null?"":FinanceiroConsultarGuiasDt.getNumeroProcesso())%>" onkeyup="autoTab(this,15)" onkeypress="return DigitarNumeroProcesso(this, event)" />
						Exemplo: <b>48032.1</b>
						<%
						List listaGuiaTipoDt = (List)request.getAttribute("listaGuiaTipoDt");
						if( listaGuiaTipoDt != null && listaGuiaTipoDt.size() > 0 ) 
						{ %>
							<br />
							<label class="formEdicaoLabel" for="Id_GuiaTipo">
								Tipo de Guia
							</label><br>
							<select id="Id_GuiaTipo" name="Id_GuiaTipo" style="width: 300px;">
								<option value=""></option>
								<% 
								for( int i = 0; i < listaGuiaTipoDt.size(); i++ ) {
									GuiaTipoDt guiaTipoDt = (GuiaTipoDt) listaGuiaTipoDt.get(i);
								%>
									<option value="<%=guiaTipoDt.getId()%>" <%=((FinanceiroConsultarGuiasDt.getId_GuiaTipo()!=null && FinanceiroConsultarGuiasDt.getId_GuiaTipo().equals(guiaTipoDt.getId()))?" selected ":"") %>>
										<%=guiaTipoDt.getGuiaTipo()%>
									</option>
								<%}%>
							</select>
						<%} %>
					
						<%
						List listaGuiaStatusDt = (List)request.getAttribute("listaGuiaStatusDt");
						if( listaGuiaStatusDt != null && listaGuiaStatusDt.size() > 0 )
						{ %>
							<br />
							<label class="formEdicaoLabel" for="Id_GuiaStatus">
								Status da Guia
							</label><br>
							<select id="Id_GuiaStatus" name="Id_GuiaStatus" style="width: 300px;">
								<option value=""></option>
								<% 
								for( int i = 0; i < listaGuiaStatusDt.size(); i++ ) {
									GuiaStatusDt guiaStatusDt = (GuiaStatusDt) listaGuiaStatusDt.get(i);
								%>
									<option value="<%=guiaStatusDt.getId()%>" <%=((FinanceiroConsultarGuiasDt.getId_GuiaStatus()!=null && FinanceiroConsultarGuiasDt.getId_GuiaStatus().equals(guiaStatusDt.getId()))?" selected ":"") %>>
										<%=guiaStatusDt.getGuiaStatus()%>
									</option>
								<%}%>
							</select>
						<%} %>
					
					
						<br />
						<label class="formEdicaoLabel">
							Data de Emissão
						</label><br>
						<input name="dataInicioEmissao" id="dataInicioEmissao" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataInicioEmissao()==null?"":FinanceiroConsultarGuiasDt.getDataInicioEmissao())%>" />
						<img id="calendarioDataInicioEmissao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicioEmissao,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataInicioEmissao',''); return false;" title="Limpar Data Início Emissão" />
						
						&nbsp;à&nbsp;
						
						<input name="dataFimEmissao" id="dataFimEmissao" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataFimEmissao()==null?"":FinanceiroConsultarGuiasDt.getDataFimEmissao())%>" />
						<img id="calendarioDataFimEmissao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFimEmissao,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataFimEmissao',''); return false;" title="Limpar Data Fim Emissão" />
						
						<br />
						<label class="formEdicaoLabel">
							Data de Recebimento
						</label><br>
						<input name="dataInicioRecebimento" id="dataInicioRecebimento" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataInicioRecebimento()==null?"":FinanceiroConsultarGuiasDt.getDataInicioRecebimento())%>" />
						<img id="calendarioDataInicioRecebimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicioRecebimento,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataInicioRecebimento',''); return false;" title="Limpar Data Início Recebimento" />
						
						&nbsp;à&nbsp;
						
						<input name="dataFimRecebimento" id="dataFimRecebimento" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataFimRecebimento()==null?"":FinanceiroConsultarGuiasDt.getDataFimRecebimento())%>" />
						<img id="calendarioDataFimRecebimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFimRecebimento,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataFimRecebimento',''); return false;" title="Limpar Data Fim Recebimento" />
					   	
					   	<br />
						<label class="formEdicaoLabel">
							Data de Cancelamento
						</label><br>
						<input name="dataInicioCancelamento" id="dataInicioCancelamento" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataInicioCancelamento()==null?"":FinanceiroConsultarGuiasDt.getDataInicioCancelamento())%>" />
						<img id="calendarioDataInicioCancelamento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicioCancelamento,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataInicioCancelamento',''); return false;" title="Limpar Data Início Cancelamento" />
						
						&nbsp;à&nbsp;
						
						<input name="dataFimCancelamento" id="dataFimCancelamento" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataFimCancelamento()==null?"":FinanceiroConsultarGuiasDt.getDataFimCancelamento())%>" />
						<img id="calendarioDataFimCancelamento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFimCancelamento,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataFimCancelamento',''); return false;" title="Limpar Data Fim Cancelamento" />
						
						<br />
						<label class="formEdicaoLabel">
							Data de Emissão Certidão
						</label><br>
						<input name="dataInicioCertidao" id="dataInicioCertidao" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataInicioCertidao()==null?"":FinanceiroConsultarGuiasDt.getDataInicioCertidao())%>" />
						<img id="calendarioDataInicioCertidao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicioCertidao,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataInicioCertidao',''); return false;" title="Limpar Data Início Certidão" />
						
						&nbsp;à&nbsp;
						
						<input name="dataFimCertidao" id="dataFimCertidao" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=(FinanceiroConsultarGuiasDt.getDataFimCertidao()==null?"":FinanceiroConsultarGuiasDt.getDataFimCertidao())%>" />
						<img id="calendarioDataFimCertidao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFimCertidao,'dd/mm/yyyy',this)" />
						<img src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataFimCertidao',''); return false;" title="Limpar Data Fim Certidão" />
						
						
						<%if( FinanceiroConsultarGuiasDt.getOrdenacao() != null && FinanceiroConsultarGuiasDt.getOrdenacao().trim().length() > 0 ) { %>
							<br />
							<label class="formEdicaoLabel">
								Ordenação
							</label><br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							Crescente <input type="radio" id="ordenacao" name="ordenacao" value="1" <%=(FinanceiroConsultarGuiasDt.getOrdenacao().equals("1")?" checked ":"")%> />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							Decrescente <input type="radio" id="ordenacao" name="ordenacao" value="0" <%=(FinanceiroConsultarGuiasDt.getOrdenacao().equals("0")?" checked ":"")%> />
					   	<%} %>
					   	<br />
				   	
					</fieldset>
					<br/>
				
					<center>
						<button style="width:80px;" name="consultarGuias" value="Consultar" onclick="AlterarValue('PaginaEditar','-2'); javascript:buscaDadosJSON(obtenhaUrlJSON(), false, <%=Configuracao.Localizar%>, '1' , '0', <%=Configuracao.TamanhoRetornoConsulta%>); return false;" >
							Consultar
						</button>
						
						<button style="width:80px;" name="limparFormulario" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
							Limpar
						</button>
						
						<button style="width:80px;" name="consultarTotaisGuias" value="Totais" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); AlterarValue('PaginaEditar','-3');" >
							Totais
						</button>
					</center>
					
				</div>	
				<div id="divLocalizar" class="divLocalizar">
				
					<fieldset id="VisualizaDados" class="VisualizaDados">
		            	<legend>Guias</legend>
		          		<%
		          		List<GuiaEmissaoDt> liTemp = (List<GuiaEmissaoDt>)request.getSession().getAttribute("FinanceiroConsultarGuiasCt_ListaGuiaEmissaoDt");
		          		if( liTemp != null && liTemp.size() > 0 ) 
		          		{ %>
							
							<table id="TabelaTotal" class="Tabela">
								<thead>
									<tr>
										<th>Total Encontrado</th>
										<th>Valor Total das Guias</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>
											<center>
												<%= liTemp.size() %> &nbsp; Guia(s)
											</center>
										</td>
										<td>
											<center>
												R$ <%=Funcoes.FormatarDecimal(FinanceiroConsultarGuiasCt_ValorTotalGuiasConsultadas) %>
											</center>
										</td>
									</tr>
								</tbody>
							</table>
									
							<br />
		               <% } %>
		                		
						<table id="tabelaLocalizar" class="Tabela">
							<thead>
								<tr>
									<th>Nº</th>
									<th>Número Guia</th>
									<th>Tipo Guia</th>
									<th>Processo</th>
									<th>Data Emissão</th>
									<th>Data Vencimento</th>
									<th>Data Recebimento</th>
									<th>Data Cancelamento</th>
									<th>Situação</th>
									<th>Possui Débito</th>
									<th>Enviado Cadin</th>
									<th></th>
								</tr>
							</thead>
							<tbody id="CorpoTabela">&nbsp;</tbody>
						</table>
						
						<div id="Paginacao" class="Paginacao"></div>
					</fieldset>
				</div>
			</form>   
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
		
		<script type="text/javascript" >
			
			document.getElementById("numeroGuiaCompleto").focus();
		
			function obtenhaUrlJSON(){
				$('#TabelaTotal').remove();
				
				var urlRequest = 'FinanceiroConsultarGuias';
				
				urlRequest += "?numeroGuiaCompleto=" + $('#numeroGuiaCompleto').val();
				urlRequest += "&numeroProcesso=" + $('#numeroProcesso').val();
				urlRequest += "&Id_GuiaTipo=" + $('#Id_GuiaTipo').val();
				urlRequest += "&Id_GuiaStatus="+ $('#Id_GuiaStatus').val();
				urlRequest += "&dataInicioEmissao=" + $('#dataInicioEmissao').val();
				urlRequest += "&dataFimEmissao=" + $('#dataFimEmissao').val();
				urlRequest += "&dataInicioRecebimento=" + $('#dataInicioRecebimento').val();
				urlRequest += "&dataFimRecebimento=" + $('#dataFimRecebimento').val();
				urlRequest += "&dataInicioCancelamento=" + $('#dataInicioCancelamento').val();
				urlRequest += "&dataFimCancelamento=" + $('#dataFimCancelamento').val();
				urlRequest += "&dataInicioCertidao=" + $('#dataInicioCertidao').val();
				urlRequest += "&dataFimCertidao=" + $('#dataFimCertidao').val();
				urlRequest += "&ordenacao=" + $('input[name=ordenacao]:checked', '#FinanceiroConsultarGuias').val();
				urlRequest += "&PaginaEditar=" + $('#PaginaEditar').val();
				
				AlterarValue('PaginaEditar','-1');
				
				return urlRequest;
			}	
		</script>
	</body>
</html>