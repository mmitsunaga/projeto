<!-- DOCTYPE -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- IMPORTS -->
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<!-- FIM IMPORTS -->

<!-- BEANS -->
<jsp:useBean id="Audienciadt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaDt" />
<!-- FIM BEANS -->

<!-- HTML -->
<html>
	<!-- HEAD -->
	<head>
		<!-- TITLE -->
		<title><%=request.getAttribute("TituloPagina")%></title>
    	
    	<!-- META -->
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<!-- STYLE -->
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
		<!-- FIM STYLE -->
				
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		

	</head>
	<!-- FIM HEAD -->
	
	<!-- BODY -->
	<body>
		<!-- DIV CORPO -->
		<div id="divCorpo" class="divCorpo">
			<!-- Título da Página -->
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%></h2></div>
	  		
	  		<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgImprimir" alt="Imprimir" class="imgImprimir" title="Imprimir Relatório de Audiências" name="imaImprimir" type="image"  src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>'); FormSubmit('Formulario');" />		  	
			</div>
			
			<!-- FORM -->
			<form name="Formulario" id="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
				
				<!-- INPUT HIDDEN -->
				<!-- Página Atual -->
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>">
				<!-- Menu Acionado -->
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>">
				<!-- FIM INPUT HIDDEN -->
				
				<!-- DIV EDITAR -->
				<div id="divEditar" class="divEditar" >
	
				   	<!-- DIV TABELA -->
				   	<div id="divTabela" class="divTabela">
				   	
				   		<!-- TABLE -->
				   		<table id="Tabela" class="Tabela">
				    		<!-- thead -->
				        	<thead>
								<tr class="TituloColuna1">
								    <% if (request.getAttribute("podeMovimentar") != null && request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>
										<th colspan="6">Serventia: <%=Audienciadt.getServentia()%></th>
									<%} else { %>
										<th colspan="5">Serventia: <%=Audienciadt.getServentia()%></th>
									<%} %>
								</tr>
				            	<tr class="TituloColuna">
				                 	<th width="10%">Data/Hora</th>
				                  	<th>Processo</th>
				                  	<th>Promovente</th>
				                  	<th>Promovido</th>
				                  	<th width="10%">Status</th>
				                  	<% if (request.getAttribute("podeMovimentar") != null && request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>
				                  	<th width="8%">Concluir</th>
				                  	<% } %>
				               	</tr>
				           	</thead>
				           	<!-- Fim thead -->
				           	
				           	<!-- tbody -->
				           	<tbody id="tabListaAudiencia"> 
								<%List listaAudiencias = (List)request.getAttribute("ListaAudiencias");
				 				AudienciaDt audienciaDt;
				  				boolean linha = false;
				  				int quantidadeProcessos = 1;

				  				//Verificar se foram encontradas audiências
				  				if (listaAudiencias != null){
				  				    // Organizar os dados de cada audiência consultada para montar seu dados na tela
				  				    //For para a lista de audiências consultadas (FOR AUDIENCIADT)
				  					for(int i = 0 ; i < listaAudiencias.size();i++) {
				  	  					audienciaDt = (AudienciaDt)listaAudiencias.get(i);
				  	  					//Buscar o(s) processo(s) vinculados à audiência em questão
				  	  					List processosAudiencia = audienciaDt.getListaAudienciaProcessoDt();
				  	  					//Verificar se a audiência está vinculada a algum processo
				  	  					if (processosAudiencia != null && processosAudiencia.size() > 0) {
				  	  						quantidadeProcessos = processosAudiencia.size();
				  	  					}
				  	  					//Variável para controlar se o processo é o primeiro a ser exibido vinculado à audiência
				  	  					boolean primeiro = true; %>
				  	  					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  	                		<%
				  	                		//Deve mostrar a data da audiência somente uma vez no caso de ter mais de um processo vinculado à audiência
				  	                		if (primeiro){ %> 
					  	                		<td rowspan="<%=quantidadeProcessos%>" align="center"> <%=audienciaDt.getDataAgendada()%></td>
				  	                   		<%}
				  	                   		
				  	                   		if (processosAudiencia != null && processosAudiencia.size()>0){
				  	                   		    // For para a lista de processos da audiência em questão (FOR AUDIENCIAPROCESSODT)
				  	                   			for (int j = 0; j< audienciaDt.getListaAudienciaProcessoDt().size(); j++){
				  	                   				AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosAudiencia.get(j);
					  	                   			AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
				  	                   				if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
				  	                   					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
				  	                   				}%>		
				  	                   				<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
				  	                   				<% if (!primeiro){%> 
				  	                   					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  	                   				<%} %>
			  	                   					<!--  -->
			  	                   					<td>
			  	                   						<%if(audienciaProcessoDt.getProcessoDt() != null){%>
						  	                				<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
						  	                					<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
						  	                				</a>
						  	                			<%} else if (audienciaProcessoFisicoDt != null) {%>
				  	                   						<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
			  	                   						<%}else{%>
			  	                   							-
			  	                   						<%}%>
					  	                			</td>
					  	                			
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
				  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente() == null ? "" : audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
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
				  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido() == null ? "" : audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
				  	                						<%}%>
					  	                				</ul>
					  	                			</td>
					  	                			<!-- FIM PROMOVIDOS -->
					  	                			
					  	                			<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
					  	                			<%if (primeiro){ %>
					  	                					<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatus()%></td>
					  	                					<%primeiro = !primeiro;
				  	                   				} else { %> 
				  	                   					</tr> 	
				  	                   				<%}
					  	                			
					  	                		    //Atributo consultaAdvogado criado apenas para controlar a montagem da tela que
					  	              			    //ficava desconfigurada ao listar as sessões de 2 grau para o advogado. Setado na AudienciaCt.java
					  	                			if(request.getAttribute("consultaAdvogado") == null) { 
					  	                			
							  	                			if ((audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA)) && 
						  	                					(request.getAttribute("podeMovimentar") != null && request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")))){ %>
							  	                   			<td rowspan="<%=quantidadeProcessos%>">
							  	                   				<%if (audienciaProcessoFisicoDt != null) {%>
								  	                   				<a href="AudienciaProcessoFisicoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Concluir - audiência(s) selecionada(s)">
								  	                   				Concluir
								  	                   				</a>
								  	                   			<% }else{%>	
							  	                   					<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
							  	                   					Concluir
							  	                   					</a>
							  	                   				<% } %>
							  	                   			</td>
							  	                   		<% }else{ %>
<%-- 								  	                   		<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td> --%>
							  	                   		<% } 
						  	                   		 }
				  	                			} //Fim FOR AUDIENCIAPROCESSODT
				  	                   		} else { %>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   		<%}%>
				  	                   		<%if (primeiro){%> 
				  	                    		<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatus()%></td> 
				  	                    	<%}%>
				  	               		</tr>
				  						<%linha = !linha;
				  					} //Fim FOR AUDIENCIADT
								}else{%> 
				  	               <tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  					</tr>
				  				<%}%>
				           	</tbody>
				           	<!-- Fim tbody -->
				           	
				       	</table>
				       	<!-- FIM TABLE -->
				       	
				   	</div>
				   	<!-- FIM DIV TABELA -->
				   	
				</div>
				<!-- FIM DIV EDITAR -->
				
				<% if (listaAudiencias != null){%>
				    <%@ include file="Padroes/PaginacaoAudiencia.jspf"%>
				<%}%>
			</form>
			<!-- FIM FORM -->
			
			<!-- MENSAGENS ERRO OU SUCESSO -->
			<%@ include file="Padroes/Mensagens.jspf" %>
			
		</div>
		<!-- FIM DIV CORPO -->
		
	</body>
	<!-- FIM BODY -->
	
</html>
<!--FIM HTML -->