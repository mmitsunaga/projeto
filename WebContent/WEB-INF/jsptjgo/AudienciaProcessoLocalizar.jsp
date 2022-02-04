<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>


<jsp:useBean id="Audienciadt" scope="request" class= "br.gov.go.tj.projudi.dt.AudienciaDt" />

<html>
	<head>
		<title><%=request.getAttribute("TituloPagina")%></title>
    	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
		
		<link href="./css/menusimples.css" type="text/css"  rel="stylesheet" />
		
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>  
   		
   		<script type="text/javascript">
	    	function submeter(action, passoEditar, paginaAtual, id_AudienciaProcesso){		    	
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PassoEditar', passoEditar);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_AudienciaProcesso', id_AudienciaProcesso);	    		
	    		document.Formulario.submit();
	    	}
	    </script>
	    <script type="text/javascript">
			$(document).ready(function() {			
				criarMenu('opcoes','Principal','menuA','menuAHover');
			});			
		</script>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
			
			<%
				boolean ehCamaraSegundoGrauTitulo = (request.getSession().getAttribute("UsuarioSessaoDt") != null) &&  ( ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isSegundoGrau() || ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isUPJTurmaRecursal());												  
				boolean ehGabineteSegundoGrauTitulo = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && (((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isGabineteSegundoGrau() );
				String complementoTitulo = "";				
				if(ehCamaraSegundoGrauTitulo || ehGabineteSegundoGrauTitulo) {
					complementoTitulo += " [";
					if (Audienciadt.isVirtual()) {
						complementoTitulo += "VIRTUAL";
					} else {
						complementoTitulo += "PRESENCIAL";
					}
					complementoTitulo += "]";
				}
			%>
		
	  		<div class="area"><h2>&raquo; PROCESSOS DA SESSÃO DE JULGAMENTO<%=complementoTitulo%></h2></div>
			
			<form name="Formulario" id="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
			
				<%				
					AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
					List listaAudienciaProcessosAdiadosIniciados = Audienciadt.getListaAudienciaProcessoDtAdiadosIniciados();
	 				AudienciaProcessoDt audienciaProcessoDt;
	  				boolean linha = false;
	  				long quantidadeTotal = 0;
	  				
	  				boolean ehCamaraSegundoGrau = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && ( ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isSegundoGrau() || ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isUPJTurmaRecursal());												  
					
	  				boolean ehGabineteSegundoGrau = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isGabineteSegundoGrau();
	  				
	  				boolean ehTurmaJulgadora = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isTurmaJulgadora();
	  				
					boolean ehJuizTurma = ((request.getSession().getAttribute("UsuarioSessaoDt") != null) && 
										    (Funcoes.StringToInt(((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).getGrupoTipoCodigo()) == GrupoTipoDt.JUIZ_TURMA));
	  				
	 				//Verificar se foram encontrados processos na audiência
	  				if ((ehCamaraSegundoGrau || ehGabineteSegundoGrau) && listaAudienciaProcessosAdiadosIniciados != null && listaAudienciaProcessosAdiadosIniciados.size() > 0){
	  					quantidadeTotal += listaAudienciaProcessosAdiadosIniciados.size();
				%>
				
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>">
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input type="hidden" id="PassoEditar" name="PassoEditar" value="<%=request.getAttribute("PassoEditar")%>" />
				<input type="hidden" name="__Pedido__" id="__Pedido__" value="<%=request.getAttribute("__Pedido__")%>" />	
				<input type="hidden"  id="Id_AudienciaProcesso" name="Id_AudienciaProcesso">
				<input type="hidden" name="Id_Audiencia" id="Id_Audiencia" value="<%=request.getAttribute("Id_Audiencia")%>" />
				
				<div id="divEditar" class="divEditar" >
				
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					
					<div id="divTabela" class="divTabela">				   	
				   		<table id="Tabela" class="Tabela">
				        	<thead>
								<tr class="TituloColuna1">
									<th colspan="10">
										Sessão de Julgamento: <%=Audienciadt.getDataAgendada()%>																				
									</th>
								</tr>				            					               	
				           	</thead>	           	
				           					           	
				       	</table>
				   	</div>
				   										
					   	<div id="divTabela" class="divTabela">
					   	
					   		<table id="Tabela" class="Tabela">
					        	<thead>
									<tr class="TituloColuna1">
										<th colspan="11">Julgamentos Adiados</th>
									</tr>
					            	<tr class="TituloColuna">
					                  	<th class="colunaMinima"></th>
					                  	<th width="20%">Processo</th>
					                  	<th width="10%">Sessão Adiamento</th>
					                  	<th width="20%">Recorrente / Paciente</th>
				                  	    <th width="20%">Recorrido</th>
				                  	    <th width="12%">MP Responsável</th>
				                  	    <th width="20%">Relator</th>
					                  	<th width="10%">Movimentação</th>
					                  	<th class="colunaMinima"></th>
					                  	<th class="colunaMinima">Adiado</th>
					                  	<th class="colunaMinima">Iniciado</th>
					               	</tr>
					           	</thead>
					           	<tbody id="tabListaAudiencia"> 
									<%
									linha = false;									
				  					for(int i = 0 ; i < listaAudienciaProcessosAdiadosIniciados.size();i++) {
					  	  					audienciaProcessoDt = (AudienciaProcessoDt)listaAudienciaProcessosAdiadosIniciados.get(i);
					  	  					audienciaProcessoFisicoDt = null;
						  	  				if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
		  	                   					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
		  	                   				}
									    %>
					  	  					<tr class="TabelaLinha<%=(linha?1:2)%>">
					  	  						<td><%=Funcoes.preencheZeros(i+1, 2)%></td>
						  	  					<td> 
		                   						<%if(audienciaProcessoDt.getProcessoDt() != null){%>
			               							<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
			           								<%=Funcoes.formataNumeroProcesso(audienciaProcessoDt.getProcessoDt().getProcessoNumero())%>
			              							<%if(audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0){%>
			              								&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
			              							<%}%>
			              							</a>
			              						<%} else if (audienciaProcessoFisicoDt != null) {%>
		  	                   						<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
	  	                   						<%}else{%>
		  	                   						    -
	  	                   						<%}%>
			               						</td>
			               						<td align="center"> 
		                   							<%=audienciaProcessoDt.getDataAudienciaOriginal()%> <%=(audienciaProcessoDt.isJulgamentoIniciado() ? "(I)" : "(A)")%>          							
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
			  	                								<%
			  	                									}
			  	                									  	                							}
			  	                										  	                							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
			  	                								%>
			  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
			  	                						<%
			  	                							}
			  	                						%>
		  	                						</ul>
		  	                					</td>
		  	                					<!-- FIM PROMOVENTES -->
							  	                			
		  	                					<!-- PROMOVIDOS -->
			  	            					<td>
			  	            						<ul class="partes">
			  	            							<%
			  	            								if(audienciaProcessoDt.getProcessoDt() != null){
			  	            							%>
			  	                							<%
			  	                								List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
			  	                								  	                								  	                							if (listaPromovidos != null){
			  	                								  	                								  	                								for (int y = 0; y < listaPromovidos.size(); y++){
			  	                								  	                								  	                									ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
			  	                							%>	
			  	                									<li><%=promovido.getNome()%> </li>
			  	                								<%
			  	                									}
			  	                									  	                									  	                							}
			  	                									  	                									  	            							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
			  	                								%>
			  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
			  	                						<%
			  	                							}
			  	                						%>
		  	                						</ul>
		  	                					</td>
		  	                					<!-- FIM PROMOVIDOS -->
		  	                					<td>
		  	                						<%
		  	                							if(audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0){
		  	                						%>
		  	                							<%=audienciaProcessoDt.getNomeMPProcesso()%>
		  	                						<%
		  	                							} else {
		  	                						%>
		  	                							Não há
		  	                						<%
		  	                							}
		  	                						%>
		  	                					</td>
		  	                					<td><%=audienciaProcessoDt.getNomeResponsavel()%></td>	
		  	                					<%
			  	                						if (Audienciadt.isVirtual()) {
			  	                					%>
										  	    	<td aling="center">[VIRTUAL - PJD]</td>
									  	    	<%
									  	    		} else {
									  	    	%>											  	    		               						
				  	                   				<%
											  	    		               										  	                   					if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))){
											  	    		               										  	                   				%>								  	        
										  	        <td aling="center">	   
											  	    <%
	   											  	    	if ((request.getAttribute("PodeMovimentar") != null) && (String.valueOf(request.getAttribute("PodeMovimentar")).trim().equalsIgnoreCase("S"))) {
	   											  	    %>
											  	    	<%
											  	    		if (audienciaProcessoDt.isAnalistaPodeMovimentar()) {
											  	    	%>      	     	
											  	            <div id="opcoes" class="menuEspecial"> 
									                   			<ul> 
									                   				<li>Opções<ul>						                   				    			                   			
												                   	     <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=3" title="Julgamento Iniciado">
																			Julgamento Iniciado
																			</a>
																		 </li> 
																		 <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=4" title="Julgamento Adiado">
																			Julgamento Adiado
																			</a>
																		 </li> 																		 
												                   	     <li> 
												                   	     	<a href="#" onclick="javascript: submeter('AudienciaSegundoGrau','1','<%=Configuracao.Salvar%>','<%=audienciaProcessoDt.getId()%>');return false;" title="Manter Adiamento">
												                   	     	Manter Adiamento
												                   	     	</a>
												                   	     </li>
												                   	     <li>
										                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Inserir Extrato da Ata de Julgamento">
											  	            				Inserir Extrato da Ata de Julgamento
											  	            				</a> 
										                   				 </li> 
										                   				 <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=5" title="Retirar de Pauta">
																			Retirar de Pauta
																			</a>
																		 </li>
																		 <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=6" title="Desmarcar Sessão">
																			Desmarcar Sessão
																			</a>
																		 </li>                  			
									                   				</ul></li>
									                   			</ul>
									                   		</div>
									                   		<%
									                   			} else {
									                   		%>
									                   			Aguardando Acórdão / Ementa - Status: <%=audienciaProcessoDt.getAudienciaProcessoStatusAnalista()%>
									                   			<br />
									                   			<div id="opcoes" class="menuEspecial"> 
										                   			<ul> 
										                   				<li>Opções<ul>						                   				    			                   			
													                   	     <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=null&EhAlteracaoExtratoAta=S" title="Corrigir Extrato da Ata de Julgamento">
																				Corrigir Extrato da Ata de Julgamento
																				</a>
																			 </li> 
																			 <li> 
																				<a href="Movimentacao?RedirecionaOutraServentia=13&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retirar Extrato da Ata de Julgamento
																				</a>
																			 </li>
										                   				</ul></li>
										                   			</ul>
										                   		</div>
									                   		<%
									                   			}
									                   		%>
								                   		<%
								                   			} else if ((((request.getAttribute("PodeAnalisar") != null) && (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) || ((request.getAttribute("PodePreAnalisar") != null) && (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
								                   								                   								                   			if(!audienciaProcessoDt.isAnalistaPodeMovimentar()){
								                   								                   								                   				if (audienciaProcessoDt.isResponsavelSessao()){
								                   		%>
									                   			<div id="opcoes" class="menuEspecial"> 
										                   			<ul> <li>Opções<ul>							                   					                  			
													                   	     <li>
											                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1" title="Pré-Analisar Acórdão/Ementa">
												  	            				Pré-Analisar Acórdão/Ementa
												  	            				</a> 
											                   				 </li> 
											                   				 <%
 											                   				 	if((request.getAttribute("PodeAnalisar") != null) && (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))){
 											                   				 %> 
														                   	     <li>
												                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2" title="Inserir Acórdão/Ementa">
													  	            				Inserir Acórdão/Ementa
													  	            				</a> 
												                   				 </li>
											                   				 <%
											                   				 	}
											                   				 %>                   			
										                   				</ul></li>
										                   			</ul>
										                   		</div>
										                   		<%
										                   			} else {
										                   		%>
									                   				Aguardando Acórdão / Ementa - Status: <%=audienciaProcessoDt.getAudienciaProcessoStatusAnalista()%>
									                   			<%
									                   				}
									                   			%>
								                   			<%
								                   				} else {
								                   			%>
								                   				Aguardando Extrato da Ata de Julgamento
								                   			<%
								                   				}
								                   			%>												                   	
								                   		<%
												                   									                   			}
												                   									                   		%>								  	            
										  	      	</td>
										  	        <td></td>
										  	     	<%
										  	     		} else {
										  	     	%>
											  	     	<td>
											  	        	<%=audienciaProcessoDt.getDataMovimentacao()%><br />
											  	        	<font size="1"><%=audienciaProcessoDt.getAudienciaProcessoStatus()%></font>
											  	        	<%
											  	        		if ((request.getAttribute("PodeMovimentar") != null) && (String.valueOf(request.getAttribute("PodeMovimentar")).trim().equalsIgnoreCase("S"))) {
											  	        	%>
											  	        		<br />
											  	        		<div id="opcoes" class="menuEspecial"> 
										                   			<ul> 
										                   				<li>Opções<ul>						                   				    			                   			
													                   	     <li> 
																				<%
 																					if(audienciaProcessoDt.exibeOpcaoRetornarSessaoJulgamento()) {
 																				%>
																				<a href="Movimentacao?RedirecionaOutraServentia=14&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retornar para Sessão de Julgamento
																				</a>																				
																				<%
																																									} else {
																																								%>
																				<a href="Movimentacao?RedirecionaOutraServentia=12&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retirar Acórdão/Ementa e Extrato da Ata de Julgamento
																				</a>
																				<%
																					}
																				%>
																			 </li>
										                   				</ul></li>
										                   			</ul>
										                   		</div>
											  	        	<%
											  	        		}
											  	        	%>
											  	        </td>
							  	                		<td>
						  	                   				<input name="formLocalizarimgResponsaveis" type="image" src="./imagens/imgAssistente.png" onclick="AlterarValue('Id_AudienciaProcesso','<%=audienciaProcessoDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');" title="Visualizar Responsáveis pela Sessão"/>
						  	                   			</td>
						  	                			<%
						  	                				}
						  	                			%>
						  	                		<%
						  	                			}
						  	                		%>  						  	                		
						  	                		<td>
						  	                			<%
						  	                				if (audienciaProcessoDt.possuiArquivoAtaSessaoAdiada()){
						  	                			%>
						  	                				<a href="AudienciaSegundoGrau?PassoEditar=1&IdArquivo=<%=audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada()%>"><img style="border: none;" src="./imagens/22x22/ico_arquivos.png" title="Extrato da Ata de Adiamento de Julgamento do Processo">&nbsp;</a>
						  	                			<%
						  	                				}
						  	                			%>
						  	                		</td>
						  	                		
						  	                		<td>
						  	                			<%
						  	                				if (audienciaProcessoDt.possuiArquivoAtaSessaoIniciada()){
						  	                			%>
						  	                				<a href="AudienciaSegundoGrau?PassoEditar=1&IdArquivo=<%=audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada()%>"><img style="border: none;" src="./imagens/22x22/ico_arquivos.png" title="Extrato da Ata de Inicio de Julgamento do Processo">&nbsp;</a>
						  	                			<%
						  	                				}
						  	                			%>
						  	                		</td>
					  	                	</tr>
					  						<%
					  							linha = !linha;
					  									  									  					} //Fim FOR AUDIENCIADT
					  						%>			   								
					           	</tbody>
					       	</table>
					   	</div>				   	
				   	<%
				   					   		} 
				   					   			   					   			   	
				   					   			   					   			   	   //Verificar se foram encontrados processos na audiência
				   					   			   					   			   		List listaAudienciaProcessosPautaDia = Audienciadt.getListaAudienciaProcessoDtPautaDia();  
				   					   			   					   			  				if (listaAudienciaProcessosPautaDia != null && listaAudienciaProcessosPautaDia.size() > 0){
				   					   			   					   			  					quantidadeTotal += listaAudienciaProcessosPautaDia.size();
				   					   	%>
									
					   	<div id="divTabela" class="divTabela">
					   	
					   		<table id="Tabela" class="Tabela">
					        	<thead>
									<tr class="TituloColuna1">
										<th colspan="8">Sessão de Julgamento: <%=Audienciadt.getDataAgendada()%> - Pauta do dia</th>
									</tr>
					            	<tr class="TituloColuna">
					            		<th class="colunaMinima"></th>
					                  	<th width="20%">Processo</th>
					                  	<th width="20%">Recorrente / Paciente</th>
				                  	    <th width="20%">Recorrido</th>
				                  	    <th width="12%">MP Responsável</th>
				                  	    <th width="20%">Relator</th>
					                  	<th width="10%">Movimentação</th>
					                  	<th class="colunaMinima"></th>
					               	</tr>
					           	</thead>
					           	<tbody id="tabListaAudiencia"> 
									<%
 										linha = false;					  				
 									 											  					for(int i = 0 ; i < listaAudienciaProcessosPautaDia.size();i++) {
 									 											  	  					audienciaProcessoDt = (AudienciaProcessoDt)listaAudienciaProcessosPautaDia.get(i);
 									 											  	  					audienciaProcessoFisicoDt = null;
 									 												  	  				if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
 									 										  	                   					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
 									 										  	                   				}
 									%>
				  	  					<tr class="TabelaLinha<%=(linha?1:2)%>">
				  	  						<td><%=Funcoes.preencheZeros(i+1, 2)%></td>
					  	  					<td> 
	                   						<%
 	                   							if(audienciaProcessoDt.getProcessoDt() != null){
 	                   						%>
		               							<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
		           								<%=Funcoes.formataNumeroProcesso(audienciaProcessoDt.getProcessoDt().getProcessoNumero())%>		              							
		              							<%
		              									              								if(audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0){
		              									              							%>
		              								&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
		              							<%
		              								}
		              							%>
		              							</a>
		              						<%
		              							} else if (audienciaProcessoFisicoDt != null) {
		              						%>
	  	                   						<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
  	                   						<%
	  	                   							}else{
	  	                   						%>
	  	                   						-
	  	                   					<%
	  	                   							}
	  	                   						%>
		               						</td>
	  	                					<!-- PROMOVENTES -->
	  	                					<td>
	  	                						<ul class="partes">
	  	                							<%
	  	                								if(audienciaProcessoDt.getProcessoDt() != null){
	  	                							%>
		  	                							<%
		  	                								List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
		  	                									  	                									  	                							if (listaPromoventes != null){
		  	                									  	                									  	                								for (int y = 0; y < listaPromoventes.size(); y++){
		  	                									  	                									  	                									ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
		  	                							%>
		  	                									<li><%=promovente.getNome()%></li>
		  	                								<%
		  	                									}
		  	                										  	                										  	                							}
		  	                										  	                									  	                							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
		  	                								%>
		  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
		  	                						<%
		  	                							}
		  	                						%>
	  	                						</ul>
	  	                					</td>
	  	                					<!-- FIM PROMOVENTES -->
						  	                			
	  	                					<!-- PROMOVIDOS -->
		  	            					<td>
		  	            						<ul class="partes">
		  	            							<%
		  	            								if(audienciaProcessoDt.getProcessoDt() != null){
		  	            							%>
		  	                							<%
		  	                								List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
		  	                									  	                									  	                							if (listaPromovidos != null){
		  	                									  	                									  	                								for (int y = 0; y < listaPromovidos.size(); y++){
		  	                									  	                									  	                									ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
		  	                							%>	
		  	                									<li><%=promovido.getNome()%> </li>
		  	                								<%
		  	                									}
		  	                										  	                										  	                							} 
		  	                										  	                										  	            							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
		  	                								%>
		  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
		  	                						<%
		  	                							}
		  	                						%>
	  	                						</ul>
	  	                					</td>
	  	                					<!-- FIM PROMOVIDOS -->
	  	                					<td>
	  	                						<%
	  	                							if(audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0){
	  	                						%>
	  	                							<%=audienciaProcessoDt.getNomeMPProcesso()%>
	  	                						<%
	  	                							} else {
	  	                						%>
	  	                							Não há
	  	                						<%
	  	                							}
	  	                						%>
	  	                					</td>
	  	                					<td><%=audienciaProcessoDt.getNomeResponsavel()%></td>	 
	  	                					<%
	 	  	                						if (Audienciadt.isVirtual()) {
	 	  	                					%>
									  	    		<td aling="center">[VIRTUAL - PJD]</td>
									  	    	<%
									  	    		} else {
									  	    	%> 	               						
			  	                   				<%
 	               									  	                   					if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))){
 	               									  	                   				%>
				  	                   			<%
				  	                   				if(ehCamaraSegundoGrau || ehGabineteSegundoGrau){
				  	                   			%>								  	        
										  	        <td aling="center">	   
											  	    <%
	   											  	    	if ((request.getAttribute("PodeMovimentar") != null) && (String.valueOf(request.getAttribute("PodeMovimentar")).trim().equalsIgnoreCase("S"))) {
	   											  	    %>										  	    	
												  	    	<%
										  	    													  	    		if (audienciaProcessoDt.isAnalistaPodeMovimentar()) {
										  	    													  	    	%>      	     	
												  	            <div id="opcoes" class="menuEspecial"> 
										                   			<ul> 
										                   				<li>Opções<ul>						                   				    			                   			
													                   	     <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=3" title="Julgamento Iniciado">
																				Julgamento Iniciado
																				</a>
																			 </li> 
																			 <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=4" title="Julgamento Adiado">
																				Julgamento Adiado
																				</a>
																			 </li>													 
													                   	     <li>
											                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Inserir Extrato da Ata de Julgamento">
												  	            				Inserir Extrato da Ata de Julgamento
												  	            				</a> 
											                   				 </li> 
											                   				 <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=5" title="Retirar de Pauta">
																				Retirar de Pauta
																				</a>
																			 </li> 
																			  <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=6" title="Desmarcar Sessão">
																				Desmarcar Sessão
																				</a>
																			 </li>                    			
										                   				</ul></li>
										                   			</ul>
										                   		</div>
									                   		<%
									                   			} else {
									                   		%>
									                   			Aguardando Acórdão / Ementa - Status: <%=audienciaProcessoDt.getAudienciaProcessoStatusAnalista()%>
									                   			<br />
									                   			<div id="opcoes" class="menuEspecial"> 
										                   			<ul> 
										                   				<li>Opções<ul>						                   				    			                   			
													                   	     <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=null" title="Corrigir Extrato da Ata de Julgamento">
																				Corrigir Extrato da Ata de Julgamento
																				</a>
																			 </li>
																			 <li> 
																				<a href="Movimentacao?RedirecionaOutraServentia=13&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retirar Extrato da Ata de Julgamento
																				</a>
																			 </li> 
										                   				</ul></li>
										                   			</ul>
										                   		</div>
									                   		<%
									                   			}
									                   		%>
								                   		<%
								                   			} else if ((((request.getAttribute("PodeAnalisar") != null) && (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) || ((request.getAttribute("PodePreAnalisar") != null) && (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
								                   								                   									                   			if(!audienciaProcessoDt.isAnalistaPodeMovimentar()){
								                   								                   									                   				if (audienciaProcessoDt.isResponsavelSessao()){
								                   		%>
									                   				<div id="opcoes" class="menuEspecial"> 
											                   			<ul> <li>Opções<ul>							                   					                  			
														                   	     <li>
												                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1" title="Pré-Analisar Acórdão/Ementa">
													  	            				Pré-Analisar Acórdão/Ementa
													  	            				</a> 
												                   				 </li> 
												                   				 <%
 												                   				 	if((request.getAttribute("PodeAnalisar") != null) && (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))){
 												                   				 %> 
															                   	     <li>
													                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2" title="Inserir Acórdão/Ementa">
														  	            				Inserir Acórdão/Ementa
														  	            				</a> 
													                   				 </li>
												                   				 <%
												                   				 	}
												                   				 %>                   			
											                   				</ul></li>
											                   			</ul>
											                   		</div>
											                   		<%
											                   			} else {
											                   		%>
									                   					Aguardando Acórdão / Ementa - Status: <%=audienciaProcessoDt.getAudienciaProcessoStatusAnalista()%>
									                   				<%
									                   					}
									                   				%>
									                   			<%
									                   				} else {
									                   			%>
									                   				Aguardando Extrato da Ata de Julgamento
									                   			<%
									                   				}
									                   			%>								                   			 											                   	
									                   		<%
								                   			 											                   										                   			}
								                   			 											                   										                   		%>								  	            
											  	      	</td>
											  	      <%
											  	      	}else{
											  	      %>
											  	    	<td>
			                   								<%
			                   									if(ehJuizTurma){
			                   								%>

										                   			<div id="opcoes" class="menuEspecial"> 
											                   			<ul> <li>Opções<ul>	
															                   	     <li>
													                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1&SomentePreAnalisadas=N" title="Pré-Analisar">
														  	            					Pré-Analisar
														  	            				</a> 
													                   				 </li> 
															                   	     <li>
														                   				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
													  	                   					Movimentar
													  	                   				</a>
													                   				 </li>                  			
											                   				</ul></li>
											                   			</ul>
											                   		</div>

						                   							<%
						                   								} else{
						                   											                   											                   								
						                   											                   										                   											if(ehTurmaJulgadora && !((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isAnalistaJudiciario()){
						                   							%>	
						                   								
													                   			<div id="opcoes" class="menuEspecial"> 
														                   			<ul> <li>Opções<ul>	
																		                   	     <li>
																                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1" title="Pré-Analisar">
																	  	            					Pré-Analisar
																	  	            				</a> 
																                   				 </li> 
														                   				</ul></li>
														                   			</ul>
														                   		</div>
					  	                   			
					                   								<%
					  	                   								                   									} else{
					  	                   								                   								%>	
																            	<div id="opcoes" class="menuEspecial"> 
														                   			<ul> <li>Opções<ul>	 
																		                   	     <li>
																	                   				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
																  	                   					Movimentar
																  	                   				</a>	
																                   				 </li>                  			
														                   				</ul></li>
														                   			</ul>		
				                   												</div>
						                   									<%
						                   										}
						                   													                   													                   							
						                   													                   													                   							}
						                   									%>


		                   								</td>

	                   								<%
	                   									}
	                   								%>	
										  	        <td></td>										  	       
									  	     	<%
										  	       									  	     		} else {
										  	       									  	     	%>
										  	     	<td>
										  	        	<%=audienciaProcessoDt.getDataMovimentacao()%><br />
										  	        	<font size="1"><%=audienciaProcessoDt.getAudienciaProcessoStatus()%></font>
										  	        	<%
										  	        		if ((request.getAttribute("PodeMovimentar") != null) && (String.valueOf(request.getAttribute("PodeMovimentar")).trim().equalsIgnoreCase("S"))) {
										  	        	%>
										  	        		<br />
										  	        		<div id="opcoes" class="menuEspecial"> 
									                   			<ul> 
									                   				<li>Opções<ul>						                   				    			                   			
												                   	     <li> 
																			<%
 																				if(audienciaProcessoDt.exibeOpcaoRetornarSessaoJulgamento()) {
 																			%>
																			<a href="Movimentacao?RedirecionaOutraServentia=14&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																			Retornar para Sessão de Julgamento
																			</a>																				
																			<%
																																								} else {
																																							%>
																			<a href="Movimentacao?RedirecionaOutraServentia=12&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																			Retirar Acórdão/Ementa e Extrato da Ata de Julgamento
																			</a>
																			<%
																				}
																			%>	
																		 </li>
									                   				</ul></li>
									                   			</ul>
									                   		</div>
										  	        	<%
										  	        		}
										  	        	%>
										  	        </td>
						  	                		<td>
					  	                   				<input name="formLocalizarimgResponsaveis" type="image" src="./imagens/imgAssistente.png" onclick="AlterarValue('Id_AudienciaProcesso','<%=audienciaProcessoDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');" title="Visualizar Responsáveis pela Sessão"/>
					  	                   			</td>
					  	                		<%
					  	                			}
					  	                		%>
					  	                	<%
					  	                		}
					  	                	%>
				  	                	</tr>
				  						<%
				  							linha = !linha;
				  								  								  					} //Fim FOR AUDIENCIADT
				  						%>									 
					           	</tbody>
					       	</table>
					   	</div>
				   	<%
				   		} 					   	
				   			   				   	   //Verificar se foram encontrados processos na audiência
				   			   				   		List listaAudienciaProcessosEmMesaParaJulgamento = Audienciadt.getListaAudienciaProcessoDtEmMesaParaJulgamento();  
				   			   		  				if ((ehCamaraSegundoGrau || ehGabineteSegundoGrau) && listaAudienciaProcessosEmMesaParaJulgamento != null && listaAudienciaProcessosEmMesaParaJulgamento.size() > 0){
				   			   		  					quantidadeTotal += listaAudienciaProcessosEmMesaParaJulgamento.size();
				   	%>
										
						   	<div id="divTabela" class="divTabela">
						   	
						   		<table id="Tabela" class="Tabela">
						        	<thead>
										<tr class="TituloColuna1">
											<th colspan="8">Sessão de Julgamento: <%=Audienciadt.getDataAgendada()%> - Em Mesa Para Julgamento</th>
										</tr>
						            	<tr class="TituloColuna">
						            		<th class="colunaMinima"></th>
						                  	<th width="20%">Processo</th>
						                  	<th width="20%">Recorrente / Paciente</th>
				                  	   		<th width="20%">Recorrido</th>
				                  	    	<th width="12%">MP Responsável</th>
				                  	    	<th width="20%">Relator</th>
						                  	<th width="10%">Movimentação</th>
						                  	<th class="colunaMinima"></th>
						               	</tr>
						           	</thead>
						           	<tbody id="tabListaAudiencia"> 
										<%
 											linha = false;					  				
 										 													  					for(int i = 0 ; i < listaAudienciaProcessosEmMesaParaJulgamento.size();i++) {
 										 													  	  					audienciaProcessoDt = (AudienciaProcessoDt)listaAudienciaProcessosEmMesaParaJulgamento.get(i);
 										 													  	  					audienciaProcessoFisicoDt = null;
 										 														  	  				if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
 										 												  	                   					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
 										 												  	                   				}
 										%>
					  	  					<tr class="TabelaLinha<%=(linha?1:2)%>">
					  	  						<td><%=Funcoes.preencheZeros(i+1, 2)%></td>
						  	  					<td> 
		                   						<%
 		                   							if(audienciaProcessoDt.getProcessoDt() != null){
 		                   						%>
			               							<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
			           								<%=Funcoes.formataNumeroProcesso(audienciaProcessoDt.getProcessoDt().getProcessoNumero())%>
			              							<%
			              								if(audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0){
			              							%>
			              								&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
			              							<%
			              								}
			              							%>
			              							</a>
			              						<%
			              							} else if (audienciaProcessoFisicoDt != null) {
			              						%>
		  	                   						<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
	  	                   						<%
		  	                   							}else{
		  	                   						%>
		  	                   						-
		  	                   					<%
		  	                   							}
		  	                   						%>
			               						</td>
		  	                					<!-- PROMOVENTES -->
		  	                					<td>
		  	                						<ul class="partes">
		  	                							<%
		  	                								if(audienciaProcessoDt.getProcessoDt() != null){
		  	                							%>
			  	                							<%
			  	                								List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
			  	                								  	                								  	                							if (listaPromoventes != null){
			  	                								  	                								  	                								for (int y = 0; y < listaPromoventes.size(); y++){
			  	                								  	                								  	                									ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
			  	                							%>
			  	                									<li><%=promovente.getNome()%></li>
			  	                								<%
			  	                									}
			  	                									  	                							}
			  	                										  	                							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
			  	                								%>
			  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
			  	                						<%
			  	                							}
			  	                						%>
		  	                						</ul>
		  	                					</td>
		  	                					<!-- FIM PROMOVENTES -->
							  	                			
		  	                					<!-- PROMOVIDOS -->
			  	            					<td>
			  	            						<ul class="partes">
			  	            							<%
			  	            								if(audienciaProcessoDt.getProcessoDt() != null){
			  	            							%>
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
		  	                					<td>
		  	                						<%if(audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0){%>
		  	                							<%=audienciaProcessoDt.getNomeMPProcesso()%>
		  	                						<% } else { %>
		  	                							Não há
		  	                						<% } %>
		  	                					</td>
		  	                					<td><%=audienciaProcessoDt.getNomeResponsavel()%></td>	  
		  	                					<%if (Audienciadt.isVirtual()) {%>
									  	    		<td aling="center">[VIRTUAL - PJD]</td>
									  	    	<% } else { %> 	               						
				  	                   				<%
					  	                   			if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))){%>								  	        
										  	        <td aling="center">	   
											  	    <%if ((request.getAttribute("PodeMovimentar") != null) && (String.valueOf(request.getAttribute("PodeMovimentar")).trim().equalsIgnoreCase("S"))) { %>
											  	    	<%if (audienciaProcessoDt.isAnalistaPodeMovimentar()) { %>      	     	
											  	            <div id="opcoes" class="menuEspecial"> 
									                   			<ul> <li>Opções<ul>						                   				    			                   			
												                   	    <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=3" title="Julgamento Iniciado">
																			Julgamento Iniciado
																			</a>
																		 </li> 
																		 <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=4" title="Julgamento Adiado">
																			Julgamento Adiado
																			</a>
																		 </li>																		 
												                   	     <li>
										                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Inserir Extrato da Ata de Julgamento">
											  	            				Inserir Extrato da Ata de Julgamento
											  	            				</a> 
										                   				 </li>  
										                   				 <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=5" title="Retirar de Pauta">
																			Retirar de Pauta
																			</a>
																		 </li>                   			
																		 <li> 
																			<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=6" title="Desmarcar Sessão">
																			Desmarcar Sessão
																			</a>
																		 </li>
									                   				</ul></li>
									                   			</ul>
									                   		</div>
									                   		<% } else { %>
									                   			Aguardando Acórdão / Ementa - Status: <%=audienciaProcessoDt.getAudienciaProcessoStatusAnalista()%>
									                   			<br />
									                   			<div id="opcoes" class="menuEspecial"> 
										                   			<ul> 
										                   				<li>Opções<ul>						                   				    			                   			
													                   	     <li> 
																				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=null" title="Corrigir Extrato da Ata de Julgamento">
																				Corrigir Extrato da Ata de Julgamento
																				</a>
																			 </li>
																			 <li> 
																				<a href="Movimentacao?RedirecionaOutraServentia=13&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retirar Extrato da Ata de Julgamento
																				</a>
																			 </li> 
										                   				</ul></li>
										                   			</ul>
										                   		</div>
									                   		<%} %>
								                   		<%} else if ((((request.getAttribute("PodeAnalisar") != null) && (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) || ((request.getAttribute("PodePreAnalisar") != null) && (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
								                   			if(!audienciaProcessoDt.isAnalistaPodeMovimentar()){
								                   				if (audienciaProcessoDt.isResponsavelSessao()){%>
									                   			<div id="opcoes" class="menuEspecial"> 
										                   			<ul> <li>Opções<ul>							                   					                  			
													                   	     <li>
											                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1" title="Pré-Analisar Acórdão/Ementa">
												  	            				Pré-Analisar Acórdão/Ementa
												  	            				</a> 
											                   				 </li> 
											                   				 <%if((request.getAttribute("PodeAnalisar") != null) && (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))){%> 
														                   	     <li>
												                   				 	<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2" title="Inserir Acórdão/Ementa">
													  	            				Inserir Acórdão/Ementa
													  	            				</a> 
												                   				 </li>
											                   				 <% } %>                   			
										                   				</ul></li>
										                   			</ul>
										                   		</div>
										                   		<% } else { %>
									                   				Aguardando Acórdão / Ementa - Status: <%=audienciaProcessoDt.getAudienciaProcessoStatusAnalista()%>
									                   			<% } %>
								                   			<% } else { %>
								                   				Aguardando Extrato da Ata de Julgamento
								                   			<% } %>												                   	
								                   		<% } %>								  	            
										  	      	</td>
										  	        <td></td>
										  	     	<% } else { %>
											  	     	<td>
											  	        	<%=audienciaProcessoDt.getDataMovimentacao()%><br />
											  	        	<font size="1"><%=audienciaProcessoDt.getAudienciaProcessoStatus()%></font>
											  	        	<%if ((request.getAttribute("PodeMovimentar") != null) && (String.valueOf(request.getAttribute("PodeMovimentar")).trim().equalsIgnoreCase("S"))) { %>
											  	        		<br />
											  	        		<div id="opcoes" class="menuEspecial"> 
										                   			<ul> 
										                   				<li>Opções<ul>						                   				    			                   			
													                   	     <li> 
																				<%if(audienciaProcessoDt.exibeOpcaoRetornarSessaoJulgamento()) { %>
																				<a href="Movimentacao?RedirecionaOutraServentia=14&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retornar para Sessão de Julgamento
																				</a>																				
																				<% } else { %>
																				<a href="Movimentacao?RedirecionaOutraServentia=12&Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>" title="Retirar Acórdão/Ementa e Extrato da Ata de Julgamento">
																				Retirar Acórdão/Ementa e Extrato da Ata de Julgamento
																				</a>
																				<% } %>																				
																			 </li>
										                   				</ul></li>
										                   			</ul>
										                   		</div>
											  	        	<%}%>
											  	        </td>
							  	                		<td>
						  	                   				<input name="formLocalizarimgResponsaveis" type="image" src="./imagens/imgAssistente.png" onclick="AlterarValue('Id_AudienciaProcesso','<%=audienciaProcessoDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');" title="Visualizar Responsáveis pela Sessão"/>
						  	                   			</td>
						  	                		<% } %>
						  	                	<% } %>
					  	                	</tr>
					  						<%linha = !linha;
					  					} //Fim FOR AUDIENCIADT
										%>									 
						           	</tbody>
						       	</table>
						   	</div>
					   	<%} %>
				   	
				   	<%if (quantidadeTotal == 0){ %>				   	
					   	<div id="divTabela" class="divTabela">
					   	
					   		<table id="Tabela" class="Tabela">
					        	<thead>
									<tr class="TituloColuna1">
										<th colspan="7">Sessão de Julgamento: <%=Audienciadt.getDataAgendada()%> - Pauta do dia</th>
									</tr>								
					            	<tr class="TituloColuna">
					            		<th class="colunaMinima"></th>
					            		<th></th>
					                  	<th width="8%">Processo</th>
					                  	<th width="20%">Recorrente / Paciente</th>
				                  	    <th width="20%">Recorrido</th>
				                  	    <th width="20%">MP Responsável</th>
				                  	    <th width="20%">Relator</th>
					                  	<th width="10%">Movimentação</th>
					               	</tr>				               	
					           	</thead>	           	
					           	
					           	<tbody id="tabListaAudiencia">
					           		<tr class="TabelaLinha<%=(linha?1:2)%>">					           	
					           			<td colspan="7"><em>Nenhum Processo nesta Sessão.</em></td>
					           		</tr>					           	 
					           	</tbody>				           	
					       	</table>
					   	</div>
				   	<% } %>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
			
		</div>
	</body>
</html>