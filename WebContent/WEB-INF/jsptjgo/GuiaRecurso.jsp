<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Guia de Recurso de Apelação</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript">
		$(document).ready(
						
			function() {
				//pego todos objetos que foram marcados com a class nomes
				//e verifico se tem número no nome
				 $(".nomes").each(function( index ) {
				 	var texto =  $( this ).text();
					for(var numero=0; numero<=9; numero++){
						texto= texto.replace(numero,'<p class="destacarNumero" tag="Foi utilizado número no Nome, favor conferir com os dados da petição" title="Foi utilizado número no Nome, favor conferir com os dados da petição">'+ numero +'</p>');
					}
	
					$( this ).html(texto);			
				});		
			}
		); 	
	</script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Guia</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GuiaInicial" id="GuiaInicial">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input type="hidden" id="posicaoListaCustaExcluir" name="posicaoListaCustaExcluir" value="-1" />
			<input type="hidden" id="posicaoListaBairroExcluir" name="posicaoListaBairroExcluir" value="-1" />
			<input type="hidden" id="guiaIdProcesso" name="guiaIdProcesso" value="<%=request.getAttribute("guiaIdProcesso")%>" />
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                	<div> Número </div>
					<span><a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
                </fieldset>
                
                <br /><br />
				
				<%@ include file="BuscaPartesProcesso.jspf"%>
				
                <br /><br />
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Informações </legend>
				    
					<div> Serventia </div>
					<span class="span1"><%= processoDt.getServentia()%></span>
					<br />
					
					<div> Classe </div>
				    <span style="width: 500px;"><%=processoDt.getProcessoTipo()%></span>
				    <br />
				   	
				    <%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
				   	
				    <% if (!processoDt.getValor().equals("Null")){ %>
						<div> Valor da Causa</div>
						<span class="span1"><%=processoDt.getValor()%></span>
					<%} %>
				</fieldset>
				
				<br /><br />
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Apelante e Apelado
                	</legend>
                	
                	<div> Apelante </div>
					<span>
						<select id="id_apelante" name="id_apelante" onchange="alteradoApelante()">
							<option selected value=""></option>
							<%
							if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
			   	    			for (int i=0;i < listaPromoventes.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%> - Polo Ativo</option>
								<%	}
							}%>
							<%
							if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
			   	    			for (int i=0;i < listaPromovidos.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%> - Polo Passivo</option>
								<%	}
							}%>
							<%
							if( listaOutrasPartes != null && listaOutrasPartes.size() > 0 ) {
			   	    			for (int i=0;i < listaOutrasPartes.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%> - <%=parteDt.getProcessoParteTipo()%></option>
								<%	}
							}%>							
						</select>
					</span>
					
					<br /><br />
					
					<div> Apelado </div>
					<span>
						<select id="id_apelado" name="id_apelado" onchange="alteradoApelado()">
							<option selected value=""></option>
							<%
							if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
			   	    			for (int i=0;i < listaPromoventes.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelado())?"selected":"")%>><%=parteDt.getNome()%> - Polo Ativo</option>
							<%	}
							}%>
							<%
							if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
			   	    			for (int i=0;i < listaPromovidos.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelado())?"selected":"")%>><%=parteDt.getNome()%> - Polo Passivo</option>
							<%	}
							}%>
							<%
							if( listaOutrasPartes != null && listaOutrasPartes.size() > 0 ) {
			   	    			for (int i=0;i < listaOutrasPartes.size();i++) {
				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
				   				%>
									<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(GuiaEmissaoDt.getId_Apelante())?"selected":"")%>><%=parteDt.getNome()%> - <%=parteDt.getProcessoParteTipo()%></option>
								<%	}
							}%>							
						</select>
					</span>
                </fieldset>
                
                <br/><br/>
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Dobrar Valor da Guia ?
                	</legend>
                	
                	<div style="width: 150px;">Dobrar Valor ?</div>
                	<span>
                		<input type="checkbox" name="dobrarValorGuiaCheck" id="dobrarValorGuiaCheck" value="<%=GuiaEmissaoNe.VALOR_MANUAL_SIM %>" title="Deseja dobrar o valor desta guia ?" />
                	</span>

                </fieldset>
                
                <br /><br />
                
                <%
                if( request.getAttribute("isContador") != null ) {
	                boolean isContador = (boolean)request.getAttribute("isContador");
	                if( isContador ) { %>
	                	<fieldset id="VisualizaDados" class="VisualizaDados">
		                	<legend>
		                		Recurso Complementar ?
		                	</legend>
		                	
		                	<br /><br />
		                	
			                <fieldset id="VisualizaDados" class="VisualizaDados">
			                	<legend>
			                		Porte Remessa (Informando o Valor Manual em R$)
			                	</legend>
			                	
			                	<div style="width: 150px;">Valor Manual?</div>
			                	<span>
			                		<input type="checkbox" name="porteRemessaValorManualCheck" id="porteRemessaValorManualCheck" value="<%=GuiaEmissaoNe.VALOR_MANUAL_SIM %>" onclick="AlterarValue('porteRemessaQuantidade','0')" title="Marque para adicionar cobrança de porte remessa informando o Valor Manual." />
			                	</span>
			
			                	<div style="width: 150px;">Valor Manual R$</div>
			                	<span>
			                		<input type="text" name="porteRemessaValorManual" id="porteRemessaValorManual" value="<%=GuiaEmissaoDt.getPorteRemessaValorManual()%>" maxlength="6" onkeyup="MascaraValor(this);autoTab(this,20);" onkeypress="return DigitarSoNumero(this, event)" title="Informe o Valor para o porte remessa." onblur="zerarCampoLimpo(this, '0')" />
			                	</span>
			                	
			                </fieldset>
			                
			                <br /><br />
			                
			                <fieldset id="VisualizaDados" class="VisualizaDados">
			                	<legend>
			                		Custas da Secretaria do TJGO (Informando o Valor Manual em R$)
			                	</legend>
			                	
			                	<div style="width: 150px;">Valor Manual?</div>
			                	<span>
			                		<input type="checkbox" name="custasValorManualCheck" id="custasValorManualCheck" value="<%=GuiaEmissaoNe.VALOR_MANUAL_SIM %>" title="Marque para adicionar cobrança de custas informando o Valor Manual." />
			                	</span>
			
			                	<div style="width: 150px;">Valor Manual R$</div>
			                	<span>
			                		<input type="text" name="custasValorManual" id="custasValorManual" value="<%=GuiaEmissaoDt.getCustasValorManual()%>" maxlength="6" onkeyup="MascaraValor(this);autoTab(this,20);" onkeypress="return DigitarSoNumero(this, event)" title="Informe o Valor para as Custas." onblur="zerarCampoLimpo(this, '0')" />
			                	</span>
			                	
			                </fieldset>
			                
			            </fieldset>
		                
		                <br /><br />
	                <%}
	           	}%>
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Protocolo Integrado
                	</legend>
                	
                	<div style="width: 150px;">Quantidade de Folhas</div>
                	<span>
                		<input type="text" name="protocoloIntegrado" id="protocoloIntegrado" value="<%=GuiaEmissaoDt.getProtocoloIntegrado()%>" maxlength="4" onkeypress="return DigitarSoNumero(this, event)" title="Informe a quantidade de folhas para o protocolo integrado." onblur="zerarCampoLimpo(this, '0')" />
                	</span>
                	
                </fieldset>
                
                <br /><br />
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>
                		Porte Remessa (Item Regimento 4.V)
                	</legend>
                	
                	<div style="width: 150px;">Quantidade</div>
                	<span>
                		<input type="text" name="porteRemessaQuantidade" id="porteRemessaQuantidade" value="<%=GuiaEmissaoDt.getPorteRemessaQuantidade()%>" maxlength="4" onclick="limparCamposGuiaApelacao('porteRemessaValorManualCheck','porteRemessaValorManual');" onfocus="limparCamposGuiaApelacao('porteRemessaValorManualCheck','porteRemessaValorManual');" onkeypress="return DigitarSoNumero(this, event)" title="Informe a quantidade de porte remessa." onblur="zerarCampoLimpo(this, '0')" />
                	</span>
                	
                </fieldset>
                
                <br /><br />
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgPreviaCalculo" value="Prévia do Cálculo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" >
                    	<img src="./imagens/16x16/calculadora.png" alt="Prévia do Cálculo" />
                    	Prévia do Cálculo
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	<img src="./imagens/16x16/edit-clear.png" alt="Limpar" />
                    	Limpar
                    </button>
                </div>
                
			</div>
			
			<br/><br/>
		
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>