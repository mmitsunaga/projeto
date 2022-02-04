<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroGuiaLocomocaoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="mandadoJudicial" scope="session" class="br.gov.go.tj.projudi.dt.MandadoJudicialDt" />
<jsp:useBean id="mandadoJudicialStatus" scope="session" class="br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt" />

<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />

<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<link href="./css/form.css" type="text/css" rel="stylesheet" />
<link href="./css/font-awesome-4.4.0/css/font-awesome.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./js/jquery.js"></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
<script language="javascript" type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>

<script>
	function alertar() {
		if ($("#quantidadeLocomocao").val() != $("#qtdOrigLoc").val()) {
			$('#dialog').dialog({
				autoOpen : false,
				modal : true
			});
			$('#dialog').dialog({
				buttons : [ {
					text : "SIM",
					click : function() {
						confirmarAltLoc();
						$(this).dialog("close");
					}
				}, {
					text : "NÃO",
					click : function() {
						cancelarAltLoc();
						$(this).dialog("close");
					}
				} ]
			});
			$('#dialog').html(
					"Você está diminuindo a quantidade original de locomoções de <b>"
							+ $("#qtdOrigLoc").val() + "</b> para <b>"
							+ $("#quantidadeLocomocao").val()
							+ "</b>. Está certo desta alteração?");
			$('#dialog').css({
				'background-image' : 'url("imagens/32x32/ico_erro.png")',
				'background-repeat' : 'no-repeat'
			});
			$('#divCorpo').focus();
			$("#dialog").dialog('open');
			$('html, body', window.parent.document).animate({
				scrollTop : 0
			}, 'slow');
		} else {
			cancelarAltLoc();
		}
	}

	function confirmarAltLoc() {
		$("#valorLocomocao").val("");
	}

	function cancelarAltLoc() {
		var qtdOrigLoc = $("#qtdOrigLoc").val();
		$("#valorLocomocao").val($("#valorOrigLoc").val());
		$("#quantidadeLocomocao").val(qtdOrigLoc);
	}
	
	$('#divdireita .MarcarLinha').click(function(event){
		alert("123");
		// event.target
	    var jtag = $(this);
		var id1 = jtag.attr("data_id1");
		var desc1 =  jtag.attr("data_desc1");
       	var array = jtag.attr("data_descs").split(";");
       	// faço as alterações para cada desc maior que 1
       	for (i=0; i<(array.length-1);i=i+2)                   		
       		AlterarValue(array[i],array[i+1]);
       	// submeto com um click na linha
		selecionaSubmeteJSON(id1,desc1);
	});
	
	function emitirGuiaLocomocao(){
		$("#PaginaAtual").val("6");
		$("#Fluxo").val("guiaLocomocao");
		$("#idMandJud").val($("#id").val());
		$("#Formulario").submit();
	}
	
	function emitirGuiaLocomocaoComplementar(){
		$("#PaginaAtual").val("6");
		$("#Fluxo").val("guiaLocomocaoComplementar");
		$("#idMandJud").val($("#id").val());
		$("#Formulario").submit();
	}
</script>

<style>
.input-icons i {
	/*             position: absolute;  */
	
}

.input-icons {
	margin-bottom: 10px;
}

.icon {
	padding: 10px;
	min-width: 40px;
}

.input-field {
	padding: 10px;
}
</style>

