<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaModeloDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="GuiaModelodt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaModeloDt"/>

<%@page import="br.gov.go.tj.projudi.dt.GuiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Cálculo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(GuiaModelo, "O Campo Descrição é obrigatório!")) return false;
				if (SeNulo(GuiaModeloCodigo, "O Campo Código Cálculo é obrigatório!")) return false;
				if (SeNulo(GuiaTipo, "O Campo Tipo Guia é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Cálculo</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="GuiaModelo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		<form action="GuiaModelo" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/GuiaModeloAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Cálculo </legend>
					<label class="formEdicaoLabel" for="Id_GuiaModelo">Identificar</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_GuiaModelo" id="Id_GuiaModelo" type="text" readonly="true" value="<%=GuiaModelodt.getId()%>"/>
					<br />
					
					<label class="formEdicaoLabel" for="GuiaModelo">*Descrição</label><br>
					<input class="formEdicaoInput" name="GuiaModelo" id="GuiaModelo" type="text" size="60" maxlength="60" value="<%=GuiaModelodt.getGuiaModelo()%>" onkeyup=" autoTab(this,60)"/>
					<br />
					
					<label class="formEdicaoLabel" for="GuiaModeloCodigo">*Código Cálculo</label><br>
					<input class="formEdicaoInput" name="GuiaModeloCodigo" id="GuiaModeloCodigo" type="text" size="11" maxlength="11" value="<%=GuiaModelodt.getGuiaModeloCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
					<br />
					
					<label class="formEdicaoLabel" for="Id_GuiaTipo">*Tipo de Guia
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarGuiaTipo" name="imaLocalizarGuiaTipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GuiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="GuiaTipo" id="GuiaTipo" type="text" size="50" maxlength="50" value="<%=GuiaModelodt.getGuiaTipo()%>"/>
					<br />
					
					<label class="formEdicaoLabel" for="Id_ProcessoTipo">Tipo Processo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
					<input class="FormEdicaoimgLocalizar" id="imaLimparProcessoTipo" name="imaLimparProcessoTipo" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoTipo','ProcessoTipo'); return false;" />
					</label><br>
					<input  name='Id_ProcessoTipo' id='Id_ProcessoTipo' type='hidden' value=''/>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="ProcessoTipo" id="ProcessoTipo" type="text" size="60" maxlength="255" value="<%=GuiaModelodt.getProcessoTipo()%>"/>
					<br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
