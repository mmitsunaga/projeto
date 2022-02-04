package br.gov.go.tj.utils.pdf;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class InterfaceSubReportJasper implements JRDataSource {

	private List objLista = null;
	private boolean requisitou;

	public InterfaceSubReportJasper(List detalhes) {
		this.objLista = detalhes;
	}

	public Object getFieldValue(JRField campo) throws JRException {
		return  new JRBeanCollectionDataSource(this.objLista);
	}

	public boolean next() throws JRException {
		if (!requisitou) {
			requisitou = true;
			return true;
		} else {
			return false;
		}
	}

}
