<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Visualizar Pré-Análise  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
	      
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualizar Pré-Análise </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend> Dados Pré-Análise</legend>
					
					<label class="formEdicaoLabel">Processo(s) </label><br>  
					<%
					List pendencias = AnalisePendenciadt.getListaPendenciasFechar(); 
						for (int i=0;i<pendencias.size();i++){
							PendenciaDt objPendencia = (PendenciaDt)pendencias.get(i);
					%>
						<span class="spanDestaque"><%=objPendencia.getProcessoNumero()%></span>
					<%	} %>
					<br />
				
					<label class="formEdicaoLabel"> Data Pré-Análise </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getDataPreAnalise()%></span>
					<br />
					
					<label class="formEdicaoLabel">Tipo Movimentação </label><br>  
					<span class="spanDestaque"><%=AnalisePendenciadt.getMovimentacaoTipo()%></span>
					<br />
					
					<%if (AnalisePendenciadt.getComplementoMovimentacao() != null && AnalisePendenciadt.getComplementoMovimentacao().trim().length() > 0) {%>
					<label class="formEdicaoLabel"> Descrição Movimentação </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getComplementoMovimentacao()%></span>
					<br />
					<%}%>

					<fieldset class="formLocalizar">	
						<legend>Texto Pré-Análise</legend>
						<blockquote id="divTextoEditor" class="divTextoEditor">
							<%=AnalisePendenciadt.getTextoEditor() %>
						</blockquote>
					</fieldset>
						
					<br />					
				</fieldset>
			</div>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
