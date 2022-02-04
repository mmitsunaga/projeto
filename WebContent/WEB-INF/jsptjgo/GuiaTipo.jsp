<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="GuiaTipodt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Guia</title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(GuiaTipo, "O Campo Descrição é obrigatório!")) return false;
				if (SeNulo(GuiaTipoCodigo, "O Campo Código Tipo de Guia é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tipo de Guia</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="GuiaTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		<form action="GuiaTipo" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/GuiaTipoAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Editar os dados de Tipo de Guia</legend>
					<label class="formEdicaoLabel" for="Id_GuiaTipo">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_GuiaTipo" id="Id_GuiaTipo" type="text" readonly="true" value="<%=GuiaTipodt.getId()%>" />
					<br />
					
					<label class="formEdicaoLabel" for="GuiaTipo">*Descrição</label><br>
					<input class="formEdicaoInput" name="GuiaTipo" id="GuiaTipo" type="text" size="60" maxlength="60" value="<%=GuiaTipodt.getGuiaTipo()%>" onkeyup=" autoTab(this,60)" />
					<br />
					
					<label class="formEdicaoLabel" for="GuiaTipoCodigo">*Código Tipo de Guia</label><br>
					<input class="formEdicaoInput" name="GuiaTipoCodigo" id="GuiaTipoCodigo" type="text" size="11" maxlength="11" value="<%=GuiaTipodt.getGuiaTipoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)" />
					<br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
