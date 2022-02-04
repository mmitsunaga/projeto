function mostrarTipificacao(id_parte, id_processo){
	MostrarOcultar('sub_tip'+id_parte);
	if($('#sub_tip'+id_parte).is(":visible")){
		$('#sub_tip'+id_parte).html(gerarHtmlTipificacao(id_parte,id_processo));
		atualizarTabelaPasso('ProcessoParteAssunto', 'sub_tip'+id_parte, undefined,undefined);			
	}
}

function gerarHtmlTipificacao(id_parte, id_processo){		
	var stHtml = "";
	stHtml +=" <fieldset class='VisualizaDados' ><legend>  Tipifica&ccedil;&atilde;o / Assunto</legend>";	
	stHtml+="	<input type='hidden' id='id_processo_parte' name='id_processo_parte' value='"+id_parte+"' />";
	stHtml+="	<input id='Id_Assunto' name='Id_Assunto' type='hidden' value='' />";
	stHtml+="	<input class='formEdicaoInputSomenteLeitura' id='Assunto'  name='Assunto' type='hidden'  value=''  onclick=\"salvarPasso('ProcessoParteAssunto','sub_tip"+id_parte+"',undefined,undefined)\" />";					
	stHtml+="	<input type='hidden' id='Id_Processo' name='Id_Processo' value='"+id_processo+"' />";
	stHtml+="	<input type='hidden' id='Id_ProcessoParte' name='Id_ProcessoParte' value='"+id_parte+"'/>";
	stHtml+="	<div class='col100'>";
	stHtml+="		<table  id='tb_sub_tip"+id_parte+"' class='Tabela' >";
	stHtml+="		    <thead>";
	stHtml+="		      <tr>";
	stHtml+="		      	<th class='colunaMinima' >Id</th>";
	stHtml+="		        <th width='120px' >Assunto</th>";
	stHtml+="		        <th width='60px' >Dispositivo Legal</th>";
	stHtml+="		        <th width='60px' >Artigo</th>";
	stHtml+="		        <th width='60px' >Data Inclus&atilde;o</th>";
	stHtml+="		        <th width='1px' ></th>";
	stHtml+="		        <th class='colunaMinima'>Excluir</th>"; 
	stHtml+="		      </tr>";
	stHtml+="		    </thead>";
	stHtml+="		    <tbody>";
	stHtml+="		    </tbody>";
	stHtml+="		 </table>";
	stHtml+="	</div>";
	stHtml+="	<br />";					
	stHtml+="	<button type='button' title='Clique para Incluir a Tipifica&ccedil;&atilde;o / Assunto'	onclick=\"MostrarBuscaPadrao('sub_tip"+id_parte+"','ProcessoParteAssunto','Consulta de Assunto', 'Digite o Assunto e clique em consultar.', 'Id_Assunto', 'Assunto', ['Assunto','C&oacute;digo CNJ'], ['Dispositivo Legal', 'Artigo'], '2852', '15'); return false;\" > Incluir Outra Tipifica&ccedil;&atilde;o </button>";								
	stHtml +=" </fieldset>";
	return stHtml;
}