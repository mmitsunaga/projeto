<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- IMPORTS -->
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<!-- FIM IMPORTS -->

<!-- BEANS -->
<jsp:useBean id="Audienciadt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaDt" />
<!-- FIM BEANS -->

<!-- HTML -->
<html>
	<!-- HEAD -->
	<head>
		<!-- META -->
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
    	<!-- STYLE -->
    	<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
		<!-- FIM STYLE -->
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	</head>
	<!-- HEAD -->
	
	<!-- BODY -->
	<body>
		<!-- DIV CORPO -->
		<div id="divCorpo" class="divCorpo" >
	  		<!-- Título da Página -->
	  		<div class="area"><h2>&raquo;<%=request.getAttribute("TituloPagina")%></h2></div>
	    	
	    	<!-- FORM -->
	    	<form action="Audiencia" method="post" name="Formulario" id="Formulario">
	    		
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>" />
				<input type="hidden" id="__Pedido__" name=__Pedido__ value="<%=request.getAttribute("__Pedido__")%>" />
				
				<!-- FIELDSET -->
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Confirmação de Agendamento de Audiência </legend>

					<br />
					<div> Número Processo </div>
					<span>
						<%if( Audienciadt.getAudienciaProcessoDt() != null ) {
							if( Audienciadt.getAudienciaProcessoDt().getProcessoDt() != null ) {
								if( Audienciadt.getAudienciaProcessoDt().getProcessoDt().getProcessoNumero() != null ) {
									%>
									<%=Audienciadt.getAudienciaProcessoDt().getProcessoDt().getProcessoNumero()%>
									<%
								}
							}
						}%>
					</span>
					<br />
					
					<div> Data da Audiência </div>
					<span><%=Audienciadt.getDataAgendada()%></span>
					<br />
					
					<div> Tipo Audiência</div>
					<span><%=Audienciadt.getAudienciaTipo()%></span>
					<br />
					
					<div> Cargo</div>
					<span><%=Audienciadt.getAudienciaProcessoDt().getServentiaCargo()%></span>
					<br />
					                  	
				</fieldset>
				<!-- FIM FIELDSET -->
				<br >
				<br />
				
				<div id="divConfirmarSalvar" class="ConfirmarSalvar">
        			<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
        			<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
           			<% } %>
					<br />
					           			
					<input name="BotaoConfirmar" type="submit" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')">
					<input name="BotaoVoltar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')">
        			
      			</div>
				
				<!-- MENSAGENS -->
				<%@include file="Padroes/Mensagens.jspf"%> 
				<!-- FIM MENSAGENS -->
				
			</form>
			<!-- FIM FORM -->
		</div>
		<!-- FIM DIV CORPO -->
	</body>
	<!-- FIM BODY -->
</html>
<!-- FIM HTML -->