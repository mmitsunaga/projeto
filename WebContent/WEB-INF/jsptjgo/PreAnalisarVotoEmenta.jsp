<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>	
<%@page import="java.util.*"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<title>Pré-Analisar Autos Conclusos</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
    <script type='text/javascript' src='./js/tabelas.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	   
    <%@page import="br.gov.go.tj.utils.Configuracao"%>
	<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
	<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
			
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/DivFlutuante.js'></script>
	<script type='text/javascript' src='./js/Mensagens.js'></script>
	<script type='text/javascript'>
	    $(document).ready(function() {
	    	//MostrarOcultar('Editor');
	    	//MostrarOcultar('EditorEmenta');	
	    	calcularTamanhoIframe();	    	 	    	
	    });	    
	</script>	
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina") %> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />			
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
			<!-- Variáveis para controlar Passos da Pré-Análise -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AnalisePendenciadt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=AnalisePendenciadt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AnalisePendenciadt.getPasso3()%>">
			
			<input id="ConsultaEmenta" name="ConsultaEmenta" type="hidden" value="N">			

			<div id="divEditar" class="divEditar">
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','1');">
				<% } if (!AnalisePendenciadt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AnalisePendenciadt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>')">
				<% } %>
				
				<% ProcessoDt objProcessoDt = AnalisePendenciadt.getProcessoDt(); %>
				
				<fieldset class="formEdicao">
					<legend><%=request.getAttribute("TituloPagina") %></legend>
							
					<fieldset class="formEdicao"> 
						<legend>Redigir Pré-Análise Relatório e Voto</legend>
						<!--  Redigir texto da pré-analise usando Editor de Modelos -->					
						
							<input type="hidden" id="textoEditor" name="textoEditor">
							<input type="hidden" id="id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo")%>">
							
							<a href="BuscaProcesso?Id_Processo=<%=objProcessoDt.getId()%>"><%=objProcessoDt.getProcessoNumero()%>
								&nbsp;<%=AnalisePendenciadt.getProcessoTipoSessao()%>
			        		</a>
			        		<br />
						
							<label class="formEdicaoLabel"> Tipo de Arquivo
							<% if (!AnalisePendenciadt.isArquivoTipoSomenteLeitura()){ %>
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao  + Configuracao.Localizar)%>');" title="Selecionar Tipo de Arquivo">
							<input class="FormEdicaoimgLocalizar" name="imaLimparArquivoTipo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" title="Limpar Tipo de Arquivo" />
							<%}%>
							</label><br>
							<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" id="arquivoTipo" readonly type="text" size="58" maxlength="50" value="<%=request.getAttribute("ArquivoTipo")%>">		
							<br />
										
							<input type="hidden" id="id_Modelo" name="Id_Modelo" value="<%=request.getAttribute("Id_Modelo")%>" />
							<label class="formEdicaoLabel"> Modelo 
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('ConsultaEmenta', 'N');" title="Selecionar Modelo de Arquivo Relatório e Voto">
							<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_Modelo', 'modelo'); return false;" title="Limpar Modelo">
							</label><br>
							<input class="formEdicaoInputSomenteLeitura" name="Modelo" id="modelo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("Modelo")%>">		
							<br />
							<label class="formEdicaoLabel"> Nome Arquivo</label><br>							
							<input class="formEdicaoInputSomenteLeitura" id="nomeArquivo" name="nomeArquivo" type="text" size="58" maxlength="255" value="<%=AnalisePendenciadt.getNomeArquivo()%>"/>
							<br />
							
							<label class="formEdicaoLabel"> Editor Texto
								<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
								onclick="MostrarOcultar('Editor'); return false;" title="Abrir Editor de Texto" />
							</label><br />	
						
							<div id="Editor" class="Editor" style="display:none">        			
								<textarea cols="80" class="ckeditor" id="editor1" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>								
							</div>
					</fieldset>
				
					<br />	
					
					<fieldset class="formEdicao"> 
					<legend>Redigir Pré-Análise Ementa</legend>
					<!--  Redigir texto da pré-analise usando Editor de Modelos -->					
					
						<input type="hidden" id="textoEditorEmenta" name="textoEditorEmenta">
						<input type="hidden" id="id_ArquivoTipoEmenta" name="Id_ArquivoTipoEmenta" value="<%=request.getAttribute("Id_ArquivoTipoEmenta")%>">
					
						<label class="formEdicaoLabel"> Tipo de Arquivo
						<% if (!AnalisePendenciadt.isArquivoTipoEmentaSomenteLeitura()){ %>
						<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipoEmenta" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao  + Configuracao.Localizar)%>');" title="Selecionar Tipo de Arquivo Ementa">
						<input class="FormEdicaoimgLocalizar" name="imaLimparArquivoTipoEmenta" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ArquivoTipoEmenta', 'arquivoTipoEmenta'); return false;" title="Limpar Tipo de Arquivo Ementa" />
						<%}%>
						</label><br>
						<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipoEmenta" id="arquivoTipoEmenta" readonly type="text" size="58" maxlength="50" value="<%=request.getAttribute("ArquivoTipoEmenta")%>">		
						<br />
									
						<input type="hidden" id="id_ModeloEmenta" name="Id_ModeloEmenta" value="<%=request.getAttribute("Id_ModeloEmenta")%>" />
						<label class="formEdicaoLabel"> Modelo 
						<input class="FormEdicaoimgLocalizar" name="imaLocalizarModeloEmenta" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('ConsultaEmenta', 'S');" title="Selecionar Modelo de Arquivo Ementa">
						<input class="FormEdicaoimgLocalizar" name="imaLimparModeloEmenta" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ModeloEmenta', 'modeloEmenta'); return false;" title="Limpar Modelo Ementa">
						</label><br>
						<input class="formEdicaoInputSomenteLeitura" name="ModeloEmenta" id="modeloEmenta" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("ModeloEmenta")%>">		
						<br />
						<label class="formEdicaoLabel"> Nome Arquivo</label><br>
						<input class="formEdicaoInputSomenteLeitura" id="nomeArquivoEmenta" name="nomeArquivoEmenta" type="text" size="58" maxlength="255" value="<%=AnalisePendenciadt.getNomeArquivoEmenta()%>"/>
						<br />						
					
						<label class="formEdicaoLabel"> Editor Texto
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipoEmenta" type="image"  src="./imagens/imgEditorTextoPequena.png" 
								onclick="MostrarOcultar('EditorEmenta'); return false;" title="Abrir Editor de Texto Ementa" />
						</label><br />	
						<div id="EditorEmenta" style="display:none">        			
							<textarea cols="80" class="ckeditor" id="editorEmenta" name="TextoEditorEmenta" rows="20"><%=request.getAttribute("TextoEditorEmenta")%></textarea>				
						</div>
					</fieldset>	
					
					<br />					
					<label class="formEdicaoLabel" for="julgadoMerito">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br><input class="FormEdicaoimgLocalizar" id="julgadoMerito" type="checkbox" name="julgadoMerito" onclick="AlterarValue('fluxo','1')";  value="true" <%if (AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal()!=null && AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")){%> checked<%}%> />		
				</fieldset>
				
				<br />		
				
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','1');"> 
				</div>
				<br />				
				
			</div>	
		</form>
 		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>	
</body>
</html>