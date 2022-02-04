<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CustaValorDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<jsp:useBean id="CustaValordt" scope="session" class= "br.gov.go.tj.projudi.dt.CustaValorDt"/>

<%@page import="br.gov.go.tj.projudi.dt.CustaDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Item de Custa  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type="text/javascript" src="./js/ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(LimiteMin, "O Campo Limite Mínimo é obrigatório!")) return false;
				if (SeNulo(LimiteMax, "O Campo Limite Máxima é obrigatório!")) return false;
				if (SeNulo(ValorCusta, "O Campo Valor Item da Custa é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Item de Custa</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="CustaValor" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="CustaValor" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/CustaValorAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados CustaValor </legend>
					
					<label class="formEdicaoLabel" for="Id_CustaValor">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_CustaValor" id="Id_CustaValor"  type="text"  readonly="true" value="<%=CustaValordt.getId()%>"/>
					<br />
					
<!--					<label class="formEdicaoLabel" for="CustaValor">*Descrição</label><br>-->
<!--					<input class="formEdicaoInput" name="CustaValor" id="CustaValor" type="text" size="60" maxlength="60" value="<%=CustaValordt.getCustaValor()%>" onkeyup=" autoTab(this,60)"/>-->
<!--					<br />-->
<!--					-->
<!--					<label class="formEdicaoLabel" for="CustaValorCodigo">Código Item de Custa</label><br>-->
<!--					<input class="formEdicaoInput" name="CustaValorCodigo" id="CustaValorCodigo" type="text" size="11" maxlength="11" value="<%=CustaValordt.getCustaValorCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>-->
<!--					<br />-->
					
					<label class="formEdicaoLabel" for="LimiteMin">*Limite Mínimo</label><br>
					<input class="formEdicaoInput" name="LimiteMin" id="LimiteMin"  type="text" size="21" maxlength="21" value="<%=CustaValordt.getLimiteMin()%>" onkeyup=" autoTab(this,21)"/>
					<br />
					
					<label class="formEdicaoLabel" for="LimiteMax">*Limite Máximo</label><br>
					<input class="formEdicaoInput" name="LimiteMax" id="LimiteMax" type="text" size="21" maxlength="21" value="<%=CustaValordt.getLimiteMax()%>" onkeyup=" autoTab(this,21)"/>
					<br />
					
					<label class="formEdicaoLabel" for="ValorCusta">*Valor do Item de Custa</label><br>
					<input class="formEdicaoInput" name="ValorCusta" id="ValorCusta" type="text" size="20" maxlength="21" value="<%=CustaValordt.getValorCusta()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/>
					<br />
					
					<label class="formEdicaoLabel" for="Id_Custa">*Custa
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarCusta" name="imaLocalizarCusta" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')"/>
					</label><br>
					
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="Custa" id="Custa" type="text" size="50" maxlength="50" value="<%=CustaValordt.getCusta()%>"/>
					<br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
