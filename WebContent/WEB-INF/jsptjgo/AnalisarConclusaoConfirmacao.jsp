<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<title>Analisar Autos Conclusos</title>	
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

	<body onload="atualizarPendencias(); atualizarArquivos('false'); calcularTamanhoIframe();">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input id="TituloPagina" name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">	
			<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" value="">
			<input id="pendencia" name="pendencia" type="hidden" value="">
			<input id="CodigoPendencia" name="CodigoPendencia" type="hidden" value="">
			<input id="preAnaliseConclusao" name="preAnaliseConclusao" type="hidden" value="">						
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
					<legend>Confirmação Análise de Autos Conclusos</legend>
					
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
							
					<label class="formEdicaoLabel">Classificador</label><br>  
					<span class="spanDestaque"><%=(!AnalisePendenciadt.getClassificador().equals("")?AnalisePendenciadt.getClassificador():"Nenhum")%></span>
					<br />
					
					<%if (AnalisePendenciadt.getPedidoAssistencia() != null && AnalisePendenciadt.getPedidoAssistencia().trim().length() > 0) {%>
						<label class="formEdicaoLabel">Pedido Assistência </label><br>
							<%if (AnalisePendenciadt.getPedidoAssistencia().equalsIgnoreCase("1")){ %>
								<span class="spanDestaque"> Deferido</span>
							<%} else if (AnalisePendenciadt.getPedidoAssistencia().equalsIgnoreCase("0")){ %>
								<span class="spanDestaque">Indeferido</span>
							<%}else if (AnalisePendenciadt.getPedidoAssistencia().equalsIgnoreCase("2")){ %>
								<span class="spanDestaque">Diligência</span>
							<%} %>
						 <br />
					<%}%>
					
					<br />
					<input class="destaque" name="naoGerarVerificarProcesso"  disabled="true" type="checkbox" value="true" <% if(AnalisePendenciadt.isNaoGerarVeficarProcesso()){%>  checked<%}%>>
					<span class="destaque"> Não Gerar Pendência "Verificar Processo" de forma automática</span/><br />
					
					<% String segundoGrau = (String) request.getAttribute("SegundoGrau"); %>
					<%if (AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal() != null && segundoGrau != null && segundoGrau.equalsIgnoreCase("true")){ %>
						<label class="formEdicaoLabel">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br>  
						<span class="destaque"><%=AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")?"Sim":"Não"%></span>
						<br />
					<%} %>
					
					<% if (AnalisePendenciadt.getListaArquivos() == null || AnalisePendenciadt.getListaArquivos().size() == 0 || (AnalisePendenciadt.getTextoEditor() != null && AnalisePendenciadt.getTextoEditor().trim().length() > 0) ){ %>
						<fieldset class="formLocalizar">	
							<legend>Texto Pré-Análise  <input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
							                                          onclick="MostrarOcultar('divTextoEditor'); return false;" title="Abrir Editor de Texto" />
							</legend>			
							<div id="divTextoEditor" class="divTextoEditor" style="display:block;">
								<%=AnalisePendenciadt.getTextoEditor()%>
							</div>
						</fieldset>
					<% } else { %>
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
					<% } %>
							
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
						<% if (AnalisePendenciadt.getListaArquivos() != null && AnalisePendenciadt.getListaArquivos().size() > 0){ %>
							<input name="imgConcluir" type="submit" value="Concluir Análise" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');">
						<% } else { %>
							<%
								if (UsuarioSessao.isPodeExibirPendenciaAssinatura(AnalisePendenciadt.isMultipla(), Funcoes.StringToInt(AnalisePendenciadt.getId_PendenciaTipo()))){
							%>													
								<input name="imgGuardar" type="submit" value="Guardar para Assinar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','2');" />								
							<%
																}
															%>
							<input name="imgConcluirPreanalise" type="submit" value="Salvar Pré-Análise" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','1');" />
							<%
								if (AnalisePendenciadt.isPreAnalise()) {
							%>
								<input name="imgDescartarPreanalise" type="submit" value="Descartar Pré-Análise" onclick="AlterarAction('Formulario','PreAnalisarConclusao');AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Id_Pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('preAnaliseConclusao','false');AlterarValue('FluxoEditar','');" />
								<input name="imgClassificar" type="submit" value="Classificar" onclick="AlterarAction('Formulario','DescartarPendenciaProcesso');AlterarValue('PaginaAtual','<%=Configuracao.LocalizarDWR%>');AlterarValue('Id_Pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('preAnaliseConclusao','true');AlterarValue('FluxoEditar','');" />
							<%
								}
							%>
						<%
							}
						%>
						<input name="imgDescartarConclusao" type="submit" value="Descartar Conclusão" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');" />
						<%--if (UsuarioSessao.isPodeTrocarResponsavel()){ --%>

						<% if (UsuarioSessao.isPodeTrocarResponsavelDistribuicao()){ %>
							<input name="imgDistribuirConclusao" type="submit" value="Distribuir Conclusão" onclick="AlterarAction('Formulario','PendenciaResponsavel');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('CodigoPendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getHash()%>');" />
						<%}%>
						<% if (UsuarioSessao.getVerificaPermissao(3946)){ %>						
							<input name="imgDevolverConclusao" type="submit" value="Devolver" alt='Devolve a conclusão para o assessor' title='Devolve a conclusão para o assessor' onclick="AlterarAction('Formulario','PendenciaResponsavel'), AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');  AlterarValue('pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('CodigoPendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getHash()%>');" />
						<%}%>
					</div>
				</fieldset>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	
</body>
</html>