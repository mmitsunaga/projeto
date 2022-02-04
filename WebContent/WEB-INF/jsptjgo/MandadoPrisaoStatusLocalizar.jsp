<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisaoStatus  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_MandadoPrisaoStatus")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaMandadoPrisaoStatus")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	
	
	<%@ include file="./js/Paginacao.js"%> 
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisaoStatus </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<fieldset id="formLocalizar" class="formLocalizar"  > 
			<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de MandadoPrisaoStatus </legend>
			<label  id="formLocalizarLabel"   class="formLocalizarLabel">Digite o MandadoPrisaoStatusCodigo</label><br> 
			<input  id="nomeBusca"   class="formLocalizarInput" name="nomeBusca" type="text" value="" maxlength="60" /> <br />
			<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;" />
		</fieldset>
		<input type="hidden" id="<%=request.getAttribute("tempBuscaId_MandadoPrisaoStatus").toString()%>" name="<%=request.getAttribute("tempBuscaId_MandadoPrisaoStatus").toString()%>" />
		<input type="hidden" id="<%=request.getAttribute("tempBuscaMandadoPrisaoStatus").toString()%>" name="<%=request.getAttribute("tempBuscaMandadoPrisaoStatus").toString()%>" />

		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<br />
		<div id="divTabela" class="divTabela" > 
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th></th>
						<th>MandadoPrisaoStatus</th>
						<th class="colunaMinima" title="Seleciona o registro para edição">Editar</th>
						<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("MandadoPrisaoStatus")) {%>
						<th class="colunaMinima" title="Seleciona o registro para exclusão"> Excluir </th> 
						<%}%> 
					</tr>
					</thead>
				<tbody id="tabListaMandadoPrisaoStatus">
<%
  List liTemp = (List)request.getAttribute("ListaMandadoPrisaoStatus");
 MandadoPrisaoStatusDt objTemp;
  boolean boLinha=false;
  if(liTemp!=null)
  for(int i = 0 ; i< liTemp.size();i++) {
      objTemp = (MandadoPrisaoStatusDt)liTemp.get(i); %>
					<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
						<td > <%=i+1%></td>
						<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getMandadoPrisaoStatus()%>')"><%= objTemp.getMandadoPrisaoStatus()%></td>
						<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId()%>','<%=objTemp.getMandadoPrisaoStatus()%>')" />     </td>
<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("MandadoPrisaoStatus")) {%>
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
<%@ include file="./Padroes/Paginacao.jspf"%> 
</div> 
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
