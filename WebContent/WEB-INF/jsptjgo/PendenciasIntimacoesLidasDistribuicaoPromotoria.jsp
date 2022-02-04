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
	<%@ include file="js/PendenciasIntimacoesLidasDistribuicaoPromotoria.js"%>
	
</head>
<body>
  <div class="divCorpo">
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<div class="area"><h2>&raquo;Intima&ccedil;&otilde;es Lidas Distribu&ccedil;&atilde;o</h2></div>
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
		<input name="fluxo" type="hidden" value="7" />
		<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
		<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />
		
		<div id="divLocalizar" class="divLocalizar">
			<fieldset id="formLocalizar" class="formLocalizar"  > 
				<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtro</legend>
				
				<label id="formLocalizarLabel" class="formLocalizarLabel">Processo
					<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeBusca', ''); return false;" title="Limpar o n&uacute;mero do processo" />
				</label><br>
		       
		       	<input id="nomeBusca" class="formLocalizarInput" name="nomeBusca" type="text"   maxlength="60" size="17" value="<%=request.getParameter("nomeBusca") != null?request.getParameter("nomeBusca"):""%>" />
		       	<br />		
				
				<label class="formLocalizarLabel">Data da Intimação</label><br> 
				<input class="formLocalizarInput" name="dataInicialInicio" id="dataInicialInicio" type="text" value="" maxlength="60" title="Data inicial do inicio" />
				<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
					alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicialInicio,'dd/mm/yyyy',this)" />
					     
				<label>a</label> 
				<input class="formLocalizarInput" name="dataFinalInicio" id="dataFinalInicio" type="text" value="" maxlength="60" title="Data final do inicio" />
				<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
					alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinalInicio,'dd/mm/yyyy',this)" />
				<br />
				
				<div id="divConfirmarSalvar" class="ConfirmarSalvar">	
					<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"
						onclick="buscaDados(0, <%=Configuracao.TamanhoRetornoConsulta %>); return false;"
						title="Consultar as pendências">
						
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
				           		<th width="160">Tipo</th>
				                <th width="130">Processo</th>
				                <th align="center">Movimenta&ccedil;&atilde;o</th>
				                <th width="150" align="center">Data</th>
				                <th width="150" align="center">Data Leitura</th>
				                <th class="colunaMinima">Redistribuir</th>
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
										<td align="center"><%=obj.getPendenciaTipo()%></td>
										<td width="130" align="center">
											<%if (!obj.getId_Processo().equals("")){%>
												<a href="BuscaProcesso?Id_Processo=<%=obj.getId_Processo()%>">
													<%=obj.getProcessoNumero()%>
												</a>
											<%}%>
										</td>
										<td align="center"><%=obj.getMovimentacao()%></td>
										<td align="center" width="150"><%=obj.getDataInicio()%></td>
										<td align="center" width="150"><%=obj.getDataFim()%></td>
										<td class="colunaMinima">
											<a href="PendenciaResponsavel?PaginaAtual=<%=Configuracao.Novo%>&amp;pendencia=<%=obj.getId()%>&amp;CodigoPendencia=<%=obj.getHash()%>">
												<img src="imagens/22x22/btn_encaminhar.png" alt="Distribuir" title="Efetuar troca de responsável" />
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
		
		<%@ include file="Padroes/Mensagens.jspf"%>
  </form>
 </div>
</body>
</html>