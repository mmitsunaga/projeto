function mostrarMovimentacaoObjeto(id_objeto){
	if (id_objeto==null || id_objeto==""){
		return;
	}	
	MostrarOcultar("sub_objetos_movimentos");
	if($('#sub_objetos_movimentos').is(":visible")){
		$('#sub_objetos_movimentos').html(gerarHtmlMovimentacaoObjeto(id_objeto));
		atualizarTabelaPasso('ProcessoObjetoArquivo','sub_objetos_movimentos','Info Objetos Processos','editar','excluir');			
	}
}

function encaminharLeilao(){
	if ($('#encLeicao').prop('checked')){
		$('#ProcessoObjetoArquivoMovimentacao').val('Emcaminhado para Leilao');
		$('#ProcessoObjetoArquivoMovimentacao').prop('disabled', true);		
	}else{
		$('#ProcessoObjetoArquivoMovimentacao').removeAttr('disabled');
		$('#ProcessoObjetoArquivoMovimentacao').val('');
	}
}

function LeilaoRealizado(){
	if ($('#LeicaoRealizado').prop('checked')){	
		$('#Favorecido').show();			
	}else{
		$('#Favorecido').hide();		
	}
}

function editarPassoSelecionar(){
	if ($('#ProcessoObjetoArquivoMovimentacao').val()=='Emcaminhado para Leilao'){
		$('#ProcessoObjetoArquivoMovimentacao').prop('disabled', true);
		if($('#DataRetorno').val()!="" ){
			$('#editarLeilao').hide();
		}else{
			$('#editarLeilao').show();
		}
		
		$('#divEnLeilao').hide();				
		$('#encLeicao').prop('checked','true');
				
	}
}

function atualizarMovimentacaoObjeto(id_objeto){
	if (id_objeto==null || id_objeto==""){
		return;
	}		
	$('#sub_objetos_movimentos').html(gerarHtmlMovimentacaoObjeto(id_objeto));
	atualizarTabelaPasso('ProcessoObjetoArquivo','sub_objetos_movimentos','Info Objetos Processos','editar','excluir');				
}

function gerarHtmlMovimentacaoObjeto(id_objeto){ 				  		
	var stHtml ="<fieldset class='VisualizaDados' ><legend>Objetos do Processo</legend>";
	stHtml +="	<input type='hidden' id='Id_ProcessoObjetoArquivo' name='Id_ProcessoObjetoArquivo' value='"+id_objeto+"' />";
	stHtml +="	<input type='hidden' id='stPasso' name='stPasso' value='MovimentacaoObjeto' />";
	
	stHtml +="<div class='col10'>";
	stHtml +="	<label for='Id'>Identificador</label> <input class='formEdicaoInputSomenteLeitura' name='Id'  id='Id' type='text'  readonly='true' value=''>";
	stHtml +="</div>";
	stHtml +="<div class='col10' id='divEnLeilao'>";
	stHtml +="		<label for='encLeicao'><input type='checkbox' id='encLeicao' name='scales' onchange='encaminharLeilao();'>Leil&atilde;o</label>  ";
	stHtml +="</div>";
	stHtml +="<div class='col45'>";
	stHtml +="		<label  for='ProcessoObjetoArquivoMovimentacao'>*Descri&ccedil;&atilde;o da Movimenta&ccedil;&atilde;o do Objeto</label> <input class='formEdicaoInput' name='ProcessoObjetoArquivoMovimentacao' id='ProcessoObjetoArquivoMovimentacao'  type='text' size='60' maxlength='60' value='' onkeyup=' autoTab(this,60)'>";
	stHtml +="</div>";
	stHtml +="<div class='col15' id='editarLeilao' style='display:none'>";
	stHtml +="		<label for='LeicaoRealizado'><input type='checkbox' id='LeicaoRealizado' name='LeicaoRealizado' onchange='LeilaoRealizado();'>Leil&atilde;o Realizado</label>  ";
	stHtml +="</div>";
	stHtml +="<div class='col50' id='Favorecido' style='display:none'>";
	stHtml +="    		<label for='Leilao' style='border-bottom-style: solid; border-bottom-color: #e1e1e1; border-bottom-width: 1'>Favorecido Leil&atilde;o</label>";		    		
	stHtml +="    		<input type='radio' name='FavorecidoLeilao' id='FavorecidoLeilao' value='0' />Poder Judici&aacute;rio";
	stHtml +="    		<input type='radio' name='FavorecidoLeilao' id='FavorecidoLeilao' value='1' />Uni&atilde;o";
	stHtml +="    		<input type='radio' name='FavorecidoLeilao' id='FavorecidoLeilao' value='2' />Estado de Goi&aacute;s"; 
	stHtml +="    		<input type='radio' name='FavorecidoLeilao' id='FavorecidoLeilao' value='3' />Justi&ccedil;a Federal";  			    	
	stHtml +="</div>";
	stHtml +="<div class='col15'>";
	stHtml +="		<label  for='DataMovimentacao'>*Data Retirada <img id='calendarioDataMovimentacao' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick=\"displayCalendar(DataMovimentacao,'dd/mm/yyyy',this)\"/></label>";
	stHtml +="		<input class='formEdicaoInput' name='DataMovimentacao' id='DataMovimentacao' type='text' size='18' maxlength='10' value='' onblur='verifica_data(this);' onKeyPress='return DigitarSoNumero(this, event)' OnKeyUp='mascara_data(this)'>";
	stHtml +="</div>";
	stHtml +="<div class='col15'>";
	stHtml +="	<label  for='DataRetorno'>Data Devolu&ccedil;&atilde;o<img id='calendarioDatagetDataRetorno' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick=\"displayCalendar(DataRetorno,'dd/mm/yyyy',this)\"/></label>";
	stHtml +="	<input class='formEdicaoInput' name='DataRetorno' id='DataRetorno' type='text' size='18' maxlength='10' value='' onblur='verifica_data(this);' onKeyPress='return DigitarSoNumero(this, event)' OnKeyUp='mascara_data(this)'>";
	stHtml +="</div>";					
	stHtml +="	<center class='col100 AlinharCentro'>";
	stHtml +="		<input type='button' value='Salvar' onclick=\"salvarPasso('ProcessoObjetoArquivo','sub_objetos_movimentos','Info Movimentação','sim')\">";
	stHtml +="	</center>";											
																													
	stHtml +="	<blockquote class='col100'>";
	stHtml +="		<table  id='tb_sub_objetos_movimentos' class='Tabela' >";
	stHtml +="		    <thead>";
	stHtml +="			<tr>";
	stHtml +="		      	<th class='colunaMinima' >Id</th>";
	stHtml +="		        <th width='40px' >Movimenta&ccedil;&atilde;o</th>";
	stHtml +="		        <th width='30px' >Data Retirada</th>";
	stHtml +="		        <th width='30px' >Data Devolu&ccedil;&atilde;o</th>";
	stHtml +="		        <th width='30px' >Objeto</th>";
	stHtml +="		        <th width='30px' ></th>";				
	stHtml +="		        <th class='colunaMinima'>info</th>";
	stHtml +="	        	<th class='colunaMinima'>Editar</th>";
	stHtml +="		        <th class='colunaMinima'>Excluir</th>"; 
	stHtml +="		      </tr>";
	stHtml +="		    </thead>";
	stHtml +="		    <tbody>";
	stHtml +="		    </tbody>";
	stHtml +="		 </table>";
	stHtml +="	</blockquote>";		
	stHtml +=" </fieldset>";
	
	return stHtml;																		
}