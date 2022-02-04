<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.EscalaMgDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="escalaMgDt" scope="session"
	class="br.gov.go.tj.projudi.dt.EscalaMgDt" />

<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />

<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<link href="./css/form.css" type="text/css" rel="stylesheet" />
<link href="./css/font-awesome-4.4.0/css/font-awesome.min.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./js/jquery.js"></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/DigitarSoNumero.js"></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/AutoTab.js"></script>

</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>
				&raquo;
				<%=request.getAttribute("tempPrograma")%>
			</h2>
		</div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post"
			name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden"
				value="<%=request.getAttribute("PosicaoPaginaAtual")%>" /> <input
				id="PaginaAnterior" name="PaginaAnterior" type="hidden"
				value="<%=request.getAttribute("PaginaAnterior")%>" /> <input
				id="Fluxo" name="Fluxo" type="hidden"
				value="<%=request.getAttribute("Fluxo")%>" /> <input
				id="idEscalaMg" name="idEscalaMg" type="hidden"
				value="<%=request.getAttribute("idEscalaMg")%>" /> <input
				name="__Pedido__" type="hidden"
				value="<%=request.getAttribute("__Pedido__")%>" />


			<div id="divPortaBotoes" class="divPortaBotoes">

				<%
					escalaMgDt = (EscalaMgDt) request.getAttribute("escalaMgDt");
				%>

				<input id="imgRetirar" class="imgEditar" title="Retirar"
					name="imgRetirar" type="image" src="./imagens/22X22/ico_erro.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); 
					         AlterarValue('Fluxo','alteraEscala');
					         AlterarValue('idEscalaMg','<%=escalaMgDt.getId()%>')">

				<input id="imgVoltar" class="imgEditar" title="Voltar"
					name="imgVoltar" type="image" src="./imagens/imgRestaurar.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')">

			</div>


			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">

					<legend class="formEdicaoLegenda">Retira Oficial da Escala</legend>

					<label class="formEdicaoLabel" for="id">Id</label> <br> <input
						class="formEdicaoInputSomenteLeitura" name="id" id="id"
						type="text" readonly="readonly" size="5"
						value="<%=escalaMgDt.getId()%>" /> <br> <label
						class="formEdicaoLabel" for="statusSomenteLeitura">Oficial</label>
					<br> <input class="formEdicaoInputSomenteLeitura"
						name="usuario" id="usuario" type="text" readonly="readonly"
						size="50" value="<%=escalaMgDt.getUsuario()%>" /> <br> <label
						class="formEdicaoLabel" for="tipoEscala">Tipo Escala</label> <br>
					<input class="formEdicaoInputSomenteLeitura" name="tipoEscala"
						id="tipoEscala" type="text" readonly="readonly" size="10"
						value="<%=escalaMgDt.getEscalaTipoMg()%>" /> <br> <label
						class="formEdicaoLabel" for="tipoEscala">Data Inicio</label> <br>
					<input class="formEdicaoInputSomenteLeitura" name="dataInicio"
						id="dataInicio" type="text" readonly="readonly" size="10"
						value="<%=escalaMgDt.getDataInicio()%>" />
				</fieldset>

			</div>
	</div>
	<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
	</form>
	</div>

</body>


</html>
