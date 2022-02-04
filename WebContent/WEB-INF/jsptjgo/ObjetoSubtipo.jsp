<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ObjetoSubtipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ObjetoSubtipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ObjetoSubtipoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ObjetoTipoDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ObjetoSubtipo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script language="javascript" type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de ObjetoSubtipo</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ObjetoSubtipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ObjetoSubtipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>					<a class="divPortaBotoesLink" href="Ajuda/ObjetoSubtipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				</div>
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados ObjetoSubtipo </legend>
					<div class="col10">
						<label for="Id_ObjetoSubtipo">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_ObjetoSubtipo"  id="Id_ObjetoSubtipo"  type="text"  readonly="true" value="<%=ObjetoSubtipodt.getId()%>"><br />
					</div> </br>
					<div class="col45">
						<label for="ObjetoSubtipo">*Objeto Subtipo</label> <input class="formEdicaoInput" name="ObjetoSubtipo" id="ObjetoSubtipo"  type="text" size="60" maxlength="60" value="<%=ObjetoSubtipodt.getObjetoSubtipo()%>" onkeyup=" autoTab(this,60)"><br />
					</div> </br>
						<div class="col45">
						<input id='Id_ObjetoTipo' name='Id_ObjetoTipo' type='hidden' value='<%=ObjetoSubtipodt.getId_ObjetoTipo()%>' />
						<label  for="Id_ObjetoTipo">Objeto tipo<input class="FormEdicaoimgLocalizar" id="imaLocalizarObjetotipo" name="imaLocalizarObjetotipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Formulario', 'ObjetoTipo','Consulta de Objeto tipo', 'Digite o Objeto tipo e clique em consultar.', 'Id_ObjetoTipo', 'ObjetoTipo', ['Objeto Tipo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" ></label> 
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ObjetoTipo" id="ObjetoTipo" type="text" size="60" maxlength="255" value="<%=ObjetoSubtipodt.getObjetoTipo()%>"><br />
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
