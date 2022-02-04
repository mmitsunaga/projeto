<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Análises </title>
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
	
	<link href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/tabelas.js'></script>
	<script type='text/javascript' src='./js/tabelaArquivos.js'></script>	
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js'></script>
	<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
		
	<script type="text/javascript">
		$(document).ready(function(){
			$("#DataInicial").mask("99/99/9999");
			$("#DataFinal").mask("99/99/9999");
			
			$("#nomeBusca").focus();
		});
	</script>

	<%@ include file="js/Paginacao.js"%> 
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Análises Efetuadas </h2></div>
		<div id="divLocalizar" class="divLocalizar" > 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<fieldset id="formLocalizar" class="formLocalizar"> 
				
					<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de Análises Efetuadas</legend>
					
					<div class="col15">
			    	<label for="DataInicial">Data Inicial</label><br> 
			    	<input class="formLocalizarInput" name="DataInicial" id="DataInicial" type="text" size="10" maxlength="10" value="<%=AnalisePendenciadt.getDataInicial()%>" /> 
			    	<img id="calendarioDataInicial" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" />			    	
					</div>
					
					<div class="col15">
			    	<label for="DataFinal">Data Final</label><br> 
			    	<input class="formLocalizarInput" name="DataFinal" id="DataFinal" type="text" size="10" maxlength="10" value="<%=AnalisePendenciadt.getDataFinal()%>" /> 
			    	<img id="calendarioDataFinal" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" />
					</div>
					
					<div class="clear"></div>
					
					<label id="formLocalizarLabel" class="formLocalizarLabel"> Número do Processo </label><br> 
					<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=AnalisePendenciadt.getNumeroProcesso()%>" size="30" maxlength="60" />
					<br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=AnalisePendenciadt.getFluxo()%>');" />
						<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Limpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=AnalisePendenciadt.getFluxo()%>');AlterarValue('PaginaAnterior','-1');" />
					</div>
					
				</fieldset>
				<br />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<div id="origem">
					<table class="Tabela" id="TabelaArquivos">
						<thead>
							<tr class="TituloColuna">
								<th width="18%">Processo</th>
								<th width="15%">Data Início</th>
								<th width="15%">Data Análise</th>
								<th>Usuário</th>
								<th>Ação</th>
							</tr>
						</thead>
						<tbody>
						<%
						List liTemp = (List)request.getAttribute("ListaConclusoes");
						long tipoConclusaotemp = -10;
						long tipoConclusao = 0;
						String estiloLinha="TabelaLinha1";
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
							PendenciaDt pendenciaDt = (PendenciaDt)liTemp.get(i);
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
	                   			<td><a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a></td>
	                   			<td><%= pendenciaDt.getDataInicio()%></td>
	                   			<td><%= pendenciaDt.getDataFim()%></td>
	                   			<td><%= pendenciaDt.getUsuarioFinalizador()%></td>
	                   			<td>
	                   				<a href="AnalisarConclusao?PaginaAtual=<%=Configuracao.Editar%>&Id_Pendencia=<%=pendenciaDt.getId()%>">Visualizar</a>     
	                   			</td>
							</tr>
							<tr>
								<td colspan="6" id="pai_<%=pendenciaDt.getId()%>" class="Linha"></td>
							</tr>	                   		
               				<% } 
               				}
               			%>
               			</tbody>
					</table>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>  
	</div>
</body>
</html>
