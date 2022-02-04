<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="PendenciaResponsaveldt" class= "br.gov.go.tj.projudi.dt.PendenciaResponsavelDt" scope="session"/>
<jsp:useBean id="Pendenciadt" class= "br.gov.go.tj.projudi.dt.PendenciaDt" scope="session"/>


<%@page import="br.gov.go.tj.projudi.dt.ServentiaGrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%><html>
<head>
	<title>Histórica da Conclusão</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
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
			<div class="area"><h2>&raquo; Histórica da Conclusão</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post"  name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Histórica da Conclusão</legend>
						<fieldset class="formEdicao">
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th width="30%">Pendência</th>
										<th width="20%">N&uacute;mero Processo</th>
										<th colspan="2">Responsável Atual</th>
										
									</tr>
								</thead>							
								<tbody>
									<tr class="primeiraLinha">
										<td align="center"><%=Pendenciadt.getId()%></td>
										<td ><%=Pendenciadt.getPendencia()%></td>
										<td width="15%" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>"><%=Pendenciadt.getProcessoNumero()%></a>
										</td>
										<%if (Pendenciadt.getResponsavel() != null){%>
											<%if (Pendenciadt.getResponsavel().getServentiaCargo() != null
													&& Pendenciadt.getResponsavel().getServentiaCargo().length()>0) {%>
												<td><%if (Pendenciadt.getResponsavel() != null && Pendenciadt.getResponsavel().getServentiaCargo() != null){%><%=Pendenciadt.getResponsavel().getServentiaCargo()%><%}%></td>
											<td><%if (Pendenciadt.getResponsavel().getServentiaCargo() != null){%><%=Pendenciadt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%></td>
											<% } else if (Pendenciadt.getResponsavel().getId_UsuarioResponsavel() != null
													&& Pendenciadt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
												<td>MP-GO</td>
												<td><%=Pendenciadt.getResponsavel().getNomeUsuarioResponsavel()%></td>
											<%} %>
										<%} %>
									</tr>
								</tbody>
							</table>		
						</fieldset>
						
						<%if (PendenciaResponsaveldt.getListaHistoricoPendencia() != null && PendenciaResponsaveldt.getListaHistoricoPendencia().size()>0 ){%>
							<fieldset class="formEdicao">
								<legend>Histórico da Conclusão </legend>
								<% %>
								<div id="divTabela" class="divTabela" > 
							    	<table id="Tabela" class="Tabela">
							        	<thead>
							            	<tr>
											<th width="1%"></th>
											<th width="3%">Ident.</th>
											<th colspan="2" width="27%">Serventia Cargo / Nome</th>
											<th width="8%">Unid. de Trabalho</th>
											<th width="8%">Data Início</th>
											<th width="8%%">Data Final</th>
											
										</tr>
							           	</thead>
							           	<tbody id="tabListaHistorico">
										<%
							  			List liTemp = PendenciaResponsaveldt.getListaHistoricoPendencia();
										PendenciaResponsavelHistoricoDt objTemp;
							  			String stTempNome="";
							  			for(int i = 0 ; i< liTemp.size();i++) {
							      			objTemp = (PendenciaResponsavelHistoricoDt)liTemp.get(i); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                <tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                <tr class="TabelaLinha2">
											<%}%>
							                	<td> <%=i+1%></td>                   
							                   	<td><%= objTemp.getId_ServentiaCargo()%></td>               
							                   	<td><%= objTemp.getServentiaCargo()%></td>
							                   	<td><%= objTemp.getNome() %></td>
							                   	<td><%= objTemp.getServentiaGrupo() %></td>
							                   	<td><%= objTemp.getDataInicio()%></td>               
							                   	<td><%= objTemp.getDataFim()%></td>
							               </tr>
										<%}%>
							           </tbody>
							       </table>
							   </div> 
										
							</fieldset>
						<%} %>
					</fieldset>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>