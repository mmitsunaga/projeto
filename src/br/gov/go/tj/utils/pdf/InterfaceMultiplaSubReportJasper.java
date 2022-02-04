package br.gov.go.tj.utils.pdf;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



public class InterfaceMultiplaSubReportJasper implements JRDataSource {

    private Map objLista = null;
    private boolean requisitou;

    public InterfaceMultiplaSubReportJasper(Map detalhes) {
        this.objLista = detalhes;
    }

    public Object getFieldValue(JRField campo) throws JRException {
        return  new JRBeanCollectionDataSource((List)this.objLista.get(campo.getName()));
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


