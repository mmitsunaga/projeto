<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>

<jsp:useBean id="Audienciadt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaDt" />

<html>
	<head>
		<title>Consultar Audiências Por Filtro</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
    	
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>      	
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
				
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; AUDIÊNCIAS PENDENTES</h2></div>
			<form name="Formulario" id="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
			
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>" />
				
				<div id="divLocalizar" class="divLocalizar">
					<fieldset id="formLocalizar" class="formLocalizar">
						<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Parâmetros para Consulta</legend>
						
						<div class="col15">
						<label for="DataInicialConsulta">Data Inicial</label><br> 
			    		<input class="formLocalizarInput" name="DataInicialConsulta" id="DataInicialConsulta" type="text" size="10" maxlength="10" title="Clique para escolher uma data." value="<%=Audienciadt.getDataInicialConsulta()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)"/> 
			    		<img id="calDataInicialConsulta" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicialConsulta,'dd/mm/yyyy',this)" />			    	
					    </div>
					    
					    <div class="col30">
			    		<label for="DataFinalConsulta">Data Final</label><br> 
			    		<input class="formLocalizarInput" name="DataFinalConsulta" id="DataFinalConsulta" type="text" size="10" maxlength="10" title="Clique para escolher uma data." value="<%=Audienciadt.getDataFinalConsulta()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)"/> 
			    		<img id="calDataFinalConsulta" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinalConsulta,'dd/mm/yyyy',this)" />
						</div>
					
					    <div class="col30 clear">
				       	<label class="formLocalizarLabel" id="formLocalizarLabel">Número do Processo</label><br> 
				       	<input class="formLocalizarInput" id="ProcessoNumero" name="ProcessoNumero" type="text" size="38" maxlength="30" value="<%=Audienciadt.getAudienciaProcessoDt().getProcessoNumero()%>" />
				       	</div>
					
						<div class="col45 clear">
					   	<label class="formLocalizarLabel" for="Id_AudienciaProcessoStatus">Status da Audiência
					   	<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaProcessoStatus" name="imaLocalizarAudienciaProcessoStatus" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AudienciaProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
						<input class="FormEdicaoimgLocalizar" id="imaLimparAudienciaProcessoStatus" name="imaLimparAudienciaProcessoStatus" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AudienciaProcessoStatus','AudienciaProcessoStatus'); return false;" />  	
					   	</label><br>
					   	
						<input class="formLocalizarInput" id="AudienciaProcessoStatus" name="AudienciaProcessoStatus" type="text" readonly="readonly" size="30" maxlength="100" value="<%=Audienciadt.getAudienciaProcessoDt().getAudienciaProcessoStatus()%>" />
						<input type="hidden" id="Id_AudienciaProcessoStatus" name="Id_AudienciaProcessoStatus" value="" />	 
						
					   	</div>
	
						<div class="col45 clear">
						<label class="formLocalizarLabel" for="Id_ServntiaCargo">Cargo da Serventia
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" readonly="readonly" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
						<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargo" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" />
						</label><br>
						
