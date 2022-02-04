<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>

<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				<input id="TipoConsulta" name="TipoConsulta" type="hidden" value="<%=request.getAttribute("TipoConsulta")%>">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos </legend>
					    
                        <p>
						<small> <strong>Nova Numeração</strong>:  Digite o Número do Processo "ponto" (ou "traço") e o Dígito Ex. <b>148032-91</b>.2009.8.09.002, ou seja, <strong>148032.91</strong> ou <strong>148032-91</strong><br />
   						<strong>Antiga Numeração</strong>:  Digite somente o Número do Processo Ex. 039.2006.<b>248.174</b>-5, ou seja, <strong>248174</strong><br />
   						</small>
                        </p>
                        
                        <label class="formEdicaoLabel" for="TcoNumero">*Protocolo SSP </label><br> 
					    <input class="formEdicaoInput" name="TcoNumero" id="TcoNumero"  type="text" size="30" maxlength="18" value="<%=buscaProcessoDt.getTcoNumero()%>" onkeyup=" autoTab(this,60)"/><br />                        
                        
					    <label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo </label><br> 
					    <input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="18" value="<%=buscaProcessoDt.getProcessoNumero()%>" onkeyup=" autoTab(this,60)"/><br />
					    
					    <label class="formEdicaoLabel" for="NomeParte">*Nome da Parte </label><br> 
					    <input class="formEdicaoInput" name="NomeParte" id="NomeParte"  type="text" size="70" maxlength="60" value="<%=buscaProcessoDt.getPromovente()%>" onkeyup=" autoTab(this,60)" title="Digite o nome do promovente ou promovido"/>
					    Pesquisar nome exato<input type="checkbox" name="cbPesquisarNomeExato" id="cbPesquisarNomeExato" onchange="av('pesquisarNomeExato', this.checked)" <%if(request.getParameter("pesquisarNomeExato") != null && request.getParameter("pesquisarNomeExato").equals("true")){%> checked <%} %>/>
					    <input type="hidden" id="pesquisarNomeExato" name="pesquisarNomeExato" <%if(request.getParameter("pesquisarNomeExato") != null){%> value="<%=request.getParameter("pesquisarNomeExato")%>"  <%} else { %> value="false" <%} %>/>
					    <br />
					       	    
					    <label class="formEdicaoLabel" for="CpfCnpjParte">*CPF/CNPJ da Parte</label><br> 
					    <input class="formEdicaoInput" name="CpfCnpjParte" id="CpfCnpjParte"  type="text" size="30" maxlength="18" value="<%=buscaProcessoDt.getCpfCnpjParte()%>" onkeyup=" autoTab(this,60)" title="Digite o CPF/CNPJ do promovente ou promovido"/><br />
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual', '<%=request.getAttribute("TipoConsulta")%>');">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>