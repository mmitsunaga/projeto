<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ObjetoPedidoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="ObjetoPedidodt" scope="session" class= "br.gov.go.tj.projudi.dt.ObjetoPedidoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Objeto Pedido  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ObjetoPedido, "O campo Desrição é obrigatório!")) return false;
				if (SeNulo(ObjetoPedidoCodigo, "O campo Código é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Objeto Pedido</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ObjetoPedido" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ObjetoPedido" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ObjetoPedidoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Objeto Pedido </legend>
					<label class="formEdicaoLabel" for="Id_ObjetoPedido">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ObjetoPedido"  id="Id_ObjetoPedido"  type="text"  readonly="true" value="<%=ObjetoPedidodt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="ObjetoPedido">*Descrição</label><br> <input class="formEdicaoInput" name="ObjetoPedido" id="ObjetoPedido"  type="text" size="60" maxlength="60" value="<%=ObjetoPedidodt.getObjetoPedido()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="ObjetoPedidoCodigo">*Código</label><br> <input class="formEdicaoInput" name="ObjetoPedidoCodigo" id="ObjetoPedidoCodigo"  type="text" size="11" maxlength="11" value="<%=ObjetoPedidodt.getObjetoPedidoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
