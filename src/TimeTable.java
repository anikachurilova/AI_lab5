import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimeTable {

    private final List<Slot> readySlots;
    private final int mistakes;

    public TimeTable(List<Slot> itemsOfSchedule) {
        this.readySlots = itemsOfSchedule;
        this.mistakes = findingMistakes();

    }
    private int findingMistakes() {
        int res= 0;

        for (Slot slot1 : readySlots) {
            res += findingMistakes(slot1);
            for (Slot slot2 : readySlots) {
                if (slot1 != slot2) {
                    res += findingMistakes(slot1, slot2);
                }
            }
        }
        return res;
    }

    private int findingMistakes(Slot item) {
        int res = 0;
        res += findGoodRoom(item);
        return res;
    }

    private int findingMistakes(Slot item1, Slot item2) {
        int result = 0;
        if (overlapTime(item1, item2)) {
            result += fingBadRoom(item1, item2);
            result += findBadTeacher(item1, item2);
            if (overlapStudents(item1, item2)) {
                result += findBadSubject(item1, item2);
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
        return days;
    }
    private int findBadTeacher(Slot i1, Slot i2) {
        Teacher t1 = i1.getTeacher();
        Teacher t2 = i2.getTeacher();
        if(t1.equals(t2))
            return 1;
        return 0;
    }


    private int findBadSubject(Slot i1, Slot i2) {
        boolean l1 = (i1.getTypeLesson() == "L");
        boolean l2 = (i2.getTypeLesson() == "L");
        Subject lesson1 = i1.getSubject();
        Subject lesson2 = i2.getSubject();
        boolean isNotDifferentSubj = lesson1.equals(lesson2);

        if (l1 || l2) {
            return 1;
        }
        if (!isNotDifferentSubj)
            return 1;
        return 0;
    }

    private int findGoodRoom(Slot i) {
        if (i.getTypeLesson() == "L" && !(i.getRoom().capacity >15))
            return 1;
        return 0;
    }

    private int fingBadRoom(Slot i1, Slot i2) {
        Room a1 = i1.getRoom();
        Room a2 = i2.getRoom();

        if (a1.equals(a2)) return 1;
        return 0;
    }


    @Override
    public String toString() {

        List<Slot> sheduleEn = new ArrayList<>();
        readySlots.sort(Comparator.comparing(a -> a.getPeriodTime().getTime()));
        String answer = "";
        for (Slot x : readySlots) {
            sheduleEn.add(x);
            answer += x.getPeriodTime().getTime() +" " +x.getSubject().getName() + " " + x.getTypeLesson() +" "+ x.getTeacher().getName() + " "+ x.getGroup().getName() +" "+ x.getRoom().getNumber()+ ("\n");

        }
        return answer + "                               Error amounts:"+ mistakes;
    }

    public List<Slot> getReadySlots() {
        return readySlots;
    }
}
