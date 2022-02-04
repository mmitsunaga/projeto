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
		<div class="area"><h2>&raquo; Converter Processo em Recurso</h2></div>
		<form action="CoversaoProcessoRecurso" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Dados do Processo </legend>

					<label class="formEdicaoLabel"> Processo: </label><br>
					<span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
					<br /><br />
	
						<!-- PROMOVENTES -->
					 	<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Polo Ativo </legend>
					   	<%
					   		List listaPromoventes = processoDt.getListaPolosAtivos();
					   				   	    if (listaPromoventes != null){
					   				   	    	for (int i=0; i<listaPromoventes.size();i++){
					   				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);   			 	 
					   				   				// Se existirem apenas 6 partes vinculadas são exibidas todas, caso seja um número superior serão exibidas apenas as 5 primeiras
					   					       		if ((i >= Configuracao.QtdPartesDadosProcesso) && (listaPromoventes.size() != (Configuracao.QtdPartesDadosProcesso + 1))){
					   	%>
						       			<div> </div>	       									       				       			 
						       			<span class="span1">
						       				<ul><li>
						       						<a href="javascript:mostrarTodasPartes()">
													e outros
													</a>
											</li></ul>	       				
						       			</span>	       			
						       		<%break;
						       		}%>
						       	<div> Nome </div> 
						       	<span class="span1">
						       		<%=parteDt.getNome()%>
						       		<% if (!parteDt.getAusenciaProcessoParte().equals("")){ %> <font color="red"><%=parteDt.getAusenciaProcessoParte()%></font> <%} %>
						       	</span>
						       	
						       	<div> CPF </div> 
						       	<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
						       	<br />
						 		
						 		<div> Filia&ccedil;&atilde;o </div>
						       	<span class="span1"><%=parteDt.getNomeMae()%></span>
						       	
						       	<div> Data de Nascimento </div> 
						       	<span class="span2"><%=parteDt.getDataNascimento()%></span>
						       	
						       	<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
							  		<fieldset class="fieldsetEndereco">
							  			<legend> Endereço </legend>
										<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
							    		<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
							    		<%=parteDt.getEnderecoParte().getCep()%><br />
								    	<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	   		    		
									</fieldset>
								</div>
								
								<% } %>
								<br />
						       	<div style="width:100%; margin:3px 0"></div>
					       	<%
					       		}
					   		%>
						</fieldset>
						
						<!-- PROMOVIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
					   		<legend> Polo Passivo </legend>
					   	<%
					   		List listaPromovidos = processoDt.getListaPolosPassivos();
					   				   		if (listaPromovidos != null){
					   				   			for (int i=0; i<listaPromovidos.size();i++){
					   				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					   				   				// Se existirem apenas 6 partes vinculadas são exibidas todas, caso seja um número superior serão exibidas apenas as 5 primeiras
					   					       		if ((i >= Configuracao.QtdPartesDadosProcesso) && (listaPromovidos.size() != (Configuracao.QtdPartesDadosProcesso + 1))){
					   	%>
						       			<div> </div>	       									       				       			 
						       			<span class="span1">
						       				<ul><li>
						       						<a href="javascript:mostrarTodasPartes()">
													e outros
													</a>
											</li></ul>	       				
						       			</span>	          			
						       		<%break;
						       		}%>
						    	<div> Nome </div> 
						    	<span class="span1">
						    	<%=parteDt.getNome()%>
						    	<% if (!parteDt.getAusenciaProcessoParte().equals("")){ %> <font color="red"><%=parteDt.getAusenciaProcessoParte()%></font> <%} %>
						    	</span>
						    	
						       	<div> CPF </div>
						       	<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
						       	
								<br />
								
						       	<div> Filia&ccedil;&atilde;o </div> 
						       	<span class="span1"><%=parteDt.getNomeMae()%></span>
						       	
						       	<div> Data de Nascimento </div> 
						       	<span class="span2"><%=parteDt.getDataNascimento()%></span>
						       	
								<div id="sub<%=parteDt.getNome()%>" class="DivInvisivel">
							  		<fieldset class="fieldsetEndereco">
							  			<legend> Endereço </legend>
										<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
							    		<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
							    		<%=parteDt.getEnderecoParte().getCep()%><br />
								    	<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	 	
									</fieldset>
								</div>
								
								<br />
								<div style="width:100%; margin:3px 0"></div>
								
					       	<% 	
					   			}
					   		}
					   		%>
						</fieldset> 

					     	
			    	<fieldset id="VisualizaDados" class="VisualizaDados">
			    		<legend> Outras Informações </legend>
			    		
				   		<div> Serventia </div>
						<span class="span1"><%= processoDt.getServentia()%></span><br />
						
				   		<div> Classe </div>
			    	  	<span style="width: 500px;"><%=processoDt.getProcessoTipo()%> </span><br />
			    	  				    	  			    	  
			    	  	<%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
			    	  	
			    	  	<% if (!processoDt.getValor().equals("Null")){ %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
						<%} %>
						
			    	  	<div> Processo Originário </div>
						<span class="span2"><a href="BuscaProcesso?ProcessoOutraServentia=true&Id_Processo=<%=processoDt.getId_ProcessoPrincipal()%>"><%=processoDt.getProcessoNumeroPrincipal()%></a></span/><br />	
						
						<div> Fase Processual</div>	
						<span class="span1"><%=processoDt.getProcessoFase()%></span>
									
						<div> Apenso(s)</div>
						<span class="span2">
							<% if (processoDt.temApensos()){%>
							<a href="ProcessoApenso?PaginaAtual=<%=Configuracao.Localizar%>">Visualizar</a>
							<% } %>
						</span><br />
						
						<div> Classificador </div>
					    <span class="span1">
					    <% if (UsuarioSessao.isPodeVisualizarClassificador()){ %>
					   		<%= processoDt.getClassificador()%>
					   	<% } %>
					    </span>
					    
					    <div> Data Distribui&ccedil;&atilde;o</div>
						<span class="span2"><%=processoDt.getDataRecebimento()%></span>
						<br />		
					    
						<div> Segredo de Justi&ccedil;a</div>
						<span class="span1"><%=(processoDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span>
						
						<div style="width:150px; margin-left:-35px;">Data Tr&acirc;nsito em Julgado</div>
						<span class="span2"><%=processoDt.getDataTransitoJulgado()%></span>
						<br />
						
						<div> Status </div>
						<span class="span1"><%=processoDt.getProcessoStatus()%></span>

						<div> Prioridade</div>
						<span class="span2"><%=processoDt.getProcessoPrioridade()%></span>
						
						<br />
						<div> Efeito Suspensivo </div>
						<span class="span1"><%=(processoDt.getEfeitoSuspensivo().equalsIgnoreCase("false")?"NÃO":"SIM")%></span>
			    		
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