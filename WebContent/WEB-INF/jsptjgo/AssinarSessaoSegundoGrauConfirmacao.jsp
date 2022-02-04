<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.SessaoSegundoGrauProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pr�-An�lises aguardando assinatura  </title>
	
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
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Assinar Pr�-An�lises aguardando assinatura </h2></div>
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
								<th width="13%">Processo</th>
								<th width="30%">Sess�o</th>								
								<th width="20%">Data Pr�-An�lise</th>
								<th width="34%">Usu�rio Pr�-An�lise</th>																	
							</tr>
						</thead>				
					<tbody>
						<%
						List liTemp = (List)request.getAttribute("ListaSessoes");
						ProcessoDt processoDt;
						String estiloLinha="TabelaLinha1";
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
								SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt = (SessaoSegundoGrauProcessoDt)liTemp.get(i);
								AudienciaProcessoDt audienciaProcessoDt = sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt();
								processoDt = audienciaProcessoDt.getProcessoDt();
								AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
								PendenciaArquivoDt pendenciaArquivoDtRelatorioEVoto = sessaoSegundoGrauProcessoDt.getPendenciaArquivoDtRelatorioEVoto();
								PendenciaArquivoDt pendenciaArquivoDtEmenta = sessaoSegundoGrauProcessoDt.getPendenciaArquivoDtEmenta();
								if (processoDt == null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {									
									audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
								}
								%>
								<tr class="<%=estiloLinha%>">
									<td align="center"><%=i+1 %></td>									
									<td align="left">
										<%if (processoDt != null ) {%>
		                   				<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>">		                   				
				                   			<%=Funcoes.formataNumeroProcesso(audienciaProcessoDt.getProcessoDt().getProcessoNumero())%>	              							
				                   		</a>		                   				
		                   				<%} else if (audienciaProcessoFisicoDt != null) {%>
  	                   						<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
	                   					<%}else{%>
  	                   						-
 	                   					<%}%>
		                   			</td>	
		                   			<td align="left">
			                   			<%=audienciaProcessoDt.getAudienciaDt().getDataAgendada()%>
			                   			<%if(audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0){%>
	              							&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
	              						<%}%>
		                   			</td>
		                   			<td align="center"><%=pendenciaArquivoDtRelatorioEVoto.getDataPreAnalise()%></td>        			
		                   			<td><%=(pendenciaArquivoDtRelatorioEVoto.getAssistenteResponsavel().length() > 0?pendenciaArquivoDtRelatorioEVoto.getAssistenteResponsavel():pendenciaArquivoDtRelatorioEVoto.getJuizResponsavel())%></td>	                   			
		                   			
		                   		</tr>
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