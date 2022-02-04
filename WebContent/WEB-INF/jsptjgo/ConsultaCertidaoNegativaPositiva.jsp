<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDebitoDt"%>

<jsp:useBean id="certidaoNegativaPositivaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt"/>

<html>
	<head>
		<title>Certid&atilde;o Negativa/Positiva - Estado</title>
		
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
			@import url('./css/geral.css');		
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');	
		</style>
			
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	   	<script type="text/javascript" src="./js/tabelas.js"></script>
		<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
		<%@ include file="js/buscarArquivos.js"%>
		
		
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>		  
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>	
		<script type="text/javascript">
			$(document).ready(function(){
				$("#DataNascimento").mask("99/99/9999");				
			});
		</script>		
		<%@ include file="./js/Paginacao.js"%>		
	</head>
	
	<body>
	  <div  id="divCorpo" class="divCorpo">		         
		 <div class="area"><h2>&raquo; Certid&atilde;o Negativa/Positiva - Estado</h2></div>		 			
			<form action="ConsultaCertidaoNegativaPositiva" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
				<div id="divEditar" class="divEditar">					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				    	
<!--				    	<label class="formEdicaoLabel" for="label_Comarca">*Comarca</label><br>  -->
<!--			    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','2')" >  -->
<!--			    		<input class="FormEdicaoimgLocalizar" id="imaLimparComarca" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" > -->
<!--			    		<input  name='Id_Comarca' id='Id_Comarca' type='hidden'  value='<%=certidaoNegativaPositivaDt.getId_Comarca()%>'> -->
<!--			    		<input class="formEdicaoInputSomenteLeitura"  readonly name="Comarca" id="Comarca" type="text" size="36" maxlength="60" value="<%=certidaoNegativaPositivaDt.getComarca()%>"/>						-->
<!--						<br />-->
						
						<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
		    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=certidaoNegativaPositivaDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
		    			<br />
											
						<label class="formEdicaoLabel" for="Cpf">*CPF</label><br> 
		    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="11" value="<%=certidaoNegativaPositivaDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF do requerente."/>
		    			<label for="Aviso" style="float:left;margin-left:25px;">(digitar somente números, sem pontos ou hífen)</label><br>  			
		    			<br />
		    			
						<label class="formEdicaoLabel" for="NomeMae">*Nome da M&atilde;e</label><br> 
		    			<input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="60" maxlength="255" value="<%=certidaoNegativaPositivaDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
		    			<br />	    			
		    			
		    			<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label><br> 
						<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=certidaoNegativaPositivaDt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
						<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/>
		    			<br />						
							
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar Processos" onclick="AlterarValue('PaginaAtual','2')">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','4');"> 
						</div>
					</fieldset>
					
					<fieldset class="formEdicao">
					<legend class ="formEdicaoLegenda">Lista de Processos</legend>
					<%
						
						List processos = null;
						if(certidaoNegativaPositivaDt !=null)
						processos = certidaoNegativaPositivaDt.getListaProcessos();
					%>
									<%
						if (processos != null && processos.size() > 0) {
							%>				
				<table class="Tabela" id="TabelaArquivos">
					<thead>
						<tr>
							<th></th>
							<th width="10%">N&uacute;mero Processo</th>
							<th width="5%">Sistema</th>														
							<th width="20%">Nome</th>
							<th width="10%">CPF/CNPJ</th>
							<th width="10%">Data de Nascimento</th>
							<th width="15%">Nome da Mãe</th>
							<th width="15%">Serventia</th>
							<th width="15%">Comarca</th>							
						</tr>
					</thead>
					<%
					String numeroProcesso = "";
						for (int i = 0; i < processos.size(); i++) {
							ProcessoCertidaoPositivaNegativaDt ProcessoCNPDt = (ProcessoCertidaoPositivaNegativaDt) processos.get(i);
							if(ProcessoCNPDt.getSistema().equals("SPG")) {
								numeroProcesso = ((String[])ProcessoCNPDt.getProcessoNumeroDigito().split("\\("))[0];
							} else {
								 numeroProcesso = ProcessoCNPDt.getProcessoNumeroCompleto();
							}
					%>
					<tbody>
						<tr class="primeiraLinha">
							<%
								if (!ProcessoCNPDt.getSistema().equals("SPG") ) {
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
											<%
											String nome = ProcessoCNPDt instanceof ProcessoCertidaoPositivaNegativaDebitoDt ?((ProcessoCertidaoPositivaNegativaDebitoDt) ProcessoCNPDt).getParteAverbacaoNome() : ProcessoCNPDt.getPromovidoNome();
											String nomeMae =ProcessoCNPDt instanceof ProcessoCertidaoPositivaNegativaDebitoDt  ?((ProcessoCertidaoPositivaNegativaDebitoDt)  ProcessoCNPDt).getParteAverbcaoNomeMae() : ProcessoCNPDt.getPromovidoNomeMae();
											String dataNascimento = ProcessoCNPDt instanceof ProcessoCertidaoPositivaNegativaDebitoDt  ? ((ProcessoCertidaoPositivaNegativaDebitoDt) ProcessoCNPDt).getParteAverbacaoDataNascimento() : ProcessoCNPDt.getPromovidoDataNascimento();
											String cpf = ProcessoCNPDt instanceof ProcessoCertidaoPositivaNegativaDebitoDt ?((ProcessoCertidaoPositivaNegativaDebitoDt)  ProcessoCNPDt).getParteAverbacaoCPF() : ProcessoCNPDt.getCpfCnpjFormatado();
											String serventia = ProcessoCNPDt instanceof ProcessoCertidaoPositivaNegativaDebitoDt ?((ProcessoCertidaoPositivaNegativaDebitoDt)  ProcessoCNPDt).getServentia() : ProcessoCNPDt.getServentia();
											String comarca = ProcessoCNPDt instanceof ProcessoCertidaoPositivaNegativaDebitoDt ?((ProcessoCertidaoPositivaNegativaDebitoDt)  ProcessoCNPDt).getComarca() : ProcessoCNPDt.getComarca();											
											%>
							 				<%=nome%>
							
							</td>
							<td align="center">							
							 <% 
							 	//String cpf = ProcessoCNPDt.getCpfCnpjFormatado();
							 %>
							 <%=cpf == null || cpf.isEmpty() ? "-" : cpf %> 
							</td>
							<td align="center">
							<%= dataNascimento == null || dataNascimento.isEmpty() ? "-": dataNascimento %>
							</td>
							<td align="center">
							<%=nomeMae == null || nomeMae.isEmpty() ? "-": nomeMae %>
							</td>
							<td align="center">
							<%=serventia == null || serventia.isEmpty() ? "-": serventia %>
							</td>
							<td align="center">
							<%=comarca == null || comarca.isEmpty() ? "-": comarca %>
							</td>							
						</tr>
					</tbody>
					<%
						}
						} 
					%>					
					</table>
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
				<% if (certidaoNegativaPositivaDt.getTexto() != null && !certidaoNegativaPositivaDt.getTexto().equals("")) {%>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Certidão</legend>
						<div id="divTextoEditor" class="divTextoEditor">
							<%=certidaoNegativaPositivaDt.getTexto() %>
						</div>
					
				</fieldset>
				<%} %>				
				</div>			
			</form>			
		</div>
	</body>
</html>