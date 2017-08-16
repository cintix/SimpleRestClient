package simplerestclient.gui;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author migo
 */
public class HeaderTable extends AbstractTableModel {

    private Map<Integer, Map<Integer, String>> keyValues;

    private final String[] columns = {"Header", "Value"};
    private final int startRows;

    public HeaderTable(int startRows) {
        this.startRows = startRows;

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
        }
        this.keyValues = new LinkedHashMap();

        for (int i = 0; i < 10; i++) {
            Map<Integer, String> columns = new LinkedHashMap<>();
            columns.put(0, "");
            columns.put(1, "");
            keyValues.put(i, columns);
        }

    }
    
    public String getKey(String name) {
        for (int i : keyValues.keySet()) {
            if (keyValues.get(i).get(0).toLowerCase().equals(name.toLowerCase())) {
                return keyValues.get(i).get(1).toString();
            }
        }
        return null;
    }
    
    
    public HeaderTable() {
        this(10);
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        Map<Integer, String> rows = keyValues.get(row);
        rows.put(col, (String) value);
        keyValues.put(row, rows);
        fireTableCellUpdated(row, col);
    }

    @Override
    public int getRowCount() {
        return keyValues.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    public void fillFromMap(Map<String, String> map) {
        keyValues.clear();
        System.out.println(map.toString());
        int index = 0;
        for (String key : map.keySet()) {
            Map<Integer, String> columns = new LinkedHashMap<>();
            columns.put(0, key);
            columns.put(1, map.get(key));
            keyValues.put(index, columns);
            index++;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (keyValues.size() >= rowIndex) {
            return keyValues.get(rowIndex).get(columnIndex);
        }
        return null;
    }

    public Map<Integer, Map<Integer, String>> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(Map<Integer, Map<Integer, String>> keyValues) {
        this.keyValues = keyValues;
    }

    @Override
    public String toString() {
        return "HeaderTable{" + "keyValues=" + keyValues + ", columns=" + columns + '}';
    }

}
