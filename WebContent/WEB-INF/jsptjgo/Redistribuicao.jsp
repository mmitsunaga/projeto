<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="Redistribuicaodt" class= "br.gov.go.tj.projudi.dt.RedistribuicaoDt" scope="session"/>


<html>
<head>
	<title>Redistribuição de Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   	
   	<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>	
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	
	 <script type="text/javascript"  language="javascript"    >

	 	$(document).ready(function() {
	 		var varCheck = $("input[name=opcaoRedistribuicao]:checked");
	 		mostrarOpcao('a'+ varCheck.val());
		});
	 	function mostrarOpcao(obj){
			Ocultar('a1');
			Ocultar('a2');
			Ocultar('a3');			
			Mostrar(obj);
		}
	 	
	 </script>
</head>

	<body >
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("TituloPagina")%>" />
				<input name="ProcessoTipo" type="hidden" value="<%=request.getAttribute("ProcessoTipo")%>" />
						
				<div id="divEditar" class="divEditar">																
						<fieldset>
						<%
						List processos = Redistribuicaodt.getListaProcessos();
						boolean mostrarColunaRepasse = false;
						for (int i=0;i<processos.size();i++) {
							ProcessoDt processoDt = (ProcessoDt)processos.get(i);
							//Possui algum processo de primeiro grau? Então mostra coluna de porcentagem de repasse
							if( !processoDt.isSegundoGrau() && !new ProcessoNe().isProcessoJuizadosTurmas(processoDt.getId(), null) ) {
								mostrarColunaRepasse = true;
								break;
							}
						}
						%>
						<table class="Tabela" id="TabelaArquivos">
							<thead>
								<tr>
									<th></th>
									<th>N&uacute;mero Processo</th>
									<th>Data de Distribuição</th>
									<th>Serventia</th>
									<% if( mostrarColunaRepasse ) {%>
										<th>Porcentagem Repasse *</th>
									<%} %>
									<% if (processos != null && processos.size() > 1){ %>
										<th></th>
									<%} %>
								</tr>
							</thead>
							<tbody>
							<%
							if (processos != null){
								for (int i=0;i<processos.size();i++) {
									ProcessoDt processoDt = (ProcessoDt)processos.get(i);
								%>
									<tr class="primeiraLinha">
										<td align="center"><%=i+1%></td>
										<td width="15%" align="center"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></td>
										<td width="15%" align="center"><%=processoDt.getDataRecebimento()%></td>
										<td><%=processoDt.getServentia()%></td>
										
										<%if(mostrarColunaRepasse) { %>
											<td align="center">
											
												<!--
												- Ocorrência 2018/13263
												- Alterada a redistribuição para resolver os casos dos processos digitalizados. Os processos digitalizados irão abrir o menu para escolher o percentual de repasse.
												 -->
												<%	
												String variavelAuxiliar = null;
												if( request.getSession().getAttribute("PorcentagemProcesso"+processoDt.getId()) != null ) {
													variavelAuxiliar = request.getSession().getAttribute("PorcentagemProcesso"+processoDt.getId()).toString();
												}
												if( variavelAuxiliar == null ) {
													variavelAuxiliar = "";
												}
												%>
												
												<input name="validarMenuPorcentagem" type="hidden" value="SIM" />
												
							    				<select class="formEdicaoInput" name="PorcentagemProcesso<%=processoDt.getId()%>" id="PorcentagemProcesso<%=processoDt.getId()%>" >
								    				<option value="">Informe a Porcentagem de Repasse</option>
								    				<option value="0" <%=(variavelAuxiliar != null && variavelAuxiliar.equals("0"))?"selected":""%>>0</option>
								    				<option value="50" <%=(variavelAuxiliar != null && variavelAuxiliar.equals("50"))?"selected":""%>>50</option>
								    				<option value="100" <%=(variavelAuxiliar != null && variavelAuxiliar.equals("100"))?"selected":""%>>100</option>
								    			</select>
								    			
								    			<em>%</em>
								    			
											</td>
										<%} %>
										<% if (processos != null && processos.size() > 1){ %>
				      					<td>
				      						<a href="Redistribuicao?PaginaAtual=<%=Configuracao.Excluir%>&Id_Processo=<%=processoDt.getId()%>&posicao=<%=i%>">
				      						<img name="btnRetirar" id="btnRetirar" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" />
				      						</a>
				      					</td>
				      					<% } %>
									</tr>
				        	<% 	}
							} 
							else { %>
								<tr>
									<td><em>Selecione processo(s) para redistribuição.</em></td>			  	  							    
							    </tr>
							<% } %>
							</tbody>
						</table>		
						
						<label for="Aviso" style="float:left;margin-left:5px;font-size:14px;color:#000;">Escolha uma das opções abaixo para a redistribuição.</label>	
                        </br>
                            <div id='opcoes'>																							
                                <input type="radio" name="opcaoRedistribuicao" value="1" <%=Redistribuicaodt.isOpcaoRedistribuicao("1")?"checked":""%> onChange="mostrarOpcao('a1')" /> Redistribuição Normal  
                                <input type="radio" name="opcaoRedistribuicao" value="2" <%=Redistribuicaodt.isOpcaoRedistribuicao("2")?"checked":""%> onChange="mostrarOpcao('a2')" /> Redistribuição por Prevenção/Conexão  
                                <input type="radio" name="opcaoRedistribuicao" value="3" <%=Redistribuicaodt.isOpcaoRedistribuicao("3")?"checked":""%> onChange="mostrarOpcao('a3')" /> Redistribuição Direcionada  
                             </div>                            
						<fieldset class="formEdicao" id='a1'>						
                                <label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Área Distribuição
                                <input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('opcaoRedistribuicao','1');AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
                                </label><br>  
                                <input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="80" maxlength="100" <%if (Redistribuicaodt.isOpcaoRedistribuicao("1")) {%>value="<%=Redistribuicaodt.getAreaDistribuicao()%>"<%}%>/>
                            <label>
                            <input name="verificarPrevensao" type="checkbox" id="verificarPrevensao" <%=request.getSession().getAttribute("verificarPrevensao").toString().equals("true")?"checked":""%> onChange="mudarEstado('<%=request.getAttribute("tempRetorno")%>','3','1','verificarPrevensao')" />
                            Verificar Prevenção/Conexão</label>
                                <br />
			    		</fieldset>
				    		
						<%
						ProcessoDt processoDt = null;
						if (processos != null && processos.size() > 0) {
							processoDt = (ProcessoDt)processos.get(0);
						}
						
						if (processoDt != null && processoDt.getProcessoTipoCodigo() != null && !processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){%>
				    	<fieldset class="formEdicao" id='a2'>
							
							<label class="formEdicaoLabel" for="ProcessoNumeroDependente">*Processo Principal</label><br> 
				    		<input class="formEdicaoInput" name="ProcessoNumeroDependente" id="ProcessoNumeroDependente" type="text" size="30" maxlength="25" value="<%=Redistribuicaodt.getProcessoNumeroDependente()%>" onKeyUp="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onKeyPress="return DigitarNumeroProcesso(this, event)"/>
				    		<span><small><em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em></small></span><br />
							
						</fieldset>
						
						<fieldset class="formEdicao" id='a3'>
							
							<label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Área Distribuição
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('opcaoRedistribuicao','3');AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
			    			</label><br>  
			    			<input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="80" maxlength="100" <%if ((Redistribuicaodt.getOpcaoRedistribuicao() != null && Redistribuicaodt.getOpcaoRedistribuicao().equals("3"))) {%>value="<%=Redistribuicaodt.getAreaDistribuicao()%>"<%}%>/><br />
			    			
			    			<label class="formEdicaoLabel" for="Id_Serventia">*Serventia
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('opcaoRedistribuicao','3');AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
			    			</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" readonly name="Serventia" id="Serventia" type="text" size="80" maxlength="100" value="<%=Redistribuicaodt.getServentia()%>"/><br />
			    			
			    			<label class="formEdicaoLabel" for="Id_Responsavel">Responsável
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarResponsavel" name="imaLocalizarResponsavel" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('opcaoRedistribuicao','3');AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
			    			</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" readonly name="Responsavel" id="Responsavel" type="text" size="80" maxlength="100" value="<%=Redistribuicaodt.getServentiaCargo()%>"/><br />
						</fieldset>
						<%if (!processoDt.isSegundoGrau()) { %>
		    			<fieldset class="formEdicao">
		    				<legend>Porcentagem Repasse *</legend>
		    				<font color="red">* O índice de repasse é previsto na Consolidação dos Atos Normativos ( Anexo II Regimento de Custas, Emolumentos e Taxa Judiciária e dos Tributos - Tabela III Atos dos escrivães do cível em geral - Notas Genéricas, artigo 3º )</font>
		    			</fieldset>
		    			<%} %>
						<%} %>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onClick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');" /> 
						</div>
					
					
					<%if ((request.getAttribute("opcaoRedistribuicao") != null && request.getAttribute("opcaoRedistribuicao").equals("1"))) {%><%=Redistribuicaodt.getAreaDistribuicao()%><%} %>
					
					<%if ((request.getAttribute("MensagemErro") == null ||  request.getAttribute("MensagemErro").equals(""))
							&& request.getAttribute("PaginaAnterior") != null && request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
    				<div id="divSalvar" class="divSalvar" class="divsalvar">
        				<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onClick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')"  /> <br />
        				<div class="divMensagemsalvar">Confirma a redistribuição do(s) processo(s)?</div>
      				</div>
 					<% } %>
 					</fieldset>
				</div>
				
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>