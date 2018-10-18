package ATMServer;

import com.Bank;
import com.accountType.Account;
import com.exceptionType.RegisterException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ATMServer {
    public static Bank bank=Bank.getBank();

    public static void main(String[] args) throws IOException {
        //服务端在20006端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(20006);
        Socket client = null;
        bank.readData();
        System.out.println(bank.getAccounts().toString());
        //通过调用Executors类的静态方法，创建一个ExecutorService实例
        //ExecutorService接口是Executor接口的子接口
        Executor service = Executors.newCachedThreadPool();
        boolean flag = true;
        while(flag){
            //等待客户端的连接
            client = server.accept();
            System.out.println("与客户端连接成功！");
            service.execute(new ServerThread(client));
        }
        server.close();
    }
    public static boolean registration(String password,String name, String idnum,
                                       String email, String accountType) throws RegisterException{
        if (bank.getAccounts().containsKey(name) && bank.getAccounts().get(name).getPersonId().equals(idnum)){
            return false;
        }else {
            bank.getAccounts().put(name,bank.register(password,name,idnum,email,accountType));
            bank.upDate();
            return true;
        }
    }
}
