<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ObjetoTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ObjetoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ObjetoTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ObjetoTipo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script language="javascript" type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
<%-- 	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%> --%>
<!-- 		<script language="javascript" type="text/javascript"> -->
<!--  		function VerificarCampos() { -->
<!-- 			with(document.Formulario) { -->
<!-- 			if (SeNulo(ObjetoTipo, "O Campo ObjetoTipo é obrigatório!")) return false; -->
<!-- 				submit(); -->
<!-- 			} -->
<!-- 		} -->
<!-- 		</script> -->

<%-- 	<%}%> --%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de ObjetoTipo</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ObjetoTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ObjetoTipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>					<a class="divPortaBotoesLink" href="Ajuda/ObjetoTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				</div>
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados ObjetoTipo </legend>
					<label class="formEdicaoLabel" for="Id_ObjetoTipo">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_ObjetoTipo"  id="Id_ObjetoTipo"  type="text"  readonly="true" value="<%=ObjetoTipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="ObjetoTipo">*ObjetoTipo</label> <input class="formEdicaoInput" name="ObjetoTipo" id="ObjetoTipo"  type="text" size="60" maxlength="60" value="<%=ObjetoTipodt.getObjetoTipo()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Codigo">Codigo</label> <input class="formEdicaoInput" name="Codigo" id="Codigo"  type="text" size="22" maxlength="22" value="<%=ObjetoTipodt.getCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
