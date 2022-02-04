<%@page contentType="text/html"%>
<%@page pageEncoding="LATIN1"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="GrupoPermissaodt" scope="session" class= "br.gov.go.tj.projudi.dt.GrupoPermissaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Grupo de Permissao</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');			
	</style>
	
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>			
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>

	<script language="javascript" type="text/javascript">
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		    function VerificarCampos() {
		    	if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
		       with(document.Formulario) {
		             if (Grupo.value == "") {
				         alert(" o Campo Grupo é obrigatório!");
				         Grupo.focus();
				         return false; 
				     }
		          
		              submit();
		       }
		       }
		     }
		    
		<%}%>
		
		function alterarPermissaoGrupo(id, id_grupo, id_perm) {
			if($('#chkEditar'+id_perm).prop('checked')){
				salvarItemSelecaoMultipla('GrupoPermissao',id_grupo,id_perm,6);
			}else{
				if (id!=undefined && id!=""){
					excluirItemSelecaoMultipla('GrupoPermissao',id,6);
				}
			}
		}
	
	</script>

</head>


<body>
  <div id="divCorpo" class="divCorpo" >
  <div class="area"><h2>&raquo; Cadastro de Permissões do Grupo</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
  <form action="GrupoPermissao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
  <form action="GrupoPermissao" method="post" name="Formulario" id="Formulario">
<%}%>
	  <input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
	  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
	  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
	  <div id="divPortaBotoes" class="divPortaBotoes">
	  	  <input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >	       
	      <input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"   src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
	      <a class="divPortaBotoesLink" href="Ajuda/UsuarioPermissaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
	  </div>
		<div id="divEditar" class="divEditar">
			<fieldset class="formEdicao"> 
		    	<legend class="formEdicaoLegenda">Edita as Permissões do Grupo </legend>
		    	<label class="formEdicaoLabel" for="Id_Grupo">*Grupo
		    	<input class="FormEdicaoimgLocalizar" name="imaLocalizarGrupo" type="image" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
		    	</label><br>  
		    	
		    	<input  class="formEdicaoInputSomenteLeitura" id="Id_Grupo"  readonly name="Grupo" type="text" size="60" maxlength="60" value="<%=GrupoPermissaodt.getGrupo()%>"/><br />
			</fieldset>
		</div>
	</form>
	
	<div id='ListaCheckBox'>		
		<%=request.getAttribute("ListaPermissao").toString() %>
	</div>
</div>
</body>
</html>

