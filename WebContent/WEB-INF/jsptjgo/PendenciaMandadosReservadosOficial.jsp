<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/tabelas.js"></script>
	<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
		    
</head>
<body>
<div class="divCorpo">
	<form action="Pendencia" method="post" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="TipoPendencia" name="TipoPendencia" type="hidden" value="<%=request.getAttribute("TipoPendencia")%>" />
		<input id="fluxo" name="fluxo" type="hidden" value="0" />
		<input id="opcao" name="opcao" type="hidden" value="<%=request.getAttribute("opcao")%>" />
		<input id="Filtro" name="Filtro" type="hidden" value="<%=request.getAttribute("Filtro")%>" />

		<%@ include file="Padroes/Mensagens.jspf"%>
		
		<div id="divLocalizar" class="divLocalizar" >
			<div class="area"><h2>&raquo; Mandados Reservados</h2></div>
			<fieldset id="formLocalizar" class="formLocalizar"> 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Op&ccedil;&otilde;es</legend>
				
				
				<button type="submit" name="operacao" value="Consultar"	>
					<!-- <img src="imagens/22x22/btn_pesquisar.png" alt="Consultar" /> -->
					Consultar Todos
				</button>
<!-- 				<button type="submit" name="operacao" value="ConsultarReservadas"	> -->
<!-- 					<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar Reservadas" /> -->
<!-- 					Consultar Reservadas -->
<!-- 				</button> -->
<!-- 				<button type="submit" name="operacao" value="ConsultarPreAnalisadas"	> -->
<!-- 					<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar Pré-Analisadas" /> -->
<!-- 					Consultar Pré-Analisadas -->
<!-- 				</button> -->
				
				</br></br>
				
				<div class="divBotoesDireita">
					Ordenar por:
					<select name="ordenacao">
						<option value="dataLimite" <%= request.getAttribute("ordenacao") != null && "dataLimite".equals(request.getAttribute("ordenacao")) ? "selected" : "" %> > Data limite </option>
						<option value="dataInicio" <%= request.getAttribute("ordenacao")  != null && "dataInicio".equals(request.getAttribute("ordenacao")) ? "selected" : "" %> > Data início </option>
						<option value="numeroMandado" <%= request.getAttribute("ordenacao") != null && "numeroMandado".equals(request.getAttribute("ordenacao")) ? "selected" : "" %> > Número do Mandado </option>
						<option value="processo" <%= request.getAttribute("ordenacao") != null && "processo".equals(request.getAttribute("ordenacao")) ? "selected" : "" %> > Processo </option>
					</select>
				</div>
				
		   	</fieldset>		
		
			<div id="divTabela" class="divTabela" >		
				<table id="Tabela" class="Tabela">
					<thead>
						<tr>
			           		<th class="colunaMinima">
<!-- 			           			<input type="checkbox" id="selecionarTodos" onclick="atualizarChecks(this, 'Tabela')" /> -->
			           		</th>
							<th class="colunaMinima lista_id">Mandado</th>
							<th class="colunaMinima">Processo</th>
							<th class="colunaMinima">Assistência</th>
							<th>Tipo</th>
							<th>Status</th>
							<th>Data Início</th>
							<th width="10%" >Data Limite</th>
							<th class="colunaMinima">Op&ccedil;&otilde;es</th>
						</tr>
					</thead>
	
					<%
					List lista = (List) request.getAttribute("ListaReservas");
					%>
					<tfoot>
						<tr>
							<td colspan="10">Quantidade de reservas: <%=lista != null?lista.size():0%></td>
						</tr>
					</tfoot>
					
					<tbody>
						<%
						if (lista != null && lista.size() > 0){
							int qtd = 0;
							if (lista != null){
								Iterator it = lista.iterator();
								while(it.hasNext()){
									PendenciaDt pendenciaDt = (PendenciaDt)it.next();
								%>
								<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
									<td>
<%-- 										<%if (pendenciaDt.isPodeLiberar()){%> --%>
<%-- 												<input type="checkbox" name="pendenciaSelecionada[]" value="<%=pendenciaDt.getId()%>@#;#@<%=pendenciaDt.getHash()%>" /> --%>
<%-- 										<%}%> --%>
									</td>
									<td class="colunaMinima lista_id"><%=pendenciaDt.getId_MandadoJudicial()%></td>
									<td width="160" align="center">
										<%if (!pendenciaDt.getId_Processo().equals("")){%>
												<strong>
												<%=pendenciaDt.getProcessoNumero()%>
												</strong>
										<%}%>
									</td>
									<td><%=pendenciaDt.getAssistencia()%></td>
									<td><%=pendenciaDt.getPendenciaTipo()%></td>
									<td><%=pendenciaDt.getPendenciaStatus()%></td>
									<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
									<td class="lista_data">
										<%=pendenciaDt.getDataLimite()%>
									</td>
									<td class="colunaMinima">
										<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>">
											<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
										</a>
										<%if (pendenciaDt.isPodeLiberar()){%>

											<%if(request.getAttribute("exibirLiberar") != null && request.getAttribute("exibirLiberar").equals("true")){%>										
											<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=pendenciaDt.getId()%>&amp;opcao=<%=request.getAttribute("opcao")%>&amp;TipoPendencia=<%=request.getAttribute("TipoPendencia")%>&amp;Filtro=<%=request.getAttribute("Filtro")%>&amp;fluxo=1&amp;hash=<%=pendenciaDt.getHash()%>">
												<img src="imagens/22x22/ico_liberar.png" alt="Liberar" title="Liberar a pend&ecirc;ncia" />
											</a>
											<%}%>
											
										<%}%>
									</td>
								</tr>						
								<%
								}
							}
						} else {
							%>
							<tr>
								<td colspan="10">N&atilde;o h&aacute; pend&ecirc;ncias reservadas</td>
							</tr>
							<%
						}
						%>
					</tbody>
				</table>
			</div>
		</div>		
  </form>
</div>
</body>
</html>
