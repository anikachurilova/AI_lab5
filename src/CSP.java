import java.util.*;

public class CSP {

    private final ArrayList<PeriodTime> periodTimeFromInput;
    private final ArrayList<Room> roomsFromInput;
    private final ArrayList<Teacher> teachersFromInput;
    private final ArrayList<Subject> subjectsFromInput;
    private final ArrayList<StudentGroup> groupsFromInput;
    private final ArrayList<Slot> allVar;
    private final ArrayList<Slot> groupsSubjectsTeachers;
    private final ArrayList<Slot> placeTime;
    private final Stack<SlotsAndConflicts> stack;

    public CSP(ArrayList<PeriodTime> periodTimeFromInput, ArrayList<Room> roomsFromInput, ArrayList<Teacher> teachersFromInput, ArrayList<Subject> subjectsFromInput, ArrayList<StudentGroup> groupsFromInput) {
        this.periodTimeFromInput = periodTimeFromInput;
        this.roomsFromInput = roomsFromInput;
        this.teachersFromInput = teachersFromInput;
        this.subjectsFromInput = subjectsFromInput;
        this.groupsFromInput = groupsFromInput;
        this.placeTime = createWhereWhen();
        this.groupsSubjectsTeachers = createBlocks();
        this.allVar = createAllVariants();
        this.stack = new Stack<>();
    }

    private ArrayList<Slot> createWhereWhen() {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i = 0; i < periodTimeFromInput.size(); i++) {
            for (int j = 0; j < roomsFromInput.size(); j++) {
                result.add(new Slot(periodTimeFromInput.get(i), roomsFromInput.get(j)));
            }
        }

