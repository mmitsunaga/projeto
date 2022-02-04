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
		<script type='text/javascript' src='./js/Funcoes.js'></script>
    	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
		<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
		<style type="text/css">
			@import url('./css/Principal.css');
			
		</style>
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt"); %>
			<%if(usuario != null) { %>
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
					<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial</span><br>
				
				<span class="span"><%= usuario.getServentia() + " - " + usuario.getEstadoRepresentacao()%> </span></p>
			<%} %>
		
			<%
			if (paginaInicialDt != null){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Processos/Audiências</legend>
				
				<table class="Tabela">
					<thead>
						<tr>
							<th width="50%">Tipo</th>
							<th>Qtde</th>
						</tr>
					</thead>
					<tbody>
					<%
					List dadosServentia = paginaInicialDt.getDadosServentia();
					if (dadosServentia != null && dadosServentia.size() > 0){
						for(int i=0; i < dadosServentia.size();i++){
							ListaDadosServentiaDt dadosServentiaDt = (ListaDadosServentiaDt) dadosServentia.get(i);
					%>
					
						<tr>
							<td><%=dadosServentiaDt.getDescricao()%></td>
							<td align="center">
								<a href="<%=dadosServentiaDt.getLink()%>"><%=dadosServentiaDt.getQuantidade()%></a>
							</td>
						</tr>
					
					<% }
					} else {%>
						<tr>
							<td><em>Nenhum Processo Cadastrado</em></td>
						</tr>
					<%} %>
					</tbody>
				</table>
			</fieldset>			
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias</legend>
				
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
							<fieldset>
								<legend><%=listaPendencia.getTitulo()%></legend>
								<table class="Tabela">
									<thead>
										<tr>
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
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
		
											<tr >
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
							<fieldset>
								<legend><%=listaPendencia.getTitulo()%></legend>
								<table class="Tabela">
									<thead>
										<tr>
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
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
		
											<tr >
												<td><%=pendenciaDt.getId()%></td>
												<td>
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td><%=pendenciaDt.getMovimentacao()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataFim()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
												<td class="colunaMinima">
													<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>" 
														title="Marcar Aguardando Peticionamento">
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
				
				
				<%//**
				List pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer = paginaInicialDt.getPendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer();
				if (pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer != null && pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer.size() > 0){
					Iterator itPendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer = pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer.iterator();
					while (itPendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
							if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
						%>
							<fieldset>
								<legend><%=listaPendencia.getTitulo()%></legend>
								<table class="Tabela">
									<thead>
										<tr>
											<th class="colunaMinima lista_id">N&uacute;m.</th>
											<th>Processo</th>
											<th>Movimenta&ccedil;&atilde;o</th>
											<th width=12%>Data Publicação</th>
											<th>Data Limite</th>
											<th>Marcar</th>
											<th class="colunaMinima">Descartar</th>
										</tr>
									</thead>
									<tbody>
									<%
									Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
		
											<tr >
												<td><%=pendenciaDt.getId()%></td>
												<td>
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td><%=pendenciaDt.getMovimentacao()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataFim()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
												<td class="colunaMinima">
													<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>" 
														title="Marcar Aguardando Peticionamento">
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
				}//**%>
			
				<%
				List pendencias = paginaInicialDt.getPendenciasUsuarioServentia();
				int contOutrasPendencias = 0;
				if (pendencias != null && pendencias.size() > 0){
					Iterator itPendencias = pendencias.iterator();
					while (itPendencias.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO) || 
							(Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.CARTA_CITACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
						if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
				%>
					<fieldset>
						<legend><%=listaPendencia.getTitulo()%></legend>
						<table class="Tabela">
							<thead>
								<tr>
									<th class="colunaMinima lista_id">N&uacute;m.</th>
									<th>Processo</th>
									<th>Parte</th>
									<th>Movimenta&ccedil;&atilde;o</th>
									<th>Data Início</th>
									<th>Data Limite</th>
									<th colspan="2" class="colunaMinima">Op&ccedil;&otilde;es</th>
								</tr>
							</thead>
							<tbody>
							<%
							Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
								while (itPendenciasAndamento.hasNext()){
									PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>

									<tr <%=(pendenciaDt.isAdvogadoPrincipal()?"class=\"linhaDestaqueIntimacao\" title=\"Advogado Principal\"":"")%> ">
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
												<img src="imagens/22x22/btn_movimentar.png" alt="Solucionar" title="Efetuar Leitura / Marcar Aguardando Peticionamento" />
											</a>
										</td>
										<%} else { %>															
										<td class="colunaMinima">
											<a href="Pendencia?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;fluxo=1&amp;NovaPesquisa=true">
												<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
											</a>
										</td>
										<%}%>
									</tr>
							<%}%>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="8">Quantidade: <%=pendenciasAndamento.size()%></td>
								</tr>
							</tfoot>
						</table>
					</fieldset>
					<%	}
						} 
					}//fim while
					
				} %>
				
				<%if ((pendenciasIntimacoesAguardandoParecer == null || pendenciasIntimacoesAguardandoParecer.size() == 0)
						&& (pendenciasIntimacoesLeituraAutomaticaAguardandoParecer == null || pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.size() == 0)
						&& (pendencias == null || pendencias.size() == 0)){ %>
					<em>N&atilde;o h&aacute; pend&ecirc;ncias</em>
				<%} %>
				
			</fieldset>
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Processos Aguardando Peticionamento</legend>
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
								<%if (usuario != null && (usuario.getId_UsuarioServentiaChefe() == null || usuario.getId_UsuarioServentiaChefe().equals("") )) {%>
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
					<em>N&atilde;o h&aacute; Processos Aguardando Peticionamento</em>
				<%} %>
			</fieldset>
			
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
			<%}%>
			
			<%if (paginaInicialDt.getQtdePendenciaLiberaAcesso() > 0){ %>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Liberação de acesso(s)</legend>
					<table class="Tabela">
						<thead>
							<tr>
								<th> Tipo Pendência </th>
								<th> Quantidade </th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>Liberação de acesso(s)</td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdePendenciaLiberaAcesso()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=7":"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdePendenciaLiberaAcesso() %></font>
									</a>
								</td>
							</tr>
					</table>
				</fieldset>
			<%} 
			}%> 
			<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>	
		</div>
	</body>
</html>