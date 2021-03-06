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
   		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt"); %>
			
			<%if(usuario != null) { %>
				<div class="area"><h2><i class="fa fa-user"></i>
					&Aacute;rea do <%=GrupoDt.getAtividadeUsuario(((usuario.getGrupoCodigo()!=null && usuario.getGrupoCodigo().length() > 0)?usuario.getGrupoCodigo():"0"))%>
				</h2></div>
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
							<%}%>
						</tr>
					</tbody>
					<% } %>
					
					<%if (paginaInicialDt.getQuantidadePrescritos() > 0){%>
						<tr>
							<td><b><font color="red"> <%=paginaInicialDt.getDescricaoPrescritos()%></font></b></td>
							<td align="center" > 
								<a href="<%=paginaInicialDt.getLinkPrescritos()%>">	<font color="red"><%=paginaInicialDt.getQuantidadePrescritos()%></font></a>
							</td>
						</tr>
					<%}%>
					
					<%if (paginaInicialDt.getQuantidadePrisaoForaPrazo() > 0){%>
						<tr>
							<td><b><font color="red"><%=paginaInicialDt.getDescricaoPrisaoForaPrazo()%></font></b></td>
							<td align="center" > 
								<a href="<%=paginaInicialDt.getLinkPrisaoForaPrazo()%>">	<font color="red"><%=paginaInicialDt.getQuantidadePrisaoForaPrazo()%></font></a>
							</td>
						</tr>
					<%}%>
					
				</table>
			</fieldset>			
			<% } %>
			
						<%if(paginaInicialDt.getQtdeArquivadosSemMovito() > 0 || paginaInicialDt.getQtdeInconsistenciaPoloPassivo() > 0 || paginaInicialDt.getQtdeProcessosSemAssunto() > 0 ||
			     paginaInicialDt.getQtdeProcessosComAssuntoPai() > 0 || paginaInicialDt.getQtdeProcessosComClassePai() > 0) {%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Inconsist?ncias</legend>
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
							<td title="O quantitativo de processos arquivados com falta de dados, s?o processos nesta serventia que est?o sem o indicador de tipo de arquivamento e que s?o processos criminais.">
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
							<td title="O quantitativo de processos com inconsist?ncias no polo passivo, s?o processos criminais desta serventia, cuja a parte polo passivo est? sem alguma das sequintes informa??es: nome da m?e, nome do pai, data de nascimento, sem informa??o de naturalidade, sem cpf ou sem rg.">
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
							<td title="O quantitativo de processos cadastrados com Assunto Incorreto - assunto pai na ?rvore do CNJ.">
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
							<td title="O quantitativo de processos cadastrados com Classe Incorreta - classe pai na ?rvore do CNJ.">
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
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Conclus&otilde;es</legend>
				<%
				int inQtdConclusoesGabinete = paginaInicialDt.getQtdConclusoesGabinete();
				if(inQtdConclusoesGabinete>0){%>
					<div class="col100">
						<div class="col65">&nbsp</div>
						<div class="col25" style="text-align: right"><b>Total de Conclus?es no Gabinete</b></div> 
						<div class="col8" style="text-align: center"> <a href="AnalisarConclusao?PaginaAtual=<%=Configuracao.Curinga9 %>">	<%=inQtdConclusoesGabinete%>	</a></div>
					</div>
				<%}%>
				<table class="Tabela">
					<thead>
						<tr>
							<th> Tipo Conclus?o </th>
							<th width="18%"> Aguardando assinatura </th>
							<th width="15%"> N?o analisadas </th>
							<th> Pr?-analisadas </th>
						</tr>
					</thead>
					<tbody>
						<%
						List conclusoes = paginaInicialDt.getConclusoes();
						int contNaoAssinadas = 0;
						int contNaoAnalisadas = 0;
						int contPreAnalisadas = 0;
						boolean boConclusoes = true;
						if (conclusoes != null && conclusoes.size() > 0){
							Iterator itConclusoes = conclusoes.iterator();
							while (itConclusoes.hasNext()){
								ListaConclusaoDt listaConclusao = (ListaConclusaoDt) itConclusoes.next();
								contNaoAssinadas += listaConclusao.getQtdePreAnalisadasPendentesAssinatura();
								contNaoAnalisadas += listaConclusao.getQtdeNaoAnalisadas();
								contPreAnalisadas += listaConclusao.getQtdePreAnalisadas();
						%>
						<tr>
							<td><%=listaConclusao.getTitulo()%></td>
							<%if (usuario != null && ( usuario.getId_UsuarioServentiaChefe() == null || usuario.getId_UsuarioServentiaChefe().equals("") )) {%>
							<td class="colunaMinima">
								<a href="<%=(listaConclusao.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusao.getIdTipo():"#")%>" >
									<%=listaConclusao.getQtdePreAnalisadasPendentesAssinatura()%>
								</a>
							</td>
							<td class="colunaMinima">
								<a href="<%=(listaConclusao.getQtdeNaoAnalisadas()>0?"AnalisarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusao.getIdTipo():"#")%>" >
									<%=listaConclusao.getQtdeNaoAnalisadas()%>
								</a>
							</td>
							<% } %>
							<td class="colunaMinima">
								<a href="<%=(listaConclusao.getQtdePreAnalisadas()>0?"PreAnalisarConclusao?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaConclusao.getIdTipo():"#")%>" >
									<%=listaConclusao.getQtdePreAnalisadas()%>
								</a>
							</td>
						</tr>
									
						<% 	}	%>
						<tr>
							<td align="right">Total</td>
							<td class="colunaMinima">
								<strong>   
									<a title="Assinar todas conclus?es" href="<%= (contNaoAssinadas>0?"AssinarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=":"#")%>">
									  <%=contNaoAssinadas%>							
									</a>
								
								</strong>
							</td>
							<td class="colunaMinima"><strong><%=contNaoAnalisadas%></strong></td>
							<td class="colunaMinima"><strong><%=contPreAnalisadas%></strong></td>
						</tr>
					</tbody>
					<%		
					} else {
						boConclusoes = false;
					} if (paginaInicialDt.getQtdePreAnalisesMultiplasConclusoes() > 0) {
						boConclusoes = true; %>
						<tr>
							<td> Pr?-An?lises M?ltiplas </td>
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
				if ((pendencias != null && pendencias.size() > 0) || paginaInicialDt.getQtdePrazoDecorrido()>0 || paginaInicialDt.getQtdePrazoADecorrer()>0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias Cargo</legend>
					<table class="Tabela">
						<thead>
							<tr>
								<th> Tipo Pend?ncia </th>
								<th width="18%"> Aguardando assinatura  </th>
								<th class="colunaMinima"> Pr?-analisadas </th>
							</tr>
						</thead>
						<tbody>
						<%
						Iterator itPendencias = pendencias.iterator();			
						int totalPendenciasPendentesAssinatura = 0;

						while (itPendencias.hasNext()){
							ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
							
							totalPendenciasPendentesAssinatura += listaPendencia.getQtdePreAnalisadasPendentesAssinatura();
							
						%>
							<tr>
								<td><%=listaPendencia.getTitulo()%></td>								
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
										<%=listaPendencia.getQtdePreAnalisadasPendentesAssinatura()%>
									</a>
								</td>
								<td class="colunaMinima">
									<%if (listaPendencia.getUrlRetorno()!= null && listaPendencia.getUrlRetorno().length() > 0) {%>
									<a href="<%=listaPendencia.getUrlRetorno()%>"><%=listaPendencia.getQuantidadePendencias()%></a>
									<%} else if (listaPendencia.getTitulo().equals("Elabora??o de Voto")) {%>
									<a href="<%="Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=AbertasServentiaCargo&amp;TipoPend=ElaboracaoVoto&amp;AguardandoAssinar=S&amp;TipoPendencia=" + listaPendencia.getIdTipo()%>"><%=listaPendencia.getQuantidadePendencias()%></a>
									<%} else { %>
									<a href="<%="Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=AbertasServentiaCargo&amp;AguardandoAssinar=S&amp;TipoPendencia=" + listaPendencia.getIdTipo()%>"><%=listaPendencia.getQuantidadePendencias()%></a>
									<%} %>
								</td>								
							</tr>

						<%}	%>
						
							<tr>
								<td align="right">Total</td>
								<td class="colunaMinima">
									<a href="<%=(totalPendenciasPendentesAssinatura>0?"AssinarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=":"#")%>" >
										<%=totalPendenciasPendentesAssinatura%>
									</a>
								
								</td>
							</tr>	
												
						<%if (paginaInicialDt.getQtdPendenciasEmAndamento()>0) {%>
							<tr>
								<td>Acompanhamento</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdPendenciasEmAndamento()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 +"&amp;fluxo=1":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdPendenciasEmAndamento() %></font>
									</a>
								</td>
							</tr>
						<%} %>
						<%if (paginaInicialDt.getQtdExpedidasAguardandoVisto()>0) {%>
							<tr>
								<td>Expedidas Aguardando Visto</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdExpedidasAguardandoVisto()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 +"&amp;Id_PendenciaStatus=11&PendenciaStatus=Cumprimento+Aguardando+Visto&amp;fluxo=1":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdExpedidasAguardandoVisto() %></font>
									</a>
								</td>
							</tr>
						<%} %>
						<%if (paginaInicialDt.getQtdePrazoDecorrido()>0) {%>
							<tr>
								<td>Prazo Decorrido</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdePrazoDecorrido()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=2":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdePrazoDecorrido() %></font>
									</a>
								</td>
							</tr>
						<%} %>
						
						<%if (paginaInicialDt.getQtdePrazoADecorrer()>0) {%>
							<tr>
								<td>Prazo A Decorrer</td>
								<td></td>
								<td class="colunaMinima">
									<a href="<%=(paginaInicialDt.getQtdePrazoADecorrer()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo= 6":"#")%>" >
									<font color="red"><%=paginaInicialDt.getQtdePrazoADecorrer() %></font>
									</a>
								</td>
							</tr>
						<%} %>
						
						</tbody>
					</table>
			</fieldset>
			<%}%>
			
			<%
				pendencias = paginaInicialDt.getPendenciasAnalise();
				if ( (pendencias != null && pendencias.size() > 0) || paginaInicialDt.getQtdePreAnalisesMultiplasPendencias() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend?ncias Aguardando An?lise</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th> Tipo Pend?ncia </th>							
							<th width="18%"> Aguardando assinatura </th>
							<th width="15%"> N?o analisadas </th>
							<th> Pr?-analisadas </th>
						</tr>
					</thead>
					<tbody>
					<%
						contNaoAssinadas = 0;
						contNaoAnalisadas = 0;
						contPreAnalisadas = 0;
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
								<td> Pr?-An?lises M?ltiplas </td>
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
						<legend>Pend&ecirc;ncias Libera??o de Acesso</legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th> Tipo Pend?ncia </th>
										<th> Quantidade </th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Pend?ncia(s) de libera??o de acesso</td>
										<td class="colunaMinima">
											<a href="<%=(paginaInicialDt.getQtdePendenciaLiberaAcesso()>0?"Pendencia?PaginaAtual=" + Configuracao.Curinga6 + "&amp;fluxo=7":"#")%>" >
											<font color="green"><%=paginaInicialDt.getQtdePendenciaLiberaAcesso() %></font>
											</a>
										</td>
									</tr>
								</tbody>
							</table>
					</fieldset>	
			<%} %>
			
			<%if (paginaInicialDt.getQtdePendenciaInformativa() > 0){ %>
					<fieldset class="fieldEdicaoEscuro">
						<legend>Informativo(s) do Processo Judicial Digital</legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th> Tipo Pend?ncia </th>
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
								</tbody>
							</table>
					</fieldset>	
			<%} %>
			
			<%if (paginaInicialDt.getQtdeSessoesAguardandoAcordaoPreAnalisadas() > 0 || paginaInicialDt.getQtdeSessoesAguardandoAcordaoAguardandoAssinatura() > 0) { %>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Processo(s)/Recurso(s) Apreciado(s) na Sess?o de Julgamento</legend>
						<table class="Tabela">
							<thead>
								<tr>
									<th width="60%"> Tipo </th>
									<th width="20%"> Aguardando assinatura </th>
									<th width="20%"> Texto Ac?rd?o / Ementa </th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td> Aguardando Magistrado Inserir Ac?rd?o / Ementa </td>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdeSessoesAguardandoAcordaoAguardandoAssinatura()>0?"AudienciaSegundoGrau?PaginaAtual=" + Configuracao.Curinga7 + "&amp;SomentePendentesAcordao=S&amp;SomenteAguardandoAssinatura=S":"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdeSessoesAguardandoAcordaoAguardandoAssinatura()%></font>
										</a>
									</td>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdeSessoesAguardandoAcordaoPreAnalisadas()>0?"AudienciaSegundoGrau?PaginaAtual=" + Configuracao.Curinga7 + "&amp;SomentePendentesAcordao=S&amp;SomenteAguardandoAssinatura=N":"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdeSessoesAguardandoAcordaoPreAnalisadas()%></font>
										</a>
									</td>
								</tr>
							</tbody>
						</table>
				</fieldset>	
			<%} %>
			
			<%		
				if (paginaInicialDt.getConclusoesSessao() != null && paginaInicialDt.getConclusoesSessao().size() > 0){		
			%>			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Processo(s)/Recurso(s) Aguardando Sess?o de Julgamento</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th>  </th>							
							<th width="15%"> N?o analisadas </th>
							<th width="15%"> Pr?-analisadas </th>
						</tr>
					</thead>
					<tbody>
					<%
											
						contNaoAnalisadas = 0;
						contPreAnalisadas = 0;					
						Iterator itConclusoesSessao = paginaInicialDt.getConclusoesSessao().iterator();
						while (itConclusoesSessao.hasNext()){
								ListaConclusaoDt listaConclusaoSessao = (ListaConclusaoDt) itConclusoesSessao.next();								
								contNaoAnalisadas += listaConclusaoSessao.getQtdeNaoAnalisadas();
								contPreAnalisadas += listaConclusaoSessao.getQtdePreAnalisadas();
						%>
							<tr>
								<td><%=listaConclusaoSessao.getTitulo()%></td>
								<%if (usuario.getId_UsuarioServentiaChefe() == null || usuario.getId_UsuarioServentiaChefe().equals("")) {%>								
									<td class="colunaMinima">
										<a href="<%=(listaConclusaoSessao.getQtdeNaoAnalisadas()>0?"AnalisarVotoEmenta?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusaoSessao.getIdTipo() :"#")%>" >
											<%=listaConclusaoSessao.getQtdeNaoAnalisadas()%>
										</a>
									</td>
								<% } %>
								<td class="colunaMinima">
									<a href="<%=(listaConclusaoSessao.getQtdePreAnalisadas()>0?"PreAnalisarVotoEmenta?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaConclusaoSessao.getIdTipo() :"#")%>" >
										<%=listaConclusaoSessao.getQtdePreAnalisadas()%>
									</a>
								</td>
							</tr>	
						<%}%>						
					</tbody>								
				</table>			
			</fieldset>
			<%}
			}%>	
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>