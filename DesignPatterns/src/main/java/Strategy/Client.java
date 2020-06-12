package Strategy;

/**
 * 　策略模式的重心
 * <p>
 * 　　策略模式的重心不是如何实现算法，而是如何组织、调用这些算法，从而让程序结构更灵活，具有更好的维护性和扩展性
 * 所以可以这样描述这一系列策略算法：策略算法是相同行为的不同实现。
 * 所有的具体策略类都有一些公有的行为。这时候，就应当把这些公有的行为放到共同的抽象策略角色Strategy类里面。
 * 当然这时候抽象策略角色必须要用Java抽象类实现，而不能使用接口。
 * 　　这其实也是典型的将代码向继承等级结构的上方集中的标准做法。
 * <p>
 * <p>
 * 策略模式的缺点
 * 　　（1）客户端必须知道所有的策略类，并自行决定使用哪一个策略类。这就意味着客户端必须理解这些算法的区别，以便适时选择恰当的算法类。换言之，策略模式只适用于客户端知道算法或行为的情况。
 * <p>
 * 　　（2）由于策略模式把每个具体的策略实现都单独封装成为类，如果备选的策略很多的话，那么对象的数目就会很可观。
 */
public class Client {
    public static void main(String[] args) {
        //创建一个具体的策略对象
        ConcreteStrategyA concreteStrategyA = new ConcreteStrategyA();

        //创建环境
        Context context = new Context(concreteStrategyA);

        //使用策略
        context.contextInterface();
    }
}
