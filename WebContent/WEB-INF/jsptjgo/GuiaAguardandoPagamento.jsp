<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.BancoDt"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Guia Aguardando Pagamento</title>
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
			|<%=request.getAttribute("tempPrograma")%>| Guia Aguardando Pagamento
		</h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" target="Principal" method="post" name="Formulario" >
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
			
			<div id="divEditar" class="divEditar">
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>
						Status Guia - <font color="red">Aguardando Pagamento</font>
					</legend>
					
					<%
                	if( processoDt != null ) {
                	%>
                		<div> Número </div>
						<span><a href="<%=request.getParameter("tempRetornoBuscaProcesso").toString()%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
						<br/>
					<%}%>
					
					<div>Banco Escolhido</div>
					<span class="span1"><%=request.getSession().getAttribute("nomeBanco").toString()%></span>
					<br />
					
					<div>Data de Vencimento</div>
					<span class="span1"><%=GuiaEmissaoDt.getDataVencimento()%></span>
					<br /><br />
					
					<div>Número da Guia</div>
					<span class="span1">
						<font color="red"><%=Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDt.getNumeroGuiaCompleto())%></font>
					</span>
					<br />
					
					<div>Valor da Guia</div>
					<span class="span1">R$ <%=request.getSession().getAttribute("TotalGuia") %></span>
					<br />
					
				</fieldset>
			</div>
			
			<br />
			
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<fieldset class="fieldsetEndereco">
						<legend>Atenção</legend>
						
						<em>
						1- Guarde sempre os comprovantes de suas transações financeiras.
						<p />
						2- Guarde o número desta Guia.
						</em>
						
					</fieldset>
				</fieldset>
			</div>
		</form>
		
	</div>
</body>
</html>