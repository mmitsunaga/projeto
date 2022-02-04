<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.GuiaItemDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="GuiaItemdt" scope="session" class= "br.gov.go.tj.projudi.dt.GuiaItemDt"/>

<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CustaDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Item de Guia  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Custa, "O Campo Custa é obrigatório!")) return false;
				if (SeNulo(Quantidade, "O Campo Quantidade é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Item de Guia</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="GuiaItem" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="GuiaItem" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/GuiaItemAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Item de Guia </legend>
					<label class="formEdicaoLabel" for="Id_GuiaItem">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_GuiaItem"  id="Id_GuiaItem"  type="text"  readonly="true" value="<%=GuiaItemdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="GuiaItem">*Item de Guia</label><br> <input class="formEdicaoInput" name="GuiaItem" id="GuiaItem"  type="text" size="60" maxlength="60" value="<%=GuiaItemdt.getGuiaItem()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Id_GuiaEmissao">*Guia Emitida
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarGuiaEmissao" name="imaLocalizarGuiaEmissao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GuiaEmissaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br>  <input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Id_Custa" id="Id_Custa" type="text" size="20" maxlength="20" value="<%=GuiaItemdt.getId_Custa()%>"><br />
					<label class="formEdicaoLabel" for="Custa">*Custa</label><br> <input class="formEdicaoInput" name="Custa" id="Custa"  type="text" size="50" maxlength="50" value="<%=GuiaItemdt.getCusta()%>" onkeyup=" autoTab(this,50)"><br />
					<label class="formEdicaoLabel" for="GuiaItemCodigo">Código do Item de Guia</label><br> <input class="formEdicaoInput" name="GuiaItemCodigo" id="GuiaItemCodigo"  type="text" size="20" maxlength="20" value="<%=GuiaItemdt.getGuiaItemCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"><br />
					<label class="formEdicaoLabel" for="Quantidade">*Quantidade</label><br> <input class="formEdicaoInput" name="Quantidade" id="Quantidade"  type="text" size="6" maxlength="6" value="<%=GuiaItemdt.getQuantidade()%>" onkeyup=" autoTab(this,6)"><br />
					<label class="formEdicaoLabel" for="ValorCalculado">Valor Calculado</label><br> <input class="formEdicaoInput" name="ValorCalculado" id="ValorCalculado"  type="text" size="21" maxlength="21" value="<%=GuiaItemdt.getValorCalculado()%>" onkeyup=" autoTab(this,21)"><br />
					<label class="formEdicaoLabel" for="ValorReferencia">Valor Referência</label><br> <input class="formEdicaoInput" name="ValorReferencia" id="ValorReferencia"  type="text" size="21" maxlength="21" value="<%=GuiaItemdt.getValorReferencia()%>" onkeyup=" autoTab(this,21)"><br />
					<label class="formEdicaoLabel" for="Parcelas">Parcelas</label><br> <input class="formEdicaoInput" name="Parcelas" id="Parcelas"  type="text" size="2" maxlength="2" value="<%=GuiaItemdt.getParcelas()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,2)"><br />
					<label class="formEdicaoLabel" for="ParcelaCorrente">Parcela Corrente</label><br> <input class="formEdicaoInput" name="ParcelaCorrente" id="ParcelaCorrente"  type="text" size="2" maxlength="2" value="<%=GuiaItemdt.getParcelaCorrente()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,2)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
