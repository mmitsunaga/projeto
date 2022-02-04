<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<html>
	<head>
		<title>Administracao</title>
	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		

		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
	
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>
<body>	
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Administração de Distribuição</h2></div>
		<form action="Administrador" method="post" name="Formulario" id="Formulario">
	
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="Pagina" name="Pagina" type="hidden" value="<%=request.getAttribute("Pagina")%>">
			<input id="Id_Sessao" name="Id_Sessao" type="hidden" value="<%=request.getAttribute("Id_Sessao")%>">
			
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			
			<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
				<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().trim()%></div>
			<%}%>
				
			<div id="divTabela" class="divTabela" > 
		  		<fieldset>
		  			<input type="image" src="./imagens/imgAtualizar.png" title="Atualizar a lista de Distribuição" onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarDWR%>');"> 
		  			<legend>Área de Distribuição - Ordem de Distribuição de Processos</legend>
		   	 		<table id="Tabela" class="Tabela">
						<thead>
						    <tr class="TituloColuna">
						       <th width="20%">Id_AreaDistribuicao</th>
						       <th>Serventias (Id_Serventia)</th>
						    </tr>
						</thead>
		           		<tbody id="tabListaUsuario">
						<%
						Map mapDistribuicao = (Map)request.getAttribute("mapDistribuicao");
						Iterator itDistribuicao = mapDistribuicao.keySet().iterator();
						while (itDistribuicao.hasNext()) {
							String idTipo = (String) itDistribuicao.next(); // Primeiro elemento
							List lista = (List) mapDistribuicao.get(idTipo);
						%>
						    <tr class="TabelaLinha1">
						       	<td><%= idTipo%></td>
						        <td><%= lista%></td>
					   		</tr>
						<%}%>	
						</tbody>
		       		</table>
				</fieldset>       
				
				<fieldset> 
					<input type="image" src="./imagens/imgAtualizar.png" title="Atualizar a lista de Distribuição" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');">
		  			<legend>ServentiaCargo - Ordem de Distribuição de Processos</legend>
		   	 		<table id="Tabela" class="Tabela">
						<thead>
						    <tr class="TituloColuna">
						       <th width="20%">Id_Serventia</th>
						       <th>Cargos Juízes, Relatores ou MP (Id_ServentiaCargo) </th>
						    </tr>
						</thead>
		           		<tbody id="tabListaUsuario">
						<%
						Map mapDistribuicaoServentiaCargo = (Map)request.getAttribute("mapDistribuicaoServentiaCargo");
						itDistribuicao = mapDistribuicaoServentiaCargo.keySet().iterator();
						while (itDistribuicao.hasNext()) {
							String idTipo = (String) itDistribuicao.next(); // Primeiro elemento
							List lista = (List) mapDistribuicaoServentiaCargo.get(idTipo);
						%>
							<tr class="TabelaLinha1"> 
						       	<td><%= idTipo%></td>
						        <td><%= lista%></td>
					   		</tr>
						<%}%>	
						</tbody>
		       		</table>
				</fieldset>       
				
   			</div> 
	 			
	 	</form>
 	</div>
</body> 	
 </html>


