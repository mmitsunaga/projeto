<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<jsp:useBean id="UsuarioSessao" class="br.gov.go.tj.projudi.ne.UsuarioNe" scope="request" />


<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaConclusaoDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.ne.ProcessoNe"%>
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
   		<script type='text/javascript' src='./js/Funcoes.js'></script>
   		
   		
   		<script> 


   		function AlterarAction(id, valor) {
   			var obj = document.getElementById(id);
   			obj.action = valor;
   		}

   		function AlterarValue(id, valor) {
   			obj = document.getElementById(id);

   			if (obj != null) obj.value = valor;
   		}

   		
	    	function submeterClassificar(action, id_Pendencia, paginaAtual, preAnalise, fluxoEditar){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		AlterarValue('preAnaliseConclusao', preAnalise);
	    		AlterarValue('tempFluxo1', fluxoEditar);	    		
	    		document.Formulario.submit();
	    	}

   		</script>
   		
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
			
			<br />
			<br />
		
			<%
			if (paginaInicialDt != null){
				List dadosServentia = paginaInicialDt.getDadosServentia();
					if (dadosServentia != null && dadosServentia.size() > 0){
			%>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Processos</legend>
				<%
				int inQtdConclusoesGabinete = paginaInicialDt.getQtdConclusoesGabinete();
				if(inQtdConclusoesGabinete>0){%>
					<div class="col100">
						<div class="col65">&nbsp</div>
						<div class="col25" style="text-align: right"><b>Total de Conclusões no Gabinete</b></div> 
						<div class="col8" style="text-align: center"> <a href="AnalisarConclusao?PaginaAtual=<%=Configuracao.Curinga9 %>">	<%=inQtdConclusoesGabinete%>	</a></div>
					</div>
				<%}%>
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
			
			<%
			List pendencias = paginaInicialDt.getPendenciasServentiaCargo();
			int contOutrasPendencias = 0;
			if (pendencias != null && pendencias.size() > 0){
				Iterator itPendencias = pendencias.iterator();
				while (itPendencias.hasNext()){
					ListaPendenciaDt listaPendencia = (ListaPendenciaDt)itPendencias.next();
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
										<th>Tipo Pendência</th>
										<th>Movimenta&ccedil;&atilde;o</th>
										<th>Data Início</th>
										<th class="colunaMinima">Op&ccedil;&otilde;es</th>
									</tr>
								</thead>
								<tbody>
								<%
								Iterator itPendenciasAndamento = pendenciasAndamento.iterator();
									while (itPendenciasAndamento.hasNext()){
										PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();%>
	
										<tr >
											<td><%=pendenciaDt.getId()%></td>
											<td align="center">
												<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
											</td>
											<td align="center"><%=pendenciaDt.getPendenciaTipo()%> </td>
											<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
											<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
											<td align="center">
												<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
													<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
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
				} //fim while
				
			} else {%>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Pedido de Vista/Relatório e Revisão Aguardando Distribuição</legend>
					<p> Nenhuma Pendência encontrada. </p>
			<%}%>
			</fieldset>
			
			<%
			List conclusoes = paginaInicialDt.getConclusoes();
			long tipoClassificadortemp = -10;
			long tipoClassificador = 0;
			
			
			if (conclusoes != null && conclusoes.size() > 0){
				Iterator itPendencias = conclusoes.iterator();
				while (itPendencias.hasNext()){
					ListaConclusaoDt listaConclusao = (ListaConclusaoDt)itPendencias.next();
					
					List conclusoesAndamento = listaConclusao.getConclusoesAndamento();

					
						if (conclusoesAndamento != null && conclusoesAndamento.size() > 0){
					%>
						<fieldset class="fieldEdicaoEscuro">
							<legend><%=listaConclusao.getTitulo()%></legend>
							<table class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima lista_id">N&uacute;m.</th>
										<th>Processo</th>
										<th>Tipo Pendência</th>
										<th>Movimenta&ccedil;&atilde;o</th>
										<th>Data Início</th>
										<th class="colunaMinima">Op&ccedil;&otilde;es</th>
										<th>Classificar</th>
									</tr>
								</thead>
								

								
								<tbody>
								<%
								Iterator itPendenciasAndamento = conclusoesAndamento.iterator();
								
									while (itPendenciasAndamento.hasNext()){
										PendenciaDt pendenciaDt = (PendenciaDt)itPendenciasAndamento.next();
										
										if (pendenciaDt.getId_Classificador() != null && pendenciaDt.getId_Classificador().length() > 0){
											tipoClassificador = Funcoes.StringToLong(pendenciaDt.getId_Classificador());
										} else {
											tipoClassificador = 0;
										}
										

									//Testa a necessidade de abrir uma linha para o tipo de classificador
										if (tipoClassificadortemp == -10)
										{
											tipoClassificadortemp = tipoClassificador;	
									%>
										<tr>
											<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (pendenciaDt.getClassificador() != null && pendenciaDt.getClassificador().length()>0?pendenciaDt.getClassificador():"Sem classificador")%> </th>
										</tr>
									<%
										}else if (tipoClassificadortemp != tipoClassificador){
											tipoClassificadortemp = tipoClassificador;
									%>		
											<tr>
												<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (pendenciaDt.getClassificador() != null && pendenciaDt.getClassificador().length()>0?pendenciaDt.getClassificador():"Sem classificador")%> </th>
											</tr>
									<%
										}
									%>
										
										
	
										<tr >
											<td><%=pendenciaDt.getId()%></td>
											<td align="center">
												<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
											</td>
											<td align="center"><%=pendenciaDt.getPendenciaTipo()%> </td>
											<td align="center"><%=pendenciaDt.getMovimentacao()%></td>
											<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
											<td align="center">
												<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
													<img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" />
												</a> 
											</td>
				                   			<td align="center">
				                   				<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.LocalizarDWR%>&amp;Id_Pendencia=<%=pendenciaDt.getId()%>&amp;preAnaliseConclusao=false&amp;tempFluxo1=1" >
			                   						<img src='./imagens/32x32/btn_atualizar.png' alt="Classificar" title="Classificar"/>
			                   					</a>
				                   			</td>
											
										</tr>
								<%}%>
								
								
								</tbody>
								<tfoot>
									<tr>
										<td colspan="8">Quantidade: <%=conclusoesAndamento.size()%></td>
									</tr>
								</tfoot>
							</table>
						</fieldset>
						<%	} else {%>
						<em>N&atilde;o h&aacute; pend&ecirc;ncias</em>
						<%}
				} //fim while
				
			} else {%>
				<fieldset class="fieldEdicaoEscuro">
					<legend>Conclusões Aguardando Distribuição</legend>
					<p> Nenhuma Pendência encontrada. </p>
			<%}%>
			</fieldset>
		
		<br />
			<fieldset class="fieldEdicaoEscuro">
				<legend>Pend&ecirc;ncias Serventia</legend>
				<%
				List pendenciasServentia = paginaInicialDt.getPendenciasServentia();
				if (pendenciasServentia != null && pendenciasServentia.size() > 0 || 
						(paginaInicialDt.getQtdPendenciasEmAndamento()>0)){%>
					
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
							Iterator itPendencias = pendenciasServentia.iterator();
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
					</table>
					<%} else {%>
					<p> Nenhuma Pendência encontrada.<br />
						Para visualizar as pendências o usuário deverá ser habilitado na estrutura de cargos da serventia. <br />
						Para verificar seu cargo, acesse: SEGURANÇA - CARGOS DA - SERVENTIA . <br />
						Caso você não esteja em um cargo, contate a equipe de suporte do Processo Judicial Digital. </p>
					<%} %>	
				</fieldset>
		<br />	
			
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