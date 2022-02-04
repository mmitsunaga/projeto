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

<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>
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
			if (paginaInicialDt != null){
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
							<td><%=dadosServentiaDt.getDescricao()%></td>
							<td align="center">
								<a href="<%=dadosServentiaDt.getLink()%>"><%=dadosServentiaDt.getQuantidade()%></a>
							</td>
						</tr>
					</tbody>
					<% } %>
				</table>
			</fieldset>			
			<% } %>
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias</legend>
				
				<%
				List pendencias = paginaInicialDt.getPendenciasServentia();
				int contOutrasPendencias = 0;
				if (pendencias != null && pendencias.size() > 0){
					Iterator itPendencias = pendencias.iterator();
					while (itPendencias.hasNext()){
						ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
						
						if ((Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.INTIMACAO) || (Funcoes.StringToInt(listaPendencia.getIdTipo()) == PendenciaTipoDt.CARTA_CITACAO)) {
						
						List pendenciasAndamento = listaPendencia.getPendenciasAndamento();
						
							if (pendenciasAndamento != null && pendenciasAndamento.size() > 0){
						%>
							<fieldset class="fieldEdicaoEscuro">
								<legend><%=listaPendencia.getTitulo()%></legend>
								<table class="Tabela">
									<thead>
										<tr>
											<th class="colunaMinima lista_id">N&uacute;m.</th>
											<th>Processo</th>
											<th>Movimenta&ccedil;&atilde;o</th>
											<th>Data Início</th>
											<th>Data Limite</th>
											<th colspan="2">Opções</th>
										</tr>
									</thead>
									<tbody id="CorpoTabelaDef">
									<%
									Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
										while (itPendenciasAndamento.hasNext()){
											PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
		
											<tr >
												<td><%=pendenciaDt.getId()%></td>
												<td width="130" align="center">
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
												<td width="150"><%=pendenciaDt.getDataInicio()%></td>
												<td width="150"><%=pendenciaDt.getDataLimite()%></td>
												
												<% if (Funcoes.StringToInt(usuario.getGrupoTipoCodigo()) == GrupoTipoDt.COORDENADOR_PROMOTORIA) { %>
												<td  width="15" align="center">
													<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_encaminhar.png"  title="Efetuar troca de responsável" />
													</a> 
												</td>
												<%} else if (Funcoes.StringToInt(usuario.getGrupoTipoCodigo()) == GrupoTipoDt.COORDENADOR_PROCURADORIA
																|| Funcoes.StringToInt(usuario.getGrupoTipoCodigo()) == GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO
																|| Funcoes.StringToInt(usuario.getGrupoTipoCodigo()) == GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA) {%>
												<td  width="40" align="center">
													<a href="PendenciaUsuarioServentiaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_encaminhar.png"  title="Efetuar troca de responsável" />
													</a> 
												</td>
												<%} %>
												<td  width="40" align="center">
													<a href="PendenciaUsuarioServentiaResponsavel?PaginaAtual=<%=Configuracao.Curinga6%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
														<img src="imagens/22x22/btn_excluir_2.png" alt="Retirar da Página Inicial" title="Retirar da Página Inicial" />
													</a> 
												</td>
												
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
				
				<!-- ****************************** -->
			
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
									<th class="colunaMinima lista_id">N&uacute;m.</th>
									<th>Processo</th>
									<th>Parte</th>
									<th>Movimenta&ccedil;&atilde;o</th>
									<th>Data Início</th>
									<th>Data Limite</th>
									<th colspan="2">Opções</th>
								</tr>
							</thead>
							<tbody>
							<%
							Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
								while (itPendenciasAndamento.hasNext()){
									PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>

									<tr <%=(pendenciaDt.isAdvogadoPrincipal()?"class=\"linhaDestaqueIntimacao\" title=\"Advogado Principal\"":"")%> ">
										<td><%=pendenciaDt.getId()%></td>
										<td align="center">
											<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
										</td>
										<td width="210"><%=pendenciaDt.getNomeParte()%></td>
										<td><%=pendenciaDt.getMovimentacao()%></td>
										<td width="150"><%=pendenciaDt.getDataInicio()%></td>
										<td width="150"><%=pendenciaDt.getDataLimite()%></td>
										<td>
											<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
												<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
											</a> 
										</td>
										<td align="center">
											<a href="PendenciaUsuarioServentiaResponsavel?PaginaAtual=<%=Configuracao.Curinga6%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
												<img src="imagens/22x22/btn_excluir_2.png" alt="Retirar da Página Inicial" title="Retirar da Página Inicial" />
											</a> 
										</td>
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
			
			<!-- ****************************** -->
				
				
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
			<%} %>
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>