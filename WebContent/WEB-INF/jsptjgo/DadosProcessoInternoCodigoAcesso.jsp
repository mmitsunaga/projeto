<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>      	      
		
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<%@ include file="js/buscarArquivos.js"%>
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
		<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>
		<script type="text/javascript">
			
			$(document).ready(function() {			
				criarMenu('menu','','menuA','menuAHover');
				//pego todos objetos que foram marcados com a class nomes
				//e verifico se tem número no nome
				 $(".nomes").each(function( index ) {
				 	var texto =  $( this ).text();
					for(var numero=0; numero<=9; numero++){
						texto= texto.replace(numero,'<p class="destacarNumero" tag="Foi utilizado número no Nome, favor conferir com os dados da petição" title="Foi utilizado número no Nome, favor conferir com os dados da petição">'+ numero +'</p>');
					}	
					$( this ).html(texto);			
				});	
			});			
			
			function mudarStatusMovimentacao(id_Movimentacao, descricao, novoStatus, paginaAtual) {
				if(confirm("Confirma a mudança do status da movimentacao "+ descricao +" para "+ novoStatus +"  ?")) {
					AlterarValue('PaginaAtual', paginaAtual);
					AlterarValue('Id_Movimentacao', id_Movimentacao);
					AlterarAction('Formulario','Movimentacao');
					document.Formulario.submit();
				}
			}
			
			function invalidarArquivo(id_MovimentacaoArquivo, descricao) {
				if(confirm("Confirma a Invalidação do arquivo "+ descricao + " ?")) {
					AlterarValue('PaginaAtual', '<%=Configuracao.Curinga7%>');
					AlterarValue('Id_MovimentacaoArquivo', id_MovimentacaoArquivo);
					AlterarAction('Formulario','MovimentacaoArquivo');
					document.Formulario.submit();
				}
			}
			
			function validarArquivo(id_MovimentacaoArquivo, descricao) {
				if(confirm("Confirma a Validação do arquivo "+ descricao + " ?")) {
					AlterarValue('PaginaAtual', '<%=Configuracao.Curinga6%>');
					AlterarValue('Id_MovimentacaoArquivo', id_MovimentacaoArquivo);
					AlterarAction('Formulario','MovimentacaoArquivo');
					document.Formulario.submit();
				}
			}
			
			function gerarPendencias(id_Movimentacao) {
				AlterarValue('PaginaAtual', '<%=Configuracao.Novo%>');
				AlterarValue('Id_Movimentacao', id_Movimentacao);
				AlterarAction('Formulario','PendenciaMovimentacao');
				document.Formulario.submit();
			}

			function manterEventos(descricaoMovimentacao, id_Movimentacao, paginaAtual) {
				AlterarValue('PaginaAtual', paginaAtual);
				AlterarValue('MovimentacaoDataRealizacaoTipo', descricaoMovimentacao);
				AlterarValue('Id_Movimentacao', id_Movimentacao);
				AlterarAction('Formulario','ProcessoEventoExecucao');
				document.Formulario.submit();
			}
			
			function MostrarOpcoes(){
				if ($('#chkVer').val() == 'false'){
					$('#chkVer').val('true');
					$('.imgValidar').css("display","block");
				} else {
					$('#chkVer').val('false');
					$('.imgValidar').css("display","none");
				}
			}

			function mostrarTodasPartes() {
				AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');
				AlterarValue('PassoEditar', '11');								
				document.Formulario.submit();
			}
		</script>
		
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  				<style type="text/css"> #bkg_projudi{ display:none } </style>
  				<%@ include file="/CabecalhoPublico.html" %>
  			<% } %> 
  			<div class="area"><h2>&raquo; Dados do Processo</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
  				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="" />
  				<input id="Id_Movimentacao" name="Id_Movimentacao" type="hidden" value="" />
  				<input id="Id_MovimentacaoArquivo" name="Id_MovimentacaoArquivo" type="hidden" value="" />
  				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
  				
	  			<% if (Funcoes.StringToInt(processoDt.getProcessoStatusCodigo())==ProcessoStatusDt.ARQUIVADO){ %>
	  			<img src="imagens/ProcessoArquivado.jpg" name="arquivado" alt="Processo Arquivado" title="Processo Arquivado" class="imagemArquivado">
	 			<%} else if ( processoDt.getProcessoTipoCodigo() != null && processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){%>
	  				<img src="imagens/Img_pfisico.jpg" name="calculo" alt="Processo Físico - SPG" title="Processo Físico - SPG" class="imagemArquivado">
				<%} %>
	 	
	  			<% if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) { %>
	  				<%@ include file="DadosRecursoCodigoAcesso.jspf"%>
	  			<% } %>
	
				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> Dados do Processo </legend>
						
						<div> N&uacute;mero</div>
						<span> <%=processoDt.getProcessoNumeroCompleto()%></span>
						<blockquote id="menu" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoCodigo) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoCodigo):"")%> </blockquote/><br />
						
						<% if (!processoDt.getTcoNumero().equals("")){ %>
						<div> Protocolo SSP </div> <span><%=processoDt.getTcoNumero()%></span>
						<%} %>
					
						<%@ include file="BuscaPartesProcessoCodigoAcesso.jspf"%>
					     	
				    	<fieldset id="VisualizaDados" class="VisualizaDados">
				    		<legend> Outras Informações </legend>
				    		
					   		<div> Serventia </div>
							<span class="span1"><%= processoDt.getServentia()%></span><br />
							
					   		<div> Classe </div>
				    	  	<span style="width: 500px;"><%=processoDt.getProcessoTipo()%> </span><br />
				    	  	
				    	  	<%@ include file="BuscaAssuntosProcessoCodigoAcesso.jspf"%>
				    	  	
				    	  	<% if (!processoDt.getValor().equals("Null")){ %>
							<div> Valor da Causa</div>
							<span class="span1"><%=processoDt.getValor()%></span>
							<%} %>
							
				    	  	<div> Processo Originário </div>
							<span class="span2"><a href="BuscaProcesso?ProcessoOutraServentia=true&Id_Processo=<%=processoDt.getId_ProcessoPrincipal()%>"><%=processoDt.getProcessoNumeroPrincipal()%></a></span/><br />	
							
							<div> Fase Processual</div>	
							<span class="span1"><%=processoDt.getProcessoFase()%></span>
										
							<div> Apenso(s)</div>
							<span class="span2">
								<% if (processoDt.temApensos()){%>
								<a href="ProcessoApenso?PaginaAtual=<%=Configuracao.Localizar%>">Visualizar</a>
								<% } %>
							</span><br />
							
							<div> Classificador </div>
						    <span class="span1">
						    <% if (UsuarioSessao.isPodeVisualizarClassificador()){ %>
						   		<%= processoDt.getClassificador()%>
						   	<% } %>
						    </span>
						    
						    <div> Data Distribui&ccedil;&atilde;o</div>
							<span class="span2"><%=processoDt.getDataRecebimento()%></span><br />		
						    
							<div> Segredo de Justi&ccedil;a</div>
							<span class="span1"><%=(processoDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span>
							
							<div> Prioridade</div>
							<span class="span2"><%=processoDt.getProcessoPrioridade()%></span/>
							<%if(processoDt.isPrioridade() && UsuarioSessao.getVerificaPermissao(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Curinga8)) {%>
								<a href="Processo?PaginaAtual=<%=Configuracao.Curinga8%>" border="0">
									<img src='imagens/16x16/edit-clear.png' alt="Retirar Prioridade" title="Retirar Prioridade" border="0">
								</a>
							<%} %>
							<br />
							
							<div> Status </div>
							<span class="span1"><%=processoDt.getProcessoStatus()%></span/>
							
							<div style="width: 900px;">
								<input name="inputResponsaveis" type="image" src="./imagens/22x22/ico_aviso.png" 
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');" title="Visualizar Pendências no Processo" />
								<input name="inputResponsaveis" type="image" src="./imagens/imgAssistente.png" 	
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarDWR%>');" title="Consultar Responsáveis pelo Processo" />
								<% if (UsuarioSessao.isPodeGerarPdfCompleto()){ %>
									<img src="imagens/22x22/btn_pdf.png" alt="Gerar PDF de Processo Completo" title="Gerar PDF de Processo Completo" onclick="window.open('ProcessoCompletoPDF?idProcesso=<%=processoDt.getId()%>&amp;PaginaAtual=<%=Configuracao.Imprimir%>','Processo Completo','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=yes')">
								<% } %>
							</div>
						</fieldset>
					</fieldset>
						
					<!-- MOVIMENTAÇÕES -->
					<fieldset class="formEdicao">
			    		<legend> Eventos do Processo </legend>
			    		
			    		<div align="right">			    				    				
	    					<% if (UsuarioSessao.isPodeBloquear(processoDt.getId(), processoDt.getId_Serventia())){ %>
    						<input type="checkbox" id="chkVer" value="false" title="Clique para Bloquear ou Desbloquear Movimentações e Arquivos" onclick="MostrarOpcoes()">Bloquear/Desbloquear
	    					<% } %>
			    		</div>
		    			<table id="TabelaArquivos" class="Tabela">
		        			<thead>
		            			<tr class="TituloColuna">
		                			<td>Nº</td>
		                   			<td width="45%">Movimentação</td>
		                  			<td width="10%">Data</td>
		                  			<td width="15%">Usuário</td>
		                  			<td>Arquivo(s)</td>
		                  			<% if (UsuarioSessao.isPodeGerarEvento()){ %>
		                  			<td class="colunaMinima" title="Manter Evento(s) em Movimentação">Evento(s)</td>
		                  			<%} %>
		                  			<% if (UsuarioSessao.isPodeGerarPendencia()){ %>
		                  			<td class="colunaMinima" title="Gerar Pendência(s) em Movimentação">Pendência(s)</td>
		                  			<%} %>
		                  			
		               			</tr>
		           			</thead>
		    				<tbody id="tabListaProcesso">
				    		<%
				   				List listaMovimentacoes = processoDt.getListaMovimentacoes();	
				    		    boolean boLinha=false; 
						   	    if (listaMovimentacoes != null){
						   	    	for (int i=listaMovimentacoes.size()-1;i>=0;i--){
					   			  		MovimentacaoDt movimentacaoDt = (MovimentacaoDt)listaMovimentacoes.get(i);
							%>			
								<tr <%=(!movimentacaoDt.isValida()?"class='bloqueado' title='Movimentação Bloqueada'":"class='TabelaLinha" + (boLinha?1:2) + "'")%>>
		                   			<td class="colunaMinima"> <%=i+1%></td>
		                    		<td width="55%"><%=movimentacaoDt.getMovimentacaoTipo()%><br /><%=movimentacaoDt.getComplemento()%></td>
			                   		<td width="100" align="center"><%=movimentacaoDt.getDataRealizacao()%></td>
			                   		<td width="25%">
			                   			<%=(!movimentacaoDt.isValida()? "Não disponível" : movimentacaoDt.getNomeUsuarioRealizador())%>
			                   		</td>
									<td class="colunaMinima">
									<%
										if (movimentacaoDt.temArquivos()){
									%>
										<a href="javascript:buscarArquivosMovimentacao('<%=movimentacaoDt.getId()%>', '<%=request.getAttribute("tempRetorno")%>', 'Id_MovimentacaoArquivo', <%=Configuracao.Curinga6%>, '<%=UsuarioSessao.isPodeBloquear(processoDt.getId(), processoDt.getId_Serventia())%>')">
											<img src="imagens/22x22/ico_arquivos.png" alt="Mostrar ou ocultar arquivos" title="Mostrar ou ocultar arquivos" />
										</a>
									</td>
									<%
										}
									%>
									<input id="MovimentacaoDataRealizacaoTipo" name="MovimentacaoDataRealizacaoTipo" type="hidden" value="" />
									<%
										if (UsuarioSessao.isPodeGerarEvento() && !movimentacaoDt.getId_UsuarioRealizador().equals(UsuarioServentiaDt.SistemaProjudi)){
									%>
	  									<td class="colunaMinima">		
										<%
													if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))
																							|| movimentacaoDt.temArquivos()){
												%>
	  										<img id="imgAlterar" class="imgAlterar" src="imagens/imgEditar.png" alt="Manter Evento(s) em Movimentação" title="Manter Evento(s) em Movimentação" 
	  										onclick="javascript: manterEventos('<%=movimentacaoDt.getDataRealizacao().substring(0,10) + " - " + movimentacaoDt.getMovimentacaoTipo()%>','<%=movimentacaoDt.getId()%>','<%=Configuracao.Localizar%>')"/>
			    						<%
			    							}
			    						%>
	  									</td>
			    					<%
			    						}
			    					%>
									<td class="colunaMinima">										
									<%
																				if(movimentacaoDt.isValida()){
																			%>
	  								<img id="imgValidar" class="imgValidar"  src="imagens/22x22/ico_fechar.png" alt="Bloquear Movimentação" title="Bloquear Movimentação" 
	  									onclick="javascript: mudarStatusMovimentacao('<%=movimentacaoDt.getId()%>', '<%=movimentacaoDt.getMovimentacaoTipo()%>','INVÁLIDA','<%=Configuracao.Curinga7%>')"/>
	  								<%}else { %>
	 								<img id="imgValidar" class="imgValidar" src="imagens/22x22/ico_liberar.png" alt="Desbloquear Movimentação" title="Desbloquear Movimentação" 
	 									onclick="javascript: mudarStatusMovimentacao('<%=movimentacaoDt.getId()%>', '<%=movimentacaoDt.getMovimentacaoTipo()%>','VÁLIDA','<%=Configuracao.Curinga6%>')"/>
	  								<%}%>
	  								
	  								<% if (UsuarioSessao.isPodeGerarPendencia()	&& !movimentacaoDt.getId_UsuarioRealizador().equals(UsuarioServentiaDt.SistemaProjudi)){ %>
	  									<img id="imgAlterar" class="imgAlterar" src="imagens/22x22/btn_movimentar.png" alt="Gerar Pendências em Movimentação" title="Gerar Pendências em Movimentação" 
	  										onclick="javascript: gerarPendencias('<%=movimentacaoDt.getId()%>')"/>
			    					<% } %>
			    					
	  								
									<td>
								</tr>
								<tr id="linha_<%=movimentacaoDt.getId()%>" style="display: none;">
									<td colspan="5" id="pai_<%=movimentacaoDt.getId()%>" class="Linha"></td>
								</tr>
							<%		
									boLinha = !boLinha;
									}	 
								}
							%>
		           			</tbody>
				    	</table>
					</fieldset>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>