<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt"%>
<jsp:useBean id="relatorioMandadoDt" scope="session"
	class="br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt" />
<html>
<head>
<title><%=request.getAttribute("tempPrograma")%></title>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
<style type="text/css">
@import url('./css/Principal.css');

@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
</style>
<script type='text/javascript' src='./dwr/engine.js'></script>
<script type='text/javascript' src='./dwr/util.js'></script>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type='text/javascript' src='/js/jquery.js'></script>
<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/DigitarSoNumero.js"></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/AutoTab.js"></script>
<script type="text/javascript"
	src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>
				&raquo;
				<%=request.getAttribute("tempPrograma")%>
			</h2>
		</div>
		<form action="RelatoriosMandado" method="post" name="Formulario"
			id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				name="TituloPagina" type="hidden"
				value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgImprimir" class="imgImprimir" title="Gerar Relatório"
					name="imgImprimir" type="image" src="./imagens/imgImprimir.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
			</div>
			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Para os relatórios de
						mandados gratuitos e mandados Ad Hoc, digite o ultimo dia 
						do mês de competência, na Data Inicial.</legend>


					<label class="formEdicaoLabel" for="DataInicial">*Data Inicial</label><br> <input class="formEdicaoInput" name="DataInicial"
						id="DataInicial" type="text" size="10" maxlength="10"
						value="<%=relatorioMandadoDt.getDataInicial()%>"
						onkeyup="mascara_data(this)" onblur="verifica_data(this)">
					<img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif"
						height="13" width="13" title="Calendário" alt="Calendário"
						onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" />
					<br /> <label class="formEdicaoLabel" for="DataFinal">*Data
						Final</label><br> <input class="formEdicaoInput" name="DataFinal"
						id="DataFinal" type="text" size="10" maxlength="10"
						value="<%=relatorioMandadoDt.getDataFinal()%>"
						onkeyup="mascara_data(this)" onblur="verifica_data(this)">
					<img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif"
						height="13" width="13" title="Calendário" alt="Calendário"
						onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" /><br />

				</fieldset>

				<fieldset class="formEdicao">

					<label class="formEdicaoLabel" for="tipoOpcao">*Tipo
						Relatório</label><br> <select id="tipoOpcao" name="tipoOpcao"
						class="formEdicaoCombo">
						<%-- option value="" ></option>  --%>
						<option value="1"> Relatório Mensal de Mandados Gratuitos</option>
						<option value="2"> Relatório de Mandados com Custas</option>
						<option value="3"> Relatório de Mandados AD Hoc</option>
						<option value="4"> Relatório de Guias Vinculadas ao Oficial de Justiça</option>
					</select><br />

				</fieldset>

				<%
					String grupoNome = (String) request.getAttribute("grupoNome");
					String literal = "";
					if (grupoNome.equalsIgnoreCase("analistaFinanceiro"))
						literal = "Gerar Texto de Créditos para Envio ao Banco (Somente para Mandados Gratuitos)";
				%>

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Tipo de Arquivo</legend>
					<label class="formEdicaoLabel" for="tipoArquivo">*Tipo de
						Arquivo</label><br> <input type="radio" name="tipoArquivo" value="1"
						checked="true" />Relatório
				<%
 	                if (grupoNome.equalsIgnoreCase("analistaFinanceiro"))
                %>
					<input type="radio" name="tipoArquivo" value="2" /><%=literal%>

				</fieldset>
			</div>
			
		</form>
		<%@ include file="Padroes/Mensagens.jspf"%>
	</div>
</body>
</html>