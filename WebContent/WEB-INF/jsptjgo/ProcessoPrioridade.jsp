<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<jsp:useBean id="ProcessoPrioridadedt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"/>




<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Prioridade Processual  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ProcessoPrioridade, "O campo Descrição é obrigatório!")) return false;
				if (SeNulo(ProcessoPrioridadeCodigo, "O campo Código é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Prioridade Processual</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ProcessoPrioridade" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ProcessoPrioridade" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ProcessoPrioridadeAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Prioridade Processual </legend>
					<label class="formEdicaoLabel" for="Id_ProcessoPrioridade">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ProcessoPrioridade"  id="Id_ProcessoPrioridade"  type="text"  readonly="true" value="<%=ProcessoPrioridadedt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="ProcessoPrioridade">*Descrição</label><br> <input class="formEdicaoInput" name="ProcessoPrioridade" id="ProcessoPrioridade"  type="text" size="60" maxlength="60" value="<%=ProcessoPrioridadedt.getProcessoPrioridade()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="ProcessoPrioridadeCodigo">*Código</label><br> <input class="formEdicaoInput" name="ProcessoPrioridadeCodigo" id="ProcessoPrioridadeCodigo"  type="text" size="11" maxlength="11" value="<%=ProcessoPrioridadedt.getProcessoPrioridadeCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
					<label class="formEdicaoLabel" for="ProcessoPrioridadeOrdem">*Ordem</label><br> <input class="formEdicaoInput" name="ProcessoPrioridadeOrdem" id="ProcessoPrioridadeOrdem"  type="text" size="4" maxlength="4" value="<%=ProcessoPrioridadedt.getProcessoPrioridadeOrdem()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,4)"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
