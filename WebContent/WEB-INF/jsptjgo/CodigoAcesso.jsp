<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
	
	<title>Gerar Codigo de Acesso ao Processo</title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	    
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
				
		<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>	
		
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; Gerar Código de Acesso </h2></div>
			
			<form action="DescartarPendenciaProcesso" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="Gerar Codigo de Acesso" />
				
				<div id="divEditar" class="divEditar">
				<%@ include file="Padroes/Mensagens.jspf"%>
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Modelo para código de acesso</legend>
					<label class="formEdicaoLabel"> Modelo 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');"  title="Selecionar Modelo de Arquivo" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('modelo', 'modelo'); return false;" title="Limpar Modelo" />
					</label><br>
					
					
					<input class="formEdicaoInputSomenteLeitura" name="modelo" readonly type="text" size="50" maxlength="50" id="modelo" value="<%=modeloDt.getModelo()%>" />		
					<br />
					
					<label class="formEdicaoLabel"> Editor Texto 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image" src="./imagens/imgEditorTextoPequena.png" onclick="MostrarOcultar('Editor'); return false;" title="Abrir Editor de Texto" /> 
				</label>
			
					
				<div id="Editor" class="Editor" style="display: none">
				<textarea class="ckeditor" cols="80" id="editor1" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>
				<script type="text/javascript">
				<%if (request.getAttribute("TextoEditor") != null && !request.getAttribute("TextoEditor").equals("") ){	%>MostrarOcultar('Editor');
											<%}%>		
									</script></div>
					
				</fieldset>
				
				
				</div>			
			</form>
		</div>
	</body>
</html>