<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>

<html>
	<head>
		<title><%=request.getAttribute("TituloPagina")%></title>
    	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
		
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; PROCESSOS A SEREM JULGADOS </h2></div>
			
			<form name="Formulario" id="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
				
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<div id="divEditar" class="divEditar">
				
				   	<div id="divTabela" class="divTabela">
				   		<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				                  	<th width="14%">Data Sessão</th>
				                  	<th width="10%">Processo</th>
				                  	<th>Recorrente</th>
				                  	<th>Recorrido</th>
				                  	<th width="10%">Movimentação</th>
				               	</tr>
				           	</thead>
			           		<tbody id="tabListaAudiencia"> 
							<%List listaSessoes = (List)request.getAttribute("ListaSessoes");
			 				AudienciaDt audienciaDt;
			  				boolean linha = false;
			  				int quantidadeProcessos = 1;

			  				//Verificar se foram encontradas sessões
			  				if (listaSessoes != null){
			  				    // Organizar os dados de cada sessão consultada para montar seu dados na tela
			  					for(int i = 0 ; i < listaSessoes.size();i++) {
			  	  					audienciaDt = (AudienciaDt)listaSessoes.get(i);
			  	  					//Buscar o(s) processo(s) vinculados à sessão em questão
			  	  					List processosSessao = audienciaDt.getListaAudienciaProcessoDt();
			  	  					//Verificar se a sessão está vinculada a algum processo
			  	  					if (processosSessao != null && processosSessao.size() > 0) {
			  	  						quantidadeProcessos = processosSessao.size();
			  	  					}
			  	  					//Variável para controlar se o processo é o primeiro a ser exibido vinculado à sessão
			  	  					boolean primeiro = true; %>
			  	  					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  	  					<td rowspan="<%=quantidadeProcessos%>"> <%=audienciaDt.getDataAgendada()%></td>
			  	                		<%
			  	                   		if (processosSessao != null && processosSessao.size()>0){
			  	                   			for (int j = 0; j< audienciaDt.getListaAudienciaProcessoDt().size(); j++){
			  	                   				AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessao.get(j); 
			  	                   				AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
			  	                   				if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
			  	                   					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
			  	                   				}%>		
			  	                   				<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
			  	                   				<% if (j > 0){%> 
			  	                   					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
			  	                   				<%} %>
		  	                   					<!-- PROCESSO --> 
		  	                   					<%if(audienciaProcessoDt.getProcessoDt() != null){%>
			  	                   					<td>
					  	                				<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
					  	                					<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
					  	                				</a>
					  	                			</td>
				  	                			<%} else if (audienciaProcessoFisicoDt != null) {%>
		  	                   						<td>
		  	                							<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
		  	                						</td>
	  	                   						<%}else{%>
	  	                   							<td>-</td>
	  	                   						<%}%>
	  	                   						<!-- FIM PROCESSO -->
				  	                			
				  	                			<!-- PROMOVENTES -->
				  	                			<td>
				  	                				<ul class="partes">
				  	                					<%if(audienciaProcessoDt.getProcessoDt() != null){%>
					  	                					<%
					  	                						List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
					  	                								  	                					if (listaPromoventes != null){
					  	                								  	                						for (int y = 0; y < listaPromoventes.size(); y++){
					  	                								  	                							ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
					  	                					%>
					  	                							<li><%=promovente.getNome()%></li>
					  	                						<%}
					  	                					}
				  	                					} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) { %>
			  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
			  	                						<%}%>
				  	                				</ul>
				  	                			</td>
				  	                			<!-- FIM PROMOVENTES -->
				  	                			
				  	                			<!-- PROMOVIDOS -->
					  	            			<td>
					  	            				<ul class="partes">
					  	            					<%if(audienciaProcessoDt.getProcessoDt() != null){%>
					  	                					<%
					  	                						List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
					  	                								  	                					if (listaPromovidos != null){
					  	                								  	                						for (int y = 0; y < listaPromovidos.size(); y++){
					  	                								  	                							ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
					  	                					%>	
					  	                							<li><%=promovido.getNome()%> </li>
					  	                						<%}
					  	                					} 
					  	            					} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) { %>
			  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
			  	                						<%}%>
				  	                				</ul>
				  	                			</td>
				  	                			<!-- FIM PROMOVIDOS -->
				  	                			
				  	                			<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
			  	                   				<%
				  	                			if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))){ %>
				  	                   			<td>Pendente</td>
				  	                   			<% }else{%>
				  	                   			<td><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
				  	                   			<% } 
			  	                			}
			  	                   		}else { %>
			  	                   			<td>-</td>
			  	                   			<td>-</td>
			  	                   			<td>-</td>
			  	                   			<td>-</td>
			  	                   		<%}%>
			  	               		</tr>
			  						<%linha = !linha;
			  					} //Fim FOR AUDIENCIADT
							}else{%> 
			  	               <tr class="TabelaLinha<%=(linha?1:2)%>"> 
			  						<td colspan="5"><em> Nenhum Processo a ser Julgado.</td>
			  					</tr>
			  				<%}%>
			           	</tbody>
				       	</table>
				   	</div>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>