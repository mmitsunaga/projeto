<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDebitoDt"%>


<jsp:useBean id="certidaoNegativaPositivaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
	
	<title>Certid&atilde;o Negativa/Positiva</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./css/geral.css');
		
	</style>	      
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>        	    	
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>		
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			
			<form action="CertidaoNegativaPositiva" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>"/>
				<input id="InserirDados" name="InserirDados" type="hidden" value="<%=request.getAttribute("InserirDados")%>"/>
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
				<div id="divEditar" class="divEditar">
					<div id="divPortaBotoes" class="divPortaBotoes">
						<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
						<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
						<input id="imgImprimir" alt="Imprimir"  class="imgImprimir" title="Imprimir - Gerar relatorio em pdf" name="imaImprimir" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" />
					</div>
				<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				
					<label class="formEdicaoLabel" for="Numero">*Número do Requerimento:</label><br> 
	    			<input class="formEdicaoInput" name="NumeroGuia" id="NumeroGuia" type="text" maxlength="11" size="60"  onkeypress="return DigitarSoNumero(this, event)" maxlength="255" value="<%=certidaoNegativaPositivaDt.getNumeroGuia()%>" onkeyup=" autoTab(this,255)"/>	    		
	    			(Digitar apenas números)
	    			<br />	
	    			<% if (!certidaoNegativaPositivaDt.getNome().isEmpty()) {%>
	    			<fieldset class="VisualizaDados"> 
			    	<legend>Dados do Requerimento</legend>			    			
			    			<p> <b>Nome:</b> <%= certidaoNegativaPositivaDt.getNome() %> 
							<b>&nbsp;Sexo:</b> <%= certidaoNegativaPositivaDt.getSexo() %></p>
							<p><b>CPF:</b> <%= certidaoNegativaPositivaDt.getCpfCnpj()  %> &nbsp;<b>RG:</b> <%= certidaoNegativaPositivaDt.getIdentidade() %></p>
							<p><b>Mãe:</b> <%= certidaoNegativaPositivaDt.getNomeMae() %> &nbsp;<b>Data de Nascimento:</b><%= certidaoNegativaPositivaDt.getDataNascimento() %> </p>
							<p> <b>Tipo de Pessoa</b>	<%= certidaoNegativaPositivaDt.getTipoPessoa().matches("[Ff][iIíÍ][sS][iI][cC][aA]")? "Física":"Jurídica" %>&nbsp; <b>&Aacute;rea: </b> <%= certidaoNegativaPositivaDt.getAreaCodigo().equalsIgnoreCase("1") ? "Cível" :"Criminal" %></p>
							<p> <b>Profiss&atilde;o</b>: <%= certidaoNegativaPositivaDt.getProfissao() %> &nbsp;<b>Estado Civil: </b> <%= certidaoNegativaPositivaDt.getEstadoCivil() %></p>
							<p><b>Domicilio:</b>	<%= certidaoNegativaPositivaDt.getDomicilio() %> &nbsp;<b>Nacionalidade: </b> <%= certidaoNegativaPositivaDt.getNacionalidade() %></p>
							<p> <b>Comarca:</b> <%= certidaoNegativaPositivaDt.getComarca() %> &nbsp;<b> Serventia: </b><%= certidaoNegativaPositivaDt.getServentia() %> </p>
							
					</fieldset>
						<%} %>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar Processos" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"> 
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
				<p><strong>Atenção:</strong> Certifique-se que a lista de processos está correta, remova processos se necessário.</p>
				<table class="Tabela" id="TabelaArquivos">
					<thead>
						<tr>
							<th></th>
							<th width="10%">N&uacute;mero Processo</th>
							<th width="5%">Sistema</th>
							<th width="25%">Nome</th>
							<th width="20%">CPF/CNPJ</th>
							<th width="10%">Data de Nascimento</th>
							<th width="25%">Nome da Mãe</th>
							<th width="5%">Remover</th>
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
								if (!ProcessoCNPDt.getSistema().equals("SPG") && !ProcessoCNPDt.getSistema().equalsIgnoreCase("pje")) {
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
							<%
								if (processos.size() >= 1) {
									String nometmp = ProcessoCNPDt.getPromovidoNome();
									if(nometmp == null )
										nometmp = "";
								//	if(!Funcoes.equalsIgnoraAcentoCaixa(nometmp,certidaoNegativaPositivaDt.getNome())) { 
									if(true) {	//TODO: corrigir código
							%>
							
							<td align="center"><a 	href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.LocalizarAutoPai%>&amp;posicaoLista=<%=i%>">
							<img name="btnRetirar<%=i%>" id="btnRetirar<%=i%>" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" /> </a></td>
							<%
									} else {
							%>
							
							<td align="center"><a 	href="#">
							<img name="btnRetirar<%=i%>" id="btnRetirar<%=i%>" title="Não é possível remover esse processo" src="./imagens/imgExcluirPequenad.png" /> </a></td>
							<%
								}
							}
							%>
						</tr>
					</tbody>
					<%
						}
						} else {
					%>
					<tbody>
						<tr>
							<td><em>Não foram encontrados processos.</em></td>
						</tr>
					</tbody>
					<%
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