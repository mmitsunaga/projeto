<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.InterlocutoriaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.HistoricoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.SentencaDt" %>
<%@page import="br.gov.go.tj.projudi.dt.IntimacaoDt" %>
<%@page import="br.gov.go.tj.projudi.dt.RedistribuicaoFisicoDt" %>
<%@page import="br.gov.go.tj.projudi.dt.LigacaoDt" %>


<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="processoFisicoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoFisicoDt"/>


<html>
	<head>
	
	<title>Consultar Processo Fisico</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	    
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>	
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				
			 <fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Dados do Gerais</legend>
			 		
					<div>N&uacute;mero Processo: </div>
						<span><%=processoFisicoDt.getNumeroProcesso() %></span>
					<div> Protocolo: </div>
					<span>	<%=processoFisicoDt.getDataProtocolo() %></span>
					<br>
					<div> Natureza: </div>
					<span>	<%=processoFisicoDt.getNatureza() %></span>
					<div>Autuacao: </div>
						<span><%=processoFisicoDt.getDataAutuacao() %></span>
					<br>
						<div>Distribuição: </div>
						<span><%=processoFisicoDt.getDataDistribuicao() %></span>
						<div>Descrição da Fase: </div>
						<span><%=processoFisicoDt.getDescricaoFase()%></span>		
								<br>
					<div>Fase: </div>
						<span ><%=processoFisicoDt.getFase() %></span>
										
					<br>
					<div>Primeiro Autor: </div>
						<span><%=processoFisicoDt.getPromovente()%></span>
					<div>Primeiro Reqdo: </div>
						<span><%=processoFisicoDt.getPromovido() %></span>
					<br>
								<div>Juiz: </div>
						<span><%=processoFisicoDt.getJuiz() %></span>
						<div>Comarca/Escrivania: </div>
						<span><%=processoFisicoDt.getComarca() + " - " + processoFisicoDt.getEscrivania() %></span>
					<br>
