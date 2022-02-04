<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.jus.cnj.intercomunicacao.beans.Pessoa"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaVisitaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaVinculoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaRedeSocialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TipificacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaApelidoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaEmailDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaFoneDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaFaccaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaBemDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaContaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaEnderecoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaCrimeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaEmpresaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaFotoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaImovelDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaPresidioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EnderecoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PessoaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.FaccaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RedeSocialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BemTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<jsp:useBean id="Pessoadt" scope="session" class= "br.gov.go.tj.projudi.dt.PessoaDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pessoa  </title>
	<link rel="stylesheet" href="./css/jquery.tabs.css" type="text/css" media="print, projection, screen" />
	<link href="./css/Principal.css" type="text/css"  rel="stylesheet" />
	<link href="./css/slideshow.css" type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' language="javascript" src='js/jquery.js'> </script>	
	<script type='text/javascript' language="javascript" src='js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" language="javascript" src="js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" language="javascript" src="js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' language="javascript" src='js/Funcoes.js'></script>
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
			function VerificarCampos() {
				with(document.Formulario) {
					if (SeNulo(Nome, "O Campo Nome é obrigatório!")) return false;
					submit();
				}
			}
		</script>
	<%}%>
	<script type="text/javascript">
		function importar() {
			$("#importar").show('slow');
		}
		function ChecaExtensaoArquivo(formulario){
   			var extensoesOk = ".projudi";
   			var extensao = formulario.arquivo.value.substr(formulario.arquivo.value.length - 8 ).toLowerCase();
   			if(extensoesOk.indexOf( extensao ) == -1 ){
       			alert("Arquivo inválido. A extensão do mesmo deve ser do tipo '.projudi'");
	       		return false;
     		}
   			return true;
 		}
	</script>
</head>

