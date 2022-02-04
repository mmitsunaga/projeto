<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<jsp:useBean id="RgOrgaoExpedidordt" scope="session" class= "br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"/>




<%@page import="br.gov.go.tj.projudi.dt.EstadoDt"%>
<%-----%>
<%--<jsp:setProperty name="objEstado" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Órgão Expedidor RG </title>
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
				if (SeNulo(RgOrgaoExpedidor, "O campo Nome é obrigatório!")) return false;
				if (SeNulo(Sigla, "O campo Sigla é obrigatório!")) return false;
				if (SeNulo(Estado, "O campo Estado é obrigatório!")) return false;
				if (SeNulo(EstadoCodigo, "O Campo EstadoCodigo é obrigatório!")) return false;
				if (SeNulo(Uf, "O Campo Uf é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Órgão Expedidor RG</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="RgOrgaoExpedidor" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="RgOrgaoExpedidor" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/RgOrgaoExpedidorAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Órgão Expedidor RG </legend>
					<label class="formEdicaoLabel" for="Id_RgOrgaoExpedidor">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_RgOrgaoExpedidor"  id="Id_RgOrgaoExpedidor"  type="text"  readonly="true" value="<%=RgOrgaoExpedidordt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="RgOrgaoExpedidor">*Nome</label><br> <input class="formEdicaoInput" name="RgOrgaoExpedidor" id="RgOrgaoExpedidor"  type="text" size="60" maxlength="60" value="<%=RgOrgaoExpedidordt.getRgOrgaoExpedidor()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="Sigla">*Sigla</label><br> <input class="formEdicaoInput" name="Sigla" id="Sigla"  type="text" size="10" maxlength="10" value="<%=RgOrgaoExpedidordt.getSigla()%>" onkeyup=" autoTab(this,10)"/><br />
					<label class="formEdicaoLabel" for="Id_Estado">*Estado
					  <input class="FormEdicaoimgLocalizar" id="imaLocalizarEstado" name="imaLocalizarEstado" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 	</label><br>				<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Estado" id="Estado" type="text" size="60" maxlength="60" value="<%=RgOrgaoExpedidordt.getEstado()%>"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
