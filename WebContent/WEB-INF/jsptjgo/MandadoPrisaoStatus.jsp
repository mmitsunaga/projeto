<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="MandadoPrisaoStatusdt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisaoStatus  </title>
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
				if (SeNulo(MandadoPrisaoStatusCodigo, "O Campo MandadoPrisaoStatusCodigo é obrigatório!")) return false;
				if (SeNulo(MandadoPrisaoStatus, "O Campo MandadoPrisaoStatus é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de MandadoPrisaoStatus</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="MandadoPrisaoStatus" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="MandadoPrisaoStatus" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/MandadoPrisaoStatusAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados MandadoPrisaoStatus </legend>
					<label class="formEdicaoLabel" for="Id_MandadoPrisaoStatus">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_MandadoPrisaoStatus"  id="Id_MandadoPrisaoStatus"  type="text"  readonly="true" value="<%=MandadoPrisaoStatusdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="MandadoPrisaoStatus">*MandadoPrisaoStatus</label><br> <input class="formEdicaoInput" name="MandadoPrisaoStatus" id="MandadoPrisaoStatus"  type="text" size="60" maxlength="60" value="<%=MandadoPrisaoStatusdt.getMandadoPrisaoStatus()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="MandadoPrisaoStatusCodigo">*Código</label><br> <input class="formEdicaoInput" name="MandadoPrisaoStatusCodigo" id="MandadoPrisaoStatusCodigo"  type="text" size="6" maxlength="6" value="<%=MandadoPrisaoStatusdt.getMandadoPrisaoStatusCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,6)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
