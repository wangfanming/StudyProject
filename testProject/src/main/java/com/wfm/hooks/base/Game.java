package com.wfm.hooks.base;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName Game
 * @Descripyion TODO
 * @date 2020/4/4 17:24
 */
public class Game {
    public static void main(String[] args) {
        Hero hero = new Hero(new Weapon());
        hero.attack();
    }
}
