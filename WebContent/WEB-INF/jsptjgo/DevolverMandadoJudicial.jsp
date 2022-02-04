<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>

<%@page import="br.gov.go.tj.projudi.dt.MandadoTipoRedistribuicaoDt"%>
<jsp:useBean id="MandadoTipoRedistribuicaoDt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoTipoRedistribuicaoDt"/>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
 
<html>
<head>
	<title>Devolver Mandados</title>

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
	<form action="MandadoJudicial" method="post" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
<%-- 		<input id="TipoPendencia" name="TipoPendencia" type="hidden" value="<%=request.getAttribute("TipoPendencia")%>" /> --%>
		<input id="fluxo" name="fluxo" type="hidden" value="0" />
		<input id="opcao" name="opcao" type="hidden" value="<%=request.getAttribute("opcao")%>" />
		<input id="Filtro" name="Filtro" type="hidden" value="<%=request.getAttribute("Filtro")%>" />

		<%@ include file="Padroes/Mensagens.jspf"%>
		
		<div id="divLocalizar" class="divLocalizar" >
			<div class="area"><h2>&raquo; Devolver Mandados</h2></div>
			<fieldset id="formLocalizar" class="formLocalizar"> 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Op&ccedil;&otilde;es</legend>
				
				
<%-- 				<%if(request.getAttribute("exibirLiberar") != null && request.getAttribute("exibirLiberar").equals("true")){%> --%>

 <!--  				<button type="submit" name="operacao" value="DevolverSelecionados"
					title="Devolver todos os mandados selecionados" onclick="AlterarValue('fluxo', 'devolver');">
					  <img src="imagens/22x22/ico_liberar.png" alt="Liberar Selecionadas" />  
					Devolver Selecionados
				</button>      -->
				
<%-- 				<%}%> --%>
				
<!-- 				<button type="submit" name="operacao" value="Consultar"	> -->
<!-- 					<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar" /> -->
<!-- 					Consultar Todas -->
<!-- 				</button> -->
<!-- 				<button type="submit" name="operacao" value="ConsultarReservadas"	> -->
<!-- 					<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar Reservadas" /> -->
<!-- 					Consultar Reservadas -->
<!-- 				</button> -->
<!-- 				<button type="submit" name="operacao" value="ConsultarPreAnalisadas"	> -->
<!-- 					<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar Pré-Analisadas" /> -->
<!-- 					Consultar Pré-Analisadas -->
<!-- 				</button> -->
		   	        
        
            <br> 
           
          
         
             <label class="formEdicaoLabel" for="tipoArquivo">*Motivo
			 </label><br> 
			 <select id="idMandTipoRedist"
							name="idMandTipoRedist" class="formEdicaoCombo">
							<!-- 	<option value=""></option>  -->
							<%
								List listTemp = (List) request.getAttribute("listaTipo");
									if (listTemp != null) {
										for (int i = 0; i < listTemp.size(); i++) {
											MandadoTipoRedistribuicaoDt = (MandadoTipoRedistribuicaoDt) listTemp.get(i);
							%>
							<option value="<%=MandadoTipoRedistribuicaoDt.getIdMandTipoRedist()%>"
								<%if (MandadoTipoRedistribuicaoDt.getIdMandTipoRedist() != null
								&& MandadoTipoRedistribuicaoDt.getIdMandTipoRedist().equals(request.getAttribute("idMandTipoRedist"))) {%>
								selected="selected" <%}%>>
							
							<%=MandadoTipoRedistribuicaoDt.getMandTipoRedist()%>
							</option>
							<%
								}
									}
							%>
			 </select><br>
			 </fildset>
			 <br><br> 
			 
			 
			 <button type="submit" name="operacao" value="DevolverSelecionados"
					title="Devolver todos os mandados selecionados" onclick="AlterarValue('fluxo', 'devolver');">
					<!--  <img src="imagens/22x22/ico_liberar.png" alt="Liberar Selecionadas" /> -->
					Devolver Selecionados
				</button>
			 
    	
     		<div id="divTabela" class="divTabela" >		
				<table id="Tabela" class="Tabela">
					<thead>
						<tr>
			           		<th class="colunaMinima">
			           			<input type="checkbox" id="selecionarTodos" onclick="atualizarChecksFiltro(this, 'Tabela', 'mandadoSelecionado[]')" />
			           		</th>
							<th class="colunaMinima lista_id">N&uacute;mero</th>
							<th>Tipo do Mandado</th>
							<th>Data Distribuição</th>
							<th>Data Limite</th>
							<th>Bairro</th>
							<th class="colunaMinima">Op&ccedil;&otilde;es</th>
						</tr>
					</thead>
	
					<%
					List lista = (List) request.getAttribute("listaMandados");
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
									MandadoJudicialDt mandadoDt = (MandadoJudicialDt)it.next();
								%>
								<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
									<td>
<%-- 										<%if (mandadoDt.isPodeLiberar()){%> --%>
												<input type="checkbox" name="mandadoSelecionado[]" value="<%=mandadoDt.getId()%>" />
<%-- 										<%}%> --%>
									</td>
									<td class="colunaMinima lista_id"><%=mandadoDt.getId()%></td>
									<td>
										<%=mandadoDt.getMandadoTipo()%>
									</td>
									<td class="lista_data">
										<%=Funcoes.FormatarData(mandadoDt.getDataDistribuicao())%>
									</td>
									<td class="lista_data"><%=Funcoes.FormatarData(mandadoDt.getDataLimite())%></td>
									<td><%=mandadoDt.getBairro()%></td>
									<td class="colunaMinima">
										<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=mandadoDt.getId()%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=">
											<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
										</a>
<%-- 										<%if (pendenciaDt.isPodeLiberar()){%> --%>

<%-- 											<%if(request.getAttribute("exibirLiberar") != null && request.getAttribute("exibirLiberar").equals("true")){%>										 --%>
<%-- 											<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=pendenciaDt.getId()%>&amp;opcao=<%=request.getAttribute("opcao")%>&amp;TipoPendencia=<%=request.getAttribute("TipoPendencia")%>&amp;Filtro=<%=request.getAttribute("Filtro")%>&amp;fluxo=1&amp;hash=<%=pendenciaDt.getHash()%>"> --%>
<!-- 												<img src="imagens/22x22/ico_liberar.png" alt="Liberar" title="Liberar a pend&ecirc;ncia" /> -->
<!-- 											</a> -->
<%-- 											<%}%> --%>
											
<%-- 										<%}%> --%>
									</td>
								</tr>						
								<%
								}
							}
						} else {
							%>
							<tr>
								<td colspan="10">N&atilde;o h&aacute; mandados</td>
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
