import java.util.Set;

public class SlotsAndConflicts {
    Slot slot;
    private final Set<Slot> conflicts;

    public SlotsAndConflicts(Slot slot, Set<Slot> conflicts) {
        this.slot = slot;
        this.conflicts = conflicts;
    }

    public Slot getSlot() {
        return slot;
    }

    public Set<Slot> getConflicts() {
        return conflicts;
    }
}
