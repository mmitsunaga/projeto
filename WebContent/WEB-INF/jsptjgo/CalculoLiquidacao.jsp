<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="ProcessoEventoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
	<head>
		<title>Cálculo de Liquidação de Pena</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
		<script language="javascript">

			$(document).ready(function() {
				$("#dataBasePR").mask("99/99/9999");
			});
		
			function marcarDesmarcarTudo(nomeElemento){
				var form = document.Formulario;
				for(i=1;i<form.elements.length;i++){
					if(form.elements[i].name == nomeElemento){
						if(form.elements[i].checked == false){
							form.elements[i].checked = true;
						}else{
							form.elements[i].checked = false;
						}
					}
				}
			}

			function mostrar(){
				if($("#chkComutacao").is(":checked") || $("#chkComutacaoUnificada").is(":checked")){
					$("#divComutacao").show();
					if($("#chkComutacaoUnificada").is(":checked")){
						$("#divComutacaoUnificada").show();
					} else $("#divComutacaoUnificada").hide();
				} else $("#divComutacao").hide();
				calcularTamanhoIframe();
			}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; |<%=request.getAttribute("TituloPagina")%>| Cálculo de Liquidação de Penas</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>">
  				
				<input id="Id_ProcessoEventoExecucao" name="Id_ProcessoEventoExecucao" type="hidden" value="<%=request.getAttribute("Id_ProcessoEventoExecucao")%>" />
		
				<div id="divEditar" class="divEditar">

					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
						<table>
							<tr><td><div> Processo</div><span><a href="<%=request.getAttribute("tempRetornoProcesso")%>?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span><br>
									<div> Serventia</div><span> <%=processoDt.getServentia()%> </span><br /><br />
									<div> Sentenciado</div><span> <%=processoDt.getPrimeiroPoloPassivoNome()%></span><br>
									<div> Nome da Mãe</div><span> <%=processoDt.getPrimeiroPoloPassivoNomeMae()%></span><br>
									<div> Data de Nascimento</div><span> <%=processoDt.getPrimeiroPoloPassivoDataNascimento()%></span>
								</td>
							</tr>
						</table>
					</fieldset><br /><br />

					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Opções de Cálculo
							<img src="./imagens/imgAjuda.png" title="Observação sobre os cálculos." 
								onclick="window.open('ProcessoEventoExecucao?PaginaAtual=<%=Configuracao.Curinga6%>&amp;PassoEditar=2','_blank','width=1000, height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=no')"></img>
						</legend><br>
						<input id="chkComutacao" name="chkTipoCalculo[]" type="checkbox" value="COMUTACAO" onclick="mostrar();"/>Comutação Prévia - Pena Individual<br />
						<input id="chkComutacaoUnificada" name="chkTipoCalculo[]" type="checkbox" value="COMUTACAO_UNIFICADA" onclick="mostrar();"/><b>Comutação Prévia - Pena Unificada ou Somada  (art. 111 LEP)</b><br />
						<input id="chkIndulto" name="chkTipoCalculo[]" type="checkbox" value="INDULTO" onclick="MostrarOcultar('divIndulto');"/>Indulto <i> (observar o art. 1º e incisos do Decreto Presidencial para escolha da fração)</i><br />
						<input id="chkPrescricao_Executoria_Individual" name="chkTipoCalculo[]" type="checkbox" value="PRESCRICAO_EXECUTORIA_IND" onclick="MostrarOcultar('divPrescricaoExecInd');"/>Prescrição Executória - Individual <i>(art.119 do CP e/ou utilizar quando não houver início de cumprimento de pena)</i><br />
						<input id="chkPrescricao_Executoria_Unificada" name="chkTipoCalculo[]" type="checkbox" value="PRESCRICAO_EXECUTORIA_UNI"/><b>Prescrição Executória - Pena Unificada ou Somada  (art. 111 LEP)</b><br />
						<input id="chkProgressao" name="chkTipoCalculo[]" type="checkbox" value="PROGRESSAO" onclick="MostrarOcultar('divProgressao');"/><b>Progressão de Regime e Livramento Condicional</b><br />
						<input id="chkPenaRestritiva" name="chkTipoCalculo[]" type="checkbox" value="PENARESTRITIVA"/><b>Pena Restritiva de Direito (modalidades)</b><br />
						<input id="chkSursis" name="chkTipoCalculo[]" type="checkbox" value="SURSIS"/><b>SURSIS</b><br />
						<input id="chkOutraPenaRestritiva" name="chkTipoCalculo[]" type="checkbox" value="OUTRAPENA"/>Outros<br />

						<br /><br />
						<div><a href="javascript: MostrarOcultar('divRemicao')" title="Mostrar/Ocultar opções de remição">Opções do Cálculo
								<img src="./imagens/imgAjuda.png" title="Observação sobre as opções do cálculo." 
								onclick="window.open('ProcessoEventoExecucao?PaginaAtual=<%=Configuracao.Curinga6%>&amp;PassoEditar=3','_blank','width=1000, height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=no')"></img>
							</a></div>
						<div id="divRemicao" style="display:none">
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Remição</legend>
								<label class="formEdicaoLabel" for="DataBase">Consideração do tempo de Remição no cálculo: </label><br><br />
								<input id="radioRemicao" name="radioRemicao" type="radio" value="1" checked/> Deduzir o tempo de Remição após a data base, direto no Requisito Temporal<br />
								<input id="radioRemicao" name="radioRemicao" type="radio" value="2" /> Considerar todo o tempo de Remição (antes e depois da data base) como Tempo Cumprido até a Data Base.
							</fieldset>
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Geral</legend>
								<label class="formEdicaoLabel" for="DataBase">Visualizar, no relatório, o término da pena unificada para as penas maiores que 30 anos: </label><br><br />
								<input id="radioTerminoPena" name="radioTerminoPena" type="radio" value="S" /> Sim
								<input id="radioTerminoPena" name="radioTerminoPena" type="radio" value="N" checked/> Não
								<br /><br />
								<label class="formEdicaoLabel" for="DataBase">Visualizar, no relatório, o restante da pena até último evento (para o cálculo de Pena Restritiva de Direito): </label><br><br />
								<input id="radioRestantePena" name="radioRestantePena" type="radio" value="S" /> Sim
								<input id="radioRestantePena" name="radioRestantePena" type="radio" value="N" checked/> Não
							</fieldset>
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Prescrição</legend>
								<input id="chkPrescricao_Punitiva" name="chkTipoCalculo[]" type="checkbox" value="PRESCRICAO_PUNITIVA"/>Prescrição Retroativa/Intercorrente - Individual&nbsp;&nbsp;&nbsp;<i> (Falta desenvolver o art. 366 do CPP e data de pronúncia)</i></label><br><br />
							</fieldset>
						</div>
						<div id="divComutacao" style="display:none">
							<br />
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Comutação Prévia</legend>
								<label class="formEdicaoLabel" for="DataBase">Selecione o Decreto para o Cálculo da Data Provável da Comutacao Prévia: </label><br><br />
								<select name="radioDecreto">
								<% List lista = (List) request.getSession().getAttribute("listaParametroComutacao");
									if (lista != null && lista.size() > 0){
										for (int i=lista.size()-1; i>=0; i--){%>
								<option value="<%=((ParametroComutacaoExecucaoDt)lista.get(i)).getId()%>"><%=((ParametroComutacaoExecucaoDt)lista.get(i)).getDataDecreto() %></option>
									<%  }
									}
									%>
								</select>
								<br /><br />
								<input id="chkMulherComMenoresComutacao" name="chkMulherComMenoresComutacao" type="checkbox" value="S" />Mulher responsável por menor, deficiente, incapaz ou portador de doença crônica (Válido apenas a partir de 2017)<br />
								<br />
								<div id="divComutacaoUnificada" style="display:none">
								<input id="chkSaldoDevedorHedindoComutacao" name="chkSaldoDevedorHedindoComutacao" type="checkbox" value="S" checked="checked"/>Somar a fração do crime hediondo no saldo devedor<br />
								</div>
							</fieldset>
						</div>

						<div id="divIndulto" style="display:none">
							<br />
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Indulto</legend>
								<label class="formEdicaoLabel" for="DataBase">Selecione a fração do tempo total da condenação para o cálculo da data provável do indulto: </label><br><br />
								<table>
									<tr>
										<td valign="top">
											<label class="formEdicaoLabel" style="text-align: left">Crime comum</label><br><br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="1/2"/>1/2<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="1/3"/>1/3<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="1/4"/>1/4<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="1/5"/>1/5<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="1/6"/>1/6<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="2/5"/>2/5<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="3/5"/>3/5<br />
											<input id="radioFracaoIndultoComum" name="radioFracaoIndultoComum" type="radio" value="2/3"/>2/3<br />
										</td>
										<td valign="top">
											<label class="formEdicaoLabel" style="text-align: left">Crime hediondo</label><br><br />
											<input id="radioFracaoIndultoHediondo" name="radioFracaoIndultoHediondo" type="radio" value="1/1"/>1/1<br />
											<input id="radioFracaoIndultoHediondo" name="radioFracaoIndultoHediondo" type="radio" value="2/3"/>2/3<br />
										</td>
										
									</tr>
								</table>
							</fieldset>
						</div>

						<div id="divPrescricaoExecInd" style="display:none">
							<br />
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Precrição Executória Individual</legend>
								<input id="chkIniciou" name="chkIniciou" type="checkbox" value="N" onclick="MostrarOcultar('detracao')"/>Não iniciou o cumprimento da pena
								<div id="detracao" style="display: none">
									<input id="chkDetracao" name="chkDetracao" type="checkbox" value="N" checked="checked"/>Não considerar detração (conforme RECURSO ESPECIAL Nº 1.095.225 - SP (2008/0209631-1) do STJ)<br />	
								</div>
							</fieldset>
						</div>

						<div id="divProgressao" style="display:none">
							<br />
							<fieldset class="formEdicao"  ><legend class="formEdicaoLegenda">Progressão de Regime e Livramento Condicional
									<img src="./imagens/imgAjuda.png" title="Observação sobre Progressão de Regime e Livramento Condicional." 
									onclick="window.open('ProcessoEventoExecucao?PaginaAtual=<%=Configuracao.Curinga6%>&amp;PassoEditar=4','_blank','width=1000, height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=no')"></img>
								</legend>
								<label class="formEdicaoLabel" for="DataBase">Selecione a data base para o cálculo de progressão de regime: </label><br><br />
				    			<table id="TabelaEventos" class="Tabela" width="80%">
				        			<thead>
				            			<tr class="TituloColuna">
				                   			<th width="2%">Opção</th>
				                   			<th width="60%">Evento</th>
				                  			<th width="10%">Data Início</th>
				               			</tr>
				           			</thead>
				    				<tbody id="tabListaProcesso">
						    		<%
						   				List listaEventosDataBase = (List) request.getSession().getAttribute("listaEventosDataBase");
						    		 	boolean boLinha=false; 						    		 	
								   	    if (listaEventosDataBase != null){
						   	    			for (int i=0; i<listaEventosDataBase.size(); i++){
							   			  		ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt)listaEventosDataBase.get(i);
									%>			
										<tr>
				                   			<td width="2%"  align="center"> <input id="radioDataBaseProgressao" name="radioDataBaseProgressao" type="radio" value="<%=processoEventoExecucaoDt.getDataInicio()%>"></td>
				                    		<td><%=processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao()%></td>
					                   		<td align="center"><%=processoEventoExecucaoDt.getDataInicio()%></td>
										</tr>
									<%		
											boLinha = !boLinha;
											}
								   	    }
									%>
				           			</tbody>
						    	</table>
								<br />
								<div><a href="javascript: MostrarOcultar('opcaoLC')" title="Mostrar/Ocultar opções da PR e LC">Opções da PR/LC
										<img src="./imagens/imgAjuda.png" title="Observação sobre Progressão de Regime e Livramento Condicional." 
										onclick="window.open('ProcessoEventoExecucao?PaginaAtual=<%=Configuracao.Curinga6%>&amp;PassoEditar=5','_blank','width=1000, height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=no')"></img>
									</a></div><br />
								<div id="opcaoLC" style="display: none">
									<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Livramento Condicional</legend>
										<input id="chkReincidenteEspecifico" name="chkReincidenteEspecifico" type="checkbox" value="RE"/>&nbsp;Desejo considerar a REINCIDÊNCIA ESPECÍFICA para todos os crimes hediondos existentes!<br />
										<input id="chkDataBaseLC" name="chkDataBaseLC" type="checkbox" value="DataBaseLc" onclick="javascript: MostrarOcultar('dataBaseLC')"/>&nbsp;Desejo selecionar a data base (utilizar somente com determinação judicial).<br />
										<div id="dataBaseLC" style="display: none">


							    			<table id="TabelaEventos" class="Tabela" width="80%">
							        			<thead>
							            			<tr class="TituloColuna">
							                   			<th width="2%">Opção</th>
							                   			<th width="60%">Evento</th>
							                  			<th width="10%">Data Início</th>
							               			</tr>
							           			</thead>
							    				<tbody id="tabListaProcesso">
									    		<%
									   				List listaEventosDataBaseLC = (List) request.getSession().getAttribute("listaEventosDataBase");
									    		 	boolean boLinha1=false; 						    		 	
											   	    if (listaEventosDataBaseLC != null){
									   	    			for (int i=0; i<listaEventosDataBaseLC.size(); i++){
										   			  		ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt)listaEventosDataBaseLC.get(i);
												%>			
													<tr>
							                   			<td width="2%"  align="center"> <input id="radioDataBaseLC" name="radioDataBaseLC" type="radio" value="<%=processoEventoExecucaoDt.getDataInicio()%>"></td>
							                    		<td><%=processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao()%></td>
								                   		<td align="center"><%=processoEventoExecucaoDt.getDataInicio()%></td>
													</tr>
												<%		
														boLinha1 = !boLinha1;
														}
											   	    }
												%>
							           			</tbody>
									    	</table>
										</div>
									</fieldset><br />
									<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Progressão de Regime</legend>
										<input id="chkReincidenteHediondoPR" name="chkReincidenteHediondoPR" type="checkbox" value="RE_PR"/>&nbsp;Desejo considerar a REINCIDÊNCIA para todos os crimes hediondos existentes!<br />
										<br />
										<label class="formEdicaoLabel" for="dataBaseProgressao">* Desejo informar a data base para o cálculo da PR: </label><br>
										<input id="dataBasePR" name="dataBasePR" type="text" value="" size="10" maxlength="10"/><br />
									</fieldset><br />
									<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Progressão de Regime e Livramento Condicional</legend>
										<label class="formEdicaoLabel" style="text-align: left">Calcular somente:</label><br>
											<input id="radioSomente" name="radioSomente" type="radio" value="PR"/>Progressão de Regime &nbsp;
											<input id="radioSomente" name="radioSomente" type="radio" value="LC"/>Livramento Condicional &nbsp;
											<input id="radioSomente" name="radioSomente" type="radio" value="TODOS" checked="checked"/>Todos &nbsp;
											<br />
										<input id="chkCalculoLivramento" name="chkCalculoLivramento" type="checkbox" value="CALC_LC" onclick="MostrarOcultar('divCalculoLC');"/>
												Desejo realizar o cálculo do LC e PR, ainda que o regime atual do sentenciado seja: Aberto ou Livramento Condicional!<br /><br />
										<div id="divCalculoLC" style="display:none">
											<label class="formEdicaoLabel" for="DescricaoRegime">Informe a projeção do regime para o cálculo de PR: </label><br>
											<input class="formEdicaoInput" name="DescricaoRegime" id="DescricaoRegime" type="text" size="60" maxlength="60" value=""/>
											<br />
										</div>
									</fieldset>
								</div>
							</fieldset>
							<br />
						</div>
						<br />

					</fieldset> 
					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgCalcular" type="submit" value="Calcular" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');"> 
				    </div>					     	
					<br />
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>