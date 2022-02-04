<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Situação do Processo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Situação do Processo</h2> </div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
			<fieldset id="VisualizaDados" class="formEdicao" style="background-color:#f5f5f5">
		      	<legend>Situação do Processo</legend>
		      	
		      	<br />
		      	<div> Número Processo </div>
				<span><a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=-1&Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/><br />
				<br />
				
				<!-- PENDÊNCIAS DO PROCESSO -->
				<fieldset id="VisualizaDados">
		      		<legend>Pendências no Processo</legend>
		
					<% 
	          		List[] listaPendencias = null;
					
					if (request.getAttribute("ListaPendencias") != null)
						listaPendencias = (List[]) request.getAttribute("ListaPendencias");
	          		
	          		if (listaPendencias != null){
	          			List listaAguardandoExpedicao = listaPendencias[0];
	          			List listaAguardandoVisto = listaPendencias[1];
	          			List listaAguardandoRecebimento = listaPendencias[2];
	          			List listaAguardandoCumprimento = listaPendencias[3];
	          			List listaAguardandoVerificacao = listaPendencias[4];
	          			List listaDecursoPrazo = listaPendencias[5];
	          			List listaAguardandoLeitura = listaPendencias[6];
	          			List listaPendenciasFuturas = listaPendencias[7];
	          			List listaPendenciasLiberarAcesso = listaPendencias[8];
	          			List listaAguardandoVerificacaoServentiaCargo = listaPendencias[9];
	          			List listaAguardandoCorrecao = listaPendencias[10];
	          			List listaElaboracaoVoto = listaPendencias[11];
	          			List listaPendenciasSolicitacoesCarga = listaPendencias[12];
	          			List listaPendenciasSolicitacoesCargaAguardandoRetorno = listaPendencias[13];
	          			List listaPendenciasMandadoAguardandoCumprimento = listaPendencias[14];	          			
		          			
		          		if (listaAguardandoExpedicao != null && listaAguardandoExpedicao.size() > 0){
		          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Expedição </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoExpedicao.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoExpedicao.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
				
					<% } //fim Aguardando Expedição
					
	        		if (listaAguardandoVerificacao != null && listaAguardandoVerificacao.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Verificação </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoVerificacao.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoVerificacao.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim Aguardando Expedição
						
	        		if (listaAguardandoVerificacaoServentiaCargo != null && listaAguardandoVerificacaoServentiaCargo.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Vista / Relatório / Revisão</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="40%">Responsável</td>
						               	<td width="20%">Data Início </td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoVerificacaoServentiaCargo.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoVerificacaoServentiaCargo.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[1]%></td>
					          			<td><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[3]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim Aguardando Verificacao Serventia Cargo
						
	        		if (listaPendenciasFuturas != null && listaPendenciasFuturas.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Pendências Futuras</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data</td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaPendenciasFuturas.size();i++){
				          			String[] pendenciaStr = (String[])listaPendenciasFuturas.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim pendencias futuras
						
	        		if (listaPendenciasLiberarAcesso != null && listaPendenciasLiberarAcesso.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Pendências de Liberação de Acesso</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
<!-- 						               	<td width="20%">Responsável</td> -->
						               	<td width="20%">Acesso Liberado Até</td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaPendenciasLiberarAcesso.size();i++){
				          			String[] pendenciaStr = (String[])listaPendenciasLiberarAcesso.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
<%-- 					          			<td align="center"><%=pendenciaStr[2]%></td> --%>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim pendencias de liberação de acesso
	        		
						if (listaPendenciasSolicitacoesCarga != null && listaPendenciasSolicitacoesCarga.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Pendências de Solicitação de Carga</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="1%">&nbsp;</td>
						               	<td width="8%">Tipo</td>
						               	<td width="20%">Solicitante/Responsável - Serventia</td>
						               	<td width="10%">Solicitação</td>
						               	<td width="13%">Prazo para Efetuar Carga</td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaPendenciasSolicitacoesCarga.size();i++){
				          			String[] pendenciaStr = (String[])listaPendenciasSolicitacoesCarga.get(i);
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[1]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[3]%></td>
					          			<td align="center"><%=pendenciaStr[4]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim pendencias de solicitação de carga
						
						if (listaPendenciasSolicitacoesCargaAguardandoRetorno != null && listaPendenciasSolicitacoesCargaAguardandoRetorno.size() > 0){
				          	%>
			          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
				      			<legend>Pendências de Solicitação de Carga - Aguardando Devolução dos Autos</legend>
				    			<table id="Tabela" class="Tabela">
						        	<thead>
						           		<tr class="TituloColuna">
							           		<td width="1%">&nbsp;</td>
							               	<td width="8%">Tipo</td>
							               	<td width="20%">Solicitante/Responsável - Serventia</td>
							               	<td width="10%">Entrega dos Autos</td>
							               	<td width="13%">Prazo para Devolução dos Autos</td>
						    	        </tr>
						           	</thead>
						          	<tbody>
					          		<%
					          		for(int i=0; i < listaPendenciasSolicitacoesCargaAguardandoRetorno.size();i++){
					          			String[] pendenciaStr = (String[])listaPendenciasSolicitacoesCargaAguardandoRetorno.get(i);
					          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
					          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          			<td align="center"><%=pendenciaStr[3]%></td>
						          			<td align="center"><%=pendenciaStr[4]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
						
					<% } //fim pendencias de solicitação de carga
					
		        	if (listaAguardandoRecebimento != null && listaAguardandoRecebimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Recebimento </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoRecebimento.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoRecebimento.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
						
					<% } //fim Aguardando Recebimento
					
		        	if (listaPendenciasMandadoAguardandoCumprimento != null && listaPendenciasMandadoAguardandoCumprimento.size() > 0){
					    %>
			          		<fieldset id="VisualizaDados" class="VisualizaDados">
				      			<legend>Aguardando Cumprimento </legend>
				    			<table id="Tabela" class="Tabela">
						        	<thead>
						           		<tr class="TituloColuna">
							           		<td width="5%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Responsável</td>
							               	<td width="20%">Data Início</td>
						    	        </tr>
						           	</thead>
						          	<tbody>
					          		<%
					          		for(int i=0; i < listaPendenciasMandadoAguardandoCumprimento.size();i++){
					          			String[] pendenciaStr = (String[])listaPendenciasMandadoAguardandoCumprimento.get(i);
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[3]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim Mandado Aguardando Cumprimento
		        	
		        	if (listaAguardandoCumprimento != null && listaAguardandoCumprimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Cumprimento </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoCumprimento.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoCumprimento.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
						
					<% } //fim Aguardando Cumprimento
					
		        	if (listaAguardandoCorrecao != null && listaAguardandoCorrecao.size() > 0){
					    %>
			          		<fieldset id="VisualizaDados" class="VisualizaDados">
				      			<legend>Aguardando Correção </legend>
				    			<table id="Tabela" class="Tabela">
						        	<thead>
						           		<tr class="TituloColuna">
							           		<td width="5%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Data Início </td>
						    	        </tr>
						           	</thead>
						          	<tbody>
					          		<%
					          		for(int i=0; i < listaAguardandoCorrecao.size();i++){
					          			String[] pendenciaStr = (String[])listaAguardandoCorrecao.get(i);
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[0]%></td>
						          			<td align="center"><%=pendenciaStr[1]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim Aguardando Cumprimento
		        	
					if (listaAguardandoVisto != null && listaAguardandoVisto.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Visto </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Data Início </td>
						               	<td width="20%">Data Fim </td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoVisto.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoVisto.get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[0]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
						
					<% } //fim Aguardando Visto
					
					if (listaAguardandoLeitura != null && listaAguardandoLeitura.size() > 0){
					    %>
			          		<fieldset id="VisualizaDados" class="VisualizaDados">
				      			<legend>Aguardando Leitura </legend>
				    			<table id="Tabela" class="Tabela">
						        	<thead>
						           		<tr class="TituloColuna">
							           		<td width="5%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Responsável</td>
							               	<td width="20%">Data Início </td>
							               	<td width="20%">Data Limite </td>
						    	        </tr>
						           	</thead>
						          	<tbody>
					          		<%
					          		for(int i=0; i < listaAguardandoLeitura.size();i++){
					          			String[] pendenciaStr = (String[])listaAguardandoLeitura.get(i);
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[0]%></td>
						          			<td align="center"><%=pendenciaStr[3]%></td>
						          			<td align="center"><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim de aguardando leitura
					
					if (listaDecursoPrazo != null && listaDecursoPrazo.size() > 0){
					    %>	
			          		<fieldset id="VisualizaDados" class="VisualizaDados">
				      			<legend>Aguardando Decurso de Prazo </legend>
				    			<table id="Tabela" class="Tabela">
						        	<thead>
						           		<tr class="TituloColuna">
							           		<td width="5%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Data Início </td>
							               	<td width="20%">Data Fim </td>
						    	        </tr>
						           	</thead>
						          	<tbody>
					          		<%
					          		for(int i=0; i < listaDecursoPrazo.size();i++){
					          			String[] pendenciaStr = (String[])listaDecursoPrazo.get(i);
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[0]%></td>
						          			<td align="center"><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim decurso de prazo
							
		        }%>
		        </fieldset>
		        
				<!-- CONCLUSÕES EM ABERTO -->
				<fieldset id="VisualizaDados">
		      		<legend>Conclusões Pendentes</legend>
					<% 
					boolean habilitado = UsuarioSessao.isHabilitadoConsultarHistoricoConclusao();
// 					if (request.getAttribute("HabilitadoConsultarHistoricoConclusao") != null)
// 						habilitado = (String)request.getAttribute("HabilitadoConsultarHistoricoConclusao");
					
					%>
		    		<table id="Tabela" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
			           			<td width="5%" class="colunaMinima">&nbsp;</td>
				               	<td>Tipo</td>
				                <td width="33%">Responsável</td>
				               	<td width="20%">Data Início</td>
				               	<%if(habilitado){ %>
				               		<td>Consultar Histórico</td>
				               	<%} %>
			    	        </tr>
			           	</thead>
			          	<tbody>
			          	<% 
			          	
							List listaConclusoesPendentes = null;
			          		if (request.getAttribute("ConclusaoPendente") != null)
			          			listaConclusoesPendentes = (List)request.getAttribute("ConclusaoPendente");
			          	
			          		boolean possuiConclusao = false;
			          		int i=0;
			          		if (listaConclusoesPendentes != null && listaConclusoesPendentes.size() > 0) {
				          		Iterator iteratorConclusao = listaConclusoesPendentes.iterator();
				          		while (iteratorConclusao.hasNext())	{
				          			String[] conclusaoPendente = (String[])iteratorConclusao.next();
				          			if (conclusaoPendente != null){
				          				i += 1;
				          				possuiConclusao = true;
			          		%>
				          		<tr >
				          			<td><%=i%></td>
				          			<td width="50%"><%=conclusaoPendente[1]%></td>
				          			<td><%=conclusaoPendente[5]%></td>
				          			<td align="center"><%=conclusaoPendente[2]%></td>
					          		<%if(habilitado){ 
					          			String[] id_HashOriginal =  conclusaoPendente[0].split("@#!@");
					          		%>
						               <td align="center">
											<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
												<img src="imagens/22x22/ex_ico_solucionar.png" alt="Consultar Histórico" title="Consultar Histórico" />
											</a> 
										</td>
				               		<%} %>
				          		</tr>
			          		<% 		} 
				          		}
			          		} 
			          		if (!possuiConclusao){ %>
				          		<tr><td colspan="5"><em>Nenhuma Conclusão em Aberto.</em></td></tr>
			          		<% } %>
						</tbody>
					</table>
				</fieldset>
				
				<!-- AUDIÊNCIAS EM ABERTO -->
				<fieldset id="VisualizaDados">
		      		<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
		      			<legend>Sessões Pendentes</legend>
		      		<% } else { %>
		      			<legend>Audiências Pendentes</legend>
		      		<% }  %>
		
		    		<table id="Tabela" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
			           			<td width="25%">Tipo</td>
				               	<td width="20%">Responsável</td>
				               	<td width="15%">Data Agendada</td>
				               	<td width="15%">Serventia</td>
				               	<td width="15%">Status</td>
				               	<% if (request.getAttribute("podeMovimentarAudiencia") != null && request.getAttribute("podeMovimentarAudiencia").toString().equalsIgnoreCase("true")){ %>
				               	<td width="10%">Resolver</td>
				               	<%} %>
			    	        </tr>
			           	</thead>
			          	<tbody>
			          	<% 
			          	    if(request.getAttribute("AudienciaPendente") != null){
				          		List<String[]> audienciasPendentes = (List<String[]>)request.getAttribute("AudienciaPendente");
				          		if (audienciasPendentes != null && audienciasPendentes.size() > 0 && audienciasPendentes.get(0) != null) {
					          		for (String[] audienciaPendente : audienciasPendentes) {			          		
							          	%>
						          		<tr align="center">
						          			<td><%=audienciaPendente[1]%></td>
						          			<td><%=audienciaPendente[3]%></td>
						          			<td><%=audienciaPendente[2]%></td>
						          			<td><%=audienciaPendente[8]%></td>
							          		<td>
							          			<%if (audienciaPendente.length >= 5 && audienciaPendente[4] != null && audienciaPendente[4].trim().length() > 0){ %>
								          			AGUARDANDO ACÓRDAO/EMENTA
								          		<%} else {%>
								          			Aguardando Realização
							          			<%}%>				          			  
							          		</td>
						          		<%
						          		//Link para movimentar audiência
						          		if (request.getAttribute("podeMovimentarAudiencia") != null && request.getAttribute("podeMovimentarAudiencia").toString().equalsIgnoreCase("true")){ %>
						          		<td>
						          			<%if (audienciaPendente.length >= 5 && audienciaPendente[4] != null && audienciaPendente[4].trim().length() > 0){ %>
							          			Aguardando Acórdão / Ementa - Status: <%=audienciaPendente[5]%>
							          		<% } else if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>
							          			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaPendente[0]%>&PaginaAtual=<%=Configuracao.Novo%>" title="Concluir - Executa a movimentação da sessão selecionada">
							          				<img src="imagens/22x22/ico_solucionar.png" alt="Inserir Extrato da Ata de Julgamento" title="Inserir Extrato da Ata de Julgamento" />
							          			</a>
							          		<%} else {%>
							          			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaPendente[0]%>&PaginaAtual=<%=Configuracao.Novo%>" title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
							          				<img src="imagens/22x22/ico_solucionar.png" alt="Concluir Audiência" title="Concluir Audiência" />
							          			</a>
						          			<%}%>
						          		</td>
						          		<%} %>
						          		</tr>
						        	<% } %>
						        <% } else { %>
					          		<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
						      			<tr><td colspan="5"><em>Nenhuma Sessão em Aberto.</em></td></tr>
						      		<% } else { %>
						      			<tr><td colspan="5"><em>Nenhuma Audiência em Aberto.</em></td></tr>
						      		<% }  %>	
				          		<% } %>
				         <% } else { %>
					          		<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
						      			<tr><td colspan="5"><em>Nenhuma Sessão em Aberto.</em></td></tr>
						      		<% } else { %>
						      			<tr><td colspan="5"><em>Nenhuma Audiência em Aberto.</em></td></tr>
						      		<% }  %>	
				          		<% } %>
						</tbody>
					</table>
				</fieldset>
			
					<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>
					<!-- AUDIÊNCIAS CEJUSC EM ABERTO -->
					<fieldset id="VisualizaDados">
			      		<legend>Audiências CEJUSC Pendentes</legend>
			      				
			    		<table id="Tabela" class="Tabela">
				        	<thead>
				           		<tr class="TituloColuna">
				           			<td width="25%">Tipo</td>
					               	<td width="20%">Responsável</td>
					               	<td width="15%">Data Agendada</td>
					               	<td width="15%">Serventia</td>
					               	<td width="15%">Status</td>
					               	<% if (request.getAttribute("podeMovimentarAudienciaCejusc") != null && request.getAttribute("podeMovimentarAudienciaCejusc").toString().equalsIgnoreCase("true")){ %>
					               	<td width="10%">Resolver</td>
					               	<%} %>
				    	        </tr>
				           	</thead>
				          	<tbody>
				          	<% 
				          	    if (request.getAttribute("AudienciaCejuscPendente") != null) {
					          		List<String[]> audienciasCEJUSCPendentes = (List<String[]>)request.getAttribute("AudienciaCejuscPendente");
					          		if (audienciasCEJUSCPendentes != null && audienciasCEJUSCPendentes.size() > 0 && audienciasCEJUSCPendentes.get(0) != null) {
						          		for (String[] audienciaPendente : audienciasCEJUSCPendentes) {					          		
								          	%>
							          		<tr align="center">
							          			<td><%=audienciaPendente[1]%></td>
							          			<td><%=audienciaPendente[3]%></td>
							          			<td><%=audienciaPendente[2]%></td>
							          			<td><%=audienciaPendente[8]%></td>
								          		<td>Aguardando Realização</td>
							          		<%
							          		//Link para movimentar audiência
							          		if (request.getAttribute("podeMovimentarAudienciaCejusc") != null && request.getAttribute("podeMovimentarAudienciaCejusc").toString().equalsIgnoreCase("true")){ %>
							          		<td>
							          			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaPendente[0]%>&PaginaAtual=<%=Configuracao.Novo%>" title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
							          				<img src="imagens/22x22/ico_solucionar.png" alt="Concluir Audiência" title="Concluir Audiência" />
							          			</a>
							          		</td>
							          		<%} %>
							          		</tr>
							        	<% } %>
							        <% } else { %>
						          		<tr><td colspan="5"><em>Nenhuma Audiência CEJUSC em Aberto.</em></td></tr>	
					          		<% } %>
					          <% } else { %>
						          		<tr><td colspan="5"><em>Nenhuma Audiência CEJUSC em Aberto.</em></td></tr>	
					          <% } %>
							</tbody>
						</table>
					</fieldset>
				</fieldset>
			<% } %>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div> 
</body>
</html>