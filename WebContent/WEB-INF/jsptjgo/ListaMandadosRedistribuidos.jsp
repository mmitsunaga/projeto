<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<jsp:useBean id="relatorioMandadoDt" scope="session"
	class="br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt" />
<jsp:useBean id="usuarioDt" scope="session"
	class="br.gov.go.tj.projudi.dt.UsuarioDt" />


<html>
<head>
<title>Mandados Distribuídos por Período</title>

<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">

<style type="text/css">
@import url('./css/Principal.css');

@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
</style>

<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type='text/javascript'
	src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>

</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<form action="" RelatoriosMandado" method="post" name="Formulario"
			id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden"
				value="<%=request.getAttribute("PosicaoPaginaAtual")%>" /> <input
				id="PaginaAnterior" name="PaginaAnterior" type="hidden"
				value="<%=request.getAttribute("PaginaAnterior")%>" />

			<div id="divEditar" class="divEditar">
				<h2>&raquo; Mandados Redistribuídos no Período</h2>

				<input id="imgImprimir" class="imgImprimir" title="Gerar Relatório"
					name="imgImprimir" type="image" src="./imagens/imgImprimir.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')">
			</div>


			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao">


					<%
						String grupoNome = (String) request.getAttribute("grupoNome");
						if (!grupoNome.equalsIgnoreCase("oficialJustica")) {
					%>


					<fieldset class="formEdicao" id="fieldsetUsuario">

						<input id="idUsuario" name="idUsuario"
							type="hidden" value="<%=relatorioMandadoDt.getIdUsuario()%>" />
						<label class="formEdicaoLabel" for="Id_Usuario">Oficial <input
							class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario"
							name="imaLocalizarUsuario" type="image"
							src="./imagens/imgLocalizarPequena.png"
							onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
							<input class="FormEdicaoimgLocalizar" id="imaLimparUsuario"
							name="imaLimparUsuario" type="image"
							src="./imagens/16x16/edit-clear.png"
							onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
						</label><br> <input id="Id_Usuario" name="Id_Usuario" type="hidden"
							value="<%=relatorioMandadoDt.getIdUsuario()%>" /> <input
							class="formEdicaoInputSomenteLeitura" id="Usuario" name="Usuario"
							readonly="true" type="text" size="60" maxlength="60"
							value="<%=relatorioMandadoDt.getNomeUsuario()%>" /><br />


					</fieldset>

					<%
						}
					%>

					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Período</legend>
						<label class="formEdicaoLabel" for="DataInicial">*Data
							Inicial</label><br> <input class="formEdicaoInput"
							name="DataInicial" id="DataInicial" type="text" size="10"
							maxlength="10" value="<%=relatorioMandadoDt.getDataInicial()%>"
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

				</fieldset>
			</div>


			<%@ include file="Padroes/MensagemErro.jsp"%>
			<%@ include file="Padroes/MensagemOk.jsp"%>


		</form>



	</div>
</body>
</html>