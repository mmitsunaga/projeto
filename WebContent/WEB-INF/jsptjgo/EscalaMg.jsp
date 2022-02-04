<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="br.gov.go.tj.projudi.util.Mensagem"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.EscalaMgDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EscalaTipoMgDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>

<jsp:useBean id="escalaMgDt" scope="session"
	class="br.gov.go.tj.projudi.dt.EscalaMgDt" />

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

		<form action="EscalaMg" method="post" name="Formulario"
			id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				name="__Pedido__" type="hidden"
				value="<%=request.getAttribute("__Pedido__")%>" /> <input
				id="Fluxo" name="Fluxo" type="hidden"
				value="<%=request.getAttribute("Fluxo")%>" />

			<div id="divPortaBotoes" class="divPortaBotoes">

				<input id="imgLocalizar" class="imgLocalizar"
					title="Consultar Todos" name="imgLocalizar" type="image"
					src="./imagens/imgLocalizar.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')">


				<input id="imgLocalizarSalvar" class="imgSalvar" title="Salvar"
					name="imgSalvar" type="image" src="./imagens/imgSalvar.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); 
					             AlterarValue('Fluxo','cadastraEscala')">
			</div>

			<%
				escalaMgDt = (EscalaMgDt) request.getAttribute("escalaMgDt");
			%>

			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao" id="fieldsetUsuario">

					<input id="idUsuarioEscalaTipoMg" name="idUsuarioEscalaTipoMg"
						type="hidden" value="<%=escalaMgDt.getId()%>" /> <label
						class="formEdicaoLabel" for="Id_Usuario">*Oficial <input
						class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario"
						name="imaLocalizarUsuario" type="image"
						src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
						<input class="FormEdicaoimgLocalizar" id="imaLimparUsuario"
						name="imaLimparUsuario" type="image"
						src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
					</label><br> <input id="Id_Usuario" name="Id_Usuario" type="hidden"
						value="<%=escalaMgDt.getIdUsuario()%>" /> <input
						class="formEdicaoInputSomenteLeitura" id="Usuario" name="Usuario"
						readonly="true" type="text" size="60" maxlength="60"
						value="<%=escalaMgDt.getUsuario()%>" /><br />
				</fieldset>


				<fieldset class="formEdicao" id="fieldEscalaTipoMg">

					<label class="formEdicaoLabel" for="tipoArquivo">*Escala</label><br>

					<select id="idEscalaTipoMg" name="idEscalaTipoMg"
						class="formEdicaoCombo">
						<%
							List listTemp = (List) request.getAttribute("listaEscalaTipoMg");
							if (listTemp != null) {
						%>
						<option value=""></option>
						<%
							EscalaTipoMgDt escalaTipoMgDt;
								for (int i = 0; i < listTemp.size(); i++) {
									escalaTipoMgDt = (EscalaTipoMgDt) listTemp.get(i);
						%>
						<option value="<%=escalaTipoMgDt.getId()%>"
							<%if (escalaTipoMgDt.getId() != null
							&& escalaTipoMgDt.getId().equals(request.getAttribute("idEscalaTipoMg"))) {%>
							selected="selected" <%}%>>
							<%=escalaTipoMgDt.getEscalaTipoMg()%>
						</option>
						<%
							}
							}
						%>
					</select><br />

				</fieldset>

			</div>

			<%@ include file="Padroes/MensagemErro.jsp"%>

			<%@ include file="Padroes/MensagemOk.jsp"%>


			<%
				if (!request.getAttribute("Mensagem").equals("")) {
			%>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			<%
				}
			%>

		</form>

	</div>

</body>

</html>