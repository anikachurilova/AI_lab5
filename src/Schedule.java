
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {

    private List<Slot> slot = new ArrayList<>();
    private int errors = 0;

    public Schedule(List<Slot> slot) {
        this.slot = slot;
        this.errors = calcErrors();
    }


    private int calcErrors() {
        int result = 0;
        for (Slot i1 : slot) {

            result += calcErrors(i1);
            for (Slot i2 : slot) {
                if (i1 != i2) {
                    result += calcErrors(i1, i2);
                }
            }
        }
        return result;
    }

    private int calcErrors(Slot slot) {
        int result = 0;
        result += calcGoodAudit(slot);
        return result;
    }

    private int calcErrors(Slot item1, Slot item2) {
        int result = 0;
        if (overlapTime(item1, item2)) {
            result += calcOverlapAudit(item1, item2);
            result += calcOverlapTeachers(item1, item2);
            if (overlapStudents(item1, item2)) {
                result += calcOverlapLectures(item1, item2);
            }
        }
        return result;
    }

    private boolean overlapStudents(Slot i1, Slot i2) {
        String studentsGroup1 = i1.getGroup().getName();
        String studentsGroup2 = i2.getGroup().getName();
        return studentsGroup1.equals(studentsGroup2);
    }

    private boolean overlapTime(Slot i1, Slot i2) {
        boolean days = i1.getPeriodTime().equals(i2.getPeriodTime());
        return days ;
    }

    private int calcGoodAudit(Slot i) {
        if (i.getSubject().getType() == "L" && i.getRoom().getCapacity() >= 15)
            return 1;
        return 0;
    }

    private int calcOverlapAudit(Slot i1, Slot i2) {
        Room a1 = i1.getRoom();
        Room a2 = i2.getRoom();
        if (a1.equals(a2)) return 1;
        return 0;
    }

    private int calcOverlapTeachers(Slot i1, Slot i2) {
        String t1 = i1.getTeacher().getName();
        String t2 = i2.getTeacher().getName();
        if(t1.equals(t2))
            return 1;
        return 0;
    }

    private int calcOverlapLectures(Slot i1, Slot i2) {
        boolean l1 = i1.getSubject().getType() == "L";
        boolean l2 = i2.getSubject().getType() == "L";
        String lesson1 = i1.getSubject().getName();
        String lesson2 = i2.getSubject().getName();
        boolean isNotDifferentSubj = lesson1.equals(lesson2);
        String g1 = i1.getGroup().getName();
        String g2 = i2.getGroup().getName();
        boolean isNotDifferentGroups = g1 == g2;

        if (l1 || l2) {
            return 1;
        }
        if (!isNotDifferentSubj || isNotDifferentGroups)
            return 1;
        return 0;
    }
    @Override
    public String toString() {

        List<Slot> sheduleEn = new ArrayList<>();
        slot.sort(Comparator.comparing(a -> a.getPeriodTime().getTime()));
        String answer = "";
        for (Slot x : slot) {
            sheduleEn.add(x);
            answer += x + ("\n");

        }
        return answer + "                               Error amounts:"+ errors;
    }
}
