<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<jsp:useBean id="Pendenciadt" scope="session" class="br.gov.go.tj.projudi.dt.PendenciaDt" />
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/tabelas.js"></script>
	<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
	<script type="text/javascript">
		modoEdicaoPendencia = false;
		possivelMarcar = true;
	</script>
	
	<%@ include file="js/buscarArquivos.js"%>
		
	<style type="text/css">
		@import url('./css/Principal.css');
		
		div.pendencia {
			padding: 5px;
		}
		
		div.pendencia label.lbl {
			font-weight: bold;
			float: left;
			width: 120px;
			text-align: right;
			padding-right: 10px;
			margin-top:0;
		}
	</style>
</head>
<body>
  <div id="divCorpo" class="divCorpo" >
  	<div class="area"><h2>&raquo; Detalhes da Pend&ecirc;ncia (<%=Pendenciadt.getId()%>)</h2></div>
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input type="hidden" name="fluxo" id="fluxo" value="3" />
		<input type="hidden" name="Id_PendenciaPai" value="<%=Pendenciadt.getId()%>" />
		<input type="hidden" name="Id_Pendencia" value="<%=Pendenciadt.getId()%>" />

		<br />	
		
		<%if (Pendenciadt.isPendenciaDeProcesso()){ %>
		<button type="submit" name="operacao" value="MovimentarProcessoPendencia" onclick="AlterarValue('PaginaAtual', <%=Configuracao.Localizar%>);">
			<img src="imagens/22x22/btn_movimentar.png" alt="Movimentar" />
			Movimentar Processo
		</button>
		<%} %>	
		
		<%if (Pendenciadt.isVisto()){ %>
		<button type="submit" name="operacao" value="VistarPendenciaPrazoDecorrido" onclick="AlterarValue('PaginaAtual', <%=Configuracao.Curinga7%>); AlterarValue('fluxo','6');">
			<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Vistar pendência" /> -->
				Vistar
		</button>
		<%} %>
		
		<br /><br />
		
		<div class="pendencia">
			<div class="dados">
				<div>
					<div style="width: 33%; float: left;">
						<label class="lbl">Tipo:</label>
						<%=Pendenciadt.getPendenciaTipo()%>
						<br />
						<label class="lbl">Status:</label>
						<%=Pendenciadt.getPendenciaStatus()%>
						<br />
						
						<%if (!Pendenciadt.getDataVisto().equals("")){%>
							<label class="lbl">Visualizado em:</label>
							<%=Pendenciadt.getDataVisto()%>
							<br />
							<label class="lbl" title="Tempo entre o fim e a visualiza&ccedil;&atilde;o ">Tempo:</label>
							<%=Funcoes.diferencaDatasFormatado(Pendenciadt.getDataVisto(), Pendenciadt.getDataFim())%>
							<br />
						<%}%>
						
						<%if (!Pendenciadt.getDataLimite().equals("")){%>
							<label class="lbl">Data Limite:</label>
							<%=Pendenciadt.getDataLimite()%>						
							<br />
						<%}%>
						
						<%if (!Pendenciadt.getPrazo().equals("")){%>
							<label class="lbl">Prazo:</label>
							<%=Pendenciadt.getPrazo()%>
							<br />
						<%}%>						
					</div>
					
					<div style="width: 33%; float: left;">
						<label class="lbl">Cadastrador:</label>
						<%=Pendenciadt.getNomeUsuarioCadastrador()%>
						<br />
						<label class="lbl">Data In&iacute;cio:</label>
						<%=Pendenciadt.getDataInicio()%>
						<br />
					</div>
					
					<%if (!Pendenciadt.getDataFim().equals("")){%>
						<div style="width: 33%; float: left;">
							<label class="lbl">Usuário:</label>
							<%=Pendenciadt.getNomeUsuarioFinalizador()%>
							<br />
							<label class="lbl">Data Fim:</label>
							<%=Pendenciadt.getDataFim()%>
							<br />
							
							<label class="lbl" title="Tempo para resolu&ccedil;&atilde;o da pend&ecirc;ncia">Tempo:</label>
							<%=Funcoes.diferencaDatasFormatado(Pendenciadt.getDataFim(), Pendenciadt.getDataInicio())%>
							<br />
						</div>
					<%}%>
				</div>	
				
				<br />
				<label class="lbl">Respons&aacute;vel</label>
				<%=Pendenciadt.getNomeResponsavel()%>
				<br />
				
				<%			
				if (request.getAttribute("IdMaisRecente") != null && !request.getAttribute("IdMaisRecente").equals("")){%>
					<br />
					<a href="Pendencia?PaginaAtual=7&amp;Id_Pendencia=<%=request.getAttribute("IdMaisRecente")%>&amp;fluxo=3" title="Clique para ver a movimenta&ccedil;&atilde;o mais recente">
						Existe uma pend&ecirc;ncia mais recente que esta
					</a>
					<br /><br />
				<%}%>
				
				<%if (request.getAttribute("ListaAndamentos") != null ){
					List lista = (List)request.getAttribute("ListaAndamentos");
					if (lista.size() > 0){
						PendenciaDt pendenciaFinal = (PendenciaDt)lista.get(lista.size() - 1);
						%>
						<label class="lbl" title="Tempo total de andamentos">Tempo Total</label>
						<%=Funcoes.diferencaDatasFormatado(Pendenciadt.getDataFim(), pendenciaFinal.getDataInicio())%>
						<br />
					<%}%>
				<%}%>
				
				<%if (!	(Pendenciadt.getId_Processo().equals("") && Pendenciadt.getId_Movimentacao().equals("") & 
						Pendenciadt.getId_ProcessoPrioridade().equals("") ) ){%>
					<fieldset class="fieldEdicaoEscuro">
						<legend>Origem de Processo</legend>
					
						<label class="lbl">N&uacute;mero:</label>
						<a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>">
							<%=Pendenciadt.getProcessoNumero()%>
						</a>
						<br />
						<label class="lbl">Movimenta&ccedil;&atilde;o:</label>
						<%=Pendenciadt.getMovimentacao()%>
						<br />
						
						<%if (Pendenciadt.getId_ProcessoParte() != null && !Pendenciadt.getId_ProcessoParte().trim().equals("")){%>
							<label class="lbl">Parte:</label>
							<%=Pendenciadt.getNomeParte()%>
							<br />
						<%}%>
						
						<%if (Pendenciadt.getId_ProcessoPrioridade() != null && !Pendenciadt.getId_ProcessoPrioridade().trim().equals("")){%>
							<label class="lbl">Prioridade:</label>
							<%=Pendenciadt.getProcessoPrioridade()%>
							<br />
						<%}%>
					</fieldset>
				<%}%>
				
				<%request.setAttribute("temDataVisto", Pendenciadt.temDataVisto());%>					
				<%@include   file="Padroes/ListaArquivosVisualizar.jspf"%>
				
				<%
					List lista = (List)request.getAttribute("ListaAndamentos");
				
					if (lista != null && lista.size() > 0){%>
						<fieldset class="fieldEdicaoEscuro">
							<legend>Andamentos</legend>
							
							<table id="tabelaAndamentos" class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima">Num.</th>
										<th>Tipo</th>
										<th>Status</th>
										<th>Usuário</th>
										<th>Serventia</th>
										<th>Data de In&iacute;cio</th>
										<th>Data de Fim</th>
										<th>Tempo</th>
										<th class="colunaMinima">Op&ccedil;&otilde;es</th>
									</tr>
								</thead>
				
								<tfoot>
									<tr>
										<td colspan="9">Quantidade de andamentos: <%=lista.size()%></td>
									</tr>
								</tfoot>
								
								<tbody id="TabelaArquivos">
									<%
									Iterator it = lista.iterator();
									int qtd = 0;
									while (it.hasNext()){
										PendenciaDt obj = (PendenciaDt)it.next();
										%>
										<tr>
											<td><%=obj.getId()%></td>
											<td><%=obj.getPendenciaTipo()%></td>
											<td><%=obj.getPendenciaStatus()%></td>
											<td><%=obj.getNomeUsuarioFinalizador()%></td>
											<td><%=obj.getServentiaUsuarioFinalizador()%></td>
											<td class="lista_data" width="150"><%=obj.getDataInicio()%></td>
											<td class="lista_data" width="150"><%=obj.getDataFim()%></td>
											<td class="lista_data" width="150"><%=Funcoes.diferencaDatasFormatado(obj.getDataFim(), obj.getDataInicio())%></td>
											<td class="colunaMinima">
												<a href="#" onclick="buscarArquivosPendenciaJSON('<%=obj.getId()%>'); return false;">
													<img src="imagens/22x22/ico_arquivos.png" alt="Mostrar ou ocultar arquivos" title="Mostrar ou ocultar arquivos" />
												</a>
												<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>&amp;fluxo=3"
													title="Visualizar detalhes da pend&ecirc;ncia <%=obj.getId()%>">
													<img src="imagens/imgLocalizarPequena.png" alt="Selecionar" />
												</a>
											</td>
										</tr>
										<tr id="linha_<%=obj.getId()%>" style="display: none;">
											<td colspan="7" id="pai_<%=obj.getId()%>" class="Linha"></td>
										</tr>
										<%
									}
									%>
								</tbody>
							</table>
						</fieldset>
				<%}%>
			</div>
		</div>
				
		<%@ include file="Padroes/Mensagens.jspf"%>
  </form>
 </div>
</body>
</html>