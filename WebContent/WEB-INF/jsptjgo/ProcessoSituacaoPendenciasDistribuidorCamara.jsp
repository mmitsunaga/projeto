<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>


<%@page import="java.util.Iterator"%><html>
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
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Situação do Processo </h2></div>

		<form action="DescartarPendenciaProcesso" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		
			<fieldset id="VisualizaDados" class=fieldEdicaoEscuro>
		      	<legend>Situação do Processo</legend>
		      	
		      	
		      	<div> Número Processo: 
				<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
				</div>
				 
				<div class="clear"></div>
				    
					<%if (request.getAttribute("PaginaAtual") != null && !request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado) && request.getAttribute("ocultarBotoesEstagiario") == null){ %>
					    <button type="submit" name="operacao" value="DescartarPendencias" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" >
						  Descartar Pendências
		   				</button>
	   				 <%}%>
									
				
				<br />
				
				<!-- PENDÊNCIAS DO PROCESSO -->
				<fieldset id="VisualizaDados">
		      		<legend>Pendências no Processo</legend>
		
					<% 
	          		List[] listaPendencias = null;					
					if ( request.getAttribute("ListaPendencias") != null)
						listaPendencias = (List[]) request.getAttribute("ListaPendencias");
					
					String habilitado = null;					
					if (request.getAttribute("HabilitadoTrocarResponsavelConclusao") != null)
						habilitado = (String)request.getAttribute("HabilitadoTrocarResponsavelConclusao");
	          		
	          		if (listaPendencias != null){
	          			List listaAguardandoExpedicao = listaPendencias[0];
	          			List listaAguardandoVisto = listaPendencias[1];
	          			List listaAguardandoRecebimento = listaPendencias[2];
	          			List listaAguardandoCumprimento = listaPendencias[3];
	          			List listaAguardandoVerificacao = listaPendencias[4];
	          			List listaDecursoPrazo = listaPendencias[5];
	          			List listaAguardandoLeitura = listaPendencias[6];
	          			List listaPendenciasFuturas = listaPendencias[7];
	          			List listaPendenciasLiberacaoAcesso = listaPendencias[8];
	          			List listaAguardandoVerificacaoServentiaCargo = listaPendencias[9];
	          			List listaAguardandoCorrecao = listaPendencias[10];
	          			List listaElaboracaoVoto = listaPendencias[11];
		          			
		          		if (listaAguardandoExpedicao != null && listaAguardandoExpedicao.size() > 0){
		          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
				
					<% } //fim Aguardando Expedição
					
	        		if (listaAguardandoVerificacao != null && listaAguardandoVerificacao.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Aguardando Verificação </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
						                <td width="1%"></td>
						                <td>Resolver</td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaAguardandoVerificacao.size();i++){
				          			String[] pendenciaStr = (String[])listaAguardandoVerificacao.get(i);
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[1]%></td>
					          			<td align="center"><%=pendenciaStr[3]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td>
					          			<%if ( UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_CAMARA &&
					          					( pendenciaStr[5] == null || (Funcoes.StringToInt(pendenciaStr[5]) == PendenciaTipoDt.VERIFICAR_DISTRIBUICAO 
					          						|| Funcoes.StringToInt(pendenciaStr[5]) == PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO
					          						|| Funcoes.StringToInt(pendenciaStr[5]) == PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO
					          						|| Funcoes.StringToInt(pendenciaStr[5]) == PendenciaTipoDt.VERIFICAR_CONEXAO
					          						) )
					          				 ) { %>
					          				
					          				<%if (pendenciaStr[5] == null || (Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO)) { %>
							          			<input class='chk' name='chk' type='checkbox'  title="Descartar Pendência" value="<%=pendenciaStr[0]%>"
							          			<%	
								   						if (pendenciasSelecionadas != null && pendenciasSelecionadas.length>0){
								   							for (int j = 0; j < pendenciasSelecionadas.length; j++) {
								   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
						                    			 		if (id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
						                    				 		checked
						                    		<% 			}
						                    		 		}
								   						}
								   					%>
							          			/>
							          			</td>
							          		<% } %>
						          			<td class="colunaMinima">							
												<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
													String img = "22x22/ico_solucionar.png";
													String titulo = "Solucionar a pend&ecirc;ncia";
												%>									
														
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
														<img src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
													</a>
												<%} %>
										<% } %>
										</td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim Aguardando Expedição
						
	        		if (listaPendenciasFuturas != null && listaPendenciasFuturas.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
					
						<% } //fim pendencias futuras
						
	        		if (listaPendenciasLiberacaoAcesso != null && listaPendenciasLiberacaoAcesso.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Pendências de Liberação de Acesso</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Acesso Liberado Até</td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaPendenciasLiberacaoAcesso.size();i++){
				          			String[] pendenciaStr = (String[])listaPendenciasLiberacaoAcesso.get(i);
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
					
						<% } //fim pendencias de liberação de acesso
						
	        		if (listaElaboracaoVoto != null && listaElaboracaoVoto.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Pendências Elaboração de Voto</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data da pendência</td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaElaboracaoVoto.size();i++){
				          			String[] pendenciaStr = (String[])listaElaboracaoVoto.get(i);
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
					
						<% } //fim pendencias de elaboração voto
					
		        	if (listaAguardandoRecebimento != null && listaAguardandoRecebimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Aguardando Devolução </legend>
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
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
						
					<% } //fim Aguardando Recebimento
							
		        	if (listaAguardandoCumprimento != null && listaAguardandoCumprimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
						
					<% } //fim Aguardando Cumprimento
					
		        	if (listaAguardandoCorrecao != null && listaAguardandoCorrecao.size() > 0){
					    %>
			          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
					          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
					          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim Aguardando Cumprimento
		        	
					if (listaAguardandoVisto != null && listaAguardandoVisto.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[1]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<td align="center"><%=pendenciaStr[3]%></td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
						
					<% } //fim Aguardando Visto
					
					if (listaAguardandoLeitura != null && listaAguardandoLeitura.size() > 0){
					    %>
			          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
					          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
					          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
			          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
					          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
					          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
					          		%>
							      		<tr>
							      			<td><%=i+1%></td>
						          			<td><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          			<td align="center"><%=pendenciaStr[3]%></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim decurso de prazo
						
					if (listaAguardandoVerificacaoServentiaCargo != null && listaAguardandoVerificacaoServentiaCargo.size() > 0){
						
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
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
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
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
							
		        }%>
		        </fieldset>
		        
				<!-- CONCLUSÕES EM ABERTO -->
				<fieldset id="VisualizaDados" class="VisualizaDados">
		      		<legend>Conclusões Pendentes</legend>
		      		<table id="Tabela" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
			           			<td width="5%" class="colunaMinima">&nbsp;</td>
				               	<td>Tipo</td>
				               	<td width="33%">Responsável</td>
				               	<td width="20%">Data Início</td>
				               	<td>Status</td>
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
			          				String status = "Aguardando Análise";
			          				if (Funcoes.StringToInt(conclusaoPendente[3]) > 0) {
			          					status = "Pré-Analisada";
			          					if (conclusaoPendente.length == 8 && Funcoes.StringToInt(conclusaoPendente[7]) == -3) {
			          						status += " (Aguardando Assinatura)";
			          					} 		          					
			          				}	
			          	%>
			          		<tr>
			          			<td><%=i%></td>
			          			<td width="50%"><%=conclusaoPendente[1]%></td>
			          			<td><%=conclusaoPendente[5]%></td>
			          			<td align="center"><%=conclusaoPendente[2]%></td>
				          		<td align="center"><%=status%></td>
			          		</tr>
			          	<%   }
			          	   }
		          		}
		          		if (!possuiConclusao) { %>
			          		<tr><td colspan="5"><em>Nenhuma Conclusão em Aberto.</em></td></tr>
			          	<% } %>
						</tbody>
					</table>
				</fieldset>
				
				<!-- AUDIÊNCIAS / SESSÕES EM ABERTO -->
				<fieldset id="VisualizaDados" class="VisualizaDados">
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
			    	        </tr>
			           	</thead>
			          	<tbody>
			          	<% 
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
					          			<% if (audienciaPendente.length >= 11 && audienciaPendente[10] != null && audienciaPendente[10].trim().length() > 0) { %>
					          				<%=audienciaPendente[10]%>
					          			<% } else if (audienciaPendente.length >= 5 && audienciaPendente[4] != null && audienciaPendente[4].trim().length() > 0){ %>
						          			Aguardando Acórdão / Ementa - Status: <%=audienciaPendente[5]%>
						          		<%} else {%>
						          			Aguardando Realização
					          			<%}%>				          			  
					          		</td>	
				          		</tr>
			          		<%}%>
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
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>Audiências CEJUSC Pendentes</legend>
						<table id="Tabela" class="Tabela">
				        	<thead>
				           		<tr class="TituloColuna">
					               	<td width="25%">Tipo</td>
					               	<td width="20%">Responsável</td>
					               	<td width="15%">Data Agendada</td>
					               	<td width="15%">Serventia</td>
					               	<td width="15%">Status</td>				               					               	
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
						          		</tr>
					          		<%}%>
					          	<% } else { %>
					          		<tr><td colspan="5"><em>Nenhuma Audiência CEJUSC em Aberto.</em></td></tr>	
					          	<% } %>
					        <% } else { %>
					        	<tr><td colspan="5"><em>Nenhuma Audiência CEJUSC em Aberto.</em></td></tr>	
					        <% } %>
							</tbody>
						</table>
					</fieldset>			
				<% } %>
				
				<%
				  String podeVisualizarVotoEmenta = "";
				  if (request.getAttribute("podeVisualizarVotoEmenta") != null)
					  podeVisualizarVotoEmenta = (String) request.getAttribute("podeVisualizarVotoEmenta");
					  
				  if (podeVisualizarVotoEmenta!= null && podeVisualizarVotoEmenta.length()>0 &&  podeVisualizarVotoEmenta.equalsIgnoreCase("true")){%>
			
					<!-- VOTO / EMENTA EM ABERTO -->
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>Voto / Ementa Pendentes</legend>
						<% 
						    habilitado = null;
						    if (request.getAttribute("HabilitadoTrocarResponsavelVotoEmenta") != null)
								habilitado = (String)request.getAttribute("HabilitadoTrocarResponsavelVotoEmenta");
						%>
					
						<table id="Tabela" class="Tabela">
							<thead>
								<tr class="TituloColuna">
									<td width="5%" class="colunaMinima">&nbsp;</td>
									<td>Tipo</td>
									<td width="33%">Responsável</td>
									<td width="20%">Data Início</td>
									<td>Status</td>
								</tr>
							</thead>
							<tbody>
							<% 
							List listaVotoEmenta = null;
							if (request.getAttribute("VotoEmentaPendente") != null)
								listaVotoEmenta = (List) request.getAttribute("VotoEmentaPendente");
							
							boolean possuiVotoEmenta = false;
							i=0;
							if (listaVotoEmenta != null && listaVotoEmenta.size() > 0) {
								Iterator iteratorConclusao = listaVotoEmenta.iterator();		          			
								while (iteratorConclusao.hasNext())	{
									String[] votoEmentaPendente = (String[])iteratorConclusao.next();
									if (votoEmentaPendente != null){
										i += 1;
										possuiVotoEmenta = true;
										String status = "Aguardando Análise";
				          				if (Funcoes.StringToInt(votoEmentaPendente[3]) > 0) {
				          					status = "Pré-Analisada";
				          					if (votoEmentaPendente.length == 8 && Funcoes.StringToInt(votoEmentaPendente[7]) == -3) {
				          						status += " (Aguardando Assinatura)";
				          					} 		          					
				          				}
							%>
								<tr>
									<td><%=i%></td>
									<td width="50%"><%=votoEmentaPendente[1]%></td>
									<td><%=votoEmentaPendente[5]%></td>
									<td align="center"><%=votoEmentaPendente[2]%></td>
									<td align="center"><%=status%></td>
								</tr>
							<%   }
							   }
							}
							if (!possuiVotoEmenta) { %>
								<tr><td colspan="5"><em>Nenhum Voto / Ementa em Aberto.</em></td></tr>
							<% } %>
							</tbody>
						</table>
					</fieldset>
			    <%}%>				
			</fieldset>
				
			<%if (request.getAttribute("PaginaAtual") != null && request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){%>
				<div class="Centralizado"> 	
					<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
		        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		           	<% } %>
					<br />
					<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')" >
						Confirmar
					</button>
				</div>
		     <%}%>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div> 
</body>
</html>