</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>&raquo; Autorização de Pagamento de Mandados com Custas</h2>
		</div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input id="Fluxo" name="Fluxo" type="hidden" value="<%=request.getAttribute("Fluxo")%>" />
			<input id="idMandJud" name="idMandJud" type="hidden" value="<%=request.getAttribute("idMandJud")%>" />
			<input id="botaoAutorizar" name="botaoAutorizar" type="hidden" value="<%=request.getAttribute("botaoAutorizar")%>" />
			<input id="botaoNegar" name="botaoNegar" type="hidden" value="<%=request.getAttribute("botaoNegar")%>" />
			<input id="idMandJudPagamentoStatus" name="idMandJudPagamentoStatus" type="hidden" value="<%=request.getAttribute("idMandJudPagamentoStatus")%>" />
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />

			<%
				mandadoJudicial = (MandadoJudicialDt) request.getAttribute("mandadoJudicial");

				String statusBotaoAutorizar = "";
				String statusBotaoNegar = "";
				Boolean mostrarBotoes = true;

				if (request.getAttribute("botaoAutorizar").equals("desativar")) {
					statusBotaoAutorizar = "disabled id='botaoAutorizar'";
					mostrarBotoes = false;
				}
				if (request.getAttribute("botaoNegar").equals("desativar")) {
					statusBotaoNegar = "disabled id='botaoNegar'";
					mostrarBotoes = false;
				}
			%>
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Edita os Dados do Mandado</legend>

					<div id="divesquerda" class="col45">

						<label class="formEdicaoLabel" for="id">Número Mandado</label> <br>
						<input class="formEdicaoInputSomenteLeitura" name="id" id="id"
							type="text" readonly="readonly" size="5"
							value="<%=mandadoJudicial.getId()%>" /> <br> <label
							class="formEdicaoLabel" for="statusSomenteLeitura">Tipo
							Mandado</label> <label class="formEdicaoLabel" for="tipoMandado">Status Pagamento</label><br>
	
						<input class="formEdicaoInputSomenteLeitura" name="mandadoTipo"
							id="mandadoTipo" type="text" readonly="readonly" size="20"
							value="<%=mandadoJudicial.getMandadoTipo()%>" /> <input
							class="formEdicaoInputSomenteLeitura"
							name="mandJudPagamentoStatus" id="mandJudPagamentoStatus"
							type="text" readonly="readonly" size="20"
							value="<%=mandadoJudicial.getMandJudPagamentoStatus()%>" /><br>
	
						<label class="formEdicaoLabel" for="numeroProcesso">Número
							Processo </label> <label class="formEdicaoLabel" for="nomeOficial">Oficial
							de Justiça</label><br> <input class="formEdicaoInputSomenteLeitura"
							name="procNumero" id="procNumero" type="text" readonly="readonly"
							size="20" value="<%=mandadoJudicial.getProcNumero()%>" /> <input
							class="formEdicaoInputSomenteLeitura"
							name="nomeUsuarioServentia_1" id="nomeUsuarioServentia_1"
							readonly="readonly" type="text" size="56"
							value="<%=mandadoJudicial.getNomeUsuarioServentia_1()%>" /><br>
	
						<label class="formEdicaoLabel" for="numeroProcesso">Oficial
							Companheiro</label><br> <input class="formEdicaoInputSomenteLeitura"
							name="nomeUsuarioServentia_2" id="nomeUsuarioServentia_2"
							readonly="readonly" type="text" size="45"
							value="<%=mandadoJudicial.getNomeUsuarioServentia_2()%>" /><br>
	
						<label class="col55" for="numeroProcesso">Comarca</label> <label
							class="col20" for="numeroProcesso">Bairro</label><br> <input
							class="formEdicaoInputSomenteLeitura" name="comarca" id="comarca"
							type="text" readonly="readonly" size="45"
							value="<%=mandadoJudicial.getComarca()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="bairro" id="bairro"
							type="text" readonly="readonly" size="30"
							value="<%=mandadoJudicial.getBairro()%>" /><br> <label
							class="col55" for="zona">Zona</label> <label class="col20"
							for="nomeOficial">Regiao</label><br> <input
							class="formEdicaoInputSomenteLeitura" name="zona" id="zona"
							readonly="readonly" type="text" size="45"
							value="<%=mandadoJudicial.getZona()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="regiao" id="regiao"
							readonly="readonly" type="text" size="30"
							value="<%=mandadoJudicial.getRegiao()%>" /><br>
					
							
							
						<label class="col65" for="usuarioAutorizacao">Usuário Status Pagamento</label> <label class="col15"
							for="data">Data</label><br> <input
							class="formEdicaoInputSomenteLeitura"
							name="nomeUsuUltStatusOrdemPag" id="nomeUsuUltStatusOrdemPag"
							type="text" readonly="readonly" size="54"
							value="<%=mandadoJudicial.getNomeUsuPagamentoStatus()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="dataUltStatusOrdemPag"
							id="dataUltStatusOrdemPag" type="text" readonly="readonly"
							size="20" value="<%=mandadoJudicial.getDataPagamentoStatus()%>" /><br>
	
						<label class="col65" for="usuarioEnvio">Usuário Envio de Créditos Diretoria Financeira</label>
						<label class="col15" for="dataEnvio">Data</label><br>
	
						<input class="formEdicaoInputSomenteLeitura"
							name="nomeUsuEnvioOrdemPag" id="nomeUsuEnvioOrdemPag" type="text"
							readonly="readonly" size="54"
							value="<%=mandadoJudicial.getNomeUsuPagamentoEnvio()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="dataEnvioOrdemPag"
							id="dataEnvioOrdemPag" type="text" readonly="readonly" size="20"
							value="<%=mandadoJudicial.getDataPagamentoEnvio()%>" /><br>
						<br>
							
							
							
							
							 <label
							class="col15" for="UsuarioAutoriçãcao">Locomoções</label> <label
							class="col15" for="Data">Valor</label><br>
	
						<%
							if (mandadoJudicial.getIdMandJudPagamentoStatus()
									.equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_PENDENTE) ) { 						 
						%>
	
	
						<!-- 					<select id="quantidadeLocomocao" name="quantidadeLocomocao" class="col10" onchange="alertar()"> -->
						<%-- 						<%for(int i = Funcoes.StringToInt(mandadoJudicial.getQuantidadeLocomocao()); i > 0; i--) { %> --%>
						<%-- 							<option value="<%=i%>"><%= i %></option> --%>
						<%-- 						<%} %> --%>
						<!-- 					</select> -->
	
	
	
						<select id="quantidadeLocomocao" name="quantidadeLocomocao"
							class="col10" onchange="alertar()">
							<%
								for (int i = Funcoes.StringToInt((String) request.getAttribute("quantidadeLocomocao")); i > 0; i--) {
							%>
							<option value="<%=i%>"><%=i%></option>
							<%
								}
							%>
						</select> <input id="qtdOrigLoc" name="qtdOrigLoc" type="hidden"
							value="<%=mandadoJudicial.getQuantidadeLocomocao()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="valorLocomocao"
							id="valorLocomocao" type="text" readonly="readonly" size="20"
							value="<%=mandadoJudicial.getValorLocomocao()%>" /><br /> <input
							type="hidden" id="valorOrigLoc"
							value="<%=mandadoJudicial.getValorLocomocao()%>" />
	
	
						<%
							} else {
						%>
	
						<input class="formEdicaoInputSomenteLeitura" name="locomocao"
							id="locomocao" type="text" readonly="readonly" size="16"
							value="<%=mandadoJudicial.getQuantidadeLocomocao()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="valor" id="valor"
							type="text" readonly="readonly" size="20"
							value="<%=mandadoJudicial.getValorLocomocao()%>" /><br>
	
						<%
							}
						%>		
												
						
						<br>
						
						<label class="formEdicaoLabel" for="tipoArquivo">Status Mandado</label><br> 
					
					
						<%
						
						String confirmarSalvar = (String) request.getAttribute("confirmarSalvar");
						if (mandadoJudicial.getIdMandJudPagamentoStatus()
								.equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_PENDENTE)) { 	
									
									 
						%>					
						
						
									
					
					<select id="idMandJudStatus" name ="idMandJudStatus" class="formEdicaoCombo" >
					<%
					List listTemp = (List) request.getAttribute("listaStatusMandado");
					if (listTemp != null) {
							%>
						 	<option value="" ></option>  
							<%
							MandadoJudicialStatusDt mandJudStatus;
							for (int i = 0; i < listTemp.size(); i++) {
								mandJudStatus = (MandadoJudicialStatusDt) listTemp.get(i);
							%>					
							<option value="<%=mandJudStatus.getId()%>"
								
							<%
							 
						
							if(mandJudStatus.getId() != null &&  mandJudStatus.getId().equals(mandadoJudicial.getId_MandadoJudicialStatus())){ 
								
								 
							%>
					 		 selected="selected"
							<% 
							} 
							%>								 
					        >
							<%=mandJudStatus.getMandadoJudicialStatus()%> 
							</option>
							<%
							}
					}
					%>		
					</select><br />	
					
					<%
							} else {
						%>
					
					<input class="formEdicaoInputSomenteLeitura" name="statusMandado"
							id="statusMandado" type="text" readonly="readonly" size="25"
							value="<%=mandadoJudicial.getMandadoJudicialStatus()%>" />
											
						<%} %>
						
	                <br><br> 
	
	
					</div>					
					
			        <%		
					if (mandadoJudicial.getIdMandJudPagamentoStatus().equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO) ||
						mandadoJudicial.getIdMandJudPagamentoStatus().equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_ENVIADO)) { %>
					
			    		<div id="divdireita"  class="coluna40" Style="float:right">
					    	<a href="foo" onclick="emitirGuiaLocomocao();return false;" Style="float:right">Emitir Guia Locomoção</a>
					    	<br><br>
					    	<a href="foo" onclick="emitirGuiaLocomocaoComplementar();return false;" Style="float:right">Emitir Guia Locomoção Complementar</a>
				    	</div>
					
					<%} %>					
					
					<!-- 
					<div id="divdireita"  class="col59">
					
							    <fieldset id="VisualizaDados">
									<legend>
										Listagem de Locomoções Adicionadas
										<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>'); MostrarBuscaPadrao('sub_tip766561','GuiaLocomocao','Consulta de Bairro', 'Digite o bairro e clique em consultar.', 'Id_Bairro', 'Bairro', ['Bairro','Cidade', 'UF'], ['Cidade', 'UF'], '2', '15'); return false;" />
									</legend>
									
									<table id="TabelaLocomocoes" class="Tabela">
					       				<thead>
					           				<tr class="TituloColuna">
					               				<td width="15%">Quantidade</td>
					                 			<td width="25%">Bairro</td>
					                 			<td width="20%">Cidade</td>
					                 			<td width="5%">UF</td>
					                 			<td width="30%">Finalidade</td>	 	                 			
					                 			<td class="colunaMinima"></td>				                  			
					              			</tr>			               			
					          			</thead>
						   				<tbody id="tabListaLocomocoes">
											<%					
											List liTemp = (List)request.getSession().getAttribute("ListaBairroLocomocao");
											List listaQuantidadeBairroDt = (List)request.getSession().getAttribute("ListaBairroLocomocaoQtde");
						                	if( liTemp != null && liTemp.size() > 0 && listaQuantidadeBairroDt != null && listaQuantidadeBairroDt.size() == liTemp.size()) {
							                	for( int i = 0; i < liTemp.size(); i++ ) {
							                		BairroGuiaLocomocaoDt bairroAuxDt = (BairroGuiaLocomocaoDt)liTemp.get(i);
													%>
													<tr>
														<td width="15%" align="center">
															<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao<%=i %>" name="quantidadeLocomocao<%=i %>" value="<%=listaQuantidadeBairroDt.get(i)%>" size="1" />
															<input type="button" id="somarQuantidade<%=i%>" name="somarQuantidade<%=i%>" value="+" onclick="somarQuantidade(quantidadeLocomocao<%=i %>);" />
															<input type="button" id="subtrairQuantidade<%=i%>" name="subtrairQuantidade<%=i%>" value="-" onclick="subtrairQuantidade(quantidadeLocomocao<%=i %>,'1');" />
														</td>
														<td width="25%" align="left">
															<%= bairroAuxDt.getBairroDt().getBairro() %>
														</td>
														<td width="20%" align="left">
															<%= bairroAuxDt.getBairroDt().getCidade() %>
														</td>
														<td width="5%" align="left">
															<%= bairroAuxDt.getBairroDt().getUf() %>
														</td>	
														<td width="30%" align="left">
															<%= GuiaEmissaoNe.getTextoFinalidade(Funcoes.StringToInt(bairroAuxDt.getFinalidade())) %> (<%= (bairroAuxDt.isOficialCompanheiro() ? "Sim" : "Não") %>|<%= (bairroAuxDt.isPenhora() ? "Sim" : "Não") %>|<%= (bairroAuxDt.isIntimacao() ? "Sim" : "Não") %>)
														</td>														
														<td align="center">
															<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga6%>');AlterarValue('posicaoListaBairroExcluir','<%=i%>')" title="Excluir esta Locomoção" />
														</td>
													</tr>							
													<%
												}
						                	}
						                	else {
						                		%>
						                		<tr>
						                			<td colspan="5">                			
						                				<em> Insira uma Locomoção para um Bairro. </em>
						                			</td>
						                		</tr>
						                		<%
						                	}
											%>
							    		</tbody>
									</table>					
								</fieldset>
				                
							-->
					
