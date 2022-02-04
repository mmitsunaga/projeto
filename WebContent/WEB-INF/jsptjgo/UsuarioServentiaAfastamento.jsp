<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.UsuarioAfastamentoDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt"%>
<jsp:useBean id="usuarioServentiaAfastamentoDt" scope="session"
	class="br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt" />
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<jsp:useBean id="usuarioDt" scope="session"
	class="br.gov.go.tj.projudi.dt.UsuarioDt" />

<html>
<head>
<title>Afastamento / Retorno do Oficial de Justiça</title>

<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">

<style type="text/css">
@import url('./css/Principal.css');
</style>

<script type='text/javascript' src='./js/Funcoes.js'></script>

</head>

<body>

<%
		usuarioServentiaAfastamentoDt = (UsuarioServentiaAfastamentoDt) request
				.getAttribute("usuarioServentiaAfastamentoDt");
	%>

	<div id="divCorpo" class="divCorpo">
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post"
			name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden"
				value="<%=request.getAttribute("PosicaoPaginaAtual")%>" /> <input
				id="PaginaAnterior" name="PaginaAnterior" type="hidden"
				value="<%=request.getAttribute("PaginaAnterior")%>" /> <input
				id="idUsuarioServentiaAfastamento"
				name="idUsuarioServentiaAfastamento" type="hidden"
				value="<%=request.getAttribute("idUsuarioServentiaAfastamento")%>" />

			<h2>&raquo; Afastamento / Retorno do Oficial de Justiça</h2>

			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">
						<%=request.getAttribute("nomeServentia")%>
					</legend>

					<br> <input id="idUsuario" name="idUsuario" type="hidden"
						value="<%=usuarioServentiaAfastamentoDt.getId_Usuario()%>" /> <label
						class="formEdicaoLabel" for="Id_Usuario">*Oficial &emsp;<input
						class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario"
						name="imaLocalizarUsuario" type="image" title="Localizar"
						src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">

					</label><br> <input id="Id_Usuario" name="Id_Usuario" type="hidden"
						value="<%=usuarioServentiaAfastamentoDt.getId_Usuario()%>" /> <input
						class="formEdicaoInputSomenteLeitura" id="Usuario" name="Usuario"
						readonly="true" type="text" size="60" maxlength="60"
						value="<%=usuarioServentiaAfastamentoDt.getNomeUsuario()%>" /><br />

				</fieldset>

			</div>

			<fieldset class="formEdicao" id="fieldsetUsuarioServentiaAfastamento">

				<table id="Tabela" class="Tabela">
					<thead>
						<tr class="TituloColuna">
							<td></td>
							<!-- <td>Id</td> 
					 		<td>Nome</td>
					 		<td>Cpf</td> -->
							<td>Afastamento</td>
							<td>Usuário Cadastrador</td>
							<td>DataInicio</td>
							<td>Usuário Finalizador</td>
							<td>DataFim</td>
						</tr>
					</thead>
					<tbody id="listaUsuarioServentiaAfastamento">
						<%
							List liTemp = (List) request.getAttribute("listaUsuarioServentiaAfastamento");
							int contaAfastamento = 0;
							boolean boLinha = false;
							if (liTemp != null) {
								for (int i = 0; i < liTemp.size(); i++) {
									usuarioServentiaAfastamentoDt = (UsuarioServentiaAfastamentoDt) liTemp.get(i);
									contaAfastamento++;
						%>

						<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">

							<td><%=i + 1%></td>
							<td><%=usuarioServentiaAfastamentoDt.getAfastamento()%></td>
							<td><%=usuarioServentiaAfastamentoDt.getNomeUsuarioCadastrador()%></td>
							<td><%=usuarioServentiaAfastamentoDt.getDataInicio()%></td>
							<td><%=usuarioServentiaAfastamentoDt.getNomeUsuarioFinalizador()%></td>
							<td><%=usuarioServentiaAfastamentoDt.getDataFim()%></td>

						</tr>

						<% 
							}
							}
						%>

					</tbody>

				</table>
			</fieldset>

			<%
				if (usuarioServentiaAfastamentoDt.getNomeUsuario() != null) {
					if (!usuarioServentiaAfastamentoDt.getNomeUsuario().equalsIgnoreCase("")) {
			%>

			<div id="divPortaBotoes" class="divBotoesCentralizados">

				<button
					onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');"
					name="botaoAfastarRetornar">Confirma</button>
			</div>
			<%
				}
				}
			%>

			<%@ include file="Padroes/MensagemErro.jsp"%>
			<%@ include file="Padroes/MensagemOk.jsp"%>
		</form>
	</div>
</body>
</html>