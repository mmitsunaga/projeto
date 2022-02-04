<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
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
           
      <%@ include file="./js/MovimentacaoProcesso.js" %>
      
</head>

	<body onload="atualizarPendencias();">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
			<!-- Variáveis para controlar Passos da Análise -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AnalisePendenciadt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=AnalisePendenciadt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AnalisePendenciadt.getPasso3()%>">
		
			<div id="divEditar" class="divEditar">
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','1');">
				<% } if (!AnalisePendenciadt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AnalisePendenciadt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
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
					<% 
							}
						} else {
							if(pendenciasFechar != null && pendenciasFechar.size()>0) {
								PendenciaDt pendencia = (PendenciaDt) pendenciasFechar.get(0);
								ProcessoDt processoDt = pendencia.getProcessoDt();
					%>
					<label class="formEdicaoLabel"> Processo N&uacute;mero</label><br>
					<span class="spanDestaque"><a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
					<% 		}
						} %>
					<br />
					
					<label class="formEdicaoLabel">Tipo Movimentação </label><br>  
					<span class="spanDestaque"><%=AnalisePendenciadt.getMovimentacaoTipo()%></span>
					<br />
					<label class="formEdicaoLabel">Classificador de Processo</label><br>  
					<span class="spanDestaque"><%=AnalisePendenciadt.getClassificador()%></span>
					<br />
					
					<% String segundoGrau = (String) request.getAttribute("SegundoGrau"); %>
					<%if (AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal() != null && segundoGrau != null && segundoGrau.equalsIgnoreCase("true")){ %>
						<label class="formEdicaoLabel">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br>  
						<span class="destaque"><%=AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")?"Sim":"Não"%></span>
						<br />
					<%} %>

					<fieldset class="formLocalizar">	
						<legend>Pré-Análise</legend>
						<div id="divTextoEditor" class="divTextoEditor" style="width: 940px; float:left">
							<%=AnalisePendenciadt.getTextoEditor() %>
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
						<input name="imgConcluir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');" />
						<%
							if (UsuarioSessao.isPodeExibirPendenciaAssinatura(AnalisePendenciadt.isMultipla(), Funcoes.StringToInt(AnalisePendenciadt.getId_PendenciaTipo()))){
						%>						
						<input name="imgGuardar" type="submit" value="Guardar para assinar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','2');" />
						<%} %>
					</div>
				</fieldset>
			</div>
		</form>
 	</div>	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>