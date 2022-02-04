<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ArrecadacaoCustadt" scope="session" class="br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ArrecadacaoCusta  </title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="./js/Funcoes.js"></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type="text/javascript" src="./js/ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ArrecadacaoCusta, "O Campo Descrição é obrigatório!")) return false;
				submit();
			}
		}
		</script>
	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;Cadastro de Arrecadação</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ArrecadacaoCusta" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		<form action="ArrecadacaoCusta" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/ArrecadacaoCustaAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Arrecadação</legend>
					
					<label class="formEdicaoLabel" for="Id_ArrecadacaoCusta">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_ArrecadacaoCusta" id="Id_ArrecadacaoCusta" type="text" readonly="true" value="<%=ArrecadacaoCustadt.getId()%>"/>
					<br />
					
					<label class="formEdicaoLabel" for="ArrecadacaoCusta">*Descrição</label><br>
					<input class="formEdicaoInput" name="ArrecadacaoCusta" id="ArrecadacaoCusta" type="text" size="60" maxlength="60" value="<%=ArrecadacaoCustadt.getArrecadacaoCusta()%>" onkeyup=" autoTab(this,60)"/>
					<br />
					
					<label class="formEdicaoLabel" for="ArrecadacaoCustaCodigo">Código Arrecadação Custa</label><br>
					<input class="formEdicaoInput" name="ArrecadacaoCustaCodigo" id="ArrecadacaoCustaCodigo" type="text" size="11" maxlength="11" value="<%=ArrecadacaoCustadt.getArrecadacaoCustaCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
					<br />
					
					<label class="formEdicaoLabel" for="CodigoRegimento">Código Regimento</label><br>
					<input class="formEdicaoInput" name="CodigoRegimento" id="CodigoRegimento" type="text" size="10" maxlength="10" value="<%=ArrecadacaoCustadt.getCodigoArrecadacao()%>" onkeyup=" autoTab(this,10)"/>
					<br />
					
<!--					<label class="formEdicaoLabel" for="Custa">Custa</label><br>-->
<!--					<input class="formEdicaoInput" name="Custa" id="Custa" type="text" size="50" maxlength="50" value="<%=ArrecadacaoCustadt.getCusta()%>" onkeyup=" autoTab(this,50)"/>-->
<!--					<br />-->
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