<%-- 						HELLENO --%>
<%-- 								<div id="sub_tip=parteDt.getId()" class="DivInvisivel">				  		 --%>
<!-- 				  			<h3> Tipificação / Assunto </h3> -->
<%-- 				  			<input type='hidden' id='id_processo_parte' name='id_processo_parte' value='=parteDt.getId()' /> --%>
<!-- 				  			<input id='Id_Assunto' name='Id_Assunto' type='hidden' value='' /> -->
<%-- 							<input class="formEdicaoInputSomenteLeitura" id="Assunto"  name="Assunto" type="hidden"  value=''  onclick="salvarPasso('ProcessoParteAssunto','sub_tip=parteDt.getId()',undefined,undefined)" />					 --%>
<%-- 							<input type='hidden' id='Id_Processo' name='Id_Processo' value='=parteDt.getId_Processo()' /> --%>
<%-- 							<input type='hidden' id='Id_ProcessoParte' name='Id_ProcessoParte' value='=parteDt.getId()' /> --%>
<!-- 							<div class="col100"> -->
<%-- 								<table  id="tb_sub_tip=parteDt.getId()" class='Tabela' > --%>
<!-- 								    <thead> -->
<!-- 								      <tr> -->
<!-- 								      	<th class="colunaMinima" >Id</th> -->
<!-- 								        <th width='120px' >Assunto</th> -->
<!-- 								        <th width='60px' >Dispositivo Legal</th> -->
<!-- 								        <th width='60px' >Artigo</th> -->
<!-- 								        <th width='60px' >Data Inclusão</th> -->
<!-- 								        <th width='1px' ></th> -->
<!-- 								        <th class="colunaMinima" >Excluir</th>  -->
<!-- 								      </tr> -->
<!-- 								    </thead> -->
<!-- 								    <tbody> -->
<!-- 								    </tbody> -->
<!-- 								 </table> -->
<!-- 							</div> -->
<!-- 							<br />						 -->
<%-- 							<button type="button" title="Clique para Incluir a Tipificação / Assunto"	onclick="MostrarBuscaPadrao('sub_tip=parteDt.getId()','ProcessoParteAssunto','Consulta de Assunto', 'Digite o Assunto e clique em consultar.', 'Id_Assunto', 'Assunto', ['Assunto','Código CNJ'], ['Dispositivo Legal', 'Artigo'], '=(AssuntoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)', '=Configuracao.TamanhoRetornoConsulta'); return false;" > Incluir Outra Tipificação </button>								 --%>
<!-- 						</div> -->


