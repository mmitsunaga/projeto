<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="RegimeExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.RegimeExecucaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Regime de Execução  </title>
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
				if (SeNulo(RegimeExecucao, "O campo Descrição é obrigatório!")) return false;
				if (SeNulo(PenaExecucaoTipo, "O campo Tipo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Regime de Execução</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="RegimeExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="RegimeExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/RegimeExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Regime de Execução </legend>
					<label class="formEdicaoLabel" for="Id_RegimeExecucao">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_RegimeExecucao"  id="Id_RegimeExecucao"  type="text"  readonly="true" value="<%=RegimeExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="RegimeExecucao">*Descrição</label><br> <input class="formEdicaoInput" name="RegimeExecucao" id="RegimeExecucao"  type="text" size="60" maxlength="60" value="<%=RegimeExecucaodt.getRegimeExecucao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Id_PenaExecucaoTipo">*Tipo <input class="FormEdicaoimgLocalizar" id="imaLocalizarPenaExecucaoTipo" name="imaLocalizarPenaExecucaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(PenaExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		</label><br> 			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="PenaExecucaoTipo" id="PenaExecucaoTipo" type="text" size="60" maxlength="60" value="<%=RegimeExecucaodt.getPenaExecucaoTipo()%>"><br />
					<label class="formEdicaoLabel" for="Id_ProximoRegime">Próximo regime (para cálculo da Progressão de Regime)  <input class="FormEdicaoimgLocalizar" id="imaLocalizarProximoRegime" name="imaLocalizarPenaExecucaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RegimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		</label><br>			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProximoRegimeExecucao" id="ProximoRegimeExecucao" type="text" size="60" maxlength="60" value="<%=RegimeExecucaodt.getProximoRegimeExecucao()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
