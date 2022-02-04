<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Assunto  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_Assunto")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaAssunto")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	
	
	<%@ include file="./js/Paginacao.js"%> 
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Assunto </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%= request.getAttribute("Id_Serventia")%>">
			<fieldset id="formLocalizar" class="formLocalizar"> 
				<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de Assunto </legend>
				<label  id="formLocalizarLabel"   class="formLocalizarLabel">Digite o Assunto:</label><br> 
				<input  id="nomeBusca"   class="formLocalizarInput" name="nomeBusca" type="text" value="" size="60" maxlength="60" />
				<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;" />
			</fieldset>
			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Assunto").toString()%>" name="<%=request.getAttribute("tempBuscaId_Assunto").toString()%>" />
			<input type="hidden" id="<%=request.getAttribute("tempBuscaAssunto").toString()%>" name="<%=request.getAttribute("tempBuscaAssunto").toString()%>" />
	
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<div id="divTabela" class="divTabela" > 
				<table id="Tabela" class="Tabela">
					<thead>
						<tr>
							<th></th>
							<th width="45%">Assunto</th>
							<th width="25%">Pai</th>
							<th>Disp.Legal</th>
							<th class="colunaMinima" title="Seleciona o registro para edição">Editar</th>
							<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("Assunto")) {%>
							<th class="colunaMinima" title="Seleciona o registro para exclusão"> Excluir </th> 
							<%}%> 
						</tr>
						</thead>
					<tbody id="tabListaAssunto">
					<%
	  				List liTemp = (List)request.getAttribute("ListaAssunto");
	 				AssuntoDt objTemp;
	  				boolean boLinha=false; 
	  				if(liTemp!=null)
		  				for(int i = 0 ; i< liTemp.size();i++) {
		      			objTemp = (AssuntoDt)liTemp.get(i); %>
							<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								<td> <%=i+1%></td>
								<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getAssunto()%> - <%=objTemp.getDispositivoLegal()%>')"><%= objTemp.getAssunto()%></td>
								<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getAssunto()%> - <%=objTemp.getDispositivoLegal()%>')"><%= objTemp.getAssuntoPai()%></td>
								<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getAssunto()%> - <%=objTemp.getDispositivoLegal()%>')"><%= objTemp.getDispositivoLegal()%></td>
								<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId()%>','<%=objTemp.getAssunto()%> - <%=objTemp.getDispositivoLegal()%>')" />     </td>
								<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("Assunto")) {%>
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
