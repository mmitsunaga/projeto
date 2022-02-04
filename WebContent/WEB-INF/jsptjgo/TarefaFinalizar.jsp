<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Tarefadt" scope="session" class="br.gov.go.tj.projudi.dt.TarefaDt" />

<%@page import="br.gov.go.tj.projudi.dt.TarefaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaTipoDt"%>
<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
<title>|<%=request.getAttribute("tempPrograma")%>| Consulta de Dados de Tarefa Não Finalizada</title>
<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	
</head>
<body>
<div id="divCorpo" class="divCorpo">
<div class="area"><h2>&raquo; Consulta de Dados de Tarefa Não Finalizada</h2></div>
	<form action="VistoTarefa" method="post" name="Formulario" id="Formulario">
	<input id="PaginaAtual" name="PaginaAtual" 	type="hidden"	value="<%=request.getAttribute("PaginaAtual")%>" /> 
	<input	name="__Pedido__" type="hidden"		value="<%=request.getAttribute("__Pedido__")%>" /> 
	<input	name="TituloPagina" type="hidden" 	value="<%=request.getAttribute("tempTituloPagina")%>" />
	
	<div id="divPortaBotoes" class="divPortaBotoes">
		<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
	</div>
	
	<div id="divEditar" class="divEditar">
		<fieldset id="VisualizaDados" class="VisualizaDados">
			<legend> Atualizar Tarefa </legend>
			<div> Id_Tarefa</div>
			<span class="span1"><%=Tarefadt.getId()%></span>
			<br/>
			<div><label for="Tarefa">*Tarefa</label><br></div>
			<span class="span1"><%=Tarefadt.getTarefa()%>
			</span>
			<br/>
			<div><label for="Previsao">*Previsão</label><br></div>
			<span class="span1"><%=Tarefadt.getPrevisao()%></span>
			<br/>
			
			<div><label for="PontosApf">*Apg[A]</label><br></div>
			<span class="span1"><%=Tarefadt.getPontosApf()%></span>
			<div><label for="PontosApg">*Apg[P]</label><br></div>
			<span class="span2"><%=Tarefadt.getPontosApg()%></span>
			<br/>
			<div><label>Data Início</label><br></div>
			<span class="span1"><%= Tarefadt.getDataInicio()%></span>
			<% if(Tarefadt.getDataFim() != null && !Tarefadt.getDataFim().isEmpty()){ %>
			<div><label>Data Fim</label><br></div>
			<span class="span2"><%= Tarefadt.getDataFim()%></span>
			<br/>
			<% } %>
			
			<div><label for="Id_TarefaPai">Tarefa Pai</label><br></div>
			<span class="span1"><%=Tarefadt.getTarefaPai()%>
			</span>
			<div><label for="Id_TarefaPrioridade">*Prioridade</label><br></div>
			<span class="span3"><%=Tarefadt.getTarefaPrioridade()%></span>
			<br/>
			
			<div><label for="Id_TarefaStatus">*Status da Tarefa</label><br></div>
			<span class="span1"><%=Tarefadt.getTarefaStatus()%></span>
			
			<div><label for="Id_TarefaTipo">*Tipo da Tarefa</label><br></div>
			<span class="span3"><%=Tarefadt.getTarefaTipo()%></span>
			<br/>
			
			<div><label for="Id_Projeto">*Projeto</label><br></div>
			<span class="span1"><%=Tarefadt.getProjeto()%></span>
			
			<div><label for="Id_ProjetoParticipanteResponsavel">Responsável</label><br></div>
			<span class="span3"><%=Tarefadt.getProjetoParticipanteResponsavel()%></span>
			<br/>

			<div><label for="Descricao">Descrição:</label><br></div>
			<span class="span1">
				<div id="Editor" class="Editor" style="overflow:auto; height: 50px; text-align : left; width: 100%; background-color : white;">
						<%=Tarefadt.getDescricao()%>
				</div>
			</span>
			<br/><br/>
			
			<div><label for="Resposta">Resposta:</label><br></div>
			<span class="span1">
				<div id="Editor" class="Editor" style="overflow:auto; height: 50px; text-align : left; width: 100%; background-color : white;">
					<%=Tarefadt.getResposta()%>
				</div>
			</span>
			<br/>
			
		</fieldset>
<%@ include file="Padroes/Mensagens.jspf"%>
</div>
</form>
</div>
</body>
</html>
