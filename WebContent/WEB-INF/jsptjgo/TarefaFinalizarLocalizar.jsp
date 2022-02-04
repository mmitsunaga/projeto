<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProjetoDt"%>
<jsp:useBean id="Tarefadt" scope="session" class="br.gov.go.tj.projudi.dt.TarefaDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_Tarefa")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaTarefa")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	
	<%@ include file="./js/Paginacao.js"%> 
	<script type='text/javascript' src='./js/TarefaFinalizarLocalizar.js'></script>
	<script type='text/javascript' src='./js/buscaJson.js'></script>
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Consulta de Tarefas Não Finalizadas </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
		<div id="divLocalizar" class="divLocalizar" > 
			
			<fieldset id="formLocalizar" class="formLocalizar"  > 
			
				<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Dados da Tarefa</legend>
				
				<label id="formLocalizarLabel" class="formLocalizarLabel">Projeto
				<input class="FormEdicaoimgLocalizar" id="imaLocalizarProjeto" name="imaLocalizarProjeto" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProjetoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
				<input class="FormEdicaoimgLocalizar" id="imaLimparProjeto" name="imaLimparProjeto" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Projeto','Projeto'); return false;" />
				</label><br>
				<input id="Id_Projeto" name="Id_Projeto" type="hidden" value="<%=Tarefadt.getId_Projeto()%>"/>
				<input class="formEdicaoInputSomenteLeitura" name="Projeto" id="Projeto" readonly="true" type="text" size="60" maxlength="60" value="<%=Tarefadt.getProjeto()%>" />
				<br /><br />
				
				<label  id="formLocalizarLabel" class="formLocalizarLabel">Nome da Tarefa</label><br> 
				<input  id="nomeBusca" class="formLocalizarInput" name="nomeBusca" type="text" value="<%=Tarefadt.getTarefa()%>" maxlength="60" size="60" />
				<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="button" name="Localizar" value="Consultar" onclick="buscaDados('0',<%=Configuracao.TamanhoRetornoConsulta %>); return false;" />
				
			</fieldset>
			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Tarefa").toString()%>" name="<%=request.getAttribute("tempBuscaId_Tarefa").toString()%>" />
			<input type="hidden" id="<%=request.getAttribute("tempBuscaTarefa").toString()%>" name="<%=request.getAttribute("tempBuscaTarefa").toString()%>" />
			<input id="Id_Tarefa" name="Id_Tarefa" type="hidden" value=""/>
	
			<div id="divTabela" class="divTabela" > 
				<table id="Tabela" class="Tabela">
					<thead>
						<tr>
							<th></th>
							<th>Tarefa</th>
							<th>Situação</th>
							<th>Prioridade</th>
							<th>Projeto</th>
							<th>Selecionar</th>
						</tr>
						</thead>
					<tbody id="tabListaDados">
<%-- 		<%
				if(request.getAttribute("ListaTarefa") != null){
				  	List liTemp = (List)request.getAttribute("ListaTarefa");
				 	TarefaDt objTemp;
				  	boolean boLinha=false;
				  	if(liTemp!=null)
				  		for(int i = 0 ; i< liTemp.size();i++) {
			      			objTemp = (TarefaDt)liTemp.get(i); %>
							<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								<td > <%=i+1%></td>
			                   	<td onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Tarefa','<%=objTemp.getId()%>'); FormSubmit('Formulario');"><%= objTemp.getTarefa()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Tarefa','<%=objTemp.getId()%>'); FormSubmit('Formulario');"><%= objTemp.getTarefaStatus()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Tarefa','<%=objTemp.getId()%>'); FormSubmit('Formulario');"><%= objTemp.getTarefaPrioridade()%></td>
			                   	<td onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Tarefa','<%=objTemp.getId()%>'); FormSubmit('Formulario');"><%= objTemp.getProjeto()%></td>
			                   	<td class="Centralizado"><input name="formLocalizarimgEditar" type="image" style="align:center;" src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Tarefa','<%=objTemp.getId()%>'); FormSubmit('Formulario');"/>  </td>
							</tr>
						<%
							boLinha = !boLinha;
						}}%> --%>
					</tbody>
					
					
				</table>
			</div> 
		</div> 
<%-- 		<%@ include file="./Padroes/Paginacao.jspf"%>  --%>
            <div id="Paginacao" class="Paginacao"></div>
		</form> 
	</div> 
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