<%-- 						<input class="formLocalizarInput" id="ServentiaCargo" name="ServentiaCargo" type="text" readonly="readonly" size="60" maxlength="100" value="<%=Audienciadt.getAudienciaProcessoDt().getServentiaCargo()%>" /> --%>
												
						<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
							<input class="formLocalizarInput" id="ServentiaCargo" name="ServentiaCargo" type="text" readonly="readonly" size="60" maxlength="100" value="<%=Audienciadt.getAudienciaProcessoDt().getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>" />
						<% } else {%>							
							<input class="formLocalizarInput" id="ServentiaCargo" name="ServentiaCargo" type="text" readonly="readonly" size="60" maxlength="100" value="<%=Audienciadt.getAudienciaProcessoDt().getServentiaCargo()%>" />
						<% } %>
						
						
						<input type="hidden" id="Id_ServentiaCargo" name="Id_ServentiaCargo" value="" /> 
						
						</div>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados clear">
							<input class="imgLocalizar" id="imgLocalizar" name="imaLocalizar" value="Consultar" type="submit"  title="Localizar - Localiza audiências de acordo com os campos informados" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
							<input class="imgNovo" id="imgNovo" name="imaNovo" value="Limpar" type="submit" title="Novo - Limpar Tela" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
						</div>
						
					</fieldset>
					
				   	<div id="divTabela" class="divTabela">
					   	<div align="left">
							<input name="imgMultipla" type="submit" value="Trocar Responsável" onclick="AlterarAction('Formulario','AudienciaProcesso');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
						</div>
			
				   		<table id="Tabela" class="Tabela">
				        	<thead>
								<tr class="TituloColuna1">
									<th colspan="8">Serventia: <%=Audienciadt.getServentia()%></th>
								</tr>
				            	<tr class="TituloColuna">
				            		<th class="colunaMinina">
            							<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" title="Alterar os estados de todos os itens da lista" />
			    					</th>
				                   	<th width="10%">Data/Hora</th>
				                  	<th>Processo</th>
				                  	<th>Promovente</th>
				                  	<th>Promovido</th>
				                  	<th width="10%">Status</th>
				                  	<th width="8%">Responsável</th>
				                  	<th></th>
				               	</tr>
				           	</thead>
				           	
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
				  	  					 %>
				  	  					<tr class="TabelaLinha<%=(linha?1:2)%>"> 
				  	                   		<%
				  	                   		//IF/ELSE LISTA DE PROCESSOS DA AUDIÊNCIA
				  	                		if (processosAudiencia != null && processosAudiencia.size()>0){
				  	                   		    //FOR LISTA DE PROCESSOS DA AUDIÊNCIA (For para a lista de processos da audiência em questão)
				  	                   			for (int j=0; j < audienciaDt.getListaAudienciaProcessoDt().size();j++){
				  	                   				AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosAudiencia.get(j); 
				  	                   				AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
				  	                   				if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
				  	                   					audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
				  	                   				}%>
				  	                   				
				  	                   				<td rowspan="<%=quantidadeProcessos%>">
				  	  									<input class="formEdicaoCheckBox" name="audienciaProcessos" type="checkbox" value="<%=audienciaProcessoDt.getId()%>">
							  	          			</td>
							  	          	
				  	                   				<td rowspan="<%=quantidadeProcessos%>" align="center"> <%=audienciaDt.getDataAgendada()%></td>
				  	                   				
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
				  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
				  	                						<%}%>
			  	                						</ul>
			  	                					</td>
			  	                			
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
				  	                							<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
				  	                						<%}%>
			  	                						</ul>
			  	                					</td>
			  	                			
			  	                					<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatus()%></td>
					  	                			
					  	                			<td rowspan="<%=quantidadeProcessos%>"> <%=audienciaProcessoDt.getServentiaCargo() %> </td>
					  	                			<%
					  	                			if (audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo().equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))){%>
					  	                   			<td rowspan="<%=quantidadeProcessos%>">
														<a href="AudienciaProcesso?PaginaAtual=<%=Configuracao.Novo%>&amp;Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>">
															  <img src="imagens/22x22/btn_encaminhar.png" alt="Solucionar" title="Efetuar troca de responsável" /> 
														</a> 
					  	                   			</td>
					  	                   			<% }else{%>
					  	                   			<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
					  	                   			<% } 
					  	                			} //FIM FOR LISTA DE PROCESSOS DA AUDIÊNCIA
					  	                   		}else { %>
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
				  				<%}%> 
				           	</tbody>
				       	</table>
				       	
				   	</div>
				   	
				</div>

				<% if (listaAudiencias != null){%>
				    <%@ include file="Padroes/PaginacaoAudiencia.jspf"%>
				<%}%>
				
			</form>
			
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
		
	</body>
</html>