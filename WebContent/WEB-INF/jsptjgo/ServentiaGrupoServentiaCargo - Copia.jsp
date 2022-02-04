<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt. ServentiaGrupoServentiaCargoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ServentiaGrupoServentiaCargodt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaGrupoServentiaCargoDt"/>
 
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaGrupoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Função  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/ocultar.js'></script></head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de  Função</h2></div>
		<form action=" ServentiaGrupoServentiaCargo" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id='imgNovo'  class='imgNovo' title='Novo - Limpa os campos da tela' name='imaNovo' type='image' src='./imagens/imgNovo.png' onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id='imgsalvar' class='imgsalvar title='salvar - Salva os dados digitados' name='imasalvar' type='image'  src='./imagens/imgSalvar.png'  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')" >
				<input id='imgAtualizar' class='imgAtualizar' title='Atualizar - Atualiza os dados da tela' name='imaAtualizar' type='image'   src='./imagens/imgAtualizar.png'  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
				<a class="divPortaBotoesLink" href="Ajuda/ ServentiaGrupoServentiaCargoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<input type="hidden" id="tempBuscaId_ServentiaGrupo" name="tempBuscaId_ServentiaGrupo">
			<input type="hidden" id="tempBuscaServGrupo" name="tempBuscaServGrupo">
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Função </legend>
					<label class="formEdicaoLabel" for="Id_ServentiaGrupo">Função
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaGrupo" name="imaLocalizarServentiaGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempBuscaID_SERV_GRUPO','ID_SERV_GRUPO');AlterarValue('tempBuscaSERV_GRUPO','SERV_GRUPO');AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaGrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label>
					<br><input  class="formEdicaoInputSomenteLeitura"  size="63" readonly="true" name="ServGrupo" id="SERV_GRUPO" type="text" value="<%= ServentiaGrupoServentiaCargodt.getServentiaGrupo()%>">
					<br />
				</fieldset>
				<br />
				<div id='ListaCheckBox'>
					<input type='button' value='Mostrar Todos' id='MostrarTodos' title='Mostra todos'/> <input type='button' value='Mostrar Marcados' id='MostrarMarcados' title='Mostra somentes os marcados'/> <input type='button' value='Mostrar Desmarcados' id='MostrarDesmarcados' title='Mostra somentes os desmarcados'/> <br />
					<%=request.getAttribute("ListaUlLiServentiaGrupoServentiaCargo").toString() %>
				</div>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
