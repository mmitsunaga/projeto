<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Situa??o do Processo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Situa??o do Processo</h2> </div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
			<fieldset id="VisualizaDados" class="formEdicao" style="background-color:#f5f5f5">
		      	<legend>Situa??o do Processo</legend>
		      	
		      	<br />
		      	<div> N?mero Processo </div>
				<span><a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=-1&Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/><br />
				<br />
				
				<!-- PEND?NCIAS DO PROCESSO -->
				<fieldset id="VisualizaDados">
		      		<legend>Pend?ncias no Processo</legend>
		
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
		          			
		          		if (listaAguardandoExpedicao != null && listaAguardandoExpedicao.size() > 0){
		          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Expedi??o </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Respons?vel</td>
						               	<td width="20%">Data In?cio </td>
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
				
					<% } //fim Aguardando Expedi??o
					
	        		if (listaAguardandoVerificacao != null && listaAguardandoVerificacao.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Verifica??o </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Respons?vel</td>
						               	<td width="20%">Data In?cio </td>
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
					
						<% } //fim Aguardando Expedi??o
						
	        		if (listaAguardandoVerificacaoServentiaCargo != null && listaAguardandoVerificacaoServentiaCargo.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Vista / Relat?rio / Revis?o</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="40%">Respons?vel</td>
						               	<td width="20%">Data In?cio </td>
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
			      			<legend>Pend?ncias Futuras</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Respons?vel</td>
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
			      			<legend>Pend?ncias de Libera??o de Acesso</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Respons?vel</td>
						               	<td width="20%">Acesso Liberado At?</td>
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
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[1]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim pendencias de libera??o de acesso
					
		        	if (listaAguardandoRecebimento != null && listaAguardandoRecebimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Recebimento </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Respons?vel</td>
						               	<td width="20%">Data In?cio </td>
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
							
		        	if (listaAguardandoCumprimento != null && listaAguardandoCumprimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Aguardando Cumprimento </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Respons?vel</td>
						               	<td width="20%">Data In?cio </td>
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
				      			<legend>Aguardando Corre??o </legend>
				    			<table id="Tabela" class="Tabela">
						        	<thead>
						           		<tr class="TituloColuna">
							           		<td width="5%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Data In?cio </td>
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
						               	<td width="20%">Data In?cio </td>
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
							               	<td width="20%">Respons?vel</td>
							               	<td width="20%">Data In?cio </td>
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
							               	<td width="20%">Data In?cio </td>
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
		        
				<!-- CONCLUS?ES EM ABERTO -->
				<fieldset id="VisualizaDados">
		      		<legend>Conclus?es Pendentes</legend>
					<table id="Tabela" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
			           			<td width="5%" class="colunaMinima">&nbsp;</td>
				               	<td>Tipo</td>
				                <td width="33%">Respons?vel</td>
				               	<td width="20%">Data In?cio</td>				               
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
				          		</tr>
			          		<% 		} 
				          		}
			          		} 
			          		if (!possuiConclusao){ %>
				          		<tr><td colspan="5"><em>Nenhuma Conclus?o em Aberto.</em></td></tr>
			          		<% } %>
						</tbody>
					</table>
				</fieldset>
				
				<!-- AUDI?NCIAS EM ABERTO -->
				<fieldset id="VisualizaDados">
		      		<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
		      			<legend>Sess?es Pendentes</legend>
		      		<% } else { %>
		      			<legend>Audi?ncias Pendentes</legend>
		      		<% }  %>
		
		    		<table id="Tabela" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
			           			<td width="25%">Tipo</td>
				               	<td width="20%">Respons?vel</td>
				               	<td width="15%">Data Agendada</td>
				               	<td width="15%">Serventia</td>				               	
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
						          		</tr>
						        	<% } %>
						        <% } else { %>
					          		<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
						      			<tr><td colspan="5"><em>Nenhuma Sess?o em Aberto.</em></td></tr>
						      		<% } else { %>
						      			<tr><td colspan="5"><em>Nenhuma Audi?ncia em Aberto.</em></td></tr>
						      		<% }  %>	
				          		<% } %>
				         <% } else { %>
					          		<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
						      			<tr><td colspan="5"><em>Nenhuma Sess?o em Aberto.</em></td></tr>
						      		<% } else { %>
						      			<tr><td colspan="5"><em>Nenhuma Audi?ncia em Aberto.</em></td></tr>
						      		<% }  %>	
				          		<% } %>
						</tbody>
					</table>
				</fieldset>
			
					<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>
					<!-- AUDI?NCIAS CEJUSC EM ABERTO -->
					<fieldset id="VisualizaDados">
			      		<legend>Audi?ncias CEJUSC Pendentes</legend>
			      				
			    		<table id="Tabela" class="Tabela">
				        	<thead>
				           		<tr class="TituloColuna">
				           			<td width="25%">Tipo</td>
					               	<td width="20%">Respons?vel</td>
					               	<td width="15%">Data Agendada</td>
					               	<td width="15%">Serventia</td>					               	
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
							          		</tr>
							        	<% } %>
							        <% } else { %>
						          		<tr><td colspan="5"><em>Nenhuma Audi?ncia CEJUSC em Aberto.</em></td></tr>	
					          		<% } %>
					          <% } else { %>
						          		<tr><td colspan="5"><em>Nenhuma Audi?ncia CEJUSC em Aberto.</em></td></tr>	
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