<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
	<head>
		<title>Desmembrar Processo de Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript">
		function buscarParte(){
			AlterarValue('PaginaAtual', '8');
			AlterarValue('PassoEditar', '3');
			var form = document.forms[0];
			form.submit();
		}
		</script>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen'></link>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Desmembrar Processo de Execução Penal</h2></div>
			
			<form action="ProcessoExecucao" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
				<%@ include file="ProcessoExecucaoPassos.jspf" %>
				
				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados">
				      	<legend>Processo de Execução Penal Origem</legend>
				      	<br />
				      	<div style="width: 15%"> Número do Processo </div>
						<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumeroCompleto()%></a></span/><br />
						<input name="IdProcessoOrigem" id="IdProcessoOrigem" type="hidden" size="15" maxlength="15" value="<%=processoDt.getId_Processo()%>"/>
						<br />
					</fieldset>
					<br /><br />
					<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Processo de Execução Penal Destino</legend>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Selecione as Ações Penais</legend><br>
							<%
								List liTemp = (List) request.getSession().getAttribute("listaAcoesPenais");
								if (liTemp != null && liTemp.size() > 0) {
									
							%>
							<div id="divTabela" class="divTabela">
								<table id="Tabela" class="Tabela" width="50%">
									<thead>
										<tr>
											<th width="5%"></th>
											<th width="20%">Nº do Processo de Ação Penal</th>
											<th width="20%">Data do Trânsito em Julgado/ <br />Guia de Recolhimento Provisória</th>
										</tr>
									</thead>
									<tbody id="tabListaCondenacaoProcesso">
							<%
									ProcessoExecucaoDt objTemp = new ProcessoExecucaoDt();
									boolean boLinha = false;
									for (int i = 0; i < liTemp.size(); i++) {
										objTemp = (ProcessoExecucaoDt) liTemp.get(i);
							%>
									<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
										<td align="center"><input type="checkbox" name="ckAcaoPenalProcessoOrigem[]" id="ckAcaoPenalProcessoOrigem[]" value="<%=objTemp.getNumeroAcaoPenal()%>"></input></td>
										<td	align="center"><%=objTemp.getNumeroAcaoPenal()%></td>
										<td	align="center"%><%=objTemp.getDataTransitoJulgado()%></td>
									<% boLinha = !boLinha;
									}%>
								</tbody>
							</table>
						</div>
						<% }
						else {%>
						Não existe Ação Penal cadastrada para o Processo de Origem.
						<%} %>
					    </fieldset>	
					</fieldset>

					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','19');"> 
				    </div>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>