<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
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
      
</head>
<body>
   <div class="divCorpo">
		<form action="Pendencia" method="post" name="Formulario" id="Formulario">
			<div class="area"><h2>&raquo; Pend&ecirc;ncias Finalizadas</h2></div>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input name="fluxo" type="hidden" value="5" />
			<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
			<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />
			
			<%@ include file="./js/Paginacao.js"%>	
			<%@ include file="js//PendenciaFinalizadas.js"%>
			<%@ include file="Padroes/Mensagens.jspf"%>
			
			<div id="divLocalizar" class="divLocalizar">
				<fieldset id="formLocalizar" class="formLocalizar"  > 
					<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtrar por datas</legend>
					
			       	<label id="formLocalizarLabel" class="formLocalizarLabel">Processo
			       	<input class="FormEdicaoimgLocalizar" type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeBusca', ''); return false;" title="Limpar o n&uacute;mero do processo" />
			       	</label><br>
			       
			       	<input id="nomeBusca" class="formLocalizarInput" name="nomeBusca" type="text" value="<%=request.getParameter("nomeBusca") != null?request.getParameter("nomeBusca"):""%>" maxlength="60" />
			       		
			       	<br /> 
			       	
					<input type="hidden" id="Id_PendenciaTipo" name="Id_PendenciaTipo" value="<%=request.getAttribute("Id_PendenciaTipo")%>" />
					
					<label class="lbl">Tipo de Pend&ecirc;ncia
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
						onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
								AlterarValue('tempBuscaId', 'Id_PendenciaTipo'); 
								AlterarValue('tempBuscaDescricao', 'PendenciaTipo');" 
									title="Selecione a pend&ecirc;ncia" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_PendenciaTipo', 'PendenciaTipo'); return false;" 
									title="Limpar a Tipo da pend&ecirc;ncia" />
					</label><br>
					
					<input class="somenteLeitura" name="PendenciaTipo" id="PendenciaTipo" readonly type="text" 
						size="50" maxlength="50" value="<%=request.getAttribute("PendenciaTipo")%>" />	
					
					<br />
					
					<input type="hidden" id="Id_PendenciaStatus" name="Id_PendenciaStatus" value="<%=request.getAttribute("Id_PendenciaStatus")%>" />
					
					<label class="lbl">Status da Pend&ecirc;ncia
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarPendenciaStatus" type="image"  src="./imagens/imgLocalizarPequena.png" 
						onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
								AlterarValue('tempBuscaId', 'Id_PendenciaStatus'); 
								AlterarValue('tempBuscaDescricao', 'PendenciaStatus');" 
									title="Selecione a pend&ecirc;ncia" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaStatus" type="image"  src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_PendenciaStatus', 'PendenciaStatus'); return false;" 
									title="Limpar o status da pend&ecirc;ncia" />	
					</label><br>
					
					<input class="somenteLeitura" name="PendenciaStatus" id="PendenciaStatus" readonly type="text" 
						size="50" maxlength="50" value="<%=request.getAttribute("PendenciaStatus")%>" />
						
					<br />
					
					<label class="formLocalizarLabel">Data de In&iacute;cio</label><br> 
					<input class="formLocalizarInput" name="dataInicialInicio" id="dataInicialInicio" type="text" value="" maxlength="60" title="Data inicial do inicio" />
					<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
							alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicialInicio,'dd/mm/yyyy',this)" />
					     
					<label> a </label>
					<input class="formLocalizarInput" name="dataFinalInicio" id="dataFinalInicio" type="text" value="" maxlength="60" title="Data final do inicio" />
					<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
						alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinalInicio,'dd/mm/yyyy',this)" />
					<br />
					
					<label class="formLocalizarLabel">Data Fim</label><br> 
					<input class="formLocalizarInput" name="dataInicialFim" id="dataInicialFim" type="text" value="" maxlength="60" title="Data inicial da publica&ccedil;&atilde;o" />
					<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
							alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicialFim,'dd/mm/yyyy',this)" />
					     
					<label> a </label>
					<input class="formLocalizarInput" name="dataFinalFim" id="dataFinalFim" type="text" value="" maxlength="60" title="Data final da publica&ccedil;&atilde;o" />
					<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
						alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinalFim,'dd/mm/yyyy',this)" />
					<br />
					
			       	<input type="radio" name="filtroTipo" value="1" />Somente de processo
			       	<input type="radio" name="filtroTipo" value="2" />Somente normais
			       	<input type="radio" name="filtroTipo" value="3" checked />Todas
			       			       		
					<br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"
							onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;"
							title="Consultar as pendências">
						<!--	<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar publica&ccedil;&otilde;es" /> -->
							Consultar
						</button>
					</div>
								       
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
				                <th width="100" align="center">In&iacute;cio</th>
				                <th width="85" align="center">Fim</th>
				                <th width="85" align="center">Limite</th>
				                <th width="100">Status</th>
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
										<td class="lista_data"><%=obj.getDataInicio()%></td>
										<td class="lista_data"><%=obj.getDataFim()%></td>
										<td class="lista_data"><%=obj.getDataLimite()%></td>
										<td><%=obj.getPendenciaStatus()%></td>
										<td class="colunaMinima">
											<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>&amp;fluxo=17" 
												title="Ver detalhes da pend&ecirc;ncia n&uacute;mero <%=obj.getId()%>">
												<img src="imagens/imgLocalizarPequena.png" alt="Selecionar" />
											</a>
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
	  </form>
   </div>
</body>
</html>