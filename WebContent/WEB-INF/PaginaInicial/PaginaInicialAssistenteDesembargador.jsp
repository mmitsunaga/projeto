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
				<legend>Processos Ativos</legend>
				
				<table class="Tabela">
					<thead>
						<tr>
							<th width="50%">Serventia</th>
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
			
			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Conclus&otilde;es</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th> Tipo Conclusão </th>
							<%if (usuario.isPodeGuardarParaAssinar()){%>
							<th width="18%"> Aguardando assinatura </th>
							<%}%>
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
							<%if (usuario.isPodeGuardarParaAssinar()){%>
							<td class="colunaMinima">
								<a href="<%=(listaConclusao.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusao.getIdTipo():"#")%>" >
									<%=listaConclusao.getQtdePreAnalisadasPendentesAssinatura()%>
								</a>
							</td>
							<%}%>
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
								<%if (usuario.isPodeGuardarParaAssinar()){%>
								<th width="18%"> Aguardando assinatura </th>
								<%}%>
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
								<%if (usuario.isPodeGuardarParaAssinar()){%>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
										<%=listaPendencia.getQtdePreAnalisadasPendentesAssinatura()%>
									</a>
								</td>
								<%}%>
								<td class="colunaMinima">
									<a href="<%="Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&amp;opcao=AbertasServentiaCargo&amp;TipoPendencia=" + listaPendencia.getIdTipo()%>"><%=listaPendencia.getQuantidadePendencias()%></a>
								</td>								
							</tr>
						<%}	%>
					</table>
			</fieldset>
			<%} %>
			
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
							<%if (usuario.isPodeGuardarParaAssinar()){%>
							<th width="18%"> Aguardando assinatura </th>
							<%}%>
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
							<%if (usuario.isPodeGuardarParaAssinar()){%>
								<td class="colunaMinima">
									<a href="<%=(listaPendencia.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarPendencia?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaPendencia.getIdTipo():"#")%>" >
										<%=listaPendencia.getQtdePreAnalisadasPendentesAssinatura()%>
									</a>
								</td>
							<%}%>
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
			<%} %>
			
			<%if (paginaInicialDt.getQtdeSessoesAguardandoAcordaoPreAnalisadas() > 0 || paginaInicialDt.getQtdeSessoesAguardandoAcordaoAguardandoAssinatura() > 0  || paginaInicialDt.getQtdeSessoesAguardandoAcordaoNaoAnalisadas() > 0) { %>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Processo(s)/Recurso(s) Apreciado(s) na Sessão de Julgamento</legend>
						<table class="Tabela">
							<thead>
								<tr>
									<th> Tipo </th>
									<%if (usuario.isPodeGuardarParaAssinar()){%>
									<th width="18%"> Aguardando assinatura </th>
									<%}%>
									<th width="15%"> Não analisadas </th>
									<th> Pré-analisadas </th>									
								</tr>
							</thead>
							<tbody>
								<tr>								
									<td> Aguardando Magistrado Inserir Acórdão / Ementa </td>
									<%if (usuario.isPodeGuardarParaAssinar()){%>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdeSessoesAguardandoAcordaoAguardandoAssinatura()>0?"AssinarSessaoSegundoGrau?PaginaAtual=" + Configuracao.Localizar:"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdeSessoesAguardandoAcordaoAguardandoAssinatura()%></font>
										</a>
									</td>
									<%}%>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdeSessoesAguardandoAcordaoNaoAnalisadas()>0?"AudienciaSegundoGrau?PaginaAtual=" + Configuracao.Curinga7 + "&amp;SomentePendentesAcordao=S&amp;SomenteAguardandoAssinatura=N&amp;SomentePreAnalisadas=N":"#")%>" >
										<font color="green"><%=paginaInicialDt.getQtdeSessoesAguardandoAcordaoNaoAnalisadas()%></font>
										</a>
									</td>
									<td class="colunaMinima">
										<a href="<%=(paginaInicialDt.getQtdeSessoesAguardandoAcordaoPreAnalisadas()>0?"AudienciaSegundoGrau?PaginaAtual=" + Configuracao.Curinga7 + "&amp;SomentePendentesAcordao=S&amp;SomenteAguardandoAssinatura=N&amp;SomentePreAnalisadas=S":"#")%>" >
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
				<legend>Processo(s)/Recurso(s) Aguardando Sessão de Julgamento</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th>  </th>							
							<th width="15%"> Não analisadas </th>
							<th width="15%"> Pré-analisadas </th>
						</tr>
					</thead>
					<tbody>
					<%
											
						int contNaoAnalisadas = 0;
						int contPreAnalisadas = 0;					
						Iterator itConclusoesSessao = paginaInicialDt.getConclusoesSessao().iterator();
						while (itConclusoesSessao.hasNext()){
								ListaConclusaoDt listaConclusaoSessao = (ListaConclusaoDt) itConclusoesSessao.next();								
								contNaoAnalisadas += listaConclusaoSessao.getQtdeNaoAnalisadas();
								contPreAnalisadas += listaConclusaoSessao.getQtdePreAnalisadas();
						%>
							<tr>
								<td><%=listaConclusaoSessao.getTitulo()%></td>															
								<td class="colunaMinima">
									<a href="<%=(listaConclusaoSessao.getQtdeNaoAnalisadas()>0?"AnalisarVotoEmenta?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusaoSessao.getIdTipo() :"#")%>" >
										<%=listaConclusaoSessao.getQtdeNaoAnalisadas()%>
									</a>
								</td>								
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
			<%}%>
			
			<%		
				if (paginaInicialDt.getConclusoesVotoVencido() != null && paginaInicialDt.getConclusoesVotoVencido().size() > 0){		
			%>			
			<fieldset class="fieldEdicaoEscuro">
				<legend>Aguardando Voto</legend>
				<table class="Tabela">
					<thead>
						<tr>
							<th>  </th>			
							<%if (usuario.isPodeGuardarParaAssinar()){%>
							<th width="18%"> Aguardando assinatura </th>
							<%}%>				
							<th width="15%"> Não analisados </th>
							<th width="15%"> Pré-analisados </th>
						</tr>
					</thead>
					<tbody>
					<%					 						
						int contVotoNaoAnalisados = 0;
						int contVotoPreAnalisadas = 0;					
						Iterator itConclusoesVotoVencido = paginaInicialDt.getConclusoesVotoVencido().iterator();
						while (itConclusoesVotoVencido.hasNext()){
								ListaConclusaoDt listaConclusaoVoto = (ListaConclusaoDt) itConclusoesVotoVencido.next();								
								contVotoNaoAnalisados += listaConclusaoVoto.getQtdeNaoAnalisadas();
								contVotoPreAnalisadas += listaConclusaoVoto.getQtdePreAnalisadas();
						%>
							<tr>
								<td><%=listaConclusaoVoto.getTitulo()%></td>
								<%if (usuario.isPodeGuardarParaAssinar()){%>
									<td class="colunaMinima">										
										<a href="<%=(listaConclusaoVoto.getQtdePreAnalisadasPendentesAssinatura()>0?"AssinarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusaoVoto.getIdTipo():"#")%>" >
											<%=listaConclusaoVoto.getQtdePreAnalisadasPendentesAssinatura()%>
										</a>
									</td>
								<%}%>																
								<td class="colunaMinima">
									<a href="<%=(listaConclusaoVoto.getQtdeNaoAnalisadas()>0?"AnalisarConclusao?PaginaAtual=" + Configuracao.Localizar + "&amp;tipo=" + listaConclusaoVoto.getIdTipo():"#")%>" >
										<%=listaConclusaoVoto.getQtdeNaoAnalisadas()%>
									</a>
								</td>								
								<td class="colunaMinima">
									<a href="<%=(listaConclusaoVoto.getQtdePreAnalisadas()>0?"PreAnalisarConclusao?PaginaAtual=" + Configuracao.Curinga6 + "&amp;tipo=" + listaConclusaoVoto.getIdTipo():"#")%>" >
										<%=listaConclusaoVoto.getQtdePreAnalisadas()%>
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