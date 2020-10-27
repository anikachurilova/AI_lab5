import java.util.*;

public class CSP {

    private List<Slot> allSlots = new ArrayList<>();
    private final Stack<itemOfScheduleAndDeleted> stack;

    public CSP(List<Slot> slot) {
        this.allSlots = makingAllVariantsForBlocks();
        this.stack = new Stack<>();
    }

    public void startCSP() {
        Schedule schedule = computing();
        if (schedule == null) {
            System.out.println("CANT FIND ANSWER");
        } else {
            System.out.println(schedule);
        }
    }

    private Schedule computing() {
        if (stack.size() == blocks.size()) {

            return makeScheduleFromStack();
        }
        Block followingBlock = followingBlock();

        if (followingBlock != null)
            return takingFollowingBlock(followingBlock);
        else
            return null;

    }

    private Schedule takingFollowingBlock(Block followingBlock) {
        for (WhereWhen followingWhereWhen : getAllPossibleWhereWhen(followingBlock)) {

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

    private void pushItemOfScheduleAndDeleted(Block block, WhereWhen whereWhen) {

        Set<Slot> unnecessary = new HashSet<>();

        for (WhereWhen whereWhen1 : allVariants.get(block)) {
            unnecessary.add(new Slot(block, whereWhen1));
        }

        for (Block Block : blocks) {
            addToUnnecessaryItemsIfIfExists(unnecessary, Block, whereWhen);
        }

        for (Block Block : blocks) {
            if (Block.getTeacher().equals(block.getTeacher())) {
                addToUnnecessaryItemsIfTheSameTime(unnecessary, Block, whereWhen);
            }
        }

        for (Block Block : blocks) {
            if (Block.getGroups().equals(block.getGroups()) &&
                    (Block.isLecture() || block.isLecture() || !Block.getLessons().equals(block.getLessons()))) {
                addToUnnecessaryItemsIfTheSameTime(unnecessary, Block, whereWhen);
            }
        }
        for (Slot scheduleEntity : unnecessary) {
            allVariants.get(scheduleEntity.getBlock()).remove(scheduleEntity.getWhereWhen());
        }


        itemOfScheduleAndDeleted itemOfScheduleAndDeleted = new itemOfScheduleAndDeleted(new itemOfSchedule(block, whereWhen), unnecessary);
        stack.push(itemOfScheduleAndDeleted);
    }

    private void addToUnnecessaryItemsIfTheSameTime(Set<itemOfSchedule> unnecessary, Block block, WhereWhen whereWhen) {

        for (WhereWhen whereWhen1 : allVariants.get(block)) {
            if (whereWhen1.getStudyLesson().equals(whereWhen.getStudyLesson()) &&
                    whereWhen1.getStudyDay().equals(whereWhen.getStudyDay())) {
                addToUnnecessaryItemsIfIfExists(unnecessary, block, whereWhen1);
            }
        }
    }

    private void addToUnnecessaryItemsIfIfExists(Set<itemOfSchedule> unnecessary, Block block, WhereWhen whereWhen) {
        if (allVariants.get(block).contains(whereWhen)) {
            unnecessary.add(new itemOfSchedule(block, whereWhen));
        }
    }

    private void popItemOfSceduleAndDeleted() {
        itemOfScheduleAndDeleted itemOfScheduleAndDeleted = stack.pop();

        for (itemOfSchedule itemOfSchedule : itemOfScheduleAndDeleted.getRemovedItemOfSchedule()) {
            allVariants.get(itemOfSchedule.getBlock()).add(itemOfSchedule.getWhereWhen());
        }
    }

    private Block followingBlock() {
        Block result = null;
        for (Block block :blocks){
            if (allVariants.get(block).size() > 0) {
                if(result == null || allVariants.get(block).size() < allVariants.get(result).size()){
                    result = block;
                }
            }
        }
        return result;
    }

    private List<WhereWhen> getAllPossibleWhereWhen(Block block) {
        Set<WhereWhen> whereWhens = allVariants.get(block);
        return new ArrayList<>(whereWhens);
    }

    private Map<Block, Set<WhereWhen>> makingAllVariantsForBlocks() {
        Map<Block, Set<WhereWhen>> answer = new HashMap<>();

        for (Block block : blocks) {

            Set<WhereWhen> whereWhens = new HashSet<>();
            for (WhereWhen whereWhen : whereWhen) {
                if (block.isLecture() && !whereWhen.getAuditorium().isSuitableForLectures()) {
                    continue;
                }
                whereWhens.add(whereWhen);
            }
            answer.put(block, whereWhens);
        }
        return answer;
    }

    private Schedule makeScheduleFromStack() {
        List<itemOfSchedule> itemOfSchedules = new ArrayList<>();

        for (itemOfScheduleAndDeleted itemOfScheduleAndDeleted : stack) {
            itemOfSchedules.add(itemOfScheduleAndDeleted.getItemOfSchedule());

        }
        return new Schedule(itemOfSchedules);
    }
}

