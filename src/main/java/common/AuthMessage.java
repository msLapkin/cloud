package common;

public class AuthMessage extends AbstractMessage {
    private String login;
    private String pass;

    public AuthMessage(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
