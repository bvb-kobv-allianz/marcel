package de.kobv.marcel.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Util {

    private Util() {
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(final Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

}
