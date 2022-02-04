<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ArquivoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ArquivoTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ArquivoTipo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ArquivoTipo, "O Campo ArquivoTipo é obrigatório!")) return false;
				if (SeNulo(ArquivoTipoCodigo, "O Campo ArquivoTipoCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de ArquivoTipo</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ArquivoTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ArquivoTipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ArquivoTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados ArquivoTipo </legend>
					<label class="formEdicaoLabel" for="Id_ArquivoTipo">Id_ArquivoTipo</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ArquivoTipo"  id="Id_ArquivoTipo"  type="text"  readonly="true" value="<%=ArquivoTipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="ArquivoTipo">*ArquivoTipo</label><br> <input class="formEdicaoInput" name="ArquivoTipo" id="ArquivoTipo"  type="text" size="60" maxlength="60" value="<%=ArquivoTipodt.getArquivoTipo()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="ArquivoTipoCodigo">*ArquivoTipoCodigo</label><br> <input class="formEdicaoInput" name="ArquivoTipoCodigo" id="ArquivoTipoCodigo"  type="text" size="11" maxlength="11" value="<%=ArquivoTipodt.getArquivoTipoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br /><br />
					<input class="formEdicaoInput" name="Publico" id="Publico"  type="checkbox"  value="true" <% if(ArquivoTipodt.getPublico().equalsIgnoreCase("true")){%>  checked<%}%>>
					<label for="Publico">Público - os arquivos desse tipo serão visualizados na Consulta Pública</label><br /><br />					
					<input class="formEdicaoInput" name="Dje" id="Dje"  type="checkbox"  value="true" <% if(ArquivoTipodt.getDje().equalsIgnoreCase("true")){%>  checked<%}%>>
					<label for="Dje">Publicação no Diário de Justiça Eletrônico</label> <br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
