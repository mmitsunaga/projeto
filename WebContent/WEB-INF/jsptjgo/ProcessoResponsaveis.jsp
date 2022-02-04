<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>

<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Responsáveis pelo Processo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Responsáveis pelo Processo </h2></div>

			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
			<fieldset id="VisualizaDados" class="formEdicao" style="background-color:#f5f5f5">
		      	<legend>Responsáveis pelo Processo</legend>
		      	
		      	<br />
		      	<div> Número Processo </div>
				<span><a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=-1&Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/><br />
				<br />
								
				<%@ include file="PartesProcesso.jspf"%>
				
				<br />
				<br />
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Responsáveis</legend>
					
	    			<table id="Responsaveis" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
				           		<td width="5%">&nbsp;</td>
				               	<td>Cargo</td>
				               	<td>Nome</td>
				               	<td>Serventia</td>
			    	        </tr>
			           	</thead>
			          	<tbody>
						<% 
	          			List listaResponsaveis = (List) request.getAttribute("ListaResponsaveis");
		          		
		          		if (listaResponsaveis != null && listaResponsaveis.size() > 0){
		          			for (int i=0; i < listaResponsaveis.size(); i++){
		          				ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) listaResponsaveis.get(i);
			          	%>
				      		<tr>
				      			<td><%=i+1%></td>
			          			<td><%=serventiaCargoDt.getCargoTipo()%></td>
			          			<td><%=serventiaCargoDt.getNomeUsuario()%></td>
			          			<td><%=serventiaCargoDt.getServentia()%></td>
			          		</tr>
						<% }
		          		} else {%>
		          			<p align="center"><em><b>Nenhum Responsável Cadastrado.</b></em></p>
		          		<% } %>
						</tbody>
					</table>
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Responsáveis Desabilitados</legend>
					
	    			<table id="ResponsaveisDesabilitados" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
				           		<td width="5%">&nbsp;</td>
				               	<td>Cargo</td>
				               	<td>Nome</td>
				               	<td>Serventia</td>
			    	        </tr>
			           	</thead>
			          	<tbody>
						<% 
	          			List listaResponsaveisDesabilitados = (List) request.getAttribute("ListaResponsaveisDesabilitados");
		          		
		          		if (listaResponsaveisDesabilitados != null && listaResponsaveisDesabilitados.size() > 0){
		          			for (int i=0; i < listaResponsaveisDesabilitados.size(); i++){
		          				ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) listaResponsaveisDesabilitados.get(i);
			          	%>
				      		<tr>
				      			<td><%=i+1%></td>
			          			<td><%=serventiaCargoDt.getCargoTipo()%></td>
			          			<td><%=serventiaCargoDt.getNomeUsuario()%></td>
			          			<td><%=serventiaCargoDt.getServentia()%></td>
			          		</tr>
						<% }
		          		} else {%>
		          			<p align="center"><em><b>Nenhum Responsável Desabilitados Cadastrado.</b></em></p>
		          		<% } %>
						</tbody>
					</table>
					
					<% if (request.getAttribute("podeHabilitarRelatorInativo") != null && request.getAttribute("podeHabilitarRelatorInativo").toString().equals("true")){ %>
					<a href="ProcessoResponsavel?PaginaAtual=<%=Configuracao.Curinga6%>&PassoAtual=1&Id_Processo=<%=processoDt.getId()%>" title="Clique para incluir Relator desabilitado">Incluir Relator desabilitado\Manter Responsáveis</a>
					<% } %>
				</fieldset>
			
			
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Advogados Habilitados</legend>
					<%
						List listaAdvogados = (List) request.getAttribute("ListaAdvogados");		
						ProcessoParteAdvogadoDt objTemp;
						if (listaAdvogados != null && listaAdvogados.size() > 0){
							objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(0);
					%>
			     	<table id="Advogados" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna" align="center">
			            		<td>Advogado</td>
			                	<td>OAB/Matrícula</td>			                  	
			                  	<td>Dativo</td>
			                  	<td>Recebe Intimação</td>
			                  	<td>Data Habilitação</td>
			                  	<td>Serventia</td>
			                  	<td>Parte</td>
			             	 </tr>
			           	</thead>
			          	<tbody id="tabListaAdvogadoParte">
						<%
							for(int i = 0 ; i< listaAdvogados.size();i++) {
						   		objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(i);
						%>
					  		<tr>
					  			<td width="15%"><%= objTemp.getNomeAdvogado() %> </td>					  			 
			       		        <td width="5%" align="center"><%=objTemp.getOabNumero()+ " " + objTemp.getOabComplemento()%></td>				                
				                <td width="4%" align="center"> <%=objTemp.mostrarDativo()%></td>
				                <td width="4%" align="center"><%=objTemp.mostrarRecebeIntimacao()%></td>
				                <td width="4%" align="center"><%= objTemp.getDataEntrada()%> </td>
				                <td width="10%"><%= objTemp.getServentiaHabilitacao()%> </td>
				                <td width="15%"><%= objTemp.getNomeParte()%> - <%= objTemp.getProcessoParteTipo()%></td>
				       		</tr>
						<%
							}
						} else { %>
						<p align="center"><em><b>Nenhum Advogado habilitado nesse processo.</b></em></p>
						<% } %>
						</tbody>
					</table>
					
					<% if (request.getAttribute("podeHabilitarAdvogados") != null && request.getAttribute("podeHabilitarAdvogados").toString().equals("true")){ %>
					<a href="ProcessoParteAdvogado?PaginaAtual=<%=Configuracao.Novo%>" title="Clique para Habilitar Advogados">Habilitar Advogados</a>
					<% } %>
				</fieldset>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Advogados Desabilitados</legend>
					<%
						List listaAdvogadosDesabilitados = (List) request.getAttribute("ListaAdvogadosDesabilitados");		
						ProcessoParteAdvogadoDt advogadoDesabilitado;
						if (listaAdvogadosDesabilitados != null && listaAdvogadosDesabilitados.size() > 0){
							advogadoDesabilitado = (ProcessoParteAdvogadoDt)listaAdvogadosDesabilitados.get(0);
					%>
			     	<table id="Desabilitados" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna" align="center">
			            		<td>Nome</td>
			                	<td>OAB/Matrícula</td>			                  	
			                  	<td>Dativo</td>
			                  	<td>Recebe Intimação</td>
			                  	<td>Data Habilitação</td>
			                  	<td>Data Saída</td>
			                  	<td>Serventia</td>
			                  	<td>Parte</td>
			             	 </tr>
			           	</thead>
			          	<tbody id="tabListaAdvogadoParte">
						<%
							for(int i = 0 ; i< listaAdvogadosDesabilitados.size();i++) {
								advogadoDesabilitado = (ProcessoParteAdvogadoDt)listaAdvogadosDesabilitados.get(i);
						%>
					  		<tr>					  			
				                <td width="14%"><%= advogadoDesabilitado.getNomeAdvogado() %> </td>
			       		        <td width="4%" align="center"><%=advogadoDesabilitado.getOabNumero()+ " " + advogadoDesabilitado.getOabComplemento()%></td>
				                <td width="3%" align="center"> <%=advogadoDesabilitado.mostrarDativo()%></td>
				                <td width="4%" align="center"><%=advogadoDesabilitado.mostrarRecebeIntimacao()%></td>
				                <td width="4%" align="center"><%= advogadoDesabilitado.getDataEntrada()%> </td>
				                <td width="4%" align="center"><%= advogadoDesabilitado.getDataSaida()%> </td>
				                <td width="10%"><%= advogadoDesabilitado.getServentiaHabilitacao()%> </td>
				                <td width="14%"><%= advogadoDesabilitado.getNomeParte()%> - <%= advogadoDesabilitado.getProcessoParteTipo()%></td>
				       		</tr>
						<%
							}
						} else { %>
						<p align="center"><em><b>Nenhum Advogado desabilitado nesse processo.</b></em></p>
						<% } %>
						</tbody>
					</table>
				</fieldset>
				
			</fieldset>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div> 
</body>
</html>