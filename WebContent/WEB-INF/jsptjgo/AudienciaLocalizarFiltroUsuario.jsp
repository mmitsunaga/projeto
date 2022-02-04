<!-- DOCTYPE -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- IMPORTS -->
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<!-- FIM IMPORTS -->

<jsp:useBean id="Audienciadt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaDt" />

<!-- HTML -->
<html>
	<!-- HEAD -->
	<head>
		<!-- TITLE -->
		<title>Consultar Audiências Por Filtro - Advogado</title>
    	
    	<!-- META -->
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		
		<!-- STYLE -->
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		<!-- FIM STYLE -->
		
		<!-- SCRIPTS -->
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<!-- FIM SCRIPTS -->
		
	</head>
	<!-- FIM HEAD -->
	
	<!-- BODY -->
	<body>
		<!-- DIV CORPO -->
		<div id="divCorpo" class="divCorpo">
			<!-- Título da Página -->
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%></h2></div>
			
			<!-- FORM -->
			<form name="Formulario" id="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
			
			<%
				AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
 				if (Audienciadt.getAudienciaProcessoDt() != null && Audienciadt.getAudienciaProcessoDt() instanceof AudienciaProcessoFisicoDt) {
 					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)Audienciadt.getAudienciaProcessoDt();
 				}
 			%>
				<!-- HIDDEN -->
				<!-- Página Atual -->
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<!-- Menu Acionado -->
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>" />
				
					<!-- DIV LOCALIZAR -->
				<div id="divLocalizar" class="divLocalizar">
					
					<!-- FIELDSET PARÂMETROS PARA CONSULTA -->
					<fieldset id="formLocalizar" class="formLocalizar">
						<!-- Legenda -->
						<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Parâmetros para Consulta</legend>
					   	
					    	<!-- Data Inicial -->
					    <div class="col15">
						<label for="DataInicialConsulta">Data Inicial</label><br> 
			    		<input class="formLocalizarInput" name="DataInicialConsulta" id="DataInicialConsulta" type="text" size="10" maxlength="10" title="Clique para escolher uma data." value="<%=Audienciadt.getDataInicialConsulta()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)"/> 
			    		<img id="calDataInicialConsulta" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicialConsulta,'dd/mm/yyyy',this)" />			    	
					    </div>
					    
						<!-- Data Final -->
						<div class="col15">
			    		<label for="DataFinalConsulta">Data Final</label><br> 
			    		<input class="formLocalizarInput" name="DataFinalConsulta" id="DataFinalConsulta" type="text" size="10" maxlength="10" title="Clique para escolher uma data." value="<%=Audienciadt.getDataFinalConsulta()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)"/> 
			    		<img id="calDataFinalConsulta" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinalConsulta,'dd/mm/yyyy',this)" />
						</div>
						<label for="Aviso" style="float:left;margin-left:25px;color:red;font-size:16px;"><small>Período entre Data Inicial e Data Final não pode superar 03(três) meses.</small></label><br>  
						
						<div class="clear"></div>
					
						<!-- Número do Processo -->
				       	<label class="formLocalizarLabel" id="formLocalizarLabel">Número do Processo</label><br> 
				       	<%if(Audienciadt.getAudienciaProcessoDt().getProcessoDt() != null){%>
				       		<input class="formLocalizarInput" id="ProcessoNumero" name="ProcessoNumero" type="text" size="38" maxlength="30" value="<%=Audienciadt.getAudienciaProcessoDt().getProcessoNumero()%>" />
				       	<%} else if (audienciaProcessoFisicoDt != null) {%>
				       		<input class="formLocalizarInput" id="ProcessoNumero" name="ProcessoNumero" type="text" size="38" maxlength="30" value="<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F" />
				       	<%}%>				       		
				       	<br />
					
						<!-- Status da Audiência do Processo -->
					   	<label class="formLocalizarLabel" for="Id_AudienciaProcessoStatus">Status da Audiência
					   	<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaProcessoStatus" name="imaLocalizarAudienciaProcessoStatus" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AudienciaProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
						<input class="FormEdicaoimgLocalizar" id="imaLimparAudienciaProcessoStatus" name="imaLimparAudienciaProcessoStatus" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AudienciaProcessoStatus','AudienciaProcessoStatus'); return false;" />
					   	</label><br>
					   	
						<input class="formLocalizarInput" id="AudienciaProcessoStatus" name="AudienciaProcessoStatus" type="text" readonly="readonly" size="30" maxlength="100" value="<%=Audienciadt.getAudienciaProcessoDt().getAudienciaProcessoStatus()%>" />
						<input type="hidden" id="Id_AudienciaProcessoStatus" name="Id_AudienciaProcessoStatus" value="" />	   	
					   	<br />
					   	
					   	<!-- DIV BOTÕES -->
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<!-- Botão Localizar -->
							<input class="imgLocalizar" id="imgLocalizar" name="imaLocalizar" value="Consultar" type="submit"  title="Localizar - Localiza audiências de acordo com os campos informados" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
							<!-- Botão Limpar -->
							<input class="imgNovo" id="imgNovo" name="imaNovo" value="Limpar" type="submit" title="Novo - Limpar Tela" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
						</div>
						<!-- FIM DIV BOTÕES -->
					</fieldset>
					<!-- FIM FIELDSET EDITAR -->
					<br />
					
					<!-- DIV TABLE -->
				   	<div id="divTabela" class="divTabela" >
				   	
				    	<!-- TABLE -->
				    	<table id="Tabela" class="Tabela">
				    		<!-- thead -->
				        	<thead>
				            	<tr class="TituloColuna">
				                   	<th width="10%">Data/Hora</th>
				                  	<th>Processo</th>
				                  	<th>Promovente</th>
				                  	<th>Promovido</th>
				                  	<th>Tipo de Audiência</th>
				                  	<th width="10%">Status</th>
				                  	<th width="8%">Movimentação</th>
				               	</tr>
				           	</thead>
				           	<!-- Fim thead -->
				           	
				           <!-- tbody -->
				           	<tbody id="tabListaAudiencia"> 
								<%List listaAudiencias = (List)request.getAttribute("ListaAudiencias");
				 				AudienciaDt audienciaDt;
				  				boolean linha = false;
				  				int quantidadeProcessos = 1;

				  				//IF/ELSE LISTA DE AUDIÊNCIAS CONSULTADAS (Verificar se foram encontradas audiências)
				  				if (listaAudiencias != null){
				  				    // Organizar os dados de cada audiência consultada para montar seu dados na tela
				  				    
				  				    //FOR LISTA DE AUDIÊNCIAS CONSULTADAS (For para a lista de audiências consultadas)
				  					for(int i = 0 ; i < listaAudiencias.size(); i++) {
				  	  					audienciaDt = (AudienciaDt)listaAudiencias.get(i);
				  	  					//Buscar o(s) processo(s) vinculados à audiência em questão
				  	  					List processosAudiencia = audienciaDt.getListaAudienciaProcessoDt();
				  	  					//Verificar se a audiência está vinculada a algum processo
				  	  					if (processosAudiencia != null && processosAudiencia.size() > 0) {
				  	  						quantidadeProcessos = processosAudiencia.size();
				  	  					}
				  	  					//Variável para controlar se o processo é o primeiro a ser exibido vinculado à audiência
				  	  					boolean primeiro = true; %>
				  	  					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  	                		
				  	                		<%//Deve mostrar a data da audiência somente uma vez no caso de ter mais de um processo vinculado à audiência
				  	                		  if (primeiro){ %> 
					  	                		<td rowspan="<%=quantidadeProcessos%>" align="center"> <%=audienciaDt.getDataAgendada()%></td>
				  	                   		<%}
				  	                   		
				  	                   		//IF/ELSE LISTA DE PROCESSOS DA AUDIÊNCIA
				  	                		if (processosAudiencia != null && processosAudiencia.size()>0){
				  	                   		    //FOR LISTA DE PROCESSOS DA AUDIÊNCIA (For para a lista de processos da audiência em questão)
				  	                   			for (int j=0; j < audienciaDt.getListaAudienciaProcessoDt().size();j++){
				  	                   				AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosAudiencia.get(j); 
				  	                   				audienciaProcessoFisicoDt = null;
					  	                   			if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
						  	           					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
						  	           				}
				  	                   			%>		
				  	                   				<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
				  	                   				<% if (!primeiro){%> 
				  	                   					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  	                   				<%}%>
				  	                   						<!-- PROCESSO --> 
				  	                   						<%if(audienciaProcessoDt.getProcessoDt() != null){%>
				  	                   							<td>
					  	                							<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
					  	                								<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
					  	                							</a>
					  	                						</td>
				  	                						<%} else if (audienciaProcessoFisicoDt != null) {%>
					  	                   						<td>
					  	                							<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
					  	                						</td>
				  	                   						<%}else{%>
				  	                   							<td>-</td>
				  	                   						<%}%>
				  	                   						<!-- FIM PROCESSO -->
				  	                   				
					  	                					<!-- PROMOVENTES -->
					  	                					<td>
					  	                						<ul class="partes">
					  	                							<%if(audienciaProcessoDt.getProcessoDt() != null){%>
						  	                							<%
						  	                								List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
						  	                											  	                							if (listaPromoventes != null){
						  	                											  	                								for (int y = 0; y < listaPromoventes.size(); y++){
						  	                											  	                									ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
						  	                							%>
						  	                									<li><%=promovente.getNome()%></li>
						  	                								<%}
						  	                							}
					  	                							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) { %>
						  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente() == null ? "" : audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
						  	                						<%}%>
					  	                						</ul>
					  	                					</td>
					  	                					<!-- FIM PROMOVENTES -->
					  	                			
					  	                					<!-- PROMOVIDOS -->
						  	            					<td>
						  	            						<ul class="partes">
						  	            							<%if(audienciaProcessoDt.getProcessoDt() != null){%>
						  	                							<%
						  	                								List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
						  	                											  	                							if (listaPromovidos != null){
						  	                											  	                								for (int y = 0; y < listaPromovidos.size(); y++){
						  	                											  	                									ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
						  	                							%>	
						  	                									<li><%=promovido.getNome()%> </li>
						  	                								<%}
						  	                							}
						  	            							} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) { %>
						  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido() == null ? "" : audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
						  	                						<%}%>
					  	                						</ul>
					  	                					</td>
					  	                					<!-- FIM PROMOVIDOS -->
					  	                					
					  	                					<td><%=audienciaDt.getAudienciaProcessoDt().getAudienciaTipo()%></td>
					  	                			
							  	                			<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
							  	                			<%if (primeiro){ %>
							  	                					<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatus()%></td>
							  	                					<%primeiro = !primeiro;
						  	                   				} else { %> 
						  	                   					</tr> 	
						  	                   				<%}
							  	                			if ((audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA)) &&
							  	                			    (request.getAttribute("podeMovimentar") != null && request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")))){ %>
								  	                   			<td rowspan="<%=quantidadeProcessos%>">
								  	                   				<%if (audienciaProcessoFisicoDt != null) {%>
										  	                   				<a href="AudienciaProcessoFisicoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Concluir - audiência(s) selecionada(s)">
										  	                   				Concluir
										  	                   				</a>
										  	                   			<% }else{%>
										  	                   				<a href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>" title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
										  	                   				Concluir
										  	                   				</a>
										  	                   			<% } %>
								  	                   			</td>
							  	                   			<% }else{%>
							  	                   				<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
							  	                   			<% } 
				  	                			} //FIM FOR LISTA DE PROCESSOS DA AUDIÊNCIA
				  	                   		}else { %>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   			<td>-</td>
				  	                   		<%}%> <!-- FIM IF/ELSE LISTA DE PROCESSOS DA AUDIÊNCIA -->
				  	                   		
				  	               		</tr>
				  						<%linha = !linha;
				  					} //FIM FOR LISTA DE AUDIÊNCIAS CONSULTADAS
								}else{%> 
				  	               <tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  						<td>-</td>
				  					</tr>
				  				<%}%> <!-- FIM IF/ELSE LISTA DE AUDIÊNCIAS CONSULTADAS -->
				           	</tbody>
				           	<!-- Fim tbody -->
				       	</table>
				       	<!-- FIM TABLE -->
				   	</div>
				   	<!-- FIM DIV TABLE -->
				</div>
				<!-- FIM DIV EDITAR-->
				<!-- PAGINAÇÃO -->
				<%if (listaAudiencias != null){%>
				    <%@ include file="Padroes/PaginacaoAudiencia.jspf"%>
				<%}%>
			<!-- FIM PAGINAÇÃO -->
			</form>
			<!-- FIM FORM -->
			
			<!-- MENSAGEM --> 
			<%@ include file="Padroes/Mensagens.jspf" %>
			<!-- FIM MENSAGEM -->
		</div>
		<!-- FIM DIV CORPO -->
	</body>
	<!-- FIM BODY -->
</html>
<!-- FIM HTML -->