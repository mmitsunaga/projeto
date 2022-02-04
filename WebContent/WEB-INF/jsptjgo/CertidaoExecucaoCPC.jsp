<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="CertidaoExecucaoCPCDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoExecucaoCPCDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
	
	<title>Certid&atilde;o de Execuç&atilde;o (Art. 615-a C.P.C.)</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./css/geral.css');
		
	</style>
	    
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			
			<form action="CertidaoExecucaoCPC" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>"/>
				<input id="InserirDados" name="InserirDados" type="hidden" value="<%=request.getAttribute("InserirDados")%>"/>
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
				<div id="divEditar" class="divEditar">
					<div id="divPortaBotoes" class="divPortaBotoes">
						<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
						<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
						<input id="imgImprimir" alt="Imprimir"  class="imgImprimir" title="Imprimir - Gerar relatorio em pdf" name="imaImprimir" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" />
					</div>
				<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				
					<label class="formEdicaoLabel" for="ProcessoNumeroDependente">* Numero de Processo </label><br> 
				    		<input class="formEdicaoInput" name="ProcessoNumeroDependente" id="ProcessoNumeroDependente" type="text" size="30" maxlength="20" value="<%=CertidaoExecucaoCPCDt.getNumero()%>" />
				    		<em> Digite o Número do Processo "ponto" (ou "traço") e o Dígito Ex. <b>048032.01</b> ou <b>048032-01</b></em><br />    
	    			<br />	
	    			<fieldset class="VisualizaDados"> 
			    	<legend>Dados do Processo</legend>
			    			
			    			<p> <b>Número:</b> <%= CertidaoExecucaoCPCDt.getNumero()%> </p>
							<p><b>Serventia:</b> <%= CertidaoExecucaoCPCDt.getServentia() %></p>
							<p><b>Natureza:</b> <%= CertidaoExecucaoCPCDt.getNatureza()  %> </p> 
							<p><b>Exequente:</b> <%= CertidaoExecucaoCPCDt.getPromovente() %> </p>
							<p><b>Adv Exequente:</b> <%= CertidaoExecucaoCPCDt.getAdvogadoPromovente() %> </p>
							<p> <b>Executado</b>: <%= CertidaoExecucaoCPCDt.getPromovido() %> 
							<p><b>CPF/CNPJ:</b>	<%= CertidaoExecucaoCPCDt.getCpfCnpj() %> </p>
							<p> <b>Data de Distribuiç&atilde;o:</b> <%=CertidaoExecucaoCPCDt.getDataDistribuicao()%> </p>
							<p> <b>Valor da Aç&atilde;o</b> <%= CertidaoExecucaoCPCDt.getValor() %> </p> 
							
					</fieldset>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar Processo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
							 <input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"> 
				</div>
				</fieldset>
				
			
				<%@ include file="Padroes/Mensagens.jspf"%>
				<% if (CertidaoExecucaoCPCDt.getTexto() != null && !CertidaoExecucaoCPCDt.getTexto().equals(""))  {%>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Certid&acute;o</legend>
						<div id="divTextoEditor" class="divTextoEditor">
							<%= CertidaoExecucaoCPCDt.getTexto() %> 
						</div>
					
				</fieldset>
				<%} %>
				
				</div>			
			</form>
		</div>
	</body>
</html>