<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoGuiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<jsp:useBean id="certidaoGuiaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoGuiaDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>
<html>
<head>	
	<title>Gerar Certid&atilde;o</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
    
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
	<script type='text/javascript' src='./js/DivFlutuante.js'></script>
	<script type='text/javascript' src='./js/Mensagens.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
</head>
	<body>
		<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
		<%@ include file="/CabecalhoPublico.html" %>
		<% } %>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<div id="divEditar" class="divEditar">
<!-- 					<div id="divPortaBotoes" class="divPortaBotoes"> -->
<%-- 						<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" /> --%>
<%-- 						<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />  --%>
<%-- 						<input id="imgImprimir" alt="Imprimir"  class="imgImprimir" title="Imprimir - Gerar relatorio em pdf" name="imaImprimir" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" /> --%>
<!-- 					</div> -->
				<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
			    	<div class="col45 clear">
			    	
						<label class="formEdicaoLabel" for="numeroGuia">Número da Guia:</label><br>
						
						<input name="numeroGuia" id="numeroGuia" 
						<% if( request.getAttribute("numeroGuiaOk") != null && !request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ok") ) { %> class="formEdicaoInput" <% }
						else { %> class="formEdicaoInputSomenteLeitura" readonly <% } %>						
						type="text" size="60" maxlength="255" value="<%=certidaoGuiaDt.getNumeroGuia()%>"><br>
						
						<% // Verifica se o número da guia foi encontrado. Se não foi, apresenta uma mensagem explicativa.
		               	if(request.getAttribute("numeroGuiaOk") != null && request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("inexistente")) {
		             	%>	             	
		             		<font color="#FF0000"><b>&nbsp;Guia não encontrada.</b></font>
		             		<br />
		             	<% } %>
		             	
		             	<% // Verifica se a guia foi paga.
		               	if(request.getAttribute("numeroGuiaOk") != null && request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("nao_paga")) {
		             	%>	             	
		             		<font color="#FF0000"><b>&nbsp;Esta guia não está paga.</b></font>
		             		<br />
		             	<% } %>
		             	
<%-- 		           	<% // Verifica se a guia já foi utilizada. --%>
<!-- // 		           	if(request.getAttribute("numeroGuiaOk") != null && request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ja_utilizada")) { -->
<%-- 		          	%>	             	 --%>
<!-- 		           		<font color="#FF0000"><b>&nbsp;Esta guia já foi utilizada em outra certidão.</b></font> -->
<%-- 		           	<% } %> --%>
		             	
						<br />
						<% // Botao SUBMIT (Localizar Guia)
						if( request.getAttribute("numeroGuiaOk") != null && !request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ok") ) {
						%>
							<input name="imgLocalizar" type="submit" value="Localizar Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')" >
						<% } %>
						
						<!-- Botoes Nova Busca e Limpar -->
						<input name="imgLimpar" type="submit" 
						<% if( request.getAttribute("numeroGuiaOk") != null && !request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ok") ) { %>
							value="Limpar"
						<% } else { %>
							value="Nova Busca"
						<% } %>
						onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						 
					</div>
				</fieldset>				
				</div>
				<%@ include file="Padroes/reCaptcha.jspf" %>			
			</form>
				<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
	</body>
</html>