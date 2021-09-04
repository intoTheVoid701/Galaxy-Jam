package dev.intoTheVoid.game.entities;

import dev.intoTheVoid.game.Game;
import dev.intoTheVoid.game.entities.projectiles.FriendlyProjectile;
import dev.intoTheVoid.game.gfx.Animation;
import dev.intoTheVoid.game.gfx.Assets;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity
{
    private float xMove;
    private BufferedImage[][] anims;
    private Animation[] animations;
    private Assets assets;
    private boolean attacking = false, atkAnim = false;
    private long lastAttackTimer, attackCooldown = 400, attackTimer = attackCooldown;

    public Player(Game game)
    {
        super(game.getWidth() / 2.0f, game.getHeight() - 100, defaultSize, defaultSize, game);
        assets = game.getAssets();
        title = "player";
        xMove = 0;
        anims = new BufferedImage[][]
                {
                        {assets.getSprite("player00"), assets.getSprite("player01")}, // idle animation
                        {assets.getSprite("player02"), assets.getSprite("player02")}, // firing animation
                        {assets.getSprite("player10"), assets.getSprite("player11"),
                                assets.getSprite("player12"), assets.getSprite("player13"),
                                assets.getSprite("player14"), assets.getSprite("player15")}  // death animation
                };
        animations = new Animation[]
                {
                        new Animation(100, anims[0]),
                        new Animation(200, anims[1]),
                        new Animation(150, anims[2])
                };

        bounds = new Rectangle(16, 6, 32, 66);
    }

    @Override
    public void update()
    {
        x += xMove;
        if (x > game.getWidth())
            x = 0;
        else if (x < -64)
            x = game.getWidth() - 64;
        getInput();
        fire();
        for (Animation a : animations)
        {
            a.update();
        }
    }

    private void getInput()
    {
        if (game.getInput().keyDown(KeyEvent.VK_D) || game.getInput().keyDown(KeyEvent.VK_RIGHT))
        {
            xMove = 5;
        }
        else if (game.getInput().keyDown(KeyEvent.VK_A) || game.getInput().keyDown(KeyEvent.VK_LEFT))
        {
            xMove = -5;
        }
        else
        {
            xMove = 0;
        }
    }

    private void fire()
    {
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();

        if (attackTimer < 150)
        {
            atkAnim = true;
        }
        else
        {
            atkAnim = false;
        }
        if (attackTimer < attackCooldown)
        {
            attacking = true;
            return;
        }
        else
        {
            attacking = false;
        }

        if (game.getInput().keyJustDown(KeyEvent.VK_SPACE) || game.getInput().keyJustDown(KeyEvent.VK_Z))
        {
            new FriendlyProjectile(x + width - 25, y - 25, 12, 44, game); // right side
            new FriendlyProjectile(x + 15, y - 25, 12, 44, game); // left side
        }
        else
        {
            return;
        }

        attackTimer = 0;
    }

    @Override
    public void render(Graphics g)
    {
        g.drawImage(getCurrentAnimationFrame(), (int)x, (int)y, (int)width, (int)height, null);
    }

    @Override
    public void die()
    {

    }

    private BufferedImage getCurrentAnimationFrame()
    {
        if (atkAnim)
            return animations[1].getCurrentFrame();
        else
            return animations[0].getCurrentFrame();
    }
}
