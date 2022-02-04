<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="MovimentacaoTipoClassedt" scope="session" class= "br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Movimenta��o Tipo Classe  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	
	
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(MovimentacaoTipoClasse, "O Campo Descri��o � obrigat�rio!")) return false;
				if (SeNulo(MovimentacaoTipoClasseCodigo, "O Campo C�digo � obrigat�rio!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Movimenta��o Tipo Classe</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="MovimentacaoTipoClasse" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="MovimentacaoTipoClasse" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				
				<a class="divPortaBotoesLink" href="Ajuda/MovimentacaoTipoClasseAjuda.html" target="_blank">  
				<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Movimenta��o Tipo Classe </legend>
					<label class="formEdicaoLabel" for="Id_MovimentacaoTipoClasse">Identificador</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="Id_MovimentacaoTipoClasse"  id="Id_MovimentacaoTipoClasse"  type="text"  readonly="true" value="<%=MovimentacaoTipoClassedt.getId()%>"><br />
					<label class="formEdicaoLabel" for="MovimentacaoTipoClasse">*Descri��o</label><br> 
					<input class="formEdicaoInput" name="MovimentacaoTipoClasse" id="MovimentacaoTipoClasse"  type="text" size="60" maxlength="60" value="<%=MovimentacaoTipoClassedt.getMovimentacaoTipoClasse()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="MovimentacaoTipoClasseCodigo">*C�digo</label><br> 
					<input class="formEdicaoInput" name="MovimentacaoTipoClasseCodigo" id="MovimentacaoTipoClasseCodigo"  type="text" size="4" maxlength="4" value="<%=MovimentacaoTipoClassedt.getMovimentacaoTipoClasseCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
				</fieldset>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
