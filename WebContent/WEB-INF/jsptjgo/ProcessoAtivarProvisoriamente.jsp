<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="processoDt" class="br.gov.go.tj.projudi.dt.ProcessoDt" scope="session"/>


<html>
<head>
	<title>Destivar Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
</head>
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Ativar Processo Provisoriamente</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
				<input name="NovoProcessoStatus" type="hidden" value="<%=request.getAttribute("NovoProcessoStatus")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Ativar Processo Provisoriamente</legend>
						<fieldset class="formEdicao">
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th>&nbsp;</th>
										<th>N&uacute;mero do Processo</th>
										<th>Status Atual do Processo</th>
										<th>&nbsp;</th>
									</tr>
								</thead>
								<tbody>
									<tr class="primeiraLinha">
										<td align="center">&nbsp;</td>
										<td width="25%" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a>
										</td>
										<td width="25%" align="center">
											<%=processoDt.getProcessoStatus()%>
										</td>
										<td align="center">&nbsp;</td>
									</tr>
								</tbody>
							</table>		
						</fieldset>	
						<br />
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>