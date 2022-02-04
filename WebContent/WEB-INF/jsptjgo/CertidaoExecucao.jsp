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
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
      	<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoCaracteres.js" ></script>
		
		<script type="text/javascript">
			function VerificarCampos() {
				if (document.Formulario.modelo.value == ""){
					alert("O campo Modelo da Certidão é obrigatório!"); 
					return false;
				}
				if (document.Formulario.NumeroProcesso.value == "" && document.Formulario.Cpf.value == "" && document.Formulario.Nome.value == "" && document.Formulario.NomeMae.value == "" && document.Formulario.DataNascimento.value == ""){
					alert("Informe o(s) parâmetro(s) da consulta!"); 
					return false;
				} else return true;
			}
			function selecionaSubmete(idParte, nomeParte, idProcesso){
				var form =	document.getElementById('Formulario');
				form.Id_ProcessoParte.value = idParte;
				form.Id_Processo.value = idProcesso;
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
				<fieldset class="formEdicao"> <legend class="formEdicaoLegenda">Parâmetros da Consulta</legend>
	    			<br></br>
					<label class="formEdicaoLabel" style="width: 10%"> Modelo da Certidão 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecionar Modelo de Arquivo" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('modelo', 'modelo'); return false;" title="Limpar Modelo" />
					</label><br>
					
					<input class="formEdicaoInputSomenteLeitura" name="modelo" readonly type="text" size="52" maxlength="50" id="modelo" value="<%=modeloDt.getModelo()%>" />
					<br></br>
					<br></br>
					<label class="formEdicaoLabel" for="NumeroProcesso" style="width: 20%">Nº do Processo de Execução</label><br> 
					<input type="text" name="NumeroProcesso" id="NumeroProcesso" class="formEdicaoInput" size="30" maxlength="18" onkeypress="return DigitarSoNumero(this, event)" value="<%=certidaoDt.getProcessoNumeroCompleto()%>" >
					<br/>
	    			<label class="formEdicaoLabel" for="Cpf" style="width: 20%">CPF</label><br> 
	    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="11" value="<%=certidaoDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Informe o CPF do sentenciado."/>
	    			<br />
					<label class="formEdicaoLabel" for="Nome" style="width: 20%">Nome do Sentenciado</label><br> 
	    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=certidaoDt.getRequerente()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />
					<label class="formEdicaoLabel" for="NomeMae" style="width: 20%">Nome da Mãe</label><br> 
	    			<input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="60" maxlength="255" value="<%=certidaoDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />			
					<label class="formEdicaoLabel" for="DataNascimento" style="width: 20%">Data de Nascimento</label><br> 
	    			<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento" type="text" size="60" maxlength="255" value="<%=certidaoDt.getDataNascimento()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />				
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="button" value="Consultar" onclick="if (VerificarCampos()) {AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); Formulario.submit()}">
						<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"> 
					</div>
				</fieldset>

				<% if (request.getAttribute("ListaProcessoParte") != null){ %>
				<fieldset class="formEdicao"> <legend class ="formEdicaoLegenda">Lista de Sentenciados</legend>
					<input id="Id_ProcessoParte" name="Id_ProcessoParte" type="hidden" value="" />
					<input id="Id_Processo" name="Id_Processo" type="hidden" value="" />
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
								<th width="20%">Nº Processo Execução</th>
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
							onclick="AlterarValue('PassoEditar','9'); selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getNome()%>','<%=objTemp.getId_Processo()%>');" title="Abrir Certidão" /></td>
							
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