<body >
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2> Cadastro de Pessoa</h2></div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
				<form action="Pessoa" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
				<form action="Pessoa" method="post" name="Formulario" id="Formulario">
			<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input name="status" id="status" type="hidden" value="<%=Pessoadt.getStatus()%>" />
			<input name="novoStatus" id="novoStatus" type="hidden" value="" />

			<div class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>				
					<a class="divPortaBotoesLink" href="Ajuda/PessoaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /></a>
					<input id="imgRelatorioVinculo" alt="RelatorioVinculo" class="imgImprimir" title="Imprimir relatório de vínculos da pessoa." name="imaImprimir" type="image"  src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" />				
					<input id="imgImportar" alt="Importar" class="imgCarregar" title="Importa cadastro de pessoa" name="imaCarregar" type="image" src="./imagens/22x22/download.png"  onclick="importar();return false;" />
					<input id="imgExportar" alt="Exportar" class="imgCarregar" title="Exporta cadastro de pessoa" name="imaCarregar" type="image" src="./imagens/22x22/upload.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');" />
					<% if(UsuarioSessao.isInteligenciaN1() && Pessoadt.isBloqueada()) { %>
							<input id="btFinalizar" class="imgFinalizar" title="Enviar cadastro para validação do superior." name="btFinalizar" type="image"  src="./imagens/22x22/next.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('novoStatus',<%=PessoaDt.EM_VALIDACAO%>)" />
					<%
						} else if(UsuarioSessao.isInteligenciaN2() && Pessoadt.isEmValidacao()) {
					%>
							<input id="btVoltar" class="imgVoltar" title="Retornar cadastro." name="btVoltar" type="image"  src="./imagens/22x22/previous.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('novoStatus',<%=PessoaDt.BLOQUEADO%>)" />
							<input id="btFinalizar" class="imgFinalizar" title="Finalizar cadastro." name="btFinalizar" type="image"  src="./imagens/22x22/next.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('novoStatus',<%=PessoaDt.LIBERADO%>)" />
					<% } %>
				</div>
				<fieldset class="formEdicao" id='CadastroPessoa' > 
					<legend class="formEdicaoLegenda">Edita os dados Pessoa </legend>
					
					<div class="col10">
						<label for="Id_Pessoa">Identificador</label>
						<input class="formEdicaoInputSomenteLeitura" name="Id_Pessoa"  id="Id_Pessoa" size="10" type="text"  readonly="true" value="<%=Pessoadt.getId()%>">
					</div>
					<div class="col45">
						<label  for="Nome">*Nome</label>
						<input class="formEdicaoInput" name="Nome" id="Nome"  type="text" size="85" maxlength="100" value="<%=Pessoadt.getNome()%>" onkeyup=" autoTab(this,60)">
					</div>
					<div class="col10">
						<label  for="Cpf">Cpf</label>
						<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="14" maxlength="11" value="<%=Pessoadt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
					</div>
					<div class="col15">
						<label  for="DataNasc">Data Nascimento <input type="image" id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNasc,'dd/mm/yyyy',this);return false;"/></label> 
						<input class="formEdicaoInput" name="DataNasc" id="DataNasc"  type="text" size="20" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" value="<%=Pessoadt.getDataNasc()%>">
					</div>
					<div class="col10">
			    	<label  for="Sexo">Sexo</label> 
			    		<select name="Sexo" id="Sexo" class="formEdicaoCombo">
			    			<option value="" >
			    			<option value="-1" <%=(Pessoadt.isMasculino()?"selected":"")%>>Masculino</option>
			    			<option value="1" <%=(Pessoadt.isFeminino()?"selected":"")%>>Feminino</option></option>
			    		</select>
			    	</div>
					<div class="col5">
					<label  for="Obito">Óbito</label>
						<select id="Obito" name="Obito" class="formEdicaoCombo">
							<option value=""></option>
			            	<option value="0" <%=(Pessoadt.isObito().equals("false")?"selected":"")%> >NÃO</option>
			            	<option value="1" <%=(Pessoadt.isObito().equals("true")?"selected":"")%> >SIM</option>
			        	</select>
			        </div>
			    	<div class="col20">
			    		<label  for="Cidade">Naturalidade
			    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('CadastroPessoa','Pessoa','Consulta de Cidade', 'Digite a Cidade e UF e clique em consultar.', 'Id_Naturalidade', 'Naturalidade', ['Cidade','UF'], ['Uf'], '<%=(CidadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")" >
			    		<input class="FormEdicaoimgLocalizar" name="imaLimparNaturalidade" type="image"  src="./imagens/imgLimpar.png" onclick="LimparChaveEstrangeira('Id_Cidade','Cidade'); return false;" title="Limpar Naturalidade"></label>
			    		<input class="formEdicaoInputSomenteLeitura" readonly="true"name="Naturalidade" id="Naturalidade"  type="text" size="35" value="<%=Pessoadt.getNaturalidade()%>" onkeyup=" autoTab(this,22)">
					    <input type="hidden" name="Id_Naturalidade" id="Id_Naturalidade" value="<%=Pessoadt.getId_Naturalidade()%>">
					</div>
					<div class="col15">
					<label  for="Rg">Rg</label>
						<input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="25" value="<%=Pessoadt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					</div>
					<div class="col15">
						<label  for="RgOrgao">Rg Órgão</label>
						<input class="formEdicaoInput" name="RgOrgao" id="RgOrgao"  type="text" size="25" maxlength="25" value="<%=Pessoadt.getRgOrgao()%>" onkeyup=" autoTab(this,30)">
					</div>
			        <div class="col20">
						<label  for="Situacao">Situação</label>
						<input class="formEdicaoInput" name="Situacao" id="Situacao"  type="text" size="35" maxlength="60" value="<%=Pessoadt.getSituacao()%>" onkeyup=" autoTab(this,60)">
					</div>
					<div class="col15">
						<label  for="Id_Profissao">Profissão
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarProfissao" name="imaLocalizarProfissao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('CadastroPessoa','Pessoa','Consulta de Profissao', 'Digite a Profissão e clique em consultar.', 'Id_Profissao', 'Profissao', ['Profissao'], [], '<%=(ProfissaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 					
						<input class="FormEdicaoimgLocalizar" id="imaLimparProfissao" name="imaLimparProfissao" type="image"  src="./imagens/imgLimpar.png"  onclick="LimparChaveEstrangeira('Id_Profissao','Profissao'); return false;" ></label>  
						<input id='Id_Profissao' name='Id_Profissao' type='hidden' value='<%=Pessoadt.getId_Profissao()%>' /> 
						<input class="formEdicaoInputSomenteLeitura" name="Profissao"  id="Profissao"  type="text" size="47" readonly="true" value="<%=Pessoadt.getProfissao()%>">
					</div>
					<div class="col100">
						<label  for="Observacoes">Observações</label>
						<textarea  class="formEdicaoInput" name="Observacoes" id="Observacoes" rows="3" cols="146"><%=Pessoadt.getObservacoes()%></textarea>
					</div>
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
		<form action="Pessoa" method="post" name="FormArquivo" id="FormArquivo" enctype="multipart/form-data" onsubmit="return ChecaExtensaoArquivo(this)">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=Configuracao.Curinga8%>">
			<div id="importar" style="display:none" class="modalBusca">
				<div class="modalBusca-content1">
					<div class="modalTitulo">Importar<p id='modalTitulo'></p><span class="modal_close" onclick='modalClose("importar");'>&times;</span></div> 
					<div class="modalBusca-content2">
						<fieldset class="fieldEdicaoEscuro"> 
				    <legend class="formEdicaoLegenda">Carregar Dados</legend>
					<div class="obs">Selecione o arquivo desejado e clique em Carregar para efetuar a importação.</div> <br />
					<label class="formEdicaoLabel" for="filename">Arquivo</label><br> 
					<input type="file" name="arquivo" id="filename" size="50" /> <br/>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Carregar" onclick='modalClose("importar");'> 
						<input name="imgCancelar" type="reset" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
				    </div>
				</fieldset>			
					</div>
				</div> 
			</div>
		</form>
	<%if(Pessoadt.isSalvo()){%>
		<div class="divEditar">
		<div id="abas" style="min-height:550px">
			<ul>
				<li><a href="#divApelido"><span>	Apelidos</span></a></li>				
				<li><a href="#divCrime"><span>		Crimes</span></a></li>
				<li><a href="#divEmail"><span>		Emails</span></a></li>				
				<li><a href="#divEndereco"><span>	Endereços</span></a></li>
				<li><a href="#divFaccao"><span>		Facções</span></a></li>
				<li><a href="#divFone"><span>		Fones</span></a></li>
				<li><a href="#divFoto"><span>		Fotos</span></a></li>				
				<li><a href="#divPresidio"><span>	Presídios</span></a></li>
				<li><a href="#divRedeSocial"><span>	Redes Sociais</span></a></li>				
				<li><a href="#divVisita"><span>		Visitas</span></a></li>
				<%if(!UsuarioSessao.isInteligenciaN0() && !UsuarioSessao.isInteligenciaN1() && !UsuarioSessao.isInteligenciaN2()){%>
					<li><a href="#divVinculo"><span>	Vínculos</span></a></li>
					<li><a href="#divImovel"><span>		Imóveis</span></a></li>
					<li><a href="#divEmpresa"><span>	Empresas</span></a></li>
					<li><a href="#divBem"><span>		Bens</span></a></li>
					<li><a href="#divConta"><span>		Contas</span></a></li>
				<%}%>
				<li><a href="#divProcesso"><span>	Processos</span></a></li>
				
			</ul>
<!--APELIDO---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divApelido"  >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id='Campos_Apelido' > 
						<legend class="formEdicaoLegenda">Edita Apelidos</legend>
						<input  name="Id_Pessoa" id="Id_Pessoa" type="hidden"  readonly="true" value="<%=Pessoadt.getId()%>">
						<div class="col15">
							<label  for="Id_PessoaApelido">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id" size="10" type="text" readonly="true" value="">
						</div>
						<div class="col40">
							<label  for="Apelido">*Apelido</label> 
							<input class="formEdicaoInput" name="Apelido" id="Apelido"  type="text" size="60" maxlength="60" value="" onkeyup=" autoTab(this,60)"><br />
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Apelido', 'Info Apelido','sim')">
					</div>	
					<div class="divTabela"> 
						<table  id="tb_Campos_Apelido" class="Tabela">
						    <thead>
						      <tr>
						      	<th class="colunaMinima" >Id</th>
						        <th width='120px' >Apelido</th>
						        <th width='60px' >Data Cadastro</th>
						        <th class="colunaMinima">info</th>
						        <th class="colunaMinima" >Editar</th>
						        <th class="colunaMinima" >Excluir</th> 
						      </tr>
						    </thead>
						  	<tbody>
						  		<%List lisApelidos =(List)Pessoadt.getListaApelidos();
						  		for(int i=0;lisApelidos!=null && i<lisApelidos.size();i++){
						  			PessoaApelidoDt dt =(PessoaApelidoDt)lisApelidos.get(i);%>
								<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
								<td class="colunaMinima"><%=dt.getId()%></td>
								<td width='120px' align="left"><%=dt.getApelido()%></td>
								<td width='60px' align="center"><%=dt.getDataCadastro()%></td>
								<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Apelido',<%=dt.getId()%>,'Info Apelido')"></td>
								<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Apelido',<%=dt.getId()%>)"></td>
								<td class="colunaMinima" ><img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Apelido',<%=dt.getId()%>)"></td>
								</tr> 
							<%}%>
						  	</tbody>
						  </table>
						</div>
					</div>	
			</div>
<!--EMAIL---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divEmail" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id='Campos_Email' > 
						<legend class="formEdicaoLegenda">Edita Emails</legend>
						<div class="col15">
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id" size="10" type="text"  readonly="true" value="">
						</div>
						<div class="col40">
							<label  for="Email">*Email</label> 
							<input class="formEdicaoInput" name="Email" id="Email"  type="text" size="60" maxlength="60" value="" onkeyup=" autoTab(this,60)">
						</div>
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />						
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Email', 'Info Email','sim')">
					</div>
				</div>
				<div class="divTabela"> 
					<table  id="tb_Campos_Email" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='120px' >Email</th>
					        <th width='60px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%List lisEmails =(List)Pessoadt.getListaEmails();
							for(int i=0;lisEmails!=null && i<lisEmails.size();i++){
								PessoaEmailDt dt =(PessoaEmailDt)lisEmails.get(i); %>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima"><%=dt.getId()%></td>
							<td ><%=dt.getEmail()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Email',<%=dt.getId()%>,'Info Email')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Email',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Email',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--TELEFONE---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divFone" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id='Campos_Fone' >
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' /> 
						<legend class="formEdicaoLegenda">Edita Telefones</legend>
						<div class="col15">
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id" size="10" type="text"  readonly="true" value="">
						</div>
						<div class="col25">
							<label  for="Fone">*Telefone</label> 
							<input class="formEdicaoInput" name="Fone" id="Fone"  type="text" size="30" maxlength="60" value="" onkeyup=" autoTab(this,60)">
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Fone','Info Fone','sim')">
					</div>
				</div>
				<div class="divTabela"> 
					<table  id="tb_Campos_Fone" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='120px' >Telefone</th>
					        <th width='60px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%List lisFones =(List)Pessoadt.getListaFones();
							for(int i=0;lisFones!=null && i<lisFones.size();i++){
								PessoaFoneDt dt =(PessoaFoneDt)lisFones.get(i); %>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getFone()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Fone',<%=dt.getId()%>,'Info Fone')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Fone',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Fone',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--FACÇÃO---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divFaccao" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Faccao"> 
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<legend class="formEdicaoLegenda">Edita Facções</legend>
						<div class="col15">
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id" size="10" type="text"  readonly="true" value="">
						</div>
						<div class="col35">
							<label  for="Faccao">*Facção					
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarFaccao" name="imaLocalizarFaccao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Faccao','Faccao','Consulta de Facção', 'Digite a Facção e clique em consultar.', 'Id_Faccao', 'Faccao', ['Facção'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label> 	
							<input class="formEdicaoInputSomenteLeitura" readonly="true"  name="Faccao" id="Faccao"  type="text" size="55" maxlength="60" value="" >
							<input id='Id_Faccao' name='Id_Faccao' type='hidden' value='' />
						</div>
						<div class="col35">
							<label  for="Cargo">Cargo</label> 
							<input class="formEdicaoInput" name="Cargo" id="Cargo"  type="text" size="55" maxlength="60" value="" onkeyup=" autoTab(this,60)">
						</div>		
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Faccao','Info Facção','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Faccao" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='120px' >Facção</th>
					        <th width='120px' >Cargo</th>
					        <th width='60px'  >Data Cadastro</th>	
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%List lisFaccao =(List)Pessoadt.getListaFaccoes();
							for(int i=0;lisFaccao!=null && i<lisFaccao.size();i++){
								PessoaFaccaoDt dt =(PessoaFaccaoDt)lisFaccao.get(i); %>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>														
							<td ><%=dt.getFaccao()%></td>
							<td ><%=dt.getCargo()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Faccao',<%=dt.getId()%>,'Info Facção')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Faccao',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Faccao',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--ENDEREÇO---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divEndereco" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Endereco">
						<legend class="formEdicaoLegenda">Edita Endereços</legend>
						<input id='id' name='id' type='hidden' value='' />
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<input id='Id_Endereco' name='Id_Endereco' type='hidden' value='' />
 						
 						<div class="col50">
							<label  for="Logradouro">*Logradouro</label>
							<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="90" maxlength="100" value="" onKeyUp=" autoTab(this,60)">
						</div>
						<div class="col10">
							<label  for="Numero">Número</label> 
							<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="12" maxlength="10" value="" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
						</div>
						<div class="col20">						 	
							<label  for="Complemento">Complemento</label>  
							<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="70" maxlength="100" value="" onKeyUp=" autoTab(this,255)">
						</div>
						<div class="col30">
							<label  for="Bairro">*Bairro 
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarFaccao" name="imaLocalizarFaccao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Endereco','Pessoa','Consulta de Bairro', 'Digite o Bairro e clique em consultar.', 'Id_Bairro', 'Bairro', ['Bairro','Cidade','UF'], ['Cidade', 'Uf', '', ''], '<%=(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>
							<input id='Id_Bairro' name='Id_Bairro' type='hidden' value='' />
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="50" value="">
						</div>
						<div class="col30">
							<label  for="Cidade">Cidade</label>
							<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="50" value="">
						</div>
						<div class="col10">
							<label  for="Uf">UF</label> 
							<input class="formEdicaoInputSomenteLeitura" readonly name="Uf" id="Uf"  type="text" size="10" value="">
						</div>
						<div class="col10">
							<label  for="Cep">*CEP</label> 
							<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Endereco','Info Endereço','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Endereco" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='80px' >Logradouro</th>
					        <th width='20px' >Número</th>
					        <th width='60px' >Complemento</th>
					        <th width='30px' >Cep</th>
					        <th width='30px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisEndereco =(List)Pessoadt.getListaEnderecos();
					  						for(int i=0;lisEndereco!=null && i<lisEndereco.size();i++){
					  							PessoaEnderecoDt dt =(PessoaEnderecoDt)lisEndereco.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getLogradouro()%></td>
							<td ><%=dt.getNumero()%></td>
							<td ><%=dt.getComplemento()%></td>
							<td ><%=dt.getCep()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Endereco',<%=dt.getId()%>,'Info Endereço')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Endereco',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Endereco',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>

<!--CRIME---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divCrime" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Crime">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<input id='Id_Endereco' name='Id_Endereco' type='hidden' value='' />
						 				
						<legend class="formEdicaoLegenda">Edita Crimes</legend>
						<div class="col15">				
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
						</div>
						<div class="col40">				
							<label  for="Tipificacao">*Tipificação
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarTipificacao" name="imaLocalizarTipificacao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Crime','Tipificacao','Consulta de Tipificação', 'Digite a Tipificação e clique em consultar.', 'Id_Tipificacao', 'Tipificacao', ['Tipificacao'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>
							<input  class="formEdicaoInputSomenteLeitura" type="text" size='60' readonly="true" name="Tipificacao" id="Tipificacao" value="" >
							<input id='Id_Tipificacao' name='Id_Tipificacao' type='hidden' value='' />
						</div>
						<div class="col40">				
							<label  for="Procedimento">*Procedimento</label>
							<input  class="formEdicaoInput" name="Procedimento" id="Procedimento" type="text" size="60" maxlength="60" value="">
						</div>
						<div class="col15">	
							<label  for="NumProcedimento">*N.º Procedimento</label> 
							<input class="formEdicaoInput" name="NumProcedimento" id="NumProcedimento"  type="text" size="22" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
						</div>
						<div class="col20">				
							<label  for="DataFato">*Data Fato <input type="image" id="calendarioDataFato" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(DataFato,'dd/mm/yyyy',this);return false;"/></label> 
								<input class="formEdicaoInput" name="DataFato" id="DataFato"  type="text" size="20" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" value="">							
						</div> <br />
						<div class="col40">
							<label  for="Bairro">*Bairro 
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarFaccao" name="imaLocalizarFaccao" type="image"  src="./imagens/imgLocalizarPequena.png" 
								onclick="MostrarBuscaPadrao('Campos_Crime','Pessoa','Consulta de Bairro', 'Digite o Bairro e clique em consultar.', 'Id_Bairro', 'Bairro', ['Bairro','Cidade','UF'], ['Cidade', 'Uf', '',''], '<%=(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>
							<input id='Id_Bairro' name='Id_Bairro' type='hidden' value='' />
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="60" value="">
						</div>
						<div class="col40">
							<label  for="Cidade">Cidade</label>
							<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="60" value="">
						</div>
						<div class="col10">
							<label  for="Uf">UF</label> 
							<input class="formEdicaoInputSomenteLeitura" readonly name="Uf" id="Uf"  type="text" size="10" value="">
						</div>
						<div class="col50">
							<label  for="Logradouro">*Logradouro</label>
							<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="90" maxlength="100" value="" onKeyUp=" autoTab(this,60)">
						</div>
						<div class="col10">
							<label  for="Numero">Número</label> 
							<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="12" maxlength="10" value="" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
						</div>
						<div class="col50">						 	
							<label  for="Complemento">Complemento</label>  
							<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="80" maxlength="100" value="" onKeyUp=" autoTab(this,255)">
						</div>
						<div class="col10">
							<label  for="Cep">CEP</label> 
							<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
						</div>
						
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Crime','Info Crime','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Crime" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px' >Tipificação</th>
					        <th width='40px' >Procedimento</th>
					        <th width='40px' >N.º Procedimento</th>
					        <th width='60px' >Data Cadastro</th>
					        <th width='60px' >Data Fato</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisCrime =(List)Pessoadt.getListaCrimes();
					  				  						for(int i=0;lisCrime!=null && i<lisCrime.size();i++){
					  				  							PessoaCrimeDt dt =(PessoaCrimeDt)lisCrime.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getTipificacao()%></td>
							<td ><%=dt.getProcedimento()%></td>
							<td ><%=dt.getNumProcedimento()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td ><%=dt.getDataFato()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Crime',<%=dt.getId()%>,'Info Crime')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Crime',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Crime',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>

<!--FOTO---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divFoto" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Foto">
						<legend class="formEdicaoLegenda">Edita Fotos</legend>						
						<label  for="id">Foto</label> <input type="file" accept="image/jpeg, image/png" id="foto" name="foto" onchange="salvarFotoPasso('Pessoa','Campos_Foto','<%=Pessoadt.getId()%>')" />
					</fieldset>
				</div>	
				<table id='table_row_fotos' class="row_fotos" cellspacing="15px">
					<tr id='tr_fotos' > 
				  	<%
 				  		List lisFoto =(List)Pessoadt.getListaFotos();
 				  					for(int i=0;lisFoto!=null && i<lisFoto.size();i++){
 				  						PessoaFotoDt dt =(PessoaFotoDt)lisFoto.get(i);
 				  	%>
							<td id='td_foto<%=dt.getId()%>' > 
								<img id='foto<%=dt.getId()%>' src="data:image/png;base64,<%=dt.getFotoBase64()%>" alt="<%=dt.getDataCadastro()%>" onclick="infoFoto(<%=dt.getId()%>)" class="foto">
								<img class="foto_excluir" src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Foto',<%=dt.getId()%>)">
					  		</dt>					 
					<%}%>
					</tr>
				</table>
			</div>

<!--PRESÍDIO---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divPresidio" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Presidio">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<legend class="formEdicaoLegenda">Edita Presídios</legend>
						<div class="col20">
							<label  for="id">Identificador</label>
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
						</div>
						<div class="col20">						
							<label  for="DataInicial">Data Inicial <input type="image" id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(DataInicial,'dd/mm/yyyy',this);return false;"/></label>	
							<input class="formEdicaoInput" name="DataInicial" id="DataInicial"  type="text" size="20" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" value=""> 
						</div>
						<div class="col20">
							<label  for="DataFinal">Data Final <input type="image" id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(DataFinal,'dd/mm/yyyy',this);return false;"/></label>
							<input class="formEdicaoInput" name="DataFinal" id="DataFinal"  type="text" size="20" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" value="">
						</div> 
						<div class="col20">	
							<label  for="DataProgressao">Data Progressão <input type="image" id="calendarioDataProgressao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(DataProgressao,'dd/mm/yyyy',this);return false;"/></label>
							<input class="formEdicaoInput" name="DataProgressao" id="DataProgressao"  type="text" size="20" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" value="">
						</div>
						<div class="col45">	
							<label  for="Id_Tipificacao">Tipificação  
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarTipificacao" name="imaLocalizarTipificacao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Presidio','Tipificacao','Consulta de Tipificacao', 'Digite o Tipificacao e clique em consultar.', 'Id_Tipificacao', 'Tipificacao', ['Tipificacao'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 											
								<input class="FormEdicaoimgLocalizar" id="imaLimparTipificacao" name="imaLimparTipificacao" type="image"  src="./imagens/imgLimpar.png"  onclick="LimparChaveEstrangeira('Campos_Presidio','Id_Tipificacao','Tipificacao'); return false;" >
							</label>
								<input  name='Id_Tipificacao' id='Id_Tipificacao' type='hidden'  value=''>
								<input  class="formEdicaoInputSomenteLeitura" name='Tipificacao' id='Tipificacao'  type="text"  size="60" readonly="true"  value=''>
						</div>
						<div class="col45">
							<label  for="Id_Presidio">*Presídio  
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarPresidio" name="imaLocalizarPresidio" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Presidio','Presidio','Consulta de Presidio', 'Digite o Presidio e clique em consultar.', 'Id_Presidio', 'Presidio', ['Presidio'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 					
								<input class="FormEdicaoimgLocalizar" id="imaLimparPresidio" name="imaLimparPresidio" type="image"  src="./imagens/imgLimpar.png"  onclick="LimparChaveEstrangeira('Campos_Presidio','Id_Presidio','Presidio'); return false;" >
							</label>						
								<input  name='Id_Presidio' id='Id_Presidio' type='hidden'  value=''>
								<input  class="formEdicaoInputSomenteLeitura" name='Presidio' id='Presidio'  type="text"  size="60" readonly="true"  value=''>
						</div>
						<div class="col60">
							<label  for="Observacao">Observação</label> 
							<input  class="formEdicaoInput"  name="Observacao" id="Observacao" type="text" size="160" maxlength="255" value="">
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Presidio','Info Presídio','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Presidio" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px'>Presídio</th>
					        <th width='60px'">Tipificação</th>
					        <th width='40px'>Data Inicial</th>
					        <th width='40px'>Data Final</th>
					        <th width='40px'>Data Cadastro</th>
					        <th width='40px'>Data Progressão</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisPresidio =(List)Pessoadt.getListaPresidios();
					  						for(int i=0;lisPresidio!=null && i<lisPresidio.size();i++){
					  							PessoaPresidioDt dt =(PessoaPresidioDt)lisPresidio.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getPresidio()%></td>
							<td ><%=dt.getTipificacao()%></td>
							<td ><%=dt.getDataInicial()%></td>
							<td ><%=dt.getDataFinal()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td ><%=dt.getDataProgressao()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Presidio',<%=dt.getId()%>,'Info Presídio')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Presidio',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Presidio',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--REDESOCIAL---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divRedeSocial" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_RedeSocial">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<legend class="formEdicaoLegenda">Edita Redes Sociais</legend>
						<div class='col70'>
							<div class='col25'>
								<label  for="Id_PessoaRedeSocial">Identificador</label>
							 	<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
							 </div>
							<div class='col45'>
								<label  for="RedeSocial">*Rede Social
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarRedesocial" name="imaLocalizarRedesocial" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_RedeSocial','RedeSocial','Consulta de Redesocial', 'Digite o Redesocial e clique em consultar.', 'Id_RedeSocial', 'RedeSocial', ['Rede Social'], [], '<%=Configuracao.Localizar%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 
								</label>								
								<input  class="formEdicaoInputSomenteLeitura"  readonly name="RedeSocial" id="RedeSocial" type="text" size="60" maxlength="60" value="">
								<input id='Id_RedeSocial' name='Id_RedeSocial' type='hidden' value='' />
							</div>
							<div class='col45'>
								<label  for="Link">*Link</label> <input class="formEdicaoInput" name="Link" id="Link"  type="text" size="60" maxlength="255" value="" onkeyup=" autoTab(this,255)">
							</div> <br/>
							<div class='col45'>
								<label  for="Id_">*Usuário</label> <input class="formEdicaoInput" name="Id_" id="Id_"  type="text" size="60" maxlength="60" value="" onkeyup=" autoTab(this,60)">
							</div> 
						</div>	
						<div class='col20'>
							<label  for="Foto">Foto</label> <br />
							<input class="formEdicaoInput" id="Foto"  type="image" >
							<input type="file" accept="image/jpeg, image/png" id="foto" onchange="salvarFotoPasso('Pessoa','Campos_Foto','<%=Pessoadt.getId()%>')" />
						</div>						
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_RedeSocial','Info Rede Social','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_RedeSocial" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px' >Rede Social</th>
					        <th width='60px' >Usuário</th>
					        <th width='40px' >Link</th>
					        <th width='40px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th>  
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisRedeSocial =(List)Pessoadt.getListaRedesSociais();
					  						for(int i=0;lisRedeSocial!=null && i<lisRedeSocial.size();i++){
					  							PessoaRedeSocialDt dt =(PessoaRedeSocialDt)lisRedeSocial.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getRedeSocial()%></td>
							<td ><%=dt.getUsuarioRede()%></td>
							<td ><%=dt.getLink()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_RedeSocial',<%=dt.getId()%>,'Info Rede Social')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_RedeSocial',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_RedeSocial',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>

<!--VISITA---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divVisita" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Visita">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						
						<legend class="formEdicaoLegenda">Edita Visitas</legend>
						<div class='col15'>
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
						</div>						
						<div class='col35'>
							<label  for="Id_PessoaVisitada">*Visitante  
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarPessoa" name="imaLocalizarPessoa" type="image" src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Visita','Pessoa','Consulta de Pessoa', 'Digite o Pessoa e clique em consultar.', 'Id_PessoaVisitante', 'PessoaVisitante', ['Pessoa'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>
							<input  name='Id_PessoaVisitante' id='Id_PessoaVisitante' type='hidden'  value=''>
							<input  class="formEdicaoInputSomenteLeitura" size="60"  readonly name="PessoaVisitante" id="PessoaVisitante" type="text" value=""><br />
						</div>
						<div class='col35'>
							<label  for="Id_Presidio">*Presídio  
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarPresidio" name="imaLocalizarPresidio" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_Visita','Presidio','Consulta de Presidio', 'Digite o Presidio e clique em consultar.', 'Id_Presidio', 'Presidio', ['Presidio'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>
							<input  name='Id_Presidio' id='Id_Presidio' type='hidden'  value=''>
							<input  class="formEdicaoInputSomenteLeitura" name='Presidio' id='Presidio'  type="text" size="60" readonly  value=''><br />
						</div>
						<div class='col20'>
							<label  for="DataProgressao">*Data Visita <input type="image" id="calendarioDatavisita" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(Data,'dd/mm/yyyy',this);return false;"/></label>
							<input class="formEdicaoInput" name="Data" id="Data"  type="text" size="20" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" value="">
						</div>
						<div class='col60'>
							<label  for="Observacao">Observação</label> 
							<input class="formEdicaoInput" name="Observacao" id="Observacao"  type="text" size="100" maxlength="255" value="" onkeyup=" autoTab(this,255)">
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Visita', 'Info Visita','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Visita" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px' >Visitante</th>
					        <th width='60px' >Presídio</th>
					        <th width='40px' >Data</th>
					        <th width='40px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th>
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisVisita =(List)Pessoadt.getListaVisitas();
					  						for(int i=0;lisVisita!=null && i<lisVisita.size();i++){
					  							PessoaVisitaDt dt =(PessoaVisitaDt)lisVisita.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getPessoaVisitante()%></td>
							<td ><%=dt.getPresidio()%></td>
							<td ><%=dt.getData()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Visita',<%=dt.getId()%>,'Info Visita')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Visita',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Visita',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>

<%if(!UsuarioSessao.isInteligenciaN0() && !UsuarioSessao.isInteligenciaN1() && !UsuarioSessao.isInteligenciaN2()){%>
<!--EMPRESA---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divEmpresa" >
				<div id="divEditar" class="divEditar">
											
					<fieldset class="formEdicao" id="Campos_Empresa">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<legend class="formEdicaoLegenda">Edita Empresas</legend>
						<div class='col15'>
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
						</div>
						<div class='col35'>
							<label  for="Id_Empresa">*Empresa<input class="FormEdicaoimgLocalizar" id="imaLocalizarEmpresa" name="imaLocalizarEmpresa" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Empresa','Empresa','Consulta de Empresa', 'Digite o Empresa e clique em consultar.', 'Id_Empresa', 'Empresa', ['Empresa'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>
							<input id='Id_Empresa' name='Id_Empresa' type='hidden' value='' />
							<input id='Empresa'  class="formEdicaoInputSomenteLeitura" name='Empresa' size='60' type="text"  readonly="true" value='' /> 	
						</div> <br />
						
						<div class='col15' >
							<label  for="Responsavel">Responsável</label>
								 <input type="checkbox"  id="Responsavel" name="Responsavel" value='1'> Responsável?
						</div>
						<div class='col15'>
							<label  for="Socio">Sócio</label> 
								<input type="checkbox"  id="Socio" name="Socio" value='1'> Sócio?
						</div>
						<div class='col15'>
							<label  for="Capital">Capital</label> 
							<input class="formEdicaoInput" name="Capital" id="Capital"  type="text" size="22" maxlength="22" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
						</div>
					</fieldset>					
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Empresa','Info Empresa','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Empresa" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px' >Empresa</th>
					        <th width='40px' >Responsável</th>
					        <th width='40px' >Sócio</th>
					        <th width='60px' >Capital</th>
					        <th width='60px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisEmpresa =(List)Pessoadt.getListaEmpresas();
					  						for(int i=0;lisEmpresa!=null && i<lisEmpresa.size();i++){
					  							PessoaEmpresaDt dt =(PessoaEmpresaDt)lisEmpresa.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getEmpresa()%></td>
							<td ><%=dt.getResponsavel()%></td>
							<td ><%=dt.getSocio()%></td>
							<td ><%=dt.getCapital()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Empresa',<%=dt.getId()%>,'Info Empresa')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Empresa',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Empresa',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--VÍNCULO---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divVinculo" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Vinculo">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<legend class="formEdicaoLegenda">Edita Vínculos</legend>
						<div class="col15">				
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
						</div>
						<div class="col35">
						<label  for="Id_Vinculo">*Vínculo <input class="FormEdicaoimgLocalizar" id="imaLocalizarVinculo" name="imaLocalizarVinculo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Vinculo','Vinculo','Consulta de Vínculo', 'Digite o Vínculo e clique em consultar.', 'Id_Vinculo', 'Vinculo', ['Vinculo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" ></label>						 
							<input id='Id_Vinculo' name='Id_Vinculo' type='hidden' value='' />
							<input id='Vinculo' class="formEdicaoInputSomenteLeitura" name='Vinculo' size='60' type="text"  readonly="true" value='' /> <br/>
						</div>
						<div class="col35">
							<label  for="Pessoa">*Pessoa <input class="FormEdicaoimgLocalizar" id="imaLocalizarPessoa" name="imaLocalizarPessoa" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Vinculo','Pessoa','Consulta de Pessoa', 'Digite o Nome e clique em consultar.', 'Id_PessoaVinculada', 'PessoaVinculada', ['Vinculo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" ></label>
							<input id='Id_PessoaVinculada' name='Id_PessoaVinculada' type='hidden' value='' />
							<input id='PessoaVinculada' class="formEdicaoInputSomenteLeitura" name='PessoaVinculada' size='60'  type="text"  readonly="true" value='' /> <br/>
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Vinculo','Info Vínculo','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Vinculo" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px' >Vínculo</th>
					        <th width='60px' >Pessoa</th>
					        <th width='40px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th>
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisVinculo =(List)Pessoadt.getListaVinculos();
					  						for(int i=0;lisVinculo!=null && i<lisVinculo.size();i++){
					  							PessoaVinculoDt dt =(PessoaVinculoDt)lisVinculo.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getVinculo()%></td>
							<td ><%=dt.getPessoaVinculada()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Vinculo',<%=dt.getId()%>,'Info Vínculo')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Vinculo',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Vinculo',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--BEM---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divBem" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Bem"> 
					<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
					
					<legend class="formEdicaoLegenda">Edita Bens</legend>
					<div class="col15">
						<label  for="id">Identificador</label> 
						<input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id" size="10" type="text"  readonly="true" value="">
					</div>
					<div class="col30">
						<label  for="Descricao">*Descrição</label> 
						<input class="formEdicaoInput" name="Descricao" id="Descricao"  type="text" size="60" maxlength="60" value="" onkeyup=" autoTab(this,60)">
					</div> <br />
					<div class="col40">
						<label  for="Id_Pessoa">Proprietário
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarPessoa" name="imaLocalizarPessoa" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Bem','Pessoa','Consulta de Pessoa', 'Digite o Pessoa e clique em consultar.', 'Id_PessoaLaranja', 'PessoaLaranja', ['Pessoa'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
						</label>
						<input  class="formEdicaoInputSomenteLeitura" readonly="true" class="formEdicaoInputSomenteLeitura" name="PessoaLaranja" id="PessoaLaranja" type="text" size="60"  value="">
						<input id="Id_PessoaLaranja" name="Id_PessoaLaranja" type='hidden'  value="">
					</div>
					<div class="col35">
						<label  for="Id_BemTipo">*Tipo de Bem
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarBemtipo" name="imaLocalizarBemtipo" type="image"  src="./imagens/imgLocalizarPequena.png"	onclick="MostrarBuscaPadrao('Campos_Bem','BemTipo','Consulta de Bemtipo', 'Digite o Bemtipo e clique em consultar.', 'Id_BemTipo', 'BemTipo', ['BemTipo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
						</label> 
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="BemTipo" id="BemTipo" size="55" type="text" value="">
						<input id='Id_BemTipo' name='Id_BemTipo' type='hidden' value='' />
					</div>
					<div class="col15">
						<label  for="Placa">Placa</label> 
						<input class="formEdicaoInput" name="Placa" id="Placa"  type="text" size="25" value="" onkeyup=" autoTab(this,20)">
					</div>
					<div class="col30">
						<label  for="Registro">Registro</label> 
						<input class="formEdicaoInput" name="Registro" id="Registro"  type="text" size="48" value="" onkeyup=" autoTab(this,20)">
					</div>
					<div class="col30">
						<label  for="Identificacao">Identificação</label> 
						<input class="formEdicaoInput" name="Identificacao" id="Identificacao"  type="text" size="48" value="" onkeyup=" autoTab(this,20)">
					</div>
					<div class="col10">
						<label  for="Quantidade">Quantidade</label> 
						<input class="formEdicaoInput" name="Quantidade" id="Quantidade"  type="text" size="22" maxlength="22" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
					</div>
				</fieldset>	
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Bem','Info Bem','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Bem" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='120px' >Descrição</th>
					        <th width='120px' >Identificação</th>
					        <th width='120px' >Tipo</th>
					        <th width='60px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisBem =(List)Pessoadt.getListaBens();
					  						for(int i=0;lisBem!=null && i<lisBem.size();i++){
					  							PessoaBemDt dt =(PessoaBemDt)lisBem.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getDescricao()%></td>
							<td ><%=dt.getIdentificacao()%></td>							
							<td ><%=dt.getBemTipo()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Bem',<%=dt.getId()%>,'Info Bem')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Bem',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Bem',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>
<!--CONTA---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divConta" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Conta"> 					
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						
						<legend class="formEdicaoLegenda">Edita Contas</legend>
						<div class="col15">						
							<label  for="id">Identificador</label> 
							<input class="formEdicaoInputSomenteLeitura" name="Id" id="Id"  type="text" size="10" readonly="true" value="">
						</div>
						<div class="col60">
							<label  for="Id_PessoaLaranja">Proprietário
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarPessoa" name="imaLocalizarPessoa" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Conta','Pessoa','Consulta de Pessoa', 'Digite o Pessoa e clique em consultar.', 'Id_PessoaLaranja', 'PessoaLaranja', ['Pessoa'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
							</label>  
							<input id="Id_PessoaLaranja" name="Id_PessoaLaranja" type='hidden'  value="">
							<input  class="formEdicaoInputSomenteLeitura" readonly="true" name="PessoaLaranja" id="PessoaLaranja" size='96' type="text" value="">
						</div> <br />
						<div class="col25">
							<label  for="Banco">*Banco</label> 
							<input class="formEdicaoInput" name="Banco" id="Banco"  type="text" size="38" value="" onkeyup=" autoTab(this,60)">
						</div>
						<div class="col25">
							<label  for="Conta">*Conta</label> 
							<input class="formEdicaoInput" name="Conta" id="Conta"  type="text" size="38" value="" onkeyup=" autoTab(this,60)">
						</div>
						<div class="col25">
							<label  for="Agencia">*Agência</label> 
							<input class="formEdicaoInput" name="Agencia" id="Agencia"  type="text" size="38"  value="" onkeyup=" autoTab(this,20)">
						</div>
						<div class="col20">
							<label  for="ContaTipo">Conta Tipo</label> 
							<input class="formEdicaoInput" name="ContaTipo" id="ContaTipo"  type="text" size="38" value="" onkeyup=" autoTab(this,20)">
						</div>
						<div class="col55">
							<label  for="Observacao">Observação</label> 
							<input class="formEdicaoInput" name="Observacao" id="Observacao"  type="text" size="120" value="" onkeyup=" autoTab(this,255)">
						</div>
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Conta','Info Conta','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Conta" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='60px' >Banco</th>
					        <th width='30px' >Agência</th>
					        <th width='30px' >Conta</th>
					        <th width='30px' >Tipo</th>
					        <th width='60px' >Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisConta =(List)Pessoadt.getListaContas();
					  						for(int i=0;lisConta!=null && i<lisConta.size();i++){
					  							PessoaContaDt dt =(PessoaContaDt)lisConta.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getBanco()%></td>
							<td ><%=dt.getAgencia()%></td>
							<td ><%=dt.getConta()%></td>
							<td ><%=dt.getContaTipo()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Conta',<%=dt.getId()%>,'Info Conta')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Conta',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Conta',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>	
<!--IMÓVEL---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<div id="divImovel" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Imovel">
						<input id='Id_Pessoa' name='Id_Pessoa' type='hidden' value='<%=Pessoadt.getId()%>' />
						<input id='Id_EnderecoImovel' name='Id_EnderecoImovel' type='hidden' value='' />
						<legend class="formEdicaoLegenda">Edita Imóveis</legend>
						<div class='col15'>
							<label  for="id">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id"  id="Id"  type="text"  readonly="true" value="">
						</div>
						<div class='col55'>
							<label  for="Id_Pessoa">Proprietário <input class="FormEdicaoimgLocalizar" id="imaLocalizarPessoa" name="imaLocalizarPessoa" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Imovel','Pessoa','Consulta de Pessoa', 'Digite o Pessoa e clique em consultar.', 'Id_PessoaLaranja', 'PessoaLaranja', ['Nome'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" ></label>
							<input name='Id_PessoaLaranja' id='Id_PessoaLaranja' type='hidden'  value=''>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="PessoaLaranja" id="PessoaLaranja" type="text" size="100" value=""> 
						</div>
 						<div class='col55'>
							<label  for="Logradouro">*Logradouro</label>
							<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="100" maxlength="100" value="" onKeyUp=" autoTab(this,60)">
						</div>
						<div class='col10'>
							<label  for="Numero">Número</label> 
							<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="11" maxlength="10" value="" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
						</div>
						<div class='col35'>
							<label  for="Complemento">Complemento</label>  
							<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="59" maxlength="100" value="" onKeyUp=" autoTab(this,255)">
						</div>
						<div class='col35'>
							<label  for="Bairro">*Bairro <input class="FormEdicaoimgLocalizar" id="imaLocalizarFaccao" name="imaLocalizarFaccao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Imovel','Pessoa','Consulta de Bairro', 'Digite o Bairro e clique em consultar.', 'Id_Bairro', 'Bairro', ['Bairro','Cidade','UF'], ['Cidade', 'Uf', '', ''], '<%=(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" ></label>
							<input id='Id_Bairro' name='Id_Bairro' type='hidden' value='' />
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="80" maxlength="100" value="">
						</div> 
						<div class='col30'>
							<label  for="Cidade">Cidade</label>
							<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="40" maxlength="60" value="">
						</div>
						<div class='col10'>
							<label  for="Uf">UF</label> 
							<input class="formEdicaoInputSomenteLeitura" readonly name="Uf" id="Uf"  type="text" size="10" maxlength="10" value=""> <br>
						</div>
						<div class='col15'>
							<label  for="Cep">*CEP</label> 
							<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)"> <br>
						</div>
						<div class='col30'>
							<label  for="Imovel">*Tipo de Imóvel<input class="FormEdicaoimgLocalizar" id="imaLocalizarFaccao" name="imaLocalizarFaccao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Imovel','ImovelTipo','Consulta de Tipo de Imovel', 'Digite o Tipo do Imóvel e clique em consultar.', 'Id_ImovelTipo', 'ImovelTipo', ['Tipo de Imóvel'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" ></label>
							<input id='Id_ImovelTipo' name='Id_ImovelTipo' type='hidden' value='' />
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ImovelTipo" id="ImovelTipo" type="text" size="80" maxlength="100" value=""> <br>
						</div>
						
					</fieldset>
					<div class="Centralizado">
						<input type="button" value="Salvar" onclick="salvarPasso('Pessoa','Campos_Imovel','Info Imóvel','sim')">
					</div>
				</div>	
				<div class="divTabela"> 
					<table  id="tb_Campos_Imovel" class="Tabela">
					    <thead>
					      <tr>
					      	<th class="colunaMinima" >Id</th>
					        <th width='40px' >Imóvel</th>
					        <th width='60px'>Proprietário</th>
					        <th width='120px'>Endereço</th>
					        <th width='120px' >Complemento</th>
					        <th width='60px'>Data Cadastro</th>
					        <th class="colunaMinima">info</th>
					        <th class="colunaMinima">Editar</th>
					        <th class="colunaMinima">Excluir</th> 
					      </tr>
					    </thead>
					  	<tbody>
					  	<%
					  		List lisImovel =(List)Pessoadt.getListaImoveis();
					  						for(int i=0;lisImovel!=null && i<lisImovel.size();i++){
					  							PessoaImovelDt dt =(PessoaImovelDt)lisImovel.get(i);
					  	%>
							<tr id='tr_<%=dt.getId()%>' campos='<%=dt.toJson()%>' >
							<td class="colunaMinima" ><%=dt.getId()%></td>
							<td ><%=dt.getImovelTipo()%></td>
							<td ><%=dt.getPessoaLaranja()%></td>
							<td ><%=dt.getLogradouro()%></td>
							<td ><%=dt.getComplemento()%></td>
							<td ><%=dt.getDataCadastro()%></td>
							<td class="colunaMinima" ><img src="./imagens/important_blue.png" alt="Info" onclick="infoPasso('Campos_Imovel',<%=dt.getId()%>,'Info Imóvel')"></td>
							<td class="colunaMinima" ><img src="./imagens/imgEditar.png" alt="Editar" onclick="editarPasso('Campos_Imovel',<%=dt.getId()%>)"></td>
							<td class="colunaMinima" > <img src="./imagens/imgExcluir.png" alt="remover" onclick="excluirPasso('Pessoa','Campos_Imovel',<%=dt.getId()%>)"></td>
							</tr> 
						<%}%>
					  	</tbody>
					</table>
				</div>	
			</div>			
<%}%>
			<div id="divProcesso" >
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="Campos_Visita">						
						<legend class="formEdicaoLegenda">Processos</legend>
						<div class="divTabela"> 
							<table class="Tabela">
							    <thead>
							      <tr>
							      	<th class="colunaMinima" >Id</th>
							        <th width='60px' >Processo</th>
							        <th width='60px' >Serventia</th>
							        <th width='40px' >Tipo</th>
							        <th width='40px' >Status</th>
							        <th width='40px' >Data Cadastro</th>
							      </tr>
							    </thead>
							  	<tbody>
							  	<%List lisProcessos =(List)Pessoadt.getListaProcessos();
									for(int i=0;lisProcessos!=null && i<lisProcessos.size();i++){
										ProcessoDt dt =(ProcessoDt)lisProcessos.get(i); %>
									<tr>
									<td class="colunaMinima" ><%=dt.getId()%></td>
									<td ><a href="BuscaProcessoUsuarioExterno?Id_Processo=<%=dt.getId_Processo()%>&PassoBusca=2"><%=dt.getProcessoNumero()%></a></td>
									<td ><%=dt.getServentia()%></td>
									<td ><%=dt.getProcessoTipo()%></td>
									<td ><%=dt.getProcessoStatus()%></td>
									<td ><%=dt.getDataRecebimento()%></td>
									</tr> 
								<%}%>
							  	</tbody>
							</table>
						</div>
					</fieldset>
				</div>	
			</div>	
		</div>			
	</div>	
		<script language="javascript" type="text/javascript">
		$(document).ready(function(){
			$("#abas").tabs({ active: <%= request.getSession().getAttribute("ultimaAba")%> });
			calcularTamanhoIframe();
		});
	</script>
	<%}%>
	<%@ include file="Padroes/Mensagens.jspf" %>
	
</body>
</html>