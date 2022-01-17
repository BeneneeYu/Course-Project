package fudan.se.lab2.exception;
/*
会议名全称不能重复
 */
public class MeetingFullNameDuplicatedException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;
    public MeetingFullNameDuplicatedException(String fullName){
        super("Meeting full name '" + fullName + "' has been registered");
    }
}
