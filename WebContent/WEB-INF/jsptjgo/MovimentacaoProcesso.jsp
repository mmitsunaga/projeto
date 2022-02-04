<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="java.util.*"%>

<jsp:useBean id="Movimentacaodt" class= "br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<title>Movimentar Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>     
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
   	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	<script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
</head>

<body onLoad="atualizarArquivos();">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input name="TrocarResponsavel" type="hidden" value="<%=request.getAttribute("TrocarResponsavel")%>" />
			
			<!-- Variáveis para controlar Passos da Movimentação -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=Movimentacaodt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=Movimentacaodt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=Movimentacaodt.getPasso3()%>">
			
			<div id="divEditar" class="divEditar">	
	
				<% if (!Movimentacaodt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=Movimentacaodt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
				<% } if (!Movimentacaodt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=Movimentacaodt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (!Movimentacaodt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=Movimentacaodt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<% } %>
				<br />
			
				<fieldset class="formEdicao"> 
				    <legend class="formEdicaoLegenda">Passo 1 - Dados Movimentação </legend>
					<%	List processos = Movimentacaodt.getListaProcessos();	
						if (request.getSession().getAttribute("TodosProcessosClassificados")!=null){%>
							<div class="destaque">
								ATENÇÃO: Os <%=processos.size()%> processos que estão classificados em <%=(String)request.getSession().getAttribute("Classificado") %> serão movimentados.
							</div>
						<%}else{
						
							
							if( processos != null && processos.size() > 0 ) {
								for (int i=0;i<processos.size();i++) {
									ProcessoDt processoDt = (ProcessoDt)processos.get(i);
									if(processoDt != null) {
							%>
								<div class="destaque">
									<img class="maismenos" id="imagem<%=i%>" src="./imagens/16x16/Mais.png" onclick="MostrarOcultarDiv('corpo<%=i%>','imagem<%=i%>',1)" title="Mostrar/Ocultar Detalhes do Processo"/>
									<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a>
								</div>
							
								<div id="corpo<%=i%>" class="corpo" style="display: none;">
									<fieldset id="VisualizaDados" class="VisualizaDados">
										<legend> Dados Processo</legend>
										
										<div> Classe </div>
										<span style="width:300px;"> <%=processoDt.getProcessoTipo()%> </span><br />				
										<div> Serventia </div>
										<span> <%=processoDt.getServentia()%> </span>
										<br />
										
										<div> Data DistribuiÃ§ao</div>
										<span> <%=processoDt.getDataRecebimento()%> </span>
										
										<div> Valor </div>
										<span> <%=processoDt.getValor()%> </span>
										<br />
										
										<%@ include file="PartesProcesso.jspf"%>
									</fieldset>
								</div>
								<%	}
								} 
							}
						}%>			
					<br />										
					<%if (!Movimentacaodt.isOcultaTiposMovimentacao()) { %>	
					<div class="col60">																		
						<label class="formEdicaoLabel" for="Id_MovimentacaoTipo">*Tipo Movimentação 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacaoTipo" name="imaLocalizarMovimentacaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						</label><br>							
						
						<input id="MovimentacaoTipo" name="MovimentacaoTipo" type="hidden" value="<%=Movimentacaodt.getMovimentacaoTipo()%>" />					
						<select name="Id_MovimentacaoTipo" id="Id_MovimentacaoTipo" size="1" onchange="AlterarValue('MovimentacaoTipo',this.options[this.selectedIndex].text)">
							<option value="null">--Selecione o Tipo de Movimentação--</option>
							<%
								List listaMovimentacaoTipo = Movimentacaodt.getListaTiposMovimentacaoConfigurado();
								if (listaMovimentacaoTipo != null) {
								for (int i=0;i<listaMovimentacaoTipo.size();i++){
									UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
									if(usuarioMovimentacaoTipoDt == null)
										usuarioMovimentacaoTipoDt = new UsuarioMovimentacaoTipoDt();%>
									<option value="<%=usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()%>" <%=((Movimentacaodt.getId_MovimentacaoTipo() != null && Movimentacaodt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()))?"selected='selected'":"")%>><%=usuarioMovimentacaoTipoDt.getMovimentacaoTipo()%></option>
							   <%}
								}
				            %> 	  						   
			           </select>
		           </div>																												
					<%}%>	
					<%if (!Movimentacaodt.isOcultaComplemento()) { %>						
					<div class="col60 clear">
						<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
							<input class="FormEdicaoimgLocalizar" type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />
						</label>
						<br> 
						
						<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="30" maxlength="80" value="<%=Movimentacaodt.getComplemento()%>"/>
					</div>
					<br />
					<%}%>	

					<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
					<%@ include file="Padroes/InsercaoArquivosAssinador.jspf"%>
					
				
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');"> 
					</div>
					<br />
				</fieldset>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	

</body>

</html>