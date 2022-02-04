<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Prevenções do Processo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Processos com Suspeita de Conexão </h2></div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
			<fieldset id="VisualizaDados" class="VisualizaDados">
		      	<legend>Processos com Suspeita de Conexão</legend>
		      	
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend> Dados Processo</legend>
					
					<div> Número Processo </div>
					<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
					<br />
					
					<div> Classe </div>
					<span> <%=processoDt.getProcessoTipo()%> </span>
					
					<div> Data Distribuição</div>
					<span> <%=processoDt.getDataRecebimento()%> </span>
					<br />
					
					<div> Serventia </div>
					<span> <%=processoDt.getServentia()%> </span>
					
					<div> Valor </div>
					<span> <%=processoDt.getValor()%> </span>
					<br />
					
					<%@ include file="PartesProcesso.jspf"%>
				</fieldset>
				
          		<fieldset id="VisualizaDados" class="VisualizaDados">
	      			<legend>Possíveis Conexos </legend>
	    			<table id="Tabela" class="Tabela">
			        	<thead>
			           		<tr class="TituloColuna">
				           		<td width="5%">&nbsp;</td>
				               	<td>Conexo</td>
				               	<td>Distribuição</td>
				               	<td>Serventia</td>
				               	<td>Classe</td>
				               	<td>Valor</td>
			    	        </tr>
			           	</thead>
			          	<tbody>
		          		<%
		          		ProcessoDt processoTempDt;
		          		List listaPreventos = (List) request.getAttribute("preventos");
		          		if (listaPreventos != null){
		          			for(int i=0; i < listaPreventos.size();i++){
		          				processoTempDt = (ProcessoDt)listaPreventos.get(i);
		          		%>
				      		<tr>
				      			<td><%=i+1%></td>
			          			<td><a href="BuscaProcesso?Id_Processo=<%=processoTempDt.getId_Processo()%>"><%=processoTempDt.getProcessoNumero()%></a></td>
			          			<td><%=processoTempDt.getDataRecebimento()%></td>
			          			<td><%=processoTempDt.getServentia()%></td>
			          			<td><%=processoTempDt.getProcessoTipo()%></td>
			          			<td><%=processoTempDt.getValor()%></td>
			          		</tr>
					     <% } 
					     } else {%>
					     	<tr>
					     		<td colspan="5"><em>Nenhum Prevento localizado para esse processo. </em></td>
					     	</tr>
					     <%} %>
						</tbody>
					</table>
				</fieldset>

			</fieldset>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div> 
</body>
</html>