<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Pend&ecirc;ncia</title>

    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
    
	<link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	
	
	
</head>
<body>
  <div class="divCorpo">
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
			<div class="area"><h2>&raquo; Pendência(s) Informativas do Processo Judicial Digital</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
		<input name="fluxo" type="hidden" value="2" />
		
		<%@ include file="Padroes/Mensagens.jspf"%>
		
		<div id="divLocalizar" class="divLocalizar">
			
			<div id="divTabela" class="divTabela" > 
			   	<table id="Tabela" class="Tabela">
			       	<thead>
			           	<tr>
			           		<th class="colunaMinima">Num.</th>
			                <th width="150">Tipo</th>
			                <th> Cadastrador </th>
			                <th width="160" align="center">Data Início de Exibição</th>
			                <th width="160" align="center">Data Fim de Exibição</th>
			                <th class="colunaMinima">Detalhes</th>
						</tr>
					</thead>
					
					<%if (request.getAttribute("ListaPendencia") != null){
						List lista = (List)request.getAttribute("ListaPendencia");
						Iterator it = lista.iterator();
						int qtd = 0;
						%>
		
						<tfoot>
							<tr>
								<td colspan="8">Quantidade de pend&ecirc;ncias: <span id="qtd"><%=lista.size()%></span></td>
							</tr>
						</tfoot>
				          	
						<tbody id="tabListaDados">
							<%			
							while (it.hasNext()){
								PendenciaDt obj = (PendenciaDt)it.next();
							%>
								<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
									<td class="colunaMinima lista_id"><%=obj.getId()%></td>
									<td align="center"><%=obj.getPendenciaTipo()%></td>
									<td align="center"><%=obj.getNomeUsuarioCadastrador()%></td>
									<td class="lista_data" width="160"><%=obj.getDataInicio()%></td>
									<td class="lista_data" width="160"><%=obj.getDataLimite()%></td>
									<td class="colunaMinima">
										<a href="Pendencia?PaginaAtual=6&amp;pendencia=<%=obj.getId()%>&amp;fluxo=13&amp;CodigoPendencia=<%=obj.getHash()%>" 
											title="Acessar pend&ecirc;ncia n&uacute;mero <%=obj.getId()%>">
											<img src="imagens/imgLocalizarPequena.png" alt="Acessar" />
										</a>
									</td>
								</tr>
							<%}%>
						</tbody>
					<%} else { %>
						<tbody>
							<tr>
								<td colspan="7">N&atilde;o h&aacute; pend&ecirc;ncias</td>
							</tr>
						</tbody>
					<%}%>
				</table>
			</div>
		</div>
  </form>
 </div>
</body>
</html>