<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<link href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
   	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<%@ include file="js/buscarArquivos.js"%>
	<script type='text/javascript' src='./js/tabelas.js'></script>
	<script type='text/javascript' src='./js/tabelaArquivos.js'></script>	
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
		
	<script type="text/javascript">
		$(document).ready(function(){
			$("#DataInicial").mask("99/99/9999");
			$("#DataFinal").mask("99/99/9999");
			
			$("#nomeBusca").focus();
		});
	</script>
	<%@ include file="./js/Paginacao.js"%> 
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises Finalizadas </h2></div>
		
		<div id="divLocalizar" class="divLocalizar"> 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<fieldset id="formLocalizar" class="formLocalizar">
					<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de Pré-Análises Finalizadas</legend>
			    
			    	<div class="col15">
			    	<label for="DataInicial">Data Inicial</label><br> 
			    	<input class="formLocalizarInput" name="DataInicial" id="DataInicial" type="text" size="10" maxlength="10" value="<%=AnalisePendenciadt.getDataInicial()%>"> 
			    	<img id="calendarioDataInicial" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)">			    	
			    	</div>
			    	<div class="col25">
			    	<label for="DataFinal">Data Final</label><br> 
			    	<input class="formLocalizarInput" name="DataFinal" id="DataFinal" type="text" size="10" maxlength="10" value="<%=AnalisePendenciadt.getDataFinal()%>"> 
			    	<img id="calendarioDataFinal" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)">
					</div>
					
					<div class="clear"></div>
					
					<label id="formLocalizarLabel" class="formLocalizarLabel"> Número do Processo </label><br> 
					<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=AnalisePendenciadt.getNumeroProcesso()%>" size="30" maxlength="60"><br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=AnalisePendenciadt.getFluxo()%>');" />
						<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Limpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=AnalisePendenciadt.getFluxo()%>');AlterarValue('PaginaAnterior','-1');" />
					</div>					
				</fieldset>
				<br />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<%
					List liTemp = (List)request.getAttribute("ListaPreAnalises");
					if (liTemp !=null){
				%>
				<div id="origem">
					<table class="Tabela" id="TabelaArquivos">
						<thead>
							<tr class="TituloColuna">
								<th>Processo</th>
								<th width="15%">Data Finalização</th>
								<th>Status</th>
								<th width="30%">Usuário</th>
								<th>Data Pré-Análise</th>
								<th width="15%">Ação</th>
							</tr>
						</thead>
						<tbody>
						<%
						long tipoConclusaotemp = -10;
						long tipoConclusao = 0;
						String estiloLinha="TabelaLinha1";
							for(int i = 0 ; i< liTemp.size();i++) {
							PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)liTemp.get(i);
							PendenciaDt pendenciaDt = pendenciaArquivoDt.getPendenciaDt();
							tipoConclusao = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
							
							int j=i+1;

							//Testa a necessidade de abrir uma linha para o tipo de conclusão
							if (tipoConclusaotemp == -10){
								tipoConclusaotemp = tipoConclusao;
							%>
							<tr class="linhaDestaque">
								<th colspan="7"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							} else if (tipoConclusaotemp != tipoConclusao){
								tipoConclusaotemp = tipoConclusao;
							%>
							<tr class="linhaDestaque">
								<th colspan="7"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							}//fim else
							%> 
							<tr class="<%=estiloLinha%>" align="center">
	                   			<td><a href="BuscaProcesso?Id_Processo=<%=pendenciaArquivoDt.getPendenciaDt().getId_Processo()%>"><%=pendenciaArquivoDt.getProcessoNumero()%></a></td>
	                   			<td><%= pendenciaDt.getDataFim()%></td>
								<td><%= pendenciaDt.getStatusPreAnalise()%></td>	                   			
	                   			<td><%= pendenciaDt.getUsuarioFinalizador()%></td>
	                   			<td><%= pendenciaArquivoDt.getDataPreAnalise()%></td>
	                   			<td>
	                   				<a href="PreAnalisarPendencia?PaginaAtual=<%=Configuracao.Editar%>&Id_Pendencia=<%=pendenciaDt.getId()%>">Visualizar</a>     
	                   			</td>
							</tr>
							<tr>
								<td colspan="6" id="pai_<%=pendenciaDt.getId()%>" class="Linha"></td>
							</tr>	                   		
               				<% } %>
               			</tbody>
               		</table>
				</div>
               	<%	}	%>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>  
	</div>
</body>
</html>