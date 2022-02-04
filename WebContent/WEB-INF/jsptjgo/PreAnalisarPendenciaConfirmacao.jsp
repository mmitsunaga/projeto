<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<title>Movimentar Processo</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      	  
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>

	<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
		
			<div id="divEditar" class="divEditar">
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','1');">
				<% } %>
				
				<fieldset class="formEdicao">
					<legend>Confirmação de Dados</legend>
					<%
						List pendenciasFechar = AnalisePendenciadt.getListaPendenciasFechar();
						if (pendenciasFechar != null && pendenciasFechar.size() > 0){
							%>
							<label class="formEdicaoLabel"> Processos Selecionados</label><br>
							<%
							for (int i=0;i<pendenciasFechar.size();i++){
								PendenciaDt objPendencia = (PendenciaDt)pendenciasFechar.get(i);
					%>
							<span class="spanDestaque"><%=objPendencia.getProcessoNumero()%></span>
							<%}
						} %>
					<br />
					
					<label class="formEdicaoLabel">Tipo Movimentação </label><br>  
					<span class="spanDestaque"><%=AnalisePendenciadt.getMovimentacaoTipo()%></span>
					<br />
					
					<%if (AnalisePendenciadt.getComplementoMovimentacao() != null && AnalisePendenciadt.getComplementoMovimentacao().trim().length() > 0) {%>
					<label class="formEdicaoLabel"> Descrição Movimentação </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getComplementoMovimentacao()%></span>
					<br />
					<%}%>

					<fieldset class="formLocalizar">	
						<legend>Pré-Análise</legend>
						<div id="divTextoEditor" class="divTextoEditor">
							<%=AnalisePendenciadt.getTextoEditor() %>
						</div>
					</fieldset>
							
					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');" />
						<%
							if (UsuarioSessao.isPodeExibirPendenciaAssinatura(AnalisePendenciadt.isMultipla(), Funcoes.StringToInt(AnalisePendenciadt.getId_PendenciaTipo()))){
						%>						
							<input name="imgGuardar" type="submit" value="Guardar para assinar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('PassoEditar','2');" />
						<%} %>
					</div>
				</fieldset>
			</div>
		</form>
 	</div>	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>