package br.gov.go.tj.projudi.dt;

import java.util.HashMap;
import java.util.Map;

import br.gov.go.tj.utils.Configuracao;

@SuppressWarnings("unchecked")
public class PermissaoDt extends PermissaoDtGen implements Comparable {

	private static final long serialVersionUID = 6466918058377400963L;
	public static final int CodigoPermissao = 155;
	private PermissaoDt[] veFilhos = null;

	private PermissaoDt[] funcoesPermissao = null;
	private String Id_Grupo;
	private String Id_GrupoPermissao;

	public PermissaoDt[] getFuncoesPermissao() {
		return funcoesPermissao;
	}

	public void setFuncoesPermissao(PermissaoDt[] funcoesPermissao) {
		if (funcoesPermissao != null)
			this.funcoesPermissao = funcoesPermissao;
	}

	// criado para incluir mais um objeto filho
	private PermissaoDt[] copiar(PermissaoDt[] lista, PermissaoDt permissao) {
		PermissaoDt[] tempListaPermissoes = new PermissaoDt[lista.length + 1];

		for (int i = 0; i < lista.length; i++)
			tempListaPermissoes[i] = lista[i];

		tempListaPermissoes[lista.length] = permissao;

		return tempListaPermissoes;
	}

	// criado para usar o método sort do Arrays
	public int compareTo(Object o) {
		return getPermissao().compareTo(((PermissaoDt) o).getPermissao());
	}

	// incluo um filho para a permissao
	public void incluirPermissao(PermissaoDt permissao) {
		if (veFilhos == null) {
			veFilhos = new PermissaoDt[1];
			veFilhos[0] = permissao;
		} else
			veFilhos = copiar(veFilhos, permissao);

		// Retirando ordenação do vetor pois já vem ordenado na consulta
		// Arrays.sort(veFilhos);
	}

