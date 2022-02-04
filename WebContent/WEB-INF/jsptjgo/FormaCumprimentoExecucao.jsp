<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="FormaCumprimentoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca da Forma de Cuprimento da Pena  </title>
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
				if (SeNulo(FormaCumprimentoExecucao, "O Campo FormaCumprimentoExecucao é obrigatório!")) return false;
				if (SeNulo(FormaCumprimentoExecucaoCodigo, "O Campo FormaCumprimentoExecucaoCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro da Forma de Cumprimento da Pena</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="FormaCumprimentoExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="FormaCumprimentoExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/FormaCumprimentoExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados </legend>
					<label class="formEdicaoLabel" for="Id_FormaCumprimentoExecucao">Id</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_FormaCumprimentoExecucao"  id="Id_FormaCumprimentoExecucao"  type="text"  readonly="true" value="<%=FormaCumprimentoExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="FormaCumprimentoExecucao">*Forma de Cumprimento da Pena</label><br> <input class="formEdicaoInput" name="FormaCumprimentoExecucao" id="FormaCumprimentoExecucao"  type="text" size="60" maxlength="60" value="<%=FormaCumprimentoExecucaodt.getFormaCumprimentoExecucao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="FormaCumprimentoExecucaoCodigo">*Codigo</label><br> <input class="formEdicaoInput" name="FormaCumprimentoExecucaoCodigo" id="FormaCumprimentoExecucaoCodigo"  type="text" size="6" maxlength="6" value="<%=FormaCumprimentoExecucaodt.getFormaCumprimentoExecucaoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,6)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
