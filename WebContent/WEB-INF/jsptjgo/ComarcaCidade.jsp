<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ComarcaCidadeDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ComarcaCidadedt" scope="session" class= "br.gov.go.tj.projudi.dt.ComarcaCidadeDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Cidades de uma Comarca </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/ocultar.js'></script></head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Cidades de uma Comarca</h2></div>
		<form action="ComarcaCidade" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id='imgNovo'  class='imgNovo' title='Novo - Limpa os campos da tela' name='imaNovo' type='image' src='./imagens/imgNovo.png' onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id='imgsalvar' class='imgsalvar title='salvar - Salva os dados digitados' name='imasalvar' type='image'  src='./imagens/imgSalvar.png'  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')" >
				<input id='imgAtualizar' class='imgAtualizar' title='Atualizar - Atualiza os dados da tela' name='imaAtualizar' type='image'   src='./imagens/imgAtualizar.png'  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
				<a class="divPortaBotoesLink" href="Ajuda/ComarcaCidadeAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<input type="hidden" id="tempBuscaId_Comarca" name="tempBuscaId_Comarca">
			<input type="hidden" id="tempBuscaComarca" name="tempBuscaComarca">
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Cidades de uma Comarca </legend>
					<label class="formEdicaoLabel" for="Id_Comarca">Comarca
					 <input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempBuscaId_Comarca','Id_Comarca');AlterarValue('tempBuscaComarca','Comarca');AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label><br>  					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Comarca" id="Comarca" type="text" value="<%=ComarcaCidadedt.getComarca()%>"><br />
				</fieldset>
				<br />
				<div id='ListaCheckBox'>
					<input type='button' value='Mostrar Todos' id='MostrarTodos' title='Mostra todos'/> <input type='button' value='Mostrar Marcados' id='MostrarMarcados' title='Mostra somentes os marcados'/> <input type='button' value='Mostrar Desmarcados' id='MostrarDesmarcados' title='Mostra somentes os desmarcados'/> <br />
					<%=request.getAttribute("ListaUlLiComarcaCidade").toString() %>
				</div>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
