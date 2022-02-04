<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"%>
<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>
<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen'></link>
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>

		<script type="text/javascript">
			function onLoad(){
				$("#DataDistribuicao").mask("99/99/9999");
				$("#DataPronuncia").mask("99/99/9999");
				$("#DataDenuncia").mask("99/99/9999");
				$("#DataAcordao").mask("99/99/9999");
				$("#DataSentenca").mask("99/99/9999");
				$("#DataAdmonitoria").mask("99/99/9999");
				$("#DataTransito").mask("99/99/9999");
				$("#DataTJ_MP").mask("99/99/9999");
				$("#DataInicioCumprimentoPena").mask("99/99/9999");
				$("#DataFato").mask("99/99/9999");
				$("#DataFato_0").mask("99/99/9999");
				$("#DataFato_1").mask("99/99/9999");
				$("#DataFato_2").mask("99/99/9999");
				$("#DataFato_3").mask("99/99/9999");
				$("#DataFato_4").mask("99/99/9999");
				$("#DataFato_5").mask("99/99/9999");
				$("#PrisaoProvisoria").mask("99/99/9999");
				$("#LiberdadeProvisoria").mask("99/99/9999");
				$("#DataPrimeiroRegime").mask("99/99/9999");
				$("#DataInicioSursis").mask("99/99/9999");
			}
		</script>
	</head>
	<body onload="onLoad()">
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Inserção dos dados da Condenação</h2></div>

			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario" >
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
<!--				<input id="Id_PenaExecucaoTipo_Consulta" name="Id_PenaExecucaoTipo_Consulta" type="hidden" value="<%=request.getAttribute("Id_PenaExecucaoTipo_Consulta")%>">-->

				<%@ include file="ProcessoExecucaoPassos.jspf" %>
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Sentença Condenatória</legend>
						<%@ include file="ProcessoExecucaoDadosSentenca.jspf"%>
					</fieldset>
					<%@ include file="ProcessoExecucaoDadosPrisao.jspf"%>
<%if (ProcessoExecucaodt_PE.getId_ProcessoExecucaoPenal().length() == 0 || ProcessoExecucaodt_PE.getId_ProcessoExecucaoPenal().equals("null") || ProcessoExecucaodt_PE.getDataPrimeiroRegime().length() > 0){ %>
					<%@ include file="SituacaoAtualExecucao.jsp"%>
<%} %>
				   	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','5');"> 
				    </div>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>