<!-- 					</div> -->


					<div id="divbaixo" class="col100">
					
						<%if(mostrarBotoes){ %>
						<div id="divPortaBotoes" class="divBotoesCentralizados">
			
							<button <%=statusBotaoAutorizar%>
								title="Autorizar Pagamento"
								onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');
								             AlterarValue('Fluxo','alteraStatusOrdemPg');
								             AlterarValue('idMandJudPagamentoStatus','<%=MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO%>');
								             AlterarValue('idMandJud','<%=mandadoJudicial.getId()%>');"
								value="Autorizar Pagamento" name="botaoAutorizar"> Autorizar Pagamento </button>&nbsp;
								
								<button <%=statusBotaoNegar%>
								title="Negar Pagamento"
								onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');
								             AlterarValue('Fluxo','alteraStatusOrdemPg');
								             AlterarValue('idMandJudPagamentoStatus','<%=MandadoJudicialDt.ID_PAGAMENTO_NEGADO%>'); 
								             AlterarValue('idMandJud','<%=mandadoJudicial.getId()%>');"
								value="Negar Pagamento" name="botaoDesautorizar"> Negar Pagamento </button>
						</div>
						<%} %>
					
							<%
							if (request.getAttribute("htmlCertidao") != null) {							
							    String[] htmlCertidao = (String[]) request.getAttribute("htmlCertidao");
								int contador = 1;
								for (String htmlCert: htmlCertidao) {
									if (!htmlCert.equalsIgnoreCase("") && htmlCert.contains("</a>")) {
							%>
									<br/>
									<%=htmlCert%>
									<br/>
									
							<%		} else if (!htmlCert.equalsIgnoreCase("")) { %>
							
									<br> <br>
									<fieldset>
			
										<legend>
											<input class="FormEdicaoimgLocalizar"
												name="imaLocalizarArquivoTipo" type="image"
												src="./imagens/imgEditorTextoPequena.png"
												onclick="MostrarOcultar('divTextoEditor'); return false;"
												title="Texto (Esconder/Mostrar)" /> Texto Certidão <%= contador %>
			
										</legend>
										<div id="divTextoEditor" class="divTextoEditor"
											style="display: block">
			
											<%=htmlCert%>
										</fieldset>
							<%
										contador++;	
									}
								}
							}
							%>
							
						
					
					</div>
			</div>

			</fieldset>
	</div>
	<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
	</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>


</html>
