<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
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
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#dataInicialFim").mask("99/99/9999");
			$("#dataFinalFim").mask("99/99/9999");
			$("#dataInicialInicio").mask("99/99/9999");
			$("#dataFinalInicio").mask("99/99/9999");
			
			$("#nomeBusca").focus();
		});
		
		//Preenche o campo filtro e clica no botao
		function filtrar(numero){
			$("#nomeBusca").val(numero);
			
			$("#formLocalizarBotao").trigger("click");
		}
	</script>
      
	<%@ include file="./js/Paginacao.js"%>	
	<%@ include file="js/PendenciaFinalizadasResponsavel.js"%>
	
</head>
<body>
  <div class="divCorpo">
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<div class="area"><h2>&raquo; Intima&ccedil;&otilde;es Lidas</h2></div>
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
		<input name="fluxo" type="hidden" value="7" />
		<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
		<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />
		
		<div id="divLocalizar" class="divLocalizar">
			<fieldset id="formLocalizar" class="formLocalizar"  > 
				<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtro</legend>
				
		       	<label id="formLocalizarLabel" class="formLocalizarLabel">Processo
		       	<input class="FormEdicaoimgLocalizar" type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeBusca', ''); return false;" title="Limpar o n&uacute;mero do processo" />
		       	</label><br>
		       	
		       	<input id="nomeBusca" class="formLocalizarInput" name="nomeBusca" type="text" value="<%=request.getParameter("nomeBusca") != null?request.getParameter("nomeBusca"):""%>" maxlength="60" />
		       	<br />		       	
				
				<div class="col30">
				<label class="formLocalizarLabel">Data Inicial Fim</label><br> 
				<input class="formLocalizarInput" name="dataInicialFim" id="dataInicialFim" type="text" value="" maxlength="60" title="Data inicial da publica&ccedil;&atilde;o" />
				<img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" 
						alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicialFim,'dd/mm/yyyy',this)" />
				</div>
				     
				     <div class="col30">
				<label class="formLocalizarLabel">Data Final Fim</label><br> 
				<input class="formLocalizarInput" name="dataFinalFim" id="dataFinalFim" type="text" value="" maxlength="60" title="Data final da publica&ccedil;&atilde;o" />
				<img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" 
					alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinalFim,'dd/mm/yyyy',this)" />
				</div>
				
				<div class="clear space"></div>				
								
				<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"
					onclick="buscaDados(0, <%=Configuracao.TamanhoRetornoConsulta %>); return false;"
					title="Consultar Intimações">
					
					Consultar
				</button>			       
			</fieldset>
	   
			<div id="divTabela" class="divTabela" > 
			   	<table id="Tabela" class="Tabela">
			       	<thead>
			           	<tr>
			           		<th class="colunaMinima"></th>
			           		<th class="colunaMinima">Num.</th>
			                <th>Processo</th>
			                <th>Movimenta&ccedil;&atilde;o</th>
			                <th width="150">Tipo</th>
			                <th width="85" align="center">Data Leitura</th>			                
			                <th width="85" align="center">Data Limite</th>
			                <th class="colunaMinima">Marcar</th>
			                <th class="colunaMinima">Detalhes</th>
						</tr>
					</thead>
					
					<%if (request.getAttribute("ListaPendencia") != null){
						List lista = (List)request.getAttribute("ListaPendencia");
						Iterator it = lista.iterator();
						int qtd = 0;
						%>
		
						<tbody id="tabListaDados">
							<%
							int contador = 1;
							while (it.hasNext()){
								PendenciaDt obj = (PendenciaDt)it.next();
							%>
								<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
									<td align ="center" ><%=contador%></td>
									<td class="colunaMinima lista_id"><%=obj.getId()%></td>
									<td width="160" align="center">
										<%if (!obj.getId_Processo().equals("")){%>
											<a href="BuscaProcesso?Id_Processo=<%=obj.getId_Processo()%>">
												<%=obj.getProcessoNumero()%>
											</a>
										<%}%>
									</td>
									<td><%=obj.getMovimentacao()%></td>
									<td><%=obj.getPendenciaTipo()%></td>
									<td class="lista_data" width="85"><%=obj.getDataFim()%></td>
									<%if (obj.getDataLimite() != null && !obj.getDataLimite().equals("")) {%>
										<td class="lista_data" width="85"><%=obj.getDataLimite()%></td>
									<%} else { %>
										<td align="center"> - </td>
									<%} %>
									<td class="colunaMinima">
									    <%
									    	if (obj.getDataVisto() != null && !obj.getDataVisto().equals("") 
									    		&& (obj.getCodigoTemp() != null && !obj.getCodigoTemp().equalsIgnoreCase(String.valueOf(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP)))) {
									    %>
											<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>&amp;finalizada=true" 
												title="Marcar Aguardando Parecer/Peticionamento">
												<img src="imagens/22x22/btn_movimentar.png" alt="Marcar Aguardando Parecer/Peticionamento" />
											</a>
										<%
											} else {
										%>
										<a href="DescartarPendenciaProcesso?PaginaAtual=<%=Configuracao.Curinga7%>&amp;pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>&amp;finalizada=false" 
												title="Marcar Aguardando Parecer/Peticionamento">
												<img src="imagens/22x22/btn_movimentar.png" alt="Marcar Aguardando Parecer/Peticionamento" />
											</a>
										<%
											}
										%>
									</td>
									<td class="colunaMinima">
									  <%
									  	if (obj.getDataVisto() != null && !obj.getDataVisto().equals("")
									  		&& (obj.getCodigoTemp() != null && !obj.getCodigoTemp().equalsIgnoreCase(String.valueOf(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP)))) {
									  %>
										<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>&NovaPesquisa=true&amp;finalizada=true" 
											title="Ver detalhes da pend&ecirc;ncia n&uacute;mero <%=obj.getId()%>">
											<img src="imagens/imgLocalizarPequena.png" alt="Selecionar" />
										</a>
									 <%} else { %>
									 	<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>&NovaPesquisa=true&amp;finalizada=false"
											title="Ver detalhes da pend&ecirc;ncia n&uacute;mero <%=obj.getId()%>">
											<img src="imagens/imgLocalizarPequena.png" alt="Selecionar" />
										</a>
									 <%} %>
									</td>
								</tr>
								<%contador +=1; %>
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
				
				<%@ include file="./Padroes/Paginacao.jspf"%>
			</div>
		</div>
		
		<%@ include file="Padroes/Mensagens.jspf"%>
  </form>
 </div>
</body>
</html>