<!-- 					<div>Localização: </div> -->
<%-- 						<span><%=processoFisicoDt.getLocalizacao() %></span> --%>
<!-- 					<br> -->
				
						<div>Audiência: </div>
						<span><%=processoFisicoDt.getDataAudiencia() %></span>
					<div>Sentença: </div>
						<span><%=processoFisicoDt.getDataSentenca() %></span>
					<br>
						<div>Promotor: </div>
						<span><%=processoFisicoDt.getPromotor() %></span>
					<br>
   			
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Interlocut&oacute;rias</legend>
				  	<%
				  	if (!processoFisicoDt.temInterlocutoria()){%>
						Processo sem Interlocurórias
					<%}else{
					   	Iterator<InterlocutoriaDt> i = processoFisicoDt.getInterlocutorias().iterator();
					   	while (i.hasNext()) {
					   	   InterlocutoriaDt inter = i.next();
					  %>
					   <fieldset>
					   <legend>	Interlocutoria: <%=inter.getNumero()%></legend>
					   		<div>Protocolo: </div>
					   		<span><%=inter.getDataProtocolo()%></span>
					   		<div>Identificacao: </div>
					   		<span><%=inter.getTipo()%></span>
					   		<br>
					   		<div>Fase: </div>
					   		<span><%=inter.getFase()%></span>
					   </fieldset>				
					        
					  <% 
					  }
				   	}
				   %>
				   	
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Mandados</legend>
				  	<%if (!processoFisicoDt.temMandado()){%>
						Processo sem Mandados
					<%}else{
					   	Iterator<MandadoDt> m = processoFisicoDt.getMandados().iterator();
					   	while (m.hasNext()) {
					   	   MandadoDt mand = m.next();
					  %>
					  	<fieldset >
					   	<legend >Mandado: <%=mand.getNumero()%></legend>		   		
					   		<div>Tipo: </div>
					   		<span><%=mand.getTipo() %></span>
					   		<div>Data de Emissão: </div>
					   		<span><%=mand.getEmissaoData()%></span>
					   		<br>
					   		
					   		<div>Recebimento: </div>
					   		<span><%=mand.getRecebimentoData()%></span>
					   		<div>Data Distribui&ccedil;&atilde;o: </div>
					   		<span><%=mand.getDistribuicaoData()%></span>
					   		<br>
					   		
					   		<div>Data da Audiência:</div>
					   		<span><%=mand.getAudienciaData()%></span>
					   		<div>Nome Partes:</div>
					   		<span><%=mand.getPartesNome() %></span>
					   		<br>
					   		
					   		<div>Escrivania:</div>
					   		<span><%=mand.getEscrivania()%></span>
					   		<div>Data Devolução:</div>
					   		<span><%= mand.getDevolucaoEscrivaniaData()%></span>
					   		<br>
					   		
					   		<div>Nome do Oficial:</div>
					   		<span><%=mand.getOficial()%></span>
					   		<div>Recebido:</div>
					   		<span><%=mand.getRecebimentoOficialData()%></span>
					   		<br>
					   		
					   		<div>Situação:</div>
					   		<span><%=mand.getPrazoCumprimentoData()%></span>
					   		<div>Prazo:</div>
					   		<span><%=mand.getPrazoCumprimentoData()%></span>
					   		<br>
					   		
					   		<div>Motivo Devolução:</div>
					   		<span><%=mand.getMotivoDevolucao()%></span>
					   		<div>Data Devolução:</div>
					   		<span><%=mand.getDevolucaoOficialData() %></span>
					   		<br>
					   	</fieldset>
					        
					  <% 
					  }
				  	}
				   %>
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Hist&oacute;rico</legend>
				  	<%if (!processoFisicoDt.temHistorico()){	%>
						Processo sem Histórico
					<%}else{
					   	Iterator<HistoricoDt> h = processoFisicoDt.getHistoricos().iterator();
					   	while (h.hasNext()) {
					   	   HistoricoDt hist = h.next();
					  %>
					  	<fieldset>
					   	<legend>Historico: <%=hist.getData() +" às " + hist.getHora()%></legend>
					   		<div>Fase: </div>
					   		<span><%=hist.getFase()%></span>
					   		<div>Descricao: </div>
					   		<span><%=hist.getDescricaoFase()%></span>
					   		<br>
						</fieldset>
					        
					<%}
				  	}%>
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Sente&ccedil;as</legend>
				   	<%if (!processoFisicoDt.temSentenca()){%>
						Processo sem Sentença
					<%}else{
					   	Iterator<SentencaDt> s = processoFisicoDt.getSentencas().iterator();
					   	while (s.hasNext()) {
					   	   SentencaDt sent = s.next();
					  %>
					  	<fieldset >
					   	<legend>Senten&ccedil;a: <%=sent.getSentencaData()%></legend>
					   		<div>Tipo: </div>
					   		<span><%=sent.getSentencaTipo()%></span>
					   		<div>Data Transito Julgado: </div>
					   		<span><%=sent.getTransitoJulgadoData()%></span>
					   		<br>
					   		<input name="inputEndereco" type="image" src="imagens/22x22/ico_arquivos.png" onclick="MostrarOcultar('sub<%=sent.getTexto().hashCode()%>');return false;" title="Mostrar/Ocultar Sentença" />
					   			<div id="sub<%=sent.getTexto().hashCode()%>"  class="DivInvisivel">
					  		<fieldset class="fieldsetEndereco">
					  		
					   		<%=sent.getTexto()%>
					   			</fieldset>
						</fieldset>
					        
					<%} 				   	
				   	}%>
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Intima&ccedil;&otilde;es</legend>
				  <%if (!processoFisicoDt.temIntimacao()){%>
						Processo sem Intimação
				  <%}else{	
					   	Iterator<IntimacaoDt> in = processoFisicoDt.getIntimacoes().iterator();
					   	while (in.hasNext()) {
					   	   IntimacaoDt inti = in.next();
					  %>
					  	<fieldset>
					   	<legend>Intima&ccedil;&atilde;o:</legend>
					   		<div>Data de Extrata&ccedil;&atilde;o: </div>
					   		<span><%=inti.getExtracaoData()%></span>
					   		<div>Di&aacute;rio de Justi&ccedil;a: </div>
					   		<span><%=inti.getDiarioJusticaNumero()%></span>
					   		<br>
					   		<div>Data de Publica&ccedil;&atilde;o: </div>
					   		<span><%=inti.getPublicadoData()%></span>
					   		<div>Circulado: </div>
					   		<span><%=inti.getCirculado() %></span>
					   		<br>
					   		<div>Folha: </div>
					   		<span><%=inti.getFolha()%></span>
					   		<br>
			 				<input name="inputEndereco" type="image" src="imagens/22x22/ico_arquivos.png" onclick="MostrarOcultar('sub<%=inti.getDiarioJusticaNumero()%>');return false;" title="Mostrar/Ocultar Despacho" />
					   			<div id="sub<%=inti.getDiarioJusticaNumero()%>"  class="DivInvisivel">
					  		<fieldset class="fieldsetEndereco">
					  		
					   		<%=inti.getDespacho()%>
							</fieldset>
							</fieldset>
					<%}
				  }%>
				   	
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Liga&ccedil;&otilde;es</legend>
				  <%if (!processoFisicoDt.temLigacoes()){%>
						Processo sem Ligações
				  <%}else{	
					   	LigacaoDt lig = processoFisicoDt.getLigacoes();		
					   	
					  %>
					  	<fieldset> 	<legend>Desmembramento</legend>							
					   		<div>Data</div>  			<span><%=lig.getDataApensamento()%></span> 				</br>
					   		<div>Proc. Atual: </div>	<span><%=lig.getProcessoDesmembradoAtual()%></span> 	</br>					   		
					   		<div>Lista Proc. </div>		<span><%=lig.getListaProcessosDesmembrados()%></span>	</br>
					   	</fieldset>
					   	<fieldset> 	<legend>Apensamento</legend>
					   		<div>Data : </div>			<span><%=lig.getDataApensamento() %></span>				</br>					   		
					   		<div>Num. Processoo: </div>	<span><%=lig.getNumeroProcessoApensado()%></span>		</br>
					   		<div>Lista Proc. </div>		<span><%=lig.getListaProcessosDesmembrados()%></span>	</br>
					   	</fieldset>
					   	<fieldset> 	<legend>Processo</legend>
					   		<div>Processo </div>				<span><%=lig.getProcessoAndamento()%></span>	</br>
					   		<div>Fase </div>					<span><%=lig.getFase()%></span>					</br>
					   		<div>Num. Recurso </div>			<span><%=lig.getNumeroRecurso()%></span>		</br>
					   	</fieldset>
					   							   					 									  							   													      
				<%} %>
				   	
				</fieldset>
				<fieldset class="VisualizaDados">
				   	<legend class="VisualizaDados">Redistribui&ccedil;&otilde;es</legend>
				   <%if (!processoFisicoDt.temRedistribuicao()){%>
						Processo sem Reditribuições
				  <%}else{	
					   	Iterator<RedistribuicaoFisicoDt> red = processoFisicoDt.getRedistribuicoes().iterator();
					   	while (red.hasNext()) {
					   		RedistribuicaoFisicoDt redis = red.next();
					  %>
					  
			
				  	<% 
				  	} 
				  }
				  %>
				   	
				</fieldset>
				
			</div>	
				
				<%@ include file="Padroes/Mensagens.jspf"%>
