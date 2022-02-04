<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="UsuarioPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioParteDt"/>

<html>
<head>
	<title>Usuario Parte</title>
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>		
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/DivFlutuante.js'> </script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Cadastro de Usuário Parte </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
			  
			<div id="divEditar" class="divEditar">
		  		<center>
					<fieldset class="formEdicao"> 
		    			<legend class="formEdicaoLegenda">Cadastro de Usuário Representante da Parte </legend>
		    			
		    			<div id="divAjuda" class="divAjuda" >
							<img src="./imagens/imgAjudaPequena.png" onclick="DivFlutuanteValoresIniciais('Ajuda Cadastro de Senha para Parte', getMensagem(0),'200','360','0','0', this);" onmouseup="DivFlutuanteUp('Informe');"" width="16" height="16" border="0" />
						</div>
			
		    			<label class="formEdicaoLabel" for="Nome">Parte</label><br> 
		    			<input class="formEdicaoInputSomenteLeitura" name="Nome" id="Nome" type="text" readonly size="60" value="<%=UsuarioPartedt.getParteDt().getNome()%>"/>
		    			
		    			<label class="formEdicaoLabel" for="Nome">CNPJ</label><br> 
		    			<input class="formEdicaoInputSomenteLeitura" name="Nome" id="Nome" type="text" readonly size="30" value="<%=UsuarioPartedt.getParteDt().getCpfCnpjFormatado()%>"/><br />
		    			
		    			    			
		    			<label class="formEdicaoLabel" for="CpfRepresentante">*Cpf</label><br> 
		    			<input class="formEdicaoInput" name="CpfRepresentante" id="CpfRepresentante"  type="text" size="20" maxlength="11" value="<%=UsuarioPartedt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/><br />
		    			
		    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Submeter" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','PJ');"> 
				    	</div> 
					</fieldset>
				</center>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>



