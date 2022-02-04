<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Visualizar Pré-Análise  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
   	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/tabelas.js'></script>
	<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
	      
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualizar Pré-Análise </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend> Dados Pré-Análise</legend>
					
					<% PendenciaDt pendencia = AnalisePendenciadt.getPendenciaDt(); %>
					<br />
					<label class="formEdicaoLabel"> Número Processo </label><br>
					<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=pendencia.getId_Processo()%>"><%=pendencia.getProcessoNumero()%></a></span><br />

					<label class="formEdicaoLabel"> Data Pré-Análise </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getDataPreAnalise()%></span>
						
					<label class="formEdicaoLabel"> Status Pré-Análise </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getPendenciaDt().getStatusPreAnalise()%> por <%=AnalisePendenciadt.getPendenciaDt().getNomeUsuarioFinalizador() %></span><br />
						
					<label class="formEdicaoLabel"> Tipo Pendência </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getPendenciaDt().getPendenciaTipo()%></span>

					<label> Tipo Movimentação </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getMovimentacaoTipo()%></span><br /><br />
					
					<fieldset class="formLocalizar"> 	
						<legend>Documento redigido</legend>
						<table class="Tabela">
							<thead>
    							<tr>
					      			<th>Tipo Arquivo</th>
      								<th>Anexo</th>
    							</tr>
  							</thead>
	  						<tbody>
	  							<tr>
	  							<% PendenciaArquivoDt arquivoPreAnalise = AnalisePendenciadt.getArquivoPreAnalise(); %>
	  								<%if (arquivoPreAnalise != null) { %>
	  									<td><%=arquivoPreAnalise.getArquivoDt().getArquivoTipo()%> </td>		  							
	  									<td>
				      						<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=arquivoPreAnalise.getId()%>&amp;hash=<%=arquivoPreAnalise.getHash()%>">
						      					<%=arquivoPreAnalise.getArquivoDt().getNomeArquivoFormatado()%>
						      				</a>
						      			</td>
					      			<%} %>
  								</tr>
	  						</tbody>
	  					</table>
					</fieldset>
					
					<fieldset class="formLocalizar">
					<legend>Histórico Pendência</legend>
					<table class="Tabela">
						<thead>
    						<tr>
					      		<th width="15%">Data</th>
					      		<th width="15%">Tipo</th>
      							<th>Usu&aacute;rio Responsável</th>
      							<th>Anexo(s)</th>
    						</tr>
  						</thead>
	  					<tbody>
	  					<%
						List listaHistorico = AnalisePendenciadt.getHistoricoPendencia();
  						if (listaHistorico != null && listaHistorico.size() > 0){
							for (int i=0; i< listaHistorico.size();i++){
								PendenciaDt obj = (PendenciaDt)listaHistorico.get(i);
										
								List arquivosResposta = obj.getListaArquivos();
		  						if (arquivosResposta != null && arquivosResposta.size() > 0){
									for (int j=0; j< arquivosResposta.size();j++){
										PendenciaArquivoDt arquivo = (PendenciaArquivoDt)arquivosResposta.get(j);
									%>
									<tr> 
										<td><%=arquivo.getArquivoDt().getDataInsercao()%></td>
										<td><%=(arquivo.getArquivoDt().getUsuarioAssinadorFormatado().equals("")?"PRÉ-ANÁLISE":"ANÁLISE")%>
							  			<td><%=pendencia.getNomeUsuarioFinalizador()%></td>
						      			<td>
						      				<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=arquivo.getId()%>&amp;hash=<%=arquivo.getHash()%>&amp;finalizado=true">
							      				<%=arquivo.getArquivoDt().getNomeArquivoFormatado()%>
							      			</a>
							      		</td>			  	  							    
						    		</tr>
						<% 		
									}
								} else { %>
								<tr><td colspan="4"><em>Pendência está em aberto.</em></td></tr>
						<%
						 		}
							} //fim for
  						} else { %>
  						<tr><td colspan="4"><em>Nenhum Histórico disponível.</em></td></tr>
  						<% } %>
	  					</tbody>
					</table>
				</fieldset>
			</fieldset>
		</div>
		</form>
	</div>
 
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
