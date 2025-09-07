package hnz.mitra.siteware.omie.types;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum FieldType {
    CHARACTER("C"),
    NUMERIC("N"),
    DATE("D");

    public String value;
    private static final Map<String, FieldType> lookup = new HashMap();

    private FieldType(String value) {
        this.value = value;
    }

    public static FieldType get(String code) {
        return (FieldType)lookup.get(code);
    }

    static {
        Iterator var0 = EnumSet.allOf(FieldType.class).iterator();

        while(var0.hasNext()) {
            FieldType s = (FieldType)var0.next();
            lookup.put(s.value, s);
        }
    }
}
