package com.rm.fxmap.basemap.tiles;

import com.rm.fxmap.basemap.BaseMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author rmarquez
 */
public class FileTileCache implements TileCache {

  private final File baseDir;

  /**
   *
   * @param baseDir The base directory of where to store tiles.
   * @param source The source name which is based on the base tile map type. This will be
   * used as the subfolder name.
   */
  public FileTileCache(File baseDir) {
    this.baseDir = baseDir;
    Logger.getLogger(FileTileCache.class.getName())
      .log(Level.INFO, "Map tiles wil be saved at : ''{0}''", this.baseDir);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public boolean containsKey(BaseMap baseMap, TileIndices key) {
    File file = this.getFile(baseMap, key);
    boolean result = file.exists();
    return result;
  }

  private File getFile(BaseMap baseMap, TileIndices key) {
    String keyToFile = key.level + File.separator + key.col + File.separator + key.row + ".png";
    String subDir = baseMap.getSubDirName();
    File result = new File(this.baseDir + File.separator + subDir, keyToFile);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Tile get(BaseMap baseMap, TileIndices key) {
    Tile r = new Tile(baseMap) {
      @Override
      protected Image onLoadImage() {
        File file = getFile(baseMap, key);
        InputStream is;
        try {
          is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
          throw new RuntimeException(ex);
        }
        Image result = new Image(is);
        return result;
      }
    };
    r.loadImage();
    return r;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void put(BaseMap baseMap, TileIndices key, Tile tile) {
    if (tile.getBaseMap() != baseMap) {
      File file = this.getFile(baseMap, key);
      Image image = tile.getImage().getValue();
      if (image != null) {
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdirs();
        }
        try {
          BufferedImage x = SwingFXUtils.fromFXImage(image, null);
          ImageIO.write(x, "png", file);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    }
  }

  @Override
  public String toString() {
    return "FileTileCache{" + "baseDir=" + baseDir + '}';
  }

}
