import java.util.*;

public class CSP {

    private final ArrayList<PeriodTime> periodTimeFromInput;
    private final ArrayList<Room> roomsFromInput;
    private final ArrayList<Teacher> teachersFromInput;
    private final ArrayList<Subject> subjectsFromInput;
    private final ArrayList<StudentGroup> groupsFromInput;
    private final ArrayList<Slot> allVar;
    private final ArrayList<Slot> blocks;
    private final ArrayList<Slot> wherewhen;
    private final Stack<SlotsAndConflicts> stack;

    public CSP(ArrayList<PeriodTime> periodTimeFromInput, ArrayList<Room> roomsFromInput, ArrayList<Teacher> teachersFromInput, ArrayList<Subject> subjectsFromInput, ArrayList<StudentGroup> groupsFromInput) {
        this.periodTimeFromInput = periodTimeFromInput;
        this.roomsFromInput = roomsFromInput;
        this.teachersFromInput = teachersFromInput;
        this.subjectsFromInput = subjectsFromInput;
        this.groupsFromInput = groupsFromInput;
        this.wherewhen = createWhereWhen();
        this.blocks = createBlocks();
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
//        for(int i=0; i< result.size();i++){
//            System.out.println(result.get(i).getPeriodTime() + " " + result.get(i).getRoom());
//        }
        return result;
    }

    private ArrayList<Slot> createBlocks() {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i = 0; i < groupsFromInput.size(); i++) {
            for (int j = 0; j < subjectsFromInput.size(); j++) {
                if (groupsFromInput.get(i).getSubjects().contains(subjectsFromInput.get(j))) {
                    Subject sub = subjectsFromInput.get(j);
                    for (int m = 0; m < teachersFromInput.size(); m++) {
                        Teacher teach = teachersFromInput.get(m);
                        if (sub.type =="L"  && sub.teacher.contains(teachersFromInput.get(m))) {
                            //if (sub.teacher.get(0).equals(teach)) {
                                result.add(new Slot(groupsFromInput.get(i), sub, teach, "L"));
                            //} else {
                             //   result.add(new Slot(groupsFromInput.get(i), sub, teach, "P"));
                           // }
                        }else if ( sub.type == "P" && sub.teacher.contains(teachersFromInput.get(m))){
                            result.add(new Slot(groupsFromInput.get(i), sub, teach, "P"));
                        }
                    }
                }
            }
        }

//        for(int i=0; i< result.size();i++){
//            System.out.println(result.get(i).getGroup()+ " "+result.get(i).getSubject()+" "+  result.get(i).getTeacher() + " "+result.get(i).getTypeLesson());
//        }
        return result;
    }

    private ArrayList<Slot> createAllVariants() {
        ArrayList<Slot> result = new ArrayList<>();

        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < wherewhen.size(); j++) {
                if (blocks.get(i).getTypeLesson().equals("L") && (wherewhen.get(j).getRoom().capacity <= 15)) {
                    continue;
                }
                Slot s = blocks.get(i);
                Slot ww = wherewhen.get(j);
                result.add(new Slot(s.getGroup(), s.getSubject(), s.getTeacher(), s.getTypeLesson(), ww.getPeriodTime(), ww.getRoom()));
            }
        }
