package de.kobv.marcel.core;

import de.kobv.marcel.beans.Record;

import java.util.Set;

/**
 */
public class OrRecordMatcher implements RecordMatcher {

    private Set<RecordMatcher> matchers;

    @Override
    public boolean matches(final Record record) {
        if (getMatchers() != null) {
            for (RecordMatcher matcher : getMatchers()) {
                if (matcher.matches(record)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Set<RecordMatcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(final Set<RecordMatcher> rmatchers) {
        this.matchers = rmatchers;
    }

}
