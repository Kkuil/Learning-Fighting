package src.principle.DependencyInvetion;

public class Test02 {
    public static void main(String[] args) {
        Person1 person = new Person1();
        person.receive(new Email1());
        person.receive(new Weixin());
    }
}

interface IReceiver {
    String getInfo();
}

class Email1 implements IReceiver {
    @Override
    public String getInfo() {
        return "电子邮件信息: hello,world";
    }
}

class Weixin implements IReceiver {
    @Override
    public String getInfo() {
        return "微信信息: hello,ok";
    }
}

class Person1 {
    public void receive(IReceiver receiver) {
        System.out.println(receiver.getInfo());
    }
}
