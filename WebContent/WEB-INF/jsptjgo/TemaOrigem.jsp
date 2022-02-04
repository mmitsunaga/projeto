<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.TemaOrigemDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="TemaOrigemdt" scope="session" class= "br.gov.go.tj.projudi.dt.TemaOrigemDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tema Origem  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(TemaOrigem, "O campo Tema Origem é obrigatório.")) return false;
				if (SeNulo(TemaOrigemCodigo, "O campo Código é obrigatório.")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tema Origem</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="TemaOrigem" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="TemaOrigem" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/TemaOrigemAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Tema Origem </legend>
					<label class="formEdicaoLabel" for="Id_TemaOrigem">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_TemaOrigem"  id="Id_TemaOrigem"  type="text"  readonly="true" value="<%=TemaOrigemdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="TemaOrigem">*Tema Origem</label><br> <input class="formEdicaoInput" name="TemaOrigem" id="TemaOrigem"  type="text" size="60" maxlength="60" value="<%=TemaOrigemdt.getTemaOrigem()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="TemaOrigemCodigo">*Código</label><br> <input class="formEdicaoInput" name="TemaOrigemCodigo" id="TemaOrigemCodigo"  type="text" size="10" maxlength="10" value="<%=TemaOrigemdt.getTemaOrigemCodigo()%>" onkeyup=" autoTab(this,60)" onkeypress="return DigitarSoNumero(this, event)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
