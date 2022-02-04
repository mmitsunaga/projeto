<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaStatusDt"%>
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
	          			List listaPendenciasSolicitacoesCarga = listaPendencias[12];
	          			List listaPendenciasSolicitacoesCargaAguardandoRetorno = listaPendencias[13];
	          			List listaPendenciasMandadoAguardandoCumprimento = listaPendencias[14];
	          			List listaPendenciasCorreios = listaPendencias[15];

		          		if (listaPendenciasCorreios != null && listaPendenciasCorreios.size() > 0){   	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Aguardando Correios </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Status</td>
						               	<td width="20%">Data Início </td>
					               		<td width="1%"></td>
					               		<td width="1%"></td>
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for(int i=0; i < listaPendenciasCorreios.size();i++){
				          			String[] pendenciaStr = (String[])listaPendenciasCorreios.get(i);
				          			String[] id_HashOriginal =  pendenciaStr[0].split("@#!@");
				          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td><%=pendenciaStr[1]%></td>
					          			<td align="center"><%=pendenciaStr[3]%></td>
					          			<td align="center"><%=pendenciaStr[2]%></td>
					          			<%
					          			if(Funcoes.StringToInt(pendenciaStr[6]) == PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM || Funcoes.StringToInt(pendenciaStr[6]) == PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS || Funcoes.StringToInt(pendenciaStr[6]) == PendenciaStatusDt.ID_INCONSISTENCIA_CORREIOS) {
					          			%>
						          			<td class="colunaMinima">
						          				<input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
						          				<%for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
					   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
			                    			 		if (id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
			                    				 		checked
			                    					<%}
				                    		 	}%>
					          				/>
					          				</td>
					          				<td class="colunaMinima">
						          				<a target="_blank" href="BuscaProcesso?PaginaAtual=6&Id_MovimentacaoArquivo=<%=pendenciaStr[7]%>&eCarta=true" style="display:inline-block">
													<img id="idVisualizarECarta<%=i+1%>" src="imagens/16x16/btn_endereco.png" alt="e-Carta" title="e-Carta" />
												</a>
					          				</td>
					          			<%} else {%>
					          				<td class="colunaMinima">&nbsp</td>
					          				<td class="colunaMinima">
						          				<a target="_blank" href="BuscaProcesso?PaginaAtual=6&Id_MovimentacaoArquivo=<%=pendenciaStr[7]%>&eCarta=true" style="display:inline-block">
													<img id="idVisualizarECarta<%=i+1%>" src="imagens/16x16/btn_endereco.png" alt="e-Carta" title="e-Carta" />
												</a>
											</td>
										<%}%>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
				
					<% } //fim Aguardando Correios
					
		          	if (listaAguardandoExpedicao != null && listaAguardandoExpedicao.size() > 0){%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Aguardando Expedição </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
						               	<td width="1%"></td>
						               	<td>Resolver</td>
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
					          			<td>
					          				<%if((pendenciaStr[4] != null && pendenciaStr[4].equalsIgnoreCase(""))| (pendenciaStr[4] != null && pendenciaStr[4].equalsIgnoreCase(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo()))) { %>
					          				<input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
					          			<%	
					   							for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
					   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
			                    			 		if (id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
			                    				 		checked
			                    					<%}
			                    		 		}
						   					%>
					          				/>
					          				<%} %>
					          			</td>
					          			<td class="colunaMinima">
											<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
													if((pendenciaStr[4] != null && pendenciaStr[4].equalsIgnoreCase(""))| (pendenciaStr[4] != null && pendenciaStr[4].equalsIgnoreCase(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo()))) {
														String img = "22x22/ico_solucionar.png";
														String titulo = "Solucionar a pend&ecirc;ncia";
													%>
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
														<img id="idResolverPendenciaAguardandoExpedicao<%=i+1%>" src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
													</a>
											<% 		}
												}%>
										</td>
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
						           		<td width="1%">&nbsp;</td>
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
					          			<%if ( (UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL 
					          					|| UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
					          					|| UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL 
					          					|| UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL)
						      				 && (pendenciaStr[5] == null || (Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_DISTRIBUICAO 
						      				 		&& Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO
						      				 		&& Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_CONEXAO
						      				 		&& Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO)
						      			         )
				          				  ) {%>
						          			<%if (pendenciaStr[5] == null || ( Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO)) { %>
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
						          			<%}%>
					          			
					          			</td>
					          			<td class="colunaMinima">							
											<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
												String img = "22x22/ico_solucionar.png";
												String titulo = "Solucionar a pend&ecirc;ncia";
											%>									
													
												<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
													<img id="idResolverPendenciaAguardandoVerificacao<%=i+1%>" src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
												</a>
											<%} %>
										</td>
									<%} else if (UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() != GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL 
				          							&& UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() != GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
				          							&& UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() != GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL 
				          							&& UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() != GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL) { %>
										<%if (pendenciaStr[5] == null || (Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_DISTRIBUICAO && Funcoes.StringToInt(pendenciaStr[5]) != PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO)) { %>
					          			<input class='chk' name='chk' type='checkbox'  title="Descartar Pendência" value="<%=pendenciaStr[0]%>"
					          			<%							   						
				   							for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
				   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
		                    			 		if (id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
		                    				 		checked
			                    		<% 		}
		                    		 		}						   						
						   					%>
					          			/>
					          			<% } %>
					          			</td>
					          			<td class="colunaMinima">							
											<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
												String img = "22x22/ico_solucionar.png";
												String titulo = "Solucionar a pend&ecirc;ncia";
											%>									
													
												<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
													<img src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
												</a>
											<%} %>
										</td>
									<%} %>
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
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data</td>
						                <td width="1%"></td>
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
					          			<td><input class='chk' name='chk' type='checkbox'  title="Descartar Pendência" value="<%=pendenciaStr[0]%>"
					          			<%							   						
				   							for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
				   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
		                    			 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
		                    				 		checked
		                    				<%	}
		                    		 		}%>
					          			/></td>
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
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Acesso Liberado Até</td>
						                <td width="1%"></td>
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
					          			<td><input class='chk' name='chk' type='checkbox'  title="Descartar Pendência" value="<%=pendenciaStr[0]%>"
					          			<%							   						
				   							for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
				   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
		                    			 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
		                    				 		checked
		                    		      		<%}
		                    		 		}						   						
						   					%>
					          			/></td>
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
						                <td width="1%">Resolver</td>
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
					          			<td class="colunaMinima">							
											<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
												String img = "22x22/ico_solucionar.png";
												String titulo = "Solucionar a pend&ecirc;ncia";
											%>									
													
												<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
													<img src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
												</a>
											<%} %>
										</td>
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
						                <td width="1%">Resolver</td>
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
					          			<td class="colunaMinima">							
											<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
												String img = "22x22/ico_solucionar.png";
												String titulo = "Solucionar a pend&ecirc;ncia";
											%>									
													
												<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
													<img src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
												</a>
											<%} %>
										</td>
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
						<% } //fim pendencias de solicitação de carga
						
	        		if (listaElaboracaoVoto != null && listaElaboracaoVoto.size() > 0){
			          	%>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Pendências Elaboração de Voto</legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="1%">&nbsp;</td>
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
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
						               	<td width="1%"></td>
						               	<td>Resolver</td>
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
					          			<td><input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
					          			<%							   						
				   							for (int j = 0;pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
				   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
		                    			 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
		                    				 		checked
    										<%	}
		                    		 		}						   						
						   					%>
					          			/></td>
					          			<td class="colunaMinima">							
											<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
												String img = "22x22/ico_solucionar.png";
												String titulo = "Solucionar a pend&ecirc;ncia";
											%>									
													
												<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
													<img id="idResolverPendenciaAguardandoDevolucao<%=i+1%>" src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
												</a>
											<%} %>
										</td>
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
							               	<td width="10%">Número</td>
							               	<td width="35%">Responsável</td>
							               	<td width="20%">Data Início</td>
							               	<td width="20%">Data Limite</td>
							               	<td width="10%">Resolver</td>
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
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          			<td align="center"><%=pendenciaStr[3]%></td>
						          			<td align="center"><%=pendenciaStr[4]%></td>
						          			<td align="center"><%=pendenciaStr[5]%></td>
						          			<td></td>
						          		</tr>
								     <% } %>
									</tbody>
								</table>
							</fieldset>
							
						<% } //fim Mandado Aguardando Cumprimento
							
		        	if (listaAguardandoCumprimento != null && listaAguardandoCumprimento.size() > 0){
				    %>
		          		<fieldset id="VisualizaDados" class="fieldEdicaoEscuro">
			      			<legend>Aguardando Cumprimento </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Responsável</td>
						               	<td width="20%">Data Início </td>
						               	<td width="1%"></td>
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
					          			<td>
					          			<%if (pendenciaStr.length < 5 || !Funcoes.isPendenciaSessaoVirtualNaoDescartar(pendenciaStr[4])) {%>
					          			<input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
					          			<%							   						
				   							for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
				   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
		                    			 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
		                    				 		checked
		                    		<% 			}
		                    		 		}
				   						
						   					%>
					          			/>
					          			<% } %>
					          			</td>
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
							           		<td width="1%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Data Início </td>
							               	<td width="1%"></td>
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
						          			<td>
						          			<%if (pendenciaStr.length < 5 || !Funcoes.isPendenciaSessaoVirtualNaoDescartar(pendenciaStr[4])) {%>
						          			<input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
						          			<%	
							   						
					   							for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
					   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
			                    			 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
			                    				 		checked
			                    		<% 			}
			                    		 		}							   					
							   					%>
						          			/>
						          			<% } %>
						          			</td>
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
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="20%">Data Início </td>
						               	<td width="20%">Data Fim </td>
						               	<td width="1%"></td>
						               	<td>Resolver</td>
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
					          			<td><input class='chk' name='chk' type='checkbox'  title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
					          			<%							   						
						   							for (int j = 0;pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
						   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
				                    			 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
				                    				 		checked
				                    		<% 			}
				                    		 		}						   						
						   					%>
					          			/></td>
					          			<td class="colunaMinima">
					          			<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
											String img = "22x22/ico_solucionar.png";
											String titulo = "Solucionar a pend&ecirc;ncia";
										%>	
											<a href="Pendencia?PaginaAtual=6&amp;pendencia=<%=id_HashOriginal[0]%>&amp;CodigoPendencia=<%=id_HashOriginal[1]%>&amp;fluxo=4">
												<img id="idResolverPendenciaAguardandoVisto<%=i+1%>" src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
											</a>
										<%} %>
									</td>
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
							           		<td width="1%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Responsável</td>
							               	<td width="20%">Data Início </td>
							               	<td width="20%">Data Limite </td>
							               	<td width="1%"></td>
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
						          			<td><%=pendenciaStr[1]%></td>
						          			<td align="center"><%=pendenciaStr[4]%></td>
						          			<td align="center"><%=pendenciaStr[2]%></td>
						          			<td align="center"><%=pendenciaStr[3]%></td>
						          			<td><input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
					          			<%							   						
					   							for (int j = 0;pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
					   								String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
			                    			 		if (id_HashSelecionado.length>0&& id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
			                    				 		checked
			                    		<% 			}
			                    		 		}						   						
						   					%>
					          			/></td>
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
							           		<td width="1%">&nbsp;</td>
							               	<td>Tipo</td>
							               	<td width="20%">Data Início </td>
							               	<td width="20%">Data Fim </td>
							               	<td>Resolver</td>
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
					          			<td class="colunaMinima">
					          				<% if (request.getAttribute("ocultarBotoesEstagiario") == null) {
												String img = "22x22/ico_solucionar.png";
												String titulo = "Solucionar a pend&ecirc;ncia";
											%>		
												<a href="Pendencia?PaginaAtual=6&amp;pendencia=<%=id_HashOriginal[0]%>&amp;fluxo=4&amp;CodigoPendencia=<%=id_HashOriginal[1]%>&amp;op=PrazoDecorrido">
													<img id="idResolverPendenciaAguardandoDecursoPrazo<%=i+1%>" src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
												</a>
											<%} %>
										</td>
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
						           		<td width="1%">&nbsp;</td>
						               	<td>Tipo</td>
						               	<td width="40%">Responsável</td>
						               	<td width="20%">Data Início </td>
						               	<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
						               		<td width="5%" class="colunaMinima">&nbsp;</td>
							               	<td>Trocar Responsável</td>
						               	<%} %>
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
					          			<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
						          		<td><input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=pendenciaStr[0]%>"
							          		<%									        		
							   					for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
							   						String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
					                   		 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
					                   			 		checked
					                   	<% 			}
					                   			}								   				
								   			%>
							          	/></td>
						          		
							                <td align="center">
						          				<%if(pendenciaStr[4].equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PEDIDO_VISTA))){%>
												<a href="PendenciaServentiaCargoResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
													<img id="idResolverPendenciaAguardandoRevisao<%=i+1%>" src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
												</a> 
												<%}%>
											</td>
						               	<%} %>
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
				               	<% 	if ( UsuarioSessao.isPodeAnalisar() || UsuarioSessao.isPodePreAnalisar()){ %>
				               	<td>Resolver</td>
				               	<% } %>
				               	<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
					               	<td width="5%" class="colunaMinima">&nbsp;</td>
				               		<td>Trocar Responsável</td>
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
				          		<% 
				          			//Tratando link para solucionar conclusão pré-analisada
				          			String[] id_HashOriginal =  conclusaoPendente[0].split("@#!@");
				          			if (Funcoes.StringToInt(conclusaoPendente[3])>0 ){%>
				          			<% 	if (UsuarioSessao.isPodePreAnalisar()){%>
					          			<td align="center">
							          		<a href="PreAnalisarConclusao?Id_Pendencia=<%=id_HashOriginal[0]%>&PaginaAtual=<%=Configuracao.Curinga6%>&numeroProcesso=<%=conclusaoPendente[4]%>" title="Resolver Conclusão">
												<img src="imagens/22x22/ico_solucionar.png" alt="Resolver Conclusão" title="Resolver Conclusão" />
											</a>
										</td>
									<% } %>				          		
				          		<% } else if (UsuarioSessao.isPodeAnalisar() || UsuarioSessao.isPodePreAnalisar()){ %>
						          		<td>
						          			<%
						          			//Link para solucionar conclusão não analisada (Juiz)
						          			if ( UsuarioSessao.isPodeAnalisar()){ %>
						          			<a href="AnalisarConclusao?Id_Pendencia=<%=id_HashOriginal[0]%>&PaginaAtual=<%=Configuracao.Localizar%>&numeroProcesso=<%=conclusaoPendente[4]%>" title="Resolver Conclusão">
												<img src="imagens/22x22/ico_solucionar.png" alt="Resolver Conclusão" title="Resolver Conclusão" />
											</a>
											<% }
						          			//Link para solução conclusão não analisada (assistente)
						          			else if (UsuarioSessao.isPodePreAnalisar()){%>
											<a href="PreAnalisarConclusao?Id_Pendencia=<%=id_HashOriginal[0]%>&PaginaAtual=<%=Configuracao.Localizar%>&numeroProcesso=<%=conclusaoPendente[4]%>" title="Resolver Conclusão">
												<img src="imagens/22x22/ico_solucionar.png" alt="Resolver Conclusão" title="Resolver Conclusão" />
											</a>
											<% } %>
						          		</td>
				          		<% } %>
				          		<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
				          		<td><input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=conclusaoPendente[0]%>"
					          		<%	
					          		//for(int k=0; k < listaConclusoesPendentes.size();k++){
						        		String[] pendenciaStr = (String[])listaConclusoesPendentes.get(i-1);
						        		id_HashOriginal =  pendenciaStr[0].split("@#!@");
					          			String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");						   				
					   					for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
					   						String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
			                   		 		if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
			                   			 		checked
			                   	<% 			}
			                   			}						   				
					          		//}
						   			%>
					          	/></td>
				          		
					               <td align="center">
										<a href="PendenciaServentiaCargoResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
											<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
										</a> 
									</td>
				               	<%} %>
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
				               	<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>			
					      			<td width="20%">Relator</td>
					      			<td width="20%">Redator</td>
					      		<% } else { %>
					      			<td width="20%">Responsável</td>	
					      		<% } %>				               	
				               	<td width="15%">Data Agendada</td>
				               	<td width="15%">Serventia</td>
				               	<td width="15%">Status</td>				               					               	
				               	<% if (request.getAttribute("podeMovimentarAudiencia") != null && request.getAttribute("podeMovimentarAudiencia").toString().equalsIgnoreCase("true")){ %>
				               	<td width="10%">Resolver</td>
				               	<%} %>
				               	<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
				               		<td>Trocar Responsável</td>
				               	<%} %>
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
				          			<% if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>
				          				<td><%=audienciaPendente[3]%></td>
				          				<%if (audienciaPendente.length >= 12) { %>
				          				<td><%=audienciaPendente[11]%></td>
				          				<% } else { %>
				          				<td><%=audienciaPendente[3]%></td>
				          				<% } %>
						      		<% } else { %>
						      			<td><%=audienciaPendente[3]%></td>	
						      		<% } %>
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
					          		<%
					          		//Link para movimentar audiência
					          		if (request.getAttribute("podeMovimentarAudiencia").toString().equalsIgnoreCase("true")){ %>
					          		<td>
					          			<%if (audienciaPendente[1].contains("[VIRTUAL]")){ %>
					          				[VIRTUAL - PJD]
					          			<%} else if (audienciaPendente.length >= 5 && audienciaPendente[4] != null && audienciaPendente[4].trim().length() > 0){ %>
					          				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaPendente[0]%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=null&TipoAudienciaProcessoMovimentacao=null&EhAlteracaoExtratoAta=S" title="Corrigir Extrato da Ata de Julgamento">
												<img src="imagens/22x22/ico_solucionar.png" alt="Corrigir Extrato da Ata de Julgamento" title="Corrigir Extrato da Ata de Julgamento" />
											</a>				
											<a href="Movimentacao?RedirecionaOutraServentia=13&Id_AudienciaProcesso=<%=audienciaPendente[0]%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=processoDt.getId_Processo()%>" title="Retirar Extrato da Ata de Julgamento">
												<img src="imagens/22x22/ico_solucionar.png" alt="Retirar Extrato da Ata de Julgamento" title="Retirar Extrato da Ata de Julgamento" />											
											</a>          			
					          									                   		
						          		<%} else if (request.getAttribute("processoSegundoGrau") != null && request.getAttribute("processoSegundoGrau").toString().equalsIgnoreCase("true")){ %>
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
					          		<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
						               <td align="center">
											<a href="AudienciaServentiaCargoResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;Id_AudienciaProcesso=<%=audienciaPendente[0]%>">
												<img src="imagens/22x22/btn_encaminhar.png" alt="Efetuar troca de responsável" title="Efetuar troca de responsável" />
											</a> 
										</td>
					               	<%}%>
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
					               	<% if (request.getAttribute("podeMovimentarAudienciaCejusc") != null && request.getAttribute("podeMovimentarAudienciaCejusc").toString().equalsIgnoreCase("true")){ %>
					               	<td width="10%">Resolver</td>
					               	<%} %>
					               	<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
					               		<td>Trocar Responsável</td>
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
							          			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaPendente[0]%>&PaginaAtual=<%=Configuracao.Novo%>" title="Concluir - Executa a movimentação da audiência cejusc selecionada">
							          				<img src="imagens/22x22/ico_solucionar.png" alt="Concluir Audiência CEJUSC" title="Concluir Audiência CEJUSC" />
							          			</a>
							          		</td>
							          		<%} %>
							          		<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
								               <td align="center">
													<a href="AudienciaServentiaCargoResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;Id_AudienciaProcesso=<%=audienciaPendente[0]%>">
														<img src="imagens/22x22/btn_encaminhar.png" alt="Efetuar troca de responsável" title="Efetuar troca de responsável" />
													</a> 
												</td>
							               	<%}%>
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
									<td width="5%" class="colunaMinima">&nbsp;</td>
									<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>										
										<td>Trocar Responsável</td>
									<%} %>
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
				          					if (votoEmentaPendente.length >= 8 && Funcoes.StringToInt(votoEmentaPendente[7]) == -3) {
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
	          						<% 					
									String[] id_HashOriginal =  votoEmentaPendente[0].split("@#!@");
									%>
									<td><input class='chk' name='chk' type='checkbox' title="Descartar Pendência"  value="<%=votoEmentaPendente[0]%>"
										<%	
										//for(int k=0; k < listaVotoEmenta.size();k++){
											String[] pendenciaStr = (String[])listaVotoEmenta.get(i-1);
											id_HashOriginal =  pendenciaStr[0].split("@#!@");
											String[] pendenciasSelecionadas = (String[])request.getAttribute("Ids_Pendencias");											
												for (int j = 0; pendenciasSelecionadas != null && j < pendenciasSelecionadas.length; j++) {
													String[] id_HashSelecionado =  pendenciasSelecionadas[j].split("@#!@");
													if (id_HashSelecionado.length>0 && id_HashSelecionado[0].equals(id_HashOriginal[0])){%> 
														checked
										<% 			}
												}											
										//}
										%>
									/></td>
									<%if(habilitado != null && habilitado.length()>0 && habilitado.equalsIgnoreCase("true")){ %>
									   <td align="center">
											<a href="PendenciaServentiaCargoResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=id_HashOriginal[0]%>&amp;CodigoPendencia=<%=id_HashOriginal[1]%>">
												<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
											</a> 
										</td>
									<%} %>		          					
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