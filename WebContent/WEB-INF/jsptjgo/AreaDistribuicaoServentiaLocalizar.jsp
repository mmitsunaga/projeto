<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de �rea de Distribui��o  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_AreaDistribuicao")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaAreaDistribuicao")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	
	
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de �rea de Distribui��o </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

		<input type="hidden" id="<%=request.getAttribute("tempBuscaId_AreaDistribuicao").toString()%>" name="<%=request.getAttribute("tempBuscaId_AreaDistribuicao").toString()%>" />
		<input type="hidden" id="<%=request.getAttribute("tempBuscaAreaDistribuicao").toString()%>" name="<%=request.getAttribute("tempBuscaAreaDistribuicao").toString()%>" />
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
		<br />
		<div id="divTabela" class="divTabela" > 
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th></th>
						<th>Descri��o</th> 
						<th class="colunaMinima" title="Seleciona o registro para edi��o">Editar</th>
						<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("AreaDistribuicao")) {%>
						<th class="colunaMinima" title="Seleciona o registro para exclus�o"> Excluir </th> 
						<%}%> 
					</tr>
					</thead>
				<tbody id="tabListaAreaDistribuicao">
<%
  List liTemp = (List)request.getAttribute("ListaAreaDistribuicao");
 AreaDistribuicaoDt objTemp;
  boolean boLinha=false; 
  if(liTemp!=null)
	  for(int i = 0 ; i< liTemp.size();i++) {
	      objTemp = (AreaDistribuicaoDt)liTemp.get(i); %>
						<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
							<td > <%=i+1%></td>
							<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getAreaDistribuicao()%>')"><%= objTemp.getAreaDistribuicao()%></td>
							<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId()%>','<%=objTemp.getAreaDistribuicao()%>')" />     </td>
	<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("AreaDistribuicao")) {%>
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
