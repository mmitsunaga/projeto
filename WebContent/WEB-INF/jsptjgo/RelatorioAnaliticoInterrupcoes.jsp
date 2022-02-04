<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page  import="java.util.Iterator"%>
<%@page  import="br.gov.go.tj.projudi.dt.RelatorioInterrupcao"%>
<%@page  import="br.gov.go.tj.projudi.dt.OcorrenciaInterrupcao"%>
<%@page  import="br.gov.go.tj.projudi.dt.OcorrenciaDiariaInterrupcao"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.TJDataHora"%>

<jsp:useBean id="RelIndisponibilidadeDt" scope="request" class= "br.gov.go.tj.projudi.dt.RelatorioInterrupcao"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Interrup&ccedil;&otilde;es do sistema  </title>
		
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');			
		</style>
		
		
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	  	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>	  	   
	  	
		<style>
			
			.TotalizadorDireita{					
				background-color:#C8C8C8;
				font-weight:bold;
				text-align:right !important;						
			}
			.Totalizador{				
				background-color:#C8C8C8;
				font-weight:bold;		
			}			
			.Justificado{
				text-align: justify !important;
			}
		</style>		
	</head>

	<body class="fundo">
		
		 	    
            
			
			<% 
  			String nomeSistema = "Processo Judicial";
  			if (request.getAttribute("Sistema") != null && request.getAttribute("Sistema").equals("2")) {
  				nomeSistema= "PJe"; %>
  				<p>
	    			<img width="870" height="160" alt="bnr pje2" src="imagens/bnr_pje2.png"></img>
				</p>
				<p></p>
  			<% } else { %>
  				<style type="text/css"> #bkg_projudi{ display:none } </style>
  				<%@ include file="/CabecalhoPublico.html" %>
  			<% }  %> 		
			
			<div class="divCorpo">
			<div class="area"><h2>&raquo; 
				Interrup&ccedil;&otilde;es do sistema - <%=nomeSistema%> 
			</h2></div>	
			
			<form action="RelatorioInterrupcoes" method="post" name="Formulario" id="Formulario">	
			
				<input id="RelatorioPDF" name="RelatorioPDF" type="hidden" value="<%=request.getAttribute("RelatorioPDF")%>"></input>
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				<input id="Sistema" name="Sistema" type="hidden" value="<%=request.getAttribute("Sistema")%>">
				
				<div id="divPortaBotoes" class="divPortaBotoes">															
					<input id="imgImprimir" alt="Imprimir"  class="imgImprimir" title="Imprimir - Gerar relatorio em pdf" name="imaImprimir" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PassoBusca','<%=String.valueOf(Configuracao.Imprimir)%>')" />
					<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PassoBusca','<%=String.valueOf(Configuracao.Novo)%>')" />
				</div>               						
			
				<div id="divLocalizar" class="divLocalizar" >
				
									
								<div id="divResulucao" class="divEditar">
									<fieldset class="formEdicao" > 
									
									<legend class="formEdicaoLegenda">RESOLU&Ccedil;&Atilde;O N�59, Art. 7�, DE 04 DE JULHO DE 2016.</legend>
										<div style="text-align:left;">
											<p> Na hip�tese de indisponibilidade do sistema, dever�o ser adotadas as seguintes provid�ncias:</p>
											<ol type="I">
												 <li> nas interrup��es programadas, determinadas pela autoridade competente, as medidas indicadas no ato que as anunciar; </li>
												 <li> nos demais casos, o registro da ocorr�ncia no sistema com a indica��o da data e hora do in�cio e do t�rmino da indisponibilidade.</li>
											</ol>
											
											<p>� 1o Considera-se indisponibilidade por motivo t�cnico a interrup��o de acesso ao sistema decorrente de falha nos equipamentos e programas de bancos de dados do Judici�rio, na sua aplica��o e conex�o com a internet, certificada pela Diretoria de Inform�tica.</p>
											<p>� 2o N�o se considera indisponibilidade por motivo t�cnico a impossibilidade de acesso ao sistema que decorrer de falha nos equipamentos ou programas dos usu�rios ou em suas conex�es � internet.</p>
											<p>� 3o Ficam prorrogados os prazos quando as interrup��es ultrapassarem 60 (sessenta) minutos consecutivos ou intercalados, no per�odo entre 06:00 e 23h59m, dos dias �teis.</p>
											<p>� 4o O juiz da causa poder� determinar eventual prorroga��o de prazo em curso, inclusive quando a falha de acesso ao PJD/TJGO decorrer de problemas referidos no � 1o deste artigo, cabendo �s respectivas escrivanias cumprir a decis�o em cada processo.</p>
											<p>� 5o Em caso de indisponibilidade absoluta do PJD/TJGO, devidamente certificada, e para o fim de evitar perecimento de direito ou ofensa � liberdade de locomo��o, a peti��o inicial poder� ser protocolada em meio f�sico para distribui��o manual por quem for designado pela Presid�ncia do Tribunal de Justi�a do Estado de Goi�s ou pela Diretoria do Foro, conforme o caso, com posterior digitaliza��o e inser��o no sistema pelo ju�zo a que for distribu�da.</p>
										</div>	
									</fieldset>						
								</div> 					
						
									
					
						
									<div id="disponibilidade24h" class="divEditar">
										<fieldset class="formEdicao"  > 
											<legend class="formEdicaoLegenda">Disponibilidade nas &uacute;ltimas 24 horas do dia 
											                                  <%= RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy() %></legend>
											<label class="formEdicaoLabel"><%=RelIndisponibilidadeDt.getPercentualDeDisponibilidadeNasUltimas24Horas()%>%</label><br>										
										</fieldset>						
									</div>
								
									<div id="disponibilidade30d" class="divEditar">
										<fieldset class="formEdicao"  > 
											<legend class="formEdicaoLegenda">Disponibilidade no per&iacute;odo: 
										     <%= RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoInicialUtilizado().getDataFormatadaddMMyyyy()%>
											 a 
											 <%= RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy()%>
											 <%if (RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoFinalUtilizado().isHoje()) {%>
											 	�s
											 	<%= new TJDataHora().getHoraFormatadaHHmm()%>
											 <% } %>
										   </legend>
											<label class="formEdicaoLabel"><%=RelIndisponibilidadeDt.getPercentualDeDisponibilidadeNoPeriodo()%>%</label><br>										
										</fieldset>						
									</div>
									
						<div id="divTabela" class="divTabela" > 
							<table id="Tabela" class="Tabela">
								<thead>
									<tr class="legenda">
										<td class="tipo" colspan="6">Interrup&ccedil;&otilde;es do <%=nomeSistema%> no per&iacute;odo:  
											 <%=RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoInicialUtilizado().getDataFormatadaddMMyyyy()%>
											 a
											 <%= RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy()%>
											 <%if (RelIndisponibilidadeDt.getParametrosUtilizados().getPeriodoFinalUtilizado().isHoje()) {%>
											 	�s
											 	<%= new TJDataHora().getHoraFormatadaHHmm()%>
											 <% } %>
									    </td>
									</tr>
									<tr>
										<th class="colunaMinima"></th>
										<th>QUEDA</th>	
										<td>RETORNO</td>
										<td>DURA��O</td>
										<td>MOTIVO/TIPO</td>					 
									</tr>
								</thead>
								<tbody id="tabListaIndisponibilidade">						
									<%									
			 							if (!RelIndisponibilidadeDt.possuiInterrupcoes()) { 
			 						%>
			 							<tr class="TabelaLinha2"  >
											<td class="Centralizado" colspan="5">
											 	O sistema n&atilde;o apresentou interrup&ccedil;&otilde;es.											 
											</td>																											
										</tr>	
			 						<%
			 							} else {
			 					
										Iterator<OcorrenciaDiariaInterrupcao> iteratorDiario = RelIndisponibilidadeDt.getIteratorInterrupcoesDiarias();
										Iterator<OcorrenciaInterrupcao> iteratorItem = null;
												
										OcorrenciaDiariaInterrupcao objDiarioTemp = null;														
										OcorrenciaInterrupcao objTemp = null;																						
																																																				
										boolean boLinha=false;									
										long qtdeLinha = 0;
										
										while (iteratorDiario.hasNext()){
											objDiarioTemp = iteratorDiario.next();
											iteratorItem = objDiarioTemp.getIteratorDeInterrupcoes();
																												
											while (iteratorItem.hasNext()){
																														
												objTemp = iteratorItem.next();
												qtdeLinha += 1;
									%>										
												<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
													<td class="Centralizado">
														 <%=qtdeLinha%>
													</td>
													<td class="Centralizado">
														<%= objTemp.getPeriodoInicial().getDataFormatadaddMMyyyyHHmmss() %>
													</td>
													<td class="Centralizado">
														<%= objTemp.getPeriodoFinal().getDataFormatadaddMMyyyyHHmmss() %>
													</td>
													<td>
														<%= objTemp.getTempoDeInterrupcao() %>
													</td>
													<td>
														<%= objTemp.getMotivo() %>
													</td>																											
												</tr>	
																		
										<%
												boLinha = !boLinha;
											}
											
											%>
											
											<tr>
												<td class="TotalizadorDireita" colspan="4">
													Total no dia <%= objDiarioTemp.getDataDeReferencia().getDataFormatadaddMMyyyy() %>:  
												</td>
												<td class="Totalizador">
													<%= objDiarioTemp.getTempoTotalDeInterrupcoes() %>
												</td>																											
											</tr>	
											
											<%
											
											boLinha = false;
										}//Fim do while	
										
			 							}//Fim do if/else
																						
									%>
								</tbody>
							</table>			
						</div>				
							
				</div> 
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
		</div> 
		
		<%@ include file="Padroes/Mensagens.jspf" %>

	</body>
</html>