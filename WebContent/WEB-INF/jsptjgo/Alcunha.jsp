<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AlcunhaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Alcunhadt" scope="session" class= "br.gov.go.tj.projudi.dt.AlcunhaDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Alcunha  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Alcunha, "O campo Descrição é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Alcunha</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Alcunha" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Alcunha" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/AlcunhaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Alcunha </legend>
					<label class="formEdicaoLabel" for="Id_Alcunha">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Alcunha"  id="Id_Alcunha"  type="text"  readonly="true" value="<%=Alcunhadt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Alcunha">*Descrição</label><br> <input class="formEdicaoInput" name="Alcunha" id="Alcunha"  type="text" size="60" maxlength="60" value="<%=Alcunhadt.getAlcunha()%>" onkeyup=" autoTab(this,60)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>

		</form>
	</div>
			<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
