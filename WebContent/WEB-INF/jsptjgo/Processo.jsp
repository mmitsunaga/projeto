<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoFaseDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<html>
	<head>
		<title>Processo</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>		
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script> 
   		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>  
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo" >
			<div class="area">
				<h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Modificar Dados de Processo</h2>
			</div>
			
			<form action="Processo" method="post" name="Formulario" id="Formulario">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
				<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Modificar Dados do Processo</legend>
					    
					    <label class="formEdicaoLabel"> Processo </label><br>
					    <span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a> </span>
						<br /><br />
						
					    <div class="col90 clear">
							<b> Área:</b>						
						    <%String processoArea = request.getParameter("ProcessoArea");
						    if (processoArea == null || processoArea.trim().equalsIgnoreCase("")){
						    	processoArea = processoDt.getAreaCodigo();
						    }%>
				    		<input type="radio" name="ProcessoArea" value="1" <%if(processoArea != null && processoArea.equalsIgnoreCase("1")){%> checked<%}%>> Cível
				    		<input type="radio" name="ProcessoArea" value="2" <%if(processoArea != null && processoArea.equalsIgnoreCase("2")){%> checked<%}%>> Criminal
			    		</div>
						<br />
						
					    <label class="formEdicaoLabel" for="Id_ProcessoTipo">*Classe
					    <% if(processoDt.getProcessoTipoCodigo().equalsIgnoreCase("107")) { //se o processo for do tipo CARTA PRECATORIA (código = 107), não será permitido alterá-lo %>
					    <font color="#FF0000">(Esta classe não permite alteração. Em caso de dúvidas, procure o suporte)</font></label><br>
					    <% } else { %>
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > </label>  <br> 
					    <% } %>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="200" maxlength="200" value="<%=processoDt.getProcessoTipo()%>"/>
					    <br />

			    		<!-- ASSUNTOS DO PROCESSO -->
			    		<input type="hidden" id="posicaoLista" name="posicaoLista">
			    		<div class='col100'>
				    		<fieldset id="VisualizaDados" class="VisualizaDados" >  
	   						<legend> 
	   							*Assunto(s)
	   							<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
	   						</legend>
						   		    <%List listaAssuntos = (List) request.getSession().getAttribute("ListaAssuntos");
						   		 	boolean boLinha=false;
						   	    	if (listaAssuntos != null && listaAssuntos.size() > 0){ %>
						   			<table width="98%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
						   				<thead align="left">
						   					<th width="90%">Descrição</th>
						   					<th align="center" >Excluir</th>
						   				</thead>
						   	    		  <%for(int i=0;i < listaAssuntos.size();i++){
						   	    		      ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt)listaAssuntos.get(i); %>
							   			<tbody>
											<tr class="TabelaLinha<%=(boLinha?1:2)%>">
												<td>
												<% if (assuntoDt.getIsAtivo() != null) {  
												     if (assuntoDt.getIsAtivo().equals("0") || assuntoDt.getIsAtivo().equals("I")) { %>
												       <font color="red"><%=assuntoDt.getAssunto() %></font>
												     <% } else { %>
												     <%=assuntoDt.getAssunto() %>
												     <% } %>
												<% } else { %>
												<%=assuntoDt.getAssunto() %>
												<% } %>
												</td>
						       	 				<td align="center"><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','6');AlterarValue('posicaoLista','<%=i%>')" title="Excluir assunto"/></td>
						       	 			</tr>
						       	 		</tbody>
						       		<%boLinha=!boLinha;
						       		} %>
						       	</table>
						   		<% } else { %> <em> Nenhum assunto cadastrado </em> <% } %>
							</fieldset>
						</div>
				    	<div class="col30">
						    <label  for="Id_Classificador">Classificador
						    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						   		<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">  
						    </label>						    
						    <input type="hidden" name="Id_Classificador" id="Id_Classificador">
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="50" maxlength="100" value="<%=processoDt.getClassificador()%>"/>
						</div>
					    
					    <div class="col30 ">
						    <label for="Id_ProcessoFase">*Fase Processual
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoFase" name="imaLocalizarProcessoFase" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoFaseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
						    </label>  
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoFase" id="ProcessoFase" type="text" size="50" maxlength="100" value="<%=processoDt.getProcessoFase()%>"/>
						</div>
						
						<div class="col30">
						    <label for="Id_ProcessoPrioridade">*Prioridade
							    <input type="hidden" name="Id_ProcessoPrioridade" id="Id_ProcessoPrioridade">  
							    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoPrioridade" name="imaLocalizarProcessoPrioridade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						    </label>
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoPrioridade" id="ProcessoPrioridade" type="text" size="40" maxlength="100" value="<%=processoDt.getProcessoPrioridade()%>"/>
					    </div>
					    <hr>
					    <div class="col100" style="margin-top:10px">
							<div class="col20">
								<input class="formEdicaoInput" name="SegredoJustica" id="SegredoJustica"  type="checkbox"  value="true" <% if(processoDt.getSegredoJustica().equalsIgnoreCase("true")){%>  checked<%}%>/>
					    	    <label for="SegredoJustica">Segredo de Justiça</label> 
						    </div>
		    				<div class='col20'>
			    				<input class="formEdicaoInput" name="EfeitoSuspensivo" id="EfeitoSuspensivo"  type="checkbox"  value="true" <% if(processoDt.getEfeitoSuspensivo().equalsIgnoreCase("true")){%>  checked<%}%>/> 
			    				<label for="EfeitoSuspensivo">Efeito Suspensivo</label>
			    			</div> 
			    			<%if (processoDt.isCriminal()){%>
								<div class='col20'>								
				    				<input class="formEdicaoInput" name="ReuPreso" id="ReuPreso"  type="checkbox"  value="true" <% if(processoDt.getProcessoCriminalDt().getReuPreso().equalsIgnoreCase("true")){%>  checked<%}%>/>
				    				<label for="ReuPreso">Réu Preso</label>
								</div> 
							<%} %>
						</div>							
					    		    			    		
			    		<div class="col100" style="margin-top:10px">
				    		<% if (Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU){ %>
				    			<div class='col20'>
				    				<input class="formEdicaoInput" name="Julgado2Grau" id="Julgado2Grau"  type="checkbox"  value="true" <% if(processoDt.getJulgado2Grau().equalsIgnoreCase("true")){%>  checked<%}%>/> 
				    				<label for="Julgado2Grau">Julgado 2º Grau/Turma</label><br>
				    			</div> 
							<% } %>
			    		 	<div class="col20 ">
			    				<input class="formEdicaoInput" name="Digital100" id="Digital100"  type="checkbox"  value="true" <% if(processoDt.is100Digital()){%>  checked<%}%>/> 
			    				<label for="Digital100">Juízo 100% digital</label>
			    			</div>
				    		<%if (processoDt.isCivel()){%>
							    <div class="col20 ">
							    	<label for="Valor">Valor</label>
					    			<input class="formEdicaoInput" name="Valor" id="Valor"  type="text" size="20" maxlength="20" value="<%=processoDt.getValor()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/> 	
							    </div>					
				    		 	<div class="col20 ">
				    				<input class="formEdicaoInput" name="Penhora" id="Penhora"  type="checkbox"  value="true" <% if(processoDt.getPenhora().equalsIgnoreCase("true")){%>  checked<%}%>/> 
				    				<label for="Penhora">Penhora no Rosto dos Autos</label>
				    			</div>
				    		<%} else { %>				    			
				    			<div class='col20'>
				    				<label  for="TcoNumero">Protocolo SSP</label> 
									<input class="formEdicaoInput" name="TcoNumero" id="TcoNumero" type="text" size="15" maxlength="15" value="<%=processoDt.getTcoNumero()%>"/>
								</div>				    			
								<div class='col45' id="divOpcoesArquivamento" >
									<label >*Arquivamento Tipo
										<img height="13" width="13"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('divOpcoesArquivamento','ProcessoArquivamentoTipo','Consulta tipos de arquivamento', 'Digite a descrição e clique em consultar.', 'Id_ProcessoArquivamentoTipo', 'ProcessoArquivamentoTipo', ['Tipo de Arquivamenteo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
										<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoArquivamentoTipo','ProcessoArquivamentoTipo'); return false;" title="Limpar Tipo de Arquivamento">
										<input id="Id_ProcessoArquivamentoTipo" name="Id_ProcessoArquivamentoTipo" type="hidden" value="<%=processoDt.getProcessoCriminalDt().getId_ProcessoArquivamentoTipo()%>" /> 					
									</label> 
									<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProcessoArquivamentoTipo" id="ProcessoArquivamentoTipo" type="text" size="60" maxlength="60" value="<%=processoDt.getProcessoCriminalDt().getProcessoArquivamentoTipo()%>"/>
								</div>
								<br />
								<div class='col30'>
									<label  for="DataOferecimentoQueixa">
						    			<img id="calendarioDataOferecimentoQueixa" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataOferecimentoQueixa,'dd/mm/yyyy',this)">
				    					<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataOferecimentoQueixa','DataOferecimentoQueixa'); return false;" title="Limpar Data Oferecimento Denúncia/Queixa">
				    					Data Oferecimento Denúncia/Queixa 
				    				</label>				    		
					    			<input class="formEdicaoInput" name="DataOferecimentoQueixa" id="DataOferecimentoQueixa"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataOferecimentoDenuncia()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">			    		 
								</div>
								<div class='col30'>
									<label  for="DataRecebimentoQueixa">
					    				<img id="calendarioDataRecebimentoQueixa" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataRecebimentoQueixa,'dd/mm/yyyy',this)">
					    				<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataRecebimentoQueixa','DataRecebimentoQueixa'); return false;" title="Limpar Data Recebimento Denúncia/Queixa">
					    				Data Recebimento Denúncia/Queixa 
					    			</label>
					    			<input class="formEdicaoInput" name="DataRecebimentoQueixa" id="DataRecebimentoQueixa"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataRecebimentoDenuncia()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">			    		 
								</div>
								<div class='col20'>
									<label  for="DataPrisao">
				    					<img id="calendarioDataPrisao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataPrisao,'dd/mm/yyyy',this)">
					    				<img height="13" width="13""image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataPrisao','DataPrisao'); return false;" title="Limpar Data Prisão">
					    				Data Prisão 
					    			</label>
				    				<input class="formEdicaoInput" name="DataPrisao" id="DataPrisao"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataPrisao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">			    		 
								</div>
								<div class='col30'>
				    				<label  for="DataTransacaoPenal">
				    					<img id="calendarioDataTransacaoPenal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataTransacaoPenal,'dd/mm/yyyy',this)">
				    					<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataTransacaoPenal','DataTransacaoPenal'); return false;" title="Limpar Data Transacao Penal">
				    					Data Transação Penal 
				    				</label>
					    			<input class="formEdicaoInput" name="DataTransacaoPenal" id="DataTransacaoPenal"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataTransacaoPenal()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
					    		</div>			    		 
								<div class='col30'>
									<label  for="DataSuspensaoPenal">
				    					<img id="calendarioDataSuspensaoPenal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataSuspensaoPenal,'dd/mm/yyyy',this)">
				    					<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataSuspensaoPenal','DataSuspensaoPenal'); return false;" title="Limpar Data Suspensão Penal"> 
				    					Data Suspensão Penal
				    				</label>
				    				<input class="formEdicaoInput" name="DataSuspensaoPenal" id="DataSuspensaoPenal"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataSuspensaoPenal()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
				    			</div>			    		 
								<div class='col20'>
									<label  for="DataFato">
				    					<img id="calendarioDataFato" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFato,'dd/mm/yyyy',this)">
				    					<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataFato','DataFato'); return false;" title="Limpar Data do Fato">
				    					Data do Fato 
				    				</label>
				    				<input class="formEdicaoInput" name="DataFato" id="DataFato"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataFato()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
				    			</div>				    			
				    		<%}%>
			    		</div> 
			    		<div class="col100" >   
				    		<div class="col30">   
				    		   <label  for="DataPrescricao">
					    			<img id="calendarioDataPrescricao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataPrescricao,'dd/mm/yyyy',this)">
						    	   <img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataPrescricao','DataPrescricao'); return false;" title="Limpar Data Provável Prescrição">
						    	   Data Prescricão 
					    	   </label>
					    	   <input class="formEdicaoInput" name="DataPrescricao" id="DataPrescricao"  type="text" size="10" maxlength="10" value="<%=processoDt.getDataPrescricao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">			    		 
					    	</div>				    		   
				    		<div class="col30">   
					    		<label  for="DataTransitoJulgado">
						    		<img id="calendarioDataTransitoJulgado" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataTransitoJulgado,'dd/mm/yyyy',this)">	
						    		<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataTransitoJulgado','DataTransitoJulgado'); return false;" title="Limpar Data Transito e Julgado">
						    		Data Trânsito e Julgado 
					    		</label>
					    		<input class="formEdicaoInput" name="DataTransitoJulgado" id="DataTransitoJulgado"  type="text" size="10" maxlength="10" value="<%=processoDt.getDataTransitoJulgado()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">			    		 
				    		</div>
				    		<%if (processoDt.isCriminal()){%>
				    			<div class='col20'>
									<label  for="DataBaixa">
				    					<img id="calendarioDataBaixa" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataBaixa,'dd/mm/yyyy',this)">
				    					<img height="13" width="13"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataBaixa','DataBaixa'); return false;" title="Limpar Data de Baixa">
				    					Data de Baixa 
				    				</label>
				    				<input class="formEdicaoInput" name="DataBaixa" id="DataBaixa"  type="text" size="10" maxlength="10" value="<%=processoDt.getProcessoCriminalDt().getDataBaixa()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
				    			</div>	
				    		<% } %>	
			    		</div>
			    		<br />
			    		<div class="col30">
						    <label  for="ValorCondenacao">Valor da Condenação</label>
				    		<input class="formEdicaoInput" name="ValorCondenacao" id="ValorCondenacao"  type="text" size="20" maxlength="20" value="<%=processoDt.getValorCondenacao()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/> 		    		 
			    		</div>
			    		
					    <div class="col30">
						    <label for="ValorCondenacao">Localizador</label>
				    		<input class="formEdicaoInput" name="localizador" id="localizador"  type="text" size="20" maxlength="20" value="<%=processoDt.getLocalizador()%>"  /> 		    		 
			    		</div>
			    			
		    			<div class="col100">		
		    				<b>Tipo de Custas: </b>	
				    		<input type="radio" name="CustaTipo" value="1" <%if(processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase("1")){%> checked<%}%>> Com Custas
				    		<input type="radio" name="CustaTipo" value="2" <%if(processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase("2")){%> checked<%}%>> Assistência Judiciária
				    		<input type="radio" name="CustaTipo" value="3" <%if(processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase("3")){%> checked<%}%>> Isento			
		    			</div>
		    			
		    			<br/>
		    			<br/>
		    			<%if (processoDt.isCriminal()){%>
			    			<div class="col100">								
						    	<input class="formEdicaoInput" name="DeclaracaoInconsistencias" id="DeclaracaoInconsistencias"  type="checkbox"  value="true" <%=processoDt.isCertificouNaoInconsistencia()?"checked":""%>/>
						    	<label for="DeclaracaoInconsistencias"> <%=processoDt.getCertidao()%> </label>
							</div>
						<%} %>
		    						    				
	    				<br />	    				
	    				<% if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
				    	</div>
				    	<%} %>
		    		</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>