<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page  import="java.util.Map"%>
<%@page  import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<html>
<head>
	<title>Pend&ecirc;ncia por tipos</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>      
</head>
<body>
  <div class="divCorpo">
	
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
	  <div class="area"><h2>&raquo; Pend&ecirc;ncias Abertas</h2></div>
	  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
	  
		<%
		String operacoes[] = {
					"PegarPorTipoUsuarioResponsavel",
					"PegarPorTipoServentiaTipo",
					"PegarPorTipoServentiaCargo"};
		String titulos[][] = 	{	
									{"Usuário Responsável", "ListaPendenciaUsuarioResponsavel"}, 
									{"Tipo de Serventia", "ListaPendenciaServentiaTipo"},
									{"Cargo", "ListaPendenciaServentiaCargo"}
									//, {"Total", "ListaPendencia"} //Somente para testes
								};
	
		for (int i = 0; i < titulos.length; i++){
			String operacao = operacoes[i];
			String titulo = titulos[i][0];
			String nomeLista = titulos[i][1];
		%>
			<h2><%=titulo%></h2>
		
			<div class="divTabela" >
				<table class="Tabela">
					<thead>
						<tr>
							<th>Tipo</th>
							<th class="colunaMinima">Quantidade</th>
							<th class="colunaMinima">Op&ccedil;&otilde;es</th>
						</tr>		
					</thead>
					
					<tbody>
				<%
				Map mTemp = (Map)request.getAttribute(nomeLista);
				
				if (mTemp != null && mTemp.size() > 0 ){
					Iterator it = mTemp.keySet().iterator();
					
					int qtd = 0;
					int qtdPendencias = 0;
					while (it.hasNext()){
						String chave = (String)it.next();
						qtdPendencias += Funcoes.StringToInt(((String [])mTemp.get(chave))[1]);
					%>
							<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
								<td>
									<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;operacao=<%=operacao%>&amp;tipo=<%=((String [])mTemp.get(chave))[0]%>"><%=chave%></a>
								</td>
								<td align="right">
									<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;operacao=<%=operacao%>&amp;tipo=<%=((String [])mTemp.get(chave))[0]%>"><%=((String [])mTemp.get(chave))[1]%></a>
								</td>								
								<td class="colunaMinima">
									<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;operacao=<%=operacao%>&amp;tipo=<%=((String [])mTemp.get(chave))[0]%>">
										<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
									</a>
								</td>
							</tr>
					<%}%>		
						</tbody>
						<tfoot>
							<tr>
								<td colspan="3">Total de pend&ecirc;ncias: <%=qtdPendencias%></td>
							</tr>
						</tfoot>
				<%} else { %>
					<tr>
						<td colspan="3">N&atilde;o h&aacute; pend&ecirc;ncias</td>
					</tr>
				<% } %>
				</table>
			</div>
		<%}%>
		<%@ include file="Padroes/Mensagens.jspf"%>
  </form>
 </div>
</body>
</html>
