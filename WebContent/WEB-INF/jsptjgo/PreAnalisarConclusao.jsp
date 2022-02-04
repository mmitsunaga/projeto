<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="java.util.*"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<title>Pré-Analisar Autos Conclusos</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	      
</head>

<body onload="fechar('sim');atualizarPendencias();">
	<div id="divCorpo" class="divCorpo" style="min-height: 1700px;">
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina") %> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />					
				<input id="pendencia" name="pendencia" type="hidden" value="">
				<input id="CodigoPendencia" name="CodigoPendencia" type="hidden" value="">
				<input id="SalvarRedistribuir" name="SalvarRedistribuir" type="hidden" value="">
				<!-- Variáveis para controlar Passos da Pré-Análise -->
				<input id="Passo1" name="Passo1" type="hidden" value="<%=AnalisePendenciadt.getPasso1()%>">
				<input id="Passo2" name="Passo2" type="hidden" value="<%=AnalisePendenciadt.getPasso2()%>">
				<input id="Passo3" name="Passo3" type="hidden" value="<%=AnalisePendenciadt.getPasso3()%>">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend><%=request.getAttribute("TituloPagina") %></legend>
						<div style="max-height:165px; overflow:auto; margin-bottom:10px">
						<%@ include file="DadosConclusao.jspf" %>
						</div>			
						<fieldset> 
							<legend>Redigir Pré-Análise</legend>
								<!--  Redigir texto da pré-analise usando Editor de Modelos -->
								<%@page import="br.gov.go.tj.utils.Configuracao"%>
								<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
								<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
							
								
								
								<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
								<script type='text/javascript' src='./js/Funcoes.js'></script>
								<script type='text/javascript' src='./js/DivFlutuante.js'></script>
								<script type='text/javascript' src='./js/Mensagens.js'></script>
							
								<input type="hidden" id="textoEditor" name="textoEditor">
								<input type="hidden" id="id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo")%>">
								
								<label class="formEdicaoLabel"> Tipo de Arquivo
								<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao  + Configuracao.Localizar)%>');" title="Selecionar Tipo de Arquivo">
								<input class="FormEdicaoimgLocalizar" name="imaLimparArquivoTipo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" title="Limpar Tipo de Arquivo" />
								</label><br>
								
								<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" id="arquivoTipo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("ArquivoTipo")%>">		
								<br />
											
								<input type="hidden" id="id_Modelo" name="Id_Modelo" value="<%=request.getAttribute("Id_Modelo")%>" />
								<label class="formEdicaoLabel"> Modelo 
								<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecionar Modelo de Arquivo">
								<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_Modelo', 'modelo'); return false;" title="Limpar Modelo">
								</label><br>
								
								<input class="formEdicaoInputSomenteLeitura" name="Modelo" id="modelo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("Modelo")%>">		
								<br />
								<label class="formEdicaoLabel"> Nome Arquivo
								<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeArquivo', ''); return false;" title="Limpar nome do arquivo" />
								</label><br>
								
								<input id="nomeArquivo" name="nomeArquivo" type="text" size="54" maxlength="255" value="<%=AnalisePendenciadt.getNomeArquivo()%>"/>
								<br /><br />
							
								<label class="formEdicaoLabel">Editor Texto
									<input class="FormEdicaoimgLocalizar" name="imaLocalizarEditorTexto" type="image"  src="./imagens/imgEditorTextoPequena.png" onclick="MostrarOcultar('Editor'); return false;" title="Abrir Editor de Texto" />
								</label><br />
								<div id="Editor" class="Editor">        			
									<textarea class="ckeditor" cols="90" id="editor1" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>
									<div id="divBotoesCentralizados" class="divBotoesCentralizados">
										<input name="imgSalvarParcial" type="submit" value="Salvar Texto Parcial" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','3');"> 
									</div>
								</div>
								<fieldset class="formEdicao"> 
									<legend>Selecionar Tipo de Movimentação</legend>					
										<label class="formEdicaoLabel" for="Id_MovimentacaoTipo">*Tipo Movimentação 
										<input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacaoTipo" name="imaLocalizarMovimentacaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
										</label><br>  
						
										<input id="MovimentacaoTipo" name="MovimentacaoTipo" type="hidden" value="<%=AnalisePendenciadt.getMovimentacaoTipo()%>" />					
										<select name="Id_MovimentacaoTipo" id="Id_MovimentacaoTipo" size="1" onchange="AlterarValue('MovimentacaoTipo',this.options[this.selectedIndex].text)">
											<option value="null">--Selecione o Tipo de Movimentação--</option>
											<%	
												List listaMovimentacaoTipo = AnalisePendenciadt.getListaTiposMovimentacaoConfigurado();
												if (listaMovimentacaoTipo != null) {
												for (int i=0;i<listaMovimentacaoTipo.size();i++){
													UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
													if(usuarioMovimentacaoTipoDt == null)
														usuarioMovimentacaoTipoDt = new UsuarioMovimentacaoTipoDt();%>
													<option value="<%=usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()%>" <%=((AnalisePendenciadt.getId_MovimentacaoTipo() != null && AnalisePendenciadt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()))?"selected='selected'":"")%>><%=usuarioMovimentacaoTipoDt.getMovimentacaoTipo()%></option>
											   <%
												}
												}
								            %> 	  						   
						           		</select>					
										<br />
										
										<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
										<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />						
										</label><br />
										<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="30" maxlength="80" value="<%=AnalisePendenciadt.getComplementoMovimentacao()%>"/>
						
										<br />
										<% String segundoGrau = (String) request.getAttribute("SegundoGrau"); %>
										<%if(segundoGrau != null && segundoGrau.equalsIgnoreCase("true")){ %>
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
										
								</fieldset>
					
								<fieldset class="formEdicao">
									<legend class="formEdicaoLegenda">Pendências a Gerar </legend>
										<input type="hidden" name="pendenciaTipo" id="pendenciaTipo">
										<div id="DivPendenciaTipo" class="coluna30"> 
											<label class="formEdicaoLabel"> Tipo de Pendência </label> 	
											
											<select name="codPendenciaTipo" id="codPendenciaTipo" size="1" onChange="preencherDestinatarios();">
												<option value="-1">--Selecione o Tipo de Pendência-- </option>
												<%
													List listaPendenciaTipo = AnalisePendenciadt.getListaPendenciaTipos();
													if (listaPendenciaTipo != null && listaPendenciaTipo.size() > 0){
														for (int i=0;i < listaPendenciaTipo.size();i++){
															PendenciaTipoDt pendenciaTipoDt = (PendenciaTipoDt)listaPendenciaTipo.get(i);
												%>
												<option <%=((request.getSession().getAttribute("PendenciaTipoCodigo") != null && request.getSession().getAttribute("PendenciaTipoCodigo").equals(pendenciaTipoDt.getPendenciaTipoCodigo()))?"selected":"") %> value="<%=pendenciaTipoDt.getPendenciaTipoCodigo()%>"><%=pendenciaTipoDt.getPendenciaTipo()%></option>
												<%		
														}
													}
												%> 	  						   
											</select>
										</div>
										<div id='divmostrarExpedicao' style="display:none;" > 
											<div class='coluna20' ><input class="formEdicaoInput" type="checkbox"  value="true" onChange="mostrarModeloIntimacaoCitacao();" name="expedicaoAutomatica" id="expedicaoAutomatica"/>Expedição Automática</div>
											<div  class='coluna20' id="divOpcoesExpedicaoAutomatica" style="display: none;"></div>
										</div>
										<%@ include file="PendenciasGerar.jspf"%>
								</fieldset>	
								<fieldset class="formEdicao">
									<legend class="formEdicaoLegenda">Classificador</legend>
										<label class="formEdicaoLabel" for="Id_Classificador">Classificador </label>  
										<input type="hidden" name="Id_Classificador" id="Id_Classificador" />  
										<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificaodr" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
										<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador', 'Classificador'); return false;" title="Limpar Classificador" />
										<input class="formEdicaoInputSomenteLeitura" readonly name="Classificador" id="Classificador" type="text" size="50" maxlength="60" value="<%=AnalisePendenciadt.getClassificador()%>"/><br />
								</fieldset>		
								<div id="divBotoesCentralizados" class="divBotoesCentralizados">
									<input name="imgConcluir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');" />
									<%if (UsuarioSessao.isPodeExibirPendenciaAssinatura(AnalisePendenciadt.isMultipla(), Funcoes.StringToInt(AnalisePendenciadt.getId_PendenciaTipo()))){%>									
										<input name="imgGuardar" type="submit" value="Guardar para assinar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','2');" />
									<%}%>
									<%if (UsuarioSessao.isPodeTrocarResponsavelDistribuicao()){ %>
										<input name="imgDistribuirConclusao" type="submit" value="Salvar e Distribuir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>'); AlterarValue('SalvarRedistribuir','true'); AlterarValue('pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('CodigoPendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getHash()%>');" />										
									<%}%>
									<% if (UsuarioSessao.getVerificaPermissao(3946)){ %>						
										<input name="imgDevolverConclusao" type="submit" value="Devolver" alt='Devolve a conclusão para o assessor' title='Devolve a conclusão para o assessor' onclick="AlterarAction('Formulario','PendenciaResponsavel'), AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');  AlterarValue('pendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getId()%>');AlterarValue('CodigoPendencia','<%=AnalisePendenciadt.getPrimeiraPendenciaListaFechar().getHash()%>');" />
									<%}%>
								</div>								
							</fieldset>
						</fieldset>
					</div>	
			</form>
 			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
		<script type="text/javascript">
		$(document).ready(function()  {
			preencherDestinatarios();
		 });
	
			function fechar(valor){
				if (valor!=null){
					$('.corpo').hide();
				}
			}
			
			$(document).ready(function() {
		    	calcularTamanhoIframe();	    	 	    	
		    });
		</script>
	</body>
</html>