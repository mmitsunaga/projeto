<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
   <jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
   
<html>
	<head>
		<title>Publica&ccedil;&atilde;o P&uacute;blica</title>
		
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />

		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>      		
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type='text/javascript' src='./js/checks.js'></script>  
		<script type="text/javascript" src="./js/tabelas.js"></script>
		<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
		<%@ include file="js/buscarArquivos.js"%>
		
		<link rel="stylesheet" href="./css/jquery.tabs.css" type="text/css" media="print, projection, screen" />
		<link rel="stylesheet" href="./css/Principal.css" type="text/css" />
		
	</head>
	
	<body >
		<div id="divEditar" class="divEditar">
		  <div class="divCorpo">
			<form method="post" action="Pendencia" id="Formulario">
				<div class="area"><h2>&raquo; Publica&ccedil;&atilde;o P&uacute;blica</h2></div>
				<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				
				<input id="OperacaoExpedirImprimir" name="OperacaoExpedirImprimir" type="hidden" value="<%=request.getAttribute("OperacaoExpedirImprimir")%>" />
				<%@ include file="Padroes/Mensagens.jspf"%>
			
				<div id="insercao">
					<%@ include file="Padroes/InsercaoArquivosAssinador.jspf" %>
				</div>
						
				<br />	
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<%if (request.getAttribute("operacaoPendencia") != null && request.getAttribute("operacaoPendencia").equals("Confirmar")){ %>
					<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
		        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		           	<%  }%> 
					<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
						Confirmar
					</button>
				<%} else { %>
					<button type="submit" name="operacao" value="Criar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" >
						<!--  <img src="./imagens/imgSalvar.png" alt="Criar" /> -->
						Criar Publica&ccedil;&atilde;o
					</button>
				<%}%>
				<br /><br /><br />
				</div>
				
				<%if(request.getAttribute("OperacaoExpedirImprimir") != null && 
					 String.valueOf(request.getAttribute("OperacaoExpedirImprimir")).equalsIgnoreCase("ExpedirImprimir")){%>
					 <script type="text/javascript">			 	
					 	var form = document.getElementById("Formulario");
						form.submit();	
					 </script>    		  
				<%}%>		
			</form>
		  </div>
		</div>
		 		
	</body>
</html>