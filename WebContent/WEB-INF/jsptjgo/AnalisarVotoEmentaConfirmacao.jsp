<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<title>Analisar Voto Ementa</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>       	
	
	<%@ include file="./js/MovimentacaoProcesso.js" %>		
	<%@ include file="js/InsercaoArquivo.js"%>
</head>

	<body onload="atualizarPendencias(); atualizarArquivos('false');">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">	
			<!-- Variáveis para controlar Passos da Análise -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AnalisePendenciadt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=AnalisePendenciadt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AnalisePendenciadt.getPasso3()%>">
		
			<div id="divEditar" class="divEditar">
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','1');">
				<% } if (!AnalisePendenciadt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AnalisePendenciadt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<% } %>
				
				<fieldset class="formEdicao">
					<legend>Confirmação Análise de Voto Ementa</legend>
					
					<%
						List pendenciasFechar = AnalisePendenciadt.getListaPendenciasFechar();
						if (pendenciasFechar != null && pendenciasFechar.size() > 0){
							%>
							<label class="formEdicaoLabel"> Processo(s) Selecionado(s)</label><br>
							<%
							for (int i=0;i<pendenciasFechar.size();i++){
								PendenciaDt objPendencia = (PendenciaDt)pendenciasFechar.get(i);
					%>
							<span class="destaque"><%=objPendencia.getProcessoNumero()%></span>
					<% 
							}
						}
					%>
					<br />
									
					<label class="formEdicaoLabel">Tipo Movimentação </label><br>  
					<span class="destaque"><%=AnalisePendenciadt.getMovimentacaoTipo()%></span>
					<br />
							
					<label class="formEdicaoLabel">Classificador de Processo</label><br>  
					<span class="destaque"><%=AnalisePendenciadt.getClassificador()%></span>
					<br />
					
					<label class="formEdicaoLabel">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br>  
					<span class="destaque"><%=AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")?"Sim":"Não"%></span>
					<br />
					
					<fieldset class="formLocalizar"> 	
						<legend>Arquivos Inseridos</legend>
						<div id="divTabela" class="divTabela">
							<table class="Tabela">
								<thead>
							    	<tr>
							      		<th width="40%">Descrição</th>
							      		<th width="40%">Nome</th>			  
							    	</tr>
							  	</thead>
							  	<% if (AnalisePendenciadt.getListaArquivos() != null && AnalisePendenciadt.getListaArquivos().size() > 0){ %>
							  	<tbody id="corpoTabelaArquivo">
									<tr id="idArquivo" style="display:none;">
									  	<td><span id="tableDescricao">Descrição</span> </td>
								      	<td><span id="tableNome">Nome Arquivo</span></td>			  	  							    
								    </tr>
							  	</tbody>
							  	<% } else { %>
								<tbody>
									<tr>
										<td><em>Nenhum Arquivo inserido.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>
						</div>	
					</fieldset>
							
					<fieldset class="formLocalizar"> 	
						<legend>Lista das pendências</legend>
						<div id="divTabela" class="divTabela">
							<table class="Tabela">
								<thead>
							    	<tr>
							      		<th>Tipo</th>
							      		<th>Destinatário</th>
								  		<th style="display:none">Serventia/Usuário</th>
								  		<th>Prazo</th>
								  		<th class="colunaMinima">Urgente</th>
									  	<th class="colunaMinima">Intimação</th>
							    	</tr>
							  	</thead>
							  	<% if (AnalisePendenciadt.getListaPendenciasGerar() != null && AnalisePendenciadt.getListaPendenciasGerar().size() > 0){ %>
							  	<tbody id="corpoTabela">
							    	<tr id="identificador" style="display:none;">
							      		<td><span id="tableTipo">Tipo</span> </td>
							      		<td><span id="tableDestinatario">Destinatário</span></td>
							  	  		<td style="display:none"><span id="tableSerUsu">Usuário/Serventia</span></td>
							     		<td><span id="tablePrazo">Prazo</span></td>
								  		<td><span id="tableUrgente">Urgente</span></td>
								  		<td><span id="tableIntimacao">PessoalAdvogados</span></td>
							  	  		<td style="display:none"><span id="tableOnLine">Online</span></td>
							    	</tr>
							  	</tbody>
							  	<% } else { %>
								<tbody>
									<tr>
										<td><em>Nenhuma Pendência será gerada.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>
						</div>				
					</fieldset>
					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');">
					</div>
				</fieldset>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	
</body>
</html>