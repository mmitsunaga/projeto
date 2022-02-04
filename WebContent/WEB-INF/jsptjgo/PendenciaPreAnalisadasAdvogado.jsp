<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
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

		<%@ include file="Padroes/Mensagens.jspf"%>
		
		<div id="divLocalizar" class="divLocalizar" >
			<div class="area"><h2>&raquo; Pend&ecirc;ncias Pré-Analisadas</h2></div>
			<fieldset id="formLocalizar" class="formLocalizar"> 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Opção</legend>		
				
				<button type="submit" name="operacao" value="Consultar"	>
					<!-- <img src="imagens/22x22/btn_pesquisar.png" alt="Consultar" /> -->
					Consultar Todas
				</button>
		   	</fieldset>		
		
			<div id="divTabela" class="divTabela" >		
				<table id="Tabela" class="Tabela">
					<thead>
						<tr>
							<th class="colunaMinima lista_id">N&uacute;m.</th>
							<th class="colunaMinima">Processo</th>
							<th>Movimenta&ccedil;&atilde;o</th>
							<th>Tipo</th>
							<th>Inicio</th>
							<th>Status</th>
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
									<td class="colunaMinima lista_id"><%=pendenciaDt.getId()%></td>
									<td class="lista_data">
										<%if (pendenciaDt.getProcessoNumero()!= null && !pendenciaDt.getProcessoNumero().equals("")){%>
											<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>">
												<%=pendenciaDt.getProcessoNumero()%>
											</a>
										<%} else { %>
											<b>--</b>
										<%}%>
									</td>
									<td>
										<%if (pendenciaDt.getMovimentacao()!= null && !pendenciaDt.getMovimentacao().equals("")){%>
											<%=pendenciaDt.getMovimentacao()%>
										<%} else { %>
											<b>--</b>
										<%}%>
									</td>
									<td align="center"><%=pendenciaDt.getPendenciaTipo()%></td>
									<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
									<td  align="center"><%=pendenciaDt.getPendenciaStatus()%></td>
									<td class="colunaMinima">
										<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar %>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">
											<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
										</a>
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
