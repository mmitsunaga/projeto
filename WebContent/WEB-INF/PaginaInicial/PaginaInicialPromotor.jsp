<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaDadosServentiaDt"%>

<html>
	<head>
		<title>Processo Judicial</title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
			
		</style>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt"); %>
			
			<%if(usuario != null) { %>
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
					<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial</span><br>
				
				<span class="bold"><%= usuario.getServentia() + " - " + usuario.getEstadoRepresentacao()%> </span></p>
			<%} %>
		
			<%
			
				List dadosServentia = paginaInicialDt.getDadosServentia();
					if (dadosServentia != null && dadosServentia.size() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Processos</legend>
				
				<table class="Tabela">
					<thead>
						<tr>
							<th width="50%">Tipo</th>
							<th>Qtde</th>
						</tr>
					</thead>
					<%
						for(int i=0; i < dadosServentia.size();i++){
							ListaDadosServentiaDt dadosServentiaDt = (ListaDadosServentiaDt) dadosServentia.get(i);
					%>
					<tbody>
						<tr>						
							<%if (dadosServentiaDt.isSigiloso()){ %>
							<td><b><font color="red"><%=dadosServentiaDt.getDescricao()%></font></b></td>
							<td align="center">							
								<a href="<%=dadosServentiaDt.getLink()%>"><b><font color="red"><%=dadosServentiaDt.getQuantidade()%></font></b></a>
							</td>
							<%}else{%>
								<td><%=dadosServentiaDt.getDescricao()%></td>
								<td align="center">
								<a href="<%=dadosServentiaDt.getLink()%>"><%=dadosServentiaDt.getQuantidade()%></a>
							</td>
							<%}%>
						</tr>
					</tbody>
					<% } %>
				</table>
			</fieldset>			
			<% } %>
			
			<%
				List pendenciasIntimacoesAguardandoParecer = paginaInicialDt.getPendenciasIntimacoesAguardandoParecer();
				if (pendenciasIntimacoesAguardandoParecer != null && pendenciasIntimacoesAguardandoParecer.size() > 0){
					Iterator itPendenciasAguardandoParecer = pendenciasIntimacoesAguardandoParecer.iterator();
					while (itPendenciasAguardandoParecer.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendenciasAguardandoParecer.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
							if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
						%>
							<fieldset class="fieldEdicaoEscuro">
								<legend><%=listaPendencia.getTitulo()%> </legend>
								<table class="Tabela">
									<thead>
										<tr>
											<th class="colunaMinima">Pri.</th>
											<th class="colunaMinima lista_id">N&uacute;m.</th>
											<th>Processo</th>
											<th>Movimenta&ccedil;&atilde;o</th>
											<th>Data Leitura</th>
											<th>Data Limite</th>
											<th>Marcar</th>
											<th class="colunaMinima">Descartar</th>
										</tr>
									</thead>
									<tbody>
									<%
									Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();
											String stUrgente = pendenciaDt.getNumeroImagemPrioridade();
											String mensagemUrgente = pendenciaDt.getProcessoPrioridadeCodigoTexto();
											%>
		
											<tr >
												<td>
													<%	if (stUrgente.length()==1){ %>		 
		                  								<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
		                  							<% } %>
		                  						</td>
												<td><%=pendenciaDt.getId()%></td>
												<td>
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td><%=pendenciaDt.getMovimentacao()%><%=stUrgente+"kkk"%></td>
												<td class="lista_data"><%=pendenciaDt.getDataFim()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
												<td class="colunaMinima">
													<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>" 
														title="Marcar Aguardando Parecer">
														<img src="imagens/22x22/btn_movimentar.png"/>
													</a>
												</td>
												<td align="center">
													<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga6%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_excluir_2.png" alt="Retirar da Página Inicial" title="Retirar da Página Inicial" />
													</a> 
												</td>
											</tr>
									<%}%>
									</tbody>
									<tfoot>
										<tr>
											<td colspan="7">Quantidade: <%=pendenciasAndamento.size()%></td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
							<%	} 
					 } 
				   }
				}%>
				
				<%
				List pendenciasIntimacoesLeituraAutomaticaAguardandoParecer = paginaInicialDt.getPendenciasIntimacoesLeituraAutomaticaAguardandoParecer();
				if (pendenciasIntimacoesLeituraAutomaticaAguardandoParecer != null && pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.size() > 0){
					Iterator itPendenciasAguardandoParecer = pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.iterator();
					while (itPendenciasAguardandoParecer.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendenciasAguardandoParecer.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
							if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
						%>
							<fieldset class="fieldEdicaoEscuro">
								<legend><%=listaPendencia.getTitulo()%></legend>
								<table class="Tabela">
									<thead>
										<tr>
											<th class="colunaMinima ">Pri.</th>
											<th class="colunaMinima lista_id">N&uacute;m.</th>
											<th>Processo</th>
											<th>Movimenta&ccedil;&atilde;o</th>
											<th>Data Leitura</th>
											<th>Data Limite</th>
											<th>Marcar</th>
											<th class="colunaMinima">Descartar</th>
										</tr>
									</thead>
									<tbody>
									<%
									Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();
											String stUrgente = pendenciaDt.getNumeroImagemPrioridade();
											String mensagemUrgente = pendenciaDt.getProcessoPrioridadeCodigoTexto();
											%>		
											<tr >
												<td><%	if (stUrgente.length()==1){ %>		 
		                   								<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
		                   							<% } %>
		                   						 </td>
												<td><%=pendenciaDt.getId()%></td>
												<td>
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td><%=pendenciaDt.getMovimentacao()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataFim()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
												<td class="colunaMinima">
													<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>" 
														title="Marcar Aguardando Parecer">
														<img src="imagens/22x22/btn_movimentar.png"/>
													</a>
												</td>
												<td align="center">
													<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga6%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_excluir_2.png" alt="Retirar da Página Inicial" title="Retirar da Página Inicial" />
													</a> 
												</td>
											</tr>
									<%}%>
									</tbody>
									<tfoot>
										<tr>
											<td colspan="9">Quantidade: <%=pendenciasAndamento.size()%></td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						<%	} 
					 	} 
				  	}
				} %>
				
			<!-- ****************************** -->
			
			
			 <!--  -->
				<%
					List pendenciasUsuarioServentiaLidasAutomaticamente = paginaInicialDt.getPendenciasUsuarioServentiaLidasAutomaticamente();
					if (pendenciasUsuarioServentiaLidasAutomaticamente != null && pendenciasUsuarioServentiaLidasAutomaticamente.size() > 0){
						Iterator itPendencias = pendenciasUsuarioServentiaLidasAutomaticamente.iterator();
						while (itPendencias.hasNext()){
							ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
							
							if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO) || 
								(Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.CARTA_CITACAO)) {
							
							List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
							
							if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
					%>
						<fieldset class="fieldEdicaoEscuro">
							<legend><%=listaPendencia.getTitulo()%></legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima ">Pri.</th>
										<th class="colunaMinima lista_id">N&uacute;m.</th>
										<th>Processo</th>
										<th>Movimenta&ccedil;&atilde;o</th>
										<th>Data Leitura</th>
										<th>Data Limite</th>
										<th>Marcar</th>
										<th class="colunaMinima">Descartar</th>
									</tr>
								</thead>
								<tbody>
								<%
								Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
									while (itPendenciasAndamento.hasNext()){
										PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();
										String stUrgente = pendenciaDt.getNumeroImagemPrioridade();
										String mensagemUrgente = pendenciaDt.getProcessoPrioridadeCodigoTexto();
											%>
										<tr >
											<td>
												<%	if (stUrgente.length()==1){ %>		 
	                  								<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
	                  							<% } %>
	                  						</td>
											<td><%=pendenciaDt.getId()%></td>
											<td>
												<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
											</td>
											<td><%=pendenciaDt.getMovimentacao()%></td>
											<td class="lista_data"><%=pendenciaDt.getDataFim()%></td>
											<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
											<td class="colunaMinima">
												<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>" 
													title="Marcar Aguardando Parecer">
													<img src="imagens/22x22/btn_movimentar.png"/>
												</a>
											</td>
											<td align="center">
												<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga6%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
													<img src="imagens/22x22/btn_excluir_2.png" alt="Retirar da Página Inicial" title="Retirar da Página Inicial" />
												</a> 
											</td>
										</tr>
								<%}%>
								</tbody>
								<tfoot>
									<tr>
										<td colspan="9">Quantidade: <%=pendenciasAndamento.size()%></td>
									</tr>
								</tfoot>
							</table>
						</fieldset>
						<%	}
							} 
						}//fim while
					} %>
			
			   <!--  -->
			
			<%
				List pendenciasUsuarioServentia = paginaInicialDt.getPendenciasUsuarioServentia();
				if (pendenciasUsuarioServentia != null && pendenciasUsuarioServentia.size() > 0){
					Iterator itPendencias = pendenciasUsuarioServentia.iterator();
					while (itPendencias.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO) || 
							(Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.CARTA_CITACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
						if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
				%>
					<fieldset class="fieldEdicaoEscuro">
						<legend><%=listaPendencia.getTitulo()%></legend>
						<table class="Tabela">
							<thead>
								<tr>
									<th class="colunaMinima" >Pri.</th>
									<th class="colunaMinima lista_id">N&uacute;m.</th>
									<th>Processo</th>
									<th>Parte</th>
									<th>Movimenta&ccedil;&atilde;o</th>
									<th>Data Início</th>
									<th>Data Limite</th>
									<th colspan="3" class="colunaMinima">Op&ccedil;&otilde;es</th>
								</tr>
							</thead>
							<tbody>
							<%
							Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
								while (itPendenciasAndamento.hasNext()){
									PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();
									String stUrgente = pendenciaDt.getNumeroImagemPrioridade();
									String mensagemUrgente = pendenciaDt.getProcessoPrioridadeCodigoTexto();
									%>

									<tr <%=(pendenciaDt.isAdvogadoPrincipal()?"class=\"linhaDestaqueIntimacao\" title=\"Advogado Principal\"":"")%> ">
										<td>
											<%	if (stUrgente.length()==1){ %>		 
                  								<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
                  							<% } %>
                  						</td>
										<td><%=pendenciaDt.getId()%></td>
										<td>
											<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
										</td>
										<td><%=pendenciaDt.getNomeParte()%></td>
										<td><%=pendenciaDt.getMovimentacao()%></td>
										<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
										<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
										<%if (pendenciaDt.isPendenciaDeProcesso()){%>
												<td class="colunaMinima">
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;ultimaOperacaoPendencia=MarcarLido&amp;NovaPesquisa=true">
														<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Efetuar Leitura" />
													</a>
												</td>
												<td class="colunaMinima">
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;ultimaOperacaoPendencia=MarcarLidoAguardandoParecer&amp;NovaPesquisa=true">
														<img src="imagens/22x22/btn_movimentar.png" alt="Solucionar" title="Efetuar Leitura / Marcar Aguardando Parecer" />
													</a>
												</td>
												<%} else { %>															
												<td class="colunaMinima">
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">
														<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
													</a>
												</td>
												<%}%>
												<td>
													<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
													</a> 
												</td>
									</tr>
							<%}%>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="10">Quantidade: <%=pendenciasAndamento.size()%></td>
								</tr>
							</tfoot>
						</table>
					</fieldset>
					<%	}
						} 
					}//fim while
				} %>
			
			<!-- ****************************** -->
			
				<%
				List pendencias = paginaInicialDt.getPendenciasServentiaCargo();
				int contOutrasPendencias = 0;
				if (pendencias != null && pendencias.size() > 0){
					Iterator itPendencias = pendencias.iterator();
					while (itPendencias.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
							if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
						%>
							<fieldset class="fieldEdicaoEscuro">
								<legend><%=listaPendencia.getTitulo()%></legend>
								<table class="Tabela">
									<thead>
										<tr>
											<th class="colunaMinima">Pri.</th>
											<th class="colunaMinima lista_id">N&uacute;m.</th>
											<th>Processo</th>
											<th>Movimenta&ccedil;&atilde;o</th>
											<th>Data Início</th>
											<th>Data Limite</th>
											<th colspan="3" class="colunaMinima">Op&ccedil;&otilde;es</th>
										</tr>
									</thead>
									<tbody>
									<%
									Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();
											String stUrgente = pendenciaDt.getNumeroImagemPrioridade();
											String mensagemUrgente = pendenciaDt.getProcessoPrioridadeCodigoTexto();%>
		
											<tr >
												<td>
													<%	if (stUrgente.length()==1){ %>		 
		                  								<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
		                  							<% } %>
		                  						</td>
												<td><%=pendenciaDt.getId()%></td>
												<td>
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td><%=pendenciaDt.getMovimentacao()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
												<%if (pendenciaDt.isPendenciaDeProcesso()){%>
												<td class="colunaMinima">
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;ultimaOperacaoPendencia=MarcarLido&amp;NovaPesquisa=true">
														<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Efetuar Leitura" />
													</a>
												</td>
												<td class="colunaMinima">
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;ultimaOperacaoPendencia=MarcarLidoAguardandoParecer&amp;NovaPesquisa=true">
														<img src="imagens/22x22/btn_movimentar.png" alt="Solucionar" title="Efetuar Leitura / Marcar Aguardando Parecer" />
													</a>
												</td>
												<%} else { %>															
												<td class="colunaMinima">
													<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">
														<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
													</a>
												</td>
												<%}%>
												<td>
													<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
													</a> 
												</td>
											</tr>
									<%}%>
									
									
									</tbody>
									<tfoot>
										<tr>
											<td colspan="9">Quantidade: <%=pendenciasAndamento.size()%></td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
							<%	} else {%>
							<em>N&atilde;o h&aacute; pend&ecirc;ncias</em>
							<%}
							//Tratamento de outras pendências
							} else { 			
								if (contOutrasPendencias == 0){ %>
								<fieldset class="fieldEdicaoEscuro">
								 	<legend>Outras Pend&ecirc;ncias Promotor</legend>
									<table class="Tabela">
										<thead>
											<tr>
												<th> Tipo Pendência </th>
												<th width="12%"> Não analisadas </th>
												<th> Pré-analisadas </th>
												<th> Reservadas </th>
											</tr>
										</thead>
										<tbody>
								<% } %>
										<tr>
											<td><%=listaPendencia.getTitulo()%></td>
											<td class="colunaMinima">
												<a href="<%=(listaPendencia.getQtdeNaoAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Editar + "&amp;operacao=PegarPorTipoServentiaCargo&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
													<%=listaPendencia.getQtdeNaoAnalisadas()%>
												</a>
											</td>
											<td class="colunaMinima">
												<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=PreAnalisadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=3":"#")%>" >
													<%=listaPendencia.getQtdePreAnalisadas()%>
												</a>
											</td>
											<td class="colunaMinima">
												<a href="<%=(listaPendencia.getQtdeReservadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=Reservadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=3":"#")%>" >
													<%=listaPendencia.getQtdeReservadas()%>
												</a>									
											</td>
										</tr>
									</table>
								</fieldset>
						<% contOutrasPendencias++;
						} // fim else	
					} //fim while
					//Verifica se deve fechar fieldset de Outras Pendências
					if (contOutrasPendencias > 0){ %> 
						</tbody></table></fieldset>
				<%	}
				} %>
				</fieldset>
				
				<fieldset class="fieldEdicaoEscuro">
					<legend>Processos Aguardando Parecer</legend>
					<%
					pendencias = paginaInicialDt.getPendenciasAnalise();
					if ( (pendencias != null && pendencias.size() > 0) || paginaInicialDt.getQtdePreAnalisesMultiplasPendencias() > 0){
					%>
						<table class="Tabela">
							<thead>
								<tr>
									<th> Tipo Pendência </th>
									<th width="18%"> Aguardando assinatura </th>
									<th width="15%"> Não analisadas </th>
									<th> Pré-analisadas </th>
								</tr>
							</thead>
							<tbody>
							<%
								int contNaoAssinadas = 0;
								int contNaoAnalisadas = 0;
								int contPreAnalisadas = 0;
								Iterator itPendencias = pendencias.iterator();
								while (itPendencias.hasNext()){
									ListaPendenciaDt listaPendencia = (ListaPendenciaDt) itPendencias.next();
									contNaoAssinadas += listaPendencia.getQtdePreAnalisadasPendentesAssinatura();
									contNaoAnalisadas += listaPendencia.getQtdeNaoAnalisadas();
									contPreAnalisadas += listaPendencia.getQtdePreAnalisadas();
								%>
								<tr>
									<td><%=listaPendencia.getTitulo()%></td>
									<%if (usuario.getId_UsuarioServentiaChefe() == null || usuario.getId_UsuarioServentiaChefe().equals("")) {%>
									<td class="colunaMinima">
										<a href="<%=(listaPendencia.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
											<%=listaPendencia.getQtdePreAnalisadasPendentesAssinatura()%>
										</a>
									</td>
									<td class="colunaMinima">
										<a href="<%=(listaPendencia.getQtdeNaoAnalisadas()>0?"AnalisarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
											<%=listaPendencia.getQtdeNaoAnalisadas()%>
										</a>
									</td>
									<% } %>
									<td class="colunaMinima">
										<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"PreAnalisarPendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
											<%=listaPendencia.getQtdePreAnalisadas()%>
										</a>
									</td>
								</tr>
											
								<% 	} if (pendencias != null && pendencias.size() > 0) {%>
								<tr>
									<td align="right">Total</td>
									<td class="colunaMinima"><strong><%=contNaoAssinadas%></strong></td>
									<td class="colunaMinima"><strong><%=contNaoAnalisadas%></strong></td>
									<td class="colunaMinima"><strong><%=contPreAnalisadas%></strong></td>
								</tr>
								<%		
								 }
								 if (paginaInicialDt.getQtdePreAnalisesMultiplasPendencias() > 0) { %>
									<tr>
										<td> Pré-Análises Múltiplas </td>
										<td class="colunaMinima" colspan="2">
											<a href="<%=(paginaInicialDt.getQtdePreAnalisesMultiplasPendencias()>0?"PreAnalisarPendencia?PaginaAtual=" + Configuracao.Curinga7:"#")%>" >
											<%=paginaInicialDt.getQtdePreAnalisesMultiplasPendencias() %>
											</a>
										</td>
									</tr>
								<% } %>
							</tbody>
						</table>			
					<%} else { %>
						<em>N&atilde;o h&aacute; Processos Aguardando Parecer</em>
					<%} %>
				</fieldset>
				
				<%if (paginaInicialDt.getQtdePendenciaLiberaAcesso() > 0){ %>
					<fieldset class="fieldEdicaoEscuro">
						<legend>Pend&ecirc;ncias Liberação de Acesso</legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th> Tipo Pendência </th>
										<th> Quantidade </th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Liberação de acessos</td>
										<td class="colunaMinima">
											<a href="<%=(paginaInicialDt.getQtdePendenciaLiberaAcesso()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=7":"#")%>" >
											<font color="green"><%=paginaInicialDt.getQtdePendenciaLiberaAcesso() %></font>
											</a>
										</td>
									</tr>
							</table>
					</fieldset>	
				<%} %>
			
				<%if (paginaInicialDt.getQtdePendenciaInformativa() > 0){ %>
					<fieldset class="fieldEdicaoEscuro">
						<legend>Informativo(s) do Processo Judicial Digital</legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th> Tipo Pendência </th>
										<th> Quantidade </th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Informativo(s)</td>
										<td class="colunaMinima">
											<a href="<%=(paginaInicialDt.getQtdePendenciaInformativa()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=12":"#")%>" >
											<font color="green"><%=paginaInicialDt.getQtdePendenciaInformativa() %></font>
											</a>
										</td>
									</tr>
							</table>
					</fieldset>	
				<%} %>
				
				<%
					List pendenciasServentia = paginaInicialDt.getPendenciasServentia();
					if (pendenciasServentia != null && pendenciasServentia.size() > 0){ %>
					<fieldset class="fieldEdicaoEscuro">
						<legend>Pend&ecirc;ncias Serventia</legend>
						
							<table class="Tabela">
								<thead>
									<tr>
										<th> Tipo Pendência </th>
										<th width="12%"> Não analisadas </th>
										<th> Pré-analisadas </th>
										<th> Reservadas </th>
									</tr>
								</thead>
								<tbody>
								<%
									Iterator itPendenciasServentia = pendenciasServentia.iterator();
									while (itPendenciasServentia.hasNext()){
										ListaPendenciaDt listaPendenciaServentia = (ListaPendenciaDt)itPendenciasServentia.next();
								%>
									<tr>
										<td><%=listaPendenciaServentia.getTitulo()%></td>
										<td class="colunaMinima">
											<a href="<%=(listaPendenciaServentia.getQtdeNaoAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Editar + "&amp;operacao=PegarPorTipoServentia&amp;tipo=" + listaPendenciaServentia.getIdTipo():"#")%>" >
											<%=listaPendenciaServentia.getQtdeNaoAnalisadas()%>
											</a>
										</td>
										<td class="colunaMinima">
											<a href="<%=(listaPendenciaServentia.getQtdePreAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=PreAnalisadas&amp;TipoPendencia=" + listaPendenciaServentia.getIdTipo()+ "&amp;Filtro=1":"#")%>" >
												<%=listaPendenciaServentia.getQtdePreAnalisadas()%>
											</a>
										</td>
										<td class="colunaMinima">
											<a href="<%=(listaPendenciaServentia.getQtdeReservadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=Reservadas&amp;TipoPendencia=" + listaPendenciaServentia.getIdTipo()+ "&amp;Filtro=1":"#")%>" >
												<%=listaPendenciaServentia.getQtdeReservadas()%>
											</a>									
										</td>
									</tr>
							<%		}	%>
							</table>
							<%		
									
						} %>
					</fieldset>
					<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>	
		</div>
	</body>
</html>