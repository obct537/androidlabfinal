package beaucheminm.calcfinal;

/**
 * Created by Max on 4/27/2015.
 */
public class Friendship {
    private Integer ID;
    private String email_send;
    private String email_receive;
    private String status;

    public Friendship(Integer friendshipID, String sender, String receiver, String sts){
        ID = friendshipID;
        email_send = sender;
        email_receive = receiver;
        status = sts;
    }

    public Integer getID(){
        return ID;
    }

    public String getEmail_send(){
        return email_send;
    }

    public String getEmail_receive(){
        return email_receive;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String sts){
        status = sts;
    }
}