        return result;
    }

    private ArrayList<Slot> createBlocks() {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i = 0; i < groupsFromInput.size(); i++) {
            for (int j = 0; j < subjectsFromInput.size(); j++) {
                if (groupsFromInput.get(i).getSubjects().contains(subjectsFromInput.get(j))) {
                    Subject sub = subjectsFromInput.get(j);
                    if(sub.type == "L"){
                        result.add(new Slot(groupsFromInput.get(i), sub, sub.teacher.get(0), "L"));
                    }else{
                        int num = (int) ((Math.random()*(sub.teacher.size()-2)) + 1);
                        Teacher t = sub.teacher.get(num);
                        result.add(new Slot(groupsFromInput.get(i), sub, t, "P"));
                    }

                }
            }
        }


        return result;
    }

    private ArrayList<Slot> createAllVariants() {
        ArrayList<Slot> result = new ArrayList<>();

        for (int i = 0; i < groupsSubjectsTeachers.size(); i++) {
            for (int j = 0; j < placeTime.size(); j++) {
                if (groupsSubjectsTeachers.get(i).getTypeLesson().equals("L") && (placeTime.get(j).getRoom().capacity <= 15)) {
                    continue;
                }
                Slot s = groupsSubjectsTeachers.get(i);
                Slot ww = placeTime.get(j);
                result.add(new Slot(s.getGroup(), s.getSubject(), s.getTeacher(), s.getTypeLesson(), ww.getPeriodTime(), ww.getRoom()));
            }
        }

        return result;
    }

    public TimeTable running() {
        TimeTable timeTable = makeCalculations();
        if (timeTable == null) {
            System.out.println("There is no possible timetable:(");
        } else {
            System.out.println(timeTable);
        }
        return timeTable;
    }

    private TimeTable makeCalculations() {
        if (stack.size() == groupsSubjectsTeachers.size()) {

            return createNewTimeTable();
        }
        Slot nextSlot = nextSlot();

        if (nextSlot.getGroup() != null && nextSlot.getSubject() != null && nextSlot.getTeacher() != null && nextSlot.getTypeLesson() != null)
            return processingNextSlot(nextSlot);

        else
            return null;

    }


    private TimeTable createNewTimeTable() {
        List<Slot> readySlots = new ArrayList<>();

        for (SlotsAndConflicts itemOfScheduleAndDeleted : stack) {
            readySlots.add(itemOfScheduleAndDeleted.getSlot());
        }
        return new TimeTable(readySlots);
    }

    private Slot nextSlot() {
        Slot result = null;
        for (int i = 0; i < groupsSubjectsTeachers.size(); i++) {
            List<Slot> possible = returnAllPosibleVar(groupsSubjectsTeachers.get(i));
            if (possible.size() > 0) {
                if (result == null || possible.size() < returnAllPosibleVar(result).size()) {

                    result = groupsSubjectsTeachers.get(i);
                }
            }
        }

        return result;
    }

    private List<Slot> returnAllPosibleVar(Slot sl) {
        List<Slot> possibleVarForBlock = new ArrayList<>();
        for (int j = 0; j < allVar.size(); j++) {
            if (allVar.get(j).getGroup().equals(sl.getGroup()) &&
                    allVar.get(j).getSubject().equals(sl.getSubject()) &&
                    allVar.get(j).getTeacher().equals(sl.getTeacher()) &&
                    allVar.get(j).getTypeLesson().equals(sl.getTypeLesson())) {
                possibleVarForBlock.add(allVar.get(j));
            }
        }
        return possibleVarForBlock;
    }

    private List<Slot> getAllPossibleTimePlace(Slot sl) {

        List<Slot> possibleWW = new ArrayList<>();
        for (int j = 0; j < allVar.size(); j++) {
            if (allVar.get(j).getGroup().equals(sl.getGroup()) &&
                    allVar.get(j).getSubject().equals(sl.getSubject()) &&
                    allVar.get(j).getTeacher().equals(sl.getTeacher()) &&
                    allVar.get(j).getTypeLesson().equals(sl.getTypeLesson())) {
                possibleWW.add(new Slot(allVar.get(j).getPeriodTime(), allVar.get(j).getRoom()));
            }
        }
        return possibleWW;
    }

    private TimeTable processingNextSlot(Slot nextSlot) {

        for (Slot nextPlaceTime : getAllPossibleTimePlace(nextSlot)) {

            findAllUseless(nextSlot, nextPlaceTime);
            TimeTable result = makeCalculations();
            if (result != null) {
                return result;
            } else {
                addNewToAllVar();
            }
        }
        return null;
    }

    private void findAllUseless(Slot currentSlot, Slot placeTime) {

        Set<Slot> useless = new HashSet<>();

        for (Slot whereWhen1 : getAllPossibleTimePlace(currentSlot)) {
            useless.add(new Slot(currentSlot.getGroup(), currentSlot.getSubject(), currentSlot.getTeacher(), currentSlot.getTypeLesson(), whereWhen1.getPeriodTime(), whereWhen1.getRoom()));
        }
        for (Slot slot : groupsSubjectsTeachers) {
            pushUselessInCaseExists(useless, slot, placeTime);
        }
        for (Slot slot : groupsSubjectsTeachers) {
            if (slot.getTeacher().equals(currentSlot.getTeacher())) {
                pushUselessInCaseRepeat(useless, slot, placeTime);
            }
        }
        for (Slot slot : groupsSubjectsTeachers) {
            if (slot.getGroup().equals(currentSlot.getGroup()) &&
                    (slot.getTypeLesson() == "L" || currentSlot.getTypeLesson() == "L" || !slot.getSubject().equals(currentSlot.getSubject()))) {
                pushUselessInCaseRepeat(useless, slot, placeTime);
            }
        }
        ArrayList<Slot> copyAllVar = allVar;
        for (Slot timeTableVar : useless) {
            for (int i = 0; i < copyAllVar.size(); i++) {
                Slot sl = copyAllVar.get(i);
                if (sl.getGroup().equals(timeTableVar.getGroup()) &&
                        sl.getSubject().equals(timeTableVar.getSubject()) &&
                        sl.getTeacher().equals(timeTableVar.getTeacher()) &&
                        sl.getTypeLesson().equals(timeTableVar.getTypeLesson()) &&
                        sl.getPeriodTime().equals(timeTableVar.getPeriodTime()) &&
                sl.getRoom().equals((timeTableVar.getRoom()))) {
                    allVar.remove(sl);
                }
            }
        }

        SlotsAndConflicts varOfSlot = new SlotsAndConflicts(new Slot(currentSlot.getGroup(), currentSlot.getSubject(), currentSlot.getTeacher(), currentSlot.getTypeLesson(), placeTime.getPeriodTime(), placeTime.getRoom()), useless);
        stack.push(varOfSlot);
    }

    private void pushUselessInCaseExists(Set<Slot> unnecessary, Slot slot, Slot placeTime) {
        if (getAllPossibleTimePlace(slot).contains(placeTime)) {
            unnecessary.add(new Slot(slot.getGroup(), slot.getSubject(), slot.getTeacher(), slot.getTypeLesson(), placeTime.getPeriodTime(), placeTime.getRoom()));
        }

    }

    private void pushUselessInCaseRepeat(Set<Slot> unnecessary, Slot block, Slot whereWhen) {

        for (Slot whereWhen1 : getAllPossibleTimePlace(block)) {
            if (whereWhen1.getPeriodTime().equals(whereWhen.getPeriodTime())) {
                pushUselessInCaseExists(unnecessary, block, whereWhen1);
            }
        }
    }

    private void addNewToAllVar() {
        SlotsAndConflicts itemOfScheduleAndDeleted = stack.pop();

        List<Slot> copyAllVar = allVar;
        for (Slot itemOfSchedule : itemOfScheduleAndDeleted.getConflicts()) {
            for (int i = 0; i < copyAllVar.size(); i++) {
                Slot sl = copyAllVar.get(i);
                if (sl.getGroup().equals(itemOfSchedule.getGroup()) &&
                        sl.getSubject().equals(itemOfSchedule.getSubject()) &&
                        sl.getTeacher().equals(itemOfSchedule.getTeacher()) &&
                        sl.getTypeLesson().equals(itemOfSchedule.getTypeLesson())) {
                    allVar.add(new Slot(sl.getGroup(), sl.getSubject(), sl.getTeacher(), sl.getTypeLesson(), itemOfSchedule.getPeriodTime(), itemOfSchedule.getRoom()));
                }
            }
        }
    }



}