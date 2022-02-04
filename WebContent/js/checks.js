/**
 * Tratamentos com checks boxes
 * @author Ronneesley Moura Teles
 * @since 18/11/2008 14:04
 */


/**
 * Atualizar os checks
 * @author Ronneesley Moura Teles
 * @since 21/10/2008 09:22
 */
function atualizarChecks(check, tabela){
	alterarEstadoChecks(tabela, check.checked);
}

/**
 * Atualizar os checks com filtro
 * @author Ronneesley Moura Teles
 * @since 14/01/2008 11:29
 */
function atualizarChecksFiltro(check, tabela, filtro){
	alterarEstadoChecks(tabela, check.checked, filtro);
}

/**
 * Altera o estado da selecao de todos
 * @author Ronneesley Moura Teles
 * @since 21/10/2008 10:11
 */
function alterarEstadoSelTodos(estado){
	$("#chkSelTodos").attr("checked", estado);
}

/**
 * Altera o estado dos checks
 * @author Ronneesley Moura Teles
 * @since 21/10/2008 09:51
 */ 
function alterarEstadoChecks(tabela, estado, nome){
	if (nome == null){
		filtro = "";
	} else {
		filtro = "[name*=" + nome + "]"
	}
	
	$("#" + tabela + " input[type=checkbox]" + filtro).each(function(obj){
		//Somente marca os que possuem valores
		if (this.value != null && this.value != "")
			this.checked = estado;
	});
	
	alterarEstadoSelTodos(estado);
}