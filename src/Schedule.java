import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {

    private final List<Slot> itemsOfSchedule;
    private final int errorsInSchedule;

    public Schedule(List<Slot> itemsOfSchedule) {
        this.itemsOfSchedule = itemsOfSchedule;
        this.errorsInSchedule = calcErrors();
    }


    private int calcErrors() {
        int result = 0;
        for (Slot i1 : itemsOfSchedule) {

            result += calcErrors(i1);
            for (Slot i2 : itemsOfSchedule) {
                if (i1 != i2) {
                    result += calcErrors(i1, i2);
                }
            }
        }
        return result;
    }

    private int calcErrors(Slot item) {
        int result = 0;
        result += calcGoodAudit(item);
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
        StudentGroup studentsGroup1 = i1.getGroup();
        StudentGroup studentsGroup2 = i2.getGroup();
        return studentsGroup1.equals(studentsGroup2);
    }

    private boolean overlapTime(Slot i1, Slot i2) {
        boolean days = i1.getPeriodTime().equals(i2.getPeriodTime());
       // boolean lessons = i1.getWhereWhen().getStudyLesson().equals(i2.getWhereWhen().getStudyLesson());
        return days;
    }

    private int calcGoodAudit(Slot i) {
        if (i.getTypeLesson() == "L" && !(i.getRoom().capacity >15))
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
        Teacher t1 = i1.getTeacher();
       Teacher t2 = i2.getTeacher();
        if(t1.equals(t2))
            return 1;
        return 0;
    }

    private int calcOverlapLectures(Slot i1, Slot i2) {
        boolean l1 = (i1.getTypeLesson() == "L");
        boolean l2 = (i2.getTypeLesson() == "L");
        Subject lesson1 = i1.getSubject();
        Subject lesson2 = i2.getSubject();
        boolean isNotDifferentSubj = lesson1.equals(lesson2);
//        int g1 = i1.getNumberGroup();
//        int g2 = i2.getNumberGroup();
//        boolean isNotDifferentGroups = g1 == g2;

        if (l1 || l2) {
            return 1;
        }
        if (!isNotDifferentSubj)
            return 1;
        return 0;
    }
    @Override
    public String toString() {

        List<Slot> sheduleEn = new ArrayList<>();
        itemsOfSchedule.sort(Comparator.comparing(a -> a.getPeriodTime().getTime()));
       // itemsOfSchedule.sort(Comparator.comparing(a -> a.getWhereWhen().getStudyDay()));
        String answer = "";
        for (Slot x : itemsOfSchedule) {
            sheduleEn.add(x);
            answer += x.getPeriodTime().getTime() +" " +x.getSubject().getName() + " " + x.getTypeLesson() +" "+ x.getTeacher().getName() + " "+ x.getGroup().getName() +" "+ x.getRoom().getNumber()+ ("\n");

        }
        return answer + "                               Error amounts:"+ errorsInSchedule;
    }
}
