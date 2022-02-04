<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="AudienciaMovimentacaoDt" class= "br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Finalizar Sessão de 2º Grau</title>
		
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>      
		<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type="text/javascript" src="./js/tabelas.js"></script>
		<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
	</head>
	
	<body >
		<div class="divCorpo">
			<form method="post" action="AudienciaMovimentacao" id="Formulario">
				<div class="area"><h2>&raquo; Finalizar Sessão de 2º Grau</h2></div>
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				
			
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Finalizar Sessão de 2º Grau</legend>
						
								
						<label class="formEdicaoLabel"> Data </label><br>
						<span class="spanDestaque"> <%=AudienciaMovimentacaoDt.getAudienciaDt().getDataAgendada()%> </span>
						<br />
						<div id="insercao">
							<%@ include file="Padroes/InsercaoArquivosAssinador.jspf" %>
						</div>
						
						<br />	
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgFinalizar" type="submit" value="Finalizar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
							<input name="imgCancelar" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>
				</div>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>
		<%@include file="Padroes/Mensagens.jspf"%>
		</div>

	</body>
</html>