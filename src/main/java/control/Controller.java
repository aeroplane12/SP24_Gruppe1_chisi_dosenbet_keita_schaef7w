package control;
import model.Couple;
import model.Manager;
import model.Person;

import java.util.List;

public class Controller {
    private Manager manager = Manager.getInstance();
    public Controller() {
    }

    /**
     * pushOnUndoStack
     * pushes instance on undo stack
     */
    public void pushOnUndoStack() {
        manager.pushToPrev();
    }

    /**
     * pushOnRedoStack
     * pushes instance on redo stack
     */
    public void pushOnRedoStack() {
        manager.pushToFuture();
    }

    /**
     * popOnUndoStack
     * reverts back to last change made
     */
    public void popOnUndoStack() {
        manager.setToPrev();
    }

    /**
     * popOnRedoStack
     * reverts back undo change
     */
    public void popOnRedoStack() {
        manager.setToFuture();
    }

    /**
     * uploadCSV
     * uploads the csv with the participants to the application
     * @param filename
     */
    public void uploadCSV(String filename) {
        manager.inputPeople(filename);
    }

    /**
     * uploads the location to the application
     * @param filename
     */
    public void uploadLocation(String filename) {
        manager.inputLocation(filename);
    }
    public void dissolveCouple(Couple couple) {
        pushOnUndoStack();
        manager.removeCouple(couple);
        manager.getSingles().add(couple.getPerson1());
        manager.getSingles().add(couple.getPerson2());
    }
}
