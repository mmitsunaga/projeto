<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>
<%@page import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="SituacaoAtualDt" scope="request" class="br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt"/>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.SituacaoAtualExecucaoPenalDt"%>

<html>
	<head>
		<title>Processo Execução Penal</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<script type="text/javascript">
			function buscarProcesso(id_processo) {
				//apresentar os dados do Processo
				AlterarValue('PaginaAtual', '-1');
				AlterarValue('PassoEditar', '-1');
				AlterarAction('Formulario','BuscaProcesso');
				obj = document.getElementById("Id_Processo");
				obj.value = id_processo;
				document.getElementById("Formulario").submit();
			}
		</script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Processo de Execução Penal - <%=request.getAttribute("TituloPagina")%></h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<input id="PassoImprimir" name="PassoImprimir" type="hidden" value="<%=request.getAttribute("PassoImprimir")%>"/>
				<input id="Id_Processo" name="Id_Processo" type="hidden" value=""/>
				<input id="Id_PenaExecucaoTipo" name="Id_PenaExecucaoTipo" type="hidden" value=""/>
				<br />
				<div id="divEditar" class="divEditar">
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgCancelar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('PassoEditar','4'); AlterarValue('PassoImprimir','-1');"/>
					<input name="imgImprimir" type="submit" value="Imprimir Resultado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('PassoImprimir','4');"/>
				</div>
				<%
				List listaSentenciado = (List) request.getAttribute("listaSentenciado");
				//Tela inicial de consulta de Data provavel
				if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){
				%>
				<fieldset class="formEdicao"> <legend class="formEdicaoLegenda">Parâmetros da Consulta</legend>
					<input type="hidden" name="Id_FormaCumprimento" id="Id_FormaCumprimento"  value="<%=SituacaoAtualDt.getIdFormaCumprimento()%>"/>
					<label style="width: 20%" class="formEdicaoLabel" for="Id_FormaCumprimento">Forma de Cumprimento</label><br>  
		    	    <input class="formEdicaoInputSomenteLeitura"  readonly name="FormaCumprimento" id="FormaCumprimento" type="text" size="64" maxlength="100" value="<%=Funcoes.verificarCampo(SituacaoAtualDt.getFormaCumprimento(),"","-")%>"/>
					<br />
					<input type="hidden" name="Id_LocalCumprimentoPena" id="Id_LocalCumprimentoPena"  value="<%=SituacaoAtualDt.getIdLocalCumprimentoPena()%>"/>
					<label style="width: 20%" class="formEdicaoLabel" for="Id_LocalCumprimentoPena">Local de Cumprimento da Pena</label><br>  
		    	    <input class="formEdicaoInputSomenteLeitura"  readonly name="LocalCumprimentoPena" id="LocalCumprimentoPena" type="text" size="64" maxlength="100" value="<%=Funcoes.verificarCampo(SituacaoAtualDt.getLocalCumprimentoPena(),"","-")%>"/>
					<br />					
					<input type="hidden" name="Id_Modalidade" id="Id_Modalidade"  value="<%=SituacaoAtualDt.getIdModalidade()%>"/>
					<label style="width: 20%" class="formEdicaoLabel" for="Id_Modalidade">Modalidade</label><br>  
		    	    <input class="formEdicaoInputSomenteLeitura"  readonly name="Modalidade" id="Modalidade" type="text" size="64" maxlength="100" value="<%=Funcoes.verificarCampo(SituacaoAtualDt.getModalidade(),"","-")%>"/>
					<br />
					<input type="hidden" name="Id_RegimeExecucao" id="Id_RegimeExecucao"  value="<%=SituacaoAtualDt.getIdRegime()%>"/>
					<label style="width: 20%" class="formEdicaoLabel" for="Id_RegimeExecucao">Regime</label><br>  
		    	    <input class="formEdicaoInputSomenteLeitura"  readonly name="RegimeExecucao" id="RegimeExecucao" type="text" size="64" maxlength="100" value="<%=Funcoes.verificarCampo(SituacaoAtualDt.getRegime(),"","-")%>"/>
					<br />
					<input type="hidden" name="Id_EventoExecucaoStatus" id="Id_EventoExecucaoStatus"  value="<%=SituacaoAtualDt.getIdEventoExecucaoStatus()%>"/>
					<label style="width: 20%" class="formEdicaoLabel" for="Id_SituacaoSentenciado">Situação</label><br>  
		    	    <input class="formEdicaoInputSomenteLeitura"  readonly name="EventoExecucaoStatus" id="EventoExecucaoStatus" type="text" size="64" maxlength="100" value="<%=Funcoes.verificarCampo(SituacaoAtualDt.getEventoExecucaoStatus(),"","-")%>"/><br />
		    	</fieldset>
				<fieldset  class="formEdicao">
				<legend>Lista dos Sentenciados</legend> 
					<div id="divTabela" class="divTabela">
					<table id="ListaReeducandos" class="Tabela" width="100%">
	        			<thead>
	            			<tr class="TituloColuna">
								<th></th>
	            				<th align="center">Tipo do Processo</th>
	            				<th align="center">Nº Processo</th>
	                			<th align="left">Sentenciado</th>
	                			<th align="left">Local de Cumprimento</th>
	                			<th align="left">Regime</th>
								<th align="left">Forma de Cumprimento</th>
	                			<th align="left">Situação</th>
	                			<th align="left">Modalidade(s)</th>
								<th align="center">Cálculo</th>
	                			<th align="center">Término da Pena</th>
	                			<th align="center">Atualização dos Dados</th>
	               			</tr>
	           			</thead>
	    				<tbody id="tabListaProcesso">
			    		<%
			    		 	boolean boLinha=false; 
			    			if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){
			   	    			for (int i=0; i<listaSentenciado.size(); i++){
			   	    				SituacaoAtualExecucaoPenalDt sentenciado = new SituacaoAtualExecucaoPenalDt();
			   	    				sentenciado = (SituacaoAtualExecucaoPenalDt)listaSentenciado.get(i);
						%>			
								<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>" onclick="javascript: buscarProcesso(<%=sentenciado.getIdProcesso()%>);" title="Consultar Processo">
									<td><%=i+1%></td>
									<td align="center"><%=sentenciado.getProcessoTipo()%></td>									
									<td align="center"><%=sentenciado.getNumeroProcesso()%></td>
									<td align="left"><%=sentenciado.getInformacaoSentenciado()%></td>
									<td align="center"><%=sentenciado.getDescricaoLocalCumprimentoPena()%></td>
									<td align="center"><%=sentenciado.getDescricaoRegime()%></td>
									<td align="center"><%=sentenciado.getFormaCumprimento()%></td>
		                   			<td align="center"><%=sentenciado.getDescricaoSituacao()%></td>
		                   			<td align="center"><%=Funcoes.verificarCampo(sentenciado.getDescricaoModalidade(),"","--")%></td>
									<td align="center"><%=sentenciado.getDataCalculo()%></td>
		                   			<td align="center"><%=sentenciado.getDataTerminoPena()%></td>
		                   			<td align="center"><%=sentenciado.getDataAtualizacao()%></td>
		                   		</tr>
	                   	<%		boLinha = !boLinha;}
			   	    		}%>
			   	    	</tbody>
	   	    				
	                </table>
	                </div>
		      </fieldset>     
				<%}else {%>			
					<div style="width:260px"></div><span class="span1">Não foi encontrado sentenciado.</span><br />
				<%}%>
				</div>
				<br />
 			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>