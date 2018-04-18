package model.timeline;

import model.app.ApplicationState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Timeline {

    protected Date datetimeStart;
    protected Date datetimeEnd;
    private ApplicationState lastApplicationState = ApplicationState.NOT_RUNNING;
    protected ArrayList<Period> periods = new ArrayList<>();

    public Timeline() {
        this(new Date(), new Date());
    }

    public Timeline(Date datetimeStart, Date datetimeEnd) {
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
    }

    public void update(Date datetime, ApplicationState state) {
        if (!datetimeEnd.before(datetime)) {
            return;
        }
        datetimeEnd = datetime;
        if (ApplicationState.NOT_RUNNING.equals(state)) {
            lastApplicationState = ApplicationState.NOT_RUNNING;
            return;
        }
        if (!lastApplicationState.equals(state)) {
            Period period;
            switch (state) {
                case RUNNING:
                    period = new RunningPeriod(datetime, datetime);
                    break;
                case ACTIVE:
                    period = new ActivePeriod(datetime, datetime);
                    break;
                default:
                    return;
            }
            periods.add(period);
            lastApplicationState = state;
            return;
        }
        Period lastPeriod = periods.get(periods.size() - 1);
        lastPeriod.setDatetimeEnd(datetime);
    }

    public Date getDatetimeStart() {
        return datetimeStart;
    }

    public Date getDatetimeEnd() {
        return datetimeEnd;
    }

    public List<Period> getPeriods() {
        return Collections.unmodifiableList(periods);
    }

    public int getActiveTimeInSec() {
        int timeInSec = 0;
        for (Period period : periods) {
            if (period instanceof ActivePeriod) {
                timeInSec += period.getTimeInSec();
            }
        }
        return timeInSec;
    }

    public int getRunningTimeInSec() {
        int timeInSec = 0;
        for (Period period : periods) {
            if (period instanceof RunningPeriod) {
                timeInSec += period.getTimeInSec();
            }
        }
        return timeInSec;
    }

    @Override
    public String toString() {
        return String.format("Timeline(%s, %s, %s)", datetimeStart, datetimeEnd, periods);
    }
}
