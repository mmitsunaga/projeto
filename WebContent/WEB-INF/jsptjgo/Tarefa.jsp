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
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
<title>|<%=request.getAttribute("tempPrograma")%>| Busca de Tarefa</title>
<style type="text/css">
	@import url('./css/Principal.css');
</style>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
<link type="text/css" rel="stylesheet" href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112"	media="screen"></link>
<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>

<script language="javascript" type="text/javascript">
		<%
			if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {
		%>
			function VerificarCampos() {
				with(document.Formulario) {
					if (SeNulo(Tarefa, "O Campo Tarefa é obrigatório!")) return false;
					if (SeNulo(Previsao, "O Campo Previsao é obrigatório!")) return false;
					if (SeNulo(TarefaPrioridade, "O Campo Prioridade de Tarefa é obrigatório!")) return false;
					if (SeNulo(TarefaTipo, "O Campo Tipo de Tarefa é obrigatório!")) return false;
					if (SeNulo(Projeto, "O Campo Projeto é obrigatório!")) return false;
					submit();
				}
			}
		<%
			}
		%>

		//----------------------------------------------
		//---- SCRIPTS DE RENDERIZAÇÃO
		//----------------------------------------------
		function alterarParametrosSubmit(valor){
		 	document.getElementById("PRMAcoes").value = valor;
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
	
	<!-- Atributo [ControlaRenderizacao] responsável por 
		 gerenciar os estado internos do formulário: 
		    1 -Adicionar SubTarefa, 2 - Atribuir Tarefa  -->
		    
	<input id="PRMAcoes" name="PRMAcoes" type="hidden" />
	<input id="PRMPaginaRetorno" name="PRMPaginaRetorno" type="hidden" value="8"/>
	<input id="PRMAlterarResponsavel" name="PRMAlterarResponsavel" type="hidden" value="<%=request.getAttribute("PRMAlterarResponsavel") %>" />
	
	<div id="divPortaBotoes" class="divPortaBotoes">
			<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
			<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />  
			<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
			<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')" />
			
			<!--  O Link de criar sub-tarefa somente 
			      será apresentado caso existe ID de 
			      tarefa. -->
			      
			<% if(Tarefadt.getId() != null && !Tarefadt.getId().isEmpty()){ %>
			<input id="imgSubTarefa" alt="Criar Sub-Tarefa" class="imgAtualizar" title="Criar Sub-Tarefa" name="imaAtualizar" type="image"  src="./imagens/imgEditar.png"  onclick="alterarParametrosSubmit(1);AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
			<%} %>

			<!--  Link que permite atribuir a tarefa a 
			      um participante do projeto, somente se a
			      tarefa estiver com situação ABERTA e não possuir
			      nenhum responsável. -->
			
			<%
			boolean ehGerenteTi = false;     
			if (request.getSession().getAttribute("UsuarioSessaoDt") != null){	       		
				ehGerenteTi = (Funcoes.StringToInt(((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).getGrupoCodigo()) == GrupoDt.GERENTE_TI);
   	  		}
   	  	     
			 if(ehGerenteTi && Tarefadt.getTarefaStatusCodigo().equals(TarefaStatusDt.ABERTA.toString()) 
					&& (Tarefadt.getId_ProjetoParticipanteResponsavel() == null
						|| Tarefadt.getId_ProjetoParticipanteResponsavel().isEmpty())){ %>
			<input id="imgAtribuirTarefa" alt="Atribuir Tarefa" class="imgAtualizar" title="Atribuir Tarefa" name="imaAtualizar" type="image"  src="./imagens/16x16/edit-redo.png"  onclick="alterarParametrosSubmit(2);AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
			<%} %>
	</div>
	<div id="divEditar" class="divEditar">
		<fieldset id="VisualizaDados" class="VisualizaDados">
			<legend> Edita os dados Tarefa </legend>
			
			<label for="Id_Tarefa"  class="formEdicaoLabel">Id_Tarefa</label><br>
			<input class="formEdicaoInputSomenteLeitura" name="Id_Tarefa" id="Id_Tarefa" type="text" readonly="true" value="<%=Tarefadt.getId()%>">
			<br/>
			
			<label for="Tarefa" class="formEdicaoLabel">*Tarefa</label><br>
			<input class="formEdicaoInput" name="Tarefa" id="Tarefa" type="text" size="60" maxlength="60" value="<%=Tarefadt.getTarefa()%>" onkeyup=" autoTab(this,60)">
			<br/>
			
			<label for="Previsao" title="A Medida é em Horas" class="formEdicaoLabel">*Previsão (horas)</label><br>
			<input  class="formEdicaoInput" name="Previsao" id="Previsao" type="text" size="11" maxlength="11" value="<%=Tarefadt.getPrevisao()%>" 
						onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
						
			<label for="PontosApf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*Apg[A]</label><br>
			<input 	class="formEdicaoInput" name="PontosApf" id="PontosApf" type="text" size="11" maxlength="11" value="<%=Tarefadt.getPontosApf()%>" 
						onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
			<label for="PontosApg">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*Apg[P]</label><br>
			<input 	class="formEdicaoInput" name="PontosApg" id="PontosApg" type="text" size="11" maxlength="11" value="<%=Tarefadt.getPontosApg()%>"
						onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
			<br/>
			
			
			<label for="Id_TarefaPrioridade" class="formEdicaoLabel">*Prioridade
			<input class="FormEdicaoimgLocalizar" id="imaLocalizarTarefaPrioridade" name="imaLocalizarTarefaPrioridade" type="image" src="./imagens/imgLocalizarPequena.png" 
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TarefaPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')"/>
			</label><br>
			<input class="formEdicaoInputSomenteLeitura" readonly="true" name="TarefaPrioridade" id="TarefaPrioridade" type="text" size="50" maxlength="60" value="<%=Tarefadt.getTarefaPrioridade()%>"/>
			<% if(Tarefadt.getDataInicio() != null && !Tarefadt.getDataInicio().isEmpty()){ %>
			<br><br><label>Data Início</label><br>
				<b><%= Tarefadt.getDataInicio()%></b>
			<% } %>
			<% if(Tarefadt.getDataFim() != null && !Tarefadt.getDataFim().isEmpty()){ %>
			<br><br><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Fim</label><br>
				<b><%= Tarefadt.getDataFim()%></b>
			<% } %>
			<br/>
			
			<label for="Id_TarefaTipo" class="formEdicaoLabel">*Tipo da Tarefa
			<input class="FormEdicaoimgLocalizar" id="imaLocalizarTarefaTipo" name="imaLocalizarTarefaTipo" type="image" src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TarefaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
			</label><br>
			<input class="formEdicaoInputSomenteLeitura" readonly="true" name="TarefaTipo" id="TarefaTipo" type="text" size="50" maxlength="60" value="<%=Tarefadt.getTarefaTipo()%>">

			<% if(!Tarefadt.getId().isEmpty()){ %>
			<br><br>
			<label for="Id_TarefaStatus">*Status da Tarefa
			<input class="FormEdicaoimgLocalizar" id="imaLocalizarTarefaStatus" name="imaLocalizarTarefaStatus" type="image" src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TarefaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')"/>
			</label><br>
			<input class="formEdicaoInputSomenteLeitura" readonly="true" name="TarefaStatus" id="TarefaStatus" type="text" size="40" maxlength="60" value="<%=Tarefadt.getTarefaStatus()%>"/>
			<% } %>
			<br/>
			
			<label for="Id_Projeto" class="formEdicaoLabel">*Projeto
			<input class="FormEdicaoimgLocalizar" id="imaLocalizarProjeto" name="imaLocalizarProjeto" type="image" src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProjetoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')"/>
			</label><br>
			<input class="formEdicaoInputSomenteLeitura" readonly="true" name="Projeto" id="Projeto" type="text" size="50" maxlength="50" value="<%=Tarefadt.getProjeto()%>"/>
			
			<!-- O Participante responsável só pode ser alterado se a tarefa ainda não possuir um responsável ou se o usuario possuir
				   permissão para alterar este campo. -->
			<% if(!Tarefadt.getId().isEmpty()){ %>
			<br><br>
			<label for="Id_ProjetoParticipanteResponsavel">Responsável
			<% } %>
			<%if(ehGerenteTi && (request.getAttribute("PRMAlterarResponsavel") != null && ((Boolean)request.getAttribute("PRMAlterarResponsavel"))) 
					|| (ehGerenteTi && Tarefadt.getTarefaStatusCodigo() != null && Tarefadt.getTarefaStatusCodigo().equals(TarefaStatusDt.EM_ANDAMENTO.toString()))){ %>
			<input class="FormEdicaoimgLocalizar" alt="Trocar responsável" id="imaLocalizarProjetoParticipante" name="imaLocalizarProjetoParticipante" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProjetoParticipanteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
			
			<%} %>
			</label><br>
			<% if(!Tarefadt.getId().isEmpty()){ %>
			<input  class="formEdicaoInputSomenteLeitura" readonly="true" name="ProjetoParticipanteResponsavel" id="ProjetoParticipanteResponsavel" type="text" size="50" maxlength="255" value="<%=Tarefadt.getProjetoParticipanteResponsavel()%>">
			<% } %>						
			<br/>
			
			
		</fieldset>
		<div id="Editor" class="Editor" style="overflow:auto; height: 250px; width: 100%;">
			<textarea class="ckeditor" cols="20" id="Descricao" name="Descricao" rows="5">
				<%=Tarefadt.getDescricao()%>
			</textarea>
		</div>
		

<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

</div>

</form>
</div>
<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
