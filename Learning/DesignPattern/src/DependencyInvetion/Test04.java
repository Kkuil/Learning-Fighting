package src.DependencyInvetion;

public class Test04 {
    public static void main(String[] args) {
        Person3 person = new Person3();
        person.setReceiver(new Email3());
        person.receive();
        person.setReceiver(new Weixin3());
        person.receive();
    }
}

interface IReceiver3 {
    String getInfo();
}

class Email3 implements IReceiver3 {
    public String getInfo() {
        return "电子邮件信息: hello,world";
    }
}

class Weixin3 implements IReceiver3 {
    @Override
    public String getInfo() {
        return "微信信息: hello,ok";
    }
}

class Person3 {
    IReceiver3 receiver;

    public void setReceiver(IReceiver3 receiver) {
        this.receiver = receiver;
    }

    public void receive() {
        System.out.println(this.receiver.getInfo());
    }
}
