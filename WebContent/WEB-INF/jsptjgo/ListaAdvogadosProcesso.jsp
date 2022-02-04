<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>

<jsp:useBean id="processoDt" scope="request" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
	<head>
		<title>Advogados Proceso</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; Advogados no Processo</h2></div>
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Advogados Habilitados</legend>
					
					<br />
					<div> Processo </div>
			   		<span class="span"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
			   		<br />
					
					<%
						List listaAdvogados = (List) request.getAttribute("ListaAdvogados");		
						ProcessoParteAdvogadoDt objTemp;
						if (listaAdvogados != null && listaAdvogados.size() > 0){
							objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(0);
					%>
							
			     	<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna">
			                	<td>Parte</td>
			                	<td>Tipo</td>
			                	<td>OAB/Matrícula</td>
			                  	<td>Advogado</td>
			                  	<td>Habilitação</td>
			             	 </tr>
			           	</thead>
			          	<tbody id="tabListaAdvogadoParte">
						<%
							boolean boTeste=false;
							for(int i = 0 ; i< listaAdvogados.size();i++) {
						   		objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(i);
						%>
					  		<tr class="<%=(boTeste?"Linha1":"Linha2")%>">
					  			<td width="30%"><%= objTemp.getNomeParte()%></td> 
				                <td width="12%">
				                	<% if (objTemp.getPrincipal().equalsIgnoreCase("true")){%> <b>Adv. Principal</b> <% } %>
				                </td>
			       		        <td><%=objTemp.getOabNumero()+ " " + objTemp.getOabComplemento()%></td>
				                <td width="30%"><%= objTemp.getNomeAdvogado() %> </td>
				            	<td width="10%"> <%=objTemp.getDataEntrada()%> </td>
				       		</tr>
						<%
							boTeste = !boTeste;
							}
						} else { %>
						<br /> <br />
						<p align="center"><em><b>Nenhum Advogado habilitado nesse processo.</b></em></p>
						<% } %>
						</tbody>
					</table>
				</fieldset>
			</div>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>