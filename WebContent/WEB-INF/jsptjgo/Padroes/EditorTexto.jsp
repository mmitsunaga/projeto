<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
	
	
	
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/DivFlutuante.js'></script>
	<script type='text/javascript' src='./js/Mensagens.js'></script>

	<input type="hidden" id="textoEditor" name="textoEditor">
	<input type="hidden" id="id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo")%>">

	<label class="formEdicaoLabel"> Tipo de Arquivo
	<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao  + Configuracao.Localizar)%>');" title="Selecionar Tipo de Arquivo">
	<input class="FormEdicaoimgLocalizar" name="imaLimparArquivoTipo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" title="Limpar Tipo de Arquivo" />
	</label><br>
	
	<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" id="arquivoTipo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("ArquivoTipo")%>">		
	<br />
				
	<input type="hidden" id="id_Modelo" name="Id_Modelo" value="<%=request.getAttribute("Id_Modelo")%>" />
	<label class="formEdicaoLabel"> Modelo 
	<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecionar Modelo de Arquivo">
	<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_Modelo', 'modelo'); return false;" title="Limpar Modelo">
	</label><br>
	
	<input class="formEdicaoInputSomenteLeitura" name="Modelo" id="modelo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("Modelo")%>">		
	<br />

	<div id="Editor" class="Editor">        			
		<textarea class="ckeditor" cols="80" id="editor1" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>
	</div>
