<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt" %>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>      	
	    <script type='text/javascript' src='./js/jquery.js'></script>
	    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>	
      	
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>" />
				<input id="ProcessoStatusCodigo" name="ProcessoStatusCodigo" type="hidden" value="<%=request.getAttribute("ProcessoStatusCodigo")%>" />				
				<input id="RedirecionaOutraServentia" name="RedirecionaOutraServentia" type="hidden" value="" />
			
				<div class="area"><h2>&raquo; Consulta de Processos </h2></div>
				<div id="divLocalizar" class="divLocalizar"> 
			
					<input type="hidden" id="Id_Processo" name="Id_Processo">
  	
  					<div align="left">
  						<% 	if (request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>
						<input name="imgMultipla" type="submit" value="Movimentação em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
						<% }if (request.getAttribute("podeRedistribuir").toString().equalsIgnoreCase("true")){ %>
						<input name="imgMultipla" type="submit" value="Redistribuição em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','2');"/>
						<% }if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
						<input name="imgMultipla" type="submit" value="Trocar Responsável" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','6');"/>
						<% }if (request.getAttribute("podeEncaminhar").toString().equalsIgnoreCase("true")){ %>
						<input name="imgMultipla" type="submit" value="Encaminhar Processos" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','8');"/>
						<% } %>
					</div>
				
					
					<div id="divTabela" class="divTabela"> 
    	<table id="Tabela" class="Tabela">
        	<thead>
            	<tr class="TituloColuna">
            		<td width="2%">n.</td>
            		<td class="colunaMinina">
            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
			    				title="Alterar os estados de todos os itens da lista" />
			    	</td>			    	
                  	<td width="16%">N&uacute;mero</td>
                  	<td width="642%">Partes</td>                  	
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
				if(liTemp!=null)
				//Percorre Lista Geral de Processos
				for(int i = 0 ; i< liTemp.size();i++) {
					boLinha=!boLinha;
					processoBuscaDt = (ProcessoDt)liTemp.get(i);

			%> 
					<% 
						String stUrgente = processoBuscaDt.getNumeroImagemPrioridade();
						String mensagemUrgente = processoBuscaDt.getProcessoPrioridadeCodigoTexto();
						
						%>
					<tr class="TabelaLinha<%=(boLinha?1:2)%>" onclick="submete('<%=processoBuscaDt.getId()%>')" > 
						<td align="center">
							<%=i+1%>
						</td>						  	    	  
						<td align="center">
							<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=processoBuscaDt.getId()%>">
						</td>
					  
	                   	<td >
		                   		<%	if (stUrgente.length()==1){ %>		 
		                   				<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
		                   		<% } %>
		                   		<%=processoBuscaDt.getProcessoNumero()%>
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
	  	                			<div class='coluna15'><b><%=promovente.getProcessoParteTipo()%> </b></div><div class='coluna80'><%=promovente.getNome()%> </div>  	                					  	                			
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
					  	<td><%= processoBuscaDt.getDataRecebimento()%></td>
					  		     	
	                   	<td align="center">
	                   		<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('Id_Processo','<%=processoBuscaDt.getId_Processo()%>');" >     
	                   	</td>
	                   	
               		</tr>
               		
					<%
       				
				}
			%>
           	</tbody>
       	</table>
   	</div> 

			</div>
				<%@ include file="Padroes/PaginacaoProcesso.jspf"%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body> 
</html>