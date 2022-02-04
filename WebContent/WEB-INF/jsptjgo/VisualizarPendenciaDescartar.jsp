<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>

<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<title>Pend&ecirc;ncia</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css"> 
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
	  <script type='text/javascript' src='./js/jquery.js'></script>      
	  <!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      <script type='text/javascript' src='./js/prototype.js'></script>
      <script type="text/javascript" src="./js/tabelas.js"></script>
      <script type="text/javascript" src="./js/tabelaArquivos.js"></script>
      <%@ include file="js/buscarArquivos.js"%>     
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
	<div id="divEditar" class="divEditar">
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncia</legend>
				
				<%if (AnalisePendenciadt.getPendenciaDt() !=  null){ 
					PendenciaDt pendenciaDt = AnalisePendenciadt.getPendenciaDt();%>
					<fieldset class="formLocalizar">
						<legend>Dados da Pend&ecirc;ncia</legend>
						
						<label class="formEdicaoLabel">N&uacute;mero:</label><br><b><%=pendenciaDt.getId()%></b><br />
				 
						<%if (pendenciaDt.getId_Processo() != null && !pendenciaDt.getId_Processo().equals("")){%>
							<label class="formEdicaoLabel">Processo:</label><br> 
							<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>">
								<%=pendenciaDt.getProcessoNumero()%>
							</a>
							<br />
												
							<label class="formEdicaoLabel">Movimenta&ccedil;&atilde;o:</label><br><%=pendenciaDt.getMovimentacao()%><br />
							
							<%if (pendenciaDt.getNomeParte() != null && !pendenciaDt.getNomeParte().trim().equals("")){%>
								<label class="formEdicaoLabel">Parte:</label><br><%=pendenciaDt.getNomeParte()%><br />
							<%}%>
						<%}%>
						
						<label class="formEdicaoLabel">Tipo de Pend&ecirc;ncia:</label><br> <%=pendenciaDt.getPendenciaTipo()%><br />
						<label class="formEdicaoLabel">Status:</label><br> <%=pendenciaDt.getPendenciaStatus()%><br />
						
						<%if (pendenciaDt.getDataVisto() != null && !(pendenciaDt.getDataVisto().equals(""))){ %>
							<label class="formEdicaoLabel">Data de Visto:</label><br>
						
							<%=pendenciaDt.getDataVisto()%>
							 
							<br />
						<%}%>
						
						<label class="formEdicaoLabel">Data Inicio:</label><br> <%=pendenciaDt.getDataInicio()%><br />
						<label class="formEdicaoLabel">Data Fim:</label><br> <%=pendenciaDt.getDataFim()%><br />
						
						<%if (pendenciaDt.getDataLimite() != null && !pendenciaDt.getDataLimite().equals("") ){%>
							<label class="formEdicaoLabel">Data Limite:</label><br> <%=pendenciaDt.getDataLimite()%><br />
						<%}%>
						
						<br />
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					
				<%} else {%>
					<h2>Pend&ecirc;ncia n&atilde;o encontrada</h2>
				<%}%>
			</fieldset>
		</form>
	</div>
 </div>
</body>
</html>
