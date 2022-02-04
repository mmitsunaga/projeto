<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.BancoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Pagamento On-Line</title>
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
	  	
		<div class="area"><h2>&raquo; 
			|<%=request.getAttribute("tempPrograma")%>| Pagamento On-Line
		</h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" target="Principal" method="post" name="<%=request.getAttribute("nomeForm") %>" >
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
			
			<%
			Map inputHiddens = (Map) request.getAttribute("inputHiddens");
			if( inputHiddens != null ) {
				Iterator it = inputHiddens.entrySet().iterator();
				while( it.hasNext() ) {
					Map.Entry dados = (Map.Entry)it.next();
					
					%>
					<input type="hidden" name="<%=dados.getKey()%>" value="<%=dados.getValue()%>" />
					<%
				}
			}
			%>
			
			<div id="divEditar" class="divEditar">
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Status da Guia - <font color="red">Aguardando Pagamento</font>
					</legend>
					
					<%
					ProcessoDt processoDt = (ProcessoDt)request.getSession().getAttribute("processoDt");
                	if( processoDt != null ) {
                	%>
                		<div style="width: 150px;"> Número do Processo </div>
						<span>
							<%if( request.getAttribute("visualizarLinkProcesso") != null && (Boolean)request.getAttribute("visualizarLinkProcesso") ) { %>
							<a href="<%=request.getParameter("tempRetornoBuscaProcesso").toString()%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2">
							<%} %>
								<%=processoDt.getProcessoNumero()%>
							<%if( request.getAttribute("visualizarLinkProcesso") != null && (Boolean)request.getAttribute("visualizarLinkProcesso") ) { %>
							</a>
							<%} %>
							
						</span>
						<br/>
					<%}%>
					
					<div style="width: 150px;">Banco Escolhido</div>
					<span class="span1"><%=request.getSession().getAttribute("nomeBanco").toString()%></span>
					<br />
					
					<div style="width: 150px;">Data de Vencimento</div>
					<span class="span1"><%=request.getAttribute("DataVencimentoGuia").toString()%></span>
					<br /><br />
					
					<div style="width: 150px;">Número da Guia</div>
					<span class="span1">
						<font color="red"><%=Funcoes.FormatarNumeroSerieGuia(request.getAttribute("numeroGuiaCompleto").toString())%></font>
					</span>
					<br />
					
					<div style="width: 150px;">Valor da Guia</div>
					<span class="span1">R$ <%=request.getAttribute("TotalGuia").toString() %></span>
					<br />
					
				</fieldset>
			</div>
			
			<br />
			
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<fieldset class="fieldsetEndereco">
						<legend>Atenção</legend>
						
						<em>
						1- Sua senha é de uso pessoal e intransferível.
               			<p />
               			2- Guarde o número desta Guia.
               			<p />
						3- Guarde sempre os comprovantes de suas transações financeiras.
						</em>
						
					</fieldset>
				</fieldset>
			</div>
			
			<br />
			
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<%
				String banco = request.getAttribute("banco").toString();
				%>
				<%if( banco.equals(String.valueOf(BancoDt.BANCO_DO_BRASIL)) ) { %>
				<button name="imgBancoBrasil" type="submit">
					<img src="./imagens/bancobrasil.png" alt="Pagamento via Banco do Brasil" />
					<br />
					Banco do Brasil
				</button>
				<%} %>
				
				<%if( banco.equals(String.valueOf(BancoDt.ITAU)) ) { %>
				<button name="imgItau" type="submit">
					<img src="./imagens/itau.gif" width="30" height="40" alt="Pagamento via Banco Itaú" />
					<br />
					Banco Itaú
				</button>
				<%} %>
				
				<%if( banco.equals(String.valueOf(BancoDt.CAIXA_ECONOMICA_FEDERAL)) ) { %>
				<button name="imgCEF" type="submit">
					<img src="./imagens/cef.png" width="90" height="40" alt="Pagamento via Caixa Econômica" />
					<br />
					Caixa Econômica
				</button>
				<%} %>
			</div>
			
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>