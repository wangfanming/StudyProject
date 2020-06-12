package com.wfm.hooks.base;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName Weapon
 * @Descripyion TODO
 * @date 2020/4/4 17:22
 */
public class Weapon {
    protected int damage = 10;

    public void attack() {
        System.out.println(String.format("对目标造成 %d 点伤害", damage));
    }
}
