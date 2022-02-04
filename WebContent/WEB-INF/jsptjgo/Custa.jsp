<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.CustaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Custadt" scope="session" class="br.gov.go.tj.projudi.dt.CustaDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Custa  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Custa, "O Campo Descrição é obrigatório!")) return false;
				if (SeNulo(CodigoRegimento, "O Campo Código Regimento é obrigatório!")) return false;
				if (SeNulo(CodigoRegimentoValor, "O Campo Código Regimento Valor é obrigatório!")) return false;
				if (SeNulo(Porcentagem, "O Campo Porcentagem é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Custa</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Custa" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		<form action="Custa" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/CustaAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Custa </legend>
					<label class="formEdicaoLabel" for="Id_Custa">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_Custa" id="Id_Custa" type="text" readonly="true" value="<%=Custadt.getId()%>"/>
					<br />
					
					<label class="formEdicaoLabel" for="Custa">*Descrição</label><br>
					<input class="formEdicaoInput" name="Custa" id="Custa" type="text" size="60" maxlength="60" value="<%=Custadt.getCusta()%>" onkeyup=" autoTab(this,60)"/>
					<br />
					
					<label class="formEdicaoLabel" for="CustaCodigo">Código Custa</label><br>
					<input class="formEdicaoInput" name="CustaCodigo" id="CustaCodigo" type="text" size="11" maxlength="11" value="<%=Custadt.getCustaCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
					<br />
					
					<label class="formEdicaoLabel" for="CodigoRegimento">*Código Regimento</label><br>
					<input class="formEdicaoInput" name="CodigoRegimento" id="CodigoRegimento" type="text" size="10" maxlength="10" value="<%=Custadt.getCodigoRegimento()%>" onkeyup=" autoTab(this,10)"/>
					<br />
					
					<label class="formEdicaoLabel" for="CodigoRegimentoValor">*Codigo Regimento Valor</label><br>
					<input class="formEdicaoInput" name="CodigoRegimentoValor" id="CodigoRegimentoValor" type="text" size="10" maxlength="10" value="<%=Custadt.getCodigoRegimentoValor()%>" onkeyup=" autoTab(this,10)"/>
					<br />
					
					<label class="formEdicaoLabel" for="Porcentagem">*Porcentagem</label><br>
					<input class="formEdicaoInput" name="Porcentagem" id="Porcentagem" type="text" size="7" maxlength="7" value="<%=Custadt.getPorcentagem()%>" onkeyup=" autoTab(this,7)"/>
					<br />
					
					<label class="formEdicaoLabel" for="Minimo">Mínimo</label><br>
					<input class="formEdicaoInput" name="Minimo" id="Minimo" type="text" size="1" maxlength="1" value="<%=Custadt.getMinimo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,1)"/>
					<br />
					
					<label class="formEdicaoLabel" for="Id_ArrecadacaoCusta">*Arrecadação Custa
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarArrecadacaoCusta" name="imaLocalizarArrecadacaoCusta" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArrecadacaoCustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
					</label><br>
					
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="ArrecadacaoCusta" id="ArrecadacaoCusta" type="text" size="40" maxlength="40" value="<%=Custadt.getArrecadacaoCusta()%>"/>
					<br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
