<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>
<%@page import="java.util.*"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Analisar Pendência(s)</title>	
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

	<body onload="fechar('sim');">
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
			
				<div id="divEditar" class="divEditar">
				
					<% if (AnalisePendenciadt != null && !AnalisePendenciadt.getPasso1().equals("")){ %>
					<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<% } if (AnalisePendenciadt != null && !AnalisePendenciadt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
					<% } %>
					
					<fieldset class="formEdicao">
						<legend>Analisar Pendência(s)</legend>
						
						<%@ include file="DadosPendencia.jspf" %>
						<br />
							
						<fieldset>	
							<legend>Inserir Arquivo(s)</legend>
							<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
							<%@ include file="Padroes/InsercaoArquivosAssinador.jspf"%>
						</fieldset>
					
						<br />
						
						<div class="col30">
						<label class="formEdicaoLabel" for="Id_MovimentacaoTipo">*Tipo Movimentação 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacaoTipo" name="imaLocalizarMovimentacaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
						</label><br>  
						
						<input id="MovimentacaoTipo" name="MovimentacaoTipo" type="hidden" value="<%=AnalisePendenciadt.getMovimentacaoTipo()%>" />					
						<select name="Id_MovimentacaoTipo" id="Id_MovimentacaoTipo" size="1" onchange="AlterarValue('MovimentacaoTipo',this.options[this.selectedIndex].text)">
							<option value="null">--Selecione o Tipo de Movimentação--</option>
							<%
								List listaMovimentacaoTipo = AnalisePendenciadt.getListaTiposMovimentacaoConfigurado();
								for (int i=0; listaMovimentacaoTipo != null && i<listaMovimentacaoTipo.size();i++){
									UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);%>
									<%if( usuarioMovimentacaoTipoDt != null ) { %>
										<option value="<%=usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()%>" <%=AnalisePendenciadt.isMesmaMovimentacaoTipo(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo())?"selected='selected'":""%>><%=usuarioMovimentacaoTipoDt.getMovimentacaoTipo()%></option>
									<%} %>
							   <%}
				            %>
			           </select>
			           </div>
			           <div class="col30">
						
						<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
						<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />
						</label><br> 
						
						<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="30" maxlength="80" value="<%=AnalisePendenciadt.getComplementoMovimentacao()%>"/>
						
						
						</div>
						<div class="clear"></div>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
						<br />
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
		</script>
	</body>
</html>