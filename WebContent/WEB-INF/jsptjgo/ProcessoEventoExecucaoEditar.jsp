<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CrimeExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>

<jsp:useBean id="ProcessoEventoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen'></link>
		<script type='text/javascript' src='./js/Funcoes.js'></script>	
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				mostrar();
				$("#DataInicio").mask("99/99/9999");
				$("#DataDistribuicao").mask("99/99/9999");
				$("#DataPronuncia").mask("99/99/9999");
				$("#DataDenuncia").mask("99/99/9999");
				$("#DataAcordao").mask("99/99/9999");
				$("#DataSentenca").mask("99/99/9999");
				$("#DataAdmonitoria").mask("99/99/9999");
				$("#DataTransito").mask("99/99/9999");
				$("#DataTJ_MP").mask("99/99/9999");
				$("#DataInicioCumprimentoPena").mask("99/99/9999");
				$("#DataFato").mask("99/99/9999");
				$("#DataFato_0").mask("99/99/9999");
				$("#DataFato_1").mask("99/99/9999");
				$("#DataFato_2").mask("99/99/9999");
				$("#DataFato_3").mask("99/99/9999");
				$("#DataFato_4").mask("99/99/9999");
				$("#DataFato_5").mask("99/99/9999");
				$("#DataInicioSursis").mask("99/99/9999");
				$("#VisualizaDadosTJ").show();
			});

			function localizarEvento(PaginaAtual, PassoEditar){
				var form =	document.getElementById('Formulario');
				if (form.Id_EventoExecucao.value == "<%=String.valueOf(EventoExecucaoDt.COMUTACAO_PENA)%>" && form.Id_ProcessoEventoExecucao.value != ""){
					mostrarMensagemOk("Cadastro Processo Evento Execucao", "Não é possível alterar este evento");
					return false;	
				} else{
					if (PaginaAtual != '') AlterarValue('PaginaAtual', PaginaAtual);
					if (PassoEditar != '') AlterarValue('PassoEditar', PassoEditar); 
					return true;
				} 
			}

			function mostrar(){
				if ($("input[name='radioConsiderarLivramento']:checked").val() == '0') {
					$("#divPrisao").show();
				} else {
					$("#divPrisao").hide();
				}

				if ($("input[name='radioConsiderarLivramento']:checked").val() == '0' || $("input[name='radioConsiderarLivramento']:checked").val() == '2') {
					$("#VisualizaDadosTJ").show();
				} else {
					$("#VisualizaDadosTJ").hide();
				}
				calcularTamanhoIframe();
			}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; |<%=request.getAttribute("TituloPagina")%>| Cadastrar Evento </h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
						<table>
							<tr><td><div> Processo</div><span><a href="<%=request.getAttribute("tempRetornoProcesso")%>?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span><br>
									<div> Serventia</div><span> <%=processoDt.getServentia()%> </span><br /><br />
									<div> Sentenciado</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNome()%></span><br>
									<div> Nome da Mãe</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNomeMae()%></span><br>
									<div> Data de Nascimento</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getDataNascimento()%></span>
								</td>
							</tr>
						</table>
					</fieldset><br /><br />
					<fieldset class="formEdicao"> <legend class="formEdicaoLegenda">Evento</legend>
			    	    <br />
						<input class="formEdicaoInputSomenteLeitura" name="Id_Movimentacao" id="Id_Movimentacao" type="hidden" value="<%=ProcessoEventoExecucaodt.getId_Movimentacao()%>">
						<input class="formEdicaoInputSomenteLeitura" name="Id_ProcessoEventoExecucao" id="Id_ProcessoEventoExecucao" type="hidden" value="<%=ProcessoEventoExecucaodt.getId()%>">
						<label class="formEdicaoLabel" for="MovimentacaoExecucao">*Movimentação</label><br>
			    	    <input class="formEdicaoInputSomenteLeitura" readonly name="MovimentacaoDataRealizacaoTipo" id="MovimentacaoDataRealizacaoTipo" type="text" size="60" maxlength="60" value="<%=ProcessoEventoExecucaodt.getMovimentacaoDataRealizacaoTipo()%>">
						<br /> <br />  
						<label class="formEdicaoLabel" for="EventoExecucao">*Evento

			    	    <input class="FormEdicaoimgLocalizar" id="imaLocalizarEventoExecucao" name="imaLocalizarEventoExecucao" type="image" src="./imagens/imgLocalizarPequena.png"
 							onclick="return localizarEvento('<%=String.valueOf(EventoExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '-1')" >
						</label><br>
						<input class="formEdicaoInputSomenteLeitura" readonly name="EventoExecucao" id="EventoExecucao" type="text" size="60" maxlength="60" value="<%=ProcessoEventoExecucaodt.getEventoExecucaoDt().getEventoExecucao().toUpperCase()%>">
						<input type="hidden" name="Id_EventoExecucao" id="Id_EventoExecucao"  value="<%=ProcessoEventoExecucaodt.getEventoExecucaoDt().getId()%>"/>
						<br /> <br />
						 
						<label class="formEdicaoLabel" for="DataInicio"><b>Observação: <%=ProcessoEventoExecucaodt.getEventoExecucaoDt().getObservacao()%></b></label><br>
						<br /> <br />

						<label class="formEdicaoLabel" for="DataInicio">*Data Início</label><br>
						<%if (ProcessoEventoExecucaodt.isManterAcaoPenal()){ %> 
						<input name="DataInicio" id="DataInicio" type="text" size="10" maxlength="10" value="<%=ProcessoEventoExecucaodt.getDataInicio()%>" 
								class="formEdicaoInputSomenteLeitura" readonly/>(A Data Início deste evento pode ser alterada somente no Processo de Ação Penal)   
						<%}	else {%>
						<input name="DataInicio" id="DataInicio" type="text" size="10" maxlength="10" value="<%=ProcessoEventoExecucaodt.getDataInicio()%>" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/>
								<img class="formLocalizarLabel" style="margin-right:20px; height:13px; min-width:3px; padding-right: 0px" id="calendarioDataDenuncia" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicio,'dd/mm/yyyy',this)"/>
						<%} %>
						<br />  			    
						<%if (request.getAttribute("InformarQuantidade") != null && !request.getAttribute("InformarQuantidade").toString().equals("")){ %>
						<label class="formEdicaoLabel" for="Quantidade">*Quantidade em <%=request.getAttribute("InformarQuantidade") %></label><br>
						<input name="Quantidade" id="Quantidade" type="text" size="10" maxlength="10" value="<%=ProcessoEventoExecucaodt.getQuantidade()%>" class="formEdicaoInput" />
						<br />
						<%} %>
						<%if (request.getAttribute("InformarObs") == null || request.getAttribute("InformarObs").toString().equals("") || request.getAttribute("InformarObs").toString().equals("true")){ %>
						<label class="formEdicaoLabel" for="Observacao">Observação</label><br>
						<input class="formEdicaoInput" name="Observacao" id="Observacao" type="text" style="width:400px" maxlength="200" value="<%=ProcessoEventoExecucaodt.getObservacao()%>"/>
						<br />
						<%} %>
						<%if (request.getAttribute("ObservacaoAuxiliar") != null && !request.getAttribute("ObservacaoAuxiliar").toString().equals("")){ %>
						<label class="formEdicaoLabel" for="ObservacaoAux"><%=request.getAttribute("ObservacaoAuxiliar")%></label><br>
						<input class="formEdicaoInput" name="ObservacaoAux" id="ObservacaoAux" type="text" style="width:394px" value="<%=ProcessoEventoExecucaodt.getObservacaoAux()%>"/>
						<br />
						<%} %>

						<% if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getAlteraRegime().equalsIgnoreCase("S")){ %>
						<input id="RegimeExecucao" name="RegimeExecucao" type="hidden" />
						<label class="formEdicaoLabel" for="RegimeExecucao">*Regime</label><br>  
						<select name="Id_RegimeExecucao" id="Id_RegimeExecucao" onchange="selecionaRegimePPL();">
					    	<option value=""></option>
							<%List listaRegime_PPL = (List) request.getSession().getAttribute("ListaRegime_PPL");
							if (listaRegime_PPL != null){%> 
								<%for (int i=0; i<listaRegime_PPL.size(); i++) {%>					
				    		<option value="<%=((RegimeExecucaoDt)listaRegime_PPL.get(i)).getId()%>" <%if (ProcessoEventoExecucaodt.getEventoRegimeDt().getId_RegimeExecucao().equals(((RegimeExecucaoDt)listaRegime_PPL.get(i)).getId())){%>selected<%}%>><%=((RegimeExecucaoDt)listaRegime_PPL.get(i)).getRegimeExecucao()%></option>
								<%} %>
							<%}%>
				  		</select>
						<br />
						<%} %>

						<% if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getAlteraLocal().equalsIgnoreCase("S")){ %>
						<input id="LocalCumprimentoPena" name="LocalCumprimentoPena" type="hidden" />
						<label class="formEdicaoLabel" for="LocalCumprimentoPena">*Local de Cumprimento de Pena</label><br>
			    	    <select name="Id_LocalCumprimentoPena" id="Id_LocalCumprimentoPena">
					    	<option value=""></option>
							<%List listaLocal = (List) request.getSession().getAttribute("ListaLocal");
							if (listaLocal != null){%> 
								<%for (int i=0; i<listaLocal.size(); i++) {%>					
				    		<option value="<%=((LocalCumprimentoPenaDt)listaLocal.get(i)).getId()%>" <%if (ProcessoEventoExecucaodt.getEventoLocalDt().getId_LocalCumprimentoPena().equals(((LocalCumprimentoPenaDt)listaLocal.get(i)).getId())){%>selected<%}%>><%=((LocalCumprimentoPenaDt)listaLocal.get(i)).getLocalCumprimentoPena()%></option>
								<%} %>
							<%}%>
				  		</select>
						<br />
						<%} %>  

						<%if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
								|| ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) {%>
						<label class="formEdicaoLabel" for="LocalCumprimentoPena">Data Início do Livramento Condicional</label><br>
						<input type="text" name="informacaoLivramentoCondicional" id="informacaoLivramentoCondicional"  class="formEdicaoInputSomenteLeitura" readonly value="<%=ProcessoEventoExecucaodt.getDataInicioLivramentoCondicional()%>" size="10"/><br></br>
						<label class="formEdicaoLabel" for="GuiaRecolhimento">*Tempo do Livramento Condicional - Considerar?</label><br><br />
						<input id="radioConsiderarLivramento" name="radioConsiderarLivramento" type="radio" value="0" onchange="mostrar();" <%if ((ProcessoEventoExecucaodt.getConsiderarTempoLivramentoCondicional().length()==0) || (ProcessoEventoExecucaodt.getConsiderarTempoLivramentoCondicional().toString().equals("0"))) {%> checked<%} %>>Não (Fato novo na vigência do LC)<br />
						<input id="radioConsiderarLivramento" name="radioConsiderarLivramento" type="radio" value="1" onchange="mostrar();" <%if (ProcessoEventoExecucaodt.getConsiderarTempoLivramentoCondicional().toString().equals("1")) {%> checked<%} %>>Sim (Crime anterior ao LC ou Revogação em grau de recurso do MP)<br />						
						<input id="radioConsiderarLivramento" name="radioConsiderarLivramento" type="radio" value="2" onchange="mostrar();" <%if ((ProcessoEventoExecucaodt.getConsiderarTempoLivramentoCondicional().length()==0) || (ProcessoEventoExecucaodt.getConsiderarTempoLivramentoCondicional().toString().equals("2"))) {%> checked<%} %>/>Sim (Descumprimento das condições do LC)<br />
						<input type="hidden" name="Id_LivramentoCondicional" id="Id_LivramentoCondicional"  value="<%=ProcessoEventoExecucaodt.getId_LivramentoCondicional()%>"/>
						<input type="hidden" name="DataInicioLivramentoCondicional" id="DataInicioLivramentoCondicional"  value="<%=ProcessoEventoExecucaodt.getDataInicioLivramentoCondicional()%>"/>
						<br /> 

						<div id="divPrisao">
						<%		List listaPrisao = (List)request.getAttribute("listaPrisao");
								if (listaPrisao != null && listaPrisao.size() > 0){ %>
						<label class="formEdicaoLabel" for="prisao">*Data da prisão a ser considerada após a Concessão do Livramento.</label><br><br />
								<% for (int h=0; h<listaPrisao.size(); h++){
									ProcessoEventoExecucaoDt prisao = (ProcessoEventoExecucaoDt)listaPrisao.get(h);%>
						<input id="radioPrisao" name="radioPrisao" type="radio" value="<%=prisao.getDataInicio()%>"  <%if (ProcessoEventoExecucaodt.getDataPrisaoRevogacaoLC().equals(prisao.getDataInicio())) {%> checked<%} %>/><%=prisao.getEventoExecucao() + " - " + prisao.getDataInicio()%><br />
								<%} %>										
							<%	}%>
						<br />
						</div>
						<%} %> 

						<br />
						<%if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))
								|| ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
								|| ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) {%>
						<%
					   			 List listaTJ = ProcessoEventoExecucaodt.getListaTJ();
					   	    	if (listaTJ != null && listaTJ.size() > 0){ 
					   	    		String fracao = "";
					   	    		for (int j=0; j<listaTJ.size(); j++){
					   	    			if (((HashMap)listaTJ.get(j)).get("Fracao") != null && ((HashMap)listaTJ.get(j)).get("Fracao").toString().length() > 0){
						   	    			fracao = ((HashMap)listaTJ.get(j)).get("Fracao").toString();
						   	    		}	
					   	    		}
					   	    		%>
						<% if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){ %>
						<br /><label class="formEdicaoLabel">*Fração da Comutação</label><br>&nbsp;
						<input id="radioFracaoComutacao" name="radioFracaoComutacao" type="radio" value="1/5"
							<%if (fracao.equals("1/5")){ %> checked <%} %> />1/5 &nbsp;
						<input id="radioFracaoComutacao" name="radioFracaoComutacao" type="radio" value="1/4" 
							<%if (fracao.equals("1/4")){ %> checked <%} %> />1/4 &nbsp;
						<input id="radioFracaoComutacao" name="radioFracaoComutacao" type="radio" value="1/3"
							<%if (fracao.equals("1/3")){ %> checked <%} %> />1/3 &nbsp;
						<input id="radioFracaoComutacao" name="radioFracaoComutacao" type="radio" value="1/2"
							<%if (fracao.equals("1/2")){ %> checked <%} %> />1/2 &nbsp;
						<input id="radioFracaoComutacao" name="radioFracaoComutacao" type="radio" value="2/3"
							<%if (fracao.equals("2/3")){ %> checked <%} %> />2/3
						<br /><br />
						<%} %>

						<fieldset id="VisualizaDadosTJ" class="VisualizaDados" style="width: 530px;margin-left: 60px;"><legend>Trânsito(s) em Julgado relacionado(s)</legend>						
				   		   	<table width="98%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
				   				<thead align="left">
				   					<tr>
										<th colspan="4"></th>
										<th align="center" colspan="3">Tempo de Condenação (a-m-d)</th>
										<th align="center"></th>
				   					</tr>
						   			<tr>
										<th></th>
				   						<th align="center">Nº</th>
			                   			<th align="center">Data TJ</th>
			                   			<th align="center">DICC</th>
										<th align="center">Não extinto</th>
										<th align="center">Extinto</th>
										<th align="center">Total</th>
										<th></th>
									</tr>
				   				</thead>
					   			<tbody>
							<%
				   	    		for (int i=0; i<listaTJ.size(); i++){
				   	    			HashMap map = (HashMap)listaTJ.get(i);
							%>
									<tr>
										<td><input type="hidden" value="<%=map.get("Id_ProcessoExecucao")%>"></td>
				       					<td align="center"><%=i+1%></td>
			                    		<td width="19%" align="center"><%=map.get("DataTransitoJulgado")%></td>
			                    		<td width="19%" align="center"><%=map.get("DataInicioCumprimentoPena")%></td>
										<td width="19%" align="center"><%=map.get("TempoNaoExtintoAnos")%></td>
										<td width="19%" align="center"><%=map.get("TempoExtintoAnos")%></td>
										<td width="19%" align="center"><%=map.get("TempoTotalAnos")%></td>
										<td width="5%" align="center"><input id="chkTJ[]" name="chkTJ[]" type="checkbox" value="<%=i%>"
												<%if (map.get("Checked").equals("1")){%> checked <%} %>
											/></td>
				       	 			</tr>
				       		<%	} %>
				       	 		</tbody>
				       		</table><br>
						</fieldset>
						<br />

						<% if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){ %>
						<br /><label class="formEdicaoLabel">*Data do Decreto</label><br>&nbsp;
								<select name="DataDecretoSelecionado" id="DataDecretoSelecionado">
								<option value="" selected></option>
							<%	List listaParametroComutacao = (List) request.getSession().getAttribute("listaParametroComutacao");
								for (int i=listaParametroComutacao.size()-1; i>=0; i--){ %>

								<option value="<%=((ParametroComutacaoExecucaoDt)listaParametroComutacao.get(i)).getDataDecreto()%>"
									<% if (ProcessoEventoExecucaodt.getDataDecretoComutacao().equals(((ParametroComutacaoExecucaoDt)listaParametroComutacao.get(i)).getDataDecreto())){ %> 
									selected="selected"
									<%} %>
									onclick="javascript: calcular('<%=processoDt.getId()%>','<%=((ParametroComutacaoExecucaoDt)listaParametroComutacao.get(i)).getDataDecreto()%>', 'PenaRemanescenteDias', 'PenaRemanescenteAnos', 'RestantePenaDias', 'RestantePenaAnos', 'TempoCumpridoDias', 'TempoCumpridoAnos', 'PenaRemanescenteDiasNaoHediondo', 'PenaRemanescenteAnosNaoHediondo');">
									<%=((ParametroComutacaoExecucaoDt)listaParametroComutacao.get(i)).getDataDecreto()%> 
								</option>
							<%}%>
								</select>
						<br /><br />
						<%} %>
			   		<% }  %> 

					<%if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){ %>
						<br />
						<div><a href="javascript: MostrarOcultar('divComutacao')" title="Mostrar/Ocultar Demosntrativo do Càlculo do Tempo a Comutar"> Demonstrativo do Cálculo do Tempo a Comutar</a></div>
						<br />						
 						<div id="divComutacao" style="display: none">
						<%@ include file="CalculoDemonstrativoComutacao.jspf" %>
						</div>
					<%} %>

						<%} %> 

						<%if (ProcessoEventoExecucaodt.isManterAcaoPenal() && ProcessoEventoExecucaodt.getIdEventoPai().length() == 0){ %>
						<br /><br />   
						<%@ include file="ProcessoEventoExecucao_DadosAcaoPenal.jspf" %>
						<%} %>

						<%if (ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO)) ||
							ProcessoEventoExecucaodt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.ALTERACAO_MODALIDADE))){ %>
						<br /><br />	
						<%@ include file="ProcessoExecucaoDadosSentencaCondenacaoModalidade.jspf"%>					
						<%} %>

						<br />
					    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>')">
					    </div>

						<br /><label style="text-align: right">Data da inclusão/alteração: <%=Funcoes.verificarCampo(ProcessoEventoExecucaodt.getDataAlteracao(),"","-")%></label><br>&nbsp;
						<br /><label style="text-align: right">Usuário: <%=Funcoes.verificarCampo(ProcessoEventoExecucaodt.getUsuarioAlteracao(),"","-")%></label><br>&nbsp;
					</fieldset>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>