<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
	<head>
		<title>Busca de Processo 1º Grau</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
	
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Processo 1º Grau </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda"> Busca de Processo 1º Grau </legend>
                        <p>
                        <em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em><br />					    
                        </p>
						
						<label class="formEdicaoLabel" for="CpfCnpj"> Número Processo </label><br>
						<input class="formEdicaoInput" type="text" name="ProcessoNumero"  size="30" maxlength="25"  onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)" value="<%=ProcessoCadastroDt.getProcessoNumeroPrincipal()%>">
						
						<br />
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
						</div>
					</fieldset>
				</div>
				<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
		</div>
	</body>
</html>
	