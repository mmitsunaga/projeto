 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Visualizar An�lise  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
	      
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualiza��o de An�lise Efetuada </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			
			<fieldset id="VisualizaDados" class="VisualizaDados">
				<legend> Dados An�lise de Conclus�o </legend>
				<% PendenciaDt pendencia = AnalisePendenciadt.getPendenciaDt(); %>

				<div> N�mero Processo </div>
				<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=pendencia.getId_Processo()%>"><%=pendencia.getProcessoNumero()%></a></span>
				<br>
					
				<div> Tipo Conclus�o </div> 
				<span class="spanDestaque"><%=pendencia.getPendenciaTipo()%></span>
				<br>
					
				<div> Data In�cio </div>
				<span class="spanDestaque"><%=pendencia.getDataInicio()%></span>
				<br>
				
				<div> Data An�lise </div>
				<span class="spanDestaque"><%=pendencia.getDataFim()%></span>
				<br>
					
				<div> Tempo </div>
				<span class="spanDestaque"><%=Funcoes.diferencaDatasFormatado(pendencia.getDataFim(), pendencia.getDataInicio())%></span>
				<br />
					
				<div class="formEdicaoLabel"> Respons�vel(is) </div>
				<span class="spanDestaque"><%=pendencia.getNomeUsuarioFinalizador()%></span>
				<br />
				
				<div class="formEdicaoLabel"> Assessore(s) </div>
				<%
				for (int i=0; i< AnalisePendenciadt.getHistoricoPendencia().size();i++){
					PendenciaArquivoDt obj = (PendenciaArquivoDt)AnalisePendenciadt.getHistoricoPendencia().get(i);
				%>
					<span class="spanDestaque"> <%if(i>0){%>;&nbsp;<%}%><%=obj.getAssistenteResponsavel()%></span>
				<%} %>
				<br />
							
				<fieldset class="formLocalizar">
					<legend>Arquivo(s) inserido(s)</legend>
					<table class="Tabela">
						<thead>
	    					<tr>
					      		<th width="30%">Descri&ccedil;&atilde;o</th>
	      						<th>Usu&aacute;rio Assinador</th>
	      						<th>Nome do Arquivo</th>
	    					</tr>
	  					</thead>
	  					<%
	  					int contPreAnalise = 0;
						List lista = pendencia.getListaArquivos();
	  					if (lista != null && lista.size() > 0){
						%>
		  				<tbody>
							<%
							for (int i=0; i< lista.size();i++){
								PendenciaArquivoDt obj = (PendenciaArquivoDt)lista.get(i);
								if (!obj.getArquivoDt().getUsuarioAssinador().equals("")){
							%>
							<tr> 
					  			<td><%=obj.getArquivoDt().getArquivoTipo()%></td>
					  			<td><%=obj.getArquivoDt().getUsuarioAssinadorFormatado()%></td>
				      			<td>
				      				<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=obj.getId()%>&amp;hash=<%=obj.getHash()%>">
					      				<%=obj.getArquivoDt().getNomeArquivoFormatado()%>
					      			</a>
					      		</td>			  	  							    
				    		</tr>
						<% 	} 
						} %>
		  				</tbody>
						<%} else { %>
						<tbody>
							<tr><td colspan="5">N&atilde;o h&aacute; arquivos</td></tr>
						</tbody>
						<%}%>
					</table>
				</fieldset>
					
				<fieldset class="formLocalizar">
					<legend>Pend�ncia(s) gerada(s)</legend>
					<table class="Tabela">
						<thead>
	    					<tr>
					      		<th width="30%">Descri&ccedil;&atilde;o</th>
	      						<th>Destinat�rio</th>
	      						<th>Prazo</th>
	      						<th>Urg�ncia</th>
	    					</tr>
	  					</thead>
	  					<%
						List listaPendencias = AnalisePendenciadt.getListaPendenciasGerar();
	  					if (listaPendencias != null && listaPendencias.size() > 0){
						%>
		  				<tbody>
							<%
							for (int i=0; i< listaPendencias.size();i++){
								PendenciaDt obj = (PendenciaDt)listaPendencias.get(i);
							%>
							<tr> 
					  			<td><%=obj.getPendenciaTipo()%></td>
					  			<td><%=obj.getNomeParte()%></td>
					  			<td><%=obj.getPrazo()%></td>
					  			<td><%=obj.getProcessoPrioridade()%></td>
				    		</tr>
							<% } %>
		  				</tbody>
						<%} else { %>
						<tbody>
							<tr><td colspan="5"><em>Nenhuma pend�ncia gerada.</em></td></tr>
						</tbody>
						<%}%>
					</table>
				</fieldset>
					
				<fieldset class="formLocalizar">
					<legend>Pr�-An�lise(s) Registrada(s)</legend>
					<table class="Tabela">
						<thead>
	    					<tr>
					      		<th>C�digo</th>
					      		<th width="15%">Data Pr�-An�lise</th>
					      		<th>Usu&aacute;rio Pr�-An�lise</th>
					      		<th>Status</th>
	      						<th>Anexo(s)</th>
	    					</tr>
	  					</thead>
		  				<tbody>
		  				<!-- Hist�rico de pr�-analises registradas -->
	  					<%
						List listaHistorico = AnalisePendenciadt.getHistoricoPendencia();
	  					if (listaHistorico != null && listaHistorico.size() > 0){
							for (int i=0; i< listaHistorico.size();i++){
								PendenciaArquivoDt obj = (PendenciaArquivoDt)listaHistorico.get(i);
								contPreAnalise++;
						%>
							<tr> 
								<td class="colunaMinima"><%=obj.getPendenciaDt().getId()%></td>
								<td><%=obj.getPendenciaDt().getDataFim()%></td>
						  		<td><%=(!obj.getAssistenteResponsavel().equals("")?obj.getAssistenteResponsavel():obj.getJuizResponsavel())%></td>
								<td> <%=obj.getPendenciaDt().getStatusPreAnalise()%> por <%=obj.getPendenciaDt().getNomeUsuarioFinalizador() %> </td>
								<td>
					      			<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=obj.getId()%>&amp;hash=<%=obj.getHash()%>&amp;finalizado=true">
						      			<%=obj.getArquivoDt().getNomeArquivoFormatado()%>
						      		</a>
						      	</td>	
							</tr>
						<% } %>
		  				</tbody>
						<%} %>
						<tfoot>
							<tr><td colspan="7">Quantidade de Pr�-An�lises registradas: <%=contPreAnalise%></td></tr>
						</tfoot>
					</table>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>