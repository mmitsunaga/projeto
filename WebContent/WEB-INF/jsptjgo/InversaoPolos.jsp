<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.InversaoPolosDt"%>

<jsp:useBean id="InversaoPolosdt" scope="session" class= "br.gov.go.tj.projudi.dt.InversaoPolosDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Inversão de Pólos - Partes </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Inversão de Pólos </h2></div>
		<form action="InversaoPolos" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Inversão de Pólos</legend>
					
					<br />
			      	<div> Número Processo  
					<span> <a href="BuscaProcesso?Id_Processo=<%=InversaoPolosdt.getProcessoCompletoDt().getId_Processo()%>&PassoBusca=2"><%=InversaoPolosdt.getProcessoCompletoDt().getProcessoNumero()%></a> </span/><br />
					<br />				
					</div>
							      					
					<% if(InversaoPolosdt.isRecurso()) { %>									
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda"> Partes do Recurso </legend>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> *Recorrente(s) </legend>
							<%
							List listaRecorrentes = InversaoPolosdt.getRecursoDt().getListaRecorrentesAtivos();
	 	    				for (int i=0;i < listaRecorrentes.size();i++){
	 	    					RecursoParteDt recursoParteDt = (RecursoParteDt)listaRecorrentes.get(i);
		  			  		%>
				   				<div>
					   				<input name="Recorrente" id="Recorrente" type="checkbox" value="<%=recursoParteDt.getId()%>" 
					   				<%	
					   					List listaInversaoPolosRecorrentes = InversaoPolosdt.getListaRecorrentesInversaoPolos();
										if (listaInversaoPolosRecorrentes != null && listaInversaoPolosRecorrentes.size()>0){
							            	for(int j = 0 ; j< (listaInversaoPolosRecorrentes.size());j++) {
							              		RecursoParteDt obj = (RecursoParteDt) listaInversaoPolosRecorrentes.get(j);
							                   	if (obj.getId().equals(recursoParteDt.getId())){%> 
							                   	checked
							        <% 			}
							               	}
										}
									%>
									/>
				   				</div> 
				       			<span><%=recursoParteDt.getProcessoParteDt().getNome()%> </span>
								<br />
							<%	} %>
						</fieldset>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> *Recorridos(s) </legend>
							<%
							List listaRecorridos = InversaoPolosdt.getRecursoDt().getListaRecorridosAtivos();
	 	    				for (int i=0;i < listaRecorridos.size();i++){
	 	    					RecursoParteDt recursoParteDt = (RecursoParteDt)listaRecorridos.get(i);		  			  			
		 					%>
				   				<div>
					   				<input name="Recorrido" id="Recorrido" type="checkbox" value="<%=recursoParteDt.getId()%>" 
					   				<%	
					   					List listaInversaoPolosRecorridos = InversaoPolosdt.getListaRecorridosInversaoPolos();
										if (listaInversaoPolosRecorridos != null && listaInversaoPolosRecorridos.size()>0){
							            	for(int j = 0 ; j< (listaInversaoPolosRecorridos.size());j++) {
							            		RecursoParteDt obj = (RecursoParteDt) listaInversaoPolosRecorridos.get(j);
							                   	if (obj.getId().equals(recursoParteDt.getId())){%> 
							                   	checked
							        <% 			}
							               	}
										}
									%>
									/>
				   				</div> 
				       			<span><%=recursoParteDt.getProcessoParteDt().getNome()%> </span>
				        		<br />
							<%	} %>
						</fieldset>
						
					</fieldset>
					<% } %>						
							
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda"> Partes do Processo </legend>
									
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> *Promoventes(s) </legend>
							<%
							List listaPromoventes = InversaoPolosdt.getProcessoCompletoDt().getListaPromoventesAtivos();
	 	    				for (int i=0;i < listaPromoventes.size();i++){
		  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);		  			  			
		 					%>
				   				<div>
					   				<input name="Promovente" id="Promovente" type="checkbox" value="<%=parteDt.getId()%>" 
					   				<%	
					   					List listaInversaoPolosPromoventes = InversaoPolosdt.getListaPromoventesInversaoPolos();
										if (listaInversaoPolosPromoventes != null && listaInversaoPolosPromoventes.size()>0){
							            	for(int j = 0 ; j< (listaInversaoPolosPromoventes.size());j++) {
							            		ProcessoParteDt obj = (ProcessoParteDt) listaInversaoPolosPromoventes.get(j);
							                   	if (obj.getId().equals(parteDt.getId())){%> 
							                   	checked
							        <% 			}
							               	}
										}
									%>
									/>
				   				</div> 
				       			<span><%=parteDt.getNome()%> </span>
				       			<br />
							<% } %>
						</fieldset>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> *Polo Passivo </legend>
							<%
							List listaPromovidos = InversaoPolosdt.getProcessoCompletoDt().getListaPromovidosAtivos();
	 	    				for (int i=0;i < listaPromovidos.size();i++){
		  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);		  			  			
		 					%>
				   				<div>
					   				<input name="Promovido" id="Promovido" type="checkbox" value="<%=parteDt.getId()%>" 
					   				<%	
					   					List listaInversaoPolosPromovidos = InversaoPolosdt.getListaPromovidosInversaoPolos();
										if (listaInversaoPolosPromovidos != null && listaInversaoPolosPromovidos.size()>0){
							            	for(int j = 0 ; j< (listaInversaoPolosPromovidos.size());j++) {
							            		ProcessoParteDt obj = (ProcessoParteDt) listaInversaoPolosPromovidos.get(j);
							                   	if (obj.getId_ProcessoParte().equals(parteDt.getId())){%> 
							                   	checked
							        <% 			}
							               	}
										}
									%>
									/>
				   				</div> 
				       			<span><%=parteDt.getNome()%> </span>
				       			<br />
							<% } %>
						</fieldset>
					</fieldset>
					<br />
					<br />				
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Inverter Pólos" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						<input name="imgInserir" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>