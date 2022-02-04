<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
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
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt");	%>
			
			<%if(usuario != null) { %>
<!-- 			<div class="area"><h2></h2>&Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div> -->
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
					<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial Digital</span>
					<br>
				Serventia:
					<span class="bold"><%= usuario.getServentia() %> &nbsp;&nbsp;&nbsp; </span><br>
					<% if (usuario.getCargoTipo() != null && !usuario.getCargoTipo().equalsIgnoreCase("")){ %>
					Cargo: 
					<span class="bold"> <%=usuario.getCargoTipo() %></span><br>
					<% } 
					if (usuario.getUsuarioServentiaChefe() != null && !usuario.getUsuarioServentiaChefe().equalsIgnoreCase("")){ %>
					Chefe:
					<span class="bold"> <%=usuario.getUsuarioServentiaChefe() %></span></p>
					<% } %>
			<%} %>
		
			<%
			if (paginaInicialDt != null){
				List dadosServentia = paginaInicialDt.getDadosServentia();
					if (dadosServentia != null && dadosServentia.size() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Dados Serventia</legend>
				
				<table class="Tabela">
					<thead>
						<tr>
							<th width="50%">Tipo</th>
							<th>Qtde</th>
						</tr>
					</thead>
					<tbody>
					<%
						for(int i=0; i < dadosServentia.size();i++){
							ListaDadosServentiaDt dadosServentiaDt = (ListaDadosServentiaDt) dadosServentia.get(i);
							if (!dadosServentiaDt.isSigiloso()){ %>					
								<tr>
									<td><%=dadosServentiaDt.getDescricao()%></td>
									<td align="center">
										<a href="<%=dadosServentiaDt.getLink()%>"><%=dadosServentiaDt.getQuantidade()%></a>
									</td>
								</tr>
							<%} 
						} %>
					</tbody>
				</table>
			</fieldset>			
			<% } %>
			
							<%if(paginaInicialDt.getQtdeArquivadosSemMovito() > 0 || paginaInicialDt.getQtdeInconsistenciaPoloPassivo() > 0 || paginaInicialDt.getQtdeProcessosSemAssunto() > 0 ||
			     paginaInicialDt.getQtdeProcessosComAssuntoPai() > 0 || paginaInicialDt.getQtdeProcessosComClassePai() > 0) {%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Inconsistências</legend>
				<%
				long loTotal2 = 0;
				%>
				<table class="Tabela">
					<thead>
						<tr>
							<th width="50%">Tipo</th>
							<th>Quantidade</th>
						</tr>
					</thead>
					<tbody>
						<%
						if (paginaInicialDt.getQtdeArquivadosSemMovito()>0) {
							loTotal2+= paginaInicialDt.getQtdeArquivadosSemMovito();
						%>
						<tr>
							<td title="O quantitativo de processos arquivados com falta de dados, são processos nesta serventia que estão sem o indicador de tipo de arquivamento e que são processos criminais.">
								<b><font color="red">Processos Arquivados com Falta de Dados</font></b>
							</td>
							<td align="center">					
								<a href="BuscaProcesso?PaginaAtual=<%=Configuracao.Localizar%>&tipoConsultaProcesso=6"><b><font color="red"><%=paginaInicialDt.getQtdeArquivadosSemMovito()%></font></b></a>
							</td>							
						</tr>
						<%}%>
						<%
						if (paginaInicialDt.getQtdeInconsistenciaPoloPassivo()>0) {
							loTotal2+= paginaInicialDt.getQtdeInconsistenciaPoloPassivo();
						%>
						<tr>
							<td title="O quantitativo de processos com inconsistências no polo passivo, são processos criminais desta serventia, cuja a parte polo passivo está sem alguma das sequintes informações: nome da mãe, nome do pai, data de nascimento, sem informação de naturalidade, sem cpf ou sem rg.">
								<b><font color="red">Processos com Dados de Partes Inconsistentes</font></b>
							</td>
							<td align="center">					
								<a href="BuscaProcesso?PaginaAtual=<%=Configuracao.Localizar%>&tipoConsultaProcesso=7"><b><font color="red"><%=paginaInicialDt.getQtdeInconsistenciaPoloPassivo()%></font></b></a>
							</td>							
						</tr>
						<%}%>
						<%
						if (paginaInicialDt.getQtdeProcessosSemAssunto()>0) {
							loTotal2+= paginaInicialDt.getQtdeProcessosSemAssunto();
						%>
						<tr>
							<td title="O quantitativo de processos cadastrados sem Assunto.">
								<b><font color="red">Processos sem Assunto</font></b>
							</td>
							<td align="center">					
								<a href="BuscaProcesso?PaginaAtual=<%=Configuracao.Localizar%>&tipoConsultaProcesso=10"><b><font color="red"><%=paginaInicialDt.getQtdeProcessosSemAssunto()%></font></b></a>
							</td>							
						</tr>
						<%}%>
						<%
						if (paginaInicialDt.getQtdeProcessosComAssuntoPai()>0) {
							loTotal2+= paginaInicialDt.getQtdeProcessosComAssuntoPai();
						%>
						<tr>
							<td title="O quantitativo de processos cadastrados com Assunto Incorreto - assunto pai na árvore do CNJ.">
								<b><font color="red">Processos com Assunto Incorreto (Desativado/Pai)</font></b>
							</td>
							<td align="center">					
								<a href="BuscaProcesso?PaginaAtual=<%=Configuracao.Localizar%>&tipoConsultaProcesso=11"><b><font color="red"><%=paginaInicialDt.getQtdeProcessosComAssuntoPai()%></font></b></a>
							</td>							
						</tr>
						<%}%>
						<%
						if (paginaInicialDt.getQtdeProcessosComClassePai()>0) {
							loTotal2+= paginaInicialDt.getQtdeProcessosComClassePai();
						%>
						<tr>
							<td title="O quantitativo de processos cadastrados com Classe Incorreta - classe pai na árvore do CNJ.">
								<b><font color="red">Processos com Classe Incorreta (Desativada/Pai)</font></b>
							</td>
							<td align="center">					
								<a href="BuscaProcesso?PaginaAtual=<%=Configuracao.Localizar%>&tipoConsultaProcesso=12"><b><font color="red"><%=paginaInicialDt.getQtdeProcessosComClassePai()%></font></b></a>
							</td>							
						</tr>
						<%}%>					
						<tr>
							<td>Total</td>
							<td align="center">
								<%=loTotal2%>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			<%} %>
			
			<%
			List pendencias = paginaInicialDt.getPendenciasServentia();
			if (pendencias != null && pendencias.size() > 0){ %>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias Chefe</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th> Tipo Pendência </th>
							<th> Pré-analisadas </th>
							<th> Reservadas </th>
						</tr>
					</thead>
					<tbody>
					<%
						Iterator itPendencias = pendencias.iterator();
						while (itPendencias.hasNext()){
							ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
					
							List reservadas = listaPendencia.getReservadas();
							%>
							<tr>
								<td><%=listaPendencia.getTitulo()%></td>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=PreAnalisadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=4":"#")%>" >
										<%=listaPendencia.getQtdePreAnalisadas()%>
									</a>
								</td>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdeReservadas()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=Reservadas&amp;TipoPendencia=" + listaPendencia.getIdTipo()+ "&amp;Filtro=4":"#")%>" >
										<%=listaPendencia.getQtdeReservadas()%>
									</a>									
								</td>
								
							</tr>
								
						<%
						if (reservadas != null && reservadas.size() > 0){%>									
						<%
							Iterator itReservadas = reservadas.iterator();
										
							while (itReservadas.hasNext()){
								PendenciaDt pendenciaDt = (PendenciaDt)itReservadas.next();%>
							<%}%>
						<%}%>
				<%}	%>
				</table>
			</fieldset>
			<%} %>			
			
			<%
			pendencias = paginaInicialDt.getPendenciasAnalise();
			if ( (pendencias != null && pendencias.size() > 0) || paginaInicialDt.getQtdePreAnalisesMultiplasPendencias() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Processos Aguardando Parecer</legend>
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
						int contNaoAnalisadas = 0;
						int contPreAnalisadas = 0;
						Iterator itPendencias = pendencias.iterator();
						while (itPendencias.hasNext()){
							ListaPendenciaDt listaPendencia = (ListaPendenciaDt) itPendencias.next();
							contNaoAnalisadas += listaPendencia.getQtdeNaoAnalisadas();
							contPreAnalisadas += listaPendencia.getQtdePreAnalisadas();
						%>
						<tr>
							<td><%=listaPendencia.getTitulo()%></td>
							<td class="colunaMinima">
								<a href="<%=(listaPendencia.getQtdeNaoAnalisadas()>0?"PreAnalisarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
									<%=listaPendencia.getQtdeNaoAnalisadas()%>
								</a>
							</td>
							<td class="colunaMinima">
								<a href="<%=(listaPendencia.getQtdePreAnalisadas()>0?"PreAnalisarPendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
									<%=listaPendencia.getQtdePreAnalisadas()%>
								</a>
							</td>
						</tr>
									
						<% 	} if (pendencias != null && pendencias.size() > 0) {%>
						<tr>
							<td align="right">Total</td>
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
			</fieldset>
			<%} 
			}%>
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>