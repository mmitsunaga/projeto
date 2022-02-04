<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt"%>

<jsp:useBean id="Serventiadt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaDt"/>
<jsp:useBean id="ServAreaDistDt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt"/>

<html>
	<head>
		<title>Serventia</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	  	<script language="javascript" type="text/javascript" src="./js/Digitacao/EvitarEspacoDuplo.js" ></script>
	</head>
	
	<script language="javascript" type="text/javascript">
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
	
    	function VerificarCampos() {
    		if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
       			with(document.Formulario) {
             		if (Serventia.value == "") {
		         		alert(" O Campo Descrição é obrigatório!");
		         		Serventia.focus();
		         		return false; 
		     		}
		     		if (ServentiaCodigo.value == "") {
		         		alert(" O Campo Código é obrigatório!");
		         		ServentiaCodigo.focus();
		         		return false; 
		     		}		     		
		     		if (QuantidadeDistribuicao.value == "") {
		         		alert(" O Campo Quantidade de Distribuicao é obrigatório!");
		         		QuantidadeDistribuicao.focus();
		         		return false; 
		     		}
		     		if (DataImplantacao.value == "") {
		         		alert(" O Campo Data de Implantação é obrigatório!");
		         		DataImplantacao.focus();
		         		return false; 
		     	    }
             		if (Logradouro.value == "") {
		         		alert(" O Campo Logradouro é obrigatório!");
		         		Logradouro.focus();
		         		return false; 
		     		}             		
             		if (Cep.value == "") {
		         		alert(" O Campo Cep é obrigatório!");
		         		Cep.focus();
		         		return false; 
		     	    }             		
              	submit();
       			}
       		}
     	}
    	
    	<%}%>
    	   	
		function processaHabilitacaoSubstituicao(checkSubstituicao){							
			if (checkSubstituicao!=null && checkSubstituicao.checked){	
				// habilita combo ServentiaSubstituicao			
				$("#ServentiaSubstituicao").removeAttr("disabled");
				$("#ServentiaSubstituicao").attr("style", "");
				$("#ServentiaSubstituicao").attr("class", "formEdicaoCombo");
				// desmarca e desabilita check RecebeProcesso
				$("#RecebeProcesso").attr("checked", false);
				$("#RecebeProcesso").attr("disabled", "disabled");
				// Muda label serventia relacionada	
				$("#labelServentiaRelacionada").html("Gabinete Substituto");	
				// Mostrar o fieldset substituição
				Mostrar('fieldsetSubstituicao');											
			}else{
				// desabilita combo
				$("#ServentiaSubstituicao").attr("disabled", "disabled");
				$("#ServentiaSubstituicao").attr("style", "color: #000000; background-color: #ffffff;");
				$("#ServentiaSubstituicao").attr("class", "formEdicaoInputSomenteLeitura");
				// Limpando datas selecionadas				
				$("#dataFimSubstituicao").attr("value", "");
				$("#dataInicioSubstituicao").attr("value", "");
				// Seleciona o primeiro item (limpando seleção)
				$("#ServentiaSubstituicao").attr("value", "-1");
				// habilita check RecebeProcesso
				$("#RecebeProcesso").removeAttr("disabled");
				// Muda label serventia relacionada				
				$("#labelServentiaRelacionada").html("Serventia Relacionada");
				// Ocultar o fieldset substituição
				Ocultar('fieldsetSubstituicao');											
			}						
		}	
	</script>
	
	<script type="text/javascript">
		//<![CDATA[
		onload = function()	{		
			var check = document.getElementById('Substituicao');
			processaHabilitacaoSubstituicao(check);			
		}
		//]]>
	</script>	
	<body>
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; Cadastro de Serventia</h2></div>

			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
  			<form action="Serventia" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
  			<form action="Serventia" method="post" name="Formulario" id="Formulario">
			<%}%>
  			
  			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
  			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>" />
  			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
  			
  			<input id="idRetorno" name="idRetorno" type="hidden" value="<%=request.getAttribute("idRetorno")%>" />
  			<input id="descricaoRetorno" name="descricaoRetorno" type="hidden" value="<%=request.getAttribute("descricaoRetorno")%>" />
  			<input id="rdPresidente" name="rdPresidente" type="hidden" />
  			
		  	<div id="divEditar" class="divEditar">
	  			<div id="divPortaBotoes" class="divPortaBotoes">
	      			<%@ include file="Padroes/Botoes.jspf"%>
	      			<input id="imgEditar" class="imgEditar" title="Copiar Serventia" name="imgEditar" type="image" src="./imagens/imgEditar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
	      			<a class="divPortaBotoesLink" href="Ajuda/ServentiaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
	      			 <%if(Serventiadt.getId() != null && !Serventiadt.getId().equals("")){%>			    	
				    	<%if(Serventiadt.isAtivo()) {%>			      		
				      		<input id="imgBloquearServentia" alt="Bloquear Serventia" title="Bloquear Serventia" name="imgBloquearServentia" type="image"  src="./imagens/22x22/ico_fechar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_BLOQUEIO%>');">
				    	<%} else { %>			    		
				    		<input id="imgDesbloquearServentia" alt="Desbloquear Serventia" title="Desbloquear Serventia" name="imgDesbloquearServentia" type="image"  src="./imagens/22x22/ico_liberar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_DESBLOQUEIO%>');">
				    	<%}%> 
				  <%} %>
	  			</div>
   	
				<fieldset class="formEdicao"> 
		    		<legend class="formEdicaoLegenda">Cadastro de Serventia <%if(Serventiadt.getId() != null && !Serventiadt.getId().equals("") && !Serventiadt.isAtivo()){%> <font color="red" size="-1"><strong>&lt;Bloqueada&gt;</strong></font> <%}%> </legend>
		   
		    		<label class="formEdicaoLabel" for="Id_Serventia">Identificador</label><br> 
		    		<input class="formEdicaoInputSomenteLeitura" name="Id_Serventia" id="Id_Serventia" type="text" readonly value="<%=Serventiadt.getId()%>"/>
		    		
		    		<!-- Botao 'Visualizar Log' que retorna o log da serventia atraves da pagina ServentiaLog.jsp -->
		  			<% if( Serventiadt.getId() != null && !Serventiadt.getId().equals("") ) { %>
						<span style="float:right">
							<input float="right" type="submit" value="Visualizar Log" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" />
						</span>
		  			<% } %>
		    		
		    		<br><label class="formEdicaoLabel" for="Serventia">*Descrição</label><br> 
		    		<input class="formEdicaoInput" name="Serventia" id="Serventia"  type="text" size="74" maxlength="60" value="<%=Serventiadt.getServentia()%>" onkeyup="autoTab(this,60);return EvitarEspacoDuplo(this)"/><br />
		    		
		    		<div class="col25">
		    		<label class="formEdicaoLabel" for="ServentiaCodigo">*Código</label><br> 
		    		<input class="formEdicaoInput" name="ServentiaCodigo" id="ServentiaCodigo" type="text" size="15" maxlength="10" value="<%=Serventiadt.getServentiaCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,10)">
					</div>
		    		
		    		<div class="col25">
		    		<label class="formEdicaoLabel" for="ServentiaCodigoExterno">*Código Externo</label><br> 
		    		<input class="formEdicaoInput" name="ServentiaCodigoExterno" id="ServentiaCodigoExterno"  type="text" size="15" maxlength="10" value="<%=Serventiadt.getServentiaCodigoExterno()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,10)"/>
		    		</div>
		    		
		    		<div class="col25">
		    		<label class="formEdicaoLabel" for="Id_CNJServentia">Código CNJ</label><br> 
		    		<input class="formEdicaoInput" name="Id_CNJServentia" id="Id_CNJServentia"  type="text" size="20" maxlength="20" value="<%=Serventiadt.getId_CNJServentia()%>" onkeyup=" autoTab(this,11)"/><br />
		    		</div>
		    		
		    		<div class="col45 clear">
		    		<label class="formEdicaoLabel" for="Id_ServentiaTipo">*Tipo Serventia  
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaTipo" name="imaLocalizarServentiaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((ServentiaTipoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" > 
		    		</label><br>
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="ServentiaTipo" id="Id_ServentiaTipo" type="text" size="40" maxlength="70" value="<%=Serventiadt.getServentiaTipo()%>"/>
		    		</div>
		    		
		    		<div class="col45">
		    		<label class="formEdicaoLabel" for="Id_ServentiaSubtipo">Sub-Tipo Serventia  
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaSubtipo" name="imaLocalizarServentiaSubtipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((ServentiaSubtipoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    		<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaSubtipo" name="imaLimparServentiaSubtipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaSubtipo','ServentiaSubtipo'); return false;" > 
		    		</label><br>
		    		<input  name='Id_ServentiaSubtipo' id='Id_ServentiaSubtipo' type='hidden'  value=''> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="ServentiaSubtipo" id="ServentiaSubtipo" type="text" size="48" maxlength="60" value="<%=Serventiadt.getServentiaSubtipo()%>"/><br />
		    		</div>
		    		
		    		<div class="col45 clear">
		    		<label class="formEdicaoLabel" for="label_Comarca">Comarca  
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((ComarcaDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    		<input class="FormEdicaoimgLocalizar" id="imaLimparComarca" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" > 
		    		</label><br>
		    		<input  name='Id_Comarca' id='Id_Comarca' type='hidden'  value='<%=Serventiadt.getId_Comarca()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura"  readonly name="Comarca" id="Comarca" type="text" size="36" maxlength="60" value="<%=Serventiadt.getComarca()%>"/>
		    		</div>
		    		<br>
		    		
		    		<div class="col45">
		    		<label class="formEdicaoLabel" for="label_AreaDistribuicao">1ª Área de Distribuição Recursal
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempFluxo1','<%="AD2G"%>'); AlterarValue('idRetorno', 'Id_AreaDistribuicaoPrimaria'); AlterarValue('descricaoRetorno', 'AreaDistribuicaoPrimaria'); AlterarValue('PaginaAtual','<%=String.valueOf(((AreaDistribuicaoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    		<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AreaDistribuicaoPrimaria','AreaDistribuicaoPrimaria'); return false;" > 
		    		</label><br> 
		    		<input  name='Id_AreaDistribuicaoPrimaria' id='Id_AreaDistribuicaoPrimaria' type='hidden'  value='<%=Serventiadt.getId_AreaDistribuicao()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicaoPrimaria" id="AreaDistribuicaoPrimaria" type="text" size="44" maxlength="100" value="<%=Serventiadt.getAreaDistribuicao()%>"/><br />
		    		</div>
		    		
		    		<div class="col45">
		    		<label class="formEdicaoLabel" for="label_AreaDistribuicao">2ª Área de Distribuição Recursal
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempFluxo1','<%="ADT"%>'); AlterarValue('idRetorno', 'Id_AreaDistribuicaoSecundaria'); AlterarValue('descricaoRetorno', 'AreaDistribuicaoSecundaria'); AlterarValue('PaginaAtual','<%=String.valueOf(((AreaDistribuicaoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    		<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AreaDistribuicaoSecundaria','AreaDistribuicaoSecundaria'); return false;" > 
		    		</label><br> 
		    		<input  name='Id_AreaDistribuicaoSecundaria' id='Id_AreaDistribuicaoSecundaria' type='hidden'  value="<%=Serventiadt.getId_AreaDistribuicaoSecundaria()%>" /> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="AreaDistribuicaoSecundaria" id="AreaDistribuicaoSecundaria" type="text" size="44" maxlength="100" value="<%=Serventiadt.getAreaDistribuicaoSecundaria()%>" /><br />
		    		</div>		    		

		    		<div class="col25 clear">
		    		<label class="formEdicaoLabel" for="Id_Area">Área  
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarArea" name="imaLocalizarArea" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((AreaDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    		<input class="FormEdicaoimgLocalizar" id="imaLimparArea" name="imaLimparArea" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Area','Area'); return false;" > 
		    		</label><br>
		    		<input  name='Id_Area' id='Id_Area' type='hidden'  value=''> 
		    		<input class="formEdicaoInputSomenteLeitura"  readonly name="Area" id="Area" type="text" size="30" maxlength="30" value="<%=Serventiadt.getArea()%>"/>
		    		</div>
		    		
		    		<div class="col45 ">
		    		<label class="formEdicaoLabel" for="Id_AudienciaTipo">Tipo Audiência 
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaTipo" name="imaLocalizarAudienciaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((AudienciaTipoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    		<input class="FormEdicaoimgLocalizar" id="imaLimparAudienciaTipo" name="imaLimparAudienciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AudienciaTipo','AudienciaTipo'); return false;" > 
		    		</label><br> 
		    		<input  name='Id_AudienciaTipo' id='Id_AudienciaTipo' type='hidden'  value=''> 
		    		<input class="formEdicaoInputSomenteLeitura"  readonly name="AudienciaTipo" id="AudienciaTipo" type="text" size="47" maxlength="100" value="<%=Serventiadt.getAudienciaTipo()%>"/><br />
		    		</div>
		    		
		    		<div class="col25">
				    <label class="formEdicaoLabel" for="Id_Estado">*Representatividade 
				    <input class="FormEdicaoimgLocalizar" id="imaLocalizarEstado" name="imaLocalizarEstado" type="image" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((EstadoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
				   </label><br> 
				    <input class="formEdicaoInputSomenteLeitura"  readonly name="Estado" id="Id_Estado" type="text" size="8" maxlength="20" value="<%=Serventiadt.getEstadoRepresentacao()%>"/><br />    		
				    </div>
				    
<!-- 				    <div class="col25 clear"> -->
<!-- 			    		<label class="formEdicaoLabel" for="QuantidadeDistribuicao">*Quantidade Distribuição</label><br> -->
<!-- 			    		<select id="QuantidadeDistribuicao" name ="QuantidadeDistribuicao" class="formEdicaoCombo"> -->
<%-- 			    			<%String QuantidadeDistribuicao = Serventiadt.getQuantidadeDistribuicaoDescricao();//selected='selected'%> --%>
<%-- 							<option value="0" <%=(QuantidadeDistribuicao.equalsIgnoreCase("0%")?"selected='selected'":"")%>>0%</option>						 --%>
<%-- 							<option value="0,1" <%=(QuantidadeDistribuicao.equalsIgnoreCase("10%")?"selected='selected'":"")%>>10%</option> --%>
<%-- 							<option value="0,2" <%=(QuantidadeDistribuicao.equalsIgnoreCase("20%")?"selected='selected'":"")%>>20%</option> --%>
<%-- 							<option value="0,3" <%=(QuantidadeDistribuicao.equalsIgnoreCase("30%")?"selected='selected'":"")%>>30%</option> --%>
<%-- 							<option value="0,4" <%=(QuantidadeDistribuicao.equalsIgnoreCase("40%")?"selected='selected'":"")%>>40%</option> --%>
<%-- 							<option value="0,5" <%=(QuantidadeDistribuicao.equalsIgnoreCase("50%")?"selected='selected'":"")%>>50%</option> --%>
<%-- 							<option value="0,6" <%=(QuantidadeDistribuicao.equalsIgnoreCase("60%")?"selected='selected'":"")%>>60%</option> --%>
<%-- 							<option value="0,7" <%=(QuantidadeDistribuicao.equalsIgnoreCase("70%")?"selected='selected'":"")%>>70%</option> --%>
<%-- 							<option value="0,8" <%=(QuantidadeDistribuicao.equalsIgnoreCase("80%")?"selected='selected'":"")%>>80%</option> --%>
<%-- 							<option value="0,9" <%=(QuantidadeDistribuicao.equalsIgnoreCase("90%")?"selected='selected'":"")%>>90%</option> --%>
<%-- 							<option value="1" <%=(QuantidadeDistribuicao.equalsIgnoreCase("100%")?"selected='selected'":"")%>>100%</option> --%>
<!-- 						</select> -->
<!-- 		    		</div> -->
		    			    		 
		    		<div class="col20">
		    		<label class="formEdicaoLabel" for="Telefone">Telefone</label><br> 
		    		<input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="20" maxlength="15" value="<%=Serventiadt.getTelefone()%>" onkeyup=" autoTab(this,30)"/>
		    		</div>
		    		
		    		<div class="col15">
		    		<label for="DataImplantacao">*Data de Implantação <img id="calendarioDataImplantacao" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataImplantacao,'dd/mm/yyyy',this)"/></label> 
					<input class="formEdicaoInput" name="DataImplantacao" id="DataImplantacao"  type="text" size="10" maxlength="10" value="<%=Serventiadt.getDataImplantacao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
		    		</div>
		    		
   					<div class="col45"> 	
						<label class="formEdicaoLabel" for="Email">E-mail</label><br> 
						<input class="formEdicaoInput" name="Email" id="Email"  type="text" size="59" maxlength="100" value="<%=Serventiadt.getEmail()%>" onKeyUp=" autoTab(this,255)">
					</div>
		    		
		    		
		    		<br>		    		
		    		<div class="clear">&nbsp;&nbsp;
		    		<label for="ConclusoDireto">Concluso Direto?</label>&nbsp;&nbsp;&nbsp;		    		
		    		<input type="radio" name="ConclusoDireto" id="ConclusoDireto" value="1" <% if(Serventiadt.getConclusoDireto().equalsIgnoreCase("1")){%> checked="true" <%}%>/>Sim 
				    <input type="radio" name="ConclusoDireto" id="ConclusoDireto" value="0" <% if(Serventiadt.getConclusoDireto().equalsIgnoreCase("0")){%> checked="true" <%}%>/>Não
			    	</div>
			    	
			    	<div class="clear">&nbsp;&nbsp;			    	 	
		    		<label for="RecebeProcessoQtdDist">Recebe Processo?</label>&nbsp;	    				    		
		    		<input type="radio" name="RecebeProcessoQtdDist" id="RecebeProcessoQtdDist" value="1" <% if(Serventiadt.getQuantidadeDistribuicao().equalsIgnoreCase("1")){%> checked="true" <%}%>/>Sim 
				    <input type="radio" name="RecebeProcessoQtdDist" id="RecebeProcessoQtdDist" value="0" <% if(Serventiadt.getQuantidadeDistribuicao().equalsIgnoreCase("0")){%> checked="true" <%}%>/>Não
					</div>
					
					<%@ include file="Padroes/CadastroEndereco.jspf"%>
					
		    		<%if (Serventiadt.getId() != null && !Serventiadt.getId().equalsIgnoreCase("")) {%>
		    		
			    		<fieldset class="formEdicao"> 
			    			<legend class="formEdicaoLegenda">Área(s) Distribuição(ões) Serventia</legend/>
			    			<div class="col90">
				    			<input id="imgNovo"  class="imgNovo" title="Novo - Limpa o campo Área Distribuição" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_AREA_DISTRIBUICAO%>')" >
				   				<input id="imgsalvar" class="imgsalvar" title="salvar - Salva Área Distribuição" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_AREA_DISTRIBUICAO%>')" >
			    			</div>
			    			<label class="formEdicaoLabel" for="label_AreaDistribuicao">Área de Distribuição
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempFluxo1','<%="LAS"%>'); AlterarValue('idRetorno', 'Id_AreaDistribuicaoServentia'); AlterarValue('descricaoRetorno', 'AreaDistribuicaoServentia'); AlterarValue('PaginaAtual','<%=String.valueOf(((AreaDistribuicaoDt.CodigoPermissao - ServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))%>');" >  
			    			<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AreaDistribuicaoServentia','AreaDistribuicaoServentia'); return false;" > 
			    			</label><br>  
			    			<input  name='Id_AreaDistribuicaoServentia' id='Id_AreaDistribuicaoServentia' type='hidden'  value='<%=ServAreaDistDt.getId()%>'> 
			    			<input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicaoServentia" id="AreaDistribuicaoServentia" type="text" size="60" maxlength="100" value="<%=ServAreaDistDt.getAreaDist()%>"/>
			    			
				    		<%if (!Serventiadt.getId().equalsIgnoreCase("")) { %>
				    			<div class="col25 clear">
					    		<label class="formEdicaoLabel" for="QuantidadeDistribuicao">*Quantidade Distribuição</label><br>
					    		<select id="QuantidadeDistribuicao" name ="QuantidadeDistribuicao" class="formEdicaoCombo">
					    			<%String QuantidadeDistribuicao = ServAreaDistDt.getQuantidadeDistribuicaoDescricao();//selected='selected'%>
									<option value="0" <%=(QuantidadeDistribuicao.equalsIgnoreCase("0%")?"selected='selected'":"")%>>0%</option>
									<option value="0,1" <%=(QuantidadeDistribuicao.equalsIgnoreCase("10%")?"selected='selected'":"")%>>10%</option>
									<option value="0,2" <%=(QuantidadeDistribuicao.equalsIgnoreCase("20%")?"selected='selected'":"")%>>20%</option>
									<option value="0,3" <%=(QuantidadeDistribuicao.equalsIgnoreCase("30%")?"selected='selected'":"")%>>30%</option>
									<option value="0,4" <%=(QuantidadeDistribuicao.equalsIgnoreCase("40%")?"selected='selected'":"")%>>40%</option>
									<option value="0,5" <%=(QuantidadeDistribuicao.equalsIgnoreCase("50%")?"selected='selected'":"")%>>50%</option>
									<option value="0,6" <%=(QuantidadeDistribuicao.equalsIgnoreCase("60%")?"selected='selected'":"")%>>60%</option>
									<option value="0,7" <%=(QuantidadeDistribuicao.equalsIgnoreCase("70%")?"selected='selected'":"")%>>70%</option>
									<option value="0,8" <%=(QuantidadeDistribuicao.equalsIgnoreCase("80%")?"selected='selected'":"")%>>80%</option>
<%-- 									<option value="0,9" <%=(QuantidadeDistribuicao.equalsIgnoreCase("90%")?"selected='selected'":"")%>>90%</option> --%>
<%-- 									<option value="1" <%=(QuantidadeDistribuicao.equalsIgnoreCase("100%")?"selected='selected'":"")%>>100%</option> --%>
								</select>
				    			</div>		    
			    			<%}%><br />
							<%if (Serventiadt.getListaAreasDistribuicoes() != null) { %>
			    	  		<%@ include file="AreaDistribuicaoServentiaEditar.jspf"%> 
			    			<%}%> 
			    			<%if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase(ServentiaDt.FLUXO_AREA_DISTRIBUICAO) ){%>
			  					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
			     		 			<div id="divSalvar" class="divSalvar" class="divsalvar">
			          					<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_AREA_DISTRIBUICAO%>')" /><br /> 
			           					<div class="divMensagemsalvar">Clique para Salvar Área Distribuição </div> 
			     		 			</div>
			 					<%}%>
			 					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {%> 	  
			    					<div id="divExcluir" class="divExcluir" class="divexcluir">
			        					<input class="imgexcluir" type="image" src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_AREA_DISTRIBUICAO%>')"  /><br />
			           					<div class="divMensagemexcluir">Clique para confirmar a exclusão da Área Distribuição </div>
			      					</div>
			 					<%}%>
			  				<%}%>
			    		</fieldset>
			    		<br />
			    		<fieldset class="formEdicao">			    			
			    			<legend class="formEdicaoLegenda">Serventia(s) Relacionada(s)</legend>
			    			<input name="Id_ServentiaRelacionada" id="Id_ServentiaRelacionada" type="hidden" value="<%=Serventiadt.getId_ServentiaRelacacionadaEdicao()%>">
			    			<input name="Id_ServentiaRelacao" id="Id_ServentiaRelacao" type="hidden" value="<%=Serventiadt.getId_ServentiaRelacaoEdicao()%>">
			    			<div class="col90">
			    				<input id="imgNovo"  class="imgNovo" title="Novo - Limpa o campo Serventia Relacionada" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_RELACIONADA%>')" >
			   					<input id="imgsalvar" class="imgsalvar" title="salvar - Salva Serventia Relacionada" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_RELACIONADA%>')" >
			    			</div>
			    			<div class="col35">
			    				<label for="ServentiaRelacionada"><span id="labelServentiaRelacionada">Serventia Relacionada</span>
			    				<%if (!Serventiadt.isEdicaoServentiaRelacionada()) {%>  
			    					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaRelacionada" name="imaLocalizarServentiaRelacionada" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>');AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_RELACIONADA%>')" >			    		
				    			<%} %> 
			    				</label>
			    				<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaRelacionada" id="ServentiaRelacionada" type="text" size="60" maxlength="60" value="<%=Serventiadt.getServentiaRelacacionadaEdicao()%>">
			    			</div> 
			    			
			    			<div class="col15"> 
			    				<label class="formEdicaoLabel" for="Estado">Representatividade</label><br> 
			    				<input class="formEdicaoInputSomenteLeitura"  readonly name="EstadoServentiaRelacionada" id="EstadoServentiaRelacionada" type="text" size="10" maxlength="10" value="<%=(Serventiadt.getServentiaDtRelacaoEdicao() != null ? Serventiadt.getServentiaDtRelacaoEdicao().getEstadoRepresentacao() : "")%>">   			
			    			</div>
				    		
				    		<%if (!Serventiadt.getId().equalsIgnoreCase("")) { %>			   				
			   					<%
			   							   						if ((Serventiadt.isSegundoGrau() || Serventiadt.isUPJs()) 
			   							   							   					&& Serventiadt.isEdicaoServentiaRelacionada() 
			   							   						   						&& Serventiadt.isGabineteServentiaRelacionada()){
			   							   					%>
					   				<br />
					   				<div class="col10">
					   					<label for="Probabilidae">
					   						Probabilidade(%)
					   					</label> 
										<select id="Probabilidade" name ="Probabilidade" class="formEdicaoCombo">
											<%
												String stProbabilidade = Serventiadt.getServentiaDtRelacaoEdicao().getProbabilidadeFormatada();
											%>
											<option value="<%=stProbabilidade%>" selected='selected'><%=stProbabilidade%></option>
											<option value="0,50">0,50</option>
											<option value="0,55">0,55</option>
											<option value="0,60">0,60</option>
											<option value="0,65">0,65</option>
											<option value="0,70">0,70</option>
											<option value="0,75">0,75</option>
											<option value="0,80">0,80</option>
										</select>
					   				</div>
					   				<div class="col15">
					   					<label for="RecebeProcesso"> 
					   						<input class="formEdicaoInput" name="RecebeProcesso" id="RecebeProcesso"  type="checkbox"  value="true" <%if(Serventiadt.getServentiaDtRelacaoEdicao().isRecebeProcesso()){%>  checked<%}%>/>
					   						Recebe Processo
					   					</label> 
					   				</div>
					    			<%
					    				if (Serventiadt.getListaServentiasRelacoes() != null && Serventiadt.getListaServentiasRelacoes().size() > 0) {
					    			%>
					    			<div class="col15">
					    				<label class="formEdicaoLabel" for="Substituicao">
					    					<input class="formEdicaoInput" name="Substituicao" id="Substituicao"  type="checkbox"  value="true" <%if(Serventiadt.getServentiaDtRelacaoEdicao().isSubstitutoSegundoGrau()){%>  checked<%}%> onchange="processaHabilitacaoSubstituicao(this)" />
					    					Substituição</label> 
					    			</div> <br />
					    			<fieldset id="fieldsetSubstituicao" class="formLocalizar"> 
										<legend class="formEdicaoLegenda">Gabinete a ser Substituído </legend>
										<div class="col10">
											<label for="dataInicioSubstituicao">*Data Inicial <img id="calendarioDataInicioSubstituicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicioSubstituicao,'dd/mm/yyyy',this)"/> </label> 
											<input class="formEdicaoInput" name="dataInicioSubstituicao" id="dataInicioSubstituicao"  type="text" size="10" maxlength="10" value="<%=Serventiadt.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
										</div> 
										<div class="col10">
											<label for="dataFimSubstituicao">*Data Final <img id="calendarioDataFimSubstituicao"/ src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFimSubstituicao,'dd/mm/yyyy',this)"/> </label> 
											<input class="formEdicaoInput" name="dataFimSubstituicao" id="dataFimSubstituicao"  type="text" size="10" maxlength="10" value="<%=Serventiadt.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
										</div> 
										
										<div class="col30">
											<label for="ServentiaSubstituicao">*Gabinete Principal </label>
				    						<select id="Id_ServentiaSubstituicao" name ="Id_ServentiaSubstituicao" >
				    							<%
				    								ServentiaRelacionadaDt serventiaDtRelacaoEdicao = Serventiadt.getServentiaDtRelacaoEdicao();
				    									    							if(serventiaDtRelacaoEdicao.hasServentiaSubstituicao()){
				    							%>
				    								<option value="<%=serventiaDtRelacaoEdicao.getId_ServentiaSubstituicao()%>" selected><%=serventiaDtRelacaoEdicao.getServentiaSubstituicao()%></option>
				    							<%
				    								}else{
				    							%>
				    								<option value="-1">--SELECIONE O GABINETE A SER SUBSTITUÍDO--</option>
				    							<%
				    								}
				    							%>
				    							  	<%
				    							  		ServentiaRelacionadaDt objTemp1;
				    							  												  for(int i = 0 ; i < Serventiadt.getListaServentiasRelacoes().size();i++) {
				    							  													objTemp1 = (ServentiaRelacionadaDt)Serventiadt.getListaServentiasRelacoes().get(i);
				    							  													if (objTemp1.isGabineteDesembargadorServentiaRelacionada() && !serventiaDtRelacaoEdicao.isMesmoId(objTemp1.getId()) && !objTemp1.isSubstitutoSegundoGrau()){
				    							  	%>
															<option value="<%=objTemp1.getId_ServentiaRelacao()%>"><%=objTemp1.getServentiaRelacao()%></option>
														<%
															}
														%>
				    							     <%
				    							     	}
				    							     %>
				    						</select>
			    						</div>
									</fieldset>	
					    			<%
						    				}
						    			%>
					   				<br />	 
				   				<%
	 				   					}
	 				   				%>
			    			<%
			    				}
			    			%>	 
			    			<%
	 			    				if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA) ){
	 			    			%>
					  					<%
					  						if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {
					  					%> 	
					     		 			<div id="divSalvar" class="divSalvar" class="divsalvar">			          					
					          					<%
			          										          						if(request.getAttribute("mensagemServentiaPresidencia") != null && request.getAttribute("mensagemServentiaPresidencia").toString().trim().length() > 0) {
			          										          					%>
					          					<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_RELACIONADA%>')" /><br />
					          					<div class="divMensagemsalvar"><%=request.getAttribute("mensagemServentiaPresidencia")%></div>
					          					<%
					          						} else {
					          					%>
					          					<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_RELACIONADA%>')" /><br /> 
					           					<div class="divMensagemsalvar">Clique para Salvar Serventia Relacionada</div>
					           					<%
					           						}
					           					%> 
					     		 			</div>
					 					<%
					 						}
					 					%>
					 					<%
					 						if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {
					 					%> 	  
					    					<div id="divExcluir" class="divExcluir" class="divexcluir" align="center">
					    						<br />
					        					<input class="imgexcluir" type="image" src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('tempFluxo1','<%=ServentiaDt.FLUXO_SERVENTIA_RELACIONADA%>')"  /><br />
					           					<div class="divMensagemexcluir">Clique para confirmar a exclusão da Serventia Relacionada </div>
					      					</div>
					 					<%
					 						}
					 					%>
					  				<%
					  					}
					  				%>	   			
			    			<br />
							<%
								if (Serventiadt.getListaServentiasRelacoes() != null){
							%>
			    	  			<%@ include file="ServentiaRelacionadaEditar.jspf"%>
			    			<%}%>
			    		</fieldset>
		    		<%} %>		    		
		  			
		  			
				</fieldset>
			<%if (request.getAttribute("tempFluxo1").toString().equals(ServentiaDt.FLUXO_SERVENTIA) 
					|| request.getAttribute("tempFluxo1").toString().equals(ServentiaDt.FLUXO_SERVENTIA_BLOQUEIO) 
					|| request.getAttribute("tempFluxo1").toString().equals(ServentiaDt.FLUXO_SERVENTIA_DESBLOQUEIO)){%>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
   			<%}%>
   			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>
