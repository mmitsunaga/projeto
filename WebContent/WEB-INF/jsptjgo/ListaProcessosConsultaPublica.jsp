<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<html>
	<head>
		<title><%=request.getAttribute("TituloDaPagina")%></title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');	
		</style>
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<style type="text/css"> #bkg_projudi{ display:none } </style>
      	
      	<link rel="stylesheet" href="css/cupertino/jquery-ui-1.8.1.custom.css" type="text/css" media="all" />

	</head>

	<body class="fundo">
		<%@ include file="/CabecalhoPublico.html" %> 
  		<div id="divCorpo" class="divCorpo">
  			
			<form action="BuscaProcessoPublica" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="TipoConsulta" name="TipoConsulta" type="hidden" value="<%=request.getAttribute("TipoConsulta")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>" />
				<input id="ServletRedirect" name="ServletRedirect" type="hidden" value="<%=request.getAttribute("ServletRedirect")%>">
				<input id="TituloDaPagina" name="TituloDaPagina" type="hidden" value="<%=request.getAttribute("TituloDaPagina")%>">
				
				<%@ include file="Padroes/reCaptcha.jspf" %>
				
				<div class="area"><h2>&raquo; <%=request.getAttribute("TituloDaPagina")%> </h2></div>
				<div id="divLocalizar" class="divLocalizar"> 
				   
				   	<input type="hidden" id="Id_Processo" name="Id_Processo">
					
					<div id="divTabela" class="divTabela"> 
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				                  	<td width="20%">N&uacute;mero</td>
				                  	<td width="70%">Partes</td>				                  
				                  	<td width="10%">Distribuição</td>
				                  	<td class="colunaMinima">Selecionar</td>
				               	</tr>				               	
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								List liTemp = (List)request.getAttribute("ListaProcesso");
								ProcessoDt processoBuscaDt;
								ProcessoParteDt processoParteDt;			
								String processoNumero="";
								boolean boLinha=false;
								//Percorre Lista Geral de Processos
								for(int i = 0 ; i< liTemp.size();i++) {
									processoBuscaDt = (ProcessoDt)liTemp.get(i); 
							%> 
									<tr class="TabelaLinha<%=(boLinha?1:2)%>" onclick="<%=!processoBuscaDt.isSegredoJustica()%>?submete('<%=processoBuscaDt.getId()%>'):''">
									
								   	<td >
				                   		<%=processoBuscaDt.getProcessoNumero()%>
				                   		<% if (Funcoes.StringToInt(processoBuscaDt.getProcessoStatusCodigo()) == ProcessoStatusDt.ARQUIVADO){ %>
				                   			<font color="red"> (ARQUIVADO) </font> 
				                   		<% } %>
								  	</td>					                   
				                   	<td>
				                   		<%if(processoBuscaDt.isBuscaParte()){ %>
					                   		 <div class='coluna100'>					  								  	    
									  	     	<div class='coluna15'><b><%=processoBuscaDt.getParteTipoBusca()%> </b></div><div class='coluna80'><%=processoBuscaDt.getNomeParteBuscaAbreviado()%></div>						  	
									  		</div>
								  		<%} %>
								  		<%
								  			ProcessoParteDt promovente = processoBuscaDt.getPrimeiroPoloAtivo();
								  								 	  	if(promovente!=null && !processoBuscaDt.isMesmaParteBusca(promovente.getId())){
								  		%>					 	  	
					  	            		<div class='coluna100'>  	                		
				  	                			<div class='coluna15'><b><%=promovente.getProcessoParteTipo()%></b> </div><div class='coluna80'><%=promovente.getNome()%> </div>  	                					  	                			
				  	                		</div>
			  	                		<%} %>
								 	  	<%
								 	  		ProcessoParteDt promovido = processoBuscaDt.getPrimeiroPoloPassivo();
								 	  							 	  	if(promovido!=null && !processoBuscaDt.isMesmaParteBusca(promovido.getId())){
								 	  	%>					 	  	
					  	            		<div class='coluna100'>  	                		
				  	                			<div class='coluna15'><b><%=promovido.getProcessoParteTipo()%> </b></div><div class='coluna80'><%=promovido.getNome()%> </div>  	                					  	                			
				  	                		</div>
			  	                		<%} %>
			  	                	</td>
								  	<td ><%= processoBuscaDt.getDataRecebimento()%></td>
								  	<%if (!processoBuscaDt.isSegredoJustica()) { %>  	
				                   	<td align="center">
				                   		<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('Id_Processo','<%=processoBuscaDt.getId_Processo()%>');" >     
				                   	</td>
				                   	<% } else { %>
				                   		<td></td>
				                   	<% } %>
				               		</tr>

				               		<%if (processoBuscaDt.isSegredoJustica()) { %>  	
						               		<tr id="segredojus<%=processoBuscaDt.getId()%>"> 
						               			<td colspan="5" style="color:#1d4875;text-align: justify;"><div>Processo em segredo de justiça. Para maiores informações, comparecer à Serventia <b> "<%=processoBuscaDt.getServentia()%>" </b> com o(s) documento(s) que comprove(m) parte no processo.</div></td> 
						               		</tr>
							        <% }
								}
							%>
				           	</tbody>
				       	</table>
				   	</div> 
				</div> 
				<%@ include file="Padroes/PaginacaoConsultaPublica.jspf"%>
			</form> 
		</div>
	</body>
</html>
