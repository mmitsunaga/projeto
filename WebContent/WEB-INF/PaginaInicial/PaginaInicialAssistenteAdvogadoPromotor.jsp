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
<%@page import="br.gov.go.tj.projudi.dt.ListaDadosServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
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
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
					<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial</span><br>
				
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
							<% }%>
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
							<legend><%=listaPendencia.getTitulo()%></legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima lista_id">N&uacute;m.</th>
										<th>Processo</th>
										<th>Movimenta&ccedil;&atilde;o</th>
										<th>Data Leitura</th>
										<th>Data Limite</th>
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
						<%	} else {%>
						<em>N&atilde;o h&aacute; pend&ecirc;ncias</em>
				 <%} 
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
										<th class="colunaMinima lista_id">N&uacute;m.</th>
										<th>Processo</th>
										<th>Movimenta&ccedil;&atilde;o</th>
										<th>Data Leitura</th>
										<th>Data Limite</th>
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
						<%	} else {%>
						<em>N&atilde;o h&aacute; pend&ecirc;ncias</em>
				 <%} 
				 } 
			   }
			}%>
			
			<!-- ****************************** -->
			
				<%
				List pendenciasServentiaCarogoChefe = paginaInicialDt.getPendenciasServentiaCargo();
				int contOutrasPendencias = 0;
				if (pendenciasServentiaCarogoChefe != null && pendenciasServentiaCarogoChefe.size() > 0){
					Iterator itPendencias = pendenciasServentiaCarogoChefe.iterator();
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
											<th class="colunaMinima lista_id">N&uacute;m.</th>
											<th>Processo</th>
											<th>Movimenta&ccedil;&atilde;o</th>
											<th>Data Início</th>
											<th>Data Limite</th>
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
												<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
												<td class="lista_data"><%=pendenciaDt.getDataLimite()%></td>
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
				<legend>Processos Aguardando Parecer/Peticionamento</legend>
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
			<%} %>
			
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
			<%} 
			}%>
			
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>