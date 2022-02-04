<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="MandadoPrisaoOrigemdt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisaoOrigem  </title>
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
				if (SeNulo(MandadoPrisaoOrigemCodigo, "O Campo MandadoPrisaoOrigemCodigo é obrigatório!")) return false;
				if (SeNulo(MandadoPrisaoOrigem, "O Campo MandadoPrisaoOrigem é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de MandadoPrisaoOrigem</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="MandadoPrisaoOrigem" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="MandadoPrisaoOrigem" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/MandadoPrisaoOrigemAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados MandadoPrisaoOrigem </legend>
					<label class="formEdicaoLabel" for="Id_MandadoPrisaoOrigem">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_MandadoPrisaoOrigem"  id="Id_MandadoPrisaoOrigem"  type="text"  readonly="true" value="<%=MandadoPrisaoOrigemdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="MandadoPrisaoOrigem">*MandadoPrisaoOrigem</label><br> <input class="formEdicaoInput" name="MandadoPrisaoOrigem" id="MandadoPrisaoOrigem"  type="text" size="60" maxlength="60" value="<%=MandadoPrisaoOrigemdt.getMandadoPrisaoOrigem()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="MandadoPrisaoOrigemCodigo">*Codigo</label><br> <input class="formEdicaoInput" name="MandadoPrisaoOrigemCodigo" id="MandadoPrisaoOrigemCodigo"  type="text" size="60" maxlength="60" value="<%=MandadoPrisaoOrigemdt.getMandadoPrisaoOrigemCodigo()%>" onkeyup=" autoTab(this,60)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
