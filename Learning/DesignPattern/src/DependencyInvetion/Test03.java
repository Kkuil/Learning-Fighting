package src.DependencyInvetion;

public class Test03 {
    public static void main(String[] args) {
        Person2 person = new Person2(new Email2());
        person.receive();
        person = new Person2(new Weixin2());
        person.receive();
    }
}

interface IReceiver2 {
    String getInfo();
}

class Email2 implements IReceiver2 {
    public String getInfo() {
        return "电子邮件信息: hello,world";
    }
}

class Weixin2 implements IReceiver2 {
    @Override
    public String getInfo() {
        return "微信信息: hello,ok";
    }
}

class Person2 {
    IReceiver2 receiver;

    public Person2(IReceiver2 receiver) {
        this.receiver = receiver;
    }
    public void receive() {
        System.out.println(this.receiver.getInfo());
    }
}
