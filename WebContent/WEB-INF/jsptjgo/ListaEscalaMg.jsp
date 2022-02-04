<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.ValidacaoUtil"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.EscalaMgDt"%>

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
				id="idEscalaMg" name="idEscalaMg" type="hidden"
				value="<%=request.getAttribute("idEscalaMg")%>" />

			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgVoltar" class="imgEditar" title="Voltar"
					name="imgVoltar" type="image" src="./imagens/imgRestaurar.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
			</div>

			<%
				escalaMgDt = (EscalaMgDt) request.getSession().getAttribute("escalaMgDt");
			%>

			<div id="divTabela" class="divTabela">

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Lista Escala</legend>
				</fieldset>

				<table id="Tabela" class="Tabela">



					<thead>
						<tr class="TituloColuna">
							<!--      		<td width="8%"/> -->
							<td></td>
							<td>Id</td>
							<td>Oficial</td>
							<td>Tipo Escala</td>
							<td>Data Inicio</td>
							<td>Data Final</td>
							<td>Retirar da Escala</td>
						</tr>
					</thead>
					<tbody id="listaEscalaMg">
						<%
							List liTemp = (List) request.getAttribute("listaEscalaMg");
							int contaOficiais = 0;
							boolean boLinha = false;
							if (liTemp != null) {
								for (int i = 0; i < liTemp.size(); i++) {
									escalaMgDt = (EscalaMgDt) liTemp.get(i);
									contaOficiais++;
						%>

						<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
	       
							<td><%=i + 1%></td>
							<td><%=escalaMgDt.getId()%></td>
							<td><%=escalaMgDt.getUsuario()%></td>
							<td><%=escalaMgDt.getEscalaTipoMg()%></td>
							<td><%=escalaMgDt.getDataInicio()%></td>
							<td><%=escalaMgDt.getDataFim()%></td>

							<%
								if (ValidacaoUtil.isVazio(escalaMgDt.getDataFim())) {
							%>

							<td><input id="imgAtualizar" class="imgAtualizar"
								title="Retira" name="imgAtualizar" type="image"
								src="./imagens/imgAtualizar.png"
								onclick="AlterarValue('idEscalaMg','<%=escalaMgDt.getId()%>');
							AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')">
							</td>

							<%
								}
							%>

						</tr>

						<%
							}
							}
						%>
					</tbody>

				</table>
			</div>

		</form>

	</div>

</body>

</html>