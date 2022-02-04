<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="PendenciaTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaTipoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Pendência  </title>
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
				if (SeNulo(PendenciaTipo, "O Campo PendenciaTipo é obrigatório!")) return false;
				if (SeNulo(PendenciaTipoCodigo, "O Campo PendenciaTipoCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tipo de Pendência</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="PendenciaTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="PendenciaTipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/PendenciaTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Tipo de Pendência </legend>
					<label class="formEdicaoLabel" for="Id_PendenciaTipo">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_PendenciaTipo"  id="Id_PendenciaTipo"  type="text"  readonly="true" value="<%=PendenciaTipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="PendenciaTipo">*Descrição</label><br> <input class="formEdicaoInput" name="PendenciaTipo" id="PendenciaTipo"  type="text" size="60" maxlength="60" value="<%=PendenciaTipodt.getPendenciaTipo()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="PendenciaTipoCodigo">*Código</label><br> <input class="formEdicaoInput" name="PendenciaTipoCodigo" id="PendenciaTipoCodigo"  type="text" size="11" maxlength="11" value="<%=PendenciaTipodt.getPendenciaTipoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Id_ArquivoTipo">Tipo de Arquivo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarArquivoTipo" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArquivoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparArquivoTipo" name="imaLimparArquivoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ArquivoTipo','ArquivoTipo'); return false;" >
					</label><br>   <input  name='Id_ArquivoTipo' id='Id_ArquivoTipo' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ArquivoTipo" id="ArquivoTipo" type="text" size="60" maxlength="60" value="<%=PendenciaTipodt.getArquivoTipo()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
