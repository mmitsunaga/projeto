<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAntecedenteCriminalDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoAntecedenteCriminalDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="certidaoAntecedenteCriminalDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoAntecedenteCriminalDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
	
	<title>Certid&atilde;o/Informa&ccedil;&atilde;o de Antecedentes Criminais / Menor</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	    
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type="text/javascript">
			function validarDigitacao(){
				if (document.getElementById("bloquearCampos").value=="true"){
					$('.bloquear').prop('disabled', true);
				}
			}
			
		</script>	
</head>
	
	<body onload="validarDigitacao()">
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			
			
			<form action="CertidaoAntecedenteCriminal" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input name="bloquearCampos" id="bloquearCampos" type="hidden" value="<%=request.getAttribute("bloquearCampos")%>" />
				<input name="impressaoTelaConsultaCertidao" id="impressaoTelaConsultaCertidao" type="hidden" value="true" />
				
				<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
				
			    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o/Informa&ccedil;&atilde;o</legend>
					
					<div class="col45">
					<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
	    			<input class="formEdicaoInput bloquear" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=certidaoAntecedenteCriminalDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
	    			</div>
	    			
	    			<div class="col10">
	    			<label class="formEdicaoLabel" for="Sexo">Sexo</label><br> 
			   		<select name="Sexo" id="Sexo" class="formEdicaoCombo bloquear" >
						<option <%=certidaoAntecedenteCriminalDt.getSexo().equals("")?  "selected" :"" %> value="Branco"></option>
						<option <%=certidaoAntecedenteCriminalDt.getSexo().equals("F")? "selected" :""%> value="F">F</option>
						<option <%=certidaoAntecedenteCriminalDt.getSexo().equals("M")? "selected" :""%> value="M">M</option>
					</select>
	    			</div>
	    			
	    			<div class="col30">				
					<label class="formEdicaoLabel" for="Cpf">CPF/CNPJ</label><br> 
	    			<input class="formEdicaoInput bloquear" name="Cpf" id="Cpf"  type="text" size="30" maxlength="18" value="<%=certidaoAntecedenteCriminalDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF/CNPJ do requerente."/>
	    			</div>
	    			
	    			<div class="col45">
					<label class="formEdicaoLabel" for="NomeMae">Nome da M&atilde;e</label><br> 
	    			<input class="formEdicaoInput bloquear" name="NomeMae" id="NomeMae" type="text" size="60" maxlength="255" value="<%=certidaoAntecedenteCriminalDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
	    			</div>
	    			
	    			<div class="col30">				
					<label class="formEdicaoLabel" for="Rg">RG</label><br> 
	    			<input class="formEdicaoInput bloquear" name="Rg" id="Rg"  type="text" size="30" maxlength="20" value="<%=certidaoAntecedenteCriminalDt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o Rg do requerente."/>
	    			</div>
	    			
					
					<div class="col45">
	    			<label class="formEdicaoLabel" for="NomePai">Nome do Pai</label><br> 
	    			<input class="formEdicaoInput bloquear" name="NomePai" id="NomePai" type="text" size="60" maxlength="255" value="<%=certidaoAntecedenteCriminalDt.getNomePai()%>" onkeyup=" autoTab(this,255)"/>
	    			</div>
					
					<div class="col20">    			
	    			<label class="formEdicaoLabel" for="Id_EstadoCivil">Estado Civil
	    			<input class="FormEdicaoimgLocalizar bloquear" id="imaLocalizarEstadoCivil" name="imaLocalizarEstadoCivil" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					<input class="FormEdicaoimgLocalizar bloquear" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_EstadoCivil','EstadoCivil'); return false;" title="Limpar Estado Civil">  
	    			</label><br>  
					<input type="hidden" name="Id_EstadoCivil" id="Id_EstadoCivil">  
					
					<input class="formEdicaoInputSomenteLeitura bloquear"  readonly name="EstadoCivil" id="EstadoCivil" type="text" size="20" maxlength="20" value="<%=certidaoAntecedenteCriminalDt.getEstadoCivil()%>"/>					
					</div>
	    			
	    			<div class="col45 clear">
	    			<label class="formEdicaoLabel" for="DataNascimento">Data de Nascimento</label><br> 
					<input class="formEdicaoInput bloquear" name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=certidaoAntecedenteCriminalDt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
	    			<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/><br />
	    			</div>
	    			
					<div class="col45">
					<label class="formEdicaoLabel" for="Id_Profissao">Profiss&atilde;o
					<input class="FormEdicaoimgLocalizar bloquear" id="imaLocalizarProfissao" name="imaLocalizarProfissao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					<input class="FormEdicaoimgLocalizar bloquear" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Profissao','Profissao'); return false;" title="Limpar Profissão">  
					</label><br>  
					<input type="hidden" name="Id_Profissao" id="Id_Profissao">  
					
	    			<input class="formEdicaoInputSomenteLeitura bloquear"  readonly name="Profissao" id="Profissao" type="text" size="50" maxlength="50" value="<%=certidaoAntecedenteCriminalDt.getProfissao()%>"/>
					</div>
					    
					<div class="col45 clear">
					<label class="formEdicaoLabel" for="Id_Naturalidade">Naturalidade
					<input class="FormEdicaoimgLocalizar bloquear" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					<input class="FormEdicaoimgLocalizar bloquear" name="imaLimparNaturalidade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Naturalidade','Naturalidade'); return false;" title="Limpar Naturalidade">  
					</label><br>
					<input type="hidden" name="Id_Naturalidade" id="Id_Naturalidade" value="<%=certidaoAntecedenteCriminalDt.getId_Naturalidade()%>">
					<input class="formEdicaoInputSomenteLeitura bloquear"  readonly name="Naturalidade" id="Naturalidade" type="text" size="50" maxlength="60" value="<%=certidaoAntecedenteCriminalDt.getNaturalidade()%>">
					</div>
						
					<div class="col45">
					<label class="formEdicaoLabel" for="Nacionalidade">Nacionalidade</label><br> 
	    			<input class="formEdicaoInput bloquear" name="Nacionalidade" id="Nacionalidade" type="text" size="60" maxlength="255" value="<%=certidaoAntecedenteCriminalDt.getNacionalidade()%>" onkeyup=" autoTab(this,255)" />
	    			</div>
	    			
	    			<br>
	    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
	    			<%if(request.getSession().getAttribute("exibirCheckMenorInfrator") != null) { %>
		    			<input class="formEdicaoInput bloquear" type="checkbox" id="chkMenorInfrator" name="chkMenorInfrator" title="Marque se o antecedente for para menor infrator." value="true" <%if (certidaoAntecedenteCriminalDt.getChkMenorInfrator() != null && certidaoAntecedenteCriminalDt.getChkMenorInfrator().equalsIgnoreCase("true")){%> checked<%}%> /> Informação de menor infrator
						<br />	
					<%} %>
						
						<input id="imgSubmeter" name="imgSubmeter" type="submit" value="Consultar Processos" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
						<input id="imgLimpar" name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"> 
					</div>
				</fieldset>
				
				<fieldset class="formEdicao">
					<legend class ="formEdicaoLegenda">Lista de Processos</legend>
					<%
						
						List processos = null;
						if(certidaoAntecedenteCriminalDt !=null)
						processos = certidaoAntecedenteCriminalDt.getListaProcesso();
					%>
									<%
						if (processos != null && processos.size() > 0) {
							%>
				<p><font color="red"><strong>Atenção: Certifique-se que a lista de processos está correta, marque os processos que devem constar na certidão/informação e clique no botão abaixo para gerar a certidão/informação.</font></strong></p>
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input id="imgImprimir" name="imgImprimir" alt="Imprimir" type="submit" value="Imprimir Certidão/Informação" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
				</div>
				<table class="Tabela" id="TabelaArquivos">
					<thead>
						<tr>
							<th></th>
							<th width="10%">N&uacute;mero Processo</th>
							<th width="5%">Sistema</th>
							<th width="25%">Nome</th>
							<th width="10%">Data de Nascimento</th>
							<th width="25%">Nome da Mãe</th>
							<th width="20%">CPF/CNPJ</th>
							<th width="5%">Incluir</th>
						</tr>
					</thead>
					<%
					String numeroProcesso = "";
						for (int i = 0; i < processos.size(); i++) {
							ProcessoAntecedenteCriminalDt ProcessoCNPDt = (ProcessoAntecedenteCriminalDt) processos.get(i);
					%>
					<tbody>
						<%
							if(ProcessoCNPDt.isProcessoFisico()) {
												numeroProcesso = ((String[])ProcessoCNPDt.getProcessoNumeroDigito().split("\\("))[0];
											} else {
												 numeroProcesso = ProcessoCNPDt.getProcessoNumeroCompleto();
											}
						%>
						<tr class="primeiraLinha">
							<%
								if (!ProcessoCNPDt.isProcessoFisico() ) {
							%>
							<td align="center"><%=i + 1%></td>
							<td align="center"><a
								href="BuscaProcesso?Id_Processo=<%=ProcessoCNPDt.getId_Processo()%>"><%=numeroProcesso%></a>
							</td>
							<%
								} else {
							%>
									<td align="center"><%=i + 1%></td>
									<td align="center"><b><%=numeroProcesso%></b>
									</td>
							<%
								}
							%>
							<td align="center">
							 				
							 				<%=ProcessoCNPDt.getSistema()%>
							
							</td>
							<td align="center">
							 				<%=ProcessoCNPDt.getPromovidoNome()%>
							
							</td>
							<td align="center">
							<%
								String dataNascimento = ProcessoCNPDt.getPromovidoDataNascimento();
							%>
							<%=dataNascimento == null || dataNascimento.trim().isEmpty() ? "-": ProcessoCNPDt.isProcessoFisico() ? dataNascimento : Funcoes.FormatarDataHora(dataNascimento).split(" ")[0]%>
							</td>
							<td align="center">
							<%String nomeMae = ProcessoCNPDt.getPromovidoNomeMae();%>
							<%=nomeMae == null || nomeMae.trim().isEmpty() ? "-": nomeMae %>
							</td>
							<td align="center">
							 <% 
							 	String cpf = ProcessoCNPDt.isPessoaJuridica() ? Funcoes.formataCNPJ(ProcessoCNPDt.getPromovidoCnpj()) : Funcoes.formataCPF(ProcessoCNPDt.getPromovidoCpf());
							 %>
							 <%=cpf == null || cpf.trim().equals("0")  || cpf.trim().equals("000.000.000-00") ? "-" : cpf %> 
							</td>
							<%
								if (processos.size() >= 1) {
							%>
							<td align="center">
								<input class="formEdicaoCheckBox" name="listaProcessosCertidao" type="checkbox" value="<%=numeroProcesso%>">
							</td>
							<%
								}
							%>
						</tr>
					</tbody>
					<%
						}
						} else if(certidaoAntecedenteCriminalDt.getNome() != null && !certidaoAntecedenteCriminalDt.getNome().equals("")){
					%>
					<tbody>
						<tr>
							<td>
								<font color="red"><strong>Não foram encontrados processos para os dados informados. Clique no botão abaixo para gerar a certidão/informação.</strong></font>
							</td>
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgImprimir" alt="Imprimir" type="submit" value="Imprimir Certidão/Informação" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
							</div>
						</tr>
					</tbody>
					<%
						}
					%>
					</table>
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
				</div>			
			</form>
		</div>
	</body>
</html>