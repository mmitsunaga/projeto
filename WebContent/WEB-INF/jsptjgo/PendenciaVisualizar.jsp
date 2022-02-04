<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	            
	  <%@ include file="js/PendenciaPegar.js"%>
	  
	  <script type="text/javascript">
	  	//Modifica o modo de visualizacao da lista
	  	modoEdicaoPendencia = false;
	  </script>
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
	<div id="divEditar" class="divEditar">
		<fieldset class="fieldEdicaoEscuro">
			<legend>Pend&ecirc;ncia</legend>
			
			<%@ include file="Padroes/Mensagens.jspf"%>
			
			<%if (Pendenciadt!= null  && Pendenciadt.getId() != null && !Pendenciadt.equals("")){ %>
				<fieldset class="formLocalizar">
					<legend>Dados da Pend&ecirc;ncia</legend>
					
					<label class="formEdicaoLabel">N&uacute;mero:</label><br><b><%=Pendenciadt.getId()%></b><br />
			 
					<%if (Pendenciadt.getId_Processo() != null && !Pendenciadt.getId_Processo().equals("")){%>
						<label class="formEdicaoLabel">Processo:</label><br> 
						<a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>">
							<%=Pendenciadt.getProcessoNumero()%>
						</a>
						<br />
						
						<%if (Pendenciadt.getMovimentacao() != null && !Pendenciadt.getMovimentacao().equals("")){%>					
							<label class="formEdicaoLabel">Movimenta&ccedil;&atilde;o:</label><br><%=Pendenciadt.getMovimentacao()%><br />
						<%}%>
						
						<%if (Pendenciadt.getNomeParte() != null && !Pendenciadt.getNomeParte().trim().equals("")){%>
							<label class="formEdicaoLabel">Parte:</label><br><%=Pendenciadt.getNomeParte()%><br />
						<%}%>
					<%}%>
					
					<label class="formEdicaoLabel">Tipo de Pend&ecirc;ncia:</label><br> <%=Pendenciadt.getPendenciaTipo()%><br />
					<label class="formEdicaoLabel">Status:</label><br> <%=Pendenciadt.getPendenciaStatus()%><br />
					
					<%if (Pendenciadt.getDataVisto() != null && !(Pendenciadt.getDataVisto().equals(""))){ %>
						<label class="formEdicaoLabel">Data de Visto:</label><br>
					
						<%=Pendenciadt.getDataVisto()%>
						 
						<br />
					<%}%>
					
					<label class="formEdicaoLabel">Data Inicio:</label><br> <%=Pendenciadt.getDataInicio()%><br />
					<label class="formEdicaoLabel">Data Fim:</label><br> <%=Pendenciadt.getDataFim()%><br />
					
					<%if (Pendenciadt.getPrazo() != null && !Pendenciadt.getPrazo().equals("")) {%>
						<label class="formEdicaoLabel">Prazo:</label><br> <%=Pendenciadt.getPrazo()+ "  Dia(s)"%><br />
					<%} %>
					
					<%if (Pendenciadt.getDataLimite() != null && !Pendenciadt.getDataLimite().equals("") ){%>
						<label class="formEdicaoLabel">Data Limite:</label><br> <%=Pendenciadt.getDataLimite()%><br />
					<%}%>
					
					<br />
				</fieldset>
				
				<%if (request.getAttribute("ListaArquivos") != null){%>				
					<%request.setAttribute("temDataVisto", Pendenciadt.temDataVisto());%>					
					<%@include   file="Padroes/ListaArquivosVisualizar.jspf"%>
				<%}%>
				
			<%} else {%>
				<h2>Pend&ecirc;ncia n&atilde;o encontrada</h2>
			<%}%>
		</fieldset>
	</div>
 </div>
	<form action="Pendencia" method="post" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="OperacaoExpedirImprimir" name="OperacaoExpedirImprimir" type="hidden" value="<%=request.getAttribute("OperacaoExpedirImprimir")%>" />
		<%if(request.getAttribute("ultimaOperacao") != null && 
			 String.valueOf(request.getAttribute("OperacaoExpedirImprimir")).equalsIgnoreCase("ExpedirImprimir")){%>
			 <script type="text/javascript">			 	
			 	var form = document.getElementById("Formulario");
				form.submit();	
			 </script>    		  
		<%}%>		
	</form>		
</body>
</html>
