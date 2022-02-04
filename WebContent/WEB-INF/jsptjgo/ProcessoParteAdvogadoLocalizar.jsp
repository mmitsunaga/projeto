<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Area  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_UsuarioAdvogado")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaNomeAdvogado")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<%@ include file="./js/Paginacao.js"%> 
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Advogados </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

			<fieldset id="VisualizaDados" class="VisualizaDados">
		   		<legend> Advogados encontrados </legend>
		
			   	<input type="hidden" id="<%=request.getAttribute("tempBuscaId_UsuarioAdvogado").toString()%>" name="<%=request.getAttribute("tempBuscaId_UsuarioAdvogado").toString()%>">
		   		<input type="hidden" id="<%=request.getAttribute("tempBuscaNomeAdvogado").toString()%>" name="<%=request.getAttribute("tempBuscaNomeAdvogado").toString()%>">
			  	<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			  	
		   		<table id="Tabela" class="Tabela">
		    		<thead>
		            	<tr class="TituloColuna">
		                	<th class="colunaMinima"></th>
		                   	<th width="15%">OAB/Matrícula</th>
		                  	<th>Nome</th>
		                  	<th width="10%">Complemento</th>
		                  	<th class="colunaMinima">Selecionar</th>
		               	</tr>
		           	</thead>
		           	<tbody id="tabListaProcessoParteAdvogado">
					<%
						UsuarioDt objTemp;
						String stTempNome="";
						List liTemp = (List)request.getAttribute("ListaProcessoParteAdvogado");
									
						for(int i = 0 ; i< liTemp.size();i++) {
		      				objTemp = (UsuarioDt)liTemp.get(i); %>
							<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
		                   	<tr class="Linha1"> 
							<%}else{ stTempNome=""; %>    
		                   	<tr class="Linha2">
							<%}%>
		                   		<td> <%=i+1%></td>
		                    	<td onclick="selecionaSubmete('<%=objTemp.getId_UsuarioServentia()%>','<%=objTemp.getNome()%>')"><%= objTemp.getOabNumero() + "-" + objTemp.getOabEstado()%></td>
		                    	<td onclick="selecionaSubmete('<%=objTemp.getId_UsuarioServentia()%>','<%=objTemp.getNome()%>')"><%= objTemp.getNome()%></td>
		                    	<td onclick="selecionaSubmete('<%=objTemp.getId_UsuarioServentia()%>','<%=objTemp.getNome()%>')"><%= objTemp.getOabComplemento()%></td>
		                    	<td>
		                   			<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId_UsuarioServentia()%>','<%=objTemp.getNome()%>')" />     
		                   		</td>
		               		</tr>
					<%	} %>
		           	</tbody>
		       	</table>
			</fieldset>
		</form>
	</div>
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html> 