<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="CalculoLiquidacaodt" scope="session" class= "br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt"/>


<%@page import="br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt"%>
<html>
	<head>
		<title>Eventos da Execu��o Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
		</style>

		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>	
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivosEventoExecucao.js'></script>
		<script type='text/javascript' src='./js/tabelaCondenacoesEventoExecucao.js'></script>
<!--		<script type='text/javascript' src='./js/tabelaAcaoPenalEventoExecucao.js'></script>-->
		<%@ include file="js/buscarArquivos.js"%>

		<script type='text/javascript' src='js/CondenacaoExecucao.js'></script>
		<script type="text/javascript">

			$(document).ready(function() {
				$("#dataHomologacao").mask("99/99/9999");
			});

			function confirmaExclusao(mensagem, PaginaAtual, PassoEditar, posicao){
				if (confirm(mensagem)){
					if (PaginaAtual != '') AlterarValue('PaginaAtual', PaginaAtual);
					if (PassoEditar != '') AlterarValue('PassoEditar', PassoEditar);
					AlterarValue('posicaoLista',posicao);
					return true;
				} else return false;
			}
			function confirmaCalculo(mensagem, PaginaAtual, PassoEditar){
				if (confirm(mensagem)){
					AlterarValue('PaginaAtual', PaginaAtual);
					AlterarValue('PassoEditar', PassoEditar);
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
							'<tr><td colspan="4">N� A��o Penal: ' + item.numeroAcaoPenal +
							'</td></tr>' +
							'<tr><td colspan="4">Vara Origem: ' + item.varaOrigem +
							'</td></tr>' +
							'<tr><td align="right">Distribui��o: </td><td>' + item.dataDistribuicao +
							'</td><td align="right">Pron�ncia: </td><td>' + item.dataPronuncia +
							'</td></tr>' +
							'<tr><td align="right">Recebimento da Den�ncia: </td><td>' + item.dataDenuncia +
							'</td><td align="right">Ac�rd�o: </td><td>' + item.dataAcordao +
							'</td></tr>' +
							'<tr><td align="right">Publica��o da Senten�a: </td><td>' + item.dataSentenca +
							'</td><td align="right">Audi�ncia Admonit�ria: </td><td>' + item.dataAdmonitoria +
							'</td></tr>' +
							'<tr><td align="right">Tr�nsito Julgado Defesa: </td><td>' + item.dataTransitoJulgado +
							'</td><td align="right">Tr�nsito em Julgado Acusa��o: </td><td>' + item.dataTransitoJulgadoMP +
							'</td></tr>' +
							'<tr><td align="right">In�cio Cumprimento da Condena��o (DICC): </td><td colspan="3">' + item.dataInicioCumprimentoPena +
							'</td></tr>' +
							'</table>');
						});
						$('#linha_acao_penal_'+idEventoExecucao).show();
					},
					error: function(request, status, error){				
						if (error=='timeout'){
							mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
						}else{
							mostrarMensagemErro("Projudi - Erro", request.responseText);
						}
			        }	 
				}); // fim do .ajax*/				     
			}
			
			function marcarDesmarcar(){
				var e = document.getElementsByTagName("input");
				var master = document.getElementById("selecionaTodos");	

				for(var i=1;i<e.length;i++){
					if (e[i].type=="checkbox"){
						e[i].checked =  master.checked;
					}	
				}
			}
		</script>
		<style type="text/css">
			.EventoFalta{background-color: #F2DEDE; color:#B94A48 !important; font-style: inherit;}
		</style>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%></h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>"/>
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>"/>
				<input id="permissaoEditarEvento" name="permissaoEditarEvento" type="hidden" value="<%=request.getAttribute("permissaoEditarEvento")%>"/>
				<input id="tempRetorno" name="tempRetorno" type="hidden" value="<%=request.getAttribute("tempRetorno")%>" />
  				
				<input id="Id_ProcessoEventoExecucao" name="Id_ProcessoEventoExecucao" type="hidden" value="<%=request.getAttribute("Id_ProcessoEventoExecucao")%>" />
				<input id="Id_ProcessoExecucao" name="Id_ProcessoExecucao" type="hidden" value="<%=request.getAttribute("Id_ProcessoExecucao")%>" />
				<input id="Id_Movimentacao" name="Id_Movimentacao" type="hidden" value="" />
  				<input id="Id_MovimentacaoArquivo" name="Id_MovimentacaoArquivo" type="hidden" value="" />

				<% if (request.getAttribute("permissaoImprimir") == null || request.getAttribute("permissaoImprimir").toString().equalsIgnoreCase("true")) { %>
				<div id="divPortaBotoes" class="divPortaBotoes">
					<input id="imgImprimir"  class="imgImprimir" title="Imprimir Atestado de Pena a Cumprir" name="imgImprimir" type="image" src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>');AlterarValue('PassoEditar','2')"/>	
		  		</div>
				<%}%>

				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
						<table>
							<tr><td><div> Processo</div><span><a href="<%=request.getAttribute("tempRetornoProcesso")%>?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span><br>
									<div> Serventia</div><span> <%=processoDt.getServentia()%> </a></span><br /><br />
									<div> Sentenciado</div><span> <%=processoDt.getPrimeiroPoloPassivoNome()%></span><br>
									<div> Nome da M�e</div><span> <%=processoDt.getPrimeiroPoloPassivoNomeMae()%></span><br>
									<div> Data de Nascimento</div><span> <%=processoDt.getPrimeiroPoloPassivoDataNascimento()%></span>
								</td>
								<td> 
									<% if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true")) { %>
									<blockquote id="menu" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesLiquidacaoCodigo) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesLiquidacaoCodigo):"")%> </blockquote/><br />
									<%}%>
								</td>
							</tr>
						</table>
					</fieldset><br /><br />
						
					<!-- HIST�RICO DE EVENTOS -->
					<fieldset class="formEdicao">
			    		<legend> Hist�rico de Eventos de Execu��o do Processo (principal e apensos, se houver) </legend>

						<% //para editar ou excluir o evento deve ter permiss�o e n�o ser o c�lculo de liquida��o
								if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true") 
										&& !request.getAttribute("PassoEditar").toString().equals("4")
										&& !request.getAttribute("PaginaAtual").toString().equals(String.valueOf(Configuracao.Curinga6))) {
							%>	
			    		<button type="submit" name="operacao" value="ExcluirTodos" onclick="return confirmaExclusao('Confirma exclus�o de todos os eventos selecionados?', '<%=Configuracao.Curinga8%>', '-1', '-1');"  style="float: right">
							<img src="imagens/imgExcluir_14x14.png" alt="Excluir todos os eventos selecionados" />
								Excluir Selecionados
						</button>
						<% } %>

		    			<table id="TabelaEventos" class="Tabela">
		        			<thead>
		            			<tr class="TituloColuna">
		                			<th>N�</th>
		                   			<th width="28%">Evento</th>
		                  			<th width="8%">In�cio</th>
									<th width="8%">Fim</th>
		                  			<th width="10%" title="Condena��o (ano-m�s-dia)">Condena��o (a-m-d)</th>
									<th width="10%" title="Tempo Cumprido (ano-m�s-dia)">Tempo Cumprido (a-m-d)</th>
									<th width="15%">Regime</th>
		                  			<th width="10%">Observa��o</th>
		                  			<th class="colunaMinima" title="Condena��o(�es)">Cond.</th>
									<th class="colunaMinima" title="Arquivo(s)">Arq.</th>
									<% //para editar ou excluir o evento deve ter permiss�o e n�o ser o c�lculo de liquida��o
										if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true") 
												&& !request.getAttribute("PassoEditar").toString().equals("4")
												&& !request.getAttribute("PaginaAtual").toString().equals(String.valueOf(Configuracao.Curinga6))) {
									%>									
									<th class="colunaMinima" title="Editar Evento">Ed.</th>
									<th class="colunaMinima" title="Excluir Evento">Ex.<input type="checkbox" id="selecionaTodos" onclick="marcarDesmarcar();"/></th>
									<% } %>
		               			</tr>

								

		    				<tbody id="tabListaProcesso">
				    		<%
				    			List listaEventos = (List) request.getSession().getAttribute("listaEventos");
   				    		 	boolean boLinha=false; 
   				    		 	int x=0;
   				    		 	String id_movimentacao, movimentacaoDataRealizacao;
   						   	    if (listaEventos != null){
   				   	    			for (int i=0; i<listaEventos.size(); i++){
   					   			  		ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt)listaEventos.get(i);
   					   			  			x++;
				    		%>	
								<tr  <%if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA))){%>class="EventoFalta"<% }%>>
		                   			<td class="colunaMinima"> <%=x%></td>
		                    		<td width="24%"><%=processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao()%></td>
			                   		<td width="8%" align="center"><%=processoEventoExecucaoDt.getDataInicio()%></td>
									<td width="8%" align="center"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getDataFim(),"","-")%></td>
			                   		<td width="10%" align="center" title="Tempo de condena��o (ano-m�s-dia)"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getCondenacaoAnos(),"","-")%></td>
			                   		<td width="10%" align="center" title="Tempo cumprido (ano-m�s-dia)"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getTempoCumpridoAnos(),"","-")%></td>
			                   		<td width="15%" align="center"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getEventoRegimeDt().getRegimeExecucao(),"","-")%></td>
			                   		<td width="10%"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getObservacaoVisualizada(),"","-")%></td>
			                   		<td align="center">
			                   		<% if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))
			                   			|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))){ %>
									<table>
									<tr>
										<% //if (request.getAttribute("permissaoImprimir") == null || request.getAttribute("permissaoImprimir").toString().equalsIgnoreCase("true")) { %>
										<td><a href="">
											<img src="./imagens/important_blue.png" alt="Mostrar ou ocultar dados da a��o penal" title="Mostrar ou ocultar dados da a��o penal" 
												onclick="javascript:buscaAcaoPenalJSON('<%=request.getAttribute("tempRetorno")%>', '<%=Configuracao.Editar%>', '<%=processoEventoExecucaoDt.getId()%>','<%=processoEventoExecucaoDt.getId_ProcessoExecucao()%>'); return false;"/>
											</a></td>
										<td><a href="javascript:buscarListaCondenacoesProcessoExecucao('<%=processoEventoExecucaoDt.getId()%>','<%=processoEventoExecucaoDt.getId_ProcessoExecucao()%>','1')">
											<img src="./imagens/ico_arquivos_14x14.png" alt="Mostrar ou ocultar condena��es" title="Mostrar ou ocultar condena��es" />
											</a></td>
										<% //} %> 
									</tr>
									</table>
									<% } else { %> - <% } %>
									</td>
									<td align="center">
									<% for (int j=0; j<processoDt.getListaMovimentacoes().size(); j++){ 
																	MovimentacaoDt movimentacaoDt = (MovimentacaoDt)processoDt.getListaMovimentacoes().get(j);
																		if (movimentacaoDt.getId().equals(processoEventoExecucaoDt.getId_Movimentacao())){
																			if (movimentacaoDt.temArquivos()){
																				id_movimentacao = movimentacaoDt.getId();
																				movimentacaoDataRealizacao = movimentacaoDt.getDataRealizacao().substring(0,10);
									%>
										<a href="javascript:buscarArquivosMovimentacaoEventoExecucao('<%=processoEventoExecucaoDt.getId()%>', '<%=id_movimentacao%>', '<%=movimentacaoDataRealizacao%>', 'BuscaProcesso', 'Id_MovimentacaoArquivo', <%=Configuracao.Curinga6%>, 'null')">
											<img src="./imagens/ico_arquivos_14x14.png" alt="Mostrar ou ocultar arquivos" title="Mostrar ou ocultar arquivos" />
										</a>
									<% } else { %> - <% 
										}
										}
										} // for - lista de movimenta��o
									%>
									</td>
									<% //para editar ou excluir o evento deve ter permiss�o e n�o ser o c�lculo de liquida��o
										if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true") 
												&& !request.getAttribute("PassoEditar").toString().equals("4")
												&& !request.getAttribute("PaginaAtual").toString().equals(String.valueOf(Configuracao.Curinga6))) { %>
									<td><input name="formLocalizarimgEditar" type="image" title="Editar evento" src="./imagens/imgEditar_14x14.png" 
											onclick="AlterarValue('PassoEditar','0'); AlterarValue('PaginaAtual','-1'); AlterarValue('Id_ProcessoEventoExecucao','<%=processoEventoExecucaoDt.getId()%>'); AlterarValue('posicaoLista','<%=i%>'); AlterarValue('Id_ProcessoExecucao','<%=processoEventoExecucaoDt.getId_ProcessoExecucao()%>');" /></td>
			                   		<td width="5%" align="center">
			                   		<% if (!processoEventoExecucaoDt.isManterAcaoPenal()){ %>
			                   		<input name="formLocalizarimgexcluir" type="image" title="Excluir evento" src="./imagens/imgExcluir_14x14.png" 
										onclick="return confirmaExclusao('Confirma exclus�o do evento?', '<%=Configuracao.Curinga8%>', '-1', '<%=i%>');" />
									<input type="checkbox" id="selecionaExcluir_<%=processoEventoExecucaoDt.getId()%>" name="chkExcluir[]" value="<%=processoEventoExecucaoDt.getId()%>"/>
									<% } else { %> - <% } %>	
									</td>
									<% } %>
								</tr>
							<% boLinha = !boLinha; %>
								<!--Dados da A��o Penal-->
								<tr id="linha_acao_penal_<%=processoEventoExecucaoDt.getId()%>" style="display: none;">
									<td colspan="10" class="Linha">
										<table class="Tabela">
											<thead>
												<tr class="TituloColuna"><th>Dados da A��o Penal</th></tr>
											</thead>
											<tbody>
												<tr><td id="TabelaAcaoPenal_<%=processoEventoExecucaoDt.getId()%>" class="Linha"></tr>
											</tbody>
											<tr></tr>
										</table>
									</td>
								</tr>
								<!--Dados das condena��es-->
								<tr id="linha_condenacoes_<%=processoEventoExecucaoDt.getId()%>" style="display: none;">
									<td colspan="10" id="pai_condenacoes_<%=processoEventoExecucaoDt.getId()%>" class="Linha">
										<table class="Tabela">
											<thead>
												<tr class="TituloColuna">
				    							  <th width="60%">Crime</th>
                	                    			<th width="10%">Pena (a-m-d)</th>
		                                			<th width="10%">Data do Fato</th>
									                <th width="10%">Reincid�ncia</th>
		                  			                <th width="10%">Extinto</th>
												</tr>
											</thead>
											<tbody id="TabelaCondenacoes_<%=processoEventoExecucaoDt.getId()%>" consultado="n�o" class="Linha">
											</tbody>
											<tr></tr>
										</table>
									</td>
								</tr>
								<!--Dados dos arquivos da movimenta��o-->
								<tr id="linha_<%=processoEventoExecucaoDt.getId()%>" style="display: none;">
									<td colspan="10" id="pai_<%=processoEventoExecucaoDt.getId()%>" class="Linha"></td>
								</tr>
								<% } // for - lista de evento
								} // if - lista de evento %>
		           			</tbody>
				    	</table>
						<!-- FIM EVENTOS -->

						<label>*DICC: Data de In�cio de Cumprimento da(s) Condena��o(�es)</label><br><br /><br />

						<%@ include file="DadosProcessoEventoExecucaoCalculo.jspf"%> <!-- DADOS GERAIS (c�lculo) -->

					</fieldset>	<!-- FIM HIST�RICO DE EVENTOS -->
					<br />

					<%
						if (Funcoes.StringToBoolean(request.getAttribute("isPenaRestritivaDireito").toString())) {
					%>
					<%@ include file="DadosProcessoEventoExecucao_Historico_PRD.jspf"%>
					<%} %>
					<%@ include file="CalculoLiquidacaoResultado.jspf"%>
					<%@ include file="CalculoLiquidacaoResultadoPenaRestritiva.jspf"%>
					<br />
					<!-- CONDENA��O EXTINTA -->
		    		<%
		   				List listaCondenacaoExtinta = (List) request.getSession().getAttribute("listaCondenacaoExtinta");
				   	    if (listaCondenacaoExtinta != null && listaCondenacaoExtinta.size() > 0){
					%>
					<div><a href="javascript: MostrarOcultar('divCondenacaoExtinta')" title="Mostrar/Ocultar condena��o(�es) extinta(s)"> Condena��o(�es) extinta(s)</a></div>
					<div id="divCondenacaoExtinta" style="display: none">
		    			<table id="TabelaCondenacoes" class="Tabela">
		        			<thead>
		            			<tr class="TituloColuna">
		                			<th class="colunaMinima">N�</th>
		                   			<th width="58%">Crime</th>
		                  			<th width="10%">Data do Fato</th>
									<th width="12%">Tempo de Pena (a-m-d)</th>
		                  			<th width="10%">Data TJ</th>
									<th width="10%">Extin��o</th>
		               			</tr>
		           			</thead>
		    				<tbody id="tabListaCondenacaoExtinta">
				    		<%for (int w=0; w<listaCondenacaoExtinta.size(); w++){
		   	    				CondenacaoExecucaoDt condenacaoDt = (CondenacaoExecucaoDt)listaCondenacaoExtinta.get(w);%>
								<tr>
		                   			<td align="center"> <%=w+1%></td>
		                    		<td><%=condenacaoDt.getCrimeExecucao()%></td>
			                   		<td align="center"><%=condenacaoDt.getDataFato()%></td>
									<td align="center"><%=condenacaoDt.getTempoPenaEmAnos()%></td>
			                   		<td align="center"><%=condenacaoDt.getDataTransitoJulgado()%></td>
			                   		<td align="center"><%=condenacaoDt.getCondenacaoExecucaoSituacao()%></td>
								</tr>
							<%	}%>
		           			</tbody>
				    	</table>
					</div>
					<!-- CONDENA��O EXTINTA -->
					<% } %>
					
					<%@ include file="SituacaoAtualExecucao.jsp"%>
					<br />
					<!--INFORMA��O DO �LTIMO C�LCULO-->
					<%if (request.getSession().getAttribute("ultimoCalculo") != null){ 
						DataProvavelDt ultimoCalculo = (DataProvavelDt) request.getSession().getAttribute("ultimoCalculo");%>
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> Informa��o do �ltimo C�lculo de Liquida��o de Pena realizado
					<%if (request.getAttribute("visualizaUltimoCalculo") != null && request.getAttribute("visualizaUltimoCalculo").equals("true")){ %>
						<input id="imgVisularCalculo"  class="imgVisularCalculo" title="Visualizar �ltimo Atestado de Pena a Cumprir" name="imgImprimir" type="image" src="./imagens/22x22/contact-new.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>');AlterarValue('PassoEditar','6')"/>
					<%} %>
						</legend>
						<table class="Tabela">
							<tr><td align="right" width="25%">Data do C�lculo:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataCalculo(),"","-") %></b></td>
								<td align="right" width="25%">Hora Restante PSC:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getHoraRestantePSC(),"","-") %></b></td></tr>

							<tr><td align="right" width="25%">Prov�vel T�rmino da Pena:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataProvavelTerminoPena(),"","-")%> </b></td>
								<td align="right" width="25%">Data T�rmino LFS:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataTerminoLFS(),"","-")%> </b></td> </tr>

							<tr><td align="right" width="25%">Prov�vel Progress�o de Regime:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataProvavelProgressao(),"","-")%></b></td>
								<td align="right" width="25%">Data T�rmino ITD:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataTerminoITD(),"","-")%></b></td></tr>

							<tr><td align="right" width="25%">Prov�vel Livramento Condicional:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataProvavelLivramento(),"","-")%></b></td>
								<td align="right" width="25%">Valor Devido PEC:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getValorDevidoPEC(),"","-")%></b></td></tr>
							
							<tr><td align="right" width="25%">Validade do Mandado de Pris�o:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataValidadeMandadoPrisao(),"","-")%></b></td>
								<td align="right" width="25%">T�rmino SURSIS:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getTerminoSURSIS(),"","-")%></b></td></tr>
								
							<tr><td align="right" width="25%">Cestas B�sicas Restantes:</td> <td width="25%"><b><%=Funcoes.verificarCampo(ultimoCalculo.getQtdDevidaPCB(),"","-")%></b></td>
								<td align="right" width="25%"></td> <td width="25%"></td></tr>

							<tr>
								<td colspan="4">
									<div style="width: 200px;"> Informa��o: </div><span style="width: 700px;"> <%=Funcoes.verificarCampo(ultimoCalculo.getInformacaoSentenciado(),"","-")%></span>
								</td></tr>
							<tr><td colspan="4">
									<%  //para editar ou excluir o evento deve ter permiss�o e n�o ser o c�lculo de liquida��o
										if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true") 
												&& !request.getAttribute("PassoEditar").toString().equals("4")
												&& !request.getAttribute("PaginaAtual").toString().equals(String.valueOf(Configuracao.Curinga6))
												) {
									%>

									<table>
										<tr><td>Data de Homologa��o do C�lculo:</td>
											<td valign="middle"><input id="dataHomologacao"  name="dataHomologacao" type="text" size="10" maxlength="10" value="<%=ultimoCalculo.getDataHomologacao()%>">
																<input id="imgsalvar" alt="Salvar" title="Salvar dados do c�lculo" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>'); AlterarValue('PassoEditar','7')" />
												*Informe a �ltima data, de homologa��o ou decis�o, sobre os benef�cios alcan�ados por este c�lculo.
											</td></tr>
									</table>
									<%} else {%>
									<table>
										<tr><td>Data de Homologa��o do C�lculo:</td><td><b><%=Funcoes.verificarCampo(ultimoCalculo.getDataHomologacao(),"","-")%></b></td></tr>
									</table>
									<%} %>
								</td>
							</tr>
						</table>
					</fieldset>
					<br /><br />
					<%} %>
					<!--FIM INFORMA��O DO �LTIMO C�LCULO-->

					
					<%//bot�o de confirmar o calculo ser� apresenta��o somente para o c�lculo de liquida��o
					if (request.getAttribute("PassoEditar").toString().equals("4") || request.getAttribute("PaginaAtual").toString().equals(String.valueOf(Configuracao.Curinga6))) {%>
						<div id="divEditar" class="divEditar">
							<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Informa��es Adicionais</legend>
								<textarea name="InformacaoAdicional" id="InformacaoAdicional" cols=110 rows=6 value="<%=CalculoLiquidacaodt.getInformacaoAdicional() %>"><%=CalculoLiquidacaodt.getInformacaoAdicional() %></textarea>
							</fieldset>
						</div>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConfirmar" type="submit" value="Confirmar" 
							onclick="return confirmaCalculo('Deseja realmente cadastrar o C�lculo de Liquida��o de Penas?', '<%=Configuracao.Imprimir%>', '1');"></input>
							<!-- onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');AlterarValue('PassoEditar','1');"-->
					    </div>
					    <br></br>
					<%}%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html> 