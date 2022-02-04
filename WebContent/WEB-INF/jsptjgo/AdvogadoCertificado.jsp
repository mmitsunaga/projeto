<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt"%>

<jsp:useBean id="Advogadodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>

<html>
<head>
	<title>Advogados</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>		
</head>

<script language="javascript" type="text/javascript">
    function VerificarCampos() {
    	if (document.Formulario.PaginaAtual.value == <%=Configuracao.Salvar%>){
       		with(document.Formulario) {
		        if (SeNulo(Nome, "O Campo Nome é obrigatório!")) return false;           
	        	if (SeNulo(Sexo, "O Campo Sexo é obrigatório!")) return false;
		        if (SeNulo(DataNascimento, "O Campo Data de Nascimento é obrigatório!")) return false;
		        if (SeNulo(Rg, "O Campo Rg é obrigatório!")) return false;
		       	if (SeNulo(Cpf,"O Campo Cpf é obrigatório!")) return false;
              	submit();
       		}
       	}
	}
</script>
<body>
	
	<div id="divCorpo" class="divCorpo">
  		<div id="divTitulo" class="divTitulo"> Cadastro de Advogado/Def. Público </div>
		
		<form action="AdvogadoCertificado" method="post" name="Formulario" id="Formulario" <% if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(Configuracao.Curinga7)))  {%>OnSubmit="JavaScript:return VerificarCampos()"<%}%>>
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
		    <div id="divEditar" class="divEditar">
		  		<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Cadastro de Advogado/Def. Público</legend>
			    	<label class="formEdicaoLabel" for="Nome">*Nome</label> 
			    	<input class="formEdicaoInput" name="Nome" id="Nome"  type="text" size="60" maxlength="255" value="<%=Advogadodt.getNome()%>" onkeyup=" autoTab(this,255)" readonly="readonly"/><br />
	
					<label class="formEdicaoLabel" for="Cpf">*CPF</label> 
					<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="20" maxlength="20" value="<%=Advogadodt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" readonly="readonly"/>
			    	
					<br />
			    	
			    	<label class="formEdicaoLabel" for="Sexo">*Sexo</label> 
			    	<select name="Sexo" id="Sexo" class="formEdicaoCombo">
				        	<option>F</option>
					        <option>M</option>
				    	    <option selected><%=Advogadodt.getSexo()%></option>
				  	</select>
					<br>
					<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label>
					<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=Advogadodt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
					<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/><br />
					
			    	<label class="formEdicaoLabel" for="Rg">*RG</label>
			    	<input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="20" value="<%=Advogadodt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
			    	<br>
			    	<label class="formEdicaoLabel" for="RgOrgaoExpedidor">*Órgão Expedidor
			    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >   
			    	</label>
			    	
			    	<input class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="RgOrgaoExpedidor" type="text" size="18" maxlength="255" value="<%=Advogadodt.getRgOrgaoExpedidor()%>"/>
			    	<br>
			    	<label class="formEdicaoLabel" for="RgDataExpedicao">Data de Expedição</label>
			    	<input class="formEdicaoInput" name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" value="<%=Advogadodt.getRgDataExpedicao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
			    	<img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)">
			    	<br>
			    	<label class="formEdicaoLabel" for="Cidade">*Naturalidade
			    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			    	</label>   <br>
			    	
			    	<input  class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="60" maxlength="255" value="<%=Advogadodt.getNaturalidade()%>"/><br />
			    	
			    	<label class="formEdicaoLabel" for="EMail">E-Mail</label> <br>
			    	<input class="formEdicaoInput" name="EMail" id="EMail"  type="text" size="45" maxlength="50" value="<%=Advogadodt.getEMail()%>" onkeyup=" autoTab(this,50)"/><br />
			    	
			    	<label class="formEdicaoLabel" for="Telefone">Telefone Fixo</label>  <br>
			    	<input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="20" maxlength="30" value="<%=Advogadodt.getTelefone()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)">
			    	<br>
			    	<label class="formEdicaoLabel" for="Celular">Telefone Celular</label>  <br>
			    	<input class="formEdicaoInput" name="Celular" id="Celular"  type="text" size="20" maxlength="30" value="<%=Advogadodt.getCelular()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)"/><br />
			    	
			    	<!-- OAB's do Advogado -->
			    	<fieldset class="formEdicao">
			    		<legend class="formEdicaoLegenda">Serventia/OAB</legend>
			    		<%if (Advogadodt.getListaUsuarioServentias() != null) { %>
			    	  		<div>
							   	<div id="divTabela" class="divTabela" > 
							    	<table id="Tabela" class="Tabela">
							        	<thead>
							            	<tr class="TituloColuna">
							                	<td></td>
							                  	<td>Serventia/OAB</td>                
							                  	<td>OAB</td>
							                  	<td>Status</td>
							               	</tr>
							           	</thead>
							           	<tbody id="tabListaUsuario">
										<%
								  		List liTemp = Advogadodt.getListaUsuarioServentias();
										UsuarioServentiaOabDt objTemp;
							  			String stTempNome="";
							  			for(int i = 0 ; i< liTemp.size();i++) {
							      			objTemp = (UsuarioServentiaOabDt)liTemp.get(i); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                   <tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                   <tr class="TabelaLinha2">
											<%}%>
							                	<td> <%=i+1%></td>
							                   	
							                  	<td><%= objTemp.getServentia()%></td>               
							                   	<td><%= objTemp.getOabNumero()+"-"+objTemp.getOabComplemento()%></td>
							                   	<%if (!objTemp.isInativo()){ %>
							                   	<td>ATIVO</td>
							                   	<%} else {%>
							                   	<td> <font color="red" size="-1"><strong>INATIVO</strong></font></td>
							                   	<%}%>                    
							               </tr>
										<%}%>
							           </tbody>
							       </table>
							   </div> 
							</div>
			    		<%}%>
			    	</fieldset>
				    <%@ include file="Padroes/CadastroEndereco.jspf"%> 
				    <fieldset class="formEdicao">
				    	<legend class="formEdicaoLegenda">Termos e Condições de Uso</legend>
				    	<p>Eu, <%=Advogadodt.getNome()%>, concordo com os termos e condições de uso abaixo:</p>
				    	<p>O credenciando acima identificado aceita as condições do presente TERMO DE COMPROMISSO 
				    		para a utilização do Sistema de Processo Judicial Eletrônico de 1º Grau do Tribunal de Justiça, 
				    		<b>concordando em cumprir as normas de sua respectiva Resolução e aquelas que vierem a ser editadas 
				    		para regulamentação do uso do Sistema</b>, especialmente que:
				    	</p>
				    	<p>1. No processo judicial eletrônico deverá ser utilizado exclusivamente programa de computador (software) 
				    		do sistema aprovado pelo Poder Judiciário de Goiás. 
				    	</p>
				    	<p>2. O acesso ao Sistema, a prática de atos processuais em geral e o envio de petições e recursos, por 
				    		meio eletrônico, serão admitidos mediante uso de certificação digital (ICP-Brasil), sendo obrigatório 
				    		o credenciamento prévio. 
				    	</p>
				    	<p>3. O credenciamento é ato pessoal, direto, intransferível e indelegável, sendo os atos praticados no 
				    		Sistema de Processo Judicial Eletrônico de sua responsabilidade exclusiva. 
				    	</p>
				    	<p>4. A conclusão do credenciamento com a assinatura digital (ICP-Brasil) do termo de compromisso torna 
				    		o advogado ou procurador apto para a utilização do Sistema 
				    	</p>
				    	<p>5. Os atos gerados no Sistema serão registrados com a identificação do usuário, a data e o horário 
				    		de sua realização. 
				    	</p>
				    	<p>6. A aquisição e utilização dos equipamentos necessários ao acesso do Sistema de Processo Eletrônico, 
				    		assim como dos serviços correlatos (provedor de acesso à Internet, certificação digital etc.), 
				    		correrá por conta e risco do advogado ou procurador. 
				    	</p>
				    	<p>7. A digitalização de petições e documentos deverá ser realizada pelo próprio usuário, sendo sua a 
				    		exclusiva responsabilidade pela qualidade e/ou legibilidade dos documentos anexados ao Sistema. 
				    	</p>
				    	<p>8. Os documentos digitalizados juntados em processo eletrônico somente estarão disponíveis para acesso 
				    		por meio da rede externa para suas respectivas partes processuais e para o Ministério Público, 
				    		respeitado o disposto em lei para as situações de sigilo e de segredo de justiça. 
				    	</p>
				    	<p>9. Os documentos produzidos eletronicamente e juntados aos processos eletrônicos com garantia da origem 
				    		e de seu signatário, através de certificação digital (ICP-Brasil), serão considerados originais para 
				    		todos os efeitos legais. 
				    	</p>
				    	<p>10. É da exclusiva responsabilidade do usuário a utilização de sua assinatura digital (ICP-Brasil) para 
				    		acesso e prática de atos no Sistema, devendo adotar cautelas para preservação da senha respectiva e 
				    		respondendo por eventual uso indevido. 
				    	</p>
				    	<p>11. O sistema de Processo Eletrônico será implantado em etapas. As etapas seguintes do novo sistema serão 
				    		divulgadas, inclusive com a indicação do momento em que serão válidas e aplicadas as regras previstas 
				    		na Lei nº 11.419/2006 que disciplinam o uso de meio eletrônico para tramitação de processos judiciais, 
				    		comunicação de atos e transmissão de peças processuais. 
				    	</p>
				    	<p>12. Uma vez validado e assinado, este termo se constituirá em documento válido para todas as etapas de 
							implantação do referido sistema. 
						</p>
				    </fieldset>
				</fieldset>
			    <div id="divConfirmarSalvar" class="ConfirmarSalvar">
		        	<input id='Submeter' class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')"  > <br />
		      	</div>
			    </div>
  		</form>
  		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
