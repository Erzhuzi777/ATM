package ATMpro;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class AtmSystem {
    public static void main(String[] args){
        //1定义账户类
        //2定义一个集合容器，负责以后存储全部的账户对象，进行相关的业务操作
        ArrayList<Account> accounts = new ArrayList<Account>();
        Scanner sc = new Scanner(System.in);
        //3展示首页
        while (true) {
            System.out.println("------------二柱子ATM系统-------------");
            System.out.println("1.账户登录");
            System.out.println("2.账户开户");
            System.out.println("请您选择操作：");
            int command = sc.nextInt();


            switch (command){
                case 1:
                    //用户登录系统
                    login(accounts, sc);
                    break;
                case 2:
                    //开户系统
                    register(accounts,sc);
                    break;
                default:
                    System.out.println("您输入的操作命令不存在~~");
            }
        }
    }

    /**
     * 登录功能
     * @param accounts 全部账户对象的集合
     * @param sc 扫描器
     */
    private static void login(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("------------系统登录操作------------");
        //1判断账户集合中是否存在账户，如果不存在账户，登录功能不能进行
        if(accounts.size() == 0){
           System.out.println("对不起，当前系统中无任何账户，请您先开户");
           return;
        }

        //2进入登录操作
        while (true) {
            System.out.println("请您输入登录的卡号：");
            String cardId = sc.next();

            //3判断卡号是否存在，根据卡号去账户集合中查询账号对象
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc != null){
                while (true) {
                    //卡号存在
                    //4让用户输入密码
                    System.out.println("请输入密码：");
                    String passWord = sc.next();
                    //判断当前密码是否正确
                    if (acc.getPassWord().equals(passWord)){
                        //登录成功
                        System.out.println("恭喜您，"+ acc.getUserName() + "进入系统，您的卡号是；"+ acc.getCardID());
                        //进行其他操作
                        showUserCommond(sc,acc,accounts);
                        return;//干掉登录方法
                    }else{
                        System.out.println("对不起，您输入的密码有误");
                    }
                }
            }else{
                System.out.println("对不起，系统中不存在该账户卡号");
            }
        }
    }

    /**
     * 展示登录后的操作页
     */
    private static void showUserCommond(Scanner sc,Account acc,ArrayList<Account> accounts) {
        while (true) {
            System.out.println("------------用户操作页------------");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出");
            System.out.println("7.注销账户");
            System.out.println("请输入：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    //查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //存款
                    depositMoney(acc,sc);
                    break;
                case 3:
                    //取款
                    drawMoney(acc,sc);
                    break;
                case 4:
                    //转账
                    transferMoney(sc,acc,accounts );
                    break;
                case 5:
                    //修改密码
                    updatePassWord(sc,acc);
                    return;
                case 6:
                    //退出
                    System.out.println("退出成功");
                    return;//让当前方法停止执行
                case 7:
                    //注销账户
                    if(deleteAccoun(acc,sc,accounts)){
                        //销户成功
                        return;
                    }else {
                        //没有销户成功
                        break;
                    }
                default:
                    System.out.println("您输入的操作命令不正确");
            }
        }
    }

    /**
     * 销户功能
     * @param acc 当前账户
     * @param sc 扫描器
     */
    private static boolean deleteAccoun(Account acc, Scanner sc,ArrayList<Account> accounts) {
        //注销账户
        //从当前用户集合中删除当前账户
        System.out.println("---------------用户注销---------------");
        System.out.println("请再次确认销户操作(输入Y or N):");
        String rs = sc.next();
        switch (rs){
            case "Y":
                //真正的销户
                if(acc.getMoney() > 0){
                    System.out.println("您的账户中还有余额,请把钱取走");
                }else{
                    accounts.remove(acc);
                    System.out.println("您的账户销户完毕:");
                    return true;
                }
                break;
            default:
                System.out.println("当前账户继续保留");
        }
        return false;
    }

    /**
     * 修改密码
     * @param sc 扫描器
     * @param acc 当前登录成功的账户对象
     */
    private static void updatePassWord(Scanner sc, Account acc) {
        System.out.println("---------------用户密码修改---------------");
        while (true) {
            System.out.println("请您输入当前密码:");
            String passWord = sc.next();
            //1判断这个密码是否正确
            if(acc.getPassWord().equals(passWord)){
                while (true) {
                    //密码正确
                    //输入新密码
                    System.out.println("请您输入新密码:");
                    String newPassWord = sc.next();

                    System.out.println("请您确认新密码:");
                    String okPassWord = sc.next();

                    if(newPassWord.equals(okPassWord)){
                        //2密码一致,可以修改
                        acc.setPassWord(newPassWord);
                        System.out.println("恭喜您,密码修改成功");
                        return;
                    }else{
                        System.out.println("您输入的两次密码不一致");
                    }
                }
            }else{
                System.out.println("您输入的密码不正确");
            }
        }
    }

    /**
     * 转账功能
     * @param sc 扫描器
     * @param acc 自己的账户
     * @param accounts 全部的账户
     */
    private static void transferMoney(Scanner sc, Account acc, ArrayList<Account> accounts) {
        System.out.println("----------------用户转账操作----------------");
        //1判断是否有两个账户
        if(accounts.size() < 2){
            System.out.println("当前系统中不足两个账户,无法转账");
            return;
        }

        //2判断是否有钱
        if(acc.getMoney() == 0){
            System.out.println("对不起,您的余额不足,无法转账");
            return;
        }

        while (true) {
            //3开始转账
            System.out.println("请您输入对方账户的卡号");
            String cardId = sc.next();
            //判断卡号是否是自己的卡号
            if(cardId.equals(acc.getCardID())){
                System.out.println("对不起,您不可以给自己转账");
                continue;
            }
            //判断卡号是否存在
            Account account = getAccountByCardId(cardId,accounts);
            if(account == null){
                System.out.println("对不起,您输入的对方卡号不存在");
            }else{
                //存在,认真他的姓氏
                String userName = account.getUserName();
                String tip = "*" + userName.substring(1);
                System.out.println("请您输入[" + tip + "]的姓氏");
                String preName = sc.next();

                //认证姓氏是否输入正确
                if(userName.startsWith(preName)){
                    while (true) {
                        //认证通过
                        System.out.println("请输入转账金额:");
                        double money = sc.nextDouble();
                        //判断余额
                        if(money > acc.getMoney()){
                            System.out.println("对不起,您余额不足,您最多可以转账:" + acc.getMoney());
                        }else{
                            //余额足够,转账
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(account.getMoney() + money);
                            System.out.println("转账成功!您的账户余额剩余:" + acc.getMoney());
                            return;
                        }
                    }
                }else{
                    System.out.println("对不起您输入的信息有误");
                }
            }
        }
    }



    /**
     * 取款
     * @param acc 当前账户对象
     * @param sc 扫描器
     */
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("------------用户取钱操作-------------");
        //1判断是否有100元
        if(acc.getMoney() < 100){
            System.out.println("对不起当前用户余额不足100，不能取钱");
            return;
        }

        while (true) {
            //2提示用户输入取款金额
            System.out.println("请输入取款金额");
            double money = sc.nextDouble();

            //3判断这个金额是否满足要求
            if(money > acc.getQuotaMoney()){
                System.out.println("对不起,您当前取款金额超过买次限额,每次最多取:" + acc.getQuotaMoney());
            }else{
                //没有超过限额
                //4判断是否超过用户总余额
                if(money > acc.getMoney()){
                    System.out.println("余额不足,您当前账户的总余额是:" + acc.getMoney());
                }else{
                    //取钱
                    System.out.println("恭喜你,取钱" + money + "元,成功");
                    //更新余额
                    acc.setMoney(acc.getMoney() - money);
                    //取钱结束了
                    showAccount(acc);
                    return;
                }
            }
        }
    }

    /**
     * 存款
     * @param acc 当前账户对象
     * @param sc 扫描器
     */
    private static void depositMoney(Account acc, Scanner sc) {
        System.out.println("-------------用户存款操作------------");
        System.out.println("请输入存款金额：");
        double money = sc.nextDouble();

        //更新账户余额
        acc.setMoney(acc.getMoney()+money);
        System.out.println("恭喜您，存款成功，当前账户信息如下：");
        showAccount(acc);
    }

    /**
     * 展示账户信息
     * @param acc
     */
    private static void showAccount(Account acc) {
        System.out.println("--------------当前账户信息--------------");
        System.out.println("卡号：" + acc.getCardID());
        System.out.println("户主：" + acc.getUserName());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("限额：" + acc.getQuotaMoney());
    }

    /**
     * 用户开户功能
     * @param accounts
     * @param sc
     */
    private static void register(ArrayList<Account> accounts,Scanner sc) {
        System.out.println("------------系统开户操作------------");
        //1创建一个账户对象，用于后期封装账户信息
        Account account = new Account();

        //2录入当前账户信息，注入到账户对象去
        System.out.println("请您输入账户用户名：");
        String userName = sc.next();
        account.setUserName(userName);

        while (true) {
            System.out.println("请您输入账户密码");
            String passWord = sc.next();
            System.out.println("请您输入确认密码");
            String okPassWord = sc.next();
            if(okPassWord.equals(passWord)){
                //密码认真通过，注入给账户对象
                account.setPassWord(okPassWord);
                break;
            }else {
                System.out.println("对不起，您输入的两次密码不一致，请重新确认");
            }
        }

        System.out.println("请输入账户当次限额：");
        double quotaMoney = sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        //为账户生成一个随机卡号，不重复
        String cardId = getRandomCardID(accounts);
        account.setCardID(cardId);

        //3.把账户对象添加到账户集合中去；
        accounts.add(account);
        System.out.println("恭喜您，"+ userName + "开户成功！您的卡号是：" + cardId + "请你妥善保管。");
    }

    /**
     * 生成随机8位数字
     * @param accounts
     * @return cardId
     */
    private static String getRandomCardID(ArrayList<Account> accounts) {
        Random r = new Random();
        while (true) {
            //1.生成8位数字
            String cardId = "";
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }
            Account acc = getAccountByCardId(cardId,accounts);
            if (acc == null){
                //说明没有重复
                return cardId;
            }
        }
    }

    /**
     * 根据卡号查询出一个账号对象
     * @param cardId
     * @param accounts
     * @return acc
     */
    private static Account getAccountByCardId(String cardId,ArrayList<Account> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardID().equals(cardId)){
                return acc;
            }
        }
        return null;//没有查到
    }
}
