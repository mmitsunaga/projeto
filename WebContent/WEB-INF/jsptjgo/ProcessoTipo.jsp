<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Processo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ProcessoTipo, "O Campo ProcessoTipo � obrigat�rio!")) return false;
				if (SeNulo(ProcessoTipoCodigo, "O Campo ProcessoTipoCodigo � obrigat�rio!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tipo de Processo</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ProcessoTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ProcessoTipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ProcessoTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Tipo de Processo</legend>
					<label class="formEdicaoLabel" for="Id_ProcessoTipo">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ProcessoTipo"  id="Id_ProcessoTipo"  type="text"  readonly="true" value="<%=ProcessoTipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="ProcessoTipo">*Descri��o</label><br> <input class="formEdicaoInput" name="ProcessoTipo" id="ProcessoTipo"  type="text" size="60" maxlength="60" value="<%=ProcessoTipodt.getProcessoTipo()%>" onKeyUp=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="ProcessoTipoCodigo">*C�digo</label><br> <input class="formEdicaoInput" name="ProcessoTipoCodigo" id="ProcessoTipoCodigo"  type="text" size="11" maxlength="11" value="<%=ProcessoTipodt.getProcessoTipoCodigo()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)"><br />
   					<label class="formEdicaoLabel" for="CnjCodigo">*CNJ C�digo</label><br> <input class="formEdicaoInput" name="CnjCodigo" id="CnjCodigo"  type="text" size="11" maxlength="11" value="<%=ProcessoTipodt.getCnjCodigo()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Ordem2Grau">Ordem 2� Grau</label><br> <input class="formEdicaoInput" name="Ordem2Grau" id="Ordem2Grau"  type="text" size="11" maxlength="4" value="<%=ProcessoTipodt.getOrdem2Grau()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,4)"><br />
					<label for="Publico">Publico</label> <input class="formEdicaoInput" name="Publico" id="Publico"  type="checkbox"  value="true" <% if(ProcessoTipodt.getPublico().equalsIgnoreCase("true")){%>checked<%}%>><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
