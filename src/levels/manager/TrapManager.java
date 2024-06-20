package levels.manager;

import entities.trap.*;
import levels.Map;
import main.Game;

import java.awt.*;
import java.util.ArrayList;

import static entities.trap.TrapID.*;

public class TrapManager {
    private final Game game;
    private final Map map;
    private final ArrayList<Bat> bats;
    private final ArrayList<Bug> bugs;
    private final ArrayList<Spike> spikes;
    private final ArrayList<RockHead> rockHeads;
    private final ArrayList<BrickRandom> bricks;
    private final ArrayList<Turtle> turtles;
    private final ArrayList<BrokenBrick> brokenBricks;
    private final ArrayList<Gate> gates;
    private final ArrayList<Totem> totems;
    private Boss boss;

    public TrapManager(Game game, Map map){
        this.game = game;
        this.map = map;
        bats = new ArrayList<>();
        bugs = new ArrayList<>();
        spikes = new ArrayList<>();
        rockHeads = new ArrayList<>();
        bricks = new ArrayList<>();
        turtles = new ArrayList<>();
        brokenBricks = new ArrayList<>();
        gates = new ArrayList<>();
        totems = new ArrayList<>();
        initTrap();
    }

    public void update(){
        ArrayList<Bat> batRemove = new ArrayList<>();
        for(Bat bat : bats){
            if(bat.isVisible()){
                bat.update(game.getPlayer().getWeapons(), game.getPlayer());
            }else batRemove.add(bat);
        }
        bats.removeAll(batRemove);

        ArrayList<Bug> bugRemove = new ArrayList<>();
        for(Bug bug : bugs){
            if(bug.isVisible()){
                bug.update(game.getPlayer().getWeapons(), game.getPlayer());
            }else bugRemove.add(bug);
        }
        bugs.removeAll(bugRemove);

        for (Spike spike : spikes) {
            spike.update(game.getPlayer());
        }

        ArrayList<RockHead> rockHeadRemove = new ArrayList<>();
        for(RockHead rockHead : rockHeads){
            if(rockHead.isVisible()){
                rockHead.update(game.getPlayer());
            }else rockHeadRemove.add(rockHead);
        }
        rockHeads.removeAll(rockHeadRemove);

        for(BrickRandom brick : bricks){
            brick.update(game.getMapManager().getCurrentMap().getItemManager(), game.getPlayer());
        }

        ArrayList<Turtle> turtleRemove = new ArrayList<>();
        for(Turtle turtle : turtles){
            if(turtle.isVisible()){
                turtle.update(game.getPlayer());
            }else turtleRemove.add(turtle);
        }
        turtles.removeAll(turtleRemove);

        if(boss != null && boss.isVisible()) {
            boss.update(game.getPlayer());
        }else boss = null;

        ArrayList<BrokenBrick> brokenBrickRemove = new ArrayList<>();
        for(BrokenBrick brick : brokenBricks){
            if(brick.isVisible()){
                brick.update(game.getMapManager(), game.getPlayer());
            }else brokenBrickRemove.add(brick);
        }
        brokenBricks.removeAll(brokenBrickRemove);

        ArrayList<Gate> gateRemove = new ArrayList<>();
        for(Gate gate : gates){
            if(gate.isVisible()){
                if(gate.update(game.getPlayer())){
                    game.nextHiddenMap();
                }
            }else gateRemove.add(gate);
        }
        gates.removeAll(gateRemove);

        ArrayList<Totem> totemRemove = new ArrayList<>();
        for(Totem totem : totems){
            if(totem.isVisible()){
                totem.update(game.getPlayer());
            }else totemRemove.add(totem);
        }
        totems.removeAll(totemRemove);
    }
    public void render(Graphics g){
        for(Bat bat : bats){
            bat.render(g);
        }
        for(Bug bug : bugs){
            bug.render(g);
        }
        for(Spike s : spikes){
            s.render(g);
        }
        for(RockHead rock : rockHeads){
            rock.render(g);
        }
        for(BrickRandom brick : bricks){
            brick.render(g);
        }
        for(Turtle turtle : turtles){
            turtle.render(g);
        }
        if(boss != null){
            boss.render(g);
        }
        for(BrokenBrick brick : brokenBricks){
            brick.render(g);
        }
        for(Gate gate : gates){
            gate.render(g);
        }
        for(Totem totem : totems){
            totem.render(g);
        }
    }

    private void initTrap(){
        int tile;
        for (int j = 0; j < map.getLevel().getLevelData().length; j++)
            for (int i = 0; i < map.getLevel().getLevelData()[0].length; i++) {
                tile = map.getLevel().getGreenLvlData()[j][i];
                if (tile == 1)
                    addTrap(BAT, i* Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 2)
                    addTrap(TrapID.BUG, i* Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 3)
                    addTrap(TrapID.SPIKE, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 4)
                    addTrap(TrapID.ROCKHEAD, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 5)
                    addTrap(TrapID.BRICKRANDOM, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 6)
                    addTrap(TrapID.TURTLE, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 7)
                    addTrap(TrapID.BOSS, i*Game.TILES_SIZE-3*Game.TILES_SIZE, j*Game.TILES_SIZE-3*Game.TILES_SIZE);
                else if (tile == 8)
                    addTrap(TrapID.BROKENBRICK, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 9)
                    addTrap(TrapID.GATE, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 10)
                    addTrap(TrapID.TOTEM, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
            }
    }
    public void addTrap(TrapID id, int x, int y){
        if (id == BAT)
            bats.add(new Bat(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game, game.getMapManager()));
        else if (id == BUG)
            bugs.add(new Bug(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game, game.getMapManager()));
        else if (id == SPIKE)
            spikes.add(new Spike(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game, game.getMapManager()));
        else if (id == ROCKHEAD)
            rockHeads.add(new RockHead(x, y, (int)(42*Game.SCALE), (int)(42*Game.SCALE), game, map.getLevel().getGreenLvlData()));
        else if (id == BRICKRANDOM)
            bricks.add(new BrickRandom(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game, game.getMapManager()));
        else if (id == TURTLE)
            turtles.add(new Turtle(x, y, (int)(44*Game.SCALE), (int)(26*Game.SCALE), game, game.getMapManager()));
        else if (id == BOSS)
            boss = new Boss(x, y, (int)(32*4*Game.SCALE), (int)(32*4*Game.SCALE), game, game.getMapManager());
        else if (id == BROKENBRICK)
            brokenBricks.add(new BrokenBrick(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game));
        else if(id == GATE)
            gates.add(new Gate(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game));
        else if(id == TOTEM)
            totems.add(new Totem(x, y, Game.TILES_SIZE, Game.TILES_SIZE, game));
    }
    public Boss getBoss(){return boss;}
}
