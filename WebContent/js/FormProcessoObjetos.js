function mostrarObjetos(id_proc){
	MostrarOcultar("sub_objetos");
	if($('#sub_objetos').is(":visible")){
		$('#sub_objetos').html(gerarHtmlObjetos(id_proc));
		atualizarTabelaPasso('ProcessoObjetoArquivo','sub_objetos','Info Objetos Processos',undefined,undefined);			
	}
}

function gerarHtmlObjetos(id_proc){ 				  		
	var stHtml ="<fieldset class='VisualizaDados' ><legend>Objetos do Processo</legend>";
	stHtml +="	<input type='hidden' id='sub_objetos_Id_Proc' name='sub_objetos_Id_Proc' value='"+id_proc+"' />";																
																										
	stHtml +="	<blockquote class='col100'>";
	stHtml +="		<table  id='tb_sub_objetos' class='Tabela' >";
	stHtml +="		    <thead>";
	stHtml +="			<tr>";
	stHtml +="		      	<th class='colunaMinima' >Id</th>";
	stHtml +="		        <th width='60px' >Objeto</th>";
	stHtml +="		        <th width='30px' >Objeto SubTipo</th>";
	stHtml +="		        <th width='30px' >Dt. Entrada</th>";
	stHtml +="		        <th width='30px' >Status Baixa</th>";
	stHtml +="		        <th width='30px' >Delegacia</th>";
	stHtml +="		        <th width='30px' >Arquivo</th>";
	stHtml +="		        <th class='colunaMinima'>info</th>";
/*	stHtml +="	        	<th class='colunaMinima'>Editar</th>";*/
/*	stHtml +="		        <th class='colunaMinima'>Excluir</th>"; */
	stHtml +="		      </tr>";
	stHtml +="		    </thead>";
	stHtml +="		    <tbody>";
	stHtml +="		    </tbody>";
	stHtml +="		 </table>";
	stHtml +="	</blockquote>";		
	stHtml +=" </fieldset>";
	
	return stHtml;																		
}