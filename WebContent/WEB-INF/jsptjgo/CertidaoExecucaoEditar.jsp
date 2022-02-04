<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteSinalDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>

<jsp:useBean id="certidaoDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
	
	<title><%=request.getAttribute("TituloPagina")%></title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./css/Paginacao.css');
	</style>
	    
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
		<script type="text/javascript" src="./js/jqDnR.js"> </script>  
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		
		<script type="text/javascript">
			function selecionaSubmete(id, descricao){
				var form =	document.getElementById('Formulario');
				form.Id_ProcessoParte.value = id;
				form.PaginaAtual.value = '<%=Configuracao.Editar%>' ;
				form.submit();
			}
		</script>
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				
				<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> <legend class="formEdicaoLegenda">Informações do Sentenciado</legend>
					<br></br>
					<label class="formEdicaoLabel" style="width: 20%"> Modelo da Certidão </label><br>
					<input class="formEdicaoInputSomenteLeitura" name="modeloInformado" readonly type="text" size="52" maxlength="50" id="modeloInformado" value="<%=modeloDt.getModelo()%>" />
					<br></br>
					<br></br>
					<label class="formEdicaoLabel" for="NumeroProcesso" style="width: 20%">Nº do Processo de Execução</label><br> 
					<input type="text" name="numeroProcessoInformado" class="formEdicaoInputSomenteLeitura" size="30" maxlength="18" onkeypress="return DigitarSoNumero(this, event)" value="<%=certidaoDt.getProcessoNumeroCompleto()%>">
					<br></br>
	    			<label class="formEdicaoLabel" for="CpfInformado" style="width: 20%">CPF</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" name="CpfInformado" id="CpfInformado"  type="text" size="30" maxlength="11" value="<%=certidaoDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Informe o CPF do sentenciado."/>
	    			<br />
					<label class="formEdicaoLabel" for="NomeInformado" style="width: 20%">Nome do Sentenciado</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" name="NomeInformado" id="NomeInformado" type="text" size="60" maxlength="255" value="<%=certidaoDt.getRequerente()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />
					<label class="formEdicaoLabel" for="NomeMaeInformado" style="width: 20%">Nome da Mãe</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" name="NomeMaeInformado" id="NomeMaeInformado" type="text" size="60" maxlength="255" value="<%=certidaoDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />			
					<label class="formEdicaoLabel" for="DataNascimentoInformado" style="width: 20%">Data de Nascimento</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" name="DataNascimentoInformado" id="DataNascimentoInformado" type="text" size="60" maxlength="255" value="<%=certidaoDt.getDataNascimento()%>" onkeyup=" autoTab(this,255)"/>
	    			
	    			<input class="formEdicaoInputSomenteLeitura" name="modelo" readonly type="hidden" id="modelo" value="<%=request.getAttribute("modelo")%>" />
	    			<input class="formEdicaoInputSomenteLeitura"  name="Cpf" id="Cpf"  type="hidden" size="30" maxlength="11" value="<%=request.getAttribute("Cpf")%>" />
	    			<input class="formEdicaoInputSomenteLeitura"  name="Nome" id="Nome" type="hidden" size="60" maxlength="255" value="<%=request.getAttribute("Nome")%>"/>
	    			<input class="formEdicaoInputSomenteLeitura"  name="NomeMae" id="NomeMae" type="hidden" size="60" maxlength="255" value="<%=request.getAttribute("NomeMae")%>"/>
	    			<input class="formEdicaoInputSomenteLeitura"  name="DataNascimento" id="DataNascimento" type="hidden" size="60" maxlength="255" value="<%=request.getAttribute("DataNascimento")%>"/>
	    			<input class="formEdicaoInputSomenteLeitura"  name="NumeroProcesso" id="NumeroProcesso" type="hidden" size="60" maxlength="255" value="<%=request.getAttribute("NumeroProcesso")%>"/>
	    			<br />		
	    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgLimpar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); Formulario.submit();"> 
					</div>
				</fieldset>
				<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Certidão</legend>
					<br />
					<div id="Editor" class="Editor" style="display: none">
					<textarea class="ckeditor" cols="80" id="editor1" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>
					<script type="text/javascript">
						MostrarOcultar('Editor');					
					</script></div>
				</fieldset>
				<% if (request.getAttribute("ListaProcessoParte") != null){ %>
				<fieldset class="formEdicao"> <legend class ="formEdicaoLegenda">Lista de Sentenciados</legend>
					<input id="Id_ProcessoParte" name="Id_ProcessoParte" type="hidden" value="" />
						<%
							List liTemp = (List) request.getAttribute("ListaProcessoParte");
							ProcessoParteDt objTemp;
							boolean boLinha = false;
							if (liTemp!=null){
							
						%>
					<div id="divTabela" class="divTabela">
					<table id="Tabela" class="Tabela">
						<thead>
							
							<tr>
								<th class="colunaMinima"></th>
								<th width="20%">Nº do Processo de Execução</th>
								<th>Nome do Sentenciado</th>
								<th>Nome da Mãe</th>
								<th>Data de Nascimento</th>
								<th align="center">Certidão</th>
							</tr>
						</thead>
						<tbody id="tabListaProcessoParte">
						<%
							for (int i = 0; i < liTemp.size(); i++) {
								objTemp = (ProcessoParteDt) liTemp.get(i);
						%>
						<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
							<td><img src="./imagens/execpen_icons/down.png" title="Detalhamento das informações do Sentenciado" style="cursor: pointer; cursor: hand;"
						onclick="return mostrarLinhaTabela('imprimir_<%=objTemp.getId()%>',this)"></img></td>
							<td align="center"><%=Funcoes.formataNumeroProcesso(objTemp.getProcessoNumero())%></td>
							<td><%=objTemp.getNome()%></td>
							<td><%=objTemp.getNomeMae()%></td>
							<td align="center"><%=objTemp.getDataNascimento()%></td>
							<td align="center"><input name="imaLocalizarArquivoTipo" type="image" src="./imagens/imgEditorTextoPequena.png"
							onclick="AlterarValue('PassoEditar','9'); selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getNome()%>');" title="Abrir Certidão" /></td>
							
						</tr>
						<tr>
							<td colspan=5>
							<div id="imprimir_<%=objTemp.getId()%>" align="justify" style="display: none">
								Pai: <%=objTemp.getNomePai()%><br />
								CPF: <%=objTemp.getCpfFormatado()%><br />
								Rg:<%=objTemp.getRg() + "  " + objTemp.getSiglaOrgaoExpedidor() + "   " + objTemp.getRgDataExpedicao()%><br />
								Sexo: <%=objTemp.getSexo()%><br />
								Estado civil: <%=objTemp.getEstadoCivil()%><br />
								Profissão: <%=objTemp.getProfissao()%> <br />
								Naturalidade: <%=objTemp.getCidadeNaturalidade()%> <br />
								Endereço: <%=objTemp.getEnderecoParte().getLogradouro() + ",  " + objTemp.getEnderecoParte().getComplemento()
											+ ",  " + objTemp.getEnderecoParte().getBairro() + ",  " + objTemp.getEnderecoParte().getCidade()
											+ " - " + objTemp.getEnderecoParte().getUf() + " - " + objTemp.getEnderecoParte().getCep() + "    "
											+ objTemp.getEMail() + "    " + objTemp.getTelefone()%> <br />
								Alcunha: <%if(objTemp.getListaAlcunhaParte() != null) { 
												for (int j=0; j<objTemp.getListaAlcunhaParte().size(); j++){%>
													<%=((ProcessoParteAlcunhaDt)objTemp.getListaAlcunhaParte().get(j)).getAlcunha() + ", " %> 
										<%}	}%><br />
								Sinal Particular: <%if(objTemp.getListaSinalParte() != null) { 
														for (int j=0; j<objTemp.getListaSinalParte().size(); j++){%>
														<%=((ProcessoParteSinalDt)objTemp.getListaSinalParte().get(j)).getSinal() + ", " %>
													<%}	}%>
							</div>
							</td>
						</tr>
						<% boLinha = !boLinha;
						}
						}%>
						
					</tbody>
				</table>
				</div>
				</fieldset>
				<%} %>
				</div>	
				
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf"%>	
	</body>
</html>