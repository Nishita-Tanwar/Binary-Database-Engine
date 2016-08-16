package edu.utdallas.nxt150030;

import java.util.Comparator;

/**
 * Created by nishitatanwar on 4/11/16.
 */
public class OrdinalComparator implements Comparator<ColumnType> {

    @Override
    public int compare(ColumnType o1, ColumnType o2) {
        return o1.getOrdinal() - o2.getOrdinal();
    }
}
