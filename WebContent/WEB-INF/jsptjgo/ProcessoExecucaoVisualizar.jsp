<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Confirmação de Dados </h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">				
				
				<%@ include file="ProcessoExecucaoPassos.jspf" %>
				
				<div id="Editar">
					<fieldset id="Editar">
						<h2 align="center" style="color: red;"> Verifique os dados inseridos e clique no botão "Confirmar" para efetuar o cadastro do Processo! </h2>
						<br />
						<%@ include file="ProcessoExecucaoConfirmacao.jspf" %>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" id="imgInserir" type="submit" value="Confirmar" onclick="AlterarValue('PaginaAtual',<%=Configuracao.SalvarResultado%>);"> 
							<input name="imgCancelar" id="imgCancelar" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual',<%=Configuracao.Novo%>);">
						</div>
					</fieldset>
				</div>
			</form>
		</div>
	</body>
</html>