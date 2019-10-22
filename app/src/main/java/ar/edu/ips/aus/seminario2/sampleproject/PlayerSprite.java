package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

class PlayerSprite {

    private static final String TAG = PlayerSprite.class.getSimpleName();
    private static final int SPRITE_HEIGHT = 72;
    private static final int SPRITE_WIDTH = 52;

    private final Bitmap sprites;

    public PlayerSprite(Resources resources){
        sprites = BitmapFactory.decodeResource(resources, R.drawable.characters);
        Log.d(TAG, String.format("metadata -bytes: %d - size: %d x %d",
                sprites.getByteCount(), sprites.getWidth(), sprites.getHeight()));
    }

    public Bitmap getSprites() {
        return sprites;
    }

    public Rect getSourceRectangle(GameView view, MazeBoard board, Player player, int order){
        float tileWidth = view.getWidth()/board.getHorizontalTileCount();
        float tileHeight = view.getHeight()/board.getVerticalTileCount();

        float x = (float) (player.getX() * tileWidth) - (SPRITE_WIDTH/2);
        float y = (float) (player.getY() * tileHeight) - (SPRITE_HEIGHT/ 2);

        int srcTop = 0;
        int srcLeft = 0;
        int srcRight = 0;
        int srcBottom = 0;
        int[] spriteOffset = { SPRITE_WIDTH *6, SPRITE_WIDTH*7, SPRITE_WIDTH*8, SPRITE_WIDTH*7, SPRITE_WIDTH*6};
        switch (player.getDirection()){
            case WEST:
                srcTop = SPRITE_HEIGHT;
                srcLeft = spriteOffset[(int) (x % spriteOffset.length)];
                break;
            case NORTH:
                srcTop = SPRITE_HEIGHT * 3;
                srcLeft = spriteOffset[(int) (y % spriteOffset.length)];
                break;
            case EAST:
                srcTop = SPRITE_HEIGHT * 2;
                srcLeft = spriteOffset[(int) (x % spriteOffset.length)];
                break;
            case SOUTH:
                srcTop = 0;
                srcLeft = spriteOffset[(int) (y % spriteOffset.length)];
                break;
            case NONE:
                // FIXME fill in
                break;

        }
        srcBottom = srcTop + SPRITE_HEIGHT;
        srcRight = srcLeft + SPRITE_WIDTH;
        return new Rect(srcLeft, srcTop, srcRight, srcBottom);
    }

    public Rect getDestinationRectangle(GameView view, MazeBoard board, Player player) {
        float tileWidth = view.getWidth()/board.getHorizontalTileCount();
        float tileHeight = view.getHeight()/board.getVerticalTileCount();

        float x = (float) (player.getX() * tileWidth) - (SPRITE_WIDTH/2);
        float y = (float) (player.getY() * tileHeight) - (SPRITE_HEIGHT/ 2);

        return new Rect((int)x,(int)y,(int)x+SPRITE_WIDTH,(int)y+SPRITE_HEIGHT);
    }
}
