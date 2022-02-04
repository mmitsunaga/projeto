<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<html>
	<head>
		<title>Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	</head>

	<body>	
  		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualizar Partes no Processo</h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
				<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Visualizar Partes no Processo</legend>
					    
					    <label class="formEdicaoLabel"> Processo </label><br>
					    <%if(UsuarioSessao.isPublico()) { %>
					    	<span class="span"> <a href="BuscaProcessoPublica?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a> </span>
					    <%} else if (UsuarioSessao.isUsuarioInterno()) { %>
					    	<span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a> </span>
					    <%} else { %>
					    	<span class="span"> <a href="BuscaProcessoUsuarioExterno?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a> </span>
					    <% } %>
					    
				    	<!-- PROMOVENTES -->
		  				<input type="hidden" id="posicaoLista" name="posicaoLista">
			  	
		  				<fieldset id="VisualizaDados" class="VisualizaDados">   
		  					<legend> Polo Ativo</legend>
							<%
								List listaPromoventes = processoDt.getListaPolosAtivos();
										   	    	if (listaPromoventes != null){
										   	    		for (int i=0;i < listaPromoventes.size();i++){
											   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
							%>
							       	<div> Nome </div> 
							       	<span class="span1"><%=parteDt.getNome()%></span>
							       	<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span class="span1" style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span class="span1" style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
			   						<br />
					   	    		<%}
			  					}
			  				%>
						</fieldset>
						
						<!-- PROMOVIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
			  				<legend> Polo Passivo</legend>
					 		<%
					 			List listaPromovidos = processoDt.getListaPolosPassivos();
					 					   			if (listaPromovidos != null){
					 					   				for (int i=0;i < listaPromovidos.size();i++){
					 					   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					 		%>
				   					<div> Nome </div> 
				   					<span class="span1"><%=parteDt.getNome()%></span>
				   					<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span class="span1" style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span class="span1" style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
									<br />
					   				<% }   
			  					} 
			  				%>	
						</fieldset/>
						
						<!-- OUTRAS PARTES -->
						
						<%
						List listaOutrasPartes = processoDt.getListaOutrasPartes();
						if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){
						
					   		List litisconsorteAtivos = new ArrayList();
					   		List litisconsortePassivos = new ArrayList();
					   		List substitutosProcessual = new ArrayList();
					   		List comunicantes = new ArrayList();
					   		List curadores = new ArrayList();
					   		List terceiros = new ArrayList();
					   		List testemunhas = new ArrayList();
					   		List pacientes = new ArrayList();
					   		List outros = new ArrayList();
					   		
					   		String strLitisconsorteAtivo = "";
					   		String strLitisconsortePassivo = "";
					   		String strSubstitutoProcessual = "";
					   		String strComunicante = "";
					   		String strCurador = "";
					   		String strTerceiro = "";
					   		String strTestemunha = "";
					   		String strPaciente = "";
					   		
					   		for (int i=0; i<listaOutrasPartes.size();i++){
				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
				   			  	if (parteDt.getProcessoParteTipoCodigo() != null) {
				   			  		int parteTipoCodigo =  Funcoes.StringToInt(parteDt.getProcessoParteTipoCodigo());
					   			  	switch (parteTipoCodigo) {
						   			 	case ProcessoParteTipoDt.LITIS_CONSORTE_ATIVO:
							   				strLitisconsorteAtivo = (String) parteDt.getProcessoParteTipo();
					   			  			litisconsorteAtivos.add(parteDt);
						 					break;
						   				case ProcessoParteTipoDt.LITIS_CONSORTE_PASSIVO:
							   				strLitisconsortePassivo = (String) parteDt.getProcessoParteTipo();
					   			  			litisconsortePassivos.add(parteDt);
							 				break;
						   				case ProcessoParteTipoDt.SUBSTITUTO_PROCESSUAL:
						   					strSubstitutoProcessual = (String) parteDt.getProcessoParteTipo();
					   			  			substitutosProcessual.add(parteDt);
					   			  			break;
						   				case ProcessoParteTipoDt.COMUNICANTE:
						   					strComunicante = (String) parteDt.getProcessoParteTipo();
						   					comunicantes.add(parteDt);
						   					break;
						   				case ProcessoParteTipoDt.CURADOR:
						   					strCurador = (String) parteDt.getProcessoParteTipo();
						   					curadores.add(parteDt);
						   					break;
						   				case ProcessoParteTipoDt.TERCEIRO:
						   					strTerceiro = (String) parteDt.getProcessoParteTipo();
						   					terceiros.add(parteDt);
						   					break;
						   				case ProcessoParteTipoDt.TESTEMUNHA:
						   					strTestemunha = (String) parteDt.getProcessoParteTipo();
						   					testemunhas.add(parteDt);
						   					break;
						   				case ProcessoParteTipoDt.PACIENTE:
						   					strPaciente = (String) parteDt.getProcessoParteTipo();
						   					pacientes.add(parteDt);
						   					break;
						   				default:
						   					outros.add(parteDt);
						   					break;
					   			  	}
				   			  	}
		   					} %>
		   					
		   					
	   					<%if (litisconsorteAtivos != null && litisconsorteAtivos.size() > 0) { %>
							<fieldset id="VisualizaDados" class="VisualizaDados">
						   		<legend><%=strLitisconsorteAtivo%></legend>
						 		<%
						   				for (int i=0;i < litisconsorteAtivos.size();i++){
						   			  		ProcessoParteDt parteDt = (ProcessoParteDt)litisconsorteAtivos.get(i);
							   	%>
						    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
						    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
										<br />
					      		    	<%}
						        		} %>  
							</fieldset>
							
							<%if (pacientes != null && pacientes.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strPaciente%></legend>
							 		<%
							   				for (int i=0;i < pacientes.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)pacientes.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
							<%if (litisconsortePassivos != null && litisconsortePassivos.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strLitisconsortePassivo%></legend>
							 		<%
							   				for (int i=0;i < litisconsortePassivos.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)litisconsortePassivos.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
							<%if (substitutosProcessual != null && substitutosProcessual.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strSubstitutoProcessual%></legend>
							 		<%
							   				for (int i=0;i < substitutosProcessual.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)substitutosProcessual.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
							<%if (comunicantes != null && comunicantes.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strComunicante%></legend>
							 		<%
							   				for (int i=0;i < comunicantes.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)comunicantes.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
							<%if (curadores != null && curadores.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strCurador%></legend>
							 		<%
							   				for (int i=0;i < curadores.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)curadores.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
							<%if (terceiros != null && terceiros.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strTerceiro%></legend>
							 		<%
							   				for (int i=0;i < terceiros.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)terceiros.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
							<%if (testemunhas != null && testemunhas.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strTestemunha%></legend>
							 		<%
							   				for (int i=0;i < testemunhas.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)testemunhas.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
							    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
							    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%} else {%>
							    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
							    				<%}%>
							    			<%}%>
								       		<br />
						      		    	<%}
							       		} %>  
							</fieldset>
							
					   		<fieldset id="VisualizaDados" class="VisualizaDados">
								   		<legend> Outras Partes / Sujeitos </legend>
								 		<%
								 		if (outros != null && outros.size() > 0) {
								   				for (int i=0;i < outros.size();i++){
								   			  		ProcessoParteDt parteDt = (ProcessoParteDt)outros.get(i);
									   	%>
								    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome() + " - [" + parteDt.getProcessoParteTipo() + "]"%></span>
								    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
								    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
								    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
								    				<%} else {%>
								    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
								    				<%}%>
								    			<%}%>
									       		<br />
							      		  <%} %>
							</fieldset><br />
						<%} else {  %>
				   			<fieldset id="VisualizaDados" class="VisualizaDados">
								   		<legend> Outras Partes / Sujeitos </legend>
								 		<%
								 		if (listaOutrasPartes != null && listaOutrasPartes.size() > 0) {
								   				for (int i=0;i < listaOutrasPartes.size();i++){
								   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
									   	%>
									    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome() + " - [" + parteDt.getProcessoParteTipo() + "]"%></span>
									    			<%if(parteDt.getDataBaixa() != null && !parteDt.getDataBaixa().equals("")) {%>
									    				<%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>
									    					<span style="color:red;">Parte EXCLUÍDA em <%=parteDt.getDataBaixa()%>.</span>
									    				<%} else {%>
									    					<span style="color:red;">Parte BAIXADA em <%=parteDt.getDataBaixa()%>.</span>
									    				<%}%>
									    			<%}%>
									       		<br />
							   					<%  } %>
									</fieldset><br />
										<%  } %>
						<%  } %>
					<%  } %>	
			
					</fieldset>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>