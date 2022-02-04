<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>


<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%><html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Area  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Processos Dependentes/Apensos </h2></div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
		<fieldset id="VisualizaDados" class="VisualizaDados">
	      	<legend>Processos Dependentes/Apensos</legend>
	      	
	      	<br />
	      	<div> Número Processo </div>
			<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/><br />
			<br />
			
	
			<input type="hidden" id="posicaoLista" name="posicaoLista" value="<%=request.getAttribute("posicaoLista")%>">		
	    	<input type="hidden" id="Id_ProcessoApenso" name="Id_ProcessoApenso">
	    	<input type="hidden" id="ProcessoNumeroApenso" name="ProcessoNumeroApenso">
	    	<input type="hidden" id="Id_ProcessoDependente" name="Id_ProcessoDependente">
	    	<input type="hidden" id="ProcessoNumeroDependente" name="ProcessoNumeroDependente">
	    
    		<table id="Tabela" class="Tabela">
	        	<thead>
	           		<tr class="TituloColuna">
		           		<td>&nbsp;</td>
		               	<td>Número Processo</td>
		               	<td>Classe</td>
		               	<td> Tipo </td>
	    	        </tr>
	           	</thead>
	    		<%
					List listaDependentes = (List) request.getAttribute("listaApensos");		
					ProcessoDt tempProcessoDt;
					if (listaDependentes != null && listaDependentes.size() > 0){
				%>
	          	<tbody>
				<%
						boolean boTeste=false;
						int cont=1;
						for(int i = 0 ; i< listaDependentes.size();i++) {
							tempProcessoDt = (ProcessoDt)listaDependentes.get(i);
							
				%>
				 		<tr class="<%=(boTeste?"Linha1":"Linha2")%>"> 
	       			        <td><%=cont%></td>
	       			        <td align="center">
	       			        	<a href="BuscaProcesso?ProcessoOutraServentia=true&Id_Processo=<%=tempProcessoDt.getId_Processo()%>"><%=tempProcessoDt.getProcessoNumero()%></a>
	       			        </td>
	       			        
	       			        <td align="center"><%=tempProcessoDt.getProcessoTipo()%></td>
	       			        <%if (tempProcessoDt.getApenso() != null	&& tempProcessoDt.getApenso().equals("true") ) {%>
	       			        	<td align="center"> Apenso </td>
	       			        <%} else { %>
	       			         	<td align="center"> Dependente </td>
	       			        <%} %>
		       			</tr>
				<%
							boTeste = !boTeste;
							cont++;
							}
						
					} else { 
				%>
						<tr> 
							<td colspan="2"><em> Nenhum Processo Apenso/Dependente. </em></td>
						</tr>
				<% } %>
				</tbody>
			</table>
		</fieldset>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div> 
</body>
</html>