/*
 *  Copyright 2014 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.classlib.java.lang;

import org.teavm.classlib.java.io.TSerializable;
import org.teavm.javascript.ni.GeneratedBy;
import org.teavm.javascript.ni.Rename;

/**
 *
 * @author Alexey Andreev
 */
public class TBoolean extends TObject implements TSerializable, TComparable<TBoolean> {
    public static final TBoolean TRUE = new TBoolean(true);
    public static final TBoolean FALSE = new TBoolean(false);
    public static final TClass<TBoolean> TYPE = TClass.booleanClass();
    private boolean value;

    public TBoolean(boolean value) {
        this.value = value;
    }

    public TBoolean(TString value) {
        this.value = parseBoolean(value);
    }

    @Override
    public int compareTo(TBoolean other) {
        return compare(value, other.value);
    }

    public static int compare(boolean x, boolean y) {
        if (x) {
            if (!y) {
                return 1;
            }
        } else {
            if (y) {
                return -1;
            }
        }
        return 0;
    }

    public static boolean parseBoolean(TString s) {
        return s != null && s.toLowerCase().equals("true");
    }

    public boolean booleanValue() {
        return value;
    }

    public static TBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static TBoolean valueOf(TString value) {
        return valueOf(parseBoolean(value));
    }

    public static TString toString(boolean value) {
        return value ? TString.wrap("true") : TString.wrap("false");
    }

    @Override
    @Rename("toString")
    public TString toString0() {
        return toString(value);
    }

    @Override
    @GeneratedBy(ObjectNativeGenerator.class)
    public int hashCode() {
        return value ? 1231 : 1237;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof TBoolean && ((TBoolean)obj).value == value;
    }

    public boolean getBoolean(TString key) {
        return valueOf(TSystem.getProperty(key)).booleanValue();
    }
}
