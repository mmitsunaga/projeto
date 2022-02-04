<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Visualizar Pr�-An�lise  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
   	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>

 	<%@ include file="js/buscarArquivos.js"%>
 	<%@ include file="js/PendenciaPegar.js"%>
 	<%@ include file="./js/MovimentacaoProcesso.js" %>
</head>

<body onload="atualizarPendencias();">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualizar Pr�-An�lise </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend> Dados Pr�-An�lise</legend>
					
					<% PendenciaDt pendencia = AnalisePendenciadt.getPendenciaDt(); %>
					<br />
					<label class="formEdicaoLabel"> N�mero Processo </label><br>
					<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=pendencia.getId_Processo()%>"><%=pendencia.getProcessoNumero()%></a></span><br />

					<label class="formEdicaoLabel"> Data Pr�-An�lise </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getDataPreAnalise()%></span>
						
					<label> Usu�rio Respons�vel </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getUsuarioPreAnalise()%></span><br />
					
					<label class="formEdicaoLabel"> Status Pr�-An�lise </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getPendenciaDt().getStatusPreAnalise()%> por <%=AnalisePendenciadt.getPendenciaDt().getNomeUsuarioFinalizador() %></span><br />
						
					<label class="formEdicaoLabel"> Tipo </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getPendenciaDt().getPendenciaTipo()%></span>

					<label> Tipo Movimenta��o </label><br>
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
  									<td><%=arquivoPreAnalise.getArquivoDt().getArquivoTipo()%> </td>		  							
  									<td>
			      						<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=arquivoPreAnalise.getId()%>&amp;hash=<%=arquivoPreAnalise.getHash()%>">
					      					<%=arquivoPreAnalise.getArquivoDt().getNomeArquivoFormatado()%>
					      				</a>
					      			</td>
  								</tr>
	  						</tbody>
	  					</table>
					</fieldset>
					
					<fieldset class="formLocalizar"> 	
						<legend>Pend�ncia(s) a gerar</legend>
							<table class="Tabela">
								<thead>
							    	<tr>
							      		<th>Tipo</th>
							      		<th>Destinat�rio</th>
								  		<th style="display:none">Serventia/Usu�rio</th>
								  		<th>Prazo</th>
								  		<th class="colunaMinima">Urgente</th>
								  		<th style="display:none">on-line</th>
								  		<th class="colunaMinima">Pessoal/Advogados</th>
							    	</tr>
							  	</thead>
							  	<% if (AnalisePendenciadt.getListaPendenciasGerar() != null && AnalisePendenciadt.getListaPendenciasGerar().size() > 0){ %>
							  	<tbody id="corpoTabela">
							    	<tr id="identificador" style="display:none;">
							      		<td><span id="tableTipo">Tipo</span> </td>
							      		<td><span id="tableDestinatario">Destinat�rio</span></td>
							  	  		<td style="display:none"><span id="tableSerUsu">Usu�rio/Serventia</span></td>
							     		<td><span id="tablePrazo">Prazo</span></td>
								  		<td><span id="tableUrgente">Urgente</span></td>
							  	  		<td style="display:none"><span id="tableOnLine">Online</span></td>
							  	  		<td><span id="tablePessoalAdvogados">PessoalAdvogados</span></td>
							    	</tr>
							  	</tbody>
								<% } else { %>
								<tbody>
									<tr><td><em>Nenhuma Pend�ncia a ser gerada.</em></td></tr>
						  		</tbody>								    
								<% } %>								  	
							</table>
					</fieldset>
						
					<fieldset class="formLocalizar">
					<legend>Hist�rico </legend>
					<table class="Tabela">
						<thead>
    						<tr>
					      		<th width="15%">Data</th>
					      		<th width="15%">Tipo</th>
      							<th>Usu&aacute;rio Respons�vel</th>
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
										<td><%=(arquivo.getArquivoDt().getUsuarioAssinadorFormatado().equals("")?"PR�-AN�LISE":"AN�LISE")%>
							  			<td><%=pendencia.getNomeUsuarioFinalizador()%></td>
						      			<td>
						      				<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=arquivo.getId()%>&amp;hash=<%=arquivo.getHash()%>">
							      				<%=arquivo.getArquivoDt().getNomeArquivoFormatado()%>
							      			</a>
							      		</td>			  	  							    
						    		</tr>
						<% 		
									}
								} else { %>
								<tr><td colspan="4"><em>Pend�ncia est� em aberto.</em></td></tr>
						<%
						 		}
							} //fim for
  						} else { %>
  						<tr><td colspan="4"><em>Nenhum Hist�rico dispon�vel.</em></td></tr>
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
