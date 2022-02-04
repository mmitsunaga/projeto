<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.InterrupcaoTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="InterrupcaoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.InterrupcaoTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Interrupção  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(InterrupcaoTipo, "O Campo Descrição é obrigatório!")) return false;				
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tipo de Interrupção</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="InterrupcaoTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="InterrupcaoTipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>			
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Tipo de Interrupção</legend>
					<label class="formEdicaoLabel" for="Id_InterrupcaoTipo">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_InterrupcaoTipo"  id="Id_InterrupcaoTipo"  type="text"  readonly="true" value="<%=InterrupcaoTipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="InterrupcaoTipo">*Descrição</label><br> <input class="formEdicaoInput" name="InterrupcaoTipo" id="InterrupcaoTipo"  type="text" size="60" maxlength="60" value="<%=InterrupcaoTipodt.getInterrupcaoTipo()%>" onKeyUp=" autoTab(this,60)"><br /><br />
					<label for="InterrupcaoTotal">Interrupção Total?</label> <input class="formEdicaoInput" name="InterrupcaoTotal" id="InterrupcaoTotal"  type="checkbox"  value="true" <%=(InterrupcaoTipodt.getInterrupcaoTotal().equalsIgnoreCase("true") ? "checked='checked'" : "")%> /><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
