<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Tarefadt" scope="session" class="br.gov.go.tj.projudi.dt.TarefaDt" />

<%@page import="br.gov.go.tj.projudi.dt.TarefaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProjetoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProjetoParticipanteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
<title>|<%=request.getAttribute("tempPrograma")%>| Atualizar Tarefa</title>
<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
<link type="text/css" rel="stylesheet" href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112"	media="screen"></link>
<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
<script type='text/javascript' src='./js/jquery.js'></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 

<%
	/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/
%>

<script language="javascript" type="text/javascript">
		<%
			if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {
		%>
			function VerificarCampos() {
				with(document.Formulario) {
					if (SeNulo(Resposta, "O Campo Resposta é obrigatório!")) return false;
					submit();
				}
			}
		<%
			}
		%>

		//----------------------------------------------
		//---- ATUALIZA O PARAMETRO DE NAVEGAÇÃO UTILIZADO 
		//---- NO CURINGA 6
		//----------------------------------------------
		function setPrmNavegacao(prmNavegacao){
		 	document.getElementById("PRMNavegacao").value = prmNavegacao;
		}
		
		//----------------------------------------------
		//---- DIRECIONA PARA A PÁGINA DE LISTAGEM DE TAREFAS
		//---- DO USUÁRIO
		//----------------------------------------------
		function listarTarefasUsuario(prmNavegacao, PaginaAtual){
			setPrmNavegacao(prmNavegacao);
		 	AlterarValue('PaginaAtual', PaginaAtual);
		}

		//------------------------------------
		//-- EXECUTA A AÇÃO DE SOLUCIONAR TAREFA
		//------------------------------------
		function solucionarTarefa(prmNavegacao, PaginaAtual){
			var resposta = document.getElementById("Resposta").value;
			if(resposta == ""){
				alert("O Campo Resposta é obrigatório!");
				document.getElementById("Resposta").focus();
				return;
			}
			var answer = confirm("Tarefa será Solucionada, Confirma ?");
			if (answer){
				setPrmNavegacao(prmNavegacao);
			 	AlterarValue('PaginaAtual', PaginaAtual);
			}
		}
</script>
</head>
<body>
<div id="divCorpo" class="divCorpo">
<div class="area"><h2>&raquo; Cadastro de Tarefa</h2></div>
<%
	if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {
%>
<form action="Tarefa" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%
	} else {
%>
<form action="Tarefa" method="post" name="Formulario" id="Formulario">
<%
	}
%> 	
	<input id="PaginaAtual" name="PaginaAtual" 	type="hidden"	value="<%=request.getAttribute("PaginaAtual")%>" /> 
	<input	name="__Pedido__" type="hidden"		value="<%=request.getAttribute("__Pedido__")%>" /> 
	<input	name="TituloPagina" type="hidden" 	value="<%=request.getAttribute("tempTituloPagina")%>" />
	
	<!-- Parametro indicando 
		 a pesquisa de tarefas -->
	<input type="hidden" id="PRMNavegacao" name="PRMNavegacao" value="<%=request.getAttribute("PRMNavegacao")%>"/>
	
	<div id="divPortaBotoes" class="divPortaBotoes">
		<input id="imgLocalizar" 		alt="Localizar" 	class="imgLocalizar" 	title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="listarTarefasUsuario('8','<%=String.valueOf(Configuracao.Curinga6)%>')" />
		<%
			if (Tarefadt.getTarefaStatusCodigo().equals(TarefaStatusDt.EM_ANDAMENTO.toString())) {
		%> 
		<input id="imgSolucionar" 		alt="Solucionar" 	class="imgLocalizar" 	title="Solucionar Tarefa" name="imaLocalizar" type="image"  src="./imagens/16x16/edit-redo.png"  onclick="solucionarTarefa('2','<%=String.valueOf(Configuracao.Curinga6)%>')" />
		<%
			}
		%> 
		 
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
			
			<!--   O Participante responsável só pode ser alterado se
				   a tarfa ainda não possuir um responsável ou se o usuario possuir
				   permissão para alterar este campo. -->
			
			<div><label for="Id_ProjetoParticipanteResponsavel">Responsável</label><br></div>
			<span class="span3"><%=Tarefadt.getProjetoParticipanteResponsavel()%></span>
			<br/>

			<div><label for="Descricao">Descrição:</label><br></div>
			<span class="span1">
				<div id="Editor" class="Editor" style="overflow:auto; height: 50px; text-align : left; width: 100%; background-color : white;">
						<%=Tarefadt.getDescricao()%>
				</div>
			</span>
			<br/>
			
			<div><label for="Resposta">Resposta:</label><br></div>
		</fieldset>
		<div id="Editor" class="Editor" style="overflow:auto; height: 250px; width: 100%;">
			<textarea class="ckeditor" cols="20" id="Resposta" name="Resposta" rows="5">
				<%=Tarefadt.getResposta()%>
			</textarea>
		</div>

<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

</div>

</form>
</div>
<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
