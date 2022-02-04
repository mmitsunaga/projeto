<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BuscaProcessoDt"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>

<html>
	<head>
		<title>Classificar de Processos</title>
		
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		
		<script language='javascript' type='text/javascript'>
			function VerificarCampos() {
				with(document.Formulario) {
					var mensagemErro = 'Preencha no mínimo um dos Critérios para a Consulta (';
					
					if ($('#CpfPoloAtivo').val() == '') {
						if (document.getElementById('PoloAtivoNull').checked == false) {
							mensagemErro += 'CPF/CNPJ da Parte do Polo Ativo, ';
						}
						if ($('#CpfPoloPassivo').val() == '') {
							if (document.getElementById('PoloPassivoNull').checked == false) {
								mensagemErro += 'CPF/CNPJ da Parte do Polo Passivo, ';
							}
							if ($('#Id_ProcessoTipo').val() == '' && $('#Id_Assunto').val() == '' && $('#Id_Classificador').val() == '' && $('#MaxValor').val() == '' && $('#MinValor').val() == '') {
								mensagemErro += 'Tipo do Processo, Assunto, Classificador, Maior ou Igual, Menor ou Igual)';
								mostrarMensagemErro('Verifique os campos', mensagemErro);
								return false;
							}
						}
					}
					
					submit();
				}
			}
			
			function desabilitarPolo(obj, id) {
				var checado;
				var cor;
				
				if ($('#' + obj.value).is(':checked') == true) {
					checado = true;
					cor = '#DCDCDC';
				} else {
					checado = false;
					cor = '#ffffff';
				}
				
				$('#' + id).val("");
				$('#' + id).attr('readonly', checado);
				$('#' + id).css('background', cor);
			}
		</script>
	</head>
	
	<body>
		<%
			Integer proprios = (Integer)request.getSession().getAttribute("Proprios");
		%>
		
		<div id='divCorpo' class='divCorpo'>
			<div class='area'>
				<h2>
					&raquo;
					<%=request.getAttribute("TituloPagina")%>
				</h2>
			</div>
			
			<form id='Formulario' name='Formulario' method='post' action='BuscaProcesso'>
				<input type='hidden' id='PaginaAtual' name='PaginaAtual' value='<%=request.getAttribute("PaginaAtual")%>' />
				<input type='hidden' id='PaginaAnterior' name='PaginaAnterior' value='<%=request.getAttribute("PaginaAnterior")%>'>				
				<input type='hidden' id='tipoConsultaProcesso' name='tipoConsultaProcesso' value='5'>
				<input type='hidden' id='fluxoClassificador' name='fluxoClassificador' value='2'>
				<input type='hidden' id='PassoBusca' name='PassoBusca' value='1'>
				<input type='hidden' id='Id_Processo' name='Id_Processo' value=''>						
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">
							Classificar Processos
						</legend>
						<div align="left">
							<big style="color:#3181C3;"><b>Escolha os critérios para selecionar os processos que serão classificados</b></big>
						</div>
						<div class='col40'>
							<fieldset class='formEdicao'>
								<legend class='formEdicaoLegenda'>Polo Ativo</legend>
								
								<label class='formEdicaoLabel' for='CpfCnpjPoloAtivo'>CPF/CNPJ da Parte</label>
								<input type='text' id='CpfPoloAtivo' name='CpfPoloAtivo' title='Digite o CPF/CNPJ do Polo Ativo' size='30' maxlength='18' value='<%=buscaProcessoDt.getCpfPoloAtivo()%>' onkeyup='autoTab(this,60)' onkeypress='return DigitarSoNumero(this, event)'/>
								
								<input type='checkbox' id='PoloAtivoNull' name='PoloAtivoNull' value='PoloAtivoNull' onclick='desabilitarPolo(this, "CpfPoloAtivo")'>Vazio
							</fieldset>
						</div>
						<div class='col40'>
							<fieldset class='formEdicao'>
								<legend class='formEdicaoLegenda'>Polo Passivo</legend>
								
								<label class='formEdicaoLabel' for='CpfCnpjPoloPassivo'>CPF/CNPJ da Parte</label>
								<input type='text' id='CpfPoloPassivo' name='CpfPoloPassivo' title='Digite o CPF/CNPJ do Polo Passivo' size='30' maxlength='18' value='<%=buscaProcessoDt.getCpfPoloPassivo()%>' onkeyup='autoTab(this,60)' onkeypress='return DigitarSoNumero(this, event)'/>
								
								<input type='checkbox' id='PoloPassivoNull' name='PoloPassivoNull' value='PoloPassivoNull' onclick='desabilitarPolo(this, "CpfPoloPassivo")'>Vazio
							</fieldset>
						</div>
						<fieldset id='fieldsetDadosProcesso' class='formEdicao'> 
							<legend class="formEdicaoLegenda">Dados Processos</legend>
							
							<div class="col40">
								<label class="formEdicaoLabel" for="Id_ProcessoTipo">
									Tipo do Processo
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('fieldsetDadosProcesso','ProcessoTipo','Consulta de Tipos de Processo', 'Digite o Tipo do Processo e clique em consultar.', 'Id_ProcessoTipo', 'ProcessoTipo', ['ProcessoTipo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")"/>
									<input class="FormEdicaoimgLocalizar" name="imaLimparProcessoTipo" type="image" src="./imagens/16x16/edit-clear.png" onClick="LimparChaveEstrangeira('Id_ProcessoTipo','ProcessoTipo'); return false;" title="Limpar Tipo do Processo">
								</label>
								<br>
								<input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=buscaProcessoDt.getId_ProcessoTipo()%>">
								<input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoTipo" id="ProcessoTipo"  value="<%=buscaProcessoDt.getProcessoTipo()%>" type="text" size="67" maxlength="100" value=""/>
								<br>
							</div>
							<div class="col40">
								<label class="formEdicaoLabel" for="Id_Assunto">
									Assunto
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarAssunto" name="imaLocalizarAssunto" type="image" src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('fieldsetDadosProcesso','Assunto','Consulta de Assuntos', 'Digite o Assunto e clique em consultar.', 'Id_Assunto', 'Assunto', ['Assunto'], ['Código do Assunto'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")"/>
									<input class="FormEdicaoimgLocalizar" name="imaLimparAssunto" type="image" src="./imagens/16x16/edit-clear.png" onClick="LimparChaveEstrangeira('Id_Assunto','Assunto'); return false;" title="Limpar Assunto">
								</label>
								<br>
								<input type="hidden" name="Id_Assunto" id="Id_Assunto" value="<%=buscaProcessoDt.getId_Assunto()%>">
								<input class="formEdicaoInputSomenteLeitura" readonly name="Assunto" id="Assunto"  value="<%=buscaProcessoDt.getAssunto()%>" type="text" size="67" maxlength="100" value=""/>
								<br>
							</div>
							<div class="col40">
								<label class="formEdicaoLabel" for="Id_Classificador">
									Classificador
									<input class="FormEdicaoimgLocalizar" name="imaLocalizarClassificador" type="image" src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'Classificador', 'Consulta de Classificadores', 'Digite o Classificador e clique em consultar.', 'Id_Classificador', 'Classificador', ['Classificador'], ['Prioridade'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")"/>
									<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image" src="./imagens/16x16/edit-clear.png" onClick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">
								</label>
								<br>
								<input type="hidden" name="Id_Classificador" id="Id_Classificador" value="<%=buscaProcessoDt.getId_Classificador()%>">
								<input class="formEdicaoInputSomenteLeitura" readonly name="Classificador" id="Classificador"  value="<%=buscaProcessoDt.getClassificador()%>" type="text" size="67" maxlength="100" value=""/>
								<br>
							</div>
							<br>
							<div>
								<fieldset class="formEdicao"> 
									<legend class="formEdicaoLegenda">Valores</legend>
									<div class="col40">
										<label for="ValorMinimo">Maior ou Igual</label>
										<input name="MinValor" id="MinValor" type="text" size="30" maxlength="30" value="<%=buscaProcessoDt.getMinValor()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" title="Digite o menor valor possível"/>
									</div>
									<div class="col40">
										<label for="ValorMaximo">Menor ou Igual</label>
										<input name="MaxValor" id="MaxValor" type="text" size="30" maxlength="30" value="<%=buscaProcessoDt.getMaxValor()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)" title="Digite o maior valor possível"/>
									</div>
								</fieldset>
							</div>
						</fieldset>
						
						<% List tempList = null;
							boolean boMostrar = false;
							if (request.getAttribute("ListaProcesso")!=null) {
								tempList = (List)request.getAttribute("ListaProcesso");
								if (tempList.size()>0){
									boMostrar=true;
								}
							}
						%>
						<%if (!boMostrar){ %>
							<div class='divBotoesCentralizados'>							
								<input name='imgSubmeter' type='submit' value='Buscar' onclick='AlterarAction("Formulario", "BuscaProcesso");AlterarValue("PaginaAtual","<%=Configuracao.Localizar%>");return VerificarCampos();'>
								<input name='imgLimpar' type='submit' value='Limpar' onclick='AlterarAction("Formulario", "BuscaProcesso");AlterarValue("PaginaAtual","<%=Configuracao.Novo%>");'>								
							</div>
						<%}else{ %>
							<div class='divBotoesCentralizados'>
								<input name='imgLimpar' type='submit' value='Limpar' onclick='AlterarAction("Formulario", "BuscaProcesso");AlterarValue("PaginaAtual","<%=Configuracao.Novo%>");'>
							</div>						
							<fieldset class="formEdicao" id='fieldsetClassificador'> 
								<legend class="formEdicaoLegenda">
									Classificar para:
								</legend>
								<div align="left">
									<big style="color:#3181C3;"><b> Se não informar o classificador, os processos serão desclassificados</b></big>
								</div>
								<div>
									<label class="formEdicaoLabel" for="Id_Classificador_Alteracao">
										Classificador
										<input class="FormEdicaoimgLocalizar" name="imaLocalizarClassificadorAlteracao" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('botaoClassificar', 'Classificar');MostrarBuscaPadrao('fieldsetClassificador', 'Classificador', 'Consulta de Classificadores', 'Digite o Classificador e clique em consultar.', 'Id_Classificador_Alteracao', 'Classificador_Alteracao', ['Classificador'], ['Prioridade'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")"/>
										<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image" src="./imagens/16x16/edit-clear.png" onClick="AlterarValue('botaoClassificar', 'Desclassificar');LimparChaveEstrangeira('Id_Classificador_Alteracao','Classificador_Alteracao'); return false;" title="Limpar Classificador">
									</label>
									<input type="hidden" name="Id_Classificador_Alteracao" id="Id_Classificador_Alteracao" value="<%=buscaProcessoDt.getId_ClassificadorAlteracao()%>">
									<br>
									<input class="formEdicaoInputSomenteLeitura" readonly name="Classificador_Alteracao" id="Classificador_Alteracao" value="<%=buscaProcessoDt.getClassificadorAlteracao()%>" type="text" size="67" maxlength="100" value=""/>
								</div>
							</fieldset>											
						<%}%>						

						<%if (boMostrar){ %>
							<%@ include file="ListaProcessos.jspf"%>
							<%@ include file="Padroes/PaginacaoProcesso.jspf"%>
							<div align="left">
								<big style="color:#3181C3;"><b>ATENÇÃO.: Todos os processos serão classificados ou desclassificados</b></big>
							</div>
							<div class='divBotoesCentralizados'>
								<input name='imgSubmeter' id='botaoClassificar' type='submit' value='Desclassificar' onclick='AlterarAction("Formulario", "Classificador");AlterarValue("PaginaAtual", "<%=Configuracao.Curinga6%>");return VerificarCampos();'>															
							</div>
						<%}%>
					</fieldset>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>
