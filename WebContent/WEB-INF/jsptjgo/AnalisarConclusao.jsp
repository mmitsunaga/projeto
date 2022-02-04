<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>
<%@page import="java.util.*"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Analisar Autos Conclusos</title>	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
	    <script type='text/javascript' src='./js/Funcoes.js'></script>
	    <script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>       	
		<script type='text/javascript' src='./js/Digitacao/MascararHoraResumida.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarHoraResumida.js'></script>
		<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
		<link rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	</head>

	<body onload="fechar('sim'); atualizarArquivos(); " >
		<div id="divCorpo" class="divCorpo" style="min-height: 1700px;">
			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
				<!-- Variáveis para controlar Passos da Análise -->
				<input id="Passo1" name="Passo1" type="hidden" value="<%=AnalisePendenciadt.getPasso1()%>">
				<input id="Passo2" name="Passo2" type="hidden" value="<%=AnalisePendenciadt.getPasso2()%>">
				<input id="Passo3" name="Passo3" type="hidden" value="<%=AnalisePendenciadt.getPasso3()%>">
			
				<div id="divEditar" class="divEditar">
				
					<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
					<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','1');">
					<% } if (!AnalisePendenciadt.getPasso3().equals("")){ %>				
					<input name="imgPasso3" type="submit" value="<%=AnalisePendenciadt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>')">
					<% } %>
					
					<fieldset class="formEdicao">
						<legend><%=request.getAttribute("TituloPagina") %></legend>
						<div style="max-height:165px; overflow:auto; margin-bottom:10px">
							<%@ include file="DadosConclusao.jspf" %>
						</div>
							
						<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
						<%@ include file="Padroes/InsercaoArquivosAssinador.jspf"%>
						<br />
						
						<div class="col45">
							<label class="formEdicaoLabel" for="Id_MovimentacaoTipo">*Tipo Movimentação 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacaoTipo" name="imaLocalizarMovimentacaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
							</label><br>  
							
							<input id="MovimentacaoTipo" name="MovimentacaoTipo" type="hidden" value="<%=AnalisePendenciadt.getMovimentacaoTipo()%>" />					
							<select name="Id_MovimentacaoTipo" id="Id_MovimentacaoTipo" size="1" onchange="AlterarValue('MovimentacaoTipo',this.options[this.selectedIndex].text)">
								<option value="null">--Selecione o Tipo de Movimentação--</option>
								<%
									List listaMovimentacaoTipo = AnalisePendenciadt.getListaTiposMovimentacaoConfigurado();
									
									for (int i=0;listaMovimentacaoTipo != null && i<listaMovimentacaoTipo.size();i++){
										UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
										
										if (usuarioMovimentacaoTipoDt!=null){%>
											<option value="<%=usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()%>" <%=AnalisePendenciadt.isMesmaMovimentacaoTipo(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo())?"selected='selected'":""%>><%=usuarioMovimentacaoTipoDt.getMovimentacaoTipo()%></option>										
									  <%}%>
								   <%}%> 	  						   
				           </select>
						</div>
						
						<div class="col45 clear">
						
						<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
						<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />
						</label><br> 
						
						<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="30" maxlength="80" value="<%=AnalisePendenciadt.getComplementoMovimentacao()%>"/>
						</div>
						<div class="clear"></div>
						<% String segundoGrau = (String) request.getAttribute("SegundoGrau"); %>
						<%if(segundoGrau != null && segundoGrau.equalsIgnoreCase("true")){ %>
							<br />
							<label class="formEdicaoLabel" for="julgadoMerito">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br><input class="FormEdicaoimgLocalizar" id="julgadoMerito" type="checkbox" name="julgadoMerito" onclick="AlterarValue('fluxo','1')";  value="true" <%if (AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal()!=null && AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")){%> checked<%}%> />
							<br />
						<%} %>
						
						<%if (AnalisePendenciadt.isVisualizarPedidoAssistencia()) { %>
							<label class="formEdicaoLabel"> *Pedido Assistência </label>
						    <div class="col90 clear">
							    <%String pedidoAssistencia = request.getParameter("PedidoAssistencia");
							    if (pedidoAssistencia == null || pedidoAssistencia.trim().equalsIgnoreCase("")){
							    	pedidoAssistencia = AnalisePendenciadt.getPedidoAssistencia(); 
							     } %>
					    		<input type="radio" id="1a" onchange="FormSubmit('Formulario');" name="PedidoAssistencia" value="1" <%if(pedidoAssistencia != null && pedidoAssistencia.equalsIgnoreCase("1")){%> checked<%}%>> Deferido
					    		<input type="radio" id="1b" onchange="FormSubmit('Formulario');" name="PedidoAssistencia" value="0" <%if(pedidoAssistencia != null && pedidoAssistencia.equalsIgnoreCase("0")){%> checked<%}%>> Indeferido
					    		<input type="radio" id="1b" onchange="FormSubmit('Formulario');" name="PedidoAssistencia" value="2" <%if(pedidoAssistencia != null && pedidoAssistencia.equalsIgnoreCase("2")){%> checked<%}%>> Diligência 
					    		
				    		</div>
							<br /> 
						<%} %>
						
						<br />
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','1');"> 
						</div>
					</fieldset>
				</div>
			</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>	
		 		
 		<script type="text/javascript">
		function fechar(valor){
			if (valor!=null){
				$('.corpo').hide();
			}
		}
				
// 		$(document).ready(function(){
// 			$('input[type=radio][name=PedidoAssistencia]').change(function() {
// 		        FormSubmit('Formulario');		        
// 		    });
// 		})	
		
		</script>
	</body>
</html>