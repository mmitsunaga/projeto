<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Cargo da Serventia  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='js/Funcoes.js'></script>
	<script type="text/javascript" src="js/jquery.js"> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/buscaJson.js'></script> 
	<script type="text/javascript">
	
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_ServentiaCargo")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaServentiaCargo")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	
	<%@ include file="./js/Paginacao.js"%> 	
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Cargo da Serventia </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<fieldset id="formLocalizar" class="formLocalizar"  > 
			<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de ServentiaCargo </legend>
			<label  id="formLocalizarLabel"   class="formLocalizarLabel">Digite a descrição</label><br> 
			<input  id="nomeBusca"   class="formLocalizarInput" name="nomeBusca" type="text" value="" size="60" maxlength="60">
			<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;" >
		</fieldset>
		<input type="hidden" id="id_Serventia" name="id_Serventia" value="<%=request.getAttribute("id_Serventia")%>" />
		<input type="hidden" id="grupoTipoCodigo" name="grupoTipoCodigo" value="<%=request.getAttribute("grupoTipoCodigo")%>" />
		<input type="hidden" id="<%=request.getAttribute("tempBuscaId_ServentiaCargo").toString()%>" name="<%=request.getAttribute("tempBuscaId_ServentiaCargo").toString()%>">
		<input type="hidden" id="<%=request.getAttribute("tempBuscaServentiaCargo").toString()%>" name="<%=request.getAttribute("tempBuscaServentiaCargo").toString()%>">

		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
		<br />
		<div id="divTabela" class="divTabela" > 
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th class="colunaMinima"></th>
						<th>Descrição</th>
						<th>Tipo Cargo</th>
						<th>Nome Usuário</th>
						<th>Serventia</th>
						<th class="colunaMinima" title="Seleciona o registro para edição">Editar</th>
						<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("ServentiaCargo")) {%>
						<th class="colunaMinima" title="Seleciona o registro para exclusão"> Excluir </th> 
						<%}%>
					</tr>
				</thead>
				<tbody id="tabListaServentiaCargo">
				<%
				List liTemp = (List)request.getAttribute("ListaServentiaCargo");
				ServentiaCargoDt objTemp;
				boolean boLinha=false; 
				if(liTemp!=null)
					for(int i = 0 ; i< liTemp.size();i++) {
						objTemp = (ServentiaCargoDt)liTemp.get(i); %>
						<tr class="TabelaLinha<%=(boLinha?1:2)%>">
							<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getServentiaCargo()%>')"><%=i+1%></td>
							<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getServentiaCargo()%>')"><%=objTemp.getServentiaCargo()%></td>						
							<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getServentiaCargo()%>')"><%=objTemp.getCargoTipo()%></td>
							<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getNomeUsuario()%>')"><%=objTemp.getNomeUsuario()%></td>
							<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getServentiaCargo()%>')"><%=objTemp.getServentia()%></td>
							<td>
								<input name="formLocalizarimgEditar" type="image" src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId()%>','<%=objTemp.getServentiaCargo()%>')" />
							</td>
							<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("ServentiaCargo")) {%>
							<td>
								<input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarExcluir('<%=objTemp.getId()%>')" />
							</td> 
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
<%@ include file="Padroes/Mensagens.jspf" %>
</div> 
</body>
</html>
