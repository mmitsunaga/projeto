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
<title>|<%=request.getAttribute("tempPrograma")%>| Pegar Tarefa</title>
<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
<link type="text/css" rel="stylesheet" href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112"	media="screen"></link>
<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>

<%
	/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/
%>

<script language="javascript" type="text/javascript">

		<%
		if (request.getAttribute("PRM_MENSAGEM") != null) {
		%>
			alert('<%= request.getAttribute("PRM_MENSAGEM").toString() %>');
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

		//------------------------------------
		//-- EXECUTA A AÇÃO DE SOLUCIONAR TAREFA
		//------------------------------------
		function pegarTarefa(prmNavegacao, PaginaAtual){
			var projeto = document.getElementById("Projeto").value;
			if(projeto == ""){
				alert("É necessário selecionar o Projeto.");
				document.getElementById("Resposta").focus();
				return;
			}
			var answer = confirm("Tarefa mais antiga do projeto "+projeto+" ficará sobre sua responsabilidade, Confirma ?");
			if (answer){
				setPrmNavegacao(prmNavegacao);
			 	AlterarValue('PaginaAtual', PaginaAtual);
			}
		}
</script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Pegar Tarefa</h2></div>
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
			<input type="hidden" id="PRMNavegacao" 		name="PRMNavegacao" 	value="<%=request.getAttribute("PRMNavegacao")%>"/>
			<input type="hidden" id="PRMPaginaRetorno" 	name="PRMPaginaRetorno" value="2"/>
					
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="pegarTarefa('4','<%=String.valueOf(Configuracao.Curinga6)%>')" />  
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend> Dados do Projeto </legend>
					<label for="Id_Projeto" class="formEdicaoLabel">*Projeto
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProjeto" name="imaLocalizarProjeto" type="image" src="./imagens/imgLocalizarPequena.png"
							onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProjetoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
					</label><br>
					<input 	class="formEdicaoInputSomenteLeitura" readonly="true" name="Projeto" id="Projeto" type="text" size="60" maxlength="60" value="<%=Tarefadt.getProjeto()%>"><br />
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
