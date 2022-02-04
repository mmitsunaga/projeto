<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>


<%@page import="br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt"%><html>
	<head>
		<title>Eventos da Movimentação do Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
		</style>
       			     	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivosEventoExecucao.js'></script>
		<script type='text/javascript' src='./js/tabelaCondenacoesEventoExecucao.js'></script>
		<%@ include file="js/buscarArquivos.js"%>
		<script type='text/javascript' src='js/CondenacaoExecucao.js'></script>
		<script type="text/javascript">
			function buscarProcesso() {
				//apresentar os dados do Processo
				AlterarValue('PaginaAtual', '-1');
				AlterarAction('Formulario','BuscaProcesso');
				document.Formulario.submit();
			}
			function confirmaExclusao(mensagem, PaginaAtual, PassoEditar, posicao){
				if (confirm(mensagem)){
					if (PaginaAtual != '') AlterarValue('PaginaAtual', PaginaAtual);
					if (PassoEditar != '') AlterarValue('PassoEditar', PassoEditar);
					AlterarValue('posicaoLista',posicao);
					return true;
				} else return false;
			}
			function buscaAcaoPenalJSON(tempRetorno, paginaAtual, idEventoExecucao, idProcessoExecucao){
				var tabela =  $('#TabelaAcaoPenal_'+idEventoExecucao);
				if (tabela.html() != ''){
					$('#linha_acao_penal_'+idEventoExecucao).toggle();
					return;
				}
				tabela.html('');
				
				$.ajax({
					url:tempRetorno+'?PaginaAtual='+ paginaAtual + '&PassoEditar=8&idProcessoExecucao='+idProcessoExecucao,
					context: document.body,
					timeout: 300000, async: true,
					success: function(retorno){
						var inLinha=1;			
						var totalPaginas =0;
						$.each(retorno, function(i,item){
							tabela.append('<table class="Tabela" >' +
									'<tr><td colspan="4">Nº Ação Penal: ' + item.numeroAcaoPenal +
									'</td></tr>' +
									'<tr><td colspan="4">Vara Origem: ' + item.varaOrigem +
									'</td></tr>' +
									'<tr><td align="right">Distribuição: </td><td>' + item.dataDistribuicao +
									'</td><td align="right">Pronúncia: </td><td>' + item.dataPronuncia +
									'</td></tr>' +
									'<tr><td align="right">Recebimento da Denúncia: </td><td>' + item.dataDenuncia +
									'</td><td align="right">Acórdão: </td><td>' + item.dataAcordao +
									'</td></tr>' +
									'<tr><td align="right">Publicação da Sentença: </td><td>' + item.dataSentenca +
									'</td><td align="right">Audiência Admonitória: </td><td>' + item.dataAdmonitoria +
									'</td></tr>' +
									'<tr><td align="right">Trânsito Julgado Defesa: </td><td>' + item.dataTransitoJulgado +
									'</td><td align="right">Trânsito em Julgado Acusação: </td><td>' + item.dataTransitoJulgadoMP +
									'</td></tr>' +
									'<tr><td align="right">Início Cumprimento da Condenação (DICC): </td><td colspan="3">' + item.dataInicioCumprimentoPena +
									'</td></tr>' +
									'</table>');
						});
						$('#linha_acao_penal_'+idEventoExecucao).show();
					},
					error: function(data){
						  alert(data +  ' erro');
					} 
				}); // fim do .ajax*/				     
			}
		</script>
	
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%></h2></div>
  			<%	List listaEventos = (List) request.getSession().getAttribute("listaEventos"); 
					String movimentacaoDataRealizacaoTipo = "";
					String id_movimentacao = "";
					if (listaEventos != null && listaEventos.size() > 0) {
						ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt)listaEventos.get(0);
						movimentacaoDataRealizacaoTipo = processoEventoExecucaoDt.getMovimentacaoDataRealizacaoTipo();
						id_movimentacao = processoEventoExecucaoDt.getId_Movimentacao();
					}
				%>
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>">
  				<input id="Id_ProcessoEventoExecucao" name="Id_ProcessoEventoExecucao" type="hidden" value="<%=request.getAttribute("Id_ProcessoEventoExecucao")%>" />
				<input id="Id_ProcessoExecucao" name="Id_ProcessoExecucao" type="hidden" value="<%=request.getAttribute("Id_ProcessoExecucao")%>" />
				<input id="Id_Movimentacao" name="Id_Movimentacao" type="hidden" value="<%=id_movimentacao%>" />
				<input id="MovimentacaoDataRealizacaoTipo" name="MovimentacaoDataRealizacaoTipo" type="hidden" value="<%=movimentacaoDataRealizacaoTipo%>" />
  				<input id="Id_MovimentacaoArquivo" name="Id_MovimentacaoArquivo" type="hidden" value=""/>
				<input id="Id_Processo" name="Id_Processo" type="hidden" value="<%=processoDt.getId()%>"/>
				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> Dados do Processo </legend>
						<div> Processo</div><span> <a href="javascript: buscarProcesso();" title="Apresentar os dados do Processo"> <%=processoDt.getProcessoNumeroCompleto()%> </a></span><br />
						<div> Serventia</div><span> <%=processoDt.getServentia()%> </a></span><br /><br />
						<div > Movimentação</div><span  style="width: 80%"><%=movimentacaoDataRealizacaoTipo%></span>
					</fieldset>
						
					<!-- EVENTOS -->
					<fieldset class="formEdicao">
			    		<legend> Evento(s) da Movimentação </legend>
		    			<table id="TabelaEventos" class="Tabela">
		        			<thead>
		            			<tr class="TituloColuna">
		                			<th>Nº</th>
		                   			<th width="15%">Evento</th>
		                  			<th width="8%">Início</th>
		                  			<th width="10%">Condenação (a-m-d)</th>
									<th width="15%">Regime</th>
		                  			<th width="15%">Observação</th>
		                  			<th class="colunaMinima" title="Condenação(ões)">Cond.</th>
									<%if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true")) {%>									
									<th class="colunaMinima" title="Editar Evento">Ed.</th>
									<th class="colunaMinima" title="Excluir Evento">Ex.</th>
									<%} %>
		               			</tr>
		           			</thead>
		    				<tbody id="tabListaProcesso">
				    		<%
				    		 	boolean boLinha=false; 
				    		 	String movimentacaoDataRealizacao;
				    		 	id_movimentacao = "";
						   	    if ((listaEventos != null && listaEventos.size()>0) && ((ProcessoEventoExecucaoDt)listaEventos.get(0)).getId_EventoExecucao().length() > 0){
				   	    			for (int i=0; i<listaEventos.size(); i++){
					   			  		ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt)listaEventos.get(i);
							%>			
								<tr>
		                   			<td class="colunaMinima"> <%=i+1%></td>
		                    		<td width="30%"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao(),"","-")%></td>
			                   		<td width="8%" align="center"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getDataInicio(),"","-")%></td>
			                   		<td width="10%" align="center"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getCondenacaoAnos(),"","-")%></td>
			                   		<td width="15%" align="center"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getEventoRegimeDt().getRegimeExecucao(),"","-")%></td>
			                   		<td width="25%"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getObservacao() + processoEventoExecucaoDt.getObservacaoAux(),"","-")%></td>
									<td align="center">
			                   		<%if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))
			            					|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))){%>
									<table>
									<tr>
										<td>
										<a href="">
											<img src="./imagens/important_blue.png" alt="Mostrar ou ocultar dados da ação penal" title="Mostrar ou ocultar dados da ação penal" 
												onclick="javascript:buscaAcaoPenalJSON('<%=request.getAttribute("tempRetorno")%>', '<%=Configuracao.Editar%>','<%=processoEventoExecucaoDt.getId()%>','<%=processoEventoExecucaoDt.getId_ProcessoExecucao()%>'); return false;"/>
										</a>
										</td> 
										<td>
										<a href="javascript:buscarListaCondenacoesProcessoExecucao('<%=processoEventoExecucaoDt.getId()%>','<%=processoEventoExecucaoDt.getId_ProcessoExecucao()%>','1')">
											<img src="./imagens/ico_arquivos_14x14.png" alt="Mostrar ou ocultar condenações" title="Mostrar ou ocultar condenações" />
										</a>
										</td>
									</tr>
									</table>
									<%}
									else {%>-
									<%}%>
									</td>
									<%if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true")) {%>
									<td><input name="formLocalizarimgEditar" type="image" title="Editar evento" src="./imagens/imgEditar_14x14.png" 
										onclick="AlterarValue('PassoEditar','0'); AlterarValue('PaginaAtual','-1'); AlterarValue('Id_ProcessoEventoExecucao','<%=processoEventoExecucaoDt.getId()%>'); AlterarValue('posicaoLista','<%=i%>'); AlterarValue('Id_ProcessoExecucao','<%=processoEventoExecucaoDt.getId_ProcessoExecucao()%>');" />
									</td>
									<td>
									<%if (!processoEventoExecucaoDt.isManterAcaoPenal()){%>
			                   		<input name="formLocalizarimgexcluir" type="image" title="Excluir evento" src="./imagens/imgExcluir_14x14.png" 
										onclick="return confirmaExclusao('Confirma exclusão do evento?', '<%=Configuracao.Curinga8%>', '2', '<%=i%>');" />
									<%}
									else{ %>
									-
									<%}%>
									</td>
									<%}%>
								</tr>
								<!--Dados da Ação Penal-->
								<tr id="linha_acao_penal_<%=processoEventoExecucaoDt.getId()%>" style="display: none;">
									<td colspan="10" class="Linha">
										<table class="Tabela">
											<thead>
												<tr class="TituloColuna"><th>Dados da Ação Penal</th></tr>
											</thead>
											<tbody>
												<tr><td id="TabelaAcaoPenal_<%=processoEventoExecucaoDt.getId()%>" class="Linha"></tr>
											</tbody>
											<tr></tr>
										</table>
									</td>
								</tr>
								<!--Dados das condenações-->
								<tr id="linha_condenacoes_<%=processoEventoExecucaoDt.getId()%>" style="display: none;">
									<td colspan="10" id="pai_condenacoes_<%=processoEventoExecucaoDt.getId()%>" class="Linha">
										<table class="Tabela">
											<thead>
												<tr class="TituloColuna">
				    							  <th width="60%">Crime</th>
                	                    			<th width="10%">Pena (a-m-d)</th>
		                                			<th width="10%">Data do Fato</th>
									                <th width="10%">Reincidência</th>
		                  			                <th width="10%">Extinto</th>
												</tr>
											</thead>
											<tbody id="TabelaCondenacoes_<%=processoEventoExecucaoDt.getId()%>" consultado="não" class="Linha">
											</tbody>
											<tr></tr>
										</table>
									</td>
								</tr>
							<%		
									}
						   	    }
							%>
		           			</tbody>
				    	</table>
						<!-- FIM EVENTOS -->
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					    	<input name="imgInserir" type="submit" value="Incluir Evento" onclick="AlterarValue('PaginaAtual','-1');AlterarValue('PassoEditar','3');">
					    </div>
					</fieldset>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html> 