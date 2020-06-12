package com.wfm.hooks.safeHook;

import com.wfm.hooks.base.Weapon;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName Hero
 * @Descripyion TODO
 * @date 2020/4/5 0:19
 */
public class Hero {
    private Weapon weapon;
    private final int weaponId;

    public Hero(Weapon weapon) {
        this.weapon = weapon;
        this.weaponId = this.weapon.hashCode(); //记录原始Weapon对象的Id
    }

    public void attack() throws IllegalAccessException {
        if (this.weapon.hashCode() != weaponId){
            throw new IllegalAccessException(String.format("非法访问！访问者ID= %d",this.weapon.hashCode()));
        }
        weapon.attack();
    }
}
