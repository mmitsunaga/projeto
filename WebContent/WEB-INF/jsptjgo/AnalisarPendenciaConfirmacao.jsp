<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<title>Analisar Pendência(s)</title>	
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
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">	
		
			<div id="divEditar" class="divEditar">
				<%
					boolean podeAnalisar_preAnalisar = GrupoTipoDt.podeAnalisarOuPreanalisarVotoEmenta(String.valueOf(request.getAttribute("GrupoTipoUsuarioLogado")));
				%>
			
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<% } %>
				
				<fieldset class="formEdicao">
					<legend>Confirmação Análise de Pendência(s)</legend>
					
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
							
					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Concluir Análise" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');">
						
						<%
													if (UsuarioSessao.isPodeExibirPendenciaAssinatura(AnalisePendenciadt.isMultipla(), Funcoes.StringToInt(AnalisePendenciadt.getId_PendenciaTipo()))){
												%>
												
						<input name="imgGuardar" type="submit" value="Guardar para Assinar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','2');" />
						<%
							String textoBotaoSalvar = "Salvar";
							if (podeAnalisar_preAnalisar) {
								textoBotaoSalvar = "Salvar Pré-Análise";
							}
						%>
						<input name="imgConcluirPreanalise" type="submit" value="<%=textoBotaoSalvar%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','1');" />
						<%} %>
					</div>
				</fieldset>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	
</body>
</html>