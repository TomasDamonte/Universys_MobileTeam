package universis.universys;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Se utiliza para decorar el calendario.
 */
public class CalendarDecorator implements DayViewDecorator {
    private final int color;
    private final HashSet<CalendarDay> dates;

    public CalendarDecorator(Collection<CalendarDay> dates,int color) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    /**
     * Qué día se debe decorar.
     * @param day Dia a decorar.
     * @return
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    /**
     * Decora el dia.
     * @param view
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, color));
    }

}
