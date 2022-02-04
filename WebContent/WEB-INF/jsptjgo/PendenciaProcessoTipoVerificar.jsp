<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<link rel="stylesheet" href="./css/jquery.tabs.css" type="text/css" media="print, projection, screen" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');

	</style>
			
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	
    
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->

	<%@ include file="js/PendenciaPegar.js"%>
	
</head>
<body>
<div  id="divCorpo" class="divCorpo">
	<br />
	<form action="Pendencia" method="post" id="Formulario">
			<div class="area"><h2>&raquo; Resolver Pend&ecirc;ncia Processo Tipo Verificar</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		
		<%if (Pendenciadt!= null  && Pendenciadt.getId() != null && !Pendenciadt.equals("")){ %>
			<div id="divEditar" class="divEditar">			
				
				<div id="dados">
					<label class="formEdicaoLabel" style="margin-top:0;">N&uacute;mero:</label><b><%=Pendenciadt.getId()%></b><br />
					
					<%if (Pendenciadt.getId_Processo() != null && !Pendenciadt.getId_Processo().equals("")){%>
						<label class="formEdicaoLabel" style="margin-top:0;">Processo:</label>
							<a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>"> 
								<%=Pendenciadt.getProcessoNumero()%>
							</a>
						<br />
						<label class="formEdicaoLabel" style="margin-top:0;">Movimenta&ccedil;&atilde;o:</label><%=Pendenciadt.getMovimentacao()%><br />
					<%}%>
					
					<label class="formEdicaoLabel" style="margin-top:0;">Data Inicio:</label> <%=Pendenciadt.getDataInicio()%><br />
					<label class="formEdicaoLabel" style="margin-top:0;">Cadastrador:</label> <%=Pendenciadt.getNomeUsuarioCadastrador()%><br />
					<br /><br />
					<div class="area"><h2>&raquo; Arquivos Pendência</h2></div>
					<%@ include file="TabelaArquivosPendencia.jspf" %>	
					<br /><br /><br /><br />
                    <% if (request.getAttribute("ocultarBotoesEstagiario") == null) { %>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					    <% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
		        			<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		           		<%  }%> 
						<%if (request.getAttribute("PaginaAtual") != null && request.getAttribute("PaginaAtual").equals(Configuracao.Salvar)){ %>
						<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')">
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
							Finalizar
						</button>
						<%} else if (request.getAttribute("PaginaAtual") != null && request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
						<button type="submit" name="operacao" value="Finalizar">
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Finalizar" /> -->
							Finalizar
						</button>
						<%if (request.getAttribute("habilitarResponder") != null && request.getAttribute("habilitarResponder").toString().equalsIgnoreCase("true")){ %> 
							<button type="submit" name="operacao" value="FinalizarMovimentar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')">
							<!--	<img src="imagens/22x22/ico_sucesso.png" alt="Finalizar e Movimentar" /> -->
									Finalizar Movimentando Processo
							</button>
						<% } else {%>
							<button type="submit" disabled="false" name="operacao" value="FinalizarMovimentar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')">
								<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Finalizar e Movimentar" /> -->
									Finalizar Movimentando Processo
							</button>
						<%} %>
						<%} %>
					</div>
                  <%} %>
				</div>				
			</div>
		<%} else {%>
			<h2>Pend&ecirc;ncia n&atilde;o encontrada</h2>
		<%}%>
			<br /><br />
			<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
	</form>
</div>
</body>
</html>