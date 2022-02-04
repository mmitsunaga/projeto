<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ServentiaTipo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_ServentiaTipo")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaServentiaTipo")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de ServentiaTipo </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
		<input type="hidden" id="<%=request.getAttribute("tempBuscaId_ServentiaTipo").toString()%>" name="<%=request.getAttribute("tempBuscaId_ServentiaTipo").toString()%>">
		<input type="hidden" id="<%=request.getAttribute("tempBuscaServentiaTipo").toString()%>" name="<%=request.getAttribute("tempBuscaServentiaTipo").toString()%>">

		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/><br />
		<div id="divTabela" class="divTabela" > 
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th></th>
						<th>ServentiaTipo</th>
						<th class="colunaMinima" title="Seleciona o registro para edição">Editar</th>
						<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("ServentiaTipo")) {%>
						<th class="colunaMinima" title="Seleciona o registro para exclusão"> Excluir </th> 
						<%}%> 
					</tr>
				</thead>
				<tbody id="tabListaServentiaTipo">
					<%
	  				List liTemp = (List)request.getAttribute("ListaServentiaTipo");
	 				ServentiaTipoDt objTemp;
	  				boolean boLinha=false; 
	  				if(liTemp!=null)
		  				for(int i = 0 ; i< liTemp.size();i++) {
		      				objTemp = (ServentiaTipoDt)liTemp.get(i); %>
							<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								<td > <%=i+1%></td>
								<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getServentiaTipo()%>')"><%= objTemp.getServentiaTipo()%></td>
								<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId()%>','<%=objTemp.getServentiaTipo()%>')" />     </td>
								<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("ServentiaTipo")) {%>
								<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarExcluir('<%=objTemp.getId()%>')" /></td> 
								<%}%> 
							</tr>
						<%
						boLinha = !boLinha;
						}%>
				</tbody>
			</table>
		</div> 
		</form> 
	</div>  
</div> 
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
