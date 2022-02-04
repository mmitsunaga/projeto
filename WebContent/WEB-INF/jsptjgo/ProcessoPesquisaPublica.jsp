<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>

<html>
	<head>
		<title><%=request.getAttribute("TituloDaPagina")%></title>
		<link rel="shortcut icon" href="./imagens/favicon.png">
	
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
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
      	
		<script language="javascript" type="text/javascript">
		   	function VerificarCampos() {
	     		with(document.Formulario) {
	     			if ( (ProcessoNumero.value=="") && (NomeParte.value=="") && (CpfCnpjParte.value=="")){
		        	alert("Preencha no mínimo um dos Critérios para a Consulta.");
		         	ProcessoNumero.focus();
		         	return false; 
		     	} 
		     	submit();
	      		}
	    	}
		</script>
		
		<script language="javascript" type="text/javascript">
  			$(document).ready(function() {
	   			$('.submit_on_enter').keydown(function(event) {
		        	if (event.keyCode == 13) {
		            	AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');
		            	VerificarCampos();
		            	return false;
         			}
    			});
  			});
		</script>
		      	
		<style type="text/css"> 
		#bkg_projudi{ display:none } 
		</style>
		<meta name="keywords" content="projudi, processo judicial digital, processo judicial, tribunal de justiça do estado de goiás, consulta processual" />
		<meta name="description" content="Consulta de Processo Judicial Digital" />
	</head>
	<body class="fundo">

	<%@ include file="/CabecalhoPublico.html" %>  

  		<div id="divCorpo" class="divCorpo" >
  			      
  			 <div class="area"><h2>&raquo; <%=request.getAttribute("TituloDaPagina")%> </h2></div>
	  		<form action="BuscaProcessoPublica" method="post" name="Formulario" id="Formulario">
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				<input id="ServletRedirect" name="ServletRedirect" type="hidden" value="<%=request.getAttribute("ServletRedirect")%>">
				<input id="TituloDaPagina" name="TituloDaPagina" type="hidden" value="<%=request.getAttribute("TituloDaPagina")%>">

				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos </legend>
                        <p>
					   	<small> <strong>Nova Numeração</strong>:  Digite o Número do Processo "ponto" (ou "traço") e o Dígito Ex. <b>148032-91</b>.2009.8.09.002, ou seja, <strong>148032.91</strong> ou <strong>148032-91</strong><br />
   						<strong>Antiga Numeração</strong>:  Digite somente o Número do Processo Ex. 039.2006.<b>248.174</b>-5, ou seja, <strong>248174</strong><br />
   						</small>
                        </p>
						
						<p><label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo </label><br> 
					    <input class="submit_on_enter" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="18" onkeypress="return DigitarNumeroProcesso(this, event)" value="<%=buscaProcessoDt.getProcessoNumeroSimples()%>" onkeyup=" autoTab(this,60)"/>					    
					    					    
					    <br>ou<br />
					    
					   <p> <label class="formEdicaoLabel" for="NomeParte">*Nome da Parte </label><br> 
					    <input class="submit_on_enter" name="NomeParte" id="NomeParte"  type="text" size="70" maxlength="60" value="<%=buscaProcessoDt.getPromovente()%>" onkeyup=" autoTab(this,60)" title="Digite o nome do promovente ou promovido"/>
					    Pesquisar nome exato<input type="checkbox" name="cbPesquisarNomeExato" id="cbPesquisarNomeExato" onchange="av('pesquisarNomeExato', this.checked)" <%if(request.getParameter("pesquisarNomeExato") != null && request.getParameter("pesquisarNomeExato").equals("true")){%> checked <%} %>/><br>
					    <input type="hidden" id="pesquisarNomeExato" name="pesquisarNomeExato" <%if(request.getParameter("pesquisarNomeExato") != null){%> value="<%=request.getParameter("pesquisarNomeExato")%>"  <%} else { %> value="false" <%} %>/>
					    											
					    <br>ou<br />
					       	    
					    <label class="formEdicaoLabel" for="CpfCnpjParte">*CPF/CNPJ da Parte</label><br> 
					    <input class="submit_on_enter" name="CpfCnpjParte" id="CpfCnpjParte"  type="text" size="30" maxlength="18" onkeypress="return DigitarSoNumero(this, event)" value="<%=buscaProcessoDt.getCpfCnpjParte()%>" onkeyup=" autoTab(this,60)" title="Digite o CPF/CNPJ do promovente ou promovido"/>
					    <label for="Aviso" style="float:left;margin-left:25px;">(digitar somente números, sem pontos ou hífen)</label><br>
					    <br />

						<%@ include file="Padroes/reCaptcha.jspf" %>
				   		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="button" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');return VerificarCampos();">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>
