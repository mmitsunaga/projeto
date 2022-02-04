<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Pré-Análise  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
		
    <%@ include file="./js/MovimentacaoProcesso.js" %>
    
    <script type="text/javascript">
    	function submeter(action, id_Pendencia, paginaAtual, preAnalise, fluxoEditar){
    		AlterarAction('Formulario', action);
    		AlterarValue('PaginaAtual', paginaAtual);
    		AlterarValue('Id_Pendencia', id_Pendencia);
    		AlterarValue('preAnaliseConclusao', preAnalise);
    		AlterarValue('FluxoEditar', fluxoEditar);	    		
    		document.Formulario.submit();
    	}
    </script>
</head>

<body onload="atualizarPendencias();">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Pré-Análise </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>" />
			<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" value="<%=request.getAttribute("Id_Pendencia")%>">
			<input id="preAnaliseConclusao" name="preAnaliseConclusao" type="hidden" value="<%=request.getAttribute("preAnaliseConclusao")%>">
			<input id="FluxoEditar" name="FluxoEditar" type="hidden" value="<%=request.getAttribute("FluxoEditar")%>">
			<input id="pendencia" name="pendencia" type="hidden" value="<%=request.getAttribute("pendencia")%>">	
			<input id="CodigoPendencia" name="CodigoPendencia" type="hidden" value="<%=request.getAttribute("CodigoPendencia")%>">		
						
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend> Dados Pré-Análise</legend>
					<table width="100%" >	
						<thead>	
							<tr>
								<th width="10%">Processo(s)</th>
								<th width="20%">Data </th>
								<th width="20%">Usuário </th>
								<th width="15%">Tipo Movimentação </th>  
								<th width="35%">Classificador</th> 								
							</tr>
						</thead>
						<tbody>			
							<tr>
								<td class="formEdicaoInputSomenteLeitura">
								<%	List pendencias = AnalisePendenciadt.getListaPendenciasFechar(); 
									for (int i=0;i<pendencias.size();i++){
										PendenciaDt objPendencia = (PendenciaDt)pendencias.get(i);	%>
										<%=objPendencia.getProcessoNumero()%>
								<%	} %>
								</td>															
								<td class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getDataPreAnalise()%></td>												
								<td class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getUsuarioPreAnalise()%></td>																		 
								<td class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getMovimentacaoTipo()%></td>												 
								<td class="formEdicaoInputSomenteLeitura"><%=(!AnalisePendenciadt.getClassificador().equals("")?AnalisePendenciadt.getClassificador():"Nenhum")%></td>																																						
							</tr>
						</tbody>
					</table>	
					
					<%if (AnalisePendenciadt.getComplementoMovimentacao() != null && AnalisePendenciadt.getComplementoMovimentacao().length()>0){ %>
						<div class="spanDestaque"> Complemento:<span class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getComplementoMovimentacao()%></span></div>
					<%} %>
					
					<%if (AnalisePendenciadt.getPedidoAssistencia() != null && AnalisePendenciadt.getPedidoAssistencia().trim().length() > 0) {%>
						<label class="formEdicaoLabel">Pedido Assistência </label><br>
							<%if (AnalisePendenciadt.getPedidoAssistencia().equalsIgnoreCase("1")){ %>
								<span class="spanDestaque"> Deferido</span>
							<%} else if (AnalisePendenciadt.getPedidoAssistencia().equalsIgnoreCase("0")){ %>
								<span class="spanDestaque">Indeferido</span>
							<%}else if (AnalisePendenciadt.getPedidoAssistencia().equalsIgnoreCase("2")){ %>
								<span class="spanDestaque">Diligência</span>
							<%} %>
					<%}%>
												
					<fieldset class="formLocalizar">	
						<legend> <input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png"
							                                          onclick="MostrarOcultar('divTextoEditor'); return false;" title="Texto (Esconder/Mostrar)" />
							                                          Texto Pré-Análise

							</legend>			
							<div id="divTextoEditor" class="divTextoEditor" style="display:block"> 
								<%=AnalisePendenciadt.getTextoEditor()%>
							</div>
					</fieldset>
					
					<div id="divLocalizar" class="divLocalizar" >		
						<fieldset class="formLocalizar"> 	
							<legend>Lista das pendências</legend>
							<div id="divTabela" class="divTabela" >
								<table class="Tabela">
									<thead>
								    	<tr>
								      		<th>Tipo</th>
								      		<th>Destinatário</th>
									  		<th style="display:none">Serventia/Usuário</th>
									  		<th>Prazo</th>
									  		<th class="colunaMinima">Urgente</th>
									  		<th style="display:none">on-line</th>
									  		<th class="colunaMinima">Pessoal/Advogados</th>
									  		<th width="15%">Outro(s)</th>
								    	</tr>
								  	</thead>
								  	<tbody id="corpoTabela">
								    	<tr id="identificador" style="display:none;">
								      		<td><span id="tableTipo">Tipo</span> </td>
								      		<td><span id="tableDestinatario">Destinatário</span></td>
								  	  		<td style="display:none"><span id="tableSerUsu">Usuário/Serventia</span></td>
								     		<td><span id="tablePrazo">Prazo</span></td>
									  		<td align="center" ><span id="tableUrgente">Urgente</span></td>
								  	  		<td style="display:none"><span id="tableOnLine">Online</span></td>
								  	  		<td align="center" ><span id="tablePessoalAdvogados">Pessoal/Advogados</span></td>
								  	  		<td><span id="tableIntimacao">Outro(s)</span></td>
								    	</tr>
								  	</tbody>
								</table>
							</div>				
						</fieldset>
					</div>
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<% if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
							<input name="imgAnalisarConclusao" type="submit" value="Analisar" onclick="javascript: submeter('AnalisarConclusao','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>','<%=Configuracao.Novo%>','true', '');return false;" />           						
               			<% } %>
               			
               			<input name="imgPreAnalisarConclusao" type="submit" value="Refazer" onclick="javascript: submeter('PreAnalisarConclusao', '<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>','<%=Configuracao.Novo%>','false', '');return false;" />	
               			
               			<% if(request.getAttribute("podeDescartarPreAnalise") == null || !request.getAttribute("podeDescartarPreAnalise").toString().equalsIgnoreCase("false")){ %>
               				<input name="imgDescartarPreAnalise" type="submit" value="Descartar" onclick="javascript: submeter('PreAnalisarConclusao', '<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>','<%=Configuracao.Excluir%>','false', '');return false;" /> 
	                   	<% } %>	                   		                   	
                   		<% if (UsuarioSessao.isPodeTrocarResponsavelDistribuicao()){ %>
                   			<input name="imgDistribuirConclusao" type="submit" value="Distribuir Conclusão" onclick="AlterarAction('Formulario','PendenciaResponsavel');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('CodigoPendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getHash()%>');" />                   			
                  		<%}	%>
                  		<% if (UsuarioSessao.getVerificaPermissao(3946)){ %>						
							<input name="imgDevolverConclusao" type="submit" value="Devolver" alt='Devolve a conclusão para o assessor' title='Devolve a conclusão para o assessor' onclick="AlterarAction('Formulario','PendenciaResponsavel'), AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');  AlterarValue('pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('CodigoPendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getHash()%>');" />
						<%}%>
                  		
                  		<input name="imgClassificar" type="submit" value="Classificar" onclick="javascript: submeter('DescartarPendenciaProcesso', '<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>','<%=Configuracao.LocalizarDWR%>','true', '');return false;" />                  		                  		
                  		
                  		<%if (UsuarioSessao.isPodeExibirPendenciaAssinatura(AnalisePendenciadt.isMultipla(), Funcoes.StringToInt(AnalisePendenciadt.getId_PendenciaTipo()))){%>                  		
                  			<input name="imgGuardar" type="submit" value="Guardar para Assinar" onclick="AlterarValue('tempFluxo1','2'); submeter('PreAnalisarConclusao', '<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>','<%=Configuracao.SalvarResultado%>','false', '2');return false;" />                  						                   																
						<% } %>						
					</div>					
				</fieldset>
			</div>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
