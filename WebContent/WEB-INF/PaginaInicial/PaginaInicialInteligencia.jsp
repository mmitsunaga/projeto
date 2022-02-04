<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
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
   		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt");	%>
			
			<%if( usuario != null ) { %>
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
				<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial</span><br>
				
				Serventia:
				<span class="bold"><%= usuario.getServentia() %> &nbsp;&nbsp;&nbsp; </span><br>
				<% if (usuario.getServentiaCargo() != null && !usuario.getServentiaCargo().equalsIgnoreCase("")){ %>
					Serventia Cargo:
					<% if (usuario.getServentiaCargo() != null && !usuario.getServentiaCargo().equalsIgnoreCase("")){ %>
						<span title="Cargo: <%=usuario.getCargoTipo()%>" class="bold"> <%=usuario.getServentiaCargo() %></span></p>
					<% } else {%>
						<span class="bold"> <%=usuario.getServentiaCargo() %></span></p>
					<% } %>
				<% } %>
			<%} %>
		
			<% if (paginaInicialDt != null){ %>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias Serventia</legend>
				<%
				List pendencias = paginaInicialDt.getPendenciasServentia();
				if (pendencias != null && pendencias.size() > 0 || 
						(paginaInicialDt.getQtdPendenciasEmAndamento()>0 || paginaInicialDt.getQtdExpedidasAguardandoVisto()>0 
								|| paginaInicialDt.getQtdePrazoDecorrido()>0)){%>
					
					<table class="Tabela">
						<thead>
							<tr>
								<th> Tipo Pendência </th>
								<th width="12%"> Pré-analisadas Serventia </th>
								<th width="12%"> Não analisadas </th>
								<th> Pré-analisadas </th>
								<th> Reservadas </th>
							</tr>
						</thead>
						<tbody>
						<%
							Iterator itPendencias = pendencias.iterator();
						    int contPreAnalisadasServentia = 0;
							int contNaoAnalisadas = 0;
						    int contPreAnalisadas = 0;
						    int contReservadas = 0;
							while (itPendencias.hasNext()){
								ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
								contPreAnalisadasServentia += listaPendencia.getQtdePreAnalisadasServentia();
								contNaoAnalisadas += listaPendencia.getQtdeNaoAnalisadas();
								contPreAnalisadas += listaPendencia.getQtdePreAnalisadas();
								contReservadas += listaPendencia.getQtdeReservadas();
						%>
							<tr>
								<td><%=listaPendencia.getTitulo()%></td>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdePreAnalisadasServentia()>0?"Pendencia?PaginaAtual=" + Configuracao.Editar + "&amp;operacao=PreAnalisadasServentia&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
									<%=listaPendencia.getQtdePreAnalisadasServentia()%>
									</a>
								</td>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdeNaoAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Editar + "&amp;operacao=PegarPorTipoServentia&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
									<%=listaPendencia.getQtdeNaoAnalisadas()%>
									</a>
								</td>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=PreAnalisadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=1":"#")%>" >
										<%=listaPendencia.getQtdePreAnalisadas()%>
									</a>
								</td>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdeReservadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=Reservadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=1":"#")%>" >
										<%=listaPendencia.getQtdeReservadas()%>
									</a>									
								</td>
							</tr>
					<%		}	%>
						<tr>
						<tr>
							<td align="right"><strong>Total</strong></td>
							<td class="colunaMinima"><strong><%=contPreAnalisadasServentia%></strong></td>
							<td class="colunaMinima"><strong><%=contNaoAnalisadas%></strong></td>
							<td class="colunaMinima"><strong><%=contPreAnalisadas%></strong></td>
							<td class="colunaMinima"><strong><%=contReservadas%></strong></td>
						</tr>
						<!-- Para usuários do perfil Estagiário estes itens devem ser ocultados -->
						<%if (request.getAttribute("ocultarBotoesEstagiario") == null) { %>
							<tr>
								<td>Acompanhamento</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdPendenciasEmAndamento()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 +"&amp;fluxo=1":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdPendenciasEmAndamento() %></font>
									</a>
								</td>
								<td></td>
							</tr>
							<tr>
								<td>Expedidas Aguardando Visto</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdExpedidasAguardandoVisto()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 +"&amp;Id_PendenciaStatus=11&PendenciaStatus=Cumprimento+Aguardando+Visto&amp;fluxo=1":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdExpedidasAguardandoVisto() %></font>
									</a>
								</td>
								<td></td>
							</tr>
							<tr>
								<td>Prazo Decorrido</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdePrazoDecorrido()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=2":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdePrazoDecorrido() %></font>
									</a>
								</td>
								<td></td>
							</tr>
							<%if (paginaInicialDt.getQtdePrazoDecorridoDevolucaoAutos()>0) {%>
								<tr>
									<td>Prazo Decorrido Devolução de Autos</td>
									<td></td>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdePrazoDecorridoDevolucaoAutos()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=15":"#")%>" >
										<font color="red"><%=paginaInicialDt.getQtdePrazoDecorridoDevolucaoAutos() %></font>
										</a>
									</td>
									<td></td>
								</tr>
							<%} %>
							</tbody>
						<%} %>
					</table>
					<%} else {%>
					<p> Nenhuma Pendência encontrada.<br />
						Para visualizar as pendências o usuário deverá ser habilitado na estrutura de cargos da serventia. <br />
						Para verificar seu cargo, acesse: SEGURANÇA - CARGOS DA - SERVENTIA . <br />
						Caso você não esteja em um cargo, contate a equipe de suporte do Processo Judicial Digital. </p>
					<%} %>	
				</fieldset>
			
				<%
				List pendenciasServentiaTipo = paginaInicialDt.getPendenciasServentiaTipo();
				if (pendenciasServentiaTipo != null && pendenciasServentiaTipo.size() > 0 || paginaInicialDt.getQtdePendenciaInformativa() > 0){
				%>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Pend&ecirc;ncias Serventia Tipo</legend>
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
							Iterator itPendencias = pendenciasServentiaTipo.iterator();
							int teste = 0;
							while (itPendencias.hasNext()){
								ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
							%>
								<tr>
									<td><%=listaPendencia.getTitulo()%></td>
									<td class="colunaMinima">
										<a href="<%=(listaPendencia.getQtdeNaoAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Editar + "&amp;operacao=PegarPorTipoServentiaTipo&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
											<%=listaPendencia.getQtdeNaoAnalisadas()%>
										</a>
									</td>
									<td class="colunaMinima">
										<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=PreAnalisadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=2":"#")%>" >
											<%=listaPendencia.getQtdePreAnalisadas()%>
										</a>
									</td>
									<td class="colunaMinima">
										<a href="<%=(listaPendencia.getQtdeReservadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=Reservadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=2":"#")%>" >
											<%=listaPendencia.getQtdeReservadas()%>
										</a>									
									</td>
								</tr>
							<%}	%>
							<%if (paginaInicialDt.getQtdePendenciaInformativa() > 0){ %>
								<tr>
									<td>Informativo(s)</td>
									<td></td>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdePendenciaInformativa()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=12":"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdePendenciaInformativa() %></font>
										</a>
									</td>
									<td></td>
								</tr>
							<%} %>
							</tbody>
						</table>
				</fieldset>
				<%} %>
			
				<%
				List pendenciasServentiaCargo = paginaInicialDt.getPendenciasServentiaCargo();
				if ((pendenciasServentiaCargo != null && pendenciasServentiaCargo.size() > 0) || paginaInicialDt.getQtdePendenciaLiberaAcesso() > 0){
				%>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Pend&ecirc;ncias Cargo</legend>
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
							Iterator itPendencias = pendenciasServentiaCargo.iterator();
							while (itPendencias.hasNext()){
								ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
							%>
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
							<%}	%>
							<%if (paginaInicialDt.getQtdePendenciaLiberaAcesso() > 0){ %>
								<tr>
									<td>Liberação de acesso(s)</td>
									<td></td>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdePendenciaLiberaAcesso()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=7":"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdePendenciaLiberaAcesso() %></font>
										</a>
									</td>
									<td></td>
								</tr>
							<%} %>
						</tbody>
					</table>
				</fieldset>
				<%}%>
			<%
				pendencias = paginaInicialDt.getPendenciasServentiaMandadoPrisao();
				if (pendencias != null && pendencias.size() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias de Mandado de Prisão</legend>
					<table class="Tabela">
						<thead>
							<tr>
								<th> Tipo Pendência </th>
								<th class="colunaMinima"> Quantidade </th>
							</tr>
						</thead>
						<tbody>
						<%
						Iterator itPendencias = pendencias.iterator();
						while (itPendencias.hasNext()){
							ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
						%>
							<tr>
								<td><%=listaPendencia.getTitulo()%></td>
								<td class="colunaMinima">
									<a href="<%=listaPendencia.getUrlRetorno()%>"><%=listaPendencia.getQuantidadePendencias()%></a>
								</td>								
							</tr>
						<%}	%>
						</tbody>
					</table>
			</fieldset>
			<%} 
		}%>
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>