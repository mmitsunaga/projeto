<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
	<head>
		<title>Busca Publica de Processo</title>
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
		<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
      	
		<script language="javascript" type="text/javascript">
		   	function VerificarCampos() {
	     		with(document.Formulario) {
	     			if ((ProcessoNumero.value=="") && (CodigoAcesso.value=="")){
		        	alert("O Número do Processo e Código de Acesso são campos obrigatórios.");
		         	ProcessoNumero.focus();
		         	return false; 
		     	} 
		     	submit();
	      		}
	    	}
		</script>      	
		<style type="text/css"> 
		#bkg_projudi{ display:none } 
		</style>
	</head>
	<body class="fundo">
	<%@ include file="/CabecalhoPublico.html" %>   
  		<div id="divCorpo" class="divCorpo" >
  			     
  			<div class="area"><h2>&raquo; Consulta de Processos por Código de Acesso</h2></div>
	  		<form action="BuscaProcessoCodigoAcesso" method="post" name="Formulario" id="Formulario">
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				
				<%@ include file="Padroes/reCaptcha.jspf" %>

				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos por Código de Acesso </legend>
					    <br />
						
						<label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo </label><br> 
					    <input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="18" onkeypress="return DigitarNumeroProcesso(this, event)" value="<%=request.getAttribute("ProcessoNumero")%>" onkeyup=" autoTab(this,18)"/>
					    <em> <small>Digite o Número do Processo "ponto" (ou "traço") e o Dígito Ex. <b>048032.01</b> ou <b>048032-01</b></small></em>
					    <br />
					    
					    <label class="formEdicaoLabel" for="CodigoAcesso">*Código de Acesso</label><br> 
					    <input class="formEdicaoInput" name="CodigoAcesso" id="CodigoAcesso"  type="text" size="40" maxlength="40" value="<%=request.getAttribute("CodigoAcesso")%>" onkeyup=" autoTab(this,40)" title="Digite o Código de Acesso"/>
					    <em><small> Código diferencia letras maiúsculas e minúsculas</small></em>
					    <br />
					       	  
					    <br />
					    <br />  
				   		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');" title="Consultar Processo por Código de Acesso">
								Consultar
							</button>	
							
							<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Limpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
								Limpar
							</button>	
						</div>
					</fieldset>		
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>
