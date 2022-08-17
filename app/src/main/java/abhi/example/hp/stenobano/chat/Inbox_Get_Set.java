package abhi.example.hp.stenobano.chat;

/**
 * Created by AQEEL on 9/11/2018.
 */

public class Inbox_Get_Set {

    String id,rid,name,mesg,message,picture,type,timestamp,status,block,date,token_sender,token_reciever;

    public String getRid() {
        return rid;
    }

    public String getToken_sender() {
        return token_sender;
    }

    public void setToken_sender(String token_sender) {
        this.token_sender = token_sender;
    }

    public String getToken_reciever() {
        return token_reciever;
    }

    public void setToken_reciever(String token_reciever) {
        this.token_reciever = token_reciever;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

}
