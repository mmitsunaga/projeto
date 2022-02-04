<!-- DOCTYPE -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- IMPORTS -->
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<!-- FIM IMPORTS -->

<!-- BEANS -->
<jsp:useBean id="Audienciadt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaDt" />
<!-- FIM BEANS -->

<!-- HTML -->
<html>
	<!-- HEAD -->
	<head>
		<!-- TITLE -->
		<title>Listar Audiências Livres Para Agendamento Manual</title>
    	
    	<!-- META -->
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
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
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<!-- FIM SCRIPTS -->
					
	</head>
	<!-- FIM HEAD -->
	
	<!-- BODY -->
	<body>
		<!-- DIVCORPO -->
		<div id="divCorpo" class="divCorpo">
			<!-- Título da Página -->
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%></h2></div>
	  				
			<!-- FORM -->
			<form name="Formulario" id="Formulario" action="Audiencia" method="post" >
				
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden" id="PaginaAnterior" name="PaginaAnterior" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>" />
				<input type="hidden" id="Id_Audiencia" name="Id_Audiencia" />
				<input type="hidden" id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" />
				<input type="hidden" id="AudienciaTipoCodigo" name="AudienciaTipoCodigo" value="<%=Audienciadt.getAudienciaTipoCodigo()%>" />
			    
			   	<!-- DIV LOCALIZAR -->
				<div id="divLocalizar" class="divLocalizar">
			    	
			    	<!-- FIELDSET PRAZO PARA AGENDAMENTO DA AUDIÊNCIA -->
					<fieldset id="formLocalizar" class="formLocalizar">
						<!-- Número do processo -->
						<legend>
							Processo Nº 
							<%if(Audienciadt.getAudienciaProcessoDt() != null && Audienciadt.getAudienciaProcessoDt().getProcessoDt() != null) { %>
								<a href="BuscaProcesso?Id_Processo=<%=Audienciadt.getAudienciaProcessoDt().getProcessoDt().getId_Processo()%>">
									<%=Audienciadt.getAudienciaProcessoDt().getProcessoDt().getProcessoNumero()%>
								</a>
							<%} %>
						</legend>
						<div class="col25">
						<label for="DataInicialConsulta">Data Inicial</label><br> 
			    		<input class="formLocalizarInput" name="DataInicialConsulta" id="DataInicialConsulta" type="text" size="10" maxlength="10" value="<%=Audienciadt.getDataInicialConsulta()%>" /> 
			    		<img id="calendarioDataInicial" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicialConsulta,'dd/mm/yyyy',this)" />			    	
					    </div>
					    <div class="col25">
			    		<label for="DataFinalConsulta">Data Final</label><br> 
			    		<input class="formLocalizarInput" name="DataFinalConsulta" id="DataFinalConsulta" type="text" size="10" maxlength="10" value="<%=Audienciadt.getDataFinalConsulta()%>" /> 
			    		<img id="calendarioDataFinal" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinalConsulta,'dd/mm/yyyy',this)" />
						</div>
						<label for="Aviso" style="float:left;color:red;font-size:16px;"><small>Período entre Data Inicial e Data Final não pode superar 03(três) meses.</small></label><br>  
						<div class="col50 clear">
						<!-- Prazo para agendamento da audiência -->
			       		<label class="formLocalizarLabel" id="PrazoAgendamentoAudiencia" >Prazo Agendamento(dias)</label><br> 
			       		<input class="formLocalizarInput" id="PrazoAgendamentoAudiencia" name="PrazoAgendamentoAudiencia" type="text" size="15" maxlength="20" onkeypress="return DigitarSoNumero(this, event)" value="<%=Audienciadt.getPrazoAgendamentoAudiencia()%>" />
			       		</div>
			       		<div class="clear"></div>
			    		<!-- DIV BOTÕES -->
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="BotaoConsultar" type="submit" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')">
							<input name="BotaoVoltar" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
						</div>
						<!-- FIM DIV BOTÕES -->
						
					</fieldset>
			   		<!-- FIM FIELDSET PRAZO PARA AGENDAMENTO DA AUDIÊNCIA -->
					<br />
			    	
			    	<!-- DIV TABELA -->
			   		<div id="divTabela" class="divTabela" > 
				    	<!-- TABELA -->
				    	<table id="Tabela" class="Tabela">
			    		
				    		<!-- thead -->
				        	<thead>
				        		<tr class="TituloColuna1">
									<td colspan="4">Agendas Livres - Serventia: <%=Audienciadt.getServentia()%></td>
								</tr>
				            	<tr class="TituloColuna">
				                  	<td>Data da Audiência</td>
				                  	<td>Responsável</td>
				                  	<td>Reservada?</td>
				                  	<td>Agendar Audiência</td>
				               	</tr>
				           	</thead>
				           	<!-- Fim thead -->
				           	
				           	<!-- tbody -->
				           	<tbody id="tabListaAudiencia">
								<%List listaAudiencias = (List)request.getAttribute("ListaAudiencias");
				 				AudienciaDt audienciaDt;
				  				String stTempNome="";
				  				if (listaAudiencias != null){
				  					for(int i = 0 ; i < listaAudiencias.size();i++) {
				  	  					audienciaDt = (AudienciaDt)listaAudiencias.get(i); %>
				  						<%if (stTempNome.equalsIgnoreCase("")) { 
				  						    stTempNome="a";%> 
				  	                   		<tr class="Linha1" align="center"> 
				  						<%}else{ 
				  						    stTempNome=""; %>    
				  	                   		<tr class="Linha2" align="center">
				  						<%}%>
				  	                			<!-- Data Agendada -->
				  	                    		<td><%=audienciaDt.getDataAgendada()%></td>
				  	                    		<!-- Responsável -->
				  	                    		<td><%=audienciaDt.getAudienciaProcessoDt().getServentiaCargo()%></td>
				  	                    		<!-- Reservada -->
				  	                    		<td>
				  	                    			<%if (audienciaDt.getReservada().equalsIgnoreCase("true")){%>Sim    
					  	                      		<%}else{%> Não <%}%>
				  	                    		</td>
				  	                    		<td>
				  	                   				<input id="imgSalvar" class="imgSalvar" name="imaSalvar" type="image"  src="./imagens/22x22/btn_agendar.png" title="Agendar Audiência - Executa o agendamento da audiência selecionada" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>');AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>')">
				  	                   			</td>
				  	               			</tr>
				  						<%}    
				  					}else{
				  						if (stTempNome.equalsIgnoreCase("")) { 
				  						    stTempNome="a";%> 
				  	                   		<tr class="Linha1"> 
				  						<%}else{ 
				  							stTempNome=""; %>    
				  	                		<tr class="Linha2">
				  						<%}%>
				  								<td>-</td>
				  								<td>-</td>
				  								<td>-</td>
				  								<td>-</td>
				  							</tr>
				  					<%}%>
						    	</tbody>
				           		<!-- Fim tbody -->
				        	</table>
			       			<!-- FIM TABLE -->
			       		<div />
			       		<!-- FIM TABLE -->
			   		</div>
			   		<!-- FIM DIV TABLE --> 
				</div>
				<!-- FIM DIV EDITAR -->
			</form>
			<!-- FIM FORM -->
					
		</div>
		<!-- FIM DIVCORPO -->
		
		<!-- PAGINAÇÃO -->
		<%if (listaAudiencias != null){%>
	    	<%@ include file="Padroes/PaginacaoSubmit.jspf"%> 			
		<%}%>
		<!-- FIM PAGINAÇÃO -->
		
		<!-- MENSAGEM --> 
		<%@ include file="Padroes/Mensagens.jspf" %>
		<!-- FIM MENSAGEM -->
		
	</body>
	<!-- FIM BODY -->
</html>
<!-- FIM HTML --> 
