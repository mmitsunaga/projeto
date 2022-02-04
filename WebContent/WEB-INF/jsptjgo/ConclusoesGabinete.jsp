<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
	<head>
		<title>Conclusões do Gabinete</title>

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
			<div class="area"><h2>Lista de Conclusões do Gabinete </h2></div>
			<div id="divLocalizar" class="divLocalizar">
				<div id="divTabela" class="divTabela"> 
			    	<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna">
			            		<td align="center" width="5%">n.º</td>
			                  	<td align="center" width="5%">Dias</td>
			            		<td align="center" width="5%">Prioridade</td>
			                  	<td align="center" width="10%">Processo</td>
			                  	<td width="30%">Tipo</td>
			                  	<td width="45%">Usuário</td>
			                  	
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaProcesso">
						<%
							//("id_proc"),("proc_numero"), ("pend_tipo"), ("ID_PROC_PRIOR"), ("PROC_PRIOR"), ("PROC_PRIOR_CODIGO"), ("USUARIO"), ("DIAS")});
							//   0              1               2                  3                4               5                  6           7
							List liTemp = (List)request.getAttribute("ListaConclusoesGabinete");
															
							boolean boLinha=false; 
							//Percorre Lista Geral de Processos
							PendenciaDt pendDt = new PendenciaDt();
							String ultimoIdListado = "";
							int contador = 0;
							for(int i = 0 ; liTemp != null && i< liTemp.size();i++) {
								String[] dados = (String[])liTemp.get(i);
								String stUrgente = pendDt.getNumeroImagemPrioridade(dados[5]);
								String mensagemUrgente = dados[4];%>
								
								<tr class="TabelaLinha<%=(boLinha?1:2)%>"> 
								<%if(!ultimoIdListado.equals(dados[0])) {
									contador++;
									ultimoIdListado = dados[0];
									boLinha=!boLinha;%> 
									<td align="center"><%=contador%></td>
								  	<td align="center"><%=dados[7]%></td>
									<td align="center"><img src="./imagens/16x16/imgPrioridade<%=stUrgente%>.png" alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/></td>
				                   	<td align="center"><a href="BuscaProcesso?PaginaAtual=-1&Id_Processo=<%=dados[0]%>&PassoBusca=2"><%=dados[1]%></a></td>
				                   	<td><%=dados[2]%></td>
				  	            	<td><%=dados[6]%></td>			               		
							<%	} else { 	%> 								
									<td align="center"><%=contador%></td>
								  	<td align="center"><%=dados[7]%></td>
									<td align="center"><img src="./imagens/16x16/imgPrioridade<%=stUrgente%>.png" alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/></td>
				                   	<td align="center"><a href="BuscaProcesso?PaginaAtual=-1&Id_Processo=<%=dados[0]%>&PassoBusca=2"><%=dados[1]%></a></td>
				                   	<td><%=dados[2]%></td>
				  	            	<td><%=dados[6]%></td>		
							<%}%>
			               		</tr>
							<%}%>
			           	</tbody>
			       	</table>
  					</div> 
			</div>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body> 
</html>