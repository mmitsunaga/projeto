<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.ne.UsuarioNe"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="Peticionamentodt" scope="session" class="br.gov.go.tj.projudi.dt.PeticionamentoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<title>Peticionamento</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
    <%@ include file="js/InsercaoArquivo.js"%>
    
</head>

	<body onload="atualizarArquivos('false');">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">	
		
			<div id="divEditar" class="divEditar">
				<input type="image" class="imgVoltar" src="./imagens/imgVoltarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');" title="Voltar Página Anterior">
				<fieldset class="formEdicao">
					<legend>Confirmar o Peticionamento</legend>
					
					<%
						List processos = Peticionamentodt.getListaProcessos();
						if (processos != null && processos.size() > 0){
					%>
						<label class="formEdicaoLabel"> Processo(s) </label><br>
						<%
							for (int i=0;i<processos.size();i++){
								ProcessoDt objProcesso = (ProcessoDt)processos.get(i);
						%>
						<span class="spanDestaque"><%=objProcesso.getProcessoNumero()%></span>
					<% 
							}
						}
					%>
					<br />
					
					<label class="formEdicaoLabel">Tipo Movimentação </label><br>  
					<span class="spanDestaque"><%=Peticionamentodt.getMovimentacaoTipo()%></span>
					<br />
					<br />
									
					<fieldset class="formEdicao"> 	
						<legend>Arquivos Inseridos</legend>
						<div id="divTabela" class="divTabela">
							<table class="Tabela">
								<thead>
							    	<tr>
							      		<th width="40%">Descrição</th>
							      		<th width="40%">Nome</th>			  
							    	</tr>
							  	</thead>
							  
							  		<% if (Peticionamentodt.getListaArquivos() != null && Peticionamentodt.getListaArquivos().size() > 0){ %>
							  		<tbody id="corpoTabelaArquivo">
										<tr id="idArquivo" style="display:none;">
									  		<td><span id="tableDescricao">Descrição</span> </td>
								      		<td><span id="tableNome">Nome Arquivo</span></td>			  	  							    
								    	</tr>
								    </tbody>
								    <% } else { %>
								    <tbody>
								    	<tr>
									  		<td><em>Nenhum arquivo foi adicionado.</em></td>			  	  							    
								    	</tr>
							  		</tbody>								    
								    <% } %>
							</table>
						</div>	
					</fieldset>
							
					<br />
					
					<!--  Concluindo o envio da petição --><br />
					<input class="formEdicaoInput" name="pedidoUrgencia" type="checkbox" value="true" <% if(Peticionamentodt.isPedidoUrgencia()){%>  checked<%}%>>
					<span class="span"> Envolve pedido de urg&ecirc;ncia (tutelas, liminares, arrestos) </span/><br />
										
					<% if(UsuarioSessao.isMp()) {%>
						<input class="formEdicaoInput" name="pedidoSegredoJustica" type="checkbox" value="true" <% if(Peticionamentodt.isSegredoJustica()){%>  checked<%}%>>
						<span class="span"> Segredo de Justiça (Visibilidade apenas para Ministério Público, Cartório e Magistrado.) </span/><br />
					<%}%>
		
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');">
					</div>
				</fieldset>
			</div>
		</form>
 	</div>	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>