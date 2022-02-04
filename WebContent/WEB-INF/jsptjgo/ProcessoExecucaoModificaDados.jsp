<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
	<head>
		<title>Processo de Execução Penal - Cálculo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Alterar Número do Proccesso</h2></div>
			
			<form action="ProcessoExecucao" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				
				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados">
				      	<legend>Processo de Cálculo de Liquidação de Pena</legend>
				      	<br />
				      	<label class="formEdicaoLabel" for="ProcessoFisicoNumero">*Número do Processo de Execução Penal</label><br>
						<input class="formEdicaoInput" name="ProcessoFisicoNumero" id="ProcessoFisicoNumero"  type="text" size="30" maxlength="25" value="<%=processoDt.getProcessoNumeroCompleto()%>" onkeyup=" autoTab(this,25)"
								onkeypress="mascara1(this, numeroProcessoCNJ)" onBlur="preencheZeros(this,25)"/>
						<br />
					</fieldset>
					<br /><br />

					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','17');"> 
				    </div>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>