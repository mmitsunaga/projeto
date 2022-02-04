<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen'></link>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<script type="text/javascript">
			$(document).ready(function() {
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
				$("#DataPrimeiroRegime").mask("99/99/9999");
				$("#DataInicioSursis").mask("99/99/9999");
				$("#LiberdadeProvisoria").mask("99/99/9999");
				$("#PrisaoProvisoria").mask("99/99/9999");
			});
		</script>
	</head>
	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo;|<%=request.getAttribute("tempPrograma")%>| Editar dados da Ação Penal </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PassoEditarSalvarAcao" name="PassoEditarSalvarAcao" type="hidden" value="<%=request.getSession().getAttribute("PassoEditarSalvarAcao")%>">
				<input type="hidden" id="posicaoLista" name="posicaoLista">

				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo de Execução Penal</legend>
						<div> N&uacute;mero</div>
						<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>"><%=processoDt.getProcessoNumeroCompleto()%></a></span>
					</fieldset>
					<fieldset class="formEdicao"> <legend class="formEdicaoLegenda">Ação Penal</legend>
			    	    <br />
						<label class="formEdicaoLabel" for="NumeroAcaoPenal">Nº do Processo de Ação Penal:</label><br>    
					    <input class="formEdicaoInput" name="NumeroAcaoPenal" id="NumeroAcaoPenal" type="text" size="15" maxlength="15" value="<%=ProcessoExecucaodt_PE.getNumeroAcaoPenal()%>"/><br />
				    	<br />
						<label class="formEdicaoLabel" for="Id_CidadeOrigem">*Comarca de Origem: 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarCidade" name="imaLocalizarCidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" >							
					    </label><br> 
					    <input class="formEdicaoInput" name="CidadeOrigem" id="CidadeOrigem" type="text" size="54" maxlength="54" value="<%=ProcessoExecucaodt_PE.getCidadeOrigem()%>"/>				    		
						<input class="formEdicaoInputSomenteLeitura"  readonly name="EstadoOrigem" id="EstadoOrigem" type="text" size="2" maxlength="2" value="<%=ProcessoExecucaodt_PE.getUfOrigem()%>"/>
			    	    <br />
						<label class="formEdicaoLabel" for="VaraOrigem">*Vara de Origem:</label><br>    
					    <input class="formEdicaoInput" name="VaraOrigem" id="VaraOrigem" type="text" size="66" maxlength="66" value="<%=ProcessoExecucaodt_PE.getVaraOrigem()%>"/><br />
				    	<br />
						<%@ include file="ProcessoExecucaoDadosSentenca.jspf"%>
					</fieldset>
				    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('PassoEditar','<%=request.getSession().getAttribute("PassoEditarSalvarAcao")%>')">
				    </div>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>