	/**
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 30/05/2008 - 14:38 Modificacoes nas tags a serem adicionadas
	 * @author Ronneesley Moura Teles
	 * @since 03/09/2008 10:05 Modificacoes quanto a formatacao e hiperlink para
	 *        a permissao
	 * @return String
	 */
	public String getListaUlLi() {
		
		//String stTemp = "<li><input class='formEdicaoCheckBox' id='chkEditar" + getId_Assunto() + "' name='chkEditar" + getId_Assunto() + "' type='checkbox' value='" + getId_Assunto() + "' onchange='alterarServentiaSubtipoAssunto(\"" + getId() + "\",\"" + getId_Assunto() + "\",\"" + getId_ServentiaSubtipo() + "\")'";
		
		String stTemp = "<ul><li><input class='formEdicaoCheckBox'  id='chkEditar" + getId() + "' name='chkEditar" + getId() + "' type='checkbox' value='" + getId() + "'  onchange='alterarPermissaoGrupo(\"" + getId_GrupoPermissao() + "\",\"" + getId_Grupo() + "\",\"" + getId() + "\")'";
		// verifico se existe codigotemp, que indica a existencia de permissão
		// para o grutpo
		if (getCodigoTemp().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong><a href='Permissao?PaginaAtual=" + Configuracao.Editar + "&amp;Id_Permissao=" + this.getId() + "'>" + getPermissao() + "</a></strong> ";

		// Adiciona ifem somente quando ha um titulo
		if (!this.getTitulo().trim().equals(""))
			stTemp += " - " + getTitulo();

		if (veFilhos != null)
			for (int i = 0; i < veFilhos.length; i++)
				stTemp += ((PermissaoDt) veFilhos[i]).getListaUlLi();

		stTemp += "</li> </ul>";

		return stTemp;
	}

	public String getListaMenu() {

		String stTemp = "<ul>";

		stTemp += getListaSubMenu(0);

		stTemp += "</ul>";

		return stTemp;
	}

	public String getListaSubMenu(int nivel) {

		String stTemp = "<li>";

		if (veFilhos == null) {
			// se não houver filhos
			stTemp += "<a href='" + getLink() + "' ";

			if (getTitulo().length() > 0)
				stTemp += " title='" + getTitulo() + "'";

			if (getPermissaoCodigo() != null && getPermissaoCodigo().trim().equalsIgnoreCase("156")) { // Trocar
																										// Serventia
				stTemp += " target='_self'>" + getPermissao() + "</a>";
			} else {
				stTemp += " target='userMainFrame'>" + getPermissao() + "</a>";
			}

		} else {
			stTemp += "<a";
			if (nivel != 0)
				stTemp += " class='TemFilho' ";
			stTemp += " href='#'";

			if (getTitulo().length() > 0)
				stTemp += " title='" + getTitulo() + "'";

			stTemp += " onclick='return false;'>" + getPermissao() + "</a>";

			stTemp += "<ul>";
			for (int i = 0; i < veFilhos.length; i++) {
				stTemp += ((PermissaoDt) veFilhos[i]).getListaSubMenu(1);
			}
			stTemp += "</ul>";
		}
		stTemp += "</li>";

		return stTemp;
	}

	public void limpar() {
		super.limpar();
		funcoesPermissao = null;
	}

	public String getNomeFuncao(int numeroFuncao) {
		String[] funcoes = { "EXCLUIR", "IMPRIMIR", "LOCALIZAR", "LOCALIZARDWR", "NOVO", "SALVAR", "CURINGA6", "CURINGA7", "CURINGA8", "CURINGA9" };
		return funcoes[numeroFuncao];
	}

	public String getListaMenuPJD() {
		return getListaSubMenu(0);
	}

	public String getListaMenuEspecial() {
		return getListaSubMenuEspecial(0);
	}

	public String getListaMenuBox() {
		return getListaSubMenuBox(0);
	}

	private String getNovoLink() {
		if (getIrPara() != null && getIrPara().trim().equals("userMainFrame")) {
			return "<a href='javascript:abrirMenu(\"" + getLink() + "\");'";
		}
		return " <a href='#'";
	}

	private String getNovoLinkMenuBox() {
		if (getIrPara() != null && getIrPara().trim().equals("userMainFrame")) {
			return " <a href='javascript:abrirMenu(\"" + getLink() + "\",\"conteudoLista\");'";
		}
		return " <a href='#'";
	}

	public String getListaSubMenuBox(int nivel) {
		String stTemp = "";

		if (veFilhos == null) {
			// se não houver filhos
			if (nivel == 0) {
				// <li> <a href='javascript: abrirMenu("Pais");'><i class='fa
				// fa-home'></i> <span>Início</span></a> </li>
				stTemp += " <li>";

				stTemp += getNovoLinkMenuBox();

				if (getTitulo() != null && getTitulo().length() > 0)
					stTemp += " title='" + getTitulo() + "'";

				stTemp += " ><span>" + getPermissao() + "</span></a>";

				stTemp += " </li>";
			} else {
				// <li> <a href='#'><i class='fa fa-file'></i> Sem
				// Movimentação</a> </li>
				stTemp += " <li>";

				stTemp += getNovoLinkMenuBox();

				if (getTitulo() != null && getTitulo().length() > 0)
					stTemp += " title='" + getTitulo() + "'";

				stTemp += " >" + getPermissao() + "</a>";

				stTemp += " </li>";
			}

		} else {
			if (getPermissao().equals("Cumprimentos") || getPermissao().equals("Conclusões")) {
				stTemp += "";
			} else {
				// <li> <a href='#'><i class='fa fa-file'></i> 1º Grau <i
				// class='fa fa-angle-left pull-right'></i></a>
				stTemp += " <li>";
				stTemp += " <a href='javascript:void(0);' role='button' aria-haspopup='true' aria-expanded='false'>";
				stTemp += getPermissao();
				stTemp += "</a>";
				// <ul class='treeview-menu'>
				stTemp += " <ul class='nav nav-menuTree-pills nav-stacked'>";
			}

			for (int i = 0; i < veFilhos.length; i++) {
				stTemp += ((PermissaoDt) veFilhos[i]).getListaSubMenuBox(nivel + 1);
			}

			stTemp += " </ul>";

			stTemp += " </li>";
		}

		return stTemp;
	}

	public String getListaSubMenuPJD(int nivel) {

		String stTemp = "";

		if (veFilhos == null) {
			// se não houver filhos
			if (nivel == 0) {
				// <li> <a href='javascript: abrirMenu("Pais");'><i class='fa
				// fa-home'></i> <span>Início</span></a> </li>
				stTemp += " <li>";

				stTemp += getNovoLink();

				if (getTitulo() != null && getTitulo().length() > 0)
					stTemp += " title='" + getTitulo() + "'";

				// stTemp += " ><i class='fa fa-home'></i> <span>" +
				// getPermissao() + "</span></a>";
				stTemp += " id='linkPaginaInicial' ><i class='fa fa-home'></i> <span>" + getPermissao() + "</span></a>";

				stTemp += " </li>";
			} else {
				// <li> <a href='#'><i class='fa fa-file'></i> Sem
				// Movimentação</a> </li>
				stTemp += " <li><i class='fa fa-fw fa-circle'></i>";

				stTemp += getNovoLink();

				if (getTitulo() != null && getTitulo().length() > 0)
					stTemp += " title='" + getTitulo() + "'";

				stTemp += " >" + getPermissao() + "</a>";

				stTemp += " </li>";
			}

		} else {

			if (nivel == 0) {

				// <li class='treeview'> <a href='#'><i class='fa fa-file'></i>
				// <span>Processos </span> <i class='fa fa-angle-left
				// pull-right'></i> </a>
				stTemp += " <li class='treeview'>";
				stTemp += " <a href='#'><i class='" + getIconeMenu(getPermissao()) + "'></i> <span>";
				stTemp += getPermissao();
				stTemp += " </span> <i class='fa fa-angle-left pull-right'></i>";
				stTemp += " </a>";
				// <ul class='treeview-menu'>
				stTemp += " <ul class='treeview-menu ulMenu'>";
			} else {
				// <li> <a href='#'><i class='fa fa-file'></i> 1º Grau <i
				// class='fa fa-angle-left pull-right'></i></a>
				stTemp += " <li><i class='fa fa-fw fa-circle'></i>";
				stTemp += " <a href='#'> ";
				stTemp += getPermissao();
				stTemp += " <i class='fa fa-angle-left pull-right'></i></a>";
				// <ul class='treeview-menu'>
				stTemp += " <ul class='treeview-menu ulMenu'>";
			}

			for (int i = 0; i < veFilhos.length; i++) {
				stTemp += ((PermissaoDt) veFilhos[i]).getListaSubMenu(nivel + 1);
			}

			stTemp += " </ul>";

			stTemp += " </li>";
		}

		return stTemp;
	}

	public String getListaSubMenuEspecial(int nivel) {
		String stTemp = "";

		if (veFilhos == null) {
			// se não houver filhos
			if (nivel == 0) {
				// <li> <a href='javascript: abrirMenu("Pais");'><i class='fa
				// fa-home'></i> <span>Início</span></a> </li>
				stTemp += " <li>";

				stTemp += getNovoLink();

				if (getTitulo() != null && getTitulo().length() > 0)
					stTemp += " title='" + getTitulo() + "'";

				stTemp += " >" + getPermissao() + "</a>";

				stTemp += " </li>";
			} else {
				// <li> <a href='#'><i class='fa fa-file'></i> Sem
				// Movimentação</a> </li>
				stTemp += " <li>";

				stTemp += getNovoLink();

				if (getTitulo() != null && getTitulo().length() > 0)
					stTemp += " title='" + getTitulo() + "'";

				stTemp += " >" + getPermissao() + "</a>";

				stTemp += " </li>";
			}

		} else {

			if (!this.getPermissao().equals("Opções Processo")) {

				if (nivel == 0) {

					// <li class='treeview'> <a href='#'><i class='fa
					// fa-file'></i> <span>Processos </span> <i class='fa
					// fa-angle-left pull-right'></i> </a>
					stTemp += " <li class='dropdown-submenu'>";
					stTemp += " <a tabindex='-1' href='#'>";
					stTemp += getPermissao();
					stTemp += " </a>";
					// <ul class='treeview-menu'>
					stTemp += " <ul class='dropdown-menu'>";
				} else {
					// <li> <a href='#'><i class='fa fa-file'></i> 1º Grau <i
					// class='fa fa-angle-left pull-right'></i></a>
					stTemp += " <li class='dropdown-submenu'>";
					stTemp += " <a tabindex='-1' href='#'>";
					stTemp += getPermissao();
					stTemp += "</a>";
					// <ul class='treeview-menu'>
					stTemp += " <ul class='dropdown-menu'>";
				}
			}

			for (int i = 0; i < veFilhos.length; i++) {
				stTemp += ((PermissaoDt) veFilhos[i]).getListaSubMenuEspecial(nivel + 1);
			}

			stTemp += " </ul>";

			stTemp += " </li>";
		}

		return stTemp;
	}

	/**
	 * Recupera o ícone do menu. O ideal é usar alguma coluna da tabela PERM
	 * para guardar o nome do icone.
	 * Sugestão: usar a coluna codigoTemp para indicar que o menu é usado e o
	 * irPara para guardar o nome do icone.
	 * 
	 * @param nomeMenu
	 * @return
	 */
	public String getIconeMenu(String nomeMenu) {
		String[] imagens = { "fa fa-file", "fa fa-comments", "fa fa-edit", "fa fa-legal", "fa fa-bar-chat", "fa fa-lock", "fa fa-certificate" };
		Map<String, String> icones = new HashMap<String, String>();
		icones.put("Processo", "fa fa-file");
		icones.put("Audiências", "fa fa-comments");
		icones.put("Mandados", "fa fa-university");
		icones.put("Gerenciamento", "fa fa-cogs");
		icones.put("Cadastros", "fa fa-edit");
		icones.put("Cumprimentos", "fa fa-legal");
		icones.put("Estatísticas", "fa fa-bar-chart");
		icones.put("Segurança", "fa fa-lock");
		icones.put("Certificados", "fa fa-certificate");
		icones.put("Sessões", "fa fa-users");
		return icones.containsKey(nomeMenu) ? icones.get(nomeMenu) : "fa fa-file";
	}

	/**
	 * Obtém os filhos da permissão para montar o meu no JSP
	 * 
	 * @author gschiquini
	 * @return
	 */
	public PermissaoDt[] getVeFilhos() {
		return veFilhos;
	}

	public void setId_Grupo(String valor) {
		Id_Grupo = valor;		
	}
	
	public String getId_Grupo() {
		return Id_Grupo;
	}
				
	public void setId_GrupoPermissao(String valor) {
		Id_GrupoPermissao = valor;
		
	}

	public String getId_GrupoPermissao() {
		return Id_GrupoPermissao;
	}
	
}
