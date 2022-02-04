<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="ProcessoPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDt"/>

<html>
	<head>
		<title>Busca de Partes Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Partes </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<%-- Campos hidden necessários para evitar o autocomplete do firefox para os inputs --%>
				<input type="text" style="display:none">
				<input type="password" style="display:none">
				<%-- ------------------------------------------------------------------------------ --%>		
				
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="Hash" name="Hash" type="hidden" value="<%= Funcoes.GeraHashMd5(processoDt.getId_Processo()) %>" />
				<input id="HashParte" name="HashParte" type="hidden" value="<%=Funcoes.GeraHashMd5(ProcessoPartedt.getId_ProcessoParte())%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda"> Busca de Partes </legend>
						
						<div class="col45">
						<label class="formEdicaoLabel" for="CpfCnpj"> CPF / CNPJ </label><br>
						<input type="text" name="CpfCnpj" class="formEdicaoInput" size="30" maxlength="18">
						</div>
						
						<!--  Parâmetros de pesquisa para busca do sentenciado do processo de execução penal -->
						<% 
							if (request.getAttribute("ParteTipo").toString().equalsIgnoreCase("9"))	{ %>
							<br />	
							<label class="formEdicaoLabel" for="Sentenciado">Nome do sentenciado</label><br> 
						    <input class="formEdicaoInput" name="Sentenciado" id="Sentenciado"  type="text" size="50" maxlength="50" onkeyup=" autoTab(this,50)"><br />
							
							<label class="formEdicaoLabel" for="Mae"> Nome da mãe</label><br>		
							<input class="formEdicaoInput" name="Mae" id="Mae" type="text" size="50" maxlength="50" onkeyup=" autoTab(this,20)"/><br />
							
							<label class="formEdicaoLabel" for="DataNascimento"> Data de nascimento</label><br>
							<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento" type="text" size="20" maxlength="20" onkeyup=" autoTab(this,20)">
						<%} else { 
						
							if (Funcoes.StringToInt(request.getAttribute("ParteTipo").toString()) != ProcessoParteTipoDt.POLO_ATIVO_CODIGO) { %>
							<br />	
							
							<div class="col45 clear">
							<label class="formEdicaoLabel" for="Rg">Rg</label><br> 
						    <input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="18" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
							</div>
							
							<div class="col45">
							<label class="formEdicaoLabel" for="Ctps"> CTPS </label><br>		
							<input class="formEdicaoInput" name="Ctps" id="Ctps" type="text" size="20" maxlength="20" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/><br />
							</div>
							
							<div class="col45 clear">
							<label class="formEdicaoLabel" for="TituloEleitor"> Título Eleitor </label><br>
							<input class="formEdicaoInput" name="TituloEleitor" id="TituloEleitor" type="text" size="20" maxlength="20" onkeyup=" autoTab(this,20)">
							</div>
							
							<div class="col45">	
							<label class="formEdicaoLabel" for="Pis"> PIS </label><br>
							<input class="formEdicaoInput" name="Pis" id="Pis" type="text" size="20" maxlength="20" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)">
							</div>
							<%} %>
							
							
							<div class="col45 clear">
							<label for="imaLocalizar" title="Sem Personalidade Jurídica ou Civil">Impersonificável</label> <input id="imgLocalizar" class="imgLocalizar" title="Localizar sem Personalidade Jurídica ou Civil" name="imaLocalizar" type="image" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PassoEditar','9')">
						</div>
						<%} %>
						
						
						<div class="clear"></div>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PassoEditar','2')">
						</div>
					</fieldset>
				</div>
				<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
		</div>
	</body>
</html>
	