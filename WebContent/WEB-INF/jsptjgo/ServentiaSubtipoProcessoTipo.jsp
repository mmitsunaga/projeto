<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ServentiaSubtipoProcessoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Processo de Subtipo de Serventia </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>	
	<script type='text/javascript' src='./js/ocultar.js'></script></head>

	<script type="text/javascript">
		function alterarServentiaSubtipoProcessoTipo(id, id_processo_tipo, id_serventia_subtipo) {
			if($('#chkEditar'+id_processo_tipo).prop('checked')){
				salvarItemSelecaoMultipla('ServentiaSubtipoProcessoTipo',id_serventia_subtipo,id_processo_tipo,6);
			}else{
				if (id!=undefined && id!=""){
					excluirItemSelecaoMultipla('ServentiaSubtipoProcessoTipo',id,6);
				}
			}
		}
	</script>
<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Tipo de Processo de Subtipo de Serventia</h2></div>
		<form action="ServentiaSubtipoProcessoTipo" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id='imgNovo'  class='imgNovo' title='Novo - Limpa os campos da tela' name='imaNovo' type='image' src='./imagens/imgNovo.png' onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >				
				<input id='imgAtualizar' class='imgAtualizar' title='Atualizar - Atualiza os dados da tela' name='imaAtualizar' type='image'   src='./imagens/imgAtualizar.png'  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
				<a class="divPortaBotoesLink" href="Ajuda/ServentiaSubtipoProcessoTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<input type="hidden" id="tempBuscaId_ServentiaSubtipo" name="tempBuscaId_ServentiaSubtipo">
			<input type="hidden" id="tempBuscaServentiaSubtipo" name="tempBuscaServentiaSubtipo">
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Tipo de Processo de Subtipo de Serventia </legend>
					<label class="formEdicaoLabel" for="Id_ServentiaSubtipo">Subtipo de Serventia 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaSubtipo" name="imaLocalizarServentiaSubtipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempBuscaId_ServentiaSubtipo','Id_ServentiaSubtipo');AlterarValue('tempBuscaServentiaSubtipo','ServentiaSubtipo');AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="ServentiaSubtipo" id="ServentiaSubtipo" type="text" value="<%=ServentiaSubtipoProcessoTipodt.getServentiaSubtipo()%>" size="100"><br />
				</fieldset>
				<br />
			</div>
		</form>
		<div id='ListaCheckBox'>
			<input type='button' value='Mostrar Todos' id='MostrarTodos' title='Mostra todos'/> <input type='button' value='Mostrar Marcados' id='MostrarMarcados' title='Mostra somentes os marcados'/> <input type='button' value='Mostrar Desmarcados' id='MostrarDesmarcados' title='Mostra somentes os desmarcados'/> <br />
			<%=request.getAttribute("ListaUlLiServentiaSubtipoProcessoTipo").toString() %>
		</div>
		
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
