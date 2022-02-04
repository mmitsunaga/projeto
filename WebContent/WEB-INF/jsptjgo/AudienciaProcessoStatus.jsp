<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="AudienciaProcessoStatusdt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de AudienciaProcessoStatus  </title>
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
				if (SeNulo(AudienciaProcessoStatus, "O Campo AudienciaProcessoStatus é obrigatório!")) return false;
				if (SeNulo(AudienciaProcessoStatusCodigo, "O Campo AudienciaProcessoStatusCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de AudienciaProcessoStatus</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="AudienciaProcessoStatus" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="AudienciaProcessoStatus" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/AudienciaProcessoStatusAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados AudienciaProcessoStatus </legend>
					<label class="formEdicaoLabel" for="Id_AudienciaProcessoStatus">Id_AudienciaProcessoStatus</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_AudienciaProcessoStatus"  id="Id_AudienciaProcessoStatus"  type="text"  readonly="true" value="<%=AudienciaProcessoStatusdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="AudienciaProcessoStatus">*AudienciaProcessoStatus</label><br> <input class="formEdicaoInput" name="AudienciaProcessoStatus" id="AudienciaProcessoStatus"  type="text" size="60" maxlength="60" value="<%=AudienciaProcessoStatusdt.getAudienciaProcessoStatus()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="AudienciaProcessoStatusCodigo">*AudienciaProcessoStatusCodigo</label><br> <input class="formEdicaoInput" name="AudienciaProcessoStatusCodigo" id="AudienciaProcessoStatusCodigo"  type="text" size="11" maxlength="11" value="<%=AudienciaProcessoStatusdt.getAudienciaProcessoStatusCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Id_ServentiaTipo">ServentiaTipo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaTipo" name="imaLocalizarServentiaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaTipo" name="imaLimparServentiaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaTipo','ServentiaTipo'); return false;" >
					</label><br>   <input  name='Id_ServentiaTipo' id='Id_ServentiaTipo' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ServentiaTipo" id="ServentiaTipo" type="text" size="60" maxlength="255" value="<%=AudienciaProcessoStatusdt.getServentiaTipo()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
