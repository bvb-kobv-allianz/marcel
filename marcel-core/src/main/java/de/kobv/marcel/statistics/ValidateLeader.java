package de.kobv.marcel.statistics;

import de.kobv.marcel.beans.Record;
import org.apache.log4j.Logger;

/**
 * Validates leader of MARC record.
 *
 * TODO "The leader field '...' is not valid"
 * TODO move validation to Record class
 */
public class ValidateLeader extends AbstractRecordProcessor {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(ValidateLeader.class);

    private static final String ERROR = "INVALID_LEADER";

    /**
     * Regular expression for validating MARC record leader.
     */
    private String leaderRegExp =
            "[\\d ]{5}[\\dA-Za-z ]{1}[\\dA-Za-z]{1}[\\dA-Za-z ]{3}(2| )(2| )[\\d ]{5}[\\dA-Za-z ]{3}(4500|    )";

    @Override
    public void process(final Record record) {
        LOG.debug("Validate leader of " + record.getUid());
        String leader = record.getLeader();
        if (!leader.matches(getLeaderRegExp())) {
            getErrorHandler().error(record.getUid(), ERROR, leader);
        }
    }

    public String getLeaderRegExp() {
        return leaderRegExp;
    }

    public void setLeaderRegExp(final String regExp) {
        this.leaderRegExp = regExp;
    }

}
