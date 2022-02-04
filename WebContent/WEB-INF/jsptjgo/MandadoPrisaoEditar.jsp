<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PrisaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>

<jsp:useBean id="MandadoPrisaodt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoDt"/>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />

	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
   	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>

	<script language="javascript" type="text/javascript">
		
		$(document).ready(
				function() {
			if (<%=request.getAttribute("bloquearEdicao")%>){
				$('#divBloqueio > input').attr('disabled', true);
				$('#divBloqueio > select').attr('disabled', true);
			}
			

			if ($("#DataEmissao").val() == "") { $("#data1").hide();} 
			if ($("#DataExpedicao").val() == "") { $("#data2").hide();}
			if ($("#DataImpressao").val() == "") { $("#data3").hide();}
			if ($("#DataCumprimento").val() == "") { $("#data4").hide();}
			if ($("#MandadoPrisaoStatus").val() == "") { $("#status").hide();}
		});
	
		function verificaValorOrigem(componente){
			if (componente.value == <%=MandadoPrisaoOrigemDt.OUTRO%>){ //OPÇÃO: "OUTRO"
				document.getElementById('divDescricaoOrigem').style.display = "block";
			} else 
				document.getElementById('divDescricaoOrigem').style.display = "none";
		}

		function verificaValorPrisaoTipo(componente){
			if (componente.value == <%=PrisaoTipoDt.TEMPORARIA%>){ //OPÇÃO: "PRISÃO TEMPORÁRIA"
				document.getElementById('divPrisaoTipo').style.display = "block";
			} else 
				document.getElementById('divPrisaoTipo').style.display = "none";
		}

		
		function Verificar() {
			with(document.Formulario) {
				if (SeNulo(Id_ProcessoParte, "Informe o Promovido!")) return false;
				if (Ano.value == "" && Mes.value == "" && Dia.value == ""){ 
					alert("Informe a pena imposta!");
					return false;
				}
			}
			return true;
		}

		function calculaValidadeMandadoJSON(){
			if (!Verificar()) return false;
			
			var tempoAno = document.getElementById('Ano').value;
			var tempoMes = document.getElementById('Mes').value;
			var tempoDia = document.getElementById('Dia').value;
			var idProcessoParte = document.getElementById('Id_ProcessoParte').value;
			
			$.ajax({
				url:'MandadoPrisao?PaginaAtual=-1&tempFluxo1=1&tempoAno='+tempoAno+'&tempoMes='+tempoMes+'&tempoDia='+tempoDia+'&idProcessoParte='+idProcessoParte,
				context: document.body,
				timeout: 300000, async: true,
				success: function(retorno){
					var inLinha=1;			
					var totalPaginas =0;
					$('#DataValidade').val(retorno.DataValidade); 
				},
				error: function(data){
					  alert(data +  ' erro');
				} 
			}); // fim do .ajax*/				     
		}

		function confirma(mensagem, PaginaAtual, tempFluxo1) {
			if (confirm(mensagem)) {
				if (PaginaAtual != ''){
					AlterarValue('PaginaAtual', PaginaAtual);
					AlterarValue('tempFluxo1', tempFluxo1);
					submit();
				}
			} else
				return false;
		}
		
		function formataNumeroOrigem(v){
		    v=v.replace(/\D/g,"");//Remove tudo o que não é dígito
		    v=v.replace(/(\d)(\d{4})$/,"$1/$2");//coloca o barra antes do 11º digito
		    return v;
		}
		
		
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;Cadastro de Mandado de Prisao</h2></div>
		<form action="MandadoPrisao" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="bloquearEdicao" name="bloquearEdicao" type="hidden" value="<%=request.getAttribute("bloquearEdicao")%>">

			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />

			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Cadastrar Mandado de Prisão" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Lista todos os Mandados de Prisão do Processo" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
				<%if (request.getAttribute("mostarImgVisualizaMandado") != null && request.getAttribute("mostarImgVisualizaMandado").equals("true")){ %>
				<input id="imgArquivo" alt="Visualizar Mandado de Prisão" class="imgAtualizar" title="Visualizar Mandado de Prisão" name="imgArquivo" type="image"  
					src="./imagens/22x22/ico_arquivos.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','3');" />
				<%} %>
				<%if (request.getAttribute("mostarImgExcluir") != null && request.getAttribute("mostarImgExcluir").equals("true")){ %>
				<input id="imgExcluir" alt="Excluir Mandado de Prisão" title="Excluir Mandado de Prisão" name="imgExcluir" type="image"  
					src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');" />
				<%} %>
				<%if (request.getAttribute("mostarImgImprimir") != null && request.getAttribute("mostarImgImprimir").equals("true")){ %>
				<input id="imgImprimir" alt="Imprimir Mandado de Prisão" class="imgAtualizar" title="Imprimir Mandado de Prisão" name="imgImprimir" type="image"  
					src="./imagens/22x22/btn_pdf.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');" />
				<%} %>
			</div>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			<div id="divEditar" class="divEditar">

				<!--INÍCIO DADOS DO PROCESSO-->
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend> Processo </legend>
					<div> N&uacute;mero</div>
					<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span>
					<div> Área</div>
					<span class="span"> <%=processoDt.getArea()%></span><br />
					<div></div>
					<span></span>
					<div> Classe</div>
					<span class="span"> <%=processoDt.getProcessoTipo()%></span><br />
				</fieldset>
				<!--FIM DADOS DO PROCESSO-->

				<!--INÍCIO DADOS MANDADO DE PRISÃO-->
				<fieldset id="formEdicao" class="formEdicao">
					<legend>Dados do Mandado de Prisão</legend>

					<label class="formEdicaoLabel" for="NumeroMandado" style="width:250px">Número do Mandado</label><br>
					<%if (MandadoPrisaodt.getMandadoPrisaoNumero().length() > 0){ %>
					<span class="span"><b>&nbsp; &nbsp;<%=processoDt.getProcessoNumeroCompleto() + "-" + MandadoPrisaodt.getMandadoPrisaoNumero()%></b></span><br />
					<%} %>
					<br /><br />

				<div id="divBloqueio">
					<label class="formEdicaoLabel" for="Sigilo" style="width:250px">Mandado Sigiloso</label><br>    

				    <select name="Sigilo" <% if (request.getAttribute("usuarioTipo").toString().equalsIgnoreCase("serventia")){%> disabled="disabled" <%} %>>
				    	<option value="true" <%if (MandadoPrisaodt.isSigilo()){%>selected<%}%>>Sim</option>
				    	<option value="false" <%if (!MandadoPrisaodt.isSigilo()){%>selected<%}%>>Não</option>
				    </select>
					<br />
					<label class="formEdicaoLabel" for="ProcessoParte" style="width:250px">*Promovido</label><br>
				    <select name="Id_ProcessoParte" id="Id_ProcessoParte">
					    <option value=""></option>
						<%
							if (processoDt.getListaPolosPassivos() != null){
						%> 
							<%
 								for (int i=0; i<processoDt.getListaPolosPassivos().size(); i++) {
 							%>					
				    	<option value="<%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(i)).getId_ProcessoParte()%>" <%if (MandadoPrisaodt.getId_ProcessoParte().equals(((ProcessoParteDt)processoDt.getListaPolosPassivos().get(i)).getId_ProcessoParte())){%>selected<%}%>><%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(i)).getNome()%></option>
							<%} %>
						<%}%>
				    </select>
					<br />
					<label class="formEdicaoLabel" for="Assunto" style="width:250px">*Assunto do Processo</label><br>
				    <select name="Id_Assunto">
						<%if (processoDt.getListaAssuntos() != null){%>
					    <option value=""></option> 
							<%for (int i=0; i<processoDt.getListaAssuntos().size(); i++) {%>					
				    	<option value="<%=((ProcessoAssuntoDt)processoDt.getListaAssuntos().get(i)).getId_Assunto()%>" <%if (MandadoPrisaodt.getId_Assunto().equals(((ProcessoAssuntoDt)processoDt.getListaAssuntos().get(i)).getId_Assunto())){%>selected<%}%>><%=((ProcessoAssuntoDt)processoDt.getListaAssuntos().get(i)).getAssunto()%></option>
							<%} %>
						<%}%>
				    </select>
					<br />
					<label class="formEdicaoLabel" for="MandadoPrisaoOrigem" style="width:250px">*Procedimento de Origem</label><br>
				    <select name="MandadoPrisaoOrigemCodigo" onchange="verificaValorOrigem(this);" >
					    <option value=""></option>
						<%if (MandadoPrisaodt.getListaMandadoPrisaoOrigem() != null){%> 
							<%for (int i=0; i<MandadoPrisaodt.getListaMandadoPrisaoOrigem().size(); i++) {%>					
				    	<option value="<%=((MandadoPrisaoOrigemDt)MandadoPrisaodt.getListaMandadoPrisaoOrigem().get(i)).getMandadoPrisaoOrigemCodigo()%>" <%if (MandadoPrisaodt.getMandadoPrisaoOrigemCodigo().equals(((MandadoPrisaoOrigemDt)MandadoPrisaodt.getListaMandadoPrisaoOrigem().get(i)).getMandadoPrisaoOrigemCodigo())){%>selected<%}%>><%=((MandadoPrisaoOrigemDt)MandadoPrisaodt.getListaMandadoPrisaoOrigem().get(i)).getMandadoPrisaoOrigem()%></option>
							<%} %>
						<%}%>
				    </select>
					<br />
					<label class="formEdicaoLabel" for="NumeroDocumentoOrigem" style="width:250px">*Número do Procedimento de Origem</label><br>    
					<input class="formEdicaoInput" name="NumeroDocumentoOrigem" id="NumeroDocumentoOrigem" type="text" size="22" maxlength="20" value="<%=MandadoPrisaodt.getNumeroOrigem()%>"
						onkeypress="mascara1(this, formataNumeroOrigem)" onBlur="preencheZeros(this,13)"	/>

					<div id="divDescricaoOrigem" style="<%=request.getAttribute("displayOrigem")%>;" >
						<br />
						<label class="formEdicaoLabel" for="DescricaoOrigem" style="width:270px">*Procedimento de Origem</label><br>    
					    <select name="DescricaoOrigem">
					    	<option value="Representação de Prisão Preventiva" <%if (MandadoPrisaodt.getOrigem().equalsIgnoreCase("Representação de Prisão Preventiva")){%>selected<%}%>>Representação de Prisão Preventiva</option>
					    	<option value="Representação de Prisão Temporária" <%if (MandadoPrisaodt.getOrigem().equalsIgnoreCase("Representação de Prisão Temporária")){%>selected<%}%>>Representação de Prisão Temporária</option>
					    	<option value="Representação para Quebra de Sigilo" <%if (MandadoPrisaodt.getOrigem().equalsIgnoreCase("Representação para Quebra de Sigilo")){%>selected<%}%>>Representação para Quebra de Sigilo</option>
					    	<option value="Guia de Recolhimento" <%if (MandadoPrisaodt.getOrigem().equalsIgnoreCase("Guia de Recolhimento")){%>selected<%}%>>Guia de Recolhimento</option>
					    	<option value="Procedimento Administrativo" <%if (MandadoPrisaodt.getOrigem().equalsIgnoreCase("Procedimento Administrativo")){%>selected<%}%>>Procedimento Administrativo</option>
					    </select>
						<br />
					</div>
					<br />
					<label class="formEdicaoLabel" for="PrisaoTipo" style="width:250px">*Tipo de Prisão</label><br>
				    <select name="PrisaoTipoCodigo" onchange="verificaValorPrisaoTipo(this);">
					    <option value=""></option>
						<%if (MandadoPrisaodt.getListaPrisaoTipo() != null){%> 
							<%for (int i=0; i<MandadoPrisaodt.getListaPrisaoTipo().size(); i++) {%>					
				    	<option value="<%=((PrisaoTipoDt)MandadoPrisaodt.getListaPrisaoTipo().get(i)).getPrisaoTipoCodigo()%>" <%if (MandadoPrisaodt.getPrisaoTipoCodigo().equals(((PrisaoTipoDt)MandadoPrisaodt.getListaPrisaoTipo().get(i)).getPrisaoTipoCodigo())){%>selected<%}%>><%=((PrisaoTipoDt)MandadoPrisaodt.getListaPrisaoTipo().get(i)).getPrisaoTipo()%></option>
							<%} %>
						<%}%>
				    </select>
			
					<div id="divPrisaoTipo" style="<%=request.getAttribute("displayPrisaoTipo")%>;" >
						<br />
						<label class="formEdicaoLabel" for="PrazoPrisao" style="width:270px">*Prazo da Prisão</label><br>    
						<input class="formEdicaoInput" name="PrazoPrisao" id="PrazoPrisao" type="text" size="4" maxlength="4" value="<%=MandadoPrisaodt.getPrazoPrisao()%>" onkeypress="return DigitarSoNumero(this, event)"/> (dias)
					</div>
					<br />
					<label class="formEdicaoLabel" for="LocalRecolhimento" style="width:250px">Local do Recolhimento</label><br>    
					<input class="formEdicaoInput" name="LocalRecolhimento" id="LocalRecolhimento" type="text" size="62" maxlength="60" value="<%=MandadoPrisaodt.getLocalRecolhimento()%>"/>
					<br />

					<label class="formEdicaoLabel" for="Regime" style="width:250px">*Regime</label><br>
				    <select name="Id_Regime">
					    <option value=""></option>
						<%if (MandadoPrisaodt.getListaRegime() != null){%> 
							<%for (int i=0; i<MandadoPrisaodt.getListaRegime().size(); i++) {%>					
				    	<option value="<%=((RegimeExecucaoDt)MandadoPrisaodt.getListaRegime().get(i)).getId()%>" <%if (MandadoPrisaodt.getId_RegimeExecucao().equals(((RegimeExecucaoDt)MandadoPrisaodt.getListaRegime().get(i)).getId())){%>selected<%}%>><%=((RegimeExecucaoDt)MandadoPrisaodt.getListaRegime().get(i)).getRegimeExecucao()%></option>
							<%} %>
						<%}%>
				    </select>
					<br />
					<label class="formEdicaoLabel" for="TempoPena" style="width:250px">*Pena imposta:</label><br>
					<input class="formEdicaoInput" name="Ano" id="Ano" type="text" size="4" maxlength="4" value="<%=MandadoPrisaodt.getTempoPenaAno()%>"/>
					<input class="formEdicaoInput" name="Mes" id="Mes" type="text" size="4" maxlength="4" value="<%=MandadoPrisaodt.getTempoPenaMes()%>"/>
					<input class="formEdicaoInput" name="Dia" id="Dia" type="text" size="4" maxlength="4" value="<%=MandadoPrisaodt.getTempoPenaDia()%>"/>(anos/meses/dias)
					<br />
					<label class="formEdicaoLabel" for="ValorFianca" style="width:250px">Valor da Fiança R$</label><br>    
					<input class="formEdicaoInput" name="ValorFianca" id="ValorFianca" type="text" size="22" maxlength="20" value="<%=MandadoPrisaodt.getValorFianca()%>" onkeyup="MascaraValor(this);" onkeypress="return DigitarSoNumero(this, event)"/>
					<br />
					<br />
					<label class="formEdicaoLabel" for="DataValidadeLabel" style="width:350px">*Data limite para cumprimento do mandado</label><br>    
				    <input class="formEdicaoInput" name="DataValidade" id="DataValidade"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataValidade()%>" onkeypress="return formataCampo(event, this, 10)"> 
		   	    	<input class="FormEdicaoimgLocalizar" id="imaCalcular" name="imaCalcular" type="image" src="./imagens/imgAtualizarPequena.png" 
							onclick="calculaValidadeMandadoJSON(); return false;" title="Calcula a validade do mandado com base na DATA ATUAL.">
					<br /><br />
					<div id="data1">
					<label class="formEdicaoLabel" for="DataEmissao" style="width:250px">Data de Emissão do Mandado</label><br>    
				    <input class="formEdicaoInputSomenteLeitura" readonly name="DataEmissao" id="DataEmissao"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataEmissao()%>" onkeypress="return formataCampo(event, this, 10)"> 
					<br /> </div>
					<div id="data2">
					<label class="formEdicaoLabel" for="DataExpedicao" style="width:250px">Data de Expedição do Mandado</label><br>    
				    <input class="formEdicaoInputSomenteLeitura" readonly name="DataExpedicao" id="DataExpedicao"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataExpedicao()%>" onkeypress="return formataCampo(event, this, 10)"> 
					<br /></div>
					<div id="data3">
					<label class="formEdicaoLabel" for="DataImpressao" style="width:250px">Data de Impressão do Mandado</label><br>    
				    <input class="formEdicaoInputSomenteLeitura" readonly name="DataImpressao" id="DataImpressao"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataImpressao()%>" onkeypress="return formataCampo(event, this, 10)"> 
					<br /></div>
					<div id="data4">
					<label class="formEdicaoLabel" for="DataCumprimento" style="width:250px">Data do Cumprimento do Mandado</label><br>    
				    <input class="formEdicaoInputSomenteLeitura" readonly name="DataCumprimento" id="DataCumprimento"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataCumprimento()%>" onkeypress="return formataCampo(event, this, 10)"> 
					<br /></div>
					<div id="status">
					<label class="formEdicaoLabel" for="MandadoPrisaoStatus" style="width:250px">Status do Mandado de Prisão</label><br>
					<input name="MandadoPrisaoStatusCodigo" id="MandadoPrisaoStatusCodigo" type="hidden" size="60" maxlength="60" value="<%=MandadoPrisaodt.getMandadoPrisaoStatusCodigo()%>"/>
					<input class="formEdicaoInputSomenteLeitura" readonly name="MandadoPrisaoStatus" id="MandadoPrisaoStatus" type="text" size="60" maxlength="60" value="<%=MandadoPrisaodt.getMandadoPrisaoStatus()%>"/>
					<br /></div>
					
					<label class="formEdicaoLabel" for="SinteseDecisao" style="width:250px">*Síntese da Decisão:</label><br>
					<textarea name="SinteseDecisao" id="SinteseDecisao" cols=110 rows=15 value="<%=MandadoPrisaodt.getSinteseDecisao()%>"><%=MandadoPrisaodt.getSinteseDecisao()%></textarea>
					<br />
					

					<div id="data1">
					<label class="formEdicaoLabel" for="DataEmissao" style="width:250px">Data de Atualização do Mandado</label><br>    
				    <input class="formEdicaoInputSomenteLeitura" readonly name="Atualizacao" id="Atualizacao"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataAtualizacao()%>" onkeypress="return formataCampo(event, this, 10)">

				</div>
				</fieldset>		
				<!--FIM DADOS MANDADO DE PRISÃO-->


				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<%if (request.getAttribute("mostarBotaoExpedir") != null && request.getAttribute("mostarBotaoExpedir").equals("true")){ %>
					<button type="submit" name="operacao" value="Expedir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('tempFluxo1','4');"  >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Expedir Mandado de Prisão"  align="center"/> -->
						Expedir
					</button>
				<%} %>
				<%if (request.getAttribute("mostarBotaoEmitir") != null && request.getAttribute("mostarBotaoEmitir").equals("true")){ %>
					<button type="submit" name="operacao" value="Emitir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('tempFluxo1','1');" >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Emitir Mandado de Prisão" /> -->
						Emitir
					</button>
				<%} %>
				<%if (request.getAttribute("mostarBotaoFinalizar") != null && request.getAttribute("mostarBotaoFinalizar").equals("true")){ %>

					<button type="submit" name="operacao" value="Cumprir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('tempFluxo1','1');"  >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Cumprir Mandado de Prisão" /> -->
						Cumprir
					</button>
					<% if (request.getAttribute("usuarioTipo").toString().equalsIgnoreCase("juiz")){%>
					<button type="submit" name="operacao" value="Revogar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('tempFluxo1','2');"  >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Revogar Mandado de Prisão" /> -->
						Revogar
					</button>
						<% if (MandadoPrisaodt.isSigilo()){%>
					<button type="submit" name="operacao" value="RetirarSigilo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('tempFluxo1','3');"  >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Retirar Caráter Sigiloso" /> -->
						Retirar Caráter Sigiloso
					</button>
						<%} %>
					<%} %>
				<%} %>
				<br /><br /><br />
				</div>

			</div>
		<%if(request.getAttribute("Imprimir") != null && String.valueOf(request.getAttribute("Imprimir")).equalsIgnoreCase("true")){%>
			 <script type="text/javascript">			 	
			 	var form = document.getElementById("Formulario");
			 	AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');
			 	AlterarValue('bloquearEdicao','true');
			 	AlterarValue('tempFluxo1','4');
				form.submit();	
			 </script>    		  
		<%}%>		
		</form>
	</div>
			<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
