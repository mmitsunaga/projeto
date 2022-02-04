<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%><html>
	<head>
		<title>Busca de Processos para Trocar MP Responsável de Processo e Intimações</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="BuscaProcesso" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="id_UsuarioServentiaAtual" name="id_UsuarioServentiaAtual" type="hidden" value="" />
				<input id="RedirecionaOutraServentia" name="RedirecionaOutraServentia" type="hidden" value="" />				
				<input id="trocaResponsavelProcesso" name="trocaResponsavelProcesso" type="hidden" value="" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos para Trocar MP Responsável de Processo e Intimações</legend>
                       	
					    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					    </label><br>
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=buscaProcessoDt.getId_Serventia()%>">  
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=buscaProcessoDt.getServentia()%>"/><br />
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
						</div>
					</fieldset>		
				</div>
				
				<div id="divLocalizar" class="divLocalizar"> 
					<input type="hidden" id="Id_Processo" name="Id_Processo">
					
					<div id="divTabela" class="divTabela"> 
						<div align="left">
							<%if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
							<input name="imgMultipla" type="submit" value="Trocar Responsável Lote" onclick="AlterarAction('Formulario','PendenciaResponsavel');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('id_UsuarioServentiaAtual','<%=buscaProcessoDt.getId_UsuarioServentia()%>');AlterarValue('RedirecionaOutraServentia','6');AlterarValue('trocaResponsavelProcesso','true');"/>
							<% } %>
						</div>
						
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				            		<td class="colunaMinina">
				            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
							    				title="Alterar os estados de todos os itens da lista" />
							    	</td>
							    	<td width="10%">Processo</td>
				                  	<td width="20%">Movimenta&ccedil;&atilde;o</td>
				                  	<td width="30%">Responsável</td>
				                  	<td width="20%">Data Início</td>
				                  	<td width="20%">Data Limite</td>
				                  	<td class="colunaMinima">Trocar</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								ListaPendenciaDt listaIntimacoes = (ListaPendenciaDt)request.getAttribute("ListaIntimacoes");
								ListaPendenciaDt listaIntimacoesSubstitutoProcessual = (ListaPendenciaDt)request.getAttribute("ListaIntimacoesSubstitutoProcessual");
								ListaPendenciaDt listaIntimacoesLidas = (ListaPendenciaDt)request.getAttribute("ListaIntimacoesLidas");
								ListaPendenciaDt listaIntimacoesLidasSubstitutoProcessual = (ListaPendenciaDt)request.getAttribute("ListaIntimacoesLidasSubstitutoProcessual");
								
								int contPendenciasProcessos = 0;
								boolean boLinha=false;
								
								if (listaIntimacoes != null && listaIntimacoes.getQuantidadePendencias() > 0) {
							%>
									<tr class="Tabela"> 
										<td colspan="7" align="center" class="TituloColuna">
											<%=listaIntimacoes.getTitulo()%>
										</td>					                  
				               		</tr>								
									<%
									if (listaIntimacoes.getQuantidadePendencias() > 0){
										Iterator itPendenciasAndamento = listaIntimacoes.getPendenciasAndamento().iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
											<tr class="TabelaLinha<%=(boLinha?1:2)%>">
												<td align="center">
													<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=pendenciaDt.getId()%>-<%=pendenciaDt.getId_Processo()%>-<%=pendenciaDt.getHash()%>">
												</td>									
												<td width="130" align="center">
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
												<td width="150">
												<%if (pendenciaDt.getResponsavel() != null){%>
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null
														&& pendenciaDt.getResponsavel().getServentiaCargo().length()>0) {%>
													<%if (pendenciaDt.getResponsavel() != null && pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getServentiaCargo()%><%}%> |
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%>
													<% } else if (pendenciaDt.getResponsavel().getId_UsuarioResponsavel() != null
														&& pendenciaDt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
													MP-GO |
													<%=pendenciaDt.getResponsavel().getNomeUsuarioResponsavel()%>
												<%} %>
												<%} %>
												</td>
												<td width="150"><%=pendenciaDt.getDataInicio()%></td>
												<td width="150"><%=pendenciaDt.getDataLimite()%></td>
												<td align="center">
							                   		<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;processo=<%=pendenciaDt.getId_Processo()%>&amp;trocaResponsavelProcesso=true">
														<img src="imagens/22x22/btn_encaminhar.png"  title="Efetuar troca de responsável" />
													</a>     
							                   	</td>
											</tr>							
										
										<%
											contPendenciasProcessos++;
										}
									}
								}
							%>
							
							<%
								boLinha=false;
								
								if (listaIntimacoesSubstitutoProcessual != null && listaIntimacoesSubstitutoProcessual.getQuantidadePendencias() > 0) {
							%>
									<tr class="Tabela"> 
										<td colspan="7" align="center" class="TituloColuna">
											<%=listaIntimacoesSubstitutoProcessual.getTitulo()%>
										</td>					                  
				               		</tr>								
									<%
									if (listaIntimacoesSubstitutoProcessual.getQuantidadePendencias() > 0){
										Iterator itPendenciasAndamento = listaIntimacoesSubstitutoProcessual.getPendenciasAndamento().iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
											<tr class="TabelaLinha<%=(boLinha?1:2)%>">
												<td align="center">
													<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=pendenciaDt.getId()%>-<%=pendenciaDt.getId_Processo()%>-<%=pendenciaDt.getHash()%>">
												</td>									
												<td width="130" align="center">
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
												<td width="150">
												<%if (pendenciaDt.getResponsavel() != null){%>
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null
														&& pendenciaDt.getResponsavel().getServentiaCargo().length()>0) {%>
													<%if (pendenciaDt.getResponsavel() != null && pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getServentiaCargo()%><%}%> |
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%>
													<% } else if (pendenciaDt.getResponsavel().getId_UsuarioResponsavel() != null
														&& pendenciaDt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
													MP-GO |
													<%=pendenciaDt.getResponsavel().getNomeUsuarioResponsavel()%>
												<%} %>
												<%} %>
												</td>
												<td width="150"><%=pendenciaDt.getDataInicio()%></td>
												<td width="150"><%=pendenciaDt.getDataLimite()%></td>
												<td align="center">
							                   		<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;processo=<%=pendenciaDt.getId_Processo()%>&amp;trocaResponsavelProcesso=true">
														<img src="imagens/22x22/btn_encaminhar.png"  title="Efetuar troca de responsável" />
													</a>     
							                   	</td>
											</tr>							
										
										<%
											contPendenciasProcessos++;
										}
									}
								}
							%>
							
							<%
								boLinha=false;
								
								if (listaIntimacoesLidas != null && listaIntimacoesLidas.getQuantidadePendencias() > 0) {
							%>
									<tr class="Tabela"> 
										<td colspan="7" align="center" class="TituloColuna">
											<%=listaIntimacoesLidas.getTitulo()%>
										</td>					                  
				               		</tr>								
									<%
									if (listaIntimacoesLidas.getQuantidadePendencias() > 0){
										Iterator itPendenciasAndamento = listaIntimacoesLidas.getPendenciasAndamento().iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
											<tr class="TabelaLinha<%=(boLinha?1:2)%>">
												<td align="center">
													<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=pendenciaDt.getId()%>-<%=pendenciaDt.getId_Processo()%>-<%=pendenciaDt.getHash()%>">
												</td>									
												<td width="130" align="center">
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
												<td width="150">
												<%if (pendenciaDt.getResponsavel() != null){%>
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null
														&& pendenciaDt.getResponsavel().getServentiaCargo().length()>0) {%>
													<%if (pendenciaDt.getResponsavel() != null && pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getServentiaCargo()%><%}%> |
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%>
													<% } else if (pendenciaDt.getResponsavel().getId_UsuarioResponsavel() != null
														&& pendenciaDt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
													MP-GO |
													<%=pendenciaDt.getResponsavel().getNomeUsuarioResponsavel()%>
												<%} %>
												<%} %>
												</td>
												<td width="150"><%=pendenciaDt.getDataInicio()%></td>
												<td width="150"><%=pendenciaDt.getDataLimite()%></td>
												<td align="center">
							                   		<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;processo=<%=pendenciaDt.getId_Processo()%>&amp;trocaResponsavelProcesso=true">
														<img src="imagens/22x22/btn_encaminhar.png"  title="Efetuar troca de responsável" />
													</a>     
							                   	</td>
											</tr>							
										
										<%
											contPendenciasProcessos++;
										}
									}
								}
							%>
							
							<%
								boLinha=false;
								
								if (listaIntimacoesLidasSubstitutoProcessual != null && listaIntimacoesLidasSubstitutoProcessual.getQuantidadePendencias() > 0) {
							%>
									<tr class="Tabela"> 
										<td colspan="7" align="center" class="TituloColuna">
											<%=listaIntimacoesLidasSubstitutoProcessual.getTitulo()%>
										</td>					                  
				               		</tr>								
									<%
									if (listaIntimacoesLidasSubstitutoProcessual.getQuantidadePendencias() > 0){
										Iterator itPendenciasAndamento = listaIntimacoesLidasSubstitutoProcessual.getPendenciasAndamento().iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
											<tr class="TabelaLinha<%=(boLinha?1:2)%>">
												<td align="center">
													<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=pendenciaDt.getId()%>-<%=pendenciaDt.getId_Processo()%>-<%=pendenciaDt.getHash()%>">
												</td>									
												<td width="130" align="center">
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
												<td width="150">
												<%if (pendenciaDt.getResponsavel() != null){%>
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null
														&& pendenciaDt.getResponsavel().getServentiaCargo().length()>0) {%>
													<%if (pendenciaDt.getResponsavel() != null && pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getServentiaCargo()%><%}%> |
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaDt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%>
													<% } else if (pendenciaDt.getResponsavel().getId_UsuarioResponsavel() != null
														&& pendenciaDt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
													MP-GO |
													<%=pendenciaDt.getResponsavel().getNomeUsuarioResponsavel()%>
												<%} %>
												<%} %>
												</td>
												<td width="150"><%=pendenciaDt.getDataInicio()%></td>
												<td width="150"><%=pendenciaDt.getDataLimite()%></td>
												<td align="center">
							                   		<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;processo=<%=pendenciaDt.getId_Processo()%>&amp;trocaResponsavelProcesso=true">
														<img src="imagens/22x22/btn_encaminhar.png"  title="Efetuar troca de responsável" />
													</a>     
							                   	</td>
											</tr>							
										
										<%
											contPendenciasProcessos++;
										}
									}
								}
							%>
				           	</tbody>
				           	<tfoot>
								<tr>
									<td colspan="7">Quantidade de processos: <span id="qtd"><%=contPendenciasProcessos%></span></td>
								</tr>
							</tfoot>
				       	</table>
   					</div> 
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>