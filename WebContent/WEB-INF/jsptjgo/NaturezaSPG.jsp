<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.NaturezaSPGDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="NaturezaSPGdt" scope="session" class="br.gov.go.tj.projudi.dt.NaturezaSPGDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Natureza SPG  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			if ($("#PaginaAtual").val() == '<%= String.valueOf(Configuracao.SalvarResultado) %>') {
				with(document.Formulario) {
					if (SeNulo(NaturezaSPG, "O Campo Natureza SPG é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(NaturezaSPGCodigo, "O Campo Código Natureza SPG é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}					
					submit();
				}	
			}			
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Natureza SPG</h2></div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
				<form action="NaturezaSPG" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
				<form action="NaturezaSPG" method="post" name="Formulario" id="Formulario">
			<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>								
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Natureza SPG </legend>
					<label class="formEdicaoLabel" for="Id_NaurezaSPG">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_NaturezaSPG"  id="Id_NaturezaSPG"  type="text"  readonly="true" value="<%=NaturezaSPGdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="NaurezaSPG">*Natureza SPG</label><br> <input class="formEdicaoInput" name="NaturezaSPG" id="NaturezaSPG"  type="text" size="60" maxlength="60" value="<%=NaturezaSPGdt.getNaturezaSPG()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="NaurezaSPGCodigo">*Código Natureza SPG</label><br> <input class="formEdicaoInput" name="NaturezaSPGCodigo" id="NaturezaSPGCodigo"  type="text" size="11" maxlength="11" value="<%=NaturezaSPGdt.getNaturezaSPGCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />			
				</fieldset>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
