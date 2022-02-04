<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="GuiaEmissaodt" scope="session" class= "br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.GuiaModeloDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Guia Emitida  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Serventia, "O Campo Serventia é obrigatório!")) return false;
				if (SeNulo(ProcessoTipo, "O Campo ProcessoTipo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Guia Emitida</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="GuiaEmissao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="GuiaEmissao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/GuiaEmissaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Guia Emitida </legend>
					<label class="formEdicaoLabel" for="Id_GuiaEmissao">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_GuiaEmissao"  id="Id_GuiaEmissao"  type="text"  readonly="true" value="<%=GuiaEmissaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="GuiaEmissao">*Guia Emitida</label><br> <input class="formEdicaoInput" name="GuiaEmissao" id="GuiaEmissao"  type="text" size="60" maxlength="60" value="<%=GuiaEmissaodt.getGuiaEmissao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Id_GuiaModelo">*Template de Guia
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarGuiaModelo" name="imaLocalizarGuiaModelo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GuiaModeloDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		
					</label><br>  			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="GuiaModelo" id="GuiaModelo" type="text" size="60" maxlength="60" value="<%=GuiaEmissaodt.getGuiaModelo()%>"><br />
					<label class="formEdicaoLabel" for="Id_Processo">Processo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcesso" name="imaLocalizarProcesso" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparProcesso" name="imaLimparProcesso" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Processo','Id_Serventia'); return false;" >
					</label><br>   <input  name='Id_Processo' id='Id_Processo' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Id_Serventia" id="Id_Serventia" type="text" size="11" maxlength="11" value="<%=GuiaEmissaodt.getId_Serventia()%>"><br />
					<label class="formEdicaoLabel" for="Serventia">*Serventia</label><br> <input class="formEdicaoInput" name="Serventia" id="Serventia"  type="text" size="60" maxlength="60" value="<%=GuiaEmissaodt.getServentia()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Id_ProcessoTipo">Processo Tipo
					 <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparProcessoTipo" name="imaLimparProcessoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ProcessoTipo','ProcessoTipo'); return false;" > 
					</label><br> <input  name='Id_ProcessoTipo' id='Id_ProcessoTipo' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProcessoTipo" id="ProcessoTipo" type="text" size="60" maxlength="255" value="<%=GuiaEmissaodt.getProcessoTipo()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
