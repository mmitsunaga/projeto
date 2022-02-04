<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoFaseDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoAssuntoDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Coverter Processo em Recurso  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Converter Recurso em Processo</h2></div>
		<form action="CoversaoProcessoRecurso" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Dados do Recurso </legend>

					<label class="formEdicaoLabel"> Recurso: </label><br>
					<span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
					<br /><br />
	
				<% List listaRecorrentes = processoDt.getRecursoDt().getListaRecorrentes();
			   	    if (listaRecorrentes != null && listaRecorrentes.size()>0){ %>
						<!-- RECORRENTES -->
					 	<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Recorrente(s) </legend>
					   	<%   		
					   	    	for (int i=0; i<listaRecorrentes.size();i++){
					   			  	RecursoParteDt recursoParteDt = (RecursoParteDt)listaRecorrentes.get(i);
					   			  	ProcessoParteDt parteDt = recursoParteDt.getProcessoParteDt();
					   	%>
					       	<div> Nome </div> <span class="span1"><%=parteDt.getNome()%></span>
					       	<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
					 		<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
						  		<fieldset class="fieldsetEndereco">
						  			<legend> Endereço </legend>
									<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
					    			<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
					    			<%=parteDt.getEnderecoParte().getCep() + " " + parteDt.getEMail() + " " + parteDt.getTelefone() %>
					    		</fieldset>
							</div>
					
					       	<%}%>   		   	
						</fieldset>
				<%}%>
				<%List listaRecorridos = processoDt.getRecursoDt().getListaRecorridos();
					if (listaRecorridos != null && listaRecorridos.size()>0){ %>
						<!-- RECORRIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
					   		<legend> Recorrido(s) </legend>
					   	<%
					   		
					   			for (int i=0; i<listaRecorridos.size();i++){
					   				RecursoParteDt recursoParteDt = (RecursoParteDt)listaRecorridos.get(i);
					   				ProcessoParteDt parteDt = recursoParteDt.getProcessoParteDt();
					   	%>
					    	<div> Nome </div> <span class="span1"><%=parteDt.getNome()%></span>
					       	<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
					  		<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
						  		<fieldset class="fieldsetEndereco">
						  			<legend> Endereço </legend>
									<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
						    		<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
						    		<%=parteDt.getEnderecoParte().getCep() + " " + parteDt.getEMail() + " " + parteDt.getTelefone() %>
					    		</fieldset>
							</div>
					
					       	<%}%>
					   	</fieldset> 
				<%}%>

					     	
			    	<fieldset id="VisualizaDados" class="VisualizaDados">
			    		<legend> Outras Informações </legend>
			    		
				   		<div> Serventia Recurso </div>
						<span class="span1"><%=  processoDt.getRecursoDt().getServentiaRecurso()%></span><br />
						
						<div> Serventia Origem </div>
						<span class="span1"><%=  processoDt.getRecursoDt().getServentiaOrigem()%></span><br />
						
				   		<div> Classe </div>
			    	  	<span style="width: 500px;"><%=processoDt.getRecursoDt().getProcessoTipo()%> </span><br />
			    	  	
			    	  	<%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
			    	  	
			    	  	<% if (!processoDt.getValor().equals("Null")){ %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
						<%} %>
						
			    	  	<div> Processo Originário </div>
						<span class="span2"><a href="BuscaProcesso?ProcessoOutraServentia=true&Id_Processo=<%=processoDt.getId_ProcessoPrincipal()%>"><%=processoDt.getProcessoNumeroPrincipal()%></a></span/><br />	
						
						<div> Fase Processual</div>	
						<span class="span1"><%=processoDt.getProcessoFase()%></span>
									
						<div> Classificador </div>
					    <span class="span2">
					    <% if (UsuarioSessao.isPodeVisualizarClassificador()){ %>
					   		<%= processoDt.getClassificador()%>
					   	<% } %>
					    </span>
					    <br />
					    
	   					<div> Data Autuação </div>
						<span class="span1"><%=processoDt.getRecursoDt().getDataRecebimento()%></span>	
					    
						<div> Segredo de Justi&ccedil;a</div>
						<span class="span2"><%=(processoDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span>
						<br />
						
						<div> Status </div>
						<span class="span2"><%=processoDt.getProcessoStatus()%></span>
						<br />

						<div> Prioridade</div>
						<span class="span1"><%=processoDt.getProcessoPrioridade()%></span>
			    		
				</fieldset>
				
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
				</div>

		   </fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
		</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>