//        for(int i=0; i< result.size();i++){
//            System.out.println(result.get(i).getGroup()+ " "+result.get(i).getSubject()+" "+  result.get(i).getTeacher() + " "+result.get(i).getTypeLesson() +" "+ result.get(i).getPeriodTime()+result.get(i).getRoom());
//        }

        return result;
    }

    public void startCSP() {
//System.out.println(stack.size());
        Schedule schedule = computing();
        if (schedule == null) {
            System.out.println("CANT FIND ANSWER");
        } else {
            System.out.println(schedule);
        }
    }

    private Schedule computing() {
        if (stack.size() == blocks.size()) {
//            System.out.println("Stack size: " +stack.size());
  //          System.out.println("Blocks size: " +blocks.size());
            return makeScheduleFromStack();
        }
        Slot followingBlock = followingBlock();

        if (followingBlock.getGroup() != null && followingBlock.getSubject() != null && followingBlock.getTeacher() != null && followingBlock.getTypeLesson() != null)
            return takingFollowingBlock(followingBlock);
        else
            return null;

    }

    private Slot followingBlock() {
        Slot result = null;
        for (int i = 0; i < blocks.size(); i++) {
            //create list of possible var for this block
            List<Slot> possible = returnAllPosibleVar(blocks.get(i));
           // System.out.println(possible.size());
            if (possible.size() > 0) {
               // System.out.println(possible.size());
                if (result == null || possible.size() < returnAllPosibleVar(result).size()) {

                    result = blocks.get(i);
                  //  System.out.println(result.getGroup()+" "+ result.getSubject()+" "+ result.getTeacher()+" "+ result.getTypeLesson());
                }
            }
        }
      //  System.out.println(result);

        return result;
    }

    private List<Slot> returnAllPosibleVar(Slot sl) {
       // System.out.println("AllVar size: "+allVar.size());
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

    private List<Slot> getAllPossibleWhereWhen(Slot sl) {
        //System.out.println("here");
        List<Slot> possibleWW = new ArrayList<>();
        for (int j = 0; j < allVar.size(); j++) {
            if (allVar.get(j).getGroup().equals(sl.getGroup()) &&
                    allVar.get(j).getSubject().equals(sl.getSubject()) &&
                    allVar.get(j).getTeacher().equals(sl.getTeacher()) &&
                    allVar.get(j).getTypeLesson().equals(sl.getTypeLesson())) {
                possibleWW.add(new Slot(allVar.get(j).getPeriodTime(), allVar.get(j).getRoom()));
            }
        }
        //System.out.println("PossibleWWsize "+ possibleWW.size());
        return possibleWW;
    }

    private Schedule takingFollowingBlock(Slot followingBlock) {

        for (Slot followingWhereWhen : getAllPossibleWhereWhen(followingBlock)) {

            pushItemOfScheduleAndDeleted(followingBlock, followingWhereWhen);
            Schedule result = computing();
            if (result != null) {
                return result;
            } else {
                popItemOfSceduleAndDeleted();
            }
        }
        return null;
    }

    private void pushItemOfScheduleAndDeleted(Slot block, Slot whereWhen) {

        Set<Slot> unnecessary = new HashSet<>();

        for (Slot whereWhen1 : getAllPossibleWhereWhen(block)) {
            unnecessary.add(new Slot(block.getGroup(), block.getSubject(), block.getTeacher(), block.getTypeLesson(), whereWhen1.getPeriodTime(), whereWhen1.getRoom()));
        }

        for (Slot slot : blocks) {
            addToUnnecessaryItemsIfIfExists(unnecessary, slot, whereWhen);
        }

        for (Slot slot : blocks) {
            if (slot.getTeacher().equals(block.getTeacher())) {
                addToUnnecessaryItemsIfTheSameTime(unnecessary, slot, whereWhen);
            }
        }

        for (Slot slot : blocks) {
            if (slot.getGroup().equals(block.getGroup()) &&
                    (slot.getTypeLesson() == "L" || block.getTypeLesson() == "L" || !slot.getSubject().equals(block.getSubject()))) {
                addToUnnecessaryItemsIfTheSameTime(unnecessary, slot, whereWhen);
            }
        }
        ArrayList<Slot> copyAllVar = allVar;
        for (Slot scheduleEntity : unnecessary) {
            for (int i = 0; i < copyAllVar.size(); i++) {
                Slot sl = copyAllVar.get(i);
                if (sl.getGroup().equals(scheduleEntity.getGroup()) &&
                        sl.getSubject().equals(scheduleEntity.getSubject()) &&
                        sl.getTeacher().equals(scheduleEntity.getTeacher()) &&
                        sl.getTypeLesson().equals(scheduleEntity.getTypeLesson()) &&
                        sl.getPeriodTime().equals(scheduleEntity.getPeriodTime()) &&
                sl.getRoom().equals((scheduleEntity.getRoom()))) {
                    allVar.remove(sl);
                }
            }
        }

        SlotsAndConflicts itemOfScheduleAndDeleted = new SlotsAndConflicts(new Slot(block.getGroup(), block.getSubject(), block.getTeacher(), block.getTypeLesson(), whereWhen.getPeriodTime(), whereWhen.getRoom()), unnecessary);
        stack.push(itemOfScheduleAndDeleted);
    }

    private void addToUnnecessaryItemsIfIfExists(Set<Slot> unnecessary, Slot block, Slot whereWhen) {
        if (getAllPossibleWhereWhen(block).contains(whereWhen)) {
            unnecessary.add(new Slot(block.getGroup(), block.getSubject(), block.getTeacher(), block.getTypeLesson(), whereWhen.getPeriodTime(), whereWhen.getRoom()));
        }

    }

    private void addToUnnecessaryItemsIfTheSameTime(Set<Slot> unnecessary, Slot block, Slot whereWhen) {

        for (Slot whereWhen1 : getAllPossibleWhereWhen(block)) {
            if (whereWhen1.getPeriodTime().equals(whereWhen.getPeriodTime())) {
                addToUnnecessaryItemsIfIfExists(unnecessary, block, whereWhen1);
            }
        }
    }

    private void popItemOfSceduleAndDeleted() {
        SlotsAndConflicts itemOfScheduleAndDeleted = stack.pop();

//        for (Slot itemOfSchedule : itemOfScheduleAndDeleted.getConflicts()) {
//            allVariants.get(itemOfSchedule.getBlock()).add(itemOfSchedule.getWhereWhen());
//        }
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


    private Schedule makeScheduleFromStack() {
        List<Slot> itemOfSchedules = new ArrayList<>();

        for (SlotsAndConflicts itemOfScheduleAndDeleted : stack) {
            itemOfSchedules.add(itemOfScheduleAndDeleted.getSlot());
        }
        return new Schedule(itemOfSchedules);
    }


}