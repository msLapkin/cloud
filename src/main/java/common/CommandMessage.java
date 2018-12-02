package common;

public class CommandMessage extends AbstractMessage {
    private int command;
    public static final int FILE_LIST=1;
    public static final int DLD=2;
    public static final int ULD=3;
    public static final int AUTH_OK=4;
    public static final int AUTH_FAIL=5;

    private Object[] object;

    public CommandMessage(int command, Object... objects) {
        this.command = command;
        this.object = objects;
    }

    public int getCommand() {
        return command;
    }

    public Object[] getObject() {
        return object;
    }
}
