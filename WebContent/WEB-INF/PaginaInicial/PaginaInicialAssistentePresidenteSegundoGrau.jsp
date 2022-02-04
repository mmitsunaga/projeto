<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaConclusaoDt"%>
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
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt"); %>
			
			<%if(usuario != null) { %>
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
					<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial Digital</span>
				
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
		
			<%
			if (paginaInicialDt != null){
				List dadosServentia = paginaInicialDt.getDadosServentia();
			%>
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Conclus&otilde;es</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th> Tipo Conclusão </th>
							<th width="15%"> Não analisadas </th>
							<th> Pré-analisadas </th>
						</tr>
					</thead>
					<tbody>
					<%
						boolean boConclusoes = true;
						List conclusoes = paginaInicialDt.getConclusoes();
						if (conclusoes != null && conclusoes.size() > 0){
							Iterator itConclusoes = conclusoes.iterator();
							while (itConclusoes.hasNext()){
								ListaConclusaoDt listaConclusao = (ListaConclusaoDt) itConclusoes.next();
						%>
						<tr>
							<td><%=listaConclusao.getTitulo()%></td>
							<td class="colunaMinima">
								<a href="<%=(listaConclusao.getQtdeNaoAnalisadas()>0?"PreAnalisarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusao.getIdTipo() + "&amp;paginaInicial=true":"#")%>" >
									<%=listaConclusao.getQtdeNaoAnalisadas()%>
								</a>
							</td>
							<td class="colunaMinima">
								<a href="<%=(listaConclusao.getQtdePreAnalisadas()>0?"PreAnalisarConclusao?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaConclusao.getIdTipo():"#")%>" >
									<%=listaConclusao.getQtdePreAnalisadas()%>
								</a>
							</td>
						</tr>
						<% 	}	%>
						
					</tbody>
					<%		
					} else {
						boConclusoes = false; 
					}
					if (paginaInicialDt.getQtdePreAnalisesMultiplasConclusoes() > 0) {
						boConclusoes = true; %>
					<tr>
						<td> Pré-Análises Múltiplas </td>
						<td class="colunaMinima" colspan="2">
							<a href="<%=(paginaInicialDt.getQtdePreAnalisesMultiplasConclusoes()>0?"PreAnalisarConclusao?PaginaAtual=" + Configuracao.Curinga7:"#")%>" >
							<%=paginaInicialDt.getQtdePreAnalisesMultiplasConclusoes() %>
							</a>
						</td>
					</tr>
					<% } else if (!boConclusoes) boConclusoes = false; %>
					
					<% if (!boConclusoes) { %>
					<tr><td><em>N&atilde;o h&aacute; Conclus&otilde;es</em></td></tr>
					<% } %>
				</table>			
			</fieldset>
			
			<%
				List pendencias = paginaInicialDt.getPendenciasServentiaCargo();
				if (pendencias != null && pendencias.size() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias Chefe</legend>
					<table class="Tabela">
						<thead>
							<tr>
								<th> Tipo Pendência </th>
								<th class="colunaMinima"> Pré-analisadas </th>
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
									<a href="<%="Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=AbertasServentiaCargo&amp;TipoPendencia=" + listaPendencia.getIdTipo()%>"><%=listaPendencia.getQuantidadePendencias()%></a>
								</td>								
							</tr>
						<%}	%>
					</table>
			</fieldset>
			<%}%>
			
			<%
				List pendenciasAnalise = paginaInicialDt.getPendenciasAnalise();
				if ( (pendenciasAnalise != null && pendenciasAnalise.size() > 0) || paginaInicialDt.getQtdePreAnalisesMultiplasPendencias() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pendências Aguardando Análise</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th> Tipo Pendência </th>
							<th width="15%"> Não analisadas </th>
							<th> Pré-analisadas </th>
						</tr>
					</thead>
					<tbody>
					<%
						boConclusoes = true;
						Iterator itPendencias = pendenciasAnalise.iterator();
						while (itPendencias.hasNext()){
							ListaPendenciaDt listaPendencia = (ListaPendenciaDt) itPendencias.next();
					%>
						<tr>
							<td><%=listaPendencia.getTitulo()%></td>
							<td class="colunaMinima">
								<a href="<%=(listaPendencia.getQtdeNaoAnalisadas()>0?"PreAnalisarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo() + "&amp;paginaInicial=true":"#")%>" >
									<%=listaPendencia.getQtdeNaoAnalisadas()%>
								</a>
							</td>
							<td class="colunaMinima">
								<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"PreAnalisarPendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
									<%=listaPendencia.getQtdePreAnalisadas()%>
								</a>
							</td>
						</tr>
					<% 	}
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
			</fieldset>
			<% } %>
			
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
										<td>Pendência(s) de liberação de acesso</td>
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
			
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>