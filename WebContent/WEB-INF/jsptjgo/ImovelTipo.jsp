<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ImovelTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Imoveltipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ImovelTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Imóvel Tipo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ImovelTipo, "O Campo Imóvel Tipo é obrigatório!")) return false;
				submit();
			}
		}
		</script>
	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div  class="area"><h2> Cadastro de Imóvel Tipo</h2></div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
					<form action="ImovelTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
					<form action="ImovelTipo" method="post" name="Formulario" id="Formulario">
			<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ImoveltipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				</div>
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Imóvel Tipo </legend>
					<label class="formEdicaoLabel" for="Id_ImovelTipo">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_ImovelTipo"  id="Id_ImovelTipo"  type="text"  readonly="true" value="<%=Imoveltipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="ImovelTipo">*Imóvel Tipo</label> <input class="formEdicaoInput" name="ImovelTipo" id="ImovelTipo"  type="text" size="60" maxlength="60" value="<%=Imoveltipodt.getImovelTipo()%>" onkeyup=" autoTab(this,60)"><br />
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>