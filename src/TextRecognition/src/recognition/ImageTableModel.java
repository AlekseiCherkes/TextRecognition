package recognition;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.*;

import java.util.*;

public class ImageTableModel extends QAbstractItemModel {

    public static final int IMAGE_COL = 0;
    public static final int SYMBOL_COL = 1;
    public static final int PROBABILITY_COL = 2;

    public ImageTableModel(QObject parent) {
        super(parent);
        images = new ArrayList<QImage>();
        probabilities = new ArrayList<Double>();
        symbols = new ArrayList<String>();
    }

    public QImage imageAt(int row) {
        return images.get(row);
    }

    public int columnCount(QModelIndex parent) {
        return 3;
    }

    public QModelIndex parent(QModelIndex child) {
        return null;
    }

    public QModelIndex index(int row, int column, QModelIndex parent) {
        if (parent == null)
            return createIndex(row, column);
        return null;
    }

    public Object data(QModelIndex index, int role) {
        int row = index.row();
        int col = index.column();
        if(images.size() > row){
            if (role == Qt.ItemDataRole.DisplayRole) {
                if (col == IMAGE_COL) {
                    return "no image";
                } else if (col == SYMBOL_COL) {
                    String s = symbols.get(row);
                    return s != null ? s : "No symbol";
                } else if (col == PROBABILITY_COL) {
                    Double p = probabilities.get(row);
                    return p != null ? p : "Not recognized";
                }
            } else if (role == Qt.ItemDataRole.DecorationRole) {
                if (col == IMAGE_COL) {
                    return images.get(row);
                }
            }
        }
        return null;
    }

    public int rowCount(QModelIndex parent__0) {
        return images.size();
    }

    public Object headerData(int section, Qt.Orientation orientation, int role) {
        if (orientation == Qt.Orientation.Horizontal
            && (role == Qt.ItemDataRole.DisplayRole || role == Qt.ItemDataRole.EditRole)) {
            switch (section) {
            case IMAGE_COL: return "Image";
            case SYMBOL_COL: return "Symbol";
            case PROBABILITY_COL: return "Probability";
            }
        }
        return super.headerData(section, orientation, role);
    }

    public void updateRow(int i) {
        dataChanged.emit(index(i, 2), index(i, 3));
    }

    public void addRow(QImage image, Double probability, String symbol) {
        images.add(image);
        probabilities.add(probability);
        symbols.add(symbol);
    }

    public void clearRows() {
        images.clear();
        probabilities.clear();
        symbols.clear();
    }

    // TODO: filnd more feasiable solution
    public void resetRows() {
        reset();
    }

    private ArrayList<QImage> images;
    private ArrayList<Double> probabilities;
    private ArrayList<String> symbols;
}
