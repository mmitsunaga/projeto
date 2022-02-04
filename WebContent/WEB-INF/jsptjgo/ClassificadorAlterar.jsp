<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="processoDt" class= "br.gov.go.tj.projudi.dt.ProcessoDt" scope="session"/>

<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<html>
<head>
	<title>Alteração de Classificador do Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	      
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Alteração de Classificador de Processo</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Alteração de Classificador de Processo</legend>
						
						<label class="formEdicaoLabel" for="Processo">N&uacute;mero Processo</label><br>
						<span class="spanDestaque"><a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a>
						<br />
						<br />
						
						<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Classificador
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ClassificadorNovo" name="imaLocalizarId_ClassificadorNovo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','1');" >
		    			<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_NovoClassificador','NovoClassificador'); return false;" title="Limpar Classificador" />
						</label><br>  
						<input type="hidden" name="Id_NovoClassificador" id="Id_NovoClassificador" value="<%=request.getAttribute("Id_NovoClassificador")%>" />
		    			
		    			<input class="formEdicaoInputSomenteLeitura"  readonly name="NovoClassificador" id="NovoClassificador" type="text" size="76" maxlength="100" value="<%=request.getAttribute("NovoClassificador")%>"/><br />
				    		
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','2');"> 
						</div>
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>