<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="CargoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.CargoTipoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Cargo </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(CargoTipo, "O Campo Descrição é obrigatório!")) return false;
				if (SeNulo(CargoTipoCodigo, "O Campo Código é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tipo de Cargo</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="CargoTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		<form action="CargoTipo" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/CargoTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Cadastro de Tipo de Cargo </legend>
					<label class="formEdicaoLabel" for="Id_CargoTipo">Identificador</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="Id_CargoTipo"  id="Id_CargoTipo"  type="text"  readonly="true" value="<%=CargoTipodt.getId()%>"><br />
					
					<label class="formEdicaoLabel" for="CargoTipo">*Descrição</label><br> 
					<input class="formEdicaoInput" name="CargoTipo" id="CargoTipo"  type="text" size="60" maxlength="60" value="<%=CargoTipodt.getCargoTipo()%>" onkeyup=" autoTab(this,60)"><br />
					
					<label class="formEdicaoLabel" for="CargoTipoCodigo">*Código</label><br> 
					<input class="formEdicaoInput" name="CargoTipoCodigo" id="CargoTipoCodigo"  type="text" size="11" maxlength="11" value="<%=CargoTipodt.getCargoTipoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					
					<label class="formEdicaoLabel" for="Id_Grupo">*Grupo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarGrupo" name="imaLocalizarGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br>  
					
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Grupo" id="Grupo" type="text" size="60" maxlength="100" value="<%=CargoTipodt.getGrupo()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
