<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="ServentiaCargoEscalaDt" scope="session" class="br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt" />

<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EscalaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt" %>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title>|<%=request.getAttribute("tempPrograma")%>| Busca de Serventia Cargo Escala  </title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />	
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='/js/Funcoes.js'></script>
	<script type='text/javascript' src='/js/ocultar.js'></script>
	</head>
	
<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Serventia Cargo Escala</h2></div>
		
		<form action="ServentiaCargoEscala" method="post" name="Formulario" id="Formulario" >
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id='imgNovo'  class='imgNovo' title='Novo - Limpa os campos da tela' name='imaNovo' type='image' src='./imagens/imgNovo.png' onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id='imgSalvar' class='imgSalvar' title='Salvar - Salva os dados digitados' name='imasalvar' type='image' src='./imagens/imgSalvar.png' onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />
				
				<input id="imgLocalizar" name="imaLocalizar" class="imgLocalizar" alt="Localizar" title="Localizar - Localiza um registro no banco" type="image" src="./imagens/imgLocalizar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
				<input id="imgExcluir" alt="Excluir" class="imgexcluir" title="Excluir - Exclui o registro localizado" name="imaexcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Excluir)%>');" />
				
				<input id='imgAtualizar' class='imgAtualizar' title='Atualizar - Atualiza os dados da tela' name='imaAtualizar' type='image' src='./imagens/imgAtualizar.png' onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')" />
				<a class="divPortaBotoesLink" href="Ajuda/ServentiaCargoEscalaAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			
			<input type="hidden" id="tempBuscaEscala" name="tempBuscaEscala" value="<%=ServentiaCargoEscalaDt.getEscala()%>" />
			<input type="hidden" id="tempBuscaId_Escala" name="tempBuscaId_Escala" value="<%=ServentiaCargoEscalaDt.getId_Escala()%>" />
			<input type="hidden" id="tempBuscaServentiaCargo" name="tempBuscaServentiaCargo" value="<%=ServentiaCargoEscalaDt.getServentiaCargo()%>" />
			<input type="hidden" id="tempBuscaId_ServentiaCargo" name="tempBuscaId_ServentiaCargo" value="<%=ServentiaCargoEscalaDt.getId_ServentiaCargo()%>" />
			<input type="hidden" id="tempBuscaId_ServentiaCargoEscalaStatus" name="tempBuscaId_ServentiaCargoEscalaStatus" value="<%=ServentiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getId()%>" />
			<input type="hidden" id="tempBuscaServentiaCargoEscalaStatus" name="tempBuscaServentiaCargoEscalaStatus" value="<%=ServentiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus()%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Editar os dados Serventia Cargo Escala</legend>
					
					<label class="formEdicaoLabel" for="Id_Escala">*Escala
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarEscala" name="imaLocalizarEscala" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EscalaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" size="60" name="Escala" id="Escala" type="text" value="<%=ServentiaCargoEscalaDt.getEscala()%>" />
					<br />
					
					<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Serventia Cargo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" size="60" name="ServentiaCargo" id="ServentiaCargo" type="text" value="<%=ServentiaCargoEscalaDt.getServentiaCargo()%>" />
					<br />
					
					<label class="formEdicaoLabel" for="Id_ServentiaCargoEscalaStatus">*Status
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarEscala" name="imaLocalizarEscala" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoEscalaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" size="60" name="ServentiaCargoEscalaStatus" id="ServentiaCargoEscalaStatus" type="text" value="<%=ServentiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus()%>" />
					<br />
					
					<label class="formEdicaoLabel" for="DataVinculacao">Data Vinculação na Escala</label><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="DataVinculacao" id="DataVinculacao" type="text" value="<%=Funcoes.TelaData(ServentiaCargoEscalaDt.getDataVinculacao())%>" />
					<br />
					
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>