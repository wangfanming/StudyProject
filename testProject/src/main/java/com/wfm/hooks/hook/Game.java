package com.wfm.hooks.hook;

import com.wfm.hooks.base.Hero;
import com.wfm.hooks.base.Weapon;
import sun.reflect.misc.ReflectUtil;

import java.lang.reflect.Field;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName Game
 * @Descripyion TODO
 * @date 2020/4/4 23:37
 */
public class Game {
    public static void main(String[] args) {
        Hero hero = new Hero(new Weapon());
        try {
            Field weapon = hero.getClass().getDeclaredField("weapon");
            weapon.setAccessible(true);
            Weapon weaponHook = new WeaponHook();

            ((WeaponHook) weaponHook).setOnUseWeaponAttackListener(damage -> {
                System.out.println("damage = " + damage);
                return damage;
            });

            weapon.set(hero,weaponHook);
            hero.attack();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
