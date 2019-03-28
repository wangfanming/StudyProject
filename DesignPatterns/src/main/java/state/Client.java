package state;

public class Client {
    public static void main(String[] args) {
        VoteManager voteManager = new VoteManager();
        for (int i=0;i<9;i++){
            voteManager.vote("admin","AAA");
        }
    }
}
