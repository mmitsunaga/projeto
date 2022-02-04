<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>

<jsp:useBean id="ProcessoResponsaveldt" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>

<html>
<head>
	<title>Troca de Redator do Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Trocar Redator de Processo</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input name="redator" id="redator" type="hidden" value="<%=request.getAttribute("redator")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend> Trocar Redator de Processo</legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th>N&uacute;mero Processo</th>
										<th>Redator</th>
										<th>Cargo Responsável Atual</th>
										<th>Nome Responsável Atual</th>											
										<th width="30%">Serventia</th>										
										<th></th>
									</tr>									
								</thead>
								<%		
								//A lista que vem no DT é sempre de um único processo. Foi feito dessa forma para seguir o padrão que
								//já havia sido criado para esse DT.
								ProcessoDt processoDt = (ProcessoDt)ProcessoResponsaveldt.getListaProcessos().get(0);
								boolean boLinha=false;
								%>
								<tbody>
									<tr class="TabelaLinha1">									
										<td width="15%" rowspan="3" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a>
										</td>
										<td align="center">
											<%if(processoDt.getServentiaCargoResponsavelDt() != null){
												if(processoDt.getServentiaCargoResponsavelDt().getId() != null && request.getAttribute("redator") != null && processoDt.getServentiaCargoResponsavelDt().getId().equalsIgnoreCase(request.getAttribute("redator").toString())){%>
													<input type="radio" name="rdRedator" value="<%=request.getAttribute("redator").toString()%>" checked="checked" onclick="javascript:document.getElementById('redator').value = this.value;" >
												<%} else { %>
													<input type="radio" name="rdRedator" value="<%=processoDt.getServentiaCargoResponsavelDt().getId()%>" onclick="javascript:document.getElementById('redator').value = this.value;" >
											<%	}
											}%>
										</td>											
										<td><%=(processoDt.getServentiaCargoResponsavelDt() != null?processoDt.getServentiaCargoResponsavelDt().getCargoTipo():"")%></td>																														
										<td><%=(processoDt.getServentiaCargoResponsavelDt() != null?processoDt.getServentiaCargoResponsavelDt().getNomeUsuario():"")%></td>									
										<td><%=processoDt.getServentiaCargoResponsavelDt().getServentia()%></td>											
									</tr>
									<tr class="TabelaLinha2">	
		      							<td align="center">
		      								<%if(processoDt.getServentiaCargoRevisorResponsavelDt() != null){
		      									if(processoDt.getServentiaCargoRevisorResponsavelDt().getId() != null && request.getAttribute("redator") != null && processoDt.getServentiaCargoRevisorResponsavelDt().getId().equalsIgnoreCase(request.getAttribute("redator").toString())){%>
												<input type="radio" name="rdRedator" value="<%=request.getAttribute("redator").toString()%>" checked="checked" onclick="javascript:document.getElementById('redator').value = this.value;" >
											<%} else { %>
												<input type="radio" name="rdRedator" value="<%=processoDt.getServentiaCargoRevisorResponsavelDt().getId()%>" onclick="javascript:document.getElementById('redator').value = this.value;" >
										<%	}
											}%>
										</td>			
		      							<td><%=(processoDt.getServentiaCargoRevisorResponsavelDt() != null?processoDt.getServentiaCargoRevisorResponsavelDt().getCargoTipo():"")%></td>
										<td><%=(processoDt.getServentiaCargoRevisorResponsavelDt() != null?processoDt.getServentiaCargoRevisorResponsavelDt().getNomeUsuario():"")%></td>									
										<td><%=(processoDt.getServentiaCargoRevisorResponsavelDt() != null?processoDt.getServentiaCargoRevisorResponsavelDt().getServentia():"")%></td>										
		      						</tr>
		      						<tr class="TabelaLinha1">	
										<td align="center">
											<%if(processoDt.getServentiaCargoVogalResponsavelDt() != null) {
			      									if(processoDt.getServentiaCargoVogalResponsavelDt().getId() != null && request.getAttribute("redator") != null && processoDt.getServentiaCargoVogalResponsavelDt().getId().equalsIgnoreCase(request.getAttribute("redator").toString())){%>
													<input type="radio" name="rdRedator" value="<%=request.getAttribute("redator").toString()%>" checked="checked" onclick="javascript:document.getElementById('redator').value = this.value;" >
												<%} else { %>
													<input type="radio" name="rdRedator" value="<%=processoDt.getServentiaCargoVogalResponsavelDt().getId()%>" onclick="javascript:document.getElementById('redator').value = this.value;" >
											<%	}
											}%>
										</td>
		      							<td><%=(processoDt.getServentiaCargoVogalResponsavelDt() != null?processoDt.getServentiaCargoVogalResponsavelDt().getCargoTipo():"")%></td>			
										<td><%=(processoDt.getServentiaCargoVogalResponsavelDt() != null?processoDt.getServentiaCargoVogalResponsavelDt().getNomeUsuario():"")%></td>									
										<td><%=(processoDt.getServentiaCargoVogalResponsavelDt() != null?processoDt.getServentiaCargoVogalResponsavelDt().getServentia():"")%></td>											
			      						</tr>
								</tbody>
							</table>		
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