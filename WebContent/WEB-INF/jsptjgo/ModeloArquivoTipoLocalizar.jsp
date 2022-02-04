<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_Modelo")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaModelo")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>		
	<%@ include file="./js/Paginacao.js"%> 
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Modelos </h2></div>
		
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					 
					<fieldset id="formLocalizar" class="formLocalizar"> 
		    			<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de Modelos </legend>
		    	       	<label id="formLocalizarLabel" class="formLocalizarLabel">Digite a descrição: </label><br>
		    	       	<input id="Id_ArquivoTipo" name="Id_ArquivoTipo" type="hidden" value="<%= request.getAttribute("Id_ArquivoTipo")%>"> 
		       			<input id="nomeBusca" class="formLocalizarInput" name="nomeBusca" type="text" value="" size="60" maxlength="60" /> 
		       			<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;" />
		   			</fieldset>
		   
		   			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Modelo").toString()%>" name="<%=request.getAttribute("tempBuscaId_Modelo").toString()%>" />
		   			<input type="hidden" id="<%=request.getAttribute("tempBuscaModelo").toString()%>" name="<%=request.getAttribute("tempBuscaModelo").toString()%>" />
		
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
					<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
					
		   			<div id="divTabela" class="divTabela" > 
		    			<table id="Tabela" class="Tabela">
		        			<thead>
		            			<tr>
		                			<th class="colunaMinima"></th>
	                  				<th>Modelo</th>
		                  			<th>Tipo Arquivo</th>
		                   			<th class="colunaMinima">Selecionar</th>
		                   			<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("Modelo")) {%>
		                    		<th class="colunaMinima">Excluir</th> 
		                   			<%}%>
		               			</tr>
		           			</thead>
		           	
		           			<tbody id="tabListaModelo">
							<%
							List liTemp = (List)request.getAttribute("ListaModelo");
							ModeloDt objTemp;
							boolean boLinha = false;
							for(int i = 0 ; i< liTemp.size();i++) {
								objTemp = (ModeloDt)liTemp.get(i); %>
								<tr class="TabelaLinha<%=(boLinha?1:2)%>">
    			               		<td><%=i+1%></td>
									<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getModelo()%>')"><%=objTemp.getModelo()%></td>
									<td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getModelo()%>')"><%=objTemp.getArquivoTipo()%></td>
			                   		<td>
                   						<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditar('<%=objTemp.getId()%>','<%=objTemp.getModelo()%>')" />     
                   					</td>
									<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("Modelo")) {%>
		                			<td>
		                				<input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarExcluir('<%=objTemp.getId()%>')" /></td>
		                			</td> 
									<%}%>
               					</tr>
							<%
							boLinha = !boLinha;
							}
							%>
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