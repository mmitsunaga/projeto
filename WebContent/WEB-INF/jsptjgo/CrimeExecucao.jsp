<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CrimeExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="CrimeExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.CrimeExecucaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Crime da Execução Penal </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(CrimeExecucao, "O campo Crime de Execução é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Crime da Execução Penal</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="CrimeExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="CrimeExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/CrimeExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Crime da Execução Penal </legend>
					<label class="formEdicaoLabel" for="Id_CrimeExecucao">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_CrimeExecucao"  id="Id_CrimeExecucao"  type="text"  readonly="true" value="<%=CrimeExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="CrimeExecucao">*Crime da Execução</label><br> <input class="formEdicaoInput" name="CrimeExecucao" id="CrimeExecucao"  type="text" size="100" maxlength="100" value="<%=CrimeExecucaodt.getCrimeExecucao()%>" onkeyup=" autoTab(this,100)"><br />
					<label class="formEdicaoLabel" for="Lei">Lei</label><br> <input class="formEdicaoInput" name="Lei" id="Lei"  type="text" size="20" maxlength="20" value="<%=CrimeExecucaodt.getLei()%>" onkeyup=" autoTab(this,20)"><br />
					<label class="formEdicaoLabel" for="Artigo">Artigo</label><br> <input class="formEdicaoInput" name="Artigo" id="Artigo"  type="text" size="20" maxlength="20" value="<%=CrimeExecucaodt.getArtigo()%>" onkeyup=" autoTab(this,20)"><br />
					<label class="formEdicaoLabel" for="Paragrafo">Parágrafo</label><br> <input class="formEdicaoInput" name="Paragrafo" id="Paragrafo"  type="text" size="20" maxlength="20" value="<%=CrimeExecucaodt.getParagrafo()%>" onkeyup=" autoTab(this,20)"><br />
					<label class="formEdicaoLabel" for="Inciso">Inciso</label><br> <input class="formEdicaoInput" name="Inciso" id="Inciso"  type="text" size="20" maxlength="20" value="<%=CrimeExecucaodt.getInciso()%>" onkeyup=" autoTab(this,20)"><br />
					<label class="formEdicaoLabel" for="CrimeExecucaoCodigo">*Crime Execução Código (id do SPG)</label><br> <input class="formEdicaoInput" name="CrimeExecucaoCodigo" id="CrimeExecucaoCodigo"  type="text" size="10" maxlength="10" value="<%=CrimeExecucaodt.getCrimeExecucaoCodigo()%>" onkeyup=" autoTab(this,60)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
