package com.wfm.hooks.base;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName Hero
 * @Descripyion TODO
 * @date 2020/4/4 17:21
 */
public class Hero {
    private Weapon weapon;

    public Hero(Weapon weapon) {
        this.weapon = weapon;
    }

    public void attack() {
        weapon.attack();
    }
}
