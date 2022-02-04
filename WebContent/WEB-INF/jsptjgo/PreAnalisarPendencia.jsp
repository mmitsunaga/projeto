<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>
<%@page import="java.util.*"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

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
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	
</head>

<body onload="fechar('sim');">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina") %> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	

			<div id="divEditar" class="divEditar">
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<% } %>
				
				<fieldset class="formEdicao">
					<legend><%=request.getAttribute("TituloPagina") %></legend>
					
					<%@ include file="DadosPendencia.jspf" %>
			
					<fieldset class="formEdicao"> 
						<legend>Redigir Pré-Análise</legend>
						<!--  Redigir texto da pré-analise usando Editor de Modelos -->
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
							<label class="formEdicaoLabel"> Nome Arquivo
							<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeArquivo', ''); return false;" title="Limpar nome do arquivo" />
							</label><br>
							
							<input id="nomeArquivo" name="nomeArquivo" type="text" size="54" maxlength="255" value="<%=AnalisePendenciadt.getNomeArquivo()%>"/>
							<br /><br />
						
							<div id="Editor" class="Editor">        			
								<textarea class="ckeditor" cols="80" id="editor1" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>
							</div>
					</fieldset>
				
					<br />
					<label class="formEdicaoLabel" for="Id_MovimentacaoTipo">*Tipo Movimentação 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacaoTipo" name="imaLocalizarMovimentacaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
					</label><br>  
					<input id="MovimentacaoTipo" name="MovimentacaoTipo" type="hidden" value="<%=AnalisePendenciadt.getMovimentacaoTipo()%>" />					
					<select name="Id_MovimentacaoTipo" id="Id_MovimentacaoTipo" size="1" onchange="AlterarValue('MovimentacaoTipo',this.options[this.selectedIndex].text)">
						<option value="null">--Selecione o Tipo de Movimentação--</option>
						<%
							List listaMovimentacaoTipo = AnalisePendenciadt.getListaTiposMovimentacaoConfigurado();
							for (int i=0;i<listaMovimentacaoTipo.size();i++){
								UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);%>
								<option value="<%=usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()%>" <%=((AnalisePendenciadt.getId_MovimentacaoTipo() != null && AnalisePendenciadt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()))?"selected='selected'":"")%>><%=usuarioMovimentacaoTipoDt.getMovimentacaoTipo()%></option>
						   <%}
			            %> 	  						   
		           	</select>
					<br />
					<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
						<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />
						</label><br> 
						<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="30" maxlength="80" value="<%=AnalisePendenciadt.getComplementoMovimentacao()%>"/>
					
					<br />
				
				</fieldset>		
					
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
				</div>
				<br />
			</div>	
		</form>
 		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
	<script type="text/javascript">
		function fechar(valor){
			if (valor!=null){
				$('.corpo').hide();
			}
		}
	</script>
</body>
</html>