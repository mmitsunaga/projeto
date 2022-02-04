package br.gov.go.tj.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class ResultSetTJGO {
    private static final int SQLCODE_INVALID_COLUMN_NAME = 17006;
	ResultSet result = null;
    PreparedStatement ps = null;
    
    public ResultSetTJGO(PreparedStatement ps, ResultSet rs) throws Exception {
        this.result = rs;
        this.ps = ps; 
    }

    public boolean next() throws Exception {
        
        return this.result.next();
    }

    public String getString(String string) throws Exception {
        String valor = this.result.getString(string);
        Funcoes.validarString(valor);
        return valor;
    }
    public void close() throws Exception {
        try{if (result != null) result.close();} catch(Exception e) {e.printStackTrace();}
        try{if (ps != null) ps.close();} catch(Exception e) {e.printStackTrace();}        
    }

    public Long getLong(String string) throws Exception {
        
        return this.result.getLong(string);
    }

    public void beforeFirst() throws Exception {
        this.result.beforeFirst();        
    }

    public boolean first() throws Exception {
        
        return this.result.first();
    }

    public boolean getBoolean(String string) throws Exception {
        
        return this.result.getBoolean(string);
    }

    public byte[] getBytes(String string) throws Exception {
        
        return this.result.getBytes(string);
    }
    // retorna a data e a hora 
    public Date getDateTime(String string) throws Exception {
        
        return this.result.getTimestamp(string);
    }
    // retorna somente a hora 
    public Date getDate(String string) throws Exception {
        
        return this.result.getDate(string);
    }
    
    public double getDouble(String string) throws Exception {
        
        return this.result.getDouble(string);
    }

    public int getInt(String string) throws Exception {
        
        return this.result.getInt(string);
    }

    public boolean isAfterLast() throws Exception {
        
        return this.result.isAfterLast();
    }

    public void previous() throws Exception {
        this.result.previous();        
    }

    public String getString(int i) throws Exception {        
    	 String valor = this.result.getString(i);
         Funcoes.validarString(valor);
         return valor;                
    }

    public int getColumnCount() throws Exception {
        
        return this.result.getMetaData().getColumnCount();        
    }

    public int getRow() throws Exception{
    	return this.result.getRow();
    }
    
    public boolean last() throws Exception{
    	return this.result.last();
    }
    
    public String getColumnName(int column) throws Exception
    {
    	return this.result.getMetaData().getColumnName(column);
    }
    
    public Object getObject(String string) throws Exception {       
        return this.result.getObject(string);
    } 
    
    public boolean isDate(String string) throws Exception{
    	Object obj = this.result.getObject(string);
    	if (obj == null) return false;
    	return obj instanceof Date;
    }
    
    public Time getTime(String string) throws Exception {
        // TODO Auto-generated method stub
        return this.result.getTime(string);
    }
    
    public Timestamp getTimestamp(String string) throws Exception {
        return this.result.getTimestamp(string);
    }
    
    public boolean isNull(String string) throws Exception {
    	return this.result.getObject(string) == null;
    }
    
    public boolean contains(String string) throws SQLException {
    	try {
    		return this.result.findColumn(string) >= 0;
    	} catch (SQLException e) {
    		if (e.getErrorCode() == SQLCODE_INVALID_COLUMN_NAME) {
    			return false;
    		}
    		throw e;
    	}    	
    }
}
