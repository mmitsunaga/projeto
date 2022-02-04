<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Grupodt" scope="session" class= "br.gov.go.tj.projudi.dt.GrupoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Grupo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Grupo, "O Campo Grupo é obrigatório!")) return false;
				if (SeNulo(GrupoCodigo, "O Campo GrupoCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Grupo</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Grupo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Grupo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/GrupoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Grupo </legend>
					<label class="formEdicaoLabel" for="Id_Grupo">Id_Grupo</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Grupo"  id="Id_Grupo"  type="text"  readonly="true" value="<%=Grupodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Grupo">*Grupo</label><br> <input class="formEdicaoInput" name="Grupo" id="Grupo"  type="text" size="60" maxlength="60" value="<%=Grupodt.getGrupo()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="GrupoCodigo">*GrupoCodigo</label><br> <input class="formEdicaoInput" name="GrupoCodigo" id="GrupoCodigo"  type="text" size="11" maxlength="11" value="<%=Grupodt.getGrupoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Id_ServentiaTipo">ServentiaTipo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaTipo" name="imaLocalizarServentiaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaTipo" name="imaLimparServentiaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaTipo','ServentiaTipo'); return false;" > <input  name='Id_ServentiaTipo' id='Id_ServentiaTipo' type='hidden'  value=''>
					</label><br>   					
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ServentiaTipo" id="ServentiaTipo" type="text" size="60" maxlength="255" value="<%=Grupodt.getServentiaTipo()%>"><br />
					<label class="formEdicaoLabel" for="Id_GrupoTipo">GrupoTipo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarGrupoTipo" name="imaLocalizarGrupoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GrupoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparGrupoTipo" name="imaLimparGrupoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_GrupoTipo','GrupoTipo'); return false;" > 
					<input  name='Id_GrupoTipo' id='Id_GrupoTipo' type='hidden'  value=''>
					</label><br>  
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="GrupoTipo" id="GrupoTipo" type="text" size="60" maxlength="60" value="<%=Grupodt.getGrupoTipo()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
