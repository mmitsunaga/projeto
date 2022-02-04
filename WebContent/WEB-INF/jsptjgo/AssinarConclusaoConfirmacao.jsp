<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises aguardando assinatura  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/checks.js'></script>
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>	 	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Assinar Pré-Análises aguardando assinatura </h2></div>
		<div id="divLocalizar" class="divLocalizar"> 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">			
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<%if(request.getAttribute("conteudoArquivos") != null){
// 					String nomeArquivosSemAspasDuplas = String.valueOf(request.getAttribute("nomeArquivos")).replaceAll("\"","ASPAS_DUPLAS");
					String conteudoArquivosSemAspasDuplas = String.valueOf(request.getAttribute("conteudoArquivos")).replaceAll("\"","ASPAS_DUPLAS").replaceAll("&","&amp;");
				%>
<%-- 					<input id="nomeArquivos" name="nomeArquivos" type="hidden" value="<%=nomeArquivosSemAspasDuplas%>" />				 --%>
					<input id="conteudoArquivos" name="conteudoArquivos" type="hidden" value="<%=conteudoArquivosSemAspasDuplas%>" />
					<input id="assinaAssincrono" name="assinaAssincrono" type="hidden" value="true" />
				<%}%>				
				
				<%@ include file="js/AssinarPendencia.js"%>	

				<div id="divTabela" class="divTabela" > 
					<table id="Tabela" class="Tabela">
						<thead id="CabecalhoTabela">
							<tr class="TituloColuna">
								<th width="3%"> </th>								
								<th width="23%">Processo</th>
								<th width="20%">Data Início</th>								
								<th width="20%">Data Pré-Análise</th>
								<th width="34%">Usuário Pré-Análise</th>																
							</tr>
						</thead>				
					<tbody>
						<%
						List liTemp = (List)request.getAttribute("ListaPreAnalises");
						long tipoClassificadortemp = -10;
						long tipoClassificador = 0;
						long tipoConclusaotemp = -10;
						long tipoConclusao = 0;
						ProcessoDt processoDt;
						String estiloLinha="TabelaLinha1";
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
							PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)liTemp.get(i);
							PendenciaDt pendenciaDt = pendenciaArquivoDt.getPendenciaDt();
							processoDt = pendenciaDt.getProcessoDt();
							tipoConclusao = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
							
							int j=i+1;
							
							if (processoDt.getId_Classificador().length() > 0){
								tipoClassificador = Funcoes.StringToLong(processoDt.getId_Classificador());
							} else tipoClassificador = 0;

							//Testa a necessidade de abrir uma linha para o tipo de conclusão
							if (tipoConclusaotemp == -10){
								tipoConclusaotemp = tipoConclusao;
							%>
							<tr>
								<th colspan="8" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							} else if (tipoConclusaotemp != tipoConclusao){
								tipoConclusaotemp = tipoConclusao;
								tipoClassificadortemp = -10;
							%>
							<tr>
								<th colspan="8" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							}//fim else
							 
							
							//Testa a necessidade de abrir uma linha para o tipo de classificador
							if (tipoClassificadortemp == -10){
								tipoClassificadortemp = tipoClassificador;	
							%>
							<tr>
								<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador().length()>0?processoDt.getClassificador():"Sem classificador")%> </th>
							</tr>
							<%
							}else if (tipoClassificadortemp != tipoClassificador){
								tipoClassificadortemp = tipoClassificador;
							%>		
							<tr>
								<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador().length()>0?processoDt.getClassificador():"Sem classificador")%> </th>
							</tr>
							<%
								} //fim else
							%>
							<tr class="<%=estiloLinha%>">
								<td align="center"><%=i+1 %></td>								
								<% 
									boolean boUrgente = pendenciaDt.isProcessoPrioridade();									
								%>
	                   			<td align="center">
	                   				<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getProcessoDt().getId()%>" title="<%=(boUrgente?pendenciaDt.getProcessoPrioridadeCodigoTexto():"")%>">
		                   				<%	if (boUrgente){ %>		 
			                   				<img src='./imagens/16x16/imgPrioridade<%=pendenciaDt.getNumeroImagemPrioridade()%>.png' alt="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>" title="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>"/>
			                   			<% } %>
			                   			<%=pendenciaDt.getProcessoDt().getProcessoNumero()%></a>
	                   				</a>
	                   			</td>	
	                   			<td align="center"><%=pendenciaArquivoDt.getPendenciaDt().getDataInicio()%></td>
	                   			<td align="center"><%=pendenciaArquivoDt.getDataPreAnalise()%></td>        			
	                   			<td><%=(pendenciaArquivoDt.getAssistenteResponsavel().length() > 0?pendenciaArquivoDt.getAssistenteResponsavel():pendenciaArquivoDt.getJuizResponsavel())%></td>
               				<% } 
               				}
               			%>
               			</tbody>
					</table>
					<br />							
					<%@ include file="Padroes/Assinador.jspf"%>
					<br />					
					<div id="divBotaoSalvar" style="display:none; text-align:center;">
						<input name="btnSalvar" id="btnSalvar" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');">
					</div>
				</div> 
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>  
	</div>
</body>
</html>