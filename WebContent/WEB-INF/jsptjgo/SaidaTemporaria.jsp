<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>

<jsp:useBean id="SaidaTemporariaBean" scope="request" class="br.gov.go.tj.projudi.dt.relatorios.SaidaTemporariaDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
	<head>
		<title>Processo Execução Penal - Saídas Temporárias</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
   		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivosEventoExecucao.js'></script>
		<%@ include file="js/buscarArquivos.js"%>
				
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Processo de Execução Penal - Consultar Saídas Temporárias</h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="Id_ProcessoEventoExecucao" name="Id_ProcessoEventoExecucao" type="hidden" value="<%=request.getAttribute("Id_ProcessoEventoExecucao")%>" />
				<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>">
				<input id="Id_MovimentacaoArquivo" name="Id_MovimentacaoArquivo" type="hidden" value="" />
				<br />
				<div id="divEditar" class="divEditar">
				<% if (SaidaTemporariaBean.getAno().length() == 0) {%>
					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
						<table>
							<tr><td><div> Processo</div><span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>"><%=processoDt.getProcessoNumeroCompleto()%></a></span><br>
									<div> Serventia</div><span> <%=processoDt.getServentia()%> </a></span><br /><br />
									<div> Sentenciado</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNome()%></span><br>
									<div> Nome da Mãe</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNomeMae()%></span><br>
									<div> Data de Nascimento</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getDataNascimento()%></span>
								</td>
							</tr>
						</table>
					</fieldset><br /><br />

					<fieldset class="formEdicao"> <legend>Eventos de Saída Temporária</legend>
						<%
			    			List listaEventos = (List) request.getSession().getAttribute("listaEventoSaidaTemporaria");
  				    		 	boolean boLinha=false; 
  						   	    if (listaEventos != null && listaEventos.size() > 0){
			    		%>			    			
						<table id="TabelaEventos" class="Tabela">
		        			<thead>
		            			<tr class="TituloColuna">
		                			<th class="colunaMinima">Nº</th>
		                   			<th width="30%">Evento</th>
		                  			<th width="15%">Início</th>
		                  			<th width="45%">Observação</th>
									<th class="colunaMinima" title="Arquivo(s)">Arq.</th>
									<% //para editar ou excluir o evento deve ter permissão
										if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true")) {
									%>									
									<th class="colunaMinima" title="Editar Evento">Ed.</th>
									<th class="colunaMinima" title="Excluir Evento">Ex.</th>
									<% } %>
		               			</tr>
		           			</thead>
		    				<tbody id="tabListaProcesso">
						<%for (int i=0; i<listaEventos.size(); i++){
							String id_movimentacao, movimentacaoDataRealizacao;
	  						ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt)listaEventos.get(i); %>				    				
								<tr>
		                   			<td class="colunaMinima"> <%=i+1%></td>
		                    		<td width="24%"><%=processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao()%></td>
			                   		<td width="8%" align="center"><%=processoEventoExecucaoDt.getDataInicio()%></td>
			                   		<td width="10%"><%=Funcoes.verificarCampo(processoEventoExecucaoDt.getObservacao(),"","-")%></td>

									<td align="center">
									<% 
									for (int j=0; j<processoDt.getListaMovimentacoes().size(); j++){ 
										MovimentacaoDt movimentacaoDt = (MovimentacaoDt)processoDt.getListaMovimentacoes().get(j);
											if (movimentacaoDt.getId().equals(processoEventoExecucaoDt.getId_Movimentacao())){
												if (movimentacaoDt.temArquivos()){
													id_movimentacao = movimentacaoDt.getId();
													movimentacaoDataRealizacao = movimentacaoDt.getDataRealizacao().substring(0,10);
									%>
										<a href="javascript:buscarArquivosMovimentacaoEventoExecucao('<%=processoEventoExecucaoDt.getId()%>', '<%=id_movimentacao%>', '<%=movimentacaoDataRealizacao%>', 'BuscaProcesso', 'Id_MovimentacaoArquivo', <%=Configuracao.Curinga6%>, 'null')">
											<img src="./imagens/ico_arquivos_14x14.png" alt="Mostrar ou ocultar arquivos" title="Mostrar ou ocultar arquivos" />
										</a>
									<% } else { %> - <% } 
									}}%>
									</td>


									<%
										//para editar ou excluir o evento deve ter permissão e não ser o cálculo de liquidação
										if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true")) {
									%>
									<td><input name="formLocalizarimgEditar" type="image" title="Editar evento" src="./imagens/imgEditar_14x14.png" 
											onclick="AlterarValue('PassoEditar','5'); AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('Id_ProcessoEventoExecucao','<%=processoEventoExecucaoDt.getId()%>'); AlterarValue('posicaoLista','<%=i%>');" /></td>
			                   		<td>
			                   		<input name="formLocalizarimgexcluir" type="image" title="Excluir evento" src="./imagens/imgExcluir_14x14.png" 
										onclick="return confirmaExclusao('Confirma exclusão do evento?', '<%=Configuracao.Curinga7%>', '4', '<%=i%>');" />
									</td>
									<% } %>
								</tr>
							<%	boLinha = !boLinha;	%>
								<!--Dados dos arquivos da movimentação-->
								<tr id="linha_<%=processoEventoExecucaoDt.getId()%>" style="display: none;">
									<td colspan="10" id="pai_<%=processoEventoExecucaoDt.getId()%>" class="Linha"></td>
								</tr>

							<%} // for - lista de evento
					%>
		           			</tbody>
				    	</table>
					<% } else { // if - lista de evento != null%>
					<div> Não existe evento de Saída Temporária neste Processo!</div>
					<%} %>
						<!-- FIM EVENTOS -->

					</fieldset>
					<br />
					<%if (request.getAttribute("permissaoEditarEvento").toString().equalsIgnoreCase("true")) {%>			
					<fieldset class="formEdicao"> <legend>Relatório de Saída Temporária</legend>
						<label class="formEdicaoLabel" for="AnoSaidaTemporaria">Gerar relatório das saídas temporárias do Ano de: </label><br>    
					    <input class="formEdicaoInput" name="AnoSaidaTemporaria" id="AnoSaidaTemporaria" type="text" size="4" maxlength="4" value="" onkeypress="return DigitarSoNumero(this, event)"/>
						<input name="btnConsultar" type="submit" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('PassoEditar','3');"><br />
					</fieldset>
					<%} %>
					
				<%} else { %>
					<fieldset  id="VisualizaDados" class="VisualizaDados"> 
						<input id="Ano" name="Ano" type="hidden" value="<%=SaidaTemporariaBean.getAno()%>">
						<legend >Saída(s) Temporária(s) do Ano de: <%=SaidaTemporariaBean.getAno()%></legend>
						<br />
						<div style="width:300px"> Número do processo: </div> <span class="span1"><a href="BuscaProcesso?Id_Processo=<%=SaidaTemporariaBean.getIdProcesso()%>"><%=SaidaTemporariaBean.getNumeroProcesso()%></a></span><br />
						<div style="width:300px"> Nome do sentenciado: </div> <span class="span1"><%=SaidaTemporariaBean.getNomeSentenciado()%></span><br />
						<div style="width:300px"> Nome da mãe: </div> <span class="span1"><%=SaidaTemporariaBean.getNomeMae()%></span><br />
						<div style="width:300px"> Data de nascimento: </div> <span class="span1"><%=SaidaTemporariaBean.getDataNascimento()%></span><br />
						<br />
						<div style="width:300px"> Regime: </div> <span class="span1"><%=SaidaTemporariaBean.getRegime()%></span><br />
						<div style="width:300px"> Situação: </div> <span class="span1"><%=SaidaTemporariaBean.getStatus()%></span><br />
						<div style="width:300px"> Condenado em crime hediondo: </div> <span class="span1"><%=SaidaTemporariaBean.getHediondo()%></span><br />
						<br />
						<div style="width:300px"> Saídas bloqueadas? </div> <span class="span1"><%=SaidaTemporariaBean.getBloqueioSaidas()%></span><br />
						<div style="width:300px"> Trabalho bloqueado? </div> <span class="span1"><%=SaidaTemporariaBean.getBloqueioTrabalho()%></span><br />
						<div style="width:300px"> Reincidência: </div> <span class="span1"><%=SaidaTemporariaBean.getDescricaoReincidencia()%></span><br />								
						<br />
						<div style="width:300px"> Tempo total de condenação: </div> <span class="span1"><%=SaidaTemporariaBean.getTempoTotalCondenacao()%> (a-m-d)</span><br />
						<div style="width:300px"> Tempo de pena (1/6): </div> <span class="span1"><%=SaidaTemporariaBean.getTempoCondenacao16anos()%> (a-m-d)</span><br />
						<div style="width:300px"> Tempo de pena (1/4): </div> <span class="span1"><%=SaidaTemporariaBean.getTempoCondenacao14anos()%> (a-m-d)</span><br />
						<div style="width:300px"> Tempo cumprido até data atual (<%=new SimpleDateFormat("dd/MM/yyyy").format(new Date()) %>): </div> <span class="span1"><%=SaidaTemporariaBean.getTempoCumpridoAnos()%> (a-m-d)</span><br />
						<br />
						<div style="width:300px"> Saída(s) no ano de <%=SaidaTemporariaBean.getAno()%>: </div>
						<%if (Integer.parseInt(SaidaTemporariaBean.getTotalSaidasTemporarias()) > 0) {
							for (int i=0; i<SaidaTemporariaBean.getListaSaidasTemporarias().size(); i++){ 
								if (i!=0){%>
								<div style="width:300px"></div>
								<%}%>
								<span class="span1"><%=((ProcessoEventoExecucaoDt)SaidaTemporariaBean.getListaSaidasTemporarias().get(i)).getDataInicio()%></span><br />
						<%	}
						} else { %>			
							<span class="span1">Não há saída temporária neste período.</span><br />
						<% } %>

						<div style="width:300px">Total de saídas no ano de <%=SaidaTemporariaBean.getAno()%>: </div><span class="span1"><%=SaidaTemporariaBean.getTotalSaidasTemporarias()%></span><br />
						<br />
						<div style="width:300px">Data do último cálculo: </div><span class="span1"><%=SaidaTemporariaBean.getDataUltimoCalculo() %></span><br />
						<div style="width:300px">Data da consulta: </div><span class="span1"><%=SaidaTemporariaBean.getDataConsulta()%></span><br />
					</fieldset>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
<%request.setAttribute("SaidaTemporariaBean", SaidaTemporariaBean); %>
						<input name="imgCancelar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('PassoEditar','1');">
						<input name="imgCancelar" type="submit" value="Imprimir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('PassoEditar','6');">
						
					</div>
				<